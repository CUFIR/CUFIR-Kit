package org.cufir.s.data.dao.impl;

import org.cufir.s.data.util.DbUtil;

/**
 * EcoreExample数据库操作
 * @author zqh
 *
 */
public class EcoreCommonImpl{

	public void deleteIso20022EcoreData() throws Exception{
		String sql1 = "delete from ECORE_BUSINESS_AREA where is_from_iso20022='1'" ;
		String sql2 = "delete from ECORE_BUSINESS_COMPONENT where is_from_iso20022='1'" ;
		String sql3 = "delete from ECORE_BUSINESS_ELEMENT where is_from_iso20022='1'" ;
		String sql4 = "delete from ECORE_BUSINESS_PROCESS where is_from_iso20022='1'" ;
		String sql5 = "delete from ECORE_BUSINESS_ROLE where is_from_iso20022='1'" ;
		String sql6 = "delete from ECORE_CODE where is_from_iso20022='1'" ;
		String sql7 = "delete from ECORE_CONSTRAINT where is_from_iso20022='1'" ;
		String sql8 = "delete from ECORE_DATA_TYPE where is_from_iso20022='1'" ;
		String sql9 = "delete from ECORE_DOCLET where is_from_iso20022='1'" ;
		String sql11 = "delete from ECORE_EXAMPLE where is_from_iso20022='1'" ;
		String sql12 = "delete from ECORE_MESSAGE_SET where is_from_iso20022='1'" ;
		String sql13 = "delete from ECORE_MESSAGE_SET_DEFINTION_RL where is_from_iso20022='1'" ;
		String sql14 = "delete from ECORE_MESSAGE_BUILDING_BLOCK where is_from_iso20022='1'" ;
		String sql15 = "delete from ECORE_MESSAGE_COMPONENT where is_from_iso20022='1'" ;
		String sql16 = "delete from ECORE_MESSAGE_DEFINITION where is_from_iso20022='1'" ;
		String sql17 = "delete from ECORE_MESSAGE_ELEMENT where is_from_iso20022='1'" ;
		String sql18 = "delete from ECORE_SEMANTIC_MARKUP where is_from_iso20022='1'" ;
		String sql19 = "delete from ECORE_SEMANTIC_MARKUP_ELEMENT where is_from_iso20022='1'" ;
		String sql20 = "delete from ECORE_EXTERNAL_SCHEMA where is_from_iso20022='1'" ;
		String sql21 = "delete from ECORE_NAMESPACE_LIST where is_from_iso20022='1'" ;
		String sql22 = "delete from ECORE_BUSINESS_COMPONENT_RL where is_from_iso20022='1'" ;
		String sql23 = "delete from ECORE_NEXT_VERSIONS where is_from_iso20022='1'";
		DbUtil.get().update(sql1);
		DbUtil.get().update(sql2);
		DbUtil.get().update(sql3);
		DbUtil.get().update(sql4);
		DbUtil.get().update(sql5);
		DbUtil.get().update(sql6);
		DbUtil.get().update(sql7);
		DbUtil.get().update(sql8);
		DbUtil.get().update(sql9);
		DbUtil.get().update(sql11);
		DbUtil.get().update(sql12);
		DbUtil.get().update(sql13);
		DbUtil.get().update(sql14);
		DbUtil.get().update(sql15);
		DbUtil.get().update(sql16);
		DbUtil.get().update(sql17);
		DbUtil.get().update(sql18);
		DbUtil.get().update(sql19);
		DbUtil.get().update(sql20);
		DbUtil.get().update(sql21);
		DbUtil.get().update(sql22);
		DbUtil.get().update(sql23);
	}
}
