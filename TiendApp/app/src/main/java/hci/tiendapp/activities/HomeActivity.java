package hci.tiendapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hci.tiendapp.Catalogue;
import hci.tiendapp.R;
import hci.tiendapp.backend.Filter;
import hci.tiendapp.backend.Product;
import hci.tiendapp.background.DrawImageAsyncTask;
import hci.tiendapp.constants.Constants;
import hci.tiendapp.customviews.HorizontalListView;
import hci.tiendapp.util.UtilClass;

/**
 * Created by JuanMarcos on 19/11/15.
 */

public class HomeActivity extends MyDrawerActivity {


    List<CatalogueAdapter> adapters;
    CatalogueAdapter menAdapter;
    CatalogueAdapter womenAdapter;
    CatalogueAdapter kidsAdapter;
    CatalogueAdapter babiesAdapter;
    CatalogueAdapter newsAdapter;
    CatalogueAdapter salesAdapter;


    public HomeActivity() {
        super(R.layout.activity_home, R.id.home_layout);
        super.setContext(this);
        adapters = new ArrayList<CatalogueAdapter>();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        menAdapter = new CatalogueAdapter(this, R.layout.home_products_lists, Constants.comingFromGender, Constants.menCategory, "0");
        womenAdapter = new CatalogueAdapter(this, R.layout.home_products_lists, Constants.comingFromGender, Constants.womenCategory, "1");
        kidsAdapter = new CatalogueAdapter(this, R.layout.home_products_lists, Constants.comingFromGender, Constants.kidsCategory, "2");
        babiesAdapter = new CatalogueAdapter(this, R.layout.home_products_lists, Constants.comingFromGender, Constants.babiesCategory, "3");
        newsAdapter = new CatalogueAdapter(this, R.layout.home_products_lists, Constants.news,"", "4");
        salesAdapter= new CatalogueAdapter(this, R.layout.home_products_lists, Constants.sales, "", "5");

        adapters.add(menAdapter);
        adapters.add(womenAdapter);
        adapters.add(kidsAdapter);
        adapters.add(babiesAdapter);
        adapters.add(newsAdapter);
        adapters.add(salesAdapter);

        menAdapter.startAsyncTask();
        womenAdapter.startAsyncTask();
        kidsAdapter.startAsyncTask();
        babiesAdapter.startAsyncTask();
        newsAdapter.startAsyncTask();
        salesAdapter.startAsyncTask();

        final HorizontalListView l1 = (HorizontalListView) findViewById(R.id.men_products_home);
        final HorizontalListView l2 = (HorizontalListView) findViewById(R.id.women_products_home);
        final HorizontalListView l3 = (HorizontalListView) findViewById(R.id.kids_products_home);
        final HorizontalListView l4 = (HorizontalListView) findViewById(R.id.babies_products_home);
        final HorizontalListView l5 = (HorizontalListView) findViewById(R.id.news_products_home);
        final HorizontalListView l6 = (HorizontalListView) findViewById(R.id.sales_products_home);

        l1.setAdapter(menAdapter);
        l2.setAdapter(womenAdapter);
        l3.setAdapter(kidsAdapter);
        l4.setAdapter(babiesAdapter);
        l5.setAdapter(newsAdapter);
        l6.setAdapter(salesAdapter);

        l1.setSaveEnabled(true);
        l2.setSaveEnabled(true);
        l3.setSaveEnabled(true);
        l4.setSaveEnabled(true);
        l5.setSaveEnabled(true);
        l6.setSaveEnabled(true);









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



    public class CatalogueAdapter extends BaseAdapter {



        Context context;
        int layout;
        List<Product> products = new ArrayList<Product>();
        String whichOne;
        String gender;
        String adapterNumber;


        private LayoutInflater inflater = null;


        public CatalogueAdapter(Context context, int layout, String whichOne, String gender, String adapterNumber) {

            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.layout = layout;
            this.whichOne = whichOne;
            this.gender = gender;
            this.adapterNumber = adapterNumber;


        }

        public void startAsyncTask() {
            new GetProductsAsyncTask(this).execute(whichOne, adapterNumber, gender);
        }






        public void clear() {
            products = new ArrayList<Product>();
        }

        public boolean addAll(Collection<Product> c) {
            if (!c.isEmpty()) {
                products = new ArrayList<Product>(c);
                return true;
            }
            return false;
        }

        @Override
        public int getCount() {
            return products.size();
        }

        @Override
        public Object getItem(int position) {
            return products.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        public class Holder {
            ImageView image;
            TextView name;
            TextView price;

            public Holder(ImageView image, TextView name, TextView price) {
                this.image = image;
                this.name = name;
                this.price = price;
            }
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View view = inflater.inflate(layout, null);
            ImageView image = (ImageView) view.findViewById(R.id.product_image);
            TextView name = (TextView) view.findViewById(R.id.product_name);
            TextView price = (TextView) view.findViewById(R.id.product_price);


            String[] productImages = products.get(position).getImageUrl();
            if (productImages.length == 0) {
                image.setImageResource(R.drawable.no_picture);
            } else {
                new DrawImageForCatalogueAsyncTask(context, image, 70, 70).execute(products.get(position).getImageUrl()[0]); //, position + "");
            }
            name.setText(products.get(position).getName());
            price.setText("$" + products.get(position).getPrice());



            Holder holder = new Holder(image, name, price);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, ProductActivity.class);
                    Product result = (Product)adapters.get(Integer.parseInt(adapterNumber)).getItem(position);
                    intent.putExtra(Constants.productId, result.getId() + "");
                    intent.putExtra(Constants.productName, result.getName());
                    startActivity(intent);


                }
            });
            return view;
        }
    }


    private class GetProductsAsyncTask extends AsyncTask<String, Long, Collection<Product>> {

        private final String baseURL = "http://eiffel.itba.edu.ar/hci/service3/Catalog.groovy?method=GetAllProducts";

        private CatalogueAdapter adapter;

        public  GetProductsAsyncTask(CatalogueAdapter adapter) {
            this.adapter = adapter;
        }


        private String inputStreamToString(URLConnection urlConnection) {

            String result = "";
            try {
                InputStream in = new BufferedInputStream((urlConnection.getInputStream()));
                result = UtilClass.readStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }


        private String getDataFromJSON(String JSON, String property) {

            String result = "";

            try {
                JSONObject aux = new JSONObject(JSON);
                result = aux.getString(property);
                if (result == null) {
                    throw new RuntimeException("Wrong Method");
                }
            } catch  (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }




        private String getAllProductsWithGenderFilter(int total, String genderSelection){

            String requestURL = baseURL + "&page_size=" + total + "&filters=";
            String filters = "";
            filters = Constants.sectionFilters[UtilClass.getSectionFilterId(HomeActivity.this, genderSelection)];
            try {
                filters = URLEncoder.encode(filters, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return fetchDesiredProducts(requestURL + filters);
        }

        private String getAllProductsWithNewsFilter(int total){

            String requestURL = baseURL + "&page_size=" + total + "&filters=";
            String filters = Constants.newsFilter;

            try {
                filters = URLEncoder.encode(filters, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return fetchDesiredProducts(requestURL + filters);
        }


        private String getAllProductsWithSalesFilter(int total){

            String requestURL = baseURL + "&page_size=" + total + "&filters=";
            String filters = Constants.salesFilter;

            try {
                filters = URLEncoder.encode(filters, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return fetchDesiredProducts(requestURL + filters);
        }



        private String fetchDesiredProducts(String requestURL) {


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

            String response = inputStreamToString(urlConnection);

            return getDataFromJSON(response, "products");                // Filters fetched data


        }




        private String getProducts (String comingFrom, String gender) {


            String result = "";
            switch (comingFrom) {

                case Constants.comingFromGender:
                    result = getAllProductsWithGenderFilter(6, gender);
                    break;
                case Constants.news:
                    result = getAllProductsWithNewsFilter(6);

                    break;
                case Constants.sales:
                    result = getAllProductsWithSalesFilter(6);
                    break;


            }
            return result;
        }



        private Collection<Product> fromJSONToCollection(String JSON) {

            Gson parser = new Gson();
            Type dataSetListType = new TypeToken<Collection<Product>>() {
            }.getType();
            return (Collection<Product>) parser.fromJson(JSON, dataSetListType);

        }


        @Override
        protected Collection<Product> doInBackground(String... params) {

            System.out.println("Backgroud");

            Collection<Product> products;

            String whichOne = params[0];
            int adapter = Integer.parseInt(params[1]);
            String genderSelection = params[2];

            String result = getProducts(whichOne, genderSelection);
            if (result != null) {

                products = fromJSONToCollection(result);
            } else {
                products = new HashSet<Product>();
            }
            if (products == null || products.isEmpty() || adapters.get(adapter) == null) {
                return null;
            }
            adapters.get(adapter).clear();
            adapters.get(adapter).addAll(products);

            return products;
        }

        @Override
        protected void onPostExecute(Collection<Product> products) {
            super.onPostExecute(products);
            adapter.notifyDataSetChanged();

        }
    }

    private class DrawImageForCatalogueAsyncTask extends DrawImageAsyncTask {

        ImageView imageView;

        public DrawImageForCatalogueAsyncTask(Context context, ImageView imageView, int width, int height) {
            super(context, width, height);
            this.imageView = imageView;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            System.out.println("Imanges");

            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.drawable.no_picture);
            }
            for (CatalogueAdapter each : adapters) {
                //each.notifyDataSetChanged();
            }

        }


    }
}
