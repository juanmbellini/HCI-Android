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
public class HomeActivity extends MyDrawerActivity {



    public HomeActivity() {
        super(R.layout.activity_home, R.id.home_layout);
        super.setContext(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            //Intent k = new Intent(HomeActivity.this, ProductActivity.class);
            //startActivity(k);


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
}
