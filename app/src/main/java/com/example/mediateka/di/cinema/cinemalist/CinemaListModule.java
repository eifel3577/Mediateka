package com.example.mediateka.di.cinema.cinemalist;



import com.example.mediateka.di.ActivityScope;
import com.example.mediateka.domain.CinemaRepository;
import com.example.mediateka.domain.cinemausecases.GetCinemas;
import com.example.mediateka.domain.cinemausecases.GetTopRatedCinemas;
import com.example.mediateka.domain.cinemausecases.GetUpComingCinemas;
import com.example.mediateka.presentation.cinemalist.CinemaListPresenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

@Module
public class CinemaListModule {

    @ActivityScope
    @Provides
    CinemaListPresenter provideCinemaListPresenter(GetCinemas getCinemas ,
                                                   GetTopRatedCinemas getTopRatedCinemas ,
                                                   GetUpComingCinemas getUpComingCinemas){
        return new CinemaListPresenter(getCinemas , getTopRatedCinemas , getUpComingCinemas);
    }

    @ActivityScope
    @Provides
    GetCinemas provideGetCinemas(@Named("executor_thread")Scheduler executorThread ,
                                 @Named("ui_thread")Scheduler uiThread ,
                                 CinemaRepository repository){
        return new GetCinemas(executorThread , uiThread , repository);
    }

    @ActivityScope
    @Provides
    GetTopRatedCinemas provideGetTopRatedCinemas(@Named("executor_thread")Scheduler executorThread ,
                                                 @Named("ui_thread")Scheduler uiThread ,
                                                 CinemaRepository repository){
        return new GetTopRatedCinemas(executorThread , uiThread , repository);
    }

    @ActivityScope
    @Provides
    GetUpComingCinemas provideGetUpComingCinemas(@Named("executor_thread")Scheduler executorThread ,
                                                 @Named("ui_thread")Scheduler uiThread ,
                                                 CinemaRepository repository){
        return new GetUpComingCinemas(executorThread , uiThread , repository);
    }
}
