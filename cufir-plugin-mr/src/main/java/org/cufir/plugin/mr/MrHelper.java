package org.cufir.plugin.mr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.cufir.plugin.mr.editor.MrImplManager;
import org.cufir.s.data.Constant;
import org.cufir.s.data.bean.EcoreBusinessArea;
import org.cufir.s.data.bean.EcoreBusinessComponent;
import org.cufir.s.data.bean.EcoreBusinessElement;
import org.cufir.s.data.bean.EcoreCode;
import org.cufir.s.data.bean.EcoreConstraint;
import org.cufir.s.data.bean.EcoreDataType;
import org.cufir.s.data.bean.EcoreExample;
import org.cufir.s.data.bean.EcoreExternalSchema;
import org.cufir.s.data.bean.EcoreMessageBuildingBlock;
import org.cufir.s.data.bean.EcoreMessageComponent;
import org.cufir.s.data.bean.EcoreMessageDefinition;
import org.cufir.s.data.bean.EcoreMessageElement;
import org.cufir.s.data.bean.EcoreNextVersions;
import org.cufir.s.data.bean.EcoreSemanticMarkup;
import org.cufir.s.data.bean.EcoreSemanticMarkupElement;
import org.cufir.s.data.vo.EcoreTreeNode;

/**
 * 直接操作数据库
 * @author gongyi_tt
 *
 */
public class MrHelper {
	
	static String url ="";
	
	static {
		url = "jdbc:derby:" + Constant.CONFIG_PATH + "\\db;create=false";
	}	

	/**
	 * 从EcoreDataType集合中获取id对应数据
	 * @param dts
	 * @param id
	 * @return
	 */
	public static EcoreDataType getDataTypeById(List<EcoreDataType> dts, String id) {
		EcoreDataType dt = new EcoreDataType();
		List<EcoreDataType> edts = dts.stream().filter(edt-> id.equals(edt.getId())).collect(Collectors.toList());
		if(edts != null && edts.size() > 0) {
			dt = edts.get(0);
		}
		return dt;
	}
	
	/**
	 * 从EcoreMessageComponent集合中获取id对应数据
	 * @param mcs
	 * @param id
	 * @return
	 */
	public static EcoreMessageComponent getMessageComponentById(List<EcoreMessageComponent> mcs, String id) {
		EcoreMessageComponent mc = new EcoreMessageComponent();
		List<EcoreMessageComponent> emcs = mcs.stream().filter(emc-> id.equals(emc.getId())).collect(Collectors.toList());
		if(emcs != null && emcs.size() > 0) {
			mc = emcs.get(0);
		}
		return mc;
	}
	
	/**
	 * 从EcoreMessageComponent集合中获取trace对应数据
	 * @param mcs
	 * @param trace
	 * @return
	 */
	public static List<EcoreMessageComponent> findMessageComponentByTrace(List<EcoreMessageComponent> mcs, String trace) {
		return mcs.stream().filter(emc->trace.equals(emc.getTrace())).collect(Collectors.toList());
	}
	
	/**
	 * 从EcoreMessageComponentMap集合中获取msgComponentIds对应数据
	 * @param emcMap
	 * @param msgComponentIds
	 * @return
	 */
	public static List<EcoreMessageComponent> findMessageComponentByMsgComponentIds(Map<String, EcoreMessageComponent> emcMap, List<String> msgComponentIds) {
		if(msgComponentIds != null && msgComponentIds.size() > 0) {
			return msgComponentIds.stream().map(id -> emcMap.get(id)).collect(Collectors.toList());
		}else {
			return new ArrayList<EcoreMessageComponent>();
		}
	}
	
	/**
	 * 从EcoreMessageComponent集合中获取id对应数据
	 * @param mcs
	 * @param id
	 * @return
	 */
	public static List<EcoreMessageElement> findMessageElementByMessageComponentId(List<EcoreMessageElement> mes, String messageComponentId) {
		return mes.stream().filter(me -> messageComponentId.equals(me.getMessageComponentId())).collect(Collectors.toList());
	}
	
