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

public  class ListToTree implements XSDTreeDao{
	private static Logger logger = Logger.getLogger(ListToTree.class);
	Map<String,Object> map=new HashMap<String,Object>();
	public static List<ElementNode>complexnodes=new ArrayList<ElementNode>();
	public static List<ElementNode>simplenodes=new ArrayList<ElementNode>();
	public static String namespaces="";
	public static String fileName="";
	public static String fileName_xmlTag="";
	public static void main(String[] args)throws Exception{
		long startTime=System.currentTimeMillis();//获取开始时间
		
		
		XSDTreeDao dao = new ListToTree();
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
	static Map<String,String> CompentMaps=new HashMap<String,String>();
	static Map<String,List<ElementNode>> DataTypeMaps=new HashMap<String,List<ElementNode>>();
	static Map<String,String> DataTypeNameMaps=new HashMap<String,String>();
	static Map<String,String> SchemaMaps=new HashMap<String,String>();
	
	public  Map<String, Object> queryListToMap(String id,String path)throws SQLException{
		ClearMap();
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
 		String sql_topNode="select b.max_occurs,b.min_occurs,b.data_type,mc.name,mc.id,b.xml_tag "
 				+ " from ecore_message_building_block b,ecore_message_component mc "
 				+ " where b.message_id='"+id+"'"
 				+ " and mc.id=b.data_type_id";
		
		String sql_code="select t.code_set_id, t.name,t.code_name from ecore_code t ";
		String sql_element="select t.name,t.type,t.type_id,t.xml_tag,t.max_occurs,t.min_occurs,t.message_component_id "
				+ " from ecore_message_element t";
		String sql_component="select c.id,c.name,c.component_type from ecore_message_component c ";
		String sql_dataType="select * from ecore_data_type t";
		String sql_namespace=" select a.name,a.xml_tag,(a.business_area||'.'||a.MESSAGE_FUNCTIONALITY||'.'||a.FLAVOUR||'.'||a.VERSION) As NameSpace  "
				+ " FROM ECORE_MESSAGE_DEFINITION a "
				+ " where a.id='"+id+"'";
		String sql_schema="select s.name,s.id from ecore_external_schema s";
		
		try {
			//将混合类型数据datatype从数据库中取出来，并放进map
			ResultSet rs0=st.executeQuery(sql_schema);
			while(rs0.next()) {
				String name=rs0.getString("NAME");
				String Id=rs0.getString("ID");
				SchemaMaps.put(Id, name);
			}
			//将混合类型数据datatype从数据库中取出来，并放进map
			ResultSet rs=st.executeQuery(sql_dataType);
			while(rs.next()) {
				ElementNode cb=new ElementNode();
				cb.setTypeId(rs.getString("ID"));
				cb.setName(rs.getString("NAME"));
				cb.setType(rs.getString("TYPE"));
				cb.setFractionDigits(rs.getString("FRACTION_DIGITS"));
				cb.setTotalDigits(rs.getString("TOTAL_DIGITS"));
				cb.setMinInclusive(rs.getString("MIN_INCLUSIVE"));
				cb.setMaxInclusive(rs.getString("MAX_INCLUSIVE"));
				cb.setMaxExclusive(rs.getString("MAX_EXCLUSIVE"));
				cb.setMinExclusive(rs.getString("MIN_EXCLUSIVE"));
				cb.setMinLength(rs.getString("MIN_LENGTH"));
				cb.setMaxLength(rs.getString("MAX_LENGTH"));
				cb.setPattern(rs.getString("PATTERN"));
				String Id=rs.getString("ID");
				String name=rs.getString("NAME");
				DataTypeNameMaps.put(Id, name);
				if(DataTypeMaps.containsKey(Id)) {
					DataTypeMaps.get(Id).add(cb);
				}
				else {
					List<ElementNode> nodes=new ArrayList<ElementNode>();
			        nodes.add(cb);
			        DataTypeMaps.put(Id,nodes);
				}
			}
			//将简单类型数据从数据库中取出来，并放进map
			ResultSet rs1=st.executeQuery(sql_code);
			while(rs1.next()) {
				ElementNode sb=new ElementNode();
				sb.setName(rs1.getString("NAME"));
				sb.setCodeName(rs1.getString("CODE_NAME"));
				String codeSetId=rs1.getString("CODE_SET_ID");
				if(SimpleMaps.containsKey(codeSetId)) {
					SimpleMaps.get(codeSetId).add(sb);
				}
				else {
					List<ElementNode> nodes=new ArrayList<ElementNode>();
	        		nodes.add(sb);
	        		SimpleMaps.put(codeSetId,nodes);
				}
			}
			//将混合类型数据从数据库中取出来，并放进map
			ResultSet rs2=st.executeQuery(sql_component);
			while(rs2.next()) {
//				ElementNode cb=new ElementNode();
//				cb.setTypeId(rs2.getString("ID"));
//				cb.setName(rs2.getString("NAME"));
				String Id=rs2.getString("ID");
				String name=rs2.getString("NAME");
//				if(CompentMaps.containsKey(Id)) {
//					CompentMaps.get(Id).add(cb);
//				}
//				else {
//					List<ElementNode> nodes=new ArrayList<ElementNode>();
//			        nodes.add(cb);
			        CompentMaps.put(Id,name);
//				}
			}
			//将混合类型中的元素从数据库中取出来，并放进map
			ResultSet rs3=st.executeQuery(sql_element);
			while(rs3.next()) {
				ElementNode cb=new ElementNode();
				String TypeId=rs3.getString("TYPE_ID");
				String CompentId=rs3.getString("MESSAGE_COMPONENT_ID");
				String type=rs3.getString("TYPE");
				if ("1".equals(type)) {
					List<ElementNode> list=DataTypeMaps.get(TypeId);
					if (list==null) {
						logger.warn("遍历element表时DataTypeMaps中没有找到相关数据"+TypeId);
					}
					if (list!=null) {
						for (ElementNode elementNode : list) {
					cb.setType(elementNode.getName());
					}
					}
					
				}else if("2".equals(type)){
					String name=CompentMaps.get(TypeId);
					if (name==null) {
						logger.warn("遍历element表时CompentMaps中没有找到相关数据"+TypeId);
					}
					if (name!=null) {
						cb.setType(name);
					}
					
				}else if("3".equals(type)){
					List<ElementNode> list=DataTypeMaps.get(TypeId);
					if (list==null) {
						logger.warn("遍历element表时DataTypeMaps中没有找到相关数据"+TypeId);
					}
					if (list!=null) {
						for (ElementNode elementNode : list) {
						cb.setType(elementNode.getName());
					}
					}
					
				}else if("4".equals(type)){
					String name=SchemaMaps.get(TypeId);
					if (name==null) {
						logger.warn("遍历element表时SchemaMaps中没有找到相关数据"+TypeId);
					}
					if (name!=null) {
						cb.setType(name);
					}
					
				}
				
				cb.setTypeId(rs3.getString("TYPE_ID"));
				//XSD元素呈现为name=rs1.XMLTAG type=NAME
				cb.setName(rs3.getString("XML_TAG"));
				
				cb.setDataType(rs3.getString("TYPE"));
				cb.setMaxOccurs(rs3.getString("MAX_OCCURS"));
				cb.setMinOccurs(rs3.getString("MIN_OCCURS"));
				String ComponentId=rs3.getString("MESSAGE_COMPONENT_ID");
				if(ComplexMaps.containsKey(ComponentId)) {
					ComplexMaps.get(ComponentId).add(cb);
				}
				else {
					List<ElementNode> nodes=new ArrayList<ElementNode>();
	        		nodes.add(cb);
	        		ComplexMaps.put(ComponentId,nodes);
				}
			}
			
			//将混合类型数据datatype从数据库中取出来，并放进map
			ResultSet rs4=st.executeQuery(sql_namespace);
			while (rs4.next()) {
				String name=rs4.getString("NAMESPACE");
				String fileName=rs4.getString("XML_TAG");
				String Type=rs4.getString("NAME");
				namespaces=name;
				ListToTree.fileName=fileName;
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
			ResultSet rs5=st.executeQuery(sql_topNode);
			while (rs5.next()) {
				ElementNode ob=new ElementNode();
//				String Id=rs3.getString("MESSAGE_ID");
				ob.setName(fileName_xmlTag);
				List<ElementNode> ls=new ArrayList<ElementNode>();
				ElementNode eb=new ElementNode();
				String name=fileName_xmlTag;
				eb.setName(rs5.getString("XML_TAG"));
				eb.setType(rs5.getString("NAME"));
				eb.setMaxOccurs(rs5.getString("MAX_OCCURS"));
				eb.setMinOccurs(rs5.getString("MIN_OCCURS"));
				ls.add(eb);
				ob.setList(ls);
		        complexnodes.add(ob);
		        //遍历集合，将下一层循环数据加入集合
				for (ElementNode elementNode : complexnodes) {
					ElementNode ee=new ElementNode();
					List<ElementNode> le=new ArrayList<ElementNode>();
					if(name.equals(elementNode.getName())) {
						ee.setName(rs5.getString("XML_TAG"));
						ee.setType(rs5.getString("NAME"));
						ee.setMaxOccurs(rs5.getString("MAX_OCCURS"));
						ee.setMinOccurs(rs5.getString("MIN_OCCURS"));
						le.add(ee);
						elementNode.getList().addAll(le);
					}
				}
				if ("1".equals(rs5.getString("DATA_TYPE"))) {
					
					ElementNode cb=new ElementNode();
					String typeId=rs5.getString("ID");
					//去简单类型表中取数据
					List<ElementNode> list_0=DataTypeMaps.get(typeId);
					for (ElementNode elementNode : list_0) {
						String type=elementNode.getType();
						if("1"==type) {
						List<ElementNode> list_1= ListToTree.queryToSimple(typeId);
						System.out.println("简单类型列表"+list_1+"====================");
						cb.setName(rs5.getString("NAME"));
						cb.setCodes(list_1);
						simplenodes.add(cb);
						}else {
						    //如果不是1，去dataTypemap中取数据，取数据
						    ElementNode en=new ElementNode();
						    ElementNode ln=new ElementNode();
						    List<ElementNode> lll=new ArrayList<ElementNode>();
						    en.setName(elementNode.getName());
						    ln.setType(elementNode.getType());
						    ln.setFractionDigits(elementNode.getFractionDigits());
						    ln.setTotalDigits(elementNode.getTotalDigits());
						    ln.setMaxLength(elementNode.getMaxLength());
						    ln.setMinLength(elementNode.getMinLength());
						    ln.setMinInclusive(elementNode.getMinInclusive());
						    ln.setMaxInclusive(elementNode.getMaxInclusive());
						    ln.setPattern(elementNode.getPattern());
						    ln.setMaxExclusive(elementNode.getMaxExclusive());
						    ln.setMinExclusive(elementNode.getMinExclusive());
						    lll.add(ln);
						    en.setCodes(lll);
						    simplenodes.add(en);
						    }
					}
					
				} else if("2".equals(rs5.getString("DATA_TYPE"))) {
					//表中取数据,根据datatype判断执行查询，datatype等于2，去Component表中取数据
					String dataTypeId=rs5.getString("ID");
					ElementNode cnode=new ElementNode();
					List<ElementNode> list=ListToTree.queryToElement(dataTypeId);
					cnode.setName(rs5.getString("NAME"));
					cnode.setList(list);
					complexnodes.add(cnode);
				 }
			
			}
			connection.commit();
			rs0.close();
			rs.close();
			rs1.close();
			rs2.close();
			rs3.close();
			rs4.close();
			rs5.close();
//			SimpleMaps.clear();
//			ComplexMaps.clear();
//			CompentMaps.clear();
//			DataTypeMaps.clear();
//			DataTypeNameMaps.clear();
//			SchemaMaps.clear();
		}  catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		st.close();
		connection.close();
		return null;
	}
	
	public static List<ElementNode> queryToSimple(String TypeID) {
//		String sql_simple="select t.name,t.code_name from ecore_code t where t.code_set_id='"+TypeID+"'";
		
		List<ElementNode> list= SimpleMaps.get(TypeID);
		//去重，加快运行速度
//		List list_sim=new ArrayList<>();
//		for (ElementNode object : list) {
//			ElementNode ob=new ElementNode();
//			String name=object.getName();
//			if (!list_sim.contains(name)) {
//			//ob.setName(object.getName());
//			ob.setCodes(list);
//			simplenodes.add(ob);
//			}
//			list_sim.add(name);
//		}
		
		return list;
	}
	
	public static List queryToElement(String ComponentId) {
		List<ElementNode> list=ComplexMaps.get(ComponentId);
		List list_com=new ArrayList<>();
		for (ElementNode complexBean : list) {
			
			String name=complexBean.getType();
			String typeId=complexBean.getTypeId();
			if("2".equals(complexBean.getDataType())){
				ElementNode nn=new ElementNode();
				String cc=complexBean.getTypeId();
				List<ElementNode> ls=ListToTree.queryToElement(cc);
				nn.setName(complexBean.getType());
				nn.setList(ls);
				complexnodes.add(nn);
			}else if("1".equals(complexBean.getDataType())){
			//type为1,去datatypemap中取数据
			List<ElementNode> list1=DataTypeMaps.get(typeId);
			for (ElementNode complexBean2 : list1) {
				ElementNode cn=new ElementNode();
				String names=complexBean2.getName();
				//如果datatypemap中type=1，则去simplemap中取数据
				if ("1".equals(complexBean2.getType())) {
				List<ElementNode> ll=ListToTree.queryToSimple(typeId);
				cn.setName(names);
				cn.setCodes(ll);
				simplenodes.add(cn);
			    }else {
			    //如果不是1，去dataTypemap中取数据，取数据
			    ElementNode en=new ElementNode();
			    ElementNode ln=new ElementNode();
			    List<ElementNode> lll=new ArrayList<ElementNode>();
			    en.setName(complexBean2.getName());
			    ln.setType(complexBean2.getType());
			    ln.setFractionDigits(complexBean2.getFractionDigits());
			    ln.setTotalDigits(complexBean2.getTotalDigits());
			    ln.setMaxLength(complexBean2.getMaxLength());
			    ln.setMinLength(complexBean2.getMinLength());
			    ln.setMinInclusive(complexBean2.getMinInclusive());
			    ln.setMaxInclusive(complexBean2.getMaxInclusive());
			    ln.setPattern(complexBean2.getPattern());
			    ln.setMaxExclusive(complexBean2.getMaxExclusive());
			    ln.setMinExclusive(complexBean2.getMinExclusive());
			    lll.add(ln);
			    en.setCodes(lll);
			    simplenodes.add(en);
			    }
			}
			}
		}
		return list;
	}
	
		
//	public static List queryToComponent(String dataTypeID) {
//		List<ElementNode> list=CompentMaps.get(dataTypeID);
//		for (ElementNode elementNode : list) {
//			//String name=elementNode.getName();
//			String c=elementNode.getTypeId();
//			if ("1".equals(elementNode.getComponentType())) {
//				ListToTree.queryToSimple(c);
//			
//			}else if("2".equals(elementNode.getComponentType())){
//				ElementNode ob=new ElementNode();
//				String cc=elementNode.getTypeId();
//				List list1=ListToTree.queryToElement(cc);
//				ob.setName(elementNode.getName());
//				ob.setType(elementNode.getXmlTag());
//				ob.setList(list1);
//				complexnodes.add(ob);
//			}
//		}
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
//		return list;
//	}
	public void ClearMap() {
		SimpleMaps.clear();
		ComplexMaps.clear();
		CompentMaps.clear();
		DataTypeMaps.clear();
		DataTypeNameMaps.clear();
		SchemaMaps.clear();
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
