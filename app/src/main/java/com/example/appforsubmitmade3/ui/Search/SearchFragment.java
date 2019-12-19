package com.example.appforsubmitmade3.ui.Search;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.appforsubmitmade3.Adapter;
import com.example.appforsubmitmade3.DetailActivity;
import com.example.appforsubmitmade3.Film;
import com.example.appforsubmitmade3.R;

import java.util.ArrayList;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchFragment extends Fragment {
    private Adapter adapter;
    private ProgressBar progressBar;
    private Boolean success = false;
    private SearchViewModel searchViewModel;
    private String type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        progressBar = root.findViewById(R.id.progress_bar);

        adapter = new Adapter(getActivity());
        adapter.clearData();
        adapter.notifyDataSetChanged();

        RecyclerView rvFilm = root.findViewById(R.id.rv_film);
        rvFilm.setItemAnimator(new DefaultItemAnimator());
        rvFilm.setLayoutManager(new LinearLayoutManager(root.getContext()));
        rvFilm.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);
        type = getArguments().getString("type");
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        if(getArguments() != null){
            searchViewModel.searchFilm(this.getContext(), getLocale(), getArguments().getString("query"), type);
            searchViewModel.getSearch().observe(this, getSearch);
        }
        if (!success){
            searchViewModel.getStatus().observe(this, getStatusCode);
        }

        return root;
    }

    private String getLocale() {
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        if (language.equals("in")) {
            language = "id";
        } else if (language.equals("en")) {
            language = "en";
        }
        return language;
    }

    private Observer<ArrayList<Film>> getSearch = new Observer<ArrayList<Film>>() {
        @Override
        public void onChanged(ArrayList<Film> films) {
            if (films != null){
                adapter.setData(films);
                progressBar.setVisibility(View.GONE);
                adapter.setOnItemClickCallback(new Adapter.OnItemClickCallback() {
                    @Override
                    public void onItemClicked(Film data, int position) {
                        Intent showDetail = new Intent(getContext(), DetailActivity.class);
                        showDetail.putExtra(DetailActivity.FILM_PARCEL, data);
                        showDetail.putExtra(DetailActivity.TYPE, type);
                        startActivity(showDetail);
                    }
                });
            }
        }
    };


    private Observer<Integer> getStatusCode = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            if (integer == 0){
                showAlertDialog(getResources().getString(R.string.con_fail_title),getResources().getString(R.string.con_fail_body));
            }else if (integer == 401){
                showAlertDialog(getResources().getString(R.string.server_eror),getResources().getString(R.string.server_eror_body));
            }else if (integer == 200){
                success = true;
            }
        }
    };

    private void showAlertDialog(String title, String message){
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.try_again), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                progressBar.setVisibility(View.VISIBLE);
                searchViewModel.searchFilm(getContext(), getLocale(), getArguments().getString("query"), type);
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        progressBar.setVisibility(View.GONE);
        alertDialog.show();
    }
}
