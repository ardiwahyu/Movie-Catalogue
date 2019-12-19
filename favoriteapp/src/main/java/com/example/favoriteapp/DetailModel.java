package com.example.favoriteapp;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import cz.msebera.android.httpclient.Header;

public class DetailModel extends ViewModel {
    private MutableLiveData<Film> itemFilm = new MutableLiveData<>();
    private MutableLiveData<Integer> status = new MutableLiveData<>();

    public void setItemFilm(final String language, final String type, final int id){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.themoviedb.org/3/"+type+"/"+id+"?api_key="+ BuildConfig.API_KEY +"&language="+language;
        Log.d("url", url);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try{
                    String result = new String(responseBody);
                    JSONObject jsonObject = new JSONObject(result);
                    Film film = new Film(jsonObject, type);
                    status.postValue(statusCode);
                    itemFilm.postValue(film);
                } catch (JSONException e) {
                    Log.d("salah", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                status.postValue(statusCode);
            }
        });
    }

    public LiveData<Film> getFilms() {
        return itemFilm;
    }

    public LiveData<Integer> getStatus(){
        return status;
    }
}