	/**
	 * 从EcoreConstraint集合中获取objId对应数据集合
	 * @param cs
	 * @param objId
	 * @return
	 */
	public static List<EcoreConstraint> findConstraintsByObjId(List<EcoreConstraint> cs, String objId) {
		return cs.stream().filter(ec->objId.equals(ec.getObj_id())).collect(Collectors.toList());
	}
	
	public static Map<String, List<EcoreConstraint>> getConstraintMapByObjId(List<EcoreConstraint> cs){
		return cs.stream().collect(Collectors.groupingBy(EcoreConstraint::getObj_id));
	}
	
	/**
	 * 从EcoreCode集合中获取codeSetId对应数据集合
	 * @param cs
	 * @param codeSetId
	 * @return
	 */
	public static List<EcoreCode> findCodesByCodeSetId(List<EcoreCode> cs, String codeSetId) {
		return cs.stream().filter(ec->codeSetId.equals(ec.getCodesetid())).collect(Collectors.toList());
	}
	
	/**
	 * 从EcoreExternalSchema集合中获取id对应数据
	 * @param ess
	 * @param id
	 * @return
	 */
	public static EcoreExternalSchema getEcoreExternalSchemaById(List<EcoreExternalSchema> ess, String id) {
		EcoreExternalSchema es = new EcoreExternalSchema();
		List<EcoreExternalSchema> eess = ess.stream().filter(ees->id.equals(ees.getId())).collect(Collectors.toList());
		if(eess != null && eess.size() > 0) {
			es = eess.get(0);
		}
		return es;
	}
	
	public static Map<String, EcoreMessageElement> getMsgElementMapById(List<EcoreMessageElement> mes){
		return mes.stream().collect(Collectors.toMap(EcoreMessageElement::getId, EcoreMessageElement -> EcoreMessageElement));
	}
	
	public static Map<String, List<EcoreMessageElement>> getMsgElementMapByMsgComponentId(List<EcoreMessageElement> mes){
		return mes.stream().collect(Collectors.groupingBy(EcoreMessageElement::getMessageComponentId));
	}
	
	public static Map<String, EcoreMessageComponent> getMessageComponentMapById(List<EcoreMessageComponent> mcs){
		return mcs.stream().collect(Collectors.toMap(EcoreMessageComponent::getId, EcoreMessageComponent -> EcoreMessageComponent));
	}
	
	public static Map<String, EcoreDataType> getDataTypeMapById(List<EcoreDataType> dts){
		return dts.stream().collect(Collectors.toMap(EcoreDataType::getId, EcoreDataType -> EcoreDataType));
	}
	
	public static Map<String, List<EcoreCode>> getCodeMapByCodeSetId(List<EcoreCode> cs){
		return cs.stream().collect(Collectors.groupingBy(EcoreCode::getCodesetid));
	}
	
	public static Map<String, EcoreExternalSchema> getExternalSchemaMapById(List<EcoreExternalSchema> ess){
		return ess.stream().collect(Collectors.toMap(EcoreExternalSchema::getId, EcoreExternalSchema -> EcoreExternalSchema));
	}
	
	public static Map<String, List<EcoreExample>> getExampleMapByObjId(List<EcoreExample> es){
		return es.stream().collect(Collectors.groupingBy(EcoreExample::getObjId));
	}
	
	public static Map<String, EcoreBusinessComponent> getBusinessComponentMapById(List<EcoreBusinessComponent> ebc){
		return ebc.stream().collect(Collectors.toMap(EcoreBusinessComponent::getId, EcoreBusinessComponent -> EcoreBusinessComponent));
	}
	
	public static Map<String, List<EcoreBusinessElement>> getBusinessElementMapByBusinessComponentId(List<EcoreBusinessElement> ebe){
		return ebe.stream().collect(Collectors.groupingBy(EcoreBusinessElement::getBusinessComponentId));
	}
	
