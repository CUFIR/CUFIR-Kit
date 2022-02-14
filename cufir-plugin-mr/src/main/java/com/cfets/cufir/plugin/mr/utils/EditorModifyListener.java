/**
 * 
 */
package com.cfets.cufir.plugin.mr.utils;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.cfets.cufir.plugin.mr.editor.MultiPageEditorParent;
import com.cfets.cufir.s.data.bean.EcoreConstraint;
import com.cfets.cufir.s.data.bean.EcoreMessageBuildingBlock;
/**
 * 编辑监听
 * @author gongyi_tt
 *
 */
public class EditorModifyListener implements ModifyListener{

	private MultiPageEditorParent editor;
	
	private TreeItem modelExploreTreeItem;
	
	private Tree elementsTree;
	
	public EditorModifyListener(MultiPageEditorParent editor) {
		this.editor = editor;
	}
	
	public EditorModifyListener(MultiPageEditorParent editor, TreeItem modelExploreTreeItem) {
		this.editor = editor;
		this.modelExploreTreeItem = modelExploreTreeItem;
		
	}
	
	public EditorModifyListener(MultiPageEditorParent editor, Tree elementsTree) {
		this.editor = editor;
		this.elementsTree = elementsTree;
	}
	
	@Override
	public void modifyText(ModifyEvent e) {
		
		Text text = null;
		if (e.widget instanceof Text) {
			text = (Text)e.widget;
		}
		
		
		if (editor != null && modelExploreTreeItem != null) {
			this.editor.setPartName(text.getText());
			//左边的树的名称展示
			modelExploreTreeItem.setText(text.getText());
		}

		if (elementsTree != null) {
			if (elementsTree.getSelection().length == 0) {
				return;
			}
			TreeItem treeItem = elementsTree.getSelection()[0];
			if (treeItem.getData("bbId") != null) {
				String nodeDisplayName = treeItem.getText();
				int index = nodeDisplayName.indexOf("[");
				EcoreMessageBuildingBlock msgBldBlock = (EcoreMessageBuildingBlock)treeItem.getData("msgBldBlock");
				if (String.valueOf(text.getData("hint")).equals("name")) {
					msgBldBlock.setName(text.getText());
					treeItem.setText(nodeDisplayName.replaceFirst(nodeDisplayName.substring(0, index - 1), text.getText()));
				} else if (String.valueOf(text.getData("hint")).equals("definition")) {
					msgBldBlock.setDefinition(text.getText());
				} else if (String.valueOf(text.getData("hint")).equals("min")) {
					msgBldBlock.setMinOccurs(NumberFormatUtil.getInt(text.getText()));
				} else if (String.valueOf(text.getData("hint")).equals("max")) {
					msgBldBlock.setMaxOccurs(NumberFormatUtil.getInt(text.getText()));
				}
			} else if (treeItem.getData("constraint") != null) {
				EcoreConstraint constraint = (EcoreConstraint)treeItem.getData("constraint");
				if (String.valueOf(text.getData("hint")).equals("name")) {
					constraint.setName(text.getText());
					String displayName = treeItem.getText();
					treeItem.setText(displayName.substring(0, displayName.indexOf(":")) + ": " + text.getText());
				} else if (String.valueOf(text.getData("hint")).equals("document")) {
					constraint.setDefinition(text.getText());
				} else if (String.valueOf(text.getData("hint")).equals("expression")) {
					constraint.setExpression(text.getText());
				} else if (String.valueOf(text.getData("hint")).equals("expressionLanguage")) {
					constraint.setExpressionlanguage(text.getText());
				}
			} else if (treeItem.getData("isNewConstraint") != null) {
				if (String.valueOf(text.getData("hint")).equals("name")) {
					treeItem.setData("name", text.getText());
					treeItem.setText("Textual" + " : " + text.getText());
				} else if (String.valueOf(text.getData("hint")).equals("document")) {
					treeItem.setData("document", text.getText());
				} else if (String.valueOf(text.getData("hint")).equals("expression")) {
					treeItem.setData("expression", text.getText());
				} else if (String.valueOf(text.getData("hint")).equals("expressionLanguage")) {
					treeItem.setData("expressionLanguage", text.getText());
				}
			}
//			else if (treeItem.getData("msgComponentId") != null) {
//				String nodeDisplayName = treeItem.getText();
//				int index = nodeDisplayName.indexOf("[");
//				treeItem.setText(nodeDisplayName.replaceFirst(nodeDisplayName.substring(0, index - 1), text.getText()));
//				treeItem.setData("name", text.getText());
//			}
		}
		
		this.editor.setDirty(true);
	}
	
}
