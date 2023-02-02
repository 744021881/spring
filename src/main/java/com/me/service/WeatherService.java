package com.me.service;

import com.me.entity.City;
import com.me.entity.User;

import java.util.List;

public interface WeatherService {
    List<City> selectAllCity(String cityName);
    boolean insertAddress(String address);
    User signIn(String username, String password);
    User selectByUserId(int id);
    boolean insertNewUser(User user);
    boolean selectCount(User user);
    boolean selectWhether(User user);
    boolean updatePwd(User user);
}
