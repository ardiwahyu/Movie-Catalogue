package com.example.appforsubmitmade3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.appforsubmitmade3.alarm.AlarmReceiver;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private int REQUEST_CODE = 100;
    public static String EXTRA_SELECTED_VALUE = "extra_selected_value";
    public static int RESULT_CODE = 110;
    private TextView tvLanguange;
    private SwitchCompat scRelease, scDaily;
    private RelativeLayout rlLanguage, rlDaily, rlReminder;
    private AlarmReceiver alarmReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setTheme(R.style.AppThemeNoActionBar);
        setContentView(R.layout.activity_setting);
        setColorStatusBar(R.color.colorPrimaryDark);

        Toolbar toolbar = findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initComponent();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE){
            if (resultCode == LanguageActivity.RESULT_CODE){
                String language = data.getStringExtra(LanguageActivity.EXTRA_SELECTED_VALUE);
                SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
                editor.putString("Language", language);
                editor.apply();
                recreate();
            }
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
        String language = preferences.getString("Language", "en");
        setLocal(language);
    }

    private void initComponent(){
        alarmReceiver = new AlarmReceiver();
        tvLanguange = findViewById(R.id.tv_choose_lang);
        scDaily = findViewById(R.id.switch_today);
        scRelease = findViewById(R.id.switch_release);
        rlLanguage = findViewById(R.id.rl_language);
        rlDaily = findViewById(R.id.rl_daily);
        rlReminder = findViewById(R.id.rl_reminder);
        scDaily.setOnClickListener(this);
        scRelease.setOnClickListener(this);
        rlLanguage.setOnClickListener(this);
        rlDaily.setOnClickListener(this);
        rlReminder.setOnClickListener(this);
        if(Locale.getDefault().toString().equals("en")){
            tvLanguange.setText(getResources().getString(R.string.english));
        }else if (Locale.getDefault().toString().equals("in")){
            tvLanguange.setText(getResources().getString(R.string.indonesia));
        }
        if(getReleaseReminder().equals("1")){
            scRelease.setChecked(true);
        }else{
            scRelease.setChecked(false);
        }
        if(getDailyReminder().equals("1")){
            scDaily.setChecked(true);
        }else
            scDaily.setChecked(false);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.switch_release || v.getId() == R.id.rl_reminder){
            if(getReleaseReminder().equals("1")) {
                scRelease.setChecked(false);
                setReleaseReminder(0);
            }
            else {
                scRelease.setChecked(true);
                setReleaseReminder(1);
            }
        }else if (v.getId() == R.id.switch_today || v.getId() == R.id.rl_daily){
            if(getDailyReminder().equals("1")){
                scDaily.setChecked(false);
                setDailyReminder(0);
            }else{
                scDaily.setChecked(true);
                setDailyReminder(1);
            }
        }else if (v.getId() == R.id.rl_language){
            Intent intent = new Intent(SettingActivity.this, LanguageActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    public boolean onSupportNavigateUp() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SELECTED_VALUE, Locale.getDefault().toString());
        setResult(RESULT_CODE, resultIntent);
        finish();
        return super.onSupportNavigateUp();
    }

    private void setColorStatusBar(int color){
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), color));
    }

    private void setReleaseReminder(int set){
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("Release", String.valueOf(set));
        editor.apply();

        if(set == 1){
            alarmReceiver.setRepeatingAlarm(this, "08:00", null, null,
                    AlarmReceiver.TYPE_REQUEST, AlarmReceiver.ID_REQUEST);
        }else if (set == 0){
            alarmReceiver.cancelJob(this);
            alarmReceiver.cancelAlarm(this, AlarmReceiver.ID_REQUEST);
        }
    }

    private String getReleaseReminder(){
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        return preferences.getString("Release", "0");
    }

    private void setDailyReminder(int set){
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("Daily", String.valueOf(set));
        editor.apply();

        if(set == 1){
            Log.d("jam", "set");
            alarmReceiver.setRepeatingAlarm(this, "07:00", "Catalogue Movie missing you", "Catalogue Movie",
                    AlarmReceiver.TYPE_REMINDER, AlarmReceiver.ID_REMINDER);
        }else if(set == 0){
            alarmReceiver.cancelAlarm(this, AlarmReceiver.ID_REMINDER);
        }
    }

    private String getDailyReminder(){
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        return preferences.getString("Daily", "0");
    }
}
