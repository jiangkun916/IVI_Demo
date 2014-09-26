package com.borqs.ivi_collect.util;



import java.io.File;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Util {

	public static final String TAG = "IVI_DATA";

	/**
	 * 
	 * Define the actions.
	 *
	 */	
	
	public static class Action {
		public static final String ACTION_MSG_BOOT      = "com.borqs.ivi_collect.ACTION_MSG_BOOT";
		public static final String DATA_REPORT          = "com.borqs.ivi_collectr.DATA_REPORT";
		public static final String SEND_REPORT          = "com.borqs.ivi_collect.SEND_REPORT";
		
		public static final String MONITOR_LIVE_TIME    = "com.borqs.ivi_collect.MONITOR_LIVE_TIME";
		

		
		
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
		
		
		
		
		public static final String reportJsonString = "reportJsonString";
		
	}
	/**
	 * Define report data structure.
	 */	
	public static class ReportData {
		
		
		public String tuid         = null;
		public String imsi         = null;
		public String imei         = null;
		public String Longitude    = null;
		public String Latitude     = null;
		public String build_number = null;
		public String model        = null;

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
					+ Latitude + "]";
		}

	}
	/**
	 * 
	 * JSON keys definitions 
	 *
	 */
	public static class JSON {
		
		public static final String TUID         = "tuid";
		public static final String IMSI         = "imsi";
		public static final String IMEI         = "imei";
		public static final String LONGITUDE    = "Longitude";
		public static final String LATITUDE     = "Latitude";	
		public static final String GPS          = "gps";	
		public static final String STATUS       = "status";
		public static final String BUILD_NUMBER = "build_number";
		public static final String MODEL        = "model";

	}
	
	//判断网络是否链接
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connManager == null){
			return false;
		}
		
		NetworkInfo netInfo = connManager.getActiveNetworkInfo();
		
		if(netInfo == null){
			return false;
		}
		
		if(netInfo.isConnected()){
			int type = netInfo.getType();
			switch (type) {
			case ConnectivityManager.TYPE_WIFI: {
				Log.i(TAG,"Type:WiFi");
				return true;
			}
			case ConnectivityManager.TYPE_MOBILE: {
				Log.i(TAG,"Type:3G");
				return true;
			}
			default: {
				return false;
			}
			}
		}
		return false;
		
	}
	public static boolean fileIsExists(){
        File f=new File("/sdcard/a.txt");
        if(!f.exists()){
                return false;
        }
        return true;
}
	
}
