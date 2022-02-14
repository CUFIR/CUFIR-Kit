package com.cfets.cufir.s.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.cfets.cufir.s.data.dao.EcoreBusinessComponentDao;
import com.cfets.cufir.s.data.dao.EcoreMessageComponentDao;
import com.cfets.cufir.s.data.dao.impl.EcoreBusinessComponentDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreMessageComponentDaoImpl;
import com.cfets.cufir.s.data.util.XmlUtil;
import com.cfets.cufir.s.data.vo.EcoreFullBusinessModelVO;
import com.cfets.cufir.s.data.vo.EcoreMessageComponentTracesVO;

public class MDR3Mgr {
	
	static HSSFWorkbook wb=null;
	static int index=1;
	
	static XMLWriter w=null;
	
	static HSSFCellStyle modelLineStyle =null;
	static HSSFCellStyle modelELineStyle =null;
	static HSSFCellStyle modelLineTraceStyle =null;
	static HSSFCellStyle modelELineTraceStyle =null;
	static HSSFCellStyle modelLineEndStyle =null;
	static HSSFCellStyle modelELineEndStyle =null;
	static List<EcoreMessageComponentTracesVO> cmodels =null;
	static Set<String> bcnamemaps = null;
	static Map<String,String> bcRlMap = null;
	static Map<String,List<EcoreFullBusinessModelVO>> bcMaps = null;
	static Set<String> bcSets = null;
	
	/**
	 * 生成MDR3相关的数据
	 * @param id
	 * @param path
	 * @return
	 * @throws  
	 * @throws Exception 
	 */
	public boolean generateMDR3Excel(String setId,String messageSetName,String path) throws Exception {
		boolean key=false;
		index=1;
		wb=new HSSFWorkbook();
		//定义样式
		createLineCellStyle();
		createELineCellEndStyle();
		createLineCellEndStyle();
		createELineCellStyle();
		createLineCellTraceStyle();
		createELineCellTraceStyle();
		//创建简介
		createIntroductionSheet(messageSetName);
		HSSFSheet fullBusinessModelSheet=wb.createSheet("FullBusinessModel");
		HSSFSheet tracesSheet=wb.createSheet("Traces");
		HSSFSheet mcSheet=wb.createSheet(messageSetName.replaceAll(" ", ""));
		//MC和trace
		createTracesSheet(tracesSheet,setId);
		//BC信息
		createFullBusinessModelSheet(fullBusinessModelSheet,setId);
		createMcSheet(mcSheet);
		File excel=new File(path);
		OutputStream os=new FileOutputStream(excel);
		File xmlpath=excel.getParentFile();
		File xml=new File(xmlpath.getPath()+"\\cufir_BC.xml");
		Document d=DocumentHelper.createDocument();
		exportBCXML(mcSheet,xml,d);
		OutputFormat format=OutputFormat.createPrettyPrint();
		XMLWriter w=new XMLWriter(new FileOutputStream(xml),format);
		try {
			wb.write(os);
			os.close();
			wb.close();
			w.write(d);
			w.close();
			key=true;
		} catch (Exception e) {
			throw e;
		} finally {
			os.close();
			wb.close();
			w.close();
		}
		return key;
	}

