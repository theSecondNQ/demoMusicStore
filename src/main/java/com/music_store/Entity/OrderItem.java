package com.music_store.Entity;

public class OrderItem {
    private Integer id;
    private Integer orderId;
    private Integer musicId;
    private String artist;
    private Double price;
    private Integer quantity;

    public OrderItem() {}

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
}