package com.ess.essandroidbaselibrary.util.coreUtilities;

import com.google.android.gms.maps.model.LatLng;

/**
 * eSS Technologies (pvt) Ltd.
 * All right reserved.
 * <p>
 * Created by Malith on 6/9/2018.
 */

public class MathsUtilities
{
    private static final int EARTH_RADIUS = 6371;

    public static double bearingDegreeAngleBetweenTwoLocations(LatLng from, LatLng to)
    {
        double x = to.latitude - from.latitude;
        double y = to.longitude - from.longitude;

        if (y == 0)
        {
            return 0;
        }

        double res = Math.atan2(x, y);

        if (res < 0)
        {
            res = (2 * Math.PI) + res;
        }

        if (!(0 <= res && res < 360))
        {
            res = 0;
        }

        return res * 180 / Math.PI;
    }

    public static double distance2DBetweenTwoLocations(LatLng from, LatLng to)
    {
        return Math.sqrt(Math.pow(from.latitude - to.latitude, 2) + Math.pow(from.longitude - to.longitude, 2));
    }

    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     * @returns Distance in Meters
     */
    public static double earthDistanceBetweenTwoLocations(LatLng from, LatLng to)
    {
        double latDistance = Math.toRadians(to.latitude - from.latitude);
        double lonDistance = Math.toRadians(to.longitude - from.longitude);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(from.latitude)) * Math.cos(Math.toRadians(to.latitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c * 1000; // convert to meters

        return Math.sqrt(Math.pow(distance, 2));
    }
}
