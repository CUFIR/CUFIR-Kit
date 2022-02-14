/**
 * 
 */
package com.cfets.cufir.plugin.mr.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.cfets.cufir.plugin.mr.bean.TreeItemDataBean;
import com.cfets.cufir.plugin.mr.bean.TreeListItem;
import com.cfets.cufir.plugin.mr.editor.BusinessAreaCreateEditor;
import com.cfets.cufir.plugin.mr.editor.BusinessComponentCreateEditor;
import com.cfets.cufir.plugin.mr.editor.DataTypesCreateEditor;
import com.cfets.cufir.plugin.mr.editor.ExternalSchemasCreateEditor;
import com.cfets.cufir.plugin.mr.editor.MessageComponentCreateEditor;
import com.cfets.cufir.plugin.mr.editor.MessageDefinitionCreateEditor;
import com.cfets.cufir.plugin.mr.editor.MessageSetCreateEditor;
import com.cfets.cufir.plugin.mr.editor.MyEditorInput;
import com.cfets.cufir.plugin.mr.enums.DataTypesEnum;
import com.cfets.cufir.plugin.mr.enums.RegistrationStatus;
import com.cfets.cufir.plugin.mr.enums.TreeLevelEnum;
import com.cfets.cufir.plugin.mr.enums.TreeParentEnum;
import com.cfets.cufir.plugin.mr.utils.DerbyDaoUtil;
import com.cfets.cufir.plugin.mr.utils.ImgUtil;
import com.cfets.cufir.s.data.bean.EcoreDataType;
import com.cfets.cufir.s.data.bean.EcoreMessageBuildingBlock;
import com.cfets.cufir.s.data.bean.EcoreMessageComponent;
import com.cfets.cufir.s.data.bean.EcoreMessageDefinition;
import com.cfets.cufir.s.data.bean.EcoreMessageSet;
import com.cfets.cufir.s.data.bean.EcoreMessageSetDefinitionRL;
import com.cfets.cufir.s.data.dao.EcoreBusinessAreaDao;
import com.cfets.cufir.s.data.dao.EcoreBusinessComponentDao;
import com.cfets.cufir.s.data.dao.EcoreDataTypeDao;
import com.cfets.cufir.s.data.dao.EcoreExternalSchemaDao;
import com.cfets.cufir.s.data.dao.EcoreMessageComponentDao;
import com.cfets.cufir.s.data.dao.EcoreMessageDefinitionDao;
import com.cfets.cufir.s.data.dao.EcoreMessageSetDao;
import com.cfets.cufir.s.data.dao.impl.EcoreBusinessAreaDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreBusinessComponentDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreDataTypeDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreExternalSchemaDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreMessageComponentDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreMessageDefinitionDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreMessageSetDaoImpl;
import com.cfets.cufir.s.data.vo.EcoreBusinessAreaVO;
import com.cfets.cufir.s.data.vo.EcoreBusinessComponentVO;
import com.cfets.cufir.s.data.vo.EcoreBusinessElementVO;
import com.cfets.cufir.s.data.vo.EcoreCodeVO;
import com.cfets.cufir.s.data.vo.EcoreDataTypeVO;
import com.cfets.cufir.s.data.vo.EcoreExternalSchemaVO;
import com.cfets.cufir.s.data.vo.EcoreMessageComponentVO;
import com.cfets.cufir.s.data.vo.EcoreMessageDefinitionVO;
import com.cfets.cufir.s.data.vo.EcoreMessageElementVO;
import com.cfets.cufir.s.data.vo.EcoreMessageSetVO;
import com.cfets.cufir.s.data.vo.EcoreTreeNode;

/**
 * 保存按钮
 * 
 * @author gongyi_tt
 *
 */
