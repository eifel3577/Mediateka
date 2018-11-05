package com.example.mediateka.domain.cinemausecases;



import com.example.mediateka.domain.CinemaRepository;
import com.example.mediateka.domain.UseCase;
import com.example.mediateka.models.model.Cinema;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;

import static com.example.mediateka.utils.FormatterUtils.DEFAULT_VALUE;


public class GetTopRatedCinemas extends UseCase<List<Cinema>> {

    private final CinemaRepository repository;

    public GetTopRatedCinemas(Scheduler executorThread ,
                              Scheduler uiThread ,
                              CinemaRepository repository) {
        super(executorThread, uiThread);
        this.repository = repository;
    }

    @Override
    public Flowable<List<Cinema>> createUseCase() {
        return repository.getTopRatedCinemas(pageIndex)
                .toFlowable()
                .flatMap(Flowable::fromIterable)
                .filter(cinema -> !cinema.getDescription().equals(""))
                .filter(cinema -> !cinema.getReleaseDate().equals(DEFAULT_VALUE))
                .toList()
                .toFlowable();
    }
}
