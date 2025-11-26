package com.music_store.Service;

import com.music_store.Dao.PlaylistDao;
import com.music_store.Entity.Music;
import com.music_store.Entity.Playlist;
import com.music_store.Entity.PlaylistTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistDao playlistDao;

    @Autowired
    private MusicService musicService;

    public List<Playlist> listByUser(Integer userId) {
        return playlistDao.selectByUserId(userId);
    }

    public Playlist getById(Integer id) {
        Playlist p = playlistDao.selectById(id);
        if (p == null) return null;
        List<PlaylistTrack> pts = playlistDao.selectTracksByPlaylistId(id);
        List<Music> tracks = new ArrayList<>();
        for (PlaylistTrack pt : pts) {
            Music m = musicService.getById(pt.getMusicId());
            if (m != null) {
                tracks.add(m);
            }
        }
        p.setTracks(tracks);
        return p;
    }

    public Integer create(Integer userId, String name, String description, String visibility) {
        Playlist p = new Playlist();
        p.setUserId(userId);
        p.setName(name);
        p.setDescription(description);
        p.setVisibility(visibility != null ? visibility : "private");
        playlistDao.insertPlaylist(p);
        return p.getId();
    }

    public void update(Playlist p) {
        playlistDao.updatePlaylist(p);
    }

    public void delete(Integer playlistId) {
        playlistDao.deletePlaylist(playlistId);
    }

    public List<Playlist> listShared() {
        return playlistDao.selectShared();
    }

    public boolean addTrack(Integer playlistId, Integer musicId) {
        // 简单防重：若已存在则忽略
        if (playlistDao.existsMusicInPlaylist(playlistId, musicId) > 0) {
            return false;
        }
        List<PlaylistTrack> existing = playlistDao.selectTracksByPlaylistId(playlistId);
        int nextPos = existing.stream().map(PlaylistTrack::getPosition).max(Comparator.naturalOrder()).orElse(0) + 1;
        PlaylistTrack pt = new PlaylistTrack();
        pt.setPlaylistId(playlistId);
        pt.setMusicId(musicId);
        pt.setPosition(nextPos);
        playlistDao.insertTrack(pt);
        return true;
    }

    public void removeTrack(Integer playlistId, Integer musicId) {
        playlistDao.deleteTrackByPlaylistAndMusic(playlistId, musicId);
    }

    public void reorder(Integer trackId, Integer position) {
        playlistDao.updateTrackPosition(trackId, position);
    }
}