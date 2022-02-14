package com.cfets.cufir.s.xsd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;


import com.cfets.cufir.s.xsd.bean.ElementNode;
import com.cfets.cufir.s.xsd.dao.XSDTreeDao;
import com.cfets.cufir.s.xsd.util.DerbyUtil;

public  class ListToTree2 implements XSDTreeDao{
	private static Logger logger = Logger.getLogger(ListToTree2.class);
	Map<String,Object> map=new HashMap<String,Object>();
	public static List<ElementNode>complexnodes=new ArrayList<ElementNode>();
	public static List<ElementNode>simplenodes=new ArrayList<ElementNode>();
	public static String namespaces="";
	public static String fileName="";
	public static String fileName_xmlTag="";
	public static void main(String[] args)throws Exception{
		long startTime=System.currentTimeMillis();//获取开始时间
		
		
		XSDTreeDao dao = new ListToTree2();
		String id = "_CSk2UTJdEeO58eY-wGOnqw";
  		String path = "D:\\XSDSchema";
		dao.queryListToMap(id,path);
		//从数据库中查询所有分组的节点
//		Map<String, Object> arrMap=queryListToMap();
//		
//		//获取缓存区数据
//		for (int i = 0; i < arrMap.size(); i++) {
//			Map<String,String>map=(Map<String, String>) arrMap.get(i);
//			String name="NAME";
//			if (name.equals("SafekeepingAccount")) {
//				System.out.println("取出成功"+map.get(i)+"==========");
//				return;
//				}
//				System.out.println(map);
//		}
		//List<Tree> list=arrMap.get("root");
		//System.out.println(list.size());
		//获取结束时间
		long endTime=System.currentTimeMillis();
		System.out.println("程序运行时间: "+(endTime-startTime)+"ms");
	}
	
	/**
	 * 1.分层查询，将每层数据放入map
	 * 2.根据type判断执行那个步骤
	 * @return 
	 */
	static Map<String,List<ElementNode>> SimpleMaps=new HashMap<String,List<ElementNode>>();
	static Map<String,List<ElementNode>> ComplexMaps=new HashMap<String,List<ElementNode>>();
	static Map<String,List<ElementNode>> CompentMaps=new HashMap<String,List<ElementNode>>();
	static Map<String,List<ElementNode>> DataTypeMaps=new HashMap<String,List<ElementNode>>();
	
