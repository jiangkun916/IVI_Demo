package com.borqs.ivi_collect.collector;


import com.borqs.ivi_collect.util.Util;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.os.Build;
import android.os.SystemProperties;

public class DataCollector extends IntentService {

	private final static String TAG = "DataCollector";
	private static long recordInterval = 5 * 1000;// AlarmManager.INTERVAL_FIFTEEN_MINUTES;
	private String imsi = null;
	private String imei = null;
	private String tuid = null;
	private String Latitude = null;
	private String Longitude = null;
	private String build = null;
	private String model = null;
	private String poweron = null;
	private String lastpoweroff = null;
	private String time = null;

	public DataCollector() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		Intent target = new Intent();

		tuid = getTuid();
		build = getBuild_number();
		model = getModel();
		imsi = getImsi();
		imei = getImei();

		poweron = getPowerOn();
		lastpoweroff = getLastPowerOff();
		Longitude = getLongitude();
		Latitude = getLatitude();
		time = getCollectTime();

		// if (tuid == null || Longitude == null || Latitude == null) {
		// Log.i(TAG,
		// "---Longitude--->>"+Longitude+"---Latitude--->>"+Latitude);
		// return;
		// }
		target.setAction(Util.Action.DATA_SAVE);

		if (Util.FirstSend) {

			target.putExtra(Util.ExtraKeys.LASTPOWEROFF, lastpoweroff);
			Log.i(TAG, "---------------lastpowerof======" + lastpoweroff
					+ "------------------------");

			target.putExtra(Util.ExtraKeys.POWERON, poweron);
			Log.i(TAG, "---------------poweron======" + poweron
					+ "------------------------");

			target.putExtra(Util.ExtraKeys.TUID, tuid);
			Log.i(TAG, "---------------tuid======" + tuid
					+ "------------------------");

			target.putExtra(Util.ExtraKeys.IMSI, imsi);
			Log.i(TAG, "---------------imsi======" + imsi
					+ "------------------------");

			target.putExtra(Util.ExtraKeys.IMEI, imei);
			Log.i(TAG, "---------------imei======" + imei
					+ "------------------------");

			target.putExtra(Util.ExtraKeys.LONGITUDE, Longitude);
			Log.i(TAG, "---------------Longitude======" + Longitude
					+ "--------------------");

			target.putExtra(Util.ExtraKeys.LATITUDE, Latitude);
			Log.i(TAG, "---------------Latitude======" + Latitude
					+ "---------------------");

			target.putExtra(Util.ExtraKeys.TIME, time);
			Log.i(TAG, "---------------time======" + time
					+ "---------------------");

			target.putExtra(Util.ExtraKeys.BUILD, build);
			Log.i(TAG, "---------------build======" + build + "--------");

			target.putExtra(Util.ExtraKeys.MODEL, model);
			Log.i(TAG, "---------------model======" + model
					+ "----------------------");
		}else{
			target.putExtra(Util.ExtraKeys.TUID, tuid);
			Log.i(TAG, "---------------tuid======" + tuid
					+ "------------------------");
			
			target.putExtra(Util.ExtraKeys.LONGITUDE, Longitude);
			Log.i(TAG, "---------------Longitude======" + Longitude
					+ "--------------------");

			target.putExtra(Util.ExtraKeys.LATITUDE, Latitude);
			Log.i(TAG, "---------------Latitude======" + Latitude
					+ "---------------------");

			target.putExtra(Util.ExtraKeys.TIME, time);
			Log.i(TAG, "---------------time======" + time
					+ "---------------------");
		}

		startService(target);
		target = null;

	}

	public static void set(Context context) {

		Log.i(TAG, "-----------------");

		Intent liveTimeIntent = new Intent(Util.Action.DATA_REPORT);
		PendingIntent liveTimeTriger = PendingIntent.getService(context, 0,
				liveTimeIntent, 0);
		// launch 20s later to avoid too many works on receiving boot completed.
		long firstTime = System.currentTimeMillis() + 20 * 1000;
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC, firstTime, recordInterval,
				liveTimeTriger);

	}

	// get imsi
	private String getImsi() {
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (tm != null) {
			String imsi = tm.getLine1Number();
			if (imsi == null || imsi.equals("")) {
				return "00000000000";
			}
			return imsi;
		} else {
			return "00000000000";

		}
	}

	// get imei
	private String getImei() {
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (tm != null) {
			String imei = tm.getDeviceId();
			if (imei == null || imei.equals("")) {
				return "000000000000000";
			}
			return imei;
		} else {
			return "000000000000000";
		}
	}

	// get tuid
	private String getTuid() {
		return Settings.System.getString(this.getContentResolver(),
				"CHINA_TSP_TUID");
	}

	// get type
	private String getBuild_number() {

		String date = SystemProperties.get("ro.build.version.incremental",
				Build.UNKNOWN);
		String ibuildNumber = SystemProperties.get("ro.build.revision",
				Build.UNKNOWN);
		String type = SystemProperties.get("ro.build.type", Build.UNKNOWN);
		String end = type + "." + date.substring(13, 21) + "." + ibuildNumber;

		return end;
	}

	// get model
	private String getModel() {
		return android.os.Build.MODEL;

	}

	// get Longitude,Latitude and time
	// private void getLongitudeAndLatitude() {
	// LocationManager locMan = (LocationManager)
	// getSystemService(Context.LOCATION_SERVICE);
	// Location loc = locMan
	// .getLastKnownLocation(LocationManager.GPS_PROVIDER);
	// if (loc != null) {
	// Longitude = String.valueOf(loc.getLongitude());
	// Latitude = String.valueOf(loc.getLatitude());
	//
	// }
	//
	// time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new
	// Date(System.currentTimeMillis()));
	// }

	// get Longitude
	private String getLongitude() {
		LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location loc = locMan
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (loc != null) {
			return String.valueOf(loc.getLongitude());
		}

		return null;
	}

	// get Latitude
	private String getLatitude() {
		LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location loc = locMan
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (loc != null) {
			return String.valueOf(loc.getLatitude());
		}

		return null;
	}

	// get time
	private String getCollectTime() {
//		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(
//				System.currentTimeMillis()));
		return Long.toString(System.currentTimeMillis()); 

	}

	// get power on
	private String getPowerOn() {
		
		SharedPreferences sharedata = getSharedPreferences("poweron", Context.MODE_PRIVATE);    
		String poweron = sharedata.getString("time", null);    
		return poweron;

	}

	// get last shutdown time
	private String getLastPowerOff() {
//		File file = null;
//		file = new File("/sdcard/a.txt");
//		if (!file.exists()) {
//			return null;
//		}
//		String result = null;
//		FileReader fileReader = null;
//		BufferedReader bufferedReader = null;
//		try {
//			fileReader = new FileReader(file);
//			bufferedReader = new BufferedReader(fileReader);
//			try {
//				result = bufferedReader.readLine();
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (bufferedReader != null) {
//				try {
//					bufferedReader.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			if (fileReader != null) {
//				try {
//					fileReader.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return result;
		
		
		
		SharedPreferences sharedata = getSharedPreferences("poweroff", Context.MODE_PRIVATE);    
		String poweroff = sharedata.getString("time", null);    
		return poweroff;
	}

}
