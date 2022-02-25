package org.cufir.s.xsd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.cufir.s.xsd.bean.AttributeBean;
import org.cufir.s.xsd.bean.ComplexBean;
import org.cufir.s.xsd.bean.ElementBean;
import org.cufir.s.xsd.bean.SimpleBean;
import org.cufir.s.xsd.bean.TreeBean;
import org.cufir.s.xsd.bean.XsdModel;
import org.cufir.s.xsd.util.DerbyUtil;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * xsd文件
 * 1.按消息id查询数据库得到xsd的各个子元素列表
 * 2.将xsd各个子元素组装成xsd文件，并存到文件中
 * 3.将xsd文件解析成TreeBean的结构
 * @author tangmaoquan
 * @Date 2021年10月15日
 */
@SuppressWarnings("rawtypes")
public class XsdBuilder {

	private static Logger logger = Logger.getLogger(XsdBuilder.class);

	/**
	 * 将一个报文生成.xsd文件到指定目录下
	 * 
	 * @param msgId
	 * @param xsdFileDir
	 */
	public void build(String msgId, String xsdFileDir) throws Exception {
		XsdModel xm = buildeXsdModel(msgId);
		buildXsd(xm, xsdFileDir);
	}

	/**
	 * 通过报文的Id获得XsdModel
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unused")
	private XsdModel buildeXsdModel(String msgId) throws SQLException {
		XsdModel component = new XsdModel();
		component.clean();
		Connection connection = DerbyUtil.getConnection();
		// 连接失败,再次请求连接
		if (connection == null) {
			connection = DerbyUtil.getConnection(); // 获得数据库连接
		}
		Statement st = connection.createStatement();
		// 查询出所有报文信息并保存到缓存
		// 查出最外层的组件信息
		String sql_topNode = "select b.max_occurs,b.min_occurs,b.data_type,mc.name,mc.id,b.xml_tag "
				+ " from ecore_message_building_block b,ecore_message_component mc " + " where b.message_id='" + msgId
				+ "'" + " and mc.id=b.data_type_id";

		String sql_code = "select t.code_set_id, t.name,t.code_name from ecore_code t ";
		String sql_element = "select t.name,t.type,t.type_id,t.xml_tag,t.max_occurs,t.min_occurs,t.message_component_id "
				+ " from ecore_message_element t";
		String sql_component = "select c.id,c.name,c.component_type from ecore_message_component c ";
		String sql_dataType = "select * from ecore_data_type t";
		String sql_namespace = " select a.name,a.xml_tag,(a.business_area||'.'||a.MESSAGE_FUNCTIONALITY||'.'||a.FLAVOUR||'.'||a.VERSION) As NameSpace  "
				+ " FROM ECORE_MESSAGE_DEFINITION a " + " where a.id='" + msgId + "'";
		String sql_schema = "select s.name,s.id from ecore_external_schema s";
		
		// 查出最外层的基本数据信息
		String sql_topDataType = "select b.max_occurs,b.min_occurs,b.data_type,mc.name,mc.id,b.xml_tag from ecore_message_building_block b,ECORE_DATA_TYPE mc  where b.message_id='" + msgId+ "' and mc.id=b.data_type_id";

		try {
			// 将混合类型数据datatype从数据库中取出来，并放进map
			ResultSet rs0 = st.executeQuery(sql_schema);
			while (rs0.next()) {
				String name = rs0.getString("NAME");
				String Id = rs0.getString("ID");
				component.getSchemaMaps().put(Id, name);
			}
			// 将混合类型数据datatype从数据库中取出来，并放进map
			ResultSet rs = st.executeQuery(sql_dataType);
			while (rs.next()) {
				ElementBean cb = new ElementBean();
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
				String Id = rs.getString("ID");
				String name = rs.getString("NAME");
				component.getDataTypeNameMaps().put(Id, name);
				if (component.getDataTypeMaps().containsKey(Id)) {
					component.getDataTypeMaps().get(Id).add(cb);
				} else {
					List<ElementBean> nodes = new ArrayList<ElementBean>();
					nodes.add(cb);
					component.getDataTypeMaps().put(Id, nodes);
				}
			}
			// 将简单类型数据从数据库中取出来，并放进map
			ResultSet rs1 = st.executeQuery(sql_code);
			while (rs1.next()) {
				ElementBean sb = new ElementBean();
				sb.setName(rs1.getString("NAME"));
				sb.setCodeName(rs1.getString("CODE_NAME"));
				String codeSetId = rs1.getString("CODE_SET_ID");
				if (component.getSimpleMaps().containsKey(codeSetId)) {
					component.getSimpleMaps().get(codeSetId).add(sb);
				} else {
					List<ElementBean> nodes = new ArrayList<ElementBean>();
					nodes.add(sb);
					component.getSimpleMaps().put(codeSetId, nodes);
				}
			}
			// 将混合类型数据从数据库中取出来，并放进map
			ResultSet rs2 = st.executeQuery(sql_component);
			while (rs2.next()) {
				String Id = rs2.getString("ID");
				String name = rs2.getString("NAME");
				component.getCompentMaps().put(Id, name);
			}
			// 将混合类型中的元素从数据库中取出来，并放进map
			ResultSet rs3 = st.executeQuery(sql_element);
			while (rs3.next()) {
				ElementBean cb = new ElementBean();
				String TypeId = rs3.getString("TYPE_ID");
				String CompentId = rs3.getString("MESSAGE_COMPONENT_ID");
				String type = rs3.getString("TYPE");
				if ("1".equals(type)) {
					List<ElementBean> list = component.getDataTypeMaps().get(TypeId);
					if (list == null) {
						logger.warn("遍历element表时DataTypeMaps中没有找到相关数据" + TypeId);
					}
					if (list != null) {
						for (ElementBean elementNode : list) {
							cb.setType(elementNode.getName());
						}
					}

				} else if ("2".equals(type)) {
					String name = component.getCompentMaps().get(TypeId);
					if (name == null) {
						logger.warn("遍历element表时CompentMaps中没有找到相关数据" + TypeId);
					}
					if (name != null) {
						cb.setType(name);
					}

				} else if ("3".equals(type)) {
					List<ElementBean> list = component.getDataTypeMaps().get(TypeId);
					if (list == null) {
						logger.warn("遍历element表时DataTypeMaps中没有找到相关数据" + TypeId);
					}
					if (list != null) {
						for (ElementBean elementNode : list) {
							cb.setType(elementNode.getName());
						}
					}
				} else if ("4".equals(type)) {
					String name = component.getSchemaMaps().get(TypeId);
					if (name == null) {
						logger.warn("遍历element表时SchemaMaps中没有找到相关数据" + TypeId);
					}
					if (name != null) {
						cb.setType(name);
					}
				}
				cb.setTypeId(rs3.getString("TYPE_ID"));
				// XSD元素呈现为name=rs1.XMLTAG type=NAME
				cb.setName(rs3.getString("XML_TAG"));
				cb.setDataType(rs3.getString("TYPE"));
				cb.setMaxOccurs(rs3.getString("MAX_OCCURS"));
				cb.setMinOccurs(rs3.getString("MIN_OCCURS"));
				String ComponentId = rs3.getString("MESSAGE_COMPONENT_ID");
				if (component.getComplexMaps().containsKey(ComponentId)) {
					component.getComplexMaps().get(ComponentId).add(cb);
				} else {
					List<ElementBean> nodes = new ArrayList<ElementBean>();
					nodes.add(cb);
					component.getComplexMaps().put(ComponentId, nodes);
				}
			}

			// 将混合类型数据datatype从数据库中取出来，并放进map
			ResultSet rs4 = st.executeQuery(sql_namespace);
			while (rs4.next()) {
				String name = rs4.getString("NAMESPACE");
				String fileName = rs4.getString("XML_TAG");
				String Type = rs4.getString("NAME");
				component.setNamespaces(name);
				component.setFileName(fileName);
				component.setFileName_xmlTag(Type);

			}
			ResultSet rs5 = st.executeQuery(sql_topNode);
			while (rs5.next()) {
				ElementBean ob = new ElementBean();
				ob.setName(component.getFileName_xmlTag());
				List<ElementBean> ls = new ArrayList<ElementBean>();
				ElementBean eb = new ElementBean();
				String name = component.getFileName_xmlTag();
				eb.setName(rs5.getString("XML_TAG"));
				eb.setType(rs5.getString("NAME"));
				eb.setMaxOccurs(rs5.getString("MAX_OCCURS"));
				eb.setMinOccurs(rs5.getString("MIN_OCCURS"));
				ls.add(eb);
				ob.setList(ls);
				component.getComplexnodes().add(ob);
				// 遍历集合，将下一层循环数据加入集合
				for (ElementBean elementNode : component.getComplexnodes()) {
					ElementBean ee = new ElementBean();
					List<ElementBean> le = new ArrayList<ElementBean>();
					if (name.equals(elementNode.getName())) {
						ee.setName(rs5.getString("XML_TAG"));
						ee.setType(rs5.getString("NAME"));
						ee.setMaxOccurs(rs5.getString("MAX_OCCURS"));
						ee.setMinOccurs(rs5.getString("MIN_OCCURS"));
						le.add(ee);
						elementNode.getList().addAll(le);
					}
				}
				if ("1".equals(rs5.getString("DATA_TYPE"))) {
					ElementBean cb = new ElementBean();
					String typeId = rs5.getString("ID");
					// 去简单类型表中取数据
					List<ElementBean> list_0 = component.getDataTypeMaps().get(typeId);
					for (ElementBean elementNode : list_0) {
						String type = elementNode.getType();
						if ("1" == type) {
							List<ElementBean> list_1 = queryToSimple(typeId, component);
							System.out.println("简单类型列表" + list_1 + "====================");
							cb.setName(rs5.getString("NAME"));
							cb.setCodes(list_1);
							component.getSimplenodes().add(cb);
						} else {
							// 如果不是1，去dataTypemap中取数据，取数据
							ElementBean en = new ElementBean();
							ElementBean ln = new ElementBean();
							List<ElementBean> lll = new ArrayList<ElementBean>();
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
							component.getSimplenodes().add(en);
						}
					}
				} else if ("2".equals(rs5.getString("DATA_TYPE"))) {
					// 表中取数据,根据datatype判断执行查询，datatype等于2，去Component表中取数据
					String dataTypeId = rs5.getString("ID");
					ElementBean cnode = new ElementBean();
					@SuppressWarnings("unchecked")
					List<ElementBean> list = queryToElement(dataTypeId, component);
					cnode.setName(rs5.getString("NAME"));
					cnode.setList(list);
					component.getComplexnodes().add(cnode);
				}
			}
			ResultSet rs6 = st.executeQuery(sql_topDataType);
			while (rs6.next()) {
				ElementBean ob = new ElementBean();
				ob.setName(component.getFileName_xmlTag());
				List<ElementBean> ls = new ArrayList<ElementBean>();
				ElementBean eb = new ElementBean();
				eb.setName(rs6.getString("XML_TAG"));
				eb.setType(rs6.getString("NAME"));
				eb.setMaxOccurs(rs6.getString("MAX_OCCURS"));
				eb.setMinOccurs(rs6.getString("MIN_OCCURS"));
				ls.add(eb);
				ob.setList(ls);
				component.getDatas().add(ob);
			}
			connection.commit();
			rs0.close();
			rs.close();
			rs1.close();
			rs2.close();
			rs3.close();
			rs4.close();
			rs5.close();
			rs6.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		st.close();
		connection.close();
		System.out.println(component.getComplexnodes() + "==============" + component.getSimplenodes());
		return component;
	}

	private List<ElementBean> queryToSimple(String TypeID, XsdModel component) {
		List<ElementBean> list = component.getSimpleMaps().get(TypeID);
		return list;
	}

	@SuppressWarnings({ "unused", "unchecked" })
	private List queryToElement(String ComponentId, XsdModel component) {
		List<ElementBean> list = component.getComplexMaps().get(ComponentId);
		List list_com = new ArrayList<>();
		for (ElementBean complexBean : list) {

			String name = complexBean.getType();
			String typeId = complexBean.getTypeId();
			if ("2".equals(complexBean.getDataType())) {
				ElementBean nn = new ElementBean();
				String cc = complexBean.getTypeId();
				List<ElementBean> ls = queryToElement(cc, component);
				nn.setName(complexBean.getType());
				nn.setList(ls);
				component.getComplexnodes().add(nn);
			} else if ("1".equals(complexBean.getDataType())) {
				// type为1,去datatypemap中取数据
				List<ElementBean> list1 = component.getDataTypeMaps().get(typeId);
				for (ElementBean complexBean2 : list1) {
					ElementBean cn = new ElementBean();
					String names = complexBean2.getName();
					// 如果datatypemap中type=1，则去simplemap中取数据
					if ("1".equals(complexBean2.getType())) {
						List<ElementBean> ll = queryToSimple(typeId, component);
						cn.setName(names);
						cn.setCodes(ll);
						component.getSimplenodes().add(cn);
					} else {
						// 如果不是1，去dataTypemap中取数据，取数据
						ElementBean en = new ElementBean();
						ElementBean ln = new ElementBean();
						List<ElementBean> lll = new ArrayList<ElementBean>();
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
						component.getSimplenodes().add(en);
					}
				}
			}
		}
		return list;
	}

	private boolean buildXsd(XsdModel component, String path) {
		XsdGenerator generate = new XsdGenerator(component);
		/**
		 * 一：初始化xsd的头部信息 1.初始化xsd标签头部信息(schema) 2.初始化xsd element document
		 * 3.获取查询结果，获取报文信息，构建document节点信息，complexType 信息，name(报文名) ElementNode LIST
		 * 4.构建报文节点下面的header（block）信息 complexType ElementNode list
		 * 
		 * 二：获取复合和简单类型的数据 a.遍历complexnoDE,构建复合类型的数据complexType List<ElementNode>
		 * b.遍历simplenoDE,构建简单类型的数据SimpleType（包含code信息） List<ElementNode>
		 */
		generate.addDocumentComplexType();
		int i = 0;
		for (ElementBean node : component.getComplexnodes()) {
			List<ElementBean> ls = node.getList();
			if (ls == null) {
				logger.warn("ls没有数据");
			}
			if(i == 0) {
				generate.addTopType(node.getName(), node.getList(), component.getDatas());
				i++;
			}else {
				generate.addComplexType(node.getName(), node.getList());
			}
		}
		for (ElementBean node : component.getSimplenodes()) {
			List list11 = node.getCodes();
			if (list11 == null) {
				logger.warn("list11没有数据");
			}
			if (list11 != null) {
				generate.addSimpleType(node.getName(), node.getCodes());
			}
		}
		generate.writeToDisk(path);
		return true;
	}
	
	
	@SuppressWarnings({ "unchecked", "unused" })
	private TreeBean parse(String xsdFilePath) throws Exception {
		TreeBean tree = new TreeBean();
		Map<String, Object> xmlTypeMap = tree.getXmlTypeMap();
		Map<Object, Object> xmlCompTypeMap = tree.getXmlCompTypeMap();
		String realPath = "E:\\XSchema";
		File f = new File(xsdFilePath);
		if (!f.exists()) {
			System.out.println("文件不存在");
		}
		// 将文件从目录中读取出来，并存入字节数组
		FileInputStream InSteam = new FileInputStream(f);
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int len = 0;
		while ((len = InSteam.read(b)) != -1) {
			outSteam.write(b, 0, len);
		}
		System.out.println(b);
		System.out.println("===ok");
		InSteam.close();
		outSteam.close();
		// 将字节数组中的内容转化成字符串形式输出
		String strxml = new String(outSteam.toByteArray(), "utf-8");
		System.out.println(strxml);
		strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
		if (null == strxml || "".equals(strxml)) {
			return null;
		}
		InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(in);
		Element root = doc.getRootElement();
		Map<String, Object> map = new HashMap<String, Object>();
		/**
		 * 2.test
		 */
		List<Element> list = root.getChildren();
		List<ComplexBean> complexnodes = new ArrayList<ComplexBean>();
		List<SimpleBean> simplenodes = new ArrayList<SimpleBean>();
		for (org.jdom.Element e : list) {
			String type = e.getName();
			if ("complexType".equals(type)) {
				ComplexBean cBean = new ComplexBean();
				System.out.println(e.getName() + ":  " + e.getAttributeValue("name"));
				cBean.setName(e.getAttributeValue("name"));
				xmlTypeMap.put(e.getAttributeValue("name"), cBean);
				System.out.println("");
			} else if ("simpleType".equals(type)) {
				SimpleBean s = new SimpleBean();
				s.setName(e.getAttributeValue("name"));
				System.out.println(e.getName() + ":" + e.getAttributeValue("name"));
				xmlTypeMap.put(e.getAttributeValue("name"), s);
				System.out.println();
			}
		}
		for (Element e : list) {
			String type = e.getName();
			if ("complexType".equals(type)) {
				ComplexBean complexBean = (ComplexBean) xmlTypeMap.get(e.getAttributeValue("name"));
				System.out.println(e.getName() + " :   " + e.getAttributeValue("name") + "======================");
				if (e.getChildren() != null) {
					List<Element> childs = e.getChildren();
					for (Element ce : childs) {
						List<Element> childss = ce.getChildren();
						if (!childss.isEmpty()) {
							for (Element cee : childss) {
								Object obj = xmlTypeMap.get(cee.getAttributeValue("type"));
								if (obj instanceof ComplexBean) {
									ComplexBean child = new ComplexBean();
									xmlCompTypeMap.put(obj, complexBean.getComplexes());
									child.setName(cee.getAttributeValue("name"));
									child.setType(cee.getAttributeValue("type"));
									child.setMinOccurs(cee.getAttributeValue("minOccurs"));
									child.setMaxOccurs(cee.getAttributeValue("maxOccurs"));
									complexBean.getComplexes().add(child);
									System.out.println(cee.getName() + " :   " + cee.getAttributeValue("name") + " "
											+ cee.getAttributeValue("type") + " " + cee.getAttributeValue("minOccurs")
											+ " " + cee.getAttributeValue("maxOccurs"));
								} else if (obj instanceof SimpleBean) {
									SimpleBean child = new SimpleBean();
									child.setName(cee.getAttributeValue("name"));
									complexBean.getSimples().add(child);
								}
							}
						}
					}
				}

				complexnodes.add(complexBean);
				System.out.println("");
			} else if ("simpleType".equals(type)) {
				SimpleBean s = (SimpleBean) xmlTypeMap.get(e.getAttributeValue("name"));
				s.setName(e.getAttributeValue("name"));
				System.out.println(e.getName() + ":" + e.getAttributeValue("name"));
				if (e.getChildren() != null) {
					List<Element> childs = e.getChildren();
					for (Element ce : childs) {
						String attributeVal = ce.getAttributeValue("base");
						String dType = attributeVal.substring(attributeVal.indexOf("xs:") + 3);
						AttributeBean attri = new AttributeBean();
						attri.setName("type");
						attri.setDefaultvalue(dType);
						s.getAttributes().add(attri);
						List<org.jdom.Element> childss = ce.getChildren();
						for (org.jdom.Element cee : childss) {
							s.setValue(cee.getAttributeValue("value"));
							System.out.println(cee.getName() + ":" + cee.getAttributeValue("name"));
							AttributeBean attr = new AttributeBean();
							attr.setName(cee.getName());
							attr.setDefaultvalue(cee.getAttributeValue("value"));
							s.getAttributes().add(attr);
						}
					}
				}
				simplenodes.add(s);
				System.out.println();
			}
		}
		in.close();
		map.put("1", complexnodes);
		map.put("2", simplenodes);
		map.put("4", xmlTypeMap);
		Set<String> set = xmlTypeMap.keySet();
		Iterator<String> iter = set.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			extracted(key, tree);
		}
		List<ComplexBean> complexBeans = new ArrayList<>();
		List<SimpleBean> simpleBeans = new ArrayList<>();
		Set<String> set4 = xmlTypeMap.keySet();
		Iterator<String> iter4 = set.iterator();
		while (iter4.hasNext()) {
			String key = iter4.next();
			printNode(key, tree);
			Object obj = xmlTypeMap.get(key);
			if (obj instanceof ComplexBean) {
				complexBeans.add((ComplexBean) obj);
			} else if (obj instanceof SimpleBean) {
				simpleBeans.add((SimpleBean) obj);
			}
		}
		tree.setComplexBeans(complexBeans);
		tree.setSimpleBeans(simpleBeans);
		return tree;
	}

	private void extracted(String key, TreeBean tree) {
		Object val = tree.getXmlTypeMap().get(key);
		if (val instanceof ComplexBean) {
			ComplexBean v = (ComplexBean) val;
			List<ComplexBean> listC = v.getComplexes();
			List<SimpleBean> list4 = v.getSimples();
			for (ComplexBean bean : listC) {
				System.out.println(bean.getName() + ":----------" + bean.getType());
				extractedComp(bean.getType(), bean, tree);
			}
			for (SimpleBean simple : list4) {
				System.out.println("------------" + simple.getName());
			}
		}
		if (val instanceof SimpleBean) {
			SimpleBean simB = (SimpleBean) val;
			System.out.println("------------" + simB.getName());
		}
	}

	private void printNode(String key, TreeBean tree) {
		Object val = tree.getXmlTypeMap().get(key);
		if (val instanceof ComplexBean) {
			ComplexBean v = (ComplexBean) val;
			List<ComplexBean> listC = v.getComplexes();
			List<SimpleBean> list4 = v.getSimples();
			for (ComplexBean bean : listC) {
				System.out.println(bean.getName() + ":----------" + bean.getType());
				printNode(bean.getType(), tree);
			}
			for (SimpleBean simple : list4) {
				System.out.println("------------" + simple.getName());
			}
		}
		if (val instanceof SimpleBean) {
			SimpleBean simB = (SimpleBean) val;
			System.out.println("------------" + simB.getName());
		}
	}

	private void extractedComp(String key, ComplexBean parent, TreeBean tree) {
		Object val = tree.getXmlTypeMap().get(key);
		if (val instanceof ComplexBean) {
			ComplexBean v = (ComplexBean) val;
			List<ComplexBean> listC = v.getComplexes();
			List<SimpleBean> list4 = v.getSimples();
			for (ComplexBean bean : listC) {
				System.out.println(bean.getName() + ":----------" + bean.getType());
				extractedComp(bean.getType(), bean, tree);
			}
			for (SimpleBean simple : list4) {
				System.out.println("------------" + simple.getName());
			}
			parent.setComplexes(listC);
			parent.setSimples(list4);
		}
		if (val instanceof SimpleBean) {
			SimpleBean simB = (SimpleBean) val;
			System.out.println("------------" + simB.getName());
		}
	}

	public static void main(String[] args) throws Exception {
		XsdBuilder builder = new XsdBuilder();
		String id = "ec2b2582-6bca-475e-a4c5-ed83d82338e2";
		String path = "D:\\cufir\\1.xsd";
		builder.build(id, path);
	}
}
