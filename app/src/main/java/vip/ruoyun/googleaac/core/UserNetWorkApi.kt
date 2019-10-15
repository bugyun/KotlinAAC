package vip.ruoyun.googleaac.core

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import vip.ruoyun.googleaac.kotlin.main

class UserNetWorkApi {


//    abstract class UseCase<out Type, in Params> where Type : Any {
//
//        abstract suspend fun run(params: Params): Either<Failure, Type>
//
//        operator fun invoke(params: Params, onResult: (Either<Failure, Type>) -> Unit = {}) {
//            val job = async(CommonPool) { run(params) }
//            launch(UI) { onResult(job.await()) }
//        }
//
//        class None
//    }


    suspend inline fun <reified T> http(
        builder: Request.Builder,
        crossinline success: T.() -> Unit,
        crossinline failure: String.() -> Unit
    ) =
        withContext(IO) {
            try {
                val okHttpClient: OkHttpClient = OkHttpClient()
                val request = builder.build()
                val response = okHttpClient.newCall(request).execute()//网络异常
                val gson = Gson()
                val user = gson.fromJson(response.body?.charStream(), T::class.java)
                withContext(Main.immediate) {
                    user.success()
                }
            } catch (e: Exception) {
                withContext(Main.immediate) {
                    e.message.toString().failure()
                }
            }
        }
    //body: VM.() -> Unit

    suspend inline fun <reified T> getUser(
        crossinline success: T.() -> Unit,
        crossinline failure: String.() -> Unit
    ) {
        http(Request.Builder(), success, failure)
    }

    @ExperimentalCoroutinesApi
    suspend fun getUser2(name: String): Flow<User> = flow {
        val okHttpClient: OkHttpClient = OkHttpClient()
        val request = Request.Builder().url("").build()
        val response = okHttpClient.newCall(request).execute()
        val gson = Gson()
        emit(gson.fromJson(response.body?.charStream(), User::class.java))
    }.flowOn(IO)

}