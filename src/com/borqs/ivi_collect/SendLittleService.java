package com.borqs.ivi_collect;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.borqs.ivi_collect.database.DatabaseHelper;
import com.borqs.ivi_collect.util.Util;
import com.borqs.ivi_collect.util.Util.Report;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

public class SendLittleService extends IntentService {

	private final static String TAG = "SendLittleService";
	private final static String serverUrl = "http://releaseadmin.borqs.com/su/gps.php";
	private String[] lng = new String[5]; 
	private String[] lat = new String[5]; 
	private String[] time = new String[5]; 
	private String tuid = null;
	private String id = null;

	private DatabaseHelper mOpenHelper;


	public SendLittleService() {
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
		if (action.equals(Util.Action.SEND_LITTLE_REPORT)) {

			// Check if the network is available
			if (!Util.isNetworkAvailable(this)) {
				Log.i(TAG, "Network is not available.");
				return;
			}
			// TODO: cpu判断


			SQLiteDatabase db = mOpenHelper.getWritableDatabase();
			Cursor cursor = db.rawQuery("SELECT * FROM all_information", null);
					
			int count = cursor.getCount();
			
			
			if (count >= 5) {
				if (cursor.moveToFirst()) {
					for (int i = 0; i < 5; i++) {
						tuid = cursor.getString(cursor.getColumnIndex(Util.ExtraKeys.TUID));
						Log.i(TAG, "------tuid------>>" + tuid);

						lng[i]=cursor.getString(cursor
								.getColumnIndex(Util.ExtraKeys.LONGITUDE));
						Log.i(TAG, "------lng------>>" + lng[i]);

						lat[i]=cursor.getString(cursor
								.getColumnIndex(Util.ExtraKeys.LATITUDE));
						Log.i(TAG, "------lat------>>" + lat[i]);

						time[i]=cursor.getString(cursor
								.getColumnIndex(Util.ExtraKeys.TIME));
						Log.i(TAG, "------time------>>" + time[i]);
						
						id = cursor.getString(cursor
								.getColumnIndex(Util.ExtraKeys.COL_ID));
						Log.i(TAG, "------id------>>" + id);
						
						cursor.moveToNext();
					}
				}
			} else{
				cursor.close();
				cursor = null;
				db.close();
				return;
			}

			Report report = new Report();
			report.setTuid(tuid);
			
			report.setLng1(lng[0]);
			report.setLat1(lat[0]);
			report.setTime1(time[0]);

			report.setLng2(lng[1]);
			report.setLat2(lat[1]);
			report.setTime2(time[1]);
			
			report.setLng3(lng[2]);
			report.setLat3(lat[2]);
			report.setTime3(time[2]);
			
			report.setLng4(lng[3]);
			report.setLat4(lat[3]);
			report.setTime4(time[3]);
			
			report.setLng5(lng[4]);
			report.setLat5(lat[4]);
			report.setTime5(time[4]);
			
//			NameValuePair reportJsonString = ConvertToJSONString(report);
			
//			db.execSQL("DELETE FROM all_information where _id<="+id);
//			
//			cursor.close();
//			cursor = null;
//			db.close();
			
			try {
				// Convert report data to JSON string
				NameValuePair reportJsonString = ConvertToJSONString(report);
				if (reportJsonString == null) {
					return;
				}
				// Send report to server
				long newStatus = -1;
				newStatus = Util.sendReport(serverUrl, reportJsonString);
				Log.i(TAG, "-----newStatus----->>" + newStatus);
				
				if (newStatus == 0) {
					db.execSQL("DELETE FROM all_information where _id<="+id);
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
	 * Convert the reportLittleData into JSON string which can be send directly.
	 * JSONObject: ReportDataJSONString Extras: tuid gps (longitude, latitude,
	 * sendtime)
	 * 
	 * @param string
	 * @return NameValuePair
	 */
	public NameValuePair ConvertToJSONString(Report report) {
		try {
			// Define the report data JSON string object
			JSONObject reportDataJSONString = new JSONObject();

			// Define gps JSON string object
			JSONObject gps1 = new JSONObject();			
			JSONObject gps2 = new JSONObject();
			JSONObject gps3 = new JSONObject();
			JSONObject gps4 = new JSONObject();
			JSONObject gps5 = new JSONObject();

			// Get gps
			gps1.put(Util.ExtraKeys.LONGITUDE, report.lng1);
			gps1.put(Util.ExtraKeys.LATITUDE, report.lat1);
			gps1.put(Util.ExtraKeys.TIME, report.time1);

			gps2.put(Util.ExtraKeys.LONGITUDE, report.lng2);
			gps2.put(Util.ExtraKeys.LATITUDE, report.lat2);
			gps2.put(Util.ExtraKeys.TIME, report.time2);
			
			gps3.put(Util.ExtraKeys.LONGITUDE, report.lng3);
			gps3.put(Util.ExtraKeys.LATITUDE, report.lat3);
			gps3.put(Util.ExtraKeys.TIME, report.time3);
			
			gps4.put(Util.ExtraKeys.LONGITUDE, report.lng4);
			gps4.put(Util.ExtraKeys.LATITUDE, report.lat4);
			gps4.put(Util.ExtraKeys.TIME, report.time4);
			
			gps5.put(Util.ExtraKeys.LONGITUDE, report.lng5);
			gps5.put(Util.ExtraKeys.LATITUDE, report.lat5);
			gps5.put(Util.ExtraKeys.TIME, report.time5);
			
			// Put these info to reportDataJSONString object
			reportDataJSONString.put(Util.ExtraKeys.TUID, report.tuid);
			
			reportDataJSONString.put("gps1", gps1);
			reportDataJSONString.put("gps2", gps2);
			reportDataJSONString.put("gps3", gps3);
			reportDataJSONString.put("gps4", gps4);
			reportDataJSONString.put("gps5", gps5);
			

			NameValuePair nameValuePair = new BasicNameValuePair("info",
					reportDataJSONString.toString());

			Log.i(TAG, "nameValuePair:" + nameValuePair.toString());

			return nameValuePair;

		} catch (JSONException je) {
			return null;
		}
	}

}
