//package org.cufir.rcp.kit.handlers;
//
//import org.eclipse.core.commands.AbstractHandler;
//import org.eclipse.core.commands.ExecutionEvent;
//import org.eclipse.core.commands.ExecutionException;
//import org.eclipse.ui.IWorkbenchWindow;
//import org.eclipse.ui.PlatformUI;
//
//public class TasksHandler extends AbstractHandler {
//
//	@Override
//	public Object execute(ExecutionEvent arg0) throws ExecutionException {
//		try {
//			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
//			window.getActivePage().showView("cufir-plugin-ds.views.TaskListView");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//}
