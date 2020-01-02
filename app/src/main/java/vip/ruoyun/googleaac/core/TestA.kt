package vip.ruoyun.googleaac.core

import android.content.Context
import android.util.Log
import androidx.lifecycle.Observer
import androidx.work.*
import kotlinx.coroutines.coroutineScope
import java.util.concurrent.TimeUnit

/**
 * 使用 CoroutineWorker 可以在 kotlin 中创建 协程 使用。
 * TestListenableWorkerBuilder
 */
class TestA(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {


    override suspend fun doWork(): Result = coroutineScope {
        Log.d("TestA", Thread.currentThread().name)//DefaultDispatcher-worker-3,子线程

        val data = Data.Builder().putString("1", "2").build()
        Result.success(data)
//        Result.failure()

    }
}


fun test(context: Context) {

    //PeriodicWorkRequest 周期性的
    //OneTimeWorkRequestBuilder 执行一次的
    val request = OneTimeWorkRequestBuilder<TestA>().setInitialDelay(10, TimeUnit.MINUTES).build()
    val enqueue = WorkManager.getInstance(context).enqueue(request)

//    var observer?: Observer<Operation.State> =

    enqueue.state.observeForever {


    }

    WorkManager.getInstance(context).beginWith(listOf()).then(request).then(request).enqueue()


}

