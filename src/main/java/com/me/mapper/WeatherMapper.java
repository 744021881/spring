package com.me.mapper;

import com.me.entity.City;
import com.me.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface WeatherMapper {
    List<City> selectAllCity(String cityName);
    int insertAddress(String address);
    User signIn(String username, String password);
    User selectByUserId(int id);
    int insertNewUser(User user);
    int selectCount(User user);
    int selectWhether(User user);
    int updatePwd(User user);
}
