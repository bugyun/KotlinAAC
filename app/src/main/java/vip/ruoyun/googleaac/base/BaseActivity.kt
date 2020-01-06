package vip.ruoyun.googleaac.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    protected val TAG by lazy(LazyThreadSafetyMode.NONE) { this::class.java.simpleName }

    protected lateinit var binding: T

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<T>(this, initLayout()).apply {
            //将布局视图绑定到架构组件
            lifecycleOwner = this@BaseActivity
        }
        initStatusBar()
        initSavedInstanceState(savedInstanceState)
        initView()
        initAdapter()
        initListener()
        fetchData()
    }

    @LayoutRes
    abstract fun initLayout(): Int

    open fun initStatusBar() {}

    open fun initSavedInstanceState(savedInstanceState: Bundle?) {}

    open fun initView() {}

    open fun initAdapter() {}

    open fun initListener() {}

    open fun fetchData() {}


}