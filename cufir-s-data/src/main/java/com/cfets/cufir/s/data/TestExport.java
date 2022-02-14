package com.cfets.cufir.s.data;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.cfets.cufir.s.data.util.XmlUtil;
import com.cfets.cufir.s.data.vo.web.EcoreBusinessAreaVo;
import com.cfets.cufir.s.data.vo.web.EcoreCodeVo;
import com.cfets.cufir.s.data.vo.web.EcoreConstraintVo;
import com.cfets.cufir.s.data.vo.web.EcoreDataTypeVo;
import com.cfets.cufir.s.data.vo.web.EcoreExampleVo;
import com.cfets.cufir.s.data.vo.web.EcoreMessageBuildingBlockVo;
import com.cfets.cufir.s.data.vo.web.EcoreMessageComponentVo;
import com.cfets.cufir.s.data.vo.web.EcoreMessageDefinitionVo;
import com.cfets.cufir.s.data.vo.web.EcoreMessageElementVo;
import com.cfets.cufir.s.data.vo.web.EcoreMessageSetDefinitionRLVo;
import com.cfets.cufir.s.data.vo.web.EcoreMessageSetVo;
import com.cfets.cufir.s.data.vo.web.EcoreNextVersionsVo;
import com.cfets.cufir.s.data.vo.web.EcoreSemanticMarkupElementVo;
import com.cfets.cufir.s.data.vo.web.EcoreSemanticMarkupVo;
import com.cfets.cufir.s.data.vo.web.RemovedObjectVo;

public class TestExport {
	
	private final static String CLASS_STR = "class";

	public static void main(String[] args) {
		try {
			//testexport();
			//testfind();
			//测试生成数据
			MessageDataMgr mgr=new MessageDataMgr();
			mgr.generateMessageDataXML("68ae6e01-f539-4163-afe0-7ab5f236508a",
					"E:\\报文\\20200325\\release\\20200326\\"
					+ "4.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void testexport() throws IOException {
		File xml=new File("E:\\报文\\cufir_data.xml");
		Document d=DocumentHelper.createDocument();
		Element rootElement=createRoot();
		d.setRootElement(rootElement);
		
		EcoreBusinessAreaVo bean=new EcoreBusinessAreaVo();
		
		EcoreMessageDefinitionVo bean5=new EcoreMessageDefinitionVo();
		EcoreMessageComponentVo bean6=new EcoreMessageComponentVo();
		EcoreMessageElementVo bean7=new EcoreMessageElementVo();
		EcoreMessageBuildingBlockVo bean8=new EcoreMessageBuildingBlockVo();
		EcoreExampleVo bean10=new EcoreExampleVo();
		EcoreDataTypeVo bean12=new EcoreDataTypeVo();
		EcoreConstraintVo bean13=new EcoreConstraintVo();
		EcoreCodeVo bean14=new EcoreCodeVo();
		EcoreMessageSetVo bean16=new EcoreMessageSetVo();
		EcoreMessageSetDefinitionRLVo bean17=new EcoreMessageSetDefinitionRLVo();
		EcoreNextVersionsVo bean19=new EcoreNextVersionsVo();
		EcoreSemanticMarkupVo bean20=new EcoreSemanticMarkupVo();
		EcoreSemanticMarkupElementVo bean21=new EcoreSemanticMarkupElementVo();
		RemovedObjectVo cc=new  RemovedObjectVo();
		try {
			rootElement.add(createElement(bean));
			rootElement.add(createElement(bean));
			rootElement.add(createElement(bean5));
			rootElement.add(createElement(bean6));
			rootElement.add(createElement(bean7));
			rootElement.add(createElement(bean8));
			rootElement.add(createElement(bean10));
			rootElement.add(createElement(bean12));
			rootElement.add(createElement(bean13));
			rootElement.add(createElement(bean14));
			rootElement.add(createElement(bean16));
			rootElement.add(createElement(bean17));
			rootElement.add(createElement(bean19));
			rootElement.add(createElement(bean20));
			rootElement.add(createElement(bean21));
			rootElement.add(createElement(cc));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		OutputFormat format=OutputFormat.createPrettyPrint();
		XMLWriter w=new XMLWriter(new FileOutputStream(xml),format);
		w.write(d);
	}

	private static Element createRoot() {
		Element root=XmlUtil.createElememnt("xmi:XMI");
		root.addAttribute("xmi:version", "2.0");
		root.addAttribute("xmlns:xmi", "http://www.omg.org/XMI");
		root.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		return root;
	}
	
	private static Element createElement(Object bean) throws Exception {
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

	public static void testfind(){
		File xml=new File("E:\\报文\\cufir_data.xml");
		Document d=XmlUtil.createDocument(xml);
		Element e=d.getRootElement();
		List<Element> els=XmlUtil.findSubElements(e, "ECORE_BUSINESS_AREA");
		for(Element el:els) {
			System.out.println(el.getName()+":"+el.attributeValue("id")+":"+el.attributeValue("code"));
		}
	}

}
