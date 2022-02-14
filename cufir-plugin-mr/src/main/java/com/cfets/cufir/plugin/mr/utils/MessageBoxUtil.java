package com.cfets.cufir.plugin.mr.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.cfets.cufir.plugin.mr.bean.TreeItemDataBean;
import com.cfets.cufir.plugin.mr.bean.TreeListItem;
import com.cfets.cufir.plugin.mr.enums.DataTypesEnum;
import com.cfets.cufir.plugin.mr.enums.RegistrationStatus;
import com.cfets.cufir.plugin.mr.enums.TreeLevelEnum;
import com.cfets.cufir.plugin.mr.enums.TreeParentEnum;
import com.cfets.cufir.plugin.mr.view.ChangeShellLocation;
import com.cfets.cufir.s.data.bean.EcoreBusinessComponentRL;
import com.cfets.cufir.s.data.bean.EcoreBusinessElement;
import com.cfets.cufir.s.data.bean.EcoreConstraint;
import com.cfets.cufir.s.data.bean.EcoreExample;
import com.cfets.cufir.s.data.dao.EcoreBusinessComponentDao;
import com.cfets.cufir.s.data.dao.EcoreBusinessComponentRLDao;
import com.cfets.cufir.s.data.dao.EcoreDataTypeDao;
import com.cfets.cufir.s.data.dao.EcoreMessageComponentDao;
import com.cfets.cufir.s.data.dao.impl.EcoreBusinessComponentDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreBusinessComponentRLDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreDataTypeDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreMessageComponentDaoImpl;
import com.cfets.cufir.s.data.vo.EcoreTreeNode;

/**
 * 消息框
 * 
 * @author jin.c.li
 *
 */
public class MessageBoxUtil {
	private static EcoreDataTypeDao ecoreDataTypeDao = new EcoreDataTypeDaoImpl();
	private static EcoreMessageComponentDao ecoreMessageComponentDao = new EcoreMessageComponentDaoImpl();
	private static EcoreBusinessComponentDao businessComponentDao = new EcoreBusinessComponentDaoImpl();
	private static EcoreBusinessComponentRLDao businessComponentRLDao = new EcoreBusinessComponentRLDaoImpl();
	
	static java.util.List<EcoreTreeNode> allEcoreDataTypes = new ArrayList<EcoreTreeNode>();
	static java.util.List<EcoreTreeNode> businessComponentList = new ArrayList<EcoreTreeNode>();
//	static EcoreTreeNode businessComponentSubTypeList = new EcoreTreeNode();
	
	/**
	 * 删除失败提示
	 */
	public static void deleteFail() {
		Shell parent = new Shell(Display.getCurrent());
		MessageBox messageBox = new MessageBox(parent, SWT.OK | SWT.CANCEL);
		messageBox.setMessage("删除失败！");
		messageBox.open();
	}
	
	public static void showMessageBox(String message) {
		Shell parent = new Shell(Display.getCurrent());
		MessageBox messageBox = new MessageBox(parent, SWT.OK);
		messageBox.setMessage(message);
		messageBox.open();
	}
	
