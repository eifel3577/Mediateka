package com.example.mediateka.di.cinema.cinemadetail;



import com.example.mediateka.di.ActivityScope;
import com.example.mediateka.presentation.cinemadetail.CinemaDetailContentFragment;
import com.example.mediateka.presentation.cinemadetail.CinemaDetailsActivity;
import com.example.mediateka.presentation.smallcinemalist.SmallCinemasFragment;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = CinemaDetailModule.class)
public interface CinemaDetailComponent {

    void inject(CinemaDetailsActivity cinemaDetailsActivity);
    void inject(SmallCinemasFragment smallCinemasFragment);
    void inject(CinemaDetailContentFragment cinemaDetailContentFragment);
}
