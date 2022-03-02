package org.cufir.plugin.mr.view;

import org.cufir.plugin.mr.bean.MrTreeItem;
import org.cufir.plugin.mr.bean.TransferDataBean;
import org.cufir.plugin.mr.bean.TreeLevelEnum;
import org.cufir.plugin.mr.bean.TreeMenuEnum;
import org.cufir.plugin.mr.editor.EditorUtil;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import org.cufir.s.data.vo.EcoreTreeNode;

/**
 * MessageComponents树点击监听
 */
public class MessageComponentsTreeClickListener implements Listener {

	private Tree tree;
	
	public MessageComponentsTreeClickListener(Tree tree) {
		super();
		this.tree = tree;
	}

	@Override
	public void handleEvent(Event event) {
		if (tree.getSelection() == null || tree.getSelection().length == 0) {
			return;
		}
		// 获得触发双机事件的节点对象
		TreeItem ti = tree.getSelection()[0];
		MrTreeItem treeListItem = (MrTreeItem)ti;
		Object type = treeListItem.getData("type");
		Object data = treeListItem.getData("EcoreTreeNode");
		Object childId = treeListItem.getData("childId");
		childId = childId == null ? "" : childId;
		if(data != null) {
			EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)data;
			TransferDataBean tdb = new TransferDataBean();
			if(TreeMenuEnum.EXTERNAL_SCHEMAS.getName().equals((String)type)) {
				if(!ecoreTreeNode.getLevel().equals(TreeLevelEnum.LEVEL_1.getLevel())){
					MrTreeViewHelper.setTdb(tdb, ecoreTreeNode, type + "", treeListItem, childId + "");
					EditorUtil.open(tdb.getType(), tdb);
				}
			}else if(TreeMenuEnum.MESSAGE_COMPONENTS.getName().equals((String)type)) {
				if(ecoreTreeNode.getLevel().equals(TreeLevelEnum.LEVEL_2.getLevel())){
					MrTreeViewHelper.setTdb(tdb, ecoreTreeNode, type + "", treeListItem, childId + "");
					EditorUtil.open(tdb.getType(), tdb);
				}else if(ecoreTreeNode.getLevel().equals(TreeLevelEnum.LEVEL_3.getLevel())){
					childId = ecoreTreeNode.getId();
					treeListItem = (MrTreeItem)treeListItem.getParentItem();
					ecoreTreeNode = (EcoreTreeNode)treeListItem.getData("EcoreTreeNode");
					MrTreeViewHelper.setTdb(tdb, ecoreTreeNode, type + "", treeListItem, childId + "");
					tree.setSelection(treeListItem);
					EditorUtil.open(tdb.getType(), tdb);
				}
			}
		}
	}
}