	/**
	 * 加载example
	 */
	public static void createExampleDialogue(List exampleList, boolean... isMe) {
		Shell exampleWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		exampleWindow.setText("Enter a value for Examples");
		exampleWindow.setLayout(new FormLayout());
		Composite c = new Composite(exampleWindow, SWT.NONE);
		FormData fd_c = new FormData();
		fd_c.top = new FormAttachment(0);
		fd_c.left = new FormAttachment(0);
		fd_c.bottom = new FormAttachment(100);
		fd_c.right = new FormAttachment(100);

		Text nameText = new Text(c, SWT.BORDER);
		nameText.setBounds(10, 10, 350, 30);

		Button finishButton = new Button(c, SWT.PUSH);
		finishButton.setText("OK");
		finishButton.setBounds(280, 45, 60, 30);
		finishButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		finishButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				exampleList.add(nameText.getText());
				exampleWindow.close();
			}
		});
		Button cancelButton = new Button(c, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setBounds(350, 45, 60, 30);
		cancelButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				exampleWindow.close();
			}
		});

		exampleWindow.setBounds((Display.getDefault().getBounds().width - 480) / 2,
				(Display.getDefault().getBounds().height - 120) / 2, 480, 120);
		exampleWindow.open();
		exampleWindow.layout();
	}

	
	public static void createExampleDialogue(Table exampleTable) {
		Shell exampleWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		exampleWindow.setText("Enter a value for Examples");
		exampleWindow.setLayout(new FormLayout());
		Composite c = new Composite(exampleWindow, SWT.NONE);
		FormData fd_c = new FormData();
		fd_c.top = new FormAttachment(0);
		fd_c.left = new FormAttachment(0);
		fd_c.bottom = new FormAttachment(100);
		fd_c.right = new FormAttachment(100);

		Text nameText = new Text(c, SWT.BORDER);
		nameText.setBounds(10, 10, 350, 30);

		Button finishButton = new Button(c, SWT.PUSH);
		finishButton.setText("OK");
		finishButton.setBounds(280, 45, 60, 30);
		finishButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		finishButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TableItem tableItem = new TableItem(exampleTable, SWT.NONE);
				tableItem.setText(nameText.getText());
				EcoreExample ecoreExample = new EcoreExample();
				ecoreExample.setExample(nameText.getText());
				ecoreExample.setId(UUID.randomUUID().toString());
				tableItem.setData(ecoreExample);
				exampleWindow.close();
			}
		});
		Button cancelButton = new Button(c, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		cancelButton.setBounds(350, 45, 60, 30);
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				exampleWindow.close();
			}
		});

		exampleWindow.setBounds((Display.getDefault().getBounds().width - 480) / 2,
				(Display.getDefault().getBounds().height - 120) / 2, 480, 120);
		exampleWindow.open();
		exampleWindow.layout();
	}
	
	/**
	 * 加载namespaceList
	 */
	public static void createNamespaceListDialogue(List namespaceList) {
		Shell namespaceWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		namespaceWindow.setText("Enter a value for Namespace List");
		namespaceWindow.setLayout(new FormLayout());
		Composite c = new Composite(namespaceWindow, SWT.NONE);
		FormData fd_c = new FormData();
		fd_c.top = new FormAttachment(0);
		fd_c.left = new FormAttachment(0);
		fd_c.bottom = new FormAttachment(100);
		fd_c.right = new FormAttachment(100);

		Text nameText = new Text(c, SWT.BORDER);
		nameText.setBounds(10, 10, 350, 30);

		Button finishButton = new Button(c, SWT.PUSH);
		finishButton.setText("OK");
		finishButton.setBounds(280, 45, 60, 30);
		finishButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		finishButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				namespaceList.add(nameText.getText());
				namespaceWindow.close();
			}
		});
		Button cancelButton = new Button(c, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setBounds(350, 45, 60, 30);
		cancelButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				namespaceWindow.close();
			}
		});

		namespaceWindow.setBounds((Display.getDefault().getBounds().width - 480) / 2,
				(Display.getDefault().getBounds().height - 120) / 2, 480, 120);
		namespaceWindow.open();
		namespaceWindow.layout();
	}

	/**
	 * 创建表格对话框
	 * 
	 * @param beSynonymsTable
	 */
	public static void createSynonymsDialogue(Table beSynonymsTable, Object... selectedTableItem) {
		Shell synonymsWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		synonymsWindow.setText("Enter values for Synonym");
		synonymsWindow.setLayout(new FormLayout());
		Composite c = new Composite(synonymsWindow, SWT.NONE);
		FormData fd_c = new FormData();
		fd_c.top = new FormAttachment(0);
		fd_c.left = new FormAttachment(0);
		fd_c.bottom = new FormAttachment(100);
		fd_c.right = new FormAttachment(100);

		Label contextLabel = new Label(c, SWT.NONE);
		contextLabel.setText("Context");
		contextLabel.setBounds(10, 10, 80, 30);
		Text nameText = new Text(c, SWT.BORDER);
		nameText.setBounds(120, 10, 200, 30);

		Label synonymsLabel = new Label(c, SWT.NONE);
		synonymsLabel.setText("Synonyms");
		synonymsLabel.setBounds(10, 50, 80, 30);
		Text synonymsText = new Text(c, SWT.BORDER);
		synonymsText.setBounds(120, 50, 200, 30);

		Button finishButton = new Button(c, SWT.PUSH);
		finishButton.setText("OK");
		finishButton.setBounds(90, 100, 60, 30);
		finishButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		finishButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TableItem tableItem = new TableItem(beSynonymsTable, SWT.NONE);
				tableItem.setText(new String[] { nameText.getText(), synonymsText.getText() });

				if (selectedTableItem != null && selectedTableItem.length > 0) {
					TableItem[] items = beSynonymsTable.getItems();
					java.util.List<String[]> textList = new ArrayList<String[]>();
					for (int i = 0; i < items.length; i++) {
						textList.add(new String[] { items[i].getText(0), items[i].getText(1), items[i].getText(2),
								items[i].getText(3) });
					}
					if(selectedTableItem[0] instanceof TreeItem) {
						((TreeItem)selectedTableItem[0]).setData("beSynonymsTableItems", textList);
					} else if(selectedTableItem[0] instanceof TableItem){
						((TableItem)selectedTableItem[0]).setData("beSynonymsTableItems", textList);
					}
				}

				synonymsWindow.close();
			}
		});
		Button cancelButton = new Button(c, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setBounds(180, 100, 60, 30);
		cancelButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				synonymsWindow.close();
			}
		});

		synonymsWindow.setBounds((Display.getDefault().getBounds().width - 380) / 2,
				(Display.getDefault().getBounds().height - 200) / 2, 380, 200);
		synonymsWindow.open();
		synonymsWindow.layout();
	}

	/**
	 * 删除synonyms
	 * 
	 * @param exampleList
	 */
	public static void deleteSynonymDialogue(Table beSynonymsTable, Object... selectedTableItem) {
		// 删除操作
		TableItem[] selection = beSynonymsTable.getSelection();
		if (selection != null && selection.length > 0) {
			selection[0].dispose();

			if (selectedTableItem != null && selectedTableItem.length > 0) {
				TableItem[] items = beSynonymsTable.getItems();
				java.util.List<String[]> textList = new ArrayList<String[]>();
				for (int i = 0; i < items.length; i++) {
					textList.add(new String[] { items[i].getText(0), items[i].getText(1), items[i].getText(2),
							items[i].getText(3) });
				}
				if(selectedTableItem[0] instanceof TreeItem) {
					((TreeItem)selectedTableItem[0]).setData("beSynonymsTableItems", textList);
				} else if(selectedTableItem[0] instanceof TableItem){
					((TableItem)selectedTableItem[0]).setData("beSynonymsTableItems", textList);
				}
			}
		}
	}

	/**
	 * 删除example
	 * 
	 * @param exampleList
	 */
	public static void deleteExampleDialogue(List exampleList) {
		// 删除操作
		String[] selection = exampleList.getSelection();
		if (selection != null && selection.length > 0) {
			exampleList.remove(exampleList.getSelectionIndex());
		}
	}
	
	public static void deleteExampleDialogue(Table exampleTable) {
		// 删除操作
		for (TableItem tableItem: exampleTable.getSelection()) {
			tableItem.dispose();
		}
	}

	/**
	 * 加载Constraint
	 * 
	 * @param type    = edit编辑， type = add新增
	 * @param tabType = 1 有交互，tabType = 2无交互
	 */
	public static void createConstraintDialogue(String type, String tabType, Table constraintsTable,
			Tree constraintsTree, String dataType, TreeItem... selectedTreeItem) {
		Shell constraintWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		constraintWindow.setText("Constraint General Information");
		constraintWindow.setLayout(new FormLayout());
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
		nameText.setBounds(150, 10, 230, 30);

		Label documentationLabel = new Label(c, SWT.NONE);
		documentationLabel.setText("Documentation");
		documentationLabel.setBounds(10, 50, 100, 30);
		Text docText = new Text(c, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
		docText.setBounds(150, 50, 230, 80);

		Label expressionLabel = new Label(c, SWT.NONE);
		expressionLabel.setText("Expression");
		expressionLabel.setBounds(10, 140, 100, 30);
		Text expressionText = new Text(c, SWT.BORDER);
		expressionText.setBounds(150, 140, 230, 30);

		Label expressionLanguageLabel = new Label(c, SWT.NONE);
		expressionLanguageLabel.setText("Expression Language");
		expressionLanguageLabel.setBounds(10, 180, 100, 30);
		Text expressionLanguageText = new Text(c, SWT.BORDER);
		expressionLanguageText.setBounds(150, 180, 230, 30);

		/**
		 * 编辑的时候触发回显
		 */
		if (type == "edit") {
			TableItem[] tableItems = constraintsTable.getSelection();
			if (tableItems != null && tableItems.length > 0) {
				nameText.setText(tableItems[0].getText(0));
				docText.setText(tableItems[0].getText(1));
				expressionText.setText(tableItems[0].getText(2));
				expressionLanguageText.setText(tableItems[0].getText(3));
			}
		}

		Button finishButton = new Button(c, SWT.PUSH);
		finishButton.setText("Finish");
		finishButton.setBounds(280, 265, 60, 30);
		finishButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		finishButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (tabType == "1") {
					TableItem tableItem = new TableItem(constraintsTable, SWT.NONE);
					tableItem.setText(new String[] { nameText.getText(), docText.getText(), expressionText.getText(),
							expressionLanguageText.getText() });

					/**
					 * 编辑的时候先删除，后添加
					 */
					if (type == "edit") {
						TableItem[] tableItems = constraintsTable.getSelection();
						if (tableItems != null && tableItems.length > 0) {
							tableItems[0].dispose();
						}
					}

					if (selectedTreeItem != null && selectedTreeItem.length > 0) {
						TableItem[] items = constraintsTable.getItems();
						java.util.List<String[]> textList = new ArrayList<String[]>();
						for (int i = 0; i < items.length; i++) {
							textList.add(new String[] { items[i].getText(0), items[i].getText(1), items[i].getText(2),
									items[i].getText(3) });
						}
						selectedTreeItem[0].setData("constraintsTableItems", textList);
					}

					// 添加交互
					exchange(constraintsTable, constraintsTree, dataType);

				} else if (tabType == "2") {
					TreeItem treeItem = new TreeItem(constraintsTree, SWT.NONE);
					treeItem.setText(nameText.getText());
					treeItem.setImage(ImgUtil.createImg(ImgUtil.CONSTRAINT));
					treeItem.setData("contentNameText", nameText.getText());
					treeItem.setData("contentDocText", docText.getText());
					treeItem.setData("contentExpressionText", expressionText.getText());
					treeItem.setData("contentExpressionLanguageText", expressionLanguageText.getText());
				}

				constraintWindow.close();
			}
		});
		Button cancelButton = new Button(c, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setBounds(350, 265, 60, 30);
		cancelButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				constraintWindow.close();
			}
		});

		constraintWindow.setBounds((Display.getDefault().getBounds().width - 480) / 2,
				(Display.getDefault().getBounds().height - 350) / 2, 480, 350);
		constraintWindow.open();
		constraintWindow.layout();
	}

	/**
	 * 加载Constraint
	 * 
	 * @param type    = edit编辑， type = add新增
	 * @param tabType = 1 有交互，tabType = 2无交互
	 */
	public static void createConstraintDialogueForBC(String type, Table constraintsTable,
			Object... selectedTableItem) {
		Shell constraintWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		constraintWindow.setText("Constraint General Information");
		constraintWindow.setLayout(new FormLayout());
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
		nameText.setBounds(150, 10, 230, 30);

		Label documentationLabel = new Label(c, SWT.NONE);
		documentationLabel.setText("Documentation");
		documentationLabel.setBounds(10, 50, 100, 30);
		Text docText = new Text(c, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
		docText.setBounds(150, 50, 230, 80);

		Label expressionLabel = new Label(c, SWT.NONE);
		expressionLabel.setText("Expression");
		expressionLabel.setBounds(10, 140, 100, 30);
		Text expressionText = new Text(c, SWT.BORDER);
		expressionText.setBounds(150, 140, 230, 30);

		Label expressionLanguageLabel = new Label(c, SWT.NONE);
		expressionLanguageLabel.setText("Expression Language");
		expressionLanguageLabel.setBounds(10, 180, 100, 30);
		Text expressionLanguageText = new Text(c, SWT.BORDER);
		expressionLanguageText.setBounds(150, 180, 230, 30);

		/**
		 * 编辑的时候触发回显
		 */
		if (type == "edit") {
			TableItem[] tableItems = constraintsTable.getSelection();
			if (tableItems != null && tableItems.length > 0) {
				nameText.setText(tableItems[0].getText(0));
				docText.setText(tableItems[0].getText(1));
				expressionText.setText(tableItems[0].getText(2));
				expressionLanguageText.setText(tableItems[0].getText(3));
			}
		}

		Button finishButton = new Button(c, SWT.PUSH);
		finishButton.setText("Finish");
		finishButton.setBounds(280, 265, 60, 30);
		finishButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		finishButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TableItem tableItem = new TableItem(constraintsTable, SWT.NONE);
				tableItem.setText(new String[] { nameText.getText(), docText.getText(), expressionText.getText(),
						expressionLanguageText.getText() });

				/**
				 * 编辑的时候先删除，后添加
				 */
				if (type == "edit") {
					TableItem[] tableItems = constraintsTable.getSelection();
					if (tableItems != null && tableItems.length > 0) {
						tableItems[0].dispose();
					}
				}

				if (selectedTableItem != null && selectedTableItem.length > 0) {
					TableItem[] items = constraintsTable.getItems();
					java.util.List<String[]> textList = new ArrayList<String[]>();
					for (int i = 0; i < items.length; i++) {
						textList.add(new String[] { items[i].getText(0), items[i].getText(1), items[i].getText(2),
								items[i].getText(3) });
					}
					if(selectedTableItem[0] instanceof TreeItem) {
						((TreeItem)selectedTableItem[0]).setData("beConstraintsTableItems", textList);
					} else if(selectedTableItem[0] instanceof TableItem){
						((TableItem)selectedTableItem[0]).setData("beConstraintsTableItems", textList);
					}
				}

				constraintWindow.close();
			}
		});
		Button cancelButton = new Button(c, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setBounds(350, 265, 60, 30);
		cancelButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				constraintWindow.close();
			}
		});

		constraintWindow.setBounds((Display.getDefault().getBounds().width - 480) / 2,
				(Display.getDefault().getBounds().height - 350) / 2, 480, 350);
		constraintWindow.open();
		constraintWindow.layout();
	}

	/**
	 * 删除Constraint
	 * 
	 * @param tabType = 1 有交互，tabType = 2无交互
	 */
	public static void deleteConstraintForBC(Table constraintsTable, Object... selectedTableItem) {
		// 删除操作
		TableItem[] selection = constraintsTable.getSelection();
		if (selection != null && selection.length > 0) {
			TableItem item = selection[0];

			if (selectedTableItem != null && selectedTableItem.length > 0) {
				TableItem[] items = constraintsTable.getItems();
				java.util.List<String[]> textList = new ArrayList<String[]>();
				for (int i = 0; i < items.length; i++) {
					textList.add(new String[] { items[i].getText(0), items[i].getText(1), items[i].getText(2),
							items[i].getText(3) });
				}
				if(selectedTableItem[0] instanceof TreeItem) {
					((TreeItem)selectedTableItem[0]).setData("beConstraintsTableItems", textList);
				} else if(selectedTableItem[0] instanceof TableItem){
					((TableItem)selectedTableItem[0]).setData("beConstraintsTableItems", textList);
				}
			}

			item.dispose();
		}
	}

	/**
	 * 删除Constraint
	 * 
	 * @param tabType = 1 有交互，tabType = 2无交互
	 */
	public static void deleteConstraint(String tabType, Table constraintsTable, Tree constraintsTree, String dataType,
			TreeItem... selectedTreeItem) {

		if (tabType == "1") {
			// 删除操作
			TableItem[] selection = constraintsTable.getSelection();
			if (selection != null && selection.length > 0) {
				TableItem item = selection[0];

				if (selectedTreeItem != null && selectedTreeItem.length > 0) {
					TableItem[] items = constraintsTable.getItems();
					java.util.List<String[]> textList = new ArrayList<String[]>();
					for (int i = 0; i < items.length; i++) {
						textList.add(new String[] { items[i].getText(0), items[i].getText(1), items[i].getText(2),
								items[i].getText(3) });
					}
					selectedTreeItem[0].setData("constraintsTableItems", textList);
				}

				item.dispose();
			}

			// 添加交互
			exchange(constraintsTable, constraintsTree, dataType);

		} else if (tabType == "2") {
			if (selectedTreeItem != null && selectedTreeItem.length > 0) {
				selectedTreeItem[0].dispose();
			}
		}
	}

	/**
	 * 交互的情况
	 * 
	 * @param summaryConstraintsTable
	 * @param newCodeTree
	 * @param dataType
	 */
	public static void exchange(Table summaryConstraintsTable, Tree newCodeTree, String dataType) {
		// dataType里面非codeset的，跟后面的content有交互情况
		if (dataType.equals(DataTypesEnum.TEXT.getName()) || dataType.equals(DataTypesEnum.BOOLEAN.getName())
				|| dataType.equals(DataTypesEnum.INDICATOR.getName())
				|| dataType.equals(DataTypesEnum.DECIMAL.getName()) || dataType.equals(DataTypesEnum.RATE.getName())
				|| dataType.equals(DataTypesEnum.AMOUNT.getName()) || dataType.equals(DataTypesEnum.QUANTITY.getName())
				|| dataType.equals(DataTypesEnum.TIME.getName()) || dataType.equals(DataTypesEnum.BINARY.getName())
				|| dataType.equals(DataTypesEnum.SCHEMA_TYPES.getName())
				|| dataType.equals(DataTypesEnum.USER_DEFINED.getName())) {
			TableItem[] constraintItems = summaryConstraintsTable.getItems();
			newCodeTree.removeAll();
			if (constraintItems != null && constraintItems.length > 0) {
				for (TableItem tableItem : constraintItems) {
					TreeItem treeItem = new TreeItem(newCodeTree, SWT.NONE);
					treeItem.setText(tableItem.getText(0));
					treeItem.setImage(ImgUtil.createImg(ImgUtil.CONSTRAINT));
					treeItem.setData("contentNameText", tableItem.getText(0));
					treeItem.setData("contentDocText", tableItem.getText(1));
					treeItem.setData("contentExpressionText", tableItem.getText(2));
					treeItem.setData("contentExpressionLanguageText", tableItem.getText(3));
				}
			}
		}
	}
	
	/**
	 * 加载Constraint
	 */
	public static void createConstraintDialogueForMC(Tree constraintsTree) {
		Shell constraintWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		constraintWindow.setText("Constraint General Information");
		constraintWindow.setLayout(new FormLayout());
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
		nameText.setBounds(150, 10, 230, 30);

		Label documentationLabel = new Label(c, SWT.NONE);
		documentationLabel.setText("Documentation");
		documentationLabel.setBounds(10, 50, 100, 30);
		Text docText = new Text(c, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
		docText.setBounds(150, 50, 230, 80);

		Label expressionLabel = new Label(c, SWT.NONE);
		expressionLabel.setText("Expression");
		expressionLabel.setBounds(10, 140, 100, 30);
		Text expressionText = new Text(c, SWT.BORDER);
		expressionText.setBounds(150, 140, 230, 30);

		Label expressionLanguageLabel = new Label(c, SWT.NONE);
		expressionLanguageLabel.setText("Expression Language");
		expressionLanguageLabel.setBounds(10, 180, 100, 30);
		Text expressionLanguageText = new Text(c, SWT.BORDER);
		expressionLanguageText.setBounds(150, 180, 230, 30);

		Button finishButton = new Button(c, SWT.PUSH);
		finishButton.setText("Finish");
		finishButton.setBounds(280, 265, 60, 30);
		finishButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		finishButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TreeItem treeItem = new TreeItem(constraintsTree, SWT.NONE);
				//type = 2 ---> constraint
//				treeItem.setData("type", 2);
				treeItem.setText(nameText.getText());
				treeItem.setImage(ImgUtil.createImg(ImgUtil.CONSTRAINT));
				treeItem.setData("contentNameText", nameText.getText());
				treeItem.setData("contentDocText", docText.getText());
				treeItem.setData("contentExpressionText", expressionText.getText());
				treeItem.setData("contentExpressionLanguageText", expressionLanguageText.getText());
				constraintWindow.close();
			}
		});
		Button cancelButton = new Button(c, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setBounds(350, 265, 60, 30);
		cancelButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				constraintWindow.close();
			}
		});

		constraintWindow.setBounds((Display.getDefault().getBounds().width - 480) / 2,
				(Display.getDefault().getBounds().height - 350) / 2, 480, 350);
		constraintWindow.open();
		constraintWindow.layout();
	}
	
	public static void deleteTreeItemForMC(Tree constraintsTree) {
		if(constraintsTree.getSelectionCount() > 0) {
			TreeItem treeItem = constraintsTree.getSelection()[0];
			treeItem.dispose();
			
		}
	}

	/**
	 * 打开搜索框
	 */
	public static void createSearchDialogue(Text text,TreeItem treeItem) {
		try {
			businessComponentList = businessComponentDao.findAllEcoreBusinessComponents();
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		try {
			allEcoreDataTypes = ecoreDataTypeDao.findAllEcoreDataTypes();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Shell synonymsWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		synonymsWindow.setSize(400, 600);
		synonymsWindow.setText("select type");

		//改变弹窗位置
		ChangeShellLocation.center(synonymsWindow);
		
		Composite parentComp = new Composite(synonymsWindow, SWT.NONE);
		parentComp.setBounds(0, 0, 400, 600);

		Combo cb = new Combo(parentComp, SWT.READ_ONLY);
		cb.add("Basic types");
		cb.add("Complex types");
		cb.setBounds(20, 15, 350, 30);

		Tree tree = new Tree(parentComp, SWT.V_SCROLL);
		tree.setBounds(20, 85, 350, 400);
		
		Text seachText = new Text(parentComp, SWT.BORDER);
		seachText.setBounds(20, 50, 270, 30);
		
		cb.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				tree.removeAll();
				if (cb.getText().equals("Basic types")) {
					allEcoreDataTypes.stream()
					.filter(t -> (t.getName() != null
							&& t.getName().toUpperCase().indexOf(seachText.getText().toUpperCase()) != -1))
					.forEach(t -> {
						TreeItem item = new TreeItem(tree, SWT.NONE);
						item.setText(t.getName() == null ? "" : t.getName());
						item.setData("id", t.getId());
						item.setData("beType", t.getType());
						item.setData("status", t.getRegistrationStatus());
						if (t != null && t.getType() != null) {
							if (DataTypesEnum.CODE_SETS.getType().equals(t.getType())) {
								if ("1".equals(t.getObjType())) {
									item.setImage(ImgUtil.createImg(ImgUtil.CODE_SET_SUB1_2));
								} else {
									item.setImage(ImgUtil.createImg(ImgUtil.CODE_SET_SUB1));
								}
							} else if (DataTypesEnum.TEXT.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.TEXT_SUB1));
							} else if (DataTypesEnum.BOOLEAN.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.BOOLEAN_SUB1));
							} else if (DataTypesEnum.INDICATOR.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.INDICATOR_SUB1));
							} else if (DataTypesEnum.DECIMAL.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.DECIMAL_SUB1));
							} else if (DataTypesEnum.RATE.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.RATE_SUB1));
							} else if (DataTypesEnum.AMOUNT.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.AMOUNT_SUB1));
							} else if (DataTypesEnum.QUANTITY.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.QUANTITY_SUB1));
							} else if (DataTypesEnum.TIME.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.TIME_SUB1));
							} else if (DataTypesEnum.BINARY.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.BINARY_SUB1));
							} else if (DataTypesEnum.SCHEMA_TYPES.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.SCHEMA_TYPES_SUB1));
							} else if (DataTypesEnum.USER_DEFINED.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.USER_DEFINED_SUB1));
							}
						}
					});
				} else if (cb.getText().equals("Complex types")) {
					businessComponentList.stream()
					.filter(t -> (t.getName() != null
							&& t.getName().toUpperCase().indexOf(seachText.getText().toUpperCase()) != -1))
					.forEach(t -> {
						TreeItem item = new TreeItem(tree, SWT.NONE);
						item.setText(t.getName() == null ? "" : t.getName());
						item.setData("id", t.getId());
						item.setData("beType", t.getType());
						item.setData("status", t.getRegistrationStatus());
						item.setImage(ImgUtil.createImg(ImgUtil.BC));
					});
				}
			}
		});

		
		Button seachBtn = new Button(parentComp, SWT.NONE);
		seachBtn.setBounds(300, 50, 60, 30);
		seachBtn.setText("Search");
		seachBtn.setBackground(new Color(Display.getCurrent(),135,206,250));
		seachBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 移除全部
				tree.removeAll();
				String text = seachText.getText();
				if (cb.getText().equals("Basic types")) {
					allEcoreDataTypes.forEach(t -> {
						TreeItem item = new TreeItem(tree, SWT.NONE);
						item.setText(t.getName() == null ? "" : t.getName());
						item.setData("id", t.getId());
						item.setData("beType", t.getType());
						item.setData("status", t.getRegistrationStatus());
						if (t != null && t.getType() != null) {
							if (DataTypesEnum.CODE_SETS.getType().equals(t.getType())) {
								if ("1".equals(t.getObjType())) {
									item.setImage(ImgUtil.createImg(ImgUtil.CODE_SET_SUB1_2));
								} else {
									item.setImage(ImgUtil.createImg(ImgUtil.CODE_SET_SUB1));
								}
							} else if (DataTypesEnum.TEXT.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.TEXT_SUB1));
							} else if (DataTypesEnum.BOOLEAN.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.BOOLEAN_SUB1));
							} else if (DataTypesEnum.INDICATOR.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.INDICATOR_SUB1));
							} else if (DataTypesEnum.DECIMAL.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.DECIMAL_SUB1));
							} else if (DataTypesEnum.RATE.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.RATE_SUB1));
							} else if (DataTypesEnum.AMOUNT.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.AMOUNT_SUB1));
							} else if (DataTypesEnum.QUANTITY.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.QUANTITY_SUB1));
							} else if (DataTypesEnum.TIME.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.TIME_SUB1));
							} else if (DataTypesEnum.BINARY.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.BINARY_SUB1));
							} else if (DataTypesEnum.SCHEMA_TYPES.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.SCHEMA_TYPES_SUB1));
							} else if (DataTypesEnum.USER_DEFINED.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.USER_DEFINED_SUB1));
							}
						}
					});
				} else if (cb.getText().equals("Complex types")) {
					tree.removeAll();
					businessComponentList.forEach(t -> {
						TreeItem item = new TreeItem(tree, SWT.NONE);
						item.setText(t.getName() == null ? "" : t.getName());
						item.setData("id", t.getId());
						item.setData("beType", t.getType());
						item.setData("status", t.getRegistrationStatus());
						item.setImage(ImgUtil.createImg(ImgUtil.BC));
					});
				}
			}
		});
		
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] selection = tree.getSelection();
				seachText.setText(selection[0].getText());
			}
		});

		Button okBtn = new Button(parentComp, SWT.None);
		okBtn.setBounds(180, 510, 80, 30);
		okBtn.setText("choose");
		okBtn.setBackground(new Color(Display.getCurrent(),135,206,250));
		okBtn.addListener(SWT.MouseDown, p -> {
			TreeItem[] selection = tree.getSelection();
			for (TreeItem Item : selection) {
			text.setData("beType_id", Item.getData("id"));
			text.setData("beType", Item.getData("beType"));
			text.setData("beStatusCombo", Item.getData("status"));
			text.setData("meLever", 1);
			text.setText(Item.getText());
			}
			treeItem.setData("beType_id",text.getData("beType_id"));
			treeItem.setData("beType",text.getData("beType"));
			treeItem.setData("beStatusCombo",text.getData("beStatusCombo"));
			TreeItem[] items = treeItem.getItems();
			for (TreeItem trees : items) {
				trees.dispose();
			}
			if (text.getData("beType").equals("1")) {
			GenerateCommonTree.generateContainmentTreeForBComponentDataType(treeItem);
			}
			GenerateCommonTree.generateContainmentTreeForBComponent(treeItem,
					DerbyDaoUtil.getBizElementsByBizComponentId(String.valueOf(text.getData("beType_id"))),true);
			synonymsWindow.close();
		});

		Button calBtn = new Button(parentComp, SWT.None);
		calBtn.setBounds(280, 510, 80, 30);
		calBtn.setText("cancel");
		calBtn.setBackground(new Color(Display.getCurrent(),135,206,250));
		calBtn.addListener(SWT.MouseDown, p -> {
			synonymsWindow.close();
		});

		synonymsWindow.open();
		while (!synonymsWindow.isDisposed()) {// 窗口帧是否关闭
			if (!synonymsWindow.getDisplay().readAndDispatch()) {// 是否还有任务未完成
				synonymsWindow.getDisplay().sleep();
			}
		}
		synonymsWindow.dispose();// 释放系统资源
	}

	private static StringBuffer searchText = new StringBuffer();
	private static ArrayList<EcoreTreeNode> arrayList;
	private static String string;
	private static String string2;
	private static ArrayList<EcoreTreeNode> arrayList3;
	private static ArrayList<EcoreTreeNode> arrayList4;
	private static String string3;

	/**
	 * 添加super-type, sub-type
	 * 
	 * @param bcTree
	 * @throws Exception 
	 */
	public static void selectBCTypes(Tree tabTree, String typeName,String id) throws Exception {

		Shell selectBCWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);

		java.util.List<EcoreTreeNode> businessComponentList = businessComponentDao.findAllEcoreBusinessComponents();

		int width = 800;
		int height = 600;
		int treeWidth = (width - 50) / 2;
		selectBCWindow.setSize(width, height);
		selectBCWindow.setText("select type");

		Composite parentComp = new Composite(selectBCWindow, SWT.NONE);
		parentComp.setBounds(0, 0, width, height);

		Label selTp = new Label(parentComp, SWT.CENTER);
		selTp.setBounds(10, 50, 150, 25);
		selTp.setText("select type");

		Tree tree = new Tree(parentComp, SWT.V_SCROLL | SWT.BORDER);
		tree.setBounds(10, 75, treeWidth, 425);

		Text cb = new Text(parentComp, SWT.BORDER);
		cb.setBounds(20, 15, 350, 30);

		Label selLb = new Label(parentComp, SWT.CENTER);
		selLb.setBounds(350, 50, 150, 25);
		selLb.setText("已选择");

		Tree selTree = new Tree(parentComp, SWT.V_SCROLL | SWT.BORDER);
		selTree.setBounds(treeWidth + 20, 75, treeWidth, 425);

		// 之前选择的items
		Map<String, TreeItem> selectedItemMapBefore = new HashMap<String, TreeItem>();
		for (TreeItem treeItem : tabTree.getSelection()[0].getItems()) {
			selectedItemMapBefore.put(treeItem.getText(), treeItem);
		}

		// 之后选择的items
		Map<String, TreeItem> selectedItemMapAfter = new HashMap<String, TreeItem>();
		TreeItem parentItem = new TreeItem(tree, SWT.NONE);
		parentItem.setText(TreeParentEnum.BUSINESS_COMPONENTS.getName());
		parentItem.setImage(ImgUtil.createImg(ImgUtil.BUSINESS_COMPONENTS));

		for (int i = 0; i < businessComponentList.size(); i++) {
			TreeItem item = new TreeItem(parentItem, SWT.NONE);
			item.setText(businessComponentList.get(i).getName());
			item.setData("id", businessComponentList.get(i).getId());
			// 之前选中的
			if (selectedItemMapBefore != null && selectedItemMapBefore.get(item.getText()) != null) {
				// 之前选中的在右边展示
				TreeItem itemBeforeSelected = new TreeItem(selTree, SWT.NONE);
				itemBeforeSelected.setText(businessComponentList.get(i).getName());
				itemBeforeSelected.setImage(ImgUtil.createImg(ImgUtil.BC_SEARCH_SELECTED_YES));
				itemBeforeSelected.setData("id", businessComponentList.get(i).getId());

				// 左边展示已选中的
				item.setImage(ImgUtil.createImg(ImgUtil.BC_SEARCH_SELECTED_YES));
				selectedItemMapAfter.put(item.getText(), itemBeforeSelected);
			} else {
				item.setImage(ImgUtil.createImg(ImgUtil.BC_SEARCH_SELECTED_NO));
			}
		}

		cb.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				int code = e.keyCode;
				searchText.append(e.character);
				String text = cb.getText();

				if (code == 8) {
					// 删除
					searchText.deleteCharAt(searchText.length() - 1);
				}

				if (code == 13) {
					// 按下回车键,查找
					// 移除全部
					parentItem.removeAll();
					for (int i = 0; i < businessComponentList.size(); i++) {
						if (businessComponentList.get(i).getName().toUpperCase()
								.indexOf(text.toString().toUpperCase()) >= 0) {
							TreeItem treeItem = new TreeItem(parentItem, SWT.NONE);
							treeItem.setImage(ImgUtil.createImg(ImgUtil.BC_SEARCH_SELECTED_NO));
							treeItem.setText(businessComponentList.get(i).getName());
							treeItem.setData("id", businessComponentList.get(i).getId());

							if (selectedItemMapAfter.get(businessComponentList.get(i).getName()) != null) {
								treeItem.setImage(ImgUtil.createImg(ImgUtil.BC_SEARCH_SELECTED_YES));
							} else {
								treeItem.setImage(ImgUtil.createImg(ImgUtil.BC_SEARCH_SELECTED_NO));
							}

							if (selectedItemMapBefore != null
									&& selectedItemMapBefore.get(treeItem.getText()) != null) {
								treeItem.setImage(ImgUtil.createImg(ImgUtil.BC_SEARCH_SELECTED_YES));
							} else {
								treeItem.setImage(ImgUtil.createImg(ImgUtil.BC_SEARCH_SELECTED_NO));
							}
						}
					}
				}
			}
		});

		tree.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				TreeItem treeItem = (TreeItem) e.item;

				TreeItem[] items = selTree.getItems();
				for (int i = 0; i < items.length; i++) {
					if (items[i].getText().equals(treeItem.getText())) {
						items[i].dispose();
						selectedItemMapAfter.remove(treeItem.getText());
					}
				}

				if (treeItem.getImage().equals(ImgUtil.createImg(ImgUtil.BC_SEARCH_SELECTED_YES))) {
					// 已选中
					treeItem.setImage(ImgUtil.createImg(ImgUtil.BC_SEARCH_SELECTED_NO));
				} else if (treeItem.getImage().equals(ImgUtil.createImg(ImgUtil.BC_SEARCH_SELECTED_NO))) {
					// 未选中
					
					boolean selectFlag = false;
					if(typeName.equals("Sub-Types")) {
						selectFlag = true;
					}else if(typeName.equals("Super-Types")){
						//只能一个
						if(selectedItemMapAfter.size() == 1){
							selectFlag = false;
						}else {
							
							selectFlag = true;
							
							//获取sub-types里面所有的子菜单
//							TreeItem[] subTypeItems = tabTree.getSelection()[0].getParentItem().getItems()[1].getItems();
//							if(subTypeItems != null && subTypeItems.length > 0) {
//								for (int j = 0; j < subTypeItems.length; j++) {
//									if(treeItem.getText().equals(subTypeItems[j].getText())) {
//										selectFlag = false;
//										break;
//									}
//								}
//							}
						}
					}
					
					if(selectFlag) {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.BC_SEARCH_SELECTED_YES));
						TreeItem item = new TreeItem(selTree, SWT.None);
						item.setText(treeItem.getText());
						item.setImage(ImgUtil.createImg(ImgUtil.BC_SEARCH_SELECTED_YES));
						item.setData("id", treeItem.getData("id"));
						selectedItemMapAfter.put(treeItem.getText(), item);
					}
					
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		Button okBtn = new Button(parentComp, SWT.None);
		okBtn.setBounds(180, 510, 80, 30);
		okBtn.setText("choose");
		okBtn.setBackground(new Color(Display.getCurrent(),135,206,250));
		okBtn.addListener(SWT.MouseUp, p -> {

			java.util.List<EcoreBusinessComponentRL> addBusinessComponentRLs = new ArrayList<EcoreBusinessComponentRL>();
			java.util.List<String> deleteBusinessComponentRLIds = new ArrayList<String>();

			Set<String> keySetBefore = selectedItemMapBefore.keySet();
			keySetBefore.stream().forEach(t -> {
				EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)selectedItemMapBefore.get(t).getData("EcoreTreeNode");
				deleteBusinessComponentRLIds.add(ecoreTreeNode.getId());
				selectedItemMapBefore.get(t).dispose();
			});
			Set<String> keySetAfter = selectedItemMapAfter.keySet();

			TreeListItem treeItem = (TreeListItem) tabTree.getSelection()[0];

			
			keySetAfter.stream().forEach(t -> {
				TreeListItem treeListItem = new TreeListItem(treeItem,
						new TreeItemDataBean(selectedItemMapAfter.get(t).getData("id").toString(), t, ImgUtil.BC, "2",
								TreeLevelEnum.LEVEL_4.getLevel()));
				tabTree.setSelection(treeListItem);
				treeListItem.getParentItem().setExpanded(true);
				treeListItem.setExpanded(true);
				treeListItem.setData("id", selectedItemMapAfter.get(t).getData("id"));
				
				// 设置打开事件必备
				EcoreTreeNode ecoreTreeNodeDT = new EcoreTreeNode();
				ecoreTreeNodeDT.setId(selectedItemMapAfter.get(t).getData("id").toString());
				ecoreTreeNodeDT.setType(TreeParentEnum.BUSINESS_COMPONENTS.getName());
				ecoreTreeNodeDT.setLevel(TreeLevelEnum.LEVEL_4.getLevel());
				ecoreTreeNodeDT.setName(t);
				treeListItem.setData("type", TreeParentEnum.BUSINESS_COMPONENTS.getName());
				treeListItem.setData("EcoreTreeNode", ecoreTreeNodeDT);
				
//				EcoreTreeNode parentEcoreTreeNode = (EcoreTreeNode)treeItem.getData("EcoreTreeNode");
//				if(parentEcoreTreeNode != null && parentEcoreTreeNode.getId() != null) {
//					EcoreBusinessComponentRL businessComponentRL = new EcoreBusinessComponentRL();
//					businessComponentRL.setId(ecoreTreeNodeDT.getId());
//					businessComponentRL.setpId(parentEcoreTreeNode.getId());
//					addBusinessComponentRLs.add(businessComponentRL);
//				}
//				else {
				if (typeName.equals("Sub-Types")) {
				EcoreBusinessComponentRL businessComponentRL = new EcoreBusinessComponentRL();
				businessComponentRL.setId(treeListItem.getData("id").toString());
				businessComponentRL.setpId(id);
				addBusinessComponentRLs.add(businessComponentRL);	
				}else {
				EcoreBusinessComponentRL businessComponentRL = new EcoreBusinessComponentRL();
				businessComponentRL.setId(id);
				businessComponentRL.setpId(treeListItem.getData("id").toString());
				addBusinessComponentRLs.add(businessComponentRL);
				}
//				}
				
			});
			
			//入库
			try {
				//删除之前的
				if(deleteBusinessComponentRLIds != null && deleteBusinessComponentRLIds.size() > 0) {
					deleteBusinessComponentRLIds.forEach(t -> {
						try {
							businessComponentRLDao.deleteEcoreBusinessComponentRL(t);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					});
				}
				//添加新的
				if(addBusinessComponentRLs != null && addBusinessComponentRLs.size() > 0) {
					businessComponentRLDao.saveEcoreBusinessComponentRL(addBusinessComponentRLs);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			selectBCWindow.close();
		});

		Button calBtn = new Button(parentComp, SWT.None);
		calBtn.setBounds(280, 510, 80, 30);
		calBtn.setText("取消");
		calBtn.addListener(SWT.MouseDown, p -> {
			selectBCWindow.close();
		});

		selectBCWindow.open();
		while (!selectBCWindow.isDisposed()) {// 窗口帧是否关闭
			if (!selectBCWindow.getDisplay().readAndDispatch()) {// 是否还有任务未完成
				selectBCWindow.getDisplay().sleep();
			}
		}
		selectBCWindow.dispose();// 释放系统资源
	}
	
	/**
	 * 创建BusinessComponentElement对话框
	 */
	public static void createBusinessComponetElementSelectionDialogue(Tree elementsTree) {
		Shell businessElementWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		businessElementWindow.setText("Business Element General Information");
		businessElementWindow.setLayout(new FormLayout());
		Composite c = new Composite(businessElementWindow, SWT.NONE);
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
		maxOccursLabel.setBounds(145, 145, 100, 30);
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
		typeText.setEnabled(false);
		
		Button typeButton = new Button(c, SWT.PUSH);
		typeButton.setText("...");
		typeButton.setBounds(355, 215, 25, 30);
		typeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					createSelectBusinessElementType(typeText);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		Button finishButton = new Button(c, SWT.PUSH);
		finishButton.setText("Finish");
		finishButton.setBounds(280, 265, 60, 30);
		finishButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		finishButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TreeItem treeItem = new TreeItem(elementsTree, SWT.NONE);
				//type = 1 ---> messageElement
//				treeItem.setData("type", 1);
				String nodeName;
				nodeName = nameText.getText() + " [" + minText.getText() + ", " + maxOccursText.getText() + "]";
				treeItem.setText(nodeName);
				treeItem.setData("id",treeItem.getData("id")==null?UUID.randomUUID():treeItem.getData("id").toString());
//				treeItem.setData("id", UUID.randomUUID().toString());
				treeItem.setData("beNameText", nameText.getText());
				treeItem.setData("beDocText", docText.getText());
				treeItem.setData("beMinOccursText", minText.getText());
				treeItem.setData("beMaxOccursText", maxOccursText.getText());
				treeItem.setData("isDerivedCheckButton", isTechnicalButton.getSelection());
				treeItem.setData("beType_id", String.valueOf(typeText.getData("beType_id")));
				treeItem.setData("beType", String.valueOf(typeText.getData("beType")));
//				treeItem.setData("beStatusCombo", String.valueOf(typeText.getData("status")));
				treeItem.setData("beStatusCombo", String.valueOf(RegistrationStatus.Added.getStatus()));
				treeItem.setData("beTypeText", typeText.getText());
				
//				if ("1".equals(String.valueOf(typeText.getData("beType")))) {
//					treeItem.setImage(ImgUtil.createImg(ImgUtil.BC));	
//				} else if ("2".equals(String.valueOf(typeText.getData("beType")))) {
				treeItem.setImage(ImgUtil.createImg(ImgUtil.BC_BE));	
//				}
//				treeItem.setImage(ImgUtil.createImg(ImgUtil.ME));
				// 用Message Component里的元素，构造Message Component树
				GenerateCommonTree.generateContainmentTreeForBComponent(treeItem,
						DerbyDaoUtil.getBizElementsByBizComponentId(String.valueOf(typeText.getData("beType_id"))),true);
				businessElementWindow.close();
			}
		});
		Button cancelButton = new Button(c, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setBounds(350, 265, 60, 30);
		cancelButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				businessElementWindow.close();
			}
		});

		businessElementWindow.setSize(480, 350);
		businessElementWindow.open();
		businessElementWindow.layout();
	}
	
	/**
	 * 加载Constraint
	 * 
	 * @param type = edit编辑， type = add新增
	 */
	public static void createConstraintDialogueForBAandMS(String type, Table constraintsTable,
			TableItem... selectedTableItem) {
		Shell constraintWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		constraintWindow.setText("Constraint General Information");
		constraintWindow.setLayout(new FormLayout());
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
		nameText.setBounds(150, 10, 230, 30);

		Label documentationLabel = new Label(c, SWT.NONE);
		documentationLabel.setText("Documentation");
		documentationLabel.setBounds(10, 50, 100, 30);
		Text docText = new Text(c, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
		docText.setBounds(150, 50, 230, 80);

		Label expressionLabel = new Label(c, SWT.NONE);
		expressionLabel.setText("Expression");
		expressionLabel.setBounds(10, 140, 100, 30);
		Text expressionText = new Text(c, SWT.BORDER);
		expressionText.setBounds(150, 140, 230, 30);

		Label expressionLanguageLabel = new Label(c, SWT.NONE);
		expressionLanguageLabel.setText("Expression Language");
		expressionLanguageLabel.setBounds(10, 180, 100, 30);
		Text expressionLanguageText = new Text(c, SWT.BORDER);
		expressionLanguageText.setBounds(150, 180, 230, 30);

		/**
		 * 编辑的时候触发回显
		 */
		if (type == "edit") {
			TableItem[] tableItems = constraintsTable.getSelection();
			if (tableItems != null && tableItems.length > 0) {
				nameText.setText(tableItems[0].getText(0));
				docText.setText(tableItems[0].getText(1));
				expressionText.setText(tableItems[0].getText(2));
				expressionLanguageText.setText(tableItems[0].getText(3));
			}
		}

		Button finishButton = new Button(c, SWT.PUSH);
		finishButton.setText("Finish");
		finishButton.setBounds(280, 265, 60, 30);
		finishButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		finishButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TableItem tableItem = new TableItem(constraintsTable, SWT.NONE);
				tableItem.setText(new String[] { nameText.getText(), docText.getText(),
						expressionLanguageText.getText(), expressionText.getText() });
				
				EcoreConstraint ecoreConstraint = new EcoreConstraint();
				ecoreConstraint.setId(tableItem.getData("id").toString()==null?UUID.randomUUID().toString():tableItem.getData("id").toString());
//				ecoreConstraint.setId(UUID.randomUUID().toString());
				ecoreConstraint.setName(nameText.getText());
				ecoreConstraint.setDefinition(docText.getText());
				ecoreConstraint.setExpression(expressionText.getText());
				ecoreConstraint.setExpressionlanguage(expressionLanguageText.getText());
				tableItem.setData(ecoreConstraint);
				
				/**
				 * 编辑的时候先删除，后添加
				 */
				if (type == "edit") {
					TableItem[] tableItems = constraintsTable.getSelection();
					if (tableItems != null && tableItems.length > 0) {
						tableItems[0].dispose();
					}
				}

				if (selectedTableItem != null && selectedTableItem.length > 0) {
					TableItem[] items = constraintsTable.getItems();
					java.util.List<String[]> textList = new ArrayList<String[]>();
					for (int i = 0; i < items.length; i++) {
						textList.add(new String[] { items[i].getText(0), items[i].getText(1), items[i].getText(2),
								items[i].getText(3) });
						//新增
//						items[i].setData("id",UUID.randomUUID().toString());
					}
					
					selectedTableItem[0].setData("beConstraintsTableItems", textList);
				}

				constraintWindow.close();
			}
		});
		Button cancelButton = new Button(c, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setBounds(350, 265, 60, 30);
		cancelButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				constraintWindow.close();
			}
		});

		constraintWindow.setBounds((Display.getDefault().getBounds().width - 480) / 2,
				(Display.getDefault().getBounds().height - 350) / 2, 480, 350);
		constraintWindow.open();
		constraintWindow.layout();
	}

	public static void deleteConstraintForBA(Table constraintsTable) {
		for (TableItem tableItem : constraintsTable.getSelection()) {
			tableItem.dispose();
		}
	}

	/**
	 * 创建messageElement对话框
	 * @param text 
	 */
	public static void createMsgElementSelectionDialogue(Tree elementsTree, String text) {
		Shell messageElementWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		messageElementWindow.setText("Message Element General Information");
		messageElementWindow.setLayout(new FormLayout());
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
		maxOccursLabel.setBounds(145, 145, 100, 30);
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
		typeText.setEnabled(false);
		
		Button typeButton = new Button(c, SWT.PUSH);
		typeButton.setText("...");
		typeButton.setBounds(355, 215, 25, 30);
		typeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					createSelectMsgElementType(typeText);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		Button finishButton = new Button(c, SWT.PUSH);
		finishButton.setText("Finish");
		finishButton.setBounds(280, 265, 60, 30);
		finishButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		finishButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TreeItem treeItem = new TreeItem(elementsTree, SWT.NONE);
				//type = 1 ---> messageElement
//				treeItem.setData("type", 1);
				String nodeName;
				if (typeText.getText().isEmpty()) {
					nodeName = nameText.getText() + " [" + minText.getText() + ", " + maxOccursText.getText() + "]";
				} else {
					nodeName = nameText.getText() + " [" + minText.getText() + ", " + maxOccursText.getText() + "] : "
							+ typeText.getText();
				}
				treeItem.setText(nodeName);
				treeItem.setData("id",treeItem.getData("id")==null?UUID.randomUUID():treeItem.getData("id").toString());
//				treeItem.setData("id", UUID.randomUUID().toString());
				treeItem.setData("meNameText", nameText.getText());
				treeItem.setData("meDocText", docText.getText());
				treeItem.setData("meMinOccursText", minText.getText());
				treeItem.setData("meMaxOccursText", maxOccursText.getText());
				treeItem.setData("isTechnical", isTechnicalButton.getSelection());
				treeItem.setData("meType_id", String.valueOf(typeText.getData("meType_id")));
				treeItem.setData("meType", String.valueOf(typeText.getData("meType")));
//				treeItem.setData("meStatusCombo", String.valueOf(typeText.getData("status")));
				treeItem.setData("meStatusCombo", text);
				treeItem.setData("meTypeName", typeText.getText());
				treeItem.setData("mcstatus", 1);
				treeItem.setData("content", "add");
				
				if ("1".equals(String.valueOf(typeText.getData("meType")))) {
//					if (treeItem.getData("status")==null||treeItem.getData("status").equals("Provisionally Registered")) {
					treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));	
