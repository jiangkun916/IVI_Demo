package com.borqs.ivi_collect.util;

import android.util.Log;

public class Util {
	/**
	 * 
	 * Define the actions.
	 *
	 */
	public static final String TAG = "IVI_DATA";
	
	public static void log(String msg) {
		if (msg == null) {
			return;
		}
		Log.d(TAG, msg);
	}
	public static void log(String tag, String msg) {
		if (msg == null) {
			return;
		}
		msg = tag + "|" + msg;
		log(msg);
	}
	
	
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
	public static class ExtraKeys {
		public static final String TUID = "tuid";
		public static final String IMSI = "imsi";
		public static final String IMEI = "imei";

		public static final String LONGITUDE = "Longitude";
		public static final String LATITUDE = "Latitude";	
		
	}
	
	public static class ReportData {
		public String tudi      = null;
		public String imsi      = null;
		public String imei      = null;
		public String Longitude = null;
		public String Latitude  = null;
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
