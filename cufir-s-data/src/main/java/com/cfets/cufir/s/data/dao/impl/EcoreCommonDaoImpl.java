package com.cfets.cufir.s.data.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.cfets.cufir.s.data.dao.EcoreCommonDao;
import com.cfets.cufir.s.data.util.DerbyUtil;

/**
 * EcoreExample数据库操作
 * @author zqh
 *
 */
public class EcoreCommonDaoImpl implements EcoreCommonDao{

	private static Logger logger = Logger.getLogger(EcoreCommonDaoImpl.class);

	private Connection conn = null;
	private Statement s = null;
	private ResultSet rs = null;

	@Override
	public boolean deleteIso20022EcoreData() throws Exception{
		boolean key=false;
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			
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
			s = conn.createStatement();
			s.addBatch(sql1);
			s.addBatch(sql2);
			s.addBatch(sql3);
			s.addBatch(sql4);
			s.addBatch(sql5);
			s.addBatch(sql6);
			s.addBatch(sql7);
			s.addBatch(sql8);
			s.addBatch(sql9);
			s.addBatch(sql11);
			s.addBatch(sql12);
			s.addBatch(sql13);
			s.addBatch(sql14);
			s.addBatch(sql15);
			s.addBatch(sql16);
			s.addBatch(sql17);
			s.addBatch(sql18);
			s.addBatch(sql19);
			s.addBatch(sql20);
			s.addBatch(sql21);
			s.addBatch(sql22);
			s.addBatch(sql23);
			s.executeBatch();
			key=true;
			conn.commit();
		} catch (Exception e) {
			logger.error("更新数据失败!");
			conn.rollback();
			throw e;
		} finally {
			DerbyUtil.close(conn, s, rs);
		}
		return key;
	}
}
