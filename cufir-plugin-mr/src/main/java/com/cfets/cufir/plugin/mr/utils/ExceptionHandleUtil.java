package com.cfets.cufir.plugin.mr.utils;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import com.cfets.cufir.s.ide.utils.MarkerUtil;

/**
 * 异常工具类
 * @author jin.c.li
 *
 */
public class ExceptionHandleUtil {

	/**
	 * 异常处理
	 * @param e
	 */
	public static void handle(Exception e) {
		 // 创建具有全局性作用的标记
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		MarkerUtil.addMarkerError(root,e.getMessage(), "OpenFileHandler");
	}
}
