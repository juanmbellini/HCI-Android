package hci.tiendapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
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

import hci.tiendapp.R;
import hci.tiendapp.backend.Product;
import hci.tiendapp.background.DrawImageAsyncTask;
import hci.tiendapp.constants.Constants;
import hci.tiendapp.util.UtilClass;

public class ProductActivity extends MyDrawerActivity {


    private Product displayedProduct;
    private int quantitySelected = 0;


    private ProgressDialog progressDialog;


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


        progressDialog = new ProgressDialog(ProductActivity.this);
        progressDialog.setMessage(getString(R.string.loading_message));
        progressDialog.show();
        new GetProductAsyncTask().execute(prodId);







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_menu, menu);
        return true;
    }


    @Override
    protected void onPause() {

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {



        } else {


        }


        super.onPause();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }



    private void setVerticalLayout() {


        // Sets up the fixed tabs
        TabHost tabHost = (TabHost) findViewById(R.id.products_tab_host);
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

        // Sets title
        TextView prodTitle = (TextView) findViewById(R.id.prod_title);
        prodTitle.setText(displayedProduct.getName());

        // Sets buying controls
        RelativeLayout generalBuyingControls = (RelativeLayout) findViewById(R.id.general_buying_controls);
        RelativeLayout detailsBuyingControls = (RelativeLayout) findViewById(R.id.details_buying_controls);
        TextView generalProdPrice = (TextView) generalBuyingControls.findViewById(R.id.prod_price);
        TextView detailsProdPrice = (TextView) detailsBuyingControls.findViewById(R.id.prod_price);
        final Spinner generalSpinner = (Spinner) generalBuyingControls.findViewById(R.id.prod_quantity_selection);
        final Spinner detailsSpinner = (Spinner) detailsBuyingControls.findViewById(R.id.prod_quantity_selection);

        final AdapterView.OnItemSelectedListener spinnerOnItemSelectedListener =
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {

                        quantitySelected = position + 1;
                        generalSpinner.setSelection(position);
                        detailsSpinner.setSelection(position);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                };


        final String[] quantities = new String[20];
        for (int i = 0 ; i < quantities.length; i++) {
            quantities[i] = getResources().getString(R.string.qty_abreviation) + ": " + (i + 1);
        }
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,R.layout.quantity_spinner_item, quantities);
        spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner);

        generalSpinner.setAdapter(spinnerAdapter);
        detailsSpinner.setAdapter(spinnerAdapter);

        generalSpinner.setSelection(0);
        detailsSpinner.setSelection(0);

        generalSpinner.setOnItemSelectedListener(spinnerOnItemSelectedListener);
        detailsSpinner.setOnItemSelectedListener(spinnerOnItemSelectedListener);

        generalProdPrice.setText("$" + displayedProduct.getPrice());
        detailsProdPrice.setText("$" + displayedProduct.getPrice());


        //Sets Images
        SliderLayout sliderLayout = (SliderLayout) findViewById(R.id.prod_images_slider);
        PagerIndicator sliderPagerIndicator = (PagerIndicator) findViewById(R.id.custom_indicator);
        sliderLayout.stopAutoCycle();
        sliderLayout.setCustomIndicator(sliderPagerIndicator);


        if(displayedProduct.getImageUrl().length == 0) {
            sliderLayout.setVisibility(View.GONE);
            sliderPagerIndicator.setVisibility(View.GONE);
            findViewById(R.id.prod_no_picture).setVisibility(View.VISIBLE);
        } else {
            for (String each : displayedProduct.getImageUrl()) {
                new DrawImageAsyncTaskForSlider(this,sliderLayout,1,1).execute(each);

            }
        }


    }

    private void setHorizontalLayout() {

        // Sets title
        TextView prodTitle = (TextView) findViewById(R.id.prod_title);
        prodTitle.setText(displayedProduct.getName());

        // Sets buying controls
        RelativeLayout upperBuyingControls = (RelativeLayout) findViewById(R.id.upper_buying_controls);
        RelativeLayout lowerBuyingControls = (RelativeLayout) findViewById(R.id.lower_buying_controls);
        TextView generalProdPrice = (TextView) upperBuyingControls.findViewById(R.id.prod_price);
        TextView detailsProdPrice = (TextView) lowerBuyingControls.findViewById(R.id.prod_price);
        final Spinner upperSpinner = (Spinner) upperBuyingControls.findViewById(R.id.prod_quantity_selection);
        final Spinner lowerSpinner = (Spinner) lowerBuyingControls.findViewById(R.id.prod_quantity_selection);

        final AdapterView.OnItemSelectedListener spinnerOnItemSelectedListener =
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {

                        quantitySelected = position + 1;
                        upperSpinner.setSelection(position);
                        lowerSpinner.setSelection(position);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                };


        final String[] quantities = new String[20];
        for (int i = 0 ; i < quantities.length; i++) {
            quantities[i] = getResources().getString(R.string.qty_abreviation) + ": " + (i + 1);
        }
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,R.layout.quantity_spinner_item, quantities);
        spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner);

        upperSpinner.setAdapter(spinnerAdapter);
        lowerSpinner.setAdapter(spinnerAdapter);

        upperSpinner.setSelection(0);
        lowerSpinner.setSelection(0);

        upperSpinner.setOnItemSelectedListener(spinnerOnItemSelectedListener);
        lowerSpinner.setOnItemSelectedListener(spinnerOnItemSelectedListener);

        generalProdPrice.setText("$" + displayedProduct.getPrice());
        detailsProdPrice.setText("$" + displayedProduct.getPrice());


        // Set Images

        LinearLayout picHolder = (LinearLayout) findViewById(R.id.prod_pictures_holder);
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams picParams = new LinearLayout.LayoutParams(size,size);
        size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10,getResources().getDisplayMetrics());
        picParams.setMargins(0,size,0,0);


        if (displayedProduct.getImageUrl().length == 0) {

            ImageView noPictureImage = new ImageView(this);
            noPictureImage.setImageResource(R.drawable.no_picture);
            noPictureImage.setLayoutParams(picParams);
            picHolder.addView(noPictureImage);
        } else {

            for (String each : displayedProduct.getImageUrl()) {
                ImageView image = new ImageView(this);
                new DrawImageAsyncTaskForLandScapeMode(this, 70, 70, image, picHolder, picParams).execute(each);

            }
        }

    }




    private void setLayout() {

        getSupportActionBar().setTitle(displayedProduct.getName());

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            setVerticalLayout();

        } else {

            setHorizontalLayout();
        }
        progressDialog.dismiss();


    }


    private class MySpinnerCustomAdapter extends ArrayAdapter<String> {


        String[] elements;

        public MySpinnerCustomAdapter(Context context, int txtViewResource, String[] elements) {

            super(context, txtViewResource);
            this.elements = elements;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return getCustomView(position, convertView, parent);
        }


        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View spinner = inflater.inflate(R.layout.custom_spinner, parent, false);
            TextView text = (TextView) spinner.findViewById(R.id.custom_spinner_text);

            text.setText(elements[position]);


            return spinner;
        }
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

    private class DrawImageAsyncTaskForSlider extends DrawImageAsyncTask {

        SliderLayout sliderLayout;


        public DrawImageAsyncTaskForSlider(Context context, SliderLayout sliderLayout, int width, int height) {
            super(context, width, height);
            this.sliderLayout = sliderLayout;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if (bitmap != null) {

                sliderLayout.addSlider((new DefaultSliderView(ProductActivity.this)).image(imageURL));
            }


        }
    }

    private class DrawImageAsyncTaskForLandScapeMode extends DrawImageAsyncTask {

        ImageView imageView;
        LinearLayout picHolder;
        LinearLayout.LayoutParams picParams;


        public DrawImageAsyncTaskForLandScapeMode(Context context, int width, int height,
                                                  ImageView imageView, LinearLayout picHolder,
                                                  LinearLayout.LayoutParams picParams) {
            super(context, width, height);
            this.imageView = imageView;
            this.picHolder = picHolder;
            this.picParams = picParams;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if (bitmap != null) {

                imageView.setImageBitmap(bitmap);
                imageView.setLayoutParams(picParams);
                picHolder.addView(imageView);


            }
        }
    }

}
