package com.example.mediateka.di.actor;



import com.example.mediateka.di.ActivityScope;
import com.example.mediateka.domain.ActorRepository;
import com.example.mediateka.domain.actorusecases.GetActorsByQuery;
import com.example.mediateka.presentation.actorlist.ActorsPresenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

@Module
public class ActorListModule {

    @ActivityScope
    @Provides
    ActorsPresenter provideActorsPresenter(GetActorsByQuery getActorsByQuery){
        return new ActorsPresenter(getActorsByQuery);
    }

    @ActivityScope
    @Provides
    GetActorsByQuery provideGetActorByQuery(@Named("executor_thread") Scheduler executorThread ,
                                            @Named("ui_thread") Scheduler uiThread ,
                                            ActorRepository repository){
        return new GetActorsByQuery(executorThread , uiThread , repository);
    }
}
