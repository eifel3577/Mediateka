package com.example.mediateka.di.actor;

import android.content.Context;


import com.example.mediateka.MediatekaApp;
import com.example.mediateka.data.datasource.db.ActorDao;
import com.example.mediateka.data.datasource.db.CinemaActorJoinDao;
import com.example.mediateka.data.datasource.network.CinemaApiService;
import com.example.mediateka.data.repository.actor.ActorLocalRepository;
import com.example.mediateka.data.repository.actor.ActorRemoteRepository;
import com.example.mediateka.domain.ActorRepository;
import com.example.mediateka.models.mapper.ActorDetailEntityToActor;
import com.example.mediateka.models.mapper.ActorDetailResponseToActor;

import dagger.Module;
import dagger.Provides;

@Module
public class ActorModule {

    @ActorScope
    @Provides
    ActorRepository provideRepository(CinemaApiService apiService ,
                                      ActorDetailResponseToActor networkMapper ,
                                      ActorDetailEntityToActor dbMapper ,
                                      ActorLocalRepository cache ,
                                      CinemaActorJoinDao cinemaActorJoinDao){
        return new ActorRemoteRepository(apiService , networkMapper , dbMapper , cache , cinemaActorJoinDao);
    }

    @ActorScope
    @Provides
    ActorLocalRepository provideActorLocalRepository(ActorDao actorDao ,
                                                ActorDetailEntityToActor mapper ,
                                                CinemaActorJoinDao cinemaActorJoinDao){
        return new ActorLocalRepository(actorDao , mapper , cinemaActorJoinDao);
    }

    @ActorScope
    @Provides
    ActorDao providesActorDao(Context context){
        MediatekaApp mediatekaApp = (MediatekaApp) context;
        return mediatekaApp.getDatabaseInstance()
                .getActorDao();
    }

    @ActorScope
    @Provides
    ActorDetailEntityToActor provideActorDetailEntityToActor(){
        return new ActorDetailEntityToActor();
    }

    @ActorScope
    @Provides
    ActorDetailResponseToActor provideActorDetailResponseToActor(){
        return new ActorDetailResponseToActor();
    }

}
