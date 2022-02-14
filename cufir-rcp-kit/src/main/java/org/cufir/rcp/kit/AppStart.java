package org.cufir.rcp.kit;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchWindow;

/**
 * APP 启动任务
 * 
 * @author hrj
 *
 */
public class AppStart implements IStartup {

	@Override
	public void earlyStartup() {
		try {
			
//			Display display = Display.getDefault();
//	        if(display != null) {
//	        	Image img = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,"icons/ISOEditorLogo_128.gif").createImage();
//				if(img!= null) {
//					Shell[] shells = display.getShells();
//					for (Shell shell : shells) {
//						shell.setImage(img);
//					}
//				}
//	        }
			
//			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
//			IWorkbenchWindow[] workbenchWindows = PlatformUI.getWorkbench().getWorkbenchWindows();
//			System.out.println(workbenchWindows.length);
//			if (window == null) {
//				for (IWorkbenchWindow iWorkbenchWindow : workbenchWindows) {
//					if (iWorkbenchWindow != null) {
//						window = iWorkbenchWindow;
//					}
//				}
//			}
//			PlatformUI.getWorkbench().showPerspective("cufir-rcp-kit.perspective", window);
//
//			IWorkbenchPage page = window.getActivePage();
//			IEditorPart activeEditor = page.getActiveEditor();
//			IEditorPart[] editors = page.getEditors();
//			for (IEditorPart iEditorPart : editors) {
//				iEditorPart.dispose();
//			}
//
//			window.getActivePage().showView("org.cufir.plugin.mr.view.MrTreeView");
		} catch (Exception e) {
//			IWorkspaceRoot resource = ResourcesPlugin.getWorkspace().getRoot();
//			MarkerUtil.addMarkerError(resource, "", AppStart.class+"");
		}

	}

}
