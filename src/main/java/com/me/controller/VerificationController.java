package com.me.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.me.entity.User;
import com.me.entity.VerificationCodePlace;
import com.me.service.WeatherService;
import com.me.way.MailBox;
import com.me.way.VerificationCodeAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/verif")
public class VerificationController {
    @Value("${afterImage.location}")
    private String imgLocation;

    @Autowired
    public WeatherService weatherService;

    @RequestMapping("/getImgInfo")
    @ResponseBody
    // 随机获取背景和拼图，返回json
    public String imgInfo(){
        VerificationCodePlace vcPlace = VerificationCodeAdapter.getRandomVerificationCodePlace(imgLocation);
        ObjectMapper om = new ObjectMapper();
        String jsonResult = "";
        try {
            jsonResult = om.writeValueAsString(vcPlace);
            return jsonResult;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonResult;
    }

    @RequestMapping("/vcode")
    // 验证码主界面
    public String vCode(){
        return "retrieve";
    }
    @RequestMapping("/gotoVer")
    public String gotoVer(){
        return "verification";
    }
    @RequestMapping("/getEmailCode")
    @ResponseBody
    public void getEmailCode(String emailNum, HttpServletResponse response){
        String code = String.valueOf(Math.random()).substring(2, 8);
        Cookie cookie=new Cookie("emailCode",code);
        cookie.setMaxAge(180);
        response.addCookie(cookie);
        MailBox.setMaile(emailNum,code);
    }
    @RequestMapping("/resetPwd")
    @ResponseBody
    public String resetPwd(@RequestBody User user, String code, HttpServletRequest request){

        Cookie[] cookies = request.getCookies();
        String emailCode = "";
        for (Cookie c:cookies){
            if (c.getName().equals("emailCode")){
                emailCode=c.getValue();
            }
        }
        if (emailCode.equals("")){
            return "验证码失效，请重新获取";
        }else if (code.equals(emailCode)){
            boolean rs=weatherService.selectWhether(user);
            if (rs){
                weatherService.updatePwd(user);
                return "true";
            }else {
                return "身份校验失败，用户名与邮箱输入有误！";
            }
        }else {
            return "验证码输入错误！";
        }
    }
}
