package org.cufir.rcp.kit.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class RefreshViewHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		
		try {
			reOpenView("org.cufir.plugin.mr.view.MrTreeView");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 重新打开view
	 * 
	 * @param viewId
	 */
	public static void reOpenView(String viewId) {
		try {
			// 获取整个page
			IWorkbenchPage page = getWindow().getActivePage();
			if (page != null) {
				IViewReference[] viewReferences = page.getViewReferences();
				for (IViewReference ivr : viewReferences) {
					// 获取viewID
					if (ivr.getId().equalsIgnoreCase(viewId)) {
						// 关闭view窗口
						page.hideView(ivr);
					}
				}
			}
			// 打开depServiceListViewView窗口
			openView(viewId);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 
	 * @param viewId
	 */
	public static void openView(String viewId) {
		try {
			// 打开view
			IWorkbenchWindow window = getWindow();
			if (window != null) {
				window.getActivePage().showView(viewId);
			}
		} catch (Exception e) {
			// TODO
		}
	}
	
	/**
	 * 获取 IWorkbenchWindow
	 * 
	 * @return
	 */
	private static IWorkbenchWindow getWindow() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow(); // 获取窗体
		return window;
	}
}
