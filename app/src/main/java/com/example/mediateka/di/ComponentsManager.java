package com.example.mediateka.di;

import android.content.Context;

import com.example.mediateka.di.actor.ActorComponent;
import com.example.mediateka.di.actor.ActorModule;
import com.example.mediateka.di.application.AppComponent;
import com.example.mediateka.di.application.AppModule;
import com.example.mediateka.di.cinema.CinemaComponent;
import com.example.mediateka.di.cinema.CinemaModule;


public class ComponentsManager {

    private AppComponent appComponent;
    private CinemaComponent cinemaComponent;
    private ActorComponent actorComponent;

    private final Context context;

    public ComponentsManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public AppComponent getAppComponent(){
        if (appComponent == null){
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(context))
                    .build();
        }
        return appComponent;
    }

    public CinemaComponent plusCinemaComponent(){
        if (cinemaComponent == null){
            cinemaComponent = appComponent.plusCinemaComponent(new CinemaModule());
        }
        return cinemaComponent;
    }

    public ActorComponent plusActorComponent(){
        if (actorComponent == null){
            actorComponent = appComponent.plusActorComponent(new ActorModule());
        }
        return actorComponent;
    }

    public void clearActorComponent(){
        actorComponent = null;
    }

    public void clearCinemaComponent(){
        cinemaComponent = null;
    }


}
