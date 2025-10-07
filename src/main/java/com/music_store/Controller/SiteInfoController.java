package com.music_store.Controller;

import com.music_store.Entity.SiteInfo;
import com.music_store.Entity.User;
import com.music_store.Service.SiteInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/site")
public class SiteInfoController {

    @Autowired
    private SiteInfoService siteInfoService;

    @GetMapping("/info")
    public String siteInfoPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return "redirect:/login";
        }

        SiteInfo siteInfo = siteInfoService.getSiteInfo();
        model.addAttribute("siteInfo", siteInfo);
        return "admin/site_info";
    }

    @PostMapping("/save")
    public String saveSiteInfo(SiteInfo siteInfo, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return "redirect:/login";
        }

        siteInfoService.saveSiteInfo(siteInfo);
        return "redirect:/admin/site/info?success=1";
    }
}