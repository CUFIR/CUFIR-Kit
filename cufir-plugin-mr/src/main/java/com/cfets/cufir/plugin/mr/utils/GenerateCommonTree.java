package com.cfets.cufir.plugin.mr.utils;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.cfets.cufir.plugin.mr.enums.ObjTypeEnum;
import com.cfets.cufir.plugin.mr.enums.RegistrationStatus;
import com.cfets.cufir.s.data.bean.EcoreBusinessElement;
import com.cfets.cufir.s.data.bean.EcoreCode;
import com.cfets.cufir.s.data.bean.EcoreConstraint;
import com.cfets.cufir.s.data.bean.EcoreDataType;
import com.cfets.cufir.s.data.bean.EcoreExternalSchema;
import com.cfets.cufir.s.data.bean.EcoreMessageBuildingBlock;
import com.cfets.cufir.s.data.bean.EcoreMessageComponent;
import com.cfets.cufir.s.data.bean.EcoreMessageDefinition;
import com.cfets.cufir.s.data.bean.EcoreMessageElement;
import com.cfets.cufir.s.data.bean.EcoreSemanticMarkupElement;
import com.cfets.cufir.s.data.vo.EcoreTreeNode;

/**
 * 生成树
 * @author gongyi_tt
 *
 */
public class GenerateCommonTree {

	public static void generateContainmentTreeForMessageSetofImpactPage(TreeItem treeRoot,
			ArrayList<EcoreMessageDefinition> msgDefinitionList) {
		for (EcoreMessageDefinition msgDefinition : msgDefinitionList) {
			TreeItem treeItem = new TreeItem(treeRoot, SWT.NONE);
			treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB1));
			treeItem.setText(msgDefinition.getName());
			treeItem.setData("msgDefinitionId", msgDefinition.getId());
			GenerateCommonTree.generateContainmentTreeForMessageDefinition(treeItem,
					DerbyDaoUtil.getMsgBuildingBlock(msgDefinition.getId()));
		}
	}

	public static void generateContainmentTreeForMessageDefinition(TreeItem treeRoot,
			ArrayList<EcoreMessageBuildingBlock> msgBldBlockList) {

		for (EcoreMessageBuildingBlock msgBldBlock : msgBldBlockList) {
			TreeItem treeItem = new TreeItem(treeRoot, SWT.NONE);
			String elementDisplayName = msgBldBlock.getName() + " [" + msgBldBlock.getMinOccurs() + ","
					+ msgBldBlock.getMaxOccurs() + "]";
			treeItem.setText(elementDisplayName);
			treeItem.setData("bbId", msgBldBlock.getId());
			// 数据类型 1：datatype，2：消息组件
			if ("2".equals(msgBldBlock.getDataType())) {
				EcoreMessageComponent mc = DerbyDaoUtil.getMessageComponentById(msgBldBlock.getDataTypeId());
				treeItem.setText(elementDisplayName + " : " + mc.getName());
				// 1：MessageComponent，2：ChoiceComponent
				if ("1".equals(mc.getComponentType())) {
					treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
				} else if ("2".equals(mc.getComponentType())) {
					treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
				}
				ArrayList<EcoreMessageElement> subMeList = DerbyDaoUtil.getMessageElementList(mc.getId());
				// Recursive invoke 消息组件
				generateContainmentTreeForMessageComponent(treeItem, subMeList);

				// Load Message Component Constraint
				ArrayList<EcoreConstraint> mcConstraintList = DerbyDaoUtil.getContraints(mc.getId());
				addContraintsNode(treeItem, mcConstraintList, mc.getName(), null);
			} else {
				// 数据类型 1：datatype
				EcoreDataType dataType = DerbyDaoUtil.getDataTypeById(msgBldBlock.getDataTypeId());
				treeItem.setText(elementDisplayName + " : " + dataType.getName());
				// 1:CodeSet;2:Text;3:Boolean,4:Indicator,5:Decimal,6:Rate,7:Amount,8:Quantity,9:Time,10:Binary,11:SchemaType,12:UserDefined
				if ("1".equals(dataType.getType())) {
					treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
					for (EcoreCode ecoreCode : DerbyDaoUtil.getCodeByCodeSetId(dataType.getId())) {
						TreeItem codeTreeItem = new TreeItem(treeItem, SWT.NONE);
						codeTreeItem.setText(ecoreCode.getName() + " [" + ecoreCode.getCodeName() + "]");
						codeTreeItem.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
						codeTreeItem.setData(ecoreCode.getId());
					}
				} else {
					treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
					// Load data type constraint
					ArrayList<EcoreConstraint> dataTypeConstraintList = DerbyDaoUtil.getContraints(dataType.getId());
					addContraintsNode(treeItem, dataTypeConstraintList, dataType.getName(), null);
				}

			}
		}
	}

	public static void generateContainmentTreeForBusniessTrace(TreeItem treeRoot,
			ArrayList<EcoreMessageElement> meList) {

		for (EcoreMessageElement me : meList) {

			TreeItem treeItem = new TreeItem(treeRoot, SWT.NONE);
//		treeItem.setText(1, "false");
			String elementDisplayName = me.getName() + " [" + me.getMinOccurs() + "," + me.getMaxOccurs() + "]";
			ArrayList<EcoreConstraint> constraintList = new ArrayList<EcoreConstraint>();
//		String status=null;
//		if (MessageComponentCreateEditor.summaryStatusCombo.getText()!=null) {
//			status=MessageComponentCreateEditor.summaryStatusCombo.getText();
//		}
			if ("2".equals(me.getTraceType())) {
				treeItem.setImage(2, ImgUtil.createImg(ImgUtil.BC));
			} else if ("1".equals(me.getTraceType())) {
				treeItem.setImage(2, ImgUtil.createImg(ImgUtil.BC_BE));
			} else {

			}
			// 数据类型 1：datatype，2：消息组件
			if ("2".equals(me.getType())) {
				EcoreMessageComponent mc = DerbyDaoUtil.getMessageComponentById(me.getTypeId());
				treeItem.setText(elementDisplayName + " : " + mc.getName());
				// 1：MessageComponent，2：ChoiceComponent
				if ("1".equals(mc.getComponentType())) {
					if (treeRoot.getData("mcStatusCombo") == null
							|| treeRoot.getData("mcStatusCombo").equals("Provisionally Registered")) {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT1));
					} else {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
					}
				} else if ("2".equals(mc.getComponentType())) {
					if (treeRoot.getData("mcStatusCombo") == null
							|| treeRoot.getData("mcStatusCombo").equals("Provisionally Registered")) {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
					} else {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
					}
				}
				if (treeItem.getData("meTree") != null) {
					if ("1".equals(mc.getComponentType())) {
						if (mc.getRegistrationStatus() == null
								|| mc.getRegistrationStatus().equals("Provisionally Registered")) {
							treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT1));
						} else {
							treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
						}
					} else if ("2".equals(mc.getComponentType())) {
						if (mc.getRegistrationStatus() == null
								|| mc.getRegistrationStatus().equals("Provisionally Registered")) {
							treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
						} else {
							treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
						}
					}

				}
