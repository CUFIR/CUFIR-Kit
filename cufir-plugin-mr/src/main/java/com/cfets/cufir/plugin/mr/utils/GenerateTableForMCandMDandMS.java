/**
 * 
 */
package com.cfets.cufir.plugin.mr.utils;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.cfets.cufir.s.data.bean.EcoreMessageComponent;
import com.cfets.cufir.s.data.bean.EcoreMessageDefinition;
import com.cfets.cufir.s.data.bean.EcoreMessageSet;

/**
 * ？？？
 * @author gongyi_tt
 *
 */
public class GenerateTableForMCandMDandMS {

	public static void generateMessageComponentTableOfImpactPage(Table messageComponentTable, ArrayList<EcoreMessageComponent> ecoreMessageComponentList) {
		for (int index = 0; index < ecoreMessageComponentList.size(); index++) {
			TableItem tableItem = new TableItem(messageComponentTable, SWT.NONE);
			tableItem.setText(ecoreMessageComponentList.get(index).getName());
			tableItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_COMPONENT));
			tableItem.setData(String.valueOf(index), ecoreMessageComponentList.get(index).getId());
		}
	}
	
	public static void generateMessageDefinationTableOfImpactPage(Table messageDefinitionTable, ArrayList<EcoreMessageDefinition> msgDefinitionList) {
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
		
//		for (int index = 0; index < msgDefinitionList.size(); index++) {
//			TableItem tableItem = new TableItem(messageDefinitionTable, SWT.NONE);
//			tableItem.setText(msgDefinitionList.get(index).getName());
//			tableItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB1));
//			tableItem.setData(String.valueOf(index), msgDefinitionList.get(index).getId());
//		}
	}
	
	public static void generateMessageSetTableOfImpactPage(Table messageSetTable, ArrayList<EcoreMessageSet> msgSetList) {
		for (int index = 0; index < msgSetList.size(); index++) {
			TableItem tableItem = new TableItem(messageSetTable, SWT.NONE);
			tableItem.setText(msgSetList.get(index).getName());
			tableItem.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
			tableItem.setData(String.valueOf(index), msgSetList.get(index).getId());
		}
	}
}
