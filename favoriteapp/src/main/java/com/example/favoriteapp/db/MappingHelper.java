package com.example.favoriteapp.db;

import android.database.Cursor;

import com.example.favoriteapp.Film;

import java.util.ArrayList;

import static com.example.favoriteapp.db.DatabaseContract.FilmColumns.BACKDROP_PATH;
import static com.example.favoriteapp.db.DatabaseContract.FilmColumns.GENRE;
import static com.example.favoriteapp.db.DatabaseContract.FilmColumns.ID;
import static com.example.favoriteapp.db.DatabaseContract.FilmColumns.ORIGINAL_LANGUAGE;
import static com.example.favoriteapp.db.DatabaseContract.FilmColumns.ORIGINAL_TITLE;
import static com.example.favoriteapp.db.DatabaseContract.FilmColumns.OVERVIEW;
import static com.example.favoriteapp.db.DatabaseContract.FilmColumns.POPULARITY;
import static com.example.favoriteapp.db.DatabaseContract.FilmColumns.POSTER_PATH;
import static com.example.favoriteapp.db.DatabaseContract.FilmColumns.RELEASE_DATE;
import static com.example.favoriteapp.db.DatabaseContract.FilmColumns.TITLE;
import static com.example.favoriteapp.db.DatabaseContract.FilmColumns.VOTE_AVERAGE;
import static com.example.favoriteapp.db.DatabaseContract.FilmColumns.VOTE_COUNT;

public class MappingHelper {
    public static ArrayList<Film> mapCursorToArrayList(Cursor cursor){
        ArrayList<Film> arrayList = new ArrayList<>();
        cursor.moveToFirst();
        Film film;
        if (cursor.getCount()>0){
            do{
                film = new Film();
                film.setPopularity(cursor.getDouble(cursor.getColumnIndexOrThrow(POPULARITY)));
                film.setVoteCount(cursor.getInt(cursor.getColumnIndexOrThrow(VOTE_COUNT)));
                film.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH)));
                film.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
                film.setBackdropPath(cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP_PATH)));
                film.setOriginalLanguange(cursor.getString(cursor.getColumnIndexOrThrow(ORIGINAL_LANGUAGE)));
                film.setOriginalTitle(cursor.getString(cursor.getColumnIndexOrThrow(ORIGINAL_TITLE)));
                film.setGenre(cursor.getString(cursor.getColumnIndexOrThrow(GENRE)));
                film.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                film.setVoteAverage(cursor.getDouble(cursor.getColumnIndexOrThrow(VOTE_AVERAGE)));
                film.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                film.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));

                arrayList.add(film);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }
}
