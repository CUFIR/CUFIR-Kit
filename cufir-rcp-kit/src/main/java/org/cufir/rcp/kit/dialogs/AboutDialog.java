package org.cufir.rcp.kit.dialogs;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.cufir.rcp.kit.Activator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * About Cufir
 * 
 * @author hrj
 *
 */
public class AboutDialog extends Dialog {

	private Shell shell;
	private int width = 500;
	private int height = 312;
	private int fontSize = 12;
	
	private Label desLb;
	private Label versionLb2;
	private Label cdLb2;
	private Label abbrLb2;

	private void setText() {
		desLb.setText(Activator.CUFIR_NAME);
		versionLb2.setText(Activator.CUFIR_VERSION);
		cdLb2.setText(Activator.CUFIR_DATE);
//		abbrLb2.setText(Activator.CUFIR_ABBR);

	}

	public AboutDialog(Shell p) {
		super(p);
		this.shell = p;
		selfAdapt();
		initComp();
	}
	
	
	/**
	 * 自适应不同的分辨率
	 */
	private void selfAdapt() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width2 = screenSize.width;
		if(width2 >= 1900) {
			fontSize = 11;
		}
	}

	/**
	 * 布局
	 */
	private void initComp() {
		Display display = Display.getCurrent();
		Image logo = Activator.getImageDescriptor("icons/ISOEditorLogo_128.gif").createImage();
		Image about = Activator.getImageDescriptor("icons/about_bg.png").createImage();
		Rectangle bounds = display.getBounds();
		shell.setBounds((bounds.width - width) / 2, (bounds.height - height) / 2, width, height);
		shell.setText("About");
		shell.setImage(logo);

		Composite parent = new Composite(shell, SWT.NONE);
		parent.setBackgroundMode(SWT.INHERIT_FORCE);
		parent.setBounds(0, 0, width, height);
		parent.setBackgroundImage(about);

		Font zhFont = new Font(display, "微软雅黑", fontSize, SWT.None);
		Font enFont = new Font(display, "Arial", fontSize, SWT.BOLD);
		Color white = new Color(display, 255, 255, 255);

		desLb = new Label(parent, SWT.WRAP);
		desLb.setBounds(232, 110, 250, 30);
		desLb.setForeground(white);
		desLb.setFont(enFont);

		Label versionLb = new Label(parent, SWT.None);
		versionLb.setBounds(232, 150, 40, 25);
		versionLb.setText("版本:");
		versionLb.setForeground(white);
		versionLb.setFont(zhFont);
		versionLb2 = new Label(parent, SWT.None);
		versionLb2.setBounds(272, 150, 260, 25);
		versionLb2.setForeground(white);
		versionLb2.setFont(zhFont);

		Label cdLb = new Label(parent, SWT.None);
		cdLb.setBounds(232, 180, 70, 25);
		cdLb.setText("建造日期:");
		cdLb.setForeground(white);
		cdLb.setFont(zhFont);
		cdLb2 = new Label(parent, SWT.None);
		cdLb2.setBounds(302, 180, 230, 25);
		cdLb2.setForeground(white);
		cdLb2.setFont(zhFont);

		Label abbrLb = new Label(parent, SWT.None);
		abbrLb.setBounds(232, 210, 40, 25);
//		abbrLb.setText("缩写:");
		abbrLb.setForeground(white);
		abbrLb.setFont(zhFont);
		abbrLb2 = new Label(parent, SWT.None);
		abbrLb2.setBounds(272, 210, 270, 25);
		abbrLb2.setForeground(white);
		abbrLb2.setFont(zhFont);
	}
	

	/**
	 * 打开
	 */
	public void open() {

		setText();

		
		shell.setFocus();
		shell.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				shell.close();
			}

			@Override
			public void focusGained(FocusEvent e) {
				
			}
		});
		
		shell.open();
		while (!shell.isDisposed()) {// 窗口帧是否关闭
			if (!shell.getDisplay().readAndDispatch()) {// 是否还有任务未完成
				shell.getDisplay().sleep();
			}
		}
		shell.dispose();// 释放系统资源

	}

}
