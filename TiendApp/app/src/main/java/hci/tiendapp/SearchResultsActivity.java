package hci.tiendapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;

import hci.tiendapp.R;
import hci.tiendapp.activities.CatalogueActivity;
import hci.tiendapp.constants.Constants;

public class SearchResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.tool_bar_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Intent intentForCatalogueActivity = new Intent(this, CatalogueActivity.class);
            intentForCatalogueActivity.putExtra(Constants.comingFrom, Constants.comingFromSearchBar);
            intentForCatalogueActivity.putExtra(Constants.searchQuery, query);
            startActivity(intentForCatalogueActivity);
            onBackPressed();

        }
    }
}
