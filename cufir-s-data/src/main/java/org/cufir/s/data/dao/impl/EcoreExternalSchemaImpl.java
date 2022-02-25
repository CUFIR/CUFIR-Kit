package org.cufir.s.data.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.cufir.s.ecore.bean.EcoreConstraint;
import org.cufir.s.ecore.bean.EcoreExample;
import org.cufir.s.ecore.bean.EcoreExternalSchema;
import org.cufir.s.ecore.bean.EcoreNamespaceList;
import org.cufir.s.ecore.bean.EcoreNextVersions;
import org.cufir.s.data.util.DbUtil;
import org.cufir.s.data.util.DerbyUtil;
import org.cufir.s.data.vo.EcoreExternalSchemaVO;

/**
 * EcoreExternalSchema数据库操作
 * @author tangmaoquan
 * @Date 2021年10月15日
 */
public class EcoreExternalSchemaImpl{

	private static Logger logger = Logger.getLogger(EcoreExternalSchemaImpl.class);
	
	private final static String QSQL = "SELECT * FROM ECORE_EXTERNAL_SCHEMA t WHERE 1=1 ";

	private static Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	public List<EcoreExternalSchema> findAll() {
		return DbUtil.get().find(QSQL, EcoreExternalSchema.class);
	}
	
	public EcoreExternalSchema getById(String id) {
		EcoreExternalSchema es = DbUtil.get().findOne(QSQL + "AND t.ID = ? ", EcoreExternalSchema.class, id);
		return es == null? new EcoreExternalSchema() : es;
	}
	
	/**
	 * 获取ExternalSchema树节点
	 * @return EcoreTreeNode
	 */
	public List<Map<String, Object>> findTreeNodes() throws Exception{
		return DbUtil.get().find("SELECT T.ID,T.NAME,t.REGISTRATION_STATUS FROM ECORE_EXTERNAL_SCHEMA T ORDER BY T.NAME");
	}
	
