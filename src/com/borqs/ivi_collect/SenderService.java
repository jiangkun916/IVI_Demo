package com.borqs.ivi_collect;


import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONException;
import org.json.JSONObject;



import com.borqs.ivi_collect.manual.MainActivity;
import com.borqs.ivi_collect.util.*;
import com.borqs.ivi_collect.util.Util.ReportData;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;

public class SenderService extends IntentService{
	
	private final static String TAG = "SenderService";	
	private final static String serverUrl = null;//"http://releaseadmin.borqs.com/su/gps.php";
	private String imsi = null;
	private String imei = null;
	private String tuid = null;
	private String lag = null;
	private String lng = null;

	public SenderService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String action = intent.getAction();
		if (TextUtils.isEmpty(action)) {
			return;
		}
		if (action.equals(Util.Action.SEND_REPORT)) {
			Util.log(TAG, "----SenderService---.");
			//Check if the network is available
			if (!Util.isNetworkAvailable(this)){
				Util.log(TAG, "Network is not available.");
	    		return;
	    	}
			tuid = intent.getStringExtra(Util.ExtraKeys.TUID);
			imsi = intent.getStringExtra(Util.ExtraKeys.IMSI);
			imei = intent.getStringExtra(Util.ExtraKeys.IMEI);
			lng = intent.getStringExtra(Util.ExtraKeys.LONGITUDE);
			lag = intent.getStringExtra(Util.ExtraKeys.LATITUDE);
			
			ReportData reportData = new ReportData();
			reportData.setTuid(tuid);
			reportData.setImsi(imsi);
			reportData.setImei(imei);
			reportData.setLongitude(lng);
			reportData.setLatitude(lag);
			
			String reportJsonString = ConvertToJSONString(reportData);
		
			
			Intent target = new Intent(this,MainActivity.class);
			target.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);		
			target.putExtra(Util.ExtraKeys.reportJsonString, reportJsonString);
			target.setAction("com.borqs.ivi");
			startActivity(target);
			
			
//			sendReport(serverUrl, reportJsonString);	
			
		}
	}
	
	
	public long sendReport(String url,String reportStr) {

		long id = -1;
		HttpURLConnection conn = null;
		BufferedWriter writer = null;
		BufferedInputStream feedbackStream = null;
		BufferedInputStream errorStream = null;
		
		try{
			//Create a trust manage that doesn't validate the certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] certs,
						String authType) {
				}
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] certs,
						String authType) {
				}
			} };
			
			//Install the all-trusting trust manager
			final SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			
			//Create all-trusting host name verifier
			HostnameVerifier allHostValid = new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					// Trust all.
					return true;
				}
			};
			
			//Install the all-trusting host name verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostValid);
			
			//Transfer report stream
			URL reportUrl = new URL(url);
			conn = (HttpURLConnection) reportUrl.openConnection();
			conn.setDoInput(true);//Set whether this URLConnection allows input.
			conn.setDoOutput(true);//Set whether this URLConnection allows output.
			conn.setUseCaches(false);//Set whether this connection allows to use caches or not.
			conn.setConnectTimeout(30 * 1000);//Sets the maximum time to wait while connecting.
			conn.setReadTimeout(30 * 1000);//Sets the maximum time to wait for an input stream read.
			conn.setRequestProperty("Charset", "UTF-8");//Set the specified request header field.
			conn.setRequestProperty("Client-Name", "ivi_collect");
			conn.setRequestProperty("Content-Type", "text/plain");
			
			writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			writer.write(reportStr);
			writer.write("\n");
			writer.flush();
			writer.close();
			writer = null;
			
			//Get the response code and check if send successfully.
			int responseCode = conn.getResponseCode();
			switch(responseCode) {
			case 200://HTTP status code, 200: OK 
				feedbackStream = new BufferedInputStream(conn.getInputStream());
				String feedbackStr = readStream(feedbackStream);
				Util.log(TAG, "feedback String is: " + feedbackStr);
				JSONObject jsonObject = new JSONObject(feedbackStr);
				
				
				
				
				
//				id = Long.parseLong(jsonObject.getString(Util.JSON.Server_ID));
				
				
				
				
				
				Util.log(TAG, "Send report success,serverid = " + id);
				break;
			case 500://HTTP status code, 500: Internal error 
				errorStream = new BufferedInputStream(conn.getInputStream());
				String errorMsgStr = readStream(errorStream);
				Util.log(TAG, "Send report fail, error message: " + errorMsgStr);
				throw new Exception("Send report fail, error message: " + errorMsgStr);
			default://Throw exception with the responseCode
				Util.log(TAG, "Send report fail, with response code: " + responseCode);
				throw new Exception("Send report fail, unknown reason. " +
						"Response code: " + responseCode);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			return -1;
		}finally {
			if (errorStream != null) {
				try {
					errorStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				errorStream = null;
			}
			
			if (feedbackStream != null) {
				try {
					feedbackStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				feedbackStream = null;
			}
			
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				writer = null;
			}
			
			if (conn != null) {
				try {
					conn.disconnect();
				} catch (Exception e) {
					e.printStackTrace();
				}
				conn = null;
			}
		}
		
		//return the server id after sending successfully
		return id;
	}
	
	
	
	/**
	 * Convert the report data into JSON string which can be send directly. 
	 * JSONObject: ReportDataJSONString
	 * Extras: tuid
	 *         imsi
	 *         imei
	 *         GPS (LONGITUDE, LATITUDE)
	 *         
	 * 
	 * @param string
	 * @return string
	 */
	public String ConvertToJSONString(ReportData reportData) {
		try{
			//Define the report data JSON string object
			JSONObject reportDataJSONString = new JSONObject();
			
			//Define gps JSON string object
			JSONObject gps = new JSONObject();
			
			//Get gps
			gps.put(Util.ExtraKeys.LONGITUDE, lng );
			gps.put(Util.ExtraKeys.LATITUDE ,lag );
	
			//Put these info to reportDataJSONString object
			reportDataJSONString.put(Util.JSON.TUID, reportData.tuid);
			reportDataJSONString.put(Util.JSON.IMSI, reportData.imsi);
			reportDataJSONString.put(Util.JSON.IMEI, reportData.imei);
			reportDataJSONString.put(Util.JSON.GPS, gps);
			
			return reportDataJSONString.toString();
			
		}catch(JSONException je){
			return null;
		}
	}
	
	
	
	/*
	 * Read string from input stream.
	 */
	private String readStream(InputStream is) throws IOException  {
		StringBuffer stringBuffer = new StringBuffer();
		int len = 0;
		byte[] buf = new byte[1024];
		while ((len = is.read(buf)) != -1) {
			stringBuffer.append(new String(buf, 0, len));
		}
		return stringBuffer.toString();
	}
		
}