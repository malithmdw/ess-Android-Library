package com.ess.essandroidbaselibrary.util.uIUtilities;

import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.ess.essandroidbaselibrary.R;

public class NoInternetWaitingActivity extends AppCompatActivity
{
    private TextView titleTextView;
    private TextView descriptionTextView;
    private com.ess.essandroidbaselibrary.util.uIUtilities.GifImageView gifImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_interet_waiting);

        titleTextView = (TextView) findViewById(R.id.no_internet_waiting_textView);
        descriptionTextView = (TextView) findViewById(R.id.no_internet_waiting_description_textView);
        gifImageView = (com.ess.essandroidbaselibrary.util.uIUtilities.GifImageView) findViewById(R.id.no_internet_waiting_imageView);

        final int colorForDownloading = ContextCompat.getColor(this, R.color.downloading_data_background);
        final int colorForNoInternet = ContextCompat.getColor(this, R.color.downloading_data_no_internet_background);

        Thread waitingForDatLoadingThread = new Thread(new Runnable() {
            @Override
            public void run()
            {
//                while (!Configuration.getInstance().isDataSynchronizedWithServer())
//                {
//                    if (!InternetConnection.getInstance(NoInternetWaitingActivity.this).isNetworkAvailable())
//                    {
//                        if (!gifImageView.isThisAnimationShowing(R.drawable.loading_icon))
//                        {
//                            // Only the original thread that created a view hierarchy can touch its views.otherwise crash the application.
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run()
//                                {
//                                    // Stuff that updates the UI
//                                    titleTextView.setText(getString(R.string.no_internet_title_no_internet_text));
//                                    descriptionTextView.setText(getString(R.string.no_internet_descriptione_no_internet_text));
//
//                                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
//                                    {
//                                        gifImageView.setGifImageResource(R.drawable.loading_icon);
//                                        gifImageView.startAnimation();
//
//                                        getWindow().getDecorView().setBackgroundColor(colorForNoInternet);
//                                    }
//                                }
//                            });
//                        }
//                    }
//                    else
//                    {
//                        final int[] percentage = new int[]{0};
//
//                        // Calculate percentage.
//                        try
//                        {
//                            double insertedCount = PrivateDatabaseConnection.getInstance(NoInternetWaitingActivity.this).getSectionsDataRecordCount();
//                            double totalCount = 0;
//
//                            if (Configuration.getInstance().getAppData() != null)
//                            {
//                                totalCount = Configuration.getInstance().getAppData().getSectionDataRecordCountServer();
//
//                                if (totalCount != 0)
//                                {
//                                    percentage[0] = (int) ((insertedCount / totalCount) * 100);
//                                }
//                            }
//                        }
//                        catch (SQLException e)
//                        {
//                        }
//                        catch (Exception e)
//                        {
//                        }
//
//                        // Stuff that updates the UI
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run()
//                            {
//
//                                if (!gifImageView.isThisAnimationShowing(R.drawable.loading_blue_icon))
//                                {
//                                    titleTextView.setText(getString(R.string.no_internet_title_geting_data_text));
//                                    descriptionTextView.setText(getString(R.string.no_internet_description_geting_data_text));
//
//                                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
//                                    {
//                                        gifImageView.setGifImageResource(R.drawable.loading_blue_icon);
//                                        gifImageView.startAnimation();
//
//                                        getWindow().getDecorView().setBackgroundColor(colorForDownloading);
//                                    }
//                                }
//                                else
//                                {
//                                    descriptionTextView.setText(getString(R.string.no_internet_description_geting_data_text) + " \n\n" +  percentage[0] + " % Completed");
//                                }
//                            }
//                        });
//                    }
//
//                    // waiting
//                    try
//                    {
//                        Thread.sleep(2000);
//                    }
//                    catch (InterruptedException ignored)
//                    {}
//                    catch (Exception e)
//                    {}
//                }
//
//                // Start app main activity.
//                Intent i = new Intent(NoInternetWaitingActivity.this, MainActivity.class);
//                startActivity(i);

                // Close this activity.
                finish();
            }
        });
        waitingForDatLoadingThread.start();
    }
}