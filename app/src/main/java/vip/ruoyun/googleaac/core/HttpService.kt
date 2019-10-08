package vip.ruoyun.googleaac.core

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import java.util.*

interface HttpService {

    @GET("")
    suspend fun getHttpNum(): User

    companion object {

        @JvmField
        val instance = Retrofit.Builder()
            .baseUrl("http://gank.io/api/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(getOkHttp())
            .build()

        private fun getOkHttp(): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()

        suspend fun getData(): Response<User> = withContext(IO) {
            val response = try {
                val user = instance.create(HttpService::class.java).getHttpNum()
                Response<User>("200", "正常", user)
            } catch (e: Exception) {
                Response<User>("100", "失败")
            }
            response
        }
    }

}

data class User(var name: String = String.empty(), var id: Int = 0)
data class Response<out T>(val code: String, val message: String, val t: T? = null)

fun String.Companion.empty() = ""