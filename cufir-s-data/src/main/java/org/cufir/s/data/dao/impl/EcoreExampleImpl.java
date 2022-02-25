package org.cufir.s.data.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.cufir.s.ecore.bean.EcoreExample;
import org.cufir.s.data.util.DbUtil;

/**
 * EcoreExample数据库操作
 * @author tangmaoquan
 * @Date 2021年11月11日
 */
public class EcoreExampleImpl{
	
	private static Logger logger = Logger.getLogger(EcoreExampleImpl.class);
	
	private final static String QSQL = "SELECT t.ID,t.OBJ_ID,t.OBJ_TYPE,t.EXAMPLE,t.IS_FROM_ISO20022,t.CREATE_TIME,t.UPDATE_TIME,"
			+ "t.CREATE_USER,t.UPDATE_USER FROM ECORE_EXAMPLE t WHERE 1=1 ";

	public void delByObjId(String codeSetId, String objIds) {
		String sql = "DELETE FROM ECORE_EXAMPLE WHERE OBJ_ID=? " ;
		if(objIds != null && !objIds.equals("")) {
			sql += " OR OBJ_ID IN (" + objIds + ")";
		}
		DbUtil.get().update(sql, codeSetId);
	}
	
	public List<EcoreExample> findAll() {
		return DbUtil.get().find(QSQL, EcoreExample.class);
	}
	
	public List<EcoreExample> findByObjId(String objId) {
		return DbUtil.get().find(QSQL + "AND OBJ_ID=? ", EcoreExample.class, objId);
	}
	
	public void saveExamples(List<EcoreExample> ecoreExamples) throws Exception{
		String sql = "INSERT INTO ECORE_EXAMPLE(ID,OBJ_ID,OBJ_TYPE,EXAMPLE,CREATE_USER,CREATE_TIME,IS_FROM_ISO20022) "
				+ "VALUES(?,?,?,?,?,?,?)";
		List<Object[]> values = new ArrayList<Object[] >();
		ecoreExamples.forEach(e -> {
			Object[] objs = new Object[7];
			objs[0] = e.getId();
			objs[1] = e.getObjId();
			objs[2] = e.getObjType();
			objs[3] = e.getExample();
			objs[4] = e.getCreateUser();
			objs[5] = new Timestamp(new java.util.Date().getTime());
			objs[6] = e.getIsfromiso20022();
			values.add(objs);
		});
		int count = DbUtil.get().batchSave(sql,values);
		logger.info(count + "件数据更新成功");
	}
}
