package com.example.mediateka.data.repository.cinema;



import com.example.mediateka.data.datasource.db.CinemaActorJoinDao;
import com.example.mediateka.data.datasource.network.CinemaApiService;
import com.example.mediateka.domain.CinemaRepository;
import com.example.mediateka.models.db.CinemaActorJoinEntity;
import com.example.mediateka.models.mapper.CinemaMapper;
import com.example.mediateka.models.model.Actor;
import com.example.mediateka.models.model.Cinema;
import com.example.mediateka.models.network.CinemaDetailResponse;
import com.example.mediateka.models.network.CinemaResponse;
import com.example.mediateka.models.network.ImagesResponse;

import org.reactivestreams.Publisher;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


public class CinemaRemoteRepository implements CinemaRepository {

    private final CinemaLocalRepository cinemaLocalRepository;
    private final CinemaMapper mapper;
    private final CinemaApiService apiService;
    private final CinemaActorJoinDao cinemaActorJoinDao;

    public CinemaRemoteRepository(CinemaLocalRepository cinemaLocalRepository,
                                  CinemaApiService apiService ,
                                  CinemaMapper mapper ,
                                  CinemaActorJoinDao cinemaActorJoinDao) {
        this.cinemaLocalRepository = cinemaLocalRepository;
        this.apiService = apiService;
        this.mapper = mapper;
        this.cinemaActorJoinDao = cinemaActorJoinDao;
    }

    @Override
    public Single<List<Cinema>> getCinemas(final int pageIndex) {
        return apiService.getCinemas(pageIndex)
                .compose(saveToDatabaseCinemaList())
                .onErrorResumeNext(new Function<Throwable, SingleSource<? extends List<Cinema>>>() {
                    @Override
                    public SingleSource<? extends List<Cinema>> apply(@NonNull Throwable throwable) throws Exception {
                        return cinemaLocalRepository.getCinemas(pageIndex);
                    }
                });
    }

    @Override
    public Single<List<Cinema>> getTopRatedCinemas(final int pageIndex) {
        return apiService.getTopRatedCinemas(pageIndex)
                .compose(saveToDatabaseCinemaList())
                .onErrorResumeNext(new Function<Throwable, SingleSource<? extends List<Cinema>>>() {
                    @Override
                    public SingleSource<? extends List<Cinema>> apply(@NonNull Throwable throwable) throws Exception {
                        return cinemaLocalRepository.getTopRatedCinemas(pageIndex);
                    }
                });
    }

    @Override
    public Single<List<Cinema>> getUpComingCinemas(final int pageIndex) {
        return apiService.getUpComingCinemas(pageIndex)
                .compose(saveToDatabaseCinemaList())
                .onErrorResumeNext(new Function<Throwable, SingleSource<? extends List<Cinema>>>() {
                    @Override
                    public SingleSource<? extends List<Cinema>> apply(@NonNull Throwable throwable) throws Exception {
                        return cinemaLocalRepository.getUpComingCinemas(pageIndex);
                    }
                });
    }

    @Override
    public Single<Cinema> getCinemaById(final int cinemaId) {

        return Single.zip(apiService.getCinemaById(cinemaId, "credits"),
                apiService.getImagesForCinema(cinemaId, "en,null"), new BiFunction<CinemaDetailResponse, ImagesResponse, Cinema>() {
                    @Override
                    public Cinema apply(@NonNull CinemaDetailResponse cinemaDetailResponse, @NonNull ImagesResponse imagesResponse) throws Exception {
                        cinemaDetailResponse.setImages(imagesResponse);
                        return mapper.map(cinemaDetailResponse);
                    }
                }).doAfterSuccess(new Consumer<Cinema>() {
            @Override
            public void accept(@NonNull Cinema cinema) throws Exception {
                cinemaLocalRepository.updateCinema(cinemaId ,
                        cinema.getBudget() ,
                        cinema.getCinemaRevenue() ,
                        cinema.getDuration() ,
                        cinema.getDirectorName());
                cinemaLocalRepository.insertActors(mapper.mapActors(cinema.getActors()));
                createRelationBetweenCinemaAndActors(cinemaId , cinema.getActors());
            }
        }).onErrorResumeNext(new Function<Throwable, SingleSource<? extends Cinema>>() {
            @Override
            public SingleSource<? extends Cinema> apply(@NonNull Throwable throwable) throws Exception {
                return cinemaLocalRepository.getCinemaById(cinemaId);
            }
        });
    }

    @Override
    public Flowable<List<Cinema>> searchCinemas(final String query) {

        return apiService.searchCinemas(query)
                .map(new Function<CinemaResponse, List<Cinema>>() {
                    @Override
                    public List<Cinema> apply(@NonNull CinemaResponse cinemaResponse) throws Exception {
                        return mapper.map(cinemaResponse);
                    }
                }).doAfterNext(new Consumer<List<Cinema>>() {
                    @Override
                    public void accept(@NonNull List<Cinema> cinemas) throws Exception {
                        cinemaLocalRepository.insertCinemas(mapper.map(cinemas));
                    }
                }).onErrorResumeNext(new Function<Throwable, Publisher<? extends List<Cinema>>>() {
                    @Override
                    public Publisher<? extends List<Cinema>> apply(@NonNull Throwable throwable) throws Exception {
                        return cinemaLocalRepository.searchCinemas("%" + query + "%");
                    }
                });
    }

    private SingleTransformer<CinemaResponse, List<Cinema>> saveToDatabaseCinemaList(){

        return new SingleTransformer<CinemaResponse, List<Cinema>>() {
            @Override
            public SingleSource<List<Cinema>> apply(@NonNull Single<CinemaResponse> upstream) {
                return upstream.map(new Function<CinemaResponse, List<Cinema>>() {
                    @Override
                    public List<Cinema> apply(@NonNull CinemaResponse cinemaResponse) throws Exception {
                        return mapper.map(cinemaResponse);
                    }
                }).doAfterSuccess(cinemas -> cinemaLocalRepository.insertCinemas(mapper.map(cinemas)));
            }
        };

    }

    private void createRelationBetweenCinemaAndActors(final int cinemaId , final List<Actor> actors){
        for (Actor actor : actors){
            cinemaActorJoinDao.insert(new CinemaActorJoinEntity(cinemaId , actor.getActorId()));
        }
    }
}
