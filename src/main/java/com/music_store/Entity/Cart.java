package com.music_store.Entity;

import java.util.Date;

public class Cart {
    private Integer id;
    private Integer userId;
    private Integer musicId;
    private Integer quantity;
    private Date createTime;

    // 以下字段用于连接查询显示
    private String title;
    private String artist;
    private Double price;
    private String album;

    public Cart() {}

    public Cart(Integer userId, Integer musicId, Integer quantity) {
        this.userId = userId;
        this.musicId = musicId;
        this.quantity = quantity;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getMusicId() { return musicId; }
    public void setMusicId(Integer musicId) { this.musicId = musicId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getAlbum() { return album; }
    public void setAlbum(String album) { this.album = album; }
}