	public static Map<String, EcoreNextVersions> getNextVersionsMapByNextVersionId(List<EcoreNextVersions> envs){
		return envs.stream().collect(Collectors.toMap(EcoreNextVersions::getNextVersionId, EcoreNextVersions -> EcoreNextVersions));
	}
	
	public static Map<String, List<EcoreNextVersions>> getNextVersionsMapById(List<EcoreNextVersions> envs){
		Map<String, List<EcoreNextVersions>> map = new HashMap<>();
		envs.forEach(env ->{
			List<EcoreNextVersions> list = map.get(env.getId());
			if(list != null && list.size() > 0) {
				list.add(env);
				map.put(env.getId(), list);
			}else {
				list = new ArrayList<>();
				list.add(env);
				map.put(env.getId(), list);
			}
		});
		return map;
	}
	
	public static Map<String, EcoreMessageDefinition> getMessageDefinitionMapById(List<EcoreMessageDefinition> emds){
		return emds.stream().collect(Collectors.toMap(EcoreMessageDefinition::getId, EcoreMessageDefinition -> EcoreMessageDefinition));
	}
	
	/**
	 * 从EcoreMessageDefinition集合中获取id对应数据
	 * @param mds
	 * @param id
	 * @return
	 */
	public static EcoreMessageDefinition getMsgDefinitionById(List<EcoreMessageDefinition> mds, String id) {
		EcoreMessageDefinition md = new EcoreMessageDefinition();
		List<EcoreMessageDefinition> emds = mds.stream().filter(emd->id.equals(emd.getId())).collect(Collectors.toList());
		if(emds != null && emds.size() > 0) {
			md = emds.get(0);
		}
		return md;
	}
	
	/**
	 * 从EcoreSemanticMarkup集合中获取objId对应数据
	 * @param sms
	 * @param smes
	 * @param objId
	 * @return
	 */
	public static List<EcoreSemanticMarkupElement> findSemanticMarkupElementBySemanticMarkupObjId(List<EcoreSemanticMarkup> sms, List<EcoreSemanticMarkupElement> smes, String objId) {
		List<EcoreSemanticMarkupElement> list = new ArrayList<EcoreSemanticMarkupElement>();
		List<EcoreSemanticMarkup> esms = sms.stream().filter(sm->objId.equals(sm.getObjId())).collect(Collectors.toList());
		esms.forEach(sm ->{
			List<EcoreSemanticMarkupElement> ecoreSemanticMarkupElement = smes.stream().filter(sme -> sme.getSemanticMarkupId().equals(sm.getId())).collect(Collectors.toList());
			list.addAll(ecoreSemanticMarkupElement);
		});
		return list;
	}
	
	public static List<EcoreMessageBuildingBlock> findMsgBuildingBlocksByMsgId(List<EcoreMessageBuildingBlock> mbbs, String msgId){
		return mbbs.stream().filter(mbb->msgId.equals(mbb.getMessageId())).collect(Collectors.toList());
	}
	
