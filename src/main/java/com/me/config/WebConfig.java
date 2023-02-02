package com.me.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Resource
    public UtilConfig utilConfig;
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
//        registry.addRedirectViewController("/","/weather/index");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        File directory = new File("");
        String file=null;
        try {
            file= directory.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        registry.addResourceHandler("/soutExcel/**").addResourceLocations("file:"+file+utilConfig.getDevelopPath1());
        //此处还可继续增加目录。。。。
    }
}
