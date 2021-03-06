package vip.ruoyun.googleaac

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.annotation.IntDef
import androidx.annotation.MainThread
import androidx.annotation.NonNull
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import vip.ruoyun.googleaac.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), Observer<Resource<*>> {

    companion object {
        const val SUNDAY = 1
        const val MONDAY = 1 shl 2
        const val TUESDAY = 1 shl 3
        const val WEDNESDAY = 1 shl 4
        const val THURSDAY = 1 shl 5
        const val FRIDAY = 1 shl 6
        const val SATURDAY = 1 shl 7
    }

    @IntDef(flag = true, value = [SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY])
    @Retention(AnnotationRetention.SOURCE)
    annotation class WeekDays

    @WeekDays
    var currentDay = SUNDAY or MONDAY

    private fun useCurrentDay() {
        currentDay = SUNDAY or MONDAY
        when (currentDay) {
            SUNDAY or MONDAY -> {
                Log.e("zyh", "SUNDAY or MONDAY")
            }
            SUNDAY -> {
                Log.e("zyh", "SUNDAY")
            }
            MONDAY -> {
                Log.e("zyh", "MONDAY")
            }
        }
    }

    override fun onChanged(t: Resource<*>?) {

        if (t?.data is User) {

        }

        t?.data as User

    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        useCurrentDay()

//        startActivity(Intent(this, MainActivity::class.java).apply {
//            putExtra("XX", "")
//        })


//        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        //将当前活动指定为生命周期所有者,数据改变，UI自动会更新
        binding.lifecycleOwner = this
        //创建一个 ViewModel
        val viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        binding.viewModel = viewModel


//        viewModel.user.observe(this, Observer {
//
//        })


        lifecycle.addObserver(MyObserver())


//        ServiceLoader

//        ActivityManager
//        registerReceiver()

//        sendBroadcast()

//        BroadcastReceiver
//        NetworkLiveData.get(this).observe(this,
//            Observer<NetType> { netType ->
//                when (netType) {
//                    NetType.NET_UNKNOW -> {
//                        Toast.makeText(this@MainActivity, "未知网络", Toast.LENGTH_LONG).show()
//                        Log.e("uuu", "---MainActivity---未知网络")
//                    }
//                    NetType.NET_4G -> {
//                        Toast.makeText(this@MainActivity, "4G", Toast.LENGTH_LONG).show()
//                        Log.e("uuu", "---MainActivity---4G")
//                    }
//                    NetType.NET_3G -> {
//                        Toast.makeText(this@MainActivity, "3G", Toast.LENGTH_LONG).show()
//                        Log.e("uuu", "---MainActivity---3G")
//                    }
//                    NetType.NET_2G -> {
//                        //有网络
//                        Toast.makeText(this@MainActivity, "2G", Toast.LENGTH_LONG).show()
//                        Log.e("uuu", "---MainActivity---2G")
//                    }
//                    NetType.WIFI -> {
//                        Toast.makeText(this@MainActivity, "WIFI", Toast.LENGTH_LONG).show()
//                        Log.e("uuu", "---MainActivity---WIFI")
//                    }
//                    NetType.NOME -> {
//                        Toast.makeText(this@MainActivity, "没网络", Toast.LENGTH_LONG).show()
//                        Log.e("uuu", "---MainActivity---没网络")
//                    }
//                    else -> {
//                    }
//                }//没有网络，提示用户跳转到设置
//            })

        binding.mTextView.setOnClickListener {
            viewModel.user.value = User("李四", 22, 1)
//
            startActivity(Intent(this@MainActivity, TwoActivity::class.java).apply {
            })

//            startActivity(Intent(this, MainActivity::class.java).apply {
//                putExtra("XX", "")
//            })
        }


        val model = ViewModelProviders.of(this)[MyViewModel::class.java]
        model.getUsers().observe(this, Observer {


        })


        //请求网络
//        viewModel.loadData()

        val transformations = Transformations.map(viewModel.user) {
            "A:$it"
        }

        transformations.observe(this, changeObserver)

        binding.run {


        }


        val listAdapter = TestAdapter(lifecycleScope)

        listAdapter.submitList(arrayListOf())

        val dataSourceFactory = object : DataSource.Factory<String, String>() {
            override fun create(): DataSource<String, String> {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }


        val alls = LivePagedListBuilder(dataSourceFactory, 20).build()

        PagedList.Config.Builder().setPageSize(20).setEnablePlaceholders(false)
            .setInitialLoadSizeHint(20).build()


    }

    private val changeObserver = Observer<String> { value ->
        value?.let { binding.mTextView.text = it }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

class MyObserver : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun connectListener() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun disconnectListener() {
    }
}


data class User(val name: String, val age: Int, val sex: Int)

class UserViewModel : ViewModel() {
    val user = MutableLiveData<User>()

    fun loadData() {
        //请求网络
        user.postValue(null)
    }

}

class MyViewModel : ViewModel() {
    private val users: MutableLiveData<List<User>> by lazy {
        MutableLiveData<List<User>>().also {
            loadUsers()
        }
    }

    fun getUsers(): LiveData<List<User>> {
        return users
    }

    private fun loadUsers() {
        // Do an asynchronous operation to fetch users.
    }
}


class NetWorkViewModel : ViewModel() {

//    public fun loadUser(userId: String): LiveData<Resource<User>> {
//        return NetworkBoundResource<User, User>() {
//
//        }
//    }
}

class ApiResponse<RequestType> {

}

abstract class NetworkBoundResource<ResultType, RequestType> {
    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType);

    // Called with the data in the database to decide whether it should be
    // fetched from the network.
    @MainThread
    protected abstract fun shouldFetch(data: ResultType): Boolean

    // Called to get the cached data from the database
    @NonNull
    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    // Called to create the API call.
    @NonNull
    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    @MainThread
    protected fun onFetchFailed() {
    }

    // returns a LiveData that represents the resource, implemented
    // in the base class.
    protected abstract fun getAsLiveData(): LiveData<Resource<ResultType>>
}


class Resource<T>(var status: Int, var data: T, var message: String?) {
    companion object {
        // 包裹范围内 属于静态方法
        fun <T> success(data: T): Resource<T> {
            return Resource(1, data, null)
        }

        fun <T> error(data: T, message: String?): Resource<T> {
            return Resource(2, data, message)
        }

        fun <T> loading(data: T): Resource<T> {
            return Resource(3, data, null)
        }
    }
}
