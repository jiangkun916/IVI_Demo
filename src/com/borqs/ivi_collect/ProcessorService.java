package com.borqs.ivi_collect;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.borqs.ivi_collect.collector.DataCollector;
import com.borqs.ivi_collect.util.Util;

public class ProcessorService extends IntentService{
	private static final String TAG = "ProcessorService";

	public ProcessorService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String action = intent.getAction();
		if (TextUtils.isEmpty(action)) {
			return;
		}

		if (action.equals(Util.Action.ACTION_MSG_BOOT)) {
			
			//1. 收集数据  定时发送Context.MODE_PRIVATE
			
			Log.i(TAG ,"------sleep start-------");		
			SystemClock.sleep(60*1000);		
			Log.i(TAG ,"------sleep end-------");

			
//			String powerOnTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()-60*1000));

			String powerOnTime = Long.toString((System.currentTimeMillis()-60*1000)); 

			Editor sharedata = getSharedPreferences("poweron", Context.MODE_PRIVATE).edit();    
			sharedata.putString("time",powerOnTime);    
			sharedata.commit();

			
			DataCollector.set(this);
			
			

		}
	} 	
}
	