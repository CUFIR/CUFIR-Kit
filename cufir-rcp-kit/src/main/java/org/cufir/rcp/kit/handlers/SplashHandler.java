package org.cufir.rcp.kit.handlers;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.splash.BasicSplashHandler;

public class SplashHandler extends BasicSplashHandler{
	
	public SplashHandler() {
	}
	
	
	@Override
	public void init(Shell splash) {
		splash.setMinimumSize(new Point(400, 300));
		super.init(splash);
	}

}
