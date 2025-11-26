package com.music_store.Service;

import com.music_store.Dao.UserDailyTrialDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDailyTrialService {

    @Autowired
    private UserDailyTrialDao trialDao;

    public boolean hasUsedToday(Integer userId) {
        return trialDao.countUsedToday(userId) > 0;
    }

    public void markUsedToday(Integer userId) {
        trialDao.markUsedToday(userId);
    }
}