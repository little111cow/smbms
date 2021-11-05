package cn.app.service.userRole;

import cn.app.entity.Role;

import java.util.List;

public interface userRoleService {
    //获得用户角色列表
    public List<Role> getRoleList();
}
