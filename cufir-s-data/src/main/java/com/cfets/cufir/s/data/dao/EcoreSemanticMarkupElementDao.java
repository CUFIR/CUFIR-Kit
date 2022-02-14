package com.cfets.cufir.s.data.dao;

import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreSemanticMarkupElement;

/**
 * EcoreSemanticMarkupElement数据库操作
 * @author zqh
 *
 */
public interface EcoreSemanticMarkupElementDao {

	/**
	 * 保存SemanticMarkupElement
	 * @param ecoreSemanticMarkupElements
	 * @throws Exception
	 */
	public void addEcoreSemanticMarkupElementList(List<EcoreSemanticMarkupElement> ecoreSemanticMarkupElements) throws Exception;
}
