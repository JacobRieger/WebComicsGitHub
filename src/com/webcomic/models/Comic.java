package com.webcomic.models;

import java.util.Date;

/**
 * Created by Jacob on 9/7/13.
 */
public class Comic {

    private int Id;
    private String Name;
    private String Website;
    private String LastUpdated;
    private Content Content;
    private Boolean HasBeenViewed;

    public int getId() {
        return Id;
    }
    public void setId(int id) {
        Id = id;
    }
    public String getName(){
        return Name;
    }
    public void setName(String name){
        Name = name;
    }
    public String getWebsite(){
        return Website;
    }
    public void setWebsite(String website){
        Website = website;
    }
    public String getLastUpdated() {
        return LastUpdated;
    }
    public void setLastUpdated(Date lastUpdated) {
        LastUpdated = lastUpdated;
    }
    public Content getContent() {
        return Content;
    }
    public void setContent(Content content) {
        Content = content;
    }
    public Boolean getHasBeenViewed() {
        return HasBeenViewed;
    }
    public void setHasBeenViewed(Boolean hasBeenViewed) {
        HasBeenViewed = hasBeenViewed;
    }



    public Comic() {
    }

    public Comic(Integer id, String name, String website, Date lastUpdated, Content content, Boolean hasBeenViewed)
    {
        Id = id;
        Name = name;
        Website = website;
        LastUpdated = lastUpdated;
        Content = content;
        HasBeenViewed = hasBeenViewed;
    }
}
