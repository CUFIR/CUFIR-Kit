/**
 * 
 */
package com.cfets.cufir.plugin.mr.utils;

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

import org.eclipse.core.runtime.Platform;

import com.cfets.cufir.s.data.bean.EcoreBusinessArea;
import com.cfets.cufir.s.data.bean.EcoreBusinessComponent;
import com.cfets.cufir.s.data.bean.EcoreBusinessElement;
import com.cfets.cufir.s.data.bean.EcoreCode;
import com.cfets.cufir.s.data.bean.EcoreConstraint;
import com.cfets.cufir.s.data.bean.EcoreDataType;
import com.cfets.cufir.s.data.bean.EcoreExample;
import com.cfets.cufir.s.data.bean.EcoreExternalSchema;
import com.cfets.cufir.s.data.bean.EcoreMessageBuildingBlock;
import com.cfets.cufir.s.data.bean.EcoreMessageComponent;
import com.cfets.cufir.s.data.bean.EcoreMessageDefinition;
import com.cfets.cufir.s.data.bean.EcoreMessageElement;
import com.cfets.cufir.s.data.bean.EcoreMessageSet;
import com.cfets.cufir.s.data.bean.EcoreNamespaceList;
import com.cfets.cufir.s.data.bean.EcoreSemanticMarkupElement;
import com.cfets.cufir.s.data.vo.EcoreTreeNode;

/**
 * 直接操作数据库
 * @author gongyi_tt
 *
 */
public class DerbyDaoUtil {
	
	static String url ="jdbc:derby:D:\\cide-0.2.3\\cfets\\db;create=false";
	
	static {
		url = "jdbc:derby:"
				+ Platform.getInstallLocation().getURL().getPath().replaceFirst("/", "").replace("/", "//")
				+ "cfets\\db;create=false";
	}	

	public static EcoreMessageSet getMessageSetById(String msgSetId) {
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ecore_message_set t where t.id = ? ";
		EcoreMessageSet messageSet = new EcoreMessageSet();
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, msgSetId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				messageSet.setId(rs.getString("ID"));
				messageSet.setName(rs.getString("NAME"));
				messageSet.setDefinition(rs.getString("DEFINITION"));
				messageSet.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
				messageSet.setRegistrationStatus(rs.getString("REGISTRATION_STATUS"));
				messageSet.setRemovalDate(rs.getDate("REMOVAL_DATE"));
			}
			return messageSet;
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
	
