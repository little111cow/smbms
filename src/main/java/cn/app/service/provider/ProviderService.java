package cn.app.service.provider;

import cn.app.entity.Provider;

import java.util.List;

public interface ProviderService {
	/**
	 * 增加供应商
	 * @param provider
	 * @return
	 */
	public boolean add(Provider provider);


	/**
	 * 通过供应商名称、编码获取供应商列表-模糊查询-providerList
	 * @param proName
	 * @return
	 */
	public List<Provider> getProviderList(String proName, String proCode,Integer currentPageNo, Integer pageSize);

	public int getProviderCount(String proName, String proCode);  //获得供应商户总数

	/**
	 * 通过proId删除Provider
	 * @param delId
	 * @return
	 */
	public int deleteProviderById(String delId);
	
	
	/**
	 * 通过proId获取Provider
	 * @param id
	 * @return
	 */
	public Provider getProviderById(String id);
	
	/**
	 * 修改用户信息
	 * @param Provider
	 * @return
	 */
	public boolean modify(Provider provider);
	
}
