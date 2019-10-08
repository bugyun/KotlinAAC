package vip.ruoyun.googleaac.core

import androidx.annotation.IntDef

@IntDef(
    flag = true,
    value = [LoadMode.NO_DATA, LoadMode.ERROR, LoadMode.LOADING, LoadMode.SUCCESS, LoadMode.NO_NETWORK]
)
@Retention(AnnotationRetention.SOURCE)
annotation class LoadMode {
    companion object {
        const val NO_DATA = 0x10//没有数据
        const val ERROR = 0x15//错误
        const val LOADING = 0x20//加载中
        const val SUCCESS = 0x25//成功
        const val NO_NETWORK = 0x30//没有网络
    }
}

