package com.me.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String head;
    private String email;

    public User(Integer id, String username, String password, String head, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.head = head;
        this.email = email;
    }
}
