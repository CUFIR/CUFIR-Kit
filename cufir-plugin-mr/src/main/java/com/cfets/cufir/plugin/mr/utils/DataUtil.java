package com.cfets.cufir.plugin.mr.utils;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.cfets.cufir.plugin.mr.Activator;

/**
 * plugin-data 插件工具类
 * 
 * @author TangTao
 *
 */
public class DataUtil {


	/***
	 * 获取图片
	 * 
	 * @param args
	 */
	public static Image getImage(String imgPath) {
		Image img = getImageDescriptor(imgPath).createImage();
		return img;
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, path);
	}

	/**
	 * 验证字符串为空
	 * StringUtils.isBlank(null) = true 
	 * StringUtils.isBlank("") = true
	 * StringUtils.isBlank(" ") = true 
	 * StringUtils.isBlank("bob") = false
	 * StringUtils.isBlank(" bob ") = false
	 **/
	public static boolean isBlank(CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 验证字符串不为空
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean isNotBlank(CharSequence cs) {
		return !isBlank(cs);
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
