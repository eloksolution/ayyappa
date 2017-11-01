package in.eloksolutions.ayyappaapp.util;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

/**
 * Created by Vishnu on 12/1/2016.
 */
public class URLUtil {
    public static final String URL_STR="https://fcm.googleapis.com/fcm/send";
    public static String TAG="URLUtil.class";
    public static String post(String msg) throws  Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("to", "/topics/news");
        JSONObject msgjsonObject = new JSONObject();
        msgjsonObject.accumulate("message", msg);
        jsonObject.accumulate("data",msgjsonObject);

        // 4. convert JSONObject to JSON to String
        String json = jsonObject.toString();
        json=json.replaceAll("\\\\","");
        Log.d(TAG, "JSON  Message: " + json);
        //json="{ \"to\": \"/topics/news\",\"data\": {\"message\": \" new from app Topic new sThis is a Firebase Cloud Messaging Topic Message2222333!\" } }";
        // ** Alternative way to convert Person object to JSON string usin Jackson Lib
        // ObjectMapper mapper = new ObjectMapper();
        // json = mapper.writeValueAsString(person);

        // 5. set json to StringEntity
        HttpsURLConnection httpcon = (HttpsURLConnection) ((new URL (URL_STR).openConnection()));
        httpcon.setDoOutput(true);
        httpcon.setRequestProperty("Content-Type", "application/json");
        httpcon.setRequestProperty("Accept", "application/json");
        httpcon.setRequestProperty("Authorization", "key=AIzaSyD2yWSaqd50c-H4z2wEVJ44_QLt-lcYurA");

        httpcon.setRequestMethod("POST");
        //httpcon.connect();

        //Write
        OutputStream os = httpcon.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(json);
        writer.close();
        os.flush();
        os.close();
        System.out.println("Response code is "+httpcon.getResponseMessage()+" code is "+httpcon.getResponseCode());
        //Read
        BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(),"UTF-8"));

        String line = null;
        StringBuilder sb = new StringBuilder();

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        br.close();
        String result = sb.toString();
        return  result;
    }

    public static String postHttpsToFCM(String msg) throws  Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("to", "/topics/news");
        JSONObject msgjsonObject = new JSONObject();
        msgjsonObject.accumulate("message", msg);
        jsonObject.accumulate("date",msgjsonObject);

        // 4. convert JSONObject to JSON to String
        String json = jsonObject.toString();

        URL url = new URL(URL_STR);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

        // Create the SSL connection
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, null, new java.security.SecureRandom());
        conn.setSSLSocketFactory(sc.getSocketFactory());

        // Use this if you need SSL authentication
        /*String userpass = user + ":" + password;
        String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);
        conn.setRequestProperty("Authorization", basicAuth);*/

        // set Timeout and method
        conn.setReadTimeout(7000);
        conn.setConnectTimeout(7000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.connect();
        // Add any data you wish to post here
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(json);
        writer.close();
        os.close();


        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));

        String line = null;
        StringBuilder sb = new StringBuilder();

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        br.close();
        String result = sb.toString();
        return  result;
    }

    public static String postHttpRequest(InputStream certInputStream, String msg) throws  Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("to", "/topics/news");
        JSONObject msgjsonObject = new JSONObject();
        msgjsonObject.accumulate("message", msg);
        jsonObject.accumulate("date",msgjsonObject);

        // 4. convert JSONObject to JSON to String
        String json = jsonObject.toString();
        String result = "";
        try {
            URL url = new URL(URL_STR);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(KeyPinStore.getInstance(certInputStream).getContext().getSocketFactory()); // Tell the URLConnection to use a SocketFactory from our SSLContext
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.println(json);
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()), 8192);
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                result = result.concat(inputLine);
            }
            in.close();
            //} catch (IOException e) {
        } catch (Exception e) {
            result = e.toString();
            e.printStackTrace();
        }
        return result;
    }
}
