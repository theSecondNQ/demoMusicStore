package com.music_store.Service;

import com.music_store.Entity.SiteInfo;
import com.music_store.Dao.SiteInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SiteInfoService {

    @Autowired
    private SiteInfoDao siteInfoDao;

    public SiteInfo getSiteInfo() {
        SiteInfo siteInfo = siteInfoDao.select();
        if (siteInfo == null) {
            // 如果数据库中没有记录，创建一个默认的
            siteInfo = createDefaultSiteInfo();
            // 保存默认信息到数据库
            siteInfoDao.insert(siteInfo);
        }
        return siteInfo;
    }

    public boolean saveSiteInfo(SiteInfo siteInfo) {
        try {
            // 先检查是否存在记录
            SiteInfo existing = siteInfoDao.select();
            if (existing != null) {
                siteInfo.setId(existing.getId());
                return siteInfoDao.update(siteInfo) > 0;
            } else {
                return siteInfoDao.insert(siteInfo) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private SiteInfo createDefaultSiteInfo() {
        SiteInfo siteInfo = new SiteInfo();
        siteInfo.setSiteName("音乐商店");
        siteInfo.setCustomerServicePhone("400-123-4567");
        siteInfo.setZipCode("100000");
        siteInfo.setAddress("北京市朝阳区xxx街道");
        siteInfo.setEmail("service@musicstore.com");
        siteInfo.setAboutUs("专业的在线音乐销售平台");
        siteInfo.setCopyright("© 2025 音乐商店 版权所有");
        return siteInfo;
    }
}