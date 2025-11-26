package com.music_store.Dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserDailyTrialDao {

    @Select("SELECT COUNT(*) FROM user_daily_trial WHERE user_id = #{userId} AND trial_date = CURDATE() AND used = 1")
    int countUsedToday(Integer userId);

    @Insert("INSERT INTO user_daily_trial(user_id, trial_date, used) VALUES(#{userId}, CURDATE(), 1) " +
            "ON DUPLICATE KEY UPDATE used = 1")
    int markUsedToday(Integer userId);
}