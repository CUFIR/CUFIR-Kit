package com.cfets.cufir.plugin.mr.utils;

import java.util.ArrayList;
import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.cfets.cufir.plugin.mr.enums.ObjTypeEnum;
import com.cfets.cufir.plugin.mr.enums.RegistrationStatus;
import com.cfets.cufir.plugin.mr.view.ChangeShellLocation;
import com.cfets.cufir.s.data.bean.EcoreDataType;
import com.cfets.cufir.s.data.bean.EcoreMessageBuildingBlock;
import com.cfets.cufir.s.data.bean.EcoreMessageComponent;
import com.cfets.cufir.s.data.bean.EcoreMessageDefinition;


/**
 * 报文内容子窗口选择组件用
 * @author gongyi_tt
 *
 */
public class GenerateChooseMC {

	
	public static void createMsgElementSelectionDialogue(Tree elementsTree, EcoreMessageDefinition messageDefintion) {
		Shell messageElementWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		messageElementWindow.setText("Message Element General Information");
		messageElementWindow.setLayout(new FormLayout());
		
		//改变弹窗位置
		ChangeShellLocation.center(messageElementWindow);
		
		Composite c = new Composite(messageElementWindow, SWT.NONE);
		FormData fd_c = new FormData();
		fd_c.top = new FormAttachment(0);
		fd_c.left = new FormAttachment(0);
		fd_c.bottom = new FormAttachment(100);
		fd_c.right = new FormAttachment(100);
		Label nameLabel = new Label(c, SWT.NONE);
		nameLabel.setText("Name");
		nameLabel.setBounds(10, 10, 80, 30);
		Text nameText = new Text(c, SWT.BORDER);
		nameText.setBounds(120, 10, 230, 30);
		Label documentationLabel = new Label(c, SWT.NONE);
		documentationLabel.setText("Documentation");
		documentationLabel.setBounds(10, 50, 100, 30);
		Text docText = new Text(c, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
		docText.setBounds(120, 50, 230, 80);
		Label minOccursLable = new Label(c, SWT.NONE);
		minOccursLable.setText("Min Occurs");
		minOccursLable.setBounds(10, 145, 100, 30);
		Text minText = new Text(c, SWT.BORDER);
		minText.setText("1");
		minText.setBounds(111, 145, 15, 18);
		Label maxOccursLabel = new Label(c, SWT.NONE);
		maxOccursLabel.setText("Max Occurs");
		maxOccursLabel.setBounds(145, 145, 100, 30);;
		Text maxOccursText = new Text(c, SWT.BORDER);
		maxOccursText.setText("1");
		maxOccursText.setBounds(255, 145, 15, 18);
		Label isTechnicalLabel = new Label(c, SWT.NONE);
		isTechnicalLabel.setText("Is Technical");
		isTechnicalLabel.setBounds(10, 185, 100, 30);
		Button isTechnicalButton = new Button(c, SWT.CHECK);
		isTechnicalButton.setBounds(120, 185, 10, 18);
		Label typeLabel = new Label(c, SWT.NONE);
		typeLabel.setText("Type");
		typeLabel.setBounds(10, 215, 80, 30);
		Text typeText = new Text(c, SWT.BORDER);
		typeText.setBounds(120, 215, 230, 30);
		Button typeButton = new Button(c, SWT.PUSH);
		typeButton.setText("choose");
		typeButton.setBounds(355, 215, 50, 30);
		typeButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		typeButton.addMouseListener(new GenerateChooseMC().new TypeButtonListener(typeText));
		
		Button finishButton = new Button(c, SWT.PUSH);
		finishButton.setText("Finish");
		finishButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		finishButton.setBounds(280, 265, 60, 30);
		finishButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TreeItem treeNode = new TreeItem(elementsTree, SWT.NONE);
				String nodeName;
				if (typeText.getText().isEmpty()) {
					nodeName = nameText.getText() + " [" + minText.getText() + ", " + maxOccursText.getText() + "]";
				} else {
					nodeName = nameText.getText() + " [" + minText.getText() + ", " + maxOccursText.getText() + "] : " + typeText.getText();
				}
				
				treeNode.setText(nodeName);
				EcoreMessageBuildingBlock msgBldBlock = new EcoreMessageBuildingBlock();
				msgBldBlock.setId(UUID.randomUUID().toString());
				msgBldBlock.setMessageId(messageDefintion.getId());
				msgBldBlock.setName(nameText.getText());
				msgBldBlock.setDefinition(docText.getText());
				msgBldBlock.setMinOccurs(NumberFormatUtil.getInt(minText.getText()));
				msgBldBlock.setMaxOccurs(NumberFormatUtil.getInt(maxOccursText.getText()));
				msgBldBlock.setRegistrationStatus(RegistrationStatus.Provisionally.getStatus());
				treeNode.setData("msgBldBlock", msgBldBlock);
				treeNode.setImage(ImgUtil.createImg(ImgUtil.ME));
				
				if (typeText.getData("msgComponentId") != null) {
					treeNode.setData("msgComponentId", String.valueOf(typeText.getData("msgComponentId")));
					treeNode.setData("dataType", ObjTypeEnum.MessageComponent.getType());
					msgBldBlock.setDataTypeId(String.valueOf(typeText.getData("msgComponentId")));
					msgBldBlock.setDataType("2");
					treeNode.setImage(ImgUtil.createImg(ImgUtil.ME));
					treeNode.setData("typeName", typeText.getText());
					
					// 用Message Component里的元素，构造Message Component树
					GenerateCommonTree.generateContentTreeForMessageComponent(treeNode, DerbyDaoUtil.getMessageElementList(String.valueOf(typeText.getData("msgComponentId"))),
							DerbyDaoUtil.getMessageComponentById((String)typeText.getData("msgComponentId")).getRegistrationStatus());
					
					// 当前选中的Message Component的约束
					GenerateCommonTree.addContraintsNode(treeNode, DerbyDaoUtil.getContraints(String.valueOf(typeText.getData("msgComponentId"))), typeText.getText(), null);
					
				} else if (typeText.getData("dataTypeId") != null) {
					treeNode.setData("dataTypeId", String.valueOf(typeText.getData("dataTypeId")));
					treeNode.setData("dataType", ObjTypeEnum.DataType.getType());
					msgBldBlock.setDataTypeId(String.valueOf(typeText.getData("dataTypeId")));
					msgBldBlock.setDataType("1");
					treeNode.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
					treeNode.setData("typeName", typeText.getText());
					
					// load datatype 下的 TYPE是1的 code(ECORE_CODE)
					GenerateCommonTree.generateContentTreeForDataType(treeNode, DerbyDaoUtil.getEcoreCodeListByDataTypeId(String.valueOf(typeText.getData("dataTypeId"))));
					// 当前选中的DataType的约束
					GenerateCommonTree.addContraintsNode(treeNode, DerbyDaoUtil.getContraints(String.valueOf(typeText.getData("dataTypeId"))), typeText.getText(), null);
				}
				
				messageElementWindow.close();
			}
		});
		Button cancelButton = new Button(c, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setBounds(350, 265, 60, 30);
		cancelButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		cancelButton.addMouseListener(new GenerateChooseMC().new CancelButtonListener(messageElementWindow));
		
		messageElementWindow.setSize(480, 350);
		messageElementWindow.open();
		messageElementWindow.layout();
	}
	

	class TypeButtonListener extends MouseAdapter {
		
		private Text typeText;
		
		public TypeButtonListener(Text typeText) {
			super();
			this.typeText = typeText;
		}

		@Override
		public void mouseUp(MouseEvent e) {
			createSelectMsgElementType(typeText, DerbyDaoUtil.getMessageComponentList(), DerbyDaoUtil.getAllDataType());
		}
	}
	
	
	void createSelectMsgElementType(Text typeText, ArrayList<EcoreMessageComponent> allMessageComponentList, ArrayList<EcoreDataType> allDataTypeList) {
		Shell messageComponentWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		messageComponentWindow.setText("Select Message Element type:");
		messageComponentWindow.setLayout(new FormLayout());
		
		//改变弹窗位置
		ChangeShellLocation.center(messageComponentWindow);
		
		Composite c = new Composite(messageComponentWindow, SWT.NONE);
		FormData fd_c = new FormData();
		fd_c.top = new FormAttachment(0);
		fd_c.left = new FormAttachment(0);
		fd_c.bottom = new FormAttachment(100);
		fd_c.right = new FormAttachment(100);
		Text searchText = new Text(c, SWT.BORDER);
		searchText.setBounds(10, 10, 350, 30);
		
		Table msgComponentTable = new Table(c, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
		msgComponentTable.setLinesVisible(true);
		this.generateMessageComponentTable(msgComponentTable, allMessageComponentList, allDataTypeList);
		msgComponentTable.setBounds(10, 50, 350, 400);
		
		Button okButton = new Button(c, SWT.PUSH);
		okButton.setText("OK");
		okButton.setBounds(275, 460, 35, 30);
		okButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		okButton.addMouseListener(new OkButtonListener(typeText, msgComponentTable, messageComponentWindow));
		
		Button cancelButton = new Button(c, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setBounds(320, 460, 52, 30);
		cancelButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		cancelButton.addMouseListener(new CancelButtonListener(messageComponentWindow));
		
		messageComponentWindow.setSize(400, 550);
		messageComponentWindow.open();
		messageComponentWindow.layout();
		
		searchText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				
				msgComponentTable.removeAll();
				
				allMessageComponentList.stream().filter(t -> (t.getName() != null && t.getName().toUpperCase().indexOf(searchText.getText().toUpperCase()) != -1)).forEach(t -> {
					TableItem tableItem = new TableItem(msgComponentTable, SWT.NONE);
					tableItem.setText(t.getName());
					tableItem.setData("msgComponentId", t.getId());
					if (RegistrationStatus.Registered.getStatus().equals(t.getRegistrationStatus())) {
						tableItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_COMPONENT));
					} else {
						tableItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB3_COMPONENT));
					}
				});
				allDataTypeList.stream().filter(t -> (t.getName() != null && t.getName().toUpperCase().indexOf(searchText.getText().toUpperCase()) != -1)).forEach(t -> {
					TableItem tableItem = new TableItem(msgComponentTable, SWT.NONE);
					tableItem.setText(t.getName());
					tableItem.setData("dataTypeId", t.getId());
					tableItem.setImage(ImgUtil.createImg(identifyImageForDataType(t.getType(), t.getTrace(), t.getRegistrationStatus())));
				});
			}
			
		});
	}
	
	public static void createConstraintDialogue(Tree elementsTree) {
		Shell constraintWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		constraintWindow.setText("Constraint General Information");
		constraintWindow.setLayout(new FormLayout());
		
		//改变弹窗位置
		ChangeShellLocation.center(constraintWindow);
		
		Composite c = new Composite(constraintWindow, SWT.NONE);
		FormData fd_c = new FormData();
		fd_c.top = new FormAttachment(0);
		fd_c.left = new FormAttachment(0);
		fd_c.bottom = new FormAttachment(100);
		fd_c.right = new FormAttachment(100);
		Label nameLabel = new Label(c, SWT.NONE);
		nameLabel.setText("Name");
		nameLabel.setBounds(10, 10, 80, 30);
		Text nameText = new Text(c, SWT.BORDER);
		nameText.setBounds(120, 10, 230, 30);
		Label documentationLabel = new Label(c, SWT.NONE);
		documentationLabel.setText("Documentation");
		documentationLabel.setBounds(10, 50, 100, 30);
		Text docText = new Text(c, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
		docText.setBounds(120, 50, 230, 80);
		Button finishButton = new Button(c, SWT.PUSH);
		finishButton.setText("Finish");
		finishButton.setBounds(280, 265, 60, 30);
		finishButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		finishButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TreeItem treeNode = new TreeItem(elementsTree, SWT.NONE);
				treeNode.setData("name", nameText.getText());
				treeNode.setData("document", docText.getText());
				treeNode.setData("isNewConstraint", true);
				treeNode.setText("Textual" + " : " + nameText.getText());
				treeNode.setImage(ImgUtil.createImg(ImgUtil.CONSTRAINT));
				constraintWindow.close();
			}
		});
		Button cancelButton = new Button(c, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setBounds(350, 265, 60, 30);
		cancelButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		cancelButton.addMouseListener(new GenerateChooseMC().new CancelButtonListener(constraintWindow));
		
		constraintWindow.setSize(480, 350);
		constraintWindow.open();
		constraintWindow.layout();
	}
	
