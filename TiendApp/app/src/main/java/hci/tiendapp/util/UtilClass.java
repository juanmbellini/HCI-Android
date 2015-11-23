package hci.tiendapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import hci.tiendapp.R;
import hci.tiendapp.TiendApp;
import hci.tiendapp.activities.HomeActivity;
import hci.tiendapp.constants.Constants;

/**
 * Created by JuanMarcos on 20/11/15.
 */
public class UtilClass {

    public static String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }


    public static void goHome(Activity activity, String reason) {

        Intent intent = new Intent(activity, HomeActivity.class);
        intent.putExtra(Constants.goHome, reason);
        activity.finish();
        activity.startActivity(intent);
        return;
    }

    public static  int getSectionFilterId(Activity activity, String sectionId) {

        int id = 0;
        switch (sectionId) {

            case Constants.menCategory:
                id = 0;
                break;
            case Constants.womenCategory:
                id = 1;
                break;
            case Constants.kidsCategory:
                id = 2;
                break;
            case Constants.babiesCategory:
                id = 3;
                break;
            default:
                UtilClass.goHome(activity, Constants.wrongParameters);
                throw new RuntimeException("Algo anduvo mal");
        }
        return id;
    }

    public static String getOrderStatus(int id) {

        String result = "";
        switch (id) {
            case 1:
                result = TiendApp.getContext().getString(R.string.order_created);
                break;
            case 2:
                result = TiendApp.getContext().getString(R.string.order_confirmed);
                break;
            case 3:
                result = TiendApp.getContext().getString(R.string.order_shiped);
                break;
            case 4:
                result = TiendApp.getContext().getString(R.string.order_delivered);
                break;
        }
        return result;
    }
}
