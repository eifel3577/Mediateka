package com.example.mediateka.models.mapper;


import com.example.mediateka.models.model.Actor;
import com.example.mediateka.models.model.Cinema;
import com.example.mediateka.models.network.ActorDetailResponse;
import com.example.mediateka.models.network.ActorNetwork;
import com.example.mediateka.models.network.ActorResponse;
import com.example.mediateka.models.network.CinemaNetwork;
import com.example.mediateka.models.network.Poster;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.example.mediateka.utils.FormatterUtils.DEFAULT_VALUE;
import static com.example.mediateka.utils.FormatterUtils.defaultValueIfNull;
import static com.example.mediateka.utils.FormatterUtils.emptyValueIfNull;
import static com.example.mediateka.utils.FormatterUtils.formatDate;
import static com.example.mediateka.utils.FormatterUtils.getYearFromDate;


public class ActorDetailResponseToActor {

    public Actor map(ActorDetailResponse response){
        Actor actor = new Actor();
        actor.setActorId(response.getId());
        actor.setProfileUrl(response.getProfilePath());
        actor.setName(response.getName());
        actor.setBiography(response.getBiography());
        actor.setDeathDay((String) response.getDeathday());
        actor.setPlaceOfBirth(defaultValueIfNull(response.getPlaceOfBirth()));
        actor.setPopularity(response.getPopularity());
        checkAgeThenSet(response , actor);
        setCinemas(response , actor);
        setPosters(response , actor);
        setBackgroundPosters(response , actor);
        return actor;
    }

    public List<Actor> map(ActorResponse response){
        final List<Actor> actors = new ArrayList<>();
        for (ActorNetwork actorNetwork : response.getActors()){
            final Actor actor = new Actor();
            actor.setName(emptyValueIfNull(actorNetwork.getName()));
            actor.setActorId(actorNetwork.getActorId());
            actor.setProfileUrl(emptyValueIfNull(actorNetwork.getProfilePath()));
            actor.setPopularity(actorNetwork.getPopularity());
            actors.add(actor);
        }
        return actors;
    }

    private void setBackgroundPosters(ActorDetailResponse response, Actor actor) {
        List<String> backgroundUrls = new ArrayList<>(8);
        if (response.getTaggedImages() != null){
            int backgroundPostersSize = response.getTaggedImages().getImageResults().size();
            if (backgroundPostersSize != 0){
                for (ActorDetailResponse.ImageResult backgroundImg : response.getTaggedImages().getImageResults()){
                    backgroundUrls.add(backgroundImg.getBackgroundPoster());
                    if (backgroundUrls.size() >= 8){
                        break;
                    }
                }
            }
        } else {
            backgroundUrls = Collections.emptyList();
        }
        actor.setBackgroundUrls(backgroundUrls);
    }

    private void checkAgeThenSet(ActorDetailResponse response , Actor actor){
        if (response.getBirthday() == null){
            actor.setBirthDay(DEFAULT_VALUE);
            actor.setAge(DEFAULT_VALUE);
        } else {
            actor.setBirthDay(formatDate(response.getBirthday()));
            Calendar date = new GregorianCalendar(Locale.getDefault());
            int currentYear = date.get(Calendar.YEAR);
            actor.setAge(String.valueOf(currentYear - Integer.parseInt(getYearFromDate(response.getBirthday()))));
        }
    }

    private void setCinemas(ActorDetailResponse response , Actor actor){
        List<Cinema> cinemas = new ArrayList<>();
        if (response.getMovieCredits() != null){
            for (CinemaNetwork cinemaNetwork : response.getMovieCredits().getCinemas()){
                Cinema cinema = new Cinema();
                cinema.setId(cinemaNetwork.getId());
                cinema.setTitle(cinemaNetwork.getTitle());
                cinema.setReleaseDate(getYearFromDate(cinemaNetwork.getReleaseDate()));
                cinema.setDescription(cinemaNetwork.getDescription());
                cinema.setPosterUrl(cinemaNetwork.getPosterUrl());
                cinema.setVoteAverage(cinemaNetwork.getVoteAverage());
                cinema.setGenres(cinemaNetwork.getGenreIds());
                cinema.setCharacter(cinemaNetwork.getCharacter());
                cinemas.add(cinema);
            }
        } else {
            cinemas = Collections.emptyList();
        }
        actor.setCinemas(cinemas);
    }

    private void setPosters(ActorDetailResponse response , Actor actor){
        List<String> posterUrls = new ArrayList<>();
        if (response.getImagesResponse() != null){
            for (Poster poster : response.getImagesResponse().getActorPosters()){
                posterUrls.add(poster.getPosterUrl());
            }
        } else {
            posterUrls = Collections.emptyList();
        }
        actor.setPostersUrl(posterUrls);
    }
}
