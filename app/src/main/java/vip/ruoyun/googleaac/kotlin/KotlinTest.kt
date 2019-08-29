package vip.ruoyun.googleaac.kotlin

import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class KotlinTest {

    /**
     * 默认:SYNCHRONIZED
     * LazyThreadSafetyMode.NONE :如果你确定初始化将总是发生在与属性使用位于相同的线程.使用 none 不会有任何线程安全的保证以及相关的开销。
     * LazyThreadSafetyMode.PUBLICATION :初始化器函数可以在并发访问未初始化的[Lazy]实例值时调用多次，但是只有第一个返回的值将用作[Lazy]实例的值。
     * LazyThreadSafetyMode.SYNCHRONIZED : 锁用于确保只有一个线程可以初始化[Lazy]实例。
     */
    val lazyValue: String by lazy(LazyThreadSafetyMode.PUBLICATION) {
        "hello"
    }

    //如果是在主线程的话,直接使用 LazyThreadSafetyMode.NONE 速度更快
    val lazyNoneValue: String by lazy(LazyThreadSafetyMode.NONE) {
        "ni"
    }


    var name: String by Delegates.observable("2") { _, oldValue, newValue ->
        print("$oldValue -> $newValue")
    }

    //委托属性,自定义实现 需要重写 getValue  / setValue 方法
    var prop: String? by Delegate<String>()

    /** 源码分析
     * private val prop$delegate = MyDelegate()
     * var prop: Type
     * get() = prop$delegate.getValue(this, this::prop)
     * set(value: Type) = prop$delegate.setValue(this, this::prop, value)
     */

}

class Delegate<T> {

    private var value: T? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        println("  $thisRef, thank you for delegating '${property.name}' to me!")
        return value
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        this.value = value
        println("  $value has been assigned to '${property.name}' in $thisRef.")
    }
}

fun main() {
    val kotlinTest = KotlinTest()
    kotlinTest.name = "1."
    kotlinTest.name = "2."
    //2 -> 1.1. -> 2.
    kotlinTest.prop = "zhang"
    println(kotlinTest.prop)

    val user = User(
        mapOf(
            "name" to "nihao",
            "age" to 21
        )
    )
    println(user.toString())
}

class User(private val map: Map<String, Any?>) {
    val name: String by map
    val age: Int by map
    override fun toString(): String {
        return "User(map=$map)"
    }
}

//内联类,会在使用它的地方 用 string 值来确保唯一性
// 不存在 'Password' 类的真实实例对象
// 在运行时，'securePassword' 仅仅包含 'String'
inline class Password(val value: String) {

    //内联类 不能使用 init 代码块,会报错
    //    init {    }
    //内联类不能含有幕后字段,因此，内联类只能含有简单的计算属性（不能含有延迟初始化/委托属性）
    //java 中不能调用内联类作为形参的函数

}








