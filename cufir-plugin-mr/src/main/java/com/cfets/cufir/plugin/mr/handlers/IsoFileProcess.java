package com.cfets.cufir.plugin.mr.handlers;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.cfets.cufir.plugin.mr.utils.CufirProgress;
import com.cfets.cufir.s.data.IAnalysisProcessMonitor;
import com.cfets.cufir.s.data.Iso20022ResMgr;
import com.cfets.cufir.s.ide.utils.MarkerUtil;

/**
 * TODO 
 * @author tangmaoquan_ntt
 * @since 3.2.1.0
 * @Date 2021年9月17日
 */
public class IsoFileProcess extends CufirProgress implements IAnalysisProcessMonitor {

	private String filePath;

	private String fileName;

	private final static String VIEW_ID = "com.cfets.cufir.plugin.mr.view.TreeListView";;

	public IsoFileProcess(String filePath) {
		super("ISO20022文件解析导入:" + filePath, true);
		this.filePath = filePath;
	}

	public IsoFileProcess(String filePath, String fileName) {
		super("ISO20022文件解析导入:" + fileName, fileName, true);
		this.filePath = filePath;
		this.fileName = fileName;
	}

	@Override
	public void excute() {
		// filePath
		filePath = filePath.replace("\\", "/");
		System.out.println("filePath == " + filePath + "\t" + this);
		Iso20022ResMgr resMgr = new Iso20022ResMgr("file://" + filePath, this);
		try {
			closeView();
			resMgr.execute();

		} catch (Exception e) {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot(); // 创建具有全局性作用的标记
			MarkerUtil.addMarkerWarning(root, e.getMessage(), "IsoFileProcess");
		}
	}

	@Override
	public void info(String info, int percent) {
		super.show(info, percent);
	}

	@Override
	protected void end() {

		Display.getDefault().asyncExec(() -> {
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
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				MarkerUtil.addMarkerWarning(root, e.getMessage(), "IsoFileProcess.end");
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
