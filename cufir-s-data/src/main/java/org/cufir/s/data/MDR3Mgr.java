package org.cufir.s.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.cufir.s.ecore.bean.EcoreBusinessComponent;
import org.cufir.s.ecore.bean.EcoreBusinessElement;
import org.cufir.s.ecore.bean.EcoreMessageComponent;
import org.cufir.s.ecore.bean.EcoreMessageElement;
import org.cufir.s.ecore.bean.EcoreMessageSet;
import org.cufir.s.data.dao.impl.EcoreBusinessComponentImpl;
import org.cufir.s.data.dao.impl.EcoreBusinessComponentRLImpl;
import org.cufir.s.data.dao.impl.EcoreBusinessElementImpl;
import org.cufir.s.data.dao.impl.EcoreDataTypeImpl;
import org.cufir.s.data.dao.impl.EcoreMessageBuildingBlockImpl;
import org.cufir.s.data.dao.impl.EcoreMessageComponentImpl;
import org.cufir.s.data.dao.impl.EcoreMessageElementImpl;
import org.cufir.s.data.dao.impl.EcoreMessageSetDefinitionRLImpl;
import org.cufir.s.data.vo.EcoreFullBusinessModelVO;
import org.cufir.s.data.vo.EcoreMessageComponentTracesVO;
import org.springframework.util.StringUtils;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * 报文导出MDR3文件
 */
public class MDR3Mgr {
	
	private static HSSFWorkbook wb=null;
	private static int index=1;
	
	/**
	 * 加粗
	 */
	private static HSSFCellStyle modelBoldStartStyle = null, modelBoldStyle =null, modelBoldEndStyle =null;
	
	/**
	 * 一般
	 */
	private static HSSFCellStyle modelStartStyle =null, modelStyle =null, modelEndStyle =null;
	
	/**
	 * 加粗绿色
	 */
	private static HSSFCellStyle modelBoldGreenStartStyle =null, modelBoldGreenStyle =null, modelBoldGreenEndStyle =null;
	
	/**
	 * 绿色
	 */
	private static HSSFCellStyle modelGreenStartStyle = null, modelGreenStyle = null, modelGreenEndStyle = null;
	
	private List<EcoreBusinessElement> bes = null;
	private Map<String, EcoreBusinessComponent> bcMap = null;
	
	/**
	 * 绿色组件
	 */
	private Map<String, Integer> greenComponentMap = null;
	
	/**
	 * 绿色元素
	 */
	private Table<String, String, Integer>greenElementMap = null;
	
	private EcoreMessageSet ecoreMessageSet;
	
	public MDR3Mgr(EcoreMessageSet ecoreMessageSet) {
		this.ecoreMessageSet = ecoreMessageSet;
	}
	
