package com.ess.essandroidbaselibrary.util.uIUtilities;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Malith on 4/12/2018.
 */

public class ToastMessage
{
    // Singleton implementation
    private static ToastMessage INSTANCE;

    private void ToastMessage()
    {
    }

    public static ToastMessage getInstance()
    {
        if(INSTANCE == null)
        {
            INSTANCE = new ToastMessage();
        }
        return INSTANCE;
    }
    // Singleton implementation - END

    Activity activity;

    public void show(Activity activity)
    {
        this.activity = activity;

        Toast toast = new Toast(activity.getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}
