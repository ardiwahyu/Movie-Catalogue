package com.example.appforsubmitmade3.ui.favorite.tvShow;

import com.example.appforsubmitmade3.Film;

import java.util.ArrayList;

interface LoadTvShowCallback {
    void preExecute();
    void postExecute(ArrayList<Film> films);
}
