package hci.tiendapp.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;


import hci.tiendapp.R;

/**
 * Created by JuanMarcos on 19/11/15.
 */

public class HomeActivity extends MyDrawerActivity implements View.OnClickListener {

    private Button btn_more_men, btn_more_women, btn_more_kids, btn_more_babies, btn_more_new_ins, btn_more_sale;

    public HomeActivity() {
        super(R.layout.activity_home, R.id.home_layout);
        super.setContext(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.btn_more_men = (Button) findViewById(R.id.btn_more_men);
        this.btn_more_women = (Button) findViewById(R.id.btn_more_women);
        this.btn_more_kids = (Button) findViewById(R.id.btn_more_kids);
        this.btn_more_babies = (Button) findViewById(R.id.btn_more_babies);
        this.btn_more_new_ins = (Button) findViewById(R.id.btn_more_new_ins);
        this.btn_more_sale = (Button) findViewById(R.id.btn_more_sale);


        this.btn_more_men.setOnClickListener(this);
        this.btn_more_women.setOnClickListener(this);
        this.btn_more_kids.setOnClickListener(this);
        this.btn_more_babies.setOnClickListener(this);
        this.btn_more_new_ins.setOnClickListener(this);
        this.btn_more_sale.setOnClickListener(this);


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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_more_men:
                //TODO: Show products in men etc.;
                break;
            case R.id.btn_more_women:
                ;
                break;
            case R.id.btn_more_kids:
                ;
                break;
            case R.id.btn_more_babies:
                ;
                break;
            case R.id.btn_more_new_ins:
                ;
                break;
            case R.id.btn_more_sale:
                ;
                break;
            default:
                break;
        }
    }
}
