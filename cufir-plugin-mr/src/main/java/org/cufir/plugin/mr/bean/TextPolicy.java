package org.cufir.plugin.mr.bean;

import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseListener;

import lombok.Data;

/**
 * 文本原则
 * @author tangmaoquan_ntt
 * @since 3.2.1.0
 * @Date 2022年1月25日
 */
@Data
public class TextPolicy {

	/**
	 * 文本按钮
	 */
	public static final int TEXT_BUTTON_TYPE = 0;
	
	/**
	 * 一般文本
	 */
	public static final int TEXT_COMMONLY_TYPE = 1;
	
	/**
	 * 富文本
	 */
	public static final int TEXT_SCROLL_TYPE = 2;
	
	/**
	 * 短文本
	 */
	public static final int TEXT_SHORT_TYPE = 3;
	
	/**
	 *标题
	 */
	public static final int TEXT_TITLE_TYPE = 4;
	
	/**
	 * 描述
	 */
	public static final int TEXT_NOTE_TYPE = 5;
	
	/**
	 * 内容一般文本
	 */
	public static final int TEXT_CONTENT_COMMONLY_TYPE = 6;
	
	/**
	 * 内容富文本
	 */
	public static final int TEXT_CONTENT_SCROLL_TYPE = 7;
	
	/**
	 * 内容描述
	 */
	public static final int TEXT_CONTENT_NOTE_ONE_TYPE = 8;
	
	/**
	 * 内容描述
	 */
	public static final int TEXT_CONTENT_NOTE_TWO_TYPE = 9;
	
	/**
	 * 内容短文本
	 */
	public static final int TEXT_CONTENT_SHORT_TYPE = 10;
	
	/**
	 * 内容文本搜索
	 */
	public static final int TEXT_CONTENT_BUTTON_SELECT_TYPE = 11;
	
	/**
	 * 文本类型
	 */
	private int textType;
	
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
	 * 文本名称
	 */
	private String text;
	
	/**
	 * 文本监听
	 */
	private ModifyListener textListener;
	
	/**
	 * 按钮监听
	 */
	private MouseListener buttonListener;
	
	
	public TextPolicy(int textType, int startHeight, String label, Boolean editable, String text, ModifyListener textListener, MouseListener buttonListener){
		this.textType = textType;
		this.startHeight = startHeight;
		this.label = label;
		this.editable = editable;
		this.text = text;
		this.textListener = textListener;
		this.buttonListener = buttonListener;
	}
	
	public TextPolicy(int textType, int startHeight, String label, Boolean editable, String text, ModifyListener textListener){
		this.textType = textType;
		this.startHeight = startHeight;
		this.label = label;
		this.editable = editable;
		this.text = text;
		this.textListener = textListener;
	}
	
	public TextPolicy(int textType, int startHeight, String label, Boolean editable, String text){
		this.textType = textType;
		this.startHeight = startHeight;
		this.label = label;
		this.editable = editable;
		this.text = text;
	}
	
	public TextPolicy(int textType, int startHeight, String label, String text){
		this.textType = textType;
		this.startHeight = startHeight;
		this.label = label;
		this.text = text;
	}
	
	public TextPolicy(int textType, int startHeight, String label){
		this.textType = textType;
		this.startHeight = startHeight;
		this.label = label;
	}
	
}
