package com.fabianbleile.bakeryreloaded.Utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils {

    private static final String RECIPE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private static final String TAG = NetworkUtils.class.getSimpleName();
    public static final long serialVersionUID = -6257143524046623802L;

    public NetworkUtils() {}

    public static URL buildUrl()
    {
        Uri uri = Uri.parse(RECIPE_URL).buildUpon().build();
        URL url = null;
        try {
            url = new URL(((Uri)uri).toString());
        } catch (MalformedURLException localMalformedURLException) {
            localMalformedURLException.printStackTrace();
        }
        Log.v(TAG, String.valueOf(new StringBuilder().append("Built URI ").append(uri)));
        return (URL)url;
    }

    public static final Uri buildVideoUri(String paramString)
    {
        return Uri.parse(paramString).buildUpon().build();
    }

    public static String getResponseFromHttpsUrl(URL url)
            throws IOException
    {
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        try
        {
            connection.setDoInput(true);
            InputStream inputStream = connection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                String response = ((Scanner)scanner).next();
                return (String)response;
            } else {
                return null;
            }
        }
        finally
        {
            connection.disconnect();
        }
    }
}
