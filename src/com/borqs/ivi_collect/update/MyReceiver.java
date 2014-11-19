package com.borqs.ivi_collect.update;


import com.borqs.ivi_collect.util.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
	private final String TAG = "MyReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		// Get action, and make sure it's not null or 0-length
		String action = intent.getAction();
		if (TextUtils.isEmpty(action)) {
			return;
		}
		// Create a new intent
		Intent target = new Intent();

		// Intent.ACTION_BOOT_COMPLETED
		if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){

			target.setAction(Util.Action.INSTALL_APK);

		}

		else {
			// If there are other system messages...
			return;
		}

		// Transfer the message to processor service
		if (target != null) {
			Log.i(TAG, "=======ACTION_BOOT_COMPLETED=======");
			context.startService(target);
			target = null;
		}
	}

}