//					}
//					else {
//					treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
//					}
				} else if ("2".equals(String.valueOf(typeText.getData("meType")))) {
//					if (treeItem.getData("status")==null||treeItem.getData("status").equals("Provisionally Registered")) {
					treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT1));	
//					}else {
//					treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
//					}
				}else {
					treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
				}
//				treeItem.setImage(ImgUtil.createImg(ImgUtil.ME));
				// 用Message Component里的元素，构造Message Component树
				GenerateCommonTree.generateContainmentTreeForMessageComponent(treeItem,
						DerbyDaoUtil.getMessageElementList(String.valueOf(typeText.getData("meType_id"))),true);
				messageElementWindow.close();
			}
		});
		Button cancelButton = new Button(c, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setBounds(350, 265, 60, 30);
		cancelButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				messageElementWindow.close();
			}
		});

		messageElementWindow.setSize(480, 350);
		messageElementWindow.open();
		messageElementWindow.layout();
	}

	/**
	 * 创建选择me的框
	 * 
	 * @param typeText
	 * @throws Exception
	 */
	public static void createSelectMsgElementType(Text typeText) throws Exception {

		java.util.List<EcoreTreeNode> allEcoreMessages = ecoreMessageComponentDao.findAllEcoreMessages();
		
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
		searchText.setBounds(10, 10, 270, 30);
		Button searchBtn = new Button(c, SWT.NONE);
		searchBtn.setText("Search");
		searchBtn.setBackground(new Color(Display.getCurrent(),135,206,250));
		searchBtn.setBounds(290, 10, 60, 30);

		Table msgComponentTable = new Table(c, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
		msgComponentTable.setLinesVisible(true);
		msgComponentTable.setBounds(10, 50, 350, 400);

		searchBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				msgComponentTable.removeAll();

				allEcoreMessages.stream()
						.filter(t -> (t.getName() != null
								&& t.getName().toUpperCase().indexOf(searchText.getText().toUpperCase()) != -1))
						.forEach(t -> {
							TableItem item = new TableItem(msgComponentTable, SWT.NONE);
							item.setText(t.getName() == null ? "" : t.getName());
							item.setData("id", t.getId());
							item.setData("meType", "2");
							item.setData("status", t.getRegistrationStatus());
							if ("1".equals(t.getType())) {
								if (t.getRegistrationStatus()==null||t.getRegistrationStatus().equals("Provisionally Registered")) {
								item.setImage(ImgUtil.createImg(ImgUtil.MC_SUB3_COMPONENT));	
								}else {
								item.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_COMPONENT));
								}
							} else if ("2".equals(t.getType())) {
								if (t.getRegistrationStatus()==null||t.getRegistrationStatus().equals("Provisionally Registered")) {
								item.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_CHOICE1));	
								}else {
								item.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_CHOICE));
							}
							}
						});

				allEcoreDataTypes.stream()
						.filter(t -> (t.getName() != null
								&& t.getName().toUpperCase().indexOf(searchText.getText().toUpperCase()) != -1))
						.forEach(t -> {
							TableItem item = new TableItem(msgComponentTable, SWT.NONE);
							item.setText(t.getName() == null ? "" : t.getName());
							item.setData("id", t.getId());
							item.setData("meType", "1");
							item.setData("status", t.getRegistrationStatus());
							if (t != null && t.getType() != null) {
								if (DataTypesEnum.CODE_SETS.getType().equals(t.getType())) {
									if ("1".equals(t.getObjType())) {
										item.setImage(ImgUtil.createImg(ImgUtil.CODE_SET_SUB1_2));
									} else {
										item.setImage(ImgUtil.createImg(ImgUtil.CODE_SET_SUB1));
									}
								} else if (DataTypesEnum.TEXT.getType().equals(t.getType())) {
									item.setImage(ImgUtil.createImg(ImgUtil.TEXT_SUB1));
								} else if (DataTypesEnum.BOOLEAN.getType().equals(t.getType())) {
									item.setImage(ImgUtil.createImg(ImgUtil.BOOLEAN_SUB1));
								} else if (DataTypesEnum.INDICATOR.getType().equals(t.getType())) {
									item.setImage(ImgUtil.createImg(ImgUtil.INDICATOR_SUB1));
								} else if (DataTypesEnum.DECIMAL.getType().equals(t.getType())) {
									item.setImage(ImgUtil.createImg(ImgUtil.DECIMAL_SUB1));
								} else if (DataTypesEnum.RATE.getType().equals(t.getType())) {
									item.setImage(ImgUtil.createImg(ImgUtil.RATE_SUB1));
								} else if (DataTypesEnum.AMOUNT.getType().equals(t.getType())) {
									item.setImage(ImgUtil.createImg(ImgUtil.AMOUNT_SUB1));
								} else if (DataTypesEnum.QUANTITY.getType().equals(t.getType())) {
									item.setImage(ImgUtil.createImg(ImgUtil.QUANTITY_SUB1));
								} else if (DataTypesEnum.TIME.getType().equals(t.getType())) {
									item.setImage(ImgUtil.createImg(ImgUtil.TIME_SUB1));
								} else if (DataTypesEnum.BINARY.getType().equals(t.getType())) {
									item.setImage(ImgUtil.createImg(ImgUtil.BINARY_SUB1));
								} else if (DataTypesEnum.SCHEMA_TYPES.getType().equals(t.getType())) {
									item.setImage(ImgUtil.createImg(ImgUtil.SCHEMA_TYPES_SUB1));
								} else if (DataTypesEnum.USER_DEFINED.getType().equals(t.getType())) {
									item.setImage(ImgUtil.createImg(ImgUtil.USER_DEFINED_SUB1));
								}
							}
						});
			}
		});

		allEcoreMessages.forEach(t -> {
			TableItem item = new TableItem(msgComponentTable, SWT.NONE);
			item.setText(t.getName() == null ? "" : t.getName());
			item.setData("id", t.getId());
			item.setData("meType", "2");
			item.setData("status", t.getRegistrationStatus());
			if ("1".equals(t.getType())) {
				if (t.getRegistrationStatus()==null||t.getRegistrationStatus().equals("Provisionally Registered")) {
				item.setImage(ImgUtil.createImg(ImgUtil.MC_SUB3_COMPONENT));	
				}else {
				item.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_COMPONENT));
				}
			} else if ("2".equals(t.getType())) {
				if (t.getRegistrationStatus()==null||t.getRegistrationStatus().equals("Provisionally Registered")) {
				item.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_CHOICE1));
				}else {
				item.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_CHOICE));
				}
			}
		});
		allEcoreDataTypes = ecoreDataTypeDao.findAllEcoreDataTypes();
		allEcoreDataTypes.forEach(t -> {
			TableItem item = new TableItem(msgComponentTable, SWT.NONE);
			item.setText(t.getName() == null ? "" : t.getName());
			item.setData("id", t.getId());
			item.setData("meType", "1");
			item.setData("status", t.getRegistrationStatus());
			if (t != null && t.getType() != null) {
				if (DataTypesEnum.CODE_SETS.getType().equals(t.getType())) {
					if ("1".equals(t.getObjType())) {
						item.setImage(ImgUtil.createImg(ImgUtil.CODE_SET_SUB1_2));
					} else {
						item.setImage(ImgUtil.createImg(ImgUtil.CODE_SET_SUB1));
					}
				} else if (DataTypesEnum.TEXT.getType().equals(t.getType())) {
					item.setImage(ImgUtil.createImg(ImgUtil.TEXT_SUB1));
				} else if (DataTypesEnum.BOOLEAN.getType().equals(t.getType())) {
					item.setImage(ImgUtil.createImg(ImgUtil.BOOLEAN_SUB1));
				} else if (DataTypesEnum.INDICATOR.getType().equals(t.getType())) {
					item.setImage(ImgUtil.createImg(ImgUtil.INDICATOR_SUB1));
				} else if (DataTypesEnum.DECIMAL.getType().equals(t.getType())) {
					item.setImage(ImgUtil.createImg(ImgUtil.DECIMAL_SUB1));
				} else if (DataTypesEnum.RATE.getType().equals(t.getType())) {
					item.setImage(ImgUtil.createImg(ImgUtil.RATE_SUB1));
				} else if (DataTypesEnum.AMOUNT.getType().equals(t.getType())) {
					item.setImage(ImgUtil.createImg(ImgUtil.AMOUNT_SUB1));
				} else if (DataTypesEnum.QUANTITY.getType().equals(t.getType())) {
					item.setImage(ImgUtil.createImg(ImgUtil.QUANTITY_SUB1));
				} else if (DataTypesEnum.TIME.getType().equals(t.getType())) {
					item.setImage(ImgUtil.createImg(ImgUtil.TIME_SUB1));
				} else if (DataTypesEnum.BINARY.getType().equals(t.getType())) {
					item.setImage(ImgUtil.createImg(ImgUtil.BINARY_SUB1));
				} else if (DataTypesEnum.SCHEMA_TYPES.getType().equals(t.getType())) {
					item.setImage(ImgUtil.createImg(ImgUtil.SCHEMA_TYPES_SUB1));
				} else if (DataTypesEnum.USER_DEFINED.getType().equals(t.getType())) {
					item.setImage(ImgUtil.createImg(ImgUtil.USER_DEFINED_SUB1));
				}
			}
		});
		
		msgComponentTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				searchText.setText("" + msgComponentTable.getSelection()[0].getText());
			}
		});
		
		Button okButton = new Button(c, SWT.PUSH);
		okButton.setText("OK");
		okButton.setBounds(275, 460, 35, 30);
		okButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		okButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TableItem[] tableItemArray = msgComponentTable.getSelection();
				for (TableItem treeItem : tableItemArray) {
					typeText.setData("meType_id", treeItem.getData("id"));
					typeText.setData("meType", treeItem.getData("meType"));
					typeText.setData("status", treeItem.getData("status"));
					typeText.setData("meLever", 1);
					typeText.setText(treeItem.getText());
				}
				messageComponentWindow.close();
			}
		});

		Button cancelButton = new Button(c, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setBounds(320, 460, 52, 30);
		cancelButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				messageComponentWindow.close();
			}
		});

		messageComponentWindow.setSize(400, 550);
		messageComponentWindow.open();
		messageComponentWindow.layout();
	}
	
	/**
	 * 创建选择me的框
	 * 
	 * @param typeText
	 * @throws Exception
	 */
	public static void createSelectMsgElementType(Text typeText,TreeItem treeItem) throws Exception {

		java.util.List<EcoreTreeNode> allEcoreMessages = ecoreMessageComponentDao.findAllEcoreMessages();
		
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
		searchText.setBounds(10, 10, 270, 30);
		Button searchBtn = new Button(c, SWT.NONE);
		searchBtn.setText("Search");
		searchBtn.setBackground(new Color(Display.getCurrent(),135,206,250));
		searchBtn.setBounds(290, 10, 60, 30);

		Table msgComponentTable = new Table(c, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
		msgComponentTable.setLinesVisible(true);
		msgComponentTable.setBounds(10, 50, 350, 400);

		searchBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				msgComponentTable.removeAll();

				allEcoreMessages.stream()
						.filter(t -> (t.getName() != null
								&& t.getName().toUpperCase().indexOf(searchText.getText().toUpperCase()) != -1))
						.forEach(t -> {
							TableItem item = new TableItem(msgComponentTable, SWT.NONE);
							item.setText(t.getName() == null ? "" : t.getName());
							item.setData("id", t.getId());
							item.setData("meType", "2");
							item.setData("status", t.getRegistrationStatus());
							if ("1".equals(t.getType())) {
								if (t.getRegistrationStatus().equals("Provisionally Registered")||t.getRegistrationStatus()==null) {
								item.setImage(ImgUtil.createImg(ImgUtil.MC_SUB3_COMPONENT));	
								}else {
								item.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_COMPONENT));
								}
							} else if ("2".equals(t.getType())) {
								if (t.getRegistrationStatus().equals("Provisionally Registered")||t.getRegistrationStatus()==null) {
								item.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_CHOICE1));	
								}else {
								item.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_CHOICE));
							}
							}
						});

				allEcoreDataTypes.stream()
						.filter(t -> (t.getName() != null
								&& t.getName().toUpperCase().indexOf(searchText.getText().toUpperCase()) != -1))
						.forEach(t -> {
							TableItem item = new TableItem(msgComponentTable, SWT.NONE);
							item.setText(t.getName() == null ? "" : t.getName());
							item.setData("id", t.getId());
							item.setData("meType", "1");
							item.setData("status", t.getRegistrationStatus());
							if (t != null && t.getType() != null) {
								if (DataTypesEnum.CODE_SETS.getType().equals(t.getType())) {
									if ("1".equals(t.getObjType())) {
										item.setImage(ImgUtil.createImg(ImgUtil.CODE_SET_SUB1_2));
									} else {
										item.setImage(ImgUtil.createImg(ImgUtil.CODE_SET_SUB1));
									}
								} else if (DataTypesEnum.TEXT.getType().equals(t.getType())) {
									item.setImage(ImgUtil.createImg(ImgUtil.TEXT_SUB1));
								} else if (DataTypesEnum.BOOLEAN.getType().equals(t.getType())) {
									item.setImage(ImgUtil.createImg(ImgUtil.BOOLEAN_SUB1));
								} else if (DataTypesEnum.INDICATOR.getType().equals(t.getType())) {
									item.setImage(ImgUtil.createImg(ImgUtil.INDICATOR_SUB1));
								} else if (DataTypesEnum.DECIMAL.getType().equals(t.getType())) {
									item.setImage(ImgUtil.createImg(ImgUtil.DECIMAL_SUB1));
								} else if (DataTypesEnum.RATE.getType().equals(t.getType())) {
									item.setImage(ImgUtil.createImg(ImgUtil.RATE_SUB1));
								} else if (DataTypesEnum.AMOUNT.getType().equals(t.getType())) {
									item.setImage(ImgUtil.createImg(ImgUtil.AMOUNT_SUB1));
								} else if (DataTypesEnum.QUANTITY.getType().equals(t.getType())) {
									item.setImage(ImgUtil.createImg(ImgUtil.QUANTITY_SUB1));
								} else if (DataTypesEnum.TIME.getType().equals(t.getType())) {
									item.setImage(ImgUtil.createImg(ImgUtil.TIME_SUB1));
								} else if (DataTypesEnum.BINARY.getType().equals(t.getType())) {
									item.setImage(ImgUtil.createImg(ImgUtil.BINARY_SUB1));
								} else if (DataTypesEnum.SCHEMA_TYPES.getType().equals(t.getType())) {
									item.setImage(ImgUtil.createImg(ImgUtil.SCHEMA_TYPES_SUB1));
								} else if (DataTypesEnum.USER_DEFINED.getType().equals(t.getType())) {
									item.setImage(ImgUtil.createImg(ImgUtil.USER_DEFINED_SUB1));
								}
							}
						});
			}
		});

		allEcoreMessages.forEach(t -> {
			TableItem item = new TableItem(msgComponentTable, SWT.NONE);
			item.setText(t.getName() == null ? "" : t.getName());
			item.setData("id", t.getId());
			item.setData("meType", "2");
			item.setData("status", t.getRegistrationStatus());
			if ("1".equals(t.getType())) {
				if (t.getRegistrationStatus()==null||t.getRegistrationStatus().equals("Provisionally Registered")) {
				item.setImage(ImgUtil.createImg(ImgUtil.MC_SUB3_COMPONENT));	
				}else {
				item.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_COMPONENT));
				}
			} else if ("2".equals(t.getType())) {
				if (t.getRegistrationStatus()==null||t.getRegistrationStatus().equals("Provisionally Registered")) {
				item.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_CHOICE1));
				}else {
				item.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_CHOICE));
				}
			}
		});

		allEcoreDataTypes.forEach(t -> {
			TableItem item = new TableItem(msgComponentTable, SWT.NONE);
			item.setText(t.getName() == null ? "" : t.getName());
			item.setData("id", t.getId());
			item.setData("meType", "1");
			item.setData("status", t.getRegistrationStatus());
			if (t != null && t.getType() != null) {
				if (DataTypesEnum.CODE_SETS.getType().equals(t.getType())) {
					if ("1".equals(t.getObjType())) {
						item.setImage(ImgUtil.createImg(ImgUtil.CODE_SET_SUB1_2));
					} else {
						item.setImage(ImgUtil.createImg(ImgUtil.CODE_SET_SUB1));
					}
				} else if (DataTypesEnum.TEXT.getType().equals(t.getType())) {
					item.setImage(ImgUtil.createImg(ImgUtil.TEXT_SUB1));
				} else if (DataTypesEnum.BOOLEAN.getType().equals(t.getType())) {
					item.setImage(ImgUtil.createImg(ImgUtil.BOOLEAN_SUB1));
				} else if (DataTypesEnum.INDICATOR.getType().equals(t.getType())) {
					item.setImage(ImgUtil.createImg(ImgUtil.INDICATOR_SUB1));
				} else if (DataTypesEnum.DECIMAL.getType().equals(t.getType())) {
					item.setImage(ImgUtil.createImg(ImgUtil.DECIMAL_SUB1));
				} else if (DataTypesEnum.RATE.getType().equals(t.getType())) {
					item.setImage(ImgUtil.createImg(ImgUtil.RATE_SUB1));
				} else if (DataTypesEnum.AMOUNT.getType().equals(t.getType())) {
					item.setImage(ImgUtil.createImg(ImgUtil.AMOUNT_SUB1));
				} else if (DataTypesEnum.QUANTITY.getType().equals(t.getType())) {
					item.setImage(ImgUtil.createImg(ImgUtil.QUANTITY_SUB1));
				} else if (DataTypesEnum.TIME.getType().equals(t.getType())) {
					item.setImage(ImgUtil.createImg(ImgUtil.TIME_SUB1));
				} else if (DataTypesEnum.BINARY.getType().equals(t.getType())) {
					item.setImage(ImgUtil.createImg(ImgUtil.BINARY_SUB1));
				} else if (DataTypesEnum.SCHEMA_TYPES.getType().equals(t.getType())) {
					item.setImage(ImgUtil.createImg(ImgUtil.SCHEMA_TYPES_SUB1));
				} else if (DataTypesEnum.USER_DEFINED.getType().equals(t.getType())) {
					item.setImage(ImgUtil.createImg(ImgUtil.USER_DEFINED_SUB1));
				}
			}
		});

		msgComponentTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				searchText.setText("" + msgComponentTable.getSelection()[0].getText());
			}
		});
		
		Button okButton = new Button(c, SWT.PUSH);
		okButton.setText("OK");
		okButton.setBounds(275, 460, 35, 30);
		okButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		okButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TableItem[] tableItemArray = msgComponentTable.getSelection();
				for (TableItem treeItem : tableItemArray) {
					typeText.setData("meType_id", treeItem.getData("id"));
					typeText.setData("meType", treeItem.getData("meType"));
					typeText.setData("status", treeItem.getData("status"));
					typeText.setData("meLever", 1);
					typeText.setText(treeItem.getText());
				}
				treeItem.setData("meTypeName",typeText.getText());
				treeItem.setData("meStatusCombo",typeText.getData("status"));
