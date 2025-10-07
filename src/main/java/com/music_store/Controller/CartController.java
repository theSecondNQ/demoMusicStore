package com.music_store.Controller;

import com.music_store.Entity.Cart;
import com.music_store.Entity.User;
import com.music_store.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/list")
    public String list(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        List<Cart> cartList = cartService.getCartByUserId(user.getId());
        model.addAttribute("cartList", cartList);

        double total = cartService.calculateTotal(user.getId());
        model.addAttribute("total", total);

        return "cart_list";
    }

    @PostMapping("/add")
    public String add(Integer musicId, Integer quantity, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Cart cart = new Cart();
        cart.setUserId(user.getId());
        cart.setMusicId(musicId);
        cart.setQuantity(quantity != null ? quantity : 1);

        cartService.addToCart(cart);
        return "redirect:/cart/list";
    }

    @PostMapping("/update")
    public String update(Integer cartId, Integer quantity, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setQuantity(quantity);
        cartService.updateCart(cart);
        return "redirect:/cart/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        cartService.removeFromCart(id);
        return "redirect:/cart/list";
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<Cart> cartList = cartService.getCartByUserId(user.getId());
        if (cartList.isEmpty()) {
            return "redirect:/cart/list";
        }

        double total = cartService.calculateTotal(user.getId());

        model.addAttribute("cartList", cartList);
        model.addAttribute("total", total);

        return "checkout";
    }
}