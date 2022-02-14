package com.cfets.cufir.s.data.dao;

import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreSemanticMarkup;

/**
 * EcoreSemanticMarkup数据库操作
 * @author zqh
 *
 */
public interface EcoreSemanticMarkupDao {

	/**
	 * 保存SemanticMarkup
	 * @param ecoreSemanticMarkups
	 * @throws Exception
	 */
	public void addEcoreSemanticMarkupList(List<EcoreSemanticMarkup> ecoreSemanticMarkups) throws Exception;
}
