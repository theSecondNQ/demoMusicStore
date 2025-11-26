package com.music_store.Controller;

import com.music_store.Entity.Playlist;
import com.music_store.Entity.User;
import com.music_store.Service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/playlist")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @GetMapping("/list")
    public String list(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        List<Playlist> playlists = playlistService.listByUser(user.getId());
        model.addAttribute("playlists", playlists);
        return "playlist_list";
    }

    @GetMapping("/create")
    public String createPage(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        return "playlist_create";
    }

    @PostMapping("/create")
    public String create(String name, String description, @RequestParam(defaultValue = "private") String visibility,
                         HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        Integer id = playlistService.create(user.getId(), name, description, visibility);
        return "redirect:/playlist/view/" + id;
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Integer id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        Playlist p = playlistService.getById(id);
        if (p == null) return "redirect:/playlist/list";
        // 权限：私有仅作者可见；共享需要登录
        if ("private".equals(p.getVisibility())) {
            if (user == null || !user.getId().equals(p.getUserId())) {
                return "redirect:/login";
            }
        } else { // shared
            if (user == null) return "redirect:/login";
        }
        model.addAttribute("playlist", p);
        return "playlist_view";
    }

    @PostMapping("/addTrack")
    public String addTrack(Integer playlistId, Integer musicId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        Playlist p = playlistService.getById(playlistId);
        if (p == null || !user.getId().equals(p.getUserId())) return "redirect:/playlist/list";
        playlistService.addTrack(playlistId, musicId);
        return "redirect:/playlist/view/" + playlistId;
    }

    @PostMapping("/removeTrack")
    public String removeTrack(Integer playlistId, Integer musicId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        Playlist p = playlistService.getById(playlistId);
        if (p == null || !user.getId().equals(p.getUserId())) return "redirect:/playlist/list";
        playlistService.removeTrack(playlistId, musicId);
        return "redirect:/playlist/view/" + playlistId;
    }

    @PostMapping("/visibility/{id}")
    public String changeVisibility(@PathVariable Integer id, @RequestParam String visibility, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        Playlist p = playlistService.getById(id);
        if (p == null || !user.getId().equals(p.getUserId())) return "redirect:/playlist/list";
        p.setVisibility("shared".equals(visibility) ? "shared" : "private");
        playlistService.update(p);
        return "redirect:/playlist/view/" + id;
    }
}