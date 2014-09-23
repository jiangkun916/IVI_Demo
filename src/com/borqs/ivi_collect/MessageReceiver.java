package com.borqs.ivi_collect;

import com.borqs.ivi_collect.util.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;

/**
 * 
 * This class receives system messages including boot complete .
 */

public class MessageReceiver extends BroadcastReceiver {

	private static final String TAG = "MessageReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		//Get action, and make sure it's not null or 0-length
		
		String action = intent.getAction();
		if (TextUtils.isEmpty(action)) {
			return;
		}
		//Create a new intent
		Intent target = new Intent();

		//Intent.ACTION_BOOT_COMPLETED
		if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
//			SystemClock.sleep(60*1000);
			Util.log(TAG ,"=======ACTION_BOOT_COMPLETED=======");
			target.setAction(Util.Action.ACTION_MSG_BOOT);
		}

		else{
			//If there are other system messages...
			return;
		}
		
		//Transfer the message to processor service
		if (target != null){
			context.startService(target);
			target = null;
		}
				
	}

}
