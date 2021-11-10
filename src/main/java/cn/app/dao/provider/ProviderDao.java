package cn.app.dao.provider;

import cn.app.entity.Provider;

import java.sql.Connection;
import java.util.List;

public interface ProviderDao {
	
	/**
	 * 增加供应商
	 * @param connection
	 * @param provider
	 * @return
	 * @throws Exception
	 */
	public int add(Connection connection, Provider provider)throws Exception;


	/**
	 * 通过供应商名称、编码获取供应商列表-模糊查询-providerList
	 * @param connection
	 * @param proName
	 * @return
	 * @throws Exception
	 */
	public List<Provider> getProviderList(Connection connection, String proName, String proCode,Integer currentPageNo, Integer pageSize)throws Exception;

	public int getProviderCount(Connection connection,String proName,String proCode);

	/**
	 * 通过proId删除Provider
	 * @param delId
	 * @return
	 * @throws Exception
	 */
	public int deleteProviderById(Connection connection, String delId)throws Exception;
	
	
	/**
	 * 通过proId获取Provider
	 * @param connection
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Provider getProviderById(Connection connection, String id)throws Exception;
	
	/**
	 * 修改用户信息
	 * @param connection
	 * @param Provider
	 * @return
	 * @throws Exception
	 */
	public int modify(Connection connection, Provider provider)throws Exception;
	
	
}
