package com.cfets.cufir.s.data.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.cfets.cufir.s.data.bean.EcoreCode;
import com.cfets.cufir.s.data.bean.EcoreConstraint;
import com.cfets.cufir.s.data.bean.EcoreDataType;
import com.cfets.cufir.s.data.bean.EcoreExample;
import com.cfets.cufir.s.data.bean.EcoreNextVersions;
import com.cfets.cufir.s.data.dao.EcoreDataTypeDao;
import com.cfets.cufir.s.data.util.ConstantUtil;
import com.cfets.cufir.s.data.util.DerbyUtil;
import com.cfets.cufir.s.data.vo.EcoreCodeVO;
import com.cfets.cufir.s.data.vo.EcoreDataTypeVO;
import com.cfets.cufir.s.data.vo.EcoreTreeNode;
import com.cfets.cufir.s.data.vo.RemovedObjectVO;

/**
 * EcoreDataType数据库操作
 * @author zqh
 *
 */
public class EcoreDataTypeDaoImpl implements EcoreDataTypeDao{

	private static Logger logger = Logger.getLogger(EcoreDataTypeDaoImpl.class);

	private static Connection conn = null;
	private PreparedStatement pstmt = null;
	private Statement st=null;
	private ResultSet rs = null;
	
	/**
	 * 获取datatypeNodes
	 * @return List<EcoreTreeNode>
	 */
	public List<EcoreTreeNode> findAllEcoreDataTypes() throws Exception{
		List<EcoreTreeNode> ecoreTreeNodes=new ArrayList<EcoreTreeNode>();
		conn = DerbyUtil.getConnection(); // 获得数据库连接
		String dsql = "SELECT T.ID,T.NAME,T.TYPE,T.TRACE,t.registration_status FROM ECORE_DATA_TYPE T ORDER BY T.NAME";
		try {
			st=conn.createStatement();
			//获取dataType
			rs=st.executeQuery(dsql);
			while(rs.next()) {
				EcoreTreeNode po =new EcoreTreeNode();
            	po.setId(rs.getString(1));
            	po.setName(rs.getString(2));
            	po.setType(rs.getString(3));
            	po.setLevel("3");
            	po.setObjType("1");
            	po.setRegistrationStatus(rs.getString("registration_status"));
            	if(rs.getString(4)!=null) {
            		po.setObjType("2");
            	}
            	ecoreTreeNodes.add(po);
            }
			conn.commit();
		} catch (Exception e) {
			logger.error("查询数据失败!");
			conn.rollback();
			throw e;
		} finally {
			DerbyUtil.close(conn, pstmt, rs);
		}
		return ecoreTreeNodes;
	}
	
	/**
	 * 获取datatypeNodes
	 * @return EcoreTreeNode
	 */
	public EcoreTreeNode findEcoreDataTypeTreeNodes()  throws Exception{
		EcoreTreeNode ecoreTreeNode=new EcoreTreeNode();
		conn = DerbyUtil.getConnection(); // 获得数据库连接
		String dsql = "SELECT T.ID,T.NAME,T.TYPE,T.TRACE,t.registration_status FROM ECORE_DATA_TYPE T ORDER BY T.NAME";
		String cSql = "SELECT T.ID,T.NAME,CODE_SET_ID,t.registration_status FROM ECORE_CODE T ORDER BY T.NAME";
		Map<String,List<EcoreTreeNode>> dataTypeMaps=new HashMap<String,List<EcoreTreeNode>>();
		Map<String,List<EcoreTreeNode>> typeMaps=new HashMap<String,List<EcoreTreeNode>>();
		List<EcoreTreeNode> types=new ArrayList<EcoreTreeNode>();
		try {
			st=conn.createStatement();
			//获取codeSql
			rs=st.executeQuery(cSql);
			while(rs.next()) {
				EcoreTreeNode po =new EcoreTreeNode();
            	po.setId(rs.getString(1));
            	po.setName(rs.getString(2));
            	po.setLevel("4");
            	po.setRegistrationStatus(rs.getString("registration_status"));
            	String codeSetId=rs.getString(3);
            	if(dataTypeMaps.containsKey(codeSetId)) {
            		dataTypeMaps.get(codeSetId).add(po);
            	}else {
            		List<EcoreTreeNode> nodes=new ArrayList<EcoreTreeNode>();
            		nodes.add(po);
            		dataTypeMaps.put(codeSetId,nodes);
            	}
            }
			//获取dataType
			rs=st.executeQuery(dsql);
			while(rs.next()) {
				EcoreTreeNode po =new EcoreTreeNode();
            	po.setId(rs.getString(1));
            	po.setName(rs.getString(2));
            	po.setType(rs.getString(3));
            	po.setLevel("3");
            	po.setObjType("1");
            	po.setRegistrationStatus(rs.getString("registration_status"));
            	if(rs.getString(4)!=null) {
            		po.setObjType("2");
            	}
            	po.setChildNodes(dataTypeMaps.get(rs.getString(1)));
            	String type=rs.getString(3);
            	if(typeMaps.containsKey(type)) {
            		typeMaps.get(type).add(po);
            	}else {
            		List<EcoreTreeNode> nodes=new ArrayList<EcoreTreeNode>();
            		nodes.add(po);
            		typeMaps.put(type,nodes);
            	}
            }
			//获取datatype类型
			types.add(getNodeInfo("Code Sets","1","2",typeMaps.get("1")));
			types.add(getNodeInfo("Text","2","2",typeMaps.get("2")));
			types.add(getNodeInfo("Boolean","3","2",typeMaps.get("3")));
			types.add(getNodeInfo("Indicator","4","2",typeMaps.get("4")));
			types.add(getNodeInfo("Decimal","5","2",typeMaps.get("5")));
			types.add(getNodeInfo("Rate","6","2",typeMaps.get("6")));
			types.add(getNodeInfo("Amount","7","2",typeMaps.get("7")));
			types.add(getNodeInfo("Quantity","8","2",typeMaps.get("8")));
			types.add(getNodeInfo("Time","9","2",typeMaps.get("9")));
			types.add(getNodeInfo("Binary","10","2",typeMaps.get("10")));
			types.add(getNodeInfo("Schema Types","11","2",typeMaps.get("11")));
			types.add(getNodeInfo("User Defined","12","2",typeMaps.get("12")));
			//顶级
			ecoreTreeNode.setName("Data Types");
			ecoreTreeNode.setLevel("1");
			ecoreTreeNode.setType("1");
			ecoreTreeNode.setChildNodes(types);
			conn.commit();
		} catch (Exception e) {
			logger.error("查询数据失败!");
			conn.rollback();
			throw e;
		} finally {
			DerbyUtil.close(conn, pstmt, rs);
		}
		return ecoreTreeNode;
	}
	
	/**
	 * 设置node信息
	 * @param name
	 * @param type
	 * @param level
	 * @param nodes
	 */
	private EcoreTreeNode getNodeInfo(String name,String type,String level,List<EcoreTreeNode> nodes) {
		EcoreTreeNode node=new EcoreTreeNode();
		node.setName(name);
		node.setType(type);
		node.setLevel(level);
		node.setChildNodes(nodes);
		return node;
	}
	
