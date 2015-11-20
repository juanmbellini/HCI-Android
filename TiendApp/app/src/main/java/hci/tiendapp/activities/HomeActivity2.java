package hci.tiendapp.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.app.SearchManager;
import android.support.v7.widget.SearchView;


import java.util.Locale;

import hci.tiendapp.R;

import static android.widget.Toast.LENGTH_LONG;

public class HomeActivity2 extends AppCompatActivity implements View.OnClickListener {



    private Toolbar toolbar;


    private RecyclerView mDrawerList;
    private CharSequence mTitle;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;


    //Alarm variables
    private AlarmManager alarmManager;
    private PendingIntent alarmNotificationReceiverPendingIntent;
    private final static int INTERVAL = 30000;
    public final static String TAG = "Alarm";

    //Broadcast variable
    private BroadcastReceiver broadcastReceiver;

    //Language variables
    private TextView txt_hello;
    private Button btn_en, btn_es;
    private Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        RelativeLayout r = (RelativeLayout) findViewById(R.id.home_layout);
        setContentView(R.layout.navigation_drawer);


            this.txt_hello = (TextView)findViewById(R.id.txt_hello);
            this.btn_en = (Button)findViewById(R.id.btn_en);
            this.btn_es = (Button)findViewById(R.id.btn_es);


            this.btn_en.setOnClickListener(this);
            this.btn_es.setOnClickListener(this);

            loadLocale();


/*
        toolbar = (Toolbar) findViewById(R.id.tool_bar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);*/
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mTitle = "test";
        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);
        mDrawerList = (RecyclerView) findViewById(R.id.left_drawer);
        mDrawerList.setHasFixedSize(true);

        RecyclerView.Adapter mAdapter= new MyDrawerActivity.MyAdapter(this);

        mDrawerList.setAdapter(mAdapter);






        mDrawerList.setLayoutManager(new LinearLayoutManager(this));


        /*mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });*/

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close
        ) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mDrawerList.setFocusable(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mDrawerList.setFocusable(false);
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.setFitsSystemWindows(true);

        drawerToggle.syncState();

        drawerLayout.addView(r,0);






/*
        if (r == null) {
            Toast.makeText(this, "Null", LENGTH_LONG).show();
        } else {


        }*/
        //mDrawerList.addHeaderView(findViewById(R.id.drawer_header));



        //Alarm management

        /*
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent alarmNotificationReceiverIntent =
                new Intent(HomeActivity.this, AlarmNotificationReceiver.class);
        alarmNotificationReceiverPendingIntent =
                PendingIntent.getBroadcast(HomeActivity.this, 0, alarmNotificationReceiverIntent, 0);

        final Button setSingleAlarmButton = (Button) findViewById(R.id.set_single_alarm);
        setSingleAlarmButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + INTERVAL,
                        alarmNotificationReceiverPendingIntent);

                Log.d(TAG, "Single alarm set on:" +  DateFormat.getDateTimeInstance().format(new Date()));
                Toast.makeText(HomeActivity.this, "Single alarm set", Toast.LENGTH_LONG)
                        .show();
            }
        });

        final Button setRepeatingAlarmButton = (Button) findViewById(R.id.set_repeating_alarm);
        setRepeatingAlarmButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                        SystemClock.elapsedRealtime() + INTERVAL,
                        INTERVAL,
                        alarmNotificationReceiverPendingIntent);

                Log.d(TAG, "Repating alarm set on:" +  DateFormat.getDateTimeInstance().format(new Date()));
                Toast.makeText(HomeActivity.this, "Repeating alarm set", Toast.LENGTH_LONG)
                        .show();
            }
        });

        final Button setInexactRepeatingAlarmButton = (Button) findViewById(R.id.set_inexact_repeating_alarm);
        setInexactRepeatingAlarmButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                        SystemClock.elapsedRealtime() + INTERVAL,
                        AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                        alarmNotificationReceiverPendingIntent);

                Log.d(TAG, "Inexact repeating alarm set on:" +  DateFormat.getDateTimeInstance().format(new Date()));
                Toast.makeText(HomeActivity.this, "Inexact repeating alarm set", Toast.LENGTH_LONG)
                        .show();
            }
        });

        final Button cancelRepeatingAlarmButton = (Button) findViewById(R.id.cancel_repeating_alarm);
        cancelRepeatingAlarmButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmManager.cancel(alarmNotificationReceiverPendingIntent);

                Toast.makeText(HomeActivity.this, "Repeating alarm cancelled", Toast.LENGTH_LONG)
                        .show();
            }
        });
*/
        broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(Intent.ACTION_BATTERY_LOW)) {
                    Toast.makeText(HomeActivity2.this, "Battery low", Toast.LENGTH_LONG).show();
                    }
                if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)){
                    Toast.makeText(HomeActivity2.this, "Battery not charging", Toast.LENGTH_LONG).show();
                }
                if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)){
                    Toast.makeText(HomeActivity2.this, "Battery charging", Toast.LENGTH_LONG).show();
                }

            };
        };
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



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);

        if (myLocale != null){
            newConfig.locale = myLocale;
            Locale.setDefault(myLocale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app navigationDrawerOptionIcon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
        Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show();

        // Highlight the selected item, update the title, and close the drawer
        //mDrawerList.setItemChecked(position, true);
        //setTitle(mPlanetTitles[position]);
        drawerLayout.closeDrawer(mDrawerList);
    }



    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    //TODO ver esto que onda
    /*
    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(broadcastReceiver);

    }*/

    // TODO ver si con esto funciona el custom tool bar
    private class ToolBarItemClickListener implements android.support.v7.widget.Toolbar.OnMenuItemClickListener {


        @Override
        public boolean onMenuItemClick(MenuItem item) {
            return false;
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
        if (lang.equalsIgnoreCase(""))
            return;
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
    }

    @Override
    public void onClick(View v) {
        String lang = "es";
        switch (v.getId()) {
            case R.id.btn_en:
                lang = "en";
                break;
            case R.id.btn_es:
                lang = "es";
                break;
            default:
                break;
        }
        changeLang(lang);
    }
}
