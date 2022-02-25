package org.cufir.s.data.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.cufir.s.ecore.bean.EcoreBusinessElement;
import org.cufir.s.data.util.DbUtil;
import org.cufir.s.data.util.DerbyUtil;

/**
 * EcoreBusinessElement数据库操作
 * @author tangmaoquan
 * @Date 2021年10月15日
 */
public class EcoreBusinessElementImpl{

	private static Logger logger = Logger.getLogger(EcoreBusinessElementImpl.class);
	
	private final static String QSQL = "SELECT * FROM ECORE_BUSINESS_ELEMENT t WHERE 1=1 ";

	private static Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	public List<EcoreBusinessElement> findAll(){
		return DbUtil.get().find(QSQL + "ORDER BY NAME" ,EcoreBusinessElement.class);
	}
	
	public List<EcoreBusinessElement> findMdr3() {
		String sql = "select id, name, definition, type, type_id, business_component_id from ecore_business_element order by LOWER(name)";
		List<Map<String, Object>> maps = DbUtil.get().find(sql);
		List<EcoreBusinessElement> list = new ArrayList<EcoreBusinessElement>();
		for(Map<String, Object> m : maps) {
			EcoreBusinessElement be = new EcoreBusinessElement();
			be.setId(m.get("id") == null ? "" : m.get("id") + "");
			be.setName(m.get("name") == null ? "" : m.get("name") + "");
			be.setDefinition(m.get("definition") == null ? "" : m.get("definition") + "");
			be.setType(m.get("type") == null ? "" : m.get("type") + "");
			be.setTypeId(m.get("type_id") == null ? "" : m.get("type_id") + "");
			be.setBusinessComponentId(m.get("business_component_id") == null ? "" : m.get("business_component_id") + "");
			list.add(be);
		}
		return list;
	}
	
	public void addEcoreBusinessElementList(List<EcoreBusinessElement> ecoreBusinessElements) throws Exception{
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			
			String sql = "insert into ECORE_BUSINESS_ELEMENT(id,name,definition,registration_status,removal_date,"
					+ "object_identifier,previous_version,type,type_Id,max_Occurs,min_Occurs,"
					+ "business_component_id,version,is_derived,is_Message_Association_End,create_user,create_time,is_from_iso20022) "
					+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			for (EcoreBusinessElement po : ecoreBusinessElements) {
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
				pstmt.setString(7, po.getPreviousVersion());
				pstmt.setString(8, po.getType());
				pstmt.setString(9, po.getTypeId());
				if(po.getMaxOccurs()!=null) {
					pstmt.setInt(10, po.getMaxOccurs());
				} else {
					pstmt.setNull(10, Types.INTEGER);
				}
				if(po.getMinOccurs()!=null) {
					pstmt.setInt(11, po.getMinOccurs());
				} else {
					pstmt.setNull(11, Types.INTEGER);
				}
				pstmt.setString(12, po.getBusinessComponentId());
				pstmt.setString(13, po.getVersion());
				pstmt.setString(14, po.getIsDerived());
				pstmt.setString(15, po.getIsMessageAssociationEnd());
				pstmt.setString(16, po.getCreateUser());
				pstmt.setTimestamp(17, new Timestamp(new java.util.Date().getTime()));
				pstmt.setString(18, po.getIsfromiso20022());
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
}
