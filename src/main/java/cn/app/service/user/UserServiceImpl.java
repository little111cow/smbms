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
    //根据用户id查看用户信息
    public User view(int id) {
        User user = null;
        Connection connection = BaseDao.getConnect();
        if(connection!=null){
            try{
                connection.setAutoCommit(false);  //开启事务
                user = userDao.view(connection,id);  //调用dao层获得查看的用户
                connection.commit();  //提交事务
            }catch (Exception e){
                try {
                    connection.rollback();  //回滚事务
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }finally {
                BaseDao.closeResources(connection,null,null);  //关闭资源
            }
        }
        return user;
    }

    @Override
    //根据id删除用户
    public boolean deluser(int id) {
        boolean flag = true;
        Connection connection = BaseDao.getConnect();//数据库连接
        if(connection!=null){
            try{
                connection.setAutoCommit(false);  //开启事务
                flag = userDao.deluser(connection,id);
                connection.commit();  //提交事务
            }catch (Exception e){
                try {
                    connection.rollback();  //回滚事务
                }catch (Exception e1){
                    e1.printStackTrace();
                }
                flag = false;
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(connection,null,null);//关闭事务
            }
        }
        return flag;
    }

    @Override
    //后台判断用户名是否存在
    public boolean isUCexist(String userCode) {
        return userDao.isUCexist(userCode);
    }
//    @Test
//    public void test(){
//        System.out.println(isUCexist("admin"));
//    }

    @Override
    //添加用户
    public boolean addUser(User user) {
        Connection connection = BaseDao.getConnect();//数据库连接
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
                BaseDao.closeResources(connection,null,null);  //关闭资源
            }
        }
        return flag;
    }

    @Override
    //获得用户总数
    public int getUserCount(String userName, int userRole) {
        int count = 0;
        Connection connection = BaseDao.getConnect();//数据库连接
        if(connection!=null){
            try{
                count = userDao.getUserCount(connection,userName,userRole);  //获得用户总数
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(connection,null,null);  //关闭资源
            }
        }
        return count;
    }

    @Override
    //获得用户列表
    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize) {
        List<User> list = null;
        Connection connection = BaseDao.getConnect(); //获得数据库连接
        if(connection!=null){
            try{
                list = userDao.getUserList(connection,userName,userRole,currentPageNo,pageSize);//获得用户列表
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(connection,null,null);//关闭资源
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
