package com.music_store.Controller;

import com.music_store.Entity.Favourite;
import com.music_store.Entity.User;
import com.music_store.Service.FavouriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class FavouriteController {

    @Autowired
    private FavouriteService favouriteService;

    @PostMapping("/favourite/add/{musicId}")
    public String addFavourite(@PathVariable Integer musicId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        favouriteService.addFavourite(user.getId(), musicId);
        return "redirect:/music/detail/" + musicId;
    }

    @PostMapping("/favourite/remove/{musicId}")
    public String removeFavourite(@PathVariable Integer musicId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        favouriteService.removeFavourite(user.getId(), musicId);
        return "redirect:/music/detail/" + musicId;
    }

    @GetMapping("/favourite/list")
    public String favouriteList(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        List<Favourite> favourites = favouriteService.getFavouritesByUserId(user.getId());
        model.addAttribute("favourites", favourites);
        model.addAttribute("favouriteCount", favouriteService.getFavouriteCount(user.getId()));
        return "favourite_list";
    }

    // 从音乐列表页面快速收藏/取消收藏
    @PostMapping("/favourite/toggle/{musicId}")
    public String toggleFavourite(@PathVariable Integer musicId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        if (favouriteService.isFavourite(user.getId(), musicId)) {
            favouriteService.removeFavourite(user.getId(), musicId);
        } else {
            favouriteService.addFavourite(user.getId(), musicId);
        }

        return "redirect:/music/list";
    }
}