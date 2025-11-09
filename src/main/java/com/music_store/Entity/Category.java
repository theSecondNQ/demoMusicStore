package com.music_store.Entity;

/**
 * 音乐大类实体类
 * 对应一个音乐类型中的各项数据
 */

public class Category {

    /** 类别ID **/
    private Integer id;

    /** 类别名称 **/
    private String name;

    /** 类别描述 **/
    private String description;

    // 无参构造
    public Category() {}

    // 有参构造
    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getter和Setter
    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }
}