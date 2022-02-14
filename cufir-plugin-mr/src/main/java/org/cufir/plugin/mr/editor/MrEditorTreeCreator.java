package org.cufir.plugin.mr.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cufir.plugin.mr.MrHelper;
import org.cufir.plugin.mr.bean.ObjTypeEnum;
import org.cufir.plugin.mr.bean.RegistrationStatusEnum;
import org.cufir.plugin.mr.utils.ImgUtil;
import org.cufir.s.data.bean.EcoreBusinessElement;
import org.cufir.s.data.bean.EcoreCode;
import org.cufir.s.data.bean.EcoreConstraint;
import org.cufir.s.data.bean.EcoreDataType;
import org.cufir.s.data.bean.EcoreExternalSchema;
import org.cufir.s.data.bean.EcoreMessageBuildingBlock;
import org.cufir.s.data.bean.EcoreMessageComponent;
import org.cufir.s.data.bean.EcoreMessageDefinition;
import org.cufir.s.data.bean.EcoreMessageElement;
import org.cufir.s.data.bean.EcoreSemanticMarkup;
import org.cufir.s.data.bean.EcoreSemanticMarkupElement;
import org.cufir.s.data.vo.EcoreTreeNode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 生成树
 * 
 * @author gongyi_tt
 *
 */
public class MrEditorTreeCreator {

	private List<EcoreSemanticMarkup> sms = MrRepository.get().ecoreSemanticMarkups;
	private List<EcoreSemanticMarkupElement> smes = MrRepository.get().ecoreSemanticMarkupElements;
	private List<EcoreMessageBuildingBlock> mbbs = MrRepository.get().ecoreMessageBuildingBlocks;
	private Map<String, List<EcoreMessageElement>> mesByMsgComponentId = MrRepository.get().ecoreMessageElementMapByMsgComponentId;
	private Map<String, List<EcoreConstraint>> constraintMapByObjId = MrRepository.get().ecoreConstraintMapByObjId;
	private Map<String, EcoreMessageComponent> ecoreMessageComponents = MrRepository.get().ecoreMessageComponentMapById;
	private Map<String, EcoreDataType> dataTypeMapById = MrRepository.get().ecoreDataTypeMapById;
	private Map<String, List<EcoreCode>> codeMapByCodeSetId = MrRepository.get().ecoreCodeMapByCodeSetId;
	private Map<String, EcoreExternalSchema> externalSchemaMapById = MrRepository.get().ecoreExternalSchemaMapById;
	

