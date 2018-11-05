package com.example.mediateka.presentation.common;

/*
Must implement ALL holders!
 */

public interface HolderRenderer<T> {
    void render(T item , int adapterPosition);
}
