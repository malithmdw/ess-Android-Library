package com.ess.essandroidbaselibrary.communication;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * eSS Technologies (pvt) Ltd.
 * All right reserved.
 * <p>
 * Created by Malith on 12/15/2018.
 */

public class SMSConnection
{
    private static final SMSConnection ourInstance = new SMSConnection();
    private static final int REQUEST_READ_PHONE_STATE = 10;
    private static Activity mActivity;

    public static SMSConnection getInstance(Activity activity)
    {
        SMSConnection.mActivity = activity;
        return ourInstance;
    }

    private SMSConnection()
    {
    }

    public static boolean sendSMS(String receiverNumber, String smsText)
    {
        return sendSMS(null, 0, null, receiverNumber, smsText, null, null);
    }

    public static boolean sendSMS(int subscriptionId, String receiverNumber,String smsText)
    {
        return sendSMS(subscriptionId, 0, null, receiverNumber, smsText, null, null);
    }

    public static boolean sendSMS(Integer subscriptionId, int simID, String toNum, String centerNum, String smsText, PendingIntent sentIntent, PendingIntent deliveryIntent)
    {
        if (Build.VERSION.SDK_INT > 22)
        {
            try
            {
                int permissionCheck = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_PHONE_STATE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
                    return false;
                }

                ArrayList<Integer> subscriptionIds = new ArrayList<>();
                int exactSubscriptionId = -1;

                if (subscriptionId == null)
                {
                    SubscriptionManager subscriptionManager = SubscriptionManager.from(mActivity.getApplicationContext());
                    List<SubscriptionInfo> subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();

                    for (SubscriptionInfo subscriptionInfo : subscriptionInfoList)
                    {
                        if (subscriptionInfo.getNumber() != null && isSameMobileNumber(subscriptionInfo.getNumber(), centerNum))
                        {
                            exactSubscriptionId = subscriptionInfo.getSubscriptionId();
                        }
                        else if (subscriptionInfo.getNumber() == null)
                        {
                            subscriptionIds.add(subscriptionInfo.getSubscriptionId());
                        }
                    }
                }
                else
                {
                    exactSubscriptionId = subscriptionId;
                }

                if (exactSubscriptionId != -1)
                {
                    SmsManager.getSmsManagerForSubscriptionId(exactSubscriptionId).
                            sendTextMessage(centerNum, null, smsText, null, null);
                }
                else
                {
                    for (int id : subscriptionIds)
                    {
                        SmsManager.getSmsManagerForSubscriptionId(id).
                                sendTextMessage(centerNum, null, smsText, null, null);
                    }
                }

                return true;
            }
            catch (Exception e)
            {
                Logger.getLogger("Unable to send SMS");
            }
            return false;
        }
        else
        {
            String name;

            try {
                if (simID == 0)
                {
                    name = "isms";
                    // for model : "Philips T939" name = "isms0"
                }
                else if (simID == 1)
                {
                    name = "isms2";
                }
                else if (simID == 2)
                {
                    name = "isms0";
                }
                else
                {
                    throw new Exception("can not get service which for sim '" + simID + "', only 0,1 accepted as values");
                }
                Method method = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", String.class);
                method.setAccessible(true);
                Object param = method.invoke(null, name);

                method = Class.forName("com.android.internal.telephony.ISms$Stub").getDeclaredMethod("asInterface", IBinder.class);
                method.setAccessible(true);
                Object stubObj = method.invoke(null, param);

                if (Build.VERSION.SDK_INT < 18)
                {
                    method = stubObj.getClass().getMethod("sendText", String.class, String.class, String.class, PendingIntent.class, PendingIntent.class);
                    method.invoke(stubObj, toNum, centerNum, smsText, sentIntent, deliveryIntent);
                }
                else
                {
                    method = stubObj.getClass().getMethod("sendText", new Class[]{String.class, String.class, String.class, PendingIntent.class, PendingIntent.class});
                    method.invoke(stubObj, mActivity.getPackageName(), toNum, centerNum, smsText, sentIntent, deliveryIntent);
                }

                return true;
            } catch (ClassNotFoundException e) {
                Log.e("apipas", "ClassNotFoundException:" + e.getMessage());
            } catch (NoSuchMethodException e) {
                Log.e("apipas", "NoSuchMethodException:" + e.getMessage());
            } catch (InvocationTargetException e) {
                Log.e("apipas", "InvocationTargetException:" + e.getMessage());
            } catch (IllegalAccessException e) {
                Log.e("apipas", "IllegalAccessException:" + e.getMessage());
            } catch (Exception e) {
                Log.e("apipas", "Exception:" + e.getMessage());
            }
            return false;
        }
    }


    public static boolean sendMultipartTextSMS(Context ctx, int simID, String toNum, String centerNum, ArrayList<String> smsTextlist, ArrayList<PendingIntent> sentIntentList, ArrayList<PendingIntent> deliveryIntentList) {
        String name;
        try {
            if (simID == 0) {
                name = "isms";
                // for model : "Philips T939" name = "isms0"
            } else if (simID == 1) {
                name = "isms2";
            } else {
                throw new Exception("can not get service which for sim '" + simID + "', only 0,1 accepted as values");
            }
            Method method = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", String.class);
            method.setAccessible(true);
            Object param = method.invoke(null, name);

            method = Class.forName("com.android.internal.telephony.ISms$Stub").getDeclaredMethod("asInterface", IBinder.class);
            method.setAccessible(true);
            Object stubObj = method.invoke(null, param);
            if (Build.VERSION.SDK_INT < 18) {
                method = stubObj.getClass().getMethod("sendMultipartText", String.class, String.class, List.class, List.class, List.class);
                method.invoke(stubObj, toNum, centerNum, smsTextlist, sentIntentList, deliveryIntentList);
            } else {
                method = stubObj.getClass().getMethod("sendMultipartText", String.class, String.class, String.class, List.class, List.class, List.class);
                method.invoke(stubObj, ctx.getPackageName(), toNum, centerNum, smsTextlist, sentIntentList, deliveryIntentList);
            }
            return true;
        } catch (ClassNotFoundException e) {
            Log.e("apipas", "ClassNotFoundException:" + e.getMessage());
        } catch (NoSuchMethodException e) {
            Log.e("apipas", "NoSuchMethodException:" + e.getMessage());
        } catch (InvocationTargetException e) {
            Log.e("apipas", "InvocationTargetException:" + e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e("apipas", "IllegalAccessException:" + e.getMessage());
        } catch (Exception e) {
            Log.e("apipas", "Exception:" + e.getMessage());
        }
        return false;
    }

//    public void sendTextMessage(String clientNumber, String message)
//    {
//        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
//        {
//            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.SEND_SMS}, 1);
//        }
//        else
//        {
//            try
//            {
//                //SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(1); // API level 22 or upward
//                SmsManager smsManager = SmsManager.getDefault();
//                smsManager.sendTextMessage(clientNumber, null, message, null, null);
//            }
//            catch (IllegalArgumentException e)
//            {
//            }
//        }
//    }
//
//    public void sendTextMessage(int simID, String clientNumber, String message)
//    {
//        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
//        {
//            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.SEND_SMS}, 1);
//        }
//        else
//        {
//            String name = "isms0";
//            try
//            {
//                if (simID == 0)
//                {
//                    name = "isms0";
//                }
//                else if (simID == 1)
//                {
//                    name = "isms1";
//                }
//                else if (simID == 2)
//                {
//                    name = "isms2";
//                }
//
//                Method method = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService",String.class);
//                method.setAccessible(true);
//                Object paramObj = method.invoke(null, name);
//
//                method = Class.forName("com.android.internal.telephony.ISms$Stub").getDeclaredMethod("asInterface", IBinder.class);
//                method.setAccessible(true);
//                Object stubObj = method.invoke(null, paramObj);
//
//                if (Build.VERSION.SDK_INT < 18)
//                {
//                    method = stubObj.getClass().getMethod("sendText", String.class, String.class, String.class, PendingIntent.class,PendingIntent.class);
//                    method.invoke(stubObj, clientNumber, null, message, null, null);
//                }
//                else
//                {
//                    method = stubObj.getClass().getMethod("sendText", String.class, String.class, String.class, PendingIntent.class,PendingIntent.class);
//                    method.invoke(stubObj, mActivity.getPackageName(), clientNumber, null, message, null, null);
//                }
//            }
//            catch (IllegalArgumentException e)
//            {
//            }
//            catch (Exception ex)
//            {
//                int i=0;
//                i++;
//            }
//        }
//    }

    /**
     * Only for API LEVEL 22 or above.
     * @return
     */
    public List<SubscriptionInfo> getSubscriptionInfoList()
    {
        if (Build.VERSION.SDK_INT > 22)
        {
            SubscriptionManager subscriptionManager = SubscriptionManager.from(mActivity.getApplicationContext());
            return subscriptionManager.getActiveSubscriptionInfoList();
        }
        return new ArrayList<>();
    }

    public void deleteSMS(Context context, String message, String number)
    {
        try
        {
            Uri uriSms = Uri.parse("content://sms/inbox");
            Cursor c = context.getContentResolver().query(uriSms,
                    new String[] { "_id", "thread_id", "address",
                            "person", "date", "body" }, null, null, null);

            if (c != null && c.moveToFirst())
            {
                do
                {
                    long id = c.getLong(0);
                    long threadId = c.getLong(1);
                    String address = c.getString(2);
                    String body = c.getString(5);

                    if (message.equals(body) && address.equals(number))
                    {
                        context.getContentResolver().delete(Uri.parse("content://sms/" + id), null, null);
                        break;
                    }
                } while (c.moveToNext());
            }
        }
        catch (Exception e)
        {
        }
    }

    public static boolean isSameMobileNumber(String n1, String n2)
    {
        if (n1.length() > 8 && n2.length() > 8)
            return n1.substring(n1.length() - 9).equals(n2.substring(n2.length() - 9));
        else
            return n1.trim().equals(n2.trim());
    }

    public ArrayList<String> getSIMNames()
    {
        ArrayList<String> names = new ArrayList<>();
        int index = 1;
        if (Build.VERSION.SDK_INT > 22)
        {
            try
            {
                SubscriptionManager subscriptionManager = SubscriptionManager.from(mActivity.getApplicationContext());
                for (SubscriptionInfo info : subscriptionManager.getActiveSubscriptionInfoList())
                {
                    if (info.getCarrierName() != null && info.getCarrierName().length() != 0)
                    {
                        names.add((String) info.getCarrierName());
                    }
                    else if (info.getDisplayName() != null && info.getDisplayName().length() != 0)
                    {
                        names.add((String) info.getDisplayName());
                    }
                    else
                    {
                        names.add("SIM " + index);
                    }
                    index++;
                }
            }
            catch (SecurityException e)
            {}
            catch (Exception ex)
            {}
        }

        return names;
    }
    public ArrayList<Integer> getSubscriptionIds()
    {
        ArrayList<Integer> names = new ArrayList<>();

        if (Build.VERSION.SDK_INT > 22)
        {
            SubscriptionManager subscriptionManager = SubscriptionManager.from(mActivity.getApplicationContext());
            for (SubscriptionInfo info : subscriptionManager.getActiveSubscriptionInfoList())
            {
                names.add(info.getSubscriptionId());
            }
        }

        return names;
    }
}
