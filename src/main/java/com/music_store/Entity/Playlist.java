package com.music_store.Entity;

import java.util.Date;
import java.util.List;

public class Playlist {
    private Integer id;
    private Integer userId;
    private String name;
    private String description;
    // visibility: "private" or "shared"
    private String visibility;
    private Date createTime;
    private Date updateTime;

    // not persisted fields
    private List<Music> tracks; // optional view aggregation

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getVisibility() { return visibility; }
    public void setVisibility(String visibility) { this.visibility = visibility; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public List<Music> getTracks() { return tracks; }
    public void setTracks(List<Music> tracks) { this.tracks = tracks; }
}