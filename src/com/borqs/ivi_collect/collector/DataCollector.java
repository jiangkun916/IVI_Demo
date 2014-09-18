package com.borqs.ivi_collect.collector;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.borqs.ivi_collect.util.Util;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.SystemClock;
import android.util.Log;

public class DataCollector extends IntentService{

	private final static String TAG = "LiveTimeCollector";	
	private static long recordInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
	private static long submitInterval = 30 * 60 * 1000;// 30 min

	public static final String LIVE_TIME_RECORD = "live_time_record.txt";
	public static final String LATITUDE = "Latitude";
	public static final String LONGITUDE = "Longitude";
	public static double lat = 0;
	public static double lng = 0;

	public DataCollector() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		Intent target = new Intent();
		
		
		//得到经维度
		LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location loc = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (loc != null) {
		    lat = loc.getLatitude();
		    lng = loc.getLongitude();
//	        Log.i("SuperMap", "Location changed : Lat: "  + loc.getLatitude() + " Lng: "  + loc.getLongitude());  
		}
		
		
		
		target.setAction(Util.Action.BUG_NOTIFY);
		target.putExtra(LATITUDE, lat);
		target.putExtra(LONGITUDE, lng);

		
		
		startService(target);		
//		long[] last = getLastLiveTime(this);
//		long time = 0;
//		if(last[0]==-1 && last[1]==-1){
//			time = 0;
//		}
//		else{
//			long elapsedSinceBoot = SystemClock.elapsedRealtime();
//			if (elapsedSinceBoot < last[0]) {
//				time = last[1] + elapsedSinceBoot;
//			}else {
//				time = last[1] + (elapsedSinceBoot - last[0]);
//				//存活时间
//			}
//		}
//		if(time >= submitInterval){
//			target.setAction(Util.Action.BUG_NOTIFY);
//			
//			
//			
//			
//			
//			startService(target);		
//			time = 0;
//		}
//		setCurrentLiveTime(this, time);

	}
	
	public static void set(Context context) {
		
		Intent liveTimeIntent = new Intent(Util.Action.MONITOR_LIVE_TIME);
		PendingIntent liveTimeTriger = PendingIntent.getService(context, 0, liveTimeIntent, 0);
		//launch 5 mins later to avoid too many works on receiving boot completed. 
		long firstTime = System.currentTimeMillis() + 5 * 60 * 1000;
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC, firstTime, recordInterval, liveTimeTriger);
		


	}
	
	
	/*
	 * Set current time.
	 */
//	private void setCurrentLiveTime(Context context, long time) {
//		DataOutputStream dos = null;
//		try {
//			dos = new DataOutputStream(this.openFileOutput(LIVE_TIME_RECORD, Context.MODE_PRIVATE));
//			dos.writeLong(SystemClock.elapsedRealtime());
//			dos.writeLong(time);
//		}catch (Exception e) {
//			e.printStackTrace();
//		}finally {
//			if (dos != null){
//				try {
//					if (dos != null)
//						dos.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
	
	/*
	 * Get last live time.
	 */
//	private long[] getLastLiveTime(Context context) {
//		long lastTime = -1;
//		long lastValue = -1;
//		DataInputStream dis = null;
//		try {
//			dis = new DataInputStream(context.openFileInput(LIVE_TIME_RECORD));
//			lastTime = dis.readLong();
//			lastValue = dis.readLong();
//		}catch (Exception e) {
//			e.printStackTrace();
//		}finally {
//			try {
//				if (dis != null)
//					dis.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return new long[] {lastTime, lastValue};
//	}
	

}
