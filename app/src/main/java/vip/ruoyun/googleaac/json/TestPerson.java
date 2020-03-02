package vip.ruoyun.googleaac.json;


import org.json.JSONObject;

/**
 * Created by ruoyun on 2020/3/2.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction:
 */
public class TestPerson implements ITestJson {

    @JsonName("name")
    private String name;
    @JsonName("age")
    private int age;


    public TestPerson fromJson(String json) throws Exception {
        JSONObject jsonObject = new JSONObject(json);
        name = jsonObject.optString("name");
        age = jsonObject.optInt("age");
        return this;
    }

    public String toJson() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("age", age);
        return jsonObject.toString();
    }
}