class CancelButtonListener extends MouseAdapter  {
	
	private Shell shell;
	
	public CancelButtonListener(Shell shell) {
		this.shell = shell;
	}
	
	@Override
	public void mouseUp(MouseEvent e) {
		shell.close();
	}
}


class OkButtonListener extends MouseAdapter  {
	
	private Table table;
	
	private Shell shell;
	
	private Text typeText;
	
	public OkButtonListener(Text typeText, Table table, Shell shell) {
		this.typeText = typeText;
		this.table = table;
		this.shell = shell;
	}
	
	@Override
	public void mouseUp(MouseEvent e) {
		TableItem[] tableItemArray = table.getSelection();
		for (TableItem tableItem: tableItemArray) {
			this.typeText.setText(tableItem.getText());
			if (tableItem.getData("dataTypeId") != null) {
				this.typeText.setData("dataTypeId", String.valueOf(tableItem.getData("dataTypeId")));
			}
			if (tableItem.getData("msgComponentId") != null) {
				this.typeText.setData("msgComponentId", String.valueOf(tableItem.getData("msgComponentId")));
			}
			
		}
		shell.close();
	}
}


private void generateMessageComponentTable(Table msgComponentTable,
		ArrayList<EcoreMessageComponent> allMessageComponentList, ArrayList<EcoreDataType> allDataTypeList) {
	for (EcoreMessageComponent msgComponent: allMessageComponentList) {
		TableItem tableItem = new TableItem(msgComponentTable, SWT.NONE);
		tableItem.setText(msgComponent.getName());
		tableItem.setData("msgComponentId", msgComponent.getId());
		if (RegistrationStatus.Registered.getStatus().equals(msgComponent.getRegistrationStatus())) {
			tableItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_COMPONENT));
		} else {
			tableItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB3_COMPONENT));
		}
		
	}
	
	for (EcoreDataType ecoreDataType: allDataTypeList) {
		TableItem tableItem = new TableItem(msgComponentTable, SWT.NONE);
		tableItem.setText(ecoreDataType.getName());
		tableItem.setData("dataTypeId", ecoreDataType.getId());
		tableItem.setImage(ImgUtil.createImg(this.identifyImageForDataType(ecoreDataType.getType(), ecoreDataType.getTrace(), ecoreDataType.getRegistrationStatus())));
	}
	msgComponentTable.setSelection(0);
}


