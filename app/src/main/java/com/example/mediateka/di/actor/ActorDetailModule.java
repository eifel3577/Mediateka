package com.example.mediateka.di.actor;



import com.example.mediateka.di.ActivityScope;
import com.example.mediateka.domain.ActorRepository;
import com.example.mediateka.domain.actorusecases.GetActorById;
import com.example.mediateka.presentation.actordetail.ActorDetailContentPresenter;
import com.example.mediateka.presentation.actordetail.ActorDetailPresenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

@Module
public class ActorDetailModule {

    @ActivityScope
    @Provides
    ActorDetailPresenter provideActorDetailPresenter(GetActorById getActorById){
        return new ActorDetailPresenter(getActorById);
    }

    @ActivityScope
    @Provides
    ActorDetailContentPresenter provideActorDetailContentPresenter(){
        return new ActorDetailContentPresenter();
    }

    @ActivityScope
    @Provides
    GetActorById provideGetActorById(@Named("executor_thread")Scheduler executorThread ,
                                     @Named("ui_thread")Scheduler uiThread ,
                                     ActorRepository repository){
        return new GetActorById(executorThread , uiThread , repository);
    }
}
