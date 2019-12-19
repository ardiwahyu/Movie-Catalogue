package com.example.appforsubmitmade3.ui.tvShow;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.example.appforsubmitmade3.Adapter;
import com.example.appforsubmitmade3.DetailActivity;
import com.example.appforsubmitmade3.Film;
import com.example.appforsubmitmade3.R;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TvShowFragment extends Fragment {
    private Adapter adapter;
    private ProgressBar progressBar;
    private Boolean success = false;
    private TvShowViewModel tvShowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tv_show, container, false);

        progressBar = root.findViewById(R.id.progress_bar);

        adapter = new Adapter(getActivity());
        adapter.notifyDataSetChanged();

        RecyclerView rvFilm = root.findViewById(R.id.rv_film);
        rvFilm.setLayoutManager(new LinearLayoutManager(root.getContext()));
        rvFilm.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);
        tvShowViewModel = ViewModelProviders.of(this).get(TvShowViewModel.class);
        tvShowViewModel.getFilms().observe(this, getFilm);
        tvShowViewModel.setFilm(getLocale());
        if (!success) {
            tvShowViewModel.getStatus().observe(this, getStatusCode);
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

    private Observer<ArrayList<Film>> getFilm = new Observer<ArrayList<Film>>() {
        @Override
        public void onChanged(ArrayList<Film> films) {
            if (films != null) {
                adapter.setData(films);
                progressBar.setVisibility(View.GONE);
                adapter.setOnItemClickCallback(new Adapter.OnItemClickCallback() {
                    @Override
                    public void onItemClicked(Film data, int position) {
                        Intent showDetail = new Intent(getContext(), DetailActivity.class);
                        showDetail.putExtra(DetailActivity.FILM_PARCEL, data);
                        showDetail.putExtra(DetailActivity.TYPE, "tv");
                        startActivity(showDetail);
                    }
                });
            }
        }
    };

    private Observer<Integer> getStatusCode = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            if (integer == 0) {
                showAlertDialog(getResources().getString(R.string.con_fail_title),getResources().getString(R.string.con_fail_body));
            }else if (integer == 401){
                showAlertDialog(getResources().getString(R.string.server_eror),getResources().getString(R.string.server_eror_body));
            } else if (integer == 200) {
                success = true;
            }
        }
    };

    private void showAlertDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "TRY AGAIN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                progressBar.setVisibility(View.VISIBLE);
                tvShowViewModel.setFilm(getLocale());
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "EXIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        progressBar.setVisibility(View.GONE);
        alertDialog.show();
    }
}