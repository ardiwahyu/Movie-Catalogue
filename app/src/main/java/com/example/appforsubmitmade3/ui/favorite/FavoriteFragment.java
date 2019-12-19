package com.example.appforsubmitmade3.ui.favorite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.appforsubmitmade3.R;
import com.example.appforsubmitmade3.ui.favorite.movie.FavoriteMovieFragment;
import com.example.appforsubmitmade3.ui.favorite.tvShow.FavoriteTvFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class FavoriteFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        initComponent(view);
        return view;
    }

    private void setUpViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        FavoriteMovieFragment favoriteMovieFragment = new FavoriteMovieFragment();
        FavoriteTvFragment favoriteTvFragment = new FavoriteTvFragment();
        adapter.addFragment(favoriteMovieFragment, getResources().getString(R.string.title_movie));
        adapter.addFragment(favoriteTvFragment, getResources().getString(R.string.title_tv_show));
        viewPager.setAdapter(adapter);
    }

    private void initComponent(View view) {
        ViewPager viewPager = view.findViewById(R.id.viewpager);
        setUpViewPager(viewPager);
        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}