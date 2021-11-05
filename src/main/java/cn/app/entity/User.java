package cn.app.entity;

import java.util.Date;

public class User {
    @field("id")
    private Integer id;

    @field("用户编码")
    private String userCode;

    @field("用户名称")
    private String userName;

    @field("用户密码")
    private String userPassword;

    @field("性别")
    private Integer gender;

    @field("出生日期")
    private Date birthday;

    @field("电话")
    private String phone;

    @field("地址")
    private String address;

    @field("用户角色")
    private Integer userRole;

    @field("创建者")
    private Integer createdBy;

    @field("创建时间")
    private Date creationDate;

    @field("更新者")
    private Integer modifyBy;

    @field("更新时间")
    private Date modifyDate;

    @field("年龄")
    private Integer age;//年龄

    @field("用户角色名称")
    private String userRoleName;


    public String getUserRoleName() {
        return userRoleName;
    }
    public void setUserRoleName(String userRoleName) {
        this.userRoleName = userRoleName;
    }
    public Integer getAge() {
		/*long time = System.currentTimeMillis()-birthday.getTime();
		Integer age = Long.valueOf(time/365/24/60/60/1000).IntegerValue();*/
		if(birthday==null){  //解决空指针异常
		    age = 0;
        }else {
            Date date = new Date();
            Integer age = date.getYear() - birthday.getYear();
        }
        return age;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getUserCode() {
        return userCode;
    }
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserPassword() {
        return userPassword;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    public Integer getGender() {
        return gender;
    }
    public void setGender(Integer gender) {
        this.gender = gender;
    }
    public Date getBirthday() {
        return birthday;
    }
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public Integer getUserRole() {
        return userRole;
    }
    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
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

/**
 * 属性字段的作用注解
 * */
 @interface field{
    String value() default "";
}