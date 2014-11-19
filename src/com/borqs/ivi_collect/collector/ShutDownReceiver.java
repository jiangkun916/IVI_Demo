package com.borqs.ivi_collect.collector;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class ShutDownReceiver extends BroadcastReceiver {
	
	private final String TAG = "ShutDownReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
//		intent.ACTION_SHUTDOWN;
//		String shutDownTime = Long.toString(System.currentTimeMillis());
//		Log.i(TAG, "启动关闭中...");
//		Log.i(TAG, shutDownTime);
//		
//		try {
//			FileOutputStream fs = new FileOutputStream(
//					new File("/sdcard/a.txt"));
//			fs.write(shutDownTime.getBytes());
//			fs.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		

		String shutDownTime = Long.toString(System.currentTimeMillis()); 
		Log.i(TAG, "启动关闭中...");
		Log.i(TAG, shutDownTime);
		Editor sharedata = context.getSharedPreferences("poweroff", Context.MODE_PRIVATE).edit();    
		sharedata.putString("time",shutDownTime);    
		sharedata.commit();
	}
}
