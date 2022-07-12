package com.ess.essandroidbaselibrary.util.coreUtilities;

import java.io.Serializable;

/**
 * eSS Technologies (pvt) Ltd.
 * All right reserved.
 * <p>
 * Created by Malith on 6/11/2018.
 */

public class DateTime implements Serializable
{
    private static final long serialVersionUID = 200L;
    private Time time;
    private Date date;

    public DateTime(Date date, Time time)
    {
        this.date = date;
        this.time = time;
    }

    public DateTime(String dbFormatString)
    {
        String[] s = dbFormatString.split(" ");

        this.date = new Date(s[0]);
        this.time = new Time(s[1]);
    }


    public Time getTime()
    {
        return time;
    }

    public Date getDate()
    {
        return date;
    }

    public String getDBString()
    {
        return date.getDatabaseFormat() + " " + time.getDBFormatTime();
    }
}
