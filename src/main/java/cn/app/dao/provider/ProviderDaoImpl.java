package cn.app.dao.provider;

import cn.app.dao.BaseDao;
import cn.app.entity.Provider;
import com.mysql.jdbc.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProviderDaoImpl implements ProviderDao {

	@Override
	public int add(Connection connection, Provider provider)
			throws Exception {
		// TODO Auto-generated method stub
		PreparedStatement pstm = null;
		int flag = 0;
		if(null != connection){
			String sql = "insert into smbms_provider (proCode,proName,proDesc," +
					"proContact,proPhone,proAddress,proFax,createdBy,creationDate) " +
					"values(?,?,?,?,?,?,?,?,?)";
			Object[] params = {provider.getProCode(),provider.getProName(),provider.getProDesc(),
								provider.getProContact(),provider.getProPhone(),provider.getProAddress(),
								provider.getProFax(),provider.getCreatedBy(),provider.getCreationDate()};
			flag = BaseDao.execute(connection, pstm, sql, params);
			BaseDao.closeResources(null, pstm, null);
		}
		return flag;
	}

    @Override
    public int getProviderCount(Connection connection, String proName, String proCode){
        // TODO Auto-generated method stub
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int count = 0;
        if(connection != null){
            StringBuffer sql = new StringBuffer();  //可变sql
            sql.append("select count(1) as count from smbms_provider where 1=1");
            List<Object> list = new ArrayList<Object>();
            if(!StringUtils.isNullOrEmpty(proName)){
                sql.append(" and proName like ?");
                list.add("%"+proName+"%");
            }
            if(!StringUtils.isNullOrEmpty(proCode)){
                sql.append(" and proCode like ?");
                list.add("%"+proCode+"%");
            }
            Object[] params = list.toArray();  //参数
            System.out.println("sql ----> " + sql.toString());
            rs = BaseDao.execute(connection, pstm,sql.toString(),params,rs);  //执行查询
            try {
            if(rs.next()){
                    count = rs.getInt("count");  //sql语句中使用了别名
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(null, pstm, rs);  //关闭资源
            }

        }
        return count;
    }

    @Override
	public List<Provider> getProviderList(Connection connection, String proName, String proCode,Integer currentPageNo, Integer pageSize)
			throws Exception {
		// TODO Auto-generated method stub
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<Provider> providerList = new ArrayList<Provider>();
		if(connection != null){
			StringBuffer sql = new StringBuffer();
			sql.append("select * from smbms_provider where 1=1 ");
			List<Object> list = new ArrayList<Object>();
			if(!StringUtils.isNullOrEmpty(proName)){
				sql.append(" and proName like ?");
				list.add("%"+proName+"%");
			}
			if(!StringUtils.isNullOrEmpty(proCode)){
				sql.append(" and proCode like ?");
				list.add("%"+proCode+"%");
			}
			if(currentPageNo!=null && pageSize!=null) {
                sql.append(" order by creationDate DESC limit ?,?");  //按照创建时间降序
                currentPageNo = (currentPageNo - 1) * pageSize;
                list.add(currentPageNo);  //当前页
                list.add(pageSize);  //页面大小
            }
			Object[] params = list.toArray();
			System.out.println("sql ----> " + sql.toString());
			rs = BaseDao.execute(connection, pstm,sql.toString(), params,rs);
			while(rs.next()){
				Provider _provider = new Provider();
				_provider.setId(rs.getInt("id"));
				_provider.setProCode(rs.getString("proCode"));
				_provider.setProName(rs.getString("proName"));
				_provider.setProDesc(rs.getString("proDesc"));
				_provider.setProContact(rs.getString("proContact"));
				_provider.setProPhone(rs.getString("proPhone"));
				_provider.setProAddress(rs.getString("proAddress"));
				_provider.setProFax(rs.getString("proFax"));
				_provider.setCreationDate(rs.getTimestamp("creationDate"));
				providerList.add(_provider);
			}
			BaseDao.closeResources(null, pstm, rs);
		}
		return providerList;
	}

	@Override
	public int deleteProviderById(Connection connection, String delId)
			throws Exception {
		// TODO Auto-generated method stub
		PreparedStatement pstm = null;
		int flag = 0;
		if(null != connection){
			String sql = "delete from smbms_provider where id=?";
			Object[] params = {delId};
			flag = BaseDao.execute(connection, pstm, sql, params);
			BaseDao.closeResources(null, pstm, null);
		}
		return flag;
	}

	@Override
	public Provider getProviderById(Connection connection, String id)
			throws Exception {
		// TODO Auto-generated method stub
		Provider provider = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		if(null != connection){
			String sql = "select * from smbms_provider where id=?";
			Object[] params = {id};
			rs = BaseDao.execute(connection, pstm, sql, params,rs);
			if(rs.next()){
				provider = new Provider();
				provider.setId(rs.getInt("id"));
				provider.setProCode(rs.getString("proCode"));
				provider.setProName(rs.getString("proName"));
				provider.setProDesc(rs.getString("proDesc"));
				provider.setProContact(rs.getString("proContact"));
				provider.setProPhone(rs.getString("proPhone"));
				provider.setProAddress(rs.getString("proAddress"));
				provider.setProFax(rs.getString("proFax"));
				provider.setCreatedBy(rs.getInt("createdBy"));
				provider.setCreationDate(rs.getTimestamp("creationDate"));
				provider.setModifyBy(rs.getInt("modifyBy"));
				provider.setModifyDate(rs.getTimestamp("modifyDate"));
			}
			BaseDao.closeResources(null, pstm, rs);
		}
		return provider;
	}

	@Override
	public int modify(Connection connection, Provider provider)
			throws Exception {
		// TODO Auto-generated method stub
		int flag = 0;
		PreparedStatement pstm = null;
		if(null != connection){
			String sql = "update smbms_provider set proName=?,proDesc=?,proContact=?," +
					"proPhone=?,proAddress=?,proFax=?,modifyBy=?,modifyDate=? where id = ? ";
			Object[] params = {provider.getProName(),provider.getProDesc(),provider.getProContact(),provider.getProPhone(),provider.getProAddress(),
					provider.getProFax(),provider.getModifyBy(),provider.getModifyDate(),provider.getId()};
			flag = BaseDao.execute(connection, pstm, sql, params);
			BaseDao.closeResources(null, pstm, null);
		}
		return flag;
	}

}
