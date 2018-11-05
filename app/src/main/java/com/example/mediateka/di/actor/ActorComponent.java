package com.example.mediateka.di.actor;



import com.example.mediateka.presentation.popularactors.PopularActorsActivity;

import dagger.Subcomponent;

@ActorScope
@Subcomponent(modules = ActorModule.class)
public interface ActorComponent {

    void inject(PopularActorsActivity popularActors);

    ActorListComponent plusActorListComponent(ActorListModule actorListModule);
    ActorDetailComponent plusActorDetailComponent(ActorDetailModule actorDetailModule);
}
