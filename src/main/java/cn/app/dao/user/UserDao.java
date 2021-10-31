package cn.app.dao.user;

import cn.app.entity.User;
import java.sql.Connection;

public interface UserDao {
    // 获得要登录用户
    public User getLoginUser(String userCode,String password);

    //修改用户密码
    public boolean modifyPassword(int id,String newpassword);
}
