package vip.ruoyun.googleaac.core

import android.util.JsonReader
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.work.ListenableWorker
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.flow
import vip.ruoyun.googleaac.R
import vip.ruoyun.googleaac.base.BaseActivity
import vip.ruoyun.googleaac.databinding.ActivityHomeBinding
import vip.ruoyun.googleaac.testGson
import vip.ruoyun.googleaac.testJsonObject
import vip.ruoyun.googleaac.testJsonReader

class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    override fun initLayout(): Int = R.layout.activity_home

    private val mainScope = MainScope()//默认 是 ui 线程的调度器
    //自己创建一个协程域
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    //ViewModel 的获取方法
    //第一种方式
//    private val homeViewModel2: HomeViewModel by viewModel {
//        failure(failure, ::showToast)
//        loadMode(loadMode, ::changeLoadMode)
//        observe(userLiveData) {
//
//        }
//    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun changeLoadMode(@LoadMode mode: Int?) {

    }

    //默认参数
//    private val homeViewModel: HomeViewModel by viewModels(
//        factoryProducer = {
//            SavedStateViewModelFactory(
//                application
//                , this@HomeActivity
//                , intent.extras
//            )
//        }
//    )


    private val homeViewModel: HomeViewModel by viewModels()


//    //第二种方式
//    private val homeViewModel: HomeViewModel by viewModels {
//        object : ViewModelProvider.Factory {
//            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//                return HomeViewModel() as T
//            }
//        }
//    }

    //第二种方式.可以保存状态
//    private val homeViewModel: HomeViewModel by lazy(LazyThreadSafetyMode.NONE) {
//        //                ViewModelProviders.of(this)[HomeViewModel::class.java]
//        ViewModelProvider(
//            this, SavedStateViewModelFactory(application, this)
////            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
//        )[HomeViewModel::class.java]
//        ViewModelProvider(this)[HomeViewModel::class.java]
//    }


    private val str: String = ""

    override fun initView() {

        testGson()
        testJsonObject()
        testJsonReader()

        var map = LinkedHashMap<String, String>()
//        map.put()


        homeViewModel.userLiveData.observe(this, Observer<User> {
            binding.mTextView.text = it.name
        })

        homeViewModel.loadMode.observe(this, Observer {

        })

        homeViewModel.failure.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        failure(homeViewModel.failure) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        loadMode(homeViewModel.loadMode) {

        }

        homeViewModel.getUser()

        binding.bean = homeViewModel

        lifecycleScope.launch {
            flow {
                emit(1) // Ok
                withContext(IO) {
                    emit(2) // Will fail with ISE
                }
            }

        }


//        binding.mTextView.text = ""
//        test(this)

        repeat(10) {
            mainScope.launch {
                withContext(IO) {

                    withContext(Main.immediate) {

                    }
                }
            }
        }

        lifecycle.addObserver(MyObserver())


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