private String identifyImageForDataType(String type, String trace, String status) {
	String imageName = "";
	switch (type) {
		case "1": 
			if (trace == null) { //可衍生的Code Set
				imageName = ImgUtil.CODE_SET_SUB1_2;
				break;
			} else { //被衍生的Code Set
				if (RegistrationStatus.Registered.getStatus().equalsIgnoreCase(status)) {
					imageName = ImgUtil.CODE_SET_SUB1_LOCK;
					break;
				} else {
					imageName = ImgUtil.CODE_SET_SUB1;
					break;
				}				
			}
		case "2": // Text
			if (RegistrationStatus.Registered.getStatus().equalsIgnoreCase(status)) {
				imageName = ImgUtil.TEXT_SUB1;
				break;
			} else {
				imageName = ImgUtil.TEXT_SUB1_WITHOUT_LOCK;
				break;
			}
		case "3":	// Boolean
			if (RegistrationStatus.Registered.getStatus().equalsIgnoreCase(status)) {
				imageName = ImgUtil.BOOLEAN_SUB1_LOCK;
				break;
			} else {
				imageName = ImgUtil.BOOLEAN_SUB1;
				break;
			}
		case "4": 	// Indicator
			if (RegistrationStatus.Registered.getStatus().equalsIgnoreCase(status)) {
				imageName = ImgUtil.INDICATOR_SUB1;
				break;
			} else {
				imageName = ImgUtil.INDICATOR_SUB1_WITHOUT_LOCK;
				break;
			}
		case "5":	// Decimal
			if (RegistrationStatus.Registered.getStatus().equalsIgnoreCase(status)) {
				imageName = ImgUtil.DECIMAL_SUB1_LOCK;
				break;
			} else {
				imageName = ImgUtil.DECIMAL_SUB1;
				break;
			}
		case "6":	// Rate
			if (RegistrationStatus.Registered.getStatus().equalsIgnoreCase(status)) {
				imageName = ImgUtil.RATE_SUB1;
				break;
			} else {
				imageName = ImgUtil.RATE_SUB1_WITHOUT_LOCK;
				break;
			}
		case "7":	// Amount
			if (RegistrationStatus.Registered.getStatus().equalsIgnoreCase(status)) {
				imageName = ImgUtil.AMOUNT_SUB1;
				break;
			} else {
				imageName = ImgUtil.AMOUNT_SUB1_WITHOUT_LOCK;
				break;
			}
		case "8":	// Quantity
			if (RegistrationStatus.Registered.getStatus().equalsIgnoreCase(status)) {
				imageName = ImgUtil.QUANTITY_SUB1;
				break;
			} else {
				imageName = ImgUtil.QUANTITY_SUB1_WITHOUT_LOCK;
				break;
			}
		case "9":	// Time
			if (RegistrationStatus.Registered.getStatus().equalsIgnoreCase(status)) {
				imageName = ImgUtil.TIME_SUB1;
				break;
			} else {
				imageName = ImgUtil.TIME_SUB1_WITHOUT_LOCK;
				break;
			}
		case "10":	// Binary
			if (RegistrationStatus.Registered.getStatus().equalsIgnoreCase(status)) {
				imageName = ImgUtil.BINARY_SUB1;
				break;
			} else {
				imageName = ImgUtil.BINARY_SUB1_WITHOUT_LOCK;
				break;
			}
		case "11":	// SchemaType
			if (RegistrationStatus.Registered.getStatus().equalsIgnoreCase(status)) {
				imageName = ImgUtil.SCHEMA_TYPES_SUB1_LOCK;
				break;
			} else {
				imageName = ImgUtil.SCHEMA_TYPES_SUB1;
				break;
			}
		case "12":	// UserDefined
			if (RegistrationStatus.Registered.getStatus().equalsIgnoreCase(status)) {
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
