package com.example.appxemphim.Models;

public class Comments {
    public String userID, comment, time;
    public Comments(){
    }
    public Comments(String userID, String comment, String time) {
        this.userID = userID;
        this.comment = comment;
        this.time = time;
    }
}