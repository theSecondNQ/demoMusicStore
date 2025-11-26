package com.music_store.Dao;

import com.music_store.Entity.Music;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface MusicDao {

    @Insert("INSERT INTO music(title, artist, category_id, album, duration, price, audio_path, deleted) " +
            "VALUES(#{title}, #{artist}, #{categoryId}, #{album}, #{duration}, #{price}, #{audioPath}, IFNULL(#{deleted}, 0))")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Music music);

    @Update("UPDATE music SET title=#{title}, artist=#{artist}, category_id=#{categoryId}, " +
            "album=#{album}, duration=#{duration}, price=#{price}, audio_path=#{audioPath} WHERE id=#{id}")
    int update(Music music);

    @Delete("DELETE FROM music WHERE id=#{id}")
    int delete(Integer id);

    // 软删除：仅标记 deleted=1
    @Update("UPDATE music SET deleted = 1 WHERE id = #{id}")
    int softDelete(Integer id);

    @Select("SELECT m.*, c.name as categoryName FROM music m LEFT JOIN category c ON m.category_id = c.id WHERE m.id=#{id} AND IFNULL(m.deleted,0)=0")
    Music selectById(Integer id);

    @Select("SELECT m.*, c.name as categoryName FROM music m LEFT JOIN category c ON m.category_id = c.id WHERE IFNULL(m.deleted,0)=0")
    List<Music> selectAll();

    List<Music> selectByCondition(@Param("keyword") String keyword,
                                  @Param("category") String category);

    @Update("UPDATE music SET play_count = play_count + 1 WHERE id = #{id} AND IFNULL(deleted,0)=0")
    int incrementPlayCount(Integer id);

    @Select("SELECT m.*, c.name as categoryName FROM music m LEFT JOIN category c ON m.category_id = c.id WHERE m.category_id = #{categoryId} AND m.id <> #{excludeId} AND IFNULL(m.deleted,0)=0 ORDER BY m.play_count DESC LIMIT #{limit}")
    List<Music> selectTopByCategory(@Param("categoryId") Integer categoryId,
                                    @Param("excludeId") Integer excludeId,
                                    @Param("limit") Integer limit);

    @Select("SELECT m.*, c.name as categoryName FROM music m LEFT JOIN category c ON m.category_id = c.id WHERE m.artist = #{artist} AND m.id <> #{excludeId} AND IFNULL(m.deleted,0)=0 ORDER BY m.play_count DESC LIMIT #{limit}")
    List<Music> selectTopByArtist(@Param("artist") String artist,
                                  @Param("excludeId") Integer excludeId,
                                  @Param("limit") Integer limit);

    @Select("SELECT m.*, c.name as categoryName FROM music m LEFT JOIN category c ON m.category_id = c.id WHERE IFNULL(m.deleted,0)=0 ORDER BY m.play_count DESC LIMIT #{limit}")
    List<Music> selectTopPopular(@Param("limit") Integer limit);
}