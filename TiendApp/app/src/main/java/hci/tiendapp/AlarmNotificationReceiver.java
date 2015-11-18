package hci.tiendapp;

import java.text.DateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(HomeActivity.TAG, "Alarm at: " +  DateFormat.getDateTimeInstance().format(new Date()));
    }
}
