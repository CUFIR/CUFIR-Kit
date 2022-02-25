package org.cufir.rcp.kit;

import org.cufir.rcp.kit.dialogs.SplashDialog;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import org.cufir.s.ide.utils.i18n.I18nApi;

/**
 * This class controls all aspects of the application's execution
 * @author hrj、tangmaoquan
 * @Date 2021年10月15日
 */
public class Application implements IApplication {

	public static Shell shell = null;
	
	@Override
	public Object start(IApplicationContext context) {
		I18nApi.init(Platform.getInstallLocation().getURL().getPath().replaceFirst("/", "").replace("/", "//") + "config//i18n");
		Display display = PlatformUI.createDisplay();
		shell = new Shell(display, SWT.None);
		try {
			// 验证
			SplashDialog splashDialog = new SplashDialog(shell);
			splashDialog.openSplash(display);
			return IApplication.EXIT_OK;
		} finally {
			display.dispose();
		}
	}

	@Override
	public void stop() {
		if (!PlatformUI.isWorkbenchRunning())
			return;
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		workbench.getActiveWorkbenchWindow().close();
		display.syncExec(() -> {
			if (!display.isDisposed())
				workbench.close();
		});
	}
}
