package com.me.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.me.entity.City;
import com.me.entity.User;
import com.me.entity.Weather;
import com.me.service.WeatherService;
import com.me.way.GetGPS;
import com.me.way.GetWeather;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/weather")
public class WeatherController {
    @Autowired
    public WeatherService weatherService;
    @RequestMapping("/login")
    public String signIn(String username, String password, HttpServletResponse response){
        User user=weatherService.signIn(username,password);
        if (user!=null){
            Cookie cookie1=new Cookie("userId",String.valueOf(user.getId()));
            cookie1.setMaxAge(1800);
            Cookie cookie2=new Cookie("state","1");
            response.addCookie(cookie1);
            response.addCookie(cookie2);
            return "redirect:/weather/index";
        }else {
            return "redirect:/weather/signFalse";
        }
    }
    @RequestMapping("/signFalse")
    public ModelAndView signFalse(){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("sign","false");
        modelAndView.setViewName("login");
        return modelAndView;
    }
    @RequestMapping("/index")
    public ModelAndView selectAll(@RequestParam(defaultValue = "") String cityName, @RequestParam(defaultValue = "1") String pageNum, HttpServletRequest request,HttpServletResponse response) throws IOException {
        ModelAndView modelAndView=new ModelAndView();
        String userId= GetWeather.getUser(request);
        String state=GetWeather.getState(request);
        if (!userId.equals("")){
            User user=weatherService.selectByUserId(Integer.parseInt(userId));
            PageHelper.startPage(Integer.parseInt(pageNum),5);
            List<City> cityList=weatherService.selectAllCity(cityName);
            PageInfo<City> pageInfo=new PageInfo<>(cityList);
//            String ip= GetGPS.getIp(request);
            JSONObject jsonObject= GetGPS.getGps("117.93.60.40");
            String provinceCity=jsonObject.get("province")+" "+jsonObject.get("city");
            modelAndView.addObject("provinceCity",provinceCity);
            if (state.equals("1")){
                weatherService.insertAddress(provinceCity);
                Cookie cookie=new Cookie("state","0");
                response.addCookie(cookie);
            }
            modelAndView.addObject(pageInfo);
//            System.out.println(pageInfo);
            modelAndView.addObject("user",user);
            modelAndView.addObject("cityName",cityName);
            modelAndView.setViewName("index");
        }else {
            modelAndView.addObject("rs","false");
            modelAndView.setViewName("login");
        }

        return modelAndView;
    }
    @RequestMapping("/getSky")
    @ResponseBody
    public Weather getWeather(String cityCode,@RequestParam(defaultValue = "0") String apart) throws IOException {
        Weather weather;
        if (apart.equals("0")){
            weather= GetWeather.getTodayWeather(cityCode);
        }else if (apart.equals("-1")){
            weather=GetWeather.getPreDayWeather(cityCode);
        }else {
            int i= Integer.parseInt(apart);
            if (i>0&&i<=5){
                weather=GetWeather.getNextWeather(cityCode,i);
            }else {
                return null;
            }
        }
        return weather;
    }
    @RequestMapping("/goCore")
    public ModelAndView goCore(HttpServletRequest request){
        ModelAndView modelAndView=new ModelAndView();
        String userId=GetWeather.getUser(request);
        if (!userId.equals("")){
            User user=weatherService.selectByUserId(Integer.parseInt(userId));
            modelAndView.addObject("user",user);
        }else {
            modelAndView.addObject("rs","false");
            modelAndView.setViewName("login");
        }
        modelAndView.setViewName("ownerCore");
        return modelAndView;
    }
    @RequestMapping("gotoEnroll")
    public String gotoEnroll(){
        return "register";
    }
}