	/**
	 * 创建uml关系信息
	 * @param mcSheet
	 */
	private void createMcSheet(HSSFSheet mcSheet) {
		index=0;
		mcSheet.setDisplayGridlines(false);
		if(bcRlMap!=null && !bcRlMap.isEmpty()) {
			HSSFRow hrow=mcSheet.createRow(index++);
			HSSFCell hcell=hrow.createCell(0);
			hcell.setCellStyle(createModelHeaderStyle());
			hcell.setCellValue("Business Component Name");
			HSSFCell hcell2=hrow.createCell(1);
			hcell2.setCellStyle(createModelHeaderStyle());
			hcell2.setCellValue("Business Component Parent Name");
			HSSFCell hcell3=hrow.createCell(2);
			hcell3.setCellStyle(createModelHeaderStyle());
			hcell3.setCellValue("Business Element Name");
			HSSFCell hcell4=hrow.createCell(3);
			hcell4.setCellStyle(createModelHeaderEndStyle());
			hcell4.setCellValue("Business Element Type");
			Set<String> bcNames = bcRlMap.keySet();
			List<String> bcNamess =new ArrayList<String>(bcNames); 
			Collections.sort(bcNamess);
			//遍历下面的Bc
			for(String name:bcNamess) {
				//遍历下面的Be,有类型时BC的BE
				if(bcMaps.containsKey(name)) {
					boolean key = true;
					List<EcoreFullBusinessModelVO> lbe = bcMaps.get(name);
					for(EcoreFullBusinessModelVO vo:lbe) {
						String type = vo.getTypeName();
						if(name.equals(type)) {
							continue;
						}
						if(bcRlMap.containsKey(type)) {
							HSSFRow herow=mcSheet.createRow(index++);
							HSSFCell cell=herow.createCell(0);
							cell.setCellValue(name);
							cell.setCellStyle(modelELineStyle);
							String pn="";
							if(bcRlMap.get(name)!=null && !"".equals(bcRlMap.get(name))) {
								if(bcRlMap.containsKey(bcRlMap.get(name))) {
									pn=bcRlMap.get(name);
								}
							}
							HSSFCell cell2=herow.createCell(1);
							cell2.setCellValue(pn);
							cell2.setCellStyle(modelELineStyle);
							HSSFCell bcell=herow.createCell(2);
							bcell.setCellValue(vo.getElementName());
							bcell.setCellStyle(modelELineStyle);
							HSSFCell btcell=herow.createCell(3);
							btcell.setCellValue(type);
							btcell.setCellStyle(modelELineEndStyle);
							key = false;
						}
					}
					//如果没有BE符合条件，重新渲染BC
					if(key) {
						HSSFRow herow=mcSheet.createRow(index++);
						HSSFCell cell=herow.createCell(0);
						cell.setCellValue(name);
						cell.setCellStyle(modelELineStyle);
						String pn="";
						if(bcRlMap.get(name)!=null && !"".equals(bcRlMap.get(name))) {
							if(bcRlMap.containsKey(bcRlMap.get(name))) {
								pn=bcRlMap.get(name);
							}
						}
						HSSFCell cell2=herow.createCell(1);
						cell2.setCellValue(pn);
						cell2.setCellStyle(modelELineStyle);
						HSSFCell bcell=herow.createCell(2);
						bcell.setCellValue("");
						bcell.setCellStyle(modelELineStyle);
						HSSFCell btcell=herow.createCell(3);
						btcell.setCellValue("");
						btcell.setCellStyle(modelELineEndStyle);
					}
				} else {//如果没有BE，重新渲染BC
					HSSFRow row=mcSheet.createRow(index++);
					HSSFCell cell=row.createCell(0);
					cell.setCellValue(name);
					cell.setCellStyle(modelELineStyle);
					String pn="";
					if(bcRlMap.get(name)!=null && !"".equals(bcRlMap.get(name))) {
						if(bcRlMap.containsKey(bcRlMap.get(name))) {
							pn=bcRlMap.get(name);
						}
					}
					HSSFCell cell2=row.createCell(1);
					cell2.setCellValue(pn);
					cell2.setCellStyle(modelELineStyle);
					HSSFCell bcell=row.createCell(2);
					bcell.setCellValue("");
					bcell.setCellStyle(modelELineStyle);
					HSSFCell btcell=row.createCell(3);
					btcell.setCellValue("");
					btcell.setCellStyle(modelELineEndStyle);
				}
			}
		}
		//自适应列宽
		mcSheet.autoSizeColumn(0,true);
		mcSheet.autoSizeColumn(1,true);
		mcSheet.autoSizeColumn(2,true);
		mcSheet.autoSizeColumn(3,true);
	}

	/**
	 * 创建TracesSheet
	 * @param setId
	 * @throws Exception 
	 */
	private void createTracesSheet(HSSFSheet tracesSheet,String setId) throws Exception {
		index = 1;
		cmodels = new ArrayList<EcoreMessageComponentTracesVO>();
		bcnamemaps = new HashSet<String>();
		tracesSheet.setDisplayGridlines(false);
		EcoreMessageComponentDao dao = new EcoreMessageComponentDaoImpl();
		//获取相关的MC
		cmodels = dao.findMessageComponentTrace(setId);
		createTracesHeader(tracesSheet);
		if(cmodels!=null && !cmodels.isEmpty()) {
			for(EcoreMessageComponentTracesVO v:cmodels) {
				String bcn = v.getTraceToBusinessComponent();
				String eln = v.getTraceToElement();
				if(eln!=null && !"".equals(eln)) {
					bcnamemaps.add(bcn+"_"+eln);
				} else {
					bcnamemaps.add(bcn+"_"+bcn);
				}
				if(v.getMessageElementName()==null) {
					setCellValue(tracesSheet, v,modelLineStyle,modelLineEndStyle);
				}else {
					setCellValue(tracesSheet, v,modelELineStyle,modelELineEndStyle);
				}
			}
		}
		//自适应列宽
		tracesSheet.autoSizeColumn(0,true);
		tracesSheet.autoSizeColumn(1,true);
		tracesSheet.autoSizeColumn(2,true);
		tracesSheet.autoSizeColumn(3,true);
		tracesSheet.autoSizeColumn(4,true);
		tracesSheet.autoSizeColumn(5,true);
		tracesSheet.autoSizeColumn(6,true);
	}

