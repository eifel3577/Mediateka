package com.example.mediateka.di.cinema.cinemalist;


import com.example.mediateka.di.ActivityScope;
import com.example.mediateka.presentation.cinemalist.CinemaListFragment;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = CinemaListModule.class)
public interface CinemaListComponent {

    void inject(CinemaListFragment cinemaListFragment);
}
