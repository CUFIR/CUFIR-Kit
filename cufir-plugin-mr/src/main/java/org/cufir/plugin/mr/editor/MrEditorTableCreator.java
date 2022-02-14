package org.cufir.plugin.mr.editor;

import java.util.List;

import org.cufir.plugin.mr.utils.ImgUtil;
import org.cufir.s.data.bean.EcoreMessageComponent;
import org.cufir.s.data.bean.EcoreMessageDefinition;
import org.cufir.s.data.bean.EcoreMessageSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * 构造表格
 * @author gongyi_tt
 *
 */
public class MrEditorTableCreator {

	public static void generateMessageComponentTableOfImpactPage(Table messageComponentTable, List<EcoreMessageComponent> ecoreMessageComponentList) {
		for (int index = 0; index < ecoreMessageComponentList.size(); index++) {
			TableItem tableItem = new TableItem(messageComponentTable, SWT.NONE);
			tableItem.setText(ecoreMessageComponentList.get(index).getName());
			tableItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_COMPONENT));
			tableItem.setData(String.valueOf(index), ecoreMessageComponentList.get(index).getId());
		}
	}
	
	public static void generateMessageDefinationTableOfImpactPage(Table messageDefinitionTable, List<EcoreMessageDefinition> msgDefinitionList) {
		for (int index = 0; index < msgDefinitionList.size(); index++) {
			TableItem tableItem = new TableItem(messageDefinitionTable, SWT.NONE);
			StringBuilder sb = new StringBuilder();
			sb.append(msgDefinitionList.get(index).getName());
			sb.append(" (");
			sb.append(msgDefinitionList.get(index).getBusinessArea());
			sb.append(".");
			sb.append(msgDefinitionList.get(index).getMessageFunctionality());
			sb.append(".");
			sb.append(msgDefinitionList.get(index).getFlavour());
			sb.append(".");
			sb.append(msgDefinitionList.get(index).getVersion());
			sb.append(")");
			tableItem.setText(sb.substring(0));
			tableItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB1));
			tableItem.setData(String.valueOf(index), msgDefinitionList.get(index).getId());
		}
	}
	
	public static void generateMessageSetTableOfImpactPage(Table messageSetTable, List<EcoreMessageSet> msgSetList) {
		for (int index = 0; index < msgSetList.size(); index++) {
			TableItem tableItem = new TableItem(messageSetTable, SWT.NONE);
			tableItem.setText(msgSetList.get(index).getName());
			tableItem.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
			tableItem.setData(String.valueOf(index), msgSetList.get(index).getId());
		}
	}
}