	/**
	 * 创建FullBusinessModelSheet
	 * @param setId
	 * @throws Exception 
	 */
	private void createFullBusinessModelSheet(HSSFSheet fullBusinessModelSheet,String setId) throws Exception {
		index=1;
		bcRlMap = new HashMap<String, String>();
		bcMaps = new HashMap<String, List<EcoreFullBusinessModelVO>>();
		fullBusinessModelSheet.setDisplayGridlines(false);
		EcoreBusinessComponentDao dao = new EcoreBusinessComponentDaoImpl();
		List<EcoreFullBusinessModelVO> models = dao.findFullBusinessModel(setId);
		createFullBusinessModelHeader(fullBusinessModelSheet);
		if(models!=null && !models.isEmpty()) {
			for(EcoreFullBusinessModelVO v:models) {
				String key="";
				String bcn = v.getName();
				String eln = v.getElementName();
				if(eln==null || "".equals(eln)) {
					key = bcn+"_"+bcn;
				} else {
					key = bcn+"_"+eln;
				}
				//判断是否是BC，渲染不同的样式
				if(v.getElementName()==null) {
					if(bcnamemaps.contains(key)) {
						setCellValue(fullBusinessModelSheet, v,modelLineTraceStyle,modelLineEndStyle);
						bcRlMap.put(v.getName(), v.getSuperName());
					} else {
						setCellValue(fullBusinessModelSheet, v,modelLineStyle,modelLineEndStyle);
					}
				}else {
					if(bcnamemaps.contains(key)) {
						setCellValue(fullBusinessModelSheet, v,modelELineTraceStyle,modelELineEndStyle);
						bcRlMap.put(v.getName(), v.getSuperName());
					} else {
						setCellValue(fullBusinessModelSheet, v,modelELineStyle,modelELineEndStyle);
					}
					List<EcoreFullBusinessModelVO> lbe = new ArrayList<EcoreFullBusinessModelVO>();
					if(bcMaps.containsKey(v.getName())) {
						lbe = bcMaps.get(v.getName());
					}else {
						bcMaps.put(v.getName(), lbe);
					}
					lbe.add(v);
				}
			}
		}
		fullBusinessModelSheet.autoSizeColumn(0,true);
		fullBusinessModelSheet.autoSizeColumn(1,true);
		fullBusinessModelSheet.autoSizeColumn(2,true);
		fullBusinessModelSheet.autoSizeColumn(3,true);
		fullBusinessModelSheet.autoSizeColumn(4,true);
	}
	
	/**
	 * 写rows
	 * @param sheet
	 * @param node
	 */
	private static void setCellValue(HSSFSheet sheet,EcoreMessageComponentTracesVO v
			,HSSFCellStyle style,HSSFCellStyle endstyle) {
		HSSFRow row=sheet.createRow(index++);
		HSSFCell cell=row.createCell(0);
		cell.setCellStyle(style);
		cell.setCellValue(v.getMessageComponentName());
		cell=row.createCell(1);
		cell.setCellStyle(style);
		cell.setCellValue(v.getMessageElementName());
		cell=row.createCell(2);
		cell.setCellStyle(style);
		cell.setCellValue(v.getTypeTracTo());
		cell=row.createCell(3);
		cell.setCellStyle(style);
		cell.setCellValue(v.getTechnical());
		cell=row.createCell(4);
		cell.setCellStyle(style);
		cell.setCellValue(v.getTraceToBusinessComponent());
		cell=row.createCell(5);
		cell.setCellStyle(style);
		cell.setCellValue(v.getTraceToElement());
		cell=row.createCell(6);
		cell.setCellStyle(endstyle);
		cell.setCellValue(v.getTracePath());
	}
	
