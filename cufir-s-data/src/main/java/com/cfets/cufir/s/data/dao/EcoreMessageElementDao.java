package com.cfets.cufir.s.data.dao;

import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreMessageElement;

/**
 * EcoreMessageElement数据库操作
 * @author zqh
 *
 */
public interface EcoreMessageElementDao {

	/**
	 * 保存消息元素
	 * @param ecoreMessageElements
	 * @throws Exception
	 */
	public void addEcoreMessageElementList(List<EcoreMessageElement> ecoreMessageElements) throws Exception;
}
