package com.cfets.cufir.s.data.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

import org.apache.log4j.Logger;

import com.cfets.cufir.s.data.bean.EcoreCode;
import com.cfets.cufir.s.data.dao.EcoreCodeDao;
import com.cfets.cufir.s.data.util.DerbyUtil;

/**
 * EcoreCode数据库操作
 * @author zqh
 *
 */
public class EcoreCodeDaoImpl implements EcoreCodeDao{

	private static Logger logger = Logger.getLogger(EcoreCodeDaoImpl.class);

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	@Override
	public void addEcoreCodeList(List<EcoreCode> ecoreCodes) throws Exception{
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			
			String sql = "insert into ecore_code(id,name,code_name,definition,registration_status"
					+ ",removal_date,object_identifier,code_set_id,create_user,create_time,is_from_iso20022) "
					+ "values(?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			for (EcoreCode po : ecoreCodes) {
				pstmt.setString(1, po.getId());
				pstmt.setString(2, po.getName());
				pstmt.setString(3, po.getCodeName());
				if(po.getDefinition()!=null) {
					pstmt.setString(4, po.getDefinition());
				}
				pstmt.setString(5, po.getRegistrationStatus());
				if(po.getRemovalDate()!=null) {
					pstmt.setDate(6, new Date(po.getRemovalDate().getTime()));
				} else {
					pstmt.setNull(6,Types.DATE);
				}
				pstmt.setString(7, po.getObjectIdentifier());
				pstmt.setString(8, po.getCodesetid());
				pstmt.setString(9, po.getCreateUser());
				pstmt.setTimestamp(10, new Timestamp(new java.util.Date().getTime()));
				pstmt.setString(11, po.getIsfromiso20022());
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