public class SaveHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		MultiPageEditorPart ier = (MultiPageEditorPart) page.getActiveEditor();

		String name = ier.getPartProperty("customName");
		// 左边的树的节点增加
		MyEditorInput editorInput = (MyEditorInput) ier.getEditorInput();

		TreeListItem treeListItem = editorInput.getTransferDataBean().getTreeListItem();
		if ("dataTypesCreate".equals(name) && ier.isDirty()) {
			DataTypesCreateEditor dataTypesEditor = (DataTypesCreateEditor) ier;
			EcoreDataTypeVO allData = dataTypesEditor.getAllData();
			EcoreDataTypeDao ecoreDataTypeDao = new EcoreDataTypeDaoImpl();
			try {
				ecoreDataTypeDao.saveEcoreDataTypeVOList(Arrays.asList(allData));

				DataTypesCreateEditor dataTypeEditor = (DataTypesCreateEditor) ier;
				dataTypeEditor.setDirty(false);

				// 设置打开事件必备
				EcoreTreeNode ecoreTreeNodeDT = new EcoreTreeNode();
				ecoreTreeNodeDT.setId(allData.getEcoreDataType().getId());
				ecoreTreeNodeDT.setType("1");
				EcoreTreeNode ecoreTreeNode = (EcoreTreeNode) treeListItem.getData("EcoreTreeNode");
				ecoreTreeNodeDT.setObjType(ecoreTreeNode.getObjType());
				ecoreTreeNodeDT.setLevel(TreeLevelEnum.LEVEL_3.getLevel());
				ecoreTreeNodeDT.setName(allData.getEcoreDataType().getName());
				ecoreTreeNodeDT.setRegistrationStatus(allData.getEcoreDataType().getRegistrationStatus());
				treeListItem.setData("type", editorInput.getTransferDataBean().getType());
				treeListItem.setData("EcoreTreeNode", ecoreTreeNodeDT);

				// 保证新建和打开的只存在一个
				editorInput.getTransferDataBean().setId(ecoreTreeNodeDT.getId());
				editorInput.getTransferDataBean().setType(editorInput.getTransferDataBean().getType());
				editorInput.getTransferDataBean().setName(ecoreTreeNodeDT.getName());

				// 删除原有的
				treeListItem.removeAll();

				for (EcoreCodeVO ecoreCodeVO : allData.getEcoreCodeVOs()) {
					if (allData.getEcoreDataType().getType().equals(DataTypesEnum.CODE_SETS.getType())) {
						TreeListItem item = new TreeListItem(treeListItem,
								new TreeItemDataBean(ecoreCodeVO.getEcoreCode().getId(),
										ecoreCodeVO.getEcoreCode().getName(), ImgUtil.CODE_SET_SUB2, "1",
										TreeLevelEnum.LEVEL_4.getLevel()));
						EcoreTreeNode ecoreTreeNodeCS = new EcoreTreeNode();
						ecoreTreeNodeCS.setLevel(TreeLevelEnum.LEVEL_4.getLevel());
						String childId = ecoreCodeVO.getEcoreCode().getId();
						ecoreTreeNodeCS.setId(ecoreTreeNodeDT.getId());
						ecoreTreeNodeCS.setName(ecoreCodeVO.getEcoreCode().getName());
						item.setData("childId", childId);
						item.setData("EcoreTreeNode", ecoreTreeNodeCS);
						item.setData("type", DataTypesEnum.CODE.getName());
					}
				}

				dataTypesEditor.setDirty(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("bizComponentCreate".equals(name) && ier.isDirty()) {
			BusinessComponentCreateEditor bizEditor = (BusinessComponentCreateEditor) ier;
			EcoreBusinessComponentVO businessComponentVO = bizEditor.getAllData();
			EcoreBusinessComponentDao businessComponentDao = new EcoreBusinessComponentDaoImpl();
			try {
				businessComponentDao.saveEcoreBusinessComponentVOList(Arrays.asList(businessComponentVO));

				// 设置打开事件必备
				EcoreTreeNode ecoreTreeNodeDT = new EcoreTreeNode();
				ecoreTreeNodeDT.setId(businessComponentVO.getEcoreBusinessComponent().getId());
				ecoreTreeNodeDT.setType(editorInput.getTransferDataBean().getType());
				ecoreTreeNodeDT.setLevel(TreeLevelEnum.LEVEL_2.getLevel());
				ecoreTreeNodeDT.setName(businessComponentVO.getEcoreBusinessComponent().getName());
				treeListItem.setData("type", editorInput.getTransferDataBean().getType());
				treeListItem.setData("EcoreTreeNode", ecoreTreeNodeDT);

				// 保证新建和打开的只存在一个
				editorInput.getTransferDataBean().setId(ecoreTreeNodeDT.getId());
				editorInput.getTransferDataBean().setType(editorInput.getTransferDataBean().getType());
				editorInput.getTransferDataBean().setName(ecoreTreeNodeDT.getName());

				TreeListItem propertiesItem = (TreeListItem) treeListItem.getItem(0);

				if (businessComponentVO.getEcoreBusinessElementVOs() != null
						&& businessComponentVO.getEcoreBusinessElementVOs().size() > 0) {
					for (EcoreBusinessElementVO businessElementVO : businessComponentVO.getEcoreBusinessElementVOs()) {

						TreeListItem item = new TreeListItem(propertiesItem,
								new TreeItemDataBean(businessElementVO.getEcoreBusinessElement().getId(),
										businessElementVO.getEcoreBusinessElement().getName(), ImgUtil.BC_BE,
										TreeParentEnum.BUSINESS_COMPONENTS.getTabType(),
										TreeLevelEnum.LEVEL_4.getLevel()));
						EcoreTreeNode ecoreTreeNodeCS = new EcoreTreeNode();
						ecoreTreeNodeCS.setLevel(TreeLevelEnum.LEVEL_4.getLevel());
						String childId = businessElementVO.getEcoreBusinessElement().getId();
						ecoreTreeNodeCS.setId(ecoreTreeNodeDT.getId());
						item.setData("childId", childId);
						item.setData("EcoreTreeNode", ecoreTreeNodeCS);
						item.setData("type", TreeParentEnum.BUSINESS_COMPONENTS.getName());
					}
				}

				bizEditor.setDirty(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("externalSchemasCreate".equals(name) && ier.isDirty()) {
			ExternalSchemasCreateEditor esEditor = (ExternalSchemasCreateEditor) ier;
			EcoreExternalSchemaVO allData = esEditor.getAllData();
			EcoreExternalSchemaDao ecoreExternalSchemaDao = new EcoreExternalSchemaDaoImpl();
			try {
				ecoreExternalSchemaDao.saveEcoreExternalSchemaVOList(Arrays.asList(allData));

				// 设置打开事件必备
				EcoreTreeNode ecoreTreeNodeDT = new EcoreTreeNode();
				ecoreTreeNodeDT.setId(allData.getEcoreExternalSchema().getId());
				ecoreTreeNodeDT.setType(editorInput.getTransferDataBean().getType());
				ecoreTreeNodeDT.setLevel(TreeLevelEnum.LEVEL_2.getLevel());
				ecoreTreeNodeDT.setName(allData.getEcoreExternalSchema().getName());
				treeListItem.setData("type", editorInput.getTransferDataBean().getType());
				treeListItem.setData("EcoreTreeNode", ecoreTreeNodeDT);

				// 保证新建和打开的只存在一个
				editorInput.getTransferDataBean().setId(ecoreTreeNodeDT.getId());
				editorInput.getTransferDataBean().setType(editorInput.getTransferDataBean().getType());
				editorInput.getTransferDataBean().setName(ecoreTreeNodeDT.getName());

				esEditor.setDirty(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("msgComponentCreate".equals(name) && ier.isDirty()) {
			MessageComponentCreateEditor msgComponentEditor = (MessageComponentCreateEditor) ier;
			EcoreMessageComponentVO ecoreMessageComponentVO = msgComponentEditor.getAllData();
			EcoreMessageComponentDao messageComponentDao = new EcoreMessageComponentDaoImpl();
			try {
				messageComponentDao.saveMessageComponentVOList(Arrays.asList(ecoreMessageComponentVO));

				// 设置打开事件必备
				EcoreTreeNode ecoreTreeNodeDT = new EcoreTreeNode();
				ecoreTreeNodeDT.setId(ecoreMessageComponentVO.getEcoreMessageComponent().getId());
				ecoreTreeNodeDT.setType(editorInput.getTransferDataBean().getType());
				ecoreTreeNodeDT.setLevel(TreeLevelEnum.LEVEL_2.getLevel());
				ecoreTreeNodeDT.setName(ecoreMessageComponentVO.getEcoreMessageComponent().getName());
				treeListItem.setData("type", editorInput.getTransferDataBean().getType());
				treeListItem.setData("EcoreTreeNode", ecoreTreeNodeDT);

				if (ecoreMessageComponentVO.getEcoreMessageElementVOs() != null
						&& ecoreMessageComponentVO.getEcoreMessageElementVOs().size() > 0) {

					// 删除原有的
					TreeItem[] items = treeListItem.getItems();
					if (items != null && items.length > 0) {
						for (TreeItem treeItem : items) {
							treeItem.dispose();
						}
					}

					for (EcoreMessageElementVO ecoreMessageElementVO : ecoreMessageComponentVO
							.getEcoreMessageElementVOs()) {
						String imgPath = "";

						if ("1".equals(ecoreMessageElementVO.getEcoreMessageElement().getType())) {
							imgPath = ImgUtil.MC_SUB2_COMPONENT;
						} else if ("2".equals(ecoreMessageElementVO.getEcoreMessageElement().getType())) {
							imgPath = ImgUtil.MC_SUB2_DATA_TYPE;
						}

						TreeListItem item = new TreeListItem(treeListItem,
								new TreeItemDataBean(ecoreMessageElementVO.getEcoreMessageElement().getId(),
										ecoreMessageElementVO.getEcoreMessageElement().getName(), imgPath,
										TreeParentEnum.MESSAGE_COMPONENTS.getTabType(),
										TreeLevelEnum.LEVEL_3.getLevel()));

						EcoreTreeNode ecoreTreeNodeCS = new EcoreTreeNode();
						ecoreTreeNodeCS.setLevel(TreeLevelEnum.LEVEL_3.getLevel());
						String childId = ecoreMessageElementVO.getEcoreMessageElement().getId();
						ecoreTreeNodeCS.setId(ecoreTreeNodeDT.getId());
						item.setData("childId", childId);
						item.setData("EcoreTreeNode", ecoreTreeNodeCS);
						item.setData("type", TreeParentEnum.MESSAGE_COMPONENTS.getName());
					}
				}

				msgComponentEditor.setDirty(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("businessAreaCreate".equals(name) && ier.isDirty()) {

			BusinessAreaCreateEditor businessAreaCreateEditor = (BusinessAreaCreateEditor) ier;
			List<EcoreBusinessAreaVO> ecoreBusinessAreaVOList = businessAreaCreateEditor
					.getEcoreMessageDefinitionVOList();
			EcoreBusinessAreaDao businessAreaDao = new EcoreBusinessAreaDaoImpl();
			try {
				businessAreaDao.saveBusinessAreaVOList(ecoreBusinessAreaVOList);
			} catch (Exception e) {
//					logger.info(ExceptionUtils.getStackTrace(e));
				throw new RuntimeException(e);

			}
			businessAreaCreateEditor.setDirty(false);
			EcoreTreeNode ecoreTreeNodeDT = new EcoreTreeNode();

			ecoreTreeNodeDT.setId(businessAreaCreateEditor.getBusinessArea().getId());
			ecoreTreeNodeDT.setName(businessAreaCreateEditor.getBusinessArea().getName());
			ecoreTreeNodeDT.setLevel(TreeLevelEnum.LEVEL_2.getLevel());
			ecoreTreeNodeDT.setType(editorInput.getTransferDataBean().getType());
			ecoreTreeNodeDT.setRegistrationStatus(businessAreaCreateEditor.getBusinessArea().getRegistrationStatus());
			treeListItem.setData("EcoreTreeNode", ecoreTreeNodeDT);
			treeListItem.setData("type", editorInput.getTransferDataBean().getType());
			treeListItem.setText(businessAreaCreateEditor.getBusinessArea().getName());

			editorInput.getTransferDataBean().setId(ecoreTreeNodeDT.getId());
			editorInput.getTransferDataBean().setType(editorInput.getTransferDataBean().getType());
			editorInput.getTransferDataBean().setName(ecoreTreeNodeDT.getName());

			treeListItem.removeAll();

			if (ecoreBusinessAreaVOList.get(0).getEcoreMessageDefinitionIds() == null) {
				return null;
			}
			// Message Definition 节点
			for (String msgDefinitionId : ecoreBusinessAreaVOList.get(0).getEcoreMessageDefinitionIds()) {

				EcoreMessageDefinition ecoreMessageDefinition = DerbyDaoUtil.getMessageDefinitionById(msgDefinitionId);

				String definitionName = ecoreMessageDefinition.getName();

				EcoreTreeNode definitionEcoreTreeNode = new EcoreTreeNode();
				definitionEcoreTreeNode.setId(msgDefinitionId);
				definitionEcoreTreeNode.setName(definitionName);
				definitionEcoreTreeNode.setLevel(TreeLevelEnum.LEVEL_3.getLevel());
				definitionEcoreTreeNode.setType(TreeParentEnum.MESSAGE_DEFINITIONS.getName());

				String msgDefinitionImgPath = null;
				if (RegistrationStatus.Registered.getStatus().equals(ecoreMessageDefinition.getRegistrationStatus())) {
					msgDefinitionImgPath = ImgUtil.MD_SUB1;
				} else {
					msgDefinitionImgPath = ImgUtil.MESSAGE_DEFINITIONS;
				}

				TreeListItem definitionTreeListItem = new TreeListItem(treeListItem, new TreeItemDataBean(
						msgDefinitionId, definitionName, msgDefinitionImgPath, "3", TreeLevelEnum.LEVEL_3.getLevel()));
				definitionEcoreTreeNode.setImgPath(msgDefinitionImgPath);
				definitionTreeListItem.setData("EcoreTreeNode", definitionEcoreTreeNode);
				definitionTreeListItem.setData("type", TreeParentEnum.MESSAGE_DEFINITIONS.getName());
				definitionTreeListItem.setText(definitionName);

				// Message Element(Message Component) 节点
				for (EcoreMessageBuildingBlock msgBldBlock : DerbyDaoUtil.getMsgBuildingBlock(msgDefinitionId)) {

					String elementDisplayName = msgBldBlock.getName() + " [" + msgBldBlock.getMinOccurs() + ","
							+ msgBldBlock.getMaxOccurs() + "]";

					EcoreTreeNode meTreeNode = new EcoreTreeNode();
					meTreeNode.setImgPath(msgDefinitionImgPath);

					// 数据类型 1：datatype，2：消息组件
					if ("2".equals(msgBldBlock.getDataType())) {
						EcoreMessageComponent mc = DerbyDaoUtil.getMessageComponentById(msgBldBlock.getDataTypeId());

//							EcoreTreeNode meTreeNode = new EcoreTreeNode();
						meTreeNode.setId(msgDefinitionId);
						meTreeNode.setName(elementDisplayName + " : " + mc.getName());
						meTreeNode.setLevel(TreeLevelEnum.LEVEL_4.getLevel());
						meTreeNode.setType(TreeParentEnum.MESSAGE_DEFINITIONS_CHILD.getName());

						String mbbImgPath = null;
						if (RegistrationStatus.Registered.getStatus()
								.equals(ecoreMessageDefinition.getRegistrationStatus())) {
							mbbImgPath = ImgUtil.MC_SUB2_COMPONENT;
						} else {
							mbbImgPath = ImgUtil.ME;
						}

						TreeListItem meTreeListItem = new TreeListItem(definitionTreeListItem,
								new TreeItemDataBean(msgBldBlock.getId(), elementDisplayName + " : " + mc.getName(),
										mbbImgPath, "3", TreeLevelEnum.LEVEL_4.getLevel()));
						meTreeListItem.setData("EcoreTreeNode", meTreeNode);
						meTreeListItem.setData("type", TreeParentEnum.MESSAGE_DEFINITIONS_CHILD.getName());
						meTreeListItem.setData("childId", msgBldBlock.getId());
						meTreeListItem.setText(elementDisplayName + " : " + mc.getName());
					} else if ("1".equals(msgBldBlock.getDataType())) {
						EcoreDataType ecoreDatatype = DerbyDaoUtil.getDataTypeById(msgBldBlock.getDataTypeId());

//							EcoreTreeNode meTreeNode = new EcoreTreeNode();
						meTreeNode.setId(msgDefinitionId);
						meTreeNode.setName(elementDisplayName + " : " + ecoreDatatype.getName());
						meTreeNode.setLevel(TreeLevelEnum.LEVEL_4.getLevel());
						meTreeNode.setType(TreeParentEnum.MESSAGE_DEFINITIONS_CHILD.getName());

						String mbbImgPath = null;
						if (RegistrationStatus.Registered.getStatus()
								.equals(ecoreMessageDefinition.getRegistrationStatus())) {
							mbbImgPath = ImgUtil.MC_SUB2_DATA_TYPE;
						} else {
							mbbImgPath = ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK;
						}

						TreeListItem meTreeListItem = new TreeListItem(definitionTreeListItem,
								new TreeItemDataBean(msgBldBlock.getId(),
										elementDisplayName + " : " + ecoreDatatype.getName(), mbbImgPath, "3",
										TreeLevelEnum.LEVEL_4.getLevel()));
						meTreeListItem.setData("EcoreTreeNode", meTreeNode);
						meTreeListItem.setData("type", TreeParentEnum.MESSAGE_DEFINITIONS_CHILD.getName());
						meTreeListItem.setData("childId", msgBldBlock.getId());
						meTreeListItem.setText(elementDisplayName + " : " + ecoreDatatype.getName());
					} else {
						// 没有指定Message Component和DataType的情况
//							EcoreTreeNode meTreeNode = new EcoreTreeNode();
						meTreeNode.setId(msgDefinitionId);
						meTreeNode.setName(elementDisplayName);
						meTreeNode.setLevel(TreeLevelEnum.LEVEL_4.getLevel());
						meTreeNode.setType(TreeParentEnum.MESSAGE_DEFINITIONS_CHILD.getName());

						String mbbImgPath = null;
						if (RegistrationStatus.Registered.getStatus()
								.equals(ecoreMessageDefinition.getRegistrationStatus())) {
							mbbImgPath = ImgUtil.MC_SUB2_COMPONENT;
						} else {
							mbbImgPath = ImgUtil.ME;
						}

						TreeListItem meTreeListItem = new TreeListItem(definitionTreeListItem,
								new TreeItemDataBean(msgBldBlock.getId(), elementDisplayName, mbbImgPath, "3",
										TreeLevelEnum.LEVEL_4.getLevel()));
						meTreeListItem.setData("EcoreTreeNode", meTreeNode);
						meTreeListItem.setData("type", TreeParentEnum.MESSAGE_DEFINITIONS_CHILD.getName());
						meTreeListItem.setData("childId", msgBldBlock.getId());
						meTreeListItem.setText(elementDisplayName);
					}
				}
			}

		} else if ("msgSetCreate".equals(name) && ier.isDirty()) {
			MessageSetCreateEditor msgSetEditor = (MessageSetCreateEditor) ier;
			ArrayList<EcoreMessageSetVO> ecoreMessageSetVOList = msgSetEditor.getEcoreMessageSetVOList();
			EcoreMessageSetDao ecoreMessageSetDao = new EcoreMessageSetDaoImpl();
			try {
				ecoreMessageSetDao.saveMessageSetVOList(ecoreMessageSetVOList);
			} catch (Exception e) {
//				logger.info(ExceptionUtils.getStackTrace(e));
				throw new RuntimeException(e);
			}

			msgSetEditor.setDirty(false);

			EcoreMessageSetVO ecoreMessageSetVO = (EcoreMessageSetVO) ecoreMessageSetVOList.get(0);
			EcoreMessageSet msgSet = ecoreMessageSetVO.getEcoreMessageSet();

			EcoreTreeNode ecoreTreeNodeDT = new EcoreTreeNode();
			ecoreTreeNodeDT.setId(msgSet.getId());
			ecoreTreeNodeDT.setName(msgSet.getName());
			ecoreTreeNodeDT.setLevel(TreeLevelEnum.LEVEL_2.getLevel());
			ecoreTreeNodeDT.setType(editorInput.getTransferDataBean().getType());
			ecoreTreeNodeDT.setRegistrationStatus(msgSet.getRegistrationStatus());
			treeListItem.setData("EcoreTreeNode", ecoreTreeNodeDT);
			treeListItem.setData("type", editorInput.getTransferDataBean().getType());
			treeListItem.setText(msgSet.getName());

			editorInput.getTransferDataBean().setId(ecoreTreeNodeDT.getId());
			editorInput.getTransferDataBean().setType(editorInput.getTransferDataBean().getType());
			editorInput.getTransferDataBean().setName(ecoreTreeNodeDT.getName());

			treeListItem.removeAll();

			// Message Definition 节点
			for (EcoreMessageSetDefinitionRL rl : ecoreMessageSetVO.getEcoreMessageSetDefinitionRLs()) {
				String msgDefinitionId = rl.getMessageId();
				EcoreMessageDefinition ecoreMessageDefinition = DerbyDaoUtil.getMessageDefinitionById(msgDefinitionId);
				String definitionName = ecoreMessageDefinition.getName();

				EcoreTreeNode definitionEcoreTreeNode = new EcoreTreeNode();
				definitionEcoreTreeNode.setId(msgDefinitionId);
				definitionEcoreTreeNode.setName(definitionName);
				definitionEcoreTreeNode.setLevel(TreeLevelEnum.LEVEL_3.getLevel());
				definitionEcoreTreeNode.setType(TreeParentEnum.MESSAGE_DEFINITIONS.getName());

				String msgDefinitionImgPath = null;
				if (RegistrationStatus.Registered.getStatus().equals(ecoreMessageDefinition.getRegistrationStatus())) {
					msgDefinitionImgPath = ImgUtil.MD_SUB1;
				} else {
					msgDefinitionImgPath = ImgUtil.MESSAGE_DEFINITIONS;
				}
				TreeListItem definitionTreeListItem = new TreeListItem(treeListItem, new TreeItemDataBean(
						msgDefinitionId, definitionName, msgDefinitionImgPath, "3", TreeLevelEnum.LEVEL_3.getLevel()));
				definitionEcoreTreeNode.setImgPath(msgDefinitionImgPath);
				definitionTreeListItem.setData("EcoreTreeNode", definitionEcoreTreeNode);
				definitionTreeListItem.setData("type", TreeParentEnum.MESSAGE_DEFINITIONS.getName());
				definitionTreeListItem.setText(definitionName);

				// Message Element(Message Component) 节点
				for (EcoreMessageBuildingBlock msgBldBlock : DerbyDaoUtil.getMsgBuildingBlock(msgDefinitionId)) {

					String elementDisplayName = msgBldBlock.getName() + " [" + msgBldBlock.getMinOccurs() + ","
							+ msgBldBlock.getMaxOccurs() + "]";
					// 数据类型 1：datatype，2：消息组件
					if ("2".equals(msgBldBlock.getDataType())) {
						EcoreMessageComponent mc = DerbyDaoUtil.getMessageComponentById(msgBldBlock.getDataTypeId());

						EcoreTreeNode meTreeNode = new EcoreTreeNode();
						meTreeNode.setId(msgDefinitionId);
						meTreeNode.setName(elementDisplayName + " : " + mc.getName());
						meTreeNode.setLevel(TreeLevelEnum.LEVEL_4.getLevel());
						meTreeNode.setType(TreeParentEnum.MESSAGE_DEFINITIONS_CHILD.getName()); // MESSAGE_COMPONENTS

						String mbbImgPath = null;
						if (RegistrationStatus.Registered.getStatus()
								.equals(ecoreMessageDefinition.getRegistrationStatus())) {
							mbbImgPath = ImgUtil.MC_SUB2_COMPONENT;
						} else {
							mbbImgPath = ImgUtil.ME;
						}
						TreeListItem meTreeListItem = new TreeListItem(definitionTreeListItem,
								new TreeItemDataBean(msgBldBlock.getId(), elementDisplayName + " : " + mc.getName(),
										mbbImgPath, "3", TreeLevelEnum.LEVEL_4.getLevel()));
						meTreeNode.setImgPath(msgDefinitionImgPath);
						meTreeListItem.setData("EcoreTreeNode", meTreeNode);
						meTreeListItem.setData("type", TreeParentEnum.MESSAGE_DEFINITIONS_CHILD.getName()); // MESSAGE_COMPONENTS
						meTreeListItem.setData("childId", msgBldBlock.getId());
						meTreeListItem.setText(elementDisplayName + " : " + mc.getName());
					} else if ("1".equals(msgBldBlock.getDataType())) {
						EcoreDataType ecoreDatatype = DerbyDaoUtil.getDataTypeById(msgBldBlock.getDataTypeId());

						EcoreTreeNode meTreeNode = new EcoreTreeNode();
						meTreeNode.setId(msgDefinitionId);
						meTreeNode.setName(elementDisplayName + " : " + ecoreDatatype.getName());
						meTreeNode.setLevel(TreeLevelEnum.LEVEL_4.getLevel());
						meTreeNode.setType(TreeParentEnum.MESSAGE_DEFINITIONS_CHILD.getName());

						String mbbImgPath = null;
						if (RegistrationStatus.Registered.getStatus()
								.equals(ecoreMessageDefinition.getRegistrationStatus())) {
							mbbImgPath = ImgUtil.MC_SUB2_DATA_TYPE;
						} else {
							mbbImgPath = ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK;
						}

						TreeListItem meTreeListItem = new TreeListItem(definitionTreeListItem,
								new TreeItemDataBean(msgBldBlock.getId(),
										elementDisplayName + " : " + ecoreDatatype.getName(), mbbImgPath, "3",
										TreeLevelEnum.LEVEL_4.getLevel()));
						meTreeListItem.setData("EcoreTreeNode", meTreeNode);
						meTreeListItem.setData("type", TreeParentEnum.MESSAGE_DEFINITIONS_CHILD.getName());
						meTreeListItem.setData("childId", msgBldBlock.getId());
						meTreeListItem.setText(elementDisplayName + " : " + ecoreDatatype.getName());
					} else {
						// 没有指定Message Component和DataType的情况
						EcoreTreeNode meTreeNode = new EcoreTreeNode();
						meTreeNode.setId(msgDefinitionId);
						meTreeNode.setName(elementDisplayName);
						meTreeNode.setLevel(TreeLevelEnum.LEVEL_4.getLevel());
						meTreeNode.setType(TreeParentEnum.MESSAGE_DEFINITIONS_CHILD.getName());

						String mbbImgPath = null;
						if (RegistrationStatus.Registered.getStatus()
								.equals(ecoreMessageDefinition.getRegistrationStatus())) {
							mbbImgPath = ImgUtil.MC_SUB2_COMPONENT;
						} else {
							mbbImgPath = ImgUtil.ME;
						}

						TreeListItem meTreeListItem = new TreeListItem(definitionTreeListItem,
								new TreeItemDataBean(msgBldBlock.getId(), elementDisplayName, mbbImgPath, "3",
										TreeLevelEnum.LEVEL_4.getLevel()));
						meTreeListItem.setData("EcoreTreeNode", meTreeNode);
						meTreeListItem.setData("type", TreeParentEnum.MESSAGE_DEFINITIONS_CHILD.getName());
						meTreeListItem.setData("childId", msgBldBlock.getId());
						meTreeListItem.setText(elementDisplayName);
					}
				}
			}
		} else if ("msgDefinitionCreate".equals(name) && ier.isDirty()) {
			MessageDefinitionCreateEditor mdEditor = (MessageDefinitionCreateEditor) ier;
			List<EcoreMessageDefinitionVO> msgDefinitionVOlist = mdEditor.getEcoreMessageDefinitionVOList();
			EcoreMessageDefinitionDao msgDefinitionDao = new EcoreMessageDefinitionDaoImpl();
			try {
				msgDefinitionDao.saveMessageDefinitionVOList(msgDefinitionVOlist);
			} catch (Exception e) {
//					logger.info(ExceptionUtils.getStackTrace(e));
				throw new RuntimeException(e);
			}
			mdEditor.setDirty(false);

			// Message Definition 节点
			EcoreTreeNode ecoreTreeNodeDT = new EcoreTreeNode();

			ecoreTreeNodeDT.setId(mdEditor.getMsgDefinition().getId());
			ecoreTreeNodeDT.setName(mdEditor.getMsgDefinition().getName());
			ecoreTreeNodeDT.setLevel(TreeLevelEnum.LEVEL_2.getLevel());
			ecoreTreeNodeDT.setType(editorInput.getTransferDataBean().getType());
			ecoreTreeNodeDT.setImgPath(editorInput.getTransferDataBean().getImgPath());
			ecoreTreeNodeDT.setRegistrationStatus(mdEditor.getMsgDefinition().getRegistrationStatus());
			treeListItem.setData("EcoreTreeNode", ecoreTreeNodeDT);
			treeListItem.setData("type", editorInput.getTransferDataBean().getType());
			treeListItem.setText(mdEditor.getMsgDefinition().getName());

			editorInput.getTransferDataBean().setId(ecoreTreeNodeDT.getId());
			editorInput.getTransferDataBean().setType(editorInput.getTransferDataBean().getType());
			editorInput.getTransferDataBean().setName(ecoreTreeNodeDT.getName());

			treeListItem.removeAll();

			// Message Element(Message Component) 节点
			for (EcoreMessageBuildingBlock msgBldBlock : DerbyDaoUtil
					.getMsgBuildingBlock(mdEditor.getMsgDefinition().getId())) {

				String elementDisplayName = msgBldBlock.getName() + " [" + msgBldBlock.getMinOccurs() + ","
						+ msgBldBlock.getMaxOccurs() + "]";
				EcoreTreeNode meTreeNode = new EcoreTreeNode();
				meTreeNode.setImgPath(editorInput.getTransferDataBean().getImgPath());
				// 数据类型 1：datatype，2：消息组件
				if ("2".equals(msgBldBlock.getDataType())) {
					EcoreMessageComponent mc = DerbyDaoUtil.getMessageComponentById(msgBldBlock.getDataTypeId());

//						EcoreTreeNode meTreeNode = new EcoreTreeNode();
					meTreeNode.setId(mdEditor.getMsgDefinition().getId());
					meTreeNode.setName(elementDisplayName + " : " + mc.getName());
					meTreeNode.setLevel(TreeLevelEnum.LEVEL_3.getLevel());
					meTreeNode.setType(TreeParentEnum.MESSAGE_DEFINITIONS_CHILD.getName());

					TreeListItem meTreeListItem = new TreeListItem(treeListItem,
							new TreeItemDataBean(msgBldBlock.getId(), elementDisplayName + " : " + mc.getName(),
									ImgUtil.ME, "3", TreeLevelEnum.LEVEL_3.getLevel()));
					meTreeListItem.setData("EcoreTreeNode", meTreeNode);
					meTreeListItem.setData("type", TreeParentEnum.MESSAGE_DEFINITIONS_CHILD.getName());
					meTreeListItem.setData("childId", msgBldBlock.getId());
					meTreeListItem.setText(elementDisplayName + " : " + mc.getName());
				} else if ("1".equals(msgBldBlock.getDataType())) {
					EcoreDataType ecoreDatatype = DerbyDaoUtil.getDataTypeById(msgBldBlock.getDataTypeId());

//						EcoreTreeNode meTreeNode = new EcoreTreeNode();
					meTreeNode.setId(mdEditor.getMsgDefinition().getId());
					meTreeNode.setName(elementDisplayName + " : " + ecoreDatatype.getName());
					meTreeNode.setLevel(TreeLevelEnum.LEVEL_3.getLevel());
					meTreeNode.setType(TreeParentEnum.MESSAGE_DEFINITIONS_CHILD.getName());

					TreeListItem meTreeListItem = new TreeListItem(treeListItem,
							new TreeItemDataBean(msgBldBlock.getId(),
									elementDisplayName + " : " + ecoreDatatype.getName(),
									ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK, "3", TreeLevelEnum.LEVEL_3.getLevel()));
					meTreeListItem.setData("EcoreTreeNode", meTreeNode);
					meTreeListItem.setData("type", TreeParentEnum.MESSAGE_DEFINITIONS_CHILD.getName());
					meTreeListItem.setData("childId", msgBldBlock.getId());
					meTreeListItem.setText(elementDisplayName + " : " + ecoreDatatype.getName());
				} else {
					// 没有指定Message Component和DataType的情况
//						EcoreTreeNode meTreeNode = new EcoreTreeNode();
					meTreeNode.setId(mdEditor.getMsgDefinition().getId());
					meTreeNode.setName(elementDisplayName);
					meTreeNode.setLevel(TreeLevelEnum.LEVEL_3.getLevel());
					meTreeNode.setType(TreeParentEnum.MESSAGE_DEFINITIONS_CHILD.getName());

					TreeListItem meTreeListItem = new TreeListItem(treeListItem,
							new TreeItemDataBean(msgBldBlock.getId(), elementDisplayName, ImgUtil.ME, "3",
									TreeLevelEnum.LEVEL_3.getLevel()));
					meTreeListItem.setData("EcoreTreeNode", meTreeNode);
					meTreeListItem.setData("type", TreeParentEnum.MESSAGE_DEFINITIONS_CHILD.getName());
					meTreeListItem.setData("childId", msgBldBlock.getId());
					meTreeListItem.setText(elementDisplayName);
				}
			}
		}

		return null;
	}

}
