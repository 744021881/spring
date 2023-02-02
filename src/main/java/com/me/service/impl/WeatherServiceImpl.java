package com.me.service.impl;

import com.me.entity.City;
import com.me.entity.User;
import com.me.mapper.WeatherMapper;
import com.me.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class WeatherServiceImpl implements WeatherService {
    @Autowired()
    public WeatherMapper weatherMapper;
    @Override
    public List<City> selectAllCity(String cityName) {
        return weatherMapper.selectAllCity(cityName);
    }

    @Override
    public boolean insertAddress(String address) {
        return weatherMapper.insertAddress(address)>0;
    }

    @Override
    public User signIn(String username, String password) {
        return weatherMapper.signIn(username,password);
    }

    @Override
    public User selectByUserId(int id) {
        return weatherMapper.selectByUserId(id);
    }

    @Override
    public boolean insertNewUser(User user) {
        return weatherMapper.insertNewUser(user)>0;
    }

    @Override
    public boolean selectCount(User user) {
        return weatherMapper.selectCount(user)>0;
    }

    @Override
    public boolean selectWhether(User user) {
        return weatherMapper.selectWhether(user)>0;
    }

    @Override
    public boolean updatePwd(User user) {
        return weatherMapper.updatePwd(user)>0;
    }
}
