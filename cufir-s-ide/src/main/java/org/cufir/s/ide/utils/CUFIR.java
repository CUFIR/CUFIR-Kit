package org.cufir.s.ide.utils;

import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * cufir 工具
 * 
 * @author hrj
 *
 */
public class CUFIR {

	/**
	 * 获取 IWorkbenchWindow
	 * 
	 * @return
	 */
	private static IWorkbenchWindow getWindow() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow(); // 获取窗体
		return window;
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

}
