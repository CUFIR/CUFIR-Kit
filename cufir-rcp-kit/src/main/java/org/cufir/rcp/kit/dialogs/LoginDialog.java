package org.cufir.rcp.kit.dialogs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.cufir.rcp.kit.Activator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 登陆界面
 */
public class LoginDialog extends Dialog {

	private Shell shell;
	private int width = 750;
	private int height = 460;

	private static Map<Integer, String> roles = new ConcurrentHashMap<Integer, String>();
	private final static int ROLE_COUNT = 4;

	static {
		roles.put(0, "SI");
		roles.put(1, "RA");
		roles.put(2, "RMG");
		roles.put(3, "SEG");
	}

	public LoginDialog(Shell s) {
		super(s);
		shell = s;
		initComp();
	}

	private void initComp() {
		Display display = Display.getCurrent();
		Image login = Activator.getImageDescriptor("icons/login.bmp").createImage();
		Image logo = Activator.getImageDescriptor("icons/ISOEditorLogo_16.gif").createImage();
		shell.setBackgroundImage(login);
		Rectangle bounds = display.getBounds();
		shell.setBounds((bounds.width - width) / 2, (bounds.height - height) / 2, width, height);
		shell.setImage(logo);
		
		Composite parent = new Composite(shell, SWT.NONE);
		parent.setBackgroundMode(SWT.INHERIT_FORCE);
		parent.setBounds(0, 0, width, height);
		parent.setBackgroundImage(login);

		Label usernameLb = new Label(parent, SWT.LEFT);
		usernameLb.setBounds(380, 140, 300, 30);
		usernameLb.setText("用户名");

		Text usernameText = new Text(parent, SWT.BORDER);
		usernameText.setBounds(380, 170, 300, 30);

		Label roleLb = new Label(parent, SWT.LEFT);
		roleLb.setBounds(380, 210, 300, 30);
		roleLb.setText("用户名");

		Combo roleCombo = new Combo(parent, SWT.None);
		roleCombo.setBounds(380, 240, 300, 30);
		for (int i = 0; i < ROLE_COUNT; i++) {
			roleCombo.add(roles.get(i), i);
			if (i == 0) {
				roleCombo.select(0);
			}
		}

		Label loginBtn = new Label(parent, SWT.CENTER | SWT.BORDER | SWT.BORDER_SOLID);
		loginBtn.setBounds(380, 290, 140, 30);
		loginBtn.setText("登陆");

		Label cancleBtn = new Label(parent, SWT.CENTER | SWT.BORDER | SWT.BORDER_SOLID);
		cancleBtn.setBounds(540, 290, 140, 30);
		cancleBtn.setText("取消");

		Font font = new Font(display, "微软雅黑", 12, SWT.BOLD);
		Color color = new Color(display, 255, 255, 255);
		Color blue = new Color(display, 0, 178, 238);
		
		usernameLb.setFont(font);
		usernameLb.setForeground(color);

		usernameText.setFont(font);
		usernameText.setForeground(color);

		roleLb.setFont(font);
		roleLb.setForeground(color);

		roleCombo.setFont(font);
		roleCombo.setForeground(color);

		loginBtn.setFont(font);
		loginBtn.setForeground(blue);
		loginBtn.setBackground(color);
		
		cancleBtn.setFont(font);
		cancleBtn.setForeground(color);

	}

	public void openLogin() {
		shell.open();

		while (!shell.isDisposed()) {// 窗口帧是否关闭
			if (!shell.getDisplay().readAndDispatch()) {// 是否还有任务未完成
				shell.getDisplay().sleep();
			}
		}
		shell.dispose();// 释放系统资源

	}

}