//			if (mc.getTraceName()!=null) {
//				treeItem.setText(4, mc.getTraceName());
//				treeItem.setData("traceof",mc.getTraceName());
//			}else {
//				treeItem.setText(4, "");
//			}
				// 定位
				treeItem.setData("meTree", 1);
				treeItem.setData("mcStatusCombo", mc.getRegistrationStatus());
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
				// typeOfTracesTo取当前me的mc中traceName
//			treeItem.setData("typeOfTracesTo",mc.getTraceName());

				ArrayList<EcoreMessageElement> subMeList = DerbyDaoUtil.getMessageElementList(mc.getId());
				generateContainmentTreeForBusniessTrace(treeItem, subMeList);

				// Load Message Component Constraint
//			constraintList = DerbyDaoUtil.getContraints(mc.getId());
//			addContraintsNode(treeItem, constraintList, mc.getName(), null);
			} else {
				// datatype
//			treeItem.setText(1, "false");
				EcoreDataType dataType = DerbyDaoUtil.getDataTypeById(me.getTypeId());
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
				if ("1".equals(dataType.getType())) { // codeSet 已到最底层了
					if (treeRoot.getData("mcStatusCombo") == null
							|| treeRoot.getData("mcStatusCombo").equals("Provisionally Registered")) {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
					} else {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
					}
					for (EcoreCode ecoreCode : DerbyDaoUtil.getCodeByCodeSetId(dataType.getId())) {
						TreeItem codeTreeItem = new TreeItem(treeItem, SWT.NONE);
						codeTreeItem.setText(ecoreCode.getName() + " [" + ecoreCode.getCodeName() + "]");
						codeTreeItem.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
						codeTreeItem.setData("id", ecoreCode.getId());
						constraintList = DerbyDaoUtil.getContraints(ecoreCode.getId());
						addContraintsNode(codeTreeItem, constraintList, ecoreCode.getName(), null);
						codeTreeItem.setData("codeType", "1");
						codeTreeItem.setData("CodesName", ecoreCode.getName());
						codeTreeItem.setData("Document", ecoreCode.getDefinition());
						codeTreeItem.setData("CodeName", ecoreCode.getCodeName());
//					ArrayList<EcoreCode> codeByCodeSetId = DerbyDaoUtil.getCodeByCodeSetId(dataType.getId());
//					generateContainmentTreeForMessageComponent(treeItem, codeByCodeSetId, false);
					}
				} else {
					if (treeRoot.getData("mcStatusCombo") == null
							|| treeRoot.getData("mcStatusCombo").equals("Provisionally Registered")) {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT1));
					} else {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
					}
					treeItem.setText(1, me.getTechnical() == null ? "false" : me.getTechnical());
					treeItem.setText(2, me.getTracesTo() == null ? "" : me.getTracesTo());
					treeItem.setText(3, me.getTracePath() == null ? "" : me.getTracePath());
					treeItem.setText(4, "");
					if (treeItem.getData("bcType_id") != null) {
						treeItem.setData("trace", treeItem.getData("bcType_id"));
					} else if (treeItem.getData("beType_id") != null) {
						treeItem.setData("trace", treeItem.getData("beType_id"));
					}
					// Load data type constraint
