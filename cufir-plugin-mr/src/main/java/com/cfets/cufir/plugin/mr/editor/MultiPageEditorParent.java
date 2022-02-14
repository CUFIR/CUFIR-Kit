package com.cfets.cufir.plugin.mr.editor;

import org.eclipse.ui.part.MultiPageEditorPart;

/**
 * 编辑抽象类
 */
public abstract class MultiPageEditorParent extends MultiPageEditorPart{
	
	// 编辑情况
	private boolean dirty = true;
	
	@Override
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * 设置编辑状态
	 * 
	 * @param dirty
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		this.firePropertyChange(PROP_DIRTY);
	}
	
	public void setPartName(String partName) {
		super.setPartName(partName);
	}
	
}