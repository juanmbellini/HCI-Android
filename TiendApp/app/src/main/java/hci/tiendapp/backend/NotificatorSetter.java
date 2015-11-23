package hci.tiendapp.backend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Julian on 11/23/2015.
 */
public class NotificatorSetter extends BroadcastReceiver {
    Notificator alarm = new Notificator();
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            alarm.SetNotifications(context);
        }
    }
}
