package org.cufir.s.data.dao.impl;

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
import org.cufir.s.data.Constant;
import org.cufir.s.ecore.bean.EcoreCode;
import org.cufir.s.ecore.bean.EcoreConstraint;
import org.cufir.s.ecore.bean.EcoreDataType;
import org.cufir.s.ecore.bean.EcoreExample;
import org.cufir.s.ecore.bean.EcoreNextVersions;
import org.cufir.s.data.util.DbUtil;
import org.cufir.s.data.util.DerbyUtil;
import org.cufir.s.data.vo.EcoreCodeVO;
import org.cufir.s.data.vo.EcoreDataTypeVO;
import org.cufir.s.data.vo.EcoreTreeNode;

/**
 * EcoreDataType数据库操作
 * @author tangmaoquan
 * @Date 2021年10月15日
 */
public class EcoreDataTypeImpl{

	private static Logger logger = Logger.getLogger(EcoreDataTypeImpl.class);
	
	private final static String QSQL = "SELECT t.ID,t.DEFINITION,t.NAME,t.REGISTRATION_STATUS,t.REMOVAL_DATE,t.OBJECT_IDENTIFIER,"
			+ "t.MIN_INCLUSIVE,t.MIN_EXCLUSIVE,t.MAX_INCLUSIVE,t.MAX_EXCLUSIVE,t.PATTERN,t.PREVIOUS_VERSION,t.FRACTION_DIGITS,t.TOTAL_DIGITS,"
			+ "t.UNIT_CODE,t.BASE_VALUE,t.BASE_UNIT_CODE,t.MIN_LENGTH,t.MAX_LENGTH,t.LENGTH,t.MEANING_WHEN_TRUE,t.MEANING_WHEN_FALSE,"
			+ "t.IDENTIFICATION_SCHEME,t.VALUE,t.LITERAL,t.TYPE,t.VERSION,t.TRACE,t.IS_FROM_ISO20022,t.PROCESS_CONTENTS,t.NAMESPACE,"
			+ "t.NAMESPACE_LIST,t.CREATE_TIME,t.UPDATE_TIME,t.CREATE_USER,t.UPDATE_USER FROM ECORE_DATA_TYPE t WHERE 1=1 ";

	private static Connection conn = null;
	private PreparedStatement pstmt = null;
	private Statement st=null;
	private ResultSet rs = null;
	
	public List<Map<String, Object>> getDataName() {
		String dstrSql="SELECT ID,NAME FROM ECORE_DATA_TYPE UNION ALL SELECT ID,NAME FROM ECORE_EXTERNAL_SCHEMA UNION ALL SELECT ID,NAME FROM ECORE_MESSAGE_COMPONENT";
		return DbUtil.get().find(dstrSql);
	}
	
	public EcoreDataType getById(String dataTypeId) {
		EcoreDataType dt = DbUtil.get().findOne(QSQL + "AND t.ID = ? ", EcoreDataType.class, dataTypeId);
		return dt == null? new EcoreDataType() : dt;
	}
	
	public List<EcoreDataType> findAll() {
		return DbUtil.get().find(QSQL + "ORDER BY NAME ", EcoreDataType.class);
	}
	
	public int delById(String id) throws SQLException {
		return DbUtil.get().update("DELETE FROM ECORE_DATA_TYPE WHERE ID=?", id);
	}
	
	public int countByName(String name) {
		String sql = "SELECT count(1) FROM ECORE_DATA_TYPE t WHERE t.NAME = ? ";
		return DbUtil.get().count(sql, name);
	}
	
	public List<Map<String, Object>> findNode() {
		String sql = "SELECT T.ID,T.NAME,T.TYPE,T.TRACE,t.REGISTRATION_STATUS FROM ECORE_DATA_TYPE T ORDER BY T.NAME";
		return DbUtil.get().find(sql);
	}
	
	public Map<String, String> findMdr3() {
		String sql = "select id,name from ecore_data_type";
		List<Map<String, Object>> maps = DbUtil.get().find(sql);
		Map<String, String> map = new HashMap<>();
		for(Map<String, Object> m : maps) {
			map.put(m.get("id") + "", m.get("name") + "");
		}
		return map;
	}
	
