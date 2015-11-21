package hci.tiendapp.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;

import hci.tiendapp.R;
import hci.tiendapp.backend.Product;
import hci.tiendapp.constants.Constants;
import hci.tiendapp.util.UtilClass;

public class ProductActivity extends MyDrawerActivity {


    private Product displayedProduct;

    private TabHost tabHost;




    public ProductActivity() {
        super(R.layout.activity_product, R.id.prod_layout);
        super.setContext(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String prodTitle = intent.getStringExtra(Constants.productName);
        String prodId = intent.getStringExtra(Constants.productId);


        if (prodId == null) {
            UtilClass.goHome(ProductActivity.this, Constants.noReEstablishInformation);
            return;
        }


        new GetProductAsyncTask().execute(prodId);




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


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }


    private void setImages() {



    }

    private void setLayout() {

        getSupportActionBar().setTitle(displayedProduct.getName());
        TextView prodTitle = (TextView) findViewById(R.id.prod_title);
        TextView prodPrice = (TextView) findViewById(R.id.prod_price);
        Spinner spinner = (Spinner) findViewById(R.id.prod_quantity_selection);




        //ImageView imageView;
        //imageView.setOnClickListener();

        prodTitle.setText(displayedProduct.getName());
        prodPrice.setText("$" + displayedProduct.getPrice());

        String[] quantities = new String[20];
        for (int i = 0 ; i < quantities.length; i++) {
            quantities[i] = getResources().getString(R.string.qty_abreviation) +": " + (i + 1);
        }

       // Integer[] quantities = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,R.layout.quantity_spinner_item, quantities);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

    }


    private class GetProductAsyncTask extends AsyncTask<String, Long, Product> {

        private String requestURL = "http://eiffel.itba.edu.ar/hci/service3/Catalog.groovy?method=GetProductById&id=";


        private String getDataFromJSON(URLConnection urlConnection, String property) {

            String result = null;


            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                JSONObject aux = new JSONObject(UtilClass.readStream(in));
                result = aux.getString(property);
                if (result == null) {
                    throw new RuntimeException("Wrong Method");
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return result;
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //TODO agregar loading dialog
        }

        @Override
        protected Product doInBackground(String... params) {


            requestURL += params[0];

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
                if (urlConnection != null) {
                    urlConnection.disconnect();                     // Finishes connection
                    System.out.println("Disconnected");
                }
            }

            return new Gson().fromJson(getDataFromJSON(urlConnection,"product"),Product.class);

        }

        @Override
        protected void onPostExecute(Product product) {
            super.onPostExecute(product);
            // TODO apagar dialog

            if (product == null) {

                // Shouldn't get here, but in case...
                UtilClass.goHome(ProductActivity.this, Constants.noResponseFromGettingProductTitles);
                System.out.println("Algo salio mal");
                return;
            }

            displayedProduct = product;
            setLayout();
        }
    }

}
