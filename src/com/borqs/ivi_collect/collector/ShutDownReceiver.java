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

	@Override
	public void onReceive(Context context, Intent intent) {
		long start = System.currentTimeMillis();
		Date d = new Date(start);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(d);
		Log.i("MainActivity", "启动关闭中...");
		Log.i("MainActivity", time);

		File file = null;
		file = new File("/sdcard/b.txt");
		if (file.exists()) {
			file.delete();
		}
		
		File filec = new File("/sdcard/c.txt");
		if (filec.exists()) {
			filec.delete();
		}
		
		try {
			FileOutputStream fs = new FileOutputStream(
					new File("/sdcard/a.txt"));
			fs.write(time.getBytes());
			fs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
