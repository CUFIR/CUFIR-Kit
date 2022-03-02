package org.cufir.s.data.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.cufir.s.ecore.bean.EcoreConstraint;
import org.cufir.s.ecore.bean.EcoreExample;
import org.cufir.s.ecore.bean.EcoreMessageDefinition;
import org.cufir.s.ecore.bean.EcoreMessageSet;
import org.cufir.s.ecore.bean.EcoreMessageSetDefinitionRL;
import org.cufir.s.ide.db.DbUtil;
import org.cufir.s.ide.db.DerbyUtil;
import org.cufir.s.data.vo.EcoreMessageSetVO;

/**
 * EcoreMessageSet数据库操作
 */
public class EcoreMessageSetImpl{

	private static Logger logger = Logger.getLogger(EcoreMessageSetImpl.class);
	
	private final static String QSQL = "SELECT * FROM ECORE_MESSAGE_SET t WHERE 1=1 ";

	private static Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	/**
	 * 根据id获取报文集
	 * @param msgSetId
	 * @return
	 */
	public List<EcoreMessageSet> findAll() {
		return DbUtil.get().find(QSQL + "ORDER BY NAME",EcoreMessageSet.class);
	}
	
	/**
	 * 根据id获取报文集
	 * @param msgSetId
	 * @return
	 */
	public EcoreMessageSet getById(String msgSetId) {
		EcoreMessageSet ms = DbUtil.get().findOne(QSQL + "AND t.ID = ? ",EcoreMessageSet.class,msgSetId);
		return ms == null? new EcoreMessageSet() : ms;
	}
	
	/**
	 * 根据报文id获取报文集
	 * @param msgSetId
	 * @return
	 */
	public List<EcoreMessageSet> findByMsgDefinitionId(String msgDefinitionId) {
		String querySql = QSQL + "AND t.ID IN (SELECT t1.SET_ID FROM ECORE_MESSAGE_SET_DEFINTION_RL t1 WHERE t1.MESSAGE_ID = ?) ORDER BY t.NAME ";
		return DbUtil.get().find(querySql,EcoreMessageSet.class,msgDefinitionId);
	}
	
	/**
	 * 根据多个报文获取报文集
	 * @param msgDefinitions
	 * @return
	 */
	public List<EcoreMessageSet> findByMsgDefinitions(List<EcoreMessageDefinition> msgDefinitions) {
		if (msgDefinitions.size() == 0) {
			return new ArrayList<EcoreMessageSet>();
		}
		StringBuilder sb = new StringBuilder();
		for (EcoreMessageDefinition msgDefinition: msgDefinitions) {
			sb.append("'");
			sb.append(msgDefinition.getId());
			sb.append("'");
			sb.append(",");
		}
		String msgDefinitionIds = sb.substring(0, sb.length() - 1);
		String querySql = " SELECT DISTINCT t2.* FROM ECORE_MESSAGE_SET t2 WHERE t2.ID IN (SELECT t1.SET_ID FROM ECORE_MESSAGE_SET_DEFINTION_RL t1 WHERE t1.MESSAGE_ID IN ( " + msgDefinitionIds + " ) ) ORDER BY t2.NAME ";

		return DbUtil.get().find(querySql,EcoreMessageSet.class);
	}
	
	/**
	 * 报文id查报文集id,名称
	 * @param messageId
	 * @return
	 */
	public EcoreMessageSet getMsgSetNameByMessageId(String messageId) {
		String sql="SELECT B.ID, B.NAME FROM ECORE_MESSAGE_SET_DEFINTION_RL A INNER JOIN ECORE_MESSAGE_SET B ON B.ID = A.SET_ID WHERE A.MESSAGE_ID = ? ";
		List<Map<String, Object>> list = DbUtil.get().find(sql, messageId);
		if(list != null && list.size() > 0) {
			Map<String, Object> map = list.get(0);
			EcoreMessageSet ecoreMessageSet = new EcoreMessageSet();
			ecoreMessageSet.setId(map.get("id") + "");
			ecoreMessageSet.setName(map.get("name") + "");
			return ecoreMessageSet;
		}
		return null;
	}
	
	public void addEcoreMessageSetList(List<EcoreMessageSet> ecoreMessageSets) throws Exception{
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			
			String sql = "insert into ecore_message_set(id,name,definition,registration_status,removal_date,"
					+ "object_identifier,create_user,create_time,is_from_iso20022) "
					+ "values(?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			for (EcoreMessageSet po : ecoreMessageSets) {
				pstmt.setString(1, po.getId());
				pstmt.setString(2, po.getName());
				pstmt.setString(3, po.getDefinition());
				pstmt.setString(4, po.getRegistrationStatus());
				if(po.getRemovalDate()!=null) {
					pstmt.setDate(5, new Date(po.getRemovalDate().getTime()));
				} else {
					pstmt.setNull(5,Types.DATE);
				}
				pstmt.setString(6, po.getObjectIdentifier());
				pstmt.setString(7, po.getCreateUser());
				pstmt.setTimestamp(8, new Timestamp(new java.util.Date().getTime()));
				pstmt.setString(9, po.getIsfromiso20022());
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
	
	/**
	 * 删除消息集
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteEcoreMessageSet(String id) throws Exception{
		boolean key=false;
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			//saveRemovedInfo(id);
			String sql = "delete from ECORE_MESSAGE_SET where id=?" ;
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			int num=pstmt.executeUpdate();
			conn.commit();
			if(num>0) {
				key=true;
			}
		} catch (Exception e) {
			logger.error("删除数据失败!");
			conn.rollback();
			throw e;
		} finally {
			DerbyUtil.close(conn, pstmt, rs);
		}
		return key;
	}
	
	/**
	 * 保存EcoreMessageSet数据
	 * @param messageSetVOs
	 */
	public boolean saveMessageSetVOList(List<EcoreMessageSetVO> messageSetVOs) throws Exception{
		boolean key=false;
		List<EcoreMessageSet> ecoreMessageSets=new ArrayList<EcoreMessageSet>();
		List<String> dataIds=new ArrayList<String>();
		List<EcoreConstraint> ecoreConstraints=new ArrayList<EcoreConstraint>();
		List<EcoreExample> ecoreExamples=new ArrayList<EcoreExample>();
		List<EcoreMessageSetDefinitionRL> ecoreMessageSetDefinitionRLs=new ArrayList<EcoreMessageSetDefinitionRL>();
		for(EcoreMessageSetVO vo: messageSetVOs) {
			EcoreMessageSet ecoreMessageSet=vo.getEcoreMessageSet();
			List<EcoreConstraint> constraints=vo.getEcoreConstraints();
			List<EcoreExample> examples=vo.getEcoreExamples();
			dataIds.add(ecoreMessageSet.getId());
			if(vo.getEcoreMessageSetDefinitionRLs()!=null && !vo.getEcoreMessageSetDefinitionRLs().isEmpty()) {
				ecoreMessageSetDefinitionRLs.addAll(vo.getEcoreMessageSetDefinitionRLs());
			}
			if(constraints!=null && !constraints.isEmpty()) {
				ecoreConstraints.addAll(constraints);
			}
			if(examples!=null && !examples.isEmpty()) {
				ecoreExamples.addAll(examples);
			}
			ecoreMessageSets.add(ecoreMessageSet);
		}
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			if(dataIds!=null && !dataIds.isEmpty()) {
				for(String id:dataIds) {
					//先删除数据类型下面的所有数据
					deleteMessageSetByIds(id);
				}
			}
			//开始保存
			saveMessageSet(ecoreMessageSets);
			if(ecoreMessageSetDefinitionRLs!=null && !ecoreMessageSetDefinitionRLs.isEmpty()) {
				saveMessageSetDefinitionRL(ecoreMessageSetDefinitionRLs);
			}
			if(ecoreExamples!=null && !ecoreExamples.isEmpty()) {
				saveExample(ecoreExamples);
			}
			if(ecoreConstraints!=null && !ecoreConstraints.isEmpty()) {
				saveConstraint(ecoreConstraints);
			}
			conn.commit();
		} catch (Exception e) {
			logger.error("更新数据失败!");
			conn.rollback();
			throw e;
		} finally {
			DerbyUtil.close(conn, pstmt, rs);
		}
		key=true;
		return key;
	}

	private void saveMessageSetDefinitionRL(List<EcoreMessageSetDefinitionRL> ecoreMessageSetDefinitionRLs) throws Exception{
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
		pstmt.executeBatch();// 执行更新语句
	}

	private void saveMessageSet(List<EcoreMessageSet> ecoreMessageSets) throws Exception{
		List<EcoreMessageSet> addEcoreMessageSets=new ArrayList<EcoreMessageSet>();
		List<EcoreMessageSet> updateEcoreMessageSets=new ArrayList<EcoreMessageSet>();
		for(EcoreMessageSet po : ecoreMessageSets) {
			String csql = "SELECT T.ID FROM ecore_message_set T WHERE id=?";
			pstmt=conn.prepareStatement(csql);
			pstmt.setString(1, po.getId());
			rs=pstmt.executeQuery();
			if(rs.next()) {
				updateEcoreMessageSets.add(po);
			}else {
				addEcoreMessageSets.add(po);
			}
		}
		String sql = "insert into ecore_message_set(id,name,definition,registration_status,removal_date,"
				+ "object_identifier,create_user,create_time,is_from_iso20022) "
				+ "values(?,?,?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		for (EcoreMessageSet po : addEcoreMessageSets) {
			pstmt.setString(1, po.getId());
			pstmt.setString(2, po.getName());
			pstmt.setString(3, po.getDefinition());
			pstmt.setString(4, po.getRegistrationStatus());
			if(po.getRemovalDate()!=null) {
				pstmt.setDate(5, new Date(po.getRemovalDate().getTime()));
			} else {
				pstmt.setNull(5,Types.DATE);
			}
			pstmt.setString(6, po.getObjectIdentifier());
			pstmt.setString(7, po.getCreateUser());
			pstmt.setTimestamp(8, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(9, po.getIsfromiso20022());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
		String usql = "update ecore_message_set set update_time=?,name=?,definition=?,registration_status=?,removal_date=?,"
				+ "object_identifier=?, update_user=?,is_from_iso20022=? where id=?";
		pstmt = conn.prepareStatement(usql);
		for (EcoreMessageSet po : updateEcoreMessageSets) {
			pstmt.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(2, po.getName());
			pstmt.setString(3, po.getDefinition());
			pstmt.setString(4, po.getRegistrationStatus());
			if(po.getRemovalDate()!=null) {
				pstmt.setDate(5, new Date(po.getRemovalDate().getTime()));
			} else {
				pstmt.setNull(5,Types.DATE);
			}
			pstmt.setString(6, po.getObjectIdentifier());
			pstmt.setString(7, po.getUpdateUser());
			pstmt.setString(8, po.getIsfromiso20022());
			pstmt.setString(9, po.getId());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
	}

	private void deleteMessageSetByIds(String id) throws Exception{
		String sql = "delete from ecore_message_set_defintion_rl where set_id=?" ;
		String csql = "delete from ECORE_CONSTRAINT where obj_id=?";
		String eeql = "delete from ECORE_EXAMPLE where obj_id=?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, id);
		pstmt.executeUpdate();
		
		pstmt = conn.prepareStatement(csql);
		pstmt.setString(1, id);
		pstmt.executeUpdate();
		
		pstmt = conn.prepareStatement(eeql);
		pstmt.setString(1, id);
		pstmt.executeUpdate();
	}
	
	private void saveConstraint(List<EcoreConstraint> ecoreConstraints) throws SQLException{
		String sql = "insert into ecore_constraint(id,obj_id,obj_type,expression_language,definition"
				+ ",name,registration_status,removal_date,object_identifier,create_user,create_time,is_from_iso20022,expression) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		for (EcoreConstraint po : ecoreConstraints) {
			pstmt.setString(1, po.getId());
			pstmt.setString(2, po.getObj_id());
			pstmt.setString(3, po.getObj_type());
			pstmt.setString(4,po.getExpressionlanguage());
			if(po.getDefinition()!=null) {
				pstmt.setString(5, po.getDefinition());
			} else {
				pstmt.setString(5, "");
			}
			pstmt.setString(6, po.getName());
			pstmt.setString(7, po.getRegistrationStatus());
			if(po.getRemovalDate()!=null) {
				pstmt.setDate(8, new Date(po.getRemovalDate().getTime()));
			} else {
				pstmt.setNull(8,Types.DATE);
			}
			pstmt.setString(9, po.getObjectIdentifier());
			pstmt.setString(10, po.getCreateUser());
			pstmt.setTimestamp(11, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(12, po.getIsfromiso20022());
			if(po.getExpression()!=null) {
				pstmt.setString(13,po.getExpression());
			}else {
				pstmt.setString(13, "");
			}
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
	}
	
	private void saveExample(List<EcoreExample> ecoreExamples) throws SQLException{
		String sql = "insert into ecore_example(id,obj_id,obj_type,example,create_user,create_time,is_from_iso20022) "
				+ "values(?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		for (EcoreExample po : ecoreExamples) {
			pstmt.setString(1, po.getId());
			pstmt.setString(2, po.getObjId());
			pstmt.setString(3, po.getObjType());
			pstmt.setString(4, po.getExample());
			pstmt.setString(5, po.getCreateUser());
			pstmt.setTimestamp(6, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(7, po.getIsfromiso20022());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
	}
}
