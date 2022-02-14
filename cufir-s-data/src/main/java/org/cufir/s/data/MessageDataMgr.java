package org.cufir.s.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.cufir.s.data.dao.impl.EcoreMessageDefinitionImpl;
import org.cufir.s.data.util.XmlUtil;
import org.cufir.s.data.vo.EcoreDocumentTreeNode;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class MessageDataMgr {
	
	static HSSFWorkbook wb=null;
	static HSSFSheet sheet=null;
	static int index=1;
	
	static XMLWriter w=null;

	/**
	 * 生成message相关的数据
	 */
	public boolean generateMessageDataXML(String id,String path) throws Exception {
		boolean key=false;
		EcoreMessageDefinitionImpl ecoreMessageDefinitionDao=new EcoreMessageDefinitionImpl();
		File xml=new File(path);
		List<Object> objects=new ArrayList<Object>();
		//获取数据
		try {
			objects=ecoreMessageDefinitionDao.findMessageDataById(id);
			Document d=DocumentHelper.createDocument();
			//创建root元素
			Element rootElement=XmlUtil.createRoot();
			d.setRootElement(rootElement);
			//创建元素
			if(objects!=null && !objects.isEmpty()) {
				for(Object bean:objects) {
					rootElement.add(XmlUtil.createXmiElement(bean));
				}
			}
			//格式化
			OutputFormat format=OutputFormat.createCompactFormat();
			//生成文件
			w=new XMLWriter(new FileOutputStream(xml),format);
			w.write(d);
			key=true;
		}catch(Exception e) {
			throw e;
		} finally {
			w.close();
		}
		return key;
	}
	
	/**
	 * 生成message相关的数据
	 */
	public boolean generateMessageDataExcel(String id,String path) throws Exception {
		boolean key=false;
		index=1;
		wb=new HSSFWorkbook();
		sheet=wb.createSheet("报文结构");
		EcoreMessageDefinitionImpl ecoreMessageDefinitionDao=new EcoreMessageDefinitionImpl();
		File excel=new File(path);
		OutputStream os=new FileOutputStream(excel);
		EcoreDocumentTreeNode node=null;
		try {
			node = ecoreMessageDefinitionDao.findMessageDocumentDataById(id);
			createHeader(sheet);
			setCellValue(sheet,node);
			printCellValue(sheet, node);
			wb.write(os);
			os.close();
			wb.close();
			key=true;
		} catch (Exception e) {
			throw e;
		} finally {
			os.close();
			wb.close();
		}
		return key;
	}
	
	/**
	 * 迭代树
	 * @param sheet
	 * @param node
	 */
	private static void printCellValue(HSSFSheet sheet,EcoreDocumentTreeNode node){
		if(node.getChildNodes()!=null && !node.getChildNodes().isEmpty()) {
			for(EcoreDocumentTreeNode pnode:node.getChildNodes()) {
				setCellValue(sheet, pnode);
				printCellValue(sheet, pnode);
			}
		}
	}
	
	/**
	 * 写rows
	 * @param sheet
	 * @param node
	 */
	private static void setCellValue(HSSFSheet sheet,EcoreDocumentTreeNode node) {
		HSSFRow row=sheet.createRow(index++);
		int lvl=node.getLvl();
		String str="";
		for(int i=1;i<lvl;i++) {
			str+="    ";
		}
		HSSFCell cell=row.createCell(0);
		cell.setCellValue(index-1);
		cell=row.createCell(1);
		cell.setCellValue(node.getLvl());
		cell=row.createCell(2);
		cell.setCellValue(str+node.getName());
		cell=row.createCell(3);
		cell.setCellValue(node.getXmlTag());
		cell=row.createCell(4);
		cell.setCellValue(node.getMult());
		cell=row.createCell(5);
		cell.setCellValue(node.getType());
		cell=row.createCell(6);
		cell.setCellValue(node.getConstraint());
	}
	
	/**
	 * 设置头
	 * @param sheet
	 */
	private static void createHeader(HSSFSheet sheet) {
		HSSFRow rowTitle=sheet.createRow(0);
		HSSFCell cell=rowTitle.createCell(0);
		cell.setCellValue("Index（序号）");
		cell=rowTitle.createCell(1);
		cell.setCellValue("Lvl（缩进的层级）");
		cell=rowTitle.createCell(2);
		cell.setCellValue("Name");
		cell=rowTitle.createCell(3);
		cell.setCellValue("XML Tag");
		cell=rowTitle.createCell(4);
		cell.setCellValue("Mult");
		cell=rowTitle.createCell(5);
		cell.setCellValue("Type/Code");
		cell=rowTitle.createCell(6);
		cell.setCellValue("Constraint");
	}
}
