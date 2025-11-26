package com.music_store.Service;

import com.music_store.Dao.CartDao;
import com.music_store.Dao.FavouriteDao;
import com.music_store.Dao.MusicDao;
import com.music_store.Dao.OrderDao;
import com.music_store.Dao.PlaylistDao;
import com.music_store.Entity.Music;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MusicService {

    @Autowired
    private MusicDao musicDao;

    @Autowired
    private FavouriteDao favouriteDao;

    @Autowired
    private CartDao cartDao;

    @Autowired
    private PlaylistDao playlistDao;

    @Autowired
    private OrderDao orderDao;

    public List<Music> listMusic(String keyword, String category) {
        return musicDao.selectByCondition(keyword, category);
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

    @Transactional
    public boolean deleteMusic(Integer id) {
        // 软删除：仅标记，不实际删除数据，保留订单历史
        return musicDao.softDelete(id) > 0;
    }

    public void incrementPlayCount(Integer id) {
        musicDao.incrementPlayCount(id);
    }

    public List<Music> topByCategory(Integer categoryId, Integer excludeId, Integer limit) {
        return musicDao.selectTopByCategory(categoryId, excludeId, limit);
    }

    public List<Music> topByArtist(String artist, Integer excludeId, Integer limit) {
        return musicDao.selectTopByArtist(artist, excludeId, limit);
    }

    public List<Music> topPopular(Integer limit) {
        return musicDao.selectTopPopular(limit);
    }
}