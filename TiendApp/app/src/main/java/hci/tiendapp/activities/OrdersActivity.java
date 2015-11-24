package hci.tiendapp.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import hci.tiendapp.R;
import hci.tiendapp.TiendApp;
import hci.tiendapp.backend.Account;
import hci.tiendapp.backend.Category;
import hci.tiendapp.backend.Order;
import hci.tiendapp.backend.OrderLine;
import hci.tiendapp.backend.Product;
import hci.tiendapp.constants.Constants;
import hci.tiendapp.util.UtilClass;

/**
 * Created by JuanMarcos on 23/11/15.
 */
public class OrdersActivity extends MyDrawerActivity {

    Account account;
    String authenticationToken = "";
    OrdersAdapter adapter;


    public OrdersActivity() {
        super(R.layout.orders_activity, R.id.orders_layout);
        setContext(this);

    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);



        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String aux = preferences.getString(Constants.lastLoginAccount, "");
        if (!aux.equals("")) {
            account = new Gson().fromJson(aux, Account.class);
        }

        if (account == null) {
            UtilClass.goHome(this, Constants.noAccount);
            finish();
            //Dialog dialog = new Dialog()
            return;
        }

        authenticationToken = preferences.getString(Constants.authenticationToken, "");
        System.out.println(account.getUserName());

        adapter = new OrdersAdapter(this, R.layout.order_item);
        ListView l = (ListView)findViewById(R.id.orders_list);
        l.setAdapter(adapter);
        l.setSaveEnabled(true);

        getSupportActionBar().setTitle(getString(R.string.order_activity_name));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    private class OrdersAdapter extends BaseAdapter {



        Context context;
        int layout;
        List<Order> orders= new ArrayList<Order>();


        private LayoutInflater inflater = null;


        public OrdersAdapter(Context context, int layout) {

            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.layout = layout;

            new getAllOrdersAsyncTask().execute(account.getUserName(), authenticationToken);

        }






        public void clear() {
            orders = new ArrayList<Order>();
        }

        public boolean addAll(Collection<Order> c) {
            if (!c.isEmpty()) {
                orders = new ArrayList<Order>(c);
                return true;
            }
            return false;
        }

        @Override
        public int getCount() {
            return orders.size();
        }

        @Override
        public Object getItem(int position) {
            return orders.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        public class Holder {
            TextView ticketNumber;
            TextView state;
            TextView buyingDate;
            TextView total;

            public Holder(TextView ticketNumber, TextView state, TextView buyingDate, TextView total) {
                this.ticketNumber = ticketNumber;
                this.state = state;
                this.buyingDate = buyingDate;
                this.total = total;
            }
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View view = inflater.inflate(layout, null);


            TextView ticketNumber = (TextView) view.findViewById(R.id.ticket_number);
            TextView state = (TextView) view.findViewById(R.id.state_text);
            TextView buyingDate = (TextView) view.findViewById(R.id.buying_date_text);
            TextView total = (TextView) view.findViewById(R.id.total_text);

            ticketNumber.setText(OrdersActivity.this.getString(R.string.order) + ": " + orders.get(position).getId());


            state.setText(UtilClass.getOrderStatus(Integer.parseInt(orders.get(position).getStatus())));
            buyingDate.setText(orders.get(position).getProcessedDate());

            double orderTotal = 0;

            System.out.println(orders.get(position).getItems());

            for (OrderLine each : orders.get(position).getItems()) {
                orderTotal += each.getPrice();
            }

            total.setText("$" + orderTotal);



            Holder holder = new Holder(ticketNumber, state, buyingDate, total);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
            return view;
        }
    }

    private class getAllOrdersAsyncTask extends AsyncTask<String, Long, Collection<Order>> {


        String baseURL = "http://eiffel.itba.edu.ar/hci/service3/Order.groovy?method=GetAllOrders&username=";

        ProgressDialog progressDialog = new ProgressDialog(OrdersActivity.this);;

        private String getOrdersJSON(URLConnection urlConnection) {

            String result = null;

            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                JSONObject aux = new JSONObject(UtilClass.readStream(in));
                result = aux.getString("orders");
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

            progressDialog.setMessage(OrdersActivity.this.getString(R.string.loading_message));
            super.onPreExecute();

            progressDialog.show();
        }

        @Override
        protected Collection<Order> doInBackground(String... params) {


            String requestURL = baseURL + (params[0] + "&authentication_token=" + params[1]);
            baseURL = "http://eiffel.itba.edu.ar/hci/service3/Order.groovy?method=GetOrderById&username=";
            baseURL += (params[0] + "&authentication_token=" + params[1]);

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

            String result = getOrdersJSON(urlConnection);                // Filters fetched data


            Gson parser = new Gson();
            Type dataSetListType = new TypeToken<List<Order>>() {}.getType();
            List<Order> list = parser.fromJson(result, dataSetListType);
            return list;
        }

        @Override
        protected void onPostExecute(Collection<Order> orders) {
            super.onPostExecute(orders);
            new GetRealinformationAsyncTask(progressDialog).execute(orders);
            //progressDialog.dismiss();

        }


        private class GetRealinformationAsyncTask extends AsyncTask<Collection<Order>, Void, Collection<Order>> {

            ProgressDialog progressDialogInternal = new ProgressDialog(OrdersActivity.this);

            public GetRealinformationAsyncTask(ProgressDialog dialog) {
                progressDialogInternal = dialog;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialogInternal.setMessage(OrdersActivity.this.getString(R.string.loading_message));
                super.onPreExecute();

                progressDialog.show();
            }

            private String getOrdersJSON(URLConnection urlConnection) {

                String result = null;

                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    JSONObject aux = new JSONObject(UtilClass.readStream(in));
                    result = aux.getString("order");
                    if (result == null) {
                        throw new RuntimeException("Wrong Method");
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                return result;
            }

            @Override
            protected Collection<Order> doInBackground(Collection<Order>... params) {

                Collection<Order> realCollection = new ArrayList<Order>();



                for(Order each : params[0]) {

                    if (!each.getStatus().equals("1")) {

                        String requestURL = baseURL + "&id=" + each.getId();

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
                            }
                            System.out.println("Disconnected");
                        }

                        String result = getOrdersJSON(urlConnection);                // Filters fetched data


                        realCollection.add(new Gson().fromJson(result, Order.class));
                    }

                }

                return realCollection;

            }

            @Override
            protected void onPostExecute(Collection<Order> orders) {
                super.onPostExecute(orders);
                adapter.clear();
                adapter.addAll(orders);
                adapter.notifyDataSetChanged();

                progressDialogInternal.dismiss();
            }
        }
    }

}
