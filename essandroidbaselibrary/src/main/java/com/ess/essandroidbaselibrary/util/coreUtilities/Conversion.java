package com.ess.essandroidbaselibrary.util.coreUtilities;

import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Malith on 4/21/2018.
 */

public class Conversion
{
    private static final DecimalFormat df = new DecimalFormat("####0.00");

    public static String getCurrencyString(double v)
    {
        return df.format(v);
    }

    public static String firstCharToUpperString(String str)
    {
        if (!str.isEmpty())
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        else
            return str;
    }

    public static Date getDateBeforeDays(int days)
    {
        java.util.Date today = new java.util.Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_MONTH, -days);

        return new Date(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
    }

    public static String objectToSerializedString(Serializable o)
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(
                    new Base64OutputStream(baos, Base64.NO_PADDING
                            | Base64.NO_WRAP));
            oos.writeObject(o);
            oos.close();
            return baos.toString("UTF-8");
        }
        catch (IOException e)
        {
            return null;
        }
    }

    public static Object stringToSerializableObject(String str)
    {
        try
        {
            return new ObjectInputStream(new Base64InputStream(
                    new ByteArrayInputStream(str.getBytes()), Base64.NO_PADDING
                    | Base64.NO_WRAP)).readObject();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static String getPreFixedString(int value, int length, char fill)
    {
        String str = Integer.toString(value);
        StringBuilder sb = new StringBuilder();

        for (int toPrepend = length - str.length(); toPrepend > 0; toPrepend--)
        {
            sb.append(fill);
        }

        sb.append(str);
        return sb.toString();
    }
}
