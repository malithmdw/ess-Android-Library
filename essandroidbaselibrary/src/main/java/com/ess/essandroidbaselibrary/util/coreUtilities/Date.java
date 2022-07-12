package com.ess.essandroidbaselibrary.util.coreUtilities;

import java.io.Serializable;

/**
 * Created by Malith on 4/14/2018.
 */

public class Date implements Serializable
{
    private int day;
    private int month;
    private int year;

    public Date(String dbFormatText)
    {
        String[] s = dbFormatText.split("-");
        this.day = Integer.parseInt(s[2]);
        this.month = Integer.parseInt(s[1]);
        this.year = Integer.parseInt(s[0]);
    }

    public Date(int day, int month, int year)
    {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public void createDateFromDBString(String s)
    {
        String[] res = s.trim().split("-");

        this.year = Integer.parseInt(res[0]);
        this.month = Integer.parseInt(res[1]);
        this.day = Integer.parseInt(res[2]);
    }

    public void createDateFromDisplayString(String s)
    {
        String[] res = s.trim().split("/");

        this.year = Integer.parseInt(res[2]);
        this.month = Integer.parseInt(res[1]);
        this.day = Integer.parseInt(res[0]);
    }

    public String getDatabaseFormat()
    {
        String s = year + "-";
        if (month < 10)
            s = s + "0" + month + "-";
        else
            s = s + month + "-";

        if (day < 10)
            s = s + "0" + day;
        else
            s = s + day;

        return  s;
    }

    public String getDDMMYY()
    {
        String s = Integer.toString(year).substring(1);
        if (month < 10)
            s = "0" + month + s;
        else
            s =month + s;

        if (day < 10)
            s = "0" + day + s;
        else
            s = day + s;

        return  s;
    }


    public String getDisplayFormat()
    {
        return day + "/" + month + "/" + year;
    }

    public int getDay()
    {
        return day;
    }

    public int getMonth()
    {
        return month;
    }

    public int getYear()
    {
        return year;
    }

    public boolean equals(Object o)
    {
        Date d = (Date) o;
        return  (d.getDay() == day && d.getMonth() == month && d.getYear() == year);
    }
}