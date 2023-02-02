package com.me.way;

import com.me.config.UtilConfig;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.*;
@Component
public class DownloadHtml{
    private static DownloadHtml onself;

    @Resource()
    private UtilConfig utilConfig;

    @PostConstruct
    private void init() {
        onself= this;
    }

    static File directory = new File("");
    static String file;
    static {
        try {
            file = directory.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearModel() throws IOException {
        FileWriter fileWriter = new FileWriter(file+onself.utilConfig.getDevelopPath2()+"model.html");
        fileWriter.write("");
        fileWriter.flush();
        fileWriter.close();
        System.out.println("模板内容清除完毕");
    }
    public void downloadFileFromUrl(String htmlBody) throws IOException {
        clearModel();
        String stringHtml = "<!DOCTYPE html>" +htmlBody;
        PrintStream printStream = new PrintStream(new FileOutputStream(file+onself.utilConfig.getDevelopPath2()+"model.html"));
        printStream.println(stringHtml);
    }
}
