package hci.tiendapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;

import hci.tiendapp.R;
import hci.tiendapp.backend.Category;
import hci.tiendapp.backend.SubCategory;
import hci.tiendapp.constants.Constants;
import hci.tiendapp.util.UtilClass;

/**
 * Created by JuanMarcos on 19/11/15.
 */
public class SubCategoriesActivity extends MyDrawerActivity {

    String genderOption;        // Used to restore activity state if returning to this activity
    String categoryId;          // Used to restore activity state if returning to this activity
    String categotyName;        // Used to restore activity state if returning to this activity

    ArrayAdapter adapter;
    TabHost tabHost;


    public SubCategoriesActivity() {

        super(R.layout.activity_sub_category, R.id.sub_categories_layout);
        super.setContext(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayAdapter<Category>(this, R.layout.categories_list_item, new ArrayList<Category>());

        // Sets up the fixed tabs
        tabHost = (TabHost) findViewById(R.id.sub_categories_tab_host);
        tabHost.setup();

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {

            }
        });


        TabHost.TabSpec ts1 = tabHost.newTabSpec("Tab1");
        ts1.setIndicator(getResources().getString(R.string.sub_categories_tab_1));
        ts1.setContent(R.id.sub_categories_tab_content_sub_categories);
        tabHost.addTab(ts1);

        TabHost.TabSpec ts2 = tabHost.newTabSpec("Tab2");
        ts2.setIndicator(getResources().getString(R.string.sub_categories_tab_2));
        ts2.setContent(R.id.sub_categories_tab_content_news);
        tabHost.addTab(ts2);

        TabHost.TabSpec ts3 = tabHost.newTabSpec("Tab3");
        ts3.setIndicator(getResources().getString(R.string.sub_categories_tab_3));
        ts3.setContent(R.id.sub_categories_tab_content_sale);
        tabHost.addTab(ts3);




        Intent intent = getIntent();
        String intentOption = intent.getStringExtra(Constants.genderSelection);
        String intentCategoryId = intent.getStringExtra(Constants.categorySelectionId);
        String intentCategoryName = intent.getStringExtra(Constants.categorySelectionName);


        if (intentOption == null || intentCategoryName == null || intentCategoryId == null) {
            if (savedInstanceState != null) {
                genderOption = savedInstanceState.getString("option");
                categoryId = savedInstanceState.getString("categoryId");
                categotyName = savedInstanceState.getString("categoryName");
            }
            else {
                // Shouldn't get here, but in case...
                UtilClass.goHome(SubCategoriesActivity.this, Constants.noReEstablishInformation);
                return;

            }
        } else {
            GetCategoriesTask asyncTask = new GetCategoriesTask();
            asyncTask.execute(intentOption, intentCategoryId);
            genderOption = intentOption;

        }

        String title = null;

        switch (genderOption) {
            case Constants.menCategory:
                title = "Hombres";
                break;
            case Constants.womenCategory:
                title = "Mujeres";
                break;
            case Constants.kidsCategory:
                title = "Infantiles";
                break;
            case Constants.babiesCategory:
                title = "Bebes";
                break;

        }
        if (title != null) {
            getSupportActionBar().setTitle(title);
        }

        ((TextView)findViewById(R.id.sub_categories_list_title)).setText(intent.getStringExtra(Constants.categorySelectionName));


        ListView l = (ListView) findViewById(R.id.categories_list);
        l.setAdapter(adapter);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SubCategoriesActivity.this, CatalogueActivity.class);
                startActivity(intent);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.categories_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }


    private class GetCategoriesTask extends AsyncTask<String, Long, Collection<SubCategory>> {

        final String baseURL = "http://eiffel.itba.edu.ar/hci/service3/Catalog.groovy?method=GetAllSubcategories&id=";


        private String setUp(String sectionId, String categoryId) {


            int id = 0;
            switch (sectionId) {

                case Constants.menCategory:
                    id = 0;
                    break;
                case Constants.womenCategory:
                    id = 1;
                    break;
                case Constants.kidsCategory:
                    id = 2;
                    break;
                case Constants.babiesCategory:
                    id = 3;
                    break;
                default:
                    SubCategoriesActivity.this.finish();
                    throw new RuntimeException("Algo anduvo mal");
            }

            String encodedString = null;
            try {
                encodedString = URLEncoder.encode(Constants.sectionFilters[id], "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return baseURL + categoryId + "&filters=" + encodedString;
        }


        private String getSubCategoryJSON(URLConnection urlConnection) {

            String result = null;

            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                JSONObject aux = new JSONObject(UtilClass.readStream(in));
                result = aux.getString("subcategories");
                if (result == null) {
                    throw new RuntimeException("Wrong Method");
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return result;
        }


        @Override
        protected Collection<SubCategory> doInBackground(String... params) {

            String requestURL = setUp(params[0], params[1]);

            System.out.println(requestURL);

            URL url = null;
            try {
                url = new URL(requestURL);      // Creates an URL object based on the filter sent
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();   // Get's data from API
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if(urlConnection != null) {
                    urlConnection.disconnect();                     // Finishes connection
                }
                System.out.println("Disconnected");
            }

            String result = getSubCategoryJSON(urlConnection);                // Filters fetched data


            Gson parser = new Gson();
            Type dataSetListType = new TypeToken<Collection<SubCategory>>() {}.getType();
            return parser.fromJson(result, dataSetListType);
        }

        @Override
        protected void onPostExecute(Collection<SubCategory> subCategories) {
            super.onPostExecute(subCategories);
            adapter.clear();
            adapter.addAll(subCategories);
        }
    }


}