	/**
	 * 保存EcoreDataType数据
	 * @param ecoreDataTypes
	 */
	public void addEcoreDataTypeList(List<EcoreDataType> ecoreDataTypes) throws Exception{
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			
			String sql = "insert into ecore_data_type(id,name,definition,registration_status,removal_date,"
					+ "object_identifier,min_inclusive,min_exclusive,max_inclusive," + 
					"max_exclusive,pattern,previous_version,fraction_digits,total_digits,"
					+ "unit_code,base_value,base_unit_code,min_length,max_length,length,meaning_when_true,"
					+ "meaning_when_false,identification_scheme,value,literal,type,version,trace,process_Contents,"
					+ "namespace,namespace_List,create_user,create_time,is_from_iso20022) "
					+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			pstmt = conn.prepareStatement(sql);
			for (EcoreDataType po : ecoreDataTypes) {
				pstmt.setString(1, po.getId());
				pstmt.setString(2, po.getName());
				pstmt.setString(3, po.getDefinition());
				pstmt.setString(4, po.getRegistrationStatus());
				if(po.getRemovalDate()!=null) {
					pstmt.setDate(5, new Date(po.getRemovalDate().getTime()));
				}else {
					pstmt.setNull(5,Types.DATE);
				}
				pstmt.setString(6, po.getObjectIdentifier());
				pstmt.setString(7, po.getMinInclusive());
				pstmt.setString(8, po.getMinExclusive());
				pstmt.setString(9, po.getMaxInclusive());
				pstmt.setString(10, po.getMaxExclusive());
				pstmt.setString(11, po.getPattern());
				pstmt.setString(12, po.getPreviousVersion());
				if(po.getFractionDigits()!=null) {
					pstmt.setInt(13, po.getFractionDigits());
				}else {
					pstmt.setNull(13,Types.INTEGER);
				}
				if(po.getTotalDigits()!=null) {
					pstmt.setInt(14, po.getTotalDigits());
				}else {
					pstmt.setNull(14,Types.INTEGER);
				}
				pstmt.setString(15, po.getUnitCode());
				if(po.getBaseValue()!=null) {
					pstmt.setDouble(16, po.getBaseValue());
				}else {
					pstmt.setNull(16,Types.DOUBLE);
				}
				pstmt.setString(17, po.getBaseUnitCode());
				if(po.getMinLength()!=null) {
					pstmt.setInt(18, po.getMinLength());
				}else {
					pstmt.setNull(18,Types.INTEGER);
				}
				if(po.getMaxLength()!=null) {
					pstmt.setInt(19, po.getMaxLength());
				}else {
					pstmt.setNull(19,Types.INTEGER);
				}
				if(po.getLength()!=null) {
					pstmt.setInt(20, po.getLength());
				}else {
					pstmt.setNull(20,Types.INTEGER);
				}
				pstmt.setString(21, po.getMeaningWhenTrue());
				pstmt.setString(22, po.getMeaningWhenFalse());
				pstmt.setString(23, po.getIdentificationScheme());
				pstmt.setString(24, po.getValue());
				pstmt.setString(25, po.getLiteral());
				pstmt.setString(26, po.getType());
				pstmt.setString(27, po.getVersion());
				pstmt.setString(28, po.getTrace());
				pstmt.setString(29, po.getProcessContents());
				pstmt.setString(30, po.getNamespace());
				pstmt.setString(31, po.getNamespaceList());
				pstmt.setString(32, po.getCreateUser());
				pstmt.setTimestamp(33, new Timestamp(new java.util.Date().getTime()));
				pstmt.setString(34, po.getIsfromiso20022());
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
	 * 判断是否已经导入过数据
	 * @return boolean
	 */
	public boolean isImported() throws Exception{
		boolean key=false;
		conn = DerbyUtil.getConnection(); // 获得数据库连接
		String csql = "SELECT T.ID FROM ECORE_DATA_TYPE T WHERE T.IS_FROM_ISO20022='1'";
		try {
			st=conn.createStatement();
			rs=st.executeQuery(csql);
			while(rs.next()) {
				key=true;
				break;
			}
			conn.commit();
		} catch (Exception e) {
			logger.error("查询数据失败!");
			conn.rollback();
			throw e;
		} finally {
			DerbyUtil.close(conn, pstmt, rs);
		}
		return key;
	}
	
	private void saveRemovedInfo(String id) throws Exception{
		try {
			String sql = "insert into ECORE_REMOVED_INFO(obj_id,obj_type,create_time) "
					+ "values(?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, "1");
			pstmt.setTimestamp(3, new Timestamp(new java.util.Date().getTime()));
			pstmt.executeUpdate();// 执行更新语句
		} catch (Exception e) {
			logger.error("更新数据失败!");
			conn.rollback();
			throw e;
		}
	}
	
	
	private void saveRemovedInfo(List<RemovedObjectVO> removeds) throws Exception {
		try {
			String sql = "insert into ECORE_REMOVED_INFO(obj_id,obj_type,create_time) "
					+ "values(?,?,?)";
			pstmt = conn.prepareStatement(sql);
			for(RemovedObjectVO v:removeds) {
				pstmt.setString(1, v.getObj_id());
				pstmt.setString(2, v.getObj_type());
				pstmt.setTimestamp(3, new Timestamp(new java.util.Date().getTime()));
				pstmt.addBatch();
			}
			pstmt.executeBatch();// 执行更新语句
		} catch (Exception e) {
			logger.error("更新数据失败!");
			conn.rollback();
			throw e;
		}
	}
	
	/**
	 * 删除数据类型
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteEcoreDataType(String id) throws Exception{
		boolean key=false;
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			String dtsql = "delete from ECORE_DATA_TYPE where id=?" ;
			String ecsql = "delete from ECORE_CODE where CODE_SET_ID=?" ;
			//common
			String csql = "delete from ECORE_CONSTRAINT where obj_id=?"
					+ " or obj_id in (select id from ECORE_CODE where CODE_SET_ID=?)" ;
			String dsql = "delete from ECORE_DOCLET where obj_id=?"
					+ " or obj_id in (select id from ECORE_CODE where CODE_SET_ID=?)" ;
			String eesql = "delete from ECORE_EXAMPLE where obj_id=?"
					+ " or obj_id in (select id from ECORE_CODE where CODE_SET_ID=?)" ;
			String vsql="delete from ECORE_NEXT_VERSIONS where id=?"
					+ " or id in (select id from ECORE_CODE where CODE_SET_ID=?)";
			pstmt = conn.prepareStatement(csql);
			pstmt.setString(1, id);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement(dsql);
			pstmt.setString(1, id);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement(eesql);
			pstmt.setString(1, id);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement(vsql);
			pstmt.setString(1, id);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement(ecsql);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement(dtsql);
			pstmt.setString(1, id);
			int num=pstmt.executeUpdate();
			
			//saveRemovedInfo(id);
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
	 * 更新EcoreDataType数据
	 * @param ecoreDataTypes
	 */
	public void updateEcoreDataTypeList(List<EcoreDataType> ecoreDataTypes) throws Exception{
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			
			String sql = "update ecore_data_type set update_time=?,name=?,definition=?,registration_status=?,removal_date=?,"
					+ "object_identifier=?,min_inclusive=?,min_exclusive=?,max_inclusive=?," + 
					"max_exclusive=?,pattern=?,previous_version=?,fraction_digits=?,total_digits=?,"
					+ "unit_code=?,base_value=?,base_unit_code=?,min_length=?,max_length=?,length=?,meaning_when_true=?,"
					+ "meaning_when_false=?,identification_scheme=?,value=?,literal=?,type=?,version=?,trace=?"
					+ ",process_Contents=?,namespace=?,namespace_List=?, update_user=?,is_from_iso20022=? where id=?";

			// 连接失败,再次请求连接
			if (conn == null) {
				conn = DerbyUtil.getConnection(); // 获得数据库连接
			}
			pstmt = conn.prepareStatement(sql);
			for (EcoreDataType po : ecoreDataTypes) {
				pstmt.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
				pstmt.setString(2, po.getName());
				pstmt.setString(3, po.getDefinition());
				pstmt.setString(4, po.getRegistrationStatus());
				if(po.getRemovalDate()!=null) {
					pstmt.setDate(5, new Date(po.getRemovalDate().getTime()));
				}else {
					pstmt.setNull(5,Types.DATE);
				}
				pstmt.setString(6, po.getObjectIdentifier());
				pstmt.setString(7, po.getMinInclusive());
				pstmt.setString(8, po.getMinExclusive());
				pstmt.setString(9, po.getMaxInclusive());
				pstmt.setString(10, po.getMaxExclusive());
				pstmt.setString(11, po.getPattern());
				pstmt.setString(12, po.getPreviousVersion());
				if(po.getFractionDigits()!=null) {
					pstmt.setInt(13, po.getFractionDigits());
				}else {
					pstmt.setNull(13,Types.INTEGER);
				}
				if(po.getTotalDigits()!=null) {
					pstmt.setInt(14, po.getTotalDigits());
				}else {
					pstmt.setNull(14,Types.INTEGER);
				}
				pstmt.setString(15, po.getUnitCode());
				if(po.getBaseValue()!=null) {
					pstmt.setDouble(16, po.getBaseValue());
				}else {
					pstmt.setNull(16,Types.DOUBLE);
				}
				pstmt.setString(17, po.getBaseUnitCode());
				if(po.getMinLength()!=null) {
					pstmt.setInt(18, po.getMinLength());
				}else {
					pstmt.setNull(18,Types.INTEGER);
				}
				if(po.getMaxLength()!=null) {
					pstmt.setInt(19, po.getMaxLength());
				}else {
					pstmt.setNull(19,Types.INTEGER);
				}
				if(po.getLength()!=null) {
					pstmt.setInt(20, po.getLength());
				}else {
					pstmt.setNull(20,Types.INTEGER);
				}
				pstmt.setString(21, po.getMeaningWhenTrue());
				pstmt.setString(22, po.getMeaningWhenFalse());
				pstmt.setString(23, po.getIdentificationScheme());
				pstmt.setString(24, po.getValue());
				pstmt.setString(25, po.getLiteral());
				pstmt.setString(26, po.getType());
				pstmt.setString(27, po.getVersion());
				pstmt.setString(28, po.getTrace());
				pstmt.setString(29, po.getProcessContents());
				pstmt.setString(30, po.getNamespace());
				pstmt.setString(31, po.getNamespaceList());
				pstmt.setString(32, po.getUpdateUser());
				pstmt.setString(33, po.getIsfromiso20022());
				pstmt.setString(34, po.getId());
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
	 * 保存DataType数据
	 * @param ecoreDataTypeVOs
	 */
	public boolean saveEcoreDataTypeVOList(List<EcoreDataTypeVO> ecoreDataTypeVOs) throws Exception{
		boolean key=false;
		List<EcoreDataType> ecoreDataTypes=new ArrayList<EcoreDataType>();
		List<EcoreConstraint> ecoreConstraints=new ArrayList<EcoreConstraint>();
		List<EcoreCode> ecoreCodes=new ArrayList<EcoreCode>();
		List<EcoreExample> ecoreExamples=new ArrayList<EcoreExample>();
		List<EcoreNextVersions> ecoreNextVersions=new ArrayList<EcoreNextVersions>();
		List<String> dataIds=new ArrayList<String>();
		Map<String,List<String>> codeNameMaps=new HashMap<String, List<String>>();
		List<RemovedObjectVO> removeds=new ArrayList<RemovedObjectVO>();
		//解析保存的内容
		for(EcoreDataTypeVO vo: ecoreDataTypeVOs) {
			EcoreDataType ecoreDataType=vo.getEcoreDataType();
			List<EcoreConstraint> constraints=vo.getEcoreConstraints();
			List<EcoreExample> examples=vo.getEcoreExamples();
			List<EcoreNextVersions> nextVersions=vo.getEcoreNextVersions();
			List<EcoreCodeVO> codeVOs=vo.getEcoreCodeVOs();
			List<RemovedObjectVO> rmds=vo.getRemovedObjectVOs();
			if(constraints!=null && !constraints.isEmpty()) {
				ecoreConstraints.addAll(constraints);
			}
			if(examples!=null) {
				ecoreExamples.addAll(examples);
			}
			if(nextVersions!=null) {
				ecoreNextVersions.addAll(nextVersions);
			}
			//解析CODE
			if(codeVOs!=null) {
				for(EcoreCodeVO cvo:codeVOs) {
					EcoreCode ecoreCode=cvo.getEcoreCode();
					List<EcoreConstraint> cconstraints=cvo.getEcoreConstraints();
					List<EcoreExample> cexamples=cvo.getEcoreExamples();
					List<EcoreNextVersions> cnextVersions=cvo.getEcoreNextVersions();
					if(cconstraints!=null) {
						ecoreConstraints.addAll(cconstraints);
					}
					if(cexamples!=null) {
						ecoreExamples.addAll(cexamples);
					}
					if(cnextVersions!=null) {
						ecoreNextVersions.addAll(cnextVersions);
					}
					ecoreCodes.add(ecoreCode);
				}
			}
			if(ecoreDataType.getTrace()==null) {
				List<String> names=new ArrayList<String>();
				if(codeVOs!=null && !codeVOs.isEmpty()) {
					for(EcoreCodeVO cvo:codeVOs) {
						names.add(cvo.getEcoreCode().getCodeName());
					}
				}
				codeNameMaps.put(ecoreDataType.getId(), names);
			}
			dataIds.add(ecoreDataType.getId());
			ecoreDataTypes.add(ecoreDataType);
			if(rmds!=null && !rmds.isEmpty()) {
				removeds.addAll(rmds);
			}
		}
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			if(dataIds!=null && !dataIds.isEmpty()) {
				for(String id:dataIds) {
					//先删除数据类型下面的所有数据
					deleteDataTypeByIds(id);
					//级联删除trace中的code
					if(codeNameMaps.containsKey(id)) {
						deleteCodeNameById(id,codeNameMaps.get(id));
					}
				}
			}
			//删除已有的
			if(removeds!=null && !removeds.isEmpty()) {
				//saveRemovedInfo(removeds);
			}
			//开始保存
			if(ecoreDataTypes!=null && !ecoreDataTypes.isEmpty()) {
				saveDataType(ecoreDataTypes);
				if(ecoreCodes!=null && !ecoreCodes.isEmpty()) {
					saveCode(ecoreCodes);
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

	private void deleteCodeNameById(String id, List<String> codeNameMaps) throws SQLException {
		String sql="select id from ecore_data_type where trace = ?";
		String ids="";
		String names="";
		pstmt=conn.prepareStatement(sql);
		pstmt.setString(1, id);
		//获取block数据
		rs=pstmt.executeQuery();
		while(rs.next()) {
			ids+="'"+rs.getString(1)+"',";
		}
		if(codeNameMaps!=null && !codeNameMaps.isEmpty()) {
			for(String name:codeNameMaps) {
				names+="'"+name+"',";
			}
		}
		if(!ids.equals("")) {
			ids=ids.substring(0, ids.length()-1);
			String codenamesql="delete from ecore_code where code_set_id in("+ids+")";
			if(!names.equals("")) {
				names=names.substring(0, names.length()-1);
				codenamesql +=" and code_name not in("+names+")";
			}
			pstmt=conn.prepareStatement(codenamesql);
			pstmt.executeUpdate();
		}
	}

	private void deleteDataTypeByIds(String id) throws Exception{
		String ecsql = "delete from ECORE_CODE where CODE_SET_ID=?" ;
		//common
		String csql = "delete from ECORE_CONSTRAINT where obj_id=?"
				+ " or obj_id in (select id from ECORE_CODE where CODE_SET_ID=?)" ;
		String dsql = "delete from ECORE_DOCLET where obj_id=?"
				+ " or obj_id in (select id from ECORE_CODE where CODE_SET_ID=?)" ;
		String eesql = "delete from ECORE_EXAMPLE where obj_id=?"
				+ " or obj_id in (select id from ECORE_CODE where CODE_SET_ID=?)" ;
		String vsql="delete from ECORE_NEXT_VERSIONS where id=?"
				+ " or id in (select id from ECORE_CODE where CODE_SET_ID=?)";
		pstmt = conn.prepareStatement(csql);
		pstmt.setString(1, id);
		pstmt.setString(2, id);
		pstmt.executeUpdate();
		
		pstmt = conn.prepareStatement(dsql);
		pstmt.setString(1, id);
		pstmt.setString(2, id);
		pstmt.executeUpdate();
		
		pstmt = conn.prepareStatement(eesql);
		pstmt.setString(1, id);
		pstmt.setString(2, id);
		pstmt.executeUpdate();
		
		pstmt = conn.prepareStatement(vsql);
		pstmt.setString(1, id);
		pstmt.setString(2, id);
		pstmt.executeUpdate();
		
		pstmt = conn.prepareStatement(ecsql);
		pstmt.setString(1, id);
		pstmt.executeUpdate();
	}
	
	private void saveDataType(List<EcoreDataType> ecoreDataTypes) throws Exception{
		List<EcoreDataType> addEcoreDataTypes=new ArrayList<EcoreDataType>();
		List<EcoreDataType> updateEcoreDataTypes=new ArrayList<EcoreDataType>();
		for(EcoreDataType po : ecoreDataTypes) {
			String csql = "SELECT T.ID FROM ECORE_DATA_TYPE T WHERE id=?";
			pstmt=conn.prepareStatement(csql);
			pstmt.setString(1, po.getId());
			rs=pstmt.executeQuery();
			if(rs.next()) {
				updateEcoreDataTypes.add(po);
			}else {
				addEcoreDataTypes.add(po);
			}
		}
		String sql = "insert into ecore_data_type(id,name,definition,registration_status,removal_date,"
				+ "object_identifier,min_inclusive,min_exclusive,max_inclusive," + 
				"max_exclusive,pattern,previous_version,fraction_digits,total_digits,"
				+ "unit_code,base_value,base_unit_code,min_length,max_length,length,meaning_when_true,"
				+ "meaning_when_false,identification_scheme,value,literal,type,version,trace,process_Contents,"
				+ "namespace,namespace_List,create_user,create_time,is_from_iso20022) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		for (EcoreDataType po : addEcoreDataTypes) {
			pstmt.setString(1, po.getId());
			pstmt.setString(2, po.getName());
			pstmt.setString(3, po.getDefinition());
			pstmt.setString(4, po.getRegistrationStatus());
			if(po.getRemovalDate()!=null) {
				pstmt.setDate(5, new Date(po.getRemovalDate().getTime()));
			}else {
				pstmt.setNull(5,Types.DATE);
			}
			pstmt.setString(6, po.getObjectIdentifier());
			pstmt.setString(7, po.getMinInclusive());
			pstmt.setString(8, po.getMinExclusive());
			pstmt.setString(9, po.getMaxInclusive());
			pstmt.setString(10, po.getMaxExclusive());
			pstmt.setString(11, po.getPattern());
			pstmt.setString(12, po.getPreviousVersion());
			if(po.getFractionDigits()!=null) {
				pstmt.setInt(13, po.getFractionDigits());
			}else {
				pstmt.setNull(13,Types.INTEGER);
			}
			if(po.getTotalDigits()!=null) {
				pstmt.setInt(14, po.getTotalDigits());
			}else {
				pstmt.setNull(14,Types.INTEGER);
			}
			pstmt.setString(15, po.getUnitCode());
			if(po.getBaseValue()!=null) {
				pstmt.setDouble(16, po.getBaseValue());
			}else {
				pstmt.setNull(16,Types.DOUBLE);
			}
			pstmt.setString(17, po.getBaseUnitCode());
			if(po.getMinLength()!=null) {
				pstmt.setInt(18, po.getMinLength());
			}else {
				pstmt.setNull(18,Types.INTEGER);
			}
			if(po.getMaxLength()!=null) {
				pstmt.setInt(19, po.getMaxLength());
			}else {
				pstmt.setNull(19,Types.INTEGER);
			}
			if(po.getLength()!=null) {
				pstmt.setInt(20, po.getLength());
			}else {
				pstmt.setNull(20,Types.INTEGER);
			}
			pstmt.setString(21, po.getMeaningWhenTrue());
			pstmt.setString(22, po.getMeaningWhenFalse());
			pstmt.setString(23, po.getIdentificationScheme());
			pstmt.setString(24, po.getValue());
			pstmt.setString(25, po.getLiteral());
			pstmt.setString(26, po.getType());
			pstmt.setString(27, po.getVersion());
			pstmt.setString(28, po.getTrace());
			pstmt.setString(29, po.getProcessContents());
			pstmt.setString(30, po.getNamespace());
			pstmt.setString(31, po.getNamespaceList());
			pstmt.setString(32, po.getCreateUser());
			pstmt.setTimestamp(33, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(34, po.getIsfromiso20022());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
		
		String usql = "update ecore_data_type set update_time=?,name=?,definition=?,registration_status=?,removal_date=?,"
				+ "object_identifier=?,min_inclusive=?,min_exclusive=?,max_inclusive=?," + 
				"max_exclusive=?,pattern=?,previous_version=?,fraction_digits=?,total_digits=?,"
				+ "unit_code=?,base_value=?,base_unit_code=?,min_length=?,max_length=?,length=?,meaning_when_true=?,"
				+ "meaning_when_false=?,identification_scheme=?,value=?,literal=?,type=?,version=?,trace=?"
				+ ",process_Contents=?,namespace=?,namespace_List=?, update_user=?,is_from_iso20022=? where id=?";
		pstmt = conn.prepareStatement(usql);
		for (EcoreDataType po : updateEcoreDataTypes) {
			pstmt.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(2, po.getName());
			pstmt.setString(3, po.getDefinition());
			pstmt.setString(4, po.getRegistrationStatus());
			if(po.getRemovalDate()!=null) {
				pstmt.setDate(5, new Date(po.getRemovalDate().getTime()));
			}else {
				pstmt.setNull(5,Types.DATE);
			}
			pstmt.setString(6, po.getObjectIdentifier());
			pstmt.setString(7, po.getMinInclusive());
			pstmt.setString(8, po.getMinExclusive());
			pstmt.setString(9, po.getMaxInclusive());
			pstmt.setString(10, po.getMaxExclusive());
			pstmt.setString(11, po.getPattern());
			pstmt.setString(12, po.getPreviousVersion());
			if(po.getFractionDigits()!=null) {
				pstmt.setInt(13, po.getFractionDigits());
			}else {
				pstmt.setNull(13,Types.INTEGER);
			}
			if(po.getTotalDigits()!=null) {
				pstmt.setInt(14, po.getTotalDigits());
			}else {
				pstmt.setNull(14,Types.INTEGER);
			}
			pstmt.setString(15, po.getUnitCode());
			if(po.getBaseValue()!=null) {
				pstmt.setDouble(16, po.getBaseValue());
			}else {
				pstmt.setNull(16,Types.DOUBLE);
			}
			pstmt.setString(17, po.getBaseUnitCode());
			if(po.getMinLength()!=null) {
				pstmt.setInt(18, po.getMinLength());
			}else {
				pstmt.setNull(18,Types.INTEGER);
			}
			if(po.getMaxLength()!=null) {
				pstmt.setInt(19, po.getMaxLength());
			}else {
				pstmt.setNull(19,Types.INTEGER);
			}
			if(po.getLength()!=null) {
				pstmt.setInt(20, po.getLength());
			}else {
				pstmt.setNull(20,Types.INTEGER);
			}
			pstmt.setString(21, po.getMeaningWhenTrue());
			pstmt.setString(22, po.getMeaningWhenFalse());
			pstmt.setString(23, po.getIdentificationScheme());
			pstmt.setString(24, po.getValue());
			pstmt.setString(25, po.getLiteral());
			pstmt.setString(26, po.getType());
			pstmt.setString(27, po.getVersion());
			pstmt.setString(28, po.getTrace());
			pstmt.setString(29, po.getProcessContents());
			pstmt.setString(30, po.getNamespace());
			pstmt.setString(31, po.getNamespaceList());
			pstmt.setString(32, po.getUpdateUser());
			pstmt.setString(33, po.getIsfromiso20022());
			pstmt.setString(34, po.getId());
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
			pstmt.setString(2, po.getObj_id());
			pstmt.setString(3, po.getObj_type());
			pstmt.setString(4, po.getExample());
			pstmt.setString(5, po.getCreateUser());
			pstmt.setTimestamp(6, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(7, po.getIsfromiso20022());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
	}
	
	private void saveCode(List<EcoreCode> ecoreCodes) throws Exception{
		String sql = "insert into ecore_code(id,name,code_name,definition,registration_status"
				+ ",removal_date,object_identifier,code_set_id,create_user,create_time,is_from_iso20022) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		for (EcoreCode po : ecoreCodes) {
			pstmt.setString(1, po.getId());
			pstmt.setString(2, po.getName());
			pstmt.setString(3, po.getCodeName());
			if(po.getDefinition()!=null) {
				pstmt.setString(4, po.getDefinition());
			} else {
				pstmt.setString(4, "");
			}
			pstmt.setString(5, po.getRegistrationStatus());
			if(po.getRemovalDate()!=null) {
				pstmt.setDate(6, new Date(po.getRemovalDate().getTime()));
			} else {
				pstmt.setNull(6,Types.DATE);
			}
			pstmt.setString(7, po.getObjectIdentifier());
			pstmt.setString(8, po.getCodesetid());
			pstmt.setString(9, po.getCreateUser());
			pstmt.setTimestamp(10, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(11, po.getIsfromiso20022());
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
	 * 查询DataTypeVO数据
	 * @param id
	 */
	public EcoreDataTypeVO findDataTypeVO(String id) throws Exception{
		EcoreDataTypeVO vo=null;
		List<EcoreExample> ecoreExamples=new ArrayList<EcoreExample>();
		List<EcoreConstraint> ecoreConstraints=new ArrayList<EcoreConstraint>();
		List<EcoreNextVersions> ecoreNextVersions=new ArrayList<EcoreNextVersions>();
		List<EcoreCodeVO> ecoreCodeVOs=new ArrayList<EcoreCodeVO>();
		Map<String,String> idMap=new HashMap<String,String>();
		String presql="select id from ecore_code where code_set_id='"+id+"'";
		String mcsql = "select * FROM ecore_data_type where id=?" ;
		String csql = "select * from ECORE_CONSTRAINT where obj_id=?";
		String eeql = "select * from ECORE_EXAMPLE where obj_id=?";
		String vsql="select * from ECORE_NEXT_VERSIONS where id=?";
		String dstrSql="select id,name from ecore_data_type";
		conn = DerbyUtil.getConnection(); // 获得数据库连接
		st=conn.createStatement();
		rs=st.executeQuery(dstrSql);
		while(rs.next()) {
			idMap.put(rs.getString(1), rs.getString(2));
		}
		pstmt=conn.prepareStatement(mcsql);
		pstmt.setString(1, id);
		//获取block数据
		rs=pstmt.executeQuery();
		while(rs.next()) {
			EcoreDataType dataType = new EcoreDataType();
			vo=new EcoreDataTypeVO();
			dataType.setRemovalDate(rs.getDate("removal_date"));
			dataType.setCreateTime(rs.getDate("create_time"));
			dataType.setCreateUser(rs.getString("create_user"));
			dataType.setUpdateTime(rs.getDate("update_time"));
			dataType.setUpdateUser(rs.getString("update_user"));
			dataType.setPreviousVersion(rs.getString("previous_version"));
			dataType.setIsfromiso20022(rs.getString("is_from_iso20022"));
			dataType.setVersion(rs.getString("version"));
			dataType.setId(rs.getString("ID"));
			dataType.setName(rs.getString("NAME"));
			dataType.setType(rs.getString("TYPE"));
			dataType.setDefinition(rs.getString("DEFINITION"));
			dataType.setNamespace(rs.getString("NAMESPACE"));
			dataType.setNamespaceList(rs.getString("NAMESPACE_LIST"));
			dataType.setProcessContents(rs.getString("PROCESS_CONTENTS"));
			dataType.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
			dataType.setRegistrationStatus(rs.getString("REGISTRATION_STATUS"));
			dataType.setPreviousVersion(rs.getString("PREVIOUS_VERSION"));
			dataType.setMinLength(rs.getInt("MIN_LENGTH"));
			dataType.setMaxLength(rs.getInt("MAX_LENGTH"));
			dataType.setLength(rs.getInt("LENGTH"));
			dataType.setPattern(rs.getString("PATTERN"));
			dataType.setMeaningWhenFalse(rs.getString("MEANING_WHEN_FALSE"));
			dataType.setMeaningWhenTrue(rs.getString("MEANING_WHEN_TRUE"));
			dataType.setMinInclusive(rs.getString("MIN_INCLUSIVE"));
			dataType.setMinExclusive(rs.getString("MIN_EXCLUSIVE"));
			dataType.setMaxInclusive(rs.getString("MAX_INCLUSIVE"));
			dataType.setMaxExclusive(rs.getString("MAX_EXCLUSIVE"));
			dataType.setTotalDigits(rs.getInt("TOTAL_DIGITS"));
			dataType.setFractionDigits(rs.getInt("FRACTION_DIGITS"));
			dataType.setIdentificationScheme(rs.getString("IDENTIFICATION_SCHEME"));
			dataType.setBaseValue(rs.getDouble("BASE_VALUE"));
			dataType.setBaseUnitCode(rs.getString("BASE_UNIT_CODE"));
			dataType.setTrace(rs.getString("TRACE"));
			vo.setEcoreDataType(dataType);
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
				ec.setObj_id(rs.getString("OBJ_ID"));
				ec.setObj_type(rs.getString("Obj_type"));
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
			if(ecoreExamples!=null && !ecoreExamples.isEmpty()) {
				vo.setEcoreExamples(ecoreExamples);
			}
			if(ecoreConstraints!=null && !ecoreConstraints.isEmpty()) {
				vo.setEcoreConstraints(ecoreConstraints);
			}
			if(ecoreNextVersions!=null && !ecoreNextVersions.isEmpty()) {
				vo.setEcoreNextVersions(ecoreNextVersions);
			}
			//elment
			st=conn.createStatement();
			rs=st.executeQuery(presql);
			List<String> ids=new ArrayList<String>();
			while(rs.next()) {
	        	String mbId=rs.getString(1);
	        	ids.add(mbId);
	        }
			if(ids!=null && !ids.isEmpty()) {
				for(String eid:ids) {
		        	EcoreCodeVO evo=findEcoreCodeVO(eid,idMap);
		        	if(evo!=null) {
		        		ecoreCodeVOs.add(evo);
		        	}
				}
				vo.setEcoreCodeVOs(ecoreCodeVOs);
			}
		}
		conn.commit();
		return vo;
	}
	
	private EcoreCodeVO findEcoreCodeVO(String id, Map<String, String> idMap) throws SQLException{
		EcoreCodeVO vo=null;
		List<EcoreExample> ecoreExamples=new ArrayList<EcoreExample>();
		List<EcoreConstraint> ecoreConstraints=new ArrayList<EcoreConstraint>();
		List<EcoreNextVersions> ecoreNextVersions=new ArrayList<EcoreNextVersions>();
		String codeSql="select * from ECORE_CODE WHERE id=?";
		String csql = "select * from ECORE_CONSTRAINT where obj_id=?";
		String eeql = "select * from ECORE_EXAMPLE where obj_id=?";
		String vsql="select * from ECORE_NEXT_VERSIONS where id=?";

		pstmt=conn.prepareStatement(codeSql);
		pstmt.setString(1, id);
		//获取block数据
		rs=pstmt.executeQuery();
		while(rs.next()) {
			EcoreCode me = new EcoreCode();
			vo=new EcoreCodeVO();
			me.setId(rs.getString("ID"));
			me.setName(rs.getString("NAME"));
			me.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
			me.setDefinition(rs.getString("DEFINITION"));
			me.setRegistrationStatus(rs.getString("registration_status"));
			me.setRemovalDate(rs.getDate("removal_date"));
			me.setCreateTime(rs.getDate("create_time"));
			me.setCreateUser(rs.getString("create_user"));
			me.setUpdateTime(rs.getDate("update_time"));
			me.setCodeName(rs.getString("code_name"));
			me.setUpdateUser(rs.getString("update_user"));
			me.setCodesetid(rs.getString("code_set_id"));
			me.setIsfromiso20022(rs.getString("is_from_iso20022"));
			vo.setEcoreCode(me);
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
				ec.setObj_id(rs.getString("OBJ_ID"));
				ec.setObj_type(rs.getString("Obj_type"));
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
			if(ecoreExamples!=null && !ecoreExamples.isEmpty()) {
				vo.setEcoreExamples(ecoreExamples);
			}
			if(ecoreConstraints!=null && !ecoreConstraints.isEmpty()) {
				vo.setEcoreConstraints(ecoreConstraints);
			}
			if(ecoreNextVersions!=null && !ecoreNextVersions.isEmpty()) {
				vo.setEcoreNextVersions(ecoreNextVersions);
			}
		}
		return vo;
	}
	
	
	/**
	 * 复制版本
	 * @return
	 * @throws Exception
	 */
	public EcoreDataTypeVO newCodeSetVersion(String id) throws Exception{
		EcoreDataTypeVO vo=null;
		String newDataTypeId="";
		String traceId="";
		String traceName="";
		List<EcoreDataTypeVO> types=new ArrayList<EcoreDataTypeVO>();
		List<EcoreExample> ecoreExamples=new ArrayList<EcoreExample>();
		List<EcoreConstraint> ecoreConstraints=new ArrayList<EcoreConstraint>();
		List<EcoreCodeVO> ecoreCodeVOs=new ArrayList<EcoreCodeVO>();
		String presql="select id from ecore_code where code_set_id='"+id+"'";
		String mcsql = "select * FROM ecore_data_type where id=?" ;
		String csql = "select * from ECORE_CONSTRAINT where obj_id=?";
		String eeql = "select * from ECORE_EXAMPLE where obj_id=?";
		String dstrSql="select id,name from ecore_data_type where trace=?";
		String sstrSql="select id,name from ecore_data_type where id=?";
		Pattern ptn=Pattern.compile("[^0-9]");
		conn = DerbyUtil.getConnection(); // 获得数据库连接
		pstmt=conn.prepareStatement(mcsql);
		pstmt.setString(1, id);
		//获取block数据
		rs=pstmt.executeQuery();
		while(rs.next()) {
			EcoreDataType dataType = new EcoreDataType();
			vo=new EcoreDataTypeVO();
			newDataTypeId=UUID.randomUUID().toString();
			traceId=rs.getString("TRACE");
			dataType.setRemovalDate(rs.getDate("removal_date"));
			dataType.setCreateTime(rs.getDate("create_time"));
			dataType.setCreateUser(rs.getString("create_user"));
			dataType.setUpdateTime(rs.getDate("update_time"));
			dataType.setUpdateUser(rs.getString("update_user"));
			dataType.setPreviousVersion(rs.getString("previous_version"));
			dataType.setIsfromiso20022(rs.getString("is_from_iso20022"));
			dataType.setVersion(rs.getString("version"));
			dataType.setId(newDataTypeId);
			dataType.setName(rs.getString("NAME"));
			dataType.setType(rs.getString("TYPE"));
			dataType.setDefinition(rs.getString("DEFINITION"));
			dataType.setNamespace(rs.getString("NAMESPACE"));
			dataType.setNamespaceList(rs.getString("NAMESPACE_LIST"));
			dataType.setProcessContents(rs.getString("PROCESS_CONTENTS"));
			dataType.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
			dataType.setRegistrationStatus(ConstantUtil.PROVISIONALLY_REGISTERED);
			dataType.setPreviousVersion(rs.getString("PREVIOUS_VERSION"));
			dataType.setMinLength(rs.getInt("MIN_LENGTH"));
			dataType.setMaxLength(rs.getInt("MAX_LENGTH"));
			dataType.setLength(rs.getInt("LENGTH"));
			dataType.setPattern(rs.getString("PATTERN"));
			dataType.setMeaningWhenFalse(rs.getString("MEANING_WHEN_FALSE"));
			dataType.setMeaningWhenTrue(rs.getString("MEANING_WHEN_TRUE"));
			dataType.setMinInclusive(rs.getString("MIN_INCLUSIVE"));
			dataType.setMinExclusive(rs.getString("MIN_EXCLUSIVE"));
			dataType.setMaxInclusive(rs.getString("MAX_INCLUSIVE"));
			dataType.setMaxExclusive(rs.getString("MAX_EXCLUSIVE"));
			dataType.setTotalDigits(rs.getInt("TOTAL_DIGITS"));
			dataType.setFractionDigits(rs.getInt("FRACTION_DIGITS"));
			dataType.setIdentificationScheme(rs.getString("IDENTIFICATION_SCHEME"));
			dataType.setBaseValue(rs.getDouble("BASE_VALUE"));
			dataType.setBaseUnitCode(rs.getString("BASE_UNIT_CODE"));
			dataType.setTrace(rs.getString("TRACE"));
			vo.setEcoreDataType(dataType);
			break;
		}
		if(vo!=null) {
        	pstmt=conn.prepareStatement(csql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreConstraint ec=new EcoreConstraint();
				String newcId=UUID.randomUUID().toString();
				ec.setId(newcId);
				ec.setName(rs.getString("NAME"));
				ec.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
				ec.setDefinition(rs.getString("DEFINITION"));
				ec.setExpression(rs.getString("expression"));
				ec.setExpressionlanguage(rs.getString("expression_language"));
				ec.setObj_id(newDataTypeId);
				ec.setObj_type(rs.getString("Obj_type"));
				ec.setRegistrationStatus(ConstantUtil.PROVISIONALLY_REGISTERED);
				ec.setRemovalDate(rs.getDate("removal_date"));
				ec.setCreateTime(new java.util.Date());
				ec.setCreateUser(rs.getString("create_user"));
				ec.setUpdateTime(new java.util.Date());
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
				String neweId=UUID.randomUUID().toString();
				ec.setId(neweId);
				ec.setObj_id(newDataTypeId);
				ec.setObj_type(rs.getString("Obj_type"));
				ec.setExample(rs.getString("example"));
				ec.setCreateTime(new java.util.Date());
				ec.setCreateUser(rs.getString("create_user"));
				ec.setUpdateTime(new java.util.Date());
				ec.setUpdateUser(rs.getString("update_user"));
				ec.setIsfromiso20022(rs.getString("is_from_iso20022"));
				ecoreExamples.add(ec);
			}
			if(ecoreExamples!=null && !ecoreExamples.isEmpty()) {
				vo.setEcoreExamples(ecoreExamples);
			}
			if(ecoreConstraints!=null && !ecoreConstraints.isEmpty()) {
				vo.setEcoreConstraints(ecoreConstraints);
			}
			//elment
			st=conn.createStatement();
			rs=st.executeQuery(presql);
			List<String> ids=new ArrayList<String>();
			while(rs.next()) {
	        	String mbId=rs.getString(1);
	        	ids.add(mbId);
	        }
			if(ids!=null && !ids.isEmpty()) {
				for(String eid:ids) {
		        	EcoreCodeVO evo=findEcoreCodeVO(newDataTypeId,eid);
		        	if(evo!=null) {
		        		ecoreCodeVOs.add(evo);
		        	}
				}
				vo.setEcoreCodeVOs(ecoreCodeVOs);
			}
			pstmt=conn.prepareStatement(sstrSql);
			pstmt.setString(1, traceId);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				traceName=rs.getString("NAME");
			}
			int maxNum=0;
			pstmt=conn.prepareStatement(dstrSql);
			pstmt.setString(1, traceId);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				String name=rs.getString("NAME");
				Matcher matcher=ptn.matcher(name);
				String tmp=matcher.replaceAll("").trim();
				maxNum=Integer.parseInt(tmp)>maxNum?Integer.parseInt(tmp):maxNum;
			}
			maxNum++;
			String newName=traceName.replaceAll("\\d+","").replaceAll("Code", "")+maxNum+"Code";
			vo.getEcoreDataType().setName(newName);
		}
		conn.commit();
		types.add(vo);
		saveEcoreDataTypeVOList(types);
		return vo;
	}
	
	private EcoreCodeVO findEcoreCodeVO(String newDataTypeId,String id) throws SQLException{
		EcoreCodeVO vo=null;
		String newCodeId=null;
		List<EcoreExample> ecoreExamples=new ArrayList<EcoreExample>();
		List<EcoreConstraint> ecoreConstraints=new ArrayList<EcoreConstraint>();
		String codeSql="select * from ECORE_CODE WHERE id=?";
		String csql = "select * from ECORE_CONSTRAINT where obj_id=?";
		String eeql = "select * from ECORE_EXAMPLE where obj_id=?";

		pstmt=conn.prepareStatement(codeSql);
		pstmt.setString(1, id);
		//获取block数据
		rs=pstmt.executeQuery();
		while(rs.next()) {
			EcoreCode me = new EcoreCode();
			newCodeId=UUID.randomUUID().toString();
			vo=new EcoreCodeVO();
			me.setId(newCodeId);
			me.setName(rs.getString("NAME"));
			me.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
			me.setDefinition(rs.getString("DEFINITION"));
			me.setRegistrationStatus(ConstantUtil.PROVISIONALLY_REGISTERED);
			me.setRemovalDate(rs.getDate("removal_date"));
			me.setCreateTime(new java.util.Date());
			me.setCreateUser(rs.getString("create_user"));
			me.setUpdateTime(new java.util.Date());
			me.setCodeName(rs.getString("code_name"));
			me.setUpdateUser(rs.getString("update_user"));
			me.setCodesetid(newDataTypeId);
			me.setIsfromiso20022(rs.getString("is_from_iso20022"));
			vo.setEcoreCode(me);
			break;
        }
		if(vo!=null) {
			pstmt=conn.prepareStatement(csql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreConstraint ec=new EcoreConstraint();
				String newcId=UUID.randomUUID().toString();
				ec.setId(newcId);
				ec.setName(rs.getString("NAME"));
				ec.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
				ec.setDefinition(rs.getString("DEFINITION"));
				ec.setExpression(rs.getString("expression"));
				ec.setExpressionlanguage(rs.getString("expression_language"));
				ec.setObj_id(newCodeId);
				ec.setObj_type(rs.getString("Obj_type"));
				ec.setRegistrationStatus(ConstantUtil.PROVISIONALLY_REGISTERED);
				ec.setRemovalDate(rs.getDate("removal_date"));
				ec.setCreateTime(new java.util.Date());
				ec.setCreateUser(rs.getString("create_user"));
				ec.setUpdateTime(new java.util.Date());
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
				String neweId=UUID.randomUUID().toString();
				ec.setId(neweId);
				ec.setObj_id(newCodeId);
				ec.setObj_type(rs.getString("Obj_type"));
				ec.setExample(rs.getString("example"));
				ec.setCreateTime(new java.util.Date());
				ec.setCreateUser(rs.getString("create_user"));
				ec.setUpdateTime(new java.util.Date());
				ec.setUpdateUser(rs.getString("update_user"));
				ec.setIsfromiso20022(rs.getString("is_from_iso20022"));
				ecoreExamples.add(ec);
			}
			if(ecoreExamples!=null && !ecoreExamples.isEmpty()) {
				vo.setEcoreExamples(ecoreExamples);
			}
			if(ecoreConstraints!=null && !ecoreConstraints.isEmpty()) {
				vo.setEcoreConstraints(ecoreConstraints);
			}
		}
		return vo;
	}
	
	
	/**
	 * 复制Derive
	 * @return
	 * @throws Exception
	 */
	public EcoreDataTypeVO newCodeSetDerive(String id) throws Exception{
		EcoreDataTypeVO vo=null;
		String newDataTypeId="";
		String traceId="";
		String oldName="";
		List<EcoreDataTypeVO> types=new ArrayList<EcoreDataTypeVO>();
		List<EcoreExample> ecoreExamples=new ArrayList<EcoreExample>();
		List<EcoreConstraint> ecoreConstraints=new ArrayList<EcoreConstraint>();
		List<EcoreCodeVO> ecoreCodeVOs=new ArrayList<EcoreCodeVO>();
		String mcsql = "select * FROM ecore_data_type where id=?" ;
		String csql = "select * from ECORE_CONSTRAINT where obj_id=?";
		String eeql = "select * from ECORE_EXAMPLE where obj_id=?";
		String dstrSql="select id,name from ecore_data_type where trace=?";
		String presql="select id from ecore_code where code_set_id='"+id+"'";
		Pattern ptn=Pattern.compile("[^0-9]");
		conn = DerbyUtil.getConnection(); // 获得数据库连接
		pstmt=conn.prepareStatement(mcsql);
		pstmt.setString(1, id);
		//获取block数据
		rs=pstmt.executeQuery();
		while(rs.next()) {
			EcoreDataType dataType = new EcoreDataType();
			vo=new EcoreDataTypeVO();
			newDataTypeId=UUID.randomUUID().toString();
			traceId=rs.getString("ID");
			oldName=rs.getString("NAME");
			dataType.setRemovalDate(rs.getDate("removal_date"));
			dataType.setCreateTime(rs.getDate("create_time"));
			dataType.setCreateUser(rs.getString("create_user"));
			dataType.setUpdateTime(rs.getDate("update_time"));
			dataType.setUpdateUser(rs.getString("update_user"));
			dataType.setPreviousVersion(rs.getString("previous_version"));
			dataType.setIsfromiso20022(rs.getString("is_from_iso20022"));
			dataType.setVersion(rs.getString("version"));
			dataType.setId(newDataTypeId);
			dataType.setName(rs.getString("NAME"));
			dataType.setType(rs.getString("TYPE"));
			dataType.setDefinition(rs.getString("DEFINITION"));
			dataType.setNamespace(rs.getString("NAMESPACE"));
			dataType.setNamespaceList(rs.getString("NAMESPACE_LIST"));
			dataType.setProcessContents(rs.getString("PROCESS_CONTENTS"));
			dataType.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
			dataType.setRegistrationStatus(ConstantUtil.ADDED_REGISTERED);
			dataType.setPreviousVersion(rs.getString("PREVIOUS_VERSION"));
			dataType.setMinLength(rs.getInt("MIN_LENGTH"));
			dataType.setMaxLength(rs.getInt("MAX_LENGTH"));
			dataType.setLength(rs.getInt("LENGTH"));
			dataType.setPattern(rs.getString("PATTERN"));
			dataType.setMeaningWhenFalse(rs.getString("MEANING_WHEN_FALSE"));
			dataType.setMeaningWhenTrue(rs.getString("MEANING_WHEN_TRUE"));
			dataType.setMinInclusive(rs.getString("MIN_INCLUSIVE"));
			dataType.setMinExclusive(rs.getString("MIN_EXCLUSIVE"));
			dataType.setMaxInclusive(rs.getString("MAX_INCLUSIVE"));
			dataType.setMaxExclusive(rs.getString("MAX_EXCLUSIVE"));
			dataType.setTotalDigits(rs.getInt("TOTAL_DIGITS"));
			dataType.setFractionDigits(rs.getInt("FRACTION_DIGITS"));
			dataType.setIdentificationScheme(rs.getString("IDENTIFICATION_SCHEME"));
			dataType.setBaseValue(rs.getDouble("BASE_VALUE"));
			dataType.setBaseUnitCode(rs.getString("BASE_UNIT_CODE"));
			dataType.setTrace(id);
			vo.setEcoreDataType(dataType);
			break;
		}
		if(vo!=null) {
        	pstmt=conn.prepareStatement(csql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreConstraint ec=new EcoreConstraint();
				String newcId=UUID.randomUUID().toString();
				ec.setId(newcId);
				ec.setName(rs.getString("NAME"));
				ec.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
				ec.setDefinition(rs.getString("DEFINITION"));
				ec.setExpression(rs.getString("expression"));
				ec.setExpressionlanguage(rs.getString("expression_language"));
				ec.setObj_id(newDataTypeId);
				ec.setObj_type(rs.getString("Obj_type"));
				ec.setRegistrationStatus(ConstantUtil.PROVISIONALLY_REGISTERED);
				ec.setRemovalDate(rs.getDate("removal_date"));
				ec.setCreateTime(new java.util.Date());
				ec.setCreateUser(rs.getString("create_user"));
				ec.setUpdateTime(new java.util.Date());
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
				String neweId=UUID.randomUUID().toString();
				ec.setId(neweId);
				ec.setObj_id(newDataTypeId);
				ec.setObj_type(rs.getString("Obj_type"));
				ec.setExample(rs.getString("example"));
				ec.setCreateTime(new java.util.Date());
				ec.setCreateUser(rs.getString("create_user"));
				ec.setUpdateTime(new java.util.Date());
				ec.setUpdateUser(rs.getString("update_user"));
				ec.setIsfromiso20022(rs.getString("is_from_iso20022"));
				ecoreExamples.add(ec);
			}
			if(ecoreExamples!=null && !ecoreExamples.isEmpty()) {
				vo.setEcoreExamples(ecoreExamples);
			}
			if(ecoreConstraints!=null && !ecoreConstraints.isEmpty()) {
				vo.setEcoreConstraints(ecoreConstraints);
			}
			int maxNum=0;
			pstmt=conn.prepareStatement(dstrSql);
			pstmt.setString(1, traceId);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				String name=rs.getString("NAME");
				Matcher matcher=ptn.matcher(name);
				String tmp=matcher.replaceAll("").trim();
				maxNum=Integer.parseInt(tmp)>maxNum?Integer.parseInt(tmp):maxNum;
			}
			maxNum++;
			String newName=oldName.replaceAll("\\d+","").replaceAll("Code", "")+maxNum+"Code";
			vo.getEcoreDataType().setName(newName);
			//CODE
			st=conn.createStatement();
			rs=st.executeQuery(presql);
			List<String> ids=new ArrayList<String>();
			while(rs.next()) {
	        	String mbId=rs.getString(1);
	        	ids.add(mbId);
	        }
			if(ids!=null && !ids.isEmpty()) {
				for(String eid:ids) {
		        	EcoreCodeVO evo=findEcoreCodeVO(newDataTypeId,eid);
		        	if(evo!=null) {
		        		ecoreCodeVOs.add(evo);
		        	}
				}
				vo.setEcoreCodeVOs(ecoreCodeVOs);
			}
		}
		conn.commit();
		types.add(vo);
		saveEcoreDataTypeVOList(types);
		return vo;
	}
}
