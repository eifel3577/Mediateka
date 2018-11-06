package com.example.mediateka.presentation.cinemalist;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


import com.example.mediateka.domain.UseCase;
import com.example.mediateka.domain.UseCaseSubscriber;
import com.example.mediateka.models.model.Cinema;
import com.example.mediateka.presentation.common.CinemaTabSelectorView;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**enum ,каждое значение которого переопределяет метод loadCinemaList */
public enum CinemaTab {

    POPULAR {
        @Override
        public <T extends CinemaTabSelectorView> void loadCinemaList(UseCase<List<Cinema>> useCase,
                                                                     UseCaseSubscriber<List<Cinema>> subscriber,
                                                                     @NonNull T view) {
            super.loadCinemaList(useCase , subscriber , view);
            view.onPopularTabSelected();
        }
    },
    TOP_RATED {
        @Override
        public <T extends CinemaTabSelectorView> void loadCinemaList(UseCase<List<Cinema>> useCase,
                                                                     UseCaseSubscriber<List<Cinema>> subscriber,
                                                                     @NonNull T view) {
            super.loadCinemaList(useCase , subscriber , view);
            view.onTopRatedTabSelected();
        }
    },
    UP_COMING {
        @Override
        public <T extends CinemaTabSelectorView> void loadCinemaList(UseCase<List<Cinema>> useCase,
                                                                     UseCaseSubscriber<List<Cinema>> subscriber,
                                                                     @NonNull T view) {
            super.loadCinemaList(useCase , subscriber , view);
            view.onUpComingTabSelected();
        }
    };

    /** CompositeDisposable для хранения подписки*/
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    /**Если подписка не отписана,то отписывается */
    public void dispose(){
        if (!compositeDisposable.isDisposed()){
            compositeDisposable.dispose();
        }
    }

    /**метод,котрый переопределяют все элеменеты enum.Подписывает {@link UseCaseSubscriber<List<Cinema>} на
     * {@link UseCase<List<Cinema>>}
     * @param useCase
     * @param subscriber
     * @param view для взаимодействия с MainActivity, в частности для вызова метода активити для выделения
     * нажатого таба*/
    public <T extends CinemaTabSelectorView> void loadCinemaList(@Nullable UseCase<List<Cinema>> useCase,
                                                                 @Nullable UseCaseSubscriber<List<Cinema>> subscriber,
                                                                 @NonNull T view){
        if (useCase != null && subscriber != null){
            useCase.subscribe(subscriber);
            compositeDisposable.add(subscriber);
        }
    }
}
