package com.cfets.cufir.s.data.dao;

import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreCode;

/**
 * EcoreCode数据库操作
 * @author zqh
 *
 */
public interface EcoreCodeDao {

	/**
	 * 保存Code
	 * @param ecoreCodes
	 * @throws Exception
	 */
	public void addEcoreCodeList(List<EcoreCode> ecoreCodes) throws Exception;
}
