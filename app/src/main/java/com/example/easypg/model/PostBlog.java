package com.example.easypg.model;

public class PostBlog {

    private String Title;
   private String ImageUrl;
   private String Descript;
   private String LongLat;

    public PostBlog(String title, String imageUrl, String descript ,String longLat) {
        Title = title;
        ImageUrl = imageUrl;
        Descript = descript;
        LongLat = longLat;
    }

    public PostBlog() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getDescript() {
        return Descript;
    }

    public void setDescript(String descript) {
        Descript = descript;
    }

    public String getLongLat() { return LongLat; }

    public void setLongLat(String longLat) { LongLat = longLat; }
}
