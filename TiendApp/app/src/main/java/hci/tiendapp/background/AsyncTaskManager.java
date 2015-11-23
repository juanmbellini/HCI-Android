package hci.tiendapp.background;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.Collection;
import java.util.HashSet;

import hci.tiendapp.R;

/**
 * Created by JuanMarcos on 23/11/15.
 */
public class AsyncTaskManager {

    Activity callerActivity;
    ProgressDialog progressDialog;
    Collection<AsyncTask> tasks;
    boolean run;
    Thread manager;


    public AsyncTaskManager(Activity callerActivity) {
        this.callerActivity = callerActivity;
        tasks = new HashSet<AsyncTask>();
        progressDialog = new ProgressDialog(callerActivity);
        progressDialog.setMessage(callerActivity.getString(R.string.loading_message));

        manager = new Thread() {
            @Override
            public void run() {

                while (run) {
                    if (tasks.isEmpty()) {
                        progressDialog.dismiss();

                    } else {
                        progressDialog.show();
                    }
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    public void startTaskManager() {
        run = false;
        manager.run();
    }

    public void addTask(AsyncTask task) {
        tasks.add(task);
    }

    public void finishTask(AsyncTask task) {
        tasks.remove(task);

    }


}
