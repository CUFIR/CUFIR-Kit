package com.cfets.cufir.plugin.mr.utils;

import org.eclipse.core.runtime.Platform;

/**
 * derby数据库操作工�?
 * 
 * @author wjq
 * @deprecated
 */
public class DerbyUtil {

//	public static EcoreTreeNode getEcoreTreeNode() {
//		return new EcoreTreeNode();
//	}
	
	// 测试入口
	static String url = "jdbc:derby:D:\\cide-0.2.3\\cfets\\db;create=false";

	static {
		url = "jdbc:derby:"
				+ Platform.getInstallLocation().getURL().getPath().replaceFirst("/", "").replace("/", "//")
				+ "cfets\\db;create=false";
	}
	
//	public static List<EcoreDataType> getDataTypes() {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		List<EcoreDataType> dataTypeList = new ArrayList<>();
//		try {
//
////			url = "jdbc:derby:"
////					+ Platform.getInstallLocation().getURL().getPath().replaceFirst("/", "").replace("/", "//")
////					+ "cfets\\db;create=false";
//			conn = DriverManager.getConnection(url);
//			String sql = "select definition,name,registration_status,object_identifier,identification_scheme,value,type,id from ECORE_DATA_TYPE";
//
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();// 获取查询结果�?
//
//			// 查询结果放入List
//			while (rs.next()) {
//				EcoreDataType dataType = new EcoreDataType();
//				dataType.setId(rs.getString(8));
//				dataType.setDefinition(rs.getString(1));
//				dataType.setName(rs.getString(2));
//				dataType.setRegistrationStatus(rs.getString(3));
//				dataType.setObjectIdentifier(rs.getString(4));
//				dataType.setIdentificationScheme(rs.getString(5));
//				dataType.setValue(rs.getString(6));
//				dataType.setType(rs.getString(7));
//
//				List<EcoreCode> ecoreCodes = getEcoreCodes(rs.getString(8));
//				dataType.setEcoreCodeList(ecoreCodes);
//
//				dataTypeList.add(dataType);
//			}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (rs != null) {
//					rs.close();
//				}
//				if (pstmt != null) {
//					pstmt.close();
//				}
//				if (conn != null) {
//					conn.close();
//				}
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return dataTypeList;
//	}

//	public static List<EcoreCode> getEcoreCodes(String codeSetId) {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		List<EcoreCode> ecoreCodes = new ArrayList<>();
//		try {
//			conn = DriverManager.getConnection(url);
//			String sql = "select id,name,code_name,definition,registration_status,removal_date,object_identifier,code_set_id,is_from_iso20022 from ECORE_CODE where code_set_id = '"
//					+ codeSetId + "'";
//
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();// 获取查询结果�?
//
//			// 查询结果放入List
//			while (rs.next()) {
//				EcoreCode ecoreCode = new EcoreCode();
//				ecoreCode.setId(rs.getString(1));
//				ecoreCode.setName(rs.getString(2));
//				ecoreCode.setCodeName(rs.getString(3));
//				ecoreCode.setDefinition(rs.getString(4));
//				ecoreCode.setRegistrationStatus(rs.getString(5));
//				ecoreCode.setRemovalDate(rs.getDate(6));
//				ecoreCode.setObjectIdentifier(rs.getString(7));
//				ecoreCode.setCodesetid(rs.getString(8));
//				ecoreCode.setIsfromiso20022(rs.getString(9));
//				ecoreCodes.add(ecoreCode);
//			}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (rs != null) {
//					rs.close();
//				}
//				if (pstmt != null) {
//					pstmt.close();
//				}
//				if (conn != null) {
//					conn.close();
//				}
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return ecoreCodes;
//	}
//
//	public static List<EcoreBusinessComponent> getBusinessComponents() {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		List<EcoreBusinessComponent> list = new ArrayList<>();
//		try {
//			conn = DriverManager.getConnection(url);
//			String sql = "select id,name,definition,registration_status,removal_date,object_identifier,previous_version,version,is_from_iso20022 from ECORE_BUSINESS_COMPONENT";
//
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();// 获取查询结果�?
//
//			// 查询结果放入List
//			while (rs.next()) {
//				EcoreBusinessComponent businessComponent = new EcoreBusinessComponent();
//				businessComponent.setId(rs.getString(1));
//				businessComponent.setName(rs.getString(2));
//				businessComponent.setDefinition(rs.getString(3));
//				businessComponent.setRegistrationStatus(rs.getString(4));
//				businessComponent.setRemovalDate(rs.getDate(5));
//				businessComponent.setObjectIdentifier(rs.getString(6));
//				businessComponent.setPreviousVersion(rs.getString(7));
//				businessComponent.setVersion(rs.getString(8));
//				businessComponent.setIsfromiso20022(rs.getString(9));
//
//				// 查询下一级菜单的数据
//				List<EcoreBusinessElement> ecoreBusinessElements = DerbyUtil
//						.getBusinessElement(businessComponent.getId());
//				List<EcoreBusinessComponent> subBusinessComponentRLs = DerbyUtil
//						.getSubBusinessComponentRLs(businessComponent.getId());
//				List<EcoreBusinessComponent> superBusinessComponentRLs = DerbyUtil
//						.getSuperBusinessComponentRLs(businessComponent.getId());
//
//				businessComponent.setEcoreBusinessElements(ecoreBusinessElements);
//				businessComponent.setSubBusinessComponentRLs(subBusinessComponentRLs);
//				businessComponent.setSuperBusinessComponentRLs(superBusinessComponentRLs);
//
//				list.add(businessComponent);
//			}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (rs != null) {
//					rs.close();
//				}
//				if (pstmt != null) {
//					pstmt.close();
//				}
//				if (conn != null) {
//					conn.close();
//				}
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return list;
//	}
//
//	public static List<EcoreBusinessElement> getBusinessElement(String business_component_id) {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		List<EcoreBusinessElement> list = new ArrayList<>();
//		try {
//			conn = DriverManager.getConnection(url);
//			String sql = "select id,name,definition,registration_status,removal_date,object_identifier,previous_version,type,type_Id,max_Occurs,min_Occurs,business_component_id,version,is_derived,is_from_iso20022 from ECORE_BUSINESS_ELEMENT where business_component_id = '"
//					+ business_component_id + "'";
//
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();// 获取查询结果�?
//
//			// 查询结果放入List
//			while (rs.next()) {
//				EcoreBusinessElement businessElement = new EcoreBusinessElement();
//				businessElement.setId(rs.getString(1));
//				businessElement.setName(rs.getString(2));
//				businessElement.setDefinition(rs.getString(3));
//				businessElement.setRegistrationStatus(rs.getString(4));
//				businessElement.setRemovalDate(rs.getDate(5));
//				businessElement.setObjectIdentifier(rs.getString(6));
//				businessElement.setPreviousVersion(rs.getString(7));
//				businessElement.setType(rs.getString(8));
//				businessElement.setTypeId(rs.getString(9));
//				businessElement.setMaxOccurs(rs.getInt(10));
//				businessElement.setMinOccurs(rs.getInt(11));
//				businessElement.setBusinessComponentId(rs.getString(12));
//				businessElement.setVersion(rs.getString(13));
//				businessElement.setIsDerived(rs.getString(14));
//				businessElement.setIsfromiso20022(rs.getString(15));
//				list.add(businessElement);
//			}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (rs != null) {
//					rs.close();
//				}
//				if (pstmt != null) {
//					pstmt.close();
//				}
//				if (conn != null) {
//					conn.close();
//				}
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return list;
//	}
//
//	public static List<EcoreBusinessComponent> getSubBusinessComponentRLs(String pid) {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		List<EcoreBusinessComponent> list = new ArrayList<>();
//		try {
//			conn = DriverManager.getConnection(url);
//			String sql = "select b.id,b.name,b.definition,b.registration_status,b.removal_date,b.object_identifier,b.previous_version,b.version,b.is_from_iso20022 "
//					+ "from ECORE_BUSINESS_COMPONENT b where b.id in(select a.id from ECORE_BUSINESS_COMPONENT_RL a where a.p_id = '"
//					+ pid + "')";
//
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();// 获取查询结果�?
//
//			// 查询结果放入List
//			while (rs.next()) {
//				EcoreBusinessComponent businessComponent = new EcoreBusinessComponent();
//				businessComponent.setId(rs.getString(1));
//				businessComponent.setName(rs.getString(2));
//				businessComponent.setDefinition(rs.getString(3));
//				businessComponent.setRegistrationStatus(rs.getString(4));
//				businessComponent.setRemovalDate(rs.getDate(5));
//				businessComponent.setObjectIdentifier(rs.getString(6));
//				businessComponent.setPreviousVersion(rs.getString(7));
//				businessComponent.setVersion(rs.getString(8));
//				businessComponent.setIsfromiso20022(rs.getString(9));
//				list.add(businessComponent);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (rs != null) {
//					rs.close();
//				}
//				if (pstmt != null) {
//					pstmt.close();
//				}
//				if (conn != null) {
//					conn.close();
//				}
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return list;
//	}
//
//	public static List<EcoreBusinessComponent> getSuperBusinessComponentRLs(String id) {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		List<EcoreBusinessComponent> list = new ArrayList<>();
//		try {
//			conn = DriverManager.getConnection(url);
//			String sql = "select b.id,b.name,b.definition,b.registration_status,b.removal_date,b.object_identifier,b.previous_version,b.version,b.is_from_iso20022 "
//					+ "from ECORE_BUSINESS_COMPONENT b where b.id in(select a.p_id from ECORE_BUSINESS_COMPONENT_RL a where a.id = '"
//					+ id + "')";
//
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();// 获取查询结果�?
//
//			// 查询结果放入List
//			while (rs.next()) {
//				EcoreBusinessComponent businessComponent = new EcoreBusinessComponent();
//				businessComponent.setId(rs.getString(1));
//				businessComponent.setName(rs.getString(2));
//				businessComponent.setDefinition(rs.getString(3));
//				businessComponent.setRegistrationStatus(rs.getString(4));
//				businessComponent.setRemovalDate(rs.getDate(5));
//				businessComponent.setObjectIdentifier(rs.getString(6));
//				businessComponent.setPreviousVersion(rs.getString(7));
//				businessComponent.setVersion(rs.getString(8));
//				businessComponent.setIsfromiso20022(rs.getString(9));
//				list.add(businessComponent);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (rs != null) {
//					rs.close();
//				}
//				if (pstmt != null) {
//					pstmt.close();
//				}
//				if (conn != null) {
//					conn.close();
//				}
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return list;
//	}
//
//	public static List<EcoreMessageDefinition> getEcoreMessageDefinitions() {
//
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		ResultSet rs1 = null;
//		List<EcoreMessageDefinition> ecoreMessageDefinitions = new ArrayList<>();
//		try {
//			conn = DriverManager.getConnection(url);
//
//			String sql = "select id,name,definition,registration_status,removal_date,object_identifier,previous_version,xml_Tag,xml_name,root_element,flavour,message_functionality,business_area_id,business_area,version,is_from_iso20022 from ECORE_MESSAGE_DEFINITION";
//
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();// 获取查询结果�?
//
//			// 查询结果放入List
//			while (rs.next()) {
//				EcoreMessageDefinition ecoreMessageDefinition = new EcoreMessageDefinition();
//				ecoreMessageDefinition.setId(rs.getString(1));
//				ecoreMessageDefinition.setName(rs.getString(2));
//				ecoreMessageDefinition.setDefinition(rs.getString(3));
//				ecoreMessageDefinition.setRegistrationStatus(rs.getString(4));
//				ecoreMessageDefinition.setRemovalDate(rs.getDate(5));
//				ecoreMessageDefinition.setObjectIdentifier(rs.getString(6));
//				ecoreMessageDefinition.setPreviousVersion(rs.getString(7));
//				ecoreMessageDefinition.setXmlTag(rs.getString(8));
//				ecoreMessageDefinition.setXmlName(rs.getString(9));
//				ecoreMessageDefinition.setRootElement(rs.getString(10));
//				ecoreMessageDefinition.setFlavour(rs.getString(11));
//				ecoreMessageDefinition.setMessageFunctionality(rs.getString(12));
//				ecoreMessageDefinition.setBusinessAreaId(rs.getString(13));
//				ecoreMessageDefinition.setBusinessArea(rs.getString(14));
//				ecoreMessageDefinition.setVersion(rs.getString(15));
//				ecoreMessageDefinition.setIsfromiso20022(rs.getString(16));
//
//				String sql1 = "select id,name,definition,registration_status,removal_date,object_identifier,previous_version,xml_Tag,data_type,data_type_id,max_occurs,min_occurs,message_id,version,is_from_iso20022 from ECORE_MESSAGE_BUILDING_BLOCK where message_id = '"
//						+ rs.getString(1) + "'";
//				pstmt = conn.prepareStatement(sql1);
//				rs1 = pstmt.executeQuery();
//				List<EcoreMessageBuildingBlock> messageBuildingBlocks = new ArrayList<EcoreMessageBuildingBlock>();
//				while (rs1.next()) {
//					EcoreMessageBuildingBlock ecoreMessageBuildingBlock = new EcoreMessageBuildingBlock();
//					ecoreMessageBuildingBlock.setId(rs1.getString(1));
//					ecoreMessageBuildingBlock.setName(rs1.getString(2));
//					ecoreMessageBuildingBlock.setDefinition(rs1.getString(3));
//					ecoreMessageBuildingBlock.setRegistrationStatus(rs1.getString(4));
//					ecoreMessageBuildingBlock.setRemovalDate(rs1.getDate(5));
//					ecoreMessageBuildingBlock.setObjectIdentifier(rs1.getString(6));
//					ecoreMessageBuildingBlock.setPreviousVersion(rs1.getString(7));
//					ecoreMessageBuildingBlock.setXmlTag(rs1.getString(8));
//					ecoreMessageBuildingBlock.setDataType(rs1.getString(9));
//					ecoreMessageBuildingBlock.setDataTypeId(rs1.getString(10));
//					ecoreMessageBuildingBlock.setMaxOccurs(rs1.getInt(11));
//					ecoreMessageBuildingBlock.setMinOccurs(rs1.getInt(12));
//					ecoreMessageBuildingBlock.setMessageId(rs1.getString(13));
//					ecoreMessageBuildingBlock.setVersion(rs1.getString(14));
//					ecoreMessageBuildingBlock.setIsfromiso20022(rs1.getString(15));
//					messageBuildingBlocks.add(ecoreMessageBuildingBlock);
//				}
//
//				ecoreMessageDefinition.setMessageBuildingBlocks(messageBuildingBlocks);
//
//				ecoreMessageDefinitions.add(ecoreMessageDefinition);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (rs != null) {
//					rs.close();
//				}
//				if (rs1 != null) {
//					rs1.close();
//				}
//				if (pstmt != null) {
//					pstmt.close();
//				}
//				if (conn != null) {
//					conn.close();
//				}
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return ecoreMessageDefinitions;
//	}
//
//	public static List<EcoreExternalSchema> getEcoreExternalSchemas() {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		List<EcoreExternalSchema> list = new ArrayList<>();
//		try {
//			conn = DriverManager.getConnection(url);
//			String sql = "select id,name,definition,registration_status,removal_date, object_identifier,previous_version,process_content,technical,is_from_iso20022 "
//					+ "from ECORE_EXTERNAL_SCHEMA";
//
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//
//			// 查询结果放入List
//			while (rs.next()) {
//				EcoreExternalSchema ecoreExternalSchema = new EcoreExternalSchema();
//				ecoreExternalSchema.setId(rs.getString(1));
//				ecoreExternalSchema.setName(rs.getString(2));
//				ecoreExternalSchema.setDefinition(rs.getString(3));
//				ecoreExternalSchema.setRegistrationStatus(rs.getString(4));
//				ecoreExternalSchema.setRemovalDate(rs.getDate(5));
//				ecoreExternalSchema.setObjectIdentifier(rs.getString(6));
//				ecoreExternalSchema.setPreviousVersion(rs.getString(7));
//				ecoreExternalSchema.setProcessContent(rs.getString(8));
//				ecoreExternalSchema.setTechnical(rs.getString(9));
//				ecoreExternalSchema.setIsfromiso20022(rs.getString(10));
//				list.add(ecoreExternalSchema);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (rs != null) {
//					rs.close();
//				}
//				if (pstmt != null) {
//					pstmt.close();
//				}
//				if (conn != null) {
//					conn.close();
//				}
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return list;
//
//	}
//
//	public static List<EcoreMessageComponent> getEcoreMessageComponents() {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		ResultSet rs1 = null;
//		List<EcoreMessageComponent> list = new ArrayList<>();
//		try {
//			conn = DriverManager.getConnection(url);
//			String sql = "select id,name,definition,registration_status,removal_date,object_identifier,previous_version,component_type,version,is_from_iso20022 "
//					+ "from ECORE_MESSAGE_COMPONENT";
//
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//
//			// 查询结果放入List
//			while (rs.next()) {
//				EcoreMessageComponent ecoreMessageComponent = new EcoreMessageComponent();
//				ecoreMessageComponent.setId(rs.getString(1));
//				ecoreMessageComponent.setName(rs.getString(2));
//				ecoreMessageComponent.setDefinition(rs.getString(3));
//				ecoreMessageComponent.setRegistrationStatus(rs.getString(4));
//				ecoreMessageComponent.setRemovalDate(rs.getDate(5));
//				ecoreMessageComponent.setObjectIdentifier(rs.getString(6));
//				ecoreMessageComponent.setPreviousVersion(rs.getString(7));
//				ecoreMessageComponent.setComponentType(rs.getString(8));
//				ecoreMessageComponent.setVersion(rs.getString(9));
//				ecoreMessageComponent.setIsfromiso20022(rs.getString(10));
//
//				String sql1 = "select id,name,definition,registration_status,removal_date, object_identifier,previous_version,xml_Tag,type,type_Id,max_Occurs,min_Occurs,message_component_id,version,is_from_iso20022 "
//						+ "from ECORE_MESSAGE_ELEMENT where message_component_id = '" + rs.getString(1) + "'";
//				pstmt = conn.prepareStatement(sql1);
//				rs1 = pstmt.executeQuery();
//				List<EcoreMessageElement> ecoreMessageElementList = new ArrayList<EcoreMessageElement>();
//				while (rs1.next()) {
//					EcoreMessageElement ecoreMessageElement = new EcoreMessageElement();
//					ecoreMessageElement.setId(rs1.getString(1));
//					ecoreMessageElement.setName(rs1.getString(2));
//					ecoreMessageElement.setDefinition(rs1.getString(3));
//					ecoreMessageElement.setRegistrationStatus(rs1.getString(4));
//					ecoreMessageElement.setRemovalDate(rs1.getDate(5));
//					ecoreMessageElement.setObjectIdentifier(rs1.getString(6));
//					ecoreMessageElement.setPreviousVersion(rs1.getString(7));
//					ecoreMessageElement.setXmlTag(rs1.getString(8));
//					ecoreMessageElement.setType(rs1.getString(9));
//					ecoreMessageElement.setTypeId(rs1.getString(10));
//					ecoreMessageElement.setMaxOccurs(rs1.getInt(11));
//					ecoreMessageElement.setMinOccurs(rs1.getInt(12));
//					ecoreMessageElement.setMessageComponentId(rs1.getString(13));
//					ecoreMessageElement.setVersion(rs1.getString(14));
//					ecoreMessageElement.setIsfromiso20022(rs1.getString(15));
//					ecoreMessageElementList.add(ecoreMessageElement);
//				}
//				ecoreMessageComponent.setEcoreMessageElements(ecoreMessageElementList);
//
//				list.add(ecoreMessageComponent);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (rs != null) {
//					rs.close();
//				}
//				if (rs1 != null) {
//					rs1.close();
//				}
//				if (pstmt != null) {
//					pstmt.close();
//				}
//				if (conn != null) {
//					conn.close();
//				}
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return list;
//
//	}
//
//	public static List<EcoreMessageSet> getEcoreMessageSets() {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		ResultSet rs1 = null;
//		List<EcoreMessageSet> list = new ArrayList<>();
//		try {
//			conn = DriverManager.getConnection(url);
//			String sql = "select id,name,definition,registration_status,removal_date, object_identifier "
//					+ "from ECORE_MESSAGE_SET";
//
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//
//			// 查询结果放入List
//			while (rs.next()) {
//				EcoreMessageSet ecoreMessageSet = new EcoreMessageSet();
//				ecoreMessageSet.setId(rs.getString(1));
//				ecoreMessageSet.setName(rs.getString(2));
//				ecoreMessageSet.setDefinition(rs.getString(3));
//				ecoreMessageSet.setRegistrationStatus(rs.getString(4));
//				ecoreMessageSet.setRemovalDate(rs.getDate(5));
//				ecoreMessageSet.setObjectIdentifier(rs.getString(6));
//
//				List<EcoreMessageDefinition> ecoreMessageDefinitions = new ArrayList<>();
//				String sql1 = "select a.id,a.name,a.definition,a.registration_status,a.removal_date,a.object_identifier,a.previous_version,a.xml_Tag,a.xml_name,a.root_element,a.flavour,a.message_functionality,a.business_area_id,a.business_area,a.version,a.is_from_iso20022"
//						+ " from ECORE_MESSAGE_SET_DEFINTION_RL b inner join ECORE_MESSAGE_DEFINITION a on b.message_id = a.id where b.set_id = '"
//						+ rs.getString(1) + "'";
//				pstmt = conn.prepareStatement(sql1);
//				rs1 = pstmt.executeQuery();
//				while (rs1.next()) {
//					EcoreMessageDefinition ecoreMessageDefinition = new EcoreMessageDefinition();
//					ecoreMessageDefinition.setId(rs1.getString(1));
//					ecoreMessageDefinition.setName(rs1.getString(2));
//					ecoreMessageDefinition.setDefinition(rs1.getString(3));
//					ecoreMessageDefinition.setRegistrationStatus(rs1.getString(4));
//					ecoreMessageDefinition.setRemovalDate(rs1.getDate(5));
//					ecoreMessageDefinition.setObjectIdentifier(rs1.getString(6));
//					ecoreMessageDefinition.setPreviousVersion(rs1.getString(7));
//					ecoreMessageDefinition.setXmlTag(rs1.getString(8));
//					ecoreMessageDefinition.setXmlName(rs1.getString(9));
//					ecoreMessageDefinition.setRootElement(rs1.getString(10));
//					ecoreMessageDefinition.setFlavour(rs1.getString(11));
//					ecoreMessageDefinition.setMessageFunctionality(rs1.getString(12));
//					ecoreMessageDefinition.setBusinessAreaId(rs1.getString(13));
//					ecoreMessageDefinition.setBusinessArea(rs1.getString(14));
//					ecoreMessageDefinition.setVersion(rs1.getString(15));
//					ecoreMessageDefinition.setIsfromiso20022(rs1.getString(16));
//					ecoreMessageDefinitions.add(ecoreMessageDefinition);
//				}
//
//				ecoreMessageSet.setEcoreMessageDefinitions(ecoreMessageDefinitions);
//				list.add(ecoreMessageSet);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (rs != null) {
//					rs.close();
//				}
//				if (pstmt != null) {
//					pstmt.close();
//				}
//				if (conn != null) {
//					conn.close();
//				}
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return list;
//	}
//
//	public static List<EcoreBusinessArea> getEcoreBusinessAreas() {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		ResultSet rs1 = null;
//		List<EcoreBusinessArea> list = new ArrayList<>();
//		try {
//			conn = DriverManager.getConnection(url);
//			String sql = "select id,name,definition,registration_status,removal_date,object_identifier,code,is_from_iso20022 "
//					+ "from ECORE_BUSINESS_AREA";
//
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//
//			// 查询结果放入List
//			while (rs.next()) {
//				EcoreBusinessArea businessArea = new EcoreBusinessArea();
//				businessArea.setId(rs.getString(1));
//				businessArea.setName(rs.getString(2));
//				businessArea.setDefinition(rs.getString(3));
//				businessArea.setRegistrationStatus(rs.getString(4));
//				businessArea.setRemovalDate(rs.getDate(5));
//				businessArea.setObjectIdentifier(rs.getString(6));
//				businessArea.setCode(rs.getString(7));
//				businessArea.setIsfromiso20022(rs.getString(8));
//
//				List<EcoreMessageDefinition> ecoreMessageDefinitions = new ArrayList<>();
//				String sql1 = "select a.id,a.name,a.definition,a.registration_status,a.removal_date,a.object_identifier,a.previous_version,a.xml_Tag,a.xml_name,a.root_element,a.flavour,a.message_functionality,a.business_area_id,a.business_area,a.version,a.is_from_iso20022"
//						+ " from ECORE_MESSAGE_DEFINITION a where a.business_area_id = '" + rs.getString(1) + "'";
//				pstmt = conn.prepareStatement(sql1);
//				rs1 = pstmt.executeQuery();
//				while (rs1.next()) {
//					EcoreMessageDefinition ecoreMessageDefinition = new EcoreMessageDefinition();
//					ecoreMessageDefinition.setId(rs1.getString(1));
//					ecoreMessageDefinition.setName(rs1.getString(2));
//					ecoreMessageDefinition.setDefinition(rs1.getString(3));
//					ecoreMessageDefinition.setRegistrationStatus(rs1.getString(4));
//					ecoreMessageDefinition.setRemovalDate(rs1.getDate(5));
//					ecoreMessageDefinition.setObjectIdentifier(rs1.getString(6));
//					ecoreMessageDefinition.setPreviousVersion(rs1.getString(7));
//					ecoreMessageDefinition.setXmlTag(rs1.getString(8));
//					ecoreMessageDefinition.setXmlName(rs1.getString(9));
//					ecoreMessageDefinition.setRootElement(rs1.getString(10));
//					ecoreMessageDefinition.setFlavour(rs1.getString(11));
//					ecoreMessageDefinition.setMessageFunctionality(rs1.getString(12));
//					ecoreMessageDefinition.setBusinessAreaId(rs1.getString(13));
//					ecoreMessageDefinition.setBusinessArea(rs1.getString(14));
//					ecoreMessageDefinition.setVersion(rs1.getString(15));
//					ecoreMessageDefinition.setIsfromiso20022(rs1.getString(16));
//					ecoreMessageDefinitions.add(ecoreMessageDefinition);
//				}
//				businessArea.setEcoreMessageDefinitions(ecoreMessageDefinitions);
//
//				list.add(businessArea);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (rs != null) {
//					rs.close();
//				}
//				if (pstmt != null) {
//					pstmt.close();
//				}
//				if (conn != null) {
//					conn.close();
//				}
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return list;
//	}
//
//	public static void main(String[] args) {
//		List<EcoreBusinessComponent> businessComponents = getBusinessComponents();
//		System.out.println(businessComponents.size());
//	}
}
