package org.cufir.plugin.mr.editor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.cufir.plugin.mr.MrHelper;
import org.cufir.plugin.mr.bean.ButtonPolicy;
import org.cufir.plugin.mr.bean.ObjTypeEnum;
import org.cufir.plugin.mr.bean.RegistrationStatusEnum;
import org.cufir.plugin.mr.bean.TextPolicy;
import org.cufir.plugin.mr.utils.ImgUtil;
import org.cufir.plugin.mr.utils.NumberUtil;
import org.cufir.plugin.mr.utils.SystemUtil;
import org.cufir.s.ecore.bean.EcoreConstraint;
import org.cufir.s.ecore.bean.EcoreDataType;
import org.cufir.s.ecore.bean.EcoreMessageBuildingBlock;
import org.cufir.s.ecore.bean.EcoreMessageComponent;
import org.cufir.s.ecore.bean.EcoreMessageDefinition;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.springframework.util.StringUtils;

/**
 * 报文内容子窗口选择组件用
 * @author tangmaoquan
 * @Date 2021年10月26日
 */
public class MessageDefinitionEditorChooser {
	
	public static void createMsgElementSelectionDialogue(Tree elementsTree, EcoreMessageDefinition messageDefintion) {
		Shell shell = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("Message Element General Information");
		shell.setLayout(new FormLayout());

		// 改变弹窗位置
		SystemUtil.center(shell);

		Composite c = new Composite(shell, SWT.NONE);
		Text nameText = new SummaryRowTextComposite(c, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 10, "Name")).getText();
		
		Text docText = new SummaryRowTextComposite(c, new TextPolicy(TextPolicy.TEXT_CONTENT_SCROLL_TYPE, 38, "Documentation")).getText();

		Text minText = new SummaryRowTextComposite(c, new TextPolicy(TextPolicy.TEXT_CONTENT_SHORT_TYPE, 193, "Min Occurs", "1")).getText();
		
		Text maxOccursText = new SummaryRowTextComposite(c, new TextPolicy(TextPolicy.TEXT_CONTENT_SHORT_TYPE, 221, "Max Occurs", "1")).getText();
		
