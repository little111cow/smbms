package cn.app.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Bill {
    @field("id ")
    private Integer id;

    @field("账单编码")
    private String billCode;

    @field("商品名称")
    private String productName;

    @field("商品描述")
    private String productDesc;

    @field("商品单位")
    private String productUnit;

    @field("商品数量")
    private BigDecimal productCount;

    @field("总金额")
    private BigDecimal totalPrice;

    @field("是否支付")
    private Integer isPayment;

    @field("供应商ID")
    private Integer providerId;

    @field("创建者")
    private Integer createdBy;

    @field("创建时间")
    private Date creationDate;

    @field("更新者")
    private Integer modifyBy;

    @field("更新时间")
    private Date modifyDate;

    @field("供应商名称")
    private String providerName;


    public String getProviderName() {
        return providerName;
    }
    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getProductDesc() {
        return productDesc;
    }
    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }
    public String getProductUnit() {
        return productUnit;
    }
    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }
    public BigDecimal getProductCount() {
        return productCount;
    }
    public void setProductCount(BigDecimal productCount) {
        this.productCount = productCount;
    }
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    public Integer getIsPayment() {
        return isPayment;
    }
    public void setIsPayment(Integer isPayment) {
        this.isPayment = isPayment;
    }

    public Integer getProviderId() {
        return providerId;
    }
    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
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
