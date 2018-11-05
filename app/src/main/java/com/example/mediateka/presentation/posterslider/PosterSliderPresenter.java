package com.example.mediateka.presentation.posterslider;



import com.example.mediateka.presentation.base.BasePresenter;
import com.example.mediateka.presentation.base.BaseView;

import java.util.Locale;

import javax.inject.Inject;

public class PosterSliderPresenter extends BasePresenter<PosterSliderPresenter.View> {

    @Inject public PosterSliderPresenter() {}

    @Override
    public void initialize() {
        getView().showLoading();
    }

    @Override
    public void onDestroy() {
        setView(null);
    }

    void countPosters(int currentItem, int count) {
        currentItem += 1;
        getView().showCurrentPosition(String.format(Locale.getDefault() , "%d/%d" , currentItem , count));
    }

    interface View extends BaseView {
        void showCurrentPosition(String position);
    }
}
