package com.example.favoriteapp.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.favoriteapp.Film;

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
import static com.example.favoriteapp.db.DatabaseContract.FilmColumns.TYPE;
import static com.example.favoriteapp.db.DatabaseContract.FilmColumns.VOTE_AVERAGE;
import static com.example.favoriteapp.db.DatabaseContract.FilmColumns.VOTE_COUNT;

public class MyContentProvider extends ContentProvider {
    DataBaseHelper dataBaseHelper;
    public static final String AUTHORITY = "com.example.appforsubmitmade3";
    public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY);

    public MyContentProvider() {
    }

    @Override
    public boolean onCreate() {
        dataBaseHelper = new DataBaseHelper(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = dataBaseHelper.getWritableDatabase();
        return database.delete(getTableName(uri), selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase database = dataBaseHelper.getWritableDatabase();
        long value = database.insert(getTableName(uri), null, values);
        return Uri.withAppendedPath(CONTENT_URI, String.valueOf(value));
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
        Cursor cursor = database.query(getTableName(uri), projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static String getTableName(Uri uri){
        return uri.getPath().replace("/","");
    }

    public static ContentValues filmToContentValues(Film film, String type){
        ContentValues args = new ContentValues();
        args.put(POPULARITY, film.getPopularity());
        args.put(VOTE_COUNT, film.getVoteCount());
        args.put(POSTER_PATH, film.getPosterPath());
        args.put(ID, film.getId());
        args.put(BACKDROP_PATH, film.getBackdropPath());
        args.put(ORIGINAL_LANGUAGE, film.getOriginalLanguange());
        args.put(ORIGINAL_TITLE, film.getOriginalTitle());
        args.put(GENRE, film.getGenre());
        args.put(TITLE, film.getTitle());
        args.put(VOTE_AVERAGE, film.getVoteAverage());
        args.put(OVERVIEW, film.getOverview());
        args.put(RELEASE_DATE, film.getReleaseDate());
        args.put(TYPE, type);
        return args;
    }
}
