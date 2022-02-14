package com.cfets.cufir.plugin.mr.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.cfets.cufir.plugin.mr.bean.TransferDataBean;

public class MyEditorInput implements IEditorInput{

	private TransferDataBean transferDataBean;
	
	public TransferDataBean getTransferDataBean() {
		return transferDataBean;
	}

	public MyEditorInput(TransferDataBean transferDataBean) {
		super();
		this.transferDataBean = transferDataBean;
	}

	/**
	 * 返回true,打开编辑器后它出现在eclipse主菜单“文件”最下面的最近打开的文档栏中
	 */
	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return transferDataBean.getName();
	}

	/**
	 * 返回一个可以用作保存本编辑器输入数据状态的对象
	 */
	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return getName();
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return null;
	}

}