	/**
	 * 写rows
	 * @param sheet
	 * @param node
	 */
	private static void setCellValue(HSSFSheet sheet,EcoreFullBusinessModelVO v
			,HSSFCellStyle style,HSSFCellStyle endstyle) {
		HSSFRow row=sheet.createRow(index++);
		HSSFCell cell=row.createCell(0);
		cell.setCellStyle(style);
		cell.setCellValue(v.getName());
		cell=row.createCell(1);
		cell.setCellStyle(style);
		cell.setCellValue(v.getElementName());
		cell=row.createCell(2);
		cell.setCellStyle(style);
		cell.setCellValue(v.getParentName());
		cell=row.createCell(3);
		cell.setCellStyle(style);
		cell.setCellValue(v.getDesc());
		cell=row.createCell(4);
		cell.setCellStyle(endstyle);
		cell.setCellValue(v.getTypeName());
	}

	/**
	 * 创建introductionSheet
	 * @param sheet
	 * @param name
	 */
	private void createIntroductionSheet(String name){
		HSSFSheet sheet=wb.createSheet("Introduction");
		sheet.setDisplayGridlines(false);
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(8);
		cell.setCellValue("Introduction");
		CellRangeAddress cra = new CellRangeAddress(0,0,8,14);
		sheet.addMergedRegion(cra);
		//样式
		HSSFCellStyle style=createCellStyle(); 
		cell.setCellStyle(style);	
		//描述
		HSSFRow row2 = sheet.createRow(2);
		HSSFCell cell2 = row2.createCell(0);
		cell2.setCellValue("This document describes the Business Model Components and Elements used by the "
				+ name+" message definitions.");	
		HSSFRow row3 = sheet.createRow(4);
		HSSFCell cell3 = row3.createCell(0);
		cell3.setCellValue("In the “Business Model” tab:");
		HSSFRow row4 = sheet.createRow(5);
		HSSFCell cell4 = row4.createCell(0);
		cell4.setCellValue("  - the cells with text in green indicate the business concepts used by the "
				+ name+" message set");
		HSSFRow row5 = sheet.createRow(7);
		HSSFCell cell5 = row5.createCell(0);
		cell5.setCellValue("The “Traces” tab shows to which business concepts the "
				+ name+" message components and message elements trace.");
		//样式2
		HSSFCellStyle style2=createCellStyle2(); 
		cell2.setCellStyle(style2);
		cell3.setCellStyle(style2);
		cell4.setCellStyle(style2);
		cell5.setCellStyle(style2);
	}
	
