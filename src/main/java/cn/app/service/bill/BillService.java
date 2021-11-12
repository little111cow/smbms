package cn.app.service.bill;

import cn.app.entity.Bill;

import java.sql.Connection;
import java.util.List;

public interface BillService {

    public int getCountBycondition(String queryProductName,Integer isPayment,Integer ProviderId);

    //条件查询订单列表
    public List<Bill> getBillListByCondition(String queryProductName, Integer isPayment, Integer ProviderId, Integer currentPageNo, Integer pageSize);

    public int getBillCountByProviderId(String providerId);

    public Bill getBillById(int id);

    public boolean updateBill(Bill bill);

    public boolean delbillById(int id);

    public boolean add(Bill bill);
}
