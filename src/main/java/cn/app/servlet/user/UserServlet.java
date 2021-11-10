package cn.app.servlet.user;

import cn.app.entity.Role;
import cn.app.entity.User;
import cn.app.service.user.UserService;
import cn.app.service.user.UserServiceImpl;
import cn.app.service.userRole.userRoleService;
import cn.app.service.userRole.userRoleServiceImpl;
import cn.app.util.Constants;
import cn.app.util.PageSupport;
import com.alibaba.fastjson.JSONArray;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("all")
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");  //获取前端请求的方法
        if (method != null) {
            if (method.equals("savepwd")) {
                this.updatePassword(req, resp);  //前端请求的方法为savepwd就更新密码
            } else if (method.equals("pwdmodify")) {
                this.pwdmodify(req, resp);
            } else if (method.equals("query")) {
                this.query(req, resp);  //用户管理查询方法
            } else if (method.equals("add")) {
                this.add(req, resp);  //用户管理添加用户方法
            } else if (method.equals("getrolelist")) {
                this.getrolelist(req, resp);  //获取用户角色列表方法
            }else if(method.equals("ucexist")){
                this.ucexist(req, resp);  //后台判断用户名是否存在方法
            }else if(method.equals("deluser")){
                this.deluser(req, resp);  //删除用户方法
            }else if(method.equals("view")){
                this.view(req, resp);  //用户信息查看方法
            }else if(method.equals("modify")){
                this.modify(req,resp);  //用户信息修改方法
            }else if(method.equals("modifyexe")){
                this.modifyexe(req,resp);  //修改用户提交保存验证
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    //提取方法实现servlet复用,修改用户密码方法
    public void updatePassword(HttpServletRequest req, HttpServletResponse resp){
        String newpassword = req.getParameter("newpassword");  //从前端获取新密码
        Object obj = req.getSession().getAttribute(Constants.USER_SESSION);  //取出session中存放的用户
        int id;
        boolean flag;  //修改是否成功的标志
        if(obj!=null&&newpassword!=null){  //session不为空，新密码不为空
            id = ((User)obj).getId();
            UserService userService = new UserServiceImpl();
            flag = userService.modifyPassword(id,newpassword);  //掉有业务层修改密码
            if(flag){//修改成功
                req.getSession().setAttribute(Constants.MESSAGE,"新密码修改成功，请重新登录！");
                req.getSession().setAttribute(Constants.USER_SESSION,null);  //移除session，重新登录
            }else{//修改失败
                req.getSession().setAttribute(Constants.MESSAGE,"新密码修改失败！");//前端提示信息
            }
        }else{
            req.setAttribute(Constants.MESSAGE,"新密码不符合格式！");
        }
        try {
            req.getRequestDispatcher("pwdmodify.jsp").forward(req, resp);  //转发到当前页
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //验证旧密码
    public void pwdmodify(HttpServletRequest req, HttpServletResponse resp){
        String oldpassword = req.getParameter("oldpassword");  //ajax验证旧密码
        Object obj = req.getSession().getAttribute(Constants.USER_SESSION);
        Map<String,String> resultMap = new HashMap<String,String>();  //存放错误类型
        if(obj==null){  //session过期或者没失效
            resultMap.put("result","sessionerror");
        }else if(oldpassword==null){
            resultMap.put("result","error");  //旧密码为空
        }else{
            String userPassword = ((User)obj).getUserPassword();
            if(userPassword.equals(oldpassword)){
                resultMap.put("result","true");  //验证正确
            }else{
                resultMap.put("result","false");  //验证错误
            }
        }

        //将错误类型写为json格式返回前端ajax验证
        try{
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
//            String json = "{result:"+resultMap.get("result")+"}";
            writer.write(JSONArray.toJSONString(resultMap));  //json格式{key：value}
            writer.flush();  //情况缓冲区
            writer.close();  //关闭流
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //查询用户
    public void query(HttpServletRequest req, HttpServletResponse resp){
        //从前端获取数据
        String queryUserName = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole = 0;

        //获取用户列表
        UserService userService = new UserServiceImpl();

        //第一次走页面一定是第一页,页面大小固定的
        List<User> userList = null;
        //设置页面容量
        int pageSize = Constants.PAGESIZE;
        //当前页码
        int currentPageNo = 1;
        /**
         * http://localhost:8080/SMBMS/userlist.do
         * ----queryUserName --NULL
         * http://localhost:8090/SMBMS/userlist.do?queryname=
         * --queryUserName ---""
         */
        System.out.println("queryUserName servlet--------"+queryUserName);
        System.out.println("queryUserRole servlet--------"+queryUserRole);
        System.out.println("query pageIndex--------- > " + pageIndex);
        //可能出现的异常情况处理，增强健壮性
        if(queryUserName == null){
            queryUserName = "";
        }
        if(temp != null && !temp.equals("")){
            queryUserRole = Integer.parseInt(temp);//给查询赋值
        }

        if(pageIndex != null){
            try{
                currentPageNo = Integer.valueOf(pageIndex);
            }catch(Exception e){
                try {
                    resp.sendRedirect("error.jsp");
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }
        }
        //总数量（表）
        int totalCount	= userService.getUserCount(queryUserName,queryUserRole);
        //总页数
        PageSupport pages=new PageSupport();  //分页支持工具类

        pages.setCurrentPageNo(currentPageNo);  //设置当前页，包含异常处理

        pages.setPageSize(pageSize);  //设置页面大小

        pages.setTotalCount(totalCount);  //获得所用用户总数

        int totalPageCount = pages.getTotalPageCount();

        //控制首页和尾页
        if(currentPageNo < 1){
            currentPageNo = 1;
        }else if(currentPageNo > totalPageCount){
            currentPageNo = totalPageCount;
        }

        //查询用户列表
        userList = userService.getUserList(queryUserName,queryUserRole,currentPageNo, pageSize);
        req.setAttribute("userList", userList);  //返回前端显示
        List<Role> roleList = null;
        userRoleService roleService = new userRoleServiceImpl();
        roleList = roleService.getRoleList();  //获取用户角色列表
        //返回前端显示
        req.setAttribute("roleList", roleList);
        req.setAttribute("queryUserName", queryUserName);
        req.setAttribute("queryUserRole", queryUserRole);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("currentPageNo", currentPageNo);
        try {
            req.getRequestDispatcher("userlist.jsp").forward(req, resp);  //转发到用户显示页面
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //添加用户
    public void add(HttpServletRequest req, HttpServletResponse resp){
        User user = new User();  //创建要添加的用户
        UserService userService = new UserServiceImpl();  //调用业务层
        int _gender = 1;  //设置性别默认值，增强健壮性
        Date _birthday = null;  //设置生日默认值，增强健壮性
        int _userRole = 3;  //设置用户角色默认值为普通员工
        //获取前端参数
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");
        if(gender!=null) {
            try {
                _gender = Integer.valueOf(gender);  //注意此处要装箱为引用类型和实体类对应
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if(birthday!=null&&!birthday.equals("")) {
            try {
                _birthday = new SimpleDateFormat("yyyy-MM-dd").parse(birthday); //格式化日期
            }catch (ParseException e){
                e.printStackTrace();
            }
        }

        if(userRole!=null&&!userRole.equals("")) {
            try {
                _userRole = Integer.valueOf(userRole.trim());  //注意此处要装箱为引用类型
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //给用户添加属性
        user.setUserCode(userCode);
        user.setUserRole(_userRole);
        user.setBirthday(_birthday);
        user.setAddress(address);
        user.setPhone(phone);
        user.setGender(_gender);
        user.setUserPassword(userPassword);
        user.setUserName(userName);
        //从session中获取当前登录修改者的身份
        user.setModifyBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getUserRole());
        user.setModifyDate(new Date());  //获得当前日期为修改时间
        user.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getUserRole());
        user.setCreationDate(new Date());  //获得当前日期为创建时间
        try {
            if(userService.addUser(user)) {  //添加成功重定向自动重新查询显示
                resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
            }else {//失败转发到当前页面
                req.getRequestDispatcher("useradd.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       }

    //获取角色列表，用以添加用户时选择
    @SuppressWarnings("all")
    public void getrolelist(HttpServletRequest req, HttpServletResponse resp){
        userRoleService userRoleService = new userRoleServiceImpl();
        List<Role> list = userRoleService.getRoleList();  //获取用户角色列表

        //写入给前端显示
        resp.setContentType("application/json");
        try {
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(list));  //转为json格式写出
            writer.flush();
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            req.getRequestDispatcher("useradd.jsp");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //后台给ajax传递验证用户名是否存在
    @SuppressWarnings("all")
    public void ucexist(HttpServletRequest req, HttpServletResponse resp){
        //获取前端传递的用户名用以判断
        String userCode = req.getParameter("userCode");
        Map<String,String> resultMap = new HashMap<>();  //存放判断结果
        UserService userService = new UserServiceImpl();
        if (userService.isUCexist(userCode)) {  //存在
            resultMap.put("userCode", "exist");
        } else { //不存在
            resultMap.put("userCode", "nonexist");
        }

        //写出前端显示
        resp.setContentType("application/json");
        try{
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(resultMap));  //转为json格式
            writer.flush();
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @SuppressWarnings("all")
    //根据id删除用户
    public void deluser(HttpServletRequest req, HttpServletResponse resp){
        String temp = req.getParameter("uid");  //获取前端要删除用户的id
        Map<String,String> resultMap = new HashMap<>();  //存放删除结果
        int id = -1;
        if(temp!=null){
            id = Integer.parseInt(temp);
            UserService userService = new UserServiceImpl();
            if(userService.deluser(id)) {  //成功
                resultMap.put("delResult", "true");
            }else {  //失败
                resultMap.put("delResult","false");
            }
        }else{   //用户不存在
            resultMap.put("delResult","notexist");
        }
        //写出前端显示
        resp.setContentType("application/json");
        try {
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(resultMap));  //转为json格式
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        req.getRequestDispatcher("userlist.jsp");  //转发当前页面
    }

    //根据id查看用户
    public void view(HttpServletRequest req, HttpServletResponse resp){
        User user = null;
        String uid = req.getParameter("uid");  //从前端获取用户id
        int id = -1;
        if(uid!=null&&!uid.equals("")){  //参数不空
            id = Integer.valueOf(uid);
        }
        UserService userService = new UserServiceImpl();  //调用业务层
        user = userService.view(id);
        if(user!=null) {  //查有此人，重定向到用户信息查看页面
            req.setAttribute("user",user);  //设置属性给前端页面取来显示
            try {
                req.getRequestDispatcher("userview.jsp").forward(req,resp);  //此处不能用重定向，应为请求只在一次请求中有效，重定向后user属性就没有了，前端就取不到，显示不出来
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {  //查无此人，转发到当前页面
            try {
                req.getRequestDispatcher("userlist.jsp").forward(req, resp);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //根据用户id修改用户信息页面，此方法不涉及数据库
    public void modify(HttpServletRequest req, HttpServletResponse resp){
        //获取请求参数
        String uid = req.getParameter("uid");
        int id = -1;
        User user = null;
        UserService userService = new UserServiceImpl();  //调用业务层
        if(uid!=null&&!uid.equals("")){
            id = Integer.valueOf(uid);
        }
        user = userService.view(id);  //根据id把用户信息查出来，便于回显
        if(user!=null) {  //存在相应用户
            user.setId(id);  //此处不设置在modifyexe方法中取不到id，坑！
            try {
                req.getSession().setAttribute("user",user);
                req.getRequestDispatcher("usermodify.jsp").forward(req, resp);  //用户修改页面
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{  //不存在
            try {
                req.getRequestDispatcher("userlist.jsp").forward(req, resp);  //用户显示页面
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //保存用户信息修改
    public void modifyexe(HttpServletRequest req, HttpServletResponse resp){
        //请求中获取修改后的参数
        String userName = req.getParameter("userName");
        String gender = req.getParameter("gender");
//        String birthday = req.getParameter("birthday");  //用户生日是只读的
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        User user = (User) req.getSession().getAttribute("user");  //取得要修改的用户

        System.out.println(user.getId());
        //将用户当前信息赋为默认值，避免错误
        int _gender = user.getGender();
        int _userRole = user.getUserRole();

        if(gender!=null&&!gender.equals("")){
            _gender = Integer.valueOf(gender);
        }

        if(userRole!=null&&!userRole.equals("")){
            _userRole = Integer.valueOf(userRole);
        }

        //用户新属性封装
        user.setUserRole(_userRole);
        user.setAddress(address);
        user.setUserName(userName);
        user.setPhone(phone);
        user.setGender(_gender);
        user.setModifyDate(new Date());
        user.setModifyBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getUserRole());
        UserService userService = new UserServiceImpl();
        if(userService.modifyexe(user)){
            try {
                resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
            }catch (Exception e){
                req.setAttribute(Constants.MESSAGE,"修改失败！");
                e.printStackTrace();
            }
        }else{
            req.setAttribute(Constants.MESSAGE,"修改失败！");
            try{
                req.getRequestDispatcher("usermodify.jsp").forward(req,resp);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    }

