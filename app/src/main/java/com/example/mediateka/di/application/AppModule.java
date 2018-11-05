package com.example.mediateka.di.application;

import android.content.Context;
import android.content.SharedPreferences;


import com.example.mediateka.MediatekaApp;
import com.example.mediateka.data.SharedPreferenceManager;
import com.example.mediateka.data.datasource.db.CinemaActorJoinDao;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public class AppModule {

    private final Context context;
    private final String APP_PREFS = "Application_preferences";

    public AppModule(Context context){
        this.context = context.getApplicationContext();
    }

    @Singleton
    @Provides
    Context provideContext(){
        return context;
    }

    @Singleton
    @Provides
    SharedPreferences provideSharedPreference(){
        return context.getSharedPreferences(APP_PREFS , Context.MODE_PRIVATE);
    }

    @Singleton
    @Provides
    SharedPreferenceManager provideSharedPrefernceManager(SharedPreferences sharedPreferences){
        return new SharedPreferenceManager(sharedPreferences);
    }

    @Singleton
    @Provides
    CinemaActorJoinDao provideCinemaActorJoinDao(){
        MediatekaApp mediatekaApp = (MediatekaApp) context;
        return mediatekaApp.getDatabaseInstance()
                .getCinemaActorJoinDao();
    }

    @Named("executor_thread")
    @Provides
    Scheduler provideExecutorThread(){
        return Schedulers.io();
    }

    @Named("ui_thread")
    @Provides
    Scheduler provideUiThread(){
        return AndroidSchedulers.mainThread();
    }

}
