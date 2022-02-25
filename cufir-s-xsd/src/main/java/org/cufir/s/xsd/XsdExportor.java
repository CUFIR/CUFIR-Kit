package org.cufir.s.xsd;


import java.sql.Connection;
import java.sql.PreparedStatement;

import org.cufir.s.xsd.util.DerbyUtil;


/**
 * 导出derby数据到本地
 * @author tangmaoquan
 * @Date 2021年10月15日
 */
public class XsdExportor {
	
	/**
	 * 将Derby数据库中内容到出到一组csv文件中
	 * @throws Exception
	 */
	public static void exportCsvs() throws Exception {
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
		ps.setString(3, "derby1.csv");
		ps.setString(4, "%");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_MESSAGE_ELEMENT");
		ps.setString(3, "derby2.csv");
		ps.setString(4, "%");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_MESSAGE_DEFINITION");
		ps.setString(3, "derby3.csv");
		ps.setString(4, "%");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_BUSINESS_AREA");
		ps.setString(3, "derby4.csv");
		ps.setString(4, "%");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_BUSINESS_COMPONENT");
		ps.setString(3, "derby5.csv");
		ps.setString(4, "%");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_BUSINESS_COMPONENT_RL");
		ps.setString(3, "derby6.csv");
		ps.setString(4, "%");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_BUSINESS_ELEMENT");
		ps.setString(3, "derby7.csv");
		ps.setString(4, "%");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_BUSINESS_PROCESS");
		ps.setString(3, "derby8.csv");
		ps.setString(4, "%");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_BUSINESS_ROLE");
		ps.setString(3, "derby9.csv");
		ps.setString(4, "%");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_CODE");
		ps.setString(3, "derby10.csv");
		ps.setString(4, "%");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_CONSTRAINT");
		ps.setString(3, "derby11.csv");
		ps.setString(4, "%");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_SEMANTIC_MARKUP_ELEMENT");
		ps.setString(3, "derby12.csv");
		ps.setString(4, "%");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_DATA_TYPE");
		ps.setString(3, "derby13.csv");
		ps.setString(4, "%");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_DOCLET");
		ps.setString(3, "derby14.csv");
		ps.setString(4, "%");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_ENUM");
		ps.setString(3, "derby15.csv");
		ps.setString(4, "%");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_EXAMPLE");
		ps.setString(3, "derby16.csv");
		ps.setString(4, "%");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_EXTERNAL_SCHEMA");
		ps.setString(3, "derby17.csv");
		ps.setString(4, "%");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_MESSAGE_BUILDING_BLOCK");
		ps.setString(3, "derby18.csv");
		ps.setString(4, "%");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_MESSAGE_SET");
		ps.setString(3, "derby19.csv");
		ps.setString(4, "%");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_MESSAGE_SET_DEFINTION_RL");
		ps.setString(3, "derby20.csv");
		ps.setString(4, "%");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_NAMESPACE_LIST");
		ps.setString(3, "derby21.csv");
		ps.setString(4, "%");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_NEXT_VERSIONS");
		ps.setString(3, "derby22.csv");
		ps.setString(4, "%");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		ps.setString(1, null);
		ps.setString(2, "ECORE_SEMANTIC_MARKUP");
		ps.setString(3, "derby23.csv");
		ps.setString(4, "%");
		ps.setString(5, null);
		ps.setString(6, null);
		ps.execute();
		
		
//		ps.setString(1, null);
//		ps.setString(2, "ECORE_MESSAGE_ELEMENT");
//		ps.setString(3, "derby.csv");
//		ps.setString(4, "%");
//		ps.setString(5, null);
//		ps.setString(6, null);
//		ps.execute();
//		document=(Document) ps.executeQuery();
//		ps.setString(1, null);
//		ps.setString(2, "ecore_message_element");
//		ps.setString(3, "Derby.dat");
//		ps.setString(4, "%");
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
		@SuppressWarnings("unused")
		String path="D:\\Derby";
		XsdExportor.exportCsvs();;
		//writeToDisk(path);
		System.out.println("导出成功");
	}
}
