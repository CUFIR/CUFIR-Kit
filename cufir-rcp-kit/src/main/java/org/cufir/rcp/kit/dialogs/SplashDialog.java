package org.cufir.rcp.kit.dialogs;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.URI;
import java.util.Date;

import org.cufir.plugin.mr.editor.SummaryProgressComposite;
import org.cufir.plugin.mr.handlers.IProcessListener;
import org.cufir.plugin.mr.handlers.MrRepositoryInitProcess;
import org.cufir.rcp.kit.Activator;
import org.cufir.rcp.kit.ApplicationWorkbenchAdvisor;
import org.cufir.s.ide.utils.i18n.I18nApi;
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

/**
 * 启动界面
 * @author hrj、tangmaoquan
 * @Date 2021年10月15日
 */
public class SplashDialog implements IProcessListener  {

	private Shell shell;
	private int width = 600;
	private int height = 375;
	private int fontSize = 12;

	private Label dateSpaceLb, tipsLb, btnLb;
	private Link officialLk;
	
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
		//获取图片
		Image splash = Activator.getImageDescriptor("icons/welcome_bg.png").createImage();
		Image logo = Activator.getImageDescriptor("icons/ISOEditorLogo_128.gif").createImage();
		
		Display display = Display.getCurrent();
		Rectangle bounds = display.getBounds();
		shell.setBounds((bounds.width - width) / 2, (bounds.height - height) / 2, width, height);
		shell.setImage(logo);

		Composite parent = new Composite(shell, SWT.NONE);
		parent.setBackgroundMode(SWT.INHERIT_FORCE);
		parent.setBounds(0, 0, width, height);
		parent.setBackgroundImage(splash);
		summaryProgressComposite = new SummaryProgressComposite(parent);
		
		//设置颜色
		Color white = new Color(display, 255, 255, 255);
		Color yellow = new Color(display, 238, 238, 0);
		
		Label version = new Label(parent, SWT.LEFT);
		
		String currentLan = I18nApi.getCurrentLan();
		if("i18n-cn".equals(currentLan)) {
			version.setBounds(width - 135, 5, 130, 25);
		}else {
			version.setBounds(width - 185, 5, 180, 25);
		}
		version.setForeground(white);
		version.setText(I18nApi.get("splash.version")+ "V" + Activator.CUFIR_VERSION);

		dateSpaceLb = new Label(parent, SWT.LEFT);
		dateSpaceLb.setBounds(265, 230, 400, 35);
		dateSpaceLb.setForeground(yellow);

		officialLk = new Link(parent, SWT.LEFT | SWT.UNDERLINE_SQUIGGLE);
		officialLk.setBounds(375, 230, 65, 23);
		officialLk.setForeground(yellow);
		officialLk.setText("点击跳转");
		officialLk.setVisible(false);
		officialLk.setToolTipText("点击跳转至官网");
		Label underline = new Label(parent, SWT.BORDER);
		underline.setBounds(373, 255, 65, 1);
		officialLk.addMouseListener(new MouseListener() {
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

		tipsLb = new Label(parent, SWT.LEFT);
		tipsLb.setBounds(440, 230, 80, 35);
		tipsLb.setForeground(yellow);
		tipsLb.setText("至官网更新");
		tipsLb.setVisible(false);

		btnLb = new Label(parent, SWT.BORDER | SWT.CENTER);
		btnLb.setBounds(450, 300, 100, 30);
		btnLb.setText("确定");
		btnLb.setForeground(white);
		btnLb.setVisible(false);
		btnLb.setToolTipText("关闭");
		btnLb.addListener(SWT.MouseDown, p -> {
			btnLb.setBackground(white);
			btnLb.setForeground(new Color(display, 0, 0, 0));
		});
		btnLb.addListener(SWT.MouseUp, p -> {
			shell.close();
		});

		//设置字体
		Font font = new Font(display, "微软雅黑", fontSize, SWT.BOLD);
		
		version.setFont(font);
		btnLb.setFont(font);
		dateSpaceLb.setFont(font);
		officialLk.setFont(font);
		tipsLb.setFont(font);
	}

	private final static int EXPIRED_DAY = 7;

	private boolean openAble = true;
	
	public void openSplash(Display display) {
		try {
//			String publisher = Configuration.getInstance().getValue("cufir.publisher");
//			String deptcode = Configuration.getInstance().getValue("cufir.deptcode");
//			String expired = Configuration.getInstance().getValue("cufir.expired");
//			String sign = Configuration.getInstance().getSign("cufir.signature");
//			String text = publisher + "" + deptcode + "" + expired;
			// 1 公钥验证签名是否过期
//			if (valid(text, sign)) {
//				// 签名未过期
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//				Date parse = sdf.parse(expired);
//				long dateSpace = getDateSpace(sdf.parse(sdf.format(new Date())), parse);
//				// 2 密钥是否在有效期内，并且剩余天数大于EXPIRED_DAY
//				if (dateSpace <= EXPIRED_DAY) {
//					if (dateSpace <= 0) {
//						// 3 产品试用期已到期
//						showButton();
//						openAble = false;
//					} else {
//						// 4 密钥未过期，但是剩余过期天数少于 EXPIRED_DAY 天
//						dateSpaceLb.setText("离试用期结束还有" + dateSpace + "天");
//					}
//				}
//			} else {
//				// 签名已过期
//				showButton();
//				openAble = false;
//			}
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
		dateSpaceLb.setVisible(false);
		tipsLb.setVisible(true);
		btnLb.setVisible(true);
		officialLk.setVisible(true);
	}

	/**
	 * Md5 验证
	 * 
	 * @param text  明文（机构代码 ）
	 * @param key   密钥
	 * @param miWen 密文
	 * @return
	 */
//	private boolean valid(String text, String sign) {
//
//		try {
//			// 1 获取公钥
//			String publicKey = FileUtil.getClassPath(this.getClass(), "src/main/resources/publicKey.license");
//			// 2 验签
//			boolean verify = RSAUtils.verify(text.getBytes(), publicKey, sign);
//			return verify;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return false;
//	}

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

	@Override
	public void onEnd() {
		closeShell();
	}

	@Override
	public void onProcessing(String info, int percent) {
		// TODO Auto-generated method stub
		
	}

}
