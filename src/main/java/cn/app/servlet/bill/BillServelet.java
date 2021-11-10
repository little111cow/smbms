package cn.app.servlet.bill;

import cn.app.entity.Provider;
import cn.app.service.provider.ProviderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class BillServelet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if(method!=null){
            if(method.equals("query")){
                this.query(req,resp);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    public void query(HttpServletRequest req, HttpServletResponse resp){
        List<Provider> providerList = new ProviderServiceImpl().getProviderList(null,null,null,null);

        try{
            req.getSession().setAttribute("providerList",providerList);  //必须放在session中，由于下边是重定向，此次请求作用域就到此
            resp.sendRedirect(req.getContextPath()+"/jsp/billlist.jsp");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
