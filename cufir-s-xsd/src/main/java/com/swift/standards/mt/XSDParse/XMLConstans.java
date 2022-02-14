package com.swift.standards.mt.XSDParse;

public class XMLConstans {
	//默认数据节点,设置为空则认为根目录为默认数据节点
	public static final String MESSAGE="Features";
	
	//xml编码
	public static final String ENCODING="UTF-8";
	
	//xsd默认命名空间,设置为空则没有默认命名空间
	public static final String XSD_DEFAULT_NAMESPACE="xs";
	
	//xsd默认数据节点,设置为空则认为根目录为默认数据节点
	public static final String XSD_DEDAULT="Features";

	//xsd复合类型节点
	public static final String XSD_COMPLEX_TYPE="complexType";
	
	//xsd序列节点
	public static final String XSD_SEQUENCE="sequence";
	
	//xsd元素节点
	public static final String XSD_ELEMENT="elment";
	
	//xsd注解节点
	public static final String XSD_ANNOTATION="annotation";
	
	//xsd注解文档节点
	public static final String XSD_DOCUMENTATION="documentation";
	
	//xsd简单类型节点
	public static final String XSD_SIMPLE_TYPE="simpleType";
	
	//xsd限制节点
	public static final String XSD_RESTRICTION="restriction";
	
	//xsd name属性
	public static final String XSD_ATTRIBUTE_NAME="name";
	
	//xsd type属性
	public static final String XSD_ATTRIBUTE_TYPE="type";
	
	//xsd base属性
	public static final String XSD_ATTRIBUTE_BASE="base";
			
	//用来描述xsd中的unbounded节点信息
	public static final String XSD_UNBOUNDED="[unbounded]";
	
	public static final String XSD_UNBOUNDED_REPLATE="\\[unbounded\\]";
	
	public static final String XSD_ELEMENT_FOREACH="for-each";
	
	public static final String XSD_ELEMENT_SELECT="select";
	
	/** **********创建xslt基础变量配置*********** **/
	public static final String STYLESHEET="stylesheet";
	
	public static final String VERSION="version";
	
	public static final String VERSIONNUM="1.0";
	
	public static final String NAMESPACE="xsl";
	
	public static final String NAMESPACEADDRESS="http://www.w3.org/1999/XSL/Transform";
	
	public static final String TEMPLATE="template";
	
	public static final String MATCH="match";
	
	public static final String APPLYTEMPLATES="apply-templates";
	
	public static final String VALUEOF="value-of";
	
	public static final String SELECT="select";
	
	public static final String XSMLENCODING="UTF-8";
	
	public static final String ROOTSPER="/";
	
	public static final String DOUBELROOTSPER="//";
	
	public static final String SPER=":";
}
