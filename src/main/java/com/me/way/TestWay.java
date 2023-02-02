package com.me.way;

import com.alibaba.fastjson.JSONObject;
import com.me.entity.Weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TestWay {
    public static Weather getTodayWeather(String code,int apart) throws IOException {
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
        }
        System.out.println(weather);
        br.close();//网上资源使用结束后，数据流及时关闭
        isReader.close();
        return weather;
    }

    public static void main(String[] args) throws IOException {
        getTodayWeather("101291103",0);
    }
}
