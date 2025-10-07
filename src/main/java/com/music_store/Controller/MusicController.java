package com.music_store.Controller;

import com.music_store.Entity.Music;
import com.music_store.Entity.User;
import com.music_store.Service.CategoryService;
import com.music_store.Service.FavouriteService;
import com.music_store.Service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/music")
public class MusicController {

    @Autowired
    private MusicService musicService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FavouriteService favouriteService;

    @GetMapping("/list")
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) String category,
                       HttpSession session,
                       Model model) {
        List<Music> musicList = musicService.listMusic(keyword, category);
        model.addAttribute("musicList", musicList);
        model.addAttribute("categories", categoryService.getAllCategories());

        User user = (User) session.getAttribute("user");
        if (user != null) {
            // 为每个音乐标记是否已被用户收藏
            for (Music music : musicList) {
                boolean isFav = favouriteService.isFavourite(user.getId(), music.getId());
                music.setFavourite(isFav);
            }
        }

        return "music_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, HttpSession session, Model model) {
        Music music = musicService.getById(id);
        if (music == null) {
            return "redirect:/music/list";
        }

        // 增加播放量
        musicService.incrementPlayCount(id);
        music.setPlayCount(music.getPlayCount() + 1);

        model.addAttribute("music", music);

        User user = (User) session.getAttribute("user");
        if (user != null) {
            boolean isFavourite = favouriteService.isFavourite(user.getId(), id);
            model.addAttribute("isFavourite", isFavourite);
        }

        return "music_detail";
    }

    // 管理员功能：添加音乐页面
    @GetMapping("/add")
    public String addPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("categories", categoryService.getAllCategories());
        return "music_add";
    }

    @PostMapping("/add")
    public String add(Music music, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return "redirect:/login";
        }
        musicService.addMusic(music);
        return "redirect:/music/list";
    }

    // 管理员功能：编辑音乐页面
    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Integer id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return "redirect:/login";
        }
        Music music = musicService.getById(id);
        model.addAttribute("music", music);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "music_edit";
    }

    @PostMapping("/edit")
    public String edit(Music music, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return "redirect:/login";
        }
        musicService.updateMusic(music);
        return "redirect:/music/list";
    }

    // 管理员功能：删除音乐
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return "redirect:/login";
        }
        musicService.deleteMusic(id);
        return "redirect:/music/list";
    }
}