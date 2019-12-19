package com.example.favoriteapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Film implements Parcelable {
    private double popularity;
    private int voteCount;
    private String posterPath;
    private int id;
    private String backdropPath;
    private String originalLanguange;
    private String originalTitle;
    private String genre;
    private String title;
    private double voteAverage;
    private String overview;
    private String releaseDate;

    public Film(JSONObject object, String movie) {
        try{
            this.popularity = object.getDouble("popularity");
            this.voteCount = object.getInt("vote_count");
            this.posterPath = object.getString("poster_path");
            this.id = object.getInt("id");
            this.backdropPath = object.getString("backdrop_path");
            this.originalLanguange = convertLanguage(object.getString("original_language"));
            this.voteAverage = object.getDouble("vote_average");
            this.overview = object.getString("overview");
            if (object.isNull("genre_ids")){
                this.genre = convertJsonGenre(object.getJSONArray("genres"));
            } else{
                this.genre = convertGenre(object.getJSONArray("genre_ids"));
            }
            if (movie.equals("movie")){
                this.releaseDate = formatDate(object.getString("release_date"));
                this.originalTitle = object.getString("original_title");
                this.title = object.getString("title");
            }else if (movie.equals("tv")){
                this.releaseDate = formatDate(object.getString("first_air_date"));
                this.originalTitle = object.getString("original_name");
                this.title = object.getString("name");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOriginalLanguange() {
        return originalLanguange;
    }

    public void setOriginalLanguange(String originalLanguange) {
        this.originalLanguange = originalLanguange;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getOneGenre(){
        return getGenre().split(",")[0];
    }

    public Film() {
    }

    private String convertGenre(JSONArray jsonArray){
        String genre = "";
        String[] genString = {"Action","Adventure","Animation","Comedy","Crime","Documentary","Drama","Family","Fantasy",
                "History","Horror","Music","Mystery","Romance","Science Fiction","TV Movie","Thriller","War","Western"};
        for (int i=0; i < jsonArray.length(); i++){
            int idGen = 0;
            try {
                idGen = searchId(jsonArray.getInt(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            genre = genre + genString[idGen];
            if (i != jsonArray.length()-1)
                genre = genre + ", ";
        }
        return genre;
    }

    private String convertJsonGenre(JSONArray jsonArray){
        String genre = "";
        for (int i=0; i < jsonArray.length(); i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                genre = genre + jsonObject.get("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (i != jsonArray.length()-1)
                genre = genre + ", ";
        }
        return genre;
    }
    private int searchId(int genreId){
        int[] genId = {28,12,16, 35, 80, 99, 18, 10751, 14, 36, 27, 10402, 9648, 10749, 878, 10770, 53, 10752, 37};
        for (int i=0; i<genId.length; i++){
            if (genId[i] == genreId)
                return i;
        }
        return 0;
    }

    private String convertLanguage(String language){
        Locale locale = new Locale(language);
        return locale.getDisplayLanguage();
    }

    private String formatDate(String date){
        if (!"".equals(date)){
            Log.d("date", date);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date newdate = null;
            try {
                newdate = dateFormat.parse(date);
                dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return dateFormat.format(newdate);
        }else{
            return date;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.popularity);
        dest.writeInt(this.voteCount);
        dest.writeString(this.posterPath);
        dest.writeInt(this.id);
        dest.writeString(this.backdropPath);
        dest.writeString(this.originalLanguange);
        dest.writeString(this.originalTitle);
        dest.writeString(this.genre);
        dest.writeString(this.title);
        dest.writeDouble(this.voteAverage);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
    }

    protected Film(Parcel in) {
        this.popularity = in.readDouble();
        this.voteCount = in.readInt();
        this.posterPath = in.readString();
        this.id = in.readInt();
        this.backdropPath = in.readString();
        this.originalLanguange = in.readString();
        this.originalTitle = in.readString();
        this.genre = in.readString();
        this.title = in.readString();
        this.voteAverage = in.readDouble();
        this.overview = in.readString();
        this.releaseDate = in.readString();
    }

    public static final Creator<Film> CREATOR = new Creator<Film>() {
        @Override
        public Film createFromParcel(Parcel source) {
            return new Film(source);
        }

        @Override
        public Film[] newArray(int size) {
            return new Film[size];
        }
    };
}
