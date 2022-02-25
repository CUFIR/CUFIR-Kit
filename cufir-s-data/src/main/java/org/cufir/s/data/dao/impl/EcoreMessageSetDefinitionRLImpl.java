package org.cufir.s.data.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.cufir.s.ecore.bean.EcoreMessageDefinition;
import org.cufir.s.ecore.bean.EcoreMessageSetDefinitionRL;
import org.cufir.s.data.util.DbUtil;
import org.cufir.s.data.util.DerbyUtil;

/**
 * EcoreMessageSetDefinitionRL数据库操作
 * @author tangmaoquan
 * @Date 2021年10月15日
 */
public class EcoreMessageSetDefinitionRLImpl{

	private static Logger logger = Logger.getLogger(EcoreMessageSetDefinitionRLImpl.class);

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	public List<Map<String, Object>> findMessageIdBySetId(String setId) {
		String sql="select MESSAGE_ID from ECORE_MESSAGE_SET_DEFINTION_RL where set_id = ?";
		return DbUtil.get().find(sql, setId);
	}
	
	public Integer countByMessageId(String messageId) {
		String sql="select count(1) from ECORE_MESSAGE_SET_DEFINTION_RL where message_id = ?";
		return DbUtil.get().count(sql, messageId);
	}
	
	public List<String> findSetIdByMsgDefinitions(List<EcoreMessageDefinition> msgDefinitionList) {
		StringBuilder sb = new StringBuilder();
		for (EcoreMessageDefinition definition: msgDefinitionList) {
			sb.append("'" + definition.getId() + "'");
			sb.append(",");
		}
		String definitionIds = sb.substring(0, sb.length() - 1);
		String querySql = "SELECT t.SET_ID FROM ECORE_MESSAGE_SET_DEFINTION_RL t WHERE t.MESSAGE_ID in (" + definitionIds + " ) ";
		List<Map<String, Object>> maps = DbUtil.get().find(querySql);
		return maps.stream().map(map -> map.get("SET_ID") + "").collect(Collectors.toList());
	}
	
	public void addEcoreMessageSetDefinitionRLList(List<EcoreMessageSetDefinitionRL> ecoreMessageSetDefinitionRLs) throws Exception{
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			
			String sql = "insert into ECORE_MESSAGE_SET_DEFINTION_RL(message_id,set_id,create_user,create_time,is_from_iso20022) "
					+ "values(?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			for (EcoreMessageSetDefinitionRL po : ecoreMessageSetDefinitionRLs) {
				pstmt.setString(1, po.getMessageId());
				pstmt.setString(2, po.getSetId());
				pstmt.setString(3, po.getCreateUser());
				pstmt.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
				pstmt.setString(5, po.getIsfromiso20022());
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
