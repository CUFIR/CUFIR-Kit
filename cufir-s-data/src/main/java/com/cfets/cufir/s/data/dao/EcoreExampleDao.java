package com.cfets.cufir.s.data.dao;

import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreExample;

/**
 * EcoreExample数据库操作
 * @author zqh
 *
 */
public interface EcoreExampleDao {

	/**
	 * 保存Example
	 * @param ecoreExamples
	 * @throws Exception
	 */
	public void addEcoreExampleList(List<EcoreExample> ecoreExamples) throws Exception;
}
