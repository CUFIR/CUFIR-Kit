package org.cufir.s.data.dao.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.cufir.s.ecore.bean.EcoreCode;
import org.cufir.s.ide.db.DbUtil;

/**
 * EcoreCode数据库操作
 */
public class EcoreCodeImpl{
	
	private static Logger logger = Logger.getLogger(EcoreCodeImpl.class);
	
	private final static String QSQL = "SELECT t.ID,t.NAME,t.CODE_NAME,t.DEFINITION,t.CODE_SET_ID,t.REGISTRATION_STATUS" + 
			",t.REMOVAL_DATE,t.OBJECT_IDENTIFIER,t.IS_FROM_ISO20022,t.CREATE_TIME,t.UPDATE_TIME,t.CREATE_USER,t.UPDATE_USER FROM ECORE_CODE t WHERE 1=1 ";
	
	public List<EcoreCode> findByCodeSetId(String codeSetId) {
		return DbUtil.get().find(QSQL + "AND t.CODE_SET_ID = ? ORDER BY t.NAME ", EcoreCode.class, codeSetId);
	}
	
	public List<EcoreCode> findAll() {
		return DbUtil.get().find(QSQL, EcoreCode.class);
	}
	
	public void delByCodeSetId(String codeSetId) {
		String sql = "DELETE FROM ECORE_CODE WHERE CODE_SET_ID=?" ;
		DbUtil.get().update(sql, codeSetId);
	}
	
	public void delByCodeSetIds(String codeSetIds, String names) {
		String codenamesql="DELETE FROM ECORE_CODE WHERE CODE_SET_ID IN ("+codeSetIds+")";
		if(!names.equals("")) {
			codenamesql +=" AND CODE_NAME NOT IN("+names+")";
		}
		DbUtil.get().update(codenamesql);
	}
	
	public List<String> findIdByCodeSetId(String codeSetId) {
		String sql = "SELECT ID FROM ECORE_CODE WHERE CODE_SET_ID=?" ;
		List<Map<String, Object>> listMap = DbUtil.get().find(sql, codeSetId);
		List<String> list = new ArrayList<>();
		if(listMap.size() > 0) {
			list = listMap.stream().map(m -> m.get("ID") + "").collect(Collectors.toList());
		}
		return list;
	}

	public void saveCode(List<EcoreCode> ecoreCodes) throws Exception{
		String sql = "INSERT INTO ECORE_CODE(ID,NAME,CODE_NAME,DEFINITION,REGISTRATION_STATUS"
				+ ",REMOVAL_DATE,OBJECT_IDENTIFIER,CODE_SET_ID,CREATE_USER,CREATE_TIME,IS_FROM_ISO20022) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
		List<Object[]> values = new ArrayList<Object[] >();
		ecoreCodes.forEach(e ->{
			Object[] objs = new Object[11];
			objs[0] = e.getId();
			objs[1] = e.getName();
			objs[2] = e.getCodeName();
			objs[3] = e.getDefinition() == null? "": e.getDefinition();
			objs[4] = e.getRegistrationStatus();
			objs[5] = e.getRemovalDate() != null ? new Date(e.getRemovalDate().getTime()) : null;
			objs[6] = e.getObjectIdentifier();
			objs[7] = e.getCodesetid();
			objs[8] = e.getCreateUser();
			objs[9] = new Timestamp(new java.util.Date().getTime());
			objs[10] = e.getIsfromiso20022();
			values.add(objs);
		});
		int count = DbUtil.get().batchSave(sql,values);
		logger.info(count + "件数据更新成功");
	}
	
	public List<Map<String, Object>> findNode() {
		String sql = "SELECT T.ID,T.NAME,CODE_SET_ID,T.REGISTRATION_STATUS FROM ECORE_CODE T ORDER BY T.NAME";
		return DbUtil.get().find(sql);
	}
}
