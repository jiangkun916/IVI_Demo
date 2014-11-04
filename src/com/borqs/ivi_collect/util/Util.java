package com.borqs.ivi_collect.util;

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

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Util {
	public static boolean FirstSend = true;
	public static final String TAG = "Util";

	/**
	 * 
	 * Define the actions.
	 * 
	 */

	public static class Action {

		public static final String ACTION_MSG_BOOT    = "com.borqs.ivi_collect.ACTION_MSG_BOOT";
		public static final String DATA_REPORT        = "com.borqs.ivi_collect.DATA_REPORT";
		public static final String SEND_ALL_REPORT    = "com.borqs.ivi_collect.SEND_ALL_REPORT";
		public static final String SEND_LITTLE_REPORT = "com.borqs.ivi_collect.SEND_LITTLE_REPORT";

	}

	/**
	 * 
	 * Define the extras info.
	 * 
	 */
	public static class ExtraKeys {

		public static final String TUID         = "tuid";
		public static final String IMSI         = "imsi";
		public static final String IMEI         = "imei";
		public static final String LONGITUDE    = "Longitude";
		public static final String LATITUDE     = "Latitude";
		public static final String BUILD_NUMBER = "build_number";
		public static final String MODEL        = "model";
		public static final String POWERON      = "poweron";
		public static final String LASTPOWEROFF = "lastpowerof";
		public static final String SENDTIME     = "sendtime";
		public static final String GPS          = "gps";
		public static final String STATUS       = "status";

	}

	public static class ReportLittleData {

		public String tuid = null;
		public String Longitude = null;
		public String Latitude = null;
		public String sendtime = null;

		public String getTuid() {
			return tuid;
		}

		public void setTuid(String tuid) {
			this.tuid = tuid;
		}

		public String getSendTime() {
			return sendtime;
		}

		public void setSendTime(String sendtime) {
			this.sendtime = sendtime;
		}

		public String getLongitude() {
			return Longitude;
		}

		public void setLongitude(String longitude) {
			Longitude = longitude;
		}

		public String getLatitude() {
			return Latitude;
		}

		public void setLatitude(String latitude) {
			Latitude = latitude;
		}

		@Override
		public String toString() {
			return "ReportLittleData [tudi=" + tuid + ", sendtime=" + sendtime
					+ ", Longitude=" + Longitude + ", Latitude=" + Latitude
					+ "]";
		}

	}

	/**
	 * Define report data structure.
	 */
	public static class ReportData {

		public String tuid = null;
		public String imsi = null;
		public String imei = null;
		public String Longitude = null;
		public String Latitude = null;
		public String sendtime = null;
		public String build_number = null;
		public String model = null;
		public String poweron = null;
		public String lastpoweroff = null;

		public String getSendTime() {
			return sendtime;
		}

		public void setSendTime(String sendtime) {
			this.sendtime = sendtime;
		}

		public String getPowerOn() {
			return poweron;
		}

		public void setPowerOn(String poweron) {
			this.poweron = poweron;
		}

		public String getLastPowerOff() {
			return lastpoweroff;
		}

		public void setLastPowerOff(String lastpoweroff) {
			this.lastpoweroff = lastpoweroff;
		}

		public String getModel() {
			return model;
		}

		public void setModel(String model) {
			this.model = model;
		}

		public String getBuild_number() {
			return build_number;
		}

		public void setBuild_number(String build_number) {
			this.build_number = build_number;
		}

		public String getImsi() {
			return imsi;
		}

		public void setImsi(String imsi) {
			this.imsi = imsi;
		}

		public String getImei() {
			return imei;
		}

		public void setImei(String imei) {
			this.imei = imei;
		}

		public String getLongitude() {
			return Longitude;
		}

		public void setLongitude(String longitude) {
			Longitude = longitude;
		}

		public String getLatitude() {
			return Latitude;
		}

		public void setLatitude(String latitude) {
			Latitude = latitude;
		}

		public String getTuid() {
			return tuid;
		}

		public void setTuid(String tuid) {
			this.tuid = tuid;
		}

		public ReportData() {
			super();
		}

		@Override
		public String toString() {
			return "ReportData [tudi=" + tuid + ", imsi=" + imsi + ", imei="
					+ imei + ", Longitude=" + Longitude + ", Latitude="
					+ Latitude + ", sendtime=" + sendtime + ", build_number="
					+ build_number + ", model=" + model + ", poweron="
					+ poweron + ", lastpoweroff=" + lastpoweroff + "]";
		}

	}

	// Determine whether the network link
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager == null) {
			return false;
		}

		NetworkInfo netInfo = connManager.getActiveNetworkInfo();

		if (netInfo == null) {
			return false;
		}

		if (netInfo.isConnected()) {
			int type = netInfo.getType();
			switch (type) {
			case ConnectivityManager.TYPE_WIFI: {
				Log.i(TAG, "Type:WiFi");
				return true;
			}
			case ConnectivityManager.TYPE_MOBILE: {
				Log.i(TAG, "Type:3G");
				return true;
			}
			default: {
				return false;
			}
			}
		}
		return false;

	}
	
	public static long sendReport(String url , NameValuePair reportStr) {

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
//			conn.setRequestProperty("Client-Name", "ivi_collect");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			writer.write(reportStr.toString());
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
				Log.i(TAG, "feedback String is: " + feedbackStr);
				JSONObject jsonObject = new JSONObject(feedbackStr);			
				id = Long.parseLong(jsonObject.getString(Util.ExtraKeys.STATUS));
				Log.i(TAG, "Send report success,serverid = " + id);
				break;
			case 500://HTTP status code, 500: Internal error 
				errorStream = new BufferedInputStream(conn.getInputStream());
				String errorMsgStr = readStream(errorStream);
				Log.i(TAG, "Send report fail, error message: " + errorMsgStr);
				throw new Exception("Send report fail, error message: " + errorMsgStr);
			default://Throw exception with the responseCode
				Log.i(TAG, "Send report fail, with response code: " + responseCode);
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
	
	/*
	 * Read string from input stream.
	 */
	private static String readStream(InputStream is) throws IOException  {
		StringBuffer stringBuffer = new StringBuffer();
		int len = 0;
		byte[] buf = new byte[1024];
		while ((len = is.read(buf)) != -1) {
			stringBuffer.append(new String(buf, 0, len));
		}
		return stringBuffer.toString();
	}

}
