package com.example.votingserver;

import background.MyMessage;
import background.Processor;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	// TODO on destroy : auto stop and save previous result

	private BroadcastReceiver mIntentReceiver;
	private int smsCount = 0;
	private TextView[] tvs = new TextView[5];
	
	
	private Processor processor = new Processor();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.i(TAG, "onCreate()");
		Button startB = (Button) findViewById(R.id.start_b);
		startB.setClickable(true);
		startB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MainActivity.this, SetupActivity.class);
				startActivity(i);
			}
		});

		Button stopB = (Button) findViewById(R.id.stop_b);
		stopB.setClickable(false);

		Button chartB = (Button) findViewById(R.id.chart_b);
		chartB.setClickable(false);
		chartB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MainActivity.this, ChartActivity.class);
				startActivity(i);
			}
		});
		
		tvs[0] = (TextView) findViewById(R.id.sms0_tv);
		tvs[1] = (TextView) findViewById(R.id.sms1_tv);
		tvs[2] = (TextView) findViewById(R.id.sms2_tv);
		tvs[3] = (TextView) findViewById(R.id.sms3_tv);
		tvs[4] = (TextView) findViewById(R.id.sms4_tv);
		IntentFilter intentFilter = new IntentFilter("SmsMessage.intent.MAIN");
		mIntentReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String msg = intent.getStringExtra("get_msg");

				// Process the sms format and extract body & phoneNumber
				msg = msg.replace("\n", "");
				String body = msg.substring(msg.lastIndexOf(":") + 1,
						msg.length());
				String pNumber = msg.substring(0, msg.lastIndexOf(":"));
				Log.i(TAG, "OnReceiver()");
				Log.i(TAG, "body is "+body);
				Log.i(TAG, "pNumber is "+pNumber);	
				MyMessage newmm = new MyMessage(pNumber,body);
				processor.addMessage(newmm);
				String tvText = pNumber+":"+body;
				if(processor.isExist(newmm)){
					tvText = tvText+" (duplicated)";
					tvs[smsCount%5].setText(tvText);
				} else {
					tvs[smsCount%5].setText(tvText);
					smsCount++;					
				}						
			}
		};
		this.registerReceiver(mIntentReceiver, intentFilter);	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	protected void onResume() {
		Log.i(TAG, "onResume()");
		super.onResume();
		
	}
	@Override
	protected void onStop() {
		Log.i(TAG, "onResume()");
		super.onStop();
	}
	protected void onDestory() {
		Log.i(TAG, "onDestory()");
		try{		
			unregisterReceiver(mIntentReceiver);
		}
		catch(Exception e){
			
		}
		super.onStop();
	}
	
}
