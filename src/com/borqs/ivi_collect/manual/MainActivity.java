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

		
		Intent iviIntent = this.getIntent();
		String tuidStr = iviIntent.getStringExtra(Util.ExtraKeys.reportJsonString);

		
		tuidtv.setText(tuidStr); 

	
	}

	
}
