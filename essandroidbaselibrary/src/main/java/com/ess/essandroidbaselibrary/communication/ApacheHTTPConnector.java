package com.ess.essandroidbaselibrary.communication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import androidx.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
/*
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
*/
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
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

public class ApacheHTTPConnector extends ServerDatabaseConnection
{
    private static final String UTF_ENCODE = "UTF-8";
    private static final int NETWORK_TIMEOUT_SEC = 25;//15;

    public JSONArray getData(String fullURLOfPHPFile, String parseMethod, List<Pair<String, String>> parameters)
    {
        List<NameValuePair> params = new ArrayList<>();

        if (parameters != null)
        {
            for (Pair<String, String> pair : parameters)
            {
                params.add(new BasicNameValuePair(pair.first, pair.second));
            }
        }

//        try
//        {
            return makeHttpRequest(fullURLOfPHPFile, parseMethod, params);
            //return new JSONArrayGetter(params).execute(fullURLOfPHPFile, parseMethod).get();
//        }
//        catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }
//        catch (ExecutionException e)
//        {
//            e.printStackTrace();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        return null;
    }

    public Bitmap getImage(String fullUrlOfImage)
    {
        try
        {
            return new ApacheHTTPConnector.ImageGetter(fullUrlOfImage).execute().get();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public boolean uploadImage(String phpFilePath, String relativeURLForSave, Bitmap image)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] byteImage = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteImage,Base64.DEFAULT);

        // Upload image to server
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("image_url", relativeURLForSave));
        params.add(new BasicNameValuePair("image", encodedImage));

        try
        {
            JSONArray res = new ApacheHTTPConnector.JSONArrayGetter(params).execute(phpFilePath, "POST").get();

            if (res != null && res.getJSONObject(0) != null && res.getJSONObject(0).getString("state").equals("true"))
            {
                return true;
            }
        }
        catch (Exception e)
        {
            Log.v("log_tag", "Error in http connection " + e.toString());
        }
        return false;
    }


    private class ImageGetter extends AsyncTask<String, Void, Bitmap>
    {
        String url = "";
        ImageGetter(String url)
        {
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(String... strings)
        {
            try
            {
                URLConnection urlConnection = new URL(url).openConnection();
                return BitmapFactory.decodeStream((InputStream) urlConnection.getContent(), null, null);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (Exception e)
            {
            }
            return null;
        }
    }

    private class JSONArrayGetter extends AsyncTask<String, Void, JSONArray>
    {
        private List<NameValuePair> parameters;

        public JSONArrayGetter(List<NameValuePair> parameters)
        {
            this.parameters = parameters;
        }

        @Override
        protected JSONArray doInBackground(String... params)
        {
            final List<String> strList = new ArrayList<>();
            Collections.addAll(strList, params);

            String fileName = strList.get(0);
            String method = strList.get(1);

            return makeHttpRequest(fileName, method, parameters);
        }
    }

    /**
     * Method to issue HTTP request, parse JSON result and return JSON Object.
     *
     * @param url
     * @param method
     * @param params
     * @return
     *          - JSONArray - success.
     *          - Null - failed any process.
     *          - empty JSONArray - empty data retrieved.
     */
    @Nullable
    private JSONArray makeHttpRequest(String url, String method, List<NameValuePair> params)
    {
        // Response from the HTTP Request
        InputStream httpResponseStream = null;
        // JSON Response String to create JSON Object
        String jsonString = "";

        /**
         * ///////////////////////////////////////////////////////////////////////
         *  Create HTTP response
         * //////////////////////////////////////////////////////////////////////
         */
        try
        {
            // get a Http client
            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter("http.connection.timeout", NETWORK_TIMEOUT_SEC * 1000);
            httpClient.getParams().setParameter("http.socket.timeout", NETWORK_TIMEOUT_SEC * 1000);

            // If required HTTP method is POST
            if ("POST".equals(method))
            {
                // Create a Http POST object
                final HttpPost httpPost = new HttpPost(url);

                // Encode the passed parameters into the Http request
                if (params != null)
                    httpPost.setEntity(new UrlEncodedFormEntity(params, (String) null));
                // Execute the request and fetch Http response
                HttpResponse httpResponse = httpClient.execute(httpPost);
                // Extract the result from the response
                HttpEntity httpEntity = httpResponse.getEntity();
                // Open the result as an input stream for parsing
                httpResponseStream = httpEntity.getContent();
            }
            // Else if it is GET
            else if ("GET".equals(method))
            {
                // Format the parameters correctly for HTTP transmission
                String paramString = URLEncodedUtils.format(params, UTF_ENCODE);
                // Add parameters to url in GET format
                url += "?" + paramString;
                // Execute the request
                HttpGet httpGet = new HttpGet(url);
                // Execute the request and fetch Http response
                HttpResponse httpResponse = httpClient.execute(httpGet);
                // Extract the result from the response
                HttpEntity httpEntity = httpResponse.getEntity();
                // Open the result as an input stream for parsing
                httpResponseStream = httpEntity.getContent();
            }
            // Catch Possible Exceptions
        }
        catch (UnknownHostException ex)
        {
            // Catching this is essential - throw in limited or less internet connections.
            // (Failed to connect to server: java.net.UnknownHostException: Unable to resolve host "play.googleapis.com")
            return null;
        }
        catch (UnsupportedEncodingException e)
        {
            return null;
        }
        catch (ClientProtocolException e)
        {
            return null;
        }
        catch (IOException e)
        {
            // java.net.SocketTimeoutException: SSL handshake timed out
            return null;
        }
        catch (Exception ex)
        {
            return null;
        }

        if (httpResponseStream == null)
        {
            return null;
        }

        /**
         * ///////////////////////////////////////////////////////////////////////
         *  Retrieve data from httpResponseStream
         * //////////////////////////////////////////////////////////////////////
         */
        try
        {
            // Create buffered reader for the httpResponceStream
            BufferedReader httpResponseReader = new BufferedReader(new InputStreamReader(httpResponseStream, "iso-8859-1"), 8);
            // String to hold current line from httpResponseReader
            String line = null;

            if (httpResponseReader != null)
            {
                // While there is still more response to read
                while ((line = httpResponseReader.readLine()) != null)
                {
                    // Add line to jsonString
                    jsonString += (line + "\n");
                }
            }
        }
        catch (IOException e)
        {
            return null;
        }
        catch (Exception e)
        {
            return null;
        }

        try
        {
            // Close Response Stream
            httpResponseStream.close();
        }
        catch (IOException e)
        {}
        catch (Exception e)
        {}


        /**
         * ///////////////////////////////////////////////////////////////////////
         *  Filter the response and prepare result.
         * //////////////////////////////////////////////////////////////////////
         */
        if (!jsonString.isEmpty())
        {
            try
            {
                // Create jsonObject from the jsonString and return it
                return new JSONArray(jsonString);
            }
            catch (JSONException e)
            {
            }
        }
        else
        {

            return new JSONArray();
        }


        // Return null if in error
        return null;
    }
}
