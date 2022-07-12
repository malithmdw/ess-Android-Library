package com.ess.essandroidbaselibrary.util.uIUtilities;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.ess.essandroidbaselibrary.R;

/**
 * eSS Technologies (pvt) Ltd.
 * All right reserved.
 * <p>
 * Created by Malith on 12/26/2018.
 */

public class NotificationView
{
    private static final NotificationView ourInstance = new NotificationView();
    private static final String CHANNEL_ID = "1";

    public static NotificationView getInstance() {
        return ourInstance;
    }

    private NotificationView()
    {
    }

    public void showPlainNotification(Activity activity, String title, String description, int icon)
    {
        int image = R.drawable.info_message_icon;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity)
                .setSmallIcon(image)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Add as notification
        NotificationManager manager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    public void showWebLinkedNotification(Activity activity, String title, String description, String url, int icon)
    {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Add as notification
        NotificationManager manager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    public void showNewVersionNotification(Activity activity)
    {
        String title = "Update available";
        String description = "New version of " + activity.getString(R.string.app_name) + " is now available.";
        String url = "https://play.google.com/store/apps/details?id=" + activity.getPackageName();
        int icon = R.drawable.icon;

        showWebLinkedNotification(activity, title, description, url, icon);
    }
}
