package org.cufir.s.data.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.cufir.s.ecore.bean.EcoreSemanticMarkupElement;
import org.cufir.s.data.util.DbUtil;
import org.cufir.s.data.util.DerbyUtil;

/**
 * EcoreBusinessProcess数据库操作
 * @author tangmaoquan
 * @Date 2021年10月15日
 */
public class EcoreSemanticMarkupElementImpl{
	
	private static Logger logger = Logger.getLogger(EcoreSemanticMarkupElementImpl.class);
	
	private final static String QSQL = "SELECT * FROM ECORE_SEMANTIC_MARKUP_ELEMENT t WHERE 1=1 ";

	private static Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	public List<EcoreSemanticMarkupElement> findBySemanticMarkupObjId(String objId) {
		String querySql = QSQL + "AND t.SEMANTIC_MARKUP_ID IN  ( SELECT t1.ID FROM ECORE_SEMANTIC_MARKUP t1 WHERE t1.OBJ_ID = ? ) ";
		return DbUtil.get().find(querySql, EcoreSemanticMarkupElement.class, objId);
	}
			
	public List<EcoreSemanticMarkupElement> findBySynonymSemanticMarkupObjId(String objId) {
		String querySql = QSQL + "AND t.SEMANTIC_MARKUP_ID IN  ( SELECT t1.ID FROM ECORE_SEMANTIC_MARKUP t1 WHERE t1.OBJ_ID = ?  and t1.type='Synonym' ) ";
		return DbUtil.get().find(querySql, EcoreSemanticMarkupElement.class, objId);
	}
	
	public List<EcoreSemanticMarkupElement> findAll() {
		return DbUtil.get().find(QSQL, EcoreSemanticMarkupElement.class);
	}
	
	public void addEcoreSemanticMarkupElementList(List<EcoreSemanticMarkupElement> ecoreSemanticMarkupElements) throws Exception{
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			
			String sql = "insert into ECORE_SEMANTIC_MARKUP_ELEMENT(id,name,value,semantic_markup_id,create_user,create_time,is_from_iso20022) "
					+ "values(?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			for (EcoreSemanticMarkupElement po : ecoreSemanticMarkupElements) {
				pstmt.setString(1, po.getId());
				pstmt.setString(2, po.getName());
				pstmt.setString(3, po.getValue());
				pstmt.setString(4, po.getSemanticMarkupId());
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
