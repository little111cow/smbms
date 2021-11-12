package cn.app.servlet.bill;

import clojure.lang.Compiler;
import cn.app.entity.Bill;
import cn.app.entity.Provider;
import cn.app.entity.User;
import cn.app.service.bill.BillService;
import cn.app.service.bill.BillServiceImpl;
import cn.app.service.provider.ProviderService;
import cn.app.service.provider.ProviderServiceImpl;
import cn.app.util.Constants;
import cn.app.util.PageSupport;
import com.alibaba.fastjson.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillServelet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if(method!=null){
            if(method.equals("query")){
                this.query(req,resp);
            }else if(method.equals("view")){
                this.view(req,resp);
            }else if(method.equals("modify")){
                this.modify(req,resp);
            }else if(method.equals("modifysave")){
                this.modifysave(req,resp);
            }else if(method.equals("delbill")){
                this.delbill(req,resp);
            }else if(method.equals("add")){
                this.add(req,resp);
            }else if(method.equals("getproviderlist")){
                this.getproviderlist(req,resp);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    public void query(HttpServletRequest req, HttpServletResponse resp){
        //查出所有供应商,供查询时选择
       ProviderService providrService = new ProviderServiceImpl();
        List<Provider> providerList = providrService.getProviderList(null,null,null,null);
        List<Bill> billList;
        String queryProductName = req.getParameter("queryProductName");
        String queryProviderId = req.getParameter("queryProviderId");
        String queryIsPayment = req.getParameter("queryIsPayment");
        String pageIndex = req.getParameter("pageIndex");

        Integer ProviderId = null;
        Integer isPayment = null;
        int currentPageNo = 1;
        int totalPageCount;
        int totalCount;
        int pageSize = Constants.PAGESIZE;

        if(queryProductName==null){
            queryProductName = "";
        }
        if(queryProviderId!=null){
            ProviderId = Integer.valueOf(queryProviderId);
            if(ProviderId==0){  //为0时显示为请选择，设置为null，踩了很久的坑
                ProviderId = null;
            }
        }
        if(queryIsPayment!=null){
            isPayment = Integer.valueOf(queryIsPayment);
            if(isPayment==0){  //为0时显示为请选择，设置为null，踩了很久的坑
                isPayment = null;
            }
        }
        if(pageIndex != null){
            currentPageNo = Integer.valueOf(pageIndex);
        }

        BillService billService = new BillServiceImpl();
        totalCount = billService.getCountBycondition(queryProductName,isPayment,ProviderId);

        PageSupport pageSupport = new PageSupport();

        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        totalPageCount = pageSupport.getTotalPageCount();

        if(currentPageNo>totalPageCount){
            currentPageNo = totalPageCount;
        }else if(currentPageNo<=0){
            currentPageNo = 1;
        }

        ProviderService providerService = new ProviderServiceImpl();
        billList = billService.getBillListByCondition(queryProductName,isPayment,ProviderId,currentPageNo,pageSize);
        for(Bill bill:billList){
            bill.setProviderName(providerService.getProviderById(""+bill.getProviderId()).getProName());
        }
        try{
            req.setAttribute("billList",billList);
            req.setAttribute("totalCount",totalCount);
            req.setAttribute("totalPageCount",totalPageCount);
            req.setAttribute("currentPageNo",currentPageNo);
            req.getSession().setAttribute("providerList",providerList);
            req.getRequestDispatcher("billlist.jsp").forward(req,resp);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void view(HttpServletRequest req, HttpServletResponse resp){
        String temp = req.getParameter("billid");
        Bill bill = null;
        int billid = -1;
        if(temp!=null){
            billid = Integer.parseInt(temp);
        }
        BillService billService = new BillServiceImpl();
        ProviderService providerService = new ProviderServiceImpl();
        bill = billService.getBillById(billid);
        bill.setProviderName(providerService.getProviderById(""+bill.getProviderId()).getProName());
        try{
            req.getSession().setAttribute("bill",bill);
            resp.sendRedirect(req.getContextPath()+"/jsp/billview.jsp");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void modify(HttpServletRequest req, HttpServletResponse resp){
        String temp = req.getParameter("billid");
        Bill bill = null;
        int billid = -1;
        if(temp!=null){
            billid = Integer.parseInt(temp);
        }
        BillService billService = new BillServiceImpl();
        bill = billService.getBillById(billid);

        ProviderService providrService = new ProviderServiceImpl();
        bill.setProviderName(providrService.getProviderById(""+bill.getProviderId()).getProName());
        List<Provider> providerList = providrService.getProviderList(null,null,null,null);
        try{
            req.getSession().setAttribute("providerList",providerList);
            req.getSession().setAttribute("bill",bill);
            resp.sendRedirect("billmodify.jsp");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void modifysave(HttpServletRequest req, HttpServletResponse resp){
        String productName = req.getParameter("productName");
        String productUnit = req.getParameter("productUnit");
        String productCount = req.getParameter("productCount");
        String totalPrice = req.getParameter("totalPrice");
        String providerId = req.getParameter("providerId");
        String isPayment = req.getParameter("isPayment");
        String productDescribe = req.getParameter("productDescribe");

        BigDecimal productCount_ = new BigDecimal(productCount);
        BigDecimal totalPrice_ = new BigDecimal(totalPrice);
        int providerId_ = Integer.valueOf(providerId);
        int isPayment_ = Integer.valueOf(isPayment);

        Bill bill = (Bill) req.getSession().getAttribute("bill");
        bill.setIsPayment(isPayment_);
        bill.setProductUnit(productUnit);
        bill.setTotalPrice(totalPrice_);
        bill.setProviderId(providerId_);
        bill.setProductCount(productCount_);
        bill.setProductName(productName);
        bill.setId(bill.getId());
        bill.setProductDesc(productDescribe);
        bill.setModifyDate(new Date());
        bill.setModifyBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());


        BillService billService = new BillServiceImpl();
        if(billService.updateBill(bill)){
            try{
                req.getRequestDispatcher("/jsp/bill.do?method=query").forward(req,resp);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            try {
                req.setAttribute(Constants.MESSAGE,"修改失败！");
                req.getRequestDispatcher("billmodify.jsp").forward(req, resp);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void delbill(HttpServletRequest req, HttpServletResponse resp){
        String billid_ = req.getParameter("billid");
        Map<String,String> delResult = new HashMap<>();
        int billid = -1;
        if(billid_!=null){
            billid = Integer.valueOf(billid_);
        }
        if(billid<0){
            delResult.put("delResult","notexist");
        }else {
            BillService billService = new BillServiceImpl();
            if (billService.delbillById(billid)) {
                delResult.put("delResult", "true");
            } else {
                delResult.put("delResult", "false");
            }
        }

        resp.setContentType("application/json");
        try {
            PrintWriter printWriter = resp.getWriter();
            printWriter.write(JSONArray.toJSONString(delResult));
            printWriter.flush();
            printWriter.close();
            req.getRequestDispatcher("billlist.jsp?method=query").forward(req,resp);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void add(HttpServletRequest req, HttpServletResponse resp){
//        req.getSession().getAttribute("providerList");

        String billCode = req.getParameter("billCode");
        String productName = req.getParameter("productName");
        String productUnit = req.getParameter("productUnit");
        String productCount_ = req.getParameter("productCount");
        String totalPrice_ = req.getParameter("totalPrice");
        String providerId_ = req.getParameter("providerId");
        String isPayment_ = req.getParameter("isPayment");
        String productDescribe = req.getParameter("productDescribe");

        BigDecimal productCount = new BigDecimal(productCount_);
        BigDecimal totalPrice = new BigDecimal(totalPrice_);
        int providerId = Integer.valueOf(providerId_);
        int isPayment = Integer.valueOf(isPayment_);

        Bill bill = new Bill();
        bill.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getUserRole());
        bill.setCreationDate(new Date());
        bill.setProductName(productName);
        bill.setProviderId(providerId);
        bill.setTotalPrice(totalPrice);
        bill.setIsPayment(isPayment);
        bill.setProductUnit(productUnit);
        bill.setProductCount(productCount);
        bill.setBillCode(billCode);
        bill.setProductDesc(productDescribe);

        BillService billService = new BillServiceImpl();
        if(billService.add(bill)){
            try {
                resp.sendRedirect(req.getContextPath()+"/jsp/bill.do?method=query");
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            try {
                req.setAttribute(Constants.MESSAGE,"添加失败！");
                req.getRequestDispatcher("billadd.jsp").forward(req, resp);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void getproviderlist(HttpServletRequest req, HttpServletResponse resp){
        ProviderService providrService = new ProviderServiceImpl();
        List<Provider> providerList = providrService.getProviderList(null,null,null,null);
        try{
            req.getSession().setAttribute("providerList",providerList);
            req.getRequestDispatcher("billadd.jsp").forward(req,resp);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
