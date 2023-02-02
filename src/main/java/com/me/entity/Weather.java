package com.me.entity;


import lombok.Data;

@Data
public class Weather {
    private String city;
    private String time;
    private String temperature;
    private String weather;
    private String remind;
    private String sky;
    private String cityCode;
}
