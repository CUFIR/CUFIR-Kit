package org.cufir.plugin.mr.view;

import org.cufir.plugin.mr.bean.DataTypesEnum;
import org.cufir.plugin.mr.bean.MrTreeItem;
import org.cufir.plugin.mr.bean.TransferDataBean;
import org.cufir.plugin.mr.bean.TreeLevelEnum;
import org.cufir.plugin.mr.bean.TreeMenuEnum;
import org.cufir.plugin.mr.editor.EditorUtil;
import org.cufir.plugin.mr.utils.ImgUtil;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import org.cufir.s.data.vo.EcoreTreeNode;

/**
 * BusinessModel树点击监听
 * @author tangmaoquan
 * @Date 2021年9月17日
 */
public class BusinessModelTreeClickListener implements Listener {

	private Tree tree;
	
	public BusinessModelTreeClickListener(Tree tree) {
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
		
		//如果是CODE, 获取上一级的节点和它的数据
		Object childId = null;
		EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)treeListItem.getData("EcoreTreeNode");
		String DataTypesEnumType = (String)treeListItem.getData("type");
		if (DataTypesEnum.CODE.getName().equals(DataTypesEnumType)){
			childId = treeListItem.getData("childId");
			treeListItem = (MrTreeItem)treeListItem.getParentItem();
			ecoreTreeNode = (EcoreTreeNode)treeListItem.getData("EcoreTreeNode");
		}
		childId = childId == null ? "" : childId;
		// 获得触发双机事件的节点对象
		Object type = treeListItem.getData("type");
		TransferDataBean tdb = new TransferDataBean();
		if(DataTypesEnum.checkTypeName((String)type)) {
			String imgPath = "";
			if (type.equals(DataTypesEnum.CODE_SETS.getName())) {
				if (ecoreTreeNode.getObjType() == null ||  "1".equalsIgnoreCase(ecoreTreeNode.getObjType())) {
					imgPath = ImgUtil.CODE_SET_SUB1_2;
				} else {
					// 不可衍生的Code Set
					if ("Registered".equals(ecoreTreeNode.getRegistrationStatus())) {
						imgPath = ImgUtil.CODE_SET_SUB1_LOCK;
					} else {
						imgPath = ImgUtil.CODE_SET_SUB1;
					}
				}
			} else if (type.equals(DataTypesEnum.TEXT.getName())) {
				if ("Registered".equals(ecoreTreeNode.getRegistrationStatus())) {
					imgPath = ImgUtil.TEXT_SUB1;
				} else {
					imgPath = ImgUtil.TEXT_SUB1_WITHOUT_LOCK;
				}
			} else if (type.equals(DataTypesEnum.BOOLEAN.getName())) {
				if ("Registered".equals(ecoreTreeNode.getRegistrationStatus())) {
					imgPath = ImgUtil.BOOLEAN_SUB1_LOCK;
				} else {
					imgPath = ImgUtil.BOOLEAN_SUB1;
				}
			} else if (type.equals(DataTypesEnum.INDICATOR.getName())) {
				if ("Registered".equals(ecoreTreeNode.getRegistrationStatus())) {
					imgPath = ImgUtil.INDICATOR_SUB1;
				} else {
					imgPath = ImgUtil.INDICATOR_SUB1_WITHOUT_LOCK;
				}
				
			} else if (type.equals(DataTypesEnum.DECIMAL.getName())) {
				if ("Registered".equals(ecoreTreeNode.getRegistrationStatus())) {
					imgPath = ImgUtil.DECIMAL_SUB1_LOCK;
				} else {
					imgPath = ImgUtil.DECIMAL_SUB1;
				}
			} else if (type.equals(DataTypesEnum.RATE.getName())) {
				if ("Registered".equals(ecoreTreeNode.getRegistrationStatus())) {
					imgPath = ImgUtil.RATE_SUB1;
				} else {
					imgPath = ImgUtil.RATE_SUB1_WITHOUT_LOCK;
				}
			} else if (type.equals(DataTypesEnum.AMOUNT.getName())) {
				if ("Registered".equals(ecoreTreeNode.getRegistrationStatus())) {
					imgPath = ImgUtil.AMOUNT_SUB1;
				} else {
					imgPath = ImgUtil.AMOUNT_SUB1_WITHOUT_LOCK;
				}
				
			} else if (type.equals(DataTypesEnum.QUANTITY.getName())) {
				if ("Registered".equals(ecoreTreeNode.getRegistrationStatus())) {
					imgPath = ImgUtil.QUANTITY_SUB1;
				} else {
					imgPath = ImgUtil.QUANTITY_SUB1_WITHOUT_LOCK;
				}
			} else if (type.equals(DataTypesEnum.TIME.getName())) {
				if ("Registered".equals(ecoreTreeNode.getRegistrationStatus())) {
					imgPath = ImgUtil.TIME_SUB1;
				} else {
					imgPath = ImgUtil.TIME_SUB1_WITHOUT_LOCK;
				}
			} else if (type.equals(DataTypesEnum.BINARY.getName())) {
				if ("Registered".equals(ecoreTreeNode.getRegistrationStatus())) {
					imgPath = ImgUtil.BINARY_SUB1;
				} else {
					imgPath = ImgUtil.BINARY_SUB1_WITHOUT_LOCK;
				}
			} else if (type.equals(DataTypesEnum.SCHEMA_TYPES.getName())) {
				if ("Registered".equals(ecoreTreeNode.getRegistrationStatus())) {
					imgPath = ImgUtil.SCHEMA_TYPES_SUB1_LOCK;
				} else {
					imgPath = ImgUtil.SCHEMA_TYPES_SUB1;
				}
			} else if (type.equals(DataTypesEnum.USER_DEFINED.getName())) {
				if ("Registered".equals(ecoreTreeNode.getRegistrationStatus())) {
					imgPath = ImgUtil.USER_DEFINED_SUB1_LOCK;
				} else {
					imgPath = ImgUtil.USER_DEFINED_SUB1;
				}
			}
			tdb.setImgPath(imgPath);
			if(((String)type).equals(DataTypesEnum.CODE_SETS.getName())) {
				//四级菜单需要填写自己的id
				if(ecoreTreeNode.getLevel().equals(TreeLevelEnum.LEVEL_4.getLevel())) {
					treeListItem = (MrTreeItem)treeListItem.getParentItem();
					ecoreTreeNode = (EcoreTreeNode)treeListItem.getData("EcoreTreeNode");
					MrTreeViewHelper.setTdb(tdb, ecoreTreeNode, type + "", treeListItem, childId + "");
					tree.setSelection(treeListItem.getParentItem());
				}else {
					MrTreeViewHelper.setTdb(tdb, ecoreTreeNode, type + "", treeListItem, childId + "");
					EditorUtil.open(tdb.getType(), tdb);
				}
			}else if(DataTypesEnum.checkTypeName((String)type)){
				MrTreeViewHelper.setTdb(tdb, ecoreTreeNode, type + "", treeListItem, childId + "");
				EditorUtil.open(tdb.getType(), tdb);
			}
		}else if(TreeMenuEnum.BUSINESS_COMPONENTS.getName().equals((String)type)) {
			if(ecoreTreeNode.getLevel().equals(TreeLevelEnum.LEVEL_2.getLevel())) {
				MrTreeViewHelper.setTdb(tdb, ecoreTreeNode, type + "", treeListItem, childId + "");
				EditorUtil.open(tdb.getType(), tdb);
			}else if(ecoreTreeNode.getLevel().equals(TreeLevelEnum.LEVEL_4.getLevel())) {
				childId = ecoreTreeNode.getId();
				tdb.setChildId(ecoreTreeNode.getId() == null ? "" : (String)ecoreTreeNode.getId());
				treeListItem = (MrTreeItem)treeListItem.getParentItem().getParentItem();
				ecoreTreeNode = (EcoreTreeNode)treeListItem.getData("EcoreTreeNode");
				MrTreeViewHelper.setTdb(tdb, ecoreTreeNode, type + "", treeListItem, childId + "");
				tree.setSelection(treeListItem);
				EditorUtil.open(tdb.getType(), tdb);
			}
		}
	}
}
