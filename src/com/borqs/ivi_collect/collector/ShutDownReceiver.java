package com.borqs.ivi_collect.collector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ShutDownReceiver extends BroadcastReceiver {
	
	private static final String TAG = "ShutDownReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {

		String shutDownTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
		Log.i(TAG, "启动关闭中...");
		Log.i(TAG, shutDownTime);


//		File file = new File("/sdcard/b.txt");
//		if (file.exists()) {
//			file.delete();
//		}

		
		try {
			FileOutputStream fs = new FileOutputStream(
					new File("/sdcard/a.txt"));
			fs.write(shutDownTime.getBytes());
			fs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
