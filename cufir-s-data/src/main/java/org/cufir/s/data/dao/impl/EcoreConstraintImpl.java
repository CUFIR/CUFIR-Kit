package org.cufir.s.data.dao.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.cufir.s.ecore.bean.EcoreConstraint;
import org.cufir.s.ide.db.DbUtil;

import com.alibaba.druid.util.StringUtils;

/**
 * EcoreConstraint数据库操作
 */
public class EcoreConstraintImpl{

	private static Logger logger = Logger.getLogger(EcoreConstraintImpl.class);
	
	private final static String QSQL = "SELECT t.ID,t.OBJ_ID,t.OBJ_TYPE,t.EXPRESSION,t.EXPRESSION_LANGUAGE,t.DEFINITION,"
			+ "t.NAME,t.REGISTRATION_STATUS,t.REMOVAL_DATE,t.OBJECT_IDENTIFIER,t.IS_FROM_ISO20022,t.CREATE_TIME,t.UPDATE_TIME,"
			+ "t.CREATE_USER,t.UPDATE_USER FROM ECORE_CONSTRAINT t WHERE 1=1 ";

	public List<EcoreConstraint> findAll(){
		return DbUtil.get().find(QSQL, EcoreConstraint.class);
	}
	
	public List<EcoreConstraint> findByObjId(String objId){
		if(StringUtils.isEmpty(objId)) {
			return new ArrayList<EcoreConstraint>();
		}
		return DbUtil.get().find(QSQL + "AND t.OBJ_ID = ? ",EcoreConstraint.class, objId);
	}
	
	public void delByObjId(String codeSetId, String objIds) {
		String sql = "DELETE FROM ECORE_CONSTRAINT WHERE OBJ_ID=? " ;
		if(objIds != null && !objIds.equals("")) {
			sql += " OR OBJ_ID IN (" + objIds + ")";
		}
		DbUtil.get().update(sql, codeSetId);
	}
	
	public void saveConstraints(List<EcoreConstraint> ecoreConstraints) throws Exception{
		String sql = "INSERT INTO ECORE_CONSTRAINT(ID,OBJ_ID,OBJ_TYPE,EXPRESSION_LANGUAGE,DEFINITION"
				+ ",NAME,REGISTRATION_STATUS,REMOVAL_DATE,OBJECT_IDENTIFIER,CREATE_USER,CREATE_TIME,IS_FROM_ISO20022,EXPRESSION) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		List<Object[]> values = new ArrayList<Object[] >();
		ecoreConstraints.forEach(e -> {
			Object[] objs = new Object[13];
			objs[0] = e.getId();
			objs[1] = e.getObj_id();
			objs[2] = e.getObj_type();
			objs[3] = e.getExpressionlanguage();
			objs[4] = e.getDefinition() == null? "": e.getDefinition();
			objs[5] = e.getName();
			objs[6] = e.getRegistrationStatus();
			objs[7] = e.getRemovalDate() != null ? new Date(e.getRemovalDate().getTime()) : null;
			objs[8] = e.getObjectIdentifier();
			objs[9] = e.getCreateUser();
			objs[10] = new Timestamp(new java.util.Date().getTime());
			objs[11] = e.getIsfromiso20022();
			objs[12] = e.getExpression() == null ? "" : e.getExpression();
			values.add(objs);
		});
		int count = DbUtil.get().batchSave(sql,values);
		logger.info(count + "件数据更新成功");
	}
}
