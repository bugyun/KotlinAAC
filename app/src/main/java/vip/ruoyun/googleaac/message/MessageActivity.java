package vip.ruoyun.googleaac.message;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import vip.ruoyun.googleaac.R;

public class MessageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            SmsManager smsManager = SmsManager.getDefault();
            String appSpecificSmsToken = smsManager.createAppSpecificSmsToken(createSmsTokenPendingIntent());
        }

        for (SmsMessage pdu : Telephony.Sms.Intents.getMessagesFromIntent(getIntent())) {
//            tv.append(pdu.getDisplayMessageBody());
        }

    }

    private PendingIntent createSmsTokenPendingIntent() {
        Intent intent = new Intent(this, SMSBroadcastReceiver.class);
        intent.setAction("com.lcw.alarm");
        return PendingIntent.getBroadcast(this, 231, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    static class SMSBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), "com.lcw.alarm")) {
                //修改 ui
            }
        }
    }
}
