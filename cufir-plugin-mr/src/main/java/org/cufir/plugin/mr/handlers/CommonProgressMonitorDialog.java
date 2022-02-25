package org.cufir.plugin.mr.handlers;

import org.cufir.plugin.mr.utils.ImgUtil;
import org.cufir.plugin.mr.utils.NumberUtil;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

/**
 * 自定义 ProgressMonitorDialog 
 * @author TangTao、tangmaoquan
 * @Date 2021年10月15日
 */
public class CommonProgressMonitorDialog  extends ProgressMonitorDialog{
	
	public static String DEFAULT_IMAGE = "icons/data.gif";
	public static String DIALOG_IMAGE = "icons/data2.png";
	
	private String title;
	
	private String image;

//	public CufirProgressMonitorDialog() {
//		super(null);
//	}
//	
	public CommonProgressMonitorDialog(String title) {
		super(null);
		this.title=title;
	}
	
	public CommonProgressMonitorDialog(String title,String image) {
		super(null);
		this.title=title;
		this.image=image;
	}
	
	/**
	 * 配置 Diglog 样式标题
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(title); 
		shell.setCursor(shell.getDisplay().getSystemCursor(SWT.CURSOR_WAIT));
		shell.setImage(ImgUtil.getImage(DIALOG_IMAGE));
	}
	
	/**
	 * 设置 Dialog 图标
	 */
	@Override
	protected Image getImage() {
		if (NumberUtil.isBlank(image)) {
			// 如果图片为空则返回默认图片
			return ImgUtil.getImage(DEFAULT_IMAGE);
		}
		return ImgUtil.getImage(image);
	}
}
