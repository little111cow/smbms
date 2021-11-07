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
    //获取用户列表
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws Exception {
        // TODO Auto-generated method stub
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<User> userList = new ArrayList<>();
        if(connection != null){
            StringBuffer sql = new StringBuffer();  //用来操作动态sql语句
            sql.append("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id");//默认sql语句
            List<Object> list = new ArrayList<>();
            if(!StringUtils.isNullOrEmpty(userName)){  //前端限定用户名查询条件
                sql.append(" and u.userName like ?");
                list.add("%"+userName+"%");
            }
            if(userRole > 0){ //前端限定用户角色查询条件
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }
            sql.append(" order by creationDate DESC limit ?,?");  //按照创建时间降序
            currentPageNo = (currentPageNo-1)*pageSize;
            list.add(currentPageNo);  //当前页
            list.add(pageSize);  //页面大小

            Object[] params = list.toArray();  //参数
            System.out.println("sql ----> " + sql.toString());
            rs = BaseDao.execute(connection, pstm,sql.toString(),params,rs);  //执行查询
            while(rs.next()){
                //封装用户
                User _user = new User();
                _user.setId(rs.getInt("id"));
                _user.setUserCode(rs.getString("userCode"));
                _user.setUserName(rs.getString("userName"));
                _user.setGender(rs.getInt("gender"));
                _user.setBirthday(rs.getDate("birthday"));
                _user.setPhone(rs.getString("phone"));
                _user.setUserRole(rs.getInt("userRole"));
                _user.setUserRoleName(rs.getString("userRoleName"));
                _user.setAge(_user.getAge());  //根据生日来计算的，并没有存放在数据库中
                userList.add(_user);
            }
            BaseDao.closeResources(null, pstm, rs);  //关闭资源
        }
        return userList;
    }

    @Override
    //根据id删除用户
    public boolean deluser(Connection connection, int id) {
        String sql = "delete from  smbms_user where id=?;";
        boolean flag = true;
        PreparedStatement pstm = null;
        Object[] params = {id};  //参数
        if(connection!=null){
            try{
                if(BaseDao.execute(connection,pstm,sql,params)<=0){  //删除后受影响的行数小于等于0说明没删除成功
                    flag = false;
                }
            }catch (Exception e){
                flag = false;
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(null,pstm,null);   //关闭资源
            }
        }
        return flag;
    }

    @Override
    //后台判断用户名是否存在
    public boolean isUCexist(String userCode) {
        boolean flag = false;
        String sql = "select * from smbms_user where userCode=? ";
        Connection connection = BaseDao.getConnect();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Object[] params = {userCode};
        if(connection!=null){
            try{
                rs = BaseDao.execute(connection,pstm,sql,params,rs);  //执行
                if(rs.next()){
                    //结果集中存在和传递来的userCode相同的用户名说明用户名存在
                    if(rs.getString("userCode").equals(userCode)&&!userCode.equals("")){
                        flag = true;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(connection,pstm,rs);  //关闭资源
            }
        }
        return flag;
    }

    @Override
    //添加用户
    public boolean addUser(Connection connection, User user) {
        boolean flag = true;
        int rs;
        String sql = "insert into smbms_user(userCode,userName,userPassword,gender,birthday,phone,address,userRole,createdBy,creationDate,modifyBy,modifyDate) values(?,?,?,?,?,?,?,?,?,?,?,?);";
        PreparedStatement pstm = null;
        if(connection!=null){
            try {
                pstm = connection.prepareStatement(sql);  //预编译
                //根据要添加的用户设置参数
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

               rs = pstm.executeUpdate();  //执行添加语句
               if(rs<=0){  //受影响的行数小于等于0说明没有添加成功
                   flag = false;
               }
            }catch (Exception e){
                flag = false;
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(null,pstm,null);  //关闭资源
            }
        }
        return flag;
    }

    @Override
    //根据用户id查看用户信息
    public User view(Connection connection, int id) {
        //查询两个表获得用户信息和用户角色信息
        String sql = "select *,(select roleName as userRoleName from smbms_role where id=(select userRole from smbms_user where id=?)) as userRoleName from smbms_user where id=?;";
        PreparedStatement pstm = null;
        Object[] params = {id,id};
        ResultSet rs = null;
        User user = null;
        if(connection!=null){
            try{
                rs = BaseDao.execute(connection,pstm,sql,params,rs);  //执行查询
                if(rs.next()){
                    //封装前端需要的用户信息
                    user = new User();
                    user.setUserName(rs.getString("userName"));
                    user.setUserCode(rs.getString("userCode"));
                    user.setGender(rs.getInt("gender"));
                    user.setBirthday(rs.getDate("birthday"));
                    user.setPhone(rs.getString("phone"));
                    user.setAddress(rs.getString("address"));
                    user.setUserRoleName(rs.getString("userRoleName"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(null,pstm,rs);
            }
        }
        return user;
    }

    @Override
    //获得用户数量，便于分页
    public int getUserCount(Connection connection, String userName, int userRole) throws Exception {
        // TODO Auto-generated method stub
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int count = 0;
        if(connection != null){
            StringBuffer sql = new StringBuffer();  //可变sql
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
            Object[] params = list.toArray();  //参数
            System.out.println("sql ----> " + sql.toString());
            rs = BaseDao.execute(connection, pstm,sql.toString(),params,rs);  //执行查询
            if(rs.next()){
                count = rs.getInt("count");  //sql语句中使用了别名
            }
            BaseDao.closeResources(null, pstm, rs);  //关闭资源
        }
        return count;
    }
//    @Test
//    public void test(){
//        User user = getLoginUser( "admin","1234567");
//        System.out.println(user.getUserPassword());
//    }
}
