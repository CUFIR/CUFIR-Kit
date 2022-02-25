package org.cufir.plugin.mr.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Summary表格组件封装
 * @author tangmaoquan
 * @Date 2021年10月15日
 */
public class SummaryTableComposite {

	private Table table;
	
	public Table getTable() {
		return table;
	}
	
	/**
	 * 表格
	 * @param com
	 * @param width
	 * @param height
	 * @param textEditable 是否可編輯
	 * @param type
	 */
	public SummaryTableComposite(Composite com, int width, int height, int type) {
		GridData tableGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		if(type == 3) {
			tableGridData = new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1);
			tableGridData.widthHint = width;
			tableGridData.heightHint = height;
			table = new Table(com, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		}else if(type == 4) {
			tableGridData.widthHint = width;
			tableGridData.heightHint = height;
			table = new Table(com, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		}else {
			tableGridData.widthHint = width;
			tableGridData.heightHint = height;
			if(type == 1) {
				table = new Table(com, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
			}else if(type == 2) {
				table = new Table(com, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
			}else {
				table = new Table(com, SWT.BORDER);
			}
			table.setHeaderVisible(true);
			table.setLinesVisible(true);
		}
		table.setLayoutData(tableGridData);
	}
	
	/**
	 * 行数据
	 * @param msgElementTable
	 * @param width
	 * @param text
	 */
	public SummaryTableComposite(Table table,int width, String text) {
		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setWidth(width);
		tableColumn.setText(text);
	}
}
