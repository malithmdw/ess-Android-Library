package com.ess.essandroidbaselibrary.util.uIUtilities;

import android.app.Activity;
import android.view.Gravity;

/**
 * Created by Malith on 4/16/2018.
 */

public class MessageBox
{
    // Singleton implementation
    private static MessageBox INSTANCE;

    public enum MessageTextAlignment
    {
        CENTER,
        LEFT,
        RIGHT;

        public static int getGravity(MessageTextAlignment alignment)
        {
            switch (alignment)
            {
                case CENTER:
                    return Gravity.CENTER;
                case LEFT:
                    return Gravity.LEFT;
                case RIGHT:
                    return Gravity.RIGHT;
                default:
                    return Gravity.CENTER;
            }
        }
    }

    private void MessageBox()
    {
    }

    public static MessageBox getInstance()
    {
        if(INSTANCE == null)
        {
            INSTANCE = new MessageBox();
        }
        return INSTANCE;
    }
    // Singleton implementation - END

    Activity mActivity;
    public void showMessage(Activity context, String messageTitle, String message)
    {
        this.mActivity = context;
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mActivity);
        builder.setCancelable(true);
        builder.setTitle(messageTitle);
        builder.setMessage(message);
        builder.show();
    }
}
