package com.borqs.ivi_collect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.borqs.ivi_collect.util.Util;
import com.borqs.ivi_collect.util.Util.ReportLittleData;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

public class SendLittleService extends IntentService {

	private final static String TAG = "SendLittleService";
	private final static String serverUrl = "http://releaseadmin.borqs.com/su/gps.php";

	private String tuid = null;
	private String Latitude = null;
	private String Longitude = null;
	private String sendtime = null;

	public SendLittleService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		// Get action, and make sure it's not null or 0-length
		String action = intent.getAction();
		if (TextUtils.isEmpty(action)) {
			return;
		}
		if (action.equals(Util.Action.SEND_LITTLE_REPORT)) {
			// Check if the network is available

			// if (!Util.isNetworkAvailable(this)){
			// Log.i(TAG, "Network is not available.");
			// return;
			// }

			tuid = intent.getStringExtra(Util.ExtraKeys.TUID);
			Longitude = intent.getStringExtra(Util.ExtraKeys.LONGITUDE);
			Latitude = intent.getStringExtra(Util.ExtraKeys.LATITUDE);
			sendtime = intent.getStringExtra(Util.ExtraKeys.SENDTIME);

			ReportLittleData reportLittleData = new ReportLittleData();
			reportLittleData.setTuid(tuid);
			reportLittleData.setLongitude(Longitude);
			reportLittleData.setLatitude(Latitude);
			reportLittleData.setSendTime(sendtime);

			NameValuePair reportJsonString = ConvertToJSONString(reportLittleData);

			if (reportJsonString == null) {
				return;
			}
			// Send report to server
			long newStatus = -1;
			newStatus = Util.sendReport(serverUrl, reportJsonString);
			if (newStatus != 0) {
				File file = null;
				file = new File("/sdcard/c.txt");
				try {
					if (!file.exists())
						file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					FileWriter fw = new FileWriter(file, true);
					BufferedWriter bw = new BufferedWriter(fw);
					if (file.length() < 1560) {
						bw.write(reportJsonString.toString());
						bw.flush();
						bw.write("\n");
						bw.flush();
					}else{
						BufferedReader br = new BufferedReader(new FileReader("/sdcard/c.txt"));
						BufferedWriter bw1 = new BufferedWriter(new FileWriter("/sdcard/c.txt"));
						String line = null;
						while((line = br.readLine())!= null){
							bw1.write(line);
							bw1.newLine();
							bw1.flush();
						}
						bw.write(reportJsonString.toString());
						bw.flush();
						bw.write("\n");
						bw.flush();
						br.close();
						bw1.close();
					}
					bw.close();
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
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
	public NameValuePair ConvertToJSONString(ReportLittleData reportLittleData) {
		try {
			// Define the report data JSON string object
			JSONObject reportDataJSONString = new JSONObject();

			// Define gps JSON string object
			JSONObject gps = new JSONObject();

			// Get gps
			gps.put(Util.ExtraKeys.LONGITUDE, reportLittleData.Longitude);
			gps.put(Util.ExtraKeys.LATITUDE, reportLittleData.Latitude);
			gps.put(Util.ExtraKeys.SENDTIME, reportLittleData.sendtime);

			// Put these info to reportDataJSONString object
			reportDataJSONString
					.put(Util.ExtraKeys.TUID, reportLittleData.tuid);
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
