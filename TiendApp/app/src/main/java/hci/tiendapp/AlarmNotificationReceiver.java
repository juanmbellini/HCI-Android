package hci.tiendapp;

import java.text.DateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import hci.tiendapp.activities.HomeActivity;
import hci.tiendapp.activities.HomeActivity2;

public class AlarmNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(HomeActivity2.TAG, "Alarm at: " +  DateFormat.getDateTimeInstance().format(new Date()));
    }
}
