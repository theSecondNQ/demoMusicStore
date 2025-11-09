package com.music_store.Entity;

import java.util.Date;

/**
 * 用户实体类
 * 存储音乐商店系统的用户账户信息
 */
public class User {

    /** 用户ID **/
    private Integer id;

    /** 用户名 **/
    private String username;

    /** 登录密码 **/
    private String password;

    /** 电子邮箱 **/
    private String email;

    /** 用户角色 **/
    private String role;

    /** 账户创建时间 **/
    private Date createTime;

    //无参构造函数
    public User() {
    }

    /**
     * 有参构造函数
     * @param username 用户名
     * @param password 登录密码
     * @param email 电子邮箱
     * @param role 用户角色
     */
    public User(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    //Getter和Setter方法
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}