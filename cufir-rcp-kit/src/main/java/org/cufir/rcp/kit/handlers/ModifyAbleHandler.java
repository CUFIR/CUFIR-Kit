//package org.cufir.rcp.kit.handlers;
//
//import org.eclipse.core.commands.AbstractHandler;
//import org.eclipse.core.commands.Category;
//import org.eclipse.core.commands.CommandEvent;
//import org.eclipse.core.commands.ExecutionEvent;
//import org.eclipse.core.commands.ExecutionException;
//import org.eclipse.core.commands.ICommandListener;
//import org.eclipse.core.commands.common.NotDefinedException;
//
///**
// * 
// * @author hrj
// *
// */
//public class ModifyAbleHandler extends AbstractHandler {
//
//	@Override
//	public Object execute(ExecutionEvent event) throws ExecutionException {
//		event.getCommand().setHandler(null);
//		
////		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
//		
//		event.getCommand().addCommandListener(new ICommandListener() {
//			@Override
//			public void commandChanged(CommandEvent commandEvent) {
//				Category category;
//				try {
//					category = commandEvent.getCommand().getCategory();
//					commandEvent.getCommand().define("111", "222", category);
//				} catch (NotDefinedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
//		return null;
//	}
//
//}
