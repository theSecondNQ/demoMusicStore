package com.music_store.Dao;

import com.music_store.Entity.SiteInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SiteInfoDao {

    @Select("SELECT * FROM site_info LIMIT 1")
    SiteInfo select();

    @Insert("INSERT INTO site_info(site_name, customer_service_phone, zip_code, address, email, about_us, copyright) " +
            "VALUES(#{siteName}, #{customerServicePhone}, #{zipCode}, #{address}, #{email}, #{aboutUs}, #{copyright})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SiteInfo siteInfo);

    @Update("UPDATE site_info SET site_name=#{siteName}, customer_service_phone=#{customerServicePhone}, " +
            "zip_code=#{zipCode}, address=#{address}, email=#{email}, about_us=#{aboutUs}, copyright=#{copyright} " +
            "WHERE id=#{id}")
    int update(SiteInfo siteInfo);

    @Select("SELECT COUNT(*) FROM site_info")
    int count();
}