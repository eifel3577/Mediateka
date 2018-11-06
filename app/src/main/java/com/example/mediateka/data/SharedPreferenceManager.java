package com.example.mediateka.data;

import android.content.SharedPreferences;

import com.google.gson.annotations.Since;

public class SharedPreferenceManager {

    private final SharedPreferences preferences;
    private static final String PREF_CINEMA_SORTING_POSITION = "pref_cinema_sorting_position";

    public SharedPreferenceManager(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void saveCinemaSortingPosition(final int position){

        edit(new Consumer<SharedPreferences.Editor>() {
            @Override
            public void apply(SharedPreferences.Editor editor) {
                editor.putInt(PREF_CINEMA_SORTING_POSITION , position);
            }
        });
    }

    public int getCinemaSortingPosition(){
        return preferences.getInt(PREF_CINEMA_SORTING_POSITION , 0);
    }

    private void edit(Consumer<SharedPreferences.Editor> consumer){
        SharedPreferences.Editor editor = preferences.edit();
        consumer.apply(editor);
        editor.apply();
    }


    //@FunctionalInterface
    private interface Consumer<T>{
        void apply(T t);
    }
}