	public void generateContainmentTreeForMessageSetofImpactPage(TreeItem treeRoot,
			List<EcoreMessageDefinition> msgDefinitionList) {
		for (EcoreMessageDefinition msgDefinition : msgDefinitionList) {
			TreeItem treeItem = new TreeItem(treeRoot, SWT.NONE);
			treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB1));
			treeItem.setText(msgDefinition.getName());
			treeItem.setData("msgDefinitionId", msgDefinition.getId());
			List<EcoreMessageBuildingBlock> mbbsf = MrHelper.findMsgBuildingBlocksByMsgId(mbbs,msgDefinition.getId());
			if(mbbsf != null) {
				generateContainmentTreeForMessageDefinition(treeItem, mbbsf);
			}
		}
	}

	public void generateContainmentTreeForMessageDefinition(TreeItem treeRoot,
			List<EcoreMessageBuildingBlock> msgBldBlockList) {
		for (EcoreMessageBuildingBlock msgBldBlock : msgBldBlockList) {
			TreeItem treeItem = new TreeItem(treeRoot, SWT.NONE);
			String min = msgBldBlock.getMinOccurs() == null ? "0" : msgBldBlock.getMinOccurs() + "";
			String max = msgBldBlock.getMaxOccurs() == null ? "*" : msgBldBlock.getMaxOccurs() + "";
			String elementDisplayName = msgBldBlock.getName() + " [" + min + ", " + max + "]";
			treeItem.setText(elementDisplayName);
			treeItem.setData("bbId", msgBldBlock.getId());
			// 数据类型 1：datatype，2：消息组件
			if ("2".equals(msgBldBlock.getDataType())) {
				EcoreMessageComponent mc = ecoreMessageComponents.get(msgBldBlock.getDataTypeId());
				if(mc == null) {
					continue;
				}
				treeItem.setText(elementDisplayName + " : " + mc.getName());
				// 1：MessageComponent，2：ChoiceComponent
				if ("1".equals(mc.getComponentType())) {
					treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
				} else if ("2".equals(mc.getComponentType())) {
					treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
				}
				List<EcoreMessageElement> mesf = mesByMsgComponentId.get(mc.getId());
				// Recursive invoke 消息组件
				if(mesf != null) {
					generateContainmentTreeForMessageComponent(treeItem, mesf);
				}
				// Load Message Component Constraint
				List<EcoreConstraint> ecss = constraintMapByObjId.get(mc.getId());
				addConstraintsNode(treeItem, ecss, mc.getName(), null);
			} else {
				// 数据类型 1：datatype
				EcoreDataType dataType = dataTypeMapById.get(msgBldBlock.getDataTypeId());
				if(dataType == null) {
					continue;
				}
				treeItem.setText(elementDisplayName + " : " + dataType.getName());
				// 1:CodeSet;2:Text;3:Boolean,4:Indicator,5:Decimal,6:Rate,7:Amount,8:Quantity,9:Time,10:Binary,11:SchemaType,12:UserDefined
				if ("1".equals(dataType.getType())) {
					treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
					for (EcoreCode ecoreCode : codeMapByCodeSetId.get(dataType.getId())) {
						TreeItem codeTreeItem = new TreeItem(treeItem, SWT.NONE);
						codeTreeItem.setText(ecoreCode.getName() + " [" + ecoreCode.getCodeName() + "]");
						codeTreeItem.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
						codeTreeItem.setData(ecoreCode.getId());
					}
				} else {
					treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
					List<EcoreConstraint> ecss = constraintMapByObjId.get(dataType.getId());
					addConstraintsNode(treeItem, ecss, dataType.getName(), null);
				}
			}
		}
	}

	public void generateContainmentTreeForBusniessTrace(TreeItem treeRoot, List<EcoreMessageElement> meList) {
		for (EcoreMessageElement me : meList) {
			TreeItem treeItem = new TreeItem(treeRoot, SWT.NONE);
			String min = me.getMinOccurs() == null ? "0" : me.getMinOccurs() + "";
			String max = me.getMaxOccurs() == null ? "*" : me.getMaxOccurs() + "";
			String elementDisplayName = me.getName() + " [" + min + ", " + max + "]";
			if ("2".equals(me.getTraceType())) {
				treeItem.setImage(2, ImgUtil.createImg(ImgUtil.BC));
			} else if ("1".equals(me.getTraceType())) {
				treeItem.setImage(2, ImgUtil.createImg(ImgUtil.BC_BE));
			}
			// 数据类型 1：datatype，2：消息组件
			if ("2".equals(me.getType())) {
				EcoreMessageComponent mc = ecoreMessageComponents.get(me.getTypeId());
				if(mc == null) {
					continue;
				}
				treeItem.setText(elementDisplayName + " : " + mc.getName());
				// 1：MessageComponent，2：ChoiceComponent
				setMessageComponentTreeItemImage(treeRoot, treeItem, mc);
				if (treeItem.getData("bcType_id") != null) {
					treeItem.setData("trace", treeItem.getData("bcType_id"));
				} else if (treeItem.getData("beType_id") != null) {
					treeItem.setData("trace", treeItem.getData("beType_id"));
				}
				// 表格中TypeOfTracesTo取值逻辑,getAll存储时使用
				if (mc.getTechnical().equals("true")) {
					treeItem.setData("me_bcTrace", "--TECHNICAL--");
				} else if (mc.getTraceName() != null) {
					treeItem.setData("me_bcTrace", mc.getTraceName());
				}
				treeItem.setText(1, me.getTechnical() == null ? "false" : me.getTechnical());
				treeItem.setText(2, me.getTracesTo() == null ? "" : me.getTracesTo());
				treeItem.setText(3, me.getTracePath() == null ? "" : me.getTracePath());
				treeItem.setText(4, me.getTypeOfTracesTo() == null ? "" : me.getTypeOfTracesTo());
				if (mc.getTechnical().equals("true")) {
					treeItem.setText(4, "--TECHNICAL--");
				}
				List<EcoreMessageElement> mesf = mesByMsgComponentId.get(mc.getId());
				if(mesf != null) {
					generateContainmentTreeForBusniessTrace(treeItem, mesf);
				}
			} else {
				EcoreDataType dataType = dataTypeMapById.get(me.getTypeId());
				if(dataType == null) {
					continue;
				}
				treeItem.setText(elementDisplayName + " : " + dataType.getName());
				treeItem.setText(1, me.getTechnical() == null ? "false" : me.getTechnical());
				treeItem.setText(2, me.getTracesTo() == null ? "" : me.getTracesTo());
				treeItem.setText(3, me.getTracePath() == null ? "" : me.getTracePath());
				// 基本数据类型表格4显示空
				treeItem.setText(4, "");
				if (treeItem.getData("bcType_id") != null) {
					treeItem.setData("trace", treeItem.getData("bcType_id"));
				} else if (treeItem.getData("beType_id") != null) {
					treeItem.setData("trace", treeItem.getData("beType_id"));
				}
				// 1:CodeSet;2:Text;3:Boolean,4:Indicator,5:Decimal,6:Rate,7:Amount,8:Quantity,9:Time,10:Binary,11:SchemaType,12:UserDefined
				boolean is = treeRoot.getData("mcStatusCombo") == null || treeRoot.getData("mcStatusCombo").equals("Provisionally Registered");
				if ("1".equals(dataType.getType())) { // codeSet 已到最底层了
					setTreeItemImage(treeItem, is, ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK, ImgUtil.MC_SUB2_DATA_TYPE);
					setCodeTreeItem(codeMapByCodeSetId.get(dataType.getId()), treeItem);
				} else {
					setTreeItemImage(treeItem, is, ImgUtil.MC_SUB2_COMPONENT1, ImgUtil.MC_SUB2_COMPONENT);
					treeItem.setText(1, me.getTechnical() == null ? "false" : me.getTechnical());
					treeItem.setText(2, me.getTracesTo() == null ? "" : me.getTracesTo());
					treeItem.setText(3, me.getTracePath() == null ? "" : me.getTracePath());
					treeItem.setText(4, "");
					if (treeItem.getData("bcType_id") != null) {
						treeItem.setData("trace", treeItem.getData("bcType_id"));
					} else if (treeItem.getData("beType_id") != null) {
						treeItem.setData("trace", treeItem.getData("beType_id"));
					}
				}
			}
		}
	}

	public void generateContainmentTreeForMessageComponents(TreeItem treeRoot, List<EcoreMessageElement> meList,
			boolean... isMe) {
		List<EcoreConstraint> constraintList = new ArrayList<EcoreConstraint>();
		for (EcoreMessageElement me : meList) {
			if(constraintList != null) {
				constraintList.clear();
			}
			TreeItem treeItem = new TreeItem(treeRoot, SWT.NONE);
			String min = me.getMinOccurs() == null ? "0" : me.getMinOccurs() + "";
			String max = me.getMaxOccurs() == null ? "*" : me.getMaxOccurs() + "";
			String elementDisplayName = me.getName() + " [" + min + ", " + max + "]";
			treeItem.setText(1, me.getTechnical() == null ? "false" : me.getTechnical());
			treeItem.setText(2, me.getTracesTo() == null ? "" : me.getTracesTo());
			treeItem.setImage(2, ImgUtil.createImg(ImgUtil.BC));
			treeItem.setText(3, me.getTracePath() == null ? "" : me.getTracePath());
			treeItem.setText(4, me.getTypeOfTracesTo() == null ? "" : me.getTypeOfTracesTo());
			// 数据类型 1：datatype，2：消息组件
			if ("2".equals(me.getType())) {
				EcoreMessageComponent mc = ecoreMessageComponents.get(me.getTypeId());
				if(mc == null) {
					continue;
				}
				treeItem.setText(elementDisplayName + " : " + mc.getName());
				// 1：MessageComponent，2：ChoiceComponent
				setMessageComponentTreeItemImage(treeRoot, treeItem, mc);
				List<EcoreMessageElement> mesf = mesByMsgComponentId.get(mc.getId());
				if(mesf != null) {
					generateContainmentTreeForMessageComponents(treeItem, mesf, isMe);
				}
				// Load Message Component Constraint
				constraintList = constraintMapByObjId.get(mc.getId());
				addConstraintsNode(treeItem, constraintList, mc.getName(), null);
			} else {
				// datatype
				EcoreDataType dataType = dataTypeMapById.get(me.getTypeId());
				if(dataType == null) {
					continue;
				}
				treeItem.setText(elementDisplayName + " : " + dataType.getName());
				// 1:CodeSet;2:Text;3:Boolean,4:Indicator,5:Decimal,6:Rate,7:Amount,8:Quantity,9:Time,10:Binary,11:SchemaType,12:UserDefined
				boolean is = treeRoot.getData("mcStatusCombo") == null
						|| treeRoot.getData("mcStatusCombo").equals("Provisionally Registered");
				if ("1".equals(dataType.getType())) { // codeSet 已到最底层了
					setTreeItemImage(treeItem, is, ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK, ImgUtil.MC_SUB2_DATA_TYPE);
					setCodeTreeItem(codeMapByCodeSetId.get(dataType.getId()), treeItem);
				} else {
					setTreeItemImage(treeItem, is, ImgUtil.MC_SUB2_COMPONENT1, ImgUtil.MC_SUB2_COMPONENT);
					constraintList = constraintMapByObjId.get(dataType.getId());
					addConstraintsNode(treeItem, constraintList, dataType.getName(), null);
				}
			}
			setMessageElementItem(treeItem, me, constraintList, isMe);
		}
	}

	public void generateContainmentTreeForMessageComponent(TreeItem treeRoot, List<EcoreMessageElement> meList,
			boolean... isMe) {
		for (EcoreMessageElement me : meList) {
			TreeItem treeItem = new TreeItem(treeRoot, SWT.NONE);
			String min = me.getMinOccurs() == null ? "0" : me.getMinOccurs() + "";
			String max = me.getMaxOccurs() == null ? "*" : me.getMaxOccurs() + "";
			String elementDisplayName = me.getName() + " [" + min + ", " + max + "]";
			List<EcoreConstraint> constraintList = new ArrayList<EcoreConstraint>();
			// 数据类型 1：datatype，2：消息组件
			if ("2".equals(me.getType())) {
				EcoreMessageComponent mc = ecoreMessageComponents.get(me.getTypeId());
				if(mc == null) {
					continue;
				}
				treeItem.setText(elementDisplayName + " : " + mc.getName());
				// 1：MessageComponent，2：ChoiceComponent
				setMessageComponentTreeItemImage(treeRoot, treeItem, mc);
				List<EcoreMessageElement> mesf = mesByMsgComponentId.get(mc.getId());
				if(mesf != null) {
					generateContainmentTreeForMessageComponent(treeItem, mesf, isMe);
				}
				// Load Message Component Constraint
				constraintList = constraintMapByObjId.get(mc.getId());
				addConstraintsNode(treeItem, constraintList, mc.getName(), null);
			} else {
				// datatype
				EcoreDataType dataType = dataTypeMapById.get(me.getTypeId());
				if(dataType == null) {
					continue;
				}
				treeItem.setText(elementDisplayName + " : " + dataType.getName());
				// 1:CodeSet;2:Text;3:Boolean,4:Indicator,5:Decimal,6:Rate,7:Amount,8:Quantity,9:Time,10:Binary,11:SchemaType,12:UserDefined
				boolean is = treeRoot.getData("meStatusCombo") == null
						|| treeRoot.getData("meStatusCombo").equals("Provisionally Registered");
				if ("1".equals(dataType.getType())) { // codeSet 已到最底层了
					setTreeItemImage(treeItem, is, ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK, ImgUtil.MC_SUB2_DATA_TYPE);
					setCodeTreeItem(codeMapByCodeSetId.get(dataType.getId()), treeItem);
				} else {
					setTreeItemImage(treeItem, is, ImgUtil.MC_SUB2_COMPONENT1, ImgUtil.MC_SUB2_COMPONENT);
					constraintList = constraintMapByObjId.get(dataType.getId());
					addConstraintsNode(treeItem, constraintList, dataType.getName(), null);
				}
			}
			setMessageElementItem(treeItem, me, constraintList, isMe);
		}
	}

	public void addConstraintsNode(TreeItem treeRoot, List<EcoreConstraint> css, String nodeName, Tree tree) {
		if (nodeName == null) {
			nodeName = "Textual";
		}
		TreeItem constraintTreeItem;
		if(css != null && css.size() > 0) {
			for (EcoreConstraint cs : css) {
				if (tree == null) {
					constraintTreeItem = new TreeItem(treeRoot, SWT.NONE);
				} else {
					constraintTreeItem = new TreeItem(tree, SWT.NONE);
				}
				constraintTreeItem.setText(nodeName + " : " + cs.getName());
				constraintTreeItem.setData("constraintId", cs.getId());
				constraintTreeItem.setData("constraint", cs);
				constraintTreeItem.setImage(ImgUtil.createImg(ImgUtil.CONSTRAINT));
			}
		}
	}
	
	public void generateContentTreeForMessageComponent(TreeItem treeRoot, List<EcoreMessageElement> meList,
			String registrationStatus) {
		// 循环消息要素集合
		for (EcoreMessageElement me : meList) {
			TreeItem treeItem = new TreeItem(treeRoot, SWT.NONE);
			treeItem.setData("msgElementID", me.getId());
			String min = me.getMinOccurs() == null ? "0" : me.getMinOccurs() + "";
			String max = me.getMaxOccurs() == null ? "*" : me.getMaxOccurs() + "";
			String elementDisplayName = me.getName() + " [" + min + ", " + max + "]";
			if ("2".equals(me.getType())) {
				EcoreMessageComponent mc = ecoreMessageComponents.get(me.getTypeId());
				if(mc == null) {
					continue;
				}
				treeItem.setText(elementDisplayName + " : " + mc.getName());
				boolean is = RegistrationStatusEnum.Registered.getStatus().equals(registrationStatus);
				if ("1".equals(mc.getComponentType())) {
					setTreeItemImage(treeItem, is, ImgUtil.MC_SUB2_COMPONENT, ImgUtil.ME);
				} else if ("2".equals(mc.getComponentType())) {
					setTreeItemImage(treeItem, is, ImgUtil.MC_SUB2_DATA_TYPE, ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK);
				}
				// 在所有要素中找出关联组件的数据集
				List<EcoreMessageElement> mesf = mesByMsgComponentId.get(mc.getId());
				// 进入下一次递归循环
				if(mesf != null) {
					generateContentTreeForMessageComponent(treeItem, mesf, mc.getRegistrationStatus());
				}
				// 在所有组件中找出关联组件的数据集
				List<EcoreConstraint> mcConstraintList = constraintMapByObjId.get(mc.getId());
				addConstraintsNode(treeItem, mcConstraintList, mc.getName(), null);
				treeItem.setData("typeName", mc.getName());
				treeItem.setData("dataType", ObjTypeEnum.MessageComponent.getType());
				treeItem.setData("msgComponentId", mc.getId());
			} else if ("3".equals(me.getType()) || "1".equals(me.getType())) {
				EcoreDataType dataType = dataTypeMapById.get(me.getTypeId());
				if(dataType == null) {
					continue;
				}
				treeItem.setText(elementDisplayName + " : " + dataType.getName());
				// 1:CodeSet;2:Text;3:Boolean,4:Indicator,5:Decimal,6:Rate,7:Amount,8:Quantity,9:Time,10:Binary,11:SchemaType,12:UserDefined
				if ("1".equals(dataType.getType())) { // codeSet 已到最底层了
					boolean is = RegistrationStatusEnum.Registered.getStatus().equals(dataType.getRegistrationStatus());
					setTreeItemImage(treeItem, is, ImgUtil.MC_SUB2_DATA_TYPE, ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK);
					List<EcoreCode> ecs = codeMapByCodeSetId.get(dataType.getId());
					if(ecs != null) {
						for (EcoreCode ecoreCode : ecs) {
							TreeItem codeTreeItem = new TreeItem(treeItem, SWT.NONE);
							codeTreeItem.setText(ecoreCode.getName() + " [" + ecoreCode.getCodeName() + "]");
							codeTreeItem.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
							codeTreeItem.setData("codeId", ecoreCode.getId());
							codeTreeItem.setData("ecoreCode", ecoreCode);
							List<EcoreConstraint> ConstraintList = constraintMapByObjId.get(ecoreCode.getCodesetid());
							addConstraintsNode(treeItem, ConstraintList, ecoreCode.getName(), null);
						}
					}
				} else {
					boolean is = RegistrationStatusEnum.Registered.getStatus().equals(dataType.getRegistrationStatus());
					setTreeItemImage(treeItem, is, ImgUtil.MD_SUB2_DATA_TYPE, ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK);
					// Load data type constraint
					List<EcoreConstraint> ecs = constraintMapByObjId.get(dataType.getId());
					addConstraintsNode(treeItem, ecs, dataType.getName(), null);
				}
				treeItem.setData("typeName", dataType.getName());
				treeItem.setData("dataType", ObjTypeEnum.DataType.getType());
				treeItem.setData("dataTypeId", dataType.getId());
			} else if ("4".equals(me.getType())) {
				// External schemas
				EcoreExternalSchema ecoreExternalSchema = externalSchemaMapById.get(me.getTypeId());
				if(ecoreExternalSchema == null) {
					continue;
				}
				treeItem.setText(elementDisplayName + " : " + ecoreExternalSchema.getName());
				treeItem.setImage(ImgUtil.createImg(ImgUtil.EXTERNAL_SCHEMAS));
				List<EcoreMessageElement> mesf = mesByMsgComponentId.get(ecoreExternalSchema.getId());
				if(mesf != null) {
					generateContentTreeForMessageComponent(treeItem, mesf, ecoreExternalSchema.getRegistrationStatus());
				}
				// Load External Schema Constraint
				List<EcoreConstraint> ecs = constraintMapByObjId.get(ecoreExternalSchema.getId());
				addConstraintsNode(treeItem, ecs, ecoreExternalSchema.getName(), null);
				treeItem.setData("typeName", ecoreExternalSchema.getName());
				treeItem.setData("dataType", ObjTypeEnum.ExternalSchema.getType());
				treeItem.setData("externalSchemaId", ecoreExternalSchema.getId());
			}
		}
	}

	public void generateContentTreeForMessageDefinition(TreeItem treeRoot,
			List<EcoreMessageBuildingBlock> msgBldBlockList, Tree tree, String registrationStatus) {
		TreeItem treeItem = null;
		for (EcoreMessageBuildingBlock msgBldBlock : msgBldBlockList) {
			if (tree == null) {
				treeItem = new TreeItem(treeRoot, SWT.NONE);
			} else {
				treeItem = new TreeItem(tree, SWT.NONE);
			}
			String min = msgBldBlock.getMinOccurs() == null ? "0" : msgBldBlock.getMinOccurs() + "";
			String max = msgBldBlock.getMaxOccurs() == null ? "*" : msgBldBlock.getMaxOccurs() + "";
			String elementDisplayName = msgBldBlock.getName() + " [" + min + ", " + max + "]";
			treeItem.setText(elementDisplayName);
			treeItem.setData("bbId", msgBldBlock.getId());
			treeItem.setData("msgBldBlock", msgBldBlock);
			treeItem.setImage(ImgUtil.createImg(ImgUtil.ME));
			if ("2".equals(msgBldBlock.getDataType())) {
				//消息组件
				EcoreMessageComponent mc = ecoreMessageComponents.get(msgBldBlock.getDataTypeId());
				if(mc == null) {
					continue;
				}
				treeItem.setData("msgComponentId", mc.getId());
				treeItem.setText(elementDisplayName + " : " + mc.getName());
				// 1：MessageComponent，2：ChoiceComponent
				if ("1".equals(mc.getComponentType())) {
					boolean is = RegistrationStatusEnum.Registered.getStatus().equals(registrationStatus);
					setTreeItemImage(treeItem, is, ImgUtil.MC_SUB2_COMPONENT, ImgUtil.ME);
				} else if ("2".equals(mc.getComponentType())) {
					boolean is = RegistrationStatusEnum.Registered.getStatus().equals(registrationStatus);
					setTreeItemImage(treeItem, is, ImgUtil.MC_SUB2_DATA_TYPE, ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK);
				}
				List<EcoreMessageElement> mesf = mesByMsgComponentId.get(mc.getId());
				// Recursive invoke 消息组件
				if(mesf != null) {
					generateContentTreeForMessageComponent(treeItem, mesf, mc.getRegistrationStatus());
				}
				// Load Message Component Constraint
				List<EcoreConstraint> mcConstraintList = constraintMapByObjId.get(mc.getId());
				treeItem.setData("mbbConstraintList", constraintMapByObjId.get(msgBldBlock.getId()));
				treeItem.setData("mbbSynonymsTableItems", ConvertToKeyValueString(
						MrHelper.findSemanticMarkupElementBySemanticMarkupObjId(sms, smes, msgBldBlock.getId())));
				addConstraintsNode(treeItem, mcConstraintList, mc.getName(), null);
				treeItem.setData("typeName", mc.getName());
				treeItem.setData("dataType", ObjTypeEnum.MessageComponent.getType());
			} else if ("1".equals(msgBldBlock.getDataType())) {
				// 数据类型
				EcoreDataType dataType = dataTypeMapById.get(msgBldBlock.getDataTypeId());
				if(dataType == null) {
					continue;
				}
				treeItem.setData("dataTypeId", dataType.getId());
				treeItem.setText(elementDisplayName + " : " + dataType.getName());
				// 1:CodeSet;2:Text;3:Boolean,4:Indicator,5:Decimal,6:Rate,7:Amount,8:Quantity,9:Time,10:Binary,11:SchemaType,12:UserDefined
				if ("1".equals(dataType.getType())) {
					boolean is = RegistrationStatusEnum.Registered.getStatus().equals(registrationStatus);
					setTreeItemImage(treeItem, is, ImgUtil.MC_SUB2_DATA_TYPE, ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK);
					List<EcoreCode> ecs = codeMapByCodeSetId.get(dataType.getId());
					if(ecs != null) {
						for (EcoreCode ecoreCode : ecs) {
							TreeItem codeTreeItem = new TreeItem(treeItem, SWT.NONE);
							codeTreeItem.setText(ecoreCode.getName() + " [" + ecoreCode.getCodeName() + "]");
							codeTreeItem.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
							codeTreeItem.setData("codeId", ecoreCode.getId());
							codeTreeItem.setData("ecoreCode", ecoreCode);
						}
					}
				} else {
					boolean is = RegistrationStatusEnum.Registered.getStatus().equals(registrationStatus);
					setTreeItemImage(treeItem, is, ImgUtil.MD_SUB2_DATA_TYPE, ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK);
					// Load data type constraint
					List<EcoreConstraint> dataTypeConstraintList = constraintMapByObjId.get(dataType.getId());
					treeItem.setData("mbbConstraintList", constraintMapByObjId.get(msgBldBlock.getId()));
					treeItem.setData("mbbSynonymsTableItems", ConvertToKeyValueString(
							MrHelper.findSemanticMarkupElementBySemanticMarkupObjId(sms, smes, msgBldBlock.getId())));
					addConstraintsNode(treeItem, dataTypeConstraintList, dataType.getName(), null);
				}
				treeItem.setData("typeName", dataType.getName());
				treeItem.setData("dataType", ObjTypeEnum.DataType.getType());
			}
		}
	}

	public void generateContainmentTreeForBComponentDataType(TreeItem treeRoot) {
		List<EcoreConstraint> css = new ArrayList<EcoreConstraint>();
		EcoreDataType dataType = dataTypeMapById.get(treeRoot.getData("beTypeTextId") + "");
		if(dataType == null) {
			return;
		}
		if ("1".equals(dataType.getType())) { // codeSet 已到最底层了
			for (EcoreCode ecoreCode : codeMapByCodeSetId.get(dataType.getId())) {
				TreeItem codeTreeItem = new TreeItem(treeRoot, SWT.NONE);
				codeTreeItem.setText(ecoreCode.getName() + " [" + ecoreCode.getCodeName() + "]");
				codeTreeItem.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
				codeTreeItem.setData("id", ecoreCode.getId());
				codeTreeItem.setData("codeType", dataType.getType());
			}
		} else {
			treeRoot.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
			css = MrImplManager.get().getEcoreConstraintImpl().findByObjId(dataType.getId());
			addConstraintsNode(treeRoot, css, dataType.getName(), null);
		}
	}

