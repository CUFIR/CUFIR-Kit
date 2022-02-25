package org.cufir.rcp.kit.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;

import org.cufir.s.ide.utils.MarkerUtil;

/**
 * AddProblemsHandler
 * @author hrj、tangmaoquan
 * @Date 2021年10月15日
 */
public class AddProblemsHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			
			// 调用添加一个问题方法.
			MarkerUtil.addMarkerError(ResourcesPlugin.getWorkspace().getRoot(), "111", "org.cufir.rcp.kit.handlers.AddProblemsHandler");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}
