package com.example.mediateka.di.cinema.cinemafavourite;



import com.example.mediateka.di.ActivityScope;
import com.example.mediateka.presentation.favouritelistcinema.FavouriteListCinemaActivity;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = CinemaFavouriteListModule.class)
public interface CinemaFavouriteListComponent {

    void inject(FavouriteListCinemaActivity activity);
}
