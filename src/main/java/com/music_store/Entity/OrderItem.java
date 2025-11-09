package com.music_store.Entity;

/**
 * 订单项实体类
 * 对应订单中的单个音乐商品项信息
 */
public class OrderItem {

    /** 订单项ID **/
    private Integer id;

    /** 订单ID **/
    private Integer orderId;

    /** 音乐ID **/
    private Integer musicId;

    /** 音乐名称 **/
    private String musicTitle;

    /** 音乐作者 **/
    private String artist;

    /** 音乐单价 **/
    private Double price;

    /** 购买数量 **/
    private Integer quantity;


    //无参构造函数

    public OrderItem() {}

    //Getter和Setter方法

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public Integer getOrderId() { return orderId; }

    public void setOrderId(Integer orderId) { this.orderId = orderId; }

    public Integer getMusicId() { return musicId; }

    public void setMusicId(Integer musicId) { this.musicId = musicId; }

    public String getArtist() { return artist; }

    public void setArtist(String artist) { this.artist = artist; }

    public Double getPrice() { return price; }

    public void setPrice(Double price) { this.price = price; }

    public Integer getQuantity() { return quantity; }

    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getMusicTitle() { return musicTitle;}

    public void setMusicTitle(String musicTitle) { this.musicTitle = musicTitle; }
}