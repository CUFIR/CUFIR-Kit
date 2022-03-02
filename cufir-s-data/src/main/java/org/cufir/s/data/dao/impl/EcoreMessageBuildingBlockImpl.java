package org.cufir.s.data.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.cufir.s.ecore.bean.EcoreMessageBuildingBlock;
import org.cufir.s.ide.db.DbUtil;
import org.cufir.s.ide.db.DerbyUtil;

/**
 * EcoreMessageBuildingBlock数据库操作
 */
public class EcoreMessageBuildingBlockImpl{

	private static Logger logger = Logger.getLogger(EcoreMessageBuildingBlockImpl.class);
	
	private final static String QSQL = "SELECT * FROM ECORE_MESSAGE_BUILDING_BLOCK t WHERE 1=1 ";

	private static Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	public List<EcoreMessageBuildingBlock> findAll(){
		return DbUtil.get().find(QSQL + "ORDER BY t.NAME",EcoreMessageBuildingBlock.class);
	}
	
	public List<EcoreMessageBuildingBlock> findByMsgId(String msgId){
		String querySql = QSQL + "AND t.MESSAGE_ID = ? ORDER BY t.NAME ";
		return DbUtil.get().find(querySql,EcoreMessageBuildingBlock.class, msgId);
	}
	
	public Map<String, List<String>> findMdr3(String messageIds) {
		String sql="select data_type_id, message_id from ECORE_MESSAGE_BUILDING_BLOCK where data_type = '2' and message_id IN (" + messageIds + ")";
		List<Map<String, Object>> list = DbUtil.get().find(sql);
		Map<String, List<String>> map = new HashMap<>();
		list.forEach(l ->{
			String messageId = l.get("message_id") + "";
			if(!map.containsKey(messageId)) {
				List<String> messageComponentIds = new ArrayList<>();
				messageComponentIds.add(l.get("data_type_id") + "");
				map.put(messageId, messageComponentIds);
			}else {
				List<String> messageComponentIds = map.get(messageId);
				messageComponentIds.add(l.get("data_type_id") + "");
			}
		});
		return map;
	}
	
	/**
	 * 查找报文对应的元素关联信息
	 * @param msgId
	 * @param dtId
	 * @return
	 */
	public static EcoreMessageBuildingBlock findMsgBuildingBlocksByMsgIdAndDtId(String msgId, String dtId){
		return DbUtil.get().findOne(QSQL + "and t.MESSAGE_ID = ? and t.DATA_TYPE_ID = ? ", EcoreMessageBuildingBlock.class, msgId, dtId);
	}
	
	
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
