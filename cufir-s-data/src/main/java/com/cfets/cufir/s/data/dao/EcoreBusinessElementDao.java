package com.cfets.cufir.s.data.dao;

import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreBusinessElement;

/**
 * EcoreBusinessElement数据库操作
 * @author zqh
 *
 */
public interface EcoreBusinessElementDao {

	/**
	 * 保存BusinessElement
	 * @param ecoreBusinessElements
	 * @throws Exception
	 */
	public void addEcoreBusinessElementList(List<EcoreBusinessElement> ecoreBusinessElements) throws Exception;
}
