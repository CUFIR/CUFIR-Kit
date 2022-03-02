package org.cufir.plugin.mr.handlers;

import org.cufir.plugin.mr.editor.MrRepository;
import org.cufir.s.data.IAnalysisProgressMonitor;
import org.cufir.s.data.Iso20022Mgr;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * 打开文件创建进程
 */
public class OpenFileProgress extends CommonProgress implements IAnalysisProgressMonitor {

	private String filePath;

	@SuppressWarnings("unused")
	private String fileName;

	private final static String VIEW_ID = "org.cufir.plugin.mr.view.MrTreeView";

	public OpenFileProgress(String filePath) {
		super("ISO20022文件解析导入:" + filePath, true);
		this.filePath = filePath;
	}

	public OpenFileProgress(String filePath, String fileName) {
		super("ISO20022文件解析导入:" + fileName, fileName, true);
		this.filePath = filePath;
		this.fileName = fileName;
	}

	@Override
	public void excute() {
		// filePath
		filePath = filePath.replace("\\", "/");
		System.out.println("filePath == " + filePath + "\t" + this);
		Iso20022Mgr resMgr = new Iso20022Mgr("file://" + filePath, this);
		try {
			closeView();
			resMgr.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void info(String info, int percent) {
		super.show(info, percent);
	}

	@Override
	protected void end() {
		Display.getDefault().asyncExec(() -> {
			MrRepository.get().init();
			try {
				IWorkbenchWindow[] window2 = PlatformUI.getWorkbench().getWorkbenchWindows();
				for (IWorkbenchWindow iWorkbenchWindow : window2) {
					IWorkbenchPage[] page = iWorkbenchWindow.getPages();
					for (IWorkbenchPage iWorkbenchPage : page) {
						iWorkbenchPage.showView(VIEW_ID);
					}
	
				}
				super.end();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private void closeView() {

		Display.getDefault().asyncExec(() -> {
			IWorkbenchWindow[] window = PlatformUI.getWorkbench().getWorkbenchWindows();
			for (IWorkbenchWindow iWorkbenchWindow : window) {
				IWorkbenchPage[] page = iWorkbenchWindow.getPages();
				for (IWorkbenchPage iWorkbenchPage : page) {

					IViewReference[] viewReferences = iWorkbenchPage.getViewReferences();
					IViewReference[] var3 = viewReferences;
					int var4 = viewReferences.length;

					for (int var5 = 0; var5 < var4; ++var5) {
						IViewReference ivr = var3[var5];
						if (ivr.getId().equalsIgnoreCase(VIEW_ID)) {
							iWorkbenchPage.hideView(ivr);
						}
					}
				}
			}

		});
	}
}
