package com.cfets.cufir.s.data.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

import org.apache.log4j.Logger;

import com.cfets.cufir.s.data.bean.EcoreConstraint;
import com.cfets.cufir.s.data.dao.EcoreConstraintDao;
import com.cfets.cufir.s.data.util.DerbyUtil;

/**
 * EcoreConstraint数据库操作
 * @author zqh
 *
 */
public class EcoreConstraintDaoImpl  implements EcoreConstraintDao{

	private static Logger logger = Logger.getLogger(EcoreConstraintDaoImpl.class);

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	@Override
	public void addEcoreConstraintList(List<EcoreConstraint> ecoreConstraints) throws Exception{
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			
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
