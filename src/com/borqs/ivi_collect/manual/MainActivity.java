package com.borqs.ivi_collect.manual;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.borqs.ivi_collect.R;
import com.borqs.ivi_collect.util.Util;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TextView tuidtv = (TextView)findViewById(R.id.tuid);
//		TextView imsitv = (TextView)findViewById(R.id.imsi);
//		TextView imeitv = (TextView)findViewById(R.id.imei);
//		TextView gpstv = (TextView)findViewById(R.id.gps);
		
		Intent iviIntent = this.getIntent();
		String tuidStr = iviIntent.getStringExtra(Util.ExtraKeys.reportJsonString);
//		String imsiStr = iviIntent.getStringExtra(Util.ExtraKeys.IMSI);
//		String imeiStr = iviIntent.getStringExtra(Util.ExtraKeys.IMEI);
//		String latStr = iviIntent.getStringExtra(Util.ExtraKeys.LATITUDE);
//		String lngStr = iviIntent.getStringExtra(Util.ExtraKeys.LONGITUDE);
		
		
		tuidtv.setText(tuidStr); 
//		imsitv.setText(imsiStr);
//		imeitv.setText(imeiStr);
//		gpstv.setText(latStr+":::"+lngStr);
//		
	}

	
}