	/**
	 * 删除关联数据
	 * @param id
	 * @throws Exception
	 */
	private void deleteDataTypeById(String id) throws Exception{
		EcoreCodeImpl ecoreCodeImpl = new EcoreCodeImpl();
		String objIds = "";
		List<String> ecIds = ecoreCodeImpl.findIdByCodeSetId(id);
		if(ecIds.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (String ecId: ecIds) {
				sb.append("'"  + ecId + "'");
				sb.append(",");
			}
			objIds = sb.substring(0, sb.length() - 1);
		}
		new EcoreConstraintImpl().delByObjId(id, objIds);
		new EcoreDocletImpl().delByObjId(id, objIds);
		new EcoreExampleImpl().delByObjId(id, objIds);
		new EcoreNextVersionsImpl().delById(id, objIds);
		ecoreCodeImpl.delByCodeSetId(id);
	}
	
	/**
	 * 删除数据类型
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteEcoreDataType(String id) throws Exception{
		boolean key=false;
		int count = delById(id);
		if(count > 0) {
			key=true;
		}
		deleteDataTypeById(id);
		return key;
	}
	
	private void deleteCodeNameById(String id, List<String> codeNames) throws SQLException {
		String ids="";
		String names="";
		String sql="SELECT ID FROM ECORE_DATA_TYPE WHERE TRACE = ?";
		List<Map<String, Object>> list = DbUtil.get().find(sql, id);
		for(Map<String, Object> map : list) {
			ids+="'"+map.get("ID")+"',";
		}
		if(codeNames != null && codeNames.size() > 0) {
			for(String codeName : codeNames) {
				names+="'"+codeName+"',";
			}
			names=names.substring(0, names.length()-1);
		}
		if(!ids.equals("")) {
			ids=ids.substring(0, ids.length()-1);
			new EcoreCodeImpl().delByCodeSetIds(ids, names);
		}
	}
	
	private void updateDataType(EcoreDataType ecoreDataType) throws Exception{
		String csql = "SELECT COUNT(1) FROM ECORE_DATA_TYPE T WHERE ID=?";
		Integer count = DbUtil.get().count(csql,ecoreDataType.getId());
		if(count == null || count == 0) {
			List<EcoreDataType> list = new ArrayList<>();
			list.add(ecoreDataType);
			saveDataTypes(list);
		}else {
			String usql = "UPDATE ECORE_DATA_TYPE SET UPDATE_TIME=?,NAME=?,DEFINITION=?,REGISTRATION_STATUS=?,REMOVAL_DATE=?,"
					+ "OBJECT_IDENTIFIER=?,MIN_INCLUSIVE=?,MIN_EXCLUSIVE=?,MAX_INCLUSIVE=?," + 
					"MAX_EXCLUSIVE=?,PATTERN=?,PREVIOUS_VERSION=?,FRACTION_DIGITS=?,TOTAL_DIGITS=?,"
					+ "UNIT_CODE=?,BASE_VALUE=?,BASE_UNIT_CODE=?,MIN_LENGTH=?,MAX_LENGTH=?,LENGTH=?,MEANING_WHEN_TRUE=?,"
					+ "MEANING_WHEN_FALSE=?,IDENTIFICATION_SCHEME=?,VALUE=?,LITERAL=?,TYPE=?,VERSION=?,TRACE=?"
					+ ",PROCESS_CONTENTS=?,NAMESPACE=?,NAMESPACE_LIST=?, UPDATE_USER=?,IS_FROM_ISO20022=? WHERE ID=?";
			DbUtil.get().update(usql,new Timestamp(new java.util.Date().getTime()),ecoreDataType.getName(),ecoreDataType.getDefinition(),ecoreDataType.getRegistrationStatus(),ecoreDataType.getRemovalDate(),
					ecoreDataType.getObjectIdentifier(),ecoreDataType.getMinInclusive(),ecoreDataType.getMinExclusive(),ecoreDataType.getMaxInclusive(),ecoreDataType.getMaxExclusive(),
					ecoreDataType.getPattern(),ecoreDataType.getPreviousVersion(),ecoreDataType.getFractionDigits(),ecoreDataType.getTotalDigits(),ecoreDataType.getUnitCode(),
					ecoreDataType.getBaseValue(),ecoreDataType.getBaseUnitCode(),ecoreDataType.getMinLength(),ecoreDataType.getMaxLength(),ecoreDataType.getLength(),
					ecoreDataType.getMeaningWhenTrue(),ecoreDataType.getMeaningWhenFalse(),ecoreDataType.getIdentificationScheme(),ecoreDataType.getValue(),ecoreDataType.getLiteral(),
					ecoreDataType.getType(),ecoreDataType.getVersion(),ecoreDataType.getTrace(),ecoreDataType.getProcessContents(),ecoreDataType.getNamespace(),
					ecoreDataType.getNamespaceList(),ecoreDataType.getCreateUser(),ecoreDataType.getIsfromiso20022(),ecoreDataType.getId());
		}
	}
	
	/**
	 * 批量保存EcoreDataType数据
	 * @param ecoreDataTypes
	 */
	public void saveDataTypes(List<EcoreDataType> ecoreDataTypes) throws Exception{
		String sql = "INSERT INTO ECORE_DATA_TYPE(ID,NAME,DEFINITION,REGISTRATION_STATUS,REMOVAL_DATE,"
				+ "OBJECT_IDENTIFIER,MIN_INCLUSIVE,MIN_EXCLUSIVE,MAX_INCLUSIVE,"
				+ "MAX_EXCLUSIVE,PATTERN,PREVIOUS_VERSION,FRACTION_DIGITS,TOTAL_DIGITS,"
				+ "UNIT_CODE,BASE_VALUE,BASE_UNIT_CODE,MIN_LENGTH,MAX_LENGTH,LENGTH,MEANING_WHEN_TRUE,"
				+ "MEANING_WHEN_FALSE,IDENTIFICATION_SCHEME,VALUE,LITERAL,TYPE,VERSION,TRACE,PROCESS_CONTENTS,"
				+ "NAMESPACE,NAMESPACE_LIST,CREATE_USER,CREATE_TIME,IS_FROM_ISO20022) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		List<Object[]> values = new ArrayList<Object[] >();
		ecoreDataTypes.forEach(e ->{
			Object[] objs = new Object[34];
			objs[0] = e.getId();
			objs[1] = e.getName();
			objs[2] = e.getDefinition() == null? "": e.getDefinition();
			objs[3] = e.getRegistrationStatus();
			objs[4] = e.getRemovalDate() != null ? e.getRemovalDate() : null;
			objs[5] = e.getObjectIdentifier();
			objs[6] = e.getMinInclusive();
			objs[7] = e.getMinExclusive();
			objs[8] = e.getMaxInclusive();
			objs[9] = e.getMaxExclusive();
			objs[10] = e.getPattern();
			objs[11] = e.getPreviousVersion();
			objs[12] = e.getFractionDigits();
			objs[13] = e.getTotalDigits();
			objs[14] = e.getUnitCode();
			objs[15] = e.getBaseValue();
			objs[16] = e.getBaseUnitCode();
			objs[17] = e.getMinLength();
			objs[18] = e.getMaxLength();
			objs[19] = e.getLength();
			objs[20] = e.getMeaningWhenTrue();
			objs[21] = e.getMeaningWhenFalse();
			objs[22] = e.getIdentificationScheme();
			objs[23] = e.getValue();
			objs[24] = e.getLiteral();
			objs[25] = e.getType();
			objs[26] = e.getVersion();
			objs[27] = e.getTrace();
			objs[28] = e.getProcessContents();
			objs[29] = e.getNamespace();
			objs[30] = e.getNamespaceList();
			objs[31] = e.getCreateUser();
			objs[32] = new Timestamp(new java.util.Date().getTime());
			objs[33] = e.getIsfromiso20022();
			values.add(objs);
		});
		int count = DbUtil.get().batchSave(sql,values);
		logger.info(count + "件数据更新成功");
	}
	
