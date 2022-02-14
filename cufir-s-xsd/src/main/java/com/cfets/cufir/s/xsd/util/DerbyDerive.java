package com.cfets.cufir.s.xsd.util;


import java.sql.Connection;
import java.sql.PreparedStatement;

public class DerbyDerive {
//	static Document document=null;
	public static void Derive() throws Exception {
		PreparedStatement ps=null;
 		Connection connection=DerbyUtil.getConnection();
		// 连接失败,再次请求连接
		if (connection == null) {
			connection = DerbyUtil.getConnection(); // 获得数据库连接
		}
		
//		Statement st=connection.createStatement();
		ps=connection.prepareStatement("CALL SYSCS_UTIL.SYSCS_EXPORT_TABLE(?,?,?,?,?,?)");
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_MESSAGE_COMPONENT");
		ps.setString(3, "ECORE_MESSAGE_COMPONENT.csv");
		ps.setString(4, ",");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_MESSAGE_ELEMENT");
		ps.setString(3, "ECORE_MESSAGE_ELEMENT.csv");
		ps.setString(4, ",");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_MESSAGE_DEFINITION");
		ps.setString(3, "ECORE_MESSAGE_DEFINITION.csv");
		ps.setString(4, ",");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_BUSINESS_AREA");
		ps.setString(3, "ECORE_BUSINESS_AREA.csv");
		ps.setString(4, ",");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_BUSINESS_COMPONENT");
		ps.setString(3, "ECORE_BUSINESS_COMPONENT.csv");
		ps.setString(4, ",");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_BUSINESS_COMPONENT_RL");
		ps.setString(3, "ECORE_BUSINESS_COMPONENT_RL.csv");
		ps.setString(4, ",");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_BUSINESS_ELEMENT");
		ps.setString(3, "ECORE_BUSINESS_ELEMENT.csv");
		ps.setString(4, ",");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_BUSINESS_PROCESS");
		ps.setString(3, "ECORE_BUSINESS_PROCESS.csv");
		ps.setString(4, ",");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_BUSINESS_ROLE");
		ps.setString(3, "ECORE_BUSINESS_ROLE.csv");
		ps.setString(4, ",");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_CODE");
		ps.setString(3, "ECORE_CODE.csv");
		ps.setString(4, ",");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_CONSTRAINT");
		ps.setString(3, "ECORE_CONSTRAINT.csv");
		ps.setString(4, ",");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_SEMANTIC_MARKUP_ELEMENT");
		ps.setString(3, "ECORE_SEMANTIC_MARKUP_ELEMENT.csv");
		ps.setString(4, ",");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_DATA_TYPE");
		ps.setString(3, "ECORE_DATA_TYPE.csv");
		ps.setString(4, ",");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_DOCLET");
		ps.setString(3, "ECORE_DOCLET.csv");
		ps.setString(4, ",");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_ENUM");
		ps.setString(3, "ECORE_ENUM.csv");
		ps.setString(4, ",");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_EXAMPLE");
		ps.setString(3, "ECORE_EXAMPLE.csv");
		ps.setString(4, ",");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_EXTERNAL_SCHEMA");
		ps.setString(3, "ECORE_EXTERNAL_SCHEMA.csv");
		ps.setString(4, ",");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_MESSAGE_BUILDING_BLOCK");
		ps.setString(3, "ECORE_MESSAGE_BUILDING_BLOCK.csv");
		ps.setString(4, ",");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_MESSAGE_SET");
		ps.setString(3, "ECORE_MESSAGE_SET.csv");
		ps.setString(4, ",");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_MESSAGE_SET_DEFINTION_RL");
		ps.setString(3, "ECORE_MESSAGE_SET_DEFINTION_RL.csv");
		ps.setString(4, ",");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_NAMESPACE_LIST");
		ps.setString(3, "ECORE_NAMESPACE_LIST.csv");
		ps.setString(4, ",");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_NEXT_VERSIONS");
		ps.setString(3, "ECORE_NEXT_VERSIONS.csv");
		ps.setString(4, ",");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_SEMANTIC_MARKUP");
		ps.setString(3, "ECORE_SEMANTIC_MARKUP.csv");
		ps.setString(4, ",");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		
//		ps.setString(1, null);
//		ps.setString(2, "ECORE_MESSAGE_ELEMENT");
//		ps.setString(3, "derby.csv");
//		ps.setString(4, ",");
//		ps.setString(5, null);
//		ps.setString(6, null);
//		ps.execute();
//		document=(Document) ps.executeQuery();
//		ps.setString(1, null);
//		ps.setString(2, "ecore_message_element");
//		ps.setString(3, "Derby.dat");
//		ps.setString(4, ",");
//		ps.setString(5, null);
//		ps.setString(6, null);
//		ps.execute();
		ps.close();
		connection.close();
	}
	
	public static void main(String[] args) throws Exception {
//		ByteArrayOutputStream outSteam=new ByteArrayOutputStream();
//		String content=new String(outSteam.toByteArray(),"utf-8");
//		String content=document;
		String path="D:\\Derby";
		Derive();
		//writeToDisk(path);
		System.out.println("导出成功");
	}
}
