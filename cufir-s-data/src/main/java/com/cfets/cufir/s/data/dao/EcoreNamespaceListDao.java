package com.cfets.cufir.s.data.dao;

import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreNamespaceList;

/**
 * EcoreNamespaceList数据库操作
 * @author zqh
 *
 */
public interface EcoreNamespaceListDao {

	/**
	 * 保存NamespaceList
	 * @param ecoreNamespaceLists
	 * @throws Exception
	 */
	public void addEcoreNamespaceListList(List<EcoreNamespaceList> ecoreNamespaceLists) throws Exception;
}
