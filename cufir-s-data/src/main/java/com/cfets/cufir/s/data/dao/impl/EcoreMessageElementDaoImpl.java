package com.cfets.cufir.s.data.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

import org.apache.log4j.Logger;

import com.cfets.cufir.s.data.bean.EcoreMessageElement;
import com.cfets.cufir.s.data.dao.EcoreMessageElementDao;
import com.cfets.cufir.s.data.util.DerbyUtil;

/**
 * EcoreMessageElement数据库操作
 * @author zqh
 *
 */
public class EcoreMessageElementDaoImpl implements EcoreMessageElementDao{

	private static Logger logger = Logger.getLogger(EcoreMessageElementDaoImpl.class);

	private static Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	@Override
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
