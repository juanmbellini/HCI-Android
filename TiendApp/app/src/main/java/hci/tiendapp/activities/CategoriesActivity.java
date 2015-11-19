package hci.tiendapp.activities;


import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hci.tiendapp.R;
import hci.tiendapp.constants.Constants;

public class CategoriesActivity extends AppCompatActivity {


    private TabHost tabHost;

/*
    public CategoriesActivity() {
        super(R.layout.activity_categories);
        super.setContext(this);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_categories);





        // Sets up the fixed tabs
        tabHost = (TabHost) findViewById(R.id.categories_tab_host);
        tabHost.setup();

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {

            }
        });


        TabHost.TabSpec ts1 = tabHost.newTabSpec("Tab1");
        ts1.setIndicator(getResources().getString(R.string.tab_1));
        ts1.setContent(R.id.categories_tab_content_categories);
        tabHost.addTab(ts1);

        TabHost.TabSpec ts2 = tabHost.newTabSpec("Tab2");
        ts2.setIndicator(getResources().getString(R.string.tab_2));
        ts2.setContent(R.id.categories_tab_content_news);
        tabHost.addTab(ts2);

        TabHost.TabSpec ts3 = tabHost.newTabSpec("Tab3");
        ts3.setIndicator(getResources().getString(R.string.tab_3));
        ts3.setContent(R.id.categories_tab_content_sale);
        tabHost.addTab(ts3);

        //TODO ver que onda con el drawer



        Intent intent = getIntent();
        String option = intent.getStringExtra(Constants.genderSelection);


    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.categories_menu, menu);
        return true;
    }



}
