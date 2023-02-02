package com.me.way;

import com.alibaba.fastjson.JSONObject;
import com.me.entity.Weather;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GetWeather {
    public static Weather getTodayWeather(String code) throws IOException {
        URL url = new URL("http://t.weather.itboy.net/api/weather/city/"+code);
        InputStreamReader isReader =  new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);//“UTF- 8”万国码，可以显示中文，这是为了防止乱码
        BufferedReader br = new BufferedReader(isReader);//采用缓冲式读入
        String str;
        Weather weather=new Weather();
        while((str = br.readLine()) != null){
            JSONObject jsonObject= JSONObject.parseObject(str).getJSONObject("data");
            List weathers= (List) jsonObject.get("forecast");
            jsonObject= (JSONObject) weathers.get(0);
            String ymd=jsonObject.getString("ymd");
            String regex="\\p{Punct}+";
            String[] digit =str.split(regex);
            weather.setCity(digit[22]+digit[18]);
            weather.setTime(ymd.substring(0,4)+"年"+ymd.substring(5,7)+"月"+ymd.substring(8,10)+"日"+jsonObject.getString("week"));
            weather.setTemperature(jsonObject.getString("high")+"~"+jsonObject.getString("low"));
            weather.setWeather(jsonObject.getString("type")+" "+jsonObject.getString("fx")+jsonObject.getString("fl"));
            weather.setRemind(jsonObject.getString("notice"));
            weather.setSky(jsonObject.getString("type"));
            weather.setCityCode(code);
        }
//        System.out.println(weather);
        br.close();//网上资源使用结束后，数据流及时关闭
        isReader.close();
        return weather;
    }
    public static Weather getPreDayWeather(String code) throws IOException {
        URL url = new URL("http://t.weather.itboy.net/api/weather/city/"+code);
        InputStreamReader isReader =  new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);//“UTF- 8”万国码，可以显示中文，这是为了防止乱码
        BufferedReader br = new BufferedReader(isReader);//采用缓冲式读入
        String str;
        Weather weather=new Weather();
        while((str = br.readLine()) != null){
            JSONObject jsonObject= JSONObject.parseObject(str).getJSONObject("data").getJSONObject("yesterday");
            String ymd=jsonObject.getString("ymd");
            String regex="\\p{Punct}+";
            String[] digit =str.split(regex);
            weather.setCity(digit[22]+digit[18]);
            weather.setTime(ymd.substring(0,4)+"年"+ymd.substring(5,7)+"月"+ymd.substring(8,10)+"日"+jsonObject.getString("week"));
            weather.setTemperature(jsonObject.getString("high")+"~"+jsonObject.getString("low"));
            weather.setWeather(jsonObject.getString("type")+" "+jsonObject.getString("fx")+jsonObject.getString("fl"));
            weather.setRemind(jsonObject.getString("notice"));
            weather.setSky(jsonObject.getString("type"));
            weather.setCityCode(code);
        }
        br.close();//网上资源使用结束后，数据流及时关闭
        isReader.close();
        return weather;
    }
    public static Weather getNextWeather(String code,Integer apart) throws IOException {
        URL url = new URL("http://t.weather.itboy.net/api/weather/city/"+code);
        InputStreamReader isReader =  new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);//“UTF- 8”万国码，可以显示中文，这是为了防止乱码
        BufferedReader br = new BufferedReader(isReader);//采用缓冲式读入
        String str;
        Weather weather=new Weather();
        while((str = br.readLine()) != null){
            JSONObject jsonObject= JSONObject.parseObject(str).getJSONObject("data");
            List weathers= (List) jsonObject.get("forecast");
            jsonObject= (JSONObject) weathers.get(apart);
            String ymd=jsonObject.getString("ymd");
            String regex="\\p{Punct}+";
            String[] digit =str.split(regex);
            weather.setCity(digit[22]+digit[18]);
            weather.setTime(ymd.substring(0,4)+"年"+ymd.substring(5,7)+"月"+ymd.substring(8,10)+"日"+jsonObject.getString("week"));
            weather.setTemperature(jsonObject.getString("high")+"~"+jsonObject.getString("low"));
            weather.setWeather(jsonObject.getString("type")+" "+jsonObject.getString("fx")+jsonObject.getString("fl"));
            weather.setRemind(jsonObject.getString("notice"));
            weather.setSky(jsonObject.getString("type"));
            weather.setCityCode(code);
        }
        br.close();//网上资源使用结束后，数据流及时关闭
        isReader.close();
        return weather;
    }
    public static String getUser(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String userId = "";
        for (Cookie c:cookies){
            if (c.getName().equals("userId")){
                userId=c.getValue();
            }
        }
        return userId;
    }
    public static String getState(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String state = "";
        for (Cookie c:cookies){
            if (c.getName().equals("state")){
                state=c.getValue();
            }
        }
        return state;
    }
    public static void main(String[] args) throws IOException {
        getPreDayWeather("101291103");
    }
}
