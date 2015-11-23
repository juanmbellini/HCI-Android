package hci.tiendapp.customviews;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;
import java.util.Set;
import java.util.zip.Inflater;

import hci.tiendapp.R;
import hci.tiendapp.backend.Category;
import hci.tiendapp.backend.Filter;
import hci.tiendapp.backend.SubCategory;
import hci.tiendapp.background.AsyncTaskManager;
import hci.tiendapp.constants.Constants;
import hci.tiendapp.util.UtilClass;

/**
 * Created by JuanMarcos on 23/11/15.
 */
public class FiltersDialogBox extends CustomDialogBox {

    AsyncTaskManager asyncTaskManager;


    RelativeLayout cancel;
    RelativeLayout ok;

    Set<Filter> filtersAvailable;
    Set<Filter> filtersApplied;

    String comingFrom;
    String gender;
    String category;
    String subCategory;

    List<String> availableSections;
    List<Category> availableCategories;
    List<SubCategory> availableSubCategories;
    List<String> availableBrands;
    List<String> availableColors;
    List<String> availableSizes;
    List<String> availableOccasions;

    public FiltersDialogBox(Activity callerActivity, String comingFrom,
                            String gender, String category, String subCategory,
                            Set<Filter> filtersAvailable, Set<Filter> filtersApplied) {
        super(callerActivity, R.layout.filters_dialog_box);
        this.filtersAvailable = filtersAvailable;
        this.filtersApplied = filtersApplied;
        this.comingFrom = comingFrom;
        this.gender = gender;
        this.category = category;
        this.subCategory = subCategory;

        asyncTaskManager = new AsyncTaskManager(callerActivity);
        availableSections = new ArrayList<String>();
        availableCategories = new ArrayList<Category>();
        availableSubCategories = new ArrayList<SubCategory>();
        availableBrands = new ArrayList<String>();
        availableColors = new ArrayList<String>();
        availableSizes = new ArrayList<String>();
        availableOccasions = new ArrayList<String>();
        asyncTaskManager.startTaskManager();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        TableRow sectionRow = (TableRow) findViewById(R.id.section_filter_row);
        TableRow categoryRow = (TableRow) findViewById(R.id.category_filter_row);
        TableRow subCategoryRow = (TableRow) findViewById(R.id.sub_category_filter_row);

        TextView text = (TextView) findViewById(R.id.section_filter);
        text.setText("Hola");

       boolean gender = false, category = false, subCategory = false;


        // Sets up layout
        switch (comingFrom) {
            case Constants.comingFromSearchBar: ;
            case Constants.comingFromNoWhere:
                gender = true;
                category = true;
                subCategory = true;
                break;
            case Constants.comingFromGender:
                category = true;
                subCategory = true;
                break;
            case Constants.comingFromCategories:
                subCategory = true;
                break;
        }


        if (gender) {
            sectionRow.setVisibility(View.VISIBLE);
            getSections();
        }

        if (category) {
            categoryRow.setVisibility(View.VISIBLE);
            new GetCategoriesAsyncTask().execute(this.gender);
        }

        if(subCategory) {
            if (this.category == null) {
                subCategoryRow.setEnabled(false);
            } else {
                subCategoryRow.setVisibility(View.VISIBLE);
                new GetSubCategoriesTask().execute(this.gender, this.category);

            }
        }




        cancel = (RelativeLayout)findViewById(R.id.filters_cancel_button);
        ok = (RelativeLayout)findViewById(R.id.filters_OK_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(callerActivity, "Ok", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void setSectionSpinner(String[] sections) {

        Spinner categoriesSpinner = (Spinner) findViewById(R.id.section_spinner);
        final ArrayAdapter<String> categoriesSpinnerAdapter =
                new ArrayAdapter<String>(callerActivity,R.layout.quantity_spinner_item, sections);
        categoriesSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinner);
    }

    private void setCategoriesSpinner(Category[] categories) {

        Spinner categoriesSpinner = (Spinner) findViewById(R.id.category_spinner);
        final ArrayAdapter<Category> categoriesSpinnerAdapter =
                new ArrayAdapter<Category>(callerActivity,R.layout.quantity_spinner_item, categories);
        categoriesSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinner);
    }

    private void setSubCategoriesSpinner(SubCategory[] subCategories) {

        Spinner subCategoriesSpinner = (Spinner) findViewById(R.id.sub_category_spinner);
        final ArrayAdapter<SubCategory> categoriesSpinnerAdapter =
                new ArrayAdapter<SubCategory>(callerActivity,R.layout.quantity_spinner_item, subCategories);
        categoriesSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinner);
    }

