package com.borqs.ivi_collect;

import com.borqs.ivi_collect.util.Util;
import com.borqs.ivi_collect.util.Util.ReportData;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;

public class ReportDataWrapper extends IntentService{
	private static final String TAG = "ReportDataWrapper";

	public ReportDataWrapper() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		ReportData reportData = new ReportData();
		
		String action = intent.getAction();
		if (TextUtils.isEmpty(action)) {
			Util.log(TAG, "Invalid action!");
			return;
		}
		if (action.equals(Util.Action.DATA_REPORT)) {
			Util.log(TAG, "Received intent.");
			
			//tuid
			if(action.equals(Util.ExtraKeys.TUID)){
				reportData.tudi = intent.getStringExtra(Util.ExtraKeys.TUID);
				if (TextUtils.isEmpty(reportData.tudi)) {
					throw new IllegalArgumentException("bug tuid is empty!");
				}
				Util.log(TAG, "tuid: " + reportData.tudi);
			}
			//imsi
			if(action.equals(Util.ExtraKeys.IMSI)){
				reportData.imsi = intent.getStringExtra(Util.ExtraKeys.IMSI);
				if (TextUtils.isEmpty(reportData.imsi)) {
					throw new IllegalArgumentException("bug imsi is empty!");
				}
				Util.log(TAG, "imsi: " + reportData.imsi);
			}
			//imei
			if(action.equals(Util.ExtraKeys.IMEI)){
				reportData.imei = intent.getStringExtra(Util.ExtraKeys.IMEI);
				if (TextUtils.isEmpty(reportData.imei)) {
					throw new IllegalArgumentException("bug imei is empty!");
				}
				Util.log(TAG, "imei: " + reportData.imei);
			}
			//Longitude
			if(action.equals(Util.ExtraKeys.LONGITUDE)){
				reportData.Longitude = intent.getStringExtra(Util.ExtraKeys.LONGITUDE);
				if (TextUtils.isEmpty(reportData.Longitude)) {
					throw new IllegalArgumentException("bug Longitude is empty!");
				}
				Util.log(TAG, "Longitude: " + reportData.Longitude);
			}
			//Latitude
			if(action.equals(Util.ExtraKeys.LATITUDE)){
				reportData.Latitude = intent.getStringExtra(Util.ExtraKeys.LATITUDE);
				if (TextUtils.isEmpty(reportData.Latitude)) {
					throw new IllegalArgumentException("bug Latitude is empty!");
				}
				Util.log(TAG, "Latitude: " + reportData.Latitude);
			}
			
		}
		
	}

}
