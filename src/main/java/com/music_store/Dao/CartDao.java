package com.music_store.Dao;

import com.music_store.Entity.Cart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartDao {

    @Insert("INSERT INTO cart(user_id, music_id, quantity) VALUES(#{userId}, #{musicId}, #{quantity})")
    int insert(Cart cart);

    @Update("UPDATE cart SET quantity=#{quantity} WHERE id=#{id}")
    int update(Cart cart);

    @Delete("DELETE FROM cart WHERE id=#{id}")
    int delete(Integer id);

    @Delete("DELETE FROM cart WHERE user_id=#{userId} AND music_id=#{musicId}")
    int deleteByUserAndMusic(@Param("userId") Integer userId, @Param("musicId") Integer musicId);

    @Delete("DELETE FROM cart WHERE music_id=#{musicId}")
    int deleteByMusic(Integer musicId);

    @Select("SELECT c.*, m.title, m.artist, m.price, m.album FROM cart c " +
            "JOIN music m ON c.music_id = m.id " +
            "WHERE c.user_id=#{userId} AND IFNULL(m.deleted,0)=0")
    List<Cart> selectByUserId(Integer userId);

    @Select("SELECT * FROM cart WHERE user_id=#{userId} AND music_id=#{musicId}")
    Cart selectByUserAndMusic(@Param("userId") Integer userId, @Param("musicId") Integer musicId);
}