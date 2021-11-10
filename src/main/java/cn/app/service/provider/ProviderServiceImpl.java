package cn.app.service.provider;

import cn.app.dao.BaseDao;
import cn.app.dao.bill.BillDao;
import cn.app.dao.bill.BillDaoImpl;
import cn.app.dao.provider.ProviderDao;
import cn.app.dao.provider.ProviderDaoImpl;
import cn.app.entity.Provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProviderServiceImpl implements ProviderService {
	
	private ProviderDao providerDao;
	private BillDao  billDao;
	public ProviderServiceImpl(){
		providerDao = new ProviderDaoImpl();
		billDao = new BillDaoImpl();
	}
	@Override
	public boolean add(Provider provider) {
		// TODO Auto-generated method stub
		boolean flag = false;
		Connection connection = null;
		try {
			connection = BaseDao.getConnect();
			connection.setAutoCommit(false);//开启JDBC事务管理
			if(providerDao.add(connection,provider) > 0)
				flag = true;
			connection.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				System.out.println("rollback==================");
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			//在service层进行connection连接的关闭
			BaseDao.closeResources(connection, null, null);
		}
		return flag;
	}

	@Override
	public int getProviderCount(String proName, String proCode) {
		int count = 0;
		Connection connection = BaseDao.getConnect();//数据库连接
		if(connection!=null){
			try{
				count = providerDao.getProviderCount(connection,proName,proCode);  //获得用户总数
			}catch (Exception e){
				e.printStackTrace();
			}finally {
				BaseDao.closeResources(connection,null,null);  //关闭资源
			}
		}
		return count;
	}

	@Override
	public List<Provider> getProviderList(String proName, String proCode,Integer currentPageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		Connection connection = null;
		List<Provider> providerList = null;
		System.out.println("query proName ---- > " + proName);
		System.out.println("query proCode ---- > " + proCode);
		try {
			connection = BaseDao.getConnect();
			providerList = providerDao.getProviderList(connection, proName,proCode,currentPageNo,pageSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			BaseDao.closeResources(connection, null, null);
		}
		return providerList;
	}

	/**
	 * 业务：根据ID删除供应商表的数据之前，需要先去订单表里进行查询操作
	 * 若订单表中无该供应商的订单数据，则可以删除
	 * 若有该供应商的订单数据，则不可以删除
	 * 返回值billCount
	 * 1> billCount == 0  删除---1 成功 （0） 2 不成功 （-1）
	 * 2> billCount > 0    不能删除 查询成功（0）查询不成功（-1）
	 * 
	 * ---判断
	 * 如果billCount = -1 失败
	 * 若billCount >= 0 成功
	 */
	@Override
	public int deleteProviderById(String delId) {
		// TODO Auto-generated method stub
		Connection connection = null;
		int billCount = -1;
		try {
			connection = BaseDao.getConnect();
			connection.setAutoCommit(false);
			billCount = billDao.getBillCountByProviderId(connection,delId);
			if(billCount == 0){
				providerDao.deleteProviderById(connection, delId);
			}
			connection.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			billCount = -1;
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			BaseDao.closeResources(connection, null, null);
		}
		return billCount;
	}

	@Override
	public Provider getProviderById(String id) {
		// TODO Auto-generated method stub
		Provider provider = null;
		Connection connection = null;
		try{
			connection = BaseDao.getConnect();
			provider = providerDao.getProviderById(connection, id);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			provider = null;
		}finally{
			BaseDao.closeResources(connection, null, null);
		}
		return provider;
	}

	@Override
	public boolean modify(Provider provider) {
		// TODO Auto-generated method stub
		Connection connection = null;
		boolean flag = false;
		try {
			connection = BaseDao.getConnect();
			if(providerDao.modify(connection,provider) > 0)
				flag = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			BaseDao.closeResources(connection, null, null);
		}
		return flag;
	}

}
