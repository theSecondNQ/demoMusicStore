package com.music_store.Service;

import com.music_store.Dao.FavouriteDao;
import com.music_store.Entity.Favourite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavouriteService {

    @Autowired
    private FavouriteDao favouriteDao;

    public boolean addFavourite(Integer userId, Integer musicId) {
        // 检查是否已经收藏
        Favourite existing = favouriteDao.selectByUserAndMusic(userId, musicId);
        if (existing != null) {
            return true;
        }

        Favourite favourite = new Favourite();
        favourite.setUserId(userId);
        favourite.setMusicId(musicId);
        return favouriteDao.insert(favourite) > 0;
    }

    public boolean removeFavourite(Integer userId, Integer musicId) {
        return favouriteDao.delete(userId, musicId) > 0;
    }

    public List<Favourite> getFavouritesByUserId(Integer userId) {
        return favouriteDao.selectByUserId(userId);
    }

    public boolean isFavourite(Integer userId, Integer musicId) {
        return favouriteDao.selectByUserAndMusic(userId, musicId) != null;
    }

    public int getFavouriteCount(Integer userId) {
        return favouriteDao.countByUserId(userId);
    }
}