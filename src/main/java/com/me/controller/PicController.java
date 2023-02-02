package com.me.controller;

import com.me.entity.User;
import com.me.service.WeatherService;
import com.me.way.MailBox;
import com.me.way.UploadImg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/core")
public class PicController {
    @Autowired
    public WeatherService weatherService;
    @RequestMapping("/update")
    public String updateSelf(@RequestParam("file") MultipartFile file, User user){
        return "redirect:/weather/index";
    }
    @RequestMapping("/setPic")
    public String setPic(@RequestParam("file") MultipartFile file) throws Exception {
        UploadImg.setImg(file);
        return "image";
    }
    @RequestMapping("/email")
    @ResponseBody
    public void setEmail(String emailNum,HttpServletResponse response){
        String code = String.valueOf(Math.random()).substring(2, 8);
        Cookie cookie=new Cookie("emailCode",code);
        cookie.setMaxAge(300);
        response.addCookie(cookie);
        MailBox.setMaile(emailNum,code);
    }
    @PostMapping("/newUser")
    @ResponseBody
    public String addUser(@RequestBody User user, String code, HttpServletRequest request){
        boolean existence=weatherService.selectCount(user);
        Cookie[] cookies = request.getCookies();
        String emailCode = "";
        for (Cookie c:cookies){
            if (c.getName().equals("emailCode")){
                emailCode=c.getValue();
            }
        }
        if (emailCode==""){
            return "验证码失效，请重新获取";
        }else if (code.equals(emailCode)){
            if (!existence){
                boolean a= weatherService.insertNewUser(user);
                return "true";
            }else {
                return "用户名已存在，请更换";
            }
        }else {
            return "验证码输入有误，请重新输入";
        }
    }
    @RequestMapping("gotoLogin")
    public ModelAndView gotoLogin(@RequestParam(defaultValue = "") String rs){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("state",rs);
        modelAndView.setViewName("login");
        return modelAndView;
    }
}
