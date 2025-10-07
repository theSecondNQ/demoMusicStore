package com.music_store.Controller;

import com.music_store.Entity.Cart;
import com.music_store.Entity.Order;
import com.music_store.Entity.User;
import com.music_store.Service.CartService;
import com.music_store.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @PostMapping("/create")
    public String createOrder(String paymentMethod, String shippingAddress, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<Cart> cartItems = cartService.getCartByUserId(user.getId());
        if (cartItems.isEmpty()) {
            return "redirect:/cart/list";
        }

        double total = cartService.calculateTotal(user.getId());

        Order order = new Order();
        order.setUserId(user.getId());
        order.setTotalAmount(total);
        order.setStatus("pending");
        order.setPaymentMethod(paymentMethod);
        order.setShippingAddress(shippingAddress);

        if (orderService.createOrder(order, cartItems)) {
            return "redirect:/order/success?orderId=" + order.getId();
        } else {
            return "redirect:/cart/list?error=1";
        }
    }

    @GetMapping("/success")
    public String orderSuccess(@RequestParam Integer orderId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "order_success";
    }

    @GetMapping("/list")
    public String orderList(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<Order> orders;
        if ("admin".equals(user.getRole())) {
            orders = orderService.getAllOrders();
        } else {
            orders = orderService.getOrdersByUsername(user.getUsername());
        }

        model.addAttribute("orders", orders);
        return "order_list";
    }

    @GetMapping("/detail/{id}")
    public String orderDetail(@PathVariable Integer id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Order order = orderService.getOrderById(id);
        if (order == null) {
            return "redirect:/order/list";
        }

        // 权限检查：用户只能查看自己的订单，管理员可以查看所有
        if (!"admin".equals(user.getRole()) && !user.getId().equals(order.getUserId())) {
            return "redirect:/order/list";
        }

        model.addAttribute("order", order);
        return "order_detail";
    }

    // 管理员功能：订单管理
    @GetMapping("/admin")
    public String orderManagement(@RequestParam(required = false) String status,
                                  @RequestParam(required = false) String username,
                                  HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return "redirect:/login";
        }

        List<Order> orders;
        if (status != null && !status.isEmpty()) {
            orders = orderService.getOrdersByStatus(status);
        } else if (username != null && !username.isEmpty()) {
            orders = orderService.getOrdersByUsername(username);
        } else {
            orders = orderService.getAllOrders();
        }

        model.addAttribute("orders", orders);
        model.addAttribute("pendingCount", orderService.getOrderCountByStatus("pending"));
        model.addAttribute("paidCount", orderService.getOrderCountByStatus("paid"));
        model.addAttribute("completedCount", orderService.getOrderCountByStatus("completed"));

        return "admin/order_management";
    }

    @PostMapping("/admin/updateStatus")
    public String updateOrderStatus(Integer orderId, String status, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return "redirect:/login";
        }

        orderService.updateOrderStatus(orderId, status);
        return "redirect:/order/admin";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteOrder(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return "redirect:/login";
        }

        orderService.deleteOrder(id);
        return "redirect:/order/admin";
    }
}