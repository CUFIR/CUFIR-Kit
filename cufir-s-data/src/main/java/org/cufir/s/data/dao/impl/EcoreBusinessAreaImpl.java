package org.cufir.s.data.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.cufir.s.ecore.bean.EcoreBusinessArea;
import org.cufir.s.ecore.bean.EcoreConstraint;
import org.cufir.s.ecore.bean.EcoreExample;
import org.cufir.s.data.util.DerbyUtil;
import org.cufir.s.data.vo.EcoreBusinessAreaVO;

/**
 * EcoreBusinessArea数据库操作
 * @author tangmaoquan
 * @Date 2021年10月15日
 */
public class EcoreBusinessAreaImpl{

	private static Logger logger = Logger.getLogger(EcoreBusinessAreaImpl.class);

	private static Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	/**
	 * 保存业务领域信息
	 * @param ecoreBusinessAreas
	 * @throws Exception
	 */
	public void addEcoreBusinessAreaList(List<EcoreBusinessArea> ecoreBusinessAreas) throws Exception{
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			
			String sql = "insert into ecore_business_area(id,name,definition,registration_status,removal_date,"
					+ "object_identifier,code,create_user,create_time,is_from_iso20022) "
					+ "values(?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			for (EcoreBusinessArea po : ecoreBusinessAreas) {
				pstmt.setString(1, po.getId());
				pstmt.setString(2, po.getName());
				pstmt.setString(3, po.getDefinition());
				pstmt.setString(4, po.getRegistrationStatus());
				if(po.getRemovalDate()!=null) {
					pstmt.setDate(5, new Date(po.getRemovalDate().getTime()));
				} else {
					pstmt.setNull(5, Types.DATE);
				}
				pstmt.setString(6, po.getObjectIdentifier());
				pstmt.setString(7, po.getCode());
				pstmt.setString(8, po.getCreateUser());
				pstmt.setTimestamp(9, new Timestamp(new java.util.Date().getTime()));
				pstmt.setString(10, po.getIsfromiso20022());
				pstmt.addBatch();
			}
			pstmt.executeBatch();// 执行更新语句
			conn.commit(); // 提交
		} catch (Exception e) {
			logger.error("更新数据失败!");
			conn.rollback();
			throw e;
		} finally {
			DerbyUtil.close(conn, pstmt, rs);
		}
	}
	
	/**
	 * 删除业务领域
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteEcoreBusinessArea(String id) throws Exception{
		boolean key=false;
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			//saveRemovedInfo(id);
			String sql = "delete from ECORE_BUSINESS_AREA where id=?" ;
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
	 * 更新业务领域信息
	 * @param ecoreBusinessAreas
	 * @throws Exception
	 */
	public void updateEcoreBusinessAreaList(List<EcoreBusinessArea> ecoreBusinessAreas) throws Exception{
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			
			String sql = "update ecore_business_area set update_time=?,name=?,definition=?,registration_status=?,removal_date=?,"
					+ "object_identifier=?,code=?, update_user=?,is_from_iso20022=? where id=?";
			pstmt = conn.prepareStatement(sql);
			for (EcoreBusinessArea po : ecoreBusinessAreas) {
				pstmt.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
				pstmt.setString(2, po.getName());
				pstmt.setString(3, po.getDefinition());
				pstmt.setString(4, po.getRegistrationStatus());
				if(po.getRemovalDate()!=null) {
					pstmt.setDate(5, new Date(po.getRemovalDate().getTime()));
				} else {
					pstmt.setNull(5, Types.DATE);
				}
				pstmt.setString(6, po.getObjectIdentifier());
				pstmt.setString(7, po.getCode());
				pstmt.setString(8, po.getUpdateUser());
				pstmt.setString(9, po.getIsfromiso20022());
				pstmt.setString(10, po.getId());
				pstmt.addBatch();
			}
			pstmt.executeBatch();// 执行更新语句
			conn.commit(); // 提交
		} catch (Exception e) {
			logger.error("更新数据失败!");
			conn.rollback();
			throw e;
		} finally {
			DerbyUtil.close(conn, pstmt, rs);
		}
	}

	/**
	 * 保存EcoreBusinessArea数据
	 * @param BusinessAreaVOs
	 */
	public boolean saveBusinessAreaVOList(List<EcoreBusinessAreaVO> businessAreaVOs) throws Exception{
		boolean key=false;
		List<EcoreBusinessArea> ecoreBusinessAreas=new ArrayList<EcoreBusinessArea>();
		List<String> dataIds=new ArrayList<String>();
		List<EcoreConstraint> ecoreConstraints=new ArrayList<EcoreConstraint>();
		List<EcoreExample> ecoreExamples=new ArrayList<EcoreExample>();
		Map<String,List<String>> areaIdMap=new HashMap<String,List<String>>();
		Map<String,String> areaMap=new HashMap<String,String>();
		for(EcoreBusinessAreaVO vo: businessAreaVOs) {
			EcoreBusinessArea ecoreBusinessArea=vo.getEcoreBusinessArea();
			List<EcoreConstraint> constraints=vo.getEcoreConstraints();
			List<EcoreExample> examples=vo.getEcoreExamples();
			dataIds.add(ecoreBusinessArea.getId());
			if(constraints!=null && !constraints.isEmpty()) {
				ecoreConstraints.addAll(constraints);
			}
			if(examples!=null && !examples.isEmpty()) {
				ecoreExamples.addAll(examples);
			}
			ecoreBusinessAreas.add(ecoreBusinessArea);
			if(vo.getEcoreMessageDefinitionIds()!=null && !vo.getEcoreMessageDefinitionIds().isEmpty()) {
				areaIdMap.put(ecoreBusinessArea.getId(), vo.getEcoreMessageDefinitionIds());
			}
			areaMap.put(ecoreBusinessArea.getId(), ecoreBusinessArea.getCode());
		}
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			if(dataIds!=null && !dataIds.isEmpty()) {
				for(String id:dataIds) {
					//先删除业务领域下面的所有数据
					deleteMessageAreaByIds(id);
				}
			}
			//开始保存
			saveBusinessArea(ecoreBusinessAreas);
			if(areaIdMap!=null && !areaIdMap.isEmpty()) {
				updateMessageDefinition(areaIdMap,areaMap);
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

	private void updateMessageDefinition(Map<String, List<String>> areaIdMap,Map<String,String> areaMap) throws SQLException {
		String sql="update ecore_message_definition set business_area_id=? , business_area=? where id=?";
		pstmt=conn.prepareStatement(sql);
		for(String key:areaIdMap.keySet()) {
			List<String> ids=areaIdMap.get(key);
			if(ids!=null && !ids.isEmpty()) {
				for(String s:ids) {
					pstmt.setString(1, key);
					pstmt.setString(2, areaMap.get(key));
					pstmt.setString(3, s);
					pstmt.addBatch();
				}
			}
		}
		pstmt.executeBatch();// 执行更新语句
	}

	private void saveBusinessArea(List<EcoreBusinessArea> ecoreBusinessAreas) throws Exception{
		List<EcoreBusinessArea> addEcoreBusinessAreas=new ArrayList<EcoreBusinessArea>();
		List<EcoreBusinessArea> updateEcoreBusinessAreas=new ArrayList<EcoreBusinessArea>();
		for(EcoreBusinessArea po : ecoreBusinessAreas) {
			String csql = "SELECT T.ID FROM ecore_business_area T WHERE id=?";
			pstmt=conn.prepareStatement(csql);
			pstmt.setString(1, po.getId());
			rs=pstmt.executeQuery();
			if(rs.next()) {
				updateEcoreBusinessAreas.add(po);
			}else {
				addEcoreBusinessAreas.add(po);
			}
		}
		String sql = "insert into ecore_business_area(id,name,definition,registration_status,removal_date,"
				+ "object_identifier,code,create_user,create_time,is_from_iso20022) "
				+ "values(?,?,?,?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		for (EcoreBusinessArea po : addEcoreBusinessAreas) {
			pstmt.setString(1, po.getId());
			pstmt.setString(2, po.getName());
			pstmt.setString(3, po.getDefinition());
			pstmt.setString(4, po.getRegistrationStatus());
			if(po.getRemovalDate()!=null) {
				pstmt.setDate(5, new Date(po.getRemovalDate().getTime()));
			} else {
				pstmt.setNull(5, Types.DATE);
			}
			pstmt.setString(6, po.getObjectIdentifier());
			pstmt.setString(7, po.getCode());
			pstmt.setString(8, po.getCreateUser());
			pstmt.setTimestamp(9, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(10, po.getIsfromiso20022());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
		String usql = "update ecore_business_area set update_time=?,name=?,definition=?,registration_status=?,removal_date=?,"
				+ "object_identifier=?,code=?, update_user=?,is_from_iso20022=? where id=?";
		pstmt = conn.prepareStatement(usql);
		for (EcoreBusinessArea po : updateEcoreBusinessAreas) {
			pstmt.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(2, po.getName());
			pstmt.setString(3, po.getDefinition());
			pstmt.setString(4, po.getRegistrationStatus());
			if(po.getRemovalDate()!=null) {
				pstmt.setDate(5, new Date(po.getRemovalDate().getTime()));
			} else {
				pstmt.setNull(5, Types.DATE);
			}
			pstmt.setString(6, po.getObjectIdentifier());
			pstmt.setString(7, po.getCode());
			pstmt.setString(8, po.getUpdateUser());
			pstmt.setString(9, po.getIsfromiso20022());
			pstmt.setString(10, po.getId());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
	}

	private void deleteMessageAreaByIds(String id) throws Exception{
		String usql="update ecore_message_definition set business_area_id=null ,"
				+ " business_area=null where business_area_id=?";
		String csql = "delete from ECORE_CONSTRAINT where obj_id=?";
		String eeql = "delete from ECORE_EXAMPLE where obj_id=?";
		pstmt = conn.prepareStatement(usql);
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
