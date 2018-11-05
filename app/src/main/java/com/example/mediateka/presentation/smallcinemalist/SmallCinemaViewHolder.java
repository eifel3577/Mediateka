package com.example.mediateka.presentation.smallcinemalist;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.mediateka.models.model.Cinema;
import com.example.mediateka.presentation.common.HolderRenderer;
import com.example.mediateka.presentation.common.OnCinemaClickListener;
import com.example.mediateka.utils.FormatterUtils;
import com.example.mediateka.utils.UrlImagePathCreator;
import com.squareup.picasso.Picasso;

public class SmallCinemaViewHolder extends RecyclerView.ViewHolder implements HolderRenderer<Cinema> {

    public ConstraintLayout mViewForeground;
    private RelativeLayout mViewBackground;
    private ImageView mImageViewCinemaPoster;
    private TextView mTextViewCinemaDate , mTextViewTitle , mTextViewGenres , mTextViewCharacter;
    private final OnCinemaClickListener onCinemaClickListener;

    SmallCinemaViewHolder(View itemView , OnCinemaClickListener onCinemaClickListener) {
        super(itemView);
        this.onCinemaClickListener = onCinemaClickListener;
        mImageViewCinemaPoster = itemView.findViewById(R.id.iv_cinema_poster);
        mTextViewCinemaDate = itemView.findViewById(R.id.tv_actor_detail_cinema_date);
        mTextViewTitle = itemView.findViewById(R.id.tv_actor_detail_cinema_title);
        mTextViewGenres = itemView.findViewById(R.id.tv_actor_detail_cinema_genres);
        mTextViewCharacter = itemView.findViewById(R.id.tv_actor_detail_character);
        mViewForeground = itemView.findViewById(R.id.view_small_cinema_foreground);
        mViewBackground = itemView.findViewById(R.id.view_small_cinema_background);
        mViewBackground.setVisibility(View.GONE);
    }

    SmallCinemaViewHolder(View itemView ,
                          OnCinemaClickListener onCinemaClickListener ,
                          @ColorRes int foregroundColor ,
                          boolean withOutBackground){
        this(itemView , onCinemaClickListener);
        if (withOutBackground){
            mViewBackground.setVisibility(View.GONE);
        } else {
            mViewBackground.setVisibility(View.VISIBLE);
        }
        mViewForeground.setBackgroundResource(foregroundColor);
    }

    @Override
    public void render(Cinema cinema , int viewHolderPosition) {
        onItemClicked(cinema.getId() , viewHolderPosition);
        renderImage(UrlImagePathCreator.INSTANCE.createPictureUrlFromQuality(UrlImagePathCreator.Quality.Quality185 , cinema.getPosterUrl()));
        mTextViewCinemaDate.setText(FormatterUtils.getYearFromDate(cinema.getReleaseDate()));
        mTextViewTitle.setText(cinema.getTitle());
        mTextViewGenres.setText(FormatterUtils.formatGenres(cinema.getGenres() , getContext()));
        mTextViewCharacter.setText(TextUtils.isEmpty(cinema.getCharacter()) ? "" : getContext().getString(R.string.role , cinema.getCharacter()));
    }

    private void renderImage(String url){
        Picasso.with(getContext())
                .load(url)
                .placeholder(R.color.colorDarkBackground)
                .error(R.drawable.ic_cinema)
                .into(mImageViewCinemaPoster);
    }

    private void onItemClicked(int cinemaId , int viewHolderPosition){
        mViewForeground.setOnClickListener(v -> onCinemaClickListener.onCinemaClicked(cinemaId , viewHolderPosition));
    }

    private Context getContext(){
        return itemView.getContext();
    }
}
