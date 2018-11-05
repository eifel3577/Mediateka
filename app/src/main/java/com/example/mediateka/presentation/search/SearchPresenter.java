package com.example.mediateka.presentation.search;



import com.example.mediateka.presentation.base.BasePresenter;
import com.example.mediateka.presentation.base.BaseView;

import javax.inject.Inject;


public class SearchPresenter extends BasePresenter<SearchPresenter.View> {

    private String query = "";

    private static int currentTabPosition = 0;

    private static final int CINEMAS_TAB_POSITION = 0;
    private static final int ACTORS_TAB_POSITION = 1;

    @Inject public SearchPresenter() {
    }

    @Override
    public void initialize() {
        getView().showLoading();
    }

    @Override
    public void onDestroy() {
    }

    public void onTabSelected(int position) {
        switch (position) {
            case CINEMAS_TAB_POSITION : {
                getView().onCinemaTabSelected();
                currentTabPosition = CINEMAS_TAB_POSITION;
                break;
            }
            case ACTORS_TAB_POSITION : {
                getView().onActorTabSelected();
                currentTabPosition = ACTORS_TAB_POSITION;
                break;
            }
        }
    }

    public void onTextChanged(String query) {
        this.query = query;
        checkClearEditTextBtnState();
        switch (currentTabPosition) {
            case CINEMAS_TAB_POSITION : {
                getView().textFromCinemaTab(query);
                break;
            }
            case ACTORS_TAB_POSITION : {
                getView().textFromActorTab(query);
                break;
            }
        }
    }

    public void onClearEditTextClicked() {
        getView().clearEditText("");
        checkClearEditTextBtnState();
    }

    private void checkClearEditTextBtnState(){
        if (query.isEmpty()){
            getView().hideClearEditTextBtn();
        } else {
            getView().showEditTextBtn();
        }
    }

    public interface View extends BaseView {
        void onCinemaTabSelected();
        void onActorTabSelected();
        void textFromCinemaTab(String query);
        void textFromActorTab(String query);
        void hideClearEditTextBtn();
        void showEditTextBtn();
        void clearEditText(String s);
    }
}
