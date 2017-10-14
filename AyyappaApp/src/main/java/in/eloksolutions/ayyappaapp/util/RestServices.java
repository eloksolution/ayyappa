package in.eloksolutions.ayyappaapp.util;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestServices {
    public static String POST(URL url, String json){
        InputStream inputStream = null;
        String result = "";
        HttpURLConnection conn = null;
        try {

            Log.e("URL", "> " + url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(json.getBytes().length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(json.getBytes());
            out.close();

            // handle the response
            inputStream = conn.getInputStream();
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        }
        catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;
    }
    public static String GET(URL url){
        InputStream inputStream = null;
        String result = "";
        HttpURLConnection conn = null;
        try {

            Log.e("URL", "> " + url);
            conn = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(conn.getInputStream());
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        }
        catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;
    }
    public static String PUT(URL url, String json){
        InputStream inputStream = null;
        String result = "";
        HttpURLConnection conn = null;
        try {

            Log.e("URL", "> " + url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(json.getBytes().length);
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(json.getBytes());
            out.close();

            // handle the response
            inputStream = conn.getInputStream();
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        }
        catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;
    }
    public static String DELETE(URL url){
        InputStream inputStream = null;
        String result = "";
        HttpURLConnection conn = null;
        try {

            Log.e("URL", "> " + url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        }
        catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;
    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }
}
