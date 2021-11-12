package cn.app.dao.bill;

import cn.app.entity.Bill;

import java.security.PublicKey;
import java.sql.Connection;
import java.util.List;

public interface BillDao {
    /**
     * 条件查询订单的数量
     * @param queryProductName 查询的产品名称
     * @param isPayment 是否付款1/2
     * @param ProviderId
     * @return int
     * */
    public int getCountBycondition(String queryProductName,Integer isPayment,Integer ProviderId);

    //条件查询订单列表
    public List<Bill> getBillListByCondition(String queryProductName,Integer isPayment,Integer ProviderId,Integer currentPageNo,Integer pageSize);

    public int getBillCountByProviderId(Connection connection, String providerId);

    public Bill getBillById(int id);

    public boolean updateBill(Connection connection,Bill bill);

    public boolean delbillById(Connection connection,int id);

    public boolean add(Connection connection,Bill bill);
}
