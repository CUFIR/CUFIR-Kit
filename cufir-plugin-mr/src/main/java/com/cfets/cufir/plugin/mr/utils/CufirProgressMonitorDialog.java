package com.cfets.cufir.plugin.mr.utils;

import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

/**
 * 自定义 ProgressMonitorDialog 
 * @author TangTao
 *
 */
public class CufirProgressMonitorDialog  extends ProgressMonitorDialog{
	
	public static String DEFAULT_IMAGE = "icons/data.gif";
	public static String DIALOG_IMAGE = "icons/data2.png";
	
	private String title;
	
	private String image;

//	public CufirProgressMonitorDialog() {
//		super(null);
//	}
//	
	public CufirProgressMonitorDialog(String title) {
		super(null);
		this.title=title;
	}
	
	public CufirProgressMonitorDialog(String title,String image) {
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
		shell.setImage(DataUtil.getImage(DIALOG_IMAGE));
	}
	
	/**
	 * 设置 Dialog 图标
	 */
	@Override
	protected Image getImage() {
		if (DataUtil.isBlank(image)) {
			// 如果图片为空则返回默认图片
			return DataUtil.getImage(DEFAULT_IMAGE);
		}
		return DataUtil.getImage(image);
	}


}
