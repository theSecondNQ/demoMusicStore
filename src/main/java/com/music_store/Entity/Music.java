package com.music_store.Entity;

import java.util.Date;

/**
 * 音乐实体类
 * 对应一首具体歌曲的各项信息
 */

public class Music {

    /** 音乐ID **/
    private Integer id;

    /** 音乐名称 **/
    private String title;

    /** 音乐作者 **/
    private String artist;

    /** 分类ID（用于新增/编辑与查询） **/
    private Integer categoryId;

    /** 音乐所属类别名称 **/
    private String categoryName;

    /** 音乐所属专辑 **/
    private String album;

    /** 音乐时长 **/
    private Integer duration;

    /** 音乐累计播放次数 **/
    private Integer playCount;

    /** 音乐价格 **/
    private Double price;

    /** 音频文件相对路径（相对于配置的音频根目录） **/
    private String audioPath;

    /** 音乐创建时间 **/
    private Date createTime;

    /** 是否已被软删除（0/false 为正常，1/true 为已删除） **/
    private Boolean deleted;

    /** 当前用户是否已收藏该音乐 **/
    private Boolean favourite = false;

    // 无参构造
    public Music() {}

    // 有参构造
    public Music(String title, String artist, String album, Integer duration, Double price) {
        this.title = title;
        this.artist = artist;
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

    public String getAudioPath() { return audioPath; }

    public void setAudioPath(String audioPath) { this.audioPath = audioPath; }

    public Date getCreateTime() { return createTime; }

    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Boolean getDeleted() { return deleted; }

    public void setDeleted(Boolean deleted) { this.deleted = deleted; }

    public Boolean getFavourite() { return favourite; }

    public void setFavourite(Boolean favourite) { this.favourite = favourite; }
}