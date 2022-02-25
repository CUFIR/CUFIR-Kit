package org.cufir.plugin.mr.editor;

import org.cufir.plugin.mr.bean.ButtonPolicy;
import org.cufir.plugin.mr.bean.TextPolicy;
import org.cufir.plugin.mr.utils.SystemUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.springframework.util.StringUtils;

/**
 * Summary文本编辑组件封装
 * @author tangmaoquan
 * @Date 2021年10月15日
 */
public class SummaryRowTextComposite {
	
	/**
	 * 浅色
	 */
	public static final int BUTTON_LIGHT_COLOR = 0;
	
	/**
	 * 深色
	 */
	public static final int BUTTON_DEEP_COLOR = 1;

	private Text text;
	
	private Button button;
	
	public Text getText() {
		return text;
	}
	
	public Button getButton() {
		return button;
	}
	
	/**
	 * 文本组件
	 * @param com
	 * @param labelText
	 * @param textEditable 是否可編輯
	 * @param textText
	 * @param modifyListener
	 * @param type 封装组件类型
	 */
	public SummaryRowTextComposite(Composite com, TextPolicy tp) {
		text = null;
		Label nameLabel = new Label(com, SWT.NONE);
		nameLabel.setText(tp.getLabel());
		nameLabel.setBounds(10, tp.getStartHeight(), 200, 25);
		switch(tp.getTextType()){
			case TextPolicy.TEXT_BUTTON_TYPE:
				//生成名称组合
				text = new Text(com, SWT.BORDER);
				text.setBounds(220, tp.getStartHeight(), 480, 25);
				Button renameButton = new Button(com, SWT.NONE);
				renameButton.setBounds(715, tp.getStartHeight(), 120, 25);
				renameButton.setText("Assign Fresh Name");
				renameButton.setBackground(new Color(Display.getCurrent(), 201, 218, 248));// rgb值
				if(tp.getEditable() != null) {
					renameButton.setEnabled(tp.getEditable());
				}
				renameButton.addMouseListener(tp.getButtonListener());
				break;
			case TextPolicy.TEXT_COMMONLY_TYPE:
				//一般输入框组合
				text = new Text(com, SWT.BORDER);
				text.setBounds(220, tp.getStartHeight(), 620, 25);
				break;
			case TextPolicy.TEXT_SCROLL_TYPE:
				//富文本组合
				text = new Text(com, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
				text.setBounds(220, tp.getStartHeight(), 620, 200);
				break;
			case TextPolicy.TEXT_SHORT_TYPE:
				//短输入框组合
				text = new Text(com, SWT.BORDER);
				text.setBounds(220, tp.getStartHeight(), 200, 25);
				break;
			case TextPolicy.TEXT_TITLE_TYPE:
				//标题
				nameLabel.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
				nameLabel.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
				nameLabel.setBounds(10, tp.getStartHeight(), 400, 30);
				break;
			case TextPolicy.TEXT_NOTE_TYPE:
				//描述
				nameLabel.setBounds(10, tp.getStartHeight(), 800, 25);
				break;
			case TextPolicy.TEXT_BUTTON_SELECT_TYPE:
				//文本搜索框组合
				text = new Text(com, SWT.BORDER);
				text.setBounds(220, tp.getStartHeight(), 510, 25);
				break;
			case TextPolicy.TEXT_CONTENT_COMMONLY_TYPE:
				//内容一般输入框组合
				nameLabel.setBounds(10, tp.getStartHeight(), 150, 25);
				text = new Text(com, SWT.BORDER);
				text.setBounds(160, tp.getStartHeight(), 330, 25);
				break;
			case TextPolicy.TEXT_CONTENT_SCROLL_TYPE:
				//内容富文本组合
				nameLabel.setBounds(10, tp.getStartHeight(), 150, 25);
				text = new Text(com, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
				text.setBounds(160, tp.getStartHeight(), 330, 150);
				break;
			case TextPolicy.TEXT_CONTENT_NOTE_ONE_TYPE:
				//内容一行描述
				nameLabel.setBounds(10, tp.getStartHeight(), 490, 25);
				break;
			case TextPolicy.TEXT_CONTENT_NOTE_TWO_TYPE:
				//内容两行描述
				nameLabel.setBounds(10, tp.getStartHeight(), 490, 40);
				break;
			case TextPolicy.TEXT_CONTENT_SHORT_TYPE:
				//内容短输入框组合
				nameLabel.setBounds(10, tp.getStartHeight(), 150, 25);
				text = new Text(com, SWT.BORDER);
				text.setBounds(160, tp.getStartHeight(), 20, 23);
				break;
			case TextPolicy.TEXT_CONTENT_BUTTON_SELECT_TYPE:
				//内容文本搜索框组合
				nameLabel.setBounds(10, tp.getStartHeight(), 150, 25);
				text = new Text(com, SWT.BORDER);
				text.setBounds(160, tp.getStartHeight(), 220, 25);
				break;
			default:
				break;
		}
		if(text != null) {
			text.setText(tp.getText() == null ? "" : tp.getText());
			text.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
			if(tp.getEditable() != null) {
				text.setEditable(tp.getEditable());
			}
			if(tp.getTextListener() != null) {
				text.addModifyListener(tp.getTextListener());
			}
		}
	}
	
	/**
	 * 按钮
	 * @param com
	 * @param buttonPolicy
	 */
	public SummaryRowTextComposite(Composite com, ButtonPolicy bp) {
		switch(bp.getButtonType()){
		case ButtonPolicy.BUTTON_TYPE_NONE_LIGHT:
			button = new Button(com, SWT.NONE);
			button.setBackground(new Color(Display.getCurrent(), 240, 249, 253));
			break;
		case ButtonPolicy.BUTTON_CONTENT_TYPE_CHECK:
			Label nameLabel = new Label(com, SWT.NONE);
			nameLabel.setText(bp.getName());
			nameLabel.setBounds(10, bp.getStartHeight(), 150, 25);
			button = new Button(com, SWT.CHECK);
			button.setBackground(new Color(Display.getCurrent(), 240, 249, 253));
			if(bp.getSelection() != null) {
				button.setSelection(bp.getSelection());
			}
			button.setBounds(160, bp.getStartHeight() + 2, 15, 15);
			break;
		case ButtonPolicy.BUTTON_SUMMARY_TYPE_CHECK:
			Label summaryLabel = new Label(com, SWT.NONE);
			summaryLabel.setText(bp.getName());
			summaryLabel.setBounds(10, bp.getStartHeight(), 200, 25);
			button = new Button(com, SWT.CHECK);
			button.setBackground(new Color(Display.getCurrent(), 240, 249, 253));
			if(bp.getSelection() != null) {
				button.setSelection(bp.getSelection());
			}
			button.setBounds(220, bp.getStartHeight() + 2, 15, 15);
			break;
		case ButtonPolicy.BUTTON_TYPE_NONE_DEEP:
			button = new Button(com, SWT.NONE);
			button.setBackground(new Color(Display.getCurrent(), 201, 218, 248));
			break;
		case ButtonPolicy.BUTTON_TYPE_PUSH_LIGHT:
			button = new Button(com, SWT.PUSH);
			button.setBackground(new Color(Display.getCurrent(), 240, 249, 253));
			break;
		case ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP:
			button = new Button(com, SWT.PUSH);
			button.setBackground(new Color(Display.getCurrent(), 240, 218, 248));
			break;
		case ButtonPolicy.BUTTON_SUMMARY_TYPE_PUSH_DEEP:
			button = new Button(com, SWT.PUSH);
			button.setBackground(new Color(Display.getCurrent(), 201, 218, 248));
			break;
		default:
			button = new Button(com, SWT.CHECK);
			button.setBackground(new Color(Display.getCurrent(), 240, 249, 253));
			break;
		}
		if(bp.getImage() != null) {
			button.setImage(bp.getImage());
		}
		if(!StringUtils.isEmpty(bp.getName())) {
			button.setText(bp.getName());
		}
		button.setEnabled(bp.getTextEditable());
	}
}
