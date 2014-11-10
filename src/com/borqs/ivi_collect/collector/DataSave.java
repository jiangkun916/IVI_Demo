package com.borqs.ivi_collect.collector;

import com.borqs.ivi_collect.database.DatabaseHelper;
import com.borqs.ivi_collect.util.Util;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

public class DataSave extends IntentService {
	private static final String TAG = "DataSave";
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
	private DatabaseHelper mOpenHelper;

	public DataSave() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		mOpenHelper = new DatabaseHelper(this);
		String action = intent.getAction();
		if (TextUtils.isEmpty(action)) {
			return;
		}
		if (action.equals(Util.Action.DATA_SAVE)) {
//			if (Util.FirstSend) {

				tuid = intent.getStringExtra(Util.ExtraKeys.TUID);
				imsi = intent.getStringExtra(Util.ExtraKeys.IMSI);
				imei = intent.getStringExtra(Util.ExtraKeys.IMEI);
				Longitude = intent.getStringExtra(Util.ExtraKeys.LONGITUDE);
				Latitude = intent.getStringExtra(Util.ExtraKeys.LATITUDE);
				build = intent.getStringExtra(Util.ExtraKeys.BUILD);
				model = intent.getStringExtra(Util.ExtraKeys.MODEL);
				poweron = intent.getStringExtra(Util.ExtraKeys.POWERON);
				lastpoweroff = intent
						.getStringExtra(Util.ExtraKeys.LASTPOWEROFF);
				time = intent.getStringExtra(Util.ExtraKeys.TIME);

				addAllInformation(tuid, imsi, imei, build, model, poweron,
						lastpoweroff, Longitude, Latitude, time);

//				Util.FirstSend = false;
				Log.i(TAG, "------->>" + Util.FirstSend);
//
//			} else {
//				tuid = intent.getStringExtra(Util.ExtraKeys.TUID);
//				Longitude = intent.getStringExtra(Util.ExtraKeys.LONGITUDE);
//				Latitude = intent.getStringExtra(Util.ExtraKeys.LATITUDE);
//				time = intent.getStringExtra(Util.ExtraKeys.TIME);
//
//				addPartInformation(tuid, Longitude, Latitude, time);
//
//			}

		}

	}

	/**
	 * Save all information to database.
	 */
	public void addAllInformation(String tuid, String imsi, String imei,
			String build, String model, String poweron, String lastpoweroff,
			String Longitude, String Latitude, String time) {

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM all_information", null);
		int count = c.getCount();
		Log.i(TAG, "---------count---------->>" + count);
		
		if(count > 10){

			db.execSQL("DELETE FROM "+ "all_information");

		}
		
		ContentValues values = new ContentValues();
		values.put(Util.ExtraKeys.TUID, tuid);
		values.put(Util.ExtraKeys.IMSI, imsi);
		values.put(Util.ExtraKeys.IMEI, imei);
		values.put(Util.ExtraKeys.BUILD, build);
		values.put(Util.ExtraKeys.MODEL, model);
		values.put(Util.ExtraKeys.POWERON, poweron);
		values.put(Util.ExtraKeys.LASTPOWEROFF, lastpoweroff);
		values.put(Util.ExtraKeys.LONGITUDE, Longitude);
		values.put(Util.ExtraKeys.LATITUDE, Latitude);
		values.put(Util.ExtraKeys.TIME, time);
		Log.i(TAG, "--------values---------->>" + values);
		long rowId = db.insert("all_information", null, values);
		Log.i(TAG, "------------------->>" + rowId);

		db.close();
		
		Util.Longitude = Longitude;
		Log.i(TAG, "---------addAllInformation---------->>" + Util.Longitude);
		Util.Latitude = Latitude;
		Log.i(TAG, "---------addAllInformation---------->>" + Util.Latitude);

	}

	/**
	 * Save part information to database.
	 */
	public void addPartInformation(String tuid, String Longitude,
			String Latitude, String time) {
		
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
	
		Cursor c = db.rawQuery("SELECT * FROM all_information", null);
		int count = c.getCount();
		Log.i(TAG, "---------count---------->>" + count);
		if(count > 10000){

			db.execSQL("DELETE FROM "+ "part_information");
			
		}
		
		ContentValues values = new ContentValues();
		values.put(Util.ExtraKeys.TUID, tuid);
		values.put(Util.ExtraKeys.LONGITUDE, Longitude);
		values.put(Util.ExtraKeys.LATITUDE, Latitude);
		values.put(Util.ExtraKeys.TIME, time);
		
		double lo1 = Double.parseDouble(Util.Longitude);
		Log.i(TAG, "---------lo1---------->>" + lo1);

		double la1 = Double.parseDouble(Util.Latitude);
		Log.i(TAG, "---------la1---------->>" + la1);

		double lo2 = Double.parseDouble(Longitude);
		Log.i(TAG, "---------lo2---------->>" + lo2);

		double la2 = Double.parseDouble(Latitude);
		Log.i(TAG, "---------la2---------->>" + la2);

		if (Util.getDistance(lo1, la1, lo2, la2) > 100) {
			Log.i(TAG, "------------------->>" + values);
			long rowId = db.insert("part_information", null, values);
			Log.i(TAG, "------------------->>" + rowId);
		
			Util.Longitude = Longitude;
			Log.i(TAG, "--------addPartInformation----------->>" + Util.Longitude);
			Util.Latitude = Latitude;
			Log.i(TAG, "--------addPartInformation----------->>" + Util.Latitude);

		}
		db.close();
	}

}
