package hci.tiendapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.NumberPicker;
import android.widget.TabHost;

import hci.tiendapp.R;
import hci.tiendapp.constants.Constants;

public class ProductActivity extends MyDrawerActivity {


    private TabHost tabHost;


    public ProductActivity() {
        super(R.layout.activity_product, R.id.prod_layout);
        super.setContext(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);



        // Sets up the fixed tabs
        tabHost = (TabHost) findViewById(R.id.products_tab_host);
        tabHost.setup();

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {

            }
        });


        TabHost.TabSpec ts1 = tabHost.newTabSpec("Tab1");
        ts1.setIndicator(getResources().getString(R.string.tab_1_pro));
        ts1.setContent(R.id.products_tab_content_general);
        tabHost.addTab(ts1);

        TabHost.TabSpec ts2 = tabHost.newTabSpec("Tab2");
        ts2.setIndicator(getResources().getString(R.string.tab_2_pro));
        ts2.setContent(R.id.products_tab_content_details);
        tabHost.addTab(ts2);

        Intent intent = getIntent();
        String option = intent.getStringExtra(Constants.genderSelection);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

}
