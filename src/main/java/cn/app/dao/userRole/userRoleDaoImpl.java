package cn.app.dao.userRole;

import cn.app.dao.BaseDao;
import cn.app.entity.Role;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class userRoleDaoImpl implements userRoleDao {
    @Override
    //获得用户角色列表
    public List<Role> getRoleList(Connection connection) throws Exception {
        String sql = "select * from smbms_role;";
        List<Role> list = new ArrayList<>();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Role role ;
        if(connection!=null){
            pstm = connection.prepareStatement(sql); //预编译
            try {
                rs = BaseDao.execute(connection, pstm, sql, null, rs);  //执行查询
                while(rs.next()){
                    //封装
                    role = new Role();
                    role.setId(rs.getInt("id"));
                    role.setCreatedBy(rs.getInt("createdBy"));
                    role.setCreationDate(rs.getTimestamp("creationDate"));
                    role.setModifyBy(rs.getInt("modifyBy"));
                    role.setModifyDate(rs.getTimestamp("modifyDate"));
                    role.setRoleCode(rs.getString("roleCode"));
                    role.setRoleName(rs.getString("roleName"));
                    list.add(role);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(null,pstm,rs);  //关闭资源
            }
        }
        return list;
    }
}
