package com.ess.essandroidbaselibrary.communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import com.ess.essandroidbaselibrary.Interface.SMSReceivedAction;


/**
 * eSS Technologies (pvt) Ltd.
 * All right reserved.
 * <p>
 * Created by Malith on 1/1/2019.
 */

public class SmsReceiver extends BroadcastReceiver
{
    private SMSReceivedAction smsReceivedAction;

    public SmsReceiver(SMSReceivedAction smsReceivedAction)
    {
        this.smsReceivedAction = smsReceivedAction;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        onSMSReceived(context, intent);
    }

    private void onSMSReceived(Context context, Intent intent)
    {
        try
        {
            Bundle bundle = intent.getExtras();
            SmsMessage[] messages;
            String messageBody = "";
            String sender = "";

            if (bundle != null)
            {
                //---retrieve the SMS message received---
                if (Build.VERSION.SDK_INT >= 19)
                {
                    //KITKAT
                    messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                }
                else
                {
                    Object pdus[] = (Object[]) bundle.get("pdus");
                    messages = new SmsMessage[pdus.length];
                }

                // Make as single message.
                for (int i = 0; i < messages.length; ++i)
                {
                    messageBody += messages[i].getMessageBody().toString();
                }

                sender = messages[0].getOriginatingAddress();

                //--- Notify the new SMS message is received ---
                if (smsReceivedAction != null)
                {
                    smsReceivedAction.onSMSReceived(sender, messageBody);
                }
            }
        }
        catch (Exception e)
        {}
    }
}
