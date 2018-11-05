package com.example.mediateka.domain.cinemausecases;



import com.example.mediateka.domain.CinemaRepository;
import com.example.mediateka.domain.UseCase;
import com.example.mediateka.models.model.Cinema;

import org.reactivestreams.Publisher;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class GetCinemas extends UseCase<List<Cinema>> {

    private final CinemaRepository repository;

    public GetCinemas(Scheduler executorThread ,
                      Scheduler uiThread ,
                      CinemaRepository repository){
        super(executorThread , uiThread);
        this.repository = repository;
    }

    @Override
    public Flowable<List<Cinema>> createUseCase() {
        return repository.getCinemas(pageIndex)
                .toFlowable()
                .flatMap(Flowable::fromIterable)
                .filter(cinema -> !cinema.getDescription().isEmpty())
                .toList()
                .toFlowable();
    }
}
