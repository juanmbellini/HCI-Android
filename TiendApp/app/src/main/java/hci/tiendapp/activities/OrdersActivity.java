package hci.tiendapp.activities;

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

    public OrdersActivity(int layoutId, int childId) {
        super(R.layout.orders_activity, R.id.orders_layout);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String aux = preferences.getString(Constants.lastLoginAccount, "");
        if (!aux.equals("")) {
            account = new Gson().fromJson(aux, Account.class);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        if (account == null) {
            UtilClass.goHome(this,Constants.noAccount);
            finish();
        }

        adapter = new OrdersAdapter(this, R.layout.order_item);
        ListView l = (ListView)findViewById(R.id.orders_list);
        l.setAdapter(adapter);
        l.setSaveEnabled(true);

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
            for (OrderLine each : orders.get(position).getItems()) {
                orderTotal += each.getPrice();
            }

            total.setText("$" + orderTotal);



            Holder holder = new Holder(ticketNumber, state, buyingDate, total);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(OrdersActivity.this, OrderDetailsActivity.class);
                    Order result = (Order)getItem(position);
                    intent.putExtra(Constants.orderSelected, result.getId() + "");
                    startActivity(intent);

                }
            });
            return view;
        }
    }

    private class getAllOrdersAsyncTask extends AsyncTask<String, Long, Collection<Order>> {


        final String baseURL = "http://eiffel.itba.edu.ar/hci/service3/Order.groovy?method=GetAllOrders&username=";


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
            super.onPreExecute();
            ProgressDialog progressDialog = new ProgressDialog(OrdersActivity.this);
            progressDialog.setMessage(OrdersActivity.this.getString(R.string.loading_message));
            progressDialog.show();
        }

        @Override
        protected Collection<Order> doInBackground(String... params) {


            String requestURL = baseURL + (params[0] + "&authentication_token=" + params[1]);

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
            adapter.clear();
            adapter.addAll(orders);
            adapter.notifyDataSetChanged();

        }
    }

}
