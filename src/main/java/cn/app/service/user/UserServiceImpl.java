package cn.app.service.user;

import cn.app.dao.BaseDao;
import cn.app.dao.user.UserDao;
import cn.app.dao.user.UserDaoImpl;
import cn.app.entity.User;
import org.junit.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public boolean isUCexist(String userCode) {
        return userDao.isUCexist(userCode);
    }
//    @Test
//    public void test(){
//        System.out.println(isUCexist("admin"));
//    }

    @Override
    public boolean addUser(User user) {
        Connection connection = BaseDao.getConnect();
        boolean flag = false;
        if(connection != null){
            try{
                connection.setAutoCommit(false);  //开启事务
                flag = userDao.addUser(connection,user);
                connection.commit();  //提交事物
            }catch (Exception e){
                try {
                    connection.rollback();  //回滚事物
                }catch (Exception e1){
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(connection,null,null);
            }
        }
        return flag;
    }

    @Override
    public int getUserCount(String userName, int userRole) {
        int count = 0;
        Connection connection = BaseDao.getConnect();
        if(connection!=null){
            try{
                count = userDao.getUserCount(connection,userName,userRole);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(connection,null,null);
            }
        }
        return count;
    }

    @Override
    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize) {
        List<User> list = null;
        Connection connection = BaseDao.getConnect();
        if(connection!=null){
            try{
                list = userDao.getUserList(connection,userName,userRole,currentPageNo,pageSize);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(connection,null,null);
            }
        }
        return list;
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
