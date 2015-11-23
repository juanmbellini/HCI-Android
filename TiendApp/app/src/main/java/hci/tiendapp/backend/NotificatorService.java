package hci.tiendapp.backend;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.security.Provider;

/**
* Created by Julian on 11/23/2015.
        */
public class NotificatorService extends Service
{
    Notificator alarm = new Notificator();
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        alarm.SetNotifications(this);
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        alarm.SetNotifications(this);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}