//				constraintList = DerbyDaoUtil.getContraints(dataType.getId());
//				addContraintsNode(treeItem, constraintList, dataType.getName(), null);
				}

			}
		}

	}

	public static void generateContainmentTreeForMessageComponents(TreeItem treeRoot,
			ArrayList<EcoreMessageElement> meList, boolean... isMe) {

		for (EcoreMessageElement me : meList) {

			TreeItem treeItem = new TreeItem(treeRoot, SWT.NONE);
			String elementDisplayName = me.getName() + " [" + me.getMinOccurs() + "," + me.getMaxOccurs() + "]";
			ArrayList<EcoreConstraint> constraintList = new ArrayList<EcoreConstraint>();
//			String status=null;
//			if (MessageComponentCreateEditor.summaryStatusCombo.getText()!=null) {
//				status=MessageComponentCreateEditor.summaryStatusCombo.getText();
//			}
			treeItem.setText(1, me.getTechnical() == null ? "false" : me.getTechnical());
			treeItem.setText(2, me.getTracesTo() == null ? "" : me.getTracesTo());
			treeItem.setImage(2, ImgUtil.createImg(ImgUtil.BC));
			treeItem.setText(3, me.getTracePath() == null ? "" : me.getTracePath());
			treeItem.setText(4, me.getTypeOfTracesTo() == null ? "" : me.getTypeOfTracesTo());

			// 数据类型 1：datatype，2：消息组件
			if ("2".equals(me.getType())) {
				EcoreMessageComponent mc = DerbyDaoUtil.getMessageComponentById(me.getTypeId());
				treeItem.setText(elementDisplayName + " : " + mc.getName());
				// 1：MessageComponent，2：ChoiceComponent
				if ("1".equals(mc.getComponentType())) {
					if (treeRoot.getData("mcStatusCombo") == null
							|| treeRoot.getData("mcStatusCombo").equals("Provisionally Registered")) {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT1));
					} else {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
					}
				} else if ("2".equals(mc.getComponentType())) {
					if (treeRoot.getData("mcStatusCombo") == null
							|| treeRoot.getData("mcStatusCombo").equals("Provisionally Registered")) {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
					} else {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
					}
				}
				if (treeItem.getData("meTree") != null) {
					if ("1".equals(mc.getComponentType())) {
						if (mc.getRegistrationStatus() == null
								|| mc.getRegistrationStatus().equals("Provisionally Registered")) {
							treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT1));
						} else {
							treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
						}
					} else if ("2".equals(mc.getComponentType())) {
						if (mc.getRegistrationStatus() == null
								|| mc.getRegistrationStatus().equals("Provisionally Registered")) {
							treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
						} else {
							treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
						}
					}

				}
				treeItem.setData("meTree", 1);
				treeItem.setData("mcStatusCombo", mc.getRegistrationStatus());
				ArrayList<EcoreMessageElement> subMeList = DerbyDaoUtil.getMessageElementList(mc.getId());
				generateContainmentTreeForMessageComponents(treeItem, subMeList, isMe);

				// Load Message Component Constraint
				constraintList = DerbyDaoUtil.getContraints(mc.getId());
				addContraintsNode(treeItem, constraintList, mc.getName(), null);
			} else {
				// datatype
				EcoreDataType dataType = DerbyDaoUtil.getDataTypeById(me.getTypeId());
				treeItem.setText(elementDisplayName + " : " + dataType.getName());
				// 1:CodeSet;2:Text;3:Boolean,4:Indicator,5:Decimal,6:Rate,7:Amount,8:Quantity,9:Time,10:Binary,11:SchemaType,12:UserDefined
				if ("1".equals(dataType.getType())) { // codeSet 已到最底层了
					if (treeRoot.getData("mcStatusCombo") == null
							|| treeRoot.getData("mcStatusCombo").equals("Provisionally Registered")) {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
					} else {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
					}
					for (EcoreCode ecoreCode : DerbyDaoUtil.getCodeByCodeSetId(dataType.getId())) {
						TreeItem codeTreeItem = new TreeItem(treeItem, SWT.NONE);
						codeTreeItem.setText(ecoreCode.getName() + " [" + ecoreCode.getCodeName() + "]");
						codeTreeItem.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
						codeTreeItem.setData("id", ecoreCode.getId());
						constraintList = DerbyDaoUtil.getContraints(ecoreCode.getId());
						addContraintsNode(codeTreeItem, constraintList, ecoreCode.getName(), null);
						codeTreeItem.setData("codeType", "1");
						codeTreeItem.setData("CodesName", ecoreCode.getName());
						codeTreeItem.setData("Document", ecoreCode.getDefinition());
						codeTreeItem.setData("CodeName", ecoreCode.getCodeName());
//						ArrayList<EcoreCode> codeByCodeSetId = DerbyDaoUtil.getCodeByCodeSetId(dataType.getId());
//						generateContainmentTreeForMessageComponent(treeItem, codeByCodeSetId, false);
					}
				} else {
					if (treeRoot.getData("mcStatusCombo") == null
							|| treeRoot.getData("mcStatusCombo").equals("Provisionally Registered")) {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT1));
					} else {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
					}
					// Load data type constraint
					constraintList = DerbyDaoUtil.getContraints(dataType.getId());
					addContraintsNode(treeItem, constraintList, dataType.getName(), null);
				}
			}

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
//				treeItem.setData("mcStatus",treeRoot.getData("meStatusCombo"));
//				String mkesql="select * from ecore_semantic_markup_element where semantic_markup_id"
//						+ " in (select id from ecore_semantic_markup where obj_id=? and type='Synonym') ";
//				
//				Map<String,Map<String, String>> mkeMap=new HashMap<String,Map<String, String>>();
//				java.util.List<SynonymVO> synonymVOs = new ArrayList<SynonymVO>();
//				if(mkeMap!=null  && !mkeMap.isEmpty()) {
//					for(String eid:mkeMap.keySet()) {
//						Map<String, String> mt=mkeMap.get(eid);
//						SynonymVO v=new SynonymVO();
//						v.setObjId(me.getId());
//						v.setContext(mt.get("context"));
//						v.setSynonym(mt.get("value"));
//						synonymVOs.add(v);
//					}
//				}
//				
//				java.util.List<String[]> synonymsTableItems = new ArrayList<String[]>();
//				if (synonymVOs != null && synonymVOs.size() > 0) {
//					for (SynonymVO synonymVO : synonymVOs) {
//						String[] str = new String[2];
//						str[0] = synonymVO.getContext();
//						str[1] = synonymVO.getSynonym();
//						synonymsTableItems.add(str);
//					}
//				}
//				treeItem.setData("beSynonymsTableItems", synonymsTableItems);

				java.util.List<String[]> constraintsTableItems = new ArrayList<String[]>();
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
				treeItem.setData("beConstraintsTableItems", constraintsTableItems);
			}
		}
	}

	public static void generateContainmentTreeForMessageComponent(TreeItem treeRoot,
			ArrayList<EcoreMessageElement> meList, boolean... isMe) {

		for (EcoreMessageElement me : meList) {

			TreeItem treeItem = new TreeItem(treeRoot, SWT.NONE);
			String elementDisplayName = me.getName() + " [" + me.getMinOccurs() + "," + me.getMaxOccurs() + "]";
			ArrayList<EcoreConstraint> constraintList = new ArrayList<EcoreConstraint>();
//			String status=null;
//			if (MessageComponentCreateEditor.summaryStatusCombo.getText()!=null) {
//				status=MessageComponentCreateEditor.summaryStatusCombo.getText();
//			}

			// 数据类型 1：datatype，2：消息组件
			if ("2".equals(me.getType())) {
				EcoreMessageComponent mc = DerbyDaoUtil.getMessageComponentById(me.getTypeId());
				treeItem.setText(elementDisplayName + " : " + mc.getName());
				// 1：MessageComponent，2：ChoiceComponent
				if ("1".equals(mc.getComponentType())) {
					if (treeRoot.getData("meStatusCombo") == null
							|| treeRoot.getData("meStatusCombo").equals("Provisionally Registered")) {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT1));
					} else {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
					}
				} else if ("2".equals(mc.getComponentType())) {
					if (treeRoot.getData("meStatusCombo") == null
							|| treeRoot.getData("meStatusCombo").equals("Provisionally Registered")) {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
					} else {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
					}
				}
				if (treeItem.getData("meTree") != null) {
					if ("1".equals(mc.getComponentType())) {
						if (mc.getRegistrationStatus() == null
								|| mc.getRegistrationStatus().equals("Provisionally Registered")) {
							treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT1));
						} else {
							treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
						}
					} else if ("2".equals(mc.getComponentType())) {
						if (mc.getRegistrationStatus() == null
								|| mc.getRegistrationStatus().equals("Provisionally Registered")) {
							treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
						} else {
							treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
						}
					}

				}
				treeItem.setData("meTree", 1);
				treeItem.setData("meStatusCombo", mc.getRegistrationStatus());
				ArrayList<EcoreMessageElement> subMeList = DerbyDaoUtil.getMessageElementList(mc.getId());
				generateContainmentTreeForMessageComponent(treeItem, subMeList, isMe);

				// Load Message Component Constraint
				constraintList = DerbyDaoUtil.getContraints(mc.getId());
				addContraintsNode(treeItem, constraintList, mc.getName(), null);
			} else {
				// datatype
				EcoreDataType dataType = DerbyDaoUtil.getDataTypeById(me.getTypeId());
				treeItem.setText(elementDisplayName + " : " + dataType.getName());
				// 1:CodeSet;2:Text;3:Boolean,4:Indicator,5:Decimal,6:Rate,7:Amount,8:Quantity,9:Time,10:Binary,11:SchemaType,12:UserDefined
				if ("1".equals(dataType.getType())) { // codeSet 已到最底层了
					if (treeRoot.getData("meStatusCombo") == null
							|| treeRoot.getData("meStatusCombo").equals("Provisionally Registered")) {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
					} else {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
					}
					for (EcoreCode ecoreCode : DerbyDaoUtil.getCodeByCodeSetId(dataType.getId())) {
						TreeItem codeTreeItem = new TreeItem(treeItem, SWT.NONE);
						codeTreeItem.setText(ecoreCode.getName() + " [" + ecoreCode.getCodeName() + "]");
						codeTreeItem.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
						codeTreeItem.setData("id", ecoreCode.getId());
						constraintList = DerbyDaoUtil.getContraints(ecoreCode.getId());
						addContraintsNode(codeTreeItem, constraintList, ecoreCode.getName(), null);
						codeTreeItem.setData("codeType", "1");
						codeTreeItem.setData("CodesName", ecoreCode.getName());
						codeTreeItem.setData("Document", ecoreCode.getDefinition());
						codeTreeItem.setData("CodeName", ecoreCode.getCodeName());
//						ArrayList<EcoreCode> codeByCodeSetId = DerbyDaoUtil.getCodeByCodeSetId(dataType.getId());
//						generateContainmentTreeForMessageComponent(treeItem, codeByCodeSetId, false);
					}
				} else {
					if (treeRoot.getData("meStatusCombo") == null
							|| treeRoot.getData("meStatusCombo").equals("Provisionally Registered")) {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT1));
					} else {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
					}
					// Load data type constraint
					constraintList = DerbyDaoUtil.getContraints(dataType.getId());
					addContraintsNode(treeItem, constraintList, dataType.getName(), null);
				}
			}

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
//				treeItem.setData("mcStatus",treeRoot.getData("meStatusCombo"));
//				String mkesql="select * from ecore_semantic_markup_element where semantic_markup_id"
//						+ " in (select id from ecore_semantic_markup where obj_id=? and type='Synonym') ";
//				
//				Map<String,Map<String, String>> mkeMap=new HashMap<String,Map<String, String>>();
//				java.util.List<SynonymVO> synonymVOs = new ArrayList<SynonymVO>();
//				if(mkeMap!=null  && !mkeMap.isEmpty()) {
//					for(String eid:mkeMap.keySet()) {
//						Map<String, String> mt=mkeMap.get(eid);
//						SynonymVO v=new SynonymVO();
//						v.setObjId(me.getId());
//						v.setContext(mt.get("context"));
//						v.setSynonym(mt.get("value"));
//						synonymVOs.add(v);
//					}
//				}
//				
//				java.util.List<String[]> synonymsTableItems = new ArrayList<String[]>();
//				if (synonymVOs != null && synonymVOs.size() > 0) {
//					for (SynonymVO synonymVO : synonymVOs) {
//						String[] str = new String[2];
//						str[0] = synonymVO.getContext();
//						str[1] = synonymVO.getSynonym();
//						synonymsTableItems.add(str);
//					}
//				}
//				treeItem.setData("beSynonymsTableItems", synonymsTableItems);

				java.util.List<String[]> constraintsTableItems = new ArrayList<String[]>();
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
				treeItem.setData("beConstraintsTableItems", constraintsTableItems);
			}
		}
	}

	public static void addContraintsNode(TreeItem treeRoot, ArrayList<EcoreConstraint> constraintList, String nodeName,
			Tree tree) {
		if (nodeName == null) {
			nodeName = "Textual";
		}
		TreeItem constraintTreeItem;
		for (EcoreConstraint constraint : constraintList) {
			if (tree == null) {
				constraintTreeItem = new TreeItem(treeRoot, SWT.NONE);
			} else {
				constraintTreeItem = new TreeItem(tree, SWT.NONE);
			}

			constraintTreeItem.setText(nodeName + " : " + constraint.getName());
			constraintTreeItem.setData("constraintId", constraint.getId());
			constraintTreeItem.setData("constraint", constraint);
			constraintTreeItem.setImage(ImgUtil.createImg(ImgUtil.CONSTRAINT));
		}
	}

	public static void generateContentTreeForMessageComponent(TreeItem treeRoot, ArrayList<EcoreMessageElement> meList,
			String registrationStatus) {

		for (EcoreMessageElement me : meList) {

			TreeItem treeItem = new TreeItem(treeRoot, SWT.NONE);

			treeItem.setData("msgElementID", me.getId());

			String elementDisplayName = me.getName() + " [" + me.getMinOccurs() + "," + me.getMaxOccurs() + "]";
			// 数据类型 1：datatype，2：消息组件
			if ("2".equals(me.getType())) {
				EcoreMessageComponent mc = DerbyDaoUtil.getMessageComponentById(me.getTypeId());
				treeItem.setText(elementDisplayName + " : " + mc.getName());
				// 1：MessageComponent，2：ChoiceComponent
				if ("1".equals(mc.getComponentType())) {
					if (RegistrationStatus.Registered.getStatus().equals(registrationStatus)) {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
					} else {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.ME));
					}

				} else if ("2".equals(mc.getComponentType())) {

					if (RegistrationStatus.Registered.getStatus().equals(registrationStatus)) {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
					} else {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
					}

				}

				ArrayList<EcoreMessageElement> subMeList = DerbyDaoUtil.getMessageElementList(mc.getId());
				generateContentTreeForMessageComponent(treeItem, subMeList, mc.getRegistrationStatus());

				// Load Message Component Constraint
				ArrayList<EcoreConstraint> mcConstraintList = DerbyDaoUtil.getContraints(mc.getId());
				addContraintsNode(treeItem, mcConstraintList, mc.getName(), null);
				treeItem.setData("typeName", mc.getName());
				treeItem.setData("dataType", ObjTypeEnum.MessageComponent.getType());
				treeItem.setData("msgComponentId", mc.getId());
			} else if ("3".equals(me.getType()) || "1".equals(me.getType())) { // 数据类型，1：datatype，2：消息组件 3:
																				// Datatype(User Defined) 4: external
																				// schema
				// datatype
				EcoreDataType dataType = DerbyDaoUtil.getDataTypeById(me.getTypeId());
				treeItem.setText(elementDisplayName + " : " + dataType.getName());
				// 1:CodeSet;2:Text;3:Boolean,4:Indicator,5:Decimal,6:Rate,7:Amount,8:Quantity,9:Time,10:Binary,11:SchemaType,12:UserDefined
				if ("1".equals(dataType.getType())) { // codeSet 已到最底层了

					if (RegistrationStatus.Registered.getStatus().equals(dataType.getRegistrationStatus())) {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
					} else {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
					}
					for (EcoreCode ecoreCode : DerbyDaoUtil.getCodeByCodeSetId(dataType.getId())) {
						TreeItem codeTreeItem = new TreeItem(treeItem, SWT.NONE);
						codeTreeItem.setText(ecoreCode.getName() + " [" + ecoreCode.getCodeName() + "]");
						codeTreeItem.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
						codeTreeItem.setData("codeId", ecoreCode.getId());
						codeTreeItem.setData("ecoreCode", ecoreCode);
						ArrayList<EcoreConstraint> ConstraintList = DerbyDaoUtil
								.getContraints(ecoreCode.getCodesetid());
						addContraintsNode(treeItem, ConstraintList, ecoreCode.getName(), null);
					}
				} else {

					if (RegistrationStatus.Registered.getStatus().equals(dataType.getRegistrationStatus())) {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE));
					} else {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
					}
					// Load data type constraint
					ArrayList<EcoreConstraint> dataTypeConstraintList = DerbyDaoUtil.getContraints(dataType.getId());
					addContraintsNode(treeItem, dataTypeConstraintList, dataType.getName(), null);
				}
				treeItem.setData("typeName", dataType.getName());
				treeItem.setData("dataType", ObjTypeEnum.DataType.getType());
				treeItem.setData("dataTypeId", dataType.getId());
			} else if ("4".equals(me.getType())) {
				// External schemas
				EcoreExternalSchema ecoreExternalSchema = DerbyDaoUtil.getEcoreExternalSchemaById(me.getTypeId());
				treeItem.setText(elementDisplayName + " : " + ecoreExternalSchema.getName());
				treeItem.setImage(ImgUtil.createImg(ImgUtil.EXTERNAL_SCHEMAS));
				ArrayList<EcoreMessageElement> subMeList = DerbyDaoUtil
						.getMessageElementList(ecoreExternalSchema.getId());
				generateContentTreeForMessageComponent(treeItem, subMeList,
						ecoreExternalSchema.getRegistrationStatus());
				// Load External Schema Constraint
				ArrayList<EcoreConstraint> externalSchemaConstraintList = DerbyDaoUtil
						.getContraints(ecoreExternalSchema.getId());
				addContraintsNode(treeItem, externalSchemaConstraintList, ecoreExternalSchema.getName(), null);
				treeItem.setData("typeName", ecoreExternalSchema.getName());
				treeItem.setData("dataType", ObjTypeEnum.ExternalSchema.getType());
				treeItem.setData("externalSchemaId", ecoreExternalSchema.getId());
			}
		}
	}

	public static void generateContentTreeForMessageDefinition(TreeItem treeRoot,
			ArrayList<EcoreMessageBuildingBlock> msgBldBlockList, Tree tree, String registrationStatus) {

		TreeItem treeItem = null;

		for (EcoreMessageBuildingBlock msgBldBlock : msgBldBlockList) {
			if (tree == null) {
				treeItem = new TreeItem(treeRoot, SWT.NONE);
			} else {
				treeItem = new TreeItem(tree, SWT.NONE);
			}

			String elementDisplayName = msgBldBlock.getName() + " [" + msgBldBlock.getMinOccurs() + ","
					+ msgBldBlock.getMaxOccurs() + "]";
			treeItem.setText(elementDisplayName);
			treeItem.setData("bbId", msgBldBlock.getId());
			treeItem.setData("mbbNameValue", msgBldBlock.getName());
			treeItem.setData("mbbDocumentationValue", msgBldBlock.getDefinition());
			treeItem.setData("mbbMinOccursValue", msgBldBlock.getMinOccurs());
			treeItem.setData("mbbMaxOccursValue", msgBldBlock.getMaxOccurs());
			treeItem.setData("msgBldBlock", msgBldBlock);
			treeItem.setImage(ImgUtil.createImg(ImgUtil.ME));
			// 数据类型 1：datatype，2：消息组件
			if ("2".equals(msgBldBlock.getDataType())) {
				EcoreMessageComponent mc = DerbyDaoUtil.getMessageComponentById(msgBldBlock.getDataTypeId());
				treeItem.setData("msgComponentId", mc.getId());
				treeItem.setText(elementDisplayName + " : " + mc.getName());
				// 1：MessageComponent，2：ChoiceComponent
				if ("1".equals(mc.getComponentType())) {
					if (RegistrationStatus.Registered.getStatus().equals(registrationStatus)) {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
					} else {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.ME));
					}
				} else if ("2".equals(mc.getComponentType())) {
					if (RegistrationStatus.Registered.getStatus().equals(registrationStatus)) {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
					} else {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
					}
				}
				ArrayList<EcoreMessageElement> subMeList = DerbyDaoUtil.getMessageElementList(mc.getId());
				// Recursive invoke 消息组件
				generateContentTreeForMessageComponent(treeItem, subMeList, mc.getRegistrationStatus());

				// Load Message Component Constraint
				ArrayList<EcoreConstraint> mcConstraintList = DerbyDaoUtil.getContraints(mc.getId());
				treeItem.setData("mbbConstraintList", DerbyDaoUtil.getContraints(msgBldBlock.getId()));
				treeItem.setData("mbbSynonymsTableItems",
						ConvertToKeyValueString(DerbyDaoUtil.getSynonymsList(msgBldBlock.getId())));
				addContraintsNode(treeItem, mcConstraintList, mc.getName(), null);
				treeItem.setData("typeName", mc.getName());
				treeItem.setData("dataType", ObjTypeEnum.MessageComponent.getType());

			} else if ("1".equals(msgBldBlock.getDataType())) {
				// 数据类型 1：datatype
				EcoreDataType dataType = DerbyDaoUtil.getDataTypeById(msgBldBlock.getDataTypeId());
				treeItem.setData("dataTypeId", dataType.getId());
				treeItem.setText(elementDisplayName + " : " + dataType.getName());
				// 1:CodeSet;2:Text;3:Boolean,4:Indicator,5:Decimal,6:Rate,7:Amount,8:Quantity,9:Time,10:Binary,11:SchemaType,12:UserDefined
				if ("1".equals(dataType.getType())) {

					if (RegistrationStatus.Registered.getStatus().equals(registrationStatus)) {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
					} else {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
					}
					for (EcoreCode ecoreCode : DerbyDaoUtil.getCodeByCodeSetId(dataType.getId())) {
						TreeItem codeTreeItem = new TreeItem(treeItem, SWT.NONE);
						codeTreeItem.setText(ecoreCode.getName() + " [" + ecoreCode.getCodeName() + "]");
						codeTreeItem.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
						codeTreeItem.setData("codeId", ecoreCode.getId());
						codeTreeItem.setData("ecoreCode", ecoreCode);
					}
				} else {

					if (RegistrationStatus.Registered.getStatus().equals(registrationStatus)) {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE));
					} else {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
					}

					// Load data type constraint
					ArrayList<EcoreConstraint> dataTypeConstraintList = DerbyDaoUtil.getContraints(dataType.getId());
					treeItem.setData("mbbConstraintList", DerbyDaoUtil.getContraints(msgBldBlock.getId()));
					treeItem.setData("mbbSynonymsTableItems",
							ConvertToKeyValueString(DerbyDaoUtil.getSynonymsList(msgBldBlock.getId())));
					addContraintsNode(treeItem, dataTypeConstraintList, dataType.getName(), null);
				}
				treeItem.setData("typeName", dataType.getName());
				treeItem.setData("dataType", ObjTypeEnum.DataType.getType());
			}
		}
	}

	public static void generateContainmentTreeForBComponentDataType(TreeItem treeRoot) {

		ArrayList<EcoreConstraint> constraintList = new ArrayList<EcoreConstraint>();

		EcoreDataType dataType = DerbyDaoUtil.getDataTypeById(treeRoot.getData("beTypeTextId").toString());
		if ("1".equals(dataType.getType())) { // codeSet 已到最底层了
			for (EcoreCode ecoreCode : DerbyDaoUtil.getCodeByCodeSetId(dataType.getId())) {
				TreeItem codeTreeItem = new TreeItem(treeRoot, SWT.NONE);
				codeTreeItem.setText(ecoreCode.getName() + " [" + ecoreCode.getCodeName() + "]");
				codeTreeItem.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
				codeTreeItem.setData("id", ecoreCode.getId());
				codeTreeItem.setData("codeType", dataType.getType());
			}
		} else {
			treeRoot.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
			constraintList = DerbyDaoUtil.getContraints(dataType.getId());
			addContraintsNode(treeRoot, constraintList, dataType.getName(), null);
		}
	}

	private static ArrayList<Object> idList = new ArrayList<Object>();
	private static ArrayList<EcoreBusinessElement> bizElementsByBizComponentId;
	private static String string4;
	private static ArrayList<EcoreTreeNode> arrayList;
	private static String string7;
	private static String string8;
	private static ArrayList<EcoreTreeNode> arrayList2;
	private static String string9;
	private static String string6;

	public static void initList() {
		idList.clear();
	}

