package cn.app.service.user;

import cn.app.dao.user.UserDao;
import cn.app.dao.user.UserDaoImpl;
import cn.app.entity.User;
import org.junit.Test;

public class UserServiceImpl implements UserService {
    private UserDao userDao;   //调用Dao层
    public UserServiceImpl(){
        userDao = new UserDaoImpl();  //构造器中实例化
    }
    //返回要修改的用户
    @Override
    public User login(String userCode, String password) {
         return userDao.getLoginUser(userCode,password);
    }

    //密码修改
    @Override
    public boolean modifyPassword(int id, String newpassword) {
        return userDao.modifyPassword(id,newpassword);
    }

    //    @Test
//    public void test(){
//        User user = login("admin","1234567");
//        System.out.println(user.getUserPassword());
//    }
}
