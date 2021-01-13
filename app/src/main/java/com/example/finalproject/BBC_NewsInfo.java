package com.example.finalproject;

import java.util.Objects;

public class BBC_NewsInfo {
    private Long Id;
    private String headline;
    private  String description;
    private String link;
    private String date;

    public BBC_NewsInfo(){

    }
    public BBC_NewsInfo(String headline, String description, String link, String date) {
        this.headline=headline;
        this.description=description;
        this.link=link;
        this.date=date;
    }
    public BBC_NewsInfo(Long Id, String headline, String description, String link, String date) {
        this.Id=Id;
        this.headline=headline;
        this.description=description;
        this.link=link;
        this.date=date;
    }
    public Long getId(){ return Id; }

    public void setId(Long Id){this.Id=Id;}

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate(){ return date; }

    public void setDate(String date){ this.date=date; }




}