//				if (typeText.getData("status").equals("Registered")) {
//					if ("1".equals(typeText.getData("meType"))) {
//						treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
//					} else if ("2".equals(typeText.getData("meType"))) {
//						treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
//					}
//				}else {
//					if ("1".equals(typeText.getData("meType"))) {
//						treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT1));
//					} else if ("2".equals(typeText.getData("meType"))) {
//						treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
//					}
//				}
//				
				TreeItem[] items = treeItem.getItems();
				for (TreeItem tree : items) {
					tree.dispose();
				}
				GenerateCommonTree.generateContainmentTreeForMessageComponent(treeItem,
						DerbyDaoUtil.getMessageElementList(typeText.getData("meType_id").toString()), true);
				messageComponentWindow.close();
			}
		});

		Button cancelButton = new Button(c, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setBounds(320, 460, 52, 30);
		cancelButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				messageComponentWindow.close();
			}
		});

		messageComponentWindow.setSize(400, 550);
		messageComponentWindow.open();
		messageComponentWindow.layout();
	}
	
	/**
	 * 创建选择me的框
	 * 
	 * @param typeText
	 * @throws Exception
	 */
	public static void createSelectBusinessElementType(Text typeText) throws Exception {

		try {
			businessComponentList = businessComponentDao.findAllEcoreBusinessComponents();
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		try {
			allEcoreDataTypes = ecoreDataTypeDao.findAllEcoreDataTypes();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Shell synonymsWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		synonymsWindow.setSize(400, 600);
		synonymsWindow.setText("select type");
		
		//改变弹窗位置
		ChangeShellLocation.center(synonymsWindow);

		Composite parentComp = new Composite(synonymsWindow, SWT.NONE);
		parentComp.setBounds(0, 0, 400, 600);

		Combo cb = new Combo(parentComp, SWT.READ_ONLY);
		cb.add("Basic Type");
		cb.add("Complex Type");
		cb.setBounds(20, 15, 350, 30);

		Tree tree = new Tree(parentComp, SWT.V_SCROLL);
		tree.setBounds(20, 85, 350, 400);
		
		Text seachText = new Text(parentComp, SWT.BORDER);
		seachText.setBounds(20, 50, 270, 30);
		
		cb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tree.removeAll();
				if (cb.getText().equals("Basic Type")) {
					allEcoreDataTypes.stream()
					.filter(t -> (t.getName() != null
							&& t.getName().toUpperCase().indexOf(seachText.getText().toUpperCase()) != -1))
					.forEach(t -> {
						TreeItem item = new TreeItem(tree, SWT.NONE);
						item.setText(t.getName() == null ? "" : t.getName());
						item.setData("id", t.getId());
						item.setData("beType", t.getType());
						item.setData("status", t.getRegistrationStatus());
						if (t != null && t.getType() != null) {
							if (DataTypesEnum.CODE_SETS.getType().equals(t.getType())) {
								if ("1".equals(t.getObjType())) {
									item.setImage(ImgUtil.createImg(ImgUtil.CODE_SET_SUB1_2));
								} else {
									item.setImage(ImgUtil.createImg(ImgUtil.CODE_SET_SUB1));
								}
							} else if (DataTypesEnum.TEXT.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.TEXT_SUB1));
							} else if (DataTypesEnum.BOOLEAN.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.BOOLEAN_SUB1));
							} else if (DataTypesEnum.INDICATOR.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.INDICATOR_SUB1));
							} else if (DataTypesEnum.DECIMAL.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.DECIMAL_SUB1));
							} else if (DataTypesEnum.RATE.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.RATE_SUB1));
							} else if (DataTypesEnum.AMOUNT.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.AMOUNT_SUB1));
							} else if (DataTypesEnum.QUANTITY.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.QUANTITY_SUB1));
							} else if (DataTypesEnum.TIME.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.TIME_SUB1));
							} else if (DataTypesEnum.BINARY.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.BINARY_SUB1));
							} else if (DataTypesEnum.SCHEMA_TYPES.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.SCHEMA_TYPES_SUB1));
							} else if (DataTypesEnum.USER_DEFINED.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.USER_DEFINED_SUB1));
							}
						}
					});
				} else if (cb.getText().equals("Complex Type")) {
					businessComponentList.stream()
					.filter(t -> (t.getName() != null
							&& t.getName().toUpperCase().indexOf(seachText.getText().toUpperCase()) != -1))
					.forEach(t -> {
						TreeItem item = new TreeItem(tree, SWT.NONE);
						item.setText(t.getName() == null ? "" : t.getName());
						item.setData("id", t.getId());
						item.setData("beType", t.getType());
						item.setData("status", t.getRegistrationStatus());
						item.setImage(ImgUtil.createImg(ImgUtil.BC));
					});
				}
			}
		});

		
		Button seachBtn = new Button(parentComp, SWT.NONE);
		seachBtn.setBounds(300, 50, 60, 30);
		seachBtn.setText("Search");
		seachBtn.setBackground(new Color(Display.getCurrent(),135,206,250));
		seachBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 移除全部
				tree.removeAll();
				String text = seachText.getText();
				if (cb.getText().equals("Basic Type")) {
					allEcoreDataTypes.forEach(t -> {
						TreeItem item = new TreeItem(tree, SWT.NONE);
						item.setText(t.getName() == null ? "" : t.getName());
						item.setData("id", t.getId());
						item.setData("beType", t.getType());
						item.setData("status", t.getRegistrationStatus());
						if (t != null && t.getType() != null) {
							if (DataTypesEnum.CODE_SETS.getType().equals(t.getType())) {
								if ("1".equals(t.getObjType())) {
									item.setImage(ImgUtil.createImg(ImgUtil.CODE_SET_SUB1_2));
								} else {
									item.setImage(ImgUtil.createImg(ImgUtil.CODE_SET_SUB1));
								}
							} else if (DataTypesEnum.TEXT.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.TEXT_SUB1));
							} else if (DataTypesEnum.BOOLEAN.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.BOOLEAN_SUB1));
							} else if (DataTypesEnum.INDICATOR.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.INDICATOR_SUB1));
							} else if (DataTypesEnum.DECIMAL.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.DECIMAL_SUB1));
							} else if (DataTypesEnum.RATE.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.RATE_SUB1));
							} else if (DataTypesEnum.AMOUNT.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.AMOUNT_SUB1));
							} else if (DataTypesEnum.QUANTITY.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.QUANTITY_SUB1));
							} else if (DataTypesEnum.TIME.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.TIME_SUB1));
							} else if (DataTypesEnum.BINARY.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.BINARY_SUB1));
							} else if (DataTypesEnum.SCHEMA_TYPES.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.SCHEMA_TYPES_SUB1));
							} else if (DataTypesEnum.USER_DEFINED.getType().equals(t.getType())) {
								item.setImage(ImgUtil.createImg(ImgUtil.USER_DEFINED_SUB1));
							}
						}
					});
				} else if (cb.getText().equals("Complex Type")) {
					tree.removeAll();
					businessComponentList.forEach(t -> {
						TreeItem item = new TreeItem(tree, SWT.NONE);
						item.setText(t.getName() == null ? "" : t.getName());
						item.setData("id", t.getId());
						item.setData("beType", t.getType());
						item.setData("status", t.getRegistrationStatus());
						item.setImage(ImgUtil.createImg(ImgUtil.BC));
					});
				}
			}
		});
		
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] selection = tree.getSelection();
				seachText.setText(selection[0].getText());
			}
		});
		
		Button okButton = new Button(parentComp, SWT.PUSH);
		okButton.setText("OK");
		okButton.setBounds(275, 500, 35, 30);
		okButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		okButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TreeItem[] tableItemArray = tree.getSelection();
				for (TreeItem treeItem : tableItemArray) {
					typeText.setData("beType_id", treeItem.getData("id"));
					typeText.setData("beType", treeItem.getData("beType"));
					typeText.setData("status", treeItem.getData("status"));
					typeText.setText(treeItem.getText());
				}
				synonymsWindow.close();
			}
		});

		Button cancelButton = new Button(parentComp, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setBounds(320, 500, 52, 30);
		cancelButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				synonymsWindow.close();
			}
		});

		synonymsWindow.setSize(400, 580);
		synonymsWindow.open();
		synonymsWindow.layout();
	}
	
	
	
	public static void createSelectBusinessTrace(Text text,Tree tabletree) throws Exception {

		try {
			businessComponentList = businessComponentDao.findAllEcoreBusinessComponents();
		} catch (Exception e2) {
			e2.printStackTrace();
		}

//		try {
//			allEcoreDataTypes = ecoreDataTypeDao.findAllEcoreDataTypes();
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}

		Shell synonymsWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		synonymsWindow.setSize(400, 600);
		synonymsWindow.setText("select type");
		
		//改变弹窗位置
		ChangeShellLocation.center(synonymsWindow);
		
		Composite parentComp = new Composite(synonymsWindow, SWT.NONE);
		parentComp.setBounds(0, 0, 400, 600);


		Tree tree = new Tree(parentComp, SWT.V_SCROLL);
		tree.setBounds(20, 85, 350, 400);
		
		Text seachText = new Text(parentComp, SWT.BORDER);
		seachText.setBounds(20, 50, 270, 30);
		
