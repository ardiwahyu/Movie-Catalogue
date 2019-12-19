package com.example.favoriteapp.favorite.tvShow;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.favoriteapp.Film;
import com.example.favoriteapp.db.MappingHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.example.favoriteapp.db.DatabaseContract.FilmColumns.TYPE;


public class LoadTvShowAsync extends AsyncTask<Void, Void, ArrayList<Film>> {
    private final WeakReference<Context> weakReference;
    private final WeakReference<LoadTvShowCallback> weakCallback;
    private final WeakReference<Uri> weakUri;

    public LoadTvShowAsync(Context context, LoadTvShowCallback callback, Uri uri) {
        this.weakReference = new WeakReference<>(context);
        this.weakCallback = new WeakReference<>(callback);
        this.weakUri = new WeakReference<>(uri);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        weakCallback.get().preExecute();
    }

    @Override
    protected ArrayList<Film> doInBackground(Void... voids) {
        Context context = weakReference.get();
        Cursor cursor = context.getContentResolver().query(weakUri.get(), null, TYPE +"='tv'", null, null);
        return MappingHelper.mapCursorToArrayList(cursor);
    }

    @Override
    protected void onPostExecute(ArrayList<Film> films) {
        super.onPostExecute(films);
        weakCallback.get().postExecute(films);
    }
}
