package com.music_store.Dao;

import com.music_store.Entity.Music;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface MusicDao {

    @Insert("INSERT INTO music(title, artist, category_id, album, duration, price) " +
            "VALUES(#{title}, #{artist}, #{categoryId}, #{album}, #{duration}, #{price})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Music music);

    @Update("UPDATE music SET title=#{title}, artist=#{artist}, category_id=#{categoryId}, " +
            "album=#{album}, duration=#{duration}, price=#{price} WHERE id=#{id}")
    int update(Music music);

    @Delete("DELETE FROM music WHERE id=#{id}")
    int delete(Integer id);

    @Select("SELECT m.*, c.name as categoryName FROM music m LEFT JOIN category c ON m.category_id = c.id WHERE m.id=#{id}")
    Music selectById(Integer id);

    @Select("SELECT m.*, c.name as categoryName FROM music m LEFT JOIN category c ON m.category_id = c.id")
    List<Music> selectAll();

    List<Music> selectByCondition(@Param("keyword") String keyword,
                                  @Param("category") String category);

    @Update("UPDATE music SET play_count = play_count + 1 WHERE id = #{id}")
    int incrementPlayCount(Integer id);
}