package org.cufir.s.ide;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

/**
 * 根据Dom4j API实现XML文件的处理
 */
public final class XmlUtil {

	private static Logger logger = Logger.getLogger(XmlUtil.class);

	/**
	 * 获得某一节点下的子节点
	 */
	@SuppressWarnings("unchecked")
	public static List<Element> findSubElements(Element element, String tagName) {
		try {
			return element.elements(tagName);
		} catch (Exception e) {
			logger.warn(e);
			return new ArrayList<Element>();
		}
	}

	/**
	 * 根据InputStream获得XML的Document对象实例
	 * @param input
	 * @return
	 */
	public static Document createDocument(InputStream input) {
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(input);
			return document;
		} catch (Exception e) {
			logger.warn(e);
			return null;
		}
	}

	/**
	 * 通过一个对象，获取一个Element,将其属性和value进行键值对显示
	 * @param bean
	 * @return
	 */
	public static Element createElememnt(Object bean) {
		try {
			Element ele = DocumentHelper.createElement(bean.getClass()
					.getSimpleName().toLowerCase());
			PropertyDescriptor[] descriptors = PropertyUtils
					.getPropertyDescriptors(bean);
			for (int i = 0; i < descriptors.length; i++) {
				String name = descriptors[i].getName();
				Element fieldEle = DocumentHelper.createElement(name);
				String value = "";
				try {
					if (CLASS_STR.equals(name)) {
						value = bean.getClass().getName();
					} else {
						Object obj = PropertyUtils
								.getSimpleProperty(bean, name);
						if (obj != null) {
							value = obj.toString();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				fieldEle.addText(value);
				ele.add(fieldEle);
			}
			return ele;
		} catch (Exception e) {
			logger.warn(e);
			return null;
		}
	}

	/**
	 * 依据键值对创建Element
	 * @param key
	 * @param value
	 * @return
	 */
	public static Element createElememnt(String key, String value) {
		Element ele = DocumentHelper.createElement(key);
		if (value != null) {
			ele.add(DocumentHelper.createText(value));
		}
		return ele;
	}

	/**
	 * 依据Key创建空的Element
	 * @param key
	 * @return
	 */
	public static Element createElememnt(String key) {
		Element ele = DocumentHelper.createElement(key);
		return ele;
	}

	/**
	 * 在指定Element上添加Attribute
	 * @param ele
	 * @param name
	 * @param value
	 * @return
	 */
	public static Element addAttribute(Element ele, String name, String value) {
		ele.addAttribute(name, value);
		return ele;
	}

	/**
	 * 获得Element属性值
	 */
	public static String getAttributeValue(Element ele, String name) {
		return getAttrNotNullValue(ele, name);
	}

	/**
	 * 获得Element属性值,如果没有获得，返回""
	 */
	public static String getAttrNotNullValue(Element ele, String name) {
		String value = "";
		try {
			value = ele.attribute(name).getStringValue();
			if (value == null) {
				value = "";
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return value;
	}

	/**
	 * 获得Element属性值，非null
	 */
	public static String getNotNullAttrValue(Element ele, String name) {
		String v = getAttributeValue(ele, name);
		if (v == null) {
			v = "";
		}
		return v;
	}

	/**
	 * 创建基于Root的空的Document对象
	 * @param root
	 * @return
	 */
	public static Document createDocument(String root) {
		Document doc = DocumentHelper.createDocument();
		doc.addElement(root);
		return doc;
	}

	/**
	 * 解析一个XML文件，获取Dom对象
	 * @param xml
	 * @return
	 */
	public static Document createDocument(File xml) {
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(xml);
			return document;
		} catch (Exception e) {
			logger.warn(e);
			return null;
		}
	}

	public static Document createDocumentByXml(String xml) {
		try {
			return DocumentHelper.parseText(xml);
		} catch (Exception e) {
			logger.warn(e);
			return null;
		}
	}

	/**
	 * 创建基于对象的Document对象
	 * @param root
	 * @param bean
	 * @return
	 */
	public static Document createDocument(String root, Object bean) {
		Document doc = XmlUtil.createDocument(root);
		Element ele = XmlUtil.createElememnt(bean);
		doc.getRootElement().add(ele);
		return doc;
	}
	
	private final static String CLASS_STR = "class";
	
	/**
	 * 依据一个对象,专门的获得一个对象
	 */
	public static Document createObjectDocument(String root, Object bean) {
		Document doc = XmlUtil.createDocument(root);
		PropertyDescriptor[] descriptors = PropertyUtils
				.getPropertyDescriptors(bean);
		for (int i = 0; i < descriptors.length; i++) {
			String name = descriptors[i].getName();
			Element fieldEle = DocumentHelper.createElement(name);
			try {
				if (CLASS_STR.equals(name)) {
					// Do nothing
				} else {
					String value = "";
					Object obj = PropertyUtils.getSimpleProperty(bean, name);
					if (obj != null) {
						value = obj.toString();
					}
					fieldEle.addText(value);
					doc.getRootElement().add(fieldEle);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return doc;
	}

	/**
	 * 根据xPath，在XML文本中，获得指定xPath的元素 xPath://rootName/elementName
	 * @param xml
	 * @param xPath
	 * @return
	 */
	public static List<Element> findNodes(String xml, String xPath) {
		try {
			Document doc = DocumentHelper.parseText(xml);
			return findNodes(doc, xPath);
		} catch (Exception e) {
			logger.warn(e);
			return new ArrayList<Element>();
		}
	}

	/**
	 * 根据Doc对象获得指定的xPath节点
	 */
	@SuppressWarnings("unchecked")
	public static List<Element> findNodes(Document doc, String xPath) {
		try {
			return (List<Element>) doc.selectNodes(xPath);
		} catch (Exception e) {
			logger.warn(e);
			return new ArrayList<Element>();
		}
	}

	/**
	 * 根据Doc对象获得指定的xPath节点
	 */
	public static List<Element> findNodes(File fileXml, String xPath) {
		try {
			Document doc = createDocument(fileXml);
			return findNodes(doc, xPath);
		} catch (Exception e) {
			logger.warn(e);
			return new ArrayList<Element>();
		}
	}

	/**
	 * 获得指定单一节点的text值
	 */
	public static String getNodeText(String xml, String xPath) {
		try {
			Document doc = DocumentHelper.parseText(xml);
			return getNodeText(doc, xPath);
		} catch (Exception e) {
			logger.warn(e);
			return null;
		}
	}

	/**
	 * 获得指定单一节点的text值，参见getSingleNodeText
	 */
	public static String getNodeText(Document doc, String xPath) {
		return getSingleNodeText(doc, xPath);
	}

	/**
	 * 获得单一节点元素的值
	 */
	public static String getSingleNodeText(Document doc, String xPath) {
		try {
			List<?> list = doc.selectNodes(xPath);
			Element e = (Element) list.get(0);
			return e.getTextTrim();
		} catch (Exception e) {
			logger.warn(e);
			return null;
		}
	}

	/**
	 * 获得单一节点元素的值,需要命名空間 //pom:project/pom:artifactId Map<String,String> map=new
	 * HashMap<String,String>();
	 * map.put("pom","http://maven.apache.org/POM/4.0.0");
	 * 
	 */
	public static String getSingleNodeText(Object inode, String xPath,
			Map<String, String> ns) {
		try {
			XPath path = DocumentHelper.createXPath(xPath);
			path.setNamespaceURIs(ns);
			Element e = (Element) path.selectSingleNode(inode);
			return e.getTextTrim();
		} catch (Exception e) {
			logger.warn(e);
			return null;
		}
	}

	/**
	 * 在某一个Node节点下寻找子节点
	 */
	@SuppressWarnings("unchecked")
	public static List<Element> findNodes(Object inode, String xPath,
			Map<String, String> ns) {
		try {
			XPath path = DocumentHelper.createXPath(xPath);
			path.setNamespaceURIs(ns);
			return (List<Element>) path.selectNodes(inode);
		} catch (Exception e) {
			logger.warn(e);
			return new ArrayList<Element>();
		}
	}

	/**
	 * 作为createObjectDocument的逆运算,将对应的xml,转化成为Class的实例 不完全转换,慎重使用
	 */
	public static Object convert2Object(String xml, String root, Class<?> c) {
		try {
			Document doc = DocumentHelper.parseText(xml);
			Object instance = c.newInstance();
			PropertyDescriptor[] descriptors = PropertyUtils
					.getPropertyDescriptors(c);
			for (int i = 0; i < descriptors.length; i++) {
				try {
					String name = descriptors[i].getName();
					String xpath = "//" + root + "/" + name;
					Object value = getNodeText(doc, xpath);
					PropertyUtils.setSimpleProperty(instance, name, value);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			return instance;
		} catch (Exception e) {
			logger.warn(e);
			return null;
		}
	}

	/**
	 * 将Dom对象，转化成为XML文件
	 * @param doc
	 * @return
	 */
	public static String xml(Document doc) {
		return doc.asXML();
	}

	/**
	 * 根据object生成xmi格式数据的Element
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public static Element createXmiElement(Object bean) throws Exception {
		Element element = DocumentHelper.createElement(bean.getClass()
				.getSimpleName().toLowerCase());
		PropertyDescriptor[] descriptors = PropertyUtils
				.getPropertyDescriptors(bean);
		for (int i = 0; i < descriptors.length; i++) {
			String name = descriptors[i].getName();
			String value = "";
			if (CLASS_STR.equals(name)) {
				value = bean.getClass().getName();
			} else {
				Object obj = PropertyUtils
						.getSimpleProperty(bean, name);
				if (obj != null) {
					value = obj.toString();
				}
				element.addAttribute(name, value);
			}
		}
		return element;
	}
	
	/**
	 * 创建根元素
	 * @return
	 */
	public static Element createRoot() {
		Element root=XmlUtil.createElememnt("xmi:XMI");
		root.addAttribute("xmi:version", "2.0");
		root.addAttribute("xmlns:xmi", "http://www.omg.org/XMI");
		root.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		return root;
	}
}