//MC模块BusniessTrace 表格数据生成
	public static void generateMCDefineTrace(Tree treeRoot, ArrayList<EcoreBusinessElement> beList,
			Map<String, ArrayList<EcoreBusinessElement>> allBeByComponentId,
			Map<String, ArrayList<EcoreTreeNode>> allSubBusiness, Map<String, String> allBusinessComponent,
			Map<String, ArrayList<EcoreBusinessElement>> allBeByTyId) {

		for (EcoreBusinessElement be : beList) {
//		if (!idList.contains(be.getId())) {

			TreeItem treeItem = new TreeItem(treeRoot, SWT.NONE);
			String elementDisplayName = be.getName() + " [" + be.getMinOccurs() + "," + be.getMaxOccurs() + "]";
			string4 = allBusinessComponent.get(be.getTypeId());
			treeItem.setText(elementDisplayName + ":" + string4);
			treeItem.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
			treeItem.setData("FirstTreeName", be.getName());
//		idList.add(be.getId());
//		TreeItem fistTree = new TreeItem(treeRoot, SWT.NONE);
//		String name=bc.getName();
//		fistTree.setText(name);
//		fistTree.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
//		fistTree.setData("FirstTreeName",be.getName());
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
//			TreeItem fistTree = new TreeItem(treeRoot, SWT.NONE);
//			String name=string4;
//			fistTree.setText(name);
//			fistTree.setImage(ImgUtil.createImg(ImgUtil.BC));
//			fistTree.setData("FirstTreeName",name+"/.");
//			allSubBusiness = DerbyDaoUtil.getSubBusinessComponentById(treeItem.getData("beType_id").toString());
				// AS
				arrayList = allSubBusiness.get(be.getTypeId());
				if (arrayList != null) {
					arrayList.forEach(t -> {
						string7 = allBusinessComponent.get(t.getId());
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
							arrayList2 = allSubBusiness.get(t.getId());
							if (arrayList2 != null) {
								arrayList2.forEach(j -> {
									string6 = allBusinessComponent.get(j.getId());
									TreeItem subItem_1 = new TreeItem(subItem, SWT.NONE);
									subItem_1.setImage(ImgUtil.createImg(ImgUtil.BC));
									subItem_1.setText("as " + string6);
									subItem_1.setData("FirstTreeName",
											subItem.getData("FirstTreeName") + "/(as" + string6 + ")");

								});
							}
							// BE
							GenerateCommonTree.generateMCOfBcSubTypeforDefineTrace(subItem,
									allBeByComponentId.get(t.getId()), allBeByComponentId, allSubBusiness,
									allBusinessComponent, allBeByTyId);
						}

					});
				}

				bizElementsByBizComponentId = allBeByComponentId.get(treeItem.getData("beType_id").toString());
				if (bizElementsByBizComponentId != null) {
					bizElementsByBizComponentId.forEach(r -> {
						TreeItem beItem = new TreeItem(treeItem, SWT.NONE);
						beItem.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
						String beItemName = r.getName() + " [" + r.getMinOccurs() + "," + r.getMaxOccurs() + "]";
						if ("2".equals(r.getType())) {
							String name = allBusinessComponent.get(r.getTypeId());
							beItem.setText(beItemName + " : " + name);
							beItem.setData("FirstTreeName", treeItem.getData("FirstTreeName") + "/" + r.getName());
							if (allBeByComponentId.get(r.getTypeId()) != null) {
								// bc树头
								string8 = allBusinessComponent.get(r.getTypeId());
								TreeItem fistTree = new TreeItem(beItem, SWT.NONE);
								fistTree.setImage(ImgUtil.createImg(ImgUtil.BC));
								fistTree.setText(string8);
								fistTree.setData("FirstTreeName", string8 + "/.");
								// AS
								arrayList2 = allSubBusiness.get(r.getTypeId());
								if (arrayList2 != null) {
									arrayList2.forEach(j -> {
										string9 = allBusinessComponent.get(j.getId());
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

											GenerateCommonTree.generateMCOfBcSubTypeforDefineTrace(subItem_1,
													allBeByComponentId.get(j.getId()), allBeByComponentId,
													allSubBusiness, allBusinessComponent, allBeByTyId);
										}
									});
								}
								// BE
								GenerateCommonTree.generateMCOfBcSubTypeforDefineTrace(beItem,
										allBeByComponentId.get(r.getTypeId()), allBeByComponentId, allSubBusiness,
										allBusinessComponent, allBeByTyId);

							}
						} else {
							// datatype
							EcoreDataType dataType = DerbyDaoUtil.getDataTypeById(r.getTypeId());
							beItem.setData("beTypeText", dataType.getName());
							beItem.setData("beType_id", r.getTypeId());
							beItem.setText(beItemName + " : " + dataType.getName());
							beItem.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
						}

					});
				}

			} else {
				// datatype
				EcoreDataType dataType = DerbyDaoUtil.getDataTypeById(be.getTypeId());
				treeItem.setData("beTypeText", dataType.getName());
				treeItem.setData("beType_id", be.getTypeId());
				treeItem.setText(elementDisplayName + " : " + dataType.getName());
				treeItem.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
			}

		}
	}

