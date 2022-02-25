package org.cufir.rcp.kit.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import org.cufir.s.ide.utils.CUFIR;

/**
 * 打开MR
 * @author hrj、tangmaoquan
 * @Date 2021年10月15日
 */
public class RefreshViewHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		
		try {
			CUFIR.reOpenView("org.cufir.plugin.mr.view.MrTreeView");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

}
