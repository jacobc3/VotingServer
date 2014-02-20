package ui;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class smsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Log.i("cs.fsu", "smsReceiver: SMS Received");

		Bundle bundle = intent.getExtras();

		if (bundle != null) {
			Log.i("cs.fsu", "smsReceiver : Reading Bundle");

			Object[] pdus = (Object[]) bundle.get("pdus");
			SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdus[0]);
			Log.i("cs.fsu", "MESSAGE IS");
			Log.i("cs.fsu",sms.getMessageClass().toString());
			Log.i("cs.fsu", "BODY IS");
			Log.i("cs.fsu",sms.getMessageBody());
			String string = getString(R.string.filter_name);
			if (sms.getMessageBody().contains("FLAG")) {

			}
		}
		
		
	}
}
