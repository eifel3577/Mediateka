package com.example.mediateka.domain.actorusecases;



import com.example.mediateka.domain.ActorRepository;
import com.example.mediateka.domain.UseCase;
import com.example.mediateka.models.model.Actor;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;

public class GetActors extends UseCase<List<Actor>> {

    private final ActorRepository repository;

    @Inject public GetActors(@Named("executor_thread") Scheduler executorThread ,
                             @Named("ui_thread") Scheduler uiThread ,
                             ActorRepository repository) {
        super(executorThread, uiThread);
        this.repository = repository;
    }

    @Override
    protected Flowable<List<Actor>> createUseCase() {
        return repository.getPopularActors(1)
                .toFlowable();
    }
}
