package com.cfets.cufir.plugin.mr.utils;

import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.contexts.IContextService;

/**
 * 设置快捷键情况
 * @author jin.c.li
 *
 */
public class ContextServiceUtil {

	private final static String contextId = "cufir-plugin-mr.context";
	
	public static void setContext(IEditorSite site) {
		IContextService contextService = site.getService(IContextService.class);
		contextService.activateContext(contextId);
	}
}
