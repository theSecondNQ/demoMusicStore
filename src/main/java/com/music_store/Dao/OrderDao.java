package com.music_store.Dao;

import com.music_store.Entity.Order;
import com.music_store.Entity.OrderItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderDao {

    @Insert("INSERT INTO `order`(order_number, user_id, total_amount, status, payment_method, shipping_address) " +
            "VALUES(#{orderNumber}, #{userId}, #{totalAmount}, #{status}, #{paymentMethod}, #{shippingAddress})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Order order);

    @Insert("INSERT INTO order_item(order_id, music_id, price, quantity) " +
            "VALUES(#{orderId}, #{musicId}, #{price}, #{quantity})")
    int insertItem(OrderItem orderItem);

    @Select("SELECT o.*, u.username FROM `order` o JOIN user u ON o.user_id = u.id WHERE o.id = #{id}")
    Order selectById(Integer id);

    @Select("SELECT o.*, u.username FROM `order` o JOIN user u ON o.user_id = u.id ORDER BY o.create_time DESC")
    List<Order> selectAll();

    @Select("SELECT o.*, u.username FROM `order` o JOIN user u ON o.user_id = u.id WHERE o.status = #{status} ORDER BY o.create_time DESC")
    List<Order> selectByStatus(String status);

    @Select("SELECT o.*, u.username FROM `order` o JOIN user u ON o.user_id = u.id WHERE u.username LIKE CONCAT('%',#{username},'%') ORDER BY o.create_time DESC")
    List<Order> selectByUsername(String username);

    @Select("SELECT oi.*, m.title as music_title, m.artist FROM order_item oi JOIN music m ON oi.music_id = m.id WHERE oi.order_id = #{orderId}")
    List<OrderItem> selectItemsByOrderId(Integer orderId);

    @Update("UPDATE `order` SET status = #{status}, update_time = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Integer id, @Param("status") String status);

    @Delete("DELETE FROM `order` WHERE id = #{id}")
    int delete(Integer id);

    @Delete("DELETE FROM order_item WHERE order_id = #{orderId}")
    int deleteItems(Integer orderId);

    @Select("SELECT COUNT(*) FROM `order` WHERE status = #{status}")
    int countByStatus(String status);
}