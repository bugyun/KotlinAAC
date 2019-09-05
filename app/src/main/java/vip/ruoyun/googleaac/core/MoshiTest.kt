package vip.ruoyun.googleaac.core

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi

class MoshiTest {


    fun test() {


        val moshi = Moshi.Builder().build()



        val jsonAdapter = moshi.adapter(MoshiData::class.java)
        var bean = jsonAdapter.fromJson("")


    }

}

@JsonClass(generateAdapter = true)
data class MoshiData(
    var name: String,
    val age: Int
)