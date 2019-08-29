package vip.ruoyun.googleaac.core

import android.os.Bundle
import android.util.JsonReader
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.ListenableWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vip.ruoyun.googleaac.R
import vip.ruoyun.googleaac.databinding.ActivityHomeBinding

class HomeActivity<T : ActivityHomeBinding> : AppCompatActivity() {


    private val mainScope = MainScope()//默认 是 ui 线程的调度器
    //自己创建一个协程域
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    protected val TAG by lazy { HomeActivity::class.java.simpleName }

    private val homeViewModel: HomeViewModel by lazy {
        //                ViewModelProviders.of(this)[HomeViewModel::class.java]
        ViewModelProvider(
            this, SavedStateViewModelFactory(application, this)
//            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[HomeViewModel::class.java]

//        ViewModelProvider(this)[HomeViewModel::class.java]
    }

    private val model: HomeViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HomeViewModel() as T
            }
        }
    }

    private val binding: T by lazy {
        val binDing =
            DataBindingUtil.setContentView<T>(this, R.layout.activity_home)
        binDing.lifecycleOwner = this
        binDing
    }

    private val str: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.bean = homeViewModel

//        binding.mTextView.text = ""
//        test(this)

        repeat(10) {
            mainScope.launch {
                withContext(IO) {

                    withContext(Main) {

                    }
                }
            }
        }


        str.isNullOrEmpty()//相当于 TextUtils.isEmpty()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
        mainScope.cancel()
        coroutineScope.cancel()
    }

    suspend fun doWork(): ListenableWorker.Result = coroutineScope {
        try {
            applicationContext.assets.open("PLANT_DATA_FILENAME").use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    ListenableWorker.Result.success()
                }
            }
        } catch (ex: Exception) {
            ListenableWorker.Result.failure()
        }
    }
}