package com.borqs.ivi_collect.collector;

import com.borqs.ivi_collect.util.Util;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class DataCollector extends IntentService{

	private final static String TAG = "LiveTimeCollector";	
	private static long recordInterval = AlarmManager.INTERVAL_HOUR;

	public DataCollector() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		
	}
	
	public static void set(Context context) {
		
		Intent liveTimeIntent = new Intent(Util.Action.MONITOR_LIVE_TIME);
		PendingIntent liveTimeTriger = PendingIntent.getService(context, 0, liveTimeIntent, 0);
		//launch 5 mins later to avoid too many works on receiving boot completed. 
		long firstTime = System.currentTimeMillis() + 5 * 60 * 1000;
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC, firstTime, recordInterval, liveTimeTriger);


	}
	

}
