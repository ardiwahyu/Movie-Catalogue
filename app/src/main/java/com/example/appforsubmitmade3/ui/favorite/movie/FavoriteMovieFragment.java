package com.example.appforsubmitmade3.ui.favorite.movie;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.appforsubmitmade3.Adapter;
import com.example.appforsubmitmade3.DetailActivity;
import com.example.appforsubmitmade3.Film;
import com.example.appforsubmitmade3.R;
import com.example.appforsubmitmade3.db.MyContentProvider;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteMovieFragment extends Fragment implements LoadMoviesCallback, Adapter.OnItemClickCallback {
    private static final String DATABASE_TABLE = "film";
    private Adapter adapter;
    private ProgressBar progressBar;
    private static Uri uri;
    private static final String EXTRA_STATE = "EXTRA_STATE";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorite_movie, container, false);
        initComponent(root, savedInstanceState);
        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getListFilm());
    }

    @Override
    public void preExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void postExecute(ArrayList<Film> films) {
        progressBar.setVisibility(View.GONE);
        adapter.setData(films);
    }

    private void initComponent(View root, Bundle savedInstanceState) {
        uri = Uri.withAppendedPath(MyContentProvider.CONTENT_URI, DATABASE_TABLE);
        progressBar = root.findViewById(R.id.progress_bar);
        adapter = new Adapter(getActivity());
        adapter.notifyDataSetChanged();

        RecyclerView rvFilm = root.findViewById(R.id.rv_film);
        rvFilm.setLayoutManager(new LinearLayoutManager(root.getContext()));
        rvFilm.setAdapter(adapter);

        if (savedInstanceState == null) {
            new LoadMovieAsync(getContext(), this, uri).execute();
        } else {
            ArrayList<Film> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null)
                adapter.setData(list);
        }
        adapter.setOnItemClickCallback(this);
    }

    @Override
    public void onItemClicked(Film data, int position) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(DetailActivity.FILM_PARCEL, data);
        intent.putExtra(DetailActivity.FROM, "favorite");
        intent.putExtra(DetailActivity.EXTRA_POSITION, position);
        startActivityForResult(intent, DetailActivity.REQUEST_ACTION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DetailActivity.REQUEST_ACTION) {
            if (resultCode == DetailActivity.RESULT_REMOVE) {
                int position = data.getIntExtra(DetailActivity.EXTRA_POSITION, 0);
                adapter.removeItem(position);
                adapter.notifyItemRemoved(position);
                Toast.makeText(getContext(), getString(R.string.remove), Toast.LENGTH_LONG).show();
            }
        }
    }
}
