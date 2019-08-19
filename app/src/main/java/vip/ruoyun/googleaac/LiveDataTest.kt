package vip.ruoyun.googleaac

import androidx.lifecycle.MutableLiveData


class LiveDataTest {


    fun test() {
//
//        ComputableLiveData//
//
//        MediatorLiveData//
//
//
//        MutableLiveData//


        //LiveData 抽象类
        //MutableLiveData 继承 LiveData;
        //只有 setvalue 和 postvalue 方法



        var mutableLiveData = MutableLiveData<String>()
        mutableLiveData.value = ""//在主线程中
        mutableLiveData.postValue("")//在子线程中


    }
}

