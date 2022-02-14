package com.cfets.cufir.s.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.cfets.cufir.s.data.util.XmlUtil;

public class TestBCXMIExport {

	public static void main(String[] args) {
		try {
			testexport();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void testexport() throws IOException {
		File xml=new File("E:\\报文\\cufir_BC.xml");
		int index=100;
		Document d=DocumentHelper.createDocument();
		Element rootElement=createRoot();
		d.setRootElement(rootElement);
	    for(int i=0;i<10;i++) {
	    	Element element = DocumentHelper.createElement("packagedElement");
	    	element.addAttribute("xmi:type", "uml:Class");
	    	element.addAttribute("xmi:id", i+"");
	    	element.addAttribute("name", i+"");
	    	Element generalizationElment = DocumentHelper.createElement("generalization");
	    	generalizationElment.addAttribute("xmi:id", (index++)+"");
	    	generalizationElment.addAttribute("general", (i-1)+"");
	    	element.add(generalizationElment);
	    	rootElement.add(element);
	    }
		OutputFormat format=OutputFormat.createPrettyPrint();
		XMLWriter w=new XMLWriter(new FileOutputStream(xml),format);
		w.write(d);
	}

	private static Element createRoot() {
		Element root=XmlUtil.createElememnt("uml:Model");
		root.addAttribute("xmi:version", "2.1");
		root.addAttribute("xmlns:xmi", "http://www.omg.org/XMI/2.1");
		root.addAttribute("xmlns:uml", "http://www.eclipse.org/uml2/2.1.0/UML");
		root.addAttribute("name", "BC UML Diagram");
		root.addAttribute("xmi:id", "_kkgsMBeLEd60N8ipLbB6nA");
		return root;
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
