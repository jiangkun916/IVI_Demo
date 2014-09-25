package com.borqs.ivi_collect;

import android.app.IntentService;
import android.content.Intent;
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
			//1. 收集数据  定时发送
			Log.i(TAG ,"------ACTION_MSG_BOOT-------");
			DataCollector.set(this);

		}
	} 	
}
	