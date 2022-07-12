package com.ess.essandroidbaselibrary.communication;

import android.app.Activity;
import android.content.IntentFilter;

import com.ess.essandroidbaselibrary.Interface.SMSReceivedAction;

/**
 * eSS Technologies (pvt) Ltd.
 * All right reserved.
 * <p>
 * Created by Malith on 1/1/2019.
 */

public class SMSReceiverHandler
{
    private static SMSReceiverHandler INSTANCE;;
    private static SmsReceiver smsReceiver;

    private SMSReceiverHandler()
    {}

    public static SMSReceiverHandler getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new SMSReceiverHandler();
        }

        return INSTANCE;
    }

    public void attachReceiver(Activity activity, SMSReceivedAction smsReceivedAction)
    {
        // Create an IntentFilter instance.
        IntentFilter intentFilter = new IntentFilter();

        // Add SMS receive change action.
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");

        // Set broadcast receiver priority.
        //intentFilter.setPriority(100);

        // Create a SMS receive broadcast receiver.
        smsReceiver = new SmsReceiver(smsReceivedAction);

        // Register the SMS receiver with the intent filter object.
        activity.registerReceiver(smsReceiver, intentFilter);
    }

    public void destroyReceiver(Activity activity)
    {
        // If the broadcast receiver is not null then unregister it.
        // This action is better placed in activity onDestroy() method.
        if(smsReceiver != null)
        {
            try
            {
                activity.unregisterReceiver(smsReceiver);
                smsReceiver = null;
            }
            catch (IllegalArgumentException ignored)
            {
                //java.lang.IllegalArgumentException: Receiver not registered:
            }
            catch (Exception ignored)
            {}
        }
    }
}
