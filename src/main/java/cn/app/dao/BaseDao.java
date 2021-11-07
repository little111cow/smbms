package cn.app.dao;

import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * 操作数据库公共类
 */
public class BaseDao {
    private static String driver;  //驱动
    private static String username;  //用户名
    private static String password;  //密码
    private static String url ;  //待连接的数据库

    //静态代码块加载数据库连接所需的配置文件
    static {
        //通过类加载器加载数据库配置文件
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
        //配置文件读取
        Properties properties = new Properties();
        try {
            properties.load(is);
        }catch (Exception e){
            e.printStackTrace();
        }
        url = properties.getProperty("url");
        driver = properties.getProperty("driver");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
    }

    //获得数据库连接
    public static Connection getConnect(){
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url,username,password);
        }catch (Exception e){
            e.printStackTrace();
        }
        return conn;
    }

    //查询公共方法
    @SuppressWarnings("all")
    public static ResultSet execute(Connection conn, PreparedStatement preparedStatement,String sql,Object[] params,ResultSet rs){
        try {
            preparedStatement = conn.prepareStatement(sql);
            if (params!=null) {  //这里需要判断是否有参数
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i+1, params[i]);
                }
            }
             rs = preparedStatement.executeQuery();
        }catch (Exception e){
            e.printStackTrace();
        }
        return rs;
    }

    //增删改公共方法
    @SuppressWarnings("all")
    public static int execute(Connection conn, PreparedStatement preparedStatement,String sql,Object[] params){
        int rs=0;  //返回受影响的行数
        try {
            preparedStatement = conn.prepareStatement(sql);  //预编译
            if(params!=null) {
                for (int i = 1; i <= params.length; i++) {
                    preparedStatement.setObject(i, params[i - 1]);  //设置参数
                }
            }
            rs = preparedStatement.executeUpdate();  //执行增删改
        }catch (Exception e){
            e.printStackTrace();
        }
        return rs;
    }


    //统一关闭资源
    @SuppressWarnings("all")
    public static boolean closeResources(Connection conn, PreparedStatement preparedStatement,ResultSet rs){
        boolean flag=true;
        //关闭结果集
        if(rs!=null){
            try{
                rs.close();
                rs = null;  //便于GC
            }catch (Exception e){
                flag = false;
                e.printStackTrace();
            }
        }
        //关闭sql执行对象
        if(preparedStatement!=null){
            try{
                preparedStatement.close();
                preparedStatement = null; //便于GC
            }catch (Exception e){
                flag = false;
                e.printStackTrace();
            }
        }
        //关闭数据库连接
        if(conn!=null){
            try{
                conn.close();
                conn = null;
            }catch (Exception e){
                flag = false;
                e.printStackTrace();
            }
        }
        return flag;
    }

    //("单元测试代码")
//    @Test
//    public void test() throws Exception{
//        String sql = "select * from smbms_user;";
//        Connection conn = getConnect();
//        PreparedStatement preparedStatement = null;
//        ResultSet rs = null;
//        ResultSet result=execute(conn,preparedStatement,sql,null, null);
//        while(result.next()){
//            System.out.println(result.getObject(3));
//        }
//        closeResources(conn,preparedStatement,rs);
//    }

}
