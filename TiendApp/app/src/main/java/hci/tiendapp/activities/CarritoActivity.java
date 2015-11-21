package hci.tiendapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;

import hci.tiendapp.R;

public class CarritoActivity extends MyDrawerActivity {


    public CarritoActivity() {
        super(R.layout.activity_categories, R.id.categories_layout);
        super.setContext(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //TODO:Change menu title for every screen
        //menu.clearHeader ();
        //menu.setHeaderTitle("Share Menu.");


    }
}
