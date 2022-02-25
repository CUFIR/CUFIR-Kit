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
 * MessagesTree点击监听
 * @author tangmaoquan
 * @Date 2021年9月18日
 */
public class MessagesTreeClickListener implements Listener {

	private Tree tree;
	
	public MessagesTreeClickListener(Tree tree) {
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
		
		//如果是Message Definition的第四级节点, 获取上一级的节点和它的数据
		Object childId = null;
		EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)treeListItem.getData("EcoreTreeNode");
		String TreeParentEnumType = (String)treeListItem.getData("type");
		if (TreeMenuEnum.MESSAGE_DEFINITIONS_CHILD.getName().equals(TreeParentEnumType)) {
			childId = treeListItem.getData("childId");
			treeListItem = (MrTreeItem)treeListItem.getParentItem();
			ecoreTreeNode = (EcoreTreeNode)treeListItem.getData("EcoreTreeNode");
		}
		Object type = treeListItem.getData("type");
		if(ecoreTreeNode != null) {
			if(!ecoreTreeNode.getLevel().equals(TreeLevelEnum.LEVEL_1.getLevel())){
				TransferDataBean transferDataBean = new TransferDataBean();
				transferDataBean.setId(ecoreTreeNode.getId());
				transferDataBean.setName(ecoreTreeNode.getName());
				transferDataBean.setLevel(ecoreTreeNode.getLevel());
				transferDataBean.setType((String)type);
				transferDataBean.setChildId(childId == null ? "" : (String)childId);
				transferDataBean.setTreeListItem(treeListItem);
				transferDataBean.setImgPath(ecoreTreeNode.getImgPath());
				EditorUtil.open(transferDataBean.getType(), transferDataBean);
			}
		}		
	}
}
