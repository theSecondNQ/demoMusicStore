package com.music_store.Entity;

import java.util.Date;

/**
 * 音乐收藏实体类
 * 对应一个用户的收藏夹中的各项数据
 */

public class Favourite {

    /** 收藏夹ID **/
    private Integer id;

    /** 用户ID **/
    private Integer userId;

    /** 音乐ID **/
    private Integer musicId;

    /** 创建日期 **/
    private Date createTime;

    // ==================== 关联的音乐信息（用于显示） ====================
    /** 音乐作者 **/
    private String artist;

    /** 音乐所属专辑 **/
    private String album;

    /** 音乐价格 **/
    private Double price;

    /** 音乐所属类别名称 **/
    private String categoryName;

    // 无参构造
    public Favourite() {}

    // 有参构造
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

    public String getArtist() { return artist; }

    public void setArtist(String artist) { this.artist = artist; }

    public String getAlbum() { return album; }

    public void setAlbum(String album) { this.album = album; }

    public Double getPrice() { return price; }

    public void setPrice(Double price) { this.price = price; }

    public String getCategoryName() { return categoryName; }

    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}