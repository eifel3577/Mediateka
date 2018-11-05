package com.example.mediateka.di.application;


import com.example.mediateka.di.actor.ActorComponent;
import com.example.mediateka.di.actor.ActorModule;
import com.example.mediateka.di.cinema.CinemaComponent;
import com.example.mediateka.di.cinema.CinemaModule;
import com.example.mediateka.presentation.main.MainActivity;
import com.example.mediateka.presentation.posterslider.PosterSliderActivity;
import com.example.mediateka.presentation.search.SearchActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RetrofitModule.class , AppModule.class})
public interface AppComponent {

    void inject(MainActivity mainActivity);
    void inject(SearchActivity searchActivity);
    void inject(PosterSliderActivity posterSliderActivity);

    CinemaComponent plusCinemaComponent(CinemaModule cinemaModule);
    ActorComponent plusActorComponent(ActorModule actorModule);
}
