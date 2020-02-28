package vip.ruoyun.googleaac

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.InputStreamReader

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


    @Test
    fun test() {
//        var json = JSONObject()


        print("nklk")
    }


    /*
    [
   {
     "id": 912345678901,
     "text": "How do I read JSON on Android?",
     "geo": null,
     "user": {
       "name": "android_newb",
       "followers_count": 41
      }
   },
   {
     "id": 912345678902,
     "text": "@android_newb just use android.util.JsonReader!",
     "geo": [50.454722, -104.606667],
     "user": {
       "name": "jesse",
       "followers_count": 2
     }
   }
 ]
     */


    data class ModelUser(var name: String? = "", var followers_count: Int? = 0)

    data class Model(
        var id: Long? = 0,
        var text: String? = "",
        var geo: List<Double>? = null,
        var user: ModelUser? = null
    )

    data class ModelList(var models: List<Model>)

    @Test
    fun testJsonReader() {

        val json = """
            [
  {
    "id": 912345678901,
    "text": "How do I read JSON on Android?",
    "geo": null,
    "user": {
      "name": "android_newb",
      "followers_count": 41
    }
  },
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
]
        """
        val inputStream = ByteArrayInputStream(json.toByteArray())
        val modelList = arrayListOf<Model>()
        //通过流来完成 jsonReader 的创建
        val beginTime = System.nanoTime()
        val jsonReader = JsonReader(InputStreamReader(inputStream, "UTF-8"))
        jsonReader.beginArray()
        while (jsonReader.hasNext()) {
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
            modelList.add(model)
        }
        jsonReader.endArray()
        println("消耗：" + (System.nanoTime() - beginTime) / 1000 + "微秒")
        //打印输出
        print(modelList)
    }

    //消耗：6186167
    //消耗：21452901
    @Test
    fun testGson() {
        val gson = Gson()
        val json = """
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
        val inputStream = ByteArrayInputStream(json.toByteArray())
        //通过流来完成 jsonReader 的创建
        val beginTime = System.nanoTime()
        val jsonReader = JsonReader(InputStreamReader(inputStream, "UTF-8"))
        val models = gson.fromJson<Model>(jsonReader, Model::class.java)
        println("消耗：" + (System.nanoTime() - beginTime) / 1000 + "微秒")

        print(models)
    }


    @Test
    fun testJsonObject() {
        val json = """
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
        val beginTime = System.nanoTime()
        val model = Model()
        val jsonObject = JSONObject(json)
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
        println("消耗：" + (System.nanoTime() - beginTime) / 1000 + "微秒")
        println(model)
    }
}
