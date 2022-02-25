package org.cufir.rcp.kit;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

/**
 * ApplicationWorkbenchAdvisor
 * @author hrj、tangmaoquan
 * @Date 2021年10月15日
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String PERSPECTIVE_ID = "cufir-rcp-kit.perspective";

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);

		configurer.setSaveAndRestore(true);
	}

	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}
	
	/**
	 *  主题切换实现 TODO
	 */
	@Override
	public void preStartup() {
		try {
			IEclipsePreferences node = InstanceScope.INSTANCE.getNode("org.eclipse.e4.ui.css.swt.theme");
			node.put("themeid", "cufir.kit.theme");
//			node.put("themeid", "org.eclipse.e4.ui.css.theme.e4_default6.0,6.1,6.2,6.3,10.0");
			node.flush();
		} catch (Exception e) {
		}
		super.preStartup();
	}

}
