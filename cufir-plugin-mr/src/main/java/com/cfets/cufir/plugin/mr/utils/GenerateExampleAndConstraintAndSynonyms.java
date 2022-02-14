/**
 * 
 */
package com.cfets.cufir.plugin.mr.utils;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

import com.cfets.cufir.s.data.bean.EcoreConstraint;
import com.cfets.cufir.s.data.bean.EcoreExample;
import com.cfets.cufir.s.data.bean.EcoreSemanticMarkupElement;

/**
 * 生成实例和约束（合并）
 * @author gongyi_tt
 *
 */
public class GenerateExampleAndConstraintAndSynonyms {
	
	public static void generateExampleList(List exampleList, String id) {
		exampleList.removeAll();
		ArrayList<EcoreExample> ecoreExampleList = DerbyDaoUtil.getExample(id);
		for (EcoreExample ecoreExample: ecoreExampleList) {
			exampleList.add(ecoreExample.getExample());
		}
	}
	
	public static void generateExampleTable(Table exampleTable, String id) {
		exampleTable.removeAll();
		ArrayList<EcoreExample> ecoreExampleList = DerbyDaoUtil.getExample(id);
		for (EcoreExample ecoreExample: ecoreExampleList) {
			TableItem tableItem = new TableItem(exampleTable, SWT.NONE);
			tableItem.setText(ecoreExample.getExample());
			tableItem.setData(ecoreExample);
		}
	}
	
	public static void generateConstraintTable(Table constraintsTable, String id, TreeItem mbbTreeItem) {
		constraintsTable.removeAll();
		ArrayList<EcoreConstraint> ecoreConstraintList = new ArrayList<>();
		
		if (mbbTreeItem != null && mbbTreeItem.getData("mbbConstraintList") != null) {
			ecoreConstraintList = (ArrayList<EcoreConstraint>)mbbTreeItem.getData("mbbConstraintList");
		} else if (id != null) {
			ecoreConstraintList = DerbyDaoUtil.getContraints(id);
		}
		for (EcoreConstraint ecoreConstraint: ecoreConstraintList) {
			TableItem tableItem = new TableItem(constraintsTable, SWT.NONE);
			tableItem.setText(new String[] {ecoreConstraint.getName(), ecoreConstraint.getDefinition(), ecoreConstraint.getExpressionlanguage(), ecoreConstraint.getExpression()});
			tableItem.setData(ecoreConstraint);
		}
	}
	
	public static void generateSynonymsTable(Table synonymsTable , String id, TreeItem mbbTreeItem) {
		synonymsTable.removeAll();
		ArrayList<EcoreSemanticMarkupElement> synonymsList = new ArrayList<>();
		
		if (mbbTreeItem != null && mbbTreeItem.getData("mbbSynonymsTableItems") != null) {
			ArrayList<String> keyValueList = (ArrayList<String>)mbbTreeItem.getData("mbbSynonymsTableItems");
			for (String keyValue: keyValueList) {
				EcoreSemanticMarkupElement synonyms = new EcoreSemanticMarkupElement();
				synonyms.setName(keyValue.split(",")[0]);
				synonyms.setValue(keyValue.split(",")[1]);
				synonymsList.add(synonyms);
			}
		} else if (id != null) {
			synonymsList = DerbyDaoUtil.getSynonymsList(id);
		}
		for (EcoreSemanticMarkupElement synonyms: synonymsList) {
			TableItem tableItem = new TableItem(synonymsTable, SWT.NONE);
			
			tableItem.setText(new String[] {synonyms.getName(), synonyms.getValue()});
		}
	}

}