	public static List<EcoreBusinessComponent> getBusinessComponentListByIds(List<String> bizComponentIds) {
		
		ArrayList<EcoreBusinessComponent> businessComponentList = new ArrayList<EcoreBusinessComponent>();
		if (bizComponentIds.size() == 0) {
			return businessComponentList;
		}
		StringBuilder sb = new StringBuilder();
		for (String id: bizComponentIds) {
			sb.append("'"  + id + "'");
			sb.append(",");
		}
		String parametersString = sb.substring(0, sb.length() - 1);
		
		String querySql = " select * from ecore_business_component t where t.id in ( " + parametersString + " ) ";
		
		Connection conn= null; 
		Statement stmt=null;
		ResultSet rs=null;
		try {
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(querySql);
			while(rs.next()) {
				EcoreBusinessComponent bizComponnet = new EcoreBusinessComponent();
				bizComponnet.setId(rs.getString(""));
				bizComponnet.setName(rs.getString(""));
				businessComponentList.add(bizComponnet);
			}
			return businessComponentList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null){
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				throw new RuntimeException(e2);
			}
		}
		
	}
	
	
	public static ArrayList<EcoreMessageDefinition> getmsgDefinitionListByMsgComponentId(String msgComponentId) {
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ECORE_MESSAGE_DEFINITION t2 where t2.id in ( select t1.message_id from ECORE_MESSAGE_BUILDING_BLOCK t1 where t1.data_type_id = ? ) order by t2.name ";
		
		ArrayList<EcoreMessageDefinition> msgDefintionList = new ArrayList<>();
		
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, msgComponentId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				EcoreMessageDefinition msgDefinition = new EcoreMessageDefinition();
				msgDefinition.setId(rs.getString("ID"));
				msgDefinition.setName(rs.getString("NAME"));
				msgDefinition.setBusinessArea(rs.getString("BUSINESS_AREA"));
				msgDefinition.setMessageFunctionality(rs.getString("MESSAGE_FUNCTIONALITY"));
				msgDefinition.setFlavour(rs.getString("FLAVOUR"));
				msgDefinition.setVersion(rs.getString("VERSION"));
				msgDefintionList.add(msgDefinition);
			}
			return msgDefintionList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				throw new RuntimeException(e2);
			}
		}
	}
	
	public static List<EcoreMessageElement> getMsgElementList(String msgComponentId) {
		Map<String,String> idMap=new HashMap<String,String>();
		List<Map<String, Object>> dataName = MrImplManager.get().getEcoreDataTypeImpl().getDataName();
		dataName.forEach(m -> {
			String id = m.get("id") + "";
			String name = m.get("name") + "";
			idMap.put(id, name);
		});
		List<EcoreMessageElement> msgElements = MrImplManager.get().getEcoreMessageElementImpl().findByMsgComponentId(msgComponentId);
		msgElements.forEach(me -> {
			me.setTypeName(idMap.get(me.getTypeId()));
		});
		return msgElements;
	}
	
	public static List<EcoreMessageElement> getMsgElementList() {
		Map<String,String> idMap=new HashMap<String,String>();
		List<Map<String, Object>> dataName = MrImplManager.get().getEcoreDataTypeImpl().getDataName();
		dataName.forEach(m -> {
			String id = m.get("id") + "";
			String name = m.get("name") + "";
			idMap.put(id, name);
		});
		List<EcoreMessageElement> msgElements = MrImplManager.get().getEcoreMessageElementImpl().findAll();
		msgElements.forEach(me -> {
			me.setTypeName(idMap.get(me.getTypeId()));
		});
		return msgElements;
	}
	
	public static ArrayList<EcoreBusinessElement> getBizElementsByBizComponentId(String bizComponentId) {
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ECORE_BUSINESS_ELEMENT t where t.business_component_id = ? order by name ";
		ArrayList<EcoreBusinessElement> bizBusinessElementList = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, bizComponentId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				EcoreBusinessElement bizElement = new EcoreBusinessElement();
				bizElement.setId(rs.getString("ID"));
				bizElement.setName(rs.getString("NAME"));
				bizElement.setDefinition(rs.getString("DEFINITION"));
				bizElement.setIsDerived(rs.getString("IS_DERIVED"));
				bizElement.setMinOccurs(rs.getInt("MIN_OCCURS"));
				bizElement.setMaxOccurs(rs.getInt("MAX_OCCURS"));
				bizElement.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
				bizElement.setRegistrationStatus(rs.getString("REGISTRATION_STATUS"));
				bizElement.setRemovalDate(rs.getDate("REMOVAL_DATE"));
				bizElement.setTypeId(rs.getString("TYPE_ID"));
				bizElement.setType(rs.getString("TYPE"));
				bizElement.setBusinessComponentId(rs.getString("BUSINESS_COMPONENT_ID"));
				bizBusinessElementList.add(bizElement);
			}
			return bizBusinessElementList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				throw new RuntimeException(e2);
			}
		}
	}
	
