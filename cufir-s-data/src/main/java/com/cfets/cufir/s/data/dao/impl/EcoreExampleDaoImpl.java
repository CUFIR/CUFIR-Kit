package com.cfets.cufir.s.data.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;

import com.cfets.cufir.s.data.bean.EcoreExample;
import com.cfets.cufir.s.data.dao.EcoreExampleDao;
import com.cfets.cufir.s.data.util.DerbyUtil;

/**
 * EcoreExample数据库操作
 * @author zqh
 *
 */
public class EcoreExampleDaoImpl implements EcoreExampleDao{

	private static Logger logger = Logger.getLogger(EcoreExampleDaoImpl.class);

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	@Override
	public void addEcoreExampleList(List<EcoreExample> ecoreExamples) throws Exception{
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			
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