	public static ArrayList<EcoreMessageDefinition> getMsgDefinitionByMsgSetId(String msgSetId) {
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ECORE_MESSAGE_DEFINITION t2 where t2.id in ( " + 
				"select t1.message_id from ECORE_MESSAGE_SET_DEFINTION_RL t1 where t1.set_id = ? ) ";
		ArrayList<EcoreMessageDefinition> msgDefinitionList = new ArrayList<>();
		
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, msgSetId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				EcoreMessageDefinition msgDefiniton = new EcoreMessageDefinition();
				msgDefiniton.setId(rs.getString("ID"));
				msgDefiniton.setName(rs.getString("NAME"));
				msgDefiniton.setRegistrationStatus(rs.getString("REGISTRATION_STATUS"));
				msgDefinitionList.add(msgDefiniton);
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
	
	public static ArrayList<EcoreMessageDefinition> getAllMsgDefinition() {
		ArrayList<EcoreMessageDefinition> msgDefinitionList = new ArrayList<EcoreMessageDefinition>();
		Connection conn= null; 
		Statement stmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ecore_message_definition order by name ";
		
		try {
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(querySql);
			while(rs.next()) {
				EcoreMessageDefinition msgDefi = new EcoreMessageDefinition();
				msgDefi.setId(rs.getString("ID"));
				msgDefi.setName(rs.getString("NAME"));
				msgDefi.setRegistrationStatus(rs.getString("REGISTRATION_STATUS"));
				msgDefinitionList.add(msgDefi);
			}
			return msgDefinitionList;
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
	
	public static ArrayList<EcoreMessageDefinition> getMsgDefinitionWithoutBA() {
		
		ArrayList<EcoreMessageDefinition> msgDefinitionList = new ArrayList<EcoreMessageDefinition>();
		Connection conn= null; 
		Statement stmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ecore_message_definition t where t.business_area_id is null order by name ";
		
		try {
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(querySql);
			while(rs.next()) {
				EcoreMessageDefinition msgDefi = new EcoreMessageDefinition();
				msgDefi.setId(rs.getString("ID"));
				msgDefi.setName(rs.getString("NAME"));
				msgDefinitionList.add(msgDefi);
			}
			return msgDefinitionList;
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
	
	public static ArrayList<EcoreConstraint> getContraints(String objId) {
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ECORE_CONSTRAINT t where t.OBJ_ID = ? ";
		ArrayList<EcoreConstraint> contraintsList = new ArrayList<>();
		
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, objId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				EcoreConstraint constraint = new EcoreConstraint();
				constraint.setId(rs.getString("ID"));
				constraint.setName(rs.getString("NAME"));
				constraint.setDefinition(rs.getString("DEFINITION"));
				constraint.setExpression(rs.getString("EXPRESSION"));
				constraint.setExpressionlanguage(rs.getString("EXPRESSION_LANGUAGE"));
				constraint.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
				constraint.setObj_id(rs.getString("OBJ_ID"));
				constraint.setObj_type(rs.getString("OBJ_TYPE"));
				contraintsList.add(constraint);
			}
			return contraintsList;
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
	
	public static EcoreConstraint getConstraintById(String id) {
		
		EcoreConstraint ecoreConstraint = new EcoreConstraint();
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ecore_constraint t where t.id = ? ";
		
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ecoreConstraint.setId(rs.getString("ID"));
				ecoreConstraint.setName(rs.getString("NAME"));
				ecoreConstraint.setDefinition(rs.getString("DEFINITION"));
				ecoreConstraint.setExpression(rs.getString("EXPRESSION"));
				ecoreConstraint.setExpressionlanguage(rs.getString("EXPRESSION_LANGUAGE"));
				ecoreConstraint.setRegistrationStatus(rs.getString("REGISTRATION_STATUS"));
				ecoreConstraint.setRemovalDate(rs.getDate("REMOVAL_DATE"));
				ecoreConstraint.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
			}
			return ecoreConstraint;
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
	
	public static ArrayList<EcoreMessageBuildingBlock> getMsgBuildingBlock(String msgDefinitionId) {
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ECORE_MESSAGE_BUILDING_BLOCK t where t.MESSAGE_ID = ? order by name ";
		ArrayList<EcoreMessageBuildingBlock> msgBuildBlockList = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, msgDefinitionId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				EcoreMessageBuildingBlock msgBldBlock = new EcoreMessageBuildingBlock();
				msgBldBlock.setId(rs.getString("ID"));
				msgBldBlock.setName(rs.getString("NAME"));
				msgBldBlock.setDefinition(rs.getString("DEFINITION"));
				msgBldBlock.setRegistrationStatus(rs.getString("REGISTRATION_STATUS"));
				msgBldBlock.setRemovalDate(rs.getDate("REMOVAL_DATE"));
				msgBldBlock.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
				msgBldBlock.setPreviousVersion(rs.getString("PREVIOUS_VERSION"));
				msgBldBlock.setVersion(rs.getString("VERSION"));
				msgBldBlock.setDataType(rs.getString("DATA_TYPE"));
				msgBldBlock.setDataTypeId(rs.getString("DATA_TYPE_ID"));
				msgBldBlock.setXmlTag(rs.getString("XML_TAG"));
				msgBldBlock.setMinOccurs(rs.getInt("MIN_OCCURS"));
				msgBldBlock.setMaxOccurs(rs.getInt("MAX_OCCURS"));
				msgBldBlock.setMessageId(rs.getString("MESSAGE_ID"));
				msgBldBlock.setIsfromiso20022(rs.getString("IS_FROM_ISO20022"));
				msgBuildBlockList.add(msgBldBlock);
			}
			return msgBuildBlockList;
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
	
	public static EcoreMessageBuildingBlock getMessageBuildingBlockById(String bbid) {
		
		EcoreMessageBuildingBlock msgBB = new EcoreMessageBuildingBlock();
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ECORE_MESSAGE_BUILDING_BLOCK t where t.id = ? ";
		
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, bbid);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				msgBB.setId(rs.getString("ID"));
				msgBB.setName(rs.getString("NAME"));
				msgBB.setDefinition(rs.getString("DEFINITION"));
				msgBB.setMinOccurs(rs.getInt("MIN_OCCURS"));
				msgBB.setMaxOccurs(rs.getInt("MAX_OCCURS"));
				msgBB.setXmlTag(rs.getString("XML_TAG"));
				msgBB.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
			}
			return msgBB;
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
	
	public static ArrayList<EcoreBusinessComponent> getIncomingAssociationsList(String bizComponentId) {
		
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
			pstmt.setString(1, bizComponentId);
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
	
	public static ArrayList<EcoreMessageSet> getMessageSetList(ArrayList<EcoreMessageDefinition> msgDefinitionList) {
		
		if (msgDefinitionList.size() == 0) {
			return new ArrayList<EcoreMessageSet>();
		}
		StringBuilder sb = new StringBuilder();
		for (EcoreMessageDefinition msgDefinition: msgDefinitionList) {
			sb.append("'");
			sb.append(msgDefinition.getId());
			sb.append("'");
			sb.append(",");
		}
		return DerbyDaoUtil.getMsgSetListByMsgDefinitionIds(sb.substring(0, sb.length() - 1));
	}
	
	public static ArrayList<EcoreMessageSet> getMsgSetListByMsgDefinitionIds(String msgDefinitionIds) {

		
		Connection conn= null; 
		Statement stmt=null;
		ResultSet rs=null;
		
		String querySql = " select distinct t2.* from ECORE_MESSAGE_SET t2 where t2.id in (select t1.set_id from ECORE_MESSAGE_SET_DEFINTION_RL t1 where t1.message_id in ( " + msgDefinitionIds + " ) ) order by t2.name ";

		ArrayList<EcoreMessageSet> msgSetList = new ArrayList<>();
		
		try {
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(querySql);
			while(rs.next()) {
				EcoreMessageSet msgSet = new EcoreMessageSet();
				msgSet.setId(rs.getString("ID"));
				msgSet.setName(rs.getString("Name"));
				msgSetList.add(msgSet);
			}
			return msgSetList;
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
	
	public static ArrayList<EcoreMessageSet> getMsgSetListByMsgDefinitionId(String msgDefinitionId) {
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select t2.* from ECORE_MESSAGE_SET t2 where t2.id in (select t1.set_id from ECORE_MESSAGE_SET_DEFINTION_RL t1 where t1.message_id = ?) order by t2.name ";

		ArrayList<EcoreMessageSet> msgSetList = new ArrayList<>();
		
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, msgDefinitionId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				EcoreMessageSet msgSet = new EcoreMessageSet();
				msgSet.setId(rs.getString("ID"));
				msgSet.setName(rs.getString("Name"));
				msgSetList.add(msgSet);
			}
			return msgSetList;
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
	
	public static ArrayList<EcoreMessageDefinition> getMessageDefinitionList(ArrayList<EcoreMessageComponent> messageComponentList) {
		
		ArrayList<EcoreMessageDefinition> ecoreMessageDefinitionList = new ArrayList<EcoreMessageDefinition>();
		
		if (messageComponentList.size() == 0) {
			return ecoreMessageDefinitionList;
		}
		
		StringBuilder sb = new StringBuilder();
		for (EcoreMessageComponent msgComponent: messageComponentList) {
			sb.append("'");
			sb.append(msgComponent.getId());
			sb.append("'");
			sb.append(",");
		}
		return DerbyDaoUtil.getmsgDefinitionListByMsgComponentIds(sb.substring(0, sb.length() - 1));
	}
	
	
	public static ArrayList<EcoreMessageDefinition> getmsgDefinitionListByMsgComponentIds(String msgComponentIds) {

		
		Connection conn= null; 
		Statement stmt=null;
		ResultSet rs=null;
		
		String querySql = " select distinct * " + 
				"  from ECORE_MESSAGE_DEFINITION t2 " + 
				" where t2.id in ( select t1.message_id " + 
				"                   from ECORE_MESSAGE_BUILDING_BLOCK t1 " + 
				"                  where t1.data_type_id in ( " + msgComponentIds + " ) ) order by t2.name ";
		
		ArrayList<EcoreMessageDefinition> msgDefintionList = new ArrayList<>();
		
		try {
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(querySql);
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
		
		String querySql = " select * " + 
				"  from ECORE_MESSAGE_DEFINITION t2 " + 
				" where t2.id in ( select t1.message_id " + 
				"                   from ECORE_MESSAGE_BUILDING_BLOCK t1 " + 
				"                  where t1.data_type_id = ? ) order by t2.name ";
		
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
	
	public static EcoreMessageDefinition getMessageDefinitionById(String msgDefinitionId) {
		EcoreMessageDefinition msgDefinition = new EcoreMessageDefinition();
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ecore_message_definition t where t.id = ? ";
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, msgDefinitionId);
			rs = pstmt.executeQuery();
			while(rs.getFetchSize() == 1 && rs.next()) {
				msgDefinition.setId(rs.getString("ID"));
				msgDefinition.setName(rs.getString("NAME"));
				msgDefinition.setDefinition(rs.getString("DEFINITION"));
				msgDefinition.setRegistrationStatus(rs.getString("REGISTRATION_STATUS"));
				msgDefinition.setRemovalDate(rs.getDate("REMOVAL_DATE"));
				msgDefinition.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
				msgDefinition.setMessageDefinitionIdentifier(rs.getString("message_Definition_Identifier"));
				msgDefinition.setPreviousVersion(rs.getString("PREVIOUS_VERSION"));
				msgDefinition.setVersion(rs.getString("VERSION"));
				msgDefinition.setBusinessAreaId(rs.getString("BUSINESS_AREA_ID"));
				msgDefinition.setRootElement(rs.getString("ROOT_ELEMENT"));
				msgDefinition.setFlavour(rs.getString("FLAVOUR"));
				msgDefinition.setMessageFunctionality(rs.getString("MESSAGE_FUNCTIONALITY"));
				msgDefinition.setXmlTag(rs.getString("XML_TAG"));
				msgDefinition.setXmlName(rs.getString("XML_NAME"));
				msgDefinition.setBusinessArea(rs.getString("BUSINESS_AREA"));
				msgDefinition.setIsfromiso20022(rs.getString("IS_FROM_ISO20022"));
			}
			return msgDefinition;
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
	
	public static ArrayList<EcoreCode> getCodeByCodeSetId(String codeSetId) {
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ECORE_CODE t where t.code_set_id = ? order by t.name ";
		ArrayList<EcoreCode> ecoreCodeList = new ArrayList<>();
		
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, codeSetId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				EcoreCode ecoreCode = new EcoreCode();
				ecoreCode.setId(rs.getString("ID"));
				ecoreCode.setName(rs.getString("NAME"));
				ecoreCode.setDefinition(rs.getString("DEFINITION"));
				ecoreCode.setCodeName(rs.getString("CODE_NAME"));
				ecoreCode.setRegistrationStatus(rs.getString("REGISTRATION_STATUS"));
				ecoreCode.setRemovalDate(rs.getDate("REMOVAL_DATE"));
				ecoreCodeList.add(ecoreCode);
			}
			return ecoreCodeList;
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
	
	public static EcoreDataType getDataTypeById(String dataTypeId) {
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ECORE_DATA_TYPE t where t.id = ? ";
		EcoreDataType dataType = new EcoreDataType();
		
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, dataTypeId);
			rs = pstmt.executeQuery();
			while(rs.getFetchSize() == 1 && rs.next()) {
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
			}
			return dataType;
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
	
	public static EcoreMessageComponent getMessageComponentById(String messageComponentId) {
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ECORE_MESSAGE_COMPONENT t where t.id = ? ";
		EcoreMessageComponent messageComponent = new EcoreMessageComponent();
		
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, messageComponentId);
			rs = pstmt.executeQuery();
			
			while(rs.getFetchSize() == 1 && rs.next()) {
				messageComponent.setId(rs.getString("ID"));
				messageComponent.setName(rs.getString("NAME"));
				messageComponent.setComponentType(rs.getString("COMPONENT_TYPE"));
				messageComponent.setDefinition(rs.getString("DEFINITION"));
				messageComponent.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
				messageComponent.setRegistrationStatus(rs.getString("REGISTRATION_STATUS"));
				messageComponent.setTrace(rs.getString("TRACE"));
				messageComponent.setTraceName(rs.getString("TRACE_NAME"));
				messageComponent.setTechnical(rs.getString("TECHNICAL"));
				messageComponent.setPreviousVersion(rs.getString("PREVIOUS_VERSION"));
			}
			return messageComponent;
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
	
	public static ArrayList<EcoreMessageElement> getMessageElementList(String messageComponentId) {
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		Statement st=null;
		ResultSet rs=null;
		ResultSet rs1=null;
		Map<String,String> idMap=new HashMap<String,String>();
		
		String querySql = " select * from ECORE_MESSAGE_ELEMENT t where t.MESSAGE_COMPONENT_ID = ? ";
		String dstrSql="select id,name from ecore_data_type union all select id,name from ecore_external_schema union all select id,name from ecore_message_component";
		
		ArrayList<EcoreMessageElement> messageElementList = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(url);
			st=conn.createStatement();
			rs1=st.executeQuery(dstrSql);
			while(rs1.next()) {
				idMap.put(rs1.getString(1), rs1.getString(2));
			}
			
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, messageComponentId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				EcoreMessageElement me = new EcoreMessageElement();
				me.setId(rs.getString("ID"));
				me.setName(rs.getString("NAME"));
				me.setType(rs.getString("TYPE"));
				me.setTypeId(rs.getString("TYPE_ID"));
				me.setTypeName(idMap.get(me.getTypeId()));
				me.setMinOccurs(rs.getInt("MIN_OCCURS"));
				me.setMaxOccurs(rs.getInt("MAX_OCCURS"));
				me.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
				me.setXmlTag(rs.getString("XML_TAG"));
				me.setDefinition(rs.getString("DEFINITION"));
				me.setTraceType(rs.getString("TRACE_TYPE"));
				me.setTrace(rs.getString("TRACE"));
				me.setTracePath(rs.getString("TRACE_PATH"));
				me.setTracesTo(rs.getString("TRACES_TO"));
				me.setTypeOfTracesTo(rs.getString("TYPE_OF_TRACES_TO"));
				me.setTechnical(rs.getString("TECHNICAL"));
				me.setRegistrationStatus(rs.getString("REGISTRATION_STATUS"));
				messageElementList.add(me);
			}
			return messageElementList;
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
	
	public static ArrayList<EcoreMessageComponent> getMessageComponentList(String bizComponentId) {
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ECORE_MESSAGE_COMPONENT t where t.TRACE = ? order by t.name ";
		ArrayList<EcoreMessageComponent> messageComponentList = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, bizComponentId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				EcoreMessageComponent mc = new EcoreMessageComponent();
				mc.setId(rs.getString("ID"));
				mc.setName(rs.getString("NAME"));
				messageComponentList.add(mc);
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
	
	public static ArrayList<EcoreMessageComponent> getMessageComponentList() {
		
		Connection conn= null; 
		Statement stmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ECORE_MESSAGE_COMPONENT order by name ";
		ArrayList<EcoreMessageComponent> msgComponentList = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(querySql);
			while(rs.next()) {
				EcoreMessageComponent msgComponent = new EcoreMessageComponent();
				msgComponent.setId(rs.getString("ID"));
				msgComponent.setName(rs.getString("NAME"));
				msgComponent.setRegistrationStatus(rs.getString("REGISTRATION_STATUS"));
				msgComponentList.add(msgComponent);
			}
			return msgComponentList;
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
	
	public static ArrayList<EcoreMessageComponent> getMessageComponentListByMCId(String mcId) {
		
		ArrayList<EcoreMessageComponent> msgComponentList = new ArrayList<>();
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ecore_message_component t2 where t2.id in ( " + 
				" select t.message_component_id from ecore_message_element t where t.type_id = ? ) order by name ";
		
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, mcId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				EcoreMessageComponent msgComponent = new EcoreMessageComponent();
				msgComponent.setId(rs.getString("ID"));
				msgComponent.setName(rs.getString("NAME"));
				msgComponentList.add(msgComponent);
			}
			return msgComponentList;
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
//				bizElement.setDefinition(rs.getString("DEFINITION"));
//				bizElement.setIsDerived(rs.getString("IS_DERIVED"));
				bizElement.setMinOccurs(rs.getInt("MIN_OCCURS"));
				bizElement.setMaxOccurs(rs.getInt("MAX_OCCURS"));
//				bizElement.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
//				bizElement.setRegistrationStatus(rs.getString("REGISTRATION_STATUS"));
//				bizElement.setRemovalDate(rs.getDate("REMOVAL_DATE"));
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

public static Map<String,ArrayList<EcoreBusinessElement>> getAllBeByTyId() {
	
	Connection conn= null; 
	PreparedStatement pstmt=null;
	ResultSet rs=null;
	
	String querySql = " select * from ECORE_BUSINESS_ELEMENT order by name";
	Map<String, ArrayList<EcoreBusinessElement>> Data=new HashMap<String,ArrayList<EcoreBusinessElement>>();
	try {
		conn = DriverManager.getConnection(url);
		pstmt = conn.prepareStatement(querySql);
//		pstmt.setString(1, bizComponentId);
		rs = pstmt.executeQuery();
		while(rs.next()) {
			EcoreBusinessElement bizElement = new EcoreBusinessElement();
			bizElement.setId(rs.getString("ID"));
			bizElement.setName(rs.getString("NAME"));
//			bizElement.setDefinition(rs.getString("DEFINITION"));
//			bizElement.setIsDerived(rs.getString("IS_DERIVED"));
			bizElement.setMinOccurs(rs.getInt("MIN_OCCURS"));
			bizElement.setMaxOccurs(rs.getInt("MAX_OCCURS"));
//			bizElement.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
//			bizElement.setRegistrationStatus(rs.getString("REGISTRATION_STATUS"));
//			bizElement.setRemovalDate(rs.getDate("REMOVAL_DATE"));
			bizElement.setTypeId(rs.getString("TYPE_ID"));
			bizElement.setType(rs.getString("TYPE"));
			bizElement.setBusinessComponentId(rs.getString("BUSINESS_COMPONENT_ID"));
			String id=bizElement.getTypeId();
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

public static ArrayList<EcoreBusinessElement> getBusniessTaceBe() {
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select t.name from ECORE_BUSINESS_ELEMENT t ";
		ArrayList<EcoreBusinessElement> bizBusinessElementList = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
//			pstmt.setString(1, bizComponentId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				EcoreBusinessElement bizElement = new EcoreBusinessElement();
				bizElement.setId(rs.getString("ID"));
				bizElement.setName(rs.getString("NAME"));
//				bizElement.setDefinition(rs.getString("DEFINITION"));
//				bizElement.setIsDerived(rs.getString("IS_DERIVED"));
				bizElement.setMinOccurs(rs.getInt("MIN_OCCURS"));
				bizElement.setMaxOccurs(rs.getInt("MAX_OCCURS"));
//				bizElement.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
//				bizElement.setRegistrationStatus(rs.getString("REGISTRATION_STATUS"));
//				bizElement.setRemovalDate(rs.getDate("REMOVAL_DATE"));
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
	
	
	public static ArrayList<EcoreTreeNode> getSubBusinessComponentById(String bizComponentId) {
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		StringBuilder querySql = new StringBuilder();
		querySql.append(" select t1.* from ECORE_BUSINESS_COMPONENT t1 where t1.id in ");
		String nestedSql = " ( select t2.id from ECORE_BUSINESS_COMPONENT_RL t2 where t2.p_id = ? ) ";
		querySql.append(nestedSql);
		querySql.append(" order by name ");
		ArrayList<EcoreTreeNode> ecoreTreeNodeList = new ArrayList<EcoreTreeNode>();
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql.toString());
			pstmt.setString(1, bizComponentId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				EcoreTreeNode ecoreTreeNode = new EcoreTreeNode();
				ecoreTreeNode.setId(rs.getString("ID"));
				ecoreTreeNode.setName(rs.getString("NAME"));
				ecoreTreeNode.setType("2"); // Business Component
				ecoreTreeNode.setRegistrationStatus(rs.getString("REGISTRATION_STATUS"));
				ecoreTreeNodeList.add(ecoreTreeNode);
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

public static Map<String,ArrayList<EcoreBusinessElement>> getAllSubBusinessByBe() {
	
	Connection conn= null; 
	PreparedStatement pstmt=null;
	ResultSet rs=null;
	
//	StringBuilder querySql = new StringBuilder();
//	querySql.append(" select t1.* from ECORE_BUSINESS_COMPONENT t1 where t1.id in ");
	String querySql = " select t1.id,t1.name,t1.type_id,t1.type from ecore_business_element t1  order by name";
//	querySql.append(nestedSql);
//	querySql.append(" order by name ");
	
//	StringBuilder querySql = new StringBuilder();
//	querySql.append(" select t1.* from ECORE_BUSINESS_COMPONENT t1 where t1.id in ");
//	String nestedSql = " ( select t2.id from ECORE_BUSINESS_COMPONENT_RL t2 ) ";
//	querySql.append(nestedSql);
//	querySql.append(" order by name ");
	Map<String,ArrayList<EcoreBusinessElement>> ecoreTreeNodeList = new HashMap<String,ArrayList<EcoreBusinessElement>>();
	try {
		conn = DriverManager.getConnection(url);
		pstmt = conn.prepareStatement(querySql.toString());
//		pstmt.setString(1, bizComponentId);
		rs = pstmt.executeQuery();
		while (rs.next()) {
			EcoreBusinessElement ecoreTreeNode = new EcoreBusinessElement();
			ecoreTreeNode.setId(rs.getString("ID"));
			ecoreTreeNode.setName(rs.getString("NAME"));
			ecoreTreeNode.setType(rs.getString("TYPE"));
			ecoreTreeNode.setTypeId(rs.getString("TYPE_ID"));
//			ecoreTreeNode.setPid(rs.getString("P_ID"));
//			ecoreTreeNode.setRegistrationStatus(rs.getString("REGISTRATION_STATUS"));
			String id=ecoreTreeNode.getTypeId();
			ArrayList<EcoreBusinessElement>list=new ArrayList<EcoreBusinessElement>();
			list.add(ecoreTreeNode);
			if (ecoreTreeNodeList.containsKey(id)) {
				ArrayList<EcoreBusinessElement>list_1=new ArrayList<EcoreBusinessElement>();
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
	
public static Map<String,String> getAllBusinessComponent() {
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select t.id,t.name from ECORE_BUSINESS_COMPONENT t order by name";
		Map<String,String>map=new HashMap<String, String>();
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
//			pstmt.setString(1, bizComponentId);
			rs = pstmt.executeQuery();
			if (rs.getFetchSize() == 1) {
				EcoreBusinessComponent bizComponent = new EcoreBusinessComponent();
				while (rs.next()) {
					bizComponent.setId(rs.getString("ID"));
					bizComponent.setName(rs.getString("NAME"));
//					bizComponent.setDefinition(rs.getString("DEFINITION"));
//					bizComponent.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
//					bizComponent.setRegistrationStatus(rs.getString("REGISTRATION_STATUS"));
//					bizComponent.setRemovalDate(rs.getDate("REMOVAL_DATE"));
					map.put(bizComponent.getId(), bizComponent.getName());
				}
				return map;
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
	
	public static ArrayList<EcoreBusinessElement> getBusinessElementById(String bizElementId) {
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ECORE_BUSINESS_ELEMENT be where be.id = ? ";
		
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, bizElementId);
			rs = pstmt.executeQuery();
			if (rs.getFetchSize() == 1) {
				ArrayList<EcoreBusinessElement> businessElementList = new ArrayList<>();
				EcoreBusinessElement bizElement = new EcoreBusinessElement();
				while (rs.next()) {
					bizElement.setId(rs.getString("ID"));
					bizElement.setDefinition(rs.getString("DEFINITION"));
					bizElement.setName(rs.getString("NAME"));
					bizElement.setRegistrationStatus(rs.getString("REGISTRATION_STATUS"));
					bizElement.setRemovalDate(rs.getDate("REMOVAL_DATE"));
					bizElement.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
					bizElement.setMaxOccurs(rs.getInt("MAX_OCCURS"));
					bizElement.setMinOccurs(rs.getInt("MIN_OCCURS"));
					bizElement.setIsDerived(rs.getString("IS_DERIVED"));
					bizElement.setBusinessComponentId(rs.getString("BUSINESS_COMPONENT_ID"));
					bizElement.setType(rs.getString("TYPE"));
					bizElement.setTypeId(rs.getString("TYPE_ID"));
					businessElementList.add(bizElement);
				}
				return businessElementList;
			} else {
				throw new RuntimeException("Duplicate Business Element");
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
	
	public static ArrayList<String> getNextVersionIds(String currentVersionId) {
		ArrayList<String> nextVersionIdList = new ArrayList<>();
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select t.NEXT_VERSION_ID from ecore_next_versions t where t.id = ? ";
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, currentVersionId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				nextVersionIdList.add(rs.getString("NEXT_VERSION_ID"));
			}
			
			return nextVersionIdList;
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
	
public static List<EcoreBusinessComponent> getBusinessComponentList() {
		
		List<EcoreBusinessComponent> businessComponentList = new ArrayList<EcoreBusinessComponent>();
		String querySql = " select id, name from ecore_business_component t ";
		Connection conn= null; 
		Statement stmt=null;
		ResultSet rs=null;
		try {
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(querySql);
			while(rs.next()) {
				EcoreBusinessComponent bizComponnet = new EcoreBusinessComponent();
				bizComponnet.setId(rs.getString(1));
				bizComponnet.setName(rs.getString(2));
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
	
	public static ArrayList<EcoreBusinessComponent> getBusinessComponentListByIds(ArrayList<String> bizComponentIds) {
		
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
	
	public static ArrayList<EcoreMessageDefinition> getMsgDefinitionListByIds(ArrayList<String> msgDefinitionIds) {
		ArrayList<EcoreMessageDefinition> msgDefinitions = new ArrayList<>();
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
	
	public static ArrayList<EcoreMessageComponent> getMessageComponentList(ArrayList<String> msgComponentIds) {
		ArrayList<EcoreMessageComponent> msgComponentList = new ArrayList<>();
		if (msgComponentIds.size() == 0) {
			return msgComponentList;
		}
		StringBuilder sb = new StringBuilder();
		for (String id: msgComponentIds) {
			sb.append("'"  + id + "'");
			sb.append(",");
		}
		String parametersString = sb.substring(0, sb.length() - 1);
		Connection conn= null; 
		Statement stmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ecore_message_component t where t.id in ( " + parametersString + " ) ";
		
		try {
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(querySql);
			while(rs.next()) {
				EcoreMessageComponent msgComponent = new EcoreMessageComponent();
				msgComponent.setId(rs.getString("ID"));
				msgComponent.setName(rs.getString("NAME"));
				msgComponentList.add(msgComponent);
			}
			return msgComponentList;
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
	
	public static EcoreMessageDefinition getPreviousVersionMsgDefinitionByMsgDefId(EcoreMessageDefinition msgDefinition) {
		EcoreMessageDefinition previousMsgDefinition = DerbyDaoUtil.getMessageDefinitionById(msgDefinition.getPreviousVersion());
		return previousMsgDefinition;
	}
	
	public static EcoreMessageComponent getPreviousVersionMsgComponentByMsgComponentId(EcoreMessageComponent msgComponent) {
		return DerbyDaoUtil.getMessageComponentById(msgComponent.getPreviousVersion());
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
	
	public static ArrayList<EcoreMessageSet> getMessageSetByMsgDefinitionList(ArrayList<EcoreMessageDefinition> msgDefinitionList) {
		
		ArrayList<EcoreMessageSet> messageSetList = new ArrayList<>();
		
		if (msgDefinitionList.size() == 0) {
			return messageSetList;
		}
		
		String querySql = " select * from ecore_message_set t5 where t5.id in ( " +
				" select t4.set_id from ecore_message_set_defintion_rl t4 where t4.message_id in ( ";
		
		StringBuilder sb = new StringBuilder();
		for (EcoreMessageDefinition definition: msgDefinitionList) {
			sb.append("'" + definition.getId() + "'");
			sb.append(",");
		}
		String definitionIds = sb.substring(0, sb.length() - 1);
		querySql = querySql + definitionIds + ")) order by name ";
		
		Connection conn= null; 
		Statement stmt=null;
		ResultSet rs=null;
		
		try {
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(querySql);
			while(rs.next()) {
				EcoreMessageSet messageSet = new EcoreMessageSet();
				messageSet.setId(rs.getString("ID"));
				messageSet.setName(rs.getString("NAME"));
				messageSetList.add(messageSet);
			}
			return messageSetList;
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

	public static EcoreDataType getDataTypePreviousVersion(String previousVersionId) {
		return DerbyDaoUtil.getDataTypeById(previousVersionId);
	}
	
	public static ArrayList<EcoreDataType> getDataTypeListByIds(ArrayList<String> dataTypeIdList) {
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

	public static EcoreCode getEcoreCodeById(String ecoreCodeId) {
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		EcoreCode ecoreCode = new EcoreCode();
		String querySql = " select * from ecore_code t where t.id = ? ";
		
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, ecoreCodeId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ecoreCode.setId(rs.getString("ID"));
				ecoreCode.setCodesetid(rs.getString("CODE_SET_ID"));
			}
			return ecoreCode;
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

	public static ArrayList<EcoreCode> getEcoreCodeListByDataTypeId(String dataTypeId) {
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		ArrayList<EcoreCode> ecoreCodeList = new ArrayList<>();
		String querySql = " select * from ecore_code t where t.code_set_id = ? order by name";
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, dataTypeId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				EcoreCode ecoreCode = new EcoreCode();
				ecoreCode.setId(rs.getString("ID"));
				ecoreCode.setName(rs.getString("NAME"));
				ecoreCode.setDefinition(rs.getString("DEFINITION"));
				ecoreCode.setCodeName(rs.getString("CODE_NAME"));
				ecoreCode.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
				ecoreCode.setRegistrationStatus(rs.getString("REGISTRATION_STATUS"));
				ecoreCode.setRemovalDate(rs.getDate("REMOVAL_DATE"));
				ecoreCodeList.add(ecoreCode);
			}
			return ecoreCodeList;
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
	
	public static EcoreExternalSchema getEcoreExternalSchemaById(String externalSchemaId) {
		
		EcoreExternalSchema ecoreExternalSchema = new EcoreExternalSchema();
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ECORE_EXTERNAL_SCHEMA t where t.id = ? ";
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, externalSchemaId);
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
	
	public static ArrayList<EcoreExternalSchema> getEcoreExternalSchemaListByIds(ArrayList<String> ecoreExternalSchemaIds) {
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

	public static ArrayList<EcoreSemanticMarkupElement> getSynonymsList(String id) {
		ArrayList<EcoreSemanticMarkupElement> synonymsList = new ArrayList<>();
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ecore_semantic_markup_element t2 where t2.semantic_markup_id in " + 
				" ( select t1.id from ecore_semantic_markup t1 where t1.obj_id = ? ) ";
		
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			Map<String, EcoreSemanticMarkupElement> map = new HashMap<>();
			while(rs.next()) {
				EcoreSemanticMarkupElement markupElement = new EcoreSemanticMarkupElement();
				String markupId = rs.getString("SEMANTIC_MARKUP_ID");
				if (map.containsKey(markupId)) {
					markupElement = map.get(markupId);
				}
				
				if ("context".equalsIgnoreCase(rs.getString("NAME"))) {

					markupElement.setName(rs.getString("VALUE"));

				} else if ("value".equalsIgnoreCase(rs.getString("NAME"))) {
					
					markupElement.setValue(rs.getString("VALUE"));
				}
				map.put(markupId, markupElement);
				
			}
			synonymsList.addAll(map.values());
			return synonymsList;
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

	public static ArrayList<EcoreNamespaceList> getNameSpaceListByObjId(String objId) {
		
		ArrayList<EcoreNamespaceList> nameSpaceList = new ArrayList<>();
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ecore_namespace_list t where t.obj_id = ? ";
		
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, querySql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				EcoreNamespaceList ecoreNamespace = new EcoreNamespaceList();
				ecoreNamespace.setId(rs.getString("ID"));
				ecoreNamespace.setNamespaceList(rs.getString("NAMESPACE_LIST"));
				nameSpaceList.add(ecoreNamespace);
			}
			return nameSpaceList;
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

	public static EcoreMessageElement getMessageElementById(String msgElementID) {
		
		EcoreMessageElement ecoreMessageElement = new EcoreMessageElement();
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ecore_message_element t where t.id = ? ";
		
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, msgElementID);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ecoreMessageElement.setId(rs.getString("ID"));
				ecoreMessageElement.setName(rs.getString("NAME"));
				ecoreMessageElement.setDefinition(rs.getString("DEFINITION"));
				ecoreMessageElement.setMinOccurs(rs.getInt("MIN_OCCURS"));
				ecoreMessageElement.setMaxOccurs(rs.getInt("MAX_OCCURS"));
				ecoreMessageElement.setXmlTag(rs.getString("XML_TAG"));
				ecoreMessageElement.setTechnical(rs.getString("TECHNICAL"));
				ecoreMessageElement.setTrace(rs.getString("TRACE"));
				ecoreMessageElement.setTracesTo(rs.getString("TRACES_TO"));
				ecoreMessageElement.setTraceType(rs.getString("TRACE_TYPE"));
				ecoreMessageElement.setTypeOfTracesTo(rs.getString("TYPE_OF_TRACES_TO"));
				ecoreMessageElement.setTracePath(rs.getString("TRACE_PATH"));
				ecoreMessageElement.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
			}
			return ecoreMessageElement;
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

	public static void updateMsgDefinition(List<String> msgDefinitionIdList, String id) {
		
		if (msgDefinitionIdList.size() == 0) {
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		for (String msgDefinitionId: msgDefinitionIdList) {
			sb.append("'");
			sb.append(msgDefinitionId);
			sb.append("'");
			sb.append(",");
		}
		String idParameters = sb.substring(0, sb.length() - 1);
		
		Connection conn= null; 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String updateSql = " update ecore_message_definition set BUSINESS_AREA_ID = ? where id in ( " + idParameters  +" )";
		try {
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(updateSql);
			pstmt.setString(1, id); 
			pstmt.executeUpdate();
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
	
	public static ArrayList<EcoreDataType> getAllDataType() {
		
		ArrayList<EcoreDataType> datatypeList = new ArrayList<EcoreDataType>();
		
		Connection conn= null; 
		Statement stmt=null;
		ResultSet rs=null;
		
		String querySql = " select * from ecore_data_type order by name  ";
		try {
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(querySql);
			while(rs.next()) {
				EcoreDataType ecoreDataType = new EcoreDataType();
				ecoreDataType.setId(rs.getString("ID"));
				ecoreDataType.setName(rs.getString("NAME"));
				ecoreDataType.setRegistrationStatus(rs.getString("REGISTRATION_STATUS"));
				ecoreDataType.setType(rs.getString("TYPE"));
				ecoreDataType.setTrace(rs.getString("TRACE"));
				datatypeList.add(ecoreDataType);
			}
			return datatypeList;
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
}
