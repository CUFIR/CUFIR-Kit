package org.cufir.plugin.mr.handlers;

import org.cufir.plugin.mr.utils.SystemUtil;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import org.cufir.s.data.Iso20022Mgr;
import org.cufir.s.ide.utils.MarkerUtil;
import org.cufir.s.ide.utils.i18n.I18nApi;

/**
 * 打开文件
 * @author tangmaoquan
 * @Date 2021年9月29日
 */
public class OpenFileHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		
		Iso20022Mgr resMgr=new Iso20022Mgr();
		boolean key;
		try {
			key = resMgr.imported();
			if(key) {
				MessageDialog dialog = new MessageDialog(Display.getCurrent().getActiveShell(), I18nApi.get("menubar.file.open.title"), null,
						I18nApi.get("menubar.file.open.info"), MessageDialog.INFORMATION, new String[] { I18nApi.get("common.button.ok"), I18nApi.get("common.button.cancel") }, 1);
				int i = dialog.open(); // 按钮返回值
				if (0 == i) { // 点击按钮保存数据
					openFile();
				}
			}else {
				openFile();
			}
		} catch (Exception e) {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot(); // 创建具有全局性作用的标记                
			MarkerUtil.addMarkerError(root,e.getMessage(), "OpenFileHandler");
		}
		return null;
	}

	/**
	 * 1.浏览文件
	 */
	public void openFile() {
		Display display = Display.getCurrent();
		Shell shell = new Shell(display.getActiveShell());
		SystemUtil.center(shell);
		shell.setSize(400, 100);

		FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
		fileDialog.setFilterExtensions(new String[] {  "*.iso20022" });
		String filePath = fileDialog.open(); // 获取文件全路径
		String fileName = fileDialog.getFileName(); // 获取文件名称
		// 如果获取路径为,不解析文件
		if ("" == filePath || null == filePath) {
			shell.dispose();
			return;
		} else {
			if (shell.isDisposed()) {
				shell.dispose();
			}
			// 解析数据
			OpenFileProcess ifp = new OpenFileProcess(filePath, fileName);
			ifp.process();
		}
	}
}