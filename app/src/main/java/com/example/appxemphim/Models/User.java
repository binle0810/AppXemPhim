package com.example.appxemphim.Models;

public class User {
    public String name, email, birthday, avatar;
    public int Admin;
    public User(){

    }
    public User(String name, String email, String birthday, int Admin, String avatar) {
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.Admin = Admin;
        this.avatar = avatar;
    }
}
