package hci.tiendapp.background;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

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
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import hci.tiendapp.TiendApp;
import hci.tiendapp.backend.Product;
import hci.tiendapp.util.UtilClass;

/**
 * Created by JuanMarcos on 20/11/15.
 */
public class ProductsService  extends Application {

    private static long waitTimeTillDataIsObsolete = 25000;

    private static HttpThread httpThread;
    private boolean httpThreadIsRunning = false;



    private Collection<Product> products = new HashSet<Product>();

    private static final String baseURL = "http://eiffel.itba.edu.ar/hci/service3/Catalog.groovy?method=GetAllProducts";

    boolean alarm = false;

    private AlarmReceiver ObsolteDataAlarmReceiver = new AlarmReceiver();

    private final IBinder binder = new ProductsServiceBinder();


    public Collection<Product> getProducts() {
        return products;
    }


    private void setAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + waitTimeTillDataIsObsolete, pendingIntent);
        alarm = false;
        System.out.println("Alarm set");
    }

    private Collection<Product> fromJSONToCollection(String JSON) {

        Gson parser = new Gson();
        Type dataSetListType = new TypeToken<Collection<Product>>() {}.getType();
        return (Collection<Product>) parser.fromJson(JSON, dataSetListType);

    }

    private String fromCollectionToJSON(Collection<Product> c) {
        return new Gson().toJson(c);
    }

    private void saveDataLocally(String data) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(TiendApp.getContext());
        preferences.edit().putString("products_collection", data);

    }


    private void fetchData() {


        String result = getProducts(getProductsQuantity());
        if (result != null) {
            saveDataLocally(result);
            products = fromJSONToCollection(result);
        }
        System.out.println("There are " + products.size() + " products stored locally");
        //setAlarm();
    }


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
            if(urlConnection != null) {
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
            if(urlConnection != null) {
                urlConnection.disconnect();                     // Finishes connection
                System.out.println("Disconnected");
            }

        }


        return getDataFromJSON(urlConnection, "products");                // Filters fetched data


    }

    @Override
    public void onCreate() {
        super.onCreate();
        httpThread = new HttpThread();
    }


    @Nullable
    public IBinder onBind(Intent intent) {

        return binder;
    }




    private class HttpThread extends Thread {
        static final long DELAY = 1200000; // 20 minutes till data becomes obsoletes

        @Override
        public void run() {
            while(httpThreadIsRunning) {
                try {
                    fetchData();
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    httpThreadIsRunning = false;
                    e.printStackTrace();
                }
            }
        }
    }


    public class ProductsServiceBinder extends Binder {

        public ProductsService getService() {
            return ProductsService.this;
        }
    }


    /**
     * Created by JuanMarcos on 20/11/15.
     */
    public class AlarmReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("Ring");
            Log.d("Alarm", "Alarm at: " + DateFormat.getDateTimeInstance().format(new Date()));
            httpThreadIsRunning = false;
        }
    }


}
