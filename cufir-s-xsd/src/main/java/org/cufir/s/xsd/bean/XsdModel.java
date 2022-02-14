package org.cufir.s.xsd.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * Xsd文件-》XsdModel，封裝Xsd文件内各个元素封装列表
 * @author tangmaoquan
 */
@Data
public class XsdModel {

	/**
	 * 命名空间
	 */
	private String namespaces = "";
	
	/**
	 * 文件名
	 */
	private String fileName = "";
	
	/**
	 * 文件名tag
	 */
	private String fileName_xmlTag = "";
	
	/**
	 * xsd表层元素列表
	 */
	private List<ElementBean> datas = new ArrayList<ElementBean>();
	
	/**
	 * xsd复杂节点元素列表
	 */
	private List<ElementBean> complexnodes = new ArrayList<ElementBean>();
	
	/**
	 * xsd简单类型元素列表
	 */
	private List<ElementBean> simplenodes = new ArrayList<ElementBean>();
	
	/**
	 * xsd简单元素名和元素列表对应map
	 */
	private Map<String, List<ElementBean>> simpleMaps = new HashMap<String, List<ElementBean>>();
	
	/**
	 * xsd复杂元素名和元素列表对应map
	 */
	private Map<String, List<ElementBean>> complexMaps = new HashMap<String, List<ElementBean>>();
	
	/**
	 * xsd复杂类型元素map
	 */
	private Map<String, String> compentMaps = new HashMap<String, String>();
	
	/**
	 * xsd简单类型元素id和元素对应map
	 */
	private Map<String, List<ElementBean>> dataTypeMaps = new HashMap<String, List<ElementBean>>();
	
	/**
	 * xsd schema的id name对应map
	 */
	private Map<String, String> schemaMaps = new HashMap<String, String>();
	
	/**
	 * xsd简单数据类型元素id name对应map
	 */
	private Map<String, String> dataTypeNameMaps = new HashMap<String, String>();

	/**
	 * 清空数据
	 */
	public void clean() {
		complexnodes.clear();
		simplenodes.clear();
		simpleMaps.clear();
		complexMaps.clear();
		compentMaps.clear();
		dataTypeMaps.clear();
		schemaMaps.clear();
		dataTypeNameMaps.clear();
	}
}
