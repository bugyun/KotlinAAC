package vip.ruoyun.googleaac

import android.util.JsonReader
import android.util.JsonToken
import android.util.Log
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.InputStreamReader

/**
 * Created by ruoyun on 2020/2/28.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction:
 */

data class ModelUser(var name: String? = "", var followers_count: Int? = 0)

data class Model(
    var id: Long? = 0,
    var text: String? = "",
    var geo: List<Double>? = null,
    var user: ModelUser? = null
)

const val json = """
            {
  
    "id": 912345678902,
    "text": "@android_newb just use android.util.JsonReader!",
    "geo": [
      50.454722,
      -104.606667
    ],
    "user": {
      "name": "jesse",
      "followers_count": 2
    }
  
}
        """

fun testJsonReader(): Model {
    val beginTime = System.nanoTime()
    val inputStream = ByteArrayInputStream(json.toByteArray())
    //通过流来完成 jsonReader 的创建
    val jsonReader = JsonReader(InputStreamReader(inputStream, "UTF-8"))
    jsonReader.beginObject()
    val model = Model()
    while (jsonReader.hasNext()) {
        when (jsonReader.nextName()) {
            "id" -> {
                model.id = jsonReader.nextLong()
            }
            "text" -> {
                model.text = jsonReader.nextString()
            }
            "geo" -> {
                if (jsonReader.peek() != JsonToken.NULL) {
                    val doubles = arrayListOf<Double>()
                    jsonReader.beginArray()
                    while (jsonReader.hasNext()) {
                        doubles.add(jsonReader.nextDouble())
                    }
                    jsonReader.endArray()
                    model.geo = doubles
                } else {
                    jsonReader.skipValue()
                }
            }
            "user" -> {
                if (jsonReader.peek() != JsonToken.NULL) {
                    val modelUser = ModelUser()
                    jsonReader.beginObject()
                    while (jsonReader.hasNext()) {
                        when (jsonReader.nextName()) {
                            "name" -> {
                                modelUser.name = jsonReader.nextString()
                            }
                            "followers_count" -> {
                                modelUser.followers_count = jsonReader.nextInt()
                            }
                            else -> {
                                jsonReader.skipValue()
                            }
                        }
                    }
                    jsonReader.endObject()
                    model.user = modelUser
                } else {
                    jsonReader.skipValue()
                }
            }
            else -> {
                jsonReader.skipValue()
            }
        }
    }
    jsonReader.endObject()
    Log.d("zyh", "testJsonReader消耗：" + (System.nanoTime() - beginTime) / 1000 + "微秒")
    //打印输出
    Log.d("zyh", model.toString())
    return model
}

//消耗：6186167
//消耗：21452901
fun testGson(): Model {
    val beginTime = System.nanoTime()
    val gson = Gson()
    //通过流来完成 jsonReader 的创建
    val models = gson.fromJson<Model>(json, Model::class.java)
    Log.d("zyh", "gson消耗：" + (System.nanoTime() - beginTime) / 1000 + "微秒")
    Log.d("zyh", models.toString())
    return models
}


fun testJsonObject(): Model {
    val beginTime = System.nanoTime()
    val jsonObject = JSONObject(json)
    val model = Model()
    model.id = jsonObject.optLong("id")
    model.text = jsonObject.optString("text")
    val array = jsonObject.optJSONArray("geo")
    val arraylist = arrayListOf<Double>()
    for (item in 0 until array.length()) {
        arraylist.add(array[item] as Double)
    }
    model.geo = arraylist
    val user = ModelUser()
    val userObject = jsonObject.optJSONObject("user")
    user.name = userObject.optString("name")
    user.followers_count = userObject.optInt("followers_count")
    model.user = user
    Log.d("zyh", "testJsonObject消耗：" + (System.nanoTime() - beginTime) / 1000 + "微秒")
    Log.d("zyh", model.toString())
    return model
}

fun testCreateJson(model: Model) {
    val beginTime = System.nanoTime()
    val jsonObject = JSONObject()
    jsonObject.put("id", model.id)
    jsonObject.put("text", model.text)

    val jsonArray = JSONArray()
    for (item in model.geo!!.indices) {
        jsonArray.put(model.geo!![item])
    }
    jsonObject.put("geo", jsonArray)

    val jsonUser = JSONObject()
    jsonUser.put("name", model.user?.name)
    jsonUser.put("followers_count", model.user?.followers_count)
    jsonObject.put("user", jsonUser)

    Log.d("zyh", "testJsonObject消耗：" + (System.nanoTime() - beginTime) / 1000 + "微秒")
    Log.d("zyh", jsonObject.toString())
    //37微秒
}

//3341微秒
fun testCreateGson(model: Model) {
    val beginTime = System.nanoTime()
    //通过流来完成 jsonReader 的创建
    val models = Gson().toJson(model)
    Log.d("zyh", "gson消耗：" + (System.nanoTime() - beginTime) / 1000 + "微秒")
    Log.d("zyh", models.toString())
}


fun <T> testJsontoBean(clz: Class<T>) {
    //实际的解析
    val client = OkHttpClient()

    val request = Request.Builder().url("").build()
    val execute = client.newCall(request).execute()
    val jsonObject = JSONObject(execute.body?.string())

    var jsonReader = JsonReader(InputStreamReader(execute.body?.byteStream(), "UTF-8"))
    jsonReader = JsonReader(execute.body?.charStream())


    //一般的解析框架
    Gson().fromJson<T>("", clz)

    //MSON.fromJson(json, clazz); // 反序列化
    //MSON.toJson(bean); // 序列化

    // json 工具
    // 可以辨析

}

fun <T> goTest(clz: Class<T>) {
    //这样使用，
    testJsontoBean(Model::class.java)

    val t = clz.newInstance()











}
