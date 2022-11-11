package com.ess.essandroidbaselibrary;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ess.essandroidbaselibrary.communication.InternetConnection;
import com.ess.essandroidbaselibrary.util.uIUtilities.CustomToastMessage;

public class AboutUsActivity extends AppCompatActivity
{
    private static final String FACEBOOK_PAGE_ID = "295907784075943";
    private static final String EMAIL = "slbg.lk@gmail.com";
    private static final String YOU_TUBE_URL = "https://www.youtube.com/channel/UC5HNLTf6jtHmpfC_CgHyLnQ";

    private RelativeLayout contactUsButton;
    private RelativeLayout facebookButton;
    private RelativeLayout youtubeButton;
    private RelativeLayout playStoreButton;
    private TextView versionTextView;
    private TextView copyRightTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        try
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (NullPointerException ex)
        {
        }

        contactUsButton = (RelativeLayout) findViewById(R.id.about_us_contact_us);
        facebookButton = (RelativeLayout) findViewById(R.id.about_us_facebook);
        youtubeButton = (RelativeLayout) findViewById(R.id.about_us_youtube);
        playStoreButton = (RelativeLayout) findViewById(R.id.about_us_playstore);
        versionTextView = (TextView) findViewById(R.id.about_us_version_txt);
        copyRightTextView = (TextView) findViewById(R.id.about_us_copy_right_txt);

        String version = "";
        try
        {
            // Get the version from android/app/src/main/AndroidManifest.xml
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
        }

        versionTextView.setText(getString(R.string.about_us_txt_version) + " " + version);
        copyRightTextView.setText(getString(R.string.about_us_txt_copyright) + " " + getString(R.string.app_copyright_year) + " " + getString(R.string.app_company_name));

        contactUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onContactUsButtonClicked();
            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onFacebookButtonClicked();
            }
        });

        youtubeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onYoutubeButtonClicked();
            }
        });

        playStoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onPlayStoreButtonClicked();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            super.onBackPressed();
        }
        return true;
    }

    private void onContactUsButtonClicked()
    {
        if (InternetConnection.getInstance(this).isNetworkAvailable())
        {
            try
            {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.setType("vnd.android.cursor.item/email");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {EMAIL});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, ""); // My Email Subject
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi slbg,\n");
                startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
            }
            catch (Exception e)
            {

            }
        }
        else
        {
            CustomToastMessage.getInstance().showMessage(this,
                    CustomToastMessage.MessageType.ERROR,
                    CustomToastMessage.MessageTimeOut.SHORT,
                    getString(R.string.error_app_no_internet));
        }
    }

    private void onFacebookButtonClicked()
    {
        if (InternetConnection.getInstance(this).isNetworkAvailable())
        {
            // https://www.facebook.com/pg/295907784075943
            try
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/pg/" + FACEBOOK_PAGE_ID));
                startActivity(intent);
            }
            catch (Exception e)
            {

            }
        }
        else
        {
            CustomToastMessage.getInstance().showMessage(this,
                    CustomToastMessage.MessageType.ERROR,
                    CustomToastMessage.MessageTimeOut.SHORT,
                    getString(R.string.error_app_no_internet));
        }
    }

    private void onYoutubeButtonClicked()
    {
        if (InternetConnection.getInstance(this).isNetworkAvailable())
        {
            Intent intent;
            try
            {
                intent =new Intent(Intent.ACTION_VIEW);
                intent.setPackage("com.google.android.youtube");
                intent.setData(Uri.parse(YOU_TUBE_URL));
                startActivity(intent);
            }
            catch (ActivityNotFoundException e)
            {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(YOU_TUBE_URL));
                startActivity(intent);
            }
            catch (Exception e)
            {
            }
        }
        else
        {
            CustomToastMessage.getInstance().showMessage(this,
                    CustomToastMessage.MessageType.ERROR,
                    CustomToastMessage.MessageTimeOut.SHORT,
                    getString(R.string.error_app_no_internet));
        }
    }

    private void onPlayStoreButtonClicked()
    {
        if (InternetConnection.getInstance(this).isNetworkAvailable())
        {
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object

            try
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            }
            catch (ActivityNotFoundException anfe)
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
        else
        {
            CustomToastMessage.getInstance().showMessage(this,
                    CustomToastMessage.MessageType.ERROR,
                    CustomToastMessage.MessageTimeOut.SHORT,
                    getString(R.string.error_app_no_internet));
        }
    }
}
