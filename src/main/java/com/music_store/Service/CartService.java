package com.music_store.Service;

import com.music_store.Entity.Cart;
import com.music_store.Dao.CartDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartDao cartDao;

    public boolean addToCart(Cart cart) {
        // 检查是否已经在购物车
        Cart existing = cartDao.selectByUserAndMusic(cart.getUserId(), cart.getMusicId());
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + cart.getQuantity());
            return cartDao.update(existing) > 0;
        } else {
            return cartDao.insert(cart) > 0;
        }
    }

    public boolean updateCart(Cart cart) {
        return cartDao.update(cart) > 0;
    }

    public boolean removeFromCart(Integer cartId) {
        return cartDao.delete(cartId) > 0;
    }

    public boolean removeFromCartByUserAndMusic(Integer userId, Integer musicId) {
        return cartDao.deleteByUserAndMusic(userId, musicId) > 0;
    }

    public List<Cart> getCartByUserId(Integer userId) {
        return cartDao.selectByUserId(userId);
    }

    public double calculateTotal(Integer userId) {
        List<Cart> cartItems = getCartByUserId(userId);
        return cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}