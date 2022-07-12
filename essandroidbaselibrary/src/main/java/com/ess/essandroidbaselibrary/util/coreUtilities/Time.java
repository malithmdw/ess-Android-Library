package com.ess.essandroidbaselibrary.util.coreUtilities;

import java.io.Serializable;

/**
 * Created by Malith on 5/13/2018.
 */

public class Time implements Serializable
{
    private static final long serialVersionUID = 400L;

    public enum AMorPM
    {
        AM,
        PM,
        noon
    }

    private int hour; // Keep in 24 hour format
    private int minutes;
    private int seconds;
    private AMorPM aMorPM;

    /**
     * params should be 12 hour format
     * @param hour
     * @param minutes
     */
    public Time(int hour, int minutes, AMorPM aMorPM)
    {
        if (AMorPM.PM.equals(aMorPM))
        {
            this.hour = hour + 12;
        }
        else
        {
            this.hour = hour;
        }

        this.minutes = minutes;
        this.aMorPM = aMorPM;
    }

    /**
     * Input is HH:mm:ss in 24 hour format
     * @param in
     */
    public Time(String in)
    {
        String[] segments = in.split(":");
        this.hour = Integer.parseInt(segments[0]);
        this.minutes = Integer.parseInt(segments[1]);

        if (segments.length > 2)
        {
            this.seconds = Integer.parseInt(segments[2]);
        }

        if (this.hour == 12 && this.minutes == 0)
        {
            this.aMorPM = AMorPM.noon;
        }
        else if (this.hour < 12)
        {
            this.aMorPM = AMorPM.AM;
        }
        else
        {
            this.aMorPM = AMorPM.PM;
        }
    }

    /**
     * params should be 24 hour format
     * @param hour
     * @param minutes
     */
    public Time(int hour, int minutes)
    {
        if (hour == 12 && minutes == 0)
        {
            this.aMorPM = AMorPM.noon;
        }
        else if (hour < 12)
        {
            this.aMorPM = AMorPM.AM;
        }
        else
        {
            this.aMorPM = AMorPM.PM;
        }

        this.hour = hour;
        this.minutes = minutes;
    }

    public Time(int hour, int minutes, int seconds)
    {
        if (hour == 12 && minutes == 0)
        {
            this.aMorPM = AMorPM.noon;
        }
        else if (hour < 12)
        {
            this.aMorPM = AMorPM.AM;
        }
        else
        {
            this.aMorPM = AMorPM.PM;
        }

        this.hour = hour;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public String getDBFormatTime()
    {
        String res = "";

        int h = hour;

        if (h < 10)
        {
            res += "0" + Integer.toString(h);
        }
        else
        {
            res += Integer.toString(h);
        }

        res += ":";

        if (minutes < 10)
        {
            res += "0" + Integer.toString(minutes);
        }
        else
        {
            res += Integer.toString(minutes);
        }

        res += ":";

        if (seconds < 10)
        {
            res += "0" + Integer.toString(seconds);
        }
        else
        {
            res += Integer.toString(seconds);
        }

        return res;
    }

    public String getTimeDisplayString()
    {
        if (this.aMorPM.equals(AMorPM.noon))
        {
            return "12.00 " + aMorPM.name();
        }

        String minutesStr = (minutes < 10) ? ("0" + minutes) : Integer.toString(minutes);

        if (hour > 12)
        {
            return (hour % 12) + "." + minutesStr + " " + aMorPM.name();
        }
        else
        {
            if (hour % 12 == 0)
            {
                return "12." + minutesStr + " " + aMorPM.name();
            }
            else
            {
                return (hour % 12) + "." + minutesStr + " " + aMorPM.name();
            }
        }
    }

    public int getHourIn24()
    {
        return hour;
    }

    public int getMinutes()
    {
        return minutes;
    }

    public int getSeconds()
    {
        return seconds;
    }

    public boolean isGraterThan(Time time)
    {
        if (getHourIn24() > time.getHourIn24())
        {
            return true;
        }
        else if (getHourIn24() == time.getHourIn24())
        {
            if (getMinutes() > time.getMinutes())
            {
                return true;
            }
            else if (getMinutes() == time.getMinutes() && getSeconds() > time.getSeconds())
            {
                return true;
            }
        }
        return false;
    }
}