//		seachText.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				tree.removeAll();
//					businessComponentList.stream()
//					.filter(t -> (t.getName() != null
//							&& t.getName().toUpperCase().indexOf(seachText.getText().toUpperCase()) != -1))
//					.forEach(t -> {
//						TreeItem item = new TreeItem(tree, SWT.NONE);
//						item.setText(t.getName() == null ? "" : t.getName());
//						item.setData("id", t.getId());
//						item.setData("beType", t.getType());
//						item.setData("status", t.getRegistrationStatus());
//						item.setImage(ImgUtil.createImg(ImgUtil.BC));
//					});
//			}
//		});

		
		Button seachBtn = new Button(parentComp, SWT.NONE);
		seachBtn.setBounds(300, 50, 60, 30);
		seachBtn.setText("Search");
		seachBtn.setBackground(new Color(Display.getCurrent(),135,206,250));
		seachBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 
				tree.removeAll();
				String text = seachText.getText();
					tree.removeAll();
					businessComponentList.stream().filter(t -> (t.getName() != null
							&& t.getName().toUpperCase().indexOf(seachText.getText().toUpperCase()) != -1))
					.forEach(t ->  {
						TreeItem item = new TreeItem(tree, SWT.NONE);
						item.setText(t.getName() == null ? "" : t.getName());
						item.setData("id", t.getId());
						item.setData("beType", t.getType());
						item.setData("status", t.getRegistrationStatus());
						item.setData("bcName", t.getName());
						item.setImage(ImgUtil.createImg(ImgUtil.BC));
					});
			}
		});
		
		businessComponentList.forEach(t -> {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText(t.getName() == null ? "" : t.getName());
			item.setData("id", t.getId());
			item.setData("beType", t.getType());
			item.setData("status", t.getRegistrationStatus());
			item.setData("bcName", t.getName());
			item.setImage(ImgUtil.createImg(ImgUtil.BC));
		});
		
