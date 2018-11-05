package com.example.mediateka.data.repository.actor;


import com.example.mediateka.data.datasource.db.CinemaActorJoinDao;
import com.example.mediateka.data.datasource.network.CinemaApiService;
import com.example.mediateka.domain.ActorRepository;
import com.example.mediateka.models.db.CinemaActorJoinEntity;
import com.example.mediateka.models.mapper.ActorDetailEntityToActor;
import com.example.mediateka.models.mapper.ActorDetailResponseToActor;
import com.example.mediateka.models.model.Actor;
import com.example.mediateka.models.model.Cinema;
import com.example.mediateka.models.network.ActorDetailResponse;
import com.example.mediateka.models.network.ActorResponse;
import com.example.mediateka.models.network.ImagesResponse;

import org.reactivestreams.Publisher;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class ActorRemoteRepository implements ActorRepository {

    private final CinemaApiService apiService;
    private final ActorDetailResponseToActor networkMapper;
    private final ActorDetailEntityToActor dbMapper;
    private final ActorLocalRepository localRepository;
    private final CinemaActorJoinDao cinemaActorJoinDao;

    public ActorRemoteRepository(CinemaApiService apiService,
                                         ActorDetailResponseToActor networkMapper,
                                         ActorDetailEntityToActor dbMapper ,
                                         ActorLocalRepository localRepository ,
                                         CinemaActorJoinDao cinemaActorJoinDao) {
        this.apiService = apiService;
        this.networkMapper = networkMapper;
        this.dbMapper = dbMapper;
        this.localRepository = localRepository;
        this.cinemaActorJoinDao = cinemaActorJoinDao;
    }

    @Override
    public Single<Actor> getActorById(final int actorId) {

        return Single.zip(apiService.getActorById(actorId, "tagged_images,movie_credits"), apiService.getImagesForActor(actorId), new BiFunction<ActorDetailResponse, ImagesResponse, Actor>() {
            @Override
            public Actor apply(@NonNull ActorDetailResponse actorDetailResponse, @NonNull ImagesResponse imagesResponse) throws Exception {
                actorDetailResponse.setImagesResponse(imagesResponse);
                return networkMapper.map(actorDetailResponse);
            }
        }).doAfterSuccess(new Consumer<Actor>() {
            @Override
            public void accept(@NonNull Actor actor) throws Exception {
                localRepository.updateActor(actorId ,
                        actor.getBiography() ,
                        actor.getBirthDay() ,
                        actor.getAge() ,
                        actor.getPlaceOfBirth());
                localRepository.insertCinemasForActor(dbMapper.mapCinemas(actor.getCinemas()));
                createRelationBetweenActorAndCinemas(actorId , actor.getCinemas());
            }
        }).onErrorResumeNext(new Function<Throwable, SingleSource<? extends Actor>>() {
            @Override
            public SingleSource<? extends Actor> apply(@NonNull Throwable throwable) throws Exception {
                return localRepository.getActorById(actorId);
            }
        });
    }

    @Override
    public Single<List<Actor>> getPopularActors(final int page){

        return apiService.getPopularActors(page)
                .map(new Function<ActorResponse, List<Actor>>() {
                    @Override
                    public List<Actor> apply(@NonNull ActorResponse actorResponse) throws Exception {
                        return networkMapper.map(actorResponse);
                    }
                }).doAfterSuccess(new Consumer<List<Actor>>() {
                    @Override
                    public void accept(@NonNull List<Actor> actors) throws Exception {
                        localRepository.insertActors(dbMapper.map(actors));
                    }
                }).onErrorResumeNext(new Function<Throwable, SingleSource<? extends List<Actor>>>() {
                    @Override
                    public SingleSource<? extends List<Actor>> apply(@NonNull Throwable throwable) throws Exception {
                        return localRepository.getPopularActors(page);
                    }
                });
    }

    @Override
    public Flowable<List<Actor>> searchActors(final String query) {

        return apiService.searchActors(query)
                .map(new Function<ActorResponse, List<Actor>>() {
                    @Override
                    public List<Actor> apply(@NonNull ActorResponse actorResponse) throws Exception {
                        return networkMapper.map(actorResponse);
                    }
                }).doAfterNext(new Consumer<List<Actor>>() {
                    @Override
                    public void accept(@NonNull List<Actor> actors) throws Exception {
                        localRepository.insertActors(dbMapper.map(actors));
                    }
                }).onErrorResumeNext(new Function<Throwable, Publisher<? extends List<Actor>>>() {
                    @Override
                    public Publisher<? extends List<Actor>> apply(@NonNull Throwable throwable) throws Exception {
                        return localRepository.searchActors("%" + query + "%");
                    }
                });
    }

    private void createRelationBetweenActorAndCinemas(final int actorId , List<Cinema> cinemas){
        for (Cinema cinema : cinemas){
            cinemaActorJoinDao.insert(new CinemaActorJoinEntity(cinema.getId() , actorId));
        }
    }
}
