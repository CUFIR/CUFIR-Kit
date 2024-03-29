package org.cufir.rcp.kit;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	@Override
	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	@SuppressWarnings("restriction")
	@Override
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setShellStyle(SWT.FULL_SELECTION);
		configurer.setShowCoolBar(false);
		configurer.setShowStatusLine(true);
		configurer.setTitle(Activator.CUFIR_NAME);
		configurer.setShowMenuBar(false);
		configurer.setShowProgressIndicator(true);
	}
	
	/**
	 * 删除Search
	 */
	@Override
	public void postWindowOpen() {
		IMenuManager menuManager = getWindowConfigurer().getActionBarConfigurer().getMenuManager();
		IContributionItem[] items = menuManager.getItems();
		for (int i = 0; i < items.length; i++) {
			System.out.println(items[i].getId());
		}
		menuManager.remove("org.eclipse.search.menu");
		menuManager.remove("org.eclipse.ui.run");
		menuManager.update();
	}
}
