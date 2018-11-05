package com.example.mediateka.di.actor;



import com.example.mediateka.di.ActivityScope;
import com.example.mediateka.presentation.actordetail.ActorDetailActivity;
import com.example.mediateka.presentation.actordetail.ActorDetailContentFragment;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = ActorDetailModule.class)
public interface ActorDetailComponent {

    void inject(ActorDetailActivity actorDetailActivity);

    void inject(ActorDetailContentFragment actorDetailContentFragment);
}
