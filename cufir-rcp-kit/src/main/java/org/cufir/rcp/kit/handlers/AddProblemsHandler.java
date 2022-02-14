package org.cufir.rcp.kit.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;

import com.cfets.cufir.s.ide.utils.MarkerUtil;

public class AddProblemsHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			
			// 调用添加一个问题方法.
			MarkerUtil.addMarkerError(ResourcesPlugin.getWorkspace().getRoot(), "111", "com.cfets.cufir.plugin.app.handlers.AddProblemsHandler");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}
