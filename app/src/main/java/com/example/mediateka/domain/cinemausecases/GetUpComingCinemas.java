package com.example.mediateka.domain.cinemausecases;



import com.example.mediateka.domain.CinemaRepository;
import com.example.mediateka.domain.UseCase;
import com.example.mediateka.models.model.Cinema;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;

import static com.example.mediateka.utils.FormatterUtils.DEFAULT_VALUE;


public class GetUpComingCinemas extends UseCase<List<Cinema>> {

    private final CinemaRepository repository;

    public GetUpComingCinemas(Scheduler executorThread ,
                              Scheduler uiThread ,
                              CinemaRepository repository) {
        super(executorThread, uiThread);
        this.repository = repository;
    }

    @Override
    public Flowable<List<Cinema>> createUseCase() {
        return repository.getUpComingCinemas(pageIndex)
                .toFlowable()
                .flatMap(Flowable::fromIterable)
                .filter(cinema -> !cinema.getDescription().isEmpty())
                .filter(cinema -> !cinema.getReleaseDate().equals(DEFAULT_VALUE) && cinema.getVoteAverage() == 0)
                .toList()
                .toFlowable();
    }
}
