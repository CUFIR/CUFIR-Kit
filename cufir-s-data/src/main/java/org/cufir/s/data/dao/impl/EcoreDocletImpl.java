package org.cufir.s.data.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.cufir.s.ecore.bean.EcoreDoclet;
import org.cufir.s.data.util.DbUtil;
import org.cufir.s.data.util.DerbyUtil;

/**
 * EcoreDoclet数据库操作
 * @author tangmaoquan
 * @Date 2021年10月15日
 */
public class EcoreDocletImpl{

	private static Logger logger = Logger.getLogger(EcoreDocletImpl.class);

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	public void delByObjId(String codeSetId, String objIds) {
		String sql = "DELETE FROM ECORE_DOCLET WHERE OBJ_ID=? " ;
		if(objIds != null && !objIds.equals("")) {
			sql += " OR OBJ_ID IN (" + objIds + ")";
		}
		DbUtil.get().update(sql, codeSetId);
	}
	
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