	private static HSSFCellStyle createCellStyle2() {
		HSSFCellStyle style = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short)11);
		style.setFont(font);
		return style;
	}
	
	private static HSSFCellStyle createCellStyle() {
		HSSFCellStyle style = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short)14);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFont(font);
		return style;
	}
	
	private static void createLineCellEndStyle() {
		modelLineEndStyle = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short)11);
		modelLineEndStyle.setFont(font);
		modelLineEndStyle.setBorderTop(BorderStyle.THIN);
		modelLineEndStyle.setBorderBottom(BorderStyle.THIN);
		modelLineEndStyle.setBottomBorderColor(IndexedColors.PALE_BLUE.getIndex());
		modelLineEndStyle.setTopBorderColor(IndexedColors.PALE_BLUE.getIndex());
		modelLineEndStyle.setBorderRight(BorderStyle.THIN);
		modelLineEndStyle.setRightBorderColor(IndexedColors.PALE_BLUE.getIndex());
	}
	
	private static void createELineCellEndStyle() {
		modelELineEndStyle = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short)11);
		modelELineEndStyle.setFont(font);
		modelELineEndStyle.setBorderTop(BorderStyle.THIN);
		modelELineEndStyle.setBorderBottom(BorderStyle.THIN);
		modelELineEndStyle.setBottomBorderColor(IndexedColors.PALE_BLUE.getIndex());
		modelELineEndStyle.setTopBorderColor(IndexedColors.PALE_BLUE.getIndex());
		modelELineEndStyle.setBorderRight(BorderStyle.THIN);
		modelELineEndStyle.setRightBorderColor(IndexedColors.PALE_BLUE.getIndex());
	}
	
	private static void createLineCellStyle() {
		modelLineStyle = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short)11);
		modelLineStyle.setFont(font);
		modelLineStyle.setBorderTop(BorderStyle.THIN);
		modelLineStyle.setBorderBottom(BorderStyle.THIN);
		modelLineStyle.setBottomBorderColor(IndexedColors.PALE_BLUE.getIndex());
		modelLineStyle.setTopBorderColor(IndexedColors.PALE_BLUE.getIndex());
	}
	
	private static void createELineCellStyle() {
		modelELineStyle = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short)11);
		modelELineStyle.setFont(font);
		modelELineStyle.setBorderTop(BorderStyle.THIN);
		modelELineStyle.setBorderBottom(BorderStyle.THIN);
		modelELineStyle.setBottomBorderColor(IndexedColors.PALE_BLUE.getIndex());
		modelELineStyle.setTopBorderColor(IndexedColors.PALE_BLUE.getIndex());
	}
	
	private static void createLineCellTraceStyle() {
		modelLineTraceStyle = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		font.setBold(true);
		font.setColor(IndexedColors.GREEN.getIndex());
		font.setFontHeightInPoints((short)11);
		modelLineTraceStyle.setFont(font);
		modelLineTraceStyle.setBorderTop(BorderStyle.THIN);
		modelLineTraceStyle.setBorderBottom(BorderStyle.THIN);
		modelLineTraceStyle.setBottomBorderColor(IndexedColors.PALE_BLUE.getIndex());
		modelLineTraceStyle.setTopBorderColor(IndexedColors.PALE_BLUE.getIndex());
	}
	
	private static void createELineCellTraceStyle() {
		modelELineTraceStyle = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short)11);
		font.setColor(IndexedColors.GREEN.getIndex());
		modelELineTraceStyle.setFont(font);
		modelELineTraceStyle.setBorderTop(BorderStyle.THIN);
		modelELineTraceStyle.setBorderBottom(BorderStyle.THIN);
		modelELineTraceStyle.setBottomBorderColor(IndexedColors.PALE_BLUE.getIndex());
		modelELineTraceStyle.setTopBorderColor(IndexedColors.PALE_BLUE.getIndex());
	}
	
	/**
	 * 设置头
	 * @param sheet
	 */
	private void createFullBusinessModelHeader(HSSFSheet sheet) {
		HSSFRow rowTitle=sheet.createRow(0);
		HSSFCell cell=rowTitle.createCell(0);
		cell.setCellValue("Business Component Name");
		cell.setCellStyle(createModelHeaderStyle());
		cell=rowTitle.createCell(1);
		cell.setCellValue("Business Element Name");
		cell.setCellStyle(createModelHeaderStyle());
		cell=rowTitle.createCell(2);
		cell.setCellValue("Business Component Parent Name");
		cell.setCellStyle(createModelHeaderStyle());
		cell=rowTitle.createCell(3);
		cell.setCellValue("Documentation");
		cell.setCellStyle(createModelHeaderStyle());
		cell=rowTitle.createCell(4);
		cell.setCellValue("Data Type Name");
		cell.setCellStyle(createModelHeaderEndStyle());
	}
	
	/**
	 * 设置头
	 * @param tracesSheet
	 */
	private void createTracesHeader(HSSFSheet tracesSheet) {
		HSSFRow rowTitle=tracesSheet.createRow(0);
		HSSFCell cell=rowTitle.createCell(0);
		cell.setCellValue("Message Component Name");
		cell.setCellStyle(createModelHeaderStyle());
		cell=rowTitle.createCell(1);
		cell.setCellValue("Message Element Name");
		cell.setCellStyle(createModelHeaderStyle());
		cell=rowTitle.createCell(2);
		cell.setCellValue("Type Trace To");
		cell.setCellStyle(createModelHeaderStyle());
		cell=rowTitle.createCell(3);
		cell.setCellValue("Is Technical");
		cell.setCellStyle(createModelHeaderStyle());
		cell=rowTitle.createCell(4);
		cell.setCellValue("Trace To Business Component");
		cell.setCellStyle(createModelHeaderStyle());
		cell=rowTitle.createCell(5);
		cell.setCellValue("Trace To Element");
		cell.setCellStyle(createModelHeaderStyle());
		cell=rowTitle.createCell(6);
		cell.setCellValue("Trace Path");
		cell.setCellStyle(createModelHeaderEndStyle());
	}
	
	private static HSSFCellStyle createModelHeaderStyle() {
		HSSFCellStyle style = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short)11);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFont(font);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
		return style;
	}
	
	private static HSSFCellStyle createModelHeaderEndStyle() {
		HSSFCellStyle style = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short)11);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFont(font);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
		style.setBorderRight(BorderStyle.THIN);
		style.setRightBorderColor(IndexedColors.PALE_BLUE.getIndex());
		return style;
	}
	
	/**
	 * 导出支持uml导入的xml文件
	 * @param mcSheet
	 * @param xml
	 * @param d
	 * @throws IOException
	 */
	public static void exportBCXML(HSSFSheet mcSheet,File xml,Document d) throws IOException {
		int pk =1;
		bcSets = new HashSet<String>();
		Element rootElement=createRoot();
		d.setRootElement(rootElement);
		for(int rowIndex=1;rowIndex<index;rowIndex++) {
			HSSFRow r = mcSheet.getRow(rowIndex);
			String bcname = r.getCell(0).getStringCellValue();
			String pcname = r.getCell(1).getStringCellValue();
			//BC及继承
			if(!bcSets.contains(bcname)) {
				Element element = DocumentHelper.createElement("packagedElement");
		    	element.addAttribute("xmi:type", "uml:Class");
		    	element.addAttribute("xmi:id", bcname);
		    	element.addAttribute("name", bcname);
		    	if(pcname!=null && !"".equals(pcname)) {
		    		Element generalizationElment = DocumentHelper.createElement("generalization");
			    	generalizationElment.addAttribute("xmi:id",(pk++)+"");
			    	generalizationElment.addAttribute("general",pcname);
			    	element.add(generalizationElment);
		    	}
		    	rootElement.add(element);
		    	bcSets.add(bcname);
			}
		}
		for(int rowIndex=1;rowIndex<index;rowIndex++) {
			HSSFRow r = mcSheet.getRow(rowIndex);
			String bcname = r.getCell(0).getStringCellValue();
			String rcname = r.getCell(3).getStringCellValue();
			//BC之间的关联
			if(rcname!=null && !"".equals(rcname) && !bcSets.contains(bcname+"_"+rcname)) {
				int pkid = pk++;
				Element element = DocumentHelper.createElement("packagedElement");
		    	element.addAttribute("xmi:type", "uml:Association");
		    	element.addAttribute("xmi:id", pkid+"");
		    	element.addAttribute("name", (pk++)+"");
		    	element.addAttribute("memberEnd", bcname+" "+rcname);
		    	element.addAttribute("navigableOwnedEnd", bcname+" "+rcname);
		    	//BC关联
		    	Element ownedEnd1 = DocumentHelper.createElement("ownedEnd");
		    	ownedEnd1.addAttribute("xmi:id", (pk++)+"");
		    	ownedEnd1.addAttribute("name", "");
		    	ownedEnd1.addAttribute("visibility", "public");
		    	ownedEnd1.addAttribute("type", bcname);
		    	ownedEnd1.addAttribute("association",pkid+"");
		    	//VALUE
		    	Element upperValue1 = DocumentHelper.createElement("upperValue");
		    	upperValue1.addAttribute("xmi:type", "uml:LiteralUnlimitedNatural");
		    	upperValue1.addAttribute("xmi:id", (pk++)+"");
		    	upperValue1.addAttribute("value", "1");
		    	Element lowerValue1 = DocumentHelper.createElement("lowerValue");
		    	lowerValue1.addAttribute("xmi:type", "uml:LiteralInteger");
		    	lowerValue1.addAttribute("xmi:id", (pk++)+"");
		    	ownedEnd1.add(upperValue1);
		    	ownedEnd1.add(lowerValue1);
		    	element.add(ownedEnd1);
		    	//BC2
		    	Element ownedEnd2 = DocumentHelper.createElement("ownedEnd");
		    	ownedEnd2.addAttribute("xmi:id", (pk++)+"");
		    	ownedEnd2.addAttribute("name", "");
		    	ownedEnd2.addAttribute("visibility", "public");
		    	ownedEnd2.addAttribute("type", rcname);
		    	ownedEnd2.addAttribute("association",pkid+"");
		    	//VALUE
		    	Element upperValue2 = DocumentHelper.createElement("upperValue");
		    	upperValue2.addAttribute("xmi:type", "uml:LiteralUnlimitedNatural");
		    	upperValue2.addAttribute("xmi:id", (pk++)+"");
		    	upperValue2.addAttribute("value", "1");
		    	Element lowerValue2 = DocumentHelper.createElement("lowerValue");
		    	lowerValue2.addAttribute("xmi:type", "uml:LiteralInteger");
		    	lowerValue2.addAttribute("xmi:id", (pk++)+"");
		    	ownedEnd2.add(upperValue2);
		    	ownedEnd2.add(lowerValue2);
		    	element.add(ownedEnd2);
		    	rootElement.add(element);
		    	bcSets.add(bcname+"_"+rcname);
		    	bcSets.add(rcname+"_"+bcname);
			}
		}
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
}
