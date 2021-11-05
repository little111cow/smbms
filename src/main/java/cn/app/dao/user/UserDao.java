package cn.app.dao.user;

import cn.app.entity.User;
import java.sql.Connection;
import java.util.List;

public interface UserDao {
    // 获得要登录用户
    public User getLoginUser(String userCode,String password);

    //修改用户密码
    public boolean modifyPassword(int id,String newpassword);

    /**
     * 通过条件查询-userList
     * @param connection
     * @param userName
     * @param userRole
     * @return
     * @throws Exception
     */
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize)throws Exception;
    /**
     * 通过条件查询-用户表记录数
     * @param connection
     * @param userName
     * @param userRole
     * @return
     * @throws Exception
     */
    public int getUserCount(Connection connection, String userName, int userRole)throws Exception;

    //添加用户接口
    public boolean addUser(Connection connection,User user);

    //后台验证用户名是否存在
    public boolean isUCexist(String userCode);

    //根据id删除用户
    public boolean deluser(Connection connection,int id);
}
