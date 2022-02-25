package org.cufir.plugin.mr.handlers;

import org.cufir.plugin.mr.editor.MrRepository;
import org.cufir.s.data.IAnalysisProcessMonitor;
import org.cufir.s.data.Iso20022Mgr;
import org.cufir.s.ide.utils.MarkerUtil;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * 打开文件创建进程
 * @author tangmaoquan
 * @Date 2021年9月17日
 */
public class OpenFileProcess extends CommonProgress implements IAnalysisProcessMonitor {

	private String filePath;

	@SuppressWarnings("unused")
	private String fileName;

	private final static String VIEW_ID = "org.cufir.plugin.mr.view.MrTreeView";

	public OpenFileProcess(String filePath) {
		super("ISO20022文件解析导入:" + filePath, true);
		this.filePath = filePath;
	}

	public OpenFileProcess(String filePath, String fileName) {
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
