package hci.tiendapp.activities;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import hci.tiendapp.Catalogue;
import hci.tiendapp.R;
import hci.tiendapp.TiendApp;
import hci.tiendapp.backend.Product;
import hci.tiendapp.backend.SubCategory;
import hci.tiendapp.background.DrawImageAsyncTask;
import hci.tiendapp.background.ProductsService;
import hci.tiendapp.constants.Constants;
import hci.tiendapp.customviews.HorizontalListView;
import hci.tiendapp.util.UtilClass;

/**
 * Created by JuanMarcos on 20/11/15.
 */
public class CatalogueActivity extends MyDrawerActivity {


    boolean listDisplay = true;
    CatalogueAdapter adapter;

    public CatalogueActivity() {
        super(R.layout.activity_catalogue, R.id.catalogue_layout);
        super.setContext(this);
    }


    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);





    }

    @Override
    protected void onPause() {
        adapter = null; // Saves memory
        super.onPause();
    }

    @Override
    protected void onStart() {
        setDisplay();
        super.onStart();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        listDisplay = savedInstanceState.getBoolean("list_display");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("list_display", listDisplay);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.catalogue_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.list_view_action: {

                listDisplay = true;
                Intent intent = getIntent();
                intent.putExtra("list_display", listDisplay);
                finish();
                startActivity(intent);

                return true;
            }
            case R.id.grid_view_action: {

                listDisplay = false;
                Intent intent = getIntent();
                intent.putExtra("list_display", listDisplay);
                finish();
                startActivity(intent);

                return true;
            }

        }


        return super.onOptionsItemSelected(item);

    }

    private void setDisplay() {

        Intent intent = getIntent();
        listDisplay = intent.getBooleanExtra("list_display", true);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            final ListView l = (ListView) findViewById(R.id.product_list);
            final GridView g = (GridView) findViewById(R.id.product_grid);


            if (listDisplay) {
                g.setVisibility(View.GONE);
                adapter = new CatalogueAdapter(this, R.layout.catalogue_list);
            } else {
                l.setVisibility(View.GONE);
                adapter = new CatalogueAdapter(this, R.layout.catalogue_grid);
            }

            g.setAdapter(adapter);
            l.setAdapter(adapter);
            l.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    ;
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int firstVisibleRow = l.getFirstVisiblePosition();
                    int lastVisibleRow = l.getLastVisiblePosition();

                }
            });

            l.setSaveEnabled(false);
            g.setSaveEnabled(false);

        } else {

            final HorizontalListView l = (HorizontalListView) findViewById(R.id.product_list);
            final GridView g = (GridView) findViewById(R.id.product_grid);

            if (listDisplay) {
                g.setVisibility(View.GONE);
                adapter = new CatalogueAdapter(this, R.layout.catalogue_list);
            } else {
                l.setVisibility(View.GONE);
                adapter = new CatalogueAdapter(this, R.layout.catalogue_grid);
            }

            l.setAdapter(adapter);
            g.setAdapter(adapter);

            l.setSaveEnabled(false);
            g.setSaveEnabled(false);

        }



    }



    private class CatalogueAdapter extends BaseAdapter {



        Context context;
        int layout;
        List<Product> products = new ArrayList<Product>();


        private LayoutInflater inflater = null;

        public CatalogueAdapter(Context context, int layout) {
            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.layout = layout;
            new GetProductsAsyncTask().execute();

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
                new DrawImageAsyncTask(context, image, 70, 70).execute(products.get(position).getImageUrl()[0], position + "");
            }
            name.setText(products.get(position).getName());
            price.setText("$" + products.get(position).getPrice());



            Holder holder = new Holder(image, name, price);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CatalogueActivity.this, ProductActivity.class);
                    Product result = (Product)adapter.getItem(position);
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


        private int getProductsQuantity() {

            URL url = null;
            try {
                url = new URL(baseURL);      // Creates an URL object based on the filter sent
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return -1;
            }

            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();   // Get's data from API
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();                     // Finishes connection
                    System.out.println("Disconnected");
                }

            }

            return Integer.parseInt(getDataFromJSON(urlConnection, "total"));

        }

        private String getProducts(int total) {


            URL url = null;
            try {
                url = new URL(baseURL + "&page_size=" + total);      // Creates an URL object based on the filter sent
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


            return getDataFromJSON(urlConnection, "products");                // Filters fetched data
        }

        private Collection<Product> fromJSONToCollection(String JSON) {

            Gson parser = new Gson();
            Type dataSetListType = new TypeToken<Collection<Product>>() {
            }.getType();
            return (Collection<Product>) parser.fromJson(JSON, dataSetListType);

        }


        @Override
        protected Collection<Product> doInBackground(String... params) {


            Collection<Product> products;

            String result = getProducts(getProductsQuantity());
            if (result != null) {

                products = fromJSONToCollection(result);
            } else {
                products = new HashSet<Product>();
            }
            return products;
        }

        @Override
        protected void onPostExecute(Collection<Product> products) {
            super.onPostExecute(products);
            adapter.clear();
            adapter.addAll(products);
            adapter.notifyDataSetChanged();
        }
    }
}
