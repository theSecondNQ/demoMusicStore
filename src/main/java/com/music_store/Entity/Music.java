package com.music_store.Entity;

import java.util.Date;

public class Music {
    private Integer id;
    private String title;
    private String artist;
    private Integer categoryId;
    private String categoryName;
    private String album;
    private Integer duration;
    private Integer playCount;
    private Double price;
    private Date createTime;

    // 构造方法
    public Music() {}

    public Music(String title, String artist, Integer categoryId, String album, Integer duration, Double price) {
        this.title = title;
        this.artist = artist;
        this.categoryId = categoryId;
        this.album = album;
        this.duration = duration;
        this.price = price;
    }

    // Getter和Setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getAlbum() { return album; }
    public void setAlbum(String album) { this.album = album; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public Integer getPlayCount() { return playCount; }
    public void setPlayCount(Integer playCount) { this.playCount = playCount; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    private Boolean favourite = false;

    public Boolean getFavourite() { return favourite; }
    public void setFavourite(Boolean favourite) { this.favourite = favourite; }
}