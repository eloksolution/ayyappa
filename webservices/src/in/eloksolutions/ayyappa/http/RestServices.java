package in.eloksolutions.ayyappa.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

public class RestServices {
	public static void main(String[] args) throws Exception, IOException {
		String json="{\"to\" : \"/topics/news\" ,\"notification\" : {\"body\" : \"topic tedting\" , \"title\" : \"Topic\", \"icon\" : \"logo\"}}";
		postJSON("https://fcm.googleapis.com/fcm/send",json);
	}
	public static void postJSON(String url, String json)
			throws HttpException, IOException {
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		method.addRequestHeader("Content-Type","application/json;charset=utf-8");
		method.addRequestHeader("private", "0009-31ccd983-5601a165-3b6f-e2a6ed7cs");
		method.addRequestHeader("Accept", "application/json");
		method.addRequestHeader("Host", "fcm.googleapis.com");
		method.addRequestHeader("Authorization", "key=AIzaSyD2yWSaqd50c-H4z2wEVJ44_QLt-lcYurA");
        
		StringRequestEntity requestEntity = new StringRequestEntity(
				json,
			    "application/json",
			    "UTF-8");
		method.setRequestEntity(requestEntity);
		int responseCode=client.executeMethod(method);
		System.out.println("Response code is "+responseCode);
		System.out.println("Response code is "+method.getResponseBodyAsString());
	}
    public static String POST(String surl, String json){
        InputStream inputStream = null;
        String result = "";
        HttpURLConnection conn = null;
        try {
        	URL url=new URL(surl);
            System.out.println("URL> " + url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(json.getBytes().length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json;charset=utf-8");
            conn.setRequestProperty("X-Requested-With","XMLHttpRequest");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "key=AIzaSyD2yWSaqd50c-H4z2wEVJ44_QLt-lcYurA");
            
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
            System.out.println("result is "+result);
        }
        catch (Exception e) {
        	e.printStackTrace();
            System.out.println("InputStream"+ e.getLocalizedMessage());
        }
        return result;
    }
    public static String GET(URL url){
        InputStream inputStream = null;
        String result = "";
        HttpURLConnection conn = null;
        try {

            System.out.println("URL> " + url);
            conn = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(conn.getInputStream());
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        }
        catch (Exception e) {
            System.out.println("InputStream"+ e.getLocalizedMessage());
        }
        return result;
    }
    public static String PUT(URL url, String json){
        InputStream inputStream = null;
        String result = "";
        HttpURLConnection conn = null;
        try {

            System.out.println("URL> " + url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(json.getBytes().length);
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type","application/json;charset=utf-8");
            conn.setRequestProperty("X-Requested-With","XMLHttpRequest");
            conn.setRequestProperty("Accept", "application/json");
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
            System.out.println("InputStream"+ e.getLocalizedMessage());
        }
        return result;
    }
    public static String DELETE(URL url){
        InputStream inputStream = null;
        String result = "";
        HttpURLConnection conn = null;
        try {

            System.out.println("URL " + url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        }
        catch (Exception e) {
            System.out.println("InputStream"+ e.getLocalizedMessage());
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
