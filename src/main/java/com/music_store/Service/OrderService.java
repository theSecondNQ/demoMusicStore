package com.music_store.Service;

import com.music_store.Entity.Cart;
import com.music_store.Entity.Order;
import com.music_store.Entity.OrderItem;
import com.music_store.Dao.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private CartService cartService;

    @Transactional
    public boolean createOrder(Order order, List<Cart> cartItems) {
        // 生成订单号
        String orderNumber = "ORD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8);
        order.setOrderNumber(orderNumber);
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());

        // 插入订单
        int result = orderDao.insert(order);
        if (result <= 0) {
            return false;
        }

        // 插入订单项
        for (Cart cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setMusicTitle(cartItem.getTitle());
            orderItem.setMusicId(cartItem.getMusicId());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderDao.insertItem(orderItem);
        }

        // 清空购物车
        for (Cart cartItem : cartItems) {
            cartService.removeFromCart(cartItem.getId());
        }

        return true;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = orderDao.selectAll();
        for (Order order : orders) {
            List<OrderItem> items = orderDao.selectItemsByOrderId(order.getId());
            order.setOrderItems(items);
        }
        return orders;
    }

    public List<Order> getOrdersByStatus(String status) {
        List<Order> orders = orderDao.selectByStatus(status);
        for (Order order : orders) {
            List<OrderItem> items = orderDao.selectItemsByOrderId(order.getId());
            order.setOrderItems(items);
        }
        return orders;
    }

    public List<Order> getOrdersByUsername(String username) {
        List<Order> orders = orderDao.selectByUsername(username);
        for (Order order : orders) {
            List<OrderItem> items = orderDao.selectItemsByOrderId(order.getId());
            order.setOrderItems(items);
        }
        return orders;
    }

    public Order getOrderById(Integer id) {
        Order order = orderDao.selectById(id);
        if (order != null) {
            List<OrderItem> items = orderDao.selectItemsByOrderId(order.getId());
            order.setOrderItems(items);
        }
        return order;
    }

    public boolean updateOrderStatus(Integer id, String status) {
        return orderDao.updateStatus(id, status) > 0;
    }

    @Transactional
    public boolean deleteOrder(Integer id) {
        // 先删除订单项
        orderDao.deleteItems(id);
        // 再删除订单
        return orderDao.delete(id) > 0;
    }

    public int getOrderCountByStatus(String status) {
        return orderDao.countByStatus(status);
    }
}