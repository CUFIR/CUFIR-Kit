package org.cufir.rcp.kit.dialogs;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.cufir.plugin.mr.editor.SummaryProgressComposite;
import org.cufir.plugin.mr.handlers.IProcessListener;
import org.cufir.plugin.mr.handlers.MrRepositoryInitProcess;
import org.cufir.rcp.kit.Activator;
import org.cufir.rcp.kit.ApplicationWorkbenchAdvisor;
import org.cufir.rcp.kit.Configuration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.cfets.cufir.s.ide.utils.FileUtil;
import com.cfets.cufir.s.license.RSAUtils;

/**
 * 启动界面
 * 
 * @author hrj
 *
 */
public class SplashDialog implements IProcessListener  {

	private Shell shell;
	private int width = 600;
	private int height = 375;
	private int fontSize = 12;

	private Label lb2, lb21, lb23, yBtn;
	private Link lb22;
	
	private SummaryProgressComposite summaryProgressComposite;

	public SplashDialog(Shell s) {
		shell = s;
		selfAdapt();
		// 初始化UI控件
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

	private void initComp() {
		Display display = Display.getCurrent();
		Image splash = Activator.getImageDescriptor("icons/welcome_bg.png").createImage();
		Image logo = Activator.getImageDescriptor("icons/ISOEditorLogo_128.gif").createImage();
		Rectangle bounds = display.getBounds();
		shell.setBounds((bounds.width - width) / 2, (bounds.height - height) / 2, width, height);
		shell.setImage(logo);

		Composite parent = new Composite(shell, SWT.NONE);
		parent.setBackgroundMode(SWT.INHERIT_FORCE);
		parent.setBounds(0, 0, width, height);
		parent.setBackgroundImage(splash);
		
		Color white = new Color(display, 255, 255, 255);
		Color yellow = new Color(display, 238, 238, 0);
		
		summaryProgressComposite = new SummaryProgressComposite(parent);

		Label lb = new Label(parent, SWT.LEFT);
		lb.setBounds(265, 194, 400, 35);
		lb.setForeground(white);

		lb2 = new Label(parent, SWT.LEFT);
		lb2.setBounds(265, 230, 400, 35);
		lb2.setForeground(yellow);

		lb21 = new Label(parent, SWT.LEFT);
		lb21.setBounds(265, 230, 110, 35);
		lb21.setForeground(yellow);
		lb21.setVisible(false);

		lb22 = new Link(parent, SWT.LEFT | SWT.UNDERLINE_SQUIGGLE);
		lb22.setBounds(375, 230, 65, 23);
		lb22.setForeground(yellow);
		lb22.setText("点击跳转");
		lb22.setVisible(false);
		lb22.setToolTipText("点击跳转至官网");
		Label underline = new Label(parent, SWT.BORDER);
		underline.setBounds(373, 255, 65, 1);

		lb22.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.browse(new URI(Activator.CUFIR_URL));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

			@Override
			public void mouseDown(MouseEvent e) {
				underline.setVisible(false);
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {

			}
		});

		lb23 = new Label(parent, SWT.LEFT);
		lb23.setBounds(440, 230, 80, 35);
		lb23.setForeground(yellow);
		lb23.setText("至官网更新");
		lb23.setVisible(false);

		yBtn = new Label(parent, SWT.BORDER | SWT.CENTER);
		yBtn.setBounds(450, 300, 100, 30);
		yBtn.setText("确定");
		yBtn.setForeground(white);
		yBtn.setVisible(false);
		yBtn.setToolTipText("关闭");
		yBtn.addListener(SWT.MouseDown, p -> {
			yBtn.setBackground(white);
			yBtn.setForeground(new Color(display, 0, 0, 0));
		});
		yBtn.addListener(SWT.MouseUp, p -> {
			shell.close();
		});

		Font font = new Font(display, "微软雅黑", fontSize, SWT.BOLD);
		
		lb.setFont(font);
		yBtn.setFont(font);
		lb2.setFont(font);
		lb21.setFont(font);
		lb22.setFont(font);
		lb23.setFont(font);
	}

	private final static int EXPIRED_DAY = 7;

	private boolean openAble = true;
	
	public void openSplash(Display display) {
		try {
			String publisher = Configuration.getInstance().getValue("cufir.publisher");
			String deptcode = Configuration.getInstance().getValue("cufir.deptcode");
			String expired = Configuration.getInstance().getValue("cufir.expired");
			String sign = Configuration.getInstance().getSign("cufir.signature");
			String text = publisher + "" + deptcode + "" + expired;
			// 1 公钥验证签名是否过期
			if (valid(text, sign)) {
				// 签名未过期
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				Date parse = sdf.parse(expired);
				long dateSpace = getDateSpace(sdf.parse(sdf.format(new Date())), parse);
				// 2 密钥是否在有效期内，并且剩余天数大于EXPIRED_DAY
				if (dateSpace <= EXPIRED_DAY) {
					if (dateSpace <= 0) {
						// 3 产品试用期已到期
						showButton();
						openAble = false;
					} else {
						// 4 密钥未过期，但是剩余过期天数少于 EXPIRED_DAY 天
						lb2.setText("离试用期结束还有" + dateSpace + "天");
					}
				}
			} else {
				// 签名已过期
				showButton();
				openAble = false;
			}
			shell.open();
			// 初始化MR
			initMr();
			PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initMr() {
		// 同步
		MrRepositoryInitProcess mip = new MrRepositoryInitProcess(this, summaryProgressComposite);
		mip.process();
	}
	
	private void closeShell() {
		try {
			if (openAble) {
				shell.close();
			}
			while (!shell.isDisposed()) {// 窗口帧是否关闭
				if (!shell.getDisplay().readAndDispatch()) {// 是否还有任务未完成
					shell.getDisplay().sleep();
				}
			}
			shell.dispose();// 释放系统资源
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void showButton() {
		summaryProgressComposite.getPb().setVisible(false);
		summaryProgressComposite.getNoteLb().setVisible(false);
		summaryProgressComposite.getCountLb().setVisible(false);
		lb2.setVisible(false);
		lb21.setVisible(true);
		lb22.setVisible(true);
		lb23.setVisible(true);
		yBtn.setVisible(true);
	}

	/**
	 * Md5 验证
	 * 
	 * @param text  明文（机构代码 ）
	 * @param key   密钥
	 * @param miWen 密文
	 * @return
	 */
	private boolean valid(String text, String sign) {

		try {
			// 1 获取公钥
			String publicKey = FileUtil.getClassPath(this.getClass(), "src/main/resources/publicKey.license");
			// 2 验签
			boolean verify = RSAUtils.verify(text.getBytes(), publicKey, sign);
			return verify;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	///////////////////////////////////////////////////////////////
	/**
	 * 获得两个日期之间的天数差
	 * 
	 * @param pre
	 * @param next
	 * @return
	 */
	public static long getDateSpace(Date pre, Date next) {
		long tm1 = pre.getTime();
		long tm2 = next.getTime();
		long days = (tm2 - tm1) / (1000 * 3600 * 24);
		long l = (tm2 - tm1) % (1000 * 3600 * 24);
		if (days == 0 && l > 0) {
			days = 1;
		}
		return days + 1;

	}

	public static void main(String[] args) {
		try {
			String md5Hex = DigestUtils.md5Hex("CFETS1045820191001");
			System.out.println(md5Hex);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onEnd() {
		closeShell();
	}

	@Override
	public void onProcessing(String info, int percent) {
		// TODO Auto-generated method stub
		
	}

}
