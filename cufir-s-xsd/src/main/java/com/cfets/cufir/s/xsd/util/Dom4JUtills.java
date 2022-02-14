package com.cfets.cufir.s.xsd.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * @author gujianhua_het xpath xml operations
 */
public class Dom4JUtills {

	public static Document getDocument(String filename) throws DocumentException {
		SAXReader sr = new SAXReader();
		Document document = sr.read(filename);
		return document;
	}

	public static boolean isNodeExists(Document document, String pattern) {
		List<Node> nodeList = getNodes(document, pattern);
		if (nodeList.size() != 0) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static List<Node> getNodes(Document document, String pattern) {
		return document.selectNodes(pattern);
	}

	public static Node getNode(Document document, String pattern) {
		return document.selectSingleNode(pattern);
	}
	
	public static void deleteNode(Document document,String pattern) {
		Node node=document.selectSingleNode(pattern);
		Element parent=node.getParent();
		parent.remove(node);
	}
	
//	public static Element addObjectPath(Document document,DxsInstallInfo pojo) {
//		Element root=document.getRootElement();
//		Element newObjectPath=root.addElement("InstallationPath");
//		newObjectPath.addAttribute("path", pojo.getInstallPath());
//		return newObjectPath;
//	}
	
	/**
	 * 
	 * 新增<DxsNode>及子节点
	 * @param element
	 * @param pojo
	 */
//	public static addDxsNode(Element installationPath,DxsInstallInfo pojo) {
//		Element newDxsNode=installationPath.addElement("DxsNode");
//		newDxsNode.addAttribute("", pojo.getXXXX());
//		newDxsNode.addAttribute("", pojo.getXXXX());
//		newDxsNode.addAttribute("", pojo.getXXXX());
//		newDxsNode.addAttribute("", pojo.getXXXX());
//	}
	
	
	/**
	 * 
	 * 更新dxsNode及子节点
	 * @param dxsNode
	 * @param POJO
	 */
//	public static void  updateDxsNode(Element dxsNode,DxsInstallInfo POJO) {
//		dxsNode.element("XXXX").setText("XXXXXX");
//		dxsNode.element("XXXX").setText("XXXXXX");
//		dxsNode.element("XXXX").setText("XXXXXX");
//		dxsNode.element("XXXX").setText("XXXXXX");
//		dxsNode.element("XXXX").setText("XXXXXX");
//	}
	
	public static void documentToXml(Document document,String filename) throws IOException {
		String realPath="D:\\XSchema";
		OutputFormat format=OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		format.setIndent("    ");
		XMLWriter writer=new XMLWriter(new FileOutputStream(realPath+"/LightEditor_caaa.xsd"),format);
		writer.write(document);
		writer.close();

	}
	
}
