package com.cfets.cufir.plugin.mr.view;

import java.awt.Toolkit;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * TODO ShellPlacer->place（Shell)
 * @author tangmaoquan_ntt
 * @since 3.2.1.0
 * @Date 2021年9月17日
 */
public class ChangeShellLocation {
	
	/**
	 * 设置窗口位于屏幕中间
	 * @param shell 要调整位置的窗口对象
	 */
	
	public static void center(Shell shell) {
		
		//获取屏幕宽度
		int screenH=Toolkit.getDefaultToolkit().getScreenSize().height;
		int screenW=Toolkit.getDefaultToolkit().getScreenSize().width;
		//获取对象窗口高度和宽度
		int shellH=shell.getBounds().height;
		int shellW=shell.getBounds().width;
		
		//如果对象窗口高度超出屏幕高度，则强制其与屏幕等高
		if (shellH>screenH) {
			shellH=screenH;
		}
		
		//如果对象窗口宽度抽出屏幕宽度，则强制其与屏幕等宽
		if (shellW>screenW) {
			shellW=screenW;
		}
		
		//定位对象窗口坐标
		shell.setLocation((int) (screenW*0.37),(int) (screenH*0.26));
		
	}
	
	/**
	 * 设置窗口位于屏幕中间
	 * @param display 设备
	 * @param shell 要调整位置的对象
	 */
	
	public static void center(Display display,Shell shell) {
		Rectangle bound=display.getPrimaryMonitor().getBounds();
		Rectangle  rect=shell.getBounds();
		int x=bound.x+(bound.width-rect.width)/2;
		int y=bound.y+(bound.height-rect.height)/2;
		shell.setLocation(x,y);
	}
}
