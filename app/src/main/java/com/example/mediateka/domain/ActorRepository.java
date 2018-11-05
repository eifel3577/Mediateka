package com.example.mediateka.domain;



import com.example.mediateka.models.model.Actor;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface ActorRepository {

    Flowable<List<Actor>> searchActors(String query);
    Single<Actor> getActorById(int id);
    Single<List<Actor>> getPopularActors(int page);
}
