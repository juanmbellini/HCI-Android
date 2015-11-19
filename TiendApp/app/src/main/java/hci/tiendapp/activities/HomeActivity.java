package hci.tiendapp.activities;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.widget.SearchView;
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
