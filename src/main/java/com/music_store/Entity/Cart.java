package com.music_store.Entity;

import java.util.Date;

/**
 * 购物车实体类
 * 对应购物车中的各项数据
 */

public class Cart {

    /** 购物车ID **/
    private Integer id;

    /** 用户ID **/
    private Integer userId;

    /** 音乐ID **/
    private Integer musicId;

    /** 音乐购买数量**/
    private Integer quantity;

    /** 购物车创建时间 **/
    private Date createTime;

    // ==================== 关联的音乐信息（用于显示） ====================
    /** 音乐名称 **/
    private String title;

    /** 音乐作者 **/
    private String artist;

    /** 音乐价格 **/
    private Double price;

    /** 音乐所属专辑 **/
    private String album;

    // 无参构造
    public Cart() {}

    // 有参构造
    public Cart(Integer userId, Integer musicId, Integer quantity) {
        this.userId = userId;
        this.musicId = musicId;
        this.quantity = quantity;
    }

    // Getter和Setter
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