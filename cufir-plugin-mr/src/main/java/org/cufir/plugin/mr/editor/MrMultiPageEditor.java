package org.cufir.plugin.mr.editor;

import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.part.MultiPageEditorPart;

/**
 * 编辑抽象类
 */
public abstract class MrMultiPageEditor extends MultiPageEditorPart{
	
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
	
	private final static String contextId = "cufir-plugin-mr.context";
	
	public void setContext(IEditorSite site) {
		IContextService contextService = site.getService(IContextService.class);
		contextService.activateContext(contextId);
	}
}