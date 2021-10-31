package cn.app.entity;

import java.util.Date;
public class Role {
    @field("id")
    private Integer id;

    @field("角色编码")
    private String roleCode;

    @field("角色名称")
    private String roleName;

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
    public String getRoleCode() {
        return roleCode;
    }
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
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
