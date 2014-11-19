package com.borqs.ivi_collect;

import com.borqs.ivi_collect.util.*;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

;

public class SendAllService extends IntentService {

	private final static String TAG = "SendAllService";

	public SendAllService() {
		super(TAG);
	}

	private static SendAllService sendall;

	public static SendAllService getContext() {
		return sendall;
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		super.onCreate();
		sendall = this;

		UtilThread u = new UtilThread();
		u.start();
		
		displayBriefMemory();

	}

	private void displayBriefMemory() {
		final ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();

		activityManager.getMemoryInfo(info);

		Log.i("memoryinfo", "系统剩余内存:" + (info.availMem >> 10) + "k");

		Log.i("memoryinfo", "系统是否处于低内存运行：" + info.lowMemory);

		Log.i("memoryinfo", "当系统剩余内存低于" + info.threshold + "时就看成低内存运行");
	}
}
