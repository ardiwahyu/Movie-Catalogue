package com.example.favoriteapp.db;

import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String TABLE_FILM = "film";
    public static final String AUTHORITY = "com.example.appforsubmitmade3";
    private static final String SCHEME = "scheme";

    public static final class FilmColumns implements BaseColumns{
        public static String POPULARITY = "popularity";
        public static String VOTE_COUNT = "votecount";
        public static String POSTER_PATH = "posterpath";
        public static String ID = "id";
        public static String BACKDROP_PATH = "backdroppath";
        public static String ORIGINAL_LANGUAGE = "originallanguage";
        public static String ORIGINAL_TITLE = "originaltitle";
        public static String GENRE = "genre";
        public static String TITLE = "title";
        public static String VOTE_AVERAGE = "voteaverage";
        public static String OVERVIEW = "overview";
        public static String RELEASE_DATE = "releasedate";
        public static String TYPE = "type";
    }
}
