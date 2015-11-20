package hci.tiendapp.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by JuanMarcos on 20/11/15.
 */
public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("Ring");
        Log.d("Alarm", "Alarm at: " + DateFormat.getDateTimeInstance().format(new Date()));
    }
}