		Text xmlTagText = new SummaryRowTextComposite(c, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 249, "XML Tag")).getText();

		new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_CHECK, 277, "Is Technical",false)).getButton();
		
		Text typeText = new SummaryRowTextComposite(c, new TextPolicy(TextPolicy.TEXT_CONTENT_BUTTON_SELECT_TYPE, 305, "Type")).getText();
		
		Button typeButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,"choose")).getButton();
		typeButton.setBounds(390, 305, 50, 25);
		typeButton.addListener(SWT.MouseUp, p -> {
			createSelectMsgElementType(typeText);
		});
		Button finishButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Finish")).getButton();
		finishButton.setBounds(280, 355, 60, 30);
		finishButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TreeItem treeNode = new TreeItem(elementsTree, SWT.NONE);
				String nodeName;
				String min = StringUtils.isEmpty(minText.getText()) ? "0" : minText.getText();
				String max = StringUtils.isEmpty(maxOccursText.getText()) ? "*" : maxOccursText.getText();
				if (typeText.getText().isEmpty()) {
					nodeName = nameText.getText() + " [" + min + ", " + max + "]";
				} else {
					nodeName = nameText.getText() + " [" + min + ", " + max + "] : " + typeText.getText();
				}
				treeNode.setText(nodeName);
				EcoreMessageBuildingBlock msgBldBlock = new EcoreMessageBuildingBlock();
				msgBldBlock.setId(UUID.randomUUID().toString());
				msgBldBlock.setMessageId(messageDefintion.getId());
				msgBldBlock.setName(nameText.getText());
				msgBldBlock.setDefinition(docText.getText());
				msgBldBlock.setMinOccurs(NumberUtil.getInt(minText.getText()));
				msgBldBlock.setMaxOccurs(NumberUtil.getInt(maxOccursText.getText()));
				msgBldBlock.setXmlTag(xmlTagText.getText());
				msgBldBlock.setRegistrationStatus(RegistrationStatusEnum.Provisionally.getStatus());
				treeNode.setData("msgBldBlock", msgBldBlock);
				treeNode.setImage(ImgUtil.createImg(ImgUtil.ME));

				List<EcoreConstraint> css = MrRepository.get().ecoreConstraints;
				MrEditorTreeCreator mrEditorTreeCreator = new MrEditorTreeCreator();
				if (typeText.getData("msgComponentId") != null) {
					treeNode.setData("msgComponentId", String.valueOf(typeText.getData("msgComponentId")));
					treeNode.setData("dataType", ObjTypeEnum.MessageComponent.getType());
					msgBldBlock.setDataTypeId(String.valueOf(typeText.getData("msgComponentId")));
					msgBldBlock.setDataType("2");
					treeNode.setImage(ImgUtil.createImg(ImgUtil.ME));
					treeNode.setData("typeName", typeText.getText());

					// 用Message Component里的元素，构造Message Component树
					List<EcoreMessageComponent> mcs = MrRepository.get().ecoreMessageComponents;
					mrEditorTreeCreator.generateContentTreeForMessageComponent(treeNode,
							MrHelper.getMsgElementList(String.valueOf(typeText.getData("msgComponentId"))),
							MrHelper.getMessageComponentById(mcs, (String) typeText.getData("msgComponentId"))
									.getRegistrationStatus());
					// 当前选中的Message Component的约束
					List<EcoreConstraint> ecs = MrHelper.findConstraintsByObjId(css, typeText.getData("msgComponentId") + "");
					mrEditorTreeCreator.addConstraintsNode(treeNode, ecs, typeText.getText(), null);
				} else if (typeText.getData("dataTypeId") != null) {
					treeNode.setData("dataTypeId", String.valueOf(typeText.getData("dataTypeId")));
					treeNode.setData("dataType", ObjTypeEnum.DataType.getType());
					msgBldBlock.setDataTypeId(String.valueOf(typeText.getData("dataTypeId")));
					msgBldBlock.setDataType("1");
					treeNode.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
					treeNode.setData("typeName", typeText.getText());
					// load datatype 下的 TYPE是1的 code(ECORE_CODE)
					
					mrEditorTreeCreator.generateContentTreeForDataType(treeNode,
							MrHelper.findCodesByCodeSetId(MrRepository.get().ecoreCodes, typeText.getData("dataTypeId") + ""));
					// 当前选中的DataType的约束
					List<EcoreConstraint> ecs = MrHelper.findConstraintsByObjId(css, typeText.getData("dataTypeId") + "");
					mrEditorTreeCreator.addConstraintsNode(treeNode, ecs, typeText.getText(), null);
				}
				shell.close();
			}
		});
		Button cancelButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Cancel")).getButton();
		cancelButton.setBounds(350, 355, 60, 30);
		cancelButton.addListener(SWT.MouseUp, p -> {
			shell.close();
		});
		shell.setSize(510, 430);
		shell.open();
		shell.layout();
	}
	
	/**
	 * 加载choose
	 */
	public static void createSelectMsgElementType(Text typeText) {
		List<EcoreMessageComponent> mcs = MrRepository.get().ecoreMessageComponents;
		List<EcoreDataType> dts = MrRepository.get().ecoreDataTypes;
		
		java.util.List<EcoreMessageComponent> emcs = mcs.stream().filter(mc -> !RegistrationStatusEnum.Obsolete.getStatus().equals(mc.getRegistrationStatus())).collect(Collectors.toList());
		java.util.List<EcoreDataType> edts = dts.stream().filter(dt -> !RegistrationStatusEnum.Obsolete.getStatus().equals(dt.getRegistrationStatus())).collect(Collectors.toList());
		
		Shell shell = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("Select Message Element type:");
		shell.setLayout(new FormLayout());
		// 改变弹窗位置
		SystemUtil.center(shell);
		Composite c = new Composite(shell, SWT.NONE);
		Text searchText = new Text(c, SWT.BORDER);
		searchText.setBounds(10, 10, 350, 30);
		
		Image sub1 = ImgUtil.createImg(ImgUtil.MC_SUB1_COMPONENT);
		Image sub3 = ImgUtil.createImg(ImgUtil.MC_SUB3_COMPONENT);
		Table msgComponentTable = new Table(c, SWT.BORDER | SWT.VIRTUAL);
		msgComponentTable.setLinesVisible(true);
		msgComponentTable.setBounds(10, 50, 350, 400);
		msgComponentTable.addListener(SWT.SetData, new Listener(){
			@Override
			public void handleEvent(Event event) {
				TableItem tableItem = (TableItem)event.item;
				if(emcs.size() > event.index) {
					EcoreMessageComponent mc = emcs.get(event.index);
					tableItem.setText(mc.getName());
					tableItem.setData("msgComponentId", mc.getId());
					if (RegistrationStatusEnum.Registered.getStatus().equals(mc.getRegistrationStatus())) {
						tableItem.setImage(sub1);
					} else {
						tableItem.setImage(sub3);
					}
				}else {
					int a = event.index - emcs.size();
					EcoreDataType dt = edts.get(a);
					tableItem.setText(dt.getName());
					tableItem.setData("dataTypeId", dt.getId());
					tableItem.setImage(ImgUtil.createImg(identifyImageForDataType(dt.getType(),
							dt.getTrace(), dt.getRegistrationStatus())));
				}
			}
		});
		msgComponentTable.setItemCount(emcs.size() + edts.size());
		Button okButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"OK")).getButton();
		okButton.setBounds(275, 460, 35, 30);
		okButton.addListener(SWT.MouseUp, p -> {
			TableItem[] tableItemArray = msgComponentTable.getSelection();
			for (TableItem tableItem : tableItemArray) {
				typeText.setText(tableItem.getText());
				if (tableItem.getData("dataTypeId") != null) {
					typeText.setData("dataTypeId", String.valueOf(tableItem.getData("dataTypeId")));
				}
				if (tableItem.getData("msgComponentId") != null) {
					typeText.setData("msgComponentId", String.valueOf(tableItem.getData("msgComponentId")));
				}
			}
			shell.close();
		});
		Button cancelButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Cancel")).getButton();
		cancelButton.setBounds(320, 460, 52, 30);
		cancelButton.addListener(SWT.MouseUp, p -> {
			shell.close();
		});
		shell.setSize(400, 550);
		shell.open();
		shell.layout();
		searchText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				msgComponentTable.removeAll();
				emcs.stream().filter(t -> (t.getName() != null && t.getName().toUpperCase().indexOf(searchText.getText().toUpperCase()) != -1))
						.forEach(t -> {
							TableItem tableItem = new TableItem(msgComponentTable, SWT.NONE);
							tableItem.setText(t.getName());
							tableItem.setData("msgComponentId", t.getId());
							if (RegistrationStatusEnum.Registered.getStatus().equals(t.getRegistrationStatus())) {
								tableItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_COMPONENT));
							} else {
								tableItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB3_COMPONENT));
							}
						});
				edts.stream().filter(t -> (t.getName() != null && t.getName().toUpperCase().indexOf(searchText.getText().toUpperCase()) != -1))
						.forEach(t -> {
							TableItem tableItem = new TableItem(msgComponentTable, SWT.NONE);
							tableItem.setText(t.getName());
							tableItem.setData("dataTypeId", t.getId());
							tableItem.setImage(ImgUtil.createImg(
									identifyImageForDataType(t.getType(), t.getTrace(), t.getRegistrationStatus())));
						});
			}
		});
	}

	public static void createConstraintDialogue(Tree elementsTree) {
		Shell shell = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("Constraint General Information");
		shell.setLayout(new FormLayout());
		// 改变弹窗位置
		SystemUtil.center(shell);
		Composite c = new Composite(shell, SWT.NONE);
		
		Text nameText = new SummaryRowTextComposite(c, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 5, "Name")).getText();
		
		Text docText = new SummaryRowTextComposite(c, new TextPolicy(TextPolicy.TEXT_CONTENT_SCROLL_TYPE, 33, "Documentation")).getText();
		

		Button finishButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Finish")).getButton();
		finishButton.setBounds(280, 198, 60, 30);
		finishButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TreeItem treeNode = new TreeItem(elementsTree, SWT.NONE);
				treeNode.setData("name", nameText.getText());
				treeNode.setData("document", docText.getText());
				treeNode.setData("isNewConstraint", true);
				treeNode.setText("Textual" + " : " + nameText.getText());
				treeNode.setImage(ImgUtil.createImg(ImgUtil.CONSTRAINT));
				shell.close();
			}
		});
		Button cancelButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Cancel")).getButton();
		cancelButton.setBounds(350, 198, 60, 30);
		cancelButton.addListener(SWT.MouseUp, p -> {
			shell.close();
		});
		shell.setSize(510, 273);
		shell.open();
		shell.layout();
	}

	private static String identifyImageForDataType(String type, String trace, String status) {
		String imageName = "";
		switch (type) {
		case "1":
			if (trace == null) { // 可衍生的Code Set
				imageName = ImgUtil.CODE_SET_SUB1_2;
				break;
			} else { // 被衍生的Code Set
				if (RegistrationStatusEnum.Registered.getStatus().equalsIgnoreCase(status)) {
					imageName = ImgUtil.CODE_SET_SUB1_LOCK;
					break;
				} else {
					imageName = ImgUtil.CODE_SET_SUB1;
					break;
				}
			}
		case "2": // Text
			if (RegistrationStatusEnum.Registered.getStatus().equalsIgnoreCase(status)) {
				imageName = ImgUtil.TEXT_SUB1;
				break;
			} else {
				imageName = ImgUtil.TEXT_SUB1_WITHOUT_LOCK;
				break;
			}
		case "3": // Boolean
			if (RegistrationStatusEnum.Registered.getStatus().equalsIgnoreCase(status)) {
				imageName = ImgUtil.BOOLEAN_SUB1_LOCK;
				break;
			} else {
				imageName = ImgUtil.BOOLEAN_SUB1;
				break;
			}
		case "4": // Indicator
			if (RegistrationStatusEnum.Registered.getStatus().equalsIgnoreCase(status)) {
				imageName = ImgUtil.INDICATOR_SUB1;
				break;
			} else {
				imageName = ImgUtil.INDICATOR_SUB1_WITHOUT_LOCK;
				break;
			}
		case "5": // Decimal
			if (RegistrationStatusEnum.Registered.getStatus().equalsIgnoreCase(status)) {
				imageName = ImgUtil.DECIMAL_SUB1_LOCK;
				break;
			} else {
				imageName = ImgUtil.DECIMAL_SUB1;
				break;
			}
		case "6": // Rate
			if (RegistrationStatusEnum.Registered.getStatus().equalsIgnoreCase(status)) {
				imageName = ImgUtil.RATE_SUB1;
				break;
			} else {
				imageName = ImgUtil.RATE_SUB1_WITHOUT_LOCK;
				break;
			}
		case "7": // Amount
			if (RegistrationStatusEnum.Registered.getStatus().equalsIgnoreCase(status)) {
				imageName = ImgUtil.AMOUNT_SUB1;
				break;
			} else {
				imageName = ImgUtil.AMOUNT_SUB1_WITHOUT_LOCK;
				break;
			}
		case "8": // Quantity
			if (RegistrationStatusEnum.Registered.getStatus().equalsIgnoreCase(status)) {
				imageName = ImgUtil.QUANTITY_SUB1;
				break;
			} else {
				imageName = ImgUtil.QUANTITY_SUB1_WITHOUT_LOCK;
				break;
			}
		case "9": // Time
			if (RegistrationStatusEnum.Registered.getStatus().equalsIgnoreCase(status)) {
				imageName = ImgUtil.TIME_SUB1;
				break;
			} else {
				imageName = ImgUtil.TIME_SUB1_WITHOUT_LOCK;
				break;
			}
		case "10": // Binary
			if (RegistrationStatusEnum.Registered.getStatus().equalsIgnoreCase(status)) {
				imageName = ImgUtil.BINARY_SUB1;
				break;
			} else {
				imageName = ImgUtil.BINARY_SUB1_WITHOUT_LOCK;
				break;
			}
		case "11": // SchemaType
			if (RegistrationStatusEnum.Registered.getStatus().equalsIgnoreCase(status)) {
				imageName = ImgUtil.SCHEMA_TYPES_SUB1_LOCK;
				break;
			} else {
				imageName = ImgUtil.SCHEMA_TYPES_SUB1;
				break;
			}
		case "12": // UserDefined
			if (RegistrationStatusEnum.Registered.getStatus().equalsIgnoreCase(status)) {
				imageName = ImgUtil.USER_DEFINED_SUB1_LOCK;
				break;
			} else {
				imageName = ImgUtil.USER_DEFINED_SUB1;
				break;
			}
		}
		return imageName;
	}
}
