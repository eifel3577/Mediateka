package com.example.mediateka.presentation.actorlist;



import com.example.mediateka.domain.Actions;
import com.example.mediateka.domain.UseCaseSubscriber;
import com.example.mediateka.domain.actorusecases.GetActorsByQuery;
import com.example.mediateka.models.model.Actor;
import com.example.mediateka.presentation.base.BasePresenter;
import com.example.mediateka.presentation.base.BaseView;

import java.util.List;

public class ActorsPresenter extends BasePresenter<ActorsPresenter.View> {

    private List<Actor> actors;
    private final GetActorsByQuery getActorsByQuery;

    public ActorsPresenter(GetActorsByQuery getActorsByQuery) {
        this.getActorsByQuery = getActorsByQuery;
    }

    @Override
    public void initialize() {
        getView().showLoading();
        getActorsByQuery.subscribe(new ActorsSubscriber());
        getActorsByQuery.setActions(new Actions(
                () -> getView().showLoading() ,
                () -> getView().hideLoading() ,
                () -> getView().clearAdapter()
        ));
        getView().hideLoading();
    }

    public void setActors(List<Actor> actors){
        this.actors = actors;
        showActors(this.actors);
    }

    public void onGetTextFromSearchField(String query) {
        getActorsByQuery.setQuery(query);
    }

    public void onActorClicked(int actorId , int viewHolderPosition){
        getView().openActor(actorId , viewHolderPosition);
    }

    private void showActors(List<Actor> actors){
        getView().showActors(actors);
        getView().hideLoading();
    }

    public void onDestroy() {
        getActorsByQuery.removeActions();
        getActorsByQuery.dispose();
        setView(null);
    }

    final class ActorsSubscriber extends UseCaseSubscriber<List<Actor>> {
        @Override
        public void onNext(List<Actor> actors) {
            getView().showActors(actors);
            getView().hideLoading();
        }

        @Override
        public void onError(Throwable t) {
            t.printStackTrace();
            getView().hideLoading();
        }

        @Override
        public void onComplete() {
            getView().hideLoading();
        }
    }

    public interface View extends BaseView {
        void openActor(int actorId , int viewHolderPosition);
        void showActors(List<Actor> actors);
        void clearAdapter();
    }
}
