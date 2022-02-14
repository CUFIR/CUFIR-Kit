package com.cfets.cufir.s.data.dao;

import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreNextVersions;

/**
 * EcoreNextVersions数据库操作
 * @author zqh
 *
 */
public interface EcoreNextVersionsDao {

	/**
	 * 保存NextVersions
	 * @param ecoreNextVersionss
	 * @throws Exception
	 */
	public void addEcoreNextVersionsList(List<EcoreNextVersions> ecoreNextVersionss) throws Exception;
}
