package hci.tiendapp.activities;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import hci.tiendapp.R;

/**
 * Abstract Class representing an Activity with the Navigation Drawer
 * It provides all the set up of the drawer.
 *
 * Created by JuanMarcos on 18/11/15.
 */
public abstract class MyDrawerActivity extends AppCompatActivity{

    private RecyclerView drawerList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private int layoutId;
    private Activity childActivity;

    public MyDrawerActivity(int layoutId) {
        this.layoutId = layoutId;
    }

    public void setContext(Activity childActivity) {
        this.childActivity = childActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(layoutId);

        // Sets up the action bar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Sets up the navigation drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);
        drawerList = (RecyclerView) findViewById(R.id.left_drawer);
        RecyclerView.Adapter drawerAdapter = new MyAdapter(childActivity);
        drawerList.setAdapter(drawerAdapter);
        drawerList.setLayoutManager(new LinearLayoutManager(childActivity));

        drawerToggle = new ActionBarDrawerToggle(childActivity, drawerLayout, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.setFitsSystemWindows(true);
        drawerToggle.setDrawerIndicatorEnabled(childActivity.getClass() == HomeActivity.class);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //TODO Agregar funcionalidad
            // Do something
        }
    }
}
