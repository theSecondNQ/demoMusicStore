package com.music_store.Dao; // 或者您的实际包名

import com.music_store.Entity.User; // 或者您的实际包名
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserDao {

    @Insert("INSERT INTO user(username, password, email, role) VALUES(#{username}, #{password}, #{email}, #{role})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User selectByUsername(String username);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User selectById(Integer id);

    // 更新用户信息（包括密码和邮箱）
    @Update("UPDATE user SET password = #{password}, email = #{email} WHERE id = #{id}")
    int update(User user);

    // 专门用于修改密码的方法
    @Update("UPDATE user SET password = #{password} WHERE id = #{id}")
    int updatePassword(User user);

    // 更新用户信息（不包含密码）
    @Update("UPDATE user SET email = #{email} WHERE id = #{id}")
    int updateUserInfo(User user);

    @Delete("DELETE FROM user WHERE id = #{id}")
    int delete(Integer id);

    @Select("SELECT * FROM user WHERE role = 'user'")
    List<User> selectAllUsers();
}