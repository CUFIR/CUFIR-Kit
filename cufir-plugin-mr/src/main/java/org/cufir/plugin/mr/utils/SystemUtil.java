package org.cufir.plugin.mr.utils;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.cfets.cufir.s.ide.utils.MarkerUtil;


/**
 * 系统操作工具
 * 
 * @author wjq
 *
 */
public class SystemUtil {
	/**
	 * 获取本地IP地址
	 * 
	 * @return
	 * @throws UnknownHostException
	 */
	public static String getLocalIp() throws UnknownHostException {
		InetAddress address = InetAddress.getLocalHost();
		String hostAddress = address.getHostAddress();
		return hostAddress;
	}

	/**
	 * 获取本地MAC地址
	 * 
	 * @return
	 * @throws UnknownHostException
	 * @throws Exception
	 */
	public static String getLocalMac() throws UnknownHostException {
		String result = "";
		Process process;
		BufferedReader br;
		String line = "";
		int index = -1;
		try {
			process = Runtime.getRuntime().exec("ipconfig /all");
			br = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
			while ((line = br.readLine()) != null) {
				index = line.toLowerCase().indexOf("物理地址");
				if (index >= 0) {// 找到了
					index = line.indexOf(":");
					if (index >= 0) {
						result = line.substring(index + 1).trim();
					}
					break;
				}
			}
			br.close();
		} catch (Exception e) {
			throw new UnknownHostException();
		}
		return result;
	}
	
	/**
	 * 获取Eclipse路径
	 * 
	 * @return
	 */
	public static String getEclipsePath() {
		return System.getProperty("user.dir").replace("\\", "//");
	}
	
	/**
	 * 获取操作系统用户名
	 * 
	 * @return
	 */
	public static String getSystemUser() {
		return System.getProperty("user.name");
	}
	
	/**
	 * 获取系统用户路径
	 * 
	 * @return
	 */
	public static String getUserPath() {
		return  System.getProperty("user.home").replace("\\", "//");
	}
	
	/**
	 * 获取Workspace路径
	 * 
	 * @return
	 *//*
	public static String getWorkspacePath() {
		return  ResourcesPlugin.getWorkspace().getRoot().getLocation().toString().replace("/", "//");
	}*/
	
	/**
	 * 获取Eclipse路径
	 * 
	 * @return
	 */
	public static String getCideVersion() {
		int i = getEclipsePath().split("//").length;
		return getEclipsePath().split("//")[i-1];
	}
	
	public static Color getColor(int systemColorID) {
		Display display = Display.getCurrent();
		return display.getSystemColor(systemColorID);
	}
	
	
	public static void handle(Exception e) {
		e.printStackTrace();
		 // 创建具有全局性作用的标记
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		MarkerUtil.addMarkerError(root,e.getMessage(), "OpenFileHandler");
	}
	
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
	
	/**
	 * 设置 页面居中
	 * 
	 * @param shell
	 */
	public static void setCenter(Shell shell) {
		Rectangle bounds = Display.getCurrent().getClientArea(); // 获取屏幕尺寸
		shell.setLocation(bounds.width / 2 - 350, bounds.height / 2);
	}
	
}
