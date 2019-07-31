package vip.ruoyun.googleaac

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Created by ruoyun on 2019-07-31.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction:
 */

class TestLaunch {


    fun test() {

        GlobalScope.launch {

        }

        //启动了一个协程并等待直到它结束
        runBlocking {


        }
    }

    fun main() {
        println("Start")
        // 启动一个协程
        GlobalScope.launch {
            delay(1000)
            println("Hello")
        }

        Thread.sleep(2000) // 等待 2 秒钟
        println("Stop")
    }

}