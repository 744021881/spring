package com.me.controller;

import com.me.way.DownloadHtml;
import com.me.way.HtmlToExcel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping("/html")
public class HtmlToExcelController {
    @RequestMapping("/excel")
    @ResponseBody
    public String toExcel(String htmlName) throws IOException {
        HtmlToExcel html=new HtmlToExcel();
        return html.toExcelImpl(htmlName);
    }
    @RequestMapping("/setHTML")
    @ResponseBody
    public void toHTML(String htmlBody) throws IOException {
        DownloadHtml downloadHtml=new DownloadHtml();
        downloadHtml.downloadFileFromUrl(htmlBody);
    }
    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        return "刘斌sb";
    }
    @RequestMapping("/goMain")
    public String gotoMain(){
        return "table";
    }
}
