package cn.app.service.user;

import cn.app.entity.User;

import java.sql.Connection;
import java.util.List;

public interface UserService {
    public User login(String userCode,String password);  //获得登录的用户

    public boolean modifyPassword(int id,String newpassword);  //修改用户密码

    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize);  //根据查询用户名和用户角色获得用户列表

    public int getUserCount(String userName, int userRole);  //获得用户总数

    public boolean addUser(User user);  //添加新用户

    public boolean isUCexist(String userCode);  //后台验证用户名是否存在

    public boolean deluser(int id);  //根据id删除用户

    public User view(int id);  //根据用户id查看用户信息
}
