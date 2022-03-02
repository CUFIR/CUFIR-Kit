package org.cufir.plugin.mr.editor;

import org.cufir.plugin.mr.bean.ComboPolicy;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.springframework.util.StringUtils;

/**
 * 选择组件
 */
public class SummaryRowComboComposite {

	private ComboViewer comboViewer;
	
	public ComboViewer getComboViewer() {
		return comboViewer;
	}
	
	private Combo combo;
	
	public Combo getCombo() {
		return combo;
	}
	
	/**
	 * 选择组件
	 * @param com
	 * @param labelText
	 * @param textEditable
	 * @param list
	 * @param comboText
	 */
	public SummaryRowComboComposite(Composite com, ComboPolicy cp) {
		Label nameLabel = new Label(com, SWT.NONE);
		nameLabel.setText(cp.getLabel());
		comboViewer = new ComboViewer(com, SWT.NONE);
		if(cp.getList() != null) {
			cp.getList().forEach(l->{
				comboViewer.add(l);
			});
		}
		combo = comboViewer.getCombo();
		
		if(!StringUtils.isEmpty(cp.getComboText())) {
			combo.setText(cp.getComboText());
		}
		if(!StringUtils.isEmpty(cp.getEditable())) {
			combo.setEnabled(cp.getEditable());
		}
		switch(cp.getComboType()){
		case ComboPolicy.COMBO_COMMONLY_TYPE:
			nameLabel.setBounds(10, cp.getStartHeight(), 200, 23);
			combo.setBounds(220, cp.getStartHeight(), 200, 23);
			break;
		case ComboPolicy.COMBO_CONTENT_TYPE:
			nameLabel.setBounds(10, cp.getStartHeight(), 150, 23);
			combo.setBounds(160, cp.getStartHeight(), 330, 23);
			break;
		default:
			break;
		}
	}
}
