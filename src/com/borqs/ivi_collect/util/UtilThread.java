package com.borqs.ivi_collect.util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.borqs.ivi_collect.SendAllService;
import com.borqs.ivi_collect.database.DatabaseHelper;
import com.borqs.ivi_collect.util.Util.ReportData;

public class UtilThread extends Thread {

	private static final String TAG = "UtilThread";
	private DatabaseHelper mOpenHelper;
	private final String serverUrl = "http://releaseadmin.borqs.com/su/gps.php";

	public void run() {

		Log.i(TAG, "-------UtilThread run----------");
		Context context = SendAllService.getContext();

		if (!Util.isNetworkAvailable(context)) {
			Log.i(TAG, "Network is not available.");
			return;
		}
		// TODO: cpu判断

		mOpenHelper = new DatabaseHelper(context);
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM all_information", null);
		int count = cursor.getCount();
		Log.i(TAG, "-------count----------" + count);
		ReportData[] result = new ReportData[count];

		synchronized (result) {
			if (count > 0) {
				if (cursor.moveToFirst()) {
					for (int i = 0; i < count; i++) {
						result[i] = new ReportData();
						result[i].setId(cursor.getString(cursor
								.getColumnIndex(Util.ExtraKeys.COL_ID)));
						result[i].setTuid(cursor.getString(cursor
								.getColumnIndex(Util.ExtraKeys.TUID)));
						result[i].setImsi(cursor.getString(cursor
								.getColumnIndex(Util.ExtraKeys.IMSI)));
						result[i].setImei(cursor.getString(cursor
								.getColumnIndex(Util.ExtraKeys.IMEI)));
						result[i].setBuild(cursor.getString(cursor
								.getColumnIndex(Util.ExtraKeys.BUILD)));
						result[i].setModel(cursor.getString(cursor
								.getColumnIndex(Util.ExtraKeys.MODEL)));
						result[i].setPowerOn(cursor.getString(cursor
								.getColumnIndex(Util.ExtraKeys.POWERON)));
						result[i].setLastPowerOff(cursor.getString(cursor
								.getColumnIndex(Util.ExtraKeys.LASTPOWEROFF)));
						result[i].setLongitude(cursor.getString(cursor
								.getColumnIndex(Util.ExtraKeys.LONGITUDE)));
						result[i].setLatitude(cursor.getString(cursor
								.getColumnIndex(Util.ExtraKeys.LATITUDE)));
						result[i].setSendTime(cursor.getString(cursor
								.getColumnIndex(Util.ExtraKeys.TIME)));

						Log.i(TAG, result[i].toString());
						try {

							// Convert report data to JSON string
							NameValuePair reportJsonString = ConvertToJSONString(result[i]);
							if (reportJsonString == null) {
								return;
							}

							// Send report to server
							String id = result[i].getId();
							long newStatus = -1;
							newStatus = Util.sendReport(serverUrl,
									reportJsonString);
							Log.i(TAG, "-----newStatus----->>" + newStatus);

							if (newStatus == 0) {
								db.delete("all_information",
										Util.ExtraKeys.COL_ID + "=" + id, null);
								Log.i(TAG, "------id------>>" + id);
							}
							cursor.moveToNext();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}

		}

		cursor.close();
		cursor = null;
		db.close();

	}

	/**
	 * Convert the reportdata into JSON string which can be send directly.
	 * JSONObject: ReportDataJSONString Extras: tuid imsi imei gps (longitude,
	 * latitude, time) build model poweron lastpoweroff
	 * 
	 * @param string
	 * @return NameValuePair
	 */
	public NameValuePair ConvertToJSONString(ReportData reportData) {
		try {
			// Define the report data JSON string object
			JSONObject reportDataJSONString = new JSONObject();

			// Define gps JSON string object
			JSONObject gps = new JSONObject();

			// Get gps
			gps.put("lng", reportData.Longitude);
			gps.put("lat", reportData.Latitude);
			gps.put("t", reportData.time);

			// Put these info to reportDataJSONString object
			reportDataJSONString.put("B", reportData.build);
			reportDataJSONString.put("M", reportData.model);
			reportDataJSONString.put("on", reportData.poweron);
			reportDataJSONString.put("off", reportData.lastpoweroff);
			reportDataJSONString.put(Util.ExtraKeys.TYPE, "A");
			reportDataJSONString.put(Util.ExtraKeys.TUID, reportData.tuid);
			reportDataJSONString.put(Util.ExtraKeys.IMSI, reportData.imsi);
			reportDataJSONString.put(Util.ExtraKeys.IMEI, reportData.imei);
			reportDataJSONString.put(Util.ExtraKeys.GPS, gps);
			NameValuePair nameValuePair = new BasicNameValuePair("info",
					reportDataJSONString.toString());

			Log.i(TAG, "nameValuePair:" + nameValuePair.toString());

			return nameValuePair;

		} catch (JSONException je) {
			return null;
		}

	}

}
