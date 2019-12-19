package com.example.appforsubmitmade3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.appforsubmitmade3.db.MyContentProvider;
import com.example.appforsubmitmade3.ui.DetailModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation;

import static com.example.appforsubmitmade3.db.DatabaseContract.FilmColumns.ID;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_ACTION = 100;
    public static final int RESULT_REMOVE = 101;
    public static final String EXTRA_POSITION = "extra_position";
    private static final String DATABASE_TABLE = "film";

    private ImageView imgItem, rate1, rate2, rate3, rate4, rate5, imgBackground;
    private TextView tvJudul, tvRating, tvRelease, tvPopularity,
            tvVoteCount, tvOriLanguage, tvGenre, tvSinopsis, tvReadmore;
    private ProgressBar progressBar;
    private FloatingActionButton fab;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    final public static String FILM_PARCEL = "film";
    final public static String FROM = "from";
    final public static String TYPE = "type";
    private Boolean success = false;
    private DetailModel detailModel;
    private Film filmParcel;
    private Menu menu;
    private boolean favorite = false;
    private String type;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setTheme(R.style.AppThemeNoActionBar);
        setContentView(R.layout.activity_detail);
        initComponent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        hideOption(R.id.favorite_menu);
        if(favorite)
            menu.getItem(0).setIcon(R.drawable.ic_favorite_24dp);
        else
            menu.getItem(0).setIcon(R.drawable.ic_favorite_border_black_24dp);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.favorite_menu:
                if (!favorite) {
                    setFavorite(menu);
                    Uri hasil = getContentResolver().insert(uri, MyContentProvider.filmToContentValues(filmParcel, type));
                    long result = Long.parseLong(hasil.getPath().replace("/",""));
                    if (result > 0 ) {
                        Toast.makeText(this, getString(R.string.setfavorite), Toast.LENGTH_LONG).show();
                        sendUpdateIntent(getApplicationContext());
                    }
                } else{
                    removeFavorite(menu);
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFilm(final int id, final String type){
        detailModel = ViewModelProviders.of(this).get(DetailModel.class);
        detailModel.getFilms().observe(this, getFilm);
        detailModel.setItemFilm(getLocale(), type, id);
        if (!success){
            detailModel.getStatus().observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    if (integer == 0){
                        bindFilmParcel(filmParcel);
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.con_fail_title), Toast.LENGTH_LONG).show();
                    }else if (integer == 401){
                        bindFilmParcel(filmParcel);
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_eror), Toast.LENGTH_LONG).show();
                    }else if (integer == 200){
                        success = true;
                    }
                }
            });
        }
    }

    private Observer<Film> getFilm = new Observer<Film>() {
        @Override
        public void onChanged(Film film) {
            filmParcel = film;
            bindFilmParcel(film);
        }
    };

    private void setRate(double rate){
        if(rate > 0 && rate < 1){
            rate1.setImageResource(R.drawable.ic_half_star);
        }else if (rate > 1){
            rate1.setImageResource(R.drawable.ic_full_star);
        }if (rate > 1 && rate < 2){
            rate2.setImageResource(R.drawable.ic_half_star);
        }else if (rate > 2){
            rate2.setImageResource(R.drawable.ic_full_star);
        }if (rate > 2 && rate < 3){
            rate3.setImageResource(R.drawable.ic_half_star);
        }else if (rate > 3){
            rate3.setImageResource(R.drawable.ic_full_star);
        }if (rate > 3 && rate < 4){
            rate4.setImageResource(R.drawable.ic_half_star);
        }else if (rate > 4){
            rate4.setImageResource(R.drawable.ic_full_star);
        }if (rate > 4 && rate < 5){
            rate5.setImageResource(R.drawable.ic_half_star);
        }else if (rate == 5){
            rate5.setImageResource(R.drawable.ic_full_star);
        }
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
        String language = preferences.getString("Language", "");
        setLocal(language);
    }

    private String getLocale(){
        Locale locale = Locale.getDefault();
        String language = locale.toString();
        if (language.equals("in")){
            language = "id";
        }else if (language.equals("en")){
            language = "en";
        }
        return language;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_readmore:
                if (this.getResources().getString(R.string.hide) == tvReadmore.getText()) {
                    tvSinopsis.setMaxLines(3);
                    tvReadmore.setText(this.getResources().getString(R.string.read_synopsis));
                } else {
                    tvSinopsis.setMaxLines(Integer.MAX_VALUE);
                    tvReadmore.setText(this.getResources().getString(R.string.hide));
                }
                break;
            case R.id.fab:
                if (!favorite) {
                    setFavorite(menu);
                    Uri hasil = getContentResolver().insert(uri, MyContentProvider.filmToContentValues(filmParcel, type));
                    long result = Long.parseLong(hasil.getPath().replace("/",""));
                    if (result > 0 ) {
                        Toast.makeText(this, getString(R.string.setfavorite), Toast.LENGTH_LONG).show();
                        sendUpdateIntent(getApplicationContext());
                    }
                } else{
                    removeFavorite(menu);
                }
        }
    }

    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void bindFilmParcel(Film film){
        Glide.with(imgItem.getContext()).load("https://image.tmdb.org/t/p/w185" + film.getPosterPath())
                .apply(new RequestOptions().transform(new RoundedCorners(32))).placeholder(R.drawable.load).into(imgItem);
        Glide.with(imgBackground.getContext()).load("https://image.tmdb.org/t/p/w500" + film.getBackdropPath())
                .apply(new RequestOptions().transform(new BrightnessFilterTransformation((float) -0.3)))
                .placeholder(R.drawable.load).into(imgBackground);
        tvJudul.setText(film.getOriginalTitle().toUpperCase());
        if (tvJudul.getLineCount()>3){
            tvJudul.setTextSize(20);
        }else
            tvJudul.setTextSize(28);
        tvGenre.setText(film.getGenre());
        tvRelease.setText(film.getReleaseDate());
        tvRating.setText(String.valueOf(film.getVoteAverage()));
        setRate(film.getVoteAverage()/2);
        if (film.getOverview().trim().isEmpty()){
            tvSinopsis.setText(getResources().getString(R.string.no_overview));
            tvSinopsis.setTextAppearance(this, R.style.ItalicText);
            tvSinopsis.setGravity(View.TEXT_ALIGNMENT_CENTER);
            tvReadmore.setVisibility(View.INVISIBLE);
        }else{
            tvSinopsis.setText(film.getOverview());
            tvReadmore.setVisibility(View.VISIBLE);
        }
        tvPopularity.setText(String.valueOf(film.getPopularity()));
        tvVoteCount.setText(String.valueOf(film.getVoteCount()));
        tvOriLanguage.setText(film.getOriginalLanguange());
        progressBar.setVisibility(View.GONE);
    }

    private void setTitleAppBar(final String title){
        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    showOption(R.id.favorite_menu);
                    collapsingToolbarLayout.setTitle(getResources().getString(R.string.title_detail));
                    tvJudul.setVisibility(View.VISIBLE);
                } else if (isShow) {
                    isShow = false;
                    hideOption(R.id.favorite_menu);
                    collapsingToolbarLayout.setTitle(title.toUpperCase());
                    tvJudul.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initComponent(){
        uri = Uri.withAppendedPath(MyContentProvider.CONTENT_URI, DATABASE_TABLE);
        filmParcel = getIntent().getParcelableExtra(FILM_PARCEL);
        imgItem = findViewById(R.id.img_item);
        imgBackground = findViewById(R.id.background_image);
        rate1 = findViewById(R.id.rate1);
        rate2 = findViewById(R.id.rate2);
        rate3 = findViewById(R.id.rate3);
        rate4 = findViewById(R.id.rate4);
        rate5 = findViewById(R.id.rate5);
        tvJudul = findViewById(R.id.tv_judul);
        tvGenre = findViewById(R.id.tv_genre);
        tvRating = findViewById(R.id.tv_rating);
        tvRelease = findViewById(R.id.tv_date);
        tvSinopsis = findViewById(R.id.tv_sinopsis);
        tvPopularity = findViewById(R.id.tv_popularity);
        tvVoteCount = findViewById(R.id.tv_vote_count);
        tvOriLanguage = findViewById(R.id.tv_ori_language);
        tvReadmore = findViewById(R.id.tv_readmore);
        progressBar = findViewById(R.id.progress_bar);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        checkFavorite();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        collapsingToolbarLayout.setTitle(filmParcel.getOriginalTitle().toUpperCase());
        setTitleAppBar(filmParcel.getOriginalTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));

        tvReadmore.setOnClickListener(this);
        progressBar.setVisibility(View.VISIBLE);

        type = getIntent().getStringExtra(TYPE);
        if (!getIntent().hasExtra(FROM))
            loadFilm(filmParcel.getId(), type);
        else
            bindFilmParcel(filmParcel);
    }

    private void hideOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(false);
    }

    private void showOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(true);
    }

    private void setFavorite(Menu menu){
        menu.getItem(0).setIcon(R.drawable.ic_favorite_24dp);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        favorite = true;
    }

    private void removeFavorite(Menu menu){
        menu.getItem(0).setIcon(R.drawable.ic_favorite_border_black_24dp);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));

        int result = getContentResolver().delete(uri, ID + "= '" +filmParcel.getId()+ "'", null);
        if (result > 0){
            if (getIntent().hasExtra(FROM)){
                Intent intent = new Intent();
                intent.putExtra(EXTRA_POSITION, getIntent().getIntExtra(EXTRA_POSITION, 0));
                setResult(RESULT_REMOVE, intent);
                sendUpdateIntent(getApplicationContext());
                finish();
            }else{
                Toast.makeText(this, getString(R.string.remove), Toast.LENGTH_LONG).show();
            }
        }
        favorite = false;
    }

    private void checkFavorite(){
        favorite = checkFavorite(filmParcel.getId());
        if(favorite){
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        }else{
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
        }
    }

    public boolean checkFavorite(int id){
        Cursor cursor = getContentResolver().query(uri, null, ID + "=" + id, null, null);
        cursor.moveToFirst();
        return cursor.getCount() > 0;
    }

    public static void sendUpdateIntent(Context context){
        Intent i = new Intent(context, MovieCatalogueWidget.class);
        i.setAction(MovieCatalogueWidget.DATABASE_CHANGED);
        context.sendBroadcast(i);
    }
}
