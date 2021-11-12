package cn.app.dao.bill;

import cn.app.dao.BaseDao;
import cn.app.dao.provider.ProviderDaoImpl;
import cn.app.entity.Bill;
import com.mysql.jdbc.StringUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class BillDaoImpl implements BillDao {

    @Override
    public int getCountBycondition(String queryProductName, Integer isPayment, Integer ProviderId) {
        String sql = "select count(1) as count from smbms_bill where 1=1";
        List<Object> list = new ArrayList<>();
        StringBuffer sb = new StringBuffer(sql);
        if(queryProductName!=null&&!queryProductName.equals("")){
            sb.append(" and productName like ?");
            list.add("%"+queryProductName+"%");
        }
        if(isPayment!=null){
            sb.append(" and isPayment=?");
            list.add(isPayment);
        }
        if(ProviderId!=null){
            sb.append(" and providerId=?");
            list.add(ProviderId);
        }
        System.out.println(sb.toString());
        Object[] param = list.toArray();

        Connection connection = BaseDao.getConnect();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int count = 0;
        if(connection!=null){
            try{
                rs = BaseDao.execute(connection,pstm,sb.toString(),param,rs);
                if(rs.next()){
                    count = rs.getInt("count");
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(connection,pstm,rs);
            }
        }
        return count;
    }

    @Override
    public Bill getBillById(int id) {
        String sql = "select * from smbms_bill where id=?";
        Connection connection = BaseDao.getConnect();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Bill bill = null;
        Object[] param = {id};
        if(connection!=null){
            try{
                rs = BaseDao.execute(connection,pstm,sql,param,rs);
                while (rs.next()){
                    bill = new Bill();
                    bill.setId(rs.getInt("id"));
                    bill.setTotalPrice(rs.getBigDecimal("totalPrice"));
                    bill.setProviderId(rs.getInt("providerId"));
                    bill.setProductName(rs.getString("productName"));
                    bill.setProductCount(rs.getBigDecimal("productCount"));
                    bill.setBillCode(rs.getString("billCode"));
                    bill.setIsPayment(rs.getInt("isPayment"));
                    bill.setProductUnit(rs.getString("productUnit"));
                    bill.setProductDesc(rs.getString("productDesc"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(connection,pstm,rs);
            }
        }
        return bill;
    }

    @Override
    public boolean updateBill(Connection connection, Bill bill) {
        boolean flag = false;
        String sql = "update smbms_bill set productName=?,productUnit=?,productCount=?,totalPrice=?,providerId=?,"
                +"isPayment=?,modifyBy=?,modifyDate=?,productDesc=? where id=?";
        PreparedStatement pstm = null;
        Object[] param = {bill.getProductName(),bill.getProductUnit(),bill.getProductCount(),bill.getTotalPrice(),
                          bill.getProviderId(),bill.getIsPayment(),bill.getModifyBy(),bill.getModifyDate(),
                          bill.getProductDesc(), bill.getId()};
        int cnt = 0;
        try{
            cnt = BaseDao.execute(connection,pstm,sql,param);
            if(cnt>0){
                flag = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            BaseDao.closeResources(null,pstm,null);
        }
        return flag;
    }

    @Override
    public List<Bill> getBillListByCondition(String queryProductName, Integer isPayment, Integer ProviderId, Integer currentPageNo, Integer pageSize) {
       List<Bill> billList = new ArrayList<>();
        String sql = "select * from smbms_bill where 1=1";
        List<Object> list = new ArrayList<>();
        StringBuffer sb = new StringBuffer(sql);
        if(queryProductName!=null&&!queryProductName.equals("")){
            sb.append(" and productName like ?");
            list.add("%"+queryProductName+"%");
        }
        if(isPayment!=null){
            sb.append(" and isPayment=?");
            list.add(isPayment);
        }
        if(ProviderId!=null){
            sb.append(" and providerId=?");
            list.add(ProviderId);
        }
        if(currentPageNo!=null&&pageSize!=null){
            sb.append(" order by creationDate desc limit ?,?");
            list.add((currentPageNo-1)*pageSize);
            list.add(pageSize);
        }

        System.out.println(sb.toString());
        Object[] param = list.toArray();

        Connection connection = BaseDao.getConnect();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        if(connection!=null){
            try{
                rs = BaseDao.execute(connection,pstm,sb.toString(),param,rs);
                System.out.println(rs);
                while(rs.next()){
                    Bill bill = new Bill();
                    bill.setBillCode(rs.getString("billCode"));
                    bill.setCreationDate(rs.getTimestamp("creationDate"));
                    bill.setId(rs.getInt("id"));
                    bill.setProductName(rs.getString("productName"));
                    bill.setProviderId(rs.getInt("providerId"));
                    bill.setTotalPrice(rs.getBigDecimal("totalPrice"));
                    bill.setIsPayment(rs.getInt("isPayment"));
                    billList.add(bill);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(connection,pstm,rs);
            }
        }
       return billList;
    }

    @Override
    public boolean add(Connection connection, Bill bill) {
        String sql = "insert into smbms_bill(billCode,productName,productDesc,productUnit,productCount," +
                "totalPrice,isPayment,createdBy,creationDate,providerId) values(?,?,?,?,?,?,?,?,?,?)";
        boolean flag = false;
        PreparedStatement pstm = null;
        Object[] param ={bill.getBillCode(),bill.getProductName(),bill.getProductDesc(),
        bill.getProductUnit(),bill.getProductCount(),bill.getTotalPrice(),bill.getIsPayment(),
        bill.getCreatedBy(),bill.getCreationDate(),bill.getProviderId()};
        int cnt = 0;
        if(connection!=null){
            try{
                cnt = BaseDao.execute(connection,pstm,sql,param);
                if(cnt>0){
                    flag = true;
                }
            }catch (Exception e){
                flag = false;
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(null,pstm,null);
            }
        }
        return flag;
    }

    @Override
    public boolean delbillById(Connection connection, int id) {
        boolean flag = false;
        String sql = "delete from smbms_bill where id=? ";
        Object[] param = {id};
        PreparedStatement pstm =null;
        int cnt;
        if(connection!=null){
            try{
                cnt = BaseDao.execute(connection,pstm,sql,param);
                if(cnt>0){
                    flag = true;
                }
            }catch (Exception e){
                flag = false;
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(null,pstm,null);
            }
        }
        return flag;
    }

    @Override
    public int getBillCountByProviderId(Connection connection, String providerId) {
        // TODO Auto-generated method stub
        int count = 0;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        if(null != connection){
            String sql = "select count(1) as billCount from smbms_bill where" +
                    "	providerId = ?";
            Object[] params = {providerId};
            try {
                rs = BaseDao.execute(connection, pstm, sql, params, rs);
                if (rs.next()) {
                    count = rs.getInt("billCount");
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(null, pstm, rs);
            }
        }
        return count;
    }
}
