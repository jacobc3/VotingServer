package com.example.votingserver;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	//TODO need to handle resume problem?
	//TODO on destroy : auto stop and save previous result
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button startB = (Button) findViewById(R.id.start_b);
		startB.setClickable(true);
		startB.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				 Intent i= new Intent(MainActivity.this, SetupActivity.class);
                 startActivity(i);
			}			
		});
		
		Button stopB = (Button) findViewById(R.id.stop_b);
		stopB.setClickable(false);
		
		Button chartB = (Button) findViewById(R.id.chart_b);
		chartB.setClickable(false);
		chartB.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				 Intent i= new Intent(MainActivity.this, ChartActivity.class);
                 startActivity(i);
			}			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