//public static void generateMCDefineTraces(TreeItem treeRoot, ArrayList<EcoreBusinessElement> beList) {
//	
//	for (EcoreBusinessElement be: beList) {
//		if (!idList.contains(be.getId())) {
//		TreeItem treeItem = new TreeItem(treeRoot, SWT.NONE);
//		String elementDisplayName = be.getName() + " [" + be.getMinOccurs() + "," + be.getMaxOccurs() + "]";
//		ArrayList<EcoreConstraint> constraintList = new ArrayList<EcoreConstraint>();
//		treeItem.setText(elementDisplayName);
//		treeItem.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
//		idList.add(be.getId());
//		// 数据类型 1：datatype，2：消息组件
//		if ("2".equals(be.getType())) {
//			treeItem.setData("beTypeText",be.getName());
//			treeItem.setData("beType_id", be.getTypeId());
//			generateMCDefineTraces(treeItem,
//					DerbyDaoUtil.getBizElementsByBizComponentId(String.valueOf(be.getTypeId())));
//		} else {
//			// datatype
//			EcoreDataType dataType = DerbyDaoUtil.getDataTypeById(be.getTypeId());
//			treeItem.setData("beTypeText",dataType.getName());
//			treeItem.setData("beType_id", be.getTypeId());
//			// 1:CodeSet;2:Text;3:Boolean,4:Indicator,5:Decimal,6:Rate,7:Amount,8:Quantity,9:Time,10:Binary,11:SchemaType,12:UserDefined
//			if ("1".equals(dataType.getType())) { // codeSet 已到最底层了
//				for (EcoreCode ecoreCode: DerbyDaoUtil.getCodeByCodeSetId(dataType.getId())) {
//					TreeItem codeTreeItem = new TreeItem(treeItem, SWT.NONE);
//					codeTreeItem.setText(ecoreCode.getName() + " [" + ecoreCode.getCodeName() + "]");
//					codeTreeItem.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
//					codeTreeItem.setData("id",ecoreCode.getId());
//					codeTreeItem.setData("codeType",be.getType());
//					
//				}
//			} else {
//				// Load data type constraint
//				treeItem.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
//			}
//		}
//		}
//	}
//}
//mc中DefineTrace功能中新增加载bc的subtype
	private static String string;
	private static ArrayList<EcoreBusinessElement> arrayList5;
	private static String string5;
	private static ArrayList<EcoreTreeNode> arrayList4;
	private static ArrayList<EcoreTreeNode> arrayList6;
	private static String string3;
	private static String string2;

	public static void generateMCOfBcSubTypeforDefineTrace(TreeItem treeRoot, ArrayList<EcoreBusinessElement> beList,
			Map<String, ArrayList<EcoreBusinessElement>> allBeByComponentId,
			Map<String, ArrayList<EcoreTreeNode>> allSubBusiness, Map<String, String> allBusinessComponent,
			Map<String, ArrayList<EcoreBusinessElement>> allBeByTyId) {

		for (EcoreBusinessElement be : beList) {

			TreeItem treeItem = new TreeItem(treeRoot, SWT.NONE);
			String elementDisplayName = be.getName() + " [" + be.getMinOccurs() + "," + be.getMaxOccurs() + "]";
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
//			String name=bc.getName();
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
						// GenerateCommonTree.generateMCOfBcSubTypeforDefineTrace(subItem,
//						DerbyDaoUtil.getBizElementsByBizComponentId(t.getId()));
						// 从缓存中获取数据
//				bizElementsByBizComponent = allBeByComponentId.get(t.getId());
//				if (bizElementsByBizComponent!=null) {
//					bizElementsByBizComponent.forEach(r -> {
//					if (r.getType().equals("2")) {
//						
//						subItem.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
//						string2 = allBusinessComponent.get(r.getTypeId());
//						String SubName = r.getName() + " [" + r.getMinOccurs() + "," + r.getMaxOccurs() + "]";
//						subItem.setText(SubName+" : "+string2);
//						
//						
//					}else {
//						EcoreDataType dataType = DerbyDaoUtil.getDataTypeById(r.getTypeId());
//						subItem.setText(r.getName()+ " : " + dataType.getName());
//						subItem.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
//						
//					}
//				});
//				}

					});

				}
				// BE
				arrayList5 = allBeByComponentId.get(be.getTypeId());
				if (arrayList5 != null) {
//			TreeItem fistTree_1 = new TreeItem(treeItem, SWT.NONE);
////				String name=bc.getName();
//			fistTree_1.setText(string);
//			fistTree_1.setImage(ImgUtil.createImg(ImgUtil.BC));
//			fistTree_1.setData("FirstTreeName",treeItem.getData("FirstTreeName")+"/.");
//			
					arrayList5.forEach(v -> {
						arrayList6 = allSubBusiness.get(v.getId());
						if (arrayList6 != null) {
							arrayList6.forEach(k -> {
								string2 = allBusinessComponent.get(k.getId());
								TreeItem subItem = new TreeItem(treeItem, SWT.NONE);
								subItem.setImage(ImgUtil.createImg(ImgUtil.BC));
								subItem.setText("as " + string2);
								subItem.setData("FirstTreeName", treeItem.getData("FirstTreeName") + "/" + v.getName());
							});
						}

						String name = v.getName() + "[" + v.getMinOccurs() + "," + v.getMaxOccurs() + "]";
						string5 = allBusinessComponent.get(v.getTypeId());
						TreeItem subItem_5 = new TreeItem(treeItem, SWT.NONE);
						subItem_5.setData("FirstTreeName", treeItem.getData("FirstTreeName") + "/" + v.getName());
						if ("2".equals(v.getType())) {
							subItem_5.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
							subItem_5.setText(name + " : " + string5);
						} else {
							EcoreDataType dataType = DerbyDaoUtil.getDataTypeById(v.getTypeId());
							subItem_5.setText(name + " : " + dataType.getName());
							subItem_5.setImage(ImgUtil.createImg(ImgUtil.BC_BE));

						}

					});
				}

			} else {
				// datatype
				EcoreDataType dataType = DerbyDaoUtil.getDataTypeById(be.getTypeId());
				treeItem.setData("beTypeText", dataType.getName());
				treeItem.setData("beType_id", be.getTypeId());
				treeItem.setText(elementDisplayName + " : " + dataType.getName());
				treeItem.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
			}

		}
	}

