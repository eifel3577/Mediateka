package com.example.mediateka.data.repository.actor;



import com.example.mediateka.data.datasource.db.ActorDao;
import com.example.mediateka.data.datasource.db.CinemaActorJoinDao;
import com.example.mediateka.domain.ActorRepository;
import com.example.mediateka.models.db.ActorEntity;
import com.example.mediateka.models.db.CinemaEntity;
import com.example.mediateka.models.mapper.ActorDetailEntityToActor;
import com.example.mediateka.models.model.Actor;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class ActorLocalRepository implements ActorRepository {

    private final ActorDao actorDao;
    private final ActorDetailEntityToActor mapper;
    private final CinemaActorJoinDao cinemaActorJoinDao;

    public ActorLocalRepository(ActorDao actorDao ,
                                ActorDetailEntityToActor mapper ,
                                CinemaActorJoinDao cinemaActorJoinDao) {
        this.actorDao = actorDao;
        this.mapper = mapper;
        this.cinemaActorJoinDao = cinemaActorJoinDao;
    }

    @Override
    public Single<Actor> getActorById(final int actorId) {
        return actorDao.getActorById(actorId)
                .map(new Function<ActorEntity, Actor>() {
                    @Override
                    public Actor apply(@NonNull ActorEntity actorEntity) throws Exception {
                        return mapper.mapDetailActor(actorEntity ,
                                cinemaActorJoinDao.getCinemasForActor(actorId));
                    }
                });


    }

    @Override
    public Flowable<List<Actor>> searchActors(String query) {
        return actorDao.getAllActorsByName(query)
                .map(new Function<List<ActorEntity>, List<Actor>>() {
                    @Override
                    public List<Actor> apply(@NonNull List<ActorEntity> actorEntities) throws Exception {
                        return mapper.reverseMap(actorEntities);
                    }
                });
    }

    @Override
    public Single<List<Actor>> getPopularActors(int page){
        return actorDao.getPopularActors() //TODO FIX THIS PAGE
                .map(new Function<List<ActorEntity>, List<Actor>>() {
                    @Override
                    public List<Actor> apply(@NonNull List<ActorEntity> actorEntities) throws Exception {
                        return mapper.reverseMap(actorEntities);
                    }
                });
    }

    public void insertCinemasForActor(List<CinemaEntity> cinemaEntities) {
        actorDao.insertCinemas(cinemaEntities);
    }

    public void updateActor(int actorId, String biography, String birthDay, String age, String placeOfBirth) {
        actorDao.updateActor(actorId , biography , birthDay , age , placeOfBirth);
    }

    public void insertActors(List<ActorEntity> actorEntities){
        actorDao.insertActors(actorEntities);
    }
}
