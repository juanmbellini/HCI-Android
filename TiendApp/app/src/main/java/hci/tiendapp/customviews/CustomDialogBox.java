package hci.tiendapp.customviews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import hci.tiendapp.R;

/**
 * Created by JuanMarcos on 23/11/15.
 */
public abstract class CustomDialogBox extends Dialog {

    protected Activity callerActivity;
    protected Dialog dialog;

     int layoutId;

    public CustomDialogBox(Activity callerActivity, int layoutId) {
        super(callerActivity);
        this.callerActivity = callerActivity;
        this.layoutId = layoutId;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(layoutId);
    }

}
