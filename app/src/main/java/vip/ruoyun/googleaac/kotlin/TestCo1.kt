package vip.ruoyun.googleaac.kotlin

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TestCo1 {


    fun main() {


        GlobalScope.launch {
            flow {
                emit(1) // Ok
                emit(2) // Will fail with ISE
            }.flowOn(IO).map {

            }.catch {

            }.collect {

            }
        }
    }
}