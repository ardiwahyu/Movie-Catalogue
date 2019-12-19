package com.example.appforsubmitmade3.ui.movie;

import android.util.Log;

import com.example.appforsubmitmade3.BuildConfig;
import com.example.appforsubmitmade3.Film;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import cz.msebera.android.httpclient.Header;


public class MovieViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Film>> listFilm = new MutableLiveData<>();
    private MutableLiveData<Integer> status = new MutableLiveData<>();

    void setFilm(final String language){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Film> listItems = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/discover/movie?api_key="+ BuildConfig.API_KEY +"&language="+language;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try{
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    for (int i=0; i<list.length(); i++){
                        JSONObject itemObject = list.getJSONObject(i);
                        Film films = new Film(itemObject, "movie");
                        listItems.add(films);
                    }
                    status.postValue(statusCode);
                    listFilm.postValue(listItems);
                } catch (JSONException e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                status.postValue(statusCode);
            }
        });
    }

    LiveData<ArrayList<Film>> getFilms() {
        return listFilm;
    }

    LiveData<Integer> getStatus(){
        return status;
    }
}