public static Map<String,ArrayList<EcoreBusinessElement>> getAllBeByComponentId() {
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ECORE_BUSINESS_ELEMENT order by name";
		Map<String, ArrayList<EcoreBusinessElement>> Data=new HashMap<String,ArrayList<EcoreBusinessElement>>();
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
//			pstmt.setString(1, bizComponentId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				EcoreBusinessElement bizElement = new EcoreBusinessElement();
				bizElement.setId(rs.getString("ID"));
				bizElement.setName(rs.getString("NAME"));
				bizElement.setMinOccurs(rs.getInt("MIN_OCCURS"));
				bizElement.setMaxOccurs(rs.getInt("MAX_OCCURS"));
				bizElement.setTypeId(rs.getString("TYPE_ID"));
				bizElement.setType(rs.getString("TYPE"));
				bizElement.setBusinessComponentId(rs.getString("BUSINESS_COMPONENT_ID"));
				String id=bizElement.getBusinessComponentId();
				ArrayList<EcoreBusinessElement>list=new ArrayList<EcoreBusinessElement>();
				list.add(bizElement);
				if (Data.containsKey(id)) {
					ArrayList<EcoreBusinessElement>list_1=new ArrayList<EcoreBusinessElement>();
					list_1.add(bizElement);
					Data.get(id).addAll(list_1);
				}else {
					Data.put(id, list);
				}
				
			}
			return Data;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				throw new RuntimeException(e2);
			}
		}
	}
	
	public static Map<String,ArrayList<EcoreTreeNode>> getAllSubBusiness() {
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select tr.p_id,tr.id from ECORE_BUSINESS_COMPONENT_RL tr";
		Map<String,ArrayList<EcoreTreeNode>> ecoreTreeNodeList = new HashMap<String,ArrayList<EcoreTreeNode>>();
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql.toString());
//			pstmt.setString(1, bizComponentId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				EcoreTreeNode ecoreTreeNode = new EcoreTreeNode();
				ecoreTreeNode.setPid(rs.getString("P_ID"));
				ecoreTreeNode.setId(rs.getString("ID"));
				String id=ecoreTreeNode.getPid();
				ArrayList<EcoreTreeNode>list=new ArrayList<EcoreTreeNode>();
				list.add(ecoreTreeNode);
				if (ecoreTreeNodeList.containsKey(id)) {
					ArrayList<EcoreTreeNode>list_1=new ArrayList<EcoreTreeNode>();
					list_1.add(ecoreTreeNode);
					ecoreTreeNodeList.get(id).addAll(list_1);
				}else {
					ecoreTreeNodeList.put(id, list);
				}
			}
			return ecoreTreeNodeList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				throw new RuntimeException(e2);
			}
		}
	}
	
	public static EcoreBusinessComponent getBusinessComponentById(String bizComponentId) {
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ECORE_BUSINESS_COMPONENT t where t.id = ? ";
		
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, bizComponentId);
			rs = pstmt.executeQuery();
			if (rs.getFetchSize() == 1) {
				EcoreBusinessComponent bizComponent = new EcoreBusinessComponent();
				while (rs.next()) {
					bizComponent.setId(rs.getString("ID"));
					bizComponent.setName(rs.getString("NAME"));
					bizComponent.setDefinition(rs.getString("DEFINITION"));
					bizComponent.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
					bizComponent.setRegistrationStatus(rs.getString("REGISTRATION_STATUS"));
					bizComponent.setRemovalDate(rs.getDate("REMOVAL_DATE"));
				}
				return bizComponent;
			} else {
				throw new RuntimeException("Duplicate Business Component");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				throw new RuntimeException(e2);
			}
		}
	}
	
	public static EcoreBusinessArea getBusinessAreaByDefinitionId(String definitionId) {
		
		EcoreBusinessArea businessArea = new EcoreBusinessArea();
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ecore_business_area t where t.id in ( select t2.business_area_id from ecore_message_definition t2 where t2.id = ? ) ";
		
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, definitionId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				businessArea.setId(rs.getString("ID"));
				businessArea.setName(rs.getString("NAME"));
				businessArea.setCode(rs.getString("CODE"));
			}
			return businessArea;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				throw new RuntimeException(e2);
			}
		}
		
	}
	
	public static ArrayList<EcoreMessageDefinition> getMsgDefintionListByBizAreaId(String bizAreaId) {
		ArrayList<EcoreMessageDefinition> msgDefinitionList = new ArrayList<>();
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ecore_message_definition t where t.business_area_id = ? ";
		
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, bizAreaId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				EcoreMessageDefinition msgDefinition = new EcoreMessageDefinition();
				msgDefinition.setId(rs.getString("ID"));
				msgDefinition.setName(rs.getString("NAME"));
				msgDefinition.setBusinessArea(rs.getString("BUSINESS_AREA"));
				msgDefinition.setMessageFunctionality(rs.getString("MESSAGE_FUNCTIONALITY"));
				msgDefinition.setFlavour(rs.getString("FLAVOUR"));
				msgDefinition.setVersion(rs.getString("VERSION"));
				msgDefinitionList.add(msgDefinition);
			}
			return msgDefinitionList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				throw new RuntimeException(e2);
			}
		}
	}
	
	public static List<EcoreMessageDefinition> getMsgDefinitionListByIds(List<String> msgDefinitionIds) {
		List<EcoreMessageDefinition> msgDefinitions = new ArrayList<>();
		if (msgDefinitionIds.size() == 0) {
			return msgDefinitions;
		}
		StringBuilder sb = new StringBuilder();
		for (String id: msgDefinitionIds) {
			sb.append("'"  + id + "'");
			sb.append(",");
		}
		String parametersString = sb.substring(0, sb.length() - 1);
		Connection conn= null; 
		Statement stmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ecore_message_definition t where t.id in ( " + parametersString + " ) ";
		
		
		try {
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(querySql);
			while(rs.next()) {
				EcoreMessageDefinition msgDefinition = new EcoreMessageDefinition();
				msgDefinition.setId(rs.getString("ID"));
				msgDefinition.setDefinition(rs.getString("DEFINITION"));
				msgDefinition.setName(rs.getString("NAME"));
				msgDefinition.setRegistrationStatus(rs.getString("REGISTRATION_STATUS"));
				msgDefinition.setRemovalDate(rs.getDate("REMOVAL_DATE"));
				msgDefinition.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
				msgDefinition.setPreviousVersion(rs.getString("PREVIOUS_VERSION"));
				msgDefinition.setVersion(rs.getString("VERSION"));
				msgDefinition.setBusinessArea(rs.getString("BUSINESS_AREA_ID"));
				msgDefinition.setRootElement(rs.getString("ROOT_ELEMENT"));
				msgDefinition.setFlavour(rs.getString("FLAVOUR"));
				msgDefinition.setMessageFunctionality(rs.getString("MESSAGE_FUNCTIONALITY"));
				msgDefinition.setXmlTag(rs.getString("XML_TAG"));
				msgDefinition.setXmlName(rs.getString("XML_NAME"));
				msgDefinition.setBusinessArea(rs.getString("BUSINESS_AREA"));
				msgDefinition.setIsfromiso20022(rs.getString("IS_FROM_ISO20022"));

				msgDefinitions.add(msgDefinition);
			}
			return msgDefinitions;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null){
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				throw new RuntimeException(e2);
			}
		}
	}
	
	public static EcoreBusinessArea getBusinessAreaByBizAreaId(String bizAreaId) {
		EcoreBusinessArea bizArea = new EcoreBusinessArea();
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ecore_business_area t where t.id = ? ";
		
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, bizAreaId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				bizArea.setId(rs.getString("ID"));
				bizArea.setName(rs.getString("NAME"));
				bizArea.setDefinition(rs.getString("DEFINITION"));
				bizArea.setRegistrationStatus(rs.getString("REGISTRATION_STATUS"));
				bizArea.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
				bizArea.setCode(rs.getString("CODE"));
			}
			return bizArea;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				throw new RuntimeException(e2);
			}
		}
		
	}
	
	public static ArrayList<EcoreMessageDefinition> getmessageDefinitionListByBizAreaId(String bizAreaId) {
		ArrayList<EcoreMessageDefinition> msgDefinitionList = new ArrayList<>();
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ecore_message_definition t where t.business_area_id = ? order by name ";
		
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, bizAreaId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				EcoreMessageDefinition msgDefinition = new EcoreMessageDefinition();
				msgDefinition.setId(rs.getString("ID"));
				msgDefinition.setName(rs.getString("NAME"));
				msgDefinition.setBusinessArea(rs.getString("BUSINESS_AREA"));
				msgDefinition.setMessageFunctionality(rs.getString("MESSAGE_FUNCTIONALITY"));
				msgDefinition.setFlavour(rs.getString("FLAVOUR"));
				msgDefinition.setVersion(rs.getString("VERSION"));
				msgDefinition.setRegistrationStatus(rs.getString("REGISTRATION_STATUS"));
				msgDefinitionList.add(msgDefinition);
			}
			return msgDefinitionList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				throw new RuntimeException(e2);
			}
		}
	}
	
	//========================= DataType Use Begin ================================================
	
	public static ArrayList<EcoreExample> getExample(String objId) {
		ArrayList<EcoreExample> coreExampleList = new ArrayList<>();
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ecore_example t where t.obj_id = ? ";
		
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, objId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				EcoreExample ecoreExample = new EcoreExample();
				ecoreExample.setId(rs.getString("ID"));
				ecoreExample.setExample(rs.getString("EXAMPLE"));
				coreExampleList.add(ecoreExample);
			}
			return coreExampleList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				throw new RuntimeException(e2);
			}
		}
	}
	
	public static ArrayList<EcoreMessageComponent> getMessageComponentByDataTypeId(String dataTypeId) {
		
		ArrayList<EcoreMessageComponent> messageComponentList = new ArrayList<EcoreMessageComponent>();
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ecore_message_component t2 where t2.id in ( " + 
				" select t1.message_component_id from ecore_message_element t1 where t1.type_id = ? ) ";
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, dataTypeId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				EcoreMessageComponent messageComponent = new EcoreMessageComponent();
				messageComponent.setId(rs.getString("ID"));
				messageComponent.setName(rs.getString("NAME"));
				messageComponentList.add(messageComponent);
			}
			return messageComponentList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				throw new RuntimeException(e2);
			}
		} 
	}
	
	public static ArrayList<EcoreMessageComponent> getMessageComponentListByExternalSchemaId(String externalSchemaId) {
		
		ArrayList<EcoreMessageComponent> messageComponentList = new ArrayList<EcoreMessageComponent>();
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ecore_message_component t3 where t3.id in ( " + 
				" select t2.message_component_id from ecore_message_element t2  where t2.type_id = ? ) ";
		
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, externalSchemaId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				EcoreMessageComponent messageComponent = new EcoreMessageComponent();
				messageComponent.setId(rs.getString("ID"));
				messageComponent.setName(rs.getString("NAME"));
				messageComponentList.add(messageComponent);
			}
			return messageComponentList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				throw new RuntimeException(e2);
			}
		}
	}
	
	public static ArrayList<EcoreBusinessComponent> getIncomingAssociationsListForDataType(String dataTypeId) {
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * " + 
				"  from ecore_business_component t1 " + 
				" where t1.id in " + 
				"       (select t2.business_component_id " + 
				"          from ecore_business_element t2 " + 
				"         where t2.type_id = ? ) order by t1.name ";
		ArrayList<EcoreBusinessComponent> bizComponentList = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, dataTypeId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				EcoreBusinessComponent bizComponent = new EcoreBusinessComponent();
				bizComponent.setId(rs.getString("ID"));
				bizComponent.setName(rs.getString("NAME"));
				bizComponentList.add(bizComponent);
			}
			return bizComponentList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				throw new RuntimeException(e2);
			}
		}
	}
	
	public static List<EcoreDataType> getDataTypeListByIds(List<String> dataTypeIdList) {
		ArrayList<EcoreDataType> dataTypeList = new ArrayList<>();
		
		if (dataTypeIdList.size() == 0) {
			return dataTypeList;
		}
		
		StringBuilder sb = new StringBuilder();
		for (String id: dataTypeIdList) {
			sb.append("'"  + id + "'");
			sb.append(",");
		}
		String parametersString = sb.substring(0, sb.length() - 1);
		
		Connection conn= null; 
		Statement stmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ecore_data_type t where t.id  in ( " + parametersString + " ) ";
		
		try {
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(querySql);
			while(rs.next()) {
				EcoreDataType ecoreDataType = new EcoreDataType();
				ecoreDataType.setId(rs.getString("ID"));
				ecoreDataType.setName(rs.getString("NAME"));
				dataTypeList.add(ecoreDataType);
			}
			return dataTypeList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null){
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				throw new RuntimeException(e2);
			}
		}
	}

	public static EcoreExternalSchema getPreviousVersionExternalSchema(String ecoreExternalSchemaId) {
		
		EcoreExternalSchema ecoreExternalSchema = new EcoreExternalSchema();
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ECORE_EXTERNAL_SCHEMA t2 where t2.previous_version in ( select t1.previous_version from ECORE_EXTERNAL_SCHEMA t1 where t1.id = ? ) ";
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, ecoreExternalSchemaId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ecoreExternalSchema.setId(rs.getString("ID"));
				ecoreExternalSchema.setName(rs.getString("NAME"));
				ecoreExternalSchema.setDefinition(rs.getString("DEFINITION"));
				ecoreExternalSchema.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
				ecoreExternalSchema.setProcessContent(rs.getString("PROCESS_CONTENT"));
				ecoreExternalSchema.setTechnical(rs.getString("REGISTRATION_STATUS"));
				ecoreExternalSchema.setRegistrationStatus(rs.getString("TECHNICAL"));
			}
			return ecoreExternalSchema;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				throw new RuntimeException(e2);
			}
		}
	}
	
	public static List<EcoreExternalSchema> getEcoreExternalSchemaListByIds(List<String> ecoreExternalSchemaIds) {
		ArrayList<EcoreExternalSchema> ecoreExternalSchemaList = new ArrayList<>();
		if (ecoreExternalSchemaIds.size() == 0) {
			return ecoreExternalSchemaList;
		}
		
		StringBuilder sb = new StringBuilder();
		for (String id: ecoreExternalSchemaIds) {
			sb.append("'");
			sb.append(id);
			sb.append("'");
			sb.append(",");
		}
		String idParameters = sb.substring(0, sb.length() - 1);
		
		String querySql = " select * from ECORE_EXTERNAL_SCHEMA t where t.id in ( " + idParameters +" ) ";
		
		EcoreExternalSchema ecoreExternalSchema = new EcoreExternalSchema();
		
		Connection conn= null; 
		Statement stmt=null;
		ResultSet rs=null;
		
		try {
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(querySql);
			while(rs.next()) {
				ecoreExternalSchema.setId(rs.getString("ID"));
				ecoreExternalSchema.setName(rs.getString("NAME"));
				ecoreExternalSchemaList.add(ecoreExternalSchema);
			}
			return ecoreExternalSchemaList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null){
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				throw new RuntimeException(e2);
			}
		}
	}

	public static EcoreBusinessComponent getPreviousVersionForBizComponent(String BizComponentId) {
		
		EcoreBusinessComponent businessComponent = new EcoreBusinessComponent();
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ecore_business_Component t2 where t2.previous_version in ( select t1.previous_version from ecore_business_Component t1 where t1.id = ? ) ";
		
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, querySql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				businessComponent.setId(rs.getString("ID"));
				businessComponent.setName(rs.getString("NAME"));
			}
			return businessComponent;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				throw new RuntimeException(e2);
			}
		}
	}
}
