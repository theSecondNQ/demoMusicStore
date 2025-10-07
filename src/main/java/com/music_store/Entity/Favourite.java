package com.music_store.Entity;

import java.util.Date;

public class Favourite {
    private Integer id;
    private Integer userId;
    private Integer musicId;
    private Date createTime;

    // 关联的音乐信息（用于显示）
    private String musicTitle;
    private String artist;
    private String album;
    private Double price;
    private String categoryName;

    // 构造方法
    public Favourite() {}

    public Favourite(Integer userId, Integer musicId) {
        this.userId = userId;
        this.musicId = musicId;
    }

    // Getter和Setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getMusicId() { return musicId; }
    public void setMusicId(Integer musicId) { this.musicId = musicId; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getMusicTitle() { return musicTitle; }
    public void setMusicTitle(String musicTitle) { this.musicTitle = musicTitle; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public String getAlbum() { return album; }
    public void setAlbum(String album) { this.album = album; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}