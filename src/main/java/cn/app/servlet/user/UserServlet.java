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
                this.query(req, resp);
            } else if (method.equals("add")) {
                this.add(req, resp);
            } else if (method.equals("getrolelist")) {
                this.getrolelist(req, resp);
            }else if(method.equals("ucexist")){
                this.ucexist(req, resp);
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
         * http://localhost:8090/SMBMS/userlist.do
         * ----queryUserName --NULL
         * http://localhost:8090/SMBMS/userlist.do?queryname=
         * --queryUserName ---""
         */
        System.out.println("queryUserName servlet--------"+queryUserName);
        System.out.println("queryUserRole servlet--------"+queryUserRole);
        System.out.println("query pageIndex--------- > " + pageIndex);
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
        PageSupport pages=new PageSupport();

        pages.setCurrentPageNo(currentPageNo);

        pages.setPageSize(pageSize);

        pages.setTotalCount(totalCount);

        int totalPageCount = pages.getTotalPageCount();

        //控制首页和尾页
        if(currentPageNo < 1){
            currentPageNo = 1;
        }else if(currentPageNo > totalPageCount){
            currentPageNo = totalPageCount;
        }


        userList = userService.getUserList(queryUserName,queryUserRole,currentPageNo, pageSize);
        req.setAttribute("userList", userList);
        List<Role> roleList = null;
        userRoleService roleService = new userRoleServiceImpl();
        roleList = roleService.getRoleList();
        req.setAttribute("roleList", roleList);
        req.setAttribute("queryUserName", queryUserName);
        req.setAttribute("queryUserRole", queryUserRole);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("currentPageNo", currentPageNo);
        try {
            req.getRequestDispatcher("userlist.jsp").forward(req, resp);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //添加用户
    public void add(HttpServletRequest req, HttpServletResponse resp){
        User user = new User();
        UserService userService = new UserServiceImpl();
        int _gender = 1;
        Date _birthday = null;
        int _userRole = 3;
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
                _gender = 1;  //转换出错给它设定默认值
                e.printStackTrace();
            }
        }

        if(birthday!=null&&!birthday.equals("")) {
            try {
                _birthday = new SimpleDateFormat("yyyy-MM-dd").parse(birthday);
                System.out.println(_birthday);
            }catch (ParseException e){
                _birthday = null;  //转换出错给它设定默认值
                e.printStackTrace();
            }
        }

        if(userRole!=null&&!userRole.equals("")) {
            try {
                _userRole = Integer.valueOf(userCode.trim());  //注意此处要装箱为引用类型
            }catch (Exception e){
                _userRole = 3;  //转换出错给它设定默认值
                e.printStackTrace();
            }
        }
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
        user.setModifyDate(new Date());
        user.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getUserRole());
        user.setCreationDate(new Date());
        try {
            if(userService.addUser(user)) {
                resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
            }else {
                req.getRequestDispatcher("useradd.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       }

    //获取角色列表，用以添加用户时选择
    public void getrolelist(HttpServletRequest req, HttpServletResponse resp){
        userRoleService userRoleService = new userRoleServiceImpl();
        List<Role> list = userRoleService.getRoleList();

        resp.setContentType("application/json");
        try {
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(list));
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
        String userCode = req.getParameter("userCode");
        System.out.println(userCode);
        Map<String,String> resultMap = new HashMap<>();
        UserService userService = new UserServiceImpl();
        if (userService.isUCexist(userCode)) {
            System.out.println(userService.isUCexist(userCode));
            resultMap.put("userCode", "exist");
        } else {
            resultMap.put("userCode", "nonexist");
        }

        resp.setContentType("application/json");
        try{
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    }

