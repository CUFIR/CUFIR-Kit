package org.cufir.s.data.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.cufir.s.ecore.bean.EcoreBusinessComponentRL;
import org.cufir.s.ide.db.DbUtil;
import org.cufir.s.ide.db.DerbyUtil;

/**
 * EcoreBusinessComponentRL数据库操作
 */
public class EcoreBusinessComponentRLImpl{

	private static Logger logger = Logger.getLogger(EcoreBusinessComponentRLImpl.class);

	private static String QSQL = "SELECT * FROM ECORE_BUSINESS_COMPONENT_RL t WHERE 1=1 ";
	
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	public List<EcoreBusinessComponentRL> findAll(){
		return DbUtil.get().find(QSQL + "ORDER BY NAME", EcoreBusinessComponentRL.class);
	}
	
	public Map<String, String> findMdr3() {
		String sql = "select id, p_id from ECORE_BUSINESS_COMPONENT_RL where p_id is not null";
		List<Map<String, Object>> maps = DbUtil.get().find(sql);
		Map<String, String> map = new HashMap<String, String>();
		maps.forEach(bcRL->{
			map.put(bcRL.get("id") + "", bcRL.get("p_id") + "");
		});
		return map;
	}

	public void addEcoreBusinessComponentRLList(List<EcoreBusinessComponentRL> ecoreBusinessComponentRLs) throws Exception{
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			
			String sql = "insert into ECORE_BUSINESS_COMPONENT_RL(id,p_id,create_user,create_time,is_from_iso20022) "
					+ "values(?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			for (EcoreBusinessComponentRL po : ecoreBusinessComponentRLs) {
				pstmt.setString(1, po.getId());
				pstmt.setString(2, po.getPId());
				pstmt.setString(3, po.getCreateUser());
				pstmt.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
				pstmt.setString(5, po.getIsfromiso20022());
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
	
	/**
	 * 删除业务关联
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteEcoreBusinessComponentRL(String id) throws Exception{
		boolean key=false;
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			String rsql = "delete from ECORE_BUSINESS_COMPONENT_RL where id=?" ;
			pstmt = conn.prepareStatement(rsql);
			pstmt.setString(1, id);
			int num=pstmt.executeUpdate();
			conn.commit();
			if(num>0) {
				key=true;
			}
		} catch (Exception e) {
			logger.error("删除数据失败!");
			conn.rollback();
			throw e;
		} finally {
			DerbyUtil.close(conn, pstmt, rs);
		}
		return key;
	}
	
	/**
	 * 保存关联
	 * @param ecoreBusinessComponentRLs
	 * @throws Exception
	 */
	public boolean saveEcoreBusinessComponentRL(List<EcoreBusinessComponentRL> ecoreBusinessComponentRLs) throws Exception{
		boolean key=false;
		if(ecoreBusinessComponentRLs!=null && !ecoreBusinessComponentRLs.isEmpty()) {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			for(EcoreBusinessComponentRL rl:ecoreBusinessComponentRLs) {
				deleteBusinessRLByIds(rl.getId());
			}
			saveBusinessRL(ecoreBusinessComponentRLs);
			conn.commit(); // 提交
		}
		key=true;
		return key;
	}
	
	private void saveBusinessRL(List<EcoreBusinessComponentRL> ecoreBusinessComponentRLs) throws SQLException {
		String sql = "insert into ECORE_BUSINESS_COMPONENT_RL(id,p_id,create_user,create_time,is_from_iso20022) "
				+ "values(?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		for (EcoreBusinessComponentRL po : ecoreBusinessComponentRLs) {
			pstmt.setString(1, po.getId());
			pstmt.setString(2, po.getPId());
			pstmt.setString(3, po.getCreateUser());
			pstmt.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(5, po.getIsfromiso20022());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
	}
	
	private void deleteBusinessRLByIds(String id) throws Exception{
		String rsql = "delete from ECORE_BUSINESS_COMPONENT_RL where id=?" ;
		pstmt = conn.prepareStatement(rsql);
		pstmt.setString(1, id);
		pstmt.executeUpdate();
		
	}
}
