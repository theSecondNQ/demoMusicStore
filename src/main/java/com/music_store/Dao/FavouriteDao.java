package com.music_store.Dao;

import com.music_store.Entity.Favourite;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FavouriteDao {

    @Insert("INSERT INTO favourite(user_id, music_id) VALUES(#{userId}, #{musicId})")
    int insert(Favourite favourite);

    @Delete("DELETE FROM favourite WHERE user_id=#{userId} AND music_id=#{musicId}")
    int delete(@Param("userId") Integer userId, @Param("musicId") Integer musicId);

    @Delete("DELETE FROM favourite WHERE music_id=#{musicId}")
    int deleteByMusicId(Integer musicId);

    @Select("SELECT * FROM favourite WHERE user_id=#{userId} AND music_id=#{musicId}")
    Favourite selectByUserAndMusic(@Param("userId") Integer userId, @Param("musicId") Integer musicId);

    @Select("SELECT f.*, m.title as music_title, m.artist, m.album, m.price, c.name as category_name " +
            "FROM favourite f " +
            "JOIN music m ON f.music_id = m.id " +
            "LEFT JOIN category c ON m.category_id = c.id " +
            "WHERE f.user_id=#{userId} AND IFNULL(m.deleted,0)=0 " +
            "ORDER BY f.create_time DESC")
    List<Favourite> selectByUserId(Integer userId);

    @Select("SELECT COUNT(*) FROM favourite WHERE user_id=#{userId}")
    int countByUserId(Integer userId);
}