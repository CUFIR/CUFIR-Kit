package com.cfets.cufir.plugin.mr.listener;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.cfets.cufir.plugin.mr.bean.TransferDataBean;
import com.cfets.cufir.plugin.mr.bean.TreeListItem;
import com.cfets.cufir.plugin.mr.enums.DataTypesEnum;
import com.cfets.cufir.plugin.mr.enums.TreeLevelEnum;
import com.cfets.cufir.plugin.mr.enums.TreeParentEnum;
import com.cfets.cufir.plugin.mr.utils.EditorUtil;
import com.cfets.cufir.plugin.mr.utils.ImgUtil;
import com.cfets.cufir.s.data.vo.EcoreTreeNode;

/**
 * FirstTreeDoubleClickListener -> BusinessModelTreeClickListener
 * @author tangmaoquan_ntt
 * @since 3.2.1.0
 * @Date 2021年9月17日
 */
public class FirstTreeDoubleClickListener implements Listener {

	private Tree tree;
	
	public FirstTreeDoubleClickListener(Tree tree) {
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
		
		//如果是第四级节点, 获取上一级的节点和它的数据
		Object childId = null;
		EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)treeListItem.getData("EcoreTreeNode");
		String DataTypesEnumType = (String)treeListItem.getData("type");
		if (DataTypesEnum.CODE.getName().equals(DataTypesEnumType)){
			childId = treeListItem.getData("childId");
			treeListItem = (TreeListItem)treeListItem.getParentItem();
			ecoreTreeNode = (EcoreTreeNode)treeListItem.getData("EcoreTreeNode");
		}
		
		// 获得触发双机事件的节点对象
		Object type = treeListItem.getData("type");
//		Object data = treeListItem.getData("EcoreTreeNode");
//		Object childId = treeListItem.getData("childId");
//		EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)data;
		
		if(DataTypesEnum.checkTypeName((String)type)) {
			TransferDataBean transferDataBean = new TransferDataBean();
			transferDataBean.setId(ecoreTreeNode.getId());
			transferDataBean.setName(ecoreTreeNode.getName());
			transferDataBean.setLevel(ecoreTreeNode.getLevel());
			transferDataBean.setType((String)type);
			transferDataBean.setTreeListItem(treeListItem);
			
			String imgPath = "";
			if (type.equals(DataTypesEnum.CODE_SETS.getName())) {
				if (ecoreTreeNode.getObjType() == null ||  "1".equalsIgnoreCase(ecoreTreeNode.getObjType())) {		// 可衍生的Code Set
//					if ("Registered".equals(ecoreTreeNode.getRegistrationStatus())) {
//						imgPath = ImgUtil.CODE_SET_SUB1_2_LOCK;
//					} else {
//						imgPath = ImgUtil.CODE_SET_SUB1_2;
//					}
					imgPath = ImgUtil.CODE_SET_SUB1_2;
				} else {													// 不可衍生的Code Set
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
			
			transferDataBean.setImgPath(imgPath);
			System.out.println("id:" + ecoreTreeNode.getId());
			if(((String)type).equals(DataTypesEnum.CODE_SETS.getName())) {
				//四级菜单需要填写自己的id
				if(ecoreTreeNode.getLevel().equals(TreeLevelEnum.LEVEL_4.getLevel())) {
					transferDataBean.setChildId(childId == null ? "" : (String)childId);
					System.out.println("childId:" + childId);
				}
				EditorUtil.open(transferDataBean.getType(), transferDataBean);
			}else if(DataTypesEnum.checkTypeName((String)type)){
				EditorUtil.open(transferDataBean.getType(), transferDataBean);
			}
		}else if(TreeParentEnum.BUSINESS_COMPONENTS.getName().equals((String)type)) {
			TransferDataBean transferDataBean = new TransferDataBean();
			transferDataBean.setId(ecoreTreeNode.getId());
			transferDataBean.setName(ecoreTreeNode.getName());
			transferDataBean.setLevel(ecoreTreeNode.getLevel());
			transferDataBean.setType((String)type);
			transferDataBean.setTreeListItem(treeListItem);
			System.out.println("id:" + ecoreTreeNode.getId());
			if(ecoreTreeNode.getLevel().equals(TreeLevelEnum.LEVEL_2.getLevel()) || ecoreTreeNode.getLevel().equals(TreeLevelEnum.LEVEL_4.getLevel())) {
				//四级菜单需要填写自己的id
				if(childId != null) {
					//Properties下面的be
					transferDataBean.setChildId(childId == null ? "" : (String)childId);
					System.out.println("childId:" + childId);
				}else {
					//bc
				}

				EditorUtil.open(transferDataBean.getType(), transferDataBean);
			}
		}
	}
}