//		seachText.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
////				TreeItem[] selection = tree.getSelection();
////				seachText.setText(selection[0].getText());
//				seachText.setText("" + tree.getSelection()[0].getText());
//			}
//		});
		
		Button okButton = new Button(parentComp, SWT.PUSH);
		okButton.setText("OK");
		okButton.setBounds(280, 510, 80, 30);
		okButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		okButton.addListener(SWT.MouseDown, p -> {
			TreeItem[] selection = tree.getSelection();
			for (TreeItem Item : selection) {
			text.setData("bcType_id", Item.getData("id"));
			text.setData("bcType", Item.getData("beType"));
			text.setData("bcStatusCombo", Item.getData("status"));
			text.setData("bcName", Item.getData("bcName"));
			text.setData("meLever", 1);
			
			text.setText(Item.getText());
			}
			tabletree.setData("bcType_id", text.getData("id"));
			tabletree.setData("bcName", text.getData("bcName"));
			tabletree.setData("bcType", text.getData("bcType"));
//			treeItem.setData("beType_id",text.getData("beType_id"));
//			treeItem.setData("beType",text.getData("beType"));
//			treeItem.setData("beStatusCombo",text.getData("beStatusCombo"));
//			TreeItem[] items = treeItem.getItems();
//			for (TreeItem trees : items) {
//				trees.dispose();
//			}
//			if (text.getData("beType").equals("1")) {
//			GenerateCommonTree.generateContainmentTreeForBComponentDataType(treeItem);
//			}
//			GenerateCommonTree.generateContainmentTreeForBComponent(treeItem,
//					DerbyDaoUtil.getBizElementsByBizComponentId(String.valueOf(text.getData("beType_id"))),true);
			synonymsWindow.close();
		});

		Button calBtn = new Button(parentComp, SWT.None);
		calBtn.setBounds(280, 510, 80, 30);
		calBtn.setText("cancel");
		calBtn.setBackground(new Color(Display.getCurrent(),135,206,250));
		calBtn.addListener(SWT.MouseDown, p -> {
			synonymsWindow.close();
		});

		synonymsWindow.open();
		while (!synonymsWindow.isDisposed()) {
			if (!synonymsWindow.getDisplay().readAndDispatch()) {
				synonymsWindow.getDisplay().sleep();
			}
		}
		synonymsWindow.dispose();
	}
	
	/**
	 * 创建选择bc的框
	 * 
	 * @param typeText
	 * @throws Exception
	 */
	public static void createSelectBusinessComponentDialog(Text typeText) throws Exception {

		java.util.List<EcoreTreeNode> businessComponentList = businessComponentDao.findAllEcoreBusinessComponents();

		Shell businessComponentWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		businessComponentWindow.setText("Select Business Component type:");
		businessComponentWindow.setLayout(new FormLayout());
		
		//改变弹窗位置
		ChangeShellLocation.center(businessComponentWindow);
		
		Composite c = new Composite(businessComponentWindow, SWT.NONE);
		FormData fd_c = new FormData();
		fd_c.top = new FormAttachment(0);
		fd_c.left = new FormAttachment(0);
		fd_c.bottom = new FormAttachment(100);
		fd_c.right = new FormAttachment(100);
		Text searchText = new Text(c, SWT.BORDER);
		searchText.setBounds(10, 10, 270, 30);
		Button searchBtn = new Button(c, SWT.NONE);
		searchBtn.setText("Search");
		searchBtn.setBackground(new Color(Display.getCurrent(),135,206,250));
		searchBtn.setBounds(290, 10, 60, 30);

		Table msgComponentTable = new Table(c, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
		msgComponentTable.setLinesVisible(true);
		msgComponentTable.setBounds(10, 50, 350, 400);

		searchBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				msgComponentTable.removeAll();
				businessComponentList.stream()
						.filter(t -> (t.getName() != null
								&& t.getName().toUpperCase().indexOf(searchText.getText().toUpperCase()) != -1))
						.forEach(t -> {
							TableItem item = new TableItem(msgComponentTable, SWT.NONE);
							item.setText(t.getName() == null ? "" : t.getName());
							item.setData("id", t.getId());
							item.setImage(ImgUtil.createImg(ImgUtil.BC));
						});
			}
		});

		businessComponentList.forEach(t -> {
			TableItem item = new TableItem(msgComponentTable, SWT.NONE);
			item.setText(t.getName() == null ? "" : t.getName());
			item.setData("id", t.getId());
			item.setImage(ImgUtil.createImg(ImgUtil.BC));
		});
		
		msgComponentTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				searchText.setText("" + msgComponentTable.getSelection()[0].getText());
			}
		});

		Button okButton = new Button(c, SWT.PUSH);
		okButton.setText("OK");
		okButton.setBounds(275, 460, 35, 30);
		okButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		okButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TableItem[] tableItemArray = msgComponentTable.getSelection();
				for (TableItem treeItem : tableItemArray) {
					typeText.setText(treeItem.getText());
					typeText.setData("bc_id", treeItem.getData("id"));
				}
				businessComponentWindow.close();
			}
		});

		Button cancelButton = new Button(c, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setBounds(320, 460, 52, 30);
		cancelButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				businessComponentWindow.close();
			}
		});

		businessComponentWindow.setSize(400, 550);
		businessComponentWindow.open();
		businessComponentWindow.layout();
	}
	
	//MC模块BusniessTrace页面,表格数据展示1
	public static void createBusinessComponetTrace(Text bcTypeText,TreeItem item1, Map<String, ArrayList<EcoreBusinessElement>> allBeByComponentId, Map<String, ArrayList<EcoreTreeNode>> allSubBusiness, Map<String, String> allBusinessComponent, Map<String, ArrayList<EcoreBusinessElement>> allBeByTyId) {
		Shell businessElementWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		businessElementWindow.setText("Define semantics");
		
		//改变弹窗位置
		ChangeShellLocation.center(businessElementWindow);
		
		Composite c = new Composite(businessElementWindow, SWT.NONE);
		GridData gr=new GridData();
		gr.heightHint=1200;
		c.setBounds(0, 0, 380, 1200);
		c.setLayoutData(gr);
		Label nameLabel = new Label(c, SWT.NONE);
		nameLabel.setText("Choose the trace target from AcceptorRole");
		nameLabel.setBounds(10, 10, 260, 30);
		
		Composite msgComponentTable = new Composite(c, SWT.BORDER | SWT.MULTI );
		msgComponentTable.setBounds(10, 40, 450, 580);
		msgComponentTable.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		msgComponentTable.setLayout(new FillLayout(SWT.HORIZONTAL));
		
        Tree elementsTree = new Tree(msgComponentTable, SWT.BORDER | SWT.V_SCROLL);
		elementsTree.setLayoutData(new GridData(350, 400));
		elementsTree.setBounds(10, 10, 350, 400);
		
		TreeItem tableItem = new TreeItem(elementsTree, SWT.NONE);
		tableItem.setImage(ImgUtil.createImg(ImgUtil.BC));
		tableItem.setText(bcTypeText.getText());
		tableItem.setData("bcType_id", bcTypeText.getData("bcType_id"));
		tableItem.setData("FirstTreeName",".");
		elementsTree.setData("bcType_id", bcTypeText.getData("bcType_id"));
//		elementsTree.setData("beLever", 1);
//		tableItem.setData("beLever", 1);
//		elementsTree.setData("bcName", bcTypeText.getText());
//		ArrayList<EcoreBusinessElement> beList=DerbyDaoUtil.getBizElementsByBizComponentId(String.valueOf(bcTypeText.getData("bcType_id")));
//		GenerateCommonTree.initList();  无限递归初始化list
//		allBeByComponentId = DerbyDaoUtil.getAllBeByComponentId();
		//加载bc下be
		//加载bc下sub_type
		if (bcTypeText.getData("bcType_id")!=null&&allSubBusiness!=null) {
			arrayList = allSubBusiness.get(bcTypeText.getData("bcType_id").toString());
			if (arrayList!=null) {
			arrayList.forEach(g -> {
			string = allBusinessComponent.get(g.getId());
			TreeItem subItem = new TreeItem(elementsTree, SWT.NONE);
			subItem.setImage(ImgUtil.createImg(ImgUtil.BC));
			subItem.setText("as "+string);
			subItem.setData("FirstTreeName","(as "+string+")");
			subItem.setData("subId",g.getId());
//			if (allBeByComponentId.get(g.getId())!=null) {
			arrayList3 = allSubBusiness.get(g.getId());
			if (arrayList3!=null||allBeByComponentId.get(g.getId())!=null) {
				//bc树头 AS和BE都为空则不构造树头
				TreeItem fistTree = new TreeItem(subItem, SWT.NONE);
//				String name=t.getName();
				fistTree.setText(string);
				fistTree.setImage(ImgUtil.createImg(ImgUtil.BC));
				fistTree.setData("FirstTreeName","("+subItem.getText()+")/.");
			}
			//AS-AS
			
			if (arrayList3!=null) {
//			TreeItem fistTree = new TreeItem(subItem, SWT.NONE);
////			String name=t.getName();
//			fistTree.setText(string);
//			fistTree.setImage(ImgUtil.createImg(ImgUtil.BC));
//			fistTree.setData("FirstTreeName","("+subItem.getText()+")/.");	
			arrayList3.forEach(h ->{
				
			string2 = allBusinessComponent.get(h.getId());
			TreeItem subItems = new TreeItem(subItem, SWT.NONE);
			subItems.setImage(ImgUtil.createImg(ImgUtil.BC));
			subItems.setText("as "+string2);
			subItems.setData("FirstTreeName",subItem.getData("FirstTreeName")+"/"+"as("+string2+")");
			
			//AS
			arrayList4 = allSubBusiness.get(h.getId());
			if (arrayList4!=null||allBeByComponentId.get(h.getId())!=null) {
			//bc树头 AS和BE都为空则不构造树头
			TreeItem fistTree_1 = new TreeItem(subItems, SWT.NONE);
			fistTree_1.setText(string2);
			fistTree_1.setImage(ImgUtil.createImg(ImgUtil.BC));
			fistTree_1.setData("FirstTreeName",subItems.getData("FirstTreeName")+"("+subItems.getText()+")/.");	
			}
			if (arrayList4!=null) {//为空则不构造下层节点
			
			arrayList4.forEach(f ->{
			string3 = allBusinessComponent.get(f.getId());
			TreeItem subItem_1 = new TreeItem(subItems, SWT.NONE);
			subItem_1.setImage(ImgUtil.createImg(ImgUtil.BC));
			subItem_1.setText("as "+string3);
			subItem_1.setData("FirstTreeName",subItems.getData("FirstTreeName")+"/"+string3);
			//AS-AS-AS-BE
			if (allBeByComponentId.get(f.getId())!=null) {
				
				GenerateCommonTree.generateMCOfBcSubTypeforDefineTrace(subItem_1,
					allBeByComponentId.get(f.getId()),allBeByComponentId,allSubBusiness,allBusinessComponent,allBeByTyId);
			}
			
			});
			
			}
			//AS-AS-BE
			if (allBeByComponentId.get(h.getId())!=null) {
				
				GenerateCommonTree.generateMCOfBcSubTypeforDefineTrace(subItems,
					allBeByComponentId.get(h.getId()),allBeByComponentId,allSubBusiness,allBusinessComponent,allBeByTyId);
			}
			});	
			}
			//AS-BE
			if (allBeByComponentId.get(g.getId())!=null) {
				
			GenerateCommonTree.generateMCOfBcSubTypeforDefineTrace(subItem,
				allBeByComponentId.get(g.getId()),allBeByComponentId,allSubBusiness,allBusinessComponent,allBeByTyId);
	
			}
			//			}	
					
			});
			
				
			}
		}
			
			
		
		//生成be节点
		if (allBeByComponentId!=null&&bcTypeText.getData("bcType_id")!=null&&allBeByComponentId.get(String.valueOf(bcTypeText.getData("bcType_id")))!=null) {
			GenerateCommonTree.generateMCDefineTrace(elementsTree,
				allBeByComponentId.get(bcTypeText.getData("bcType_id")),allBeByComponentId,allSubBusiness,allBusinessComponent,allBeByTyId);
		}
		
//		GenerateCommonTree.generateContainmentTreeForBComponent(tableItem ,
//				DerbyDaoUtil.getBizElementsByBizComponentId(String.valueOf(bcTypeText.getData("bcType_id"))),true);
//		GenerateCommonTree.generateContainmentTreeForBComponent(tableItem ,
//				DerbyDaoUtil.getSubBusinessComponentById(String.valueOf(bcTypeText.getData("bcType_id"))),true);
		Button bt=new Button(msgComponentTable,SWT.PUSH);
		bt.setText("OK");
		bt.setBounds(280, 420, 80, 30);
		bt.setBackground(new Color(Display.getCurrent(),135,206,250));
		bt.addListener(SWT.MouseDown,new Listener() {

			private String substring;
			private int indexOf;
			private String data;

			@Override
			public void handleEvent(Event event) {
				TreeItem[] selection = elementsTree.getSelection();
				String name=selection[0].getText();
				for (int i = 0; i < name.length(); i++) {
					if (name.contains("[")) {
						indexOf = name.indexOf("[");
						substring = name.substring(0,indexOf);
						item1.setImage(2,ImgUtil.createImg(ImgUtil.BC_BE));
						item1.setData("Image","1");
					}else if(name.contains("AS ")){
						indexOf = name.indexOf("AS ");
						substring = name.substring(indexOf+3,name.length());
						item1.setImage(2,ImgUtil.createImg(ImgUtil.BC));
						item1.setData("Image","2");
					}else{
						item1.setImage(2,ImgUtil.createImg(ImgUtil.BC));
						item1.setData("Image","2");
					}
				
				}
//				if (selection[0].getData("beLever")!=null) {
//					item1.setData("beLever",1);
//				}
				
				//根据树节点信息生成表格2
				if (substring!=null) {
					item1.setText(2,substring);
				}else {
					item1.setText(2,name);
				}
				//取出树中层级信息生成表格3数据
				if (selection[0].getData("FirstTreeName")!=null) {
					data = selection[0].getData("FirstTreeName").toString();
					item1.setText(3, data);
				}
				
				businessElementWindow.close();
			}
		
		});
//		elementsTree.addMouseListener(SWT.MouseDoubleClick,new MouseAdapter() {
//			@Override
//			public void mouseUp(MouseEvent e) {
//				TreeItem[] selection = elementsTree.getSelection();
//				item1.setText(2, selection[0].getText());
//				item1.setText(3, selection[0].getText());
//				businessElementWindow.close();
//			}
//		});
		businessElementWindow.setSize(400, 550);
		businessElementWindow.open();
		businessElementWindow.layout();
	}
	
