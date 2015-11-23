package hci.tiendapp.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;


import hci.tiendapp.R;

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

           // Intent k = new Intent(HomeActivity.this, CarritoActivity.class);
           // startActivity(k);




    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu);


        return true;
    }
}
