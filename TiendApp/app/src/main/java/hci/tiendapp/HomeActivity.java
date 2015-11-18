package hci.tiendapp;

import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.text.DateFormat;
import java.util.Date;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Button;


public class HomeActivity extends AppCompatActivity {

    private String[] mPlanetTitles = {"Home", "Hombres", "Mujeres", "Infantiles", "Bebes"};
    private int[] icons = {R.drawable.ic_home, R.drawable.ic_men_category, R.drawable.ic_women_category, R.drawable.ic_kids_category, R.drawable.ic_babies_category};
    String name = "Juan Marcos Bellini";
    String email = "jbellini@itba.edu.ar";
    int profile = R.drawable.logo2;


    private RecyclerView mDrawerList;
    private CharSequence mTitle;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;


    //Alarm variables
    private AlarmManager alarmManager;
    private PendingIntent alarmNotificationReceiverPendingIntent;
    private final static int INTERVAL = 30000;
    public final static String TAG = "Alarm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        mTitle = "test";
        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);
        mDrawerList = (RecyclerView) findViewById(R.id.left_drawer);
        mDrawerList.setHasFixedSize(true);

        RecyclerView.Adapter mAdapter= new MyAdapter(mPlanetTitles, icons, name, email, profile);

        mDrawerList.setAdapter(mAdapter);//new ArrayAdapter<String>(this, R.layout.navigation_drawer_option, mPlanetTitles));

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
        );

        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.setFitsSystemWindows(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //mDrawerList.addHeaderView(findViewById(R.id.drawer_header));



        //Alarm management
        //TODO Make buttons for the id's
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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
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
        setTitle(mPlanetTitles[position]);
        drawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }



}
