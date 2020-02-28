package vip.ruoyun.googleaac.message;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import vip.ruoyun.googleaac.R;

public class MessageActivity extends AppCompatActivity {

    private static final String APP_SPECIFIC_SMS_RECEIVED_ACTION =
            "com.android.cts.permission.sms.APP_SPECIFIC_SMS_RECEIVED";
    private SMSBroadcastReceiver smsBroadcastReceiver;
    private String appSpecificSmsToken;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        mTextView = findViewById(R.id.mTextView);
        //注册广播接收者
        smsBroadcastReceiver = new SMSBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(APP_SPECIFIC_SMS_RECEIVED_ACTION);
        registerReceiver(smsBroadcastReceiver, filter);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            SmsManager smsManager = SmsManager.getDefault();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(APP_SPECIFIC_SMS_RECEIVED_ACTION), PendingIntent.FLAG_ONE_SHOT);
            appSpecificSmsToken = smsManager.createAppSpecificSmsToken(pendingIntent);
        }

//        AsyncChannel asyncChannel = new asyncchannel


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsBroadcastReceiver);
    }

    private class SMSBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (APP_SPECIFIC_SMS_RECEIVED_ACTION.equals(intent.getAction())) {
                for (SmsMessage pdu : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    String displayMessageBody = pdu.getDisplayMessageBody();
                    if (displayMessageBody.contains(appSpecificSmsToken)) {
                        //读取到短信密码
                        mTextView.setText(displayMessageBody);
                    }
                }
            }
        }
    }
}
