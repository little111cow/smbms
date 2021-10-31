package cn.app.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * 字符编码过滤器
 * */
public class EncodingFilter implements Filter {
    public EncodingFilter() {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse rep, FilterChain filterChain) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");  //请求的编码
        rep.setCharacterEncoding("UTF-8");  //响应的编码
        filterChain.doFilter(req,rep);  //过滤器的固定代码，放行请求
    }

    @Override
    public void destroy() {

    }
}