//	//MC模块BusniessTrace页面,表格数据展示2(关联mc的me)
//	public static void createBusinessComponetTrace2(Text bcTypeText,TreeItem item1) {
//		Shell businessElementWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
//		businessElementWindow.setText("Define semantics");
//		businessElementWindow.setLayout(new FormLayout());
//		Composite c = new Composite(businessElementWindow, SWT.NONE);
////		FormData fd_c = new FormData();
////		fd_c.top = new FormAttachment(0);
////		fd_c.left = new FormAttachment(0);
////		fd_c.bottom = new FormAttachment(100);
////		fd_c.right = new FormAttachment(100);
//		Label nameLabel = new Label(c, SWT.NONE);
//		nameLabel.setText("Choose the trace target from AcceptorRole");
//		nameLabel.setBounds(10, 10, 260, 30);
//		
//		Composite msgComponentTable = new Composite(c, SWT.BORDER | SWT.MULTI);
//		msgComponentTable.setBounds(10, 40, 360, 480);
//		msgComponentTable.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
////		msgComponentTable.setLayout(new FillLayout(SWT.HORIZONTAL));
//		
//        Tree elementsTree = new Tree(msgComponentTable, SWT.BORDER );
//		elementsTree.setLayoutData(new GridData(350, 400));
//		elementsTree.setBounds(10, 50, 360, 480);
//		
//		TreeItem tableItem = new TreeItem(elementsTree, SWT.NONE);
//		tableItem.setImage(ImgUtil.createImg(ImgUtil.BC));
//		tableItem.setText(bcTypeText.getText());
//		tableItem.setData("bcType_id", bcTypeText.getData("bcType_id"));
//		GenerateCommonTree.generateContainmentTreeForBComponent(tableItem ,
//				DerbyDaoUtil.getBizElementsByBizComponentId(String.valueOf(bcTypeText.getData("TypeBcTrace"))),true);
////		GenerateCommonTree.generateContainmentTreeForBComponent(tableItem ,
////				DerbyDaoUtil.getSubBusinessComponentById(String.valueOf(bcTypeText.getData("bcType_id"))),true);
//		elementsTree.addListener(SWT.MouseDoubleClick,new Listener() {
//
//			@Override
//			public void handleEvent(Event event) {
//				TreeItem[] selection = elementsTree.getSelection();
//				item1.setText(2, selection[0].getText());
//				item1.setText(3, selection[0].getText());
//				businessElementWindow.close();
//			}
//		
//		});
////		elementsTree.addMouseListener(SWT.MouseDoubleClick,new MouseAdapter() {
////			@Override
////			public void mouseUp(MouseEvent e) {
////				TreeItem[] selection = elementsTree.getSelection();
////				item1.setText(2, selection[0].getText());
////				item1.setText(3, selection[0].getText());
////				businessElementWindow.close();
////			}
////		});
//		businessElementWindow.setSize(400, 500);
//		businessElementWindow.open();
//		businessElementWindow.layout();
//	}
}
