package com.example.favoriteapp.favorite.tvShow;

import com.example.favoriteapp.Film;

import java.util.ArrayList;

interface LoadTvShowCallback {
    void preExecute();
    void postExecute(ArrayList<Film> films);
}
