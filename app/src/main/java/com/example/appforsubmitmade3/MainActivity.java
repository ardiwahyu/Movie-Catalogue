package com.example.appforsubmitmade3;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.appforsubmitmade3.ui.Search.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navView;
    private SearchView searchView;
    private int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setColorStatusBar(R.color.colorPrimaryDark);
        setTheme(R.style.AppThemeNoActionBar);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        settingSearchButton(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting:
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.search:
                switch (getSelectedItem(navView)){
                    case R.id.navigation_movie:
                        searchView.setQueryHint(getResources().getString(R.string.search_movie));
                        break;
                    case R.id.navigation_tv_show:
                        searchView.setQueryHint(getResources().getString(R.string.search_tv));
                        break;
                    case R.id.navigation_favorite:
                        searchView.setQueryHint(getResources().getString(R.string.search_favorite));
                        break;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private int getSelectedItem(BottomNavigationView bottomNavigationView) {
        Menu menu = bottomNavigationView.getMenu();
        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            if (menuItem.isChecked()) {
                return menuItem.getItemId();
            }
        }
        return 0;
    }

    private void setLocal(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
    }

    public void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("Language", "en");
        setLocal(language);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE){
            if (resultCode == LanguageActivity.RESULT_CODE){
//                String language = data.getStringExtra(LanguageActivity.EXTRA_SELECTED_VALUE);
//                SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
////                editor.putString("Language", language);
////                editor.apply();
                recreate();
            }
        }
    }

    private void setColorStatusBar(int color){
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), color));
    }

    private void settingSearchButton(Menu menu) {
        if (getSelectedItem(navView) == R.id.navigation_favorite) {
            menu.findItem(R.id.search).setVisible(false);
        } else {
            menu.findItem(R.id.search).setVisible(true);
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            final SearchFragment[] fragment = new SearchFragment[1];
            if (searchManager != null) {
                searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        Bundle bundle = new Bundle();
                        bundle.putString("query", query);
                        if (getSelectedItem(navView) == R.id.navigation_movie) {
                            bundle.putString("type", "movie");
                        } else if (getSelectedItem(navView) == R.id.navigation_tv_show) {
                            bundle.putString("type", "tv");
                        }else if (getSelectedItem(navView) == R.id.navigation_favorite){
                            bundle.putString("type", "favorite");
                        }
                        fragment[0] = new SearchFragment();
                        fragment[0].setArguments(bundle);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.nav_host_fragment, fragment[0], SearchFragment.class.getSimpleName())
                                .commit();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });
                menu.findItem(R.id.search).setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        if (fragment[0] != null)
                            getSupportFragmentManager().beginTransaction().remove(fragment[0]).commit();
                        return true;
                    }
                });
            }
        }
    }
}