	/**
	 * 获取datatypeNodes
	 * @return List<EcoreTreeNode>
	 */
	public List<EcoreTreeNode> findAllEcoreDataTypes() throws Exception{
		List<EcoreTreeNode> ecoreTreeNodes=new ArrayList<EcoreTreeNode>();
		conn = DerbyUtil.getConnection(); // 获得数据库连接
		String dsql = "SELECT T.ID,T.NAME,T.TYPE,T.TRACE,t.REGISTRATION_STATUS FROM ECORE_DATA_TYPE T ORDER BY T.NAME";
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
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			st=conn.createStatement();
			Map<String,List<EcoreTreeNode>> dataTypeMaps=new HashMap<String,List<EcoreTreeNode>>();
			//获取codeSql
			String cSql = "SELECT T.ID,T.NAME,CODE_SET_ID,t.REGISTRATION_STATUS FROM ECORE_CODE T ORDER BY T.NAME";
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
			Map<String,List<EcoreTreeNode>> typeMaps=new HashMap<String,List<EcoreTreeNode>>();
			//获取dataType
			String dsql = "SELECT T.ID,T.NAME,T.TYPE,T.TRACE,t.REGISTRATION_STATUS FROM ECORE_DATA_TYPE T ORDER BY T.NAME";
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
			List<EcoreTreeNode> types=new ArrayList<EcoreTreeNode>();
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
	public boolean saveEcoreDataTypeVO(EcoreDataTypeVO vo) throws Exception{
		List<EcoreConstraint> ecoreConstraints=new ArrayList<EcoreConstraint>();
		List<EcoreCode> ecoreCodes=new ArrayList<EcoreCode>();
		List<EcoreExample> ecoreExamples=new ArrayList<EcoreExample>();
		List<EcoreNextVersions> ecoreNextVersions=new ArrayList<EcoreNextVersions>();
		Map<String,List<String>> codeNameMaps=new HashMap<String, List<String>>();
		
		EcoreDataType ecoreDataType=vo.getEcoreDataType();
		if(ecoreDataType == null) {
			return false;
		}
		List<EcoreConstraint> constraints=vo.getEcoreConstraints();
		List<EcoreExample> examples=vo.getEcoreExamples();
		List<EcoreNextVersions> nextVersions=vo.getEcoreNextVersions();
		List<EcoreCodeVO> codeVOs=vo.getEcoreCodeVOs();
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
				List<EcoreConstraint> ec=cvo.getEcoreConstraints();
				List<EcoreExample> ee=cvo.getEcoreExamples();
				List<EcoreNextVersions> env=cvo.getEcoreNextVersions();
				if(ec!=null) {
					ecoreConstraints.addAll(ec);
				}
				if(ee!=null) {
					ecoreExamples.addAll(ee);
				}
				if(env!=null) {
					ecoreNextVersions.addAll(env);
				}
				ecoreCodes.add(ecoreCode);
			}
		}
		deleteDataTypeById(ecoreDataType.getId());
		//级联删除trace中的code
		List<String> names=new ArrayList<String>();
		if(ecoreDataType.getTrace()==null) {
			if(codeVOs!=null && !codeVOs.isEmpty()) {
				for(EcoreCodeVO cvo:codeVOs) {
					names.add(cvo.getEcoreCode().getCodeName());
				}
			}
			codeNameMaps.put(ecoreDataType.getId(), names);
		}
		if(codeNameMaps.containsKey(ecoreDataType.getId())) {
			deleteCodeNameById(ecoreDataType.getId(),names);
		}
		updateDataType(ecoreDataType);
		if(ecoreCodes!=null && !ecoreCodes.isEmpty()) {
			new EcoreCodeImpl().saveCode(ecoreCodes);
		}
		if(ecoreExamples!=null && !ecoreExamples.isEmpty()) {
			new EcoreExampleImpl().saveExamples(ecoreExamples);
		}
		if(ecoreConstraints!=null && !ecoreConstraints.isEmpty()) {
			new EcoreConstraintImpl().saveConstraints(ecoreConstraints);
		}
		if(ecoreNextVersions!=null && !ecoreNextVersions.isEmpty()) {
			new EcoreNextVersionsImpl().saveNextVersionses(ecoreNextVersions);
		}
		return true;
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
			dataType.setRegistrationStatus(Constant.PROVISIONALLY_REGISTERED);
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
				ec.setRegistrationStatus(Constant.PROVISIONALLY_REGISTERED);
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
				ec.setObjId(newDataTypeId);
				ec.setObjType(rs.getString("Obj_type"));
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
		saveEcoreDataTypeVO(vo);
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
			me.setRegistrationStatus(Constant.PROVISIONALLY_REGISTERED);
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
				ec.setRegistrationStatus(Constant.PROVISIONALLY_REGISTERED);
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
				ec.setObjId(newCodeId);
				ec.setObjType(rs.getString("Obj_type"));
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
			dataType.setRegistrationStatus(Constant.ADDED_REGISTERED);
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
				ec.setRegistrationStatus(Constant.PROVISIONALLY_REGISTERED);
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
				ec.setObjId(newDataTypeId);
				ec.setObjType(rs.getString("Obj_type"));
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
		saveEcoreDataTypeVO(vo);
		return vo;
	}
}
