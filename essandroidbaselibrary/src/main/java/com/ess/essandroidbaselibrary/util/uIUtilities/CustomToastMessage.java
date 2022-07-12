package com.ess.essandroidbaselibrary.util.uIUtilities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ess.essandroidbaselibrary.R;

public class CustomToastMessage extends AppCompatActivity
{
    private static TextView messageTextView;
    private static ImageView messageImageView;
    private Toast toast;

    // Singleton implementation
    private static CustomToastMessage INSTANCE;

    void CustomToastMessageView()
    {
    }

    public static CustomToastMessage getInstance()
    {
        if(INSTANCE == null)
        {
            INSTANCE = new CustomToastMessage();
        }
        return INSTANCE;
    }
    // Singleton implementation - END

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_toast_message);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    Activity activity;// Activity needs to show.
    private static int activityID = R.layout.activity_custom_toast_message;
    private static int layoutID = R.id.custom_toast_layout;
    private MessagePosition messagePosition = MessagePosition.BOTTOM; // always Bottom

    private void showMessage(String text, int messageColorID, int imageComponentID, int timeOut)
    {
        // Creating the LayoutInflater instance.
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        // Getting the View object as defined in the custom toast XML file.
        View view =  activity.findViewById(layoutID);

        View layout = layoutInflater.inflate(activityID, (ViewGroup) view);
        //layout.set

        layout.setBackgroundResource(messageColorID);
        messageImageView = (ImageView) layout.findViewById(R.id.custom_toast_image);
        messageImageView.setImageResource(imageComponentID);
        messageTextView = (TextView) layout.findViewById(R.id.custom_toast_message);
        messageTextView.setText(text);

        int position;
        switch (messagePosition)
        {
            case TOP:
                position = Gravity.TOP;
                break;
            case BOTTOM:
                position = Gravity.BOTTOM;
                break;
            case FULL_SCREEN:
                position = Gravity.CENTER;// TODO - change to
                //layout.(R.dimen.custom_toast_height);
                break;
            default:
                position = Gravity.BOTTOM;
                break;

        }

        // Creating the Toast object.
        toast = new Toast(activity.getApplicationContext());
        toast.setDuration(timeOut);
        toast.setGravity(Gravity.FILL_HORIZONTAL|position, 0, 0);
        toast.setView(layout);//setting the view of custom toast layout
        toast.show();
    }

    public enum MessagePosition
    {
        TOP,
        BOTTOM,
        FULL_SCREEN;
    }
    public enum MessageType
    {
        ERROR,
        INFO,
        WARNING;
    }

    public enum MessageTimeOut
    {
        LONG,
        SHORT;

        public int getValue(MessageTimeOut messageTimeOut)
        {
            switch (messageTimeOut)
            {
                case LONG:
                    return 1;
                case SHORT:
                    return 0;
            }
            return 0;
        }
    }

    public void showMessage(Activity activity,
                            final MessageType messageType,
                            final MessageTimeOut messageTimeOut,
                            final String messageText)
    {
        this.activity = activity;

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                switch (messageType)
                {
                    case ERROR:
                        showMessage(messageText, R.color.toast_error_color, R.drawable.error_message_icon, messageTimeOut.getValue(messageTimeOut));
                        break;
                    case INFO:
                        showMessage(messageText, R.color.toast_info_color, R.drawable.info_message_icon, messageTimeOut.getValue(messageTimeOut));
                        break;
                    case WARNING:
                        showMessage(messageText, R.color.toast_warning_color, R.drawable.warning_message_icon, messageTimeOut.getValue(messageTimeOut));
                        break;
                }
            }
        });
    }

    public void clearAllMessages()
    {
        if (toast != null)
        {
            toast.cancel();
        }
    }
}
