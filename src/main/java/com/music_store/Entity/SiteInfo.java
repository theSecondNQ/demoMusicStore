package com.music_store.Entity;

public class SiteInfo {
    private Integer id;
    private String siteName;
    private String customerServicePhone;
    private String zipCode;
    private String address;
    private String email;
    private String aboutUs;
    private String copyright;

    public SiteInfo() {}

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