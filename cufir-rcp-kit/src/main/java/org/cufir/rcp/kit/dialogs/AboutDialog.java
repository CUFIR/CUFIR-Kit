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

import org.cufir.s.ide.utils.i18n.I18nApi;

/**
 * About Cufir
 * @author hrj
 * @Date 2021年10月15日
 */
public class AboutDialog extends Dialog {

	private Shell shell;
	private int width = 500;
	private int height = 312;
	private int fontSize = 12;
	
	private Label desLb;
	private Label versionLb;
	private Label cdLb;

	private void setText() {
		desLb.setText(Activator.CUFIR_NAME);
		versionLb.setText(I18nApi.get("about.version") + Activator.CUFIR_VERSION);
		cdLb.setText(I18nApi.get("about.date") + Activator.CUFIR_DATE);
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

		versionLb = new Label(parent, SWT.None);
		versionLb.setBounds(232, 150, 300, 25);
		versionLb.setForeground(white);
		versionLb.setFont(zhFont);

		cdLb = new Label(parent, SWT.None);
		cdLb.setBounds(232, 180, 300, 25);
		cdLb.setForeground(white);
		cdLb.setFont(zhFont);
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
