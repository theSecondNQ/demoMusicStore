package com.music_store.Entity;

/**
 * 网站信息实体类
 * 存储音乐商店网站的基本信息和联系方式
 */
public class SiteInfo {

    /** 网站信息ID **/
    private Integer id;

    /** 网站名称 **/
    private String siteName;

    /** 客服电话 **/
    private String customerServicePhone;

    /** 邮政编码 **/
    private String zipCode;

    /** 公司地址 **/
    private String address;

    /** 联系邮箱 **/
    private String email;

    /** 关于我们 **/
    private String aboutUs;

    /** 版权信息 **/
    private String copyright;


    //无参构造函数
    public SiteInfo() {}

    //Getter和Setter

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getSiteName() { return siteName; }
    public void setSiteName(String siteName) { this.siteName = siteName; }

    public String getCustomerServicePhone() { return customerServicePhone; }
    public void setCustomerServicePhone(String customerServicePhone) { this.customerServicePhone = customerServicePhone; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAboutUs() { return aboutUs; }
    public void setAboutUs(String aboutUs) { this.aboutUs = aboutUs; }

    public String getCopyright() { return copyright; }
    public void setCopyright(String copyright) { this.copyright = copyright; }
}