package com.cfets.cufir.plugin.mr.listener;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.cfets.cufir.plugin.mr.bean.TransferDataBean;
import com.cfets.cufir.plugin.mr.bean.TreeListItem;
import com.cfets.cufir.plugin.mr.utils.EditorUtil;
import com.cfets.cufir.s.data.vo.EcoreTreeNode;

/**
 * 树节点对象双击事件监听器
 * 
 * @author Administrator
 *
 */
public class TreeListViewDoubleClickListener implements Listener {

	private Tree tree;
	
	public TreeListViewDoubleClickListener(Tree tree) {
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
		TreeListItem treeListItem = (TreeListItem)ti;
		Object type = treeListItem.getData("type");
		Object childId = treeListItem.getData("childId");
		
		//不是最后一个双击不打开视图
		if (treeListItem.getItemCount() > 1 || type == null){
			return;
		}
		
		EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)treeListItem.getData("EcoreTreeNode");
		
		TransferDataBean transferDataBean = new TransferDataBean();
		transferDataBean.setId(ecoreTreeNode.getId());
		transferDataBean.setName(ecoreTreeNode.getName());
		transferDataBean.setLevel(ecoreTreeNode.getLevel());
		transferDataBean.setType((String)type);
		transferDataBean.setChildId(childId == null ? "":(String)childId);
		transferDataBean.setTreeListItem(treeListItem);
		System.out.println(type);
		System.out.println(childId);
		//----------------------------------------------
		EditorUtil.open(transferDataBean.getType(), transferDataBean);
	}
	
}