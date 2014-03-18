package com.example.votingserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSBroadcastReceiver extends BroadcastReceiver {

	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private static final String TAG = "SMSBroadcastReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "Message recieved:");
		Log.i(TAG, "Intent recieved: " + intent.getAction());
		Bundle bundle = intent.getExtras();
		if (bundle == null) {
			return;
		}

		Object[] pdus = (Object[]) bundle.get("pdus");

		for (int i = 0; i < pdus.length; i++) {
			SmsMessage SMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
			String sender = SMessage.getOriginatingAddress();
			String body = SMessage.getMessageBody().toString();

			Log.i(TAG, "Message recieved:" + SMessage.getMessageBody());
			Intent in = new Intent("SmsMessage.intent.MAIN").putExtra("get_msg", sender + ":" + body);
			context.sendBroadcast(in);
		}
	}

}
