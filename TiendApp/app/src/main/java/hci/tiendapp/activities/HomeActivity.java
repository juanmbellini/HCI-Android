package hci.tiendapp.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;


import hci.tiendapp.R;
import hci.tiendapp.background.ProductsService;

/**
 * Created by JuanMarcos on 19/11/15.
 */
public class HomeActivity extends MyDrawerActivity implements View.OnClickListener {


    //Language variables
    private TextView txt_hello;
    private Button btn_en, btn_es;
    private Locale myLocale;
    public int fllag = 0;
    private View btn;

    public HomeActivity() {
        super(R.layout.activity_home, R.id.home_layout);
        super.setContext(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    //    Intent k = new Intent(HomeActivity.this, ProductActivity.class);
    //    startActivity(k);

        this.txt_hello = (TextView) findViewById(R.id.txt_hello);
        this.btn_en = (Button) findViewById(R.id.btn_en);
        this.btn_es = (Button) findViewById(R.id.btn_es);


        this.btn_en.setOnClickListener(this);
        this.btn_es.setOnClickListener(this);

        loadLocale();

        ProductsService service = new ProductsService();
        this.startService(new Intent(this, ProductsService.class));


        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(HomeActivity.this, ProductsService.AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(HomeActivity.this, 0 , intent, PendingIntent.FLAG_ONE_SHOT);
       // alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 5000, 5000, pendingIntent);
        System.out.println("Alarm set");




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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView =
                (SearchView) menu.findItem(R.id.home_tool_bar_search_bar).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
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

        txt_hello.setText(R.string.hello_world);
        btn_en.setText(R.string.btn_en);
        btn_es.setText(R.string.btn_es);

        //Refresh
        if (fllag != 0){
            fllag -= fllag;
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View v) {
        String lang = "es";
        switch (v.getId()) {
            case R.id.btn_en:
                lang = "en";
                fllag = 1;
                break;
            case R.id.btn_es:
                lang = "es";
                fllag = 1;
                break;
            default:
                break;
        }
        changeLang(lang);
    }
}
