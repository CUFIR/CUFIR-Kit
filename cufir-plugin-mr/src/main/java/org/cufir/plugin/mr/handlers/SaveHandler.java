package org.cufir.plugin.mr.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.cufir.plugin.mr.MrHelper;
import org.cufir.plugin.mr.bean.DataTypesEnum;
import org.cufir.plugin.mr.bean.MrTreeItem;
import org.cufir.plugin.mr.bean.ObjTypeEnum;
import org.cufir.plugin.mr.bean.RegistrationStatusEnum;
import org.cufir.plugin.mr.bean.TreeItemDataBean;
import org.cufir.plugin.mr.bean.TreeLevelEnum;
import org.cufir.plugin.mr.bean.TreeMenuEnum;
import org.cufir.plugin.mr.editor.BusinessAreaEditor;
import org.cufir.plugin.mr.editor.BusinessComponentEditor;
import org.cufir.plugin.mr.editor.DataTypesEditor;
import org.cufir.plugin.mr.editor.ExternalSchemasEditor;
import org.cufir.plugin.mr.editor.MessageComponentEditor;
import org.cufir.plugin.mr.editor.MessageDefinitionEditor;
import org.cufir.plugin.mr.editor.MessageSetEditor;
import org.cufir.plugin.mr.editor.MrEditorDialogCreator;
import org.cufir.plugin.mr.editor.MrEditorHelper;
import org.cufir.plugin.mr.editor.MrEditorInput;
import org.cufir.plugin.mr.editor.MrImplManager;
import org.cufir.plugin.mr.editor.MrRepository;
import org.cufir.plugin.mr.utils.ImgUtil;
import org.cufir.s.data.vo.EcoreBusinessAreaVO;
import org.cufir.s.data.vo.EcoreBusinessComponentVO;
import org.cufir.s.data.vo.EcoreBusinessElementVO;
import org.cufir.s.data.vo.EcoreCodeVO;
import org.cufir.s.data.vo.EcoreDataTypeVO;
import org.cufir.s.data.vo.EcoreExternalSchemaVO;
import org.cufir.s.data.vo.EcoreMessageComponentVO;
import org.cufir.s.data.vo.EcoreMessageDefinitionVO;
import org.cufir.s.data.vo.EcoreMessageElementVO;
import org.cufir.s.data.vo.EcoreMessageSetVO;
import org.cufir.s.data.vo.EcoreTreeNode;
import org.cufir.s.ecore.bean.EcoreDataType;
import org.cufir.s.ecore.bean.EcoreMessageBuildingBlock;
import org.cufir.s.ecore.bean.EcoreMessageComponent;
import org.cufir.s.ecore.bean.EcoreMessageDefinition;
import org.cufir.s.ecore.bean.EcoreMessageSet;
import org.cufir.s.ecore.bean.EcoreMessageSetDefinitionRL;
import org.cufir.s.ecore.bean.EcoreNextVersions;
import org.cufir.s.ide.utils.i18n.I18nApi;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.springframework.util.StringUtils;

/**
 * 保存按钮
 * @author tangmaoquan
 * @Date 2021年10月15日
 */
