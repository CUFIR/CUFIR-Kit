package org.cufir.s.data.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.cufir.s.ecore.bean.EcoreNextVersions;
import org.cufir.s.ide.db.DbUtil;

/**
 * EcoreNextVersions数据库操作
 */
public class EcoreNextVersionsImpl{

	private static Logger logger = Logger.getLogger(EcoreNextVersionsImpl.class);

	private final static String QSQL = "SELECT t.ID,t.NEXT_VERSION_ID,t.IS_FROM_ISO20022,t.OBJ_TYPE,t.CREATE_TIME,t.UPDATE_TIME,"
			+ "t.CREATE_USER,t.UPDATE_USER FROM ECORE_NEXT_VERSIONS t WHERE 1=1 ";
	
	public List<EcoreNextVersions> findAll(){
		return DbUtil.get().find(QSQL, EcoreNextVersions.class);
	}
	
	public List<EcoreNextVersions> findById(String id){
		return DbUtil.get().find(QSQL + "AND t.ID = ? ", EcoreNextVersions.class ,id);
	}
	
	public List<String> findNextVersionIdById(String id){
		String querySql = " SELECT t.NEXT_VERSION_ID FROM ECORE_NEXT_VERSIONS t WHERE t.ID = ? ";
		List<Map<String, Object>> list = DbUtil.get().find(querySql ,id);
		if(list != null && list.size() > 0) {
			return list.stream().map(l -> l.get("NEXT_VERSION_ID") + "").collect(Collectors.toList());
		}else {
			return new ArrayList<String>();
		}
	}
	
	public void delById(String id, String ids) {
		String sql = "DELETE FROM ECORE_NEXT_VERSIONS WHERE ID=? " ;
		if(ids != null && !ids.equals("")) {
			sql += " OR ID IN (" + ids + ")";
		}
		DbUtil.get().update(sql, id);
	}
	
	public void delByNextVersionId(String id) {
		String sql = "DELETE FROM ECORE_NEXT_VERSIONS WHERE NEXT_VERSION_ID=? " ;
		DbUtil.get().update(sql, id);
	}
	
	public int countByNextVersionId(String nextVersionId) {
		String sql = "SELECT count(1) FROM ECORE_NEXT_VERSIONS t WHERE t.NEXT_VERSION_ID = ? ";
		return DbUtil.get().count(sql, nextVersionId);
	}
	
	public void saveNextVersionses(List<EcoreNextVersions> ecoreNextVersionss) throws Exception{
		String sql = "INSERT INTO ECORE_NEXT_VERSIONS(ID,OBJ_TYPE,NEXT_VERSION_ID,CREATE_USER,CREATE_TIME,IS_FROM_ISO20022) "
				+ "VALUES(?,?,?,?,?,?)";
		List<Object[]> values = new ArrayList<Object[] >();
		ecoreNextVersionss.forEach(e -> {
			Object[] objs = new Object[6];
			objs[0] = e.getId();
			objs[1] = e.getObjType();
			objs[2] = e.getNextVersionId();
			objs[3] = e.getCreateUser();
			objs[4] = new Timestamp(new Date().getTime());
			objs[5] = e.getIsfromiso20022();
			values.add(objs);
		});
		int count = DbUtil.get().batchSave(sql,values);
		logger.info(count + "件数据更新成功");
	}
}
