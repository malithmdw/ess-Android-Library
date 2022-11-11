package com.ess.essandroidbaselibrary.communication;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Pair;
import org.json.JSONArray;
import java.util.List;

/**
 * Created by Malith on 4/9/2018.
 */

public abstract class ServerDatabaseConnection
{
    private static ServerDatabaseConnection INSTANCE;

    private void ServerDatabaseConnection()
    {
    }

    public static ServerDatabaseConnection getInstance()
    {
        if(INSTANCE == null)
        {
            INSTANCE = new ApacheHTTPConnector();//new VolleyHTTPConnector();//new ApacheHTTPConnector();
        }
        return INSTANCE;
    }

    public static void initializeVolleyHTTPConnector(Context context)
    {
        if (INSTANCE != null && INSTANCE instanceof VolleyHTTPConnector)
        {
            INSTANCE = new VolleyHTTPConnector(context);
        }
    }

    /**
     * Execute and get data from HTTP URL.
     *
     * @param fullURLOfPHPFile
     * @param parseMethod
     * @param parameters
     * @return
     */
    public abstract JSONArray getData(String fullURLOfPHPFile, String parseMethod, List<Pair<String, String>> parameters);

    /**
     * Download and get the image bitmap from given URL.
     *
     * @param fullUrlOfImage
     * @return
     */
    public abstract Bitmap getImage(String fullUrlOfImage);

    /**
     * Upload the Image.
     * The server script is required to upload image.
     *
     * @param phpFilePath - Full URL to access server script file to read the logic of upload image.
     * @param relativeURLForSave - The URL relative to the script file location to identify upload location.
     * @param image
     * @return
     *
     * Example : phpFilePath = http://slbg.lk/API/uploadData/uploadImage.php
     *           relativeURLForSave = ../../images
     *           (absolute path for image = http://slbg.lk/API/images)
     */
    public abstract boolean uploadImage(String phpFilePath, String relativeURLForSave, Bitmap image);
}