	public  Map<String, Object> queryListToMap(String id,String path)throws SQLException{
//		Map<String,Object> map=new HashMap<String,Object>();
//		List<ElementNode>complexnodes=new ArrayList<ElementNode>();
//		List<ElementNode>simplenodes=new ArrayList<ElementNode>();
		Connection connection=DerbyUtil.getConnection();
		// 连接失败,再次请求连接
					if (connection == null) {
						connection = DerbyUtil.getConnection(); // 获得数据库连接
					}
//		Connection connection=getJdbcConnection("jdbc:oracle:thin:@200.31.156.74:1521:mdmdev","rdt","rdt","oracle.jdbc.driver.OracleDriver");
		Statement st=connection.createStatement();
//		ResultSet rs=null;
		//查询出所有报文信息并保存到缓存
		//查出最外层的节点信息
 		String sql_topNode="select b.xml_tag, b.name,b.message_id,"
				+ " b.data_type, b.data_type_id ,b.max_occurs,b.min_occurs"
				+ " from ecore_message_building_block b "
				+ " where b.message_id= '"+id+"'";
		
		String sql_code="select t.code_set_id, t.name,t.code_name from ecore_code t ";
		String sql_element="select t.name,t.type,t.type_id,t.xml_tag,t.max_occurs,t.min_occurs,t.message_component_id "
				+ " from ecore_message_element t";
		String sql_component="select c.id,c.name,c.component_type from ecore_message_component c ";
		String sql_dataType="select * from ecore_data_type t";
		String sql_namespace=" select a.name,a.xml_tag,(a.business_area||'.'||a.MESSAGE_FUNCTIONALITY||'.'||a.FLAVOUR||'.'||a.VERSION) As NameSpace  "
				+ " FROM ECORE_MESSAGE_DEFINITION a "
				+ " where a.id='"+id+"'";
		try {
			//将简单类型数据从数据库中取出来，并放进map
			ResultSet rs=st.executeQuery(sql_code);
			while(rs.next()) {
				ElementNode sb=new ElementNode();
				sb.setName(rs.getString("NAME"));
				sb.setCodeName(rs.getString("CODE_NAME"));
				String codeSetId=rs.getString("CODE_SET_ID");
				if(SimpleMaps.containsKey(codeSetId)) {
					SimpleMaps.get(codeSetId).add(sb);
				}
				else {
					List<ElementNode> nodes=new ArrayList<ElementNode>();
	        		nodes.add(sb);
	        		SimpleMaps.put(codeSetId,nodes);
				}
			}
			//将混合类型中的元素从数据库中取出来，并放进map
			ResultSet rs1=st.executeQuery(sql_element);
			while(rs1.next()) {
				ElementNode cb=new ElementNode();
				cb.setTypeId(rs1.getString("TYPE_ID"));
				//XSD元素呈现为name=rs1.XMLTAG type=NAME
				cb.setName(rs1.getString("XML_TAG"));
				cb.setType(rs1.getString("NAME"));
				cb.setDataType(rs1.getString("TYPE"));
				cb.setMaxOccurs(rs1.getString("MAX_OCCURS"));
				cb.setMinOccurs(rs1.getString("MIN_OCCURS"));
				String ComponentId=rs1.getString("MESSAGE_COMPONENT_ID");
				if(ComplexMaps.containsKey(ComponentId)) {
					ComplexMaps.get(ComponentId).add(cb);
				}
				else {
					List<ElementNode> nodes=new ArrayList<ElementNode>();
	        		nodes.add(cb);
	        		ComplexMaps.put(ComponentId,nodes);
				}
			}
			//将混合类型数据从数据库中取出来，并放进map
			ResultSet rs2=st.executeQuery(sql_component);
			while(rs2.next()) {
				ElementNode cb=new ElementNode();
				cb.setTypeId(rs2.getString("ID"));
				cb.setName(rs2.getString("NAME"));
				String Id=rs2.getString("ID");
				if(CompentMaps.containsKey(Id)) {
					CompentMaps.get(Id).add(cb);
				}
				else {
					List<ElementNode> nodes=new ArrayList<ElementNode>();
			        nodes.add(cb);
			        CompentMaps.put(Id,nodes);
				}
			}
			
			//将混合类型数据datatype从数据库中取出来，并放进map
			ResultSet rs4=st.executeQuery(sql_dataType);
			while(rs4.next()) {
				ElementNode cb=new ElementNode();
				cb.setTypeId(rs4.getString("ID"));
				cb.setName(rs4.getString("NAME"));
				cb.setType(rs4.getString("TYPE"));
				String Id=rs4.getString("ID");
				if(DataTypeMaps.containsKey(Id)) {
					DataTypeMaps.get(Id).add(cb);
				}
				else {
					List<ElementNode> nodes=new ArrayList<ElementNode>();
			        nodes.add(cb);
			        DataTypeMaps.put(Id,nodes);
				}
			}
			//将混合类型数据datatype从数据库中取出来，并放进map
			ResultSet rs5=st.executeQuery(sql_namespace);
			while (rs5.next()) {
				String name=rs5.getString("NAMESPACE");
				String fileName=rs5.getString("XML_TAG");
				String Type=rs5.getString("NAME");
				namespaces=name;
				ListToTree2.fileName=fileName;
				fileName_xmlTag=Type;
				
			}
//			List li=new ArrayList();
//			ResultSetMetaData data1=rs5.getMetaData();
//			for (int i = 0; i < data1.getColumnCount(); i++) {
//				namespaces.getColumnCount().get(i);
//			}
//			ElementNode node=new ElementNode();
//			XSDGenerate generate=new XSDGenerate();
			//ResultSetMetaData data1=rs1.getMetaData();
			//List<ElementNode> simplenodes=new ArrayList<ElementNode>();
			//List bodyName=new ArrayList();
			ResultSet rs3=st.executeQuery(sql_topNode);
			while (rs3.next()) {
				ElementNode ob=new ElementNode();
				ob.setName(fileName_xmlTag);
				List<ElementNode> ls=new ArrayList<ElementNode>();
				ElementNode eb=new ElementNode();
				eb.setName(rs3.getString("XML_TAG"));
				eb.setType(rs3.getString("NAME"));
				eb.setMaxOccurs(rs3.getString("MAX_OCCURS"));
				eb.setMinOccurs(rs3.getString("MIN_OCCURS"));
				ls.add(eb);
				ob.setList(ls);
				complexnodes.add(ob);
				if ("1".equals(rs3.getString("DATA_TYPE"))) {
					ElementNode cb=new ElementNode();
//					List<ElementNode> lls=new ArrayList<ElementNode>();
//					ElementNode en=new ElementNode();
					String typeId=rs3.getString("DATA_TYPE_ID");
//					en.setName(rs3.getString("NAME"));
//					lls.add(en);
//					cb.setCodes(lls);
//					simplenodes.add(cb);
					//去简单类型表中取数据
					List<ElementNode> list= ListToTree2.queryToSimple(typeId);
					System.out.println("简单类型列表"+list+"====================");
//					String name=null;
//					for (ElementNode object : list) {
//						name=object.getName();
//						
//					}
					//map.put(name, simplenodes);
//					ElementNode cb=new ElementNode();
					list.stream().forEach(t ->{
//						String ComponentId=t.getTypeId();
//						String code=t.getCodeName();
						cb.getCodeName();
						
					});
//					cb.setName(rs3.getString("NAME"));
//					cb.setCodes(list);
//					simplenodes.add(cb);
//					simplenodes.add(rs1.getString("NAME"),queryToSimple);
				} else if("2".equals(rs3.getString("DATA_TYPE"))) {
					ElementNode ab=new ElementNode();
					//表中取数据,根据datatype判断执行查询，datatype等于2，去Component表中取数据
					String dataTypeId=rs3.getString("DATA_TYPE_ID");
					List<ElementNode> queryToComponent = ListToTree2.queryToComponent(dataTypeId);
					System.out.println("混合类型"+queryToComponent+"================");
					ab.setType(rs3.getString("XML_TAG"));
					ab.setName(rs3.getString("NAME"));
					ab.setList(queryToComponent);
					complexnodes.add(ab);
					//generate.addComplexType(rs1.getString("NAME"),queryToComponent);
//					map_complex.put(k, v);
//					 complexnodes.add(queryToComponent);
					//修改
					//找出Component表中元素
					
					
					//String ComponentId=ob.getDataTypeId();
						
					ElementNode cnode=new ElementNode();
					
					queryToComponent.stream().forEach(t ->{
						String ComponentId=t.getTypeId();
						String name1=t.getName();
						List<ElementNode> list=ListToTree2.queryToElement(ComponentId);
						System.out.println("混合类型列表"+list+"===========");
						cnode.setName(name1);
						cnode.setList(list);
						 //map.put(name, complexnodes);
						 //List queryToElement= ListToTree.queryToElement(ComponentId);
						//List<ElementNode> elementNodes=null;
						//queryToElement
						//generate.addComplexType(t.getName() ,queryToElement);
					});
					
					complexnodes.add(cnode);
					//List<ElementNode>list= List.
									
					//complexnodes.add((ElementNode) list);
					
					//complexnodes.add(ob);
				 }
			
			}
			connection.commit();
			rs.close();
			rs1.close();
			rs2.close();
			rs3.close();
			rs4.close();
			rs5.close();
		}  catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
//			finally {
//			DerbyUtil.close(connection, st,null);
//		}
//		rs3.close();
		st.close();
		connection.close();
		System.out.println(complexnodes+"=============="+simplenodes);
//		return arrMap;
//		for (int i = 0; i < bodyName.size(); i++) {
//			generate.initHeaderBody(i)
//		}
//		generate.initHeaderBody("Header", "CancellationResponse","SecurityTrailer");
//		generate.writeToDisk(path);
		return null;
	}
	
