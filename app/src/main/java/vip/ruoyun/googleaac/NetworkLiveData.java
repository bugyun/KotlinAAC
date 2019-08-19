package vip.ruoyun.googleaac;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.lifecycle.LiveData;

/**
 * Created by ruoyun on 2019-08-07.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction:
 */
public class NetworkLiveData extends LiveData<NetType> {

    private volatile static NetworkLiveData mNetworkLiveData;
    private final Context mContext;
    private final IntentFilter mIntentFilter;
    private final NetworkReceiver mNetworkReceiver;

    private static final String TAG = "NetworkLiveData";

    private String status;

    private NetworkLiveData(Context context) {
        mContext = context.getApplicationContext();
        mNetworkReceiver = new NetworkReceiver();
        mIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    public static NetworkLiveData get(Context context) {
        if (mNetworkLiveData == null) {
            synchronized (NetworkLiveData.class) {
                if (mNetworkLiveData == null) {
                    mNetworkLiveData = new NetworkLiveData(context);
                }
            }
        }
        return mNetworkLiveData;
    }

    //如果 NetworkLiveData 在 LifecycleOwner 不存在的话，就触发下面的方法
    //当活动观察者的数量从0变为1时调用。
    @Override
    protected void onActive() {
        mContext.registerReceiver(mNetworkReceiver, mIntentFilter);
    }

    //如果 NetworkLiveData 在 没有引用的情况下，就触发下面的方法
    //当活动观察者的数量从1变为0时调用。
    @Override
    protected void onInactive() {
        mContext.unregisterReceiver(mNetworkReceiver);
    }



    private static class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e(TAG, Thread.currentThread().getName());//主线程
            Log.e(TAG, intent.getAction());// android.net.conn.CONNECTIVITY_CHANGE
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
//                Log.d("mark", "网络状态已经改变");
//                connectivityManager = (ConnectivityManager)
//
//                        getSystemService(Context.CONNECTIVITY_SERVICE);
//                info = connectivityManager.getActiveNetworkInfo();
//                if(info != null && info.isAvailable()) {
//                    String name = info.getTypeName();
//                    Log.d("mark", "当前网络名称：" + name);
//                } else {
//                    Log.d("mark", "没有可用网络");
//                }
                boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false); //网络是否全部断开
                NetworkInfo info1 = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);//当前的
                NetworkInfo info2 = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO); //另一个网络状态
                String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);//网络状态改变的原因
                boolean failOver = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false); //wifi和gprs是否在切换
                Log.d("MY_TAG", "onReceive(): mNetworkInfo=" + info1 + " mOtherNetworkInfo = " +
                        ("info2" + info2 + " noConn=" + noConnectivity) + " reason" + reason + " failOver" + failOver);
                NetworkLiveData.get(context).setValue(NetUtil.getNetType(context));
                //onReceive(): mNetworkInfo=[type: WIFI[], state: CONNECTED/CONNECTED, reason: (unspecified), extra: "Renmai-staff", failover: false, available: true, roaming: false, metered: false] mOtherNetworkInfo = [none]

                //onReceive(): mNetworkInfo=[type: WIFI[], state: CONNECTED/CONNECTED, reason: (unspecified), extra: "Renmai-staff", failover: false, available: true, roaming: false, metered: false] mOtherNetworkInfo = info2null noConn=false reasonnull failOverfalse

            }


        }
    }
}
