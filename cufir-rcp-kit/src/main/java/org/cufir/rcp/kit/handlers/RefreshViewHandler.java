package org.cufir.rcp.kit.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.cfets.cufir.s.ide.utils.CUFIR;

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
