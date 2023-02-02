package com.me.config;


import com.me.way.DownloadHtml;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;

@Data
@Component
public class UtilConfig {

    @Value("${developPath1}")
    public String developPath1;

    @Value("${localPath1}")
    public String localPath1;

    @Value("${developPath2}")
    public String developPath2;

    @Value("${localPath2}")
    public String localPath2;
}
