package vip.ruoyun.googleaac.core

import android.os.Bundle
import android.text.TextUtils
import android.util.JsonReader
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.work.ListenableWorker
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vip.ruoyun.googleaac.R
import vip.ruoyun.googleaac.databinding.ActivityHomeBinding

class HomeActivity<T : ActivityHomeBinding> : AppCompatActivity() {

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

        str.isNullOrEmpty()//相当于 TextUtils.isEmpty()


    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
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