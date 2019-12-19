package com.example.appforsubmitmade3.ui.Search;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.appforsubmitmade3.BuildConfig;
import com.example.appforsubmitmade3.Film;
import com.example.appforsubmitmade3.db.MappingHelper;
import com.example.appforsubmitmade3.db.MyContentProvider;
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

import static com.example.appforsubmitmade3.db.DatabaseContract.FilmColumns.ORIGINAL_TITLE;

public class SearchViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Film>> listSearch = new MutableLiveData<>();
    private MutableLiveData<Integer> status = new MutableLiveData<>();

    void searchFilm(Context context, final String language, final String query, final String type){
        final ArrayList<Film> listItems = new ArrayList<>();
        if (type.equals("favorite")){
//            Cursor cursor = context.getContentResolver().query(getUri(), null, ORIGINAL_TITLE+" LIKE '%"+query+"%'", null,null);
//            if (cursor != null) {
//                listItems.addAll(MappingHelper.mapCursorToArrayList(cursor));
//            }
            return;
        }else {
            AsyncHttpClient client = new AsyncHttpClient();
            String url = "https://api.themoviedb.org/3/search/" + type + "?api_key=" + BuildConfig.API_KEY + "&language=" + language + "&query=" + query;
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String result = new String(responseBody);
                        JSONObject responseObject = new JSONObject(result);
                        JSONArray list = responseObject.getJSONArray("results");
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject itemObject = list.getJSONObject(i);
                            Film films = new Film(itemObject, type);
                            listItems.add(films);
                        }
                        status.postValue(statusCode);
                        listSearch.postValue(listItems);
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
    }

    LiveData<ArrayList<Film>> getSearch() { return  listSearch; }
    LiveData<Integer> getStatus(){
        return status;
    }

    private Uri getUri(){
        Uri uri = Uri.withAppendedPath(MyContentProvider.CONTENT_URI, "film");
        return uri;
    }
}
