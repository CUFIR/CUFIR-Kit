package com.cfets.cufir.s.data.dao;

import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreDoclet;

/**
 * EcoreDoclet数据库操作
 * @author zqh
 *
 */
public interface EcoreDocletDao {

	/**
	 * 保存Doclet
	 * @param ecoreDoclets
	 * @throws Exception
	 */
	public void addEcoreDocletList(List<EcoreDoclet> ecoreDoclets) throws Exception;	
}
