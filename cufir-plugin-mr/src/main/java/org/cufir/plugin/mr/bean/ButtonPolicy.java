package org.cufir.plugin.mr.bean;

import org.eclipse.swt.graphics.Image;

import lombok.Data;

/**
 * 按钮原则
 * @author tangmaoquan
 * @Date 2022年2月8日
 */
@Data
public class ButtonPolicy {

	/**
	 * 一般浅色
	 */
	public static final int BUTTON_TYPE_NONE_LIGHT = 0;
	
	/**
	 * 一般深色
	 */
	public static final int BUTTON_TYPE_NONE_DEEP = 1;
	
	/**
	 * 弹窗浅色
	 */
	public static final int BUTTON_TYPE_PUSH_LIGHT = 2;
	
	/**
	 * 弹窗深色
	 */
	public static final int BUTTON_CONTENT_TYPE_PUSH_DEEP = 3;
	
	/**
	 * 弹窗深色
	 */
	public static final int BUTTON_SUMMARY_TYPE_PUSH_DEEP = 4;
	
	/**
	 * 内容勾选框
	 */
	public static final int BUTTON_CONTENT_TYPE_CHECK = 5;
	
	/**
	 * 简介勾选框
	 */
	public static final int BUTTON_SUMMARY_TYPE_CHECK = 6;
	
	/**
	 * 按钮类型
	 */
	private int buttonType;
	
	/**
	 * 起始高度
	 */
	private int startHeight;
	
	/**
	 * 起始宽度
	 */
	private int startWidth;
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 选择
	 */
	private Boolean selection;
	
	/**
	 * 图片
	 */
	private Image image;
	
	/**
	 * 是否可编辑
	 */
	private Boolean textEditable = true;
	
	public ButtonPolicy(int buttonType, int startHeight, String name, Boolean selection){
		this.buttonType = buttonType;
		this.name = name;
		this.startHeight = startHeight;
		this.selection = selection;
	}
	
	public ButtonPolicy(int buttonType, String name, Boolean textEditable){
		this.buttonType = buttonType;
		this.name = name;
		this.textEditable = textEditable;
	}
	
	public ButtonPolicy(int buttonType, Image image, Boolean textEditable){
		this.buttonType = buttonType;
		this.image = image;
		this.textEditable = textEditable;
	}
	
	public ButtonPolicy(int buttonType, String name){
		this.buttonType = buttonType;
		this.name = name;
	}
	
	public ButtonPolicy(int buttonType, Image image){
		this.buttonType = buttonType;
		this.image = image;
	}
	
	public ButtonPolicy(int buttonType){
		this.buttonType = buttonType;
	}
}
