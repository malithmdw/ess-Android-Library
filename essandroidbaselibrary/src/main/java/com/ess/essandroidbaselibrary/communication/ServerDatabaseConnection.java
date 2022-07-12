package com.ess.essandroidbaselibrary.communication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
            INSTANCE = new ApacheHTTPConnector();//new VolleyHTTPConnector();//
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
