package com.example.mediateka.di.actor;




import com.example.mediateka.di.ActivityScope;
import com.example.mediateka.presentation.actorlist.ActorsFragment;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = ActorListModule.class)
public interface ActorListComponent {
    void inject(ActorsFragment actorsFragment);
}
