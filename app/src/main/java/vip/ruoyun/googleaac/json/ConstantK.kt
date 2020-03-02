package vip.ruoyun.googleaac.json

/**
 * Created by ruoyun on 2020/3/2.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction:
 */

class ConstantK {
    companion object {
        const val name = "12312"
        @JvmStatic
        val name1 = "777"
    }


    fun test() {
        print(ConstantK.name)

        print(ConstantK.name1)

        print(ConstantK.Companion.name)

        print(ConstantK.Companion.name1)
    }
}