package com.example.mediateka.presentation.actordetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.mediateka.MediatekaApp;
import com.example.mediateka.di.actor.ActorDetailModule;
import com.example.mediateka.models.model.Actor;
import com.example.mediateka.presentation.base.BaseActivity;
import com.example.mediateka.presentation.common.ViewPagerAdapter;
import com.example.mediateka.presentation.posterslider.PosterSliderActivity;
import com.example.mediateka.presentation.posterslider.PosterSliderAdapter;
import com.example.mediateka.presentation.smallcinemalist.SmallCinemasFragment;
import com.example.mediateka.presentation.widget.IndicatorView;
import com.example.mediateka.utils.AnimUtils;
import com.example.mediateka.utils.UrlImagePathCreator;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActorDetailActivity extends BaseActivity implements ActorDetailPresenter.View {

    private static final String ACTOR_ID = "actor_id";

    private CircleImageView mActorAvatar;
    private TextView mTextViewActorName;
    private ViewPager mViewPagerActorInfo;
    private ViewPager mViewPagerActorBackground;
    private IndicatorView mIndicatorView;
    private TabLayout mTabLayout;
    private ProgressBar mProgressBarActor;
    private AppBarLayout mAppBar;
    private PosterSliderAdapter mBackgroundPosterSliderAdapter;

    @Inject ActorDetailPresenter presenter;

    public static Intent makeIntent(Context context , int actorID){
        Intent intent = new Intent(context , ActorDetailActivity.class);
        intent.putExtra(ACTOR_ID , actorID);
        return intent;
    }

    @Override
    protected void onStop() {
        mAppBar.addOnOffsetChangedListener(null);
        super.onStop();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_actor_detail;
    }

    @Override
    public void showActorDetail(Actor actor) {
        AnimUtils.startRevealAnimation(mViewPagerActorBackground);
        renderImage(UrlImagePathCreator.INSTANCE
                .createPictureUrlFromQuality(UrlImagePathCreator.Quality.Quality185 , actor.getProfileUrl()) , mActorAvatar);
        mBackgroundPosterSliderAdapter = new PosterSliderAdapter(getSupportFragmentManager() , actor.getBackgroundUrls() ,false);
        mViewPagerActorBackground.setAdapter(mBackgroundPosterSliderAdapter);
        mIndicatorView.setUpWithViewPager(mViewPagerActorBackground);
        mTextViewActorName.setText(actor.getName());
        mActorAvatar.setOnClickListener(v -> presenter.onAvatarClicked(actor.getPostersUrl()));
        addOffsetChangeListener(mAppBar , actor.getName());
        setUpViewPager(mViewPagerActorInfo, mTabLayout , actor);
    }

    @Override
    public void showPosters(List<String> posterUrls){
        ActivityOptionsCompat activityOptions =  ActivityOptionsCompat.makeSceneTransitionAnimation(
                this ,
                mActorAvatar ,
                getString(R.string.transition_actor_avatar));
        Intent intent = PosterSliderActivity.makeIntent(this , posterUrls , getString(R.string.transition_actor_avatar));
        ActivityCompat.startActivity(this , intent , activityOptions.toBundle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.navigation_home : {
                navigateToMainActivity(this);
                break;
            }
            case R.id.navigation_show_on_tmdb : {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(actorInfoUrl()));
                startActivity(intent);
                break;
            }
            case R.id.navigation_share : {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT , actorInfoUrl());
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initToolbar(){
        super.initToolbar();
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void initViews(){
        mActorAvatar = findViewById(R.id.actor_detail_avatar);
        mViewPagerActorInfo = findViewById(R.id.view_pager);
        mViewPagerActorBackground = findViewById(R.id.vp_actor_detail_background);
        mIndicatorView = findViewById(R.id.actor_indicator_view);
        mTabLayout = findViewById(R.id.tab_layout);
        mTextViewActorName = findViewById(R.id.actor_detail_name);
        mProgressBarActor = findViewById(R.id.pb_actor);
        mAppBar = findViewById(R.id.app_bar_actor);
        mActorAvatar.setImageResource(R.color.colorPosterBackground);
    }

    @Override
    protected void initDagger(){
        MediatekaApp.getComponentsManager()
                .plusActorComponent()
                .plusActorDetailComponent(new ActorDetailModule())
                .inject(this);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void initPresenter(){
        presenter.setView(this);
        presenter.setActorId(getIntent().getExtras().getInt(ACTOR_ID));
        presenter.initialize();
    }

    @Override
    public void showLoading() {
        mProgressBarActor.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBarActor.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        MediatekaApp.getComponentsManager().clearActorComponent();
        super.onDestroy();
    }

    private void setUpViewPager(ViewPager viewPager , TabLayout tabLayout , Actor actor){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ActorDetailContentFragment.newInstance(actor) , getString(R.string.message_info));
        adapter.addFragment(SmallCinemasFragment.newInstance(actor.getCinemas()) , getString(R.string.message_cinemas));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private String actorInfoUrl(){
        return String.format(Locale.getDefault() ,
                "https://www.themoviedb.org/person/%d" ,
                Objects.requireNonNull(getIntent().getExtras()).getInt(ACTOR_ID));
    }

    private void renderImage(final String url, final ImageView image){
        Picasso.with(ActorDetailActivity.this)
                .load(url)
                .placeholder(R.color.colorPosterBackground)
                .error(R.drawable.ic_actor_default_avatar)
                .into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                        Palette.from(bitmap)
                                .generate(palette -> {
                                    Palette.Swatch darkMutedSwatch = palette.getDarkMutedSwatch();
                                    Palette.Swatch mutedSwatch = palette.getMutedSwatch();
                                    if (darkMutedSwatch != null) {
                                        getCollapsingToolbarLayout().setBackgroundColor(darkMutedSwatch.getRgb());
                                        getCollapsingToolbarLayout().setContentScrimColor(darkMutedSwatch.getRgb());
                                    }
                                    if (mutedSwatch != null) {
                                        mTabLayout.setBackgroundColor(mutedSwatch.getRgb());
                                    }
                                });
                    }

                    @Override
                    public void onError() {
                        if (mActorAvatar.getDrawable() == null){
                            mActorAvatar.setImageDrawable(VectorDrawableCompat
                                    .create(getResources() , R.drawable.ic_actor_default_avatar , getTheme()));
                        }
                    }
                });
    }
}
