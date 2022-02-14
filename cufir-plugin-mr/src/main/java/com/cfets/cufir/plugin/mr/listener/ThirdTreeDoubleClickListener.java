package com.cfets.cufir.plugin.mr.listener;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.cfets.cufir.plugin.mr.bean.TransferDataBean;
import com.cfets.cufir.plugin.mr.bean.TreeListItem;
import com.cfets.cufir.plugin.mr.enums.TreeLevelEnum;
import com.cfets.cufir.plugin.mr.enums.TreeParentEnum;
import com.cfets.cufir.plugin.mr.utils.EditorUtil;
import com.cfets.cufir.s.data.vo.EcoreTreeNode;

/**
 * ThirdTreeDoubleClickListener -> MessagesTreeClickListener
 * @author tangmaoquan_ntt
 * @since 3.2.1.0
 * @Date 2021年9月18日
 */
public class ThirdTreeDoubleClickListener implements Listener {

	private Tree tree;
	
	public ThirdTreeDoubleClickListener(Tree tree) {
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
		
		//如果是Message Definition的第四级节点, 获取上一级的节点和它的数据
		Object childId = null;
		EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)treeListItem.getData("EcoreTreeNode");
		String TreeParentEnumType = (String)treeListItem.getData("type");
		if (TreeParentEnum.MESSAGE_DEFINITIONS_CHILD.getName().equals(TreeParentEnumType)) {
			childId = treeListItem.getData("childId");
			treeListItem = (TreeListItem)treeListItem.getParentItem();
			ecoreTreeNode = (EcoreTreeNode)treeListItem.getData("EcoreTreeNode");
		}
		Object type = treeListItem.getData("type");
//		Object data = treeListItem.getData("EcoreTreeNode");
//		Object childId = treeListItem.getData("childId");
//		EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)data;
		
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
				System.out.println(type);
				System.out.println("id:" + ecoreTreeNode.getId());
				System.out.println("childId:" + childId);
				System.out.println("Level:" + transferDataBean.getLevel());
				System.out.println("Type:" + transferDataBean.getType());
				EditorUtil.open(transferDataBean.getType(), transferDataBean);
			}
		}else {
			//新建的
			return;
		}		
	}
}
