package vip.ruoyun.googleaac.core

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    var name: MutableLiveData<String> = MutableLiveData()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()

    init {
        //初始化界面的数据
        viewModelScope.launch {
            Log.d("viewModelScope", Thread.currentThread().name)//main
            name.value = "开始" //主线程
            name.value = loadData()
            val user = HttpService.getData()
        }
    }

    private suspend fun loadData(): String = withContext(IO) {
        Log.d("viewModelScope", Thread.currentThread().name)//DefaultDispatcher-worker-1
        delay(4000L)
        withContext(Main.immediate) {
            Log.d("viewModelScope", Thread.currentThread().name)//main
        }
        return@withContext "第一"
    }

    /**
     * Cancel all coroutines when the ViewModel is cleared.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}

