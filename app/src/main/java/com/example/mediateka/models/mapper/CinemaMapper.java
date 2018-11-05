package com.example.mediateka.models.mapper;



import com.example.mediateka.models.db.ActorEntity;
import com.example.mediateka.models.db.CinemaEntity;
import com.example.mediateka.models.model.Actor;
import com.example.mediateka.models.model.Cinema;
import com.example.mediateka.models.network.CinemaDetailResponse;
import com.example.mediateka.models.network.CinemaResponse;

import java.util.List;

public class CinemaMapper {

    private CinemaResponseToCinema cinemaResponseToCinema;
    private CinemaEntityToCinema cinemaEntityToCinema;


    public CinemaMapper(CinemaResponseToCinema cinemaResponseToCinema,
                                CinemaEntityToCinema cinemaEntityToCinema) {
        this.cinemaResponseToCinema = cinemaResponseToCinema;
        this.cinemaEntityToCinema = cinemaEntityToCinema;
    }

    public Cinema map(CinemaDetailResponse response){
        return cinemaResponseToCinema.map(response);
    }

    public List<Cinema> map(CinemaResponse response){
        return cinemaResponseToCinema.map(response);
    }

    public CinemaEntity map(Cinema cinema){
        return cinemaEntityToCinema.map(cinema);
    }

    public List<CinemaEntity> map(List<Cinema> cinemas){
        return cinemaEntityToCinema.map(cinemas);
    }

    public List<ActorEntity> mapActors(List<Actor> actors){
        return cinemaEntityToCinema.mapActors(actors);
    }

    public CinemaEntityToCinema getCinemaEntityToCinema() {
        return cinemaEntityToCinema;
    }

}