	public static List<ElementNode> queryToSimple(String TypeID) {
//		String sql_simple="select t.name,t.code_name from ecore_code t where t.code_set_id='"+TypeID+"'";
		ElementNode ob=new ElementNode();
		List<ElementNode> list= SimpleMaps.get(TypeID);
		for (ElementNode object : list) {
			ob.setName(object.getName());
		}
		ob.setCodes(list);
		simplenodes.add(ob);
		return list;
	}
	
	public static List queryToElement(String ComponentId) {
		ElementNode en=new ElementNode();
		List<ElementNode> list=ComplexMaps.get(ComponentId);
		for (ElementNode complexBean : list) {
			String typeId=complexBean.getTypeId();
			
			if("2".equals(complexBean.getDataType())){
				String cc=complexBean.getTypeId();
				List ls=ListToTree2.queryToElement(cc);
				en.setName(complexBean.getType());
				en.setList(ls);
				complexnodes.add(en);
			}else {
			List<ElementNode> list1=DataTypeMaps.get(typeId);
			for (ElementNode complexBean2 : list1) {
				if ("1".equals(complexBean2.getType())) {
				ListToTree2.queryToSimple(typeId);
			}
			}
			}
		}
		return list;
	}
	
		
	public static List queryToComponent(String dataTypeID) {
		List<ElementNode> list=CompentMaps.get(dataTypeID);
		for (ElementNode elementNode : list) {
			//String name=elementNode.getName();
			String c=elementNode.getTypeId();
			if ("1".equals(elementNode.getComponentType())) {
				ListToTree2.queryToSimple(c);
			
			}else if("2".equals(elementNode.getComponentType())){
				ElementNode ob=new ElementNode();
				String cc=elementNode.getTypeId();
				List list1=ListToTree2.queryToElement(cc);
				ob.setName(elementNode.getName());
				ob.setType(elementNode.getXmlTag());
				ob.setList(list1);
				complexnodes.add(ob);
			}
		}
//		String sql_component="select c.id,c.name,c.component_type from ecore_message_component c "
//				+ " where c.id='"+dataTypeID+"'";
//		Connection connection=getJdbcConnection("jdbc:oracle:thin:@200.31.156.74:1521:mdmdev","rdt","rdt","oracle.jdbc.driver.OracleDriver");
//		List<ElementNode>list=new ArrayList<ElementNode>();
//		try {
//			Map<String, ElementNode>map_component=new HashMap<String, ElementNode>();
//			Statement st=connection.createStatement();
//			ResultSet rs=st.executeQuery(sql_component);
//			while(rs.next()) {
//			ElementNode ob=new ElementNode();
//			ob.setTypeId(rs.getString("ID"));
//			ob.setName(rs.getString("NAME"));
//			//ob.setDataType(rs.getString("COMPONENT_TYPE"));
//			String c=rs.getString("ID");
////			List<ElementNode>list=new ArrayList<ElementNode>();
//			list.add(ob);
//			//map_component.put(c,ob);
//			if ("1".equals(rs.getString("COMPONENT_TYPE"))) {
//				ListToTree.queryToSimple(c);
//			
//			}else if("2".equals(rs.getString("COMPONENT_TYPE"))){
//				ListToTree.queryToElement(c);
//			}
//			
//			}
////			String name=rs.getString("NAME");
////			map_component.put(name, list);
//			st.close();
//			rs.close();
//			connection.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		//complexNodes.add(ob);
		return list;
	}
	
	/**
	 * 数据库连接
	 * @param url
	 * @param username
	 * @param password
	 * @param driverClassName
	 * @return
	 */
	public static Connection getJdbcConnection(String url,String username,String password,
			String driverClassName) {
		Connection connection=null;
		try {
			Properties props=new Properties();
			props.put("user", username);
			props.put("password", password);
			props.put("remarksReporting", "true");
			
			try {
				Class.forName(driverClassName).newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			connection=DriverManager.getConnection(url,props);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	

}
