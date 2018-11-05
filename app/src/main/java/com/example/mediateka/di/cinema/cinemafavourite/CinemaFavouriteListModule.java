package com.example.mediateka.di.cinema.cinemafavourite;



import com.example.mediateka.data.SharedPreferenceManager;
import com.example.mediateka.data.repository.cinema.CinemaLocalRepository;
import com.example.mediateka.di.ActivityScope;
import com.example.mediateka.domain.cinemausecases.GetFavouriteListCinema;
import com.example.mediateka.presentation.favouritelistcinema.FavouriteListCinemaPresenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

@Module
public class CinemaFavouriteListModule {

    @ActivityScope
    @Provides
    FavouriteListCinemaPresenter provideFavouriteListCinemaPresenter(GetFavouriteListCinema useCase ,
                                                                     SharedPreferenceManager sharedPreferenceManager){
        return new FavouriteListCinemaPresenter(useCase , sharedPreferenceManager);
    }

    @ActivityScope
    @Provides
    GetFavouriteListCinema provideGetFavouriteListCinema(@Named("executor_thread")Scheduler executorThread ,
                                                         @Named("ui_thread")Scheduler uiThread ,
                                                         CinemaLocalRepository repository){
        return new GetFavouriteListCinema(executorThread , uiThread , repository);
    }
}
