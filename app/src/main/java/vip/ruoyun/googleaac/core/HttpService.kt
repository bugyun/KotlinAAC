package vip.ruoyun.googleaac.core

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.http.GET
import java.lang.Exception

interface HttpService {

    @GET("")
    suspend fun getHttpNum(): User

    companion object {
        @JvmField
        val instance = Retrofit.Builder()
            .baseUrl("http://gank.io/api/")
            .build().create(HttpService::class.java)

        suspend fun getData(): Response<User> = withContext(IO) {
            val response = try {
                val user = HttpService.instance.getHttpNum()
                Response<User>("200", "正常", user)
            } catch (e: Exception) {
                Response<User>("100", "失败")
            }
            response
        }
    }

}

data class User(var name: String)
data class Response<out T>(val code: String, val message: String, val t: T? = null)
