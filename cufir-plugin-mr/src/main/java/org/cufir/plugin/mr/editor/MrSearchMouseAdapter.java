package org.cufir.plugin.mr.editor;

import org.cufir.plugin.mr.ImgUtil;
import org.cufir.plugin.mr.bean.MrTreeItem;
import org.cufir.plugin.mr.bean.ObjTypeEnum;
import org.cufir.plugin.mr.bean.TransferDataBean;
import org.cufir.s.data.vo.EcoreTreeNode;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 树内容搜索点击事件
 */
public class MrSearchMouseAdapter extends MouseAdapter {

	private Tree elementsTree;
	
	public MrSearchMouseAdapter(Tree elementsTree) {
		this.elementsTree = elementsTree;
	}
	
	@Override
	public void mouseUp(MouseEvent e) {

		if (elementsTree.getSelectionCount() == 1) {
			TreeItem treeItem = elementsTree.getSelection()[0];
			String dataType = (String)treeItem.getData("dataType");
			CTabFolder cTabFolder = MrRepository.get().tabFolder;
			if (ObjTypeEnum.MessageComponent.getType().equals(dataType)) {
				String msgComponentId = (String)treeItem.getData("msgComponentId");
				Tree secondTabTree = MrRepository.get().messageComponentsTabTree;
				TreeItem mcTreeItem = secondTabTree.getItem(1);
				for (TreeItem mcItem: mcTreeItem.getItems()) {
					EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)mcItem.getData("EcoreTreeNode");
					if (ecoreTreeNode.getId().equals(msgComponentId)) {
						TransferDataBean transferDataBean = new TransferDataBean();
						transferDataBean.setId(ecoreTreeNode.getId());
						transferDataBean.setName(ecoreTreeNode.getName());
						transferDataBean.setLevel(ecoreTreeNode.getLevel());
						transferDataBean.setType((String)mcItem.getData("type"));
						transferDataBean.setChildId(mcItem.getData("childId") == null ? "" : (String)mcItem.getData("childId"));
						if("1".equals(ecoreTreeNode.getType())) { 		// 1：MessageComponent
							if (ecoreTreeNode.getRegistrationStatus()==null || ecoreTreeNode.getRegistrationStatus().equals("Provisionally Registered")) {
							transferDataBean.setImgPath(ImgUtil.MC_SUB3_COMPONENT);	
							}else {
							transferDataBean.setImgPath(ImgUtil.MC_SUB1_COMPONENT);
							}
						}else if("2".equals(ecoreTreeNode.getType())){  // 2：ChoiceComponent
							if (ecoreTreeNode.getRegistrationStatus()==null || ecoreTreeNode.getRegistrationStatus().equals("Provisionally Registered")) {
							transferDataBean.setImgPath(ImgUtil.MC_SUB1_CHOICE1);	
							}else {
							transferDataBean.setImgPath(ImgUtil.MC_SUB1_CHOICE);
							}
						}
						transferDataBean.setTreeListItem((MrTreeItem)mcItem);
						cTabFolder.setSelection(1);
						secondTabTree.setSelection(mcItem);
						EditorUtil.open(transferDataBean.getType(), transferDataBean);
						return;
					}
				}
			}
			
			if (ObjTypeEnum.ExternalSchema.getType().equals(dataType)) {
				String externalSchemaId = (String)treeItem.getData("externalSchemaId");
				Tree secondTabTree = MrRepository.get().messageComponentsTabTree;
				TreeItem externalSchemaTreeItem = secondTabTree.getItem(0);
				for (TreeItem externalSchemaItem: externalSchemaTreeItem.getItems()) {
					EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)externalSchemaItem.getData("EcoreTreeNode");
					if (ecoreTreeNode.getId().equals(externalSchemaId)) {
						TransferDataBean transferDataBean = new TransferDataBean();
						transferDataBean.setId(ecoreTreeNode.getId());
						transferDataBean.setName(ecoreTreeNode.getName());
						transferDataBean.setLevel(ecoreTreeNode.getLevel());
						transferDataBean.setType((String)externalSchemaItem.getData("type"));
						transferDataBean.setChildId(externalSchemaItem.getData("childId") == null ? "" : (String)externalSchemaItem.getData("childId"));
						transferDataBean.setImgPath(ImgUtil.EXTERNAL_SCHEMAS);
						transferDataBean.setTreeListItem((MrTreeItem)externalSchemaItem);
						cTabFolder.setSelection(1);
						secondTabTree.setSelection(externalSchemaItem);
						EditorUtil.open(transferDataBean.getType(), transferDataBean);
						return;
					}
				}
			}
			
			if (ObjTypeEnum.DataType.getType().equals(dataType)) {
				String dataTypeId = (String)treeItem.getData("dataTypeId");
				Tree firstTabTree = MrRepository.get().businessModelTabTree;
				TreeItem dataTypeTreeItem = firstTabTree.getItem(0);
				TreeItem[] allTypeItemArray = dataTypeTreeItem.getItems();
				for (TreeItem oneTypeTreeItem: allTypeItemArray) {
					for (TreeItem typeTreeItem: oneTypeTreeItem.getItems()) {
						EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)typeTreeItem.getData("EcoreTreeNode");
						if (ecoreTreeNode.getId().equals(dataTypeId)) {
							TransferDataBean transferDataBean = new TransferDataBean();
							transferDataBean.setId(ecoreTreeNode.getId());
							transferDataBean.setName(ecoreTreeNode.getName());
							transferDataBean.setLevel(ecoreTreeNode.getLevel());
							transferDataBean.setType((String)typeTreeItem.getData("type"));
							transferDataBean.setChildId(typeTreeItem.getData("childId") == null ? "" : (String)typeTreeItem.getData("childId"));
							if("1".equals(ecoreTreeNode.getObjType())){
								transferDataBean.setImgPath(ImgUtil.CODE_SET_SUB1_2);
							}else if("2".equals(ecoreTreeNode.getObjType())) {
								transferDataBean.setImgPath(ImgUtil.CODE_SET_SUB1);
							}
							transferDataBean.setTreeListItem((MrTreeItem)typeTreeItem);
							cTabFolder.setSelection(0);
							firstTabTree.setSelection(typeTreeItem);
							EditorUtil.open(transferDataBean.getType(), transferDataBean);
							return;
						}
					}
				}
			}
		}
	}
}
