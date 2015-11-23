package hci.tiendapp.activities;

import android.app.Dialog;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
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
import hci.tiendapp.customviews.FiltersDialogBox;
import hci.tiendapp.customviews.HorizontalListView;
import hci.tiendapp.util.UtilClass;

/**
 * Created by JuanMarcos on 20/11/15.
 */
public class CatalogueActivity extends MyDrawerActivity {


    private boolean listDisplay = true;
    private CatalogueAdapter adapter;
    private String requestUrl = "";

    private Set<Filter> filtersAvailable = new HashSet<Filter>();
    private Set<Filter> filtersApplied = new HashSet<Filter>();

    public CatalogueActivity() {
        super(R.layout.activity_catalogue, R.id.catalogue_layout);
        super.setContext(this);
    }


    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

    }

    @Override
    protected void onResume() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);


        String aux = getIntent().getStringExtra(Constants.comingFrom);

        if (aux == null || aux.equals("")) {

            // If got here by anywhere (i.e. by pressing back), it shows the last search
            requestUrl = preferences.getString(Constants.restoreCatalogueRequest, "");

        }



        setDisplay();
        super.onResume();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        listDisplay = savedInstanceState.getBoolean("list_display");
        requestUrl = savedInstanceState.getString(Constants.restoreCatalogueRequest); // TODO hacer verificacion al inicio

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("list_display", listDisplay);
        outState.putString(Constants.restoreCatalogueRequest, requestUrl);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.catalogue_menu, menu);
        super.onCreateOptionsMenu(menu);
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

            case R.id.filter_action: {

                Intent intent = getIntent();
                String comingFrom = intent.getStringExtra(Constants.comingFrom);
                String gender = intent.getStringExtra(Constants.genderSelection);
                String category = intent.getStringExtra(Constants.categorySelectionName);
                String subCategory = intent.getStringExtra(Constants.subCategorySelectionId);

                Dialog filterDialog = new FiltersDialogBox(this, comingFrom, gender,
                        category, subCategory, filtersAvailable, filtersApplied);
                filterDialog.show();

            }

        }


        return super.onOptionsItemSelected(item);

    }


    private void setDisplay() {

        Intent intent = getIntent();
        listDisplay = intent.getBooleanExtra("list_display", true);
        String comingFrom = intent.getStringExtra(Constants.comingFrom);
        String query = "";
        String gender = "";
        String category = "";
        String subCategory = "";

        // Filter filter

        if (requestUrl.equals("")) {


            if (comingFrom == null || comingFrom == "") {
                comingFrom = Constants.comingFromNoWhere;
            } else {

                switch (comingFrom) {

                    case Constants.comingFromSearchBar:
                        query = intent.getStringExtra(Constants.searchQuery);
                        break;
                    case Constants.comingFromGender:
                        gender = intent.getStringExtra(Constants.genderSelection);
                        break;
                    case Constants.comingFromCategories:
                        gender = intent.getStringExtra(Constants.genderSelection);
                        category = intent.getStringExtra(Constants.categorySelectionId);
                        break;
                    case Constants.comingFromSubCategories:
                        gender = intent.getStringExtra(Constants.genderSelection);
                        category = intent.getStringExtra(Constants.categorySelectionName);
                        subCategory = intent.getStringExtra(Constants.subCategorySelectionId);
                }
            }
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            final ListView l = (ListView) findViewById(R.id.product_list);
            final GridView g = (GridView) findViewById(R.id.product_grid);


            if (listDisplay) {
                g.setVisibility(View.GONE);
                adapter = new CatalogueAdapter(this, R.layout.catalogue_list,
                        comingFrom, query, gender, category, subCategory, "", requestUrl);
            } else {
                l.setVisibility(View.GONE);
                adapter = new CatalogueAdapter(this, R.layout.catalogue_grid,
                        comingFrom, query, gender, category, subCategory, "", requestUrl);
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
                adapter = new CatalogueAdapter(this, R.layout.catalogue_list,
                        comingFrom, query, gender, category, subCategory, "", requestUrl);
            } else {
                l.setVisibility(View.GONE);
                adapter = new CatalogueAdapter(this, R.layout.catalogue_grid,
                        comingFrom, query, gender, category, subCategory, "", requestUrl);
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
        String comingFrom;
        String searchQuery;
        String gender;
        String category;
        String subCategory;
        String filters;
        String requestURL;


        private LayoutInflater inflater = null;


        public CatalogueAdapter(Context context, int layout, String comingFrom, String searchQuery,
                                String gender, String category, String subCategory, String filters,
                                String requestURL) {

            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.layout = layout;
            this.comingFrom = comingFrom;
            this.searchQuery = searchQuery;
            this.gender = gender;
            this.category = category;
            this.subCategory = subCategory;
            this.filters = filters;
            this.requestURL = requestURL;
            new GetProductsAsyncTask().execute(comingFrom, searchQuery, gender, category, subCategory, filters, requestURL);

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
            String response = inputStreamToString(urlConnection);

            return Integer.parseInt(getDataFromJSON(response, "total"));

        }

        private String getProductsBySubCategoryId(int total, String genderId, String subCategoryId) {

            String requestURL = "http://eiffel.itba.edu.ar/hci/service3/Catalog.groovy?method=GetProductsBySubcategoryId&id=";
            requestURL += subCategoryId + "&filters=";
            String filterString = Constants.sectionFilters[UtilClass.getSectionFilterId(CatalogueActivity.this, genderId)];
            System.out.println(filterString);
            try {
                filterString = URLEncoder.encode(filterString, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return fetchDesiredProducts(requestURL + filterString + "&page_size=" + total);

        }

        private String getProductsByCategoryId(int total, String genderId, String categoryId) {

            String requestURL = "http://eiffel.itba.edu.ar/hci/service3/Catalog.groovy?method=GetProductsByCategoryId&id=";
            requestURL += categoryId + "&filters=";
            String filterString = Constants.sectionFilters[UtilClass.getSectionFilterId(CatalogueActivity.this, genderId)];
            System.out.println(filterString);
            try {
                filterString = URLEncoder.encode(filterString, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return fetchDesiredProducts(requestURL + filterString + "&page_size=" + total);

        }

        private String getAllProductsWithGenderFilter(int total, String genderSelection){

            String requestURL = baseURL + "&page_size=" + total + "&filters=";
            String filters = "";
            filters = Constants.sectionFilters[UtilClass.getSectionFilterId(CatalogueActivity.this, genderSelection)];
            try {
                filters = URLEncoder.encode(filters, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return fetchDesiredProducts(requestURL + filters);
        }

        private String getProductsByName(int total, String query) {

            String requestURL = "http://eiffel.itba.edu.ar/hci/service3/Catalog.groovy?method=GetProductsByName&page_size=" + total + "&name=" + query;
            return fetchDesiredProducts(requestURL);

        }

        private String getAllProducts(int total) {
            return fetchDesiredProducts(baseURL + "&page_size=" + total);
        }

        private String fetchDesiredProducts(String requestURL) {
            CatalogueActivity.this.requestUrl = requestURL;


            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CatalogueActivity.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constants.restoreCatalogueRequest,requestURL);
            editor.apply();


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
                if (urlConnection != null) {
                    urlConnection.disconnect();                     // Finishes connection
                    System.out.println("Disconnected");
                }

            }

            String response = inputStreamToString(urlConnection);

            setAvailableFilters(response);
            return getDataFromJSON(response, "products");                // Filters fetched data


        }


        private void setAvailableFilters(String response) {

            String filters = getDataFromJSON(response,"filters");
            System.out.println("Filtros: " + filters);

            Gson parser = new Gson();
            Type dataSetListType = new TypeToken<Set<Filter>>() {
            }.getType();
            filtersAvailable = (Set<Filter>) parser.fromJson(filters, dataSetListType);

        }


        private String getProducts (String comingFrom, String searchQuery, String gender,
            String category, String subCategory, String filters, String requestURL) {



            int total = getProductsQuantity();

            if (!requestUrl.equals("")) {
                return fetchDesiredProducts(requestURL);
            }

            String result = "";
            switch (comingFrom) {

                case Constants.comingFromGender:
                    result = getAllProductsWithGenderFilter(total, gender);
                    break;
                case Constants.comingFromCategories:
                    result = getProductsByCategoryId(total, gender, category);
                    break;
                case Constants.comingFromSubCategories:
                    result = getProductsBySubCategoryId(total, gender, subCategory);
                    break;
                case Constants.comingFromSearchBar:
                    result = getProductsByName(total, searchQuery);
                    break;
                case Constants.comingFromNoWhere:
                default:
                    result = getAllProducts(total);

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


            Collection<Product> products;
            String comingFrom = params[0];
            String searchQuery = params[1];
            String gender = params[2];
            String category = params [3];
            String subCategory = params[4];
            String filters = params[5];
            String requestURL = params[6];

            String result = getProducts(comingFrom, searchQuery, gender,
                    category, subCategory, subCategory, requestURL);
            if (result != null) {

                products = fromJSONToCollection(result);
            } else {
                products = new HashSet<Product>();
            }
            return products;
        }

        @Override
        protected void onPostExecute(Collection<Product> products) {
            if (products == null || products.isEmpty() || adapter == null) {
                return;
            }
            super.onPostExecute(products);
            adapter.clear();
            adapter.addAll(products);
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

            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.drawable.no_picture);
            }

        }


    }

}
