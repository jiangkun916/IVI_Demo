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
		public static final String MANAUL_REPORT        = "com.borqs.ivi_collect.MANUAL_REPORT";
		
		public static final String VIEW_SYS_INFO        = "com.borqs.ivi_collect.VIEW_SYSTEM_INFO";
		public static final String TYPE_CONTROL_LIST    = "com.borqs.ivi_collect.TYPE_CONTROL_LIST";
		public static final String VIEW_LOCAL_DATA      = "com.borqs.ivi_collect.VIEW_LOCAL_DATA";
		public static final String VIEW_ABOUT           = "com.borqs.ivi_collect.VIEW_ABOUT";
		
		
	}
	/**
	 * 
	 * Define the extras info.
	 *
	 */
	public static class ExtraKeys {
		
		public static final String TUID      = "tuid";
		public static final String IMSI      = "imsi";
		public static final String IMEI      = "imei";
		public static final String LONGITUDE = "Longitude";
		public static final String LATITUDE  = "Latitude";	
		public static final String TYPE      = "tpye";
	
		
		
		
		
		public static final String reportJsonString = "reportJsonString";
		
	}
	/**
	 * Define report data structure.
	 */	
	public static class ReportData {
		
		
		public String tuid      = null;
		public String imsi      = null;
		public String imei      = null;
		public String Longitude = null;
		public String Latitude  = null;
		public String type  = null;

		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
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
		
		public static final String TUID      = "tuid";
		public static final String IMSI      = "imsi";
		public static final String IMEI      = "imei";
		public static final String LONGITUDE = "Longitude";
		public static final String LATITUDE  = "Latitude";	
		public static final String GPS       = "gps";	
		public static final String STATUS    = "status";
		public static final String TYPE      = "tpye";

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
