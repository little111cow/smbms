package cn.app.dao.user;

import cn.app.dao.BaseDao;
import cn.app.entity.User;
import com.mysql.jdbc.StringUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Override
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws Exception {
        // TODO Auto-generated method stub
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<User> userList = new ArrayList<>();
        if(connection != null){
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id");
            List<Object> list = new ArrayList<>();
            if(!StringUtils.isNullOrEmpty(userName)){
                sql.append(" and u.userName like ?");
                list.add("%"+userName+"%");
            }
            if(userRole > 0){
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }
            sql.append(" order by creationDate DESC limit ?,?");
            currentPageNo = (currentPageNo-1)*pageSize;
            list.add(currentPageNo);
            list.add(pageSize);

            Object[] params = list.toArray();
            System.out.println("sql ----> " + sql.toString());
            rs = BaseDao.execute(connection, pstm,sql.toString(),params,rs);
            while(rs.next()){
                User _user = new User();
                _user.setId(rs.getInt("id"));
                _user.setUserCode(rs.getString("userCode"));
                _user.setUserName(rs.getString("userName"));
                _user.setGender(rs.getInt("gender"));
                _user.setBirthday(rs.getDate("birthday"));
                _user.setPhone(rs.getString("phone"));
                _user.setUserRole(rs.getInt("userRole"));
                _user.setUserRoleName(rs.getString("userRoleName"));
                _user.setAge(_user.getAge());
                userList.add(_user);
            }
            BaseDao.closeResources(null, pstm, rs);
        }
        return userList;
    }

    @Override
    public boolean deluser(Connection connection, int id) {
        String sql = "delete from  smbms_user where id=?;";
        boolean flag = true;
        PreparedStatement pstm = null;
        Object[] params = {id};
        if(connection!=null){
            try{
                pstm = connection.prepareStatement(sql);
                if(BaseDao.execute(connection,pstm,sql,params)<=0){
                    flag = false;
                }
            }catch (Exception e){
                flag = false;
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(null,pstm,null);
            }
        }
        return flag;
    }

    @Override
    public boolean isUCexist(String userCode) {
        boolean flag = false;
        String sql = "select * from smbms_user where userCode=? ";
        Connection connection = BaseDao.getConnect();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Object[] params = {userCode};
        if(connection!=null){
            try{
                pstm = connection.prepareStatement(sql);
                rs = BaseDao.execute(connection,pstm,sql,params,rs);
                if(rs.next()){
                    if(rs.getString("userCode").equals(userCode)&&!userCode.equals("")){
                        flag = true;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(connection,pstm,rs);
            }
        }
        return flag;
    }

    @Override
    public boolean addUser(Connection connection, User user) {
        boolean flag = true;
        int rs;
        String sql = "insert into smbms_user(userCode,userName,userPassword,gender,birthday,phone,address,userRole,createdBy,creationDate,modifyBy,modifyDate) values(?,?,?,?,?,?,?,?,?,?,?,?);";
        PreparedStatement pstm = null;
        if(connection!=null){
            try {
                pstm = connection.prepareStatement(sql);
                pstm.setObject(1,user.getUserCode());
                pstm.setObject(2,user.getUserName());
                pstm.setObject(3,user.getUserPassword());
                pstm.setObject(4,user.getGender());
                pstm.setObject(5,user.getBirthday());
                pstm.setObject(6,user.getPhone());
                pstm.setObject(7,user.getAddress());
                pstm.setObject(8,user.getUserRole());
                pstm.setObject(9,user.getCreatedBy());
                pstm.setObject(10,user.getCreationDate());
                pstm.setObject(11,user.getModifyBy());
                pstm.setObject(12,user.getModifyDate());

               rs = pstm.executeUpdate();
               if(rs<=0){
                   flag = false;
               }
            }catch (Exception e){
                flag = false;
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(null,pstm,null);
            }
        }
        return flag;
    }

    @Override
    public int getUserCount(Connection connection, String userName, int userRole) throws Exception {
        // TODO Auto-generated method stub
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int count = 0;
        if(connection != null){
            StringBuffer sql = new StringBuffer();
            sql.append("select count(1) as count from smbms_user u,smbms_role r where u.userRole = r.id");
            List<Object> list = new ArrayList<Object>();
            if(!StringUtils.isNullOrEmpty(userName)){
                sql.append(" and u.userName like ?");
                list.add("%"+userName+"%");
            }
            if(userRole > 0){
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }
            Object[] params = list.toArray();
            System.out.println("sql ----> " + sql.toString());
            rs = BaseDao.execute(connection, pstm,sql.toString(),params,rs);
            if(rs.next()){
                count = rs.getInt("count");
            }
            BaseDao.closeResources(null, pstm, rs);
        }
        return count;
    }
//    @Test
//    public void test(){
//        User user = getLoginUser( "admin","1234567");
//        System.out.println(user.getUserPassword());
//    }
}
