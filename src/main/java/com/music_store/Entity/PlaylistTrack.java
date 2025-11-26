package com.music_store.Entity;

import java.util.Date;

public class PlaylistTrack {
    private Integer id;
    private Integer playlistId;
    private Integer musicId;
    private Integer position;
    private Date addTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getPlaylistId() { return playlistId; }
    public void setPlaylistId(Integer playlistId) { this.playlistId = playlistId; }

    public Integer getMusicId() { return musicId; }
    public void setMusicId(Integer musicId) { this.musicId = musicId; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }

    public Date getAddTime() { return addTime; }
    public void setAddTime(Date addTime) { this.addTime = addTime; }
}