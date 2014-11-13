package com.borqs.ivi_collect;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.borqs.ivi_collect.database.DatabaseHelper;
import com.borqs.ivi_collect.util.*;
import com.borqs.ivi_collect.util.Util.ReportData;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

public class SendAllService extends IntentService {

	private final static String TAG = "SendAllService";
	private final static String serverUrl = "http://releaseadmin.borqs.com/su/gps.php";
	private DatabaseHelper mOpenHelper;

	public SendAllService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		mOpenHelper = new DatabaseHelper(this);

		// Get action, and make sure it's not null or 0-length
		String action = intent.getAction();
		if (TextUtils.isEmpty(action)) {
			return;
		}
		if (action.equals(Util.Action.SEND_ALL_REPORT)) {

			// Check if the network is available
			if (!Util.isNetworkAvailable(this)) {
				Log.i(TAG, "Network is not available.");
				return;
			}
			// TODO: cpu判断

			SQLiteDatabase db = mOpenHelper.getWritableDatabase();
			Cursor cursor = db.rawQuery("SELECT * FROM all_information", null);
			ReportData result = new ReportData();
			
			if (cursor.moveToFirst()) {

				result.setId(cursor.getString(cursor
						.getColumnIndex(Util.ExtraKeys.COL_ID)));
				result.setTuid(cursor.getString(cursor
						.getColumnIndex(Util.ExtraKeys.TUID)));
				result.setImsi(cursor.getString(cursor
						.getColumnIndex(Util.ExtraKeys.IMSI)));
				result.setImei(cursor.getString(cursor
						.getColumnIndex(Util.ExtraKeys.IMEI)));
				result.setBuild(cursor.getString(cursor
						.getColumnIndex(Util.ExtraKeys.BUILD)));
				result.setModel(cursor.getString(cursor
						.getColumnIndex(Util.ExtraKeys.MODEL)));
				result.setPowerOn(cursor.getString(cursor
						.getColumnIndex(Util.ExtraKeys.POWERON)));
				result.setLastPowerOff(cursor.getString(cursor
						.getColumnIndex(Util.ExtraKeys.LASTPOWEROFF)));
				result.setLongitude(cursor.getString(cursor
						.getColumnIndex(Util.ExtraKeys.LONGITUDE)));
				result.setLatitude(cursor.getString(cursor
						.getColumnIndex(Util.ExtraKeys.LATITUDE)));
				result.setSendTime(cursor.getString(cursor
						.getColumnIndex(Util.ExtraKeys.TIME)));

				Log.i(TAG, result.toString());

			}

			try {
				// Convert report data to JSON string
				NameValuePair reportJsonString = ConvertToJSONString(result);
				if (reportJsonString == null) {
					return;
				}
				String id = result.getId();
				// Send report to server
				long newStatus = -1;
				newStatus = Util.sendReport(serverUrl, reportJsonString);
				Log.i(TAG, "-----newStatus----->>" + newStatus);
				
				if (newStatus == 0) {
					Util.FirstSend = false;
					db.delete("all_information", Util.ExtraKeys.COL_ID + "=" + id, null);
					Log.i(TAG, "------id------>>" + id);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			cursor.close();
			cursor = null;
			db.close();
		}
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
			gps.put(Util.ExtraKeys.LONGITUDE, reportData.Longitude);
			gps.put(Util.ExtraKeys.LATITUDE, reportData.Latitude);
			gps.put(Util.ExtraKeys.TIME, reportData.time);

			// Put these info to reportDataJSONString object
			reportDataJSONString.put(Util.ExtraKeys.BUILD, reportData.build);
			reportDataJSONString.put(Util.ExtraKeys.MODEL, reportData.model);
			reportDataJSONString.put(Util.ExtraKeys.TUID, reportData.tuid);
			reportDataJSONString.put(Util.ExtraKeys.IMSI, reportData.imsi);
			reportDataJSONString.put(Util.ExtraKeys.IMEI, reportData.imei);
			reportDataJSONString.put(Util.ExtraKeys.GPS, gps);
			reportDataJSONString
					.put(Util.ExtraKeys.POWERON, reportData.poweron);
			reportDataJSONString.put(Util.ExtraKeys.LASTPOWEROFF,
					reportData.lastpoweroff);
			NameValuePair nameValuePair = new BasicNameValuePair("info",
					reportDataJSONString.toString());

			Log.i(TAG, "nameValuePair:" + nameValuePair.toString());

			return nameValuePair;

		} catch (JSONException je) {
			return null;
		}
	}

}