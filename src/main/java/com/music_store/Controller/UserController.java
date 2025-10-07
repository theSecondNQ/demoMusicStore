package com.music_store.Controller;

import com.music_store.Entity.User;
import com.music_store.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/music/list";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(String username, String password, HttpSession session, Model model) {
        User user = userService.login(username, password);
        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/music/list";
        } else {
            model.addAttribute("msg", "用户名或密码错误");
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(User user, Model model) {
        if (userService.register(user)) {
            model.addAttribute("msg", "注册成功，请登录");
            return "login";
        } else {
            model.addAttribute("msg", "用户名已存在");
            return "register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        // 从数据库获取最新用户信息
        User currentUser = userService.selectById(user.getId());
        model.addAttribute("user", currentUser);
        return "profile";
    }

    @PostMapping("/updateProfile")
    public String updateProfile(User user, HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        user.setId(currentUser.getId());
        if (userService.updateUserInfo(user)) {
            // 更新session中的用户信息
            session.setAttribute("user", userService.selectById(currentUser.getId()));
            model.addAttribute("msg", "资料更新成功");
        } else {
            model.addAttribute("msg", "资料更新失败");
        }
        return "profile";
    }

    // 修改密码功能
    @PostMapping("/changePassword")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 HttpSession session,
                                 Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // 验证新密码和确认密码是否一致
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("passwordError", "新密码和确认密码不一致");
            return "profile";
        }

        // 验证新密码长度
        if (newPassword.length() < 6) {
            model.addAttribute("passwordError", "新密码长度不能少于6位");
            return "profile";
        }

        if (userService.changePassword(user.getId(), oldPassword, newPassword)) {
            model.addAttribute("success", "密码修改成功");
            // 更新session中的用户信息
            session.setAttribute("user", userService.selectById(user.getId()));
        } else {
            model.addAttribute("passwordError", "原密码错误");
        }

        return "profile";
    }

    // 管理员功能：用户管理
    @GetMapping("/admin/users")
    public String userManagement(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return "redirect:/login";
        }

        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/user_management";
    }

    @PostMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return "redirect:/login";
        }

        userService.deleteUser(id);
        return "redirect:/admin/users";
    }
}