package com.music_store.Service;

import com.music_store.Entity.User;
import com.music_store.Dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User login(String username, String password) {
        User user = userDao.selectByUsername(username);
        if (user != null && user.getPassword().equals(md5(password))) {
            return user;
        }
        return null;
    }

    public boolean register(User user) {
        if (userDao.selectByUsername(user.getUsername()) != null) {
            return false;
        }
        user.setPassword(md5(user.getPassword()));
        user.setRole("user");
        return userDao.insert(user) > 0;
    }

    public boolean updateUser(User user) {
        return userDao.update(user) > 0;
    }

    public boolean deleteUser(Integer id) {
        return userDao.delete(id) > 0;
    }

    public List<User> getAllUsers() {
        return userDao.selectAllUsers();
    }

    // 修改密码方法
    public boolean changePassword(Integer userId, String oldPassword, String newPassword) {
        User user = userDao.selectById(userId);
        // 用户不存在或原密码错误
        if (user == null || !user.getPassword().equals(md5(oldPassword))) {
            return false;
        }

        user.setPassword(md5(newPassword));
        return userDao.updatePassword(user) > 0;
    }

    // 更新用户信息
    public boolean updateUserInfo(User user) {
        User existingUser = userDao.selectById(user.getId());
        if (existingUser == null) {
            return false;
        }
        // 保留原密码，只更新其他信息
        user.setPassword(existingUser.getPassword());
        return userDao.update(user) > 0;
    }

    public User selectById(Integer id) {
        return userDao.selectById(id);
    }

    private String md5(String text) {
        return DigestUtils.md5DigestAsHex(text.getBytes());
    }
}