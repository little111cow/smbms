package cn.app.dao.user;

import cn.app.dao.BaseDao;
import cn.app.entity.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDaoImpl implements UserDao {
    //查询要登录的用户
    @Override
    public User getLoginUser(String userCode,String password) {
        String sql = "select * from smbms_user where userCode=? and userPassword=?;";
        Connection conn = BaseDao.getConnect();
        PreparedStatement preparedStatement = null;
        Object[] params = new Object[]{userCode,password};
        ResultSet rs = null;
        User user = null;
        if (conn != null) { //连接不空才执行sql代码
            rs = BaseDao.execute(conn, preparedStatement, sql, params, rs);
            if (rs != null) {  //结果集不空时再操作
                try {
                    if(rs.next()) {  //遍历结果集设置为一个User
                        user = new User();
                        user.setId(rs.getInt("id"));
                        user.setUserCode(rs.getString("userCode"));
                        user.setUserName(rs.getString("userName"));
                        user.setUserPassword(rs.getString("userPassword"));
                        user.setGender(rs.getInt("gender"));
                        user.setBirthday(rs.getDate("birthday"));
                        user.setPhone(rs.getString("phone"));
                        user.setAddress(rs.getString("address"));
                        user.setUserRole(rs.getInt("userRole"));
                        user.setCreatedBy(rs.getInt("createdBy"));
                        user.setCreationDate(rs.getTimestamp("creationDate"));
                        user.setModifyBy(rs.getInt("modifyBy"));
                        user.setModifyDate(rs.getTimestamp("modifyDate"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try{}finally {
                BaseDao.closeResources(conn,preparedStatement,rs);  //关闭资源
            }

        }
        return user;
    }

    //修改用户密码
    @Override
    public boolean modifyPassword(int id, String newpassword) {
        String sql = "update smbms_user set userPassword=? where id=?;";  //修改密码的mysql语句
        boolean flag = true;  //修改是否成功的标志
        Connection conn = BaseDao.getConnect();  //数据库连接
        PreparedStatement preparedStatement = null;
        Object[] params ={newpassword,id};
        //连接不为空则根据用户id进行密码修改
        if(conn!=null) {
            try {
                BaseDao.execute(conn, preparedStatement, sql, params);
            } catch (Exception e) {
                flag = false;
                e.printStackTrace();
            } finally {
                BaseDao.closeResources(conn, preparedStatement, null);  //关闭资源
            }
        }
        return flag;
    }

    //    @Test
//    public void test(){
//        User user = getLoginUser( "admin","1234567");
//        System.out.println(user.getUserPassword());
//    }
}