	/**
	 * 保存ExternalSchema
	 * @param ecoreExternalSchemas
	 */
	public void addEcoreExternalSchemaList(List<EcoreExternalSchema> ecoreExternalSchemas) throws Exception{
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			
			String sql = "insert into ECORE_EXTERNAL_SCHEMA(id,name,definition,registration_status,removal_date,"
					+ "object_identifier,previous_version,process_content,technical,create_user,create_time,is_from_iso20022) "
					+ "values(?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			for (EcoreExternalSchema po : ecoreExternalSchemas) {
				pstmt.setString(1, po.getId());
				pstmt.setString(2, po.getName());
				pstmt.setString(3, po.getDefinition());
				pstmt.setString(4, po.getRegistrationStatus());
				if(po.getRemovalDate()!=null) {
					pstmt.setDate(5, new Date(po.getRemovalDate().getTime()));
				} else {
					pstmt.setNull(5,Types.DATE);
				}
				pstmt.setString(6, po.getObjectIdentifier());
				pstmt.setString(7, po.getPreviousVersion());
				pstmt.setString(8, po.getProcessContent());
				pstmt.setString(9, po.getTechnical());
				pstmt.setString(10, po.getCreateUser());
				pstmt.setTimestamp(11, new Timestamp(new java.util.Date().getTime()));
				pstmt.setString(12, po.getIsfromiso20022());
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
	 * 删除ExternalSchema
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteEcoreExternalSchema(String id) throws Exception{
		boolean key=false;
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			String dtsql = "delete from ECORE_EXTERNAL_SCHEMA where id=?" ;
			//common
			String csql = "delete from ECORE_CONSTRAINT where obj_id=?" ;
			String dsql = "delete from ECORE_DOCLET where obj_id=?" ;
			String eesql = "delete from ECORE_EXAMPLE where obj_id=?" ;
			String nlsql = "delete from ECORE_NAMESPACE_LIST where obj_id=?" ;
			String vsql="delete from ECORE_NEXT_VERSIONS where id=?";
			pstmt = conn.prepareStatement(csql);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement(dsql);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement(eesql);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement(nlsql);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement(vsql);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			
			//saveRemovedInfo(id);
		
			pstmt = conn.prepareStatement(dtsql);
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
	 * 更新ExternalSchema
	 * @param ecoreExternalSchemas
	 */
	public void updateEcoreExternalSchemaList(List<EcoreExternalSchema> ecoreExternalSchemas) throws Exception{
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			
			String sql = "update ECORE_EXTERNAL_SCHEMA set update_time=?,name=?,definition=?,registration_status=?,removal_date=?,"
					+ "object_identifier=?,previous_version=?,process_content=?,technical=?, update_user=?"
					+ ",is_from_iso20022=? where id=? ";

			// 连接失败,再次请求连接
			if (conn == null) {
				conn = DerbyUtil.getConnection(); // 获得数据库连接
			}
			pstmt = conn.prepareStatement(sql);
			for (EcoreExternalSchema po : ecoreExternalSchemas) {
				pstmt.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
				pstmt.setString(2, po.getName());
				pstmt.setString(3, po.getDefinition());
				pstmt.setString(4, po.getRegistrationStatus());
				if(po.getRemovalDate()!=null) {
					pstmt.setDate(5, new Date(po.getRemovalDate().getTime()));
				} else {
					pstmt.setNull(5,Types.DATE);
				}
				pstmt.setString(6, po.getObjectIdentifier());
				pstmt.setString(7, po.getPreviousVersion());
				pstmt.setString(8, po.getProcessContent());
				pstmt.setString(9, po.getTechnical());
				pstmt.setString(10, po.getUpdateUser());
				pstmt.setString(11, po.getIsfromiso20022());
				pstmt.setString(12, po.getId());
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
	 * 保存ExternalSchema
	 * @param ecoreExternalSchemas
	 */
	public boolean saveEcoreExternalSchemaVOList(List<EcoreExternalSchemaVO> ecoreExternalSchemaVOs) throws Exception{
		boolean key=false;
		List<EcoreExternalSchema> ecoreExternalSchemas=new ArrayList<EcoreExternalSchema>();
		List<EcoreConstraint> ecoreConstraints=new ArrayList<EcoreConstraint>();
		List<EcoreNamespaceList> ecoreNamespaceLists=new ArrayList<EcoreNamespaceList>();
		List<EcoreExample> ecoreExamples=new ArrayList<EcoreExample>();
		List<EcoreNextVersions> ecoreNextVersions=new ArrayList<EcoreNextVersions>();
		List<String> dataIds=new ArrayList<String>();
		//解析保存的内容
		for(EcoreExternalSchemaVO vo: ecoreExternalSchemaVOs) {
			EcoreExternalSchema ecoreExternalSchema=vo.getEcoreExternalSchema();
			List<EcoreConstraint> constraints=vo.getEcoreConstraints();
			List<EcoreExample> examples=vo.getEcoreExamples();
			List<EcoreNextVersions> nextVersions=vo.getEcoreNextVersions();
			List<EcoreNamespaceList> es=vo.getEcoreNamespaceList();
			if(constraints!=null) {
				ecoreConstraints.addAll(constraints);
			}
			if(examples!=null) {
				ecoreExamples.addAll(examples);
			}
			if(nextVersions!=null) {
				ecoreNextVersions.addAll(nextVersions);
			}
			dataIds.add(ecoreExternalSchema.getId());
			ecoreExternalSchemas.add(ecoreExternalSchema);
			if(ecoreNamespaceLists!=null) {
				ecoreNamespaceLists.addAll(es);
			}
		}
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			if(dataIds!=null && !dataIds.isEmpty()) {
				for(String id:dataIds) {
					//先删除数据类型下面的所有数据
					deleteExternalSchemaByIds(id);
				}
			}
			//开始保存
			if(ecoreExternalSchemas!=null && !ecoreExternalSchemas.isEmpty()) {
				saveExternalSchema(ecoreExternalSchemas);
				if(ecoreNamespaceLists!=null && !ecoreNamespaceLists.isEmpty()) {
					saveNamespaceList(ecoreNamespaceLists);
				}
				if(ecoreExamples!=null && !ecoreExamples.isEmpty()) {
					saveExample(ecoreExamples);
				}
				if(ecoreConstraints!=null && !ecoreConstraints.isEmpty()) {
					saveConstraint(ecoreConstraints);
				}
				if(ecoreNextVersions!=null && !ecoreNextVersions.isEmpty()) {
					saveNextVersions(ecoreNextVersions);
				}
			}
			conn.commit();
		} catch (Exception e) {
			logger.error("更新数据失败!");
			conn.rollback();
			throw e;
		} finally {
			DerbyUtil.close(conn, pstmt, rs);
		}
		key=true;
		return key;
	}
	
	private void deleteExternalSchemaByIds(String id) throws Exception{
		//common
		String csql = "delete from ECORE_CONSTRAINT where obj_id=?" ;
		String dsql = "delete from ECORE_DOCLET where obj_id=?" ;
		String eesql = "delete from ECORE_EXAMPLE where obj_id=?" ;
		String nlsql = "delete from ECORE_NAMESPACE_LIST where obj_id=?" ;
		String vsql="delete from ECORE_NEXT_VERSIONS where id=?";
		pstmt = conn.prepareStatement(csql);
		pstmt.setString(1, id);
		pstmt.executeUpdate();
		
		pstmt = conn.prepareStatement(dsql);
		pstmt.setString(1, id);
		pstmt.executeUpdate();
		
		pstmt = conn.prepareStatement(eesql);
		pstmt.setString(1, id);
		pstmt.executeUpdate();
		
		pstmt = conn.prepareStatement(nlsql);
		pstmt.setString(1, id);
		pstmt.executeUpdate();
		
		pstmt = conn.prepareStatement(vsql);
		pstmt.setString(1, id);
		pstmt.executeUpdate();
	}
	
	private void saveExternalSchema(List<EcoreExternalSchema> ecoreExternalSchemas) throws Exception{
		List<EcoreExternalSchema> addEcoreExternalSchemas=new ArrayList<EcoreExternalSchema>();
		List<EcoreExternalSchema> updateEcoreExternalSchemas=new ArrayList<EcoreExternalSchema>();
		for(EcoreExternalSchema po : ecoreExternalSchemas) {
			String csql = "SELECT T.ID FROM ECORE_EXTERNAL_SCHEMA T WHERE id=?";
			pstmt=conn.prepareStatement(csql);
			pstmt.setString(1, po.getId());
			rs=pstmt.executeQuery();
			if(rs.next()) {
				updateEcoreExternalSchemas.add(po);
			}else {
				addEcoreExternalSchemas.add(po);
			}
		}
		String sql = "insert into ECORE_EXTERNAL_SCHEMA(id,name,definition,registration_status,removal_date,"
				+ "object_identifier,previous_version,process_content,technical,create_user,create_time,is_from_iso20022) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		for (EcoreExternalSchema po : addEcoreExternalSchemas) {
			pstmt.setString(1, po.getId());
			pstmt.setString(2, po.getName());
			pstmt.setString(3, po.getDefinition());
			pstmt.setString(4, po.getRegistrationStatus());
			if(po.getRemovalDate()!=null) {
				pstmt.setDate(5, new Date(po.getRemovalDate().getTime()));
			} else {
				pstmt.setNull(5,Types.DATE);
			}
			pstmt.setString(6, po.getObjectIdentifier());
			pstmt.setString(7, po.getPreviousVersion());
			pstmt.setString(8, po.getProcessContent());
			pstmt.setString(9, po.getTechnical());
			pstmt.setString(10, po.getCreateUser());
			pstmt.setTimestamp(11, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(12, po.getIsfromiso20022());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
		
		String usql = "update ECORE_EXTERNAL_SCHEMA set update_time=?,name=?,definition=?,registration_status=?,removal_date=?,"
				+ "object_identifier=?,previous_version=?,process_content=?,technical=?, update_user=?"
				+ ",is_from_iso20022=? where id=? ";
		pstmt = conn.prepareStatement(usql);
		for (EcoreExternalSchema po : updateEcoreExternalSchemas) {
			pstmt.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(2, po.getName());
			pstmt.setString(3, po.getDefinition());
			pstmt.setString(4, po.getRegistrationStatus());
			if(po.getRemovalDate()!=null) {
				pstmt.setDate(5, new Date(po.getRemovalDate().getTime()));
			} else {
				pstmt.setNull(5,Types.DATE);
			}
			pstmt.setString(6, po.getObjectIdentifier());
			pstmt.setString(7, po.getPreviousVersion());
			pstmt.setString(8, po.getProcessContent());
			pstmt.setString(9, po.getTechnical());
			pstmt.setString(10, po.getUpdateUser());
			pstmt.setString(11, po.getIsfromiso20022());
			pstmt.setString(12, po.getId());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
	}
	
	private void saveExample(List<EcoreExample> ecoreExamples) throws Exception{
		String sql = "insert into ecore_example(id,obj_id,obj_type,example,create_user,create_time,is_from_iso20022) "
				+ "values(?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		for (EcoreExample po : ecoreExamples) {
			pstmt.setString(1, po.getId());
			pstmt.setString(2, po.getObjId());
			pstmt.setString(3, po.getObjType());
			pstmt.setString(4, po.getExample());
			pstmt.setString(5, po.getCreateUser());
			pstmt.setTimestamp(6, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(7, po.getIsfromiso20022());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
	}
	
	private void saveNamespaceList(List<EcoreNamespaceList> ecoreNamespaceLists) throws Exception{
		String sql = "insert into ECORE_NAMESPACE_LIST(id,obj_id,namespace_list,create_user,create_time,is_from_iso20022) "
				+ "values(?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		for (EcoreNamespaceList po : ecoreNamespaceLists) {
			pstmt.setString(1, po.getId());
			pstmt.setString(2, po.getObj_id());
			pstmt.setString(3, po.getNamespaceList());
			pstmt.setString(4, po.getCreateUser());
			pstmt.setTimestamp(5, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(6, po.getIsfromiso20022());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
	}
	
	private void saveConstraint(List<EcoreConstraint> ecoreConstraints) throws Exception{
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
			} else {
				pstmt.setString(5, "");
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
	}
	
	private void saveNextVersions(List<EcoreNextVersions> ecoreNextVersionss) throws Exception{
		String sql = "insert into ECORE_NEXT_VERSIONS(id,obj_type,next_version_id,create_user,create_time,is_from_iso20022) "
				+ "values(?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		for (EcoreNextVersions po : ecoreNextVersionss) {
			pstmt.setString(1, po.getId());
			pstmt.setString(2, po.getObjType());
			pstmt.setString(3, po.getNextVersionId());
			pstmt.setString(4, po.getCreateUser());
			pstmt.setTimestamp(5, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(6, po.getIsfromiso20022());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
	}
	
	/**
	 * 查询ExternalSchemaVO数据
	 * @param id
	 */
	public EcoreExternalSchemaVO findExternalSchemaVO(String id) throws Exception{
		EcoreExternalSchemaVO vo=null;
		List<EcoreNamespaceList> ecoreNamespaceLists=new ArrayList<EcoreNamespaceList>();
		List<EcoreExample> ecoreExamples=new ArrayList<EcoreExample>();
		List<EcoreConstraint> ecoreConstraints=new ArrayList<EcoreConstraint>();
		List<EcoreNextVersions> ecoreNextVersions=new ArrayList<EcoreNextVersions>();
		String presql="select * from ecore_namespace_list where obj_id=?";
		String mcsql = "select * FROM ECORE_EXTERNAL_SCHEMA where id=?" ;
		String csql = "select * from ECORE_CONSTRAINT where obj_id=?";
		String eeql = "select * from ECORE_EXAMPLE where obj_id=?";
		String vsql="select * from ECORE_NEXT_VERSIONS where id=?";
		conn = DerbyUtil.getConnection(); // 获得数据库连接
		pstmt=conn.prepareStatement(mcsql);
		pstmt.setString(1, id);
		//获取block数据
		rs=pstmt.executeQuery();
		while(rs.next()) {
			EcoreExternalSchema me = new EcoreExternalSchema();
			vo=new EcoreExternalSchemaVO();
			me.setId(rs.getString("ID"));
			me.setName(rs.getString("NAME"));
			me.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
			me.setDefinition(rs.getString("DEFINITION"));
			me.setRegistrationStatus(rs.getString("registration_status"));
			me.setRemovalDate(rs.getDate("removal_date"));
			me.setCreateTime(rs.getDate("create_time"));
			me.setCreateUser(rs.getString("create_user"));
			me.setUpdateTime(rs.getDate("update_time"));
			me.setUpdateUser(rs.getString("update_user"));
			me.setProcessContent(rs.getString("process_content"));
			me.setTechnical(rs.getString("technical"));
			me.setPreviousVersion(rs.getString("previous_version"));
			me.setIsfromiso20022(rs.getString("is_from_iso20022"));
			vo.setEcoreExternalSchema(me);
			break;
		}
		if(vo!=null) {
        	pstmt=conn.prepareStatement(csql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreConstraint ec=new EcoreConstraint();
				ec.setId(rs.getString("ID"));
				ec.setName(rs.getString("NAME"));
				ec.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
				ec.setDefinition(rs.getString("DEFINITION"));
				ec.setExpression(rs.getString("expression"));
				ec.setExpressionlanguage(rs.getString("expression_language"));
				ec.setObj_id(rs.getString("OBJ_ID"));
				ec.setObj_type(rs.getString("Obj_type"));
				ec.setRegistrationStatus(rs.getString("registration_status"));
				ec.setRemovalDate(rs.getDate("removal_date"));
				ec.setCreateTime(rs.getDate("create_time"));
				ec.setCreateUser(rs.getString("create_user"));
				ec.setUpdateTime(rs.getDate("update_time"));
				ec.setUpdateUser(rs.getString("update_user"));
				ec.setIsfromiso20022(rs.getString("is_from_iso20022"));
				ecoreConstraints.add(ec);
			}
			pstmt=conn.prepareStatement(eeql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreExample ec=new EcoreExample();
				ec.setId(rs.getString("ID"));
				ec.setObjId(rs.getString("OBJ_ID"));
				ec.setObjType(rs.getString("Obj_type"));
				ec.setExample(rs.getString("example"));
				ec.setCreateTime(rs.getDate("create_time"));
				ec.setCreateUser(rs.getString("create_user"));
				ec.setUpdateTime(rs.getDate("update_time"));
				ec.setUpdateUser(rs.getString("update_user"));
				ec.setIsfromiso20022(rs.getString("is_from_iso20022"));
				ecoreExamples.add(ec);
			}
			pstmt=conn.prepareStatement(vsql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreNextVersions ec=new EcoreNextVersions();
				ec.setId(rs.getString("ID"));
				ec.setObjType(rs.getString("obj_type"));
				ec.setNextVersionId(rs.getString("next_version_id"));
				ec.setCreateTime(rs.getDate("create_time"));
				ec.setCreateUser(rs.getString("create_user"));
				ec.setUpdateTime(rs.getDate("update_time"));
				ec.setUpdateUser(rs.getString("update_user"));
				ec.setIsfromiso20022(rs.getString("is_from_iso20022"));
				ecoreNextVersions.add(ec);
			}
			//elment
			pstmt=conn.prepareStatement(presql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreNamespaceList li=new EcoreNamespaceList();
				li.setId(rs.getString("ID"));
				li.setObj_id(rs.getString("OBJ_ID"));
				li.setNamespaceList(rs.getString("NAMESPACE_LIST"));
				li.setCreateTime(rs.getDate("create_time"));
				li.setCreateUser(rs.getString("create_user"));
				li.setUpdateTime(rs.getDate("update_time"));
				li.setUpdateUser(rs.getString("update_user"));
				li.setIsfromiso20022(rs.getString("is_from_iso20022"));
				ecoreNamespaceLists.add(li);
	        }
			if(ecoreExamples!=null && !ecoreExamples.isEmpty()) {
				vo.setEcoreExamples(ecoreExamples);
			}
			if(ecoreConstraints!=null && !ecoreConstraints.isEmpty()) {
				vo.setEcoreConstraints(ecoreConstraints);
			}
			if(ecoreNextVersions!=null && !ecoreNextVersions.isEmpty()) {
				vo.setEcoreNextVersions(ecoreNextVersions);
			}
			if(ecoreNamespaceLists!=null && !ecoreNamespaceLists.isEmpty()) {
				vo.setEcoreNamespaceList(ecoreNamespaceLists);
			}
		}
		conn.commit();
		return vo;
	}
	
}
