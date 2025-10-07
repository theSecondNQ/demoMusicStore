package com.music_store.Dao;

import com.music_store.Entity.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryDao {

    @Insert("INSERT INTO category(name, description) VALUES(#{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Category category);

    @Update("UPDATE category SET name=#{name}, description=#{description} WHERE id=#{id}")
    int update(Category category);

    @Delete("DELETE FROM category WHERE id=#{id}")
    int delete(Integer id);

    @Select("SELECT * FROM category WHERE id=#{id}")
    Category selectById(Integer id);

    @Select("SELECT * FROM category")
    List<Category> selectAll();
}