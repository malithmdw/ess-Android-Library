package com.ess.essandroidbaselibrary.util.coreUtilities;

import java.util.Calendar;

/**
 * eSS Technologies (pvt) Ltd.
 * All right reserved.
 * <p>
 * Created by Malith on 12/12/2018.
 */

public class DateTimeUtility
{
    public static DateTime getCurrentTimestamp()
    {
        java.util.Date date = new java.util.Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final Time currentTime = new Time(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        Date today = new Date(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        return new DateTime(today, currentTime);
    }

    public static Date getToday() {
        java.util.Date date = new java.util.Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return new Date(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
    }

    public static Date getYesterday()
    {
        java.util.Date date = new java.util.Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        return new Date(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
    }

    /**
     * Date not considered.
     *
     * @param time
     * @param timeLowerBound
     * @param timeUpperBound
     * @return
     */
    public static boolean isWithInTimeFrame(Time time, Time timeLowerBound, Time timeUpperBound)
    {
        int fromStamp = (timeLowerBound.getHourIn24() * 100) + timeLowerBound.getMinutes();

        if (fromStamp < 0)
        {
            fromStamp = 0;
        }

        int toStamp = (timeUpperBound.getHourIn24() * 100) + timeUpperBound.getMinutes();

        int currentStamp = (time.getHourIn24() * 100) + time.getMinutes();

        if (fromStamp < toStamp)
        {
            return (fromStamp <= currentStamp && currentStamp <= toStamp);
        }
        else
        {
            // Go through 00:00
            return ((fromStamp <= currentStamp && currentStamp <= 2400)
                    || (0 <= currentStamp && currentStamp <= toStamp));
        }
    }

    public static int getTimeDifferenceMinutes(Time time1, Time time2)
    {
        return ((time1.getHourIn24() * 60) + (time1.getMinutes())
                    - (time2.getHourIn24() * 60) + (time2.getMinutes()));
    }

    /**
     * Add two times,
     * If the time is in next day, the date is not considered. result is arrange as a time.
     *
     * @param t1
     * @param t2
     * @return
     */
    public static Time addTime(Time t1, Time t2)
    {
        int h = t1.getHourIn24() + t2.getHourIn24();
        int m = t1.getMinutes() + t2.getMinutes();
        int s = t1.getSeconds() + t2.getSeconds();

        if (s > 59)
        {
            m = m + (int) (s / 60);
            s = s % 60;
        }

        if (m > 59)
        {
            h = h + (int) (m / 60);
            m = m % 60;
        }

        if (h > 23)
        {
            h = h % 24;
        }

        return new Time(h, m, s);
    }

    /**
     * t1 - t2
     *
     * @param t1
     * @param t2
     * @return
     */
    public static Time subtractTime(Time t1, Time t2)
    {
        int h = t1.getHourIn24();
        int m = t1.getMinutes();
        int s;

        // Seconds
        if (t1.getSeconds() >= t2.getSeconds())
        {
            s = t1.getSeconds() - t2.getSeconds();
        }
        else
        {
            s = 60 + t1.getSeconds() - t2.getSeconds();

            if (t1.getMinutes() > 0)
            {
                m = m - 1;
            }
            else
            {
                m = 60 + m - t2.getMinutes();

                if (t1.getHourIn24() > 0)
                {
                    h = h - 1;
                }
                else
                {
                    h = 23;
                }
            }
        }

        // minutes
        if (m >= t2.getMinutes())
        {
            m = m - t2.getMinutes();
        }
        else
        {
            m = 60 + m - t2.getMinutes();

            if (h > 0)
            {
                h = h - 1;
            }
            else
            {
                h = 23;
            }
        }

        // hours
        if (h >= t2.getHourIn24())
        {
            h = h - t2.getHourIn24();
        }
        else
        {
            h = 24 + h - t2.getHourIn24();
        }

        return new Time(h, m, s);
    }

    public static Date getNextDate(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(date.getYear(), date.getMonth() - 1, date.getDay());
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return new Date(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
    }

    public static long getDiffInSecs(DateTime dt1, DateTime dt2)
    {
        java.util.Date d1 = new java.util.Date(dt1.getDate().getYear() - 1900, dt1.getDate().getMonth() -1 , dt1.getDate().getDay(), dt1.getTime().getHourIn24(), dt1.getTime().getMinutes());
        java.util.Date d2 = new java.util.Date(dt2.getDate().getYear() - 1900, dt2.getDate().getMonth() - 1, dt2.getDate().getDay(), dt2.getTime().getHourIn24(), dt2.getTime().getMinutes());

        try
        {
            //in milliseconds
            long diff = d1.getTime() - d2.getTime();

//            long diffSeconds = diff / 1000 % 60;
//            long diffMinutes = diff / (60 * 1000) % 60;
//            long diffHours = diff / (60 * 60 * 1000) % 24;
//            long diffDays = diff / (24 * 60 * 60 * 1000);

            return (diff / 1000);
        }
        catch (Exception e)
        {
        }
        return 0;
    }
}
