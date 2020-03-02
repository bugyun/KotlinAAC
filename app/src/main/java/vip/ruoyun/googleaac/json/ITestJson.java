package vip.ruoyun.googleaac.json;

/**
 * Created by ruoyun on 2020/3/2.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction:
 */
public interface ITestJson {

    Object fromJson(String json) throws Exception;

    String toJson() throws Exception;
}
