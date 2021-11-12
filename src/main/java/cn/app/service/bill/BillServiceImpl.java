package cn.app.service.bill;

import cn.app.dao.BaseDao;
import cn.app.dao.bill.BillDao;
import cn.app.dao.bill.BillDaoImpl;
import cn.app.entity.Bill;

import java.sql.Connection;
import java.util.List;

public class BillServiceImpl implements BillService{
    private  BillDao billDao;
    public BillServiceImpl() {
        billDao = new BillDaoImpl();
    }

    @Override
    public int getCountBycondition(String queryProductName, Integer isPayment, Integer ProviderId) {
        return billDao.getCountBycondition(queryProductName,isPayment,ProviderId);
    }

    @Override
    public List<Bill> getBillListByCondition(String queryProductName, Integer isPayment, Integer ProviderId, Integer currentPageNo, Integer pageSize) {
        return billDao.getBillListByCondition(queryProductName,isPayment,ProviderId,currentPageNo,pageSize);
    }

    @Override
    public int getBillCountByProviderId(String providerId) {
        Connection collection = null;
        int count = 0;
        try {
            collection = BaseDao.getConnect();
            count = billDao.getBillCountByProviderId(collection,providerId);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            BaseDao.closeResources(collection,null,null);
        }
        return count;
    }

    @Override
    public boolean add(Bill bill) {
        boolean flag = false;
        Connection connection = BaseDao.getConnect();
        if(connection!=null){
            try{
                connection.setAutoCommit(false);
                flag = billDao.add(connection,bill);
                connection.commit();
            }catch (Exception e){
                flag =false;
                try{
                    connection.rollback();
                }catch (Exception e1){
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(connection,null,null);
            }
        }
        return flag;
    }

    @Override
    public boolean delbillById(int id) {
        boolean flag = false;
        Connection connection = BaseDao.getConnect();
        if(connection!=null){
            try{
                connection.setAutoCommit(false);
                flag = billDao.delbillById(connection,id);
                connection.commit();
            }catch (Exception e){
                flag = false;
                try{
                    connection.rollback();
                }catch (Exception e1){
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(connection,null,null);
            }
        }
        return flag;
    }

    @Override
    public boolean updateBill(Bill bill) {
        Connection connection = BaseDao.getConnect();
        boolean flag = false;
        if(connection!=null){
            try{
                connection.setAutoCommit(false);
                flag = billDao.updateBill(connection,bill);
                connection.commit();
            }catch (Exception e){
                try{
                    connection.rollback();
                }catch (Exception e1){
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(connection,null,null);
            }
        }
        return flag;
    }

    @Override
    public Bill getBillById(int id) {
        return billDao.getBillById(id);
    }
}
