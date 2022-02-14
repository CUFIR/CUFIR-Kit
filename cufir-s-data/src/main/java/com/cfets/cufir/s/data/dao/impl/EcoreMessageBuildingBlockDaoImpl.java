package com.cfets.cufir.s.data.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

import org.apache.log4j.Logger;

import com.cfets.cufir.s.data.bean.EcoreMessageBuildingBlock;
import com.cfets.cufir.s.data.dao.EcoreMessageBuildingBlockDao;
import com.cfets.cufir.s.data.util.DerbyUtil;

/**
 * EcoreMessageBuildingBlock数据库操作
 * @author zqh
 *
 */
public class EcoreMessageBuildingBlockDaoImpl implements EcoreMessageBuildingBlockDao{

	private static Logger logger = Logger.getLogger(EcoreMessageBuildingBlockDaoImpl.class);

	private static Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	@Override
	public void addEcoreMessageBuildingBlockList(List<EcoreMessageBuildingBlock> ecoreMessageBuildingBlocks) throws Exception{
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			
			String sql = "insert into ECORE_MESSAGE_BUILDING_BLOCK(id,name,definition,registration_status,removal_date,"
					+ "object_identifier,previous_version,xml_Tag,data_type,data_type_id,max_occurs"
					+ ",min_occurs,message_id,version,create_user,create_time,is_from_iso20022) "
					+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			for (EcoreMessageBuildingBlock po : ecoreMessageBuildingBlocks) {
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
				pstmt.setString(9, po.getDataType());
				pstmt.setString(10, po.getDataTypeId());
				if(po.getMaxOccurs()!=null) {
					pstmt.setInt(11, po.getMaxOccurs());
				}
				if(po.getMinOccurs()!=null) {
					pstmt.setInt(12, po.getMinOccurs());
				}
				pstmt.setString(13, po.getMessageId());
				pstmt.setString(14, po.getVersion());
				pstmt.setString(15, po.getCreateUser());
				pstmt.setTimestamp(16, new Timestamp(new java.util.Date().getTime()));
				pstmt.setString(17, po.getIsfromiso20022());
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
