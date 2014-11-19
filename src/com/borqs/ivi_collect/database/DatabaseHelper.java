package com.borqs.ivi_collect.database;

import com.borqs.ivi_collect.util.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "ivi_collect.db";
	private final String TAG = "DatabaseHelper";
	private final String ALL_INFORMATION = "all_information";
	private final String PART_INFORMATION = "part_information";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(TAG, "Create Database!");

		// Table all information
		db.execSQL("CREATE TABLE IF NOT EXISTS " + ALL_INFORMATION + " ("
				+ " _id " + " INTEGER PRIMARY KEY," 
				+ Util.ExtraKeys.TUID + " TEXT,"
				+ Util.ExtraKeys.IMSI + " TEXT,"
				+ Util.ExtraKeys.IMEI + " TEXT,"
				+ Util.ExtraKeys.BUILD + " TEXT,"
				+ Util.ExtraKeys.MODEL + " TEXT,"
				+ Util.ExtraKeys.POWERON + " TEXT,"
				+ Util.ExtraKeys.LASTPOWEROFF + " TEXT,"
				+ Util.ExtraKeys.LONGITUDE + " TEXT,"
				+ Util.ExtraKeys.LATITUDE + " TEXT,"
				+ Util.ExtraKeys.TIME + " TEXT" + ");");

		// Table part information
		db.execSQL("CREATE TABLE IF NOT EXISTS " + PART_INFORMATION + " ("
				+ " _id " + " INTEGER PRIMARY KEY,"
				+ Util.ExtraKeys.TUID + " TEXT,"
				+ Util.ExtraKeys.LONGITUDE + " TEXT,"
				+ Util.ExtraKeys.LATITUDE + " TEXT,"
				+ Util.ExtraKeys.TIME + " TEXT" + ");");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "Update Database!");
		db.execSQL("DROP TABLE IF EXISTS " + ALL_INFORMATION);
		db.execSQL("DROP TABLE IF EXISTS " + PART_INFORMATION);
		onCreate(db);

	}

}
