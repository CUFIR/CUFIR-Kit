package com.cfets.cufir.s.xsd.util;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.xpath.DefaultXPath;

public class ParseUtil {
	public static Set<String>listNotFindName=new HashSet();
	//当前type查找出来的所有相关type定义
	public static Set<String>set=new HashSet();
	//第一次是全部的，removeall()以后是没用的type
	public static Set<String>setAll=new HashSet(); 
	
	public static Set<String>settoRemeave=new HashSet();
	
	public static boolean isEnd=false;
	
	public static String beginName="TXLifeResponse";
	
	public static void main(String[] args) throws DocumentException {
		SAXReader saxReader=new SAXReader();
		Document document=saxReader.read(new File("D"));
		saxReader.read(new File("D"));
		Element root=document.getRootElement();
		for (Iterator iter=root.elementIterator();
		iter.hasNext();)
		{
			Element e=(Element) iter.next();
			System.out.println(e.attributeValue("name"));
			setAll.add(e.attributeValue("name"));
		}
		Map<String, String>xmlMap=new HashMap();
		xmlMap.put("xsd", "http://www.w3.org/2001/XMLSchema");
		DefaultXPath xpath=new DefaultXPath("/xsd:schema/xsd:simpleType[@name='"+beginName
				+"']|/xsd:schema/xsd:complexType[@name='"+beginName+"']");
		xpath.setNamespaceURIs(xmlMap);
		Element e=(Element) xpath.selectNodes(document);
//		getName(e);
//		Iterator<String>iterator=set.iterator();
//		Iterator<String>iterator1=listNotFindName.iterator();
//		System.out.println("set.size()==="+set.size());
//		System.out.println("before remove setAll.size()==="+setAll.size());
//		removeAll();
//		System.out.println("after remove setAll.size()==="+setAll.size());
//		System.out.println("listNotFindName.size()==="+listNotFindName.size());
//		WriteSetToFile(setAll,"delete");
//		WriteSetToFile(set,"allsearchType");
//		WriteSetToFile(listNotFindName,"listNotFindName");
	}
	
	static Element getElementByName(String name)throws DocumentException{
		SAXReader saxReader=new SAXReader();
		Document document=saxReader.read(new File("D"));
		Map<String, String>xmlMap=new HashMap();
		xmlMap.put("xsd", "http://www.w3.org/2001/XMLSchema");
		DefaultXPath xpath=new DefaultXPath("/xsd:schema/xsd:simpleType[@name='"+beginName
				+"']|/xsd:schema/xsd:complexType[@name='"+beginName+"']");
		xpath.setNamespaceURIs(xmlMap);
		//Element e=
//		(Element)document.selectSingleNode("/schema/simpleType[@name='"+name+"'and
//				 namespace-
//				 uri()='http://www.w3.org/2001/XMLSchema']|/
//				schema/element[@name='"+name+"'and 
//				namespace-
//				uri()='http://www.w3.org/2001/XMLSchema']");
		return null;
	}

}
