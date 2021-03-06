package hci.tiendapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import hci.tiendapp.R;

public class SettingsActivity extends MyDrawerActivity implements View.OnClickListener {


    //Language variables
    //private TextView txt_hello;
    private Button btn_en, btn_es, btn_sv;
    private Locale myLocale;
    public int fllag = 0;
    private View btn;

    public SettingsActivity() {
        super(R.layout.activity_settings, R.id.settings_layout);
        super.setContext(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //this.txt_hello = (TextView) findViewById(R.id.txt_hello);
        this.btn_en = (Button) findViewById(R.id.btn_en);
        this.btn_es = (Button) findViewById(R.id.btn_es);
        this.btn_sv = (Button) findViewById(R.id.btn_sv);

        this.btn_sv.setOnClickListener(this);
        this.btn_en.setOnClickListener(this);
        this.btn_es.setOnClickListener(this);


        loadLocale();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (myLocale != null){
            newConfig.locale = myLocale;
            Locale.setDefault(myLocale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    public void loadLocale()
    {

        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        changeLang(language);
    }

    public void saveLocale(String lang)
    {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }

    public void changeLang(String lang)
    {
        if (lang.equalsIgnoreCase("")) {
            fllag -= fllag;
            return;
        }
        myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        updateTexts();
    }

    private void updateTexts()
    {



        //Refresh
        if (fllag != 0){
            fllag -= fllag;
            Intent k = new Intent(SettingsActivity.this, HomeActivity.class);
            startActivity(k);
        }

    }

    @Override
    public void onClick(View v) {
        String lang = "en";
        switch (v.getId()) {
            case R.id.btn_en:
                lang = "en";
                fllag = 1;
                break;
            case R.id.btn_es:
                lang = "es";
                fllag = 1;
                break;
            case R.id.btn_sv:
                lang = "sv";
                fllag = 1;
                break;
            default:
                break;
        }
        changeLang(lang);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.categories_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
}
