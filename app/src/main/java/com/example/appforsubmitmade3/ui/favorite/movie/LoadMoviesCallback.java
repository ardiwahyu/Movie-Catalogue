package com.example.appforsubmitmade3.ui.favorite.movie;

import com.example.appforsubmitmade3.Film;

import java.util.ArrayList;

interface LoadMoviesCallback {
    void preExecute();
    void postExecute(ArrayList<Film> films);
}