public class SaveHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		MultiPageEditorPart ier = (MultiPageEditorPart) page.getActiveEditor();
		String name = ier.getPartProperty("customName");
		// 左边的树的节点增加
		MrEditorInput editorInput = (MrEditorInput) ier.getEditorInput();
		MrTreeItem treeListItem = editorInput.getTransferDataBean().getTreeListItem();
		if ("dataTypesCreate".equals(name) && ier.isDirty()) {
			DataTypesEditor dataTypeEditor = (DataTypesEditor) ier;
			TreeItem[] treeItems = dataTypeEditor.modelExploreTreeItem.getParentItem().getItems();
			//树节点有重复且和修改名称相等的
			Map<String, Integer> repeat = repeat(treeItems);
			EcoreDataTypeVO allData = dataTypeEditor.getAllData();
			Integer countRepeat = repeat.get(allData.getEcoreDataType().getName());
			if(countRepeat != null) {
				throw new RuntimeException("名称重复，请更新！");
			}
			try {
				MrImplManager.get().getEcoreDataTypeImpl().saveEcoreDataTypeVO(allData);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//多线程加载
			new Thread(()->{
				// 重置相关数据
				MrRepository.get().reset(MrRepository.get().ECORE_DATA_TYPE, MrRepository.get().ECORE_EXAMPLE, MrRepository.get().ECORE_CODE,
						MrRepository.get().ECORE_CONSTRAINT);
			}).start();
			
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
			setTreeItem(treeListItem, ecoreTreeNodeDT, editorInput.getTransferDataBean().getType(), "");
			// 保证新建和打开的只存在一个
			editorInput.getTransferDataBean().setId(ecoreTreeNodeDT.getId());
			editorInput.getTransferDataBean().setType(editorInput.getTransferDataBean().getType());
			editorInput.getTransferDataBean().setName(ecoreTreeNodeDT.getName());
			// 删除原有的
			treeListItem.removeAll();
			// 增加新节点
			for (EcoreCodeVO ecoreCodeVO : allData.getEcoreCodeVOs()) {
				if (allData.getEcoreDataType().getType().equals(DataTypesEnum.CODE_SETS.getType())) {
					MrTreeItem item = new MrTreeItem(treeListItem,
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
			dataTypeEditor.setDirty(false);
		} else if ("bizComponentCreate".equals(name) && ier.isDirty()) {
			BusinessComponentEditor bizEditor = (BusinessComponentEditor) ier;
			TreeItem parentItem = bizEditor.modelExploreTreeItem.getParentItem();
			Map<String, Integer> repeat = repeat(parentItem.getItems());
			EcoreBusinessComponentVO allData = bizEditor.getAllData();
			String bcName = allData.getEcoreBusinessComponent().getName();
			if( StringUtils.isEmpty(bcName)) {
				MessageDialog.openWarning(Display.getDefault().getActiveShell(), I18nApi.get("tips.name"), I18nApi.get("tips.error.name.null"));
				return null;
			}
			Integer countRepeat = repeat.get(allData.getEcoreBusinessComponent().getName());
			if(countRepeat != null) {
				MessageDialog.openWarning(Display.getDefault().getActiveShell(), I18nApi.get("tips.name"), I18nApi.get("tips.bm.error.name.one"));
				return null;
			}
			try {
				MrImplManager.get().getEcoreBusinessComponentImpl()
				.saveEcoreBusinessComponentVOList(Arrays.asList(allData));
			} catch (Exception e) {
				e.printStackTrace();
			}
			//多线程加载
			new Thread(()->{
				// 重置相关数据
				MrRepository.get().reset(MrRepository.get().ECORE_CONSTRAINT, MrRepository.get().ECORE_EXAMPLE, MrRepository.get().ECORE_SEMANTIC_MARKUP,
						MrRepository.get().ECORE_SEMANTIC_MARKUP_ELEMENT, MrRepository.get().ECORE_BUSINESS_COMPONENT, MrRepository.get().ECORE_BUSINESS_ELEMENT);
			}).start();
			// 设置打开事件必备
			EcoreTreeNode ecoreTreeNodeDT = new EcoreTreeNode();
			ecoreTreeNodeDT.setId(allData.getEcoreBusinessComponent().getId());
			ecoreTreeNodeDT.setType(editorInput.getTransferDataBean().getType());
			ecoreTreeNodeDT.setLevel(TreeLevelEnum.LEVEL_2.getLevel());
			ecoreTreeNodeDT.setName(allData.getEcoreBusinessComponent().getName());
			setTreeItem(treeListItem, ecoreTreeNodeDT, editorInput.getTransferDataBean().getType(), "");
			// 保证新建和打开的只存在一个
			editorInput.getTransferDataBean().setId(ecoreTreeNodeDT.getId());
			editorInput.getTransferDataBean().setType(editorInput.getTransferDataBean().getType());
			editorInput.getTransferDataBean().setName(ecoreTreeNodeDT.getName());
			
			MrTreeItem propertiesItem = (MrTreeItem) treeListItem.getItem(0);
			propertiesItem.removeAll();
			if (allData.getEcoreBusinessElementVOs() != null
					&& allData.getEcoreBusinessElementVOs().size() > 0) {
				for (EcoreBusinessElementVO businessElementVO : allData.getEcoreBusinessElementVOs()) {
					MrTreeItem item = new MrTreeItem(propertiesItem,
							new TreeItemDataBean(businessElementVO.getEcoreBusinessElement().getId(),
									businessElementVO.getEcoreBusinessElement().getName(), ImgUtil.BC_BE,
									TreeMenuEnum.BUSINESS_COMPONENTS.getTabType(),
									TreeLevelEnum.LEVEL_4.getLevel()));
					EcoreTreeNode ecoreTreeNodeCS = new EcoreTreeNode();
					ecoreTreeNodeCS.setLevel(TreeLevelEnum.LEVEL_4.getLevel());
					String childId = businessElementVO.getEcoreBusinessElement().getId();
					ecoreTreeNodeCS.setId(ecoreTreeNodeDT.getId());
					item.setData("childId", childId);
					item.setData("EcoreTreeNode", ecoreTreeNodeCS);
					item.setData("type", TreeMenuEnum.BUSINESS_COMPONENTS.getName());
				}
			}
			bizEditor.setDirty(false);
		} else if ("externalSchemasCreate".equals(name) && ier.isDirty()) {
			ExternalSchemasEditor esEditor = (ExternalSchemasEditor) ier;
			EcoreExternalSchemaVO allData = esEditor.getAllData();
			try {
				MrImplManager.get().getEcoreExternalSchemaImpl().saveEcoreExternalSchemaVOList(Arrays.asList(allData));
			} catch (Exception e) {
				e.printStackTrace();
			}
			//多线程加载
			new Thread(()->{
				// 重置相关数据
				MrRepository.get().reset(MrRepository.get().ECORE_CONSTRAINT, MrRepository.get().ECORE_EXAMPLE, MrRepository.get().ECORE_EXTERNAL_SCHEMA);
			}).start();
			// 设置打开事件必备
			EcoreTreeNode ecoreTreeNodeDT = new EcoreTreeNode();
			ecoreTreeNodeDT.setId(allData.getEcoreExternalSchema().getId());
			ecoreTreeNodeDT.setType(editorInput.getTransferDataBean().getType());
			ecoreTreeNodeDT.setLevel(TreeLevelEnum.LEVEL_2.getLevel());
			ecoreTreeNodeDT.setName(allData.getEcoreExternalSchema().getName());
			setTreeItem(treeListItem, ecoreTreeNodeDT, editorInput.getTransferDataBean().getType(), "");
			// 保证新建和打开的只存在一个
			editorInput.getTransferDataBean().setId(ecoreTreeNodeDT.getId());
			editorInput.getTransferDataBean().setType(editorInput.getTransferDataBean().getType());
			editorInput.getTransferDataBean().setName(ecoreTreeNodeDT.getName());
			esEditor.setDirty(false);
		} else if ("msgComponentCreate".equals(name) && ier.isDirty()) {
			MessageComponentEditor msgComponentEditor = (MessageComponentEditor) ier;
			EcoreMessageComponentVO msgComponentVO = msgComponentEditor.getAllData();
			String msgComponentName = msgComponentVO.getEcoreMessageComponent().getName();
			TreeItem[] treeItems = msgComponentEditor.modelExploreTreeItem.getParentItem().getItems();
			//树节点有重复且和修改名称相等的
			Map<String, Integer> repeat = repeat(treeItems);
			Integer countRepeat = repeat.get(msgComponentVO.getEcoreMessageComponent().getName());
			if(countRepeat != null) {
				MessageDialog.openWarning(Display.getDefault().getActiveShell(), I18nApi.get("tips.name"), I18nApi.get("tips.mc.error.name.three"));
				return null;
			}
			String pat = "\\d+";
			Pattern p = Pattern.compile(pat);
			String[] split = p.split(msgComponentName);
			String version = MrEditorHelper.getVersion(msgComponentName, p, split);
			if("".equals(version)) {
				MessageDialog.openWarning(Display.getDefault().getActiveShell(), I18nApi.get("tips.name"), I18nApi.get("tips.mc.error.name.one"));
				return null;
			}
			//删除版本关联
			MrImplManager.get().getEcoreNextVersionsImpl().delById(msgComponentVO.getEcoreMessageComponent().getId(), null);
			MrImplManager.get().getEcoreNextVersionsImpl().delByNextVersionId(msgComponentVO.getEcoreMessageComponent().getId());
			//统计上一个版本
			int count = MrImplManager.get().getEcoreNextVersionsImpl().countByNextVersionId(msgComponentVO.getEcoreMessageComponent().getId());
			if(count < 1) {
				//获取上一个版本
				String previousName = componentPreviousName(msgComponentName);
				if("0".equals(previousName)) {
					return null;
				}
				if(!StringUtils.isEmpty(previousName)) {
					String previousId = MrImplManager.get().getEcoreMessageComponentImpl().getIdByName(previousName);
					if(!StringUtils.isEmpty(previousId) && !previousId.equals(msgComponentVO.getEcoreMessageComponent().getId())) {
						List<EcoreNextVersions> nextVersionList = new ArrayList<>();
						EcoreNextVersions nextVersion = new EcoreNextVersions();
						nextVersion.setId(previousId);
						nextVersion.setNextVersionId(msgComponentVO.getEcoreMessageComponent().getId());
						nextVersion.setObjType(ObjTypeEnum.MessageComponent.getType());
						nextVersionList.add(nextVersion);
						msgComponentVO.setEcoreNextVersions(nextVersionList);
					}
				}
			}
			try {
				MrImplManager.get().getEcoreMessageComponentImpl()
				.saveMessageComponentVOList(Arrays.asList(msgComponentVO));
			} catch (Exception e) {
				e.printStackTrace();
			}
			//多线程加载
			new Thread(()->{
				// 重置相关数据
				MrRepository.get().reset(MrRepository.get().ECORE_MESSAGE_COMPONENT, MrRepository.get().ECORE_EXAMPLE,
						MrRepository.get().ECORE_MESSAGE_ELEMENT, MrRepository.get().ECORE_CONSTRAINT,
						MrRepository.get().ECORE_SEMANTIC_MARKUP, MrRepository.get().ECORE_SEMANTIC_MARKUP_ELEMENT);
			}).start();
			//版本关联实时刷新
			MrRepository.get().reset(MrRepository.get().ECORE_NEXT_VERSIONS);
			msgComponentEditor.setPreviousVersion(MrRepository.get().ecoreMessageComponentMapById);
			// 设置打开事件必备
			EcoreTreeNode ecoreTreeNodeDT = new EcoreTreeNode();
			ecoreTreeNodeDT.setId(msgComponentVO.getEcoreMessageComponent().getId());
			ecoreTreeNodeDT.setType(editorInput.getTransferDataBean().getType());
			ecoreTreeNodeDT.setLevel(TreeLevelEnum.LEVEL_2.getLevel());
			ecoreTreeNodeDT.setName(msgComponentVO.getEcoreMessageComponent().getName());
			setTreeItem(treeListItem, ecoreTreeNodeDT, editorInput.getTransferDataBean().getType(), "");
			
			// 删除原有的
			treeListItem.removeAll();
			
			if (msgComponentVO.getEcoreMessageElementVOs() != null && msgComponentVO.getEcoreMessageElementVOs().size() > 0) {
				for (EcoreMessageElementVO ecoreMessageElementVO : msgComponentVO
						.getEcoreMessageElementVOs()) {
					String imgPath = "";
					if ("1".equals(ecoreMessageElementVO.getEcoreMessageElement().getType())) {
						imgPath = ImgUtil.MC_SUB2_COMPONENT;
					} else if ("2".equals(ecoreMessageElementVO.getEcoreMessageElement().getType())) {
						imgPath = ImgUtil.MC_SUB2_DATA_TYPE;
					} else {
						imgPath = ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK;
					}
					MrTreeItem item = new MrTreeItem(treeListItem,
							new TreeItemDataBean(ecoreMessageElementVO.getEcoreMessageElement().getId(),
									ecoreMessageElementVO.getEcoreMessageElement().getName(), imgPath,
									TreeMenuEnum.MESSAGE_COMPONENTS.getTabType(),
									TreeLevelEnum.LEVEL_3.getLevel()));
					EcoreTreeNode ecoreTreeNodeCS = new EcoreTreeNode();
					ecoreTreeNodeCS.setLevel(TreeLevelEnum.LEVEL_3.getLevel());
					String childId = ecoreMessageElementVO.getEcoreMessageElement().getId();
					ecoreTreeNodeCS.setId(ecoreTreeNodeDT.getId());
					item.setData("childId", childId);
					item.setData("EcoreTreeNode", ecoreTreeNodeCS);
					item.setData("type", TreeMenuEnum.MESSAGE_COMPONENTS.getName());
					setItem(item, ecoreTreeNodeCS, editorInput.getTransferDataBean().getType(), childId, "");
				}
			}
			msgComponentEditor.setDirty(false);
		} else if ("businessAreaCreate".equals(name) && ier.isDirty()) {
			BusinessAreaEditor businessAreaCreateEditor = (BusinessAreaEditor) ier;
			EcoreBusinessAreaVO businessAreaVO = businessAreaCreateEditor.getEcoreMessageDefinitionVO();
			List<EcoreBusinessAreaVO> businessAreaVOList = new ArrayList<>();
			businessAreaVOList.add(businessAreaVO);
			try {
				MrImplManager.get().getEcoreBusinessAreaImpl().saveBusinessAreaVOList(businessAreaVOList);
				// 重置相关数据
				MrRepository.get().reset(MrRepository.get().ECORE_MESSAGE_DEFINITION, MrRepository.get().ECORE_EXAMPLE,MrRepository.get().ECORE_CONSTRAINT);

				Map<String, EcoreDataType> dtMap = MrRepository.get().ecoreDataTypeMapById;
				Map<String, EcoreMessageComponent> mcMap = MrRepository.get().ecoreMessageComponentMapById;
				List<EcoreMessageBuildingBlock> mbbs = MrRepository.get().ecoreMessageBuildingBlocks;
				List<EcoreMessageDefinition> mds = MrRepository.get().ecoreMessageDefinitions;
				
				businessAreaCreateEditor.setDirty(false);
				EcoreTreeNode ecoreTreeNodeDT = new EcoreTreeNode();
				ecoreTreeNodeDT.setId(businessAreaCreateEditor.getBusinessArea().getId());
				ecoreTreeNodeDT.setName(businessAreaCreateEditor.getBusinessArea().getName());
				ecoreTreeNodeDT.setLevel(TreeLevelEnum.LEVEL_2.getLevel());
				ecoreTreeNodeDT.setType(editorInput.getTransferDataBean().getType());
				ecoreTreeNodeDT.setRegistrationStatus(businessAreaCreateEditor.getBusinessArea().getRegistrationStatus());
				setTreeItem(treeListItem, ecoreTreeNodeDT, editorInput.getTransferDataBean().getType(), businessAreaCreateEditor.getBusinessArea().getName());
				editorInput.getTransferDataBean().setId(ecoreTreeNodeDT.getId());
				editorInput.getTransferDataBean().setType(editorInput.getTransferDataBean().getType());
				editorInput.getTransferDataBean().setName(ecoreTreeNodeDT.getName());

				treeListItem.removeAll();

				if (businessAreaVO.getEcoreMessageDefinitionIds() == null) {
					return null;
				}
				// Message Definition 节点
				for (String msgDefinitionId : businessAreaVO.getEcoreMessageDefinitionIds()) {
					EcoreMessageDefinition ecoreMessageDefinition = MrHelper.getMsgDefinitionById(mds, msgDefinitionId);
					String definitionName = ecoreMessageDefinition.getName();
					EcoreTreeNode definitionEcoreTreeNode = new EcoreTreeNode();
					definitionEcoreTreeNode.setId(msgDefinitionId);
					definitionEcoreTreeNode.setName(definitionName);
					definitionEcoreTreeNode.setLevel(TreeLevelEnum.LEVEL_3.getLevel());
					definitionEcoreTreeNode.setType(TreeMenuEnum.MESSAGE_DEFINITIONS.getName());
					String msgDefinitionImgPath = null;
					if (RegistrationStatusEnum.Registered.getStatus()
							.equals(ecoreMessageDefinition.getRegistrationStatus())) {
						msgDefinitionImgPath = ImgUtil.MD_SUB1;
					} else {
						msgDefinitionImgPath = ImgUtil.MESSAGE_DEFINITIONS;
					}
					MrTreeItem definitionTreeListItem = new MrTreeItem(treeListItem,
							new TreeItemDataBean(msgDefinitionId, definitionName, msgDefinitionImgPath, "3",
									TreeLevelEnum.LEVEL_3.getLevel()));
					definitionEcoreTreeNode.setImgPath(msgDefinitionImgPath);
					setTreeItem(definitionTreeListItem, definitionEcoreTreeNode, TreeMenuEnum.MESSAGE_DEFINITIONS.getName(), definitionName);
					// Message Element(Message Component) 节点
					List<EcoreMessageBuildingBlock> embbs = mbbs.stream()
							.filter(b -> msgDefinitionId.equals(b.getMessageId())).collect(Collectors.toList());
					for (EcoreMessageBuildingBlock msgBldBlock : embbs) {
						String elementDisplayName = msgBldBlock.getName() + " [" + msgBldBlock.getMinOccurs() + ","
								+ msgBldBlock.getMaxOccurs() + "]";
						EcoreTreeNode meTreeNode = new EcoreTreeNode();
						meTreeNode.setImgPath(msgDefinitionImgPath);
						meTreeNode.setId(msgDefinitionId);
						meTreeNode.setLevel(TreeLevelEnum.LEVEL_4.getLevel());
						meTreeNode.setType(TreeMenuEnum.MESSAGE_DEFINITIONS_CHILD.getName());
						// 数据类型 1：datatype，2：消息组件
						if ("2".equals(msgBldBlock.getDataType())) {
							EcoreMessageComponent mc = mcMap.get(msgBldBlock.getDataTypeId());
							meTreeNode.setName(elementDisplayName + " : " + mc.getName());
							String mbbImgPath = null;
							if (RegistrationStatusEnum.Registered.getStatus()
									.equals(ecoreMessageDefinition.getRegistrationStatus())) {
								mbbImgPath = ImgUtil.MC_SUB2_COMPONENT;
							} else {
								mbbImgPath = ImgUtil.ME;
							}
							MrTreeItem meTreeListItem = new MrTreeItem(definitionTreeListItem,
									new TreeItemDataBean(msgBldBlock.getId(), elementDisplayName + " : " + mc.getName(),
											mbbImgPath, "3", TreeLevelEnum.LEVEL_4.getLevel()));
							setDefinitionsChildItem(meTreeListItem, meTreeNode, msgBldBlock.getId(), elementDisplayName + " : " + mc.getName());
						} else if ("1".equals(msgBldBlock.getDataType())) {
							EcoreDataType ecoreDatatype = dtMap.get(msgBldBlock.getDataTypeId());
							meTreeNode.setName(elementDisplayName + " : " + ecoreDatatype.getName());
							String mbbImgPath = null;
							if (RegistrationStatusEnum.Registered.getStatus()
									.equals(ecoreMessageDefinition.getRegistrationStatus())) {
								mbbImgPath = ImgUtil.MC_SUB2_DATA_TYPE;
							} else {
								mbbImgPath = ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK;
							}
							MrTreeItem meTreeListItem = new MrTreeItem(definitionTreeListItem,
									new TreeItemDataBean(msgBldBlock.getId(),
											elementDisplayName + " : " + ecoreDatatype.getName(), mbbImgPath, "3",
											TreeLevelEnum.LEVEL_4.getLevel()));
							setDefinitionsChildItem(meTreeListItem, meTreeNode, msgBldBlock.getId(), elementDisplayName + " : " + ecoreDatatype.getName());
						} else {
							// 没有指定Message Component和DataType的情况
							meTreeNode.setName(elementDisplayName);
							String mbbImgPath = null;
							if (RegistrationStatusEnum.Registered.getStatus()
									.equals(ecoreMessageDefinition.getRegistrationStatus())) {
								mbbImgPath = ImgUtil.MC_SUB2_COMPONENT;
							} else {
								mbbImgPath = ImgUtil.ME;
							}
							MrTreeItem meTreeListItem = new MrTreeItem(definitionTreeListItem,
									new TreeItemDataBean(msgBldBlock.getId(), elementDisplayName, mbbImgPath, "3",
											TreeLevelEnum.LEVEL_4.getLevel()));
							setDefinitionsChildItem(meTreeListItem, meTreeNode, msgBldBlock.getId(), elementDisplayName);
						}
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else if ("msgSetCreate".equals(name) && ier.isDirty()) {
			MessageSetEditor msgSetEditor = (MessageSetEditor) ier;
			EcoreMessageSetVO msgSetVO = msgSetEditor.getEcoreMessageSetVO();
			List<EcoreMessageSetVO> msgSetVOList = new ArrayList<>();
			msgSetVOList.add(msgSetVO);
			try {
				MrImplManager.get().getEcoreMessageSetImpl().saveMessageSetVOList(msgSetVOList);
				// 重置相关数据
				MrRepository.get().reset(MrRepository.get().ECORE_CONSTRAINT, MrRepository.get().ECORE_EXAMPLE, MrRepository.get().ECORE_MESSAGE_SET);
				
				Map<String, EcoreDataType> dtMap = MrRepository.get().ecoreDataTypeMapById;
				Map<String, EcoreMessageComponent> mcMap = MrRepository.get().ecoreMessageComponentMapById;
				List<EcoreMessageBuildingBlock> mbbs = MrRepository.get().ecoreMessageBuildingBlocks;
				List<EcoreMessageDefinition> mds = MrRepository.get().ecoreMessageDefinitions;
				
				msgSetEditor.setDirty(false);
				EcoreMessageSet msgSet = msgSetVO.getEcoreMessageSet();
				EcoreTreeNode ecoreTreeNodeDT = new EcoreTreeNode();
				ecoreTreeNodeDT.setId(msgSet.getId());
				ecoreTreeNodeDT.setName(msgSet.getName());
				ecoreTreeNodeDT.setLevel(TreeLevelEnum.LEVEL_2.getLevel());
				ecoreTreeNodeDT.setType(editorInput.getTransferDataBean().getType());
				ecoreTreeNodeDT.setRegistrationStatus(msgSet.getRegistrationStatus());
				setTreeItem(treeListItem, ecoreTreeNodeDT, editorInput.getTransferDataBean().getType(), msgSet.getName());
				editorInput.getTransferDataBean().setId(ecoreTreeNodeDT.getId());
				editorInput.getTransferDataBean().setType(editorInput.getTransferDataBean().getType());
				editorInput.getTransferDataBean().setName(ecoreTreeNodeDT.getName());
				treeListItem.removeAll();
				// Message Definition 节点
				for (EcoreMessageSetDefinitionRL rl : msgSetVO.getEcoreMessageSetDefinitionRLs()) {
					String msgDefinitionId = rl.getMessageId();
					EcoreMessageDefinition ecoreMessageDefinition = MrHelper.getMsgDefinitionById(mds, msgDefinitionId);
					String definitionName = ecoreMessageDefinition.getName();
					EcoreTreeNode definitionEcoreTreeNode = new EcoreTreeNode();
					definitionEcoreTreeNode.setId(msgDefinitionId);
					definitionEcoreTreeNode.setName(definitionName);
					definitionEcoreTreeNode.setLevel(TreeLevelEnum.LEVEL_3.getLevel());
					definitionEcoreTreeNode.setType(TreeMenuEnum.MESSAGE_DEFINITIONS.getName());
					String msgDefinitionImgPath = null;
					if (RegistrationStatusEnum.Registered.getStatus()
							.equals(ecoreMessageDefinition.getRegistrationStatus())) {
						msgDefinitionImgPath = ImgUtil.MD_SUB1;
					} else {
						msgDefinitionImgPath = ImgUtil.MESSAGE_DEFINITIONS;
					}
					MrTreeItem definitionTreeListItem = new MrTreeItem(treeListItem, new TreeItemDataBean(msgDefinitionId,
							definitionName, msgDefinitionImgPath, "3", TreeLevelEnum.LEVEL_3.getLevel()));
					definitionEcoreTreeNode.setImgPath(msgDefinitionImgPath);
					setTreeItem(definitionTreeListItem, definitionEcoreTreeNode, TreeMenuEnum.MESSAGE_DEFINITIONS.getName(), definitionName);
					// Message Element(Message Component) 节点
					List<EcoreMessageBuildingBlock> embbs = mbbs.stream()
							.filter(b -> msgDefinitionId.equals(b.getMessageId())).collect(Collectors.toList());
					for (EcoreMessageBuildingBlock msgBldBlock : embbs) {
						String elementDisplayName = msgBldBlock.getName() + " [" + msgBldBlock.getMinOccurs() + ","
								+ msgBldBlock.getMaxOccurs() + "]";
						EcoreTreeNode meTreeNode = new EcoreTreeNode();
						meTreeNode.setId(msgDefinitionId);
						meTreeNode.setLevel(TreeLevelEnum.LEVEL_4.getLevel());
						meTreeNode.setType(TreeMenuEnum.MESSAGE_DEFINITIONS_CHILD.getName()); // MESSAGE_COMPONENTS
						// 数据类型 1：datatype，2：消息组件
						if ("2".equals(msgBldBlock.getDataType())) {
							EcoreMessageComponent mc = mcMap.get(msgBldBlock.getDataTypeId());
							meTreeNode.setName(elementDisplayName + " : " + mc.getName());
							String mbbImgPath = null;
							if (RegistrationStatusEnum.Registered.getStatus()
									.equals(ecoreMessageDefinition.getRegistrationStatus())) {
								mbbImgPath = ImgUtil.MC_SUB2_COMPONENT;
							} else {
								mbbImgPath = ImgUtil.ME;
							}
							MrTreeItem meTreeListItem = new MrTreeItem(definitionTreeListItem,
									new TreeItemDataBean(msgBldBlock.getId(), elementDisplayName + " : " + mc.getName(),
											mbbImgPath, "3", TreeLevelEnum.LEVEL_4.getLevel()));
							meTreeNode.setImgPath(msgDefinitionImgPath);
							setDefinitionsChildItem(meTreeListItem, meTreeNode, msgBldBlock.getId(), elementDisplayName + " : " + mc.getName());
						} else if ("1".equals(msgBldBlock.getDataType())) {
							EcoreDataType ecoreDatatype = dtMap.get(msgBldBlock.getDataTypeId());
							meTreeNode.setName(elementDisplayName + " : " + ecoreDatatype.getName());
							String mbbImgPath = null;
							if (RegistrationStatusEnum.Registered.getStatus()
									.equals(ecoreMessageDefinition.getRegistrationStatus())) {
								mbbImgPath = ImgUtil.MC_SUB2_DATA_TYPE;
							} else {
								mbbImgPath = ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK;
							}
							MrTreeItem meTreeListItem = new MrTreeItem(definitionTreeListItem,
									new TreeItemDataBean(msgBldBlock.getId(),
											elementDisplayName + " : " + ecoreDatatype.getName(), mbbImgPath, "3",
											TreeLevelEnum.LEVEL_4.getLevel()));
							setDefinitionsChildItem(meTreeListItem, meTreeNode, msgBldBlock.getId(), elementDisplayName + " : " + ecoreDatatype.getName());
						} else {
							// 没有指定Message Component和DataType的情况
							meTreeNode.setName(elementDisplayName);
							String mbbImgPath = null;
							if (RegistrationStatusEnum.Registered.getStatus()
									.equals(ecoreMessageDefinition.getRegistrationStatus())) {
								mbbImgPath = ImgUtil.MC_SUB2_COMPONENT;
							} else {
								mbbImgPath = ImgUtil.ME;
							}
							MrTreeItem meTreeListItem = new MrTreeItem(definitionTreeListItem,
									new TreeItemDataBean(msgBldBlock.getId(), elementDisplayName, mbbImgPath, "3",
											TreeLevelEnum.LEVEL_4.getLevel()));
							setDefinitionsChildItem(meTreeListItem, meTreeNode, msgBldBlock.getId(), elementDisplayName);
						}
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else if ("msgDefinitionCreate".equals(name) && ier.isDirty()) {
			MessageDefinitionEditor msgDefinitionEditor = (MessageDefinitionEditor) ier;
			TreeItem[] treeItems = msgDefinitionEditor.modelExploreTreeItem.getParentItem().getItems();
			//树节点有重复且和修改名称相等的
			Map<String, Integer> repeat = repeat(treeItems);
			//获取编辑的数据
			EcoreMessageDefinitionVO msgDefinitionVO = msgDefinitionEditor.getEcoreMessageDefinitionVO();
			EcoreMessageDefinition msgDefinition = msgDefinitionEditor.getMsgDefinition();
			Integer countRepeat = repeat.get(msgDefinition.getName());
			if(countRepeat != null) {
				MrEditorDialogCreator.definitionSaveFail(msgDefinitionEditor.nameText, MrRepository.get().ecoreMessageDefinitions);
				return null;
			}
			String pat = "V\\d+";
			Pattern p = Pattern.compile(pat);
			String[] split = p.split(msgDefinitionVO.getEcoreMessageDefinition().getName());
			String version = MrEditorHelper.getVersion(msgDefinitionVO.getEcoreMessageDefinition().getName(), p, split);
			if("".equals(version)) {
				MessageDialog.openWarning(Display.getDefault().getActiveShell(), I18nApi.get("tips.name"), I18nApi.get("tips.md.error.name.one"));
				return "0";
			}
			//删除版本关联
			MrImplManager.get().getEcoreNextVersionsImpl().delById(msgDefinition.getId(), null);
			MrImplManager.get().getEcoreNextVersionsImpl().delByNextVersionId(msgDefinition.getId());
			//统计版本关联
			int count = MrImplManager.get().getEcoreNextVersionsImpl().countByNextVersionId(msgDefinitionVO.getEcoreMessageDefinition().getId());
			if(count < 1) {
				//获取上一个版本
				String previousName = definitionPreviousName(msgDefinitionVO.getEcoreMessageDefinition().getName());
				if("0".equals(previousName)) {
					return null;
				}
				if(!StringUtils.isEmpty(previousName)) {
					String previousId = MrImplManager.get().getEcoreMessageDefinitionImpl().getIdByName(previousName);
					if(!StringUtils.isEmpty(previousId) && !previousId.equals(msgDefinitionVO.getEcoreMessageDefinition().getId())) {
						List<EcoreNextVersions> nextVersionList = new ArrayList<>();
						EcoreNextVersions nextVersion = new EcoreNextVersions();
						nextVersion.setId(previousId);
						nextVersion.setNextVersionId(msgDefinitionVO.getEcoreMessageDefinition().getId());
						nextVersion.setObjType(ObjTypeEnum.MessageDefinition.getType());
						nextVersionList.add(nextVersion);
						msgDefinitionVO.setEcoreNextVersions(nextVersionList);
					}
				}
			}
			List<EcoreMessageDefinitionVO> msgDefinitionVOlist = new ArrayList<>();
			msgDefinitionVOlist.add(msgDefinitionVO);
			try {
				MrImplManager.get().getEcoreMessageDefinitionImpl().saveMessageDefinitionVOList(msgDefinitionVOlist);
				// 重置相关数据
				MrRepository.get().reset(MrRepository.get().ECORE_MESSAGE_DEFINITION, MrRepository.get().ECORE_EXAMPLE, MrRepository.get().ECORE_MESSAGE_BUILDING_BLOCK,
						MrRepository.get().ECORE_CONSTRAINT, MrRepository.get().ECORE_SEMANTIC_MARKUP,
						MrRepository.get().ECORE_SEMANTIC_MARKUP_ELEMENT, MrRepository.get().ECORE_NEXT_VERSIONS);
				msgDefinitionEditor.setPreviousVersion(MrRepository.get().ecoreMessageDefinitionMapById);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			
			Map<String, EcoreDataType> dtMap = MrRepository.get().ecoreDataTypeMapById;
			Map<String, EcoreMessageComponent> mcMap = MrRepository.get().ecoreMessageComponentMapById;
			msgDefinitionEditor.setDirty(false);

			String mdId = msgDefinition.getId();
			// Message Definition 节点
			EcoreTreeNode ecoreTreeNodeDT = new EcoreTreeNode();

			ecoreTreeNodeDT.setId(mdId);
			ecoreTreeNodeDT.setName(msgDefinition.getName());
			ecoreTreeNodeDT.setLevel(TreeLevelEnum.LEVEL_2.getLevel());
			ecoreTreeNodeDT.setType(editorInput.getTransferDataBean().getType());
			ecoreTreeNodeDT.setImgPath(editorInput.getTransferDataBean().getImgPath());
			ecoreTreeNodeDT.setRegistrationStatus(msgDefinition.getRegistrationStatus());
			setTreeItem(treeListItem, ecoreTreeNodeDT, editorInput.getTransferDataBean().getType(), msgDefinition.getName());
			editorInput.getTransferDataBean().setId(ecoreTreeNodeDT.getId());
			editorInput.getTransferDataBean().setType(editorInput.getTransferDataBean().getType());
			editorInput.getTransferDataBean().setName(ecoreTreeNodeDT.getName());

			treeListItem.removeAll();

			// Message Element(Message Component) 节点
			List<EcoreMessageBuildingBlock> mbbs = MrImplManager.get().getEcoreMessageBuildingBlockImpl().findByMsgId(mdId);
			for (EcoreMessageBuildingBlock msgBldBlock : mbbs) {
				String elementDisplayName = msgBldBlock.getName() + " [" + msgBldBlock.getMinOccurs() + ","
						+ msgBldBlock.getMaxOccurs() + "]";
				EcoreTreeNode meTreeNode = new EcoreTreeNode();
				meTreeNode.setImgPath(editorInput.getTransferDataBean().getImgPath());
				meTreeNode.setId(mdId);
				meTreeNode.setLevel(TreeLevelEnum.LEVEL_3.getLevel());
				meTreeNode.setType(TreeMenuEnum.MESSAGE_DEFINITIONS_CHILD.getName());
				// 数据类型 1：datatype，2：消息组件
				if ("2".equals(msgBldBlock.getDataType())) {
					EcoreMessageComponent mc = mcMap.get(msgBldBlock.getDataTypeId());
					meTreeNode.setName(elementDisplayName + " : " + mc.getName());
					MrTreeItem meTreeListItem = new MrTreeItem(treeListItem,
							new TreeItemDataBean(msgBldBlock.getId(), elementDisplayName + " : " + mc.getName(),
									ImgUtil.ME, "3", TreeLevelEnum.LEVEL_3.getLevel()));
					setDefinitionsChildItem(meTreeListItem, meTreeNode, msgBldBlock.getId(), elementDisplayName + " : " + mc.getName());
				} else if ("1".equals(msgBldBlock.getDataType())) {
					EcoreDataType ecoreDatatype = dtMap.get(msgBldBlock.getDataTypeId());
					meTreeNode.setName(elementDisplayName + " : " + ecoreDatatype.getName());
					MrTreeItem meTreeListItem = new MrTreeItem(treeListItem,
							new TreeItemDataBean(msgBldBlock.getId(),
									elementDisplayName + " : " + ecoreDatatype.getName(),
									ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK, "3", TreeLevelEnum.LEVEL_3.getLevel()));
					setDefinitionsChildItem(meTreeListItem, meTreeNode, msgBldBlock.getId(), elementDisplayName + " : " + ecoreDatatype.getName());
				} else {
					// 没有指定Message Component和DataType的情况
					meTreeNode.setName(elementDisplayName);
					MrTreeItem meTreeListItem = new MrTreeItem(treeListItem, new TreeItemDataBean(msgBldBlock.getId(),
							elementDisplayName, ImgUtil.ME, "3", TreeLevelEnum.LEVEL_3.getLevel()));
					setDefinitionsChildItem(meTreeListItem, meTreeNode, msgBldBlock.getId(), elementDisplayName);
				}
			}
		}
		return null;
	}
	
	/**
	 * 设置报文节点
	 */
	private void setDefinitionsChildItem(MrTreeItem item, EcoreTreeNode meTreeNode, String childId, String name) {
		setItem(item, meTreeNode, TreeMenuEnum.MESSAGE_DEFINITIONS_CHILD.getName(), childId, name);
	}
	
	/**
	 * 设置树节点
	 */
	private void setTreeItem(MrTreeItem item, EcoreTreeNode meTreeNode, String type, String name) {
		setItem(item, meTreeNode, type, "", name);
	}
	
	/**
	 * 设置节点
	 */
	private void setItem(MrTreeItem item, EcoreTreeNode meTreeNode, String type, String childId, String name) {
		item.setData("EcoreTreeNode", meTreeNode);
		item.setData("type", type);
		if(!StringUtils.isEmpty(childId)) {
			item.setData("childId", childId);
		}
		if(!StringUtils.isEmpty(name)) {
			item.setText(name);
		}
	}
	
	/**
	 * 组件历史版本名称获取
	 * @param name
	 */
	private String componentPreviousName(String name) {
		String previous = "";
		String previousVersion = "";
		String pat = "\\d+";
		Pattern p = Pattern.compile(pat);
		String[] split = p.split(name);
		String version = MrEditorHelper.getVersion(name, p, split);
		if(!"".equals(version)) {
			if(version.length() <= 2) {
				if(version.length() == 2) {
					if((version.charAt(0) + "").equals("0")) {
						if((version.charAt(1) + "").equals("0") || (version.charAt(1) + "").equals("1")) {
							//有版本新组件
							return "";
						}else {
							int i = version.charAt(1) - '0';
							i--;
							previousVersion = "" + version.charAt(0) + i;
						}
					}else {
						int i = version.charAt(1) - '0';
						if(i > 0) {
							i--;
							previousVersion = "" + version.charAt(0) + i;
						}else {
							int j = version.charAt(0) - '0';
							j--;
							previousVersion = j + "9";
						}
					}
				}else {
					if((version.charAt(0) + "").equals("0") || (version.charAt(0) + "").equals("1")) {
						//有版本新组件
						return "";
					}else {
						int i = version.charAt(0) - '0';
						i--;
						previousVersion = i + "";
					}
				}
			}else {
				MessageDialog.openWarning(Display.getDefault().getActiveShell(), I18nApi.get("tips.name"), I18nApi.get("tips.mc.error.name.two"));
			}
		}
		if(split.length == 1) {
			previous = split[0] + previousVersion;
		}else if(split.length == 2){
			previous = split[0] + previousVersion + split[1];
		}
		return previous;
	}
	
	/**
	 * 报文历史版本名称获取
	 * @param name
	 */
	private String definitionPreviousName(String name) {
		String previous = "";
		String previousVersion = "";
		String pat = "V\\d+";
		Pattern p = Pattern.compile(pat);
		String[] split = p.split(name);
		String version = MrEditorHelper.getVersion(name, p, split);
		if(!"".equals(version)) {
			if(version.length() == 3) {
				if((version.charAt(1) + "").equals("0")) {
					if((version.charAt(2) + "").equals("0") || (version.charAt(2) + "").equals("1")) {
						//有版本新报文
						return "";
					}else {
						int i = version.charAt(2) - '0';
						i--;
						previousVersion = "" + version.charAt(0) + version.charAt(1) + i;
					}
				}else {
					int i = version.charAt(2) - '0';
					if(i > 0) {
						i--;
						previousVersion = "" + version.charAt(0) + version.charAt(1) + i;
					}else {
						int j = version.charAt(1) - '0';
						j--;
						previousVersion = "" + version.charAt(0) + j + "9";
					}
				}
			}else {
				MessageDialog.openWarning(Display.getDefault().getActiveShell(), I18nApi.get("tips.name"), I18nApi.get("tips.md.error.name.two"));
				return "0";
			}
		}
		if(split.length == 1) {
			previous = split[0] + previousVersion;
		}else if(split.length == 2){
			previous = split[0] + previousVersion + split[1];
		}
		return previous;
	}
	
	/**
	 * 找出树节点中的重复名称与重复次数
	 * @param items
	 */
	private Map<String,Integer> repeat(TreeItem[] items) {
		//数据基数
		Map<String,Integer> map = new HashMap<String, Integer>();
		//重复名称、次数
		Map<String,Integer> repeat = new HashMap<String, Integer>();
		for (int i = 0; i < items.length; i++) {
			String name = items[i].getText();
			Integer count = map.get(name);
			if(count == null) {
				map.put(name, 0);
			}else {
				count++;
				repeat.put(name, count);
			}
		}
		return repeat;
	}
}
