package vip.ruoyun.googleaac.kotlin

import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

class TestCo1 {


    fun main() {


//        GlobalScope.launch {
//            flow {
//                emit(1) // Ok
//                emit(2) // Will fail with ISE
//            }.flowOn(IO).map {
//
//            }.catch {
//
//            }.collect {
//
//            }
//        }


    }


    fun test01() {
        //在后台开启一个协程,新协程的生命周期只受整个应用程序的生命周期限制。
        GlobalScope.launch {
            delay(1000L)
            print("2")
        }
        print("1")
        Thread.sleep(2000L)
    }

    /**
     * delay 非阻塞
     * Thread.sleep 阻塞
     */
    fun test02() {
        thread {
            Thread.sleep(1000L)
            print("2")
        }
        print("1")
        Thread.sleep(2000L)
    }

    /**
     * GlobalScope.launch 不会阻塞线程
     * runBlocking 会阻塞线程,里面的执行完成之后,才会执行下面的流程
     */
    fun test03() {
        runBlocking {
            delay(2000L)
            print("2")
        }
        print("1")
    }

    /**
     * 开始执行主协程
     * 在后台启动一个新的协程并继续
     */
    fun test04() = runBlocking<Unit> {
        // 开始执行主协程
        GlobalScope.launch {
            // 在后台启动一个新的协程并继续
            delay(1000L)
            println("World!")
        }
        println("Hello,") // 主协程在这里会立即执行
        delay(2000L)      // 延迟 2 秒来保证 JVM 存活
        println("!!") // 主协程在这里会立即执行
    }


    fun test05() = runBlocking {
        val job = GlobalScope.launch {
            delay(1000L)
            print("1")
        }
        print("2")
        job.join()// 等待直到子协程执行结束
    }


    fun test06() = runBlocking {
        repeat(100000) {
            launch {
                delay(1000L)
                println(".")
            }
        }
    }


    /**
     * 使用协程进行并发总是显式的 async { }
     */
    fun test07() = runBlocking<Unit> {
        val time = measureTimeMillis {
            val one = async { doSomethingUsefulOne() }
            val two = async { doSomethingUsefulTwo() }
            println("The answer is ${one.await() + two.await()}")
        }
        println("Completed in $time ms")
    }

    suspend fun doSomethingUsefulOne(): Int {
        delay(1000L) // 假设我们在这里做了些有用的事
        return 13
    }

    suspend fun doSomethingUsefulTwo(): Int {
        delay(1000L) // 假设我们在这里也做了些有用的事
        return 29
    }



    fun asyncDoSomethingUsefulTwo() = GlobalScope.async {
        doSomethingUsefulOne()
    }

}


fun main() {
//    TestCo1().test01()
//    TestCo1().test02()
//    TestCo1().test03()
//    TestCo1().test04()
//    TestCo1().test05()
//    TestCo1().test06()
    TestCo1().test07()

}