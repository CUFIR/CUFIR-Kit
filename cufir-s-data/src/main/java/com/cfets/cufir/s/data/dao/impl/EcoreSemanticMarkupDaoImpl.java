package com.cfets.cufir.s.data.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;

import com.cfets.cufir.s.data.bean.EcoreSemanticMarkup;
import com.cfets.cufir.s.data.dao.EcoreSemanticMarkupDao;
import com.cfets.cufir.s.data.util.DerbyUtil;

/**
 * EcoreSemanticMarkup数据库操作
 * @author zqh
 *
 */
public class EcoreSemanticMarkupDaoImpl implements EcoreSemanticMarkupDao{

	private static Logger logger = Logger.getLogger(EcoreSemanticMarkupDaoImpl.class);

	private static Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	@Override
	public void addEcoreSemanticMarkupList(List<EcoreSemanticMarkup> ecoreSemanticMarkups) throws Exception{
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			
			String sql = "insert into ecore_semantic_markup(id,obj_id,obj_type,type,create_user,create_time,is_from_iso20022) "
					+ "values(?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			for (EcoreSemanticMarkup po : ecoreSemanticMarkups) {
				pstmt.setString(1, po.getId());
				pstmt.setString(2, po.getObjId());
				pstmt.setString(3, po.getObjType());
				pstmt.setString(4, po.getType());
				pstmt.setString(5, po.getCreateUser());
				pstmt.setTimestamp(6, new Timestamp(new java.util.Date().getTime()));
				pstmt.setString(7, po.getIsfromiso20022());
				pstmt.addBatch();
			}
			int[] result = pstmt.executeBatch();// 执行更新语句
			conn.commit(); // 提交
			if (result != null) {
				int count = 0;
				for (int i = 0; i < result.length; i++) {
					count = count + result[i];
				}
				logger.info(count + "件数据更新成功 SQL=" + sql);
			} else {
				logger.info("没有数据被更新 SQL=" + sql);
			}
		} catch (Exception e) {
			logger.error("更新数据失败!");
			conn.rollback();
			throw e;
		} finally {
			DerbyUtil.close(conn, pstmt, rs);
		}
	}
}
