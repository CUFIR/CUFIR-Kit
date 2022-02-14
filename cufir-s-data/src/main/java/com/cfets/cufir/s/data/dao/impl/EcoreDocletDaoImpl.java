package com.cfets.cufir.s.data.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;

import com.cfets.cufir.s.data.bean.EcoreDoclet;
import com.cfets.cufir.s.data.dao.EcoreDocletDao;
import com.cfets.cufir.s.data.util.DerbyUtil;

/**
 * EcoreDoclet数据库操作
 * @author zqh
 *
 */
public class EcoreDocletDaoImpl implements EcoreDocletDao{

	private static Logger logger = Logger.getLogger(EcoreDocletDaoImpl.class);

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	@Override
	public void addEcoreDocletList(List<EcoreDoclet> ecoreDoclets) throws Exception{
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			
			String sql = "insert into ecore_doclet(id,obj_id,obj_type,type,content,create_user,create_time,is_from_iso20022) "
					+ "values(?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			for (EcoreDoclet po : ecoreDoclets) {
				pstmt.setString(1, po.getId());
				pstmt.setString(2, po.getObj_id());
				pstmt.setString(3, po.getObj_type());
				pstmt.setString(4, po.getType());
				pstmt.setString(5, po.getContent());
				pstmt.setString(6, po.getCreateUser());
				pstmt.setTimestamp(7, new Timestamp(new java.util.Date().getTime()));
				pstmt.setString(8, po.getIsfromiso20022());
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
