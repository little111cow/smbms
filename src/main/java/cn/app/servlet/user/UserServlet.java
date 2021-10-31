package cn.app.servlet.user;

import cn.app.entity.User;
import cn.app.service.user.UserService;
import cn.app.service.user.UserServiceImpl;
import cn.app.util.Constants;
import com.alibaba.fastjson.JSONArray;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");  //获取前端请求的方法
        if(method!=null){
            if(method.equals("savepwd")){
                this.updatePassword(req,resp);  //前端请求的方法为savepwd就更新密码
            }else if(method.equals("pwdmodify")){
                this.pwdmodify(req,resp);
            }else{

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
}
