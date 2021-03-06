package vip.ruoyun.googleaac.core

import android.app.Application
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(application: Application, savedStateHandle: SavedStateHandle) :
    AndroidViewModel(application), Observable {
    private val callbacks: PropertyChangeRegistry = PropertyChangeRegistry()

    val userId: String? = savedStateHandle["userId"]

    var name: MutableLiveData<String> = MutableLiveData()

    var loadMode: MutableLiveData<Int> = MutableLiveData()
    var userLiveData: MutableLiveData<User> = MutableLiveData()
    var failure: MutableLiveData<String> = MutableLiveData()

    val name1: LiveData<String> = liveData {
        emit("nihao")
    }

    val age: LiveData<Int> = liveData {
        emit(1)
        
    }

    private fun handleFailure(errorMessage: String) {
        this.failure.value = errorMessage
    }

    private fun handleLoadState(@LoadMode mode: Int) {
        this.loadMode.value = mode
    }

    init {
        //初始化界面的数据
        viewModelScope.launch {
            Log.d("viewModelScope", Thread.currentThread().name)//main
            name.value = "开始" //主线程
            name.value = loadData()
            val user = HttpService.getData()
        }
    }

    fun getUser() = viewModelScope.launch {
        handleLoadState(LoadMode.LOADING)
        UserNetWorkApi().getUser<User>(
            success = {
                userLiveData.value = this
                handleLoadState(LoadMode.SUCCESS)
            }, failure = {
                handleLoadState(LoadMode.NO_NETWORK)
                handleFailure(this)
            })
    }

    fun saveUser(user: User) = viewModelScope.launch {

    }

    @ExperimentalCoroutinesApi
    fun getUser2() = viewModelScope.launch {
        UserNetWorkApi().getUser2("")
            .onCompletion {

            }
            .flowOn(Main)
            .catch {

            }.collect {
                userLiveData.value = it
            }
        //让普通的协程变成 flow
//        flowOf(UserNetWorkApi().getUser("")).onCompletion {
//
//        }.flowOn(Main)
//            .catch {
//
//            }.collect {
//
//            }
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
//        viewModelScope.cancel()
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.remove(callback)
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.add(callback)
    }
}

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) =
    liveData.observe(this, Observer(body))

fun <L : MutableLiveData<String>> LifecycleOwner.failure(liveData: L, body: (String?) -> Unit) =
    liveData.observe(this, Observer(body))

fun <L : MutableLiveData<Int>> LifecycleOwner.loadMode(liveData: L, body: (Int?) -> Unit) =
    liveData.observe(this, Observer(body))


@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.viewModel(
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null, body: VM.() -> Unit
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: {
        defaultViewModelProviderFactory
    }
    return ViewModelLazy(VM::class, { viewModelStore }, factoryPromise).apply { value.body() }
}