	/**
	 * 生成MDR3相关的数据
	 * @return 
	 * @throws Exception 
	 */
	public boolean generateMDR3Excel() throws Exception {
		boolean key=false;
		index=1;
		wb=new HSSFWorkbook();
		//初始化样式
		createCellBoldStartStyle();
		createCellBoldStyle();
		createCellBoldEndStyle();
		
		createCellStartStyle();
		createCellStyle();
		createCellEndStyle();
		
		createCellBoldGreenStartStyle();
		createCellBoldGreenStyle();
		createCellBoldGreenEndStyle();
		
		createCellGreenStartStyle();
		createCellGreenStyle();
		createCellGreenEndStyle();
		
		//创建简介
		createIntroductionSheet(ecoreMessageSet.getName());
		HSSFSheet fullBusinessModelSheet=wb.createSheet("FullBusinessModel");
		HSSFSheet tracesSheet=wb.createSheet("Traces");
		HSSFSheet accountSwitching=wb.createSheet("AccountSwitching");
		
		bes = new EcoreBusinessElementImpl().findMdr3();
		bcMap = new EcoreBusinessComponentImpl().findMdr3();
		
		//trace
		createTracesSheet(tracesSheet);
		//FullBusinessModel
		createFullBusinessModelSheet(fullBusinessModelSheet);
		
		//MC和trace
		File excel=new File(ecoreMessageSet.getPath());
		OutputStream os=new FileOutputStream(excel);
		try {
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
	 * 创建TracesSheet
	 * @param setId
	 * @throws Exception 
	 */
	private void createTracesSheet(HSSFSheet tracesSheet) throws Exception {
		index = 1;
		greenComponentMap = new HashMap<>();
		greenElementMap = HashBasedTable.create();
		//获取报文展示Traces数据
		List<EcoreMessageComponentTracesVO> models = getTracesModels(tracesSheet);
		//设置头部标题展示
		createTracesHeader(tracesSheet);
		if(models!=null && !models.isEmpty()) {
			for(EcoreMessageComponentTracesVO v:models) {
				if(v.getMessageElementName()==null) {
					//组件加粗展示
					setCellValue(tracesSheet, v,modelBoldStartStyle,modelBoldStyle,modelBoldEndStyle);
					//加入Map做为当前报文标记
					if(v.getTraceToBusinessComponent() != null) {
						greenComponentMap.put(v.getTraceToBusinessComponent(), 0);
					}
				}else {
					//元素不加粗
					setCellValue(tracesSheet, v,modelStartStyle,modelStyle,modelEndStyle);
					//加入Map做为当前报文标记
					if(!StringUtils.isEmpty(v.getTraceToBusinessComponent()) && !StringUtils.isEmpty(v.getTraceToElement())) {
						greenElementMap.put(v.getTraceToBusinessComponent(),v.getTraceToElement(),0);
					}else if(!StringUtils.isEmpty(v.getTraceToBusinessComponent())){
						greenComponentMap.put(v.getTraceToBusinessComponent(), 0);
					}
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
		
		//加入筛选功能
		CellRangeAddress address = CellRangeAddress.valueOf("A1:F" + (models.size() + 1));
		tracesSheet.setAutoFilter(address);
	}
	
	/**
	 * 创建FullBusinessModelSheet
	 * @param setId
	 * @throws Exception 
	 */
	private void createFullBusinessModelSheet(HSSFSheet fullBusinessModelSheet) throws Exception {
		index=1;
		fullBusinessModelSheet.setDisplayGridlines(false);
		createFullBusinessModelHeader(fullBusinessModelSheet);
		//查询FullBusiness所需数据
		List<EcoreFullBusinessModelVO> models = getFullBusinessModels();
		if(models!=null && !models.isEmpty()) {
			for(EcoreFullBusinessModelVO v:models) {
				//判断是否是BC，渲染不同的样式
				if(StringUtils.isEmpty(v.getElementName())) {
					//BC
					if(!greenComponentMap.containsKey(v.getName())) {
						setCellValue(fullBusinessModelSheet, v,modelBoldStartStyle,modelBoldStyle,modelBoldEndStyle,0);
					}else {
						//标绿
						setCellValue(fullBusinessModelSheet, v,modelBoldGreenStartStyle,modelBoldGreenStyle,modelBoldGreenEndStyle,0);
					}
				}else {
					//BE
					if(!greenElementMap.contains(v.getName(), v.getElementName())) {
						setCellValue(fullBusinessModelSheet, v,modelStartStyle,modelStyle,modelEndStyle,1);
					}else {
						//标绿
						setCellValue(fullBusinessModelSheet, v,modelGreenStartStyle,modelGreenStyle,modelGreenEndStyle,1);
					}
				}
			}
		}
		//宽度自适应
		fullBusinessModelSheet.autoSizeColumn(0,true);
		fullBusinessModelSheet.autoSizeColumn(1,true);
		fullBusinessModelSheet.autoSizeColumn(2,true);
		fullBusinessModelSheet.autoSizeColumn(3,true);
		fullBusinessModelSheet.autoSizeColumn(4,true);
		fullBusinessModelSheet.autoSizeColumn(5,true);
		//加入筛选功能
		CellRangeAddress address = CellRangeAddress.valueOf("A1:F" + (models.size() + 1));
		fullBusinessModelSheet.setAutoFilter(address);
	}
	
	/**
	 * 获取FullBusiness展示Models
	 * @return
	 */
	private List<EcoreFullBusinessModelVO> getFullBusinessModels(){
		//BusinessElement按BusinessComponentId分组
		Map<String, List<EcoreFullBusinessModelVO>> beMap = new HashMap<String, List<EcoreFullBusinessModelVO>>();
		bes.forEach(be->{
			EcoreFullBusinessModelVO vo = new EcoreFullBusinessModelVO();
			vo.setBcId(be.getBusinessComponentId());
			vo.setBeId(be.getId());
			vo.setTypeId(be.getTypeId());
			vo.setElementName(be.getName());
			vo.setDesc(be.getDefinition());
			vo.setType(be.getType());
			List<EcoreFullBusinessModelVO> blist = beMap.get(be.getBusinessComponentId());
			if (blist == null) {
				blist = new ArrayList<EcoreFullBusinessModelVO>();
				beMap.put(be.getBusinessComponentId(), blist);
			}
			blist.add(vo);
		});
		//准备DataType数据
		Map<String, String> dtMap = new EcoreDataTypeImpl().findMdr3();
		//准备BusinessComponentRL数据
		Map<String, String> bcRLMap = new EcoreBusinessComponentRLImpl().findMdr3();
		//整理排序
		List<EcoreFullBusinessModelVO> models = new ArrayList<EcoreFullBusinessModelVO>();
		bcMap.forEach((bcId,bc)->{
			EcoreFullBusinessModelVO vo = new EcoreFullBusinessModelVO();
			vo.setBcId(bcId);
			vo.setName(bc.getName());
			vo.setDesc(bc.getDefinition());
			String superId = bcRLMap.get(bcId);
			String superName = "";
			if (superId != null) {
				EcoreBusinessComponent ecoreBusinessComponent = bcMap.get(superId);
				superName = ecoreBusinessComponent.getName();
				vo.setSuperName(superName);
			}
			models.add(vo);
			List<EcoreFullBusinessModelVO> elist = beMap.get(bcId);
			if (elist != null && !elist.isEmpty()) {
				for (EcoreFullBusinessModelVO be : elist) {
					String typeId = be.getTypeId();
					String type = be.getType();
					be.setName(bcMap.get(bcId).getName());
					be.setSuperName(superName);
					// parent信息
					if ("2".equals(type) && typeId != null && beMap.containsKey(typeId)) {
						List<EcoreFullBusinessModelVO> el = beMap.get(typeId);
						if (el != null && !el.isEmpty()) {
							for (EcoreFullBusinessModelVO tempel : el) {
								if (bcId.equals(tempel.getTypeId())) {
									be.setParentName(tempel.getElementName());
									break;
								}
							}
						}
					}
					// 设置类型名称
					if (be.getTypeId() != null) {
						be.setTypeName(dtMap.get(be.getTypeId()));
					}
					models.add(be);
				}
			}
		});
		return models;
	}
	
	/**
	 * 获取Traces展示Models
	 * @param tracesSheet
	 * @param id
	 */
	private List<EcoreMessageComponentTracesVO> getTracesModels(HSSFSheet tracesSheet) {
		List<EcoreMessageComponentTracesVO> models = new ArrayList<EcoreMessageComponentTracesVO>();
		//名称排序(键排序忽略大小写)
		Map<String, List<EcoreMessageComponentTracesVO>> mctVoMap = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareToIgnoreCase(o2);
			}
		});
		tracesSheet.setDisplayGridlines(false);
		//获取相关MC按id分组map
		Map<String,EcoreMessageComponent> mcMap = new EcoreMessageComponentImpl().findMdr3();
		//获取相关ME按msgComponentId分组map
		Map<String, List<EcoreMessageElement>> meMap = new EcoreMessageElementImpl().findMdr3();
		//查询报文集中的报文
		List<Map<String, Object>> definitionRlMessageIds = new EcoreMessageSetDefinitionRLImpl().findMessageIdBySetId(ecoreMessageSet.getId());
		if(definitionRlMessageIds != null && definitionRlMessageIds.size() >0) {
			//报文集的所有报文查询第一层业务组件id
			StringBuilder messageIds = new StringBuilder(100);
			for (Map<String, Object> map: definitionRlMessageIds) {
				messageIds.append("'");
				messageIds.append(map.get("MESSAGE_ID"));
				messageIds.append("'");
				messageIds.append(",");
			}
			String d = messageIds.toString();
			String defintionIds = d.substring(0, d.length()-1);
			Map<String, List<String>> mbbMap = new EcoreMessageBuildingBlockImpl().findMdr3(defintionIds);
			//数据配比并排序
			for(String key : mbbMap.keySet()) {
				for(String msgComponentId : mbbMap.get(key)) {
					putVos(mctVoMap, mcMap, meMap, msgComponentId);
				}
			}
		}
		for(String voKey : mctVoMap.keySet()) {
			List<EcoreMessageComponentTracesVO> list = mctVoMap.get(voKey);
			models.addAll(list);
		}
		return models;
	}
	
	/**
	 * 递归加入并排序相关数据
	 * @param mctVoMap
	 * @param mcMap
	 * @param meMap
	 * @param msgComponentId
	 */
	private void putVos(Map<String, List<EcoreMessageComponentTracesVO>> mctVoMap, Map<String,EcoreMessageComponent> mcMap, 
			Map<String,List<EcoreMessageElement>> meMap, String msgComponentId) {
		List<EcoreMessageComponentTracesVO> mctVos = new ArrayList<>();
		EcoreMessageComponent mc = mcMap.get(msgComponentId);
		List<EcoreMessageComponentTracesVO> list = mctVoMap.get(mc.getName());
		if(list != null && list.size() > 0) {
			return;
		}
		//加入消息组件
		EcoreMessageComponentTracesVO mcVo = new EcoreMessageComponentTracesVO();
		mcVo.setMessageComponentName(mc.getName());
		mcVo.setTechnical(mc.getTechnical());
		mcVo.setTraceToBusinessComponent(mc.getTraceName());
		mctVos.add(mcVo);
		List<EcoreMessageElement> mes = meMap.get(mc.getId());
		if(mes != null && mes.size() > 0) {
			Map<String, EcoreBusinessElement> beMap = bes.stream().collect(Collectors.toMap(EcoreBusinessElement::getId, EcoreBusinessElement -> EcoreBusinessElement));
			//加入组件的消息要素
			for(EcoreMessageElement me :mes) {
				EcoreMessageComponentTracesVO emcVo = new EcoreMessageComponentTracesVO();
				emcVo.setMessageComponentName(mc.getName());
				emcVo.setMessageElementName(me.getName());
				emcVo.setTypeTracTo(me.getTypeOfTracesTo());
				emcVo.setTechnical(me.getTechnical());
				if(!StringUtils.isEmpty(me.getTrace())) {
					if("1".equals(me.getTraceType())) {
						EcoreBusinessElement ecoreBusinessElement = beMap.get(me.getTrace());
						if(ecoreBusinessElement != null) {
							String bcId = ecoreBusinessElement.getBusinessComponentId();
							EcoreBusinessComponent ecoreBusinessComponent = bcMap.get(bcId);
							emcVo.setTraceToBusinessComponent(ecoreBusinessComponent == null || ecoreBusinessComponent.getName() == null ? "" : ecoreBusinessComponent.getName());
							emcVo.setTraceToElement(ecoreBusinessElement.getName());
						}else {
							EcoreBusinessComponent ecoreBusinessComponent = bcMap.get(me.getTrace());
							emcVo.setTraceToBusinessComponent(ecoreBusinessComponent == null || ecoreBusinessComponent.getName() == null ? "" : ecoreBusinessComponent.getName());
						}
					}else if("2".equals(me.getTraceType())) {
						EcoreBusinessComponent ecoreBusinessComponent = bcMap.get(me.getTrace());
						emcVo.setTraceToBusinessComponent(ecoreBusinessComponent == null || ecoreBusinessComponent.getName() == null ? "" : ecoreBusinessComponent.getName());
						emcVo.setTraceToElement("");
					}
				}else {
					emcVo.setTraceToBusinessComponent("");
					emcVo.setTraceToElement("");
				}
				
				emcVo.setTracePath(me.getTracePath());
				mctVos.add(emcVo);
				String type = me.getType();
				String typeId = me.getTypeId();
				if(type.equals("2") && !StringUtils.isEmpty(typeId)) {
					putVos(mctVoMap, mcMap, meMap, typeId);
				}
			}
		}
		//加入有序map集合
		mctVoMap.put(mc.getName(), mctVos);
	}
	
	/**
	 * 写rows
	 * @param sheet
	 * @param node
	 */
	private static void setCellValue(HSSFSheet sheet,EcoreMessageComponentTracesVO v
			,HSSFCellStyle startStyle,HSSFCellStyle style,HSSFCellStyle endstyle) {
		HSSFRow row=sheet.createRow(index++);
		HSSFCell cell=row.createCell(0);
		cell.setCellStyle(startStyle);
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
			,HSSFCellStyle startStyle,HSSFCellStyle style,HSSFCellStyle endStyle, int type) {
		HSSFRow row=sheet.createRow(index++);
		HSSFCell cell=row.createCell(0);
		cell.setCellStyle(startStyle);
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
		cell.setCellStyle(style);
		cell.setCellValue(v.getTypeName());
		cell=row.createCell(5);
		cell.setCellStyle(endStyle);
		if(type == 0) {
			cell.setCellValue(v.getSuperName());
		}
	}

	/**
	 * 创建introductionSheet
	 * @param sheet
	 * @param name
	 */
	private void createIntroductionSheet(String name){
		//样式
		HSSFCellStyle style=createCellTitleStyle();
		HSSFSheet sheet=wb.createSheet("Introduction");
		sheet.setDisplayGridlines(false);
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(6);
		cell.setCellValue("Introduction");
		cell.setCellStyle(style);
		
		//描述
		final String introduction = "This document describes the Business Model Components and Elements used by the ";
		final String introduction2 = " message definitions.";
		final String introduction3 = "In the “Business Model” tab, the cells with text in green indicate the business concepts used by the ";
		final String introduction4 = "The “Traces” tab shows to which business concepts the ";
		final String introduction5 = " message components and message elements trace.";
		HSSFRow row1 = sheet.createRow(3);
		HSSFCell cell1 = row1.createCell(0);
		//第一行关键字加粗
		HSSFRichTextString rich1 = new HSSFRichTextString(introduction + name + introduction2);
		rich1.applyFont(0, introduction.length(), createFontIntroductionStyle());
		rich1.applyFont(introduction.length(), introduction.length() + name.length(), createFontBoldIntroductionStyle());
		rich1.applyFont(introduction.length() + name.length(), introduction.length() + name.length() + introduction2.length(), createFontIntroductionStyle());
		cell1.setCellValue(rich1);
		
		HSSFRow row2 = sheet.createRow(5);
		HSSFCell cell2 = row2.createCell(0);
		//第二行关键字加粗
		HSSFRichTextString rich2 = new HSSFRichTextString(introduction3 + name + introduction2);
		rich2.applyFont(0, introduction3.length(), createFontIntroductionStyle());
		rich2.applyFont(introduction3.length(), introduction3.length() + name.length() + introduction2.length(), createFontBoldIntroductionStyle());
		rich2.applyFont(introduction3.length() + name.length(), introduction3.length() + name.length() + introduction2.length(), createFontIntroductionStyle());
		cell2.setCellValue(rich2);
		
		HSSFRow row3 = sheet.createRow(7);
		HSSFCell cell3 = row3.createCell(0);
		//第三行关键字加粗
		HSSFRichTextString rich3 = new HSSFRichTextString(introduction4 + name + introduction5);
		rich3.applyFont(0, introduction4.length(), createFontIntroductionStyle());
		rich3.applyFont(introduction4.length(), introduction4.length() + name.length(), createFontBoldIntroductionStyle());
		rich3.applyFont(introduction4.length() + name.length(), introduction4.length() + name.length() + introduction5.length(), createFontIntroductionStyle());
		cell3.setCellValue(rich3);
	}
	
	/**
	 * 设置FullBusinessModel头
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
		cell.setCellValue("Business Element Type Name");
		cell.setCellStyle(createModelHeaderStyle());
		cell=rowTitle.createCell(5);
		cell.setCellValue("Name of Opposite End");
		cell.setCellStyle(createModelHeaderStyle());
	}
	
	/**
	 * 设置Traces头
	 * @param tracesSheet
	 */
	private void createTracesHeader(HSSFSheet sheet) {
		HSSFRow rowTitle=sheet.createRow(0);
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
		cell.setCellStyle(createModelHeaderStyle());
	}
	
	/**
	 * 加粗开始
	 */
	private static void createCellBoldStartStyle() {
		modelBoldStartStyle = wb.createCellStyle();
		cellStartStyle(modelBoldStartStyle, createFontBoldStyle());
	}
	
	/**
	 * 加粗
	 */
	private static void createCellBoldStyle() {
		modelBoldStyle = wb.createCellStyle();
		cellStyle(modelBoldStyle, createFontBoldStyle());
	}
	
	/**
	 * 加粗结束
	 */
	private static void createCellBoldEndStyle() {
		modelBoldEndStyle = wb.createCellStyle();
		cellEndStyle(modelBoldEndStyle, createFontBoldStyle());
	}
	
	/**
	 * 正常开始
	 */
	private static void createCellStartStyle() {
		modelStartStyle = wb.createCellStyle();
		cellStartStyle(modelStartStyle, createFontStyle());
	}
	
	/**
	 * 正常
	 */
	private static void createCellStyle() {
		modelStyle = wb.createCellStyle();
		cellStyle(modelStyle, createFontStyle());
	}
	
	/**
	 * 正常结束
	 */
	private static void createCellEndStyle() {
		modelEndStyle = wb.createCellStyle();
		cellEndStyle(modelEndStyle, createFontStyle());
	}
	
	/**
	 *  加粗绿色开始
	 */
	private static void createCellBoldGreenStartStyle() {
		modelBoldGreenStartStyle = wb.createCellStyle();
		cellStartStyle(modelBoldGreenStartStyle, createFontBoldGreenStyle());
	}
	
	/**
	 * 加粗绿色
	 */
	private static void createCellBoldGreenStyle() {
		modelBoldGreenStyle = wb.createCellStyle();
		cellStyle(modelBoldGreenStyle, createFontBoldGreenStyle());
	}
	
	/**
	 * 加粗绿色结束
	 */
	private static void createCellBoldGreenEndStyle() {
		modelBoldGreenEndStyle = wb.createCellStyle();
		cellEndStyle(modelBoldGreenEndStyle, createFontBoldGreenStyle());
	}
	
	/**
	 * 绿色开始
	 */
	private static void createCellGreenStartStyle() {
		modelGreenStartStyle = wb.createCellStyle();
		cellStartStyle(modelGreenStartStyle, createFontGreenStyle());
	}
	
	/**
	 * 绿色
	 */
	private static void createCellGreenStyle() {
		modelGreenStyle = wb.createCellStyle();
		cellStyle(modelGreenStyle, createFontGreenStyle());
	}
	
	/**
	 * 绿色结束
	 */
	private static void createCellGreenEndStyle() {
		modelGreenEndStyle = wb.createCellStyle();
		cellEndStyle(modelGreenEndStyle, createFontGreenStyle());
	}
	
	/**
	 * 简介一般字体
	 * @return
	 */
	private static HSSFFont createFontIntroductionStyle() {
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short)11);
		return font;
	}
	
	/**
	 * 简介加粗字体
	 * @return
	 */
	private static HSSFFont createFontBoldIntroductionStyle() {
		HSSFFont font = wb.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short)11);
		return font;
	}
	
	/**
	 * 加粗字体
	 * @return
	 */
	private static HSSFFont createFontBoldStyle() {
		HSSFFont font = wb.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short)10);
		return font;
	}
	
	/**
	 * 一般字体
	 * @return
	 */
	private static HSSFFont createFontStyle() {
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short)10);
		return font;
	}
	
	/**
	 * 加粗绿色字体
	 * @return
	 */
	private static HSSFFont createFontBoldGreenStyle() {
		HSSFFont font = wb.createFont();
		font.setBold(true);
		font.setColor(IndexedColors.GREEN.getIndex());
		font.setFontHeightInPoints((short)10);
		return font;
	}
	
	/**
	 * 一般绿色字体
	 * @return
	 */
	private static HSSFFont createFontGreenStyle() {
		HSSFFont font = wb.createFont();
		font.setColor(IndexedColors.GREEN.getIndex());
		font.setFontHeightInPoints((short)10);
		return font;
	}
	
	/**
	 * 标题头
	 * @return
	 */
	private static HSSFCellStyle createCellTitleStyle() {
		HSSFCellStyle style = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		font.setUnderline((byte)1);
		font.setFontHeightInPoints((short)22);
		style.setAlignment(HorizontalAlignment.LEFT);
		style.setFont(font);
		return style;
	}
	
	/**
	 * 表格头
	 * @return
	 */
	private static HSSFCellStyle createModelHeaderStyle() {
		HSSFCellStyle style = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short)10);
		font.setColor(IndexedColors.WHITE.getIndex());
		style.setFont(font);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
		return style;
	}
	
	/**
	 * 行开始
	 * @param cellStyle
	 * @param font
	 */
	private static void cellStartStyle(HSSFCellStyle cellStyle, HSSFFont font) {
		cellStyle(cellStyle, font);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setLeftBorderColor(IndexedColors.PALE_BLUE.getIndex());
	}
	
	/**
	 * 行结束
	 * @param cellStyle
	 * @param font
	 */
	private static void cellEndStyle(HSSFCellStyle cellStyle, HSSFFont font) {
		cellStyle(cellStyle, font);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setRightBorderColor(IndexedColors.PALE_BLUE.getIndex());
	}
	
	/**
	 * 行
	 * @param cellStyle
	 * @param font
	 */
	private static void cellStyle(HSSFCellStyle cellStyle, HSSFFont font) {
		cellStyle.setFont(font);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBottomBorderColor(IndexedColors.PALE_BLUE.getIndex());
		cellStyle.setTopBorderColor(IndexedColors.PALE_BLUE.getIndex());
	}
	
	public static void main(String[] args) {
		EcoreMessageSet ecoreMessageSet= new EcoreMessageSet();
		ecoreMessageSet.setId("_VkHWcRMPEeSTk5fcu7tr9w");
		ecoreMessageSet.setName("name");
		ecoreMessageSet.setPath("D:\\11.xls");
		MDR3Mgr mdr3Mgr = new MDR3Mgr(ecoreMessageSet);
		try {
			mdr3Mgr.generateMDR3Excel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
