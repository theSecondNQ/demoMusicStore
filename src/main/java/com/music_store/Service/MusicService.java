package com.music_store.Service;

import com.music_store.Entity.Music;
import com.music_store.Dao.MusicDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicService {

    @Autowired
    private MusicDao musicDao;

    public List<Music> listMusic(String keyword, String category) {
        return musicDao.selectByCondition(keyword, category);
    }

    public List<Music> getAllMusic() {
        return musicDao.selectAll();
    }

    public Music getById(Integer id) {
        return musicDao.selectById(id);
    }

    public boolean addMusic(Music music) {
        return musicDao.insert(music) > 0;
    }

    public boolean updateMusic(Music music) {
        return musicDao.update(music) > 0;
    }

    public boolean deleteMusic(Integer id) {
        return musicDao.delete(id) > 0;
    }

    public void incrementPlayCount(Integer id) {
        musicDao.incrementPlayCount(id);
    }
}