package com.music_store.Config;

import com.music_store.Entity.SiteInfo;
import com.music_store.Service.SiteInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SiteInfoInterceptor implements HandlerInterceptor {

    @Autowired
    private SiteInfoService siteInfoService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SiteInfo siteInfo = siteInfoService.getSiteInfo();
        request.setAttribute("siteInfo", siteInfo);
        return true;
    }
}