package com.ess.essandroidbaselibrary.communication;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class VolleyHTTPConnector extends ServerDatabaseConnection
{
    private static final int RESPONSE_WAIT_TIME_SEC = 15;

    private final RequestQueue mRequestQueue;
    private Map<StringRequest, Object> requestBlocker = new ConcurrentHashMap<>();

    public VolleyHTTPConnector()
    {
        mRequestQueue = null;
    }

    public VolleyHTTPConnector(Context context)
    {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public JSONArray getData(String fullURLOfPHPFile, String parseMethod, List<Pair<String, String>> parameters)
    {
        final Map params = new HashMap();
        final JSONObject jsonObject = new JSONObject();
        final ArrayList<JSONArray> result = new ArrayList<>();

        if (parameters != null)
        {
            for (Pair<String, String> data: parameters)
            {
                params.put(data.first, data.second);
                try
                {
                    jsonObject.put(data.first, data.second);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }

        StringRequest stringRequestPOSTJSON = new StringRequest(
                Request.Method.POST,
                fullURLOfPHPFile,
                new Response.Listener()
                {
                    @Override
                    public void onResponse(Object response)
                    {
                        if (requestBlocker.get(this) != null)
                        {
                            try
                            {
                                result.add(new JSONArray(response));
                            }
                            catch (JSONException e)
                            {
                            }

                            if (requestBlocker.get(this) != null)
                            {
                                synchronized (requestBlocker.get(this))
                                {
                                    requestBlocker.get(this).notifyAll();
                                    requestBlocker.remove(this);
                                }
                            }
                        }
//                        VolleyLog.wtf(response);
                    }
                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        if (error instanceof NetworkError)
                        {
                            //Toast.makeText(getApplicationContext(), "No network available", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }

                        if (requestBlocker.get(this) != null)
                        {
                            synchronized (requestBlocker.get(this))
                            {
                                requestBlocker.get(this).notifyAll();
                                requestBlocker.remove(this);
                            }
                        }
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                return params;
            }

//            public String getBodyContentType() {
//                return "text/plain; charset=utf-8";
//            }
//
//            @Override
//            public Priority getPriority()
//            {
//                return Priority.HIGH;
//            }
//
//            @Override
//            public Map getHeaders() throws AuthFailureError
//            {
//                HashMap headers = new HashMap();
//                //headers.put("Content-Type", "application/json; charset=utf-8");
//                return headers;
//            }
//
//            @Override
//            public byte[] getBody() throws AuthFailureError
//            {
//                String requestBody = jsonObject.toString();
//
//                try
//                {
//                    return requestBody == null ? null : requestBody.getBytes("utf-8");
//                }
//                catch (UnsupportedEncodingException uee)
//                {
//                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
//                    return null;
//                }
//            }
//
            protected Response<String> parseNetworkResponse(NetworkResponse response)
            {
                String responseString = "";
                if (response != null)
                {
                    try
                    {
                        responseString = new String(response.data, "UTF-8");
                    }
                    catch (UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                    }
                }

                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        stringRequestPOSTJSON.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout()
            {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount()
            {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError
            {

            }
        });

        Object newRequestObserver = new Object();
        requestBlocker.put(stringRequestPOSTJSON, newRequestObserver);
        mRequestQueue.add(stringRequestPOSTJSON);

        synchronized (newRequestObserver)
        {
            try
            {
                newRequestObserver.wait(RESPONSE_WAIT_TIME_SEC * 1000);

                if (requestBlocker.containsKey(stringRequestPOSTJSON))
                {
                    requestBlocker.remove(stringRequestPOSTJSON);
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        return !result.isEmpty()? result.get(0) : null;
    }

    @Override
    public Bitmap getImage(String fullUrlOfImage)
    {
        /*
        RequestQueue mRequestQueue = SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue();

        ImageLoader imageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache mCache = new LruCache(10);

            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }

            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });

        networkImageView.setImageUrl(IMAGE_URL, imageLoader);
        imageLoader.get(IMAGE_URL, ImageLoader.getImageListener(
                networkImageView, R.mipmap.ic_launcher, R.drawable.ic_error));
        */
        return null;
    }

    @Override
    public boolean uploadImage(String phpFilePath, String relativeURLForSave, Bitmap image)
    {
        return false;
    }

    private void GETStringAndJSONRequest(String page_1, String page_2)
    {
        /*
        mUserDataList.clear();
        numberOfRequestsCompleted = 0;
        VolleyLog.DEBUG = true;
        String uri_page_one = String.format(BASE_URL + "/api/users?page=%1$s", page_1);
        final String uri_page_two = String.format(BASE_URL + "/api/users?page=%1$s", page_2);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri_page_one, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                VolleyLog.wtf(response, "utf-8");
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                UserList userList = mGson.fromJson(response, UserList.class);
                mUserDataList.addAll(userList.userDataList);
                ++numberOfRequestsCompleted;

            }
        }, errorListener) {

            @Override
            public Priority getPriority() {
                return Priority.LOW;
            }

        };

        mRequestQueue.add(stringRequest);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(uri_page_two, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                VolleyLog.wtf(response.toString(), "utf-8");
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();

                UserList userList = mGson.fromJson(response.toString(), UserList.class);
                mUserDataList.addAll(userList.userDataList);
                ++numberOfRequestsCompleted;

            }
        }, errorListener) {

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };

        mRequestQueue.add(jsonObjectRequest);


        mRequestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                try {
                    if (request.getCacheEntry() != null) {
                        String cacheValue = new String(request.getCacheEntry().data, "UTF-8");
                        VolleyLog.d(request.getCacheKey() + " " + cacheValue);

                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (numberOfRequestsCompleted == 2) {
                    numberOfRequestsCompleted = 0;
                    startActivity(new Intent(MainActivity.this, RecyclerViewActivity.class).putExtra("users", mUserDataList));
                }
            }
        });
        */
    }

    private void imageRequest()
    {
        /*
        RequestQueue mRequestQueue = SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        ImageRequest imageRequest = new ImageRequest(IMAGE_URL, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                if (response != null) {
                    imageView.setImageBitmap(response);
                }

            }
        }, 200, 200, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888, errorListener);

        mRequestQueue.add(imageRequest);
        */
    }
}