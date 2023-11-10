package com.example.appxemphim.Models;

public class Movies {
    public String name, cast, link_poster, content, nation, category, year, viewcount, director, time, postingdate,link;
    public com.example.appxemphim.Models.stars stars;

    public Movies(){

    }

    public Movies(String cast, String category, String content, String director, String link, String link_poster,
                  String name, String nation,String postingdate, stars stars, String time, String viewcount, String year) {
        this.cast = cast;
        this.category = category;
        this.content = content;
        this.director = director;
        this.link=link;
        this.link_poster = link_poster;
        this.name = name;
        this.nation = nation;
        this.postingdate = postingdate;
        this.stars = stars;
        this.time = time;
        this.viewcount = viewcount;
        this.year = year;
    }
}