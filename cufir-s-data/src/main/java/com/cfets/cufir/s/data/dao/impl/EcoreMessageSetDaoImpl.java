package com.cfets.cufir.s.data.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cfets.cufir.s.data.bean.EcoreConstraint;
import com.cfets.cufir.s.data.bean.EcoreExample;
import com.cfets.cufir.s.data.bean.EcoreMessageSet;
import com.cfets.cufir.s.data.bean.EcoreMessageSetDefinitionRL;
import com.cfets.cufir.s.data.dao.EcoreMessageSetDao;
import com.cfets.cufir.s.data.util.DerbyUtil;
import com.cfets.cufir.s.data.vo.EcoreMessageSetVO;

/**
 * EcoreMessageSet数据库操作
 * @author zqh
 *
 */
public class EcoreMessageSetDaoImpl implements EcoreMessageSetDao{

	private static Logger logger = Logger.getLogger(EcoreMessageSetDaoImpl.class);

	private static Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	@Override
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
	
	private void saveRemovedInfo(String id) throws Exception{
		try {
			String sql = "insert into ECORE_REMOVED_INFO(obj_id,obj_type,create_time) "
					+ "values(?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, "7");
			pstmt.setTimestamp(3, new Timestamp(new java.util.Date().getTime()));
			pstmt.executeUpdate();// 执行更新语句
		} catch (Exception e) {
			logger.error("更新数据失败!");
			conn.rollback();
			throw e;
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
			pstmt.setString(2, po.getObj_id());
			pstmt.setString(3, po.getObj_type());
			pstmt.setString(4, po.getExample());
			pstmt.setString(5, po.getCreateUser());
			pstmt.setTimestamp(6, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(7, po.getIsfromiso20022());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
	}
}
