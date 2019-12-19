package com.example.favoriteapp.favorite.movie;

import com.example.favoriteapp.Film;

import java.util.ArrayList;

interface LoadMoviesCallback {
    void preExecute();
    void postExecute(ArrayList<Film> films);
}
