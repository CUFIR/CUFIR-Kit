package org.cufir.rcp.kit.handlers;

import org.cufir.rcp.kit.dialogs.AboutDialog;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * About Handler
 * 
 * @author hrj
 *
 */
public class AboutHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		AboutDialog about = new AboutDialog(new Shell(Display.getCurrent(), SWT.NONE));
		about.open();
		return true;
	}

}
