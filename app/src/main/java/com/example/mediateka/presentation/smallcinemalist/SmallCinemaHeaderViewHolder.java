package com.example.mediateka.presentation.smallcinemalist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;



public class SmallCinemaHeaderViewHolder extends RecyclerView.ViewHolder{

    private TextView mTextViewCinemaCount;

    public SmallCinemaHeaderViewHolder(View itemView) {
        super(itemView);
        mTextViewCinemaCount = itemView.findViewById(R.id.tv_actor_detail_cinemas_count);
    }

    public void render(int cinemaCount){
        mTextViewCinemaCount.setText(getContext()
                .getString(R.string.cinemas_count , cinemaCount));
    }

    private Context getContext(){
        return itemView.getContext();
    }
}
