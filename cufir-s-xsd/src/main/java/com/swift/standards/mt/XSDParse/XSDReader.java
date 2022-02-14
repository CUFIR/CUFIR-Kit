package com.swift.standards.mt.XSDParse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;


public class XSDReader {
	private List<XSDNode>list=new ArrayList<XSDNode>();
	/**
	 * 解析XSD,返回数据节点对象列表
	 * @throws Exception 
	 */
	public List<XSDNode>paserXSD(String xsd) throws Exception{
		SAXReader saxReader=new SAXReader();
//		ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream
//				(xsd.getBytes(BaseConstans.XMLENCODING));
		Document doc=saxReader.read(xsd);
		Element element=doc.getRootElement();
		//Map<String,Element>map=new HashMap<String, Element>();
		
		
		String basePath=null;
		Element dataElement=null;
		if ("".equals(XMLConstans.XSD_DEFAULT_NAMESPACE)) {
			if ("".equals(XMLConstans.MESSAGE)) {
				dataElement=element;
			}else {
				basePath="//element[@name=\'"+XMLConstans.MESSAGE+"'\"]";
				dataElement=(Element)element.selectSingleNode(basePath);
			}
		}else {
			basePath="//"+XMLConstans.XSD_DEFAULT_NAMESPACE+":element[@name=\'"+XMLConstans.MESSAGE+"'\"]";
			dataElement=(Element)element.selectSingleNode(basePath);
		}
		String elementPath=null;
		if ("".equals(XMLConstans.XSD_DEFAULT_NAMESPACE)) {
			elementPath="//element";
		}else {
			elementPath="//"+XMLConstans.XSD_DEFAULT_NAMESPACE+":element";
		}
		paseData(dataElement,"//",elementPath,"//");
		return list;
		
	}
	/**
	 * 转换XSD的数据节点，生成XSDNode对象
	 */
	public void paseData(Element element,String xPath,String xsdPath,String unboundedXpath) {
		if (element==null)return;
		//获取节点name属性
		String nodeName=element.attributeValue("name");
		//组装xml文档中节点的XPath
		xPath+=nodeName;
		unboundedXpath+=nodeName;
		//并列多节点限制属性
		String maxOccurs=element.attributeValue("maxOccurs");
		if (maxOccurs!=null&&!"1".equals(maxOccurs)&&!("//"+XMLConstans.MESSAGE+"").equals(xPath)) {//节点可以有多个
			unboundedXpath+=XMLConstans.XSD_UNBOUNDED;
		}
		//组装下一个element元素的xPath
		String currentXsdPath=xsdPath+"[@name=\'"+nodeName+"'\"]"+"/"+XMLConstans.XSD_DEFAULT_NAMESPACE
				+":complexType/"+XMLConstans.XSD_DEFAULT_NAMESPACE+":sequence/"+XMLConstans.XSD_DEFAULT_NAMESPACE
				+":element";
		//查找该节点下所有的element元素
		List<Node>elementNodes=element.selectNodes(currentXsdPath);
		if (elementNodes!=null&&elementNodes.size()>0) {//如果下面还有element,说明不是根节点
			Iterator<Node>nodes=elementNodes.iterator();
			while (nodes.hasNext()) {
				if (!xPath.endsWith("/")) {
					xPath+="/";
					unboundedXpath+="/";
				}
				Element ele=(Element)nodes.next();
				paseData(ele, xPath, currentXsdPath, unboundedXpath);
			}
		}else {//该element为根节点
			XSDNode xsdNode=new XSDNode();
			//获取注释节点
			String annotation="";
			Node annotationNode=element.selectSingleNode(xsdPath+"[@name=\""+nodeName+"\"]/"+
			XMLConstans.XSD_DEFAULT_NAMESPACE+":annottation/"+XMLConstans.XSD_DEFAULT_NAMESPACE+
			":documentation");
			if (annotationNode!=null) 
			annotation=annotationNode.getText();
			//获取节点类型属性
			String nodeType="";
			Attribute type=element.attribute("type");
			if (type!=null) {
				nodeType=type.getText();
			}else {
				String spath=xsdPath+"[@name=\'"+nodeName+"'\"]/"+XMLConstans.XSD_DEFAULT_NAMESPACE+".simpleType/"
				+XMLConstans.XSD_DEFAULT_NAMESPACE+":restriction";
				Element typeNode=(Element)element.selectSingleNode(spath);
				if (typeNode!=null) {
					Attribute base=typeNode.attribute("base");
					if (base!=null) 
						nodeType=base.getText();
					}
				}
				xsdNode.setName(nodeName);
				xsdNode.setxPath(xPath);
				xsdNode.setAnnotation(annotation);
				xsdNode.setType(nodeType);
				xsdNode.setUnboundedXpath(unboundedXpath);
				list.add(xsdNode);
			}
				
		}
	
	public static void main(String[] args) {
		try {
			String realPath=XSDReader.class.getResource("/").getPath();
			XSDReader xsdReader=new XSDReader();
			List<XSDNode>nodes=xsdReader.paserXSD(realPath+"/LightEditor_caaa.xsd");
			for (XSDNode node : nodes) {
				System.out.println(node.getUnboundedXpath()+"ok");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

