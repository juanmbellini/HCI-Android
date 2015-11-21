package hci.tiendapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
}
