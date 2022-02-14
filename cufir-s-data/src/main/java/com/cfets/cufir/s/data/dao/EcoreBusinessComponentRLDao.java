package com.cfets.cufir.s.data.dao;

import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreBusinessComponentRL;

/**
 * EcoreBusinessComponentRL数据库操作
 * @author zqh
 *
 */
public interface EcoreBusinessComponentRLDao {

	/**
	 * 保存关联
	 * @param ecoreBusinessComponentRLs
	 * @throws Exception
	 */
	public void addEcoreBusinessComponentRLList(List<EcoreBusinessComponentRL> ecoreBusinessComponentRLs) throws Exception;
	
	/**
	 * 删除业务关联
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteEcoreBusinessComponentRL(String id) throws Exception;

	/**
	 * 保存关联
	 * @param ecoreBusinessComponentRLs
	 * @throws Exception
	 */
	public boolean saveEcoreBusinessComponentRL(List<EcoreBusinessComponentRL> ecoreBusinessComponentRLs) throws Exception;
}
