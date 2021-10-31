package cn.app.entity;

import java.util.Date;

public class Provider {
    @field("id")
    private Integer id;

    @field("供应商编码")
    private String proCode;

    @field("供应商名称")
    private String proName;

    @field("供应商描述")
    private String proDesc;

    @field("供应商联系人")
    private String proContact;

    @field("供应商电话")
    private String proPhone;

    @field("供应商地址")
    private String proAddress;

    @field("供应商传真")
    private String proFax;

    @field("创建者")
    private Integer createdBy;

    @field("创建时间")
    private Date creationDate;

    @field("更新者")
    private Integer modifyBy;

    @field("更新时间")
    private Date modifyDate;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getProCode() {
        return proCode;
    }
    public void setProCode(String proCode) {
        this.proCode = proCode;
    }
    public String getProName() {
        return proName;
    }
    public void setProName(String proName) {
        this.proName = proName;
    }
    public String getProDesc() {
        return proDesc;
    }
    public void setProDesc(String proDesc) {
        this.proDesc = proDesc;
    }
    public String getProContact() {
        return proContact;
    }
    public void setProContact(String proContact) {
        this.proContact = proContact;
    }
    public String getProPhone() {
        return proPhone;
    }
    public void setProPhone(String proPhone) {
        this.proPhone = proPhone;
    }
    public String getProAddress() {
        return proAddress;
    }
    public void setProAddress(String proAddress) {
        this.proAddress = proAddress;
    }
    public String getProFax() {
        return proFax;
    }
    public void setProFax(String proFax) {
        this.proFax = proFax;
    }
    public Integer getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }
    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    public Integer getModifyBy() {
        return modifyBy;
    }
    public void setModifyBy(Integer modifyBy) {
        this.modifyBy = modifyBy;
    }
    public Date getModifyDate() {
        return modifyDate;
    }
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }


}
