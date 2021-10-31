package cn.app.filter;

import cn.app.entity.User;
import cn.app.util.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SysFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)servletRequest;  //请求类型转换
        HttpServletResponse resp = (HttpServletResponse)servletResponse;  //响应类型转换

        User user = (User) req.getSession().getAttribute(Constants.USER_SESSION);  //获得session
        if(user == null){  //注销或者未登录
            resp.sendRedirect(req.getContextPath()+"/error.jsp");  //重定向到error提示页面
        }else{
            filterChain.doFilter(servletRequest,servletResponse);  //若登录了则放行
        }
    }

    @Override
    public void destroy() {

    }
}