//private static String changString(String text) {
//	String substring=null;
//	String substring_1=null;
//	if (text.contains("[")) {
//		int indexOf = text.indexOf("[");
//		substring= text.substring(0,indexOf);
//		if (text.contains("/")) {
//		int indexOf_2 = text.indexOf("/");
//		substring_1= text.substring(indexOf_2+1,text.length());
//		     if (substring_1.contains("[")) {
//		    	 int indexOf_1 = substring_1.indexOf("[");
//		    	 String substring_2 = substring_1.substring(0,indexOf_1);
//		    	 substring=substring+"/"+substring_2;
//		    	 String substring_3 = substring_1.substring(indexOf_1+1,substring_1.length());
//		    	 String subName = null;
//		    	 if (substring_3.contains("[")) {
//		    		 if (substring_3.contains("/")) {
//		    			 int indexOf_3 = substring_3.indexOf("/");
//		    			 substring_3=substring_3.substring(indexOf_3+1,substring_3.length());
//					}
//		    		 subName =changString(substring_3);
//		    		 substring=substring+"/"+subName;
//		    	 }else {
//		    		 return substring;
//		    	 }
//		    	 
//		    	 
//			}else {
//				return substring;
//			}
//		}else {
//			return substring;
//		}
//		
//	}else {
//		substring=text;
//	}
//	
//	
//	return substring;
//}

	public static void generateContainmentTreeForBComponent(TreeItem treeRoot, ArrayList<EcoreBusinessElement> beList,
			boolean... isMe) {

		for (EcoreBusinessElement be : beList) {

			TreeItem treeItem = new TreeItem(treeRoot, SWT.NONE);
			String elementDisplayName = be.getName() + " [" + be.getMinOccurs() + "," + be.getMaxOccurs() + "]";
			ArrayList<EcoreConstraint> constraintList = new ArrayList<EcoreConstraint>();
//			String status=null;
//			if (MessageComponentCreateEditor.summaryStatusCombo.getText()!=null) {
//				status=MessageComponentCreateEditor.summaryStatusCombo.getText();
//			}
			treeItem.setText(elementDisplayName);
			treeItem.setImage(ImgUtil.createImg(ImgUtil.BC_BE));

			// 数据类型 1：datatype，2：消息组件
			if ("2".equals(be.getType())) {
				treeItem.setData("beTypeText", be.getName());
				treeItem.setData("beType_id", be.getTypeId());
//				ArrayList<EcoreBusinessElement> subMeList = DerbyDaoUtil.getBizElementsByBizComponentId(be.getTypeId());
//				generateContainmentTreeForBComponent(treeItem, subMeList,isMe);

				// Load Message Component Constraint
				constraintList = DerbyDaoUtil.getContraints(be.getBusinessComponentId());
				addContraintsNode(treeItem, constraintList, be.getName(), null);
			} else {
				// datatype
				EcoreDataType dataType = DerbyDaoUtil.getDataTypeById(be.getTypeId());
				treeItem.setData("beTypeText", dataType.getName());
				treeItem.setData("beType_id", be.getTypeId());
//				treeItem.setText(elementDisplayName + " : " + dataType.getName());
//				treeItem.setText(elementDisplayName);
				// 1:CodeSet;2:Text;3:Boolean,4:Indicator,5:Decimal,6:Rate,7:Amount,8:Quantity,9:Time,10:Binary,11:SchemaType,12:UserDefined
				if ("1".equals(dataType.getType())) { // codeSet 已到最底层了
					for (EcoreCode ecoreCode : DerbyDaoUtil.getCodeByCodeSetId(dataType.getId())) {
						TreeItem codeTreeItem = new TreeItem(treeItem, SWT.NONE);
						codeTreeItem.setText(ecoreCode.getName() + " [" + ecoreCode.getCodeName() + "]");
						codeTreeItem.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
						codeTreeItem.setData("id", ecoreCode.getId());
						codeTreeItem.setData("codeType", be.getType());

//						constraintList = DerbyDaoUtil.getContraints(ecoreCode.getId());
//						addContraintsNode(codeTreeItem, constraintList, ecoreCode.getName(), null);
//						codeTreeItem.setData("codeType", "1");
//						codeTreeItem.setData("CodesName", ecoreCode.getName());
//						codeTreeItem.setData("Document", ecoreCode.getDefinition());
//						codeTreeItem.setData("CodeName", ecoreCode.getCodeName());
//						ArrayList<EcoreCode> codeByCodeSetId = DerbyDaoUtil.getCodeByCodeSetId(dataType.getId());
//						generateContainmentTreeForMessageComponent(treeItem, codeByCodeSetId, false);
					}
				} else {
					// Load data type constraint
					treeItem.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
					constraintList = DerbyDaoUtil.getContraints(dataType.getId());
					addContraintsNode(treeItem, constraintList, dataType.getName(), null);
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
//				treeItem.setData("beTypeText", be.getTypeName());
				treeItem.setData("isDerivedCheckButton", Boolean.getBoolean(be.getIsDerived()));
//				treeItem.setData("isTechnical", "");
				treeItem.setData("beObjectIdentifierText", be.getObjectIdentifier());
				treeItem.setData("beStatusCombo", be.getRegistrationStatus());
				treeItem.setData("beRemovalDate", be.getRemovalDate());

				java.util.List<String[]> constraintsTableItems = new ArrayList<String[]>();
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
				treeItem.setData("beConstraintsTableItems", constraintsTableItems);
			}
		}
	}

	public static void generateContentTreeForDataType(TreeItem treeNode, ArrayList<EcoreCode> ecoreCodeList) {

		for (EcoreCode ecoreCode : ecoreCodeList) {
			String displayNodeName = ecoreCode.getName() + " " + "[" + ecoreCode.getCodeName() + "]";
			TreeItem treeItem = new TreeItem(treeNode, SWT.NONE);
			treeItem.setText(displayNodeName);
			treeItem.setData("codeId", ecoreCode.getId());
			treeItem.setData("ecoreCode", ecoreCode);
			treeItem.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
		}
	}

	private static ArrayList<String> ConvertToKeyValueString(ArrayList<EcoreSemanticMarkupElement> synonymsList) {
		ArrayList<String> keyValuePairList = new ArrayList<>();
		for (EcoreSemanticMarkupElement synonym : synonymsList) {
			String keyValuePair = synonym.getName() + "," + synonym.getValue();
			keyValuePairList.add(keyValuePair);
		}
		return keyValuePairList;
	}
}
