package com.example.favoriteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.example.favoriteapp.favorite.movie.FavoriteMovieFragment;
import com.example.favoriteapp.favorite.tvShow.FavoriteTvFragment;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponent();
    }

    private void setUpViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        FavoriteMovieFragment favoriteMovieFragment = new FavoriteMovieFragment();
        FavoriteTvFragment favoriteTvFragment = new FavoriteTvFragment();
        adapter.addFragment(favoriteMovieFragment, getResources().getString(R.string.title_movie));
        adapter.addFragment(favoriteTvFragment, getResources().getString(R.string.title_tv_show));
        viewPager.setAdapter(adapter);
    }

    private void initComponent() {
        ViewPager viewPager = findViewById(R.id.viewpager);
        setUpViewPager(viewPager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
