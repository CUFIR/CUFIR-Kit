package org.cufir.s.xsd;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.cufir.s.xsd.bean.ElementBean;
import org.cufir.s.xsd.bean.XsdModel;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * 根据XsdModel生成Xsd文件
 */
public class XsdGenerator {
	
	private Document document;
	private Element header;
	private Element body;
	private Element end;
	private Element request;
	private Element root;
	@SuppressWarnings("unused")
	private Element preface;
	private XsdModel component;

	ArrayList<Object> complexList = new ArrayList<>();
	ArrayList<Object> simpleList = new ArrayList<>();
	
	/**
	 * 初始化XSD
	 */
	public XsdGenerator(XsdModel component) {

		this.component = component;
		document = DocumentHelper.createDocument();
		root = initRootElement();
		request = createRequest();
	}

	/**
	 * 初始化头部信息
	 */
	@SuppressWarnings("unused")
	private Element initRootElement() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
		String startTime = sdf.format(d);
		Document root1 = document.addComment("Generated by Cufir Programmer on " + startTime);
		Element root = document.addElement("xs:schema");
		root.addNamespace("xs", "http://www.w3.org/2001/XMLSchema");
		root.addAttribute("elementFormDefault", "qualified");
		root.addAttribute("targetNamespace", "urn:cufir:std:cufir:20022:tech:xsd:" + this.component.getNamespaces());
		return root;
	}

	/**
	 * 构造Body
	 */
	@SuppressWarnings("unused")
	private Element createRequest() {
		Element request = root.addElement("xs:element");
		request.addAttribute("name", "Document");
		request.addAttribute("type", "Document");
		return request;
	}

	public void addDocumentComplexType() {
		Element complexType = root.addElement("xs:complexType").addAttribute("name", "Document");
		Element sqeuence = complexType.addElement("xs:sequence");
		sqeuence.addElement("xs:element").addAttribute("name", this.component.getFileName())
				.addAttribute("type", this.component.getFileName_xmlTag());
	}
	
	/**
	 * 新增复合元素
	 */
	public void addTopType(String typeName, List<ElementBean> elementComponentNodes, List<ElementBean> elementDataTypeNodes) {
		if (!complexList.contains(typeName)) {
			Element complexType = root.addElement("xs:complexType");
			complexType.addAttribute("name", typeName);
			Element sqeuence = complexType.addElement("xs:sequence");
			// 添加元素
			ArrayList<Object> complex_list = new ArrayList<>();
			for (int i = 0; i < elementComponentNodes.size(); i++) {
				if (!complex_list.contains(elementComponentNodes.get(i).getName())) {
					Element ele = sqeuence.addElement("xs:element");
					ele.addAttribute("name", elementComponentNodes.get(i).getName());
					ele.addAttribute("type", elementComponentNodes.get(i).getType());
					ele.addAttribute("maxOccurs", elementComponentNodes.get(i).getMaxOccurs());
					ele.addAttribute("minOccurs", elementComponentNodes.get(i).getMinOccurs());
				}
				complex_list.add(elementComponentNodes.get(i).getName());
			}
			for (int i = 0; i < elementDataTypeNodes.size(); i++) {
				ElementBean elementBean = elementDataTypeNodes.get(i).getConmmonlyElements().get(0);
				if (!complex_list.contains(elementBean.getName())) {
					Element ele = sqeuence.addElement("xs:element");
					ele.addAttribute("name", elementBean.getName());
					ele.addAttribute("type", elementBean.getType());
					ele.addAttribute("maxOccurs", elementBean.getMaxOccurs());
					ele.addAttribute("minOccurs", elementBean.getMinOccurs());
				}
				complex_list.add(elementDataTypeNodes.get(i).getName());
			}
		}
		complexList.add(typeName);
	}
	
	public void addComplexType(String typeName, List<ElementBean> elementNodes) {
		if (!complexList.contains(typeName)) {
			Element complexType = root.addElement("xs:complexType");
			complexType.addAttribute("name", typeName);
			Element sqeuence = complexType.addElement("xs:sequence");
			// 添加元素
			ArrayList<Object> complex_list = new ArrayList<>();
			for (int i = 0; i < elementNodes.size(); i++) {
				if (!complex_list.contains(elementNodes.get(i).getName())) {
					Element ele = sqeuence.addElement("xs:element");
					ele.addAttribute("name", elementNodes.get(i).getName());
					ele.addAttribute("type", elementNodes.get(i).getType());
					ele.addAttribute("maxOccurs", elementNodes.get(i).getMaxOccurs());
					ele.addAttribute("minOccurs", elementNodes.get(i).getMinOccurs());
				}
				complex_list.add(elementNodes.get(i).getName());
			}
		}
		complexList.add(typeName);
	}

	/**
	 * 新增简单元素
	 */
	public void addSimpleType(String name, List<ElementBean> node) {
		if (!simpleList.contains(name)) {
			Element simpleType = root.addElement("xs:simpleType");
			simpleType.addAttribute("name", name);
			Element sqeuence = simpleType.addElement("xs:restriction");
			// 添加元素
			ArrayList<Object> list = new ArrayList<>();
			for (int i = 0; i < node.size(); i++) {
				// 去重，根据node里的值，给面板添加不同属性
				if (node.get(i).getCodeName() != null && !list.contains(node.get(i).getCodeName())) {
					sqeuence.addAttribute("base", "xs:string");
					Element ele = sqeuence.addElement("xs:enumeration");
					ele.addAttribute("value", node.get(i).getCodeName());
					list.add(node.get(i).getCodeName());
				} else {
					if ("2".equals(node.get(i).getType())) {
						sqeuence.addAttribute("base", "xs:string");
					} else if ("3".equals(node.get(i).getType())) {
						sqeuence.addAttribute("base", "xs:boolean");
					} else if ("4".equals(node.get(i).getType())) {
						sqeuence.addAttribute("base", "xs:boolean");
					} else if ("5".equals(node.get(i).getType())) {
						sqeuence.addAttribute("base", "xs:decimal");
					} else if ("6".equals(node.get(i).getType())) {
						sqeuence.addAttribute("base", "xs:decimal");
					} else if ("7".equals(node.get(i).getType())) {
						sqeuence.addAttribute("base", "xs:decimal");
					} else if ("8".equals(node.get(i).getType())) {
						sqeuence.addAttribute("base", "xs:decimal");
					} else if ("9".equals(node.get(i).getType())) {
						sqeuence.addAttribute("base", "xs:time");
					} else if ("10".equals(node.get(i).getType())) {
						sqeuence.addAttribute("base", "xs:base64Binary");
					} else if ("11".equals(node.get(i).getType())) {
						sqeuence.addAttribute("base", "xs:string");
					} else if ("12".equals(node.get(i).getType())) {
						sqeuence.addAttribute("base", "xs:string");
					}
					if (node.get(i).getFractionDigits() != null) {
						Element el_1 = sqeuence.addElement("xs:fractionDigits");
						el_1.addAttribute("value", node.get(i).getFractionDigits());
					}
					if (node.get(i).getTotalDigits() != null) {
						Element el_2 = sqeuence.addElement("xs:totalDigits");
						el_2.addAttribute("value", node.get(i).getTotalDigits());
					}
					if (node.get(i).getPattern() != null) {
						Element el_3 = sqeuence.addElement("xs:pattern");
						el_3.addAttribute("value", node.get(i).getPattern());
					}
					if (node.get(i).getMinLength() != null) {
						Element el_4 = sqeuence.addElement("xs:minLength");
						el_4.addAttribute("value", node.get(i).getMinLength());
					}
					if (node.get(i).getMaxLength() != null) {
						Element el_5 = sqeuence.addElement("xs:maxLength");
						el_5.addAttribute("value", node.get(i).getMaxLength());
					}
					if (node.get(i).getMaxExclusive() != null) {
						Element el_6 = sqeuence.addElement("xs:maxExclusive");
						el_6.addAttribute("value", node.get(i).getMaxExclusive());
					}
					if (node.get(i).getMinExclusive() != null) {
						Element el_7 = sqeuence.addElement("xs:minExclusive");
						el_7.addAttribute("value", node.get(i).getMinExclusive());
					}
					if (node.get(i).getMaxInclusive() != null) {
						Element el_8 = sqeuence.addElement("xs:maxInclusive");
						el_8.addAttribute("value", node.get(i).getMaxInclusive());
					}
					if (node.get(i).getMinInclusive() != null) {
						Element el_9 = sqeuence.addElement("xs:minInclusive");
						el_9.addAttribute("value", node.get(i).getMinInclusive());
					}

				}
			}
		}
		simpleList.add(name);
	}

	/**
	 * 获取头部
	 */
	@SuppressWarnings("rawtypes")
	private Element initHeader(String complexTypeName) {
		header.addAttribute("type", complexTypeName);
		List list = root.selectNodes("./xs:complexType[@name='" + complexTypeName + "']");
		if (list.size() == 0) {
			throw new RuntimeException("不存在复合类型\"" + complexTypeName + "\"请选择正确的复合类型名.");
		}
		return header;
	}

	/**
	 * 获得Body元素
	 */
	@SuppressWarnings("rawtypes")
	private Element initBody(String complexTypeName) {
		body.addAttribute("type", complexTypeName);
		List list = root.selectNodes("./xs:complexType[@name='" + complexTypeName + "']");
		if (list.size() == 0) {
			throw new RuntimeException("不存在复合类型元素\"" + complexTypeName + "\"请选择正确的复合类型名.");
		}
		return body;
	}

	/**
	 * 获得end元素
	 */
	@SuppressWarnings("rawtypes")
	private Element initEnd(String complexTypeName) {
		end.addAttribute("type", complexTypeName);
		List list = root.selectNodes("./xs:complexType[@name='" + complexTypeName + "']");
		if (list.size() == 0) {
			throw new RuntimeException("不存在复合类型元素\"" + complexTypeName + "\"请选择正确的复合类型名.");
		}
		return end;
	}

	/**
	 * 初始化Header与body
	 */
	public void initHeaderBody(String headerComplexTypeName, String bodyComplexTypeName, String endComplexTypeName) {
		initHeader(headerComplexTypeName);
		initBody(bodyComplexTypeName);
		initEnd(endComplexTypeName);
	}

	/**
	 * 改变元素复合类型名称
	 */
	@SuppressWarnings("rawtypes")
	public void changeElementTypeName(String xPathQuery, String complexTypeName) {
		List list = root.selectNodes(xPathQuery);
		if (list.size() == 0) {
			throw new RuntimeException("没有搜到" + xPathQuery + "路径下的任何元素.");
		}
		if (list.size() > 1) {
			throw new RuntimeException("在此路径下" + xPathQuery + "搜索到多个元素.");
		}
		Element element = (Element) list.get(0);
		element.addAttribute("type", complexTypeName);
	}

	/**
	 * 写出
	 */
	public void writeToDisk(String path) {
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			XMLWriter writer = new XMLWriter(new FileWriter(new File(path)), format);// "test.xsd"
			writer.write(document);
			System.out.println("生成XSD文件成功,请查看本地目录" + path);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
