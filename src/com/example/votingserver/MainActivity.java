package com.example.votingserver;

import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import background.Candidate;
import background.MyMessage;
import background.Processor;

public class MainActivity extends Activity{
	private static final String TAG = "MainActivity";
	// TODO on destroy : auto stop and save previous result

	private BroadcastReceiver mIntentReceiver;
	private int smsCount = 0;
	private TextView[] tvs = new TextView[5];
    private ArrayList<String> listNums = new ArrayList<String>();
    ListView candListView;

	
	private Processor processor = new Processor();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("main");
        tabSpec.setContent(R.id.tabMain);
        tabSpec.setIndicator("Main");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("server");
        tabSpec.setContent(R.id.tabServer);
        tabSpec.setIndicator("Server");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("chart");
        tabSpec.setContent(R.id.tabChart);
        tabSpec.setIndicator("Chart");
        tabHost.addTab(tabSpec);



        /*
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
		*/

        //Chart Tab
        //Just create a list of all the candidates if the server has been started
        final ArrayAdapter<Candidate>  adapter = new CandidateListAdapter();
        candListView = (ListView) findViewById(R.id.listView);
        candListView.setAdapter(adapter);

        //Main Tab
        //For the most part I didn't touch what you did, all i added was the if statement at the end to tell whether to count a vote or not
        final int[] offset = {0};
		tvs[0] = (TextView) findViewById(R.id.sms0_tv);
		tvs[1] = (TextView) findViewById(R.id.sms1_tv);
		tvs[2] = (TextView) findViewById(R.id.sms2_tv);
		tvs[3] = (TextView) findViewById(R.id.sms3_tv);
		tvs[4] = (TextView) findViewById(R.id.sms4_tv);
        final SmsManager smsManager = SmsManager.getDefault();
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
				String tvText = newmm.getSender()+":"+ newmm.getContent();
                //String tvText = pn + ":" + cand;
                int vote = processor.hasCandidate(Integer.parseInt(body));
                if (vote > -1) {
                    if (listNums.contains(pNumber)) {
                        tvText = tvText+" (duplicated)";
                        if (processor.isRunning() && processor.allowDupes()) {
                            processor.voteFor(vote);
                            smsManager.sendTextMessage(pNumber, null, "You have already voted but duplicate votes are current allowed.  Thank you for voting.", null, null);
                        } else if (processor.isRunning() && !processor.allowDupes()) {
                            smsManager.sendTextMessage(pNumber, null, "Sorry but you have already voted.", null, null);
                        }
                    } else {
                        listNums.add(pNumber);
                        if (processor.isRunning()) {
                            processor.voteFor(vote);
                            smsManager.sendTextMessage(pNumber, null, "Your vote has been tallied.  Thank you for voting.", null, null);
                        }
                    }
                } else {
                    smsManager.sendTextMessage(pNumber, null, "Sorry but the candidate you are trying to vote for does not exist.", null, null);
                }
                adapter.notifyDataSetChanged();
				tvs[smsCount%5+ offset[0]].setText(tvText);
                smsCount++;
			}
		};
		this.registerReceiver(mIntentReceiver, intentFilter);
        //This is a toggle for duplicates to help test our server
        final Button duptB = (Button) findViewById(R.id.b_dupt);
        final Button clearmB = (Button) findViewById(R.id.b_clearm);
        duptB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                processor.dupToggle();
                if (processor.allowDupes()) {
                    duptB.setBackgroundResource(R.color.enable);
                    Toast.makeText(getApplicationContext(), "Duplicates are allowed.", Toast.LENGTH_SHORT).show();
                } else {
                    duptB.setBackgroundResource(R.color.disable);
                    Toast.makeText(getApplicationContext(), "Duplicates are not allowed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        clearmB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                tvs[0].setText("");
                tvs[1].setText("");
                tvs[2].setText("");
                tvs[3].setText("");
                tvs[4].setText("");
                offset[0] = smsCount % 5;
            }
        });

        //Server Tab
        final EditText numCands = (EditText) findViewById(R.id.t_cands);;
        final Button startB = (Button) findViewById(R.id.b_start);
        final Button stopB = (Button) findViewById(R.id.b_stop);
        final Button clearB = (Button) findViewById(R.id.b_clear);
        final Button addB = (Button) findViewById(R.id.b_addc);
        final Button removeB = (Button) findViewById(R.id.b_remc);
        final Button sortByID = (Button) findViewById(R.id.b_sortid);
        final Button sortByV = (Button) findViewById(R.id.b_sortv);
        numCands.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                startB.setEnabled(!numCands.getText().toString().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //Start the server if the Start Server button has been clicked
        startB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                processor.runServer();
                startB.setEnabled(false);
                stopB.setEnabled(true);
                clearB.setEnabled(false);
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Voting Server has been started.", Toast.LENGTH_SHORT).show();
            }
        });
        //Stop the server if the Stop Server has been clicked
        stopB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                processor.stopServer();
                adapter.notifyDataSetChanged();
                startB.setEnabled(true);
                stopB.setEnabled(false);
                clearB.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Voting Server has been stopped.", Toast.LENGTH_SHORT).show();
            }
        });
        //Clear the votes if the Clear Votes has been clicked
        clearB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listNums.clear();
                processor.clearVotes();
                clearB.setEnabled(false);
                listNums.clear();
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Votes have been cleared.", Toast.LENGTH_SHORT).show();
            }
        });
        addB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int candID = Integer.parseInt(numCands.getText().toString());
                if (processor.hasCandidate(candID) > -1) {
                    Toast.makeText(getApplicationContext(), "Candidate "+ candID + " already exists.", Toast.LENGTH_SHORT).show();
                } else {
                    processor.addCandidate(candID);
                    Toast.makeText(getApplicationContext(), "Candidate " + candID + " has been added.", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }
            }
        });
        removeB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int candIndex = processor.hasCandidate(Integer.parseInt(numCands.getText().toString()));
                if (candIndex > -1) {
                    processor.remCandidate(candIndex);
                    Toast.makeText(getApplicationContext(), "Candidate " + numCands.getText().toString() + " has been removed.", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "Candidate " + numCands.getText().toString() + " does not exist.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sortByID.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                processor.sortByID();
                Toast.makeText(getApplicationContext(), "Sorted by Candidate ID.", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
        });
        sortByV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                processor.sortByVote();
                Toast.makeText(getApplicationContext(), "Sorted by number of votes.", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
        });
	}

    private class CandidateListAdapter extends ArrayAdapter<Candidate> {
        public CandidateListAdapter() {
            super(MainActivity.this, R.layout.listview_item, processor.getCandidateList());
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);
            }
            Candidate currentCand = processor.getCandidateList().get(position);
            TextView candID = (TextView) view.findViewById(R.id.num_cand);
            candID.setText(Integer.toString(currentCand.getCandID()));
            TextView candVote = (TextView) view.findViewById(R.id.num_vote);
            candVote.setText(Integer.toString(currentCand.getNumVotes()));
            return view;
        }
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
