package com.borqs.ivi_collect.collector;



import com.borqs.ivi_collect.util.Util;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.os.Build;
import android.os.SystemProperties;

public class DataCollector extends IntentService{

	private final static String TAG = "DataCollector";	
	private static long recordInterval = 60*1000;//AlarmManager.INTERVAL_FIFTEEN_MINUTES;
	private static String imsi  = null;
	private static String imei  = null;
	private static String tuid  = null;
	private static String lag   = null;
	private static String lng   = null;
	private static String type  = null;
	private static String model = null;
	
	public DataCollector() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		
		Intent target = new Intent();
		
		if(Util.fileIsExists()){
			Log.i(TAG, "has been sent to complete");
			return;
		}
		
		getImsiAndImei();
		getTuid();
		getLongitudeAndLatitude();
		type = getType();	
		model = getModel();

		target.setAction(Util.Action.SEND_REPORT);
		
		target.putExtra(Util.ExtraKeys.TUID, tuid);		
		Log.i(TAG, "---------------tuid======"+tuid+"------------------------");
		
		target.putExtra(Util.ExtraKeys.IMSI,imsi);
		Log.i(TAG, "---------------imsi======"+imsi+"------------------------");
		
		target.putExtra(Util.ExtraKeys.IMEI, imei);
		Log.i(TAG, "---------------imei======"+imei+"------------------------");
		
		target.putExtra(Util.ExtraKeys.LONGITUDE, lng);
		Log.i(TAG, "---------------Longitude======"+lng+"------------------------");

		target.putExtra(Util.ExtraKeys.LATITUDE, lag);
		Log.i(TAG, "---------------Latitude======"+lag+"------------------------");
		
		target.putExtra(Util.ExtraKeys.TYPE, type);
		Log.i(TAG, "---------------TYPE======"+type+"------------------------");
		
		target.putExtra(Util.ExtraKeys.MODEL, model);
		Log.i(TAG, "---------------model======"+model+"------------------------");

		startService(target);

	}
	
	public static void set(Context context) {

		Log.i(TAG, "-----------------");

		Intent liveTimeIntent = new Intent(Util.Action.MONITOR_LIVE_TIME);
		PendingIntent liveTimeTriger = PendingIntent.getService(context, 0, liveTimeIntent, 0);
		//launch 5 mins later to avoid too many works on receiving boot completed. 
		long firstTime = System.currentTimeMillis()+20*1000;
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC, firstTime, recordInterval, liveTimeTriger);

	}
	//get IMSI and IMEI
	private void getImsiAndImei(){
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (tm != null) {
			imsi = tm.getLine1Number();
			if (imsi == null || imsi.equals("")) {
				imsi = tm.getSubscriberId();
				if (imsi != null && !imsi.equals("")) {
					imsi = "IMSI:" + imsi;
				} else {
					imsi = "Unknown";
				}
			}
			imei = tm.getDeviceId();
			if (imei == null || imei.equals("")) {
				imei = "Unknown";
			}
		} else {
			imsi = "Unknown";
			imei = "Unknown";
		}
	}
	
	//get tuid
	private void getTuid(){
		
		tuid = Settings.System.getString(this.getContentResolver(), "CHINA_TSP_TUID");

	}
	//get type
	private String getType(){
		
		String date = SystemProperties.get("ro.build.version.incremental",Build.UNKNOWN);
		String ibuildNumber = SystemProperties.get("ro.build.revision",Build.UNKNOWN);		
		String type = SystemProperties.get("ro.build.type", Build.UNKNOWN);
		String end = type +"."+ date.substring(13, 21) + "." + ibuildNumber;
		Log.i(TAG, "---------------eng======"+end+"------------------------");
		return end;

	}
	
	//get model
	private String getModel(){
		return android.os.Build.MODEL;

	}
	
	//get Longitude and Latitude
	private void getLongitudeAndLatitude(){
		LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location loc = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (loc != null) {		
			lng = String.valueOf(loc.getLongitude());
			lag = String.valueOf(loc.getLatitude());
		}
	}
}
