package com.borqs.ivi_collect;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.borqs.ivi_collect.database.DatabaseHelper;
import com.borqs.ivi_collect.util.Util;
import com.borqs.ivi_collect.util.Util.Report;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

public class SendLittleService extends IntentService {

	private final static String TAG = "SendLittleService";
	private final String serverUrl = "http://releaseadmin.borqs.com/su/gps.php";
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
			Cursor cursor = db.rawQuery("SELECT * FROM part_information", null);

			int count = cursor.getCount();
			if (count >= 5) {
				if (cursor.moveToFirst()) {
					for (int i = 0; i < 5; i++) {
						tuid = cursor.getString(cursor
								.getColumnIndex(Util.ExtraKeys.TUID));
						Log.i(TAG, "------tuid------>>" + tuid);

						lng[i] = cursor.getString(cursor
								.getColumnIndex(Util.ExtraKeys.LONGITUDE));
						Log.i(TAG, "------"+lng[i]+"------>>" + lng[i]);

						lat[i] = cursor.getString(cursor
								.getColumnIndex(Util.ExtraKeys.LATITUDE));
						Log.i(TAG, "------"+lat[i]+"------>>" + lat[i]);

						time[i] = cursor.getString(cursor
								.getColumnIndex(Util.ExtraKeys.TIME));
						Log.i(TAG, "------"+time[i]+"------>>" + time[i]);

						id = cursor.getString(cursor
								.getColumnIndex(Util.ExtraKeys.COL_ID));
						Log.i(TAG, "------id------>>" + id);

						cursor.moveToNext();
					}
				}
			} else {
				cursor.close();
				cursor = null;
				db.close();
				return;
			}

			cursor.close();
			cursor = null;
			Report report = new Report();
			report.setTuid(tuid);
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

				if (newStatus == 0000) {
					db.execSQL("DELETE FROM part_information where _id<=" + id);
					Log.i(TAG, "------id------>>" + id);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			db.close();
			displayBriefMemory();
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
			JSONObject[] gpsobject = new JSONObject[5];
			JSONArray gps = new JSONArray();
			for(int i=0;i<5;i++ ){
				gpsobject[i]= new JSONObject();
				gpsobject[i].put("lng", lng[i]);
				gpsobject[i].put("lat", lat[i]);
				gpsobject[i].put("t", time[i]);
				gps.put(gpsobject[i]);
				
			}


			
			// Put these info to reportDataJSONString object
			reportDataJSONString.put(Util.ExtraKeys.TUID, report.tuid);
			reportDataJSONString.put("gps", gps);
			
			reportDataJSONString.put(Util.ExtraKeys.TYPE, "B");

			NameValuePair nameValuePair = new BasicNameValuePair("info",
					reportDataJSONString.toString());
			
			Log.i(TAG, "nameValuePair:" + nameValuePair.toString());

			return nameValuePair;

		} catch (JSONException je) {
			return null;
		}
	}
	 private void displayBriefMemory() {        
         final ActivityManager activityManager = (ActivityManager)   getSystemService(ACTIVITY_SERVICE);    
 
       ActivityManager.MemoryInfo   info = new ActivityManager.MemoryInfo();   
 
         activityManager.getMemoryInfo(info);    
 
         Log.i("memoryinfo","系统剩余内存:"+(info.availMem >> 10)+"k");   
 
         Log.i("memoryinfo","系统是否处于低内存运行："+info.lowMemory);
 
         Log.i("memoryinfo","当系统剩余内存低于"+info.threshold+"时就看成低内存运行");
 
   }
}