//MC模块BusniessTrace 表格数据生成
	public void generateMCDefineTrace(Tree treeRoot, ArrayList<EcoreBusinessElement> beList,
			Map<String, ArrayList<EcoreBusinessElement>> allBeByComponentId,
			Map<String, ArrayList<EcoreTreeNode>> allSubBusiness, Map<String, String> allBusinessComponent) {
		for (EcoreBusinessElement be : beList) {
			TreeItem treeItem = new TreeItem(treeRoot, SWT.NONE);
			String min = be.getMinOccurs() == null ? "0" : be.getMinOccurs() + "";
			String max = be.getMaxOccurs() == null ? "*" : be.getMaxOccurs() + "";
			String elementDisplayName = be.getName() + " [" + min + ", " + max + "]";
			String string4 = allBusinessComponent.get(be.getTypeId());
			treeItem.setText(elementDisplayName + ":" + string4);
			treeItem.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
			treeItem.setData("FirstTreeName", be.getName());
			// 数据类型 1：datatype，2：消息组件
			if ("2".equals(be.getType())) {
				treeItem.setData("beTypeText", be.getName());
				treeItem.setData("beType_id", be.getTypeId());
				if (string4 != null || allBeByComponentId.get(treeItem.getData("beType_id")) != null) {
					TreeItem fistTree = new TreeItem(treeItem, SWT.NONE);
					fistTree.setText(string4);
					fistTree.setImage(ImgUtil.createImg(ImgUtil.BC));
					fistTree.setData("FirstTreeName", string4 + "/.");
				}
				// AS
				List<EcoreTreeNode> arrayList = allSubBusiness.get(be.getTypeId());
				if (arrayList != null) {
					arrayList.forEach(t -> {
						String string7 = allBusinessComponent.get(t.getId());
						TreeItem subItem = new TreeItem(treeItem, SWT.NONE);
						subItem.setImage(ImgUtil.createImg(ImgUtil.BC));
						subItem.setText("as " + string7);
						subItem.setData("FirstTreeName", treeItem.getData("FirstTreeName") + "/(as" + string7 + ")");
						if (allBeByComponentId.get(t.getId()) != null) {
							TreeItem fistTree = new TreeItem(subItem, SWT.NONE);
							fistTree.setText(string7);
							fistTree.setImage(ImgUtil.createImg(ImgUtil.BC));
							fistTree.setData("FirstTreeName", subItem.getData("FirstTreeName") + "/.");
							// AS
							List<EcoreTreeNode> arrayList2 = allSubBusiness.get(t.getId());
							if (arrayList2 != null) {
								arrayList2.forEach(j -> {
									String string6 = allBusinessComponent.get(j.getId());
									TreeItem subItem_1 = new TreeItem(subItem, SWT.NONE);
									subItem_1.setImage(ImgUtil.createImg(ImgUtil.BC));
									subItem_1.setText("as " + string6);
									subItem_1.setData("FirstTreeName",
											subItem.getData("FirstTreeName") + "/(as" + string6 + ")");
								});
							}
							// BE
							generateMCOfBcSubTypeforDefineTrace(subItem,
									allBeByComponentId.get(t.getId()), allBeByComponentId, allSubBusiness,
									allBusinessComponent);
						}
					});
				}

				List<EcoreBusinessElement> besByBizComponentId = allBeByComponentId.get(treeItem.getData("beType_id").toString());
				if (besByBizComponentId != null) {
					besByBizComponentId.forEach(ebe -> {
						TreeItem beItem = new TreeItem(treeItem, SWT.NONE);
						beItem.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
						String ebeMin = ebe.getMinOccurs() == null ? "0" : ebe.getMinOccurs() + "";
						String ebeMax = ebe.getMaxOccurs() == null ? "*" : ebe.getMaxOccurs() + "";
						String beItemName = ebe.getName() + " [" + ebeMin + ", " + ebeMax + "]";
						if ("2".equals(ebe.getType())) {
							String name = allBusinessComponent.get(ebe.getTypeId());
							beItem.setText(beItemName + " : " + name);
							beItem.setData("FirstTreeName", treeItem.getData("FirstTreeName") + "/" + ebe.getName());
							if (allBeByComponentId.get(ebe.getTypeId()) != null) {
								// bc树头
								String string8 = allBusinessComponent.get(ebe.getTypeId());
								TreeItem fistTree = new TreeItem(beItem, SWT.NONE);
								fistTree.setImage(ImgUtil.createImg(ImgUtil.BC));
								fistTree.setText(string8);
								fistTree.setData("FirstTreeName", string8 + "/.");
								// AS
								List<EcoreTreeNode> arrayList2 = allSubBusiness.get(ebe.getTypeId());
								if (arrayList2 != null) {
									arrayList2.forEach(j -> {
										String string9 = allBusinessComponent.get(j.getId());
										TreeItem subItem_1 = new TreeItem(beItem, SWT.NONE);
										subItem_1.setImage(ImgUtil.createImg(ImgUtil.BC));
										subItem_1.setText("as " + string9);
										subItem_1.setData("FirstTreeName",
												beItem.getData("FirstTreeName") + "/(as" + string9 + ")");
										// AS-BE
										if (allBeByComponentId.get(j.getId()) != null) {
											// 树头
											TreeItem fistTree_1 = new TreeItem(subItem_1, SWT.NONE);
											fistTree_1.setImage(ImgUtil.createImg(ImgUtil.BC));
											fistTree_1.setText(string9);
											fistTree_1.setData("FirstTreeName",
													subItem_1.getData("FirstTreeName") + string9 + "/.");

											generateMCOfBcSubTypeforDefineTrace(subItem_1,
													allBeByComponentId.get(j.getId()), allBeByComponentId,
													allSubBusiness, allBusinessComponent);
										}
									});
								}
								// BE
								generateMCOfBcSubTypeforDefineTrace(beItem,
										allBeByComponentId.get(ebe.getTypeId()), allBeByComponentId, allSubBusiness,
										allBusinessComponent);
							}
						} else {
							itemDataType(beItem, beItemName, ebe.getTypeId());
						}
					});
				}
			} else {
				itemDataType(treeItem, elementDisplayName, be.getTypeId());
			}
		}
	}

	private void itemDataType(TreeItem treeItem, String name, String typeId) {
		EcoreDataType dataType =dataTypeMapById.get(typeId);
		treeItem.setData("beTypeText", dataType.getName());
		treeItem.setData("beType_id", typeId);
		treeItem.setText(name + " : " + dataType.getName());
		treeItem.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
	}

	//mc中DefineTrace功能中新增加载bc的subtype
	private String string;
	private ArrayList<EcoreBusinessElement> arrayList5;
	private String string5;
	private ArrayList<EcoreTreeNode> arrayList4;
	private ArrayList<EcoreTreeNode> arrayList6;
	private String string3;
	private String string2;

	public void generateMCOfBcSubTypeforDefineTrace(TreeItem treeRoot, ArrayList<EcoreBusinessElement> beList,
			Map<String, ArrayList<EcoreBusinessElement>> allBeByComponentId,
			Map<String, ArrayList<EcoreTreeNode>> allSubBusiness, Map<String, String> allBusinessComponent) {

		for (EcoreBusinessElement be : beList) {
			TreeItem treeItem = new TreeItem(treeRoot, SWT.NONE);
			String min = be.getMinOccurs() == null ? "0" : be.getMinOccurs() + "";
			String max = be.getMaxOccurs() == null ? "*" : be.getMaxOccurs() + "";
			String elementDisplayName = be.getName() + " [" + min + ", " + max + "]";
			string = allBusinessComponent.get(be.getTypeId());
			treeItem.setText(elementDisplayName + ":" + string);
			treeItem.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
			treeItem.setData("FirstTreeName", treeRoot.getData("FirstTreeName") + "/" + be.getName());
			// 数据类型 1：datatype，2：消息组件
			if ("2".equals(be.getType())) {
				treeItem.setData("beTypeText", be.getName());
				treeItem.setData("beType_id", be.getTypeId());
				// bc树头
				TreeItem fistTree = new TreeItem(treeItem, SWT.NONE);
				fistTree.setText(string);
				fistTree.setImage(ImgUtil.createImg(ImgUtil.BC));
				fistTree.setData("FirstTreeName", treeItem.getData("FirstTreeName") + "/.");
				// AS
				arrayList4 = allSubBusiness.get(be.getTypeId());
				if (arrayList4 != null) {
					arrayList4.forEach(t -> {
						string3 = allBusinessComponent.get(t.getId());
						TreeItem subItem = new TreeItem(treeItem, SWT.NONE);
						if (string3 != null) {
							subItem.setImage(ImgUtil.createImg(ImgUtil.BC));
							subItem.setText("as " + string3);
							subItem.setData("FirstTreeName",
									treeItem.getData("FirstTreeName") + "/(as" + string3 + ")");
						}
					});
				}
				// BE
				arrayList5 = allBeByComponentId.get(be.getTypeId());
				if (arrayList5 != null) {
					arrayList5.forEach(ebe -> {
						arrayList6 = allSubBusiness.get(ebe.getId());
						if (arrayList6 != null) {
							arrayList6.forEach(k -> {
								string2 = allBusinessComponent.get(k.getId());
								TreeItem subItem = new TreeItem(treeItem, SWT.NONE);
								subItem.setImage(ImgUtil.createImg(ImgUtil.BC));
								subItem.setText("as " + string2);
								subItem.setData("FirstTreeName", treeItem.getData("FirstTreeName") + "/" + ebe.getName());
							});
						}
						String ebeMin = be.getMinOccurs() == null ? "0" : be.getMinOccurs() + "";
						String ebeMax = be.getMaxOccurs() == null ? "*" : be.getMaxOccurs() + "";
						String name = ebe.getName() + "[" + ebeMin + ", " + ebeMax + "]";
						string5 = allBusinessComponent.get(ebe.getTypeId());
						TreeItem subItem_5 = new TreeItem(treeItem, SWT.NONE);
						subItem_5.setData("FirstTreeName", treeItem.getData("FirstTreeName") + "/" + ebe.getName());
						if ("2".equals(ebe.getType())) {
							subItem_5.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
							subItem_5.setText(name + " : " + string5);
						} else {
							EcoreDataType dataType = dataTypeMapById.get(ebe.getTypeId());
							subItem_5.setText(name + " : " + dataType.getName());
							subItem_5.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
						}
					});
				}
			} else {
				// datatype
				EcoreDataType dataType = dataTypeMapById.get(be.getTypeId());
				treeItem.setData("beTypeText", dataType.getName());
				treeItem.setData("beType_id", be.getTypeId());
				treeItem.setText(elementDisplayName + " : " + dataType.getName());
				treeItem.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
			}
		}
	}

	public void generateContainmentTreeForBComponent(TreeItem treeRoot, ArrayList<EcoreBusinessElement> beList,
			boolean... isMe) {
		for (EcoreBusinessElement be : beList) {
			List<EcoreConstraint> constraintList = new ArrayList<EcoreConstraint>();
			TreeItem treeItem = new TreeItem(treeRoot, SWT.NONE);
			
			treeItem.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
			String min = be.getMinOccurs() == null ? "0" : be.getMinOccurs() + "";
			String max = be.getMaxOccurs() == null ? "*" : be.getMaxOccurs() + "";
			// 数据类型 1：datatype，2：消息组件
			if ("2".equals(be.getType())) {
				String elementDisplayName = be.getName() + " [" + min + ", " + max + "] : " + be.getName();
				treeItem.setText(elementDisplayName);
				treeItem.setData("beTypeText", be.getName());
				treeItem.setData("beType_id", be.getTypeId());
				// Load Message Component Constraint
				constraintList = constraintMapByObjId.get(be.getBusinessComponentId());
				addConstraintsNode(treeItem, constraintList, be.getName(), null);
			} else {
				EcoreDataType dataType = dataTypeMapById.get(be.getTypeId());
				String dataTypeName = "";
				if(dataType != null) {
					dataTypeName = dataType.getName();
				}
				String elementDisplayName = be.getName() + " [" + min + ", " + max + "] : " + dataTypeName;
				treeItem.setText(elementDisplayName);
				// datatype
				if("".equals(dataTypeName)) {
					continue;
				}
				treeItem.setData("beTypeText", dataTypeName);
				treeItem.setData("beType_id", be.getTypeId());
				// 1:CodeSet;2:Text;3:Boolean,4:Indicator,5:Decimal,6:Rate,7:Amount,8:Quantity,9:Time,10:Binary,11:SchemaType,12:UserDefined
				if ("1".equals(dataType.getType())) { // codeSet 已到最底层了
					List<EcoreCode> list = codeMapByCodeSetId.get(dataType.getId());
					if(list != null) {
						for (EcoreCode ecoreCode : list) {
							TreeItem codeTreeItem = new TreeItem(treeItem, SWT.NONE);
							codeTreeItem.setText(ecoreCode.getName() + " [" + ecoreCode.getCodeName() + "]");
							codeTreeItem.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
							codeTreeItem.setData("id", ecoreCode.getId());
							codeTreeItem.setData("codeType", be.getType());
						}
					}
				} else {
					// Load data type constraint
					treeItem.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
					constraintList = constraintMapByObjId.get(dataType.getId());
					addConstraintsNode(treeItem, constraintList, dataTypeName, null);
				}
			}
			
			if (isMe.length > 0 && isMe[0]) {
				treeItem.setData("id", be.getId());
				treeItem.setData("beNameText", be.getName());
				treeItem.setData("beDocText", be.getDefinition());
				treeItem.setData("beMinOccursText", be.getMinOccurs());
				treeItem.setData("beMaxOccursText", be.getMaxOccurs());
				treeItem.setData("beType_id", be.getTypeId());
				treeItem.setData("beType", be.getType());
				treeItem.setData("isDerivedCheckButton", Boolean.getBoolean(be.getIsDerived()));
				treeItem.setData("beObjectIdentifierText", be.getObjectIdentifier());
				treeItem.setData("beStatusCombo", be.getRegistrationStatus());
				treeItem.setData("beRemovalDate", be.getRemovalDate());
				treeItem.setData("beConstraintsTableItems", constraintsTableItems(constraintList));
			}
		}
	}

	public void generateContentTreeForDataType(TreeItem treeNode, List<EcoreCode> ecoreCodeList) {
		for (EcoreCode ecoreCode : ecoreCodeList) {
			TreeItem treeItem = new TreeItem(treeNode, SWT.NONE);
			treeItem.setText(ecoreCode.getName() + " [" + ecoreCode.getCodeName() + "]");
			treeItem.setData("codeId", ecoreCode.getId());
			treeItem.setData("ecoreCode", ecoreCode);
			treeItem.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
		}
	}

	private ArrayList<String> ConvertToKeyValueString(List<EcoreSemanticMarkupElement> synonymsList) {
		ArrayList<String> keyValuePairList = new ArrayList<>();
		for (EcoreSemanticMarkupElement synonym : synonymsList) {
			String keyValuePair = synonym.getName() + "," + synonym.getValue();
			keyValuePairList.add(keyValuePair);
		}
		return keyValuePairList;
	}

	/**
	 * 组件树图片
	 * @param treeRoot
	 * @param treeItem
	 * @param mc
	 */
	private void setMessageComponentTreeItemImage(TreeItem treeRoot, TreeItem treeItem,
			EcoreMessageComponent mc) {
		if (treeItem.getData("meTree") != null) {
			boolean is = mc.getRegistrationStatus() == null
					|| mc.getRegistrationStatus().equals("Provisionally Registered");
			if ("1".equals(mc.getComponentType())) {
				setTreeItemImage(treeItem, is, ImgUtil.MC_SUB2_COMPONENT1, ImgUtil.MC_SUB2_COMPONENT);
			} else if ("2".equals(mc.getComponentType())) {
				setTreeItemImage(treeItem, is, ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK, ImgUtil.MC_SUB2_DATA_TYPE);
			}
		} else {
			boolean is = treeRoot.getData("mcStatusCombo") == null
					|| treeRoot.getData("mcStatusCombo").equals("Provisionally Registered");
			if ("1".equals(mc.getComponentType())) {
				setTreeItemImage(treeItem, is, ImgUtil.MC_SUB2_COMPONENT1, ImgUtil.MC_SUB2_COMPONENT);
			} else if ("2".equals(mc.getComponentType())) {
				setTreeItemImage(treeItem, is, ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK, ImgUtil.MC_SUB2_DATA_TYPE);
			}
		}
		treeItem.setData("meTree", 1);
		treeItem.setData("mcStatusCombo", mc.getRegistrationStatus());
	}
	
	/**
	 * 约束表单条目数据
	 * 
	 * @param constraintList
	 * @return
	 */
	private List<String[]> constraintsTableItems(List<EcoreConstraint> constraintList) {
		List<String[]> constraintsTableItems = new ArrayList<String[]>();
		if (constraintList != null && constraintList.size() > 0) {
			for (EcoreConstraint constraint : constraintList) {
				String[] str = new String[4];
				str[0] = constraint.getName();
				str[1] = constraint.getDefinition();
				str[2] = constraint.getExpression();
				str[3] = constraint.getExpressionlanguage();
				constraintsTableItems.add(str);
			}
		}
		return constraintsTableItems;
	}

	/**
	 * code节点设置封装
	 * 
	 * @param ecoreCodes
	 * @param treeItem
	 */
	private void setCodeTreeItem(List<EcoreCode> ecoreCodes, TreeItem treeItem) {
		if(ecoreCodes != null && ecoreCodes.size() > 0) {
			for (EcoreCode ecoreCode : ecoreCodes) {
				TreeItem codeTreeItem = new TreeItem(treeItem, SWT.NONE);
				codeTreeItem.setText(ecoreCode.getName() + " [" + ecoreCode.getCodeName() + "]");
				codeTreeItem.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
				codeTreeItem.setData("id", ecoreCode.getId());
				List<EcoreConstraint> constraintList = constraintMapByObjId.get(ecoreCode.getId());
				addConstraintsNode(codeTreeItem, constraintList, ecoreCode.getName(), null);
				codeTreeItem.setData("codeType", "1");
				codeTreeItem.setData("CodesName", ecoreCode.getName());
				codeTreeItem.setData("Document", ecoreCode.getDefinition());
				codeTreeItem.setData("CodeName", ecoreCode.getCodeName());
			}
		}
	}

	/**
	 * 设置树节点图片
	 * 
	 * @param treeItem
	 * @param is
	 * @param imgUrl1
	 * @param imgUrl2
	 */
	private void setTreeItemImage(TreeItem treeItem, boolean is, String imgUrl1, String imgUrl2) {
		if (is) {
			treeItem.setImage(ImgUtil.createImg(imgUrl1));
		} else {
			treeItem.setImage(ImgUtil.createImg(imgUrl2));
		}
	}

	private void setMessageElementItem(TreeItem treeItem, EcoreMessageElement me,
			List<EcoreConstraint> constraintList, boolean... isMe) {
		if (isMe.length > 0 && isMe[0]) {
			treeItem.setData("id", me.getId());
			treeItem.setData("meNameText", me.getName());
			treeItem.setData("meDocText", me.getDefinition());
			treeItem.setData("meMinOccursText", me.getMinOccurs());
			treeItem.setData("meMaxOccursText", me.getMaxOccurs());
			treeItem.setData("meType_id", me.getTypeId());
			treeItem.setData("meType", me.getType());
			treeItem.setData("meTypeName", me.getTypeName());
			treeItem.setData("isDerivedCheckButton", Boolean.getBoolean(me.getIsDerived()));
			treeItem.setData("meXmlTagText", me.getXmlTag());
			treeItem.setData("isTechnical", "");
			treeItem.setData("meObjectIdentifierText", me.getObjectIdentifier());
			treeItem.setData("meStatusCombo", me.getRegistrationStatus());
			treeItem.setData("meRemovalDate", me.getRemovalDate());
			treeItem.setData("beConstraintsTableItems", constraintsTableItems(constraintList));
		}
	}
}
