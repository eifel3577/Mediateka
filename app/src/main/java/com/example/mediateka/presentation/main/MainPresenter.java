package com.example.mediateka.presentation.main;



import com.example.mediateka.presentation.base.BasePresenter;
import com.example.mediateka.presentation.cinemalist.CinemaTabPositionPicker;
import com.example.mediateka.presentation.common.CinemaTabSelectorView;

import javax.inject.Inject;

/**презентер,работающий с MainActivity */
public class MainPresenter extends BasePresenter<MainPresenter.View> implements SyncConnectionListener {

    private final CinemaTabPositionPicker cinemaTabPositionPicker = new CinemaTabPositionPicker();

    /**предоставляется как зависимость */
    @Inject
    public MainPresenter(){}

    /**просит MainActivity включить слушатель интернет соединения */
    @Override
    public void initialize() {
        getView().startToListenInternetConnection();
    }

    /**обнуляет ссылку на MainActivity */
    @Override
    public void onDestroy() {
        setView(null);
    }

    /**
     * @param internetConnected есть ли соединение с интернетом
     * если соединения нет,просит MainActivity показать сообщение об ошибке сети*/
    @Override
    public void onNetworkConnectionChanged(boolean internetConnected) {
        if (!internetConnected){
            getView().showNetworkError();
        }
    }

    /**нажатие накнопке Повторить.
     * @param internetConnected есть ли соединение с интернетом
     * если соединения нет,просит MainActivity показать сообщение об ошибке сети
     * если соединение есть,просит MainActivity скрыть сообщение об ошибке сети
     *  */
    void onRetryButtonClicked(boolean internetConnected) {
        if (!internetConnected){
            getView().showNetworkError();
        } else {
            getView().hideNetworkError();
        }
    }

    /**просит MainActivity пролистнуть экран до самой первой позиции */
    public void onFABScrollUpClicked() {
        getView().scrollToFirstPosition();
    }

    /**обработка нажатия на конкретном табе
     * @param position позиция нажатого таба
     * */
    public void onTabSelected(int position) {
        cinemaTabPositionPicker.loadCinemaFromCinemaPosition(position , getView());
    }

    /**интерфейс,реализуемый MainActivity  */
    public interface View extends CinemaTabSelectorView {
        void startToListenInternetConnection();
        void showNetworkError();
        void hideNetworkError();
        void scrollToFirstPosition();
    }
}
