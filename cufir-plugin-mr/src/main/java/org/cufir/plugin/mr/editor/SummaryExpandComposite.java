package org.cufir.plugin.mr.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;

/**
 * 扩展组件
 */
public class SummaryExpandComposite {

	private ExpandBar expandBar;
	
	public ExpandBar getExpandBar() {
		return expandBar;
	}
	
	/**
	 * 扩展Bar
	 * @param summaryBar
	 * @param com
	 */
	public SummaryExpandComposite(Composite com, int type) {
		if(type == 1) {
			expandBar = new ExpandBar(com, SWT.V_SCROLL);
		}else {
			expandBar = new ExpandBar(com, SWT.NONE);
		}
		expandBar.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		expandBar.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
	}
	
	/**
	 * 扩展
	 * @param summaryBar
	 * @param com
	 */
	public SummaryExpandComposite(ExpandBar summaryBar, Composite com, String expandText, int height) {
		ExpandItem expandItem = new ExpandItem(summaryBar, SWT.NONE);
		expandItem.setText(expandText);
		expandItem.setHeight(height);
		expandItem.setExpanded(true);
		expandItem.setControl(com);
	}
}
