package cn.app.service.userRole;

import cn.app.dao.BaseDao;
import cn.app.dao.userRole.userRoleDaoImpl;
import cn.app.entity.Role;
import cn.app.dao.userRole.userRoleDao;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;

public class userRoleServiceImpl implements userRoleService {
    private userRoleDao userRoleDao;  //调用Dao层

    public userRoleServiceImpl() {
        this.userRoleDao = new userRoleDaoImpl();
    }

    @Override
    public List<Role> getRoleList() {
        List<Role> list = null;
        Connection connection = BaseDao.getConnect();
        if(connection!=null){
            try{
                list = userRoleDao.getRoleList(connection);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(connection,null,null);
            }
        }
        return list;
    }

//    @Test
//    public void test(){
//        List<Role> list = getRoleList();
//        for(Role r:list){
//            System.out.println(r.getRoleName());
//        }
//    }
}
