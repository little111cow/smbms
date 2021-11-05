package cn.app.dao.userRole;

import cn.app.entity.Role;

import java.sql.Connection;
import java.util.List;

public interface userRoleDao {
    public List<Role> getRoleList(Connection connection)throws Exception;
}
