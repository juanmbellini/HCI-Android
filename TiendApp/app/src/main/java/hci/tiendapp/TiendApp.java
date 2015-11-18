package hci.tiendapp;

import android.app.Application;
import android.content.Context;

/**
 * Created by JuanMarcos on 17/11/15.
 */
public class TiendApp extends Application {

    private static TiendApp instance;

    public TiendApp() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }
}
