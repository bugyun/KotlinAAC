package vip.ruoyun.googleaac.json;

/**
 * Created by ruoyun on 2020/3/2.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction:
 */
@SuppressWarnings("all")
public class MOSN {

    //    public static <T> T fromJson(JSONObject json, Class<T> clazz) throws JsonLubeParseException

    public static <T extends ITestJson> T fromJson(String json, Class<T> clazz) throws Exception {
        ITestJson iTestJson = clazz.newInstance();
        return (T) iTestJson.fromJson(json);
    }


    public static String toJson(ITestJson iTestJson) throws Exception {
        return iTestJson.toJson();
    }

    public static void main(String[] args) {
        try {
            TestPerson testPerson = MOSN.fromJson("", TestPerson.class);

            String json = MOSN.toJson(testPerson);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
