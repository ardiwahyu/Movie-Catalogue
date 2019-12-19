package com.example.appforsubmitmade3;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class LanguageActivity extends AppCompatActivity {
    RadioGroup rgLanguage;
    Button btnApply;

    public static String EXTRA_SELECTED_VALUE = "extra_selected_value";
    public static int RESULT_CODE = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(getResources().getString(R.string.choose_languange));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        rgLanguage = findViewById(R.id.rg_languange);
        btnApply = findViewById(R.id.btn_apply);
        checkLocal();
        rgLanguage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                btnApply.setVisibility(View.VISIBLE);
            }
        });
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String language;
                switch (rgLanguage.getCheckedRadioButtonId()){
                    case R.id.rb_english:
                        language = "en";
                        break;
                    case R.id.rb_indonesia:
                    default:
                        language = "id";
                        break;
                }
                Intent resultIntent = new Intent();
                resultIntent.putExtra(EXTRA_SELECTED_VALUE, language);
                setResult(RESULT_CODE, resultIntent);
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void checkLocal(){
        final Configuration configuration = getResources().getConfiguration();
        final Locale locale = configuration.locale;
        if (locale.getDisplayLanguage().equalsIgnoreCase("Indonesia")){
            rgLanguage.check(R.id.rb_indonesia);
        }else {
            rgLanguage.check(R.id.rb_english);
        }
    }
}
