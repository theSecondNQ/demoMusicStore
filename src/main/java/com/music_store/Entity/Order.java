package com.music_store.Entity;

import java.util.Date;
import java.util.List;

/**
 * 订单实体类
 * 对应一份订单中的各项数据
 */
public class Order {

    /** 订单ID **/
    private Integer id;

    /** 订单编号 **/
    private String orderNumber;

    /** 用户ID **/
    private Integer userId;

    /** 用户名称 **/
    private String username;

    /** 订单总金额 **/
    private Double totalAmount;

    /** 订单状态（pending, paid, completed, cancelled 四个订单状态） **/
    private String status;

    /** 支付方式 **/
    private String paymentMethod;

    /** 收货地址 **/
    private String shippingAddress;

    /** 订单创建时间 **/
    private Date createTime;

    /** 订单更新时间 **/
    private Date updateTime;

    /** 订单项列表 **/
    private List<OrderItem> orderItems;


    //无参构造函数
    public Order() {}

    //Getter和Setter

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }
}