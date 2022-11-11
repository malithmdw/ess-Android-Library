package com.ess.essandroidbaselibrary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity
{
    private static final int SPLASH_TIME_OUT = 3000;
    private static final String EXTRA_PROFILE_FORM_STATE = "EXTRA_PROFILE_FORM_STATE";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set content view AFTER ABOVE sequence (to avoid crash)
        setContentView(R.layout.activity_splash);

        // Load data before splash.


        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run()
            {
                // This method will be executed once the timer is over.
                Intent nextIntent = null;



                startActivity(nextIntent);
                // close this splash activity
                finish();

            }
        }, SPLASH_TIME_OUT);
    }
}
