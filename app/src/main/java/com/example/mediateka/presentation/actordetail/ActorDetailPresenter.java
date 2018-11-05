package com.example.mediateka.presentation.actordetail;



import com.example.mediateka.domain.UseCaseSubscriber;
import com.example.mediateka.domain.actorusecases.GetActorById;
import com.example.mediateka.models.model.Actor;
import com.example.mediateka.presentation.base.BasePresenter;
import com.example.mediateka.presentation.base.BaseView;

import java.util.List;

public class ActorDetailPresenter extends BasePresenter<ActorDetailPresenter.View> {

    private final GetActorById useCaseGetCinemaById;
    private int actorId;

    public ActorDetailPresenter(GetActorById useCaseGetCinemaById) {
        this.useCaseGetCinemaById = useCaseGetCinemaById;
    }

    @Override
    public void initialize() {
        getView().showLoading();
        useCaseGetCinemaById.searchActorById(actorId);
        useCaseGetCinemaById.subscribe(new ActorDetailSubscriber());
    }

    @Override
    public void onDestroy() {
        useCaseGetCinemaById.dispose();
        setView(null);
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    public void onAvatarClicked(List<String> posterUrls) {
        getView().showPosters(posterUrls);
    }

    public interface View extends BaseView {
        void showActorDetail(Actor actor);
        void showPosters(List<String> posterUrls);
    }

    public class ActorDetailSubscriber extends UseCaseSubscriber<Actor> {
        @Override
        public void onNext(Actor actor) {
            getView().showActorDetail(actor);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            getView().hideLoading();
        }

        @Override
        public void onComplete() {
            getView().hideLoading();
        }
    }
}
