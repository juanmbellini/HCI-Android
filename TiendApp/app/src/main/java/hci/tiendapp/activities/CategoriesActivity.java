package hci.tiendapp.activities;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import hci.tiendapp.Catalogue;
import hci.tiendapp.R;
import hci.tiendapp.backend.Category;
import hci.tiendapp.constants.Constants;
import hci.tiendapp.util.UtilClass;

public class CategoriesActivity extends MyDrawerActivity {

    String genderOption;        // Used to restore categories if returning to this activity
    ArrayAdapter adapter;
    private TabHost tabHost;


    public CategoriesActivity() {
        super(R.layout.activity_categories, R.id.categories_layout);
        super.setContext(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        adapter = new ArrayAdapter<Category>(this, R.layout.categories_list_item, new ArrayList<Category>());


        // Sets up the fixed tabs
        tabHost = (TabHost) findViewById(R.id.categories_tab_host);
        tabHost.setup();

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {

            }
        });


        TabHost.TabSpec ts1 = tabHost.newTabSpec("Tab1");
        ts1.setIndicator(getResources().getString(R.string.categories_tab_1));
        ts1.setContent(R.id.categories_tab_content_categories);
        tabHost.addTab(ts1);

        TabHost.TabSpec ts2 = tabHost.newTabSpec("Tab2");
        ts2.setIndicator(getResources().getString(R.string.categories_tab_2));
        ts2.setContent(R.id.categories_tab_content_news);
        tabHost.addTab(ts2);

        TabHost.TabSpec ts3 = tabHost.newTabSpec("Tab3");
        ts3.setIndicator(getResources().getString(R.string.categories_tab_3));
        ts3.setContent(R.id.categories_tab_content_sale);
        tabHost.addTab(ts3);




        Intent intent = getIntent();
        String intentOption = intent.getStringExtra(Constants.genderSelection);


        if (intentOption == null) {
            if (savedInstanceState != null) {
                genderOption = savedInstanceState.getString("option");
            }
            else {
                // Shouldn't get here, but in case...
                UtilClass.goHome(CategoriesActivity.this, Constants.noReEstablishInformation);
                return;
            }
        } else {
            GetCategoriesTask asyncTask = new GetCategoriesTask();
            asyncTask.execute(intentOption);
            genderOption = intentOption;
        }


        String title = sectionSelector(genderOption);

        if (title != null) {
            getSupportActionBar().setTitle(title);
        }


        final String sendingOption = genderOption;

        ListView l = (ListView) findViewById(R.id.categories_list);
        l.setAdapter(adapter);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Category selectedCategory = (Category) adapter.getItem(position);

                if (selectedCategory.getId() == -1) {
                    Intent intent = new Intent(CategoriesActivity.this, CatalogueActivity.class);
                    intent.putExtra(Constants.comingFrom,Constants.comingFromCategories);
                    startActivity(intent);

                } else {

                    Intent intent = new Intent(CategoriesActivity.this, SubCategoriesActivity.class);
                    intent.putExtra(Constants.genderSelection, sendingOption);
                    intent.putExtra(Constants.categorySelectionId, selectedCategory.getId() + "");
                    intent.putExtra(Constants.categorySelectionName, selectedCategory.getName());
                    startActivity(intent);
                }


            }
        });


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        genderOption = savedInstanceState.getString("option");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString("option", genderOption);
        super.onSaveInstanceState(outState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.categories_menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            //onBackPressed();
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private String sectionSelector(String id) {

        String title = null;

        switch (id) {
            case Constants.menCategory:
                title = getString(R.string.gender_men);
                break;
            case Constants.womenCategory:
                title = getString(R.string.gender_women);
                break;
            case Constants.kidsCategory:
                title = getString(R.string.gender_kids);
                break;
            case Constants.babiesCategory:
                title = getString(R.string.gender_babies);
                break;
        }

        return title;

    }

    private class GetCategoriesTask extends AsyncTask<String, Long, Collection<Category>> {

        final String baseURL = "http://eiffel.itba.edu.ar/hci/service3/Catalog.groovy?method=GetAllCategories&filters=";


        private String setUp(String sectionId) {

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
                    CategoriesActivity.this.finish();
                    throw new RuntimeException("Algo anduvo mal");
            }

            String encodedString = null;
            try {
                encodedString = URLEncoder.encode(Constants.sectionFilters[id], "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return baseURL + encodedString;

        }

        private String getCategoryJSON(URLConnection urlConnection) {

            String result = null;

            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                JSONObject aux = new JSONObject(UtilClass.readStream(in));
                result = aux.getString("categories");
                if (result == null) {
                    throw new RuntimeException("Wrong Method");
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return result;
        }




        @Override
        protected Collection<Category> doInBackground(String... params) {

            String requestURL = setUp(params[0]);

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

            String result = getCategoryJSON(urlConnection);                // Filters fetched data


            Gson parser = new Gson();
            Type dataSetListType = new TypeToken<List<Category>>() {}.getType();
            List<Category> list = parser.fromJson(result, dataSetListType);
            list.add(0,new Category(-1,
                    CategoriesActivity.this.getString(R.string.all_in ) + " " + sectionSelector(params[0])));
            return list;
        }

        @Override
        protected void onPostExecute(Collection<Category> categories) {
            super.onPostExecute(categories);
            adapter.clear();
            adapter.addAll(categories);
        }


    }



}
