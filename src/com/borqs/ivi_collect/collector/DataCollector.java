package com.borqs.ivi_collect.collector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;


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

public class DataCollector extends IntentService {

	private final static String TAG = "DataCollector";
	private static long recordInterval = 60 * 1000;// AlarmManager.INTERVAL_FIFTEEN_MINUTES;
	private String imsi = null;
	private String imei = null;
	private String tuid = null;
	private String Latitude = null;
	private String Longitude = null;
	private String build = null;
	private String model = null;
	private final String poweron = getPowerOn();
	private String lastpoweroff = null;
	private String time = null;

	public DataCollector() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		Intent target = new Intent();
		if (Util.FirstSend) {

			// String result = null;
			// File file1 = null;
			// file1 = new File("/sdcard/b.txt");
			// if (file1.exists()) {
			// try {
			// BufferedReader br = new BufferedReader(new FileReader(file1));
			// String s = null;
			// while ((s = br.readLine()) != null) {
			// result = s;
			// Log.i(TAG, "-------------->>" + result);
			// }
			// NameValuePair nameValuePair = new BasicNameValuePair("info",
			// result.substring(5));
			// Log.i(TAG, "nameValuePair:" + nameValuePair.toString());
			// long newStatus = -1;
			// newStatus = Util.sendReport(serverUrl, nameValuePair);
			// Log.i(TAG, "newStatus:" + newStatus);
			//
			// if (newStatus == 0) {
			// file1.delete();
			// }
			//
			//
			//
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// }

			// poweron = getPowerOn();
			tuid = getTuid();
			build = getBuild_number();
			model = getModel();
			lastpoweroff = getLastPowerOff();
			imsi = getImsi();
			imei = getImei();
			// getImsiAndImei();
//			getLongitudeAndLatitude();
			Longitude = getLongitude();
			Latitude = getLatitude();
			time = getCollectTime();
			
			
//			if (tuid == null || Longitude == null || Latitude == null) {
//				Log.i(TAG, "---Longitude--->>"+Longitude+"---Latitude--->>"+Latitude);
//				return;
//			}

			target.setAction(Util.Action.DATA_SAVE);

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
			File file = null;
			file = new File("/sdcard/a.txt");

			if (file.exists()) {
				file.delete();
			}

		} else {

//			String result = null;
//			File file2 = new File("/sdcard/c.txt");
//			if (file2.exists()) {
//				try {
//					BufferedReader br = new BufferedReader(
//							new FileReader(file2));
//					String s = null;
//					while ((s = br.readLine()) != null) {
//						result = s;
//						Log.i(TAG, "-------------->>" + result);
//						NameValuePair nameValuePair = new BasicNameValuePair(
//								"info", result.substring(5));
//						Log.i(TAG, "nameValuePair:" + nameValuePair.toString());
//						long newStatus = -1;
//						newStatus = Util.sendReport(serverUrl, nameValuePair);
//						Log.i(TAG, "newStatus:" + newStatus);
//
//						if (newStatus == 0) {
//							Util.read("/sdcard/c.txt");
//							Util.delete(1);
//							Util.write("/sdcard/c.txt");
//							Util.list.clear();
//						}
//					}
//					br.close();
//
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}

			
			tuid = getTuid();
			Longitude = getLongitude();
			Latitude = getLatitude();
			time = getCollectTime();

			if (tuid == null || Longitude == null || Latitude == null) {
				Log.i(TAG, "---Longitude--->>"+Longitude+"---Latitude--->>"+Latitude);
				return;
			}

			target.setAction(Util.Action.DATA_SAVE);

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

	// // get IMSI and IMEI
	// private void getImsiAndImei() {
	// TelephonyManager tm = (TelephonyManager)
	// getSystemService(Context.TELEPHONY_SERVICE);
	// if (tm != null) {
	// imsi = tm.getLine1Number();
	// if (imsi == null || imsi.equals("")) {
	// imsi = "00000000000";
	// }
	// imei = tm.getDeviceId();
	// if (imei == null || imei.equals("")) {
	// imei = "000000000000000";
	// }
	// } else {
	// imsi = "00000000000";
	// imei = "000000000000000";
	// }
	// }

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
//	private void getLongitudeAndLatitude() {
//		LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//		Location loc = locMan
//				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//		if (loc != null) {
//			Longitude = String.valueOf(loc.getLongitude());
//			Latitude = String.valueOf(loc.getLatitude());
//
//		}
//
//		time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
//	}

	// get Longitude
	private String getLongitude(){
		LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location loc = locMan
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (loc != null) {
			return String.valueOf(loc.getLongitude());
		}
		
		return null;
	}
	
	// get Latitude
	private String getLatitude(){
		LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location loc = locMan
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (loc != null) {
			return String.valueOf(loc.getLatitude());
		}
		
		return null;
	}
	
	// get time
	private String getCollectTime(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));

	}	
	
	// get power on
	private String getPowerOn() {
		File file = null;
		file = new File("/sdcard/b.txt");
		if (!file.exists()) {
			return null;
		}
		String result = null;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			try {
				result = bufferedReader.readLine();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	// get last shutdown time
	private String getLastPowerOff() {
		File file = null;
		file = new File("/sdcard/a.txt");
		if (!file.exists()) {
			return null;
		}
		String result = null;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			try {
				result = bufferedReader.readLine();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;

	}

}
