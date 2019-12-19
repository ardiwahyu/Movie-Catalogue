package com.example.appforsubmitmade3.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "dbfilm";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_FILM = String.format("CREATE TABLE %s" +
                    "(%s INTEGER PRIMARY KEY, " +
                    "%s DOUBLE, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, " +
                    "%s DOUBLE, %s TEXT, %s TEXT, %s TEXT)",
            DatabaseContract.TABLE_FILM,
            DatabaseContract.FilmColumns.ID,
            DatabaseContract.FilmColumns.POPULARITY,
            DatabaseContract.FilmColumns.VOTE_COUNT,
            DatabaseContract.FilmColumns.POSTER_PATH,
            DatabaseContract.FilmColumns.BACKDROP_PATH,
            DatabaseContract.FilmColumns.ORIGINAL_LANGUAGE,
            DatabaseContract.FilmColumns.ORIGINAL_TITLE,
            DatabaseContract.FilmColumns.GENRE,
            DatabaseContract.FilmColumns.TITLE,
            DatabaseContract.FilmColumns.VOTE_AVERAGE,
            DatabaseContract.FilmColumns.OVERVIEW,
            DatabaseContract.FilmColumns.RELEASE_DATE,
            DatabaseContract.FilmColumns.TYPE);

    public DataBaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_FILM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_FILM);
        onCreate(db);
    }
}