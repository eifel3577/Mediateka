package com.example.mediateka.presentation.common;


import com.example.mediateka.presentation.base.BaseView;

/**интерфейс какой таб нажат */
public interface CinemaTabSelectorView extends BaseView {
    void onPopularTabSelected();
    void onTopRatedTabSelected();
    void onUpComingTabSelected();
}
