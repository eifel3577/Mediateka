package com.example.mediateka.presentation.actorlist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.mediateka.models.model.Actor;
import com.example.mediateka.presentation.common.OnActorClickListener;

import java.util.ArrayList;
import java.util.List;

public class ActorListAdapter extends RecyclerView.Adapter<ActorViewHolder> {

    private final List<Actor> actors;
    private final OnActorClickListener onActorClickListener;

    public ActorListAdapter(OnActorClickListener onActorClickListener) {
        actors = new ArrayList<>();
        this.onActorClickListener = onActorClickListener;
    }

    @Override
    @NonNull
    public ActorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actor , parent , false);
        return new ActorViewHolder(view , onActorClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ActorViewHolder holder, int position) {
        Actor actor = actors.get(position);
        holder.render(actor , holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return actors.size();
    }

    public void addAll(List<Actor> actorList){
        actors.clear();
        actors.addAll(actorList);
        notifyDataSetChanged();
    }

    public void clear(){
        actors.clear();
    }
}
