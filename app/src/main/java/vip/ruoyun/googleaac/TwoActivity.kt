package vip.ruoyun.googleaac

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

class TwoActivity : AppCompatActivity() {
    private var observer = Observer<NetType> { netType ->

        when (netType) {
            NetType.NET_UNKNOW -> {
                Toast.makeText(this@TwoActivity, "未知网络", Toast.LENGTH_LONG).show()
                Log.e("uuu", "---TwoActivity---未知网络")
            }
            NetType.NET_4G -> {
                Toast.makeText(this@TwoActivity, "4G", Toast.LENGTH_LONG).show()
                Log.e("uuu", "---TwoActivity---4G")
            }
            NetType.NET_3G -> {
                Toast.makeText(this@TwoActivity, "3G", Toast.LENGTH_LONG).show()
                Log.e("uuu", "---TwoActivity---3G")
            }
            NetType.NET_2G -> {
                //有网络
                Toast.makeText(this@TwoActivity, "2G", Toast.LENGTH_LONG).show()
                Log.e("uuu", "---TwoActivity---2G")
            }
            NetType.WIFI -> {
                Toast.makeText(this@TwoActivity, "WIFI", Toast.LENGTH_LONG).show()
                Log.e("uuu", "---TwoActivity---WIFI")
            }
            NetType.NOME -> {
                Toast.makeText(this@TwoActivity, "没网络", Toast.LENGTH_LONG).show()
                Log.e("uuu", "---TwoActivity---没网络")
            }
            else -> {
            }
        }//没有网络，提示用户跳转到设置
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two)
//        NetworkLiveData.get(this).value
        Log.e("uuu", "---TwoActivity---")
        NetworkLiveData.get(this).observe(this, observer)
    }
}
