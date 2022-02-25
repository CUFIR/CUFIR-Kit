package org.cufir.rcp.kit;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;


/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 * @author hrj、tangmaoquan
 * @Date 2021年10月15日
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	// Actions - important to allocate these only in makeActions, and then use
	// them
	// in the fill methods. This ensures that the actions aren't recreated
	// when fillActionBars is called with FILL_PROXY.

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

//	private IWorkbenchAction action1;

	@Override
	protected void makeActions(IWorkbenchWindow window) {
//		action1 = new CufirAction(window, () -> {
//			System.out.println("toolbar is clicked");
//		});
//		action1.setText("toolbar1");
//		action1.setId("product.cufir.toolbars.toolbar1");
//		register(action1);
	}

	@Override
	protected void fillMenuBar(IMenuManager menuBar) {
//		MenuManager manager = new MenuManager("menu1", "product.menus");
//		menuBar.add(manager);
//		manager.add(action1);
	}

}
