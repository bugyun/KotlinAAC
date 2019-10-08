package vip.ruoyun.googleaac.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.work.ListenableWorker

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) =
    liveData.observe(this, Observer(body))

fun <L : LiveData<ListenableWorker.Result.Failure>> LifecycleOwner.failure(
    liveData: L,
    body: (ListenableWorker.Result.Failure?) -> Unit
) =
    liveData.observe(this, Observer(body))