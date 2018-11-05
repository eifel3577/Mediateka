package com.example.mediateka.di.cinema;

import android.content.Context;


import com.example.mediateka.MediatekaApp;
import com.example.mediateka.data.datasource.db.CinemaActorJoinDao;
import com.example.mediateka.data.datasource.db.CinemaDao;
import com.example.mediateka.data.datasource.network.CinemaApiService;
import com.example.mediateka.data.repository.cinema.CinemaLocalRepository;
import com.example.mediateka.data.repository.cinema.CinemaRemoteRepository;
import com.example.mediateka.domain.CinemaRepository;
import com.example.mediateka.models.mapper.CinemaEntityToCinema;
import com.example.mediateka.models.mapper.CinemaMapper;
import com.example.mediateka.models.mapper.CinemaResponseToCinema;

import dagger.Module;
import dagger.Provides;

@Module
public class CinemaModule {

    @CinemaScope
    @Provides
    CinemaRepository provideRepository(CinemaLocalRepository cache ,
                                       CinemaApiService apiService ,
                                       CinemaMapper mapper ,
                                       CinemaActorJoinDao cinemaActorJoinDao){
        return new CinemaRemoteRepository(cache , apiService , mapper , cinemaActorJoinDao);
    }

    @CinemaScope
    @Provides
    CinemaLocalRepository provideCinemaLocalRepository(CinemaDao cinemaDao ,
                                                       CinemaMapper cinemaMapper ,
                                                       CinemaActorJoinDao cinemaActorJoinDao){
        return new CinemaLocalRepository(cinemaDao , cinemaMapper , cinemaActorJoinDao);
    }

    @CinemaScope
    @Provides
    CinemaDao providesCinemaDao(Context context){
        MediatekaApp mediatekaApp = (MediatekaApp) context;
        return mediatekaApp.getDatabaseInstance()
                .getCinemaDao();
    }

    @CinemaScope
    @Provides
    CinemaMapper provideCinemaMapper(CinemaResponseToCinema cinemaResponseToCinema , CinemaEntityToCinema cinemaEntityToCinema){
        return new CinemaMapper(cinemaResponseToCinema , cinemaEntityToCinema);
    }

    @CinemaScope
    @Provides
    CinemaResponseToCinema provideCinemaResponseToCinema(){
        return new CinemaResponseToCinema();
    }

    @CinemaScope
    @Provides
    CinemaEntityToCinema provideCinemaEntityToCinema(){
        return new CinemaEntityToCinema();
    }
}
