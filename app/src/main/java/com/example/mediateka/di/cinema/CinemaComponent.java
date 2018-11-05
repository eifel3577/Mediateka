package com.example.mediateka.di.cinema;



import com.example.mediateka.di.cinema.cinemadetail.CinemaDetailComponent;
import com.example.mediateka.di.cinema.cinemadetail.CinemaDetailModule;
import com.example.mediateka.di.cinema.cinemafavourite.CinemaFavouriteListComponent;
import com.example.mediateka.di.cinema.cinemafavourite.CinemaFavouriteListModule;
import com.example.mediateka.di.cinema.cinemalist.CinemaListComponent;
import com.example.mediateka.di.cinema.cinemalist.CinemaListModule;

import dagger.Subcomponent;

@CinemaScope
@Subcomponent(modules = CinemaModule.class)
public interface CinemaComponent {

    CinemaListComponent plusCinemaListComponent(CinemaListModule cinemaListModule);
    CinemaDetailComponent plusCinemaDetailComponent(CinemaDetailModule cinemaDetailModule);
    CinemaFavouriteListComponent plusCinemaFavouriteListComponent(CinemaFavouriteListModule cinemaFavouriteListModule);
}
