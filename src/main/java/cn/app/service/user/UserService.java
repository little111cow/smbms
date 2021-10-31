package cn.app.service.user;

import cn.app.entity.User;

public interface UserService {
    public User login(String userCode,String password);  //获得登录的用户

    public boolean modifyPassword(int id,String newpassword);  //修改用户密码
}
