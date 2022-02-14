package com.cfets.cufir.plugin.mr.handlers;

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

import com.cfets.cufir.plugin.mr.utils.DataUtil;
import com.cfets.cufir.s.data.Iso20022ResMgr;
import com.cfets.cufir.s.ide.utils.MarkerUtil;

public class OpenFileHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		
		Iso20022ResMgr resMgr=new Iso20022ResMgr();
		boolean key;
		try {
			key = resMgr.imported();
			if(key) {
				//JOptionPane.showMessageDialog(null, "请无重复导入！");
				MessageDialog dialog = new MessageDialog(Display.getCurrent().getActiveShell(), "ISO20022_eRepository导入", null,
						"数据已存在，是否要重新导入？", MessageDialog.INFORMATION, new String[] { "确认", "取消" }, 1);
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
		DataUtil.setCenter(shell);
		shell.setSize(400, 100);

		FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
		// fileDialog.setFilterNames(new String[] {"a","b"});
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
			IsoFileProcess ifp = new IsoFileProcess(filePath, fileName);
			ifp.process();
//			
//			CaculatorProcess cp = new CaculatorProcess(fileName,filePath,true);
//			cp.process();
		}
	}

}