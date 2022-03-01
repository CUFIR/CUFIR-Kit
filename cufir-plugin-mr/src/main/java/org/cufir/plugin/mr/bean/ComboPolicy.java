package org.cufir.plugin.mr.bean;

import java.util.List;

import lombok.Data;

/**
 * 下拉原则
 * @author tangmaoquan
 * @Date 2022年2月8日
 */
@Data
public class ComboPolicy {
	
	/**
	 * 一般
	 */
	public static final int COMBO_COMMONLY_TYPE = 0;
	
	/**
	 * 内容
	 */
	public static final int COMBO_CONTENT_TYPE = 1;
	
	/**
	 * 下拉类型
	 */
	private int comboType;
	
	/**
	 * 起始高度
	 */
	private int startHeight;
	
	/**
	 * Lab名称
	 */
	private String label;
	
	/**
	 * 是否可编辑
	 */
	private Boolean editable;
	
	/**
	 * 选择集合
	 */
	private List<String> list;
	
	/**
	 * 选择文本
	 */
	private String comboText;
	
	public ComboPolicy(int comboType, int startHeight, String label, Boolean editable, List<String> list, String comboText){
		this.comboType = comboType;
		this.startHeight = startHeight;
		this.label = label;
		this.editable = editable;
		this.list = list;
		this.comboText = comboText;
	}
	
	public ComboPolicy(int comboType, int startHeight, String label, Boolean editable, String comboText){
		this.comboType = comboType;
		this.startHeight = startHeight;
		this.label = label;
		this.editable = editable;
		this.comboText = comboText;
	}
}
