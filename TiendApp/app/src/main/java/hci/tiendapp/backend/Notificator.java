package hci.tiendapp.backend;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import java.util.List;

import hci.tiendapp.R;
import hci.tiendapp.util.UtilClass;

/**
 * Created by Julian on 11/23/2015.
 */
public class Notificator extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "anda wachoo", Toast.LENGTH_LONG).show(); // For example
    }

    public void SetNotifications(Context context)
    {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Notificator.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 5 * 1, pi); // Millisec * Second * Minute
    }

    public void CancelNotifications(Context context)
    {
        Intent intent = new Intent(context, Notificator.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
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
