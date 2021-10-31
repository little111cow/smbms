package cn.app.servlet.user;

import cn.app.entity.User;
import cn.app.service.user.UserService;
import cn.app.service.user.UserServiceImpl;
import cn.app.util.Constants;
import com.sun.org.apache.xpath.internal.SourceTree;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userCode = req.getParameter("userCode");  //从请求中获取用户名参数
        String userPassword = req.getParameter("userPassword");  //从请求中获取密码参数
        UserService userService = new UserServiceImpl();  //调用业务层获得登录用户
        User user = userService.login(userCode,userPassword);
        if(user != null) {
            req.getSession().setAttribute(Constants.USER_SESSION, user);  //在session中添加用户
            resp.sendRedirect("jsp/frame.jsp");  //重定向到服务端首页
        }else{
            req.setAttribute("error","用户名或者密码不正确！");  //在session中设置错误提示信息，在前端提示
            req.getRequestDispatcher("/login.jsp").forward(req,resp);  //转发回登录页
        }

    }

//    @Test
//    public void test(){
//        User user = new UserServiceImpl().login("admin","1234567");
//        System.out.println(user.getUserPassword());
//    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);  //直接调用doGet
    }
}