    private void setBrandsSpinner(String[] brands) {

        Spinner subCategoriesSpinner = (Spinner) findViewById(R.id.brand_spinner);
        final ArrayAdapter<String> categoriesSpinnerAdapter =
                new ArrayAdapter<String>(callerActivity,R.layout.quantity_spinner_item, brands);
        categoriesSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinner);
    }

    private void setColorSpinner(String[] colors) {

        Spinner subCategoriesSpinner = (Spinner) findViewById(R.id.brand_spinner);
        final ArrayAdapter<String> categoriesSpinnerAdapter =
                new ArrayAdapter<String>(callerActivity,R.layout.quantity_spinner_item, colors);
        categoriesSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinner);
    }

    private void setSizesSpinner(String[] sizes) {

        Spinner subCategoriesSpinner = (Spinner) findViewById(R.id.size_spinner);
        final ArrayAdapter<String> categoriesSpinnerAdapter =
                new ArrayAdapter<String>(callerActivity,R.layout.quantity_spinner_item, sizes);
        categoriesSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinner);
    }

    private void setOccasionSpinner(String[] occasions) {

        Spinner subCategoriesSpinner = (Spinner) findViewById(R.id.occasion_spinner);
        final ArrayAdapter<String> categoriesSpinnerAdapter =
                new ArrayAdapter<String>(callerActivity,R.layout.quantity_spinner_item, occasions);
        categoriesSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinner);
    }


    /*private class FiltersAdapter extends BaseAdapter {

        List<Filter> filters;

        int layout;
        LayoutInflater inflater;

        public FiltersAdapter(int layout, LayoutInflater inflater, List<Filter> filters) {
            this.layout = layout;
            this.inflater = inflater;
            this.filters = filters;
        }

        @Override
        public int getCount() {
            return filters.size();
        }

        @Override
        public Object getItem(int position) {
            return filters.get(position);
        }

        @Override
        public long getItemId(int position) {
            return filters.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = inflater.inflate(layout, null);
            TextView text = (TextView) view.findViewById(R.id.quantity_spinner_item);
            text.setText(filters.get());

        }
    }
*/

    private void getSections() {

        availableSections.add(callerActivity.getString(R.string.gender_men));
        availableSections.add(callerActivity.getString(R.string.gender_women));
        availableSections.add(callerActivity.getString(R.string.gender_kids));
        availableSections.add(callerActivity.getString(R.string.gender_babies));
        String[] aux = new String[4];
        setSectionSpinner(availableSections.toArray(aux));

    }



    private class GetCategoriesAsyncTask extends AsyncTask<String, Long, List<Category>> {


        final String baseURL = "http://eiffel.itba.edu.ar/hci/service3/Catalog.groovy?method=GetAllCategories";


        private String setUp(String sectionId) {

            String requestURL = baseURL;

            if (sectionId!= null) {

                int id = UtilClass.getSectionFilterId(callerActivity, sectionId);

                try {
                    requestURL += "&filters=" + URLEncoder.encode(Constants.sectionFilters[id], "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return requestURL;

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
        protected void onPreExecute() {
            super.onPreExecute();
            asyncTaskManager.addTask(this);

        }

        @Override
        protected List<Category> doInBackground(String... params) {

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
            list.add(0,new Category(-1, callerActivity.getString(R.string.all_female)));
            return list;
        }

        @Override
        protected void onPostExecute(List<Category> categories) {
            super.onPostExecute(categories);
            availableCategories = categories;
            Category[] aux = new Category[categories.size()];
            setCategoriesSpinner(categories.toArray(aux));
            asyncTaskManager.finishTask(this);

        }

    }


    private class GetSubCategoriesTask extends AsyncTask<String, Long, List<SubCategory>> {

        final String baseURL = "http://eiffel.itba.edu.ar/hci/service3/Catalog.groovy?method=GetAllSubcategories&id=";


        private String setUp(String sectionId, String categoryId) {

            String requestURL = baseURL + categoryId;

            if (sectionId != null) {

                int id = UtilClass.getSectionFilterId(callerActivity, sectionId);

                try {
                    requestURL += "&filters=" + URLEncoder.encode(Constants.sectionFilters[id], "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            return requestURL;
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
        protected void onPreExecute() {
            super.onPreExecute();
            asyncTaskManager.addTask(this);

        }


        @Override
        protected List<SubCategory> doInBackground(String... params) {

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
            Type dataSetListType = new TypeToken<List<SubCategory>>() {}.getType();
            List<SubCategory> list = parser.fromJson(result, dataSetListType);
            list.add(0,new SubCategory(-1, callerActivity.getString(R.string.all_in) + " " + params[2]));
            return list;
        }

        @Override
        protected void onPostExecute(List<SubCategory> subCategories) {

            super.onPostExecute(subCategories);
            availableSubCategories = subCategories;
            SubCategory[] aux = new SubCategory[subCategories.size()];
            setSubCategoriesSpinner(subCategories.toArray(aux));
            asyncTaskManager.finishTask(this);

        }
    }


}
