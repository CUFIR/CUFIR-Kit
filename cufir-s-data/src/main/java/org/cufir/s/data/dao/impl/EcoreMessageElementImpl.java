package org.cufir.s.data.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.cufir.s.ecore.bean.EcoreMessageElement;
import org.cufir.s.ide.db.DbUtil;
import org.cufir.s.ide.db.DerbyUtil;

/**
 * EcoreMessageElement数据库操作
 */
public class EcoreMessageElementImpl{

	private static Logger logger = Logger.getLogger(EcoreMessageElementImpl.class);
	
	private final static String QSQL = "SELECT t.ID,t.DEFINITION,t.NAME,t.REGISTRATION_STATUS,t.REMOVAL_DATE," + 
			"t.OBJECT_IDENTIFIER,t.PREVIOUS_VERSION,t.XML_TAG,t.TYPE,t.TYPE_ID,t.MAX_OCCURS,t.MIN_OCCURS," + 
			"t.MESSAGE_COMPONENT_ID,t.VERSION,t.IS_FROM_ISO20022,t.IS_MESSAGE_ASSOCIATION_END,t.TRACE,t.TRACE_TYPE," + 
			"t.IS_DERIVED,t.CREATE_TIME,t.UPDATE_TIME,t.CREATE_USER,t.UPDATE_USER,t.TRACES_TO,t.TECHNICAL,t.TYPE_OF_TRACES_TO,t.TRACE_PATH" + 
			" FROM ECORE_MESSAGE_ELEMENT t WHERE 1=1 ";

	private static Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	public List<EcoreMessageElement> findByMsgComponentId(String msgComponentId) {
		return DbUtil.get().find(QSQL + "AND t.MESSAGE_COMPONENT_ID = ? ",EcoreMessageElement.class,msgComponentId);
	}
	
	public List<EcoreMessageElement> findAll() {
		return DbUtil.get().find(QSQL,EcoreMessageElement.class);
	}
	
	public EcoreMessageElement getById(String id) {
		EcoreMessageElement me = DbUtil.get().findOne(QSQL + "AND t.ID = ? ", EcoreMessageElement.class,id);
		return me == null? new EcoreMessageElement() : me;
	}
	
	public List<Map<String, Object>> getIdByMessageComponentId(String messageComponentId) {
		String sql="select id from ECORE_MESSAGE_ELEMENT where MESSAGE_COMPONENT_ID = ?";
		return DbUtil.get().find(sql, messageComponentId);
	}
	
	public Map<String, List<EcoreMessageElement>> findMdr3() {
		String sql="SELECT T.MESSAGE_COMPONENT_ID,T.NAME,T.TYPE_OF_TRACES_TO,T.TECHNICAL,T.TRACES_TO,T.TRACE_TYPE,T.TRACE,T.TRACE_PATH,T.TYPE,T.TYPE_ID FROM ECORE_MESSAGE_ELEMENT T ORDER BY T.NAME ";
		List<Map<String, Object>> list = DbUtil.get().find(sql);
		Map<String,List<EcoreMessageElement>> map = new LinkedHashMap<>();
		for(Map<String, Object> m : list) {
			String messageComponentId = m.get("message_component_id") + "";
			EcoreMessageElement me = new EcoreMessageElement();
			me.setMessageComponentId(messageComponentId);
			me.setName(m.get("name") == null ? "" : m.get("name") + "");
			me.setTypeOfTracesTo(m.get("type_of_traces_to") == null ? "" : m.get("type_of_traces_to") + "");
			me.setTechnical(m.get("technical") == null ? "" : m.get("technical") + "");
			me.setTracesTo(m.get("traces_to") == null ? "" : m.get("traces_to") + "");
			me.setTraceType(m.get("trace_type") == null ? "" : m.get("trace_type") + "");
			me.setTrace(m.get("trace") == null ? "" : m.get("trace") + "");
			me.setTracePath(m.get("trace_path") == null ? "" : m.get("trace_path") + "");
			me.setType(m.get("type") == null ? "" : m.get("type") + "");
			me.setTypeId(m.get("type_id") == null ? "" : m.get("type_id") + "");
			List<EcoreMessageElement> mes = map.get(messageComponentId);
			if(mes == null || mes.size() < 1) {
				mes = new ArrayList<>();
				mes.add(me);
				map.put(messageComponentId, mes);
			}else {
				mes.add(me);
			}
		}
		return map;
	}
	
	public void addEcoreMessageElementList(List<EcoreMessageElement> ecoreMessageElements) throws Exception{
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			
			String sql = "insert into ECORE_MESSAGE_ELEMENT(id,name,definition,registration_status,removal_date,"
					+ "object_identifier,previous_version,xml_Tag,type,type_Id,max_Occurs,min_Occurs,"
					+ "message_Component_Id,version,is_Message_Association_End,is_derived,trace_type"
					+ ",trace,technical,traces_to,type_of_traces_to,trace_path"
					+ ",create_user,create_time,is_from_iso20022) "
					+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			for (EcoreMessageElement po : ecoreMessageElements) {
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
				pstmt.setString(7, po.getPreviousVersion());
				pstmt.setString(8, po.getXmlTag());
				pstmt.setString(9, po.getType());
				pstmt.setString(10, po.getTypeId());
				if(po.getMaxOccurs()!=null) {
					pstmt.setInt(11, po.getMaxOccurs());
				} else {
					pstmt.setNull(11,Types.INTEGER);
				}
				if(po.getMinOccurs()!=null) {
					pstmt.setInt(12, po.getMinOccurs());
				} else {
					pstmt.setNull(12,Types.INTEGER);
				}
				pstmt.setString(13, po.getMessageComponentId());
				pstmt.setString(14, po.getVersion());
				pstmt.setString(15, po.getIsMessageAssociationEnd());
				pstmt.setString(16, po.getIsDerived());
				pstmt.setString(17, po.getTraceType());
				pstmt.setString(18, po.getTrace());
				pstmt.setString(19, po.getTechnical());
				pstmt.setString(20, po.getTracesTo());
				pstmt.setString(21, po.getTypeOfTracesTo());
				pstmt.setString(22, po.getTracePath());
				pstmt.setString(23, po.getCreateUser());
				pstmt.setTimestamp(24, new Timestamp(new java.util.Date().getTime()));
				pstmt.setString(25, po.getIsfromiso20022());
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
