package com.example.mediateka.data.repository.cinema;




import com.example.mediateka.data.datasource.db.CinemaActorJoinDao;
import com.example.mediateka.data.datasource.db.CinemaDao;
import com.example.mediateka.domain.CinemaRepository;
import com.example.mediateka.models.db.ActorEntity;
import com.example.mediateka.models.db.CinemaEntity;
import com.example.mediateka.models.mapper.CinemaMapper;
import com.example.mediateka.models.model.Cinema;

import org.reactivestreams.Publisher;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class CinemaLocalRepository implements CinemaRepository {

    private final CinemaMapper mapper;
    private final CinemaDao cinemaDao;
    private final CinemaActorJoinDao cinemaActorJoinDao;

    public CinemaLocalRepository(CinemaDao cinemaDao ,
                                  CinemaMapper mapper ,
                                  CinemaActorJoinDao cinemaActorJoinDao) {
        this.cinemaDao = cinemaDao;
        this.mapper = mapper;
        this.cinemaActorJoinDao = cinemaActorJoinDao;
    }

    @Override
    public Single<List<Cinema>> getCinemas(int pageIndex) {
        return cinemaDao.getCinemas(pageIndex)
                .compose(mapCinemas());
    }

    @Override
    public Single<List<Cinema>> getTopRatedCinemas(int pageIndex) {
        return cinemaDao.getTopRatedCinemas(pageIndex)
                .compose(mapCinemas());
    }

    @Override
    public Single<List<Cinema>> getUpComingCinemas(int pageIndex) {
        return cinemaDao.getUpComingCinemas(pageIndex , Calendar.getInstance().get(Calendar.YEAR))
                .compose(mapCinemas());
    }

    @Override
    public Single<Cinema> getCinemaById(final int cinemaId) {
        
        return cinemaDao.getCinemaById(cinemaId)
                .map(new Function<CinemaEntity, Cinema>() {
                    @Override
                    public Cinema apply(@NonNull CinemaEntity cinemaEntity) throws Exception {
                        return mapper.getCinemaEntityToCinema().mapCinemaDetail(cinemaEntity ,
                                cinemaActorJoinDao.getActorsForCinema(cinemaId));
                    }
                });
    }

    @Override
    public Flowable<List<Cinema>> searchCinemas(String query) {
        
        return cinemaDao.getCinemasByName(query)
                .map(new Function<List<CinemaEntity>, List<Cinema>>() {
                    @Override
                    public List<Cinema> apply(@NonNull List<CinemaEntity> cinemaEntities) throws Exception {
                        return mapper.getCinemaEntityToCinema().reverseMap(cinemaEntities);
                    }
                });
    }

    public Maybe<List<Cinema>> getFavouriteListCinema() {
        
        return cinemaDao.getFavouriteListCinema()
                .map(new Function<List<CinemaEntity>, List<Cinema>>() {
                    @Override
                    public List<Cinema> apply(@NonNull List<CinemaEntity> cinemaEntities) throws Exception {
                        return mapper.getCinemaEntityToCinema().reverseMap(cinemaEntities);
                    }
                });
    }

    public Completable removeFromDatabaseFavouriteCinema(final int cinemaId){
        
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                cinemaDao.insertFavouriteCinema(cinemaId , false);
            }
        });
    }

    public Completable saveIntoDatabaseFavouriteCinema(final int cinemaId){
        
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                cinemaDao.insertFavouriteCinema(cinemaId , true);
            }
        });
    }

    public Completable clearFavouriteListCinema(){
        return Completable.fromPublisher(cinemaDao
                .getFavouriteListCinema()
                .toFlowable()
                .flatMap(new Function<List<CinemaEntity>, Publisher<?>>() {
                    @Override
                    public Publisher<?> apply(@NonNull List<CinemaEntity> cinemaEntities) throws Exception {
                        return Flowable.fromIterable(cinemaEntities);
                    }
                })
                .doOnNext(cinemaEntity -> cinemaDao.insertFavouriteCinema(cinemaEntity.getCinemaId() , false)));
    }

    void insertCinemas(List<CinemaEntity> cinemaEntities){
        cinemaDao.insertAll(cinemaEntities);
    }

    void insertActors(List<ActorEntity> actors){
        cinemaDao.insertActors(actors);
    }

    void updateCinema(int cinemaId , int budget , int revenue , int cinemaDuration , String directorName) {
        cinemaDao.updateCinema(cinemaId , budget , revenue , cinemaDuration , directorName);
    }

    private SingleTransformer<List<CinemaEntity> , List<Cinema>> mapCinemas(){

        return new SingleTransformer<List<CinemaEntity>, List<Cinema>>() {
            @Override
            public SingleSource<List<Cinema>> apply(@NonNull Single<List<CinemaEntity>> upstream) {
                return upstream.map(new Function<List<CinemaEntity>, List<Cinema>>() {
                    @Override
                    public List<Cinema> apply(@NonNull List<CinemaEntity> cinemaEntities) throws Exception {
                        return mapper.getCinemaEntityToCinema().reverseMap(cinemaEntities);
                    }
                });
            }
        };

    }
}
