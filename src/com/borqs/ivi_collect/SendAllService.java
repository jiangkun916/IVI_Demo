package com.borqs.ivi_collect;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;



import com.borqs.ivi_collect.util.*;
import com.borqs.ivi_collect.util.Util.ReportData;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

public class SendAllService extends IntentService{

	private final static String TAG = "SendAllService";	
	private final static String serverUrl = "http://releaseadmin.borqs.com/su/gps.php";
	private String imsi = null;
	private String imei = null;
	private String tuid = null;
	private String Latitude = null;
	private String Longitude = null;
	private String build_number = null;
	private String model = null;
	private String poweron = null;
	private String lastpoweroff = null;
	private String sendtime = null;

	public SendAllService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		//Get action, and make sure it's not null or 0-length
		String action = intent.getAction();
		if (TextUtils.isEmpty(action)) {
			return;
		}
		if (action.equals(Util.Action.SEND_ALL_REPORT)) {
			//Check if the network is available
			
			
//			if (!Util.isNetworkAvailable(this)){
//				Log.i(TAG, "Network is not available.");
//	    		return;
//	    	}
	
			tuid = intent.getStringExtra(Util.ExtraKeys.TUID);
			imsi = intent.getStringExtra(Util.ExtraKeys.IMSI);
			imei = intent.getStringExtra(Util.ExtraKeys.IMEI);
			Longitude = intent.getStringExtra(Util.ExtraKeys.LONGITUDE);
			Latitude = intent.getStringExtra(Util.ExtraKeys.LATITUDE);
			build_number = intent.getStringExtra(Util.ExtraKeys.BUILD_NUMBER);
			model = intent.getStringExtra(Util.ExtraKeys.MODEL);
			poweron = intent.getStringExtra(Util.ExtraKeys.POWERON);
			lastpoweroff = intent.getStringExtra(Util.ExtraKeys.LASTPOWEROFF);
			sendtime = intent.getStringExtra(Util.ExtraKeys.SENDTIME);
	
			ReportData reportData = new ReportData();
			reportData.setTuid(tuid);
			reportData.setImsi(imsi);
			reportData.setImei(imei);
			reportData.setLongitude(Longitude);
			reportData.setLatitude(Latitude);
			reportData.setBuild_number(build_number);
			reportData.setModel(model);
			reportData.setPowerOn(poweron);
			reportData.setLastPowerOff(lastpoweroff);
			reportData.setSendTime(sendtime);
				
			NameValuePair reportJsonString = ConvertToJSONString(reportData);
			
			if(reportJsonString == null){
				return;
			}
			
			//Send report to server 
			long newStatus = -1;
			newStatus = Util.sendReport(serverUrl, reportJsonString);
			
			if(newStatus != 0){

				try {
					FileOutputStream fs = new FileOutputStream(
							new File("/sdcard/b.txt"));
					fs.write(reportJsonString.toString().getBytes());
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	/**
	 * Convert the reportdata into JSON string which can be send directly. 
	 * JSONObject: ReportDataJSONString
	 * Extras: tuid
	 *         imsi
	 *         imei
	 *         gps (longitude, latitude,sendtime)
	 *         build_number
	 *         model
	 *         poweron
	 *         lastpoweroff
	 * 
	 * @param string
	 * @return NameValuePair
	 */
	public NameValuePair ConvertToJSONString(ReportData reportData) {
		try{
			//Define the report data JSON string object
			JSONObject reportDataJSONString = new JSONObject();
			
			//Define gps JSON string object
			JSONObject gps = new JSONObject();
			
			//Get gps
			gps.put(Util.ExtraKeys.LONGITUDE, reportData.Longitude );
			gps.put(Util.ExtraKeys.LATITUDE, reportData.Latitude );
			gps.put(Util.ExtraKeys.SENDTIME, reportData.sendtime);
	
			//Put these info to reportDataJSONString object
			reportDataJSONString.put(Util.ExtraKeys.BUILD_NUMBER, reportData.build_number);
			reportDataJSONString.put(Util.ExtraKeys.MODEL, reportData.model);
			reportDataJSONString.put(Util.ExtraKeys.TUID, reportData.tuid);
			reportDataJSONString.put(Util.ExtraKeys.IMSI, reportData.imsi);
			reportDataJSONString.put(Util.ExtraKeys.IMEI, reportData.imei);
			reportDataJSONString.put(Util.ExtraKeys.GPS, gps);
			reportDataJSONString.put(Util.ExtraKeys.POWERON, reportData.poweron);
			reportDataJSONString.put(Util.ExtraKeys.LASTPOWEROFF, reportData.lastpoweroff);
            NameValuePair nameValuePair = new BasicNameValuePair("info", reportDataJSONString.toString()); 

            Log.i(TAG,"nameValuePair:"+nameValuePair.toString());
           
            
			return nameValuePair; 
			
		}catch(JSONException je){
			return null;
		}
	}
	
	
		
}