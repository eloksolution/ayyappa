package in.eloksolutions.ayyappa.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;

public class SMSUtil {

	public static int getnerateOTP(){
		return ThreadLocalRandom.current().nextInt(1001, 9999);
	}
	public static void post(String url, Map<String, String> params)
			throws HttpException, IOException {
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		Set<String> keySet = params.keySet();
		for (String key : keySet)
			method.addParameter(key, params.get(key));
		client.executeMethod(method);

	}
	public static void main(String[] args) throws HttpException, IOException {
		String url="http://smsalertbox.com/api/sms.php";
		Map<String,String> param=new HashMap<>();
		param.put("uid", "73726964686172343537");
		param.put("pin", "2d3040316ecd0f780cd0bc869dd7ac6f");
		param.put("sender", "DPSRPS");
		param.put("route", "5");
		param.put("tempid", "61878");
		param.put("mobile", "9000011093");
		param.put("pushid", "1");
		param.put("message", "Received Water Can Enquiry from Name: Vishnu,Mobile No: 90009090,Quantity: 2 at Location: Mal,City: hyd Please Contact.");
		
		SMSUtil.post(url, param);
	}
	
}
