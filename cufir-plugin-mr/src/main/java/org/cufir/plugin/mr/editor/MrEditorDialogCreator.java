package org.cufir.plugin.mr.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.cufir.plugin.mr.MrHelper;
import org.cufir.plugin.mr.bean.ButtonPolicy;
import org.cufir.plugin.mr.bean.DataTypesEnum;
import org.cufir.plugin.mr.bean.MrTreeItem;
import org.cufir.plugin.mr.bean.RegistrationStatusEnum;
import org.cufir.plugin.mr.bean.TreeItemDataBean;
import org.cufir.plugin.mr.bean.TreeLevelEnum;
import org.cufir.plugin.mr.bean.TreeMenuEnum;
import org.cufir.plugin.mr.handlers.SaveHandler;
import org.cufir.plugin.mr.utils.ImgUtil;
import org.cufir.plugin.mr.utils.SystemUtil;
import org.cufir.s.ecore.bean.EcoreBusinessComponent;
import org.cufir.s.ecore.bean.EcoreBusinessComponentRL;
import org.cufir.s.ecore.bean.EcoreBusinessElement;
import org.cufir.s.ecore.bean.EcoreConstraint;
import org.cufir.s.ecore.bean.EcoreDataType;
import org.cufir.s.ecore.bean.EcoreExample;
import org.cufir.s.ecore.bean.EcoreMessageComponent;
import org.cufir.s.ecore.bean.EcoreMessageDefinition;
import org.cufir.s.ide.utils.i18n.I18nApi;
import org.cufir.s.data.vo.EcoreTreeNode;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
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

/**
 * 消息框（树功能、右侧窗口按钮等公用窗口功能）
 * @author tangmaoquan
 * @Date 2021年9月29日
 */
public class MrEditorDialogCreator {
	
	/**
	 * 删除失败提示
	 */
	public static void deleteFail() {
		Shell parent = new Shell(Display.getCurrent());
		MessageBox messageBox = new MessageBox(parent, SWT.OK | SWT.CANCEL);
		messageBox.setMessage("删除失败！");
		messageBox.open();
	}
	
	/**
	 * 报文保存名称重复提示
	 */
	public static void definitionSaveFail(Text nameValue, java.util.List<EcoreMessageDefinition> mds) {
		
		
		Shell parent = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL | SWT.ON_TOP);
		parent.setImage(ImgUtil.getImage("icons/ISOEditorLogo_128.gif"));
		parent.setText("Save Name Repeat");
		parent.setLayout(new FormLayout());
		Composite c = new Composite(parent, SWT.NONE);
		
		CLabel contextLabel = new CLabel(c, SWT.NONE);
		contextLabel.setImage(Display.getDefault().getSystemImage(SWT.ICON_ERROR));
		contextLabel.setText(I18nApi.get("tips.md.error.name.three"));
		contextLabel.setBounds(10, 5, 260, 70);
		
		Button finishButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"OK")).getButton();
		finishButton.setBounds(150, 85, 60, 30);
		finishButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				parent.close();
			}
		});
		
		Button cancelButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Assign Fresh Name")).getButton();
		cancelButton.setBounds(215, 85, 135, 30);
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				String name = MrEditorHelper.generateName(nameValue.getText(), mds);
				nameValue.setText(name);
				SaveHandler sh = new SaveHandler();
				try {
					sh.execute(null);
				} catch (ExecutionException ee) {
					ee.printStackTrace();
				}
				parent.close();
			}
		});
		
		parent.setBounds((Display.getDefault().getBounds().width - 376) / 2,
				(Display.getDefault().getBounds().height - 166) / 2, 376, 166);
		parent.open();
		parent.layout();
	}

	public static void showMessageBox(String message) {
		Shell parent = new Shell(Display.getCurrent());
		MessageBox messageBox = new MessageBox(parent, SWT.OK);
		messageBox.setMessage(message);
		messageBox.open();
	}

	/**
	 * 加载examples
	 */
	public static void createExamplesDialogue(List examplesList, boolean... isMe) {
		Shell examplesWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		examplesWindow.setText("Enter a value for Examples");
		examplesWindow.setLayout(new FormLayout());
		Composite c = new Composite(examplesWindow, SWT.NONE);

		Text nameText = new Text(c, SWT.BORDER);
		nameText.setBounds(10, 10, 350, 30);

		Button finishButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"OK")).getButton();
		finishButton.setBounds(280, 45, 60, 30);
		finishButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				examplesList.add(nameText.getText());
				examplesWindow.close();
			}
		});
		
		Button cancelButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Cancel")).getButton();
		cancelButton.setBounds(350, 45, 60, 30);
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				examplesWindow.close();
			}
		});

		examplesWindow.setBounds((Display.getDefault().getBounds().width - 480) / 2,
				(Display.getDefault().getBounds().height - 120) / 2, 480, 120);
		examplesWindow.open();
		examplesWindow.layout();
	}

	public static void createExamplesDialogue(Table examplesTable) {
		Shell examplesWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		examplesWindow.setText("Enter a value for Examples");
		examplesWindow.setLayout(new FormLayout());
		Composite c = new Composite(examplesWindow, SWT.NONE);

		Text nameText = new Text(c, SWT.BORDER);
		nameText.setBounds(10, 10, 350, 30);

		Button finishButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"OK")).getButton();
		finishButton.setBounds(280, 45, 60, 30);
		finishButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TableItem tableItem = new TableItem(examplesTable, SWT.NONE);
				tableItem.setText(nameText.getText());
				EcoreExample ecoreExample = new EcoreExample();
				ecoreExample.setExample(nameText.getText());
				ecoreExample.setId(UUID.randomUUID().toString());
				tableItem.setData(ecoreExample);
				examplesWindow.close();
			}
		});
		
		Button cancelButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Cancel")).getButton();
		cancelButton.setBounds(350, 45, 60, 30);
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				examplesWindow.close();
			}
		});

		examplesWindow.setBounds((Display.getDefault().getBounds().width - 480) / 2,
				(Display.getDefault().getBounds().height - 120) / 2, 480, 120);
		examplesWindow.open();
		examplesWindow.layout();
	}

	/**
	 * 加载namespaceList
	 */
	public static void createNamespaceListDialogue(List namespaceList) {
		Shell namespaceWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		namespaceWindow.setText("Enter a value for Namespace List");
		namespaceWindow.setLayout(new FormLayout());
		Composite c = new Composite(namespaceWindow, SWT.NONE);

		Text nameText = new Text(c, SWT.BORDER);
		nameText.setBounds(10, 10, 350, 30);

		Button finishButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"OK")).getButton();
		finishButton.setBounds(280, 45, 60, 30);
		finishButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				namespaceList.add(nameText.getText());
				namespaceWindow.close();
			}
		});
		
		Button cancelButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Cancel")).getButton();
		cancelButton.setBounds(350, 45, 60, 30);
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

		Label contextLabel = new Label(c, SWT.NONE);
		contextLabel.setText("Context");
		contextLabel.setBounds(10, 10, 80, 30);
		Text nameText = new Text(c, SWT.BORDER);
		nameText.setBounds(120, 10, 200, 30);

		Label synonymsLabel = new Label(c, SWT.NONE);
		synonymsLabel.setText("Synonums");
		synonymsLabel.setBounds(10, 50, 80, 30);
		Text synonymsText = new Text(c, SWT.BORDER);
		synonymsText.setBounds(120, 50, 200, 30);

		Button finishButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"OK")).getButton();
		finishButton.setBounds(90, 100, 60, 30);
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
					if (selectedTableItem[0] instanceof TreeItem) {
						((TreeItem) selectedTableItem[0]).setData("beSynonymsTableItems", textList);
					} else if (selectedTableItem[0] instanceof TableItem) {
						((TableItem) selectedTableItem[0]).setData("beSynonymsTableItems", textList);
					}
				}
				synonymsWindow.close();
			}
		});
		
		Button cancelButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Cancel")).getButton();
		cancelButton.setBounds(180, 100, 60, 30);
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
	 * @param examplesList
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
				if (selectedTableItem[0] instanceof TreeItem) {
					((TreeItem) selectedTableItem[0]).setData("beSynonymsTableItems", textList);
				} else if (selectedTableItem[0] instanceof TableItem) {
					((TableItem) selectedTableItem[0]).setData("beSynonymsTableItems", textList);
				}
			}
		}
	}

	/**
	 * 删除examples
	 * 
	 * @param examplesList
	 */
	public static void deleteExamplesDialogue(List examplesList) {
		// 删除操作
		String[] selection = examplesList.getSelection();
		if (selection != null && selection.length > 0) {
			examplesList.remove(examplesList.getSelectionIndex());
		}
	}

	public static void deleteExamplesDialogue(Table examplesTable) {
		// 删除操作
		for (TableItem tableItem : examplesTable.getSelection()) {
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
		expressionLanguageLabel.setBounds(10, 180, 130, 30);
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

		Button finishButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Finish")).getButton();
		finishButton.setBounds(280, 265, 60, 30);
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
		
		Button cancelButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Cancel")).getButton();
		cancelButton.setBounds(350, 265, 60, 30);
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
	public static void createConstraintDialogueForBC(String type, Table constraintsTable, Object... selectedTableItem) {
		Shell constraintWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		constraintWindow.setText("Constraint General Information");
		constraintWindow.setLayout(new FormLayout());
		Composite c = new Composite(constraintWindow, SWT.NONE);

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
		expressionLanguageLabel.setBounds(10, 180, 130, 30);
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

		Button finishButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Finish")).getButton();
		finishButton.setBounds(280, 265, 60, 30);
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
					if (selectedTableItem[0] instanceof TreeItem) {
						((TreeItem) selectedTableItem[0]).setData("beConstraintsTableItems", textList);
					} else if (selectedTableItem[0] instanceof TableItem) {
						((TableItem) selectedTableItem[0]).setData("beConstraintsTableItems", textList);
					}
				}
				constraintWindow.close();
			}
		});
		
		Button cancelButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Cancel")).getButton();
		cancelButton.setBounds(350, 265, 60, 30);
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
				if (selectedTableItem[0] instanceof TreeItem) {
					((TreeItem) selectedTableItem[0]).setData("beConstraintsTableItems", textList);
				} else if (selectedTableItem[0] instanceof TableItem) {
					((TableItem) selectedTableItem[0]).setData("beConstraintsTableItems", textList);
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
	private static void exchange(Table summaryConstraintsTable, Tree newCodeTree, String dataType) {
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

	public static void deleteTreeItemForMC(Tree constraintsTree) {
		if (constraintsTree.getSelectionCount() > 0) {
			TreeItem treeItem = constraintsTree.getSelection()[0];
			treeItem.dispose();
		}
	}

	/**
	 * 打开搜索框
	 */
	public static void createSearchDialogue(Text text, TreeItem treeItem) {

		java.util.List<EcoreBusinessComponent> businessComponents = MrRepository.get().ecoreBusinessComponents;
		java.util.List<EcoreDataType> dataTypes = MrRepository.get().ecoreDataTypes;

		Shell synonymsWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		synonymsWindow.setSize(400, 600);
		synonymsWindow.setText("select type");

		// 改变弹窗位置
		SystemUtil.center(synonymsWindow);

		Composite parentComp = new Composite(synonymsWindow, SWT.NONE);
		parentComp.setBounds(0, 0, 400, 600);
		
		Combo cb = new Combo(parentComp, SWT.READ_ONLY);
		cb.add("Basic Type");
		cb.add("Complex Type");
		cb.setBounds(20, 15, 350, 30);
		cb.select(0);

		Text seachText = new Text(parentComp, SWT.BORDER);
		seachText.setBounds(20, 50, 270, 30);
		
		Table table = new Table(parentComp, SWT.BORDER | SWT.VIRTUAL);
		table.setLinesVisible(true);
		table.setBounds(20, 85, 350, 400);
		addItem(table, cb.getText(), businessComponents, dataTypes);
		
		cb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addItem(table, cb.getText(), businessComponents, dataTypes);
			}
		});

		Button seachBtn = new SummaryRowTextComposite(parentComp, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Search")).getButton();
		seachBtn.setBounds(300, 50, 60, 30);
		seachBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String type = cb.getText();
				java.util.List<EcoreDataType> dts = dataTypes;
				java.util.List<EcoreBusinessComponent> bcs = businessComponents;
				if (type.equals("Basic Type")) {
					dts = dataTypes.stream().filter(dt->dt.getName().contains(seachText.getText())).collect(Collectors.toList());
				}else if (type.equals("Complex Type")) {
					bcs = businessComponents.stream().filter(bc->bc.getName().contains(seachText.getText())).collect(Collectors.toList());
				}
				addItem(table, cb.getText(), bcs, dts);
			}
		});

		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] selection = table.getSelection();
				seachText.setText(selection[0].getText());
			}
		});

		Button okBtn = new SummaryRowTextComposite(parentComp, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"choose")).getButton();
		okBtn.setBounds(180, 510, 80, 30);
		okBtn.addListener(SWT.MouseDown, p -> {
			TableItem[] selection = table.getSelection();
			for (TableItem Item : selection) {
				text.setData("beType_id", Item.getData("id"));
				text.setData("beType", Item.getData("beType"));
				text.setData("beStatusCombo", Item.getData("status"));
				text.setData("meLever", 1);
				text.setText(Item.getText());
			}
			treeItem.setData("beType_id", text.getData("beType_id"));
			treeItem.setData("beType", text.getData("beType"));
			treeItem.setData("beStatusCombo", text.getData("beStatusCombo"));
			TreeItem[] items = treeItem.getItems();
			for (TreeItem trees : items) {
				trees.dispose();
			}
			MrEditorTreeCreator mrEditorTreeCreator = new MrEditorTreeCreator();
			if (text.getData("beType").equals("1")) {
				mrEditorTreeCreator.generateContainmentTreeForBComponentDataType(treeItem);
			}
			mrEditorTreeCreator.generateContainmentTreeForBComponent(treeItem,
					MrHelper.getBizElementsByBizComponentId(String.valueOf(text.getData("beType_id"))), true);
			synonymsWindow.close();
		});

		Button calBtn = new SummaryRowTextComposite(parentComp, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Cancel")).getButton();
		calBtn.setBounds(280, 510, 80, 30);
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
	public static void selectBCTypes(Tree tabTree, String typeName, String id) throws Exception {

		Shell selectBCWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		java.util.List<EcoreBusinessComponent> businessComponents = MrRepository.get().ecoreBusinessComponents;

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
		parentItem.setText(TreeMenuEnum.BUSINESS_COMPONENTS.getName());
		parentItem.setImage(ImgUtil.createImg(ImgUtil.BUSINESS_COMPONENTS));

		for (EcoreBusinessComponent businessComponent : businessComponents) {
			TreeItem item = new TreeItem(parentItem, SWT.NONE);
			item.setText(businessComponent.getName());
			item.setData("id", businessComponent.getId());
			// 之前选中的
			if (selectedItemMapBefore != null && selectedItemMapBefore.get(item.getText()) != null) {
				// 之前选中的在右边展示
				TreeItem itemBeforeSelected = new TreeItem(selTree, SWT.NONE);
				itemBeforeSelected.setText(businessComponent.getName());
				itemBeforeSelected.setImage(ImgUtil.createImg(ImgUtil.BC_SEARCH_SELECTED_YES));
				itemBeforeSelected.setData("id", businessComponent.getId());

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
					for (EcoreBusinessComponent businessComponent : businessComponents) {
						if (businessComponent.getName().toUpperCase()
								.indexOf(text.toString().toUpperCase()) >= 0) {
							TreeItem treeItem = new TreeItem(parentItem, SWT.NONE);
							treeItem.setImage(ImgUtil.createImg(ImgUtil.BC_SEARCH_SELECTED_NO));
							treeItem.setText(businessComponent.getName());
							treeItem.setData("id", businessComponent.getId());
							if (selectedItemMapAfter.get(businessComponent.getName()) != null) {
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
					if (typeName.equals("Sub-Types")) {
						selectFlag = true;
					} else if (typeName.equals("Super-Types")) {
						// 只能一个
						if (selectedItemMapAfter.size() == 1) {
							selectFlag = false;
						} else {
							selectFlag = true;
						}
					}
					if (selectFlag) {
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

		Button okBtn = new SummaryRowTextComposite(parentComp, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"choose")).getButton();
		okBtn.setBounds(180, 510, 80, 30);
		okBtn.addListener(SWT.MouseUp, p -> {

			java.util.List<EcoreBusinessComponentRL> addBusinessComponentRLs = new ArrayList<EcoreBusinessComponentRL>();
			java.util.List<String> deleteBusinessComponentRLIds = new ArrayList<String>();

			Set<String> keySetBefore = selectedItemMapBefore.keySet();
			keySetBefore.stream().forEach(t -> {
				EcoreTreeNode ecoreTreeNode = (EcoreTreeNode) selectedItemMapBefore.get(t).getData("EcoreTreeNode");
				deleteBusinessComponentRLIds.add(ecoreTreeNode.getId());
				selectedItemMapBefore.get(t).dispose();
			});
			Set<String> keySetAfter = selectedItemMapAfter.keySet();

			MrTreeItem treeItem = (MrTreeItem) tabTree.getSelection()[0];

			keySetAfter.stream().forEach(t -> {
				MrTreeItem treeListItem = new MrTreeItem(treeItem,
						new TreeItemDataBean(selectedItemMapAfter.get(t).getData("id").toString(), t, ImgUtil.BC, "2",
								TreeLevelEnum.LEVEL_4.getLevel()));
				tabTree.setSelection(treeListItem);
				treeListItem.getParentItem().setExpanded(true);
				treeListItem.setExpanded(true);
				treeListItem.setData("id", selectedItemMapAfter.get(t).getData("id"));

				// 设置打开事件必备
				EcoreTreeNode ecoreTreeNodeDT = new EcoreTreeNode();
				ecoreTreeNodeDT.setId(selectedItemMapAfter.get(t).getData("id").toString());
				ecoreTreeNodeDT.setType(TreeMenuEnum.BUSINESS_COMPONENTS.getName());
				ecoreTreeNodeDT.setLevel(TreeLevelEnum.LEVEL_4.getLevel());
				ecoreTreeNodeDT.setName(t);
				treeListItem.setData("type", TreeMenuEnum.BUSINESS_COMPONENTS.getName());
				treeListItem.setData("EcoreTreeNode", ecoreTreeNodeDT);
				if (typeName.equals("Sub-Types")) {
					EcoreBusinessComponentRL businessComponentRL = new EcoreBusinessComponentRL();
					businessComponentRL.setId(treeListItem.getData("id").toString());
					businessComponentRL.setPId(id);
					addBusinessComponentRLs.add(businessComponentRL);
				} else {
					EcoreBusinessComponentRL businessComponentRL = new EcoreBusinessComponentRL();
					businessComponentRL.setId(id);
					businessComponentRL.setPId(treeListItem.getData("id").toString());
					addBusinessComponentRLs.add(businessComponentRL);
				}
			});

			// 入库
			try {
				// 删除之前的
				if (deleteBusinessComponentRLIds != null && deleteBusinessComponentRLIds.size() > 0) {
					deleteBusinessComponentRLIds.forEach(t -> {
						try {
							MrImplManager.get().getEcoreBusinessComponentRLImpl().deleteEcoreBusinessComponentRL(t);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					});
				}
				// 添加新的
				if (addBusinessComponentRLs != null && addBusinessComponentRLs.size() > 0) {
					MrImplManager.get().getEcoreBusinessComponentRLImpl().saveEcoreBusinessComponentRL(addBusinessComponentRLs);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			selectBCWindow.close();
		});

		Button calBtn = new SummaryRowTextComposite(parentComp, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Cancel")).getButton();
		calBtn.setBounds(280, 510, 80, 30);
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
		expressionLanguageLabel.setBounds(10, 180, 130, 30);
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

		Button finishButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Finish")).getButton();
		finishButton.setBounds(280, 265, 60, 30);
		finishButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TableItem tableItem = new TableItem(constraintsTable, SWT.NONE);
				tableItem.setText(new String[] { nameText.getText(), docText.getText(),
						expressionLanguageText.getText(), expressionText.getText() });

				EcoreConstraint ecoreConstraint = new EcoreConstraint();
				ecoreConstraint.setId(tableItem.getData("id").toString() == null ? UUID.randomUUID().toString()
						: tableItem.getData("id").toString());
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
					}
					selectedTableItem[0].setData("beConstraintsTableItems", textList);
				}
				constraintWindow.close();
			}
		});
		
		Button cancelButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Cancel")).getButton();
		cancelButton.setBounds(350, 265, 60, 30);
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
	 * 创建选择me的框
	 * 
	 * @param typeText
	 * @throws Exception
	 */
	public static void createSelectMsgElementType(Text typeText) throws Exception {

		java.util.List<EcoreMessageComponent> mcs = MrRepository.get().ecoreMessageComponents;
		java.util.List<EcoreDataType> dts = MrRepository.get().ecoreDataTypes;
		
		java.util.List<EcoreMessageComponent> emcs = mcs.stream().filter(mc -> !RegistrationStatusEnum.Obsolete.getStatus().equals(mc.getRegistrationStatus())).collect(Collectors.toList());
		java.util.List<EcoreDataType> edts = dts.stream().filter(dt -> !RegistrationStatusEnum.Obsolete.getStatus().equals(dt.getRegistrationStatus())).collect(Collectors.toList());

		Shell messageComponentWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		messageComponentWindow.setText("Select Message Element type:");
		messageComponentWindow.setLayout(new FormLayout());

		// 改变弹窗位置
		SystemUtil.center(messageComponentWindow);

		Composite c = new Composite(messageComponentWindow, SWT.NONE);
		Text searchText = new Text(c, SWT.BORDER);
		searchText.setBounds(10, 10, 270, 30);
		Button searchBtn = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Search")).getButton();
		searchBtn.setBounds(290, 10, 60, 30);

		Table msgComponentTable = new Table(c, SWT.BORDER | SWT.VIRTUAL);
		msgComponentTable.setLinesVisible(true);
		msgComponentTable.setBounds(10, 50, 350, 400);
		setChooseTable(msgComponentTable, emcs, edts);
		msgComponentTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				searchText.setText("" + msgComponentTable.getSelection()[0].getText());
			}
		});
		searchBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				msgComponentTable.removeAll();
				java.util.List<EcoreMessageComponent> smcs = emcs.stream().filter(t -> (t.getName() != null && t.getName().toUpperCase().indexOf(searchText.getText().toUpperCase()) != -1)).collect(Collectors.toList());
				java.util.List<EcoreDataType> sdts = edts.stream().filter(t -> (t.getName() != null && t.getName().toUpperCase().indexOf(searchText.getText().toUpperCase()) != -1)).collect(Collectors.toList());
				setChooseTable(msgComponentTable, smcs, sdts);
			}
		});
		Button okButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"OK")).getButton();
		okButton.setBounds(275, 460, 35, 30);
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
		
		Button cancelButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Cancel")).getButton();
		cancelButton.setBounds(320, 460, 52, 30);
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

	private static void setChooseTable(Table msgComponentTable, java.util.List<EcoreMessageComponent> mcs, java.util.List<EcoreDataType> dts) {
		msgComponentTable.addListener(SWT.SetData, new Listener(){
			@Override
			public void handleEvent(Event event) {
				TableItem tableItem = (TableItem)event.item;
				if(mcs.size() > event.index) {
					EcoreMessageComponent mc = mcs.get(event.index);
					setMessageComponentTable(tableItem, mc);
				}else {
					int a = event.index - mcs.size();
					EcoreDataType dt = dts.get(a);
					tableItem.setText(dt.getName() == null ? "" : dt.getName());
					tableItem.setData("id", dt.getId());
					tableItem.setData("meType", "1");
					tableItem.setData("status", dt.getRegistrationStatus());
					
					if (dt != null && dt.getType() != null) {
						if (DataTypesEnum.CODE_SETS.getType().equals(dt.getType())) {
							if (dt.getTrace() == null) {
								tableItem.setImage(ImgUtil.createImg(ImgUtil.CODE_SET_SUB1_2));
							} else {
								tableItem.setImage(ImgUtil.createImg(ImgUtil.CODE_SET_SUB1));
							}
						} else if (DataTypesEnum.TEXT.getType().equals(dt.getType())) {
							tableItem.setImage(ImgUtil.createImg(ImgUtil.TEXT_SUB1));
						} else if (DataTypesEnum.BOOLEAN.getType().equals(dt.getType())) {
							tableItem.setImage(ImgUtil.createImg(ImgUtil.BOOLEAN_SUB1));
						} else if (DataTypesEnum.INDICATOR.getType().equals(dt.getType())) {
							tableItem.setImage(ImgUtil.createImg(ImgUtil.INDICATOR_SUB1));
						} else if (DataTypesEnum.DECIMAL.getType().equals(dt.getType())) {
							tableItem.setImage(ImgUtil.createImg(ImgUtil.DECIMAL_SUB1));
						} else if (DataTypesEnum.RATE.getType().equals(dt.getType())) {
							tableItem.setImage(ImgUtil.createImg(ImgUtil.RATE_SUB1));
						} else if (DataTypesEnum.AMOUNT.getType().equals(dt.getType())) {
							tableItem.setImage(ImgUtil.createImg(ImgUtil.AMOUNT_SUB1));
						} else if (DataTypesEnum.QUANTITY.getType().equals(dt.getType())) {
							tableItem.setImage(ImgUtil.createImg(ImgUtil.QUANTITY_SUB1));
						} else if (DataTypesEnum.TIME.getType().equals(dt.getType())) {
							tableItem.setImage(ImgUtil.createImg(ImgUtil.TIME_SUB1));
						} else if (DataTypesEnum.BINARY.getType().equals(dt.getType())) {
							tableItem.setImage(ImgUtil.createImg(ImgUtil.BINARY_SUB1));
						} else if (DataTypesEnum.SCHEMA_TYPES.getType().equals(dt.getType())) {
							tableItem.setImage(ImgUtil.createImg(ImgUtil.SCHEMA_TYPES_SUB1));
						} else if (DataTypesEnum.USER_DEFINED.getType().equals(dt.getType())) {
							tableItem.setImage(ImgUtil.createImg(ImgUtil.USER_DEFINED_SUB1));
						}
					}
				}
			}
		});
		msgComponentTable.setItemCount(mcs.size() + dts.size());
	}
	
	private static void setMessageComponentTable(TableItem tableItem, EcoreMessageComponent mc) {
		tableItem.setText(mc.getName() == null ? "" : mc.getName());
		tableItem.setData("id", mc.getId());
		tableItem.setData("meType", "2");
		tableItem.setData("status", mc.getRegistrationStatus());
		if ("1".equals(mc.getComponentType())) {
			if (mc.getRegistrationStatus() == null || mc.getRegistrationStatus().equals("Provisionally Registered")) {
				tableItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB3_COMPONENT));
			} else {
				tableItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_COMPONENT));
			}
		} else if ("2".equals(mc.getComponentType())) {
			if (mc.getRegistrationStatus() == null || mc.getRegistrationStatus().equals("Provisionally Registered")) {
				tableItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_CHOICE1));
			} else {
				tableItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_CHOICE));
			}
		}
	}
	
	/**
	 * 创建选择me的框
	 * 
	 * @param typeText
	 * @throws Exception
	 */
	public static void createSelectMsgElementType(Text typeText, TreeItem treeItem) throws Exception {
		java.util.List<EcoreMessageComponent> mcs = MrRepository.get().ecoreMessageComponents;
		java.util.List<EcoreMessageComponent> emcs = mcs.stream().filter(mc -> !RegistrationStatusEnum.Obsolete.getStatus().equals(mc.getRegistrationStatus())).collect(Collectors.toList());
		Shell messageComponentWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		messageComponentWindow.setText("Select Message Element type:");
		messageComponentWindow.setLayout(new FormLayout());
		// 改变弹窗位置
		SystemUtil.center(messageComponentWindow);
		Composite c = new Composite(messageComponentWindow, SWT.NONE);
		Text searchText = new Text(c, SWT.BORDER);
		searchText.setBounds(10, 10, 270, 30);
		
		Button searchBtn = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Search")).getButton();
		searchBtn.setBounds(290, 10, 60, 30);

		Table msgComponentTable = new Table(c, SWT.BORDER | SWT.VIRTUAL);
		msgComponentTable.setLinesVisible(true);
		msgComponentTable.setBounds(10, 50, 350, 400);
		msgComponentTable.addListener(SWT.SetData, new Listener(){
			@Override
			public void handleEvent(Event event) {
				TableItem tableItem = (TableItem)event.item;
				EcoreMessageComponent mc = emcs.get(event.index);
				setMessageComponentTable(tableItem, mc);
			}
		});
		msgComponentTable.setItemCount(emcs.size());
		msgComponentTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				searchText.setText("" + msgComponentTable.getSelection()[0].getText());
			}
		});
		searchBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				msgComponentTable.removeAll();
				java.util.List<EcoreMessageComponent> smcs = emcs.stream().filter(t -> (t.getName() != null && t.getName().toUpperCase().indexOf(searchText.getText().toUpperCase()) != -1)).collect(Collectors.toList());
				msgComponentTable.addListener(SWT.SetData, new Listener(){
					@Override
					public void handleEvent(Event event) {
						TableItem tableItem = (TableItem)event.item;
						EcoreMessageComponent mc = smcs.get(event.index);
						setMessageComponentTable(tableItem, mc);
					}
				});
				msgComponentTable.setItemCount(smcs.size());
			}
		});
		
		Button okButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"OK")).getButton();
		okButton.setBounds(275, 460, 35, 30);
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
				treeItem.setData("meTypeName", typeText.getText());
				treeItem.setData("meStatusCombo", typeText.getData("status"));
				TreeItem[] items = treeItem.getItems();
				for (TreeItem tree : items) {
					tree.dispose();
				}
				new MrEditorTreeCreator().generateContainmentTreeForMessageComponent(treeItem,
						MrHelper.getMsgElementList(typeText.getData("meType_id").toString()), true);
				messageComponentWindow.close();
			}
		});
		Button cancelButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Cancel")).getButton();
		cancelButton.setBounds(320, 460, 52, 30);
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

		java.util.List<EcoreBusinessComponent> businessComponents = MrRepository.get().ecoreBusinessComponents;
		java.util.List<EcoreDataType> dataTypes = MrRepository.get().ecoreDataTypes;

		Shell synonymsWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		synonymsWindow.setSize(400, 600);
		synonymsWindow.setText("select type");

		// 改变弹窗位置
		SystemUtil.center(synonymsWindow);

		Composite parentComp = new Composite(synonymsWindow, SWT.NONE);
		parentComp.setBounds(0, 0, 400, 600);

		Combo cb = new Combo(parentComp, SWT.READ_ONLY);
		cb.add("Basic Type");
		cb.add("Complex Type");
		cb.setBounds(20, 15, 350, 30);
		cb.select(0);
		
		
		Text seachText = new Text(parentComp, SWT.BORDER);
		seachText.setBounds(20, 50, 270, 30);

		Table table = new Table(parentComp, SWT.BORDER | SWT.VIRTUAL);
		table.setLinesVisible(true);
		table.setBounds(20, 85, 350, 400);
		addItem(table, cb.getText(), businessComponents, dataTypes);
		
		cb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addItem(table, cb.getText(), businessComponents, dataTypes);
			}
		});
		Button seachBtn = new SummaryRowTextComposite(parentComp, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_DEEP,"Search")).getButton();
		seachBtn.setBounds(300, 50, 60, 30);
		seachBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String type = cb.getText();
				java.util.List<EcoreDataType> dts = dataTypes;
				java.util.List<EcoreBusinessComponent> bcs = businessComponents;
				if (type.equals("Basic Type")) {
					dts = dataTypes.stream().filter(dt->dt.getName().contains(seachText.getText())).collect(Collectors.toList());
				}else if (type.equals("Complex Type")) {
					bcs = businessComponents.stream().filter(bc->bc.getName().contains(seachText.getText())).collect(Collectors.toList());
				}
				addItem(table, cb.getText(), bcs, dts);
			}
		});

		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] selection = table.getSelection();
				seachText.setText(selection[0].getText());
			}
		});
		Button okButton = new SummaryRowTextComposite(parentComp, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"OK")).getButton();
		okButton.setBounds(275, 500, 35, 30);
		okButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TableItem[] tableItemArray = table.getSelection();
				for (TableItem treeItem : tableItemArray) {
					typeText.setData("beType_id", treeItem.getData("id"));
					typeText.setData("beType", treeItem.getData("beType"));
					typeText.setData("status", treeItem.getData("status"));
					typeText.setText(treeItem.getText());
				}
				synonymsWindow.close();
			}
		});
		Button cancelButton = new SummaryRowTextComposite(parentComp, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Cancel")).getButton();
		cancelButton.setBounds(320, 500, 52, 30);
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

	private static void addItem(Table table, String type, java.util.List<EcoreBusinessComponent> businessComponents, java.util.List<EcoreDataType> dataTypes) {
		table.addListener(SWT.SetData, new Listener(){
			@Override
			public void handleEvent(Event event) {
				table.clearAll();
				if (type.equals("Basic Type")) {
					TableItem tableItem = (TableItem)event.item;
					if(dataTypes.size() > event.index) {
						EcoreDataType dt = dataTypes.get(event.index);
						tableItem.setText(dt.getName());
						tableItem.setData("id", dt.getId());
						tableItem.setData("beType", dt.getType());
						tableItem.setData("status", dt.getRegistrationStatus());
						if (tableItem != null && dt.getType() != null) {
							if (DataTypesEnum.CODE_SETS.getType().equals(dt.getType())) {
								if (dt.getTrace() == null) {
									tableItem.setImage(ImgUtil.createImg(ImgUtil.CODE_SET_SUB1_2));
								} else {
									tableItem.setImage(ImgUtil.createImg(ImgUtil.CODE_SET_SUB1));
								}
							} else if (DataTypesEnum.TEXT.getType().equals(dt.getType())) {
								tableItem.setImage(ImgUtil.createImg(ImgUtil.TEXT_SUB1));
							} else if (DataTypesEnum.BOOLEAN.getType().equals(dt.getType())) {
								tableItem.setImage(ImgUtil.createImg(ImgUtil.BOOLEAN_SUB1));
							} else if (DataTypesEnum.INDICATOR.getType().equals(dt.getType())) {
								tableItem.setImage(ImgUtil.createImg(ImgUtil.INDICATOR_SUB1));
							} else if (DataTypesEnum.DECIMAL.getType().equals(dt.getType())) {
								tableItem.setImage(ImgUtil.createImg(ImgUtil.DECIMAL_SUB1));
							} else if (DataTypesEnum.RATE.getType().equals(dt.getType())) {
								tableItem.setImage(ImgUtil.createImg(ImgUtil.RATE_SUB1));
							} else if (DataTypesEnum.AMOUNT.getType().equals(dt.getType())) {
								tableItem.setImage(ImgUtil.createImg(ImgUtil.AMOUNT_SUB1));
							} else if (DataTypesEnum.QUANTITY.getType().equals(dt.getType())) {
								tableItem.setImage(ImgUtil.createImg(ImgUtil.QUANTITY_SUB1));
							} else if (DataTypesEnum.TIME.getType().equals(dt.getType())) {
								tableItem.setImage(ImgUtil.createImg(ImgUtil.TIME_SUB1));
							} else if (DataTypesEnum.BINARY.getType().equals(dt.getType())) {
								tableItem.setImage(ImgUtil.createImg(ImgUtil.BINARY_SUB1));
							} else if (DataTypesEnum.SCHEMA_TYPES.getType().equals(dt.getType())) {
								tableItem.setImage(ImgUtil.createImg(ImgUtil.SCHEMA_TYPES_SUB1));
							} else if (DataTypesEnum.USER_DEFINED.getType().equals(dt.getType())) {
								tableItem.setImage(ImgUtil.createImg(ImgUtil.USER_DEFINED_SUB1));
							}
						}
					}
				} else if (type.equals("Complex Type")) {
					TableItem tableItem = (TableItem)event.item;
					if(businessComponents.size() > event.index) {
						EcoreBusinessComponent bc = businessComponents.get(event.index);
						tableItem.setText(bc.getName() == null ? "" : bc.getName());
						tableItem.setData("id", bc.getId());
						tableItem.setData("beType", "2");
						tableItem.setData("status", bc.getRegistrationStatus());
						tableItem.setImage(ImgUtil.createImg(ImgUtil.BC));
					}
				}
				table.redraw();
			}
		});
		if (type.equals("Basic Type")) {
			table.setItemCount(dataTypes.size());
		}else if (type.equals("Complex Type")) {
			table.setItemCount(businessComponents.size());
		}
	}
	
	public static void createSelectBusinessTrace(Text text, Tree tabletree) throws Exception {

		java.util.List<EcoreBusinessComponent> businessComponents = MrRepository.get().ecoreBusinessComponents;
		Shell synonymsWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		synonymsWindow.setSize(400, 600);
		synonymsWindow.setText("select type");

		// 改变弹窗位置
		SystemUtil.center(synonymsWindow);

		Composite parentComp = new Composite(synonymsWindow, SWT.NONE);
		parentComp.setBounds(0, 0, 400, 600);

		Tree tree = new Tree(parentComp, SWT.V_SCROLL);
		tree.setBounds(20, 85, 350, 400);

		Text seachText = new Text(parentComp, SWT.BORDER);
		seachText.setBounds(20, 50, 270, 30);
		Button seachBtn = new SummaryRowTextComposite(parentComp, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_DEEP,"Search")).getButton();
		seachBtn.setBounds(300, 50, 60, 30);
		seachBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tree.removeAll();
				tree.removeAll();
				businessComponents.stream()
						.filter(t -> (t.getName() != null
								&& t.getName().toUpperCase().indexOf(seachText.getText().toUpperCase()) != -1))
						.forEach(t -> {
							TreeItem item = new TreeItem(tree, SWT.NONE);
							item.setText(t.getName() == null ? "" : t.getName());
							item.setData("id", t.getId());
							item.setData("beType", "2");
							item.setData("status", t.getRegistrationStatus());
							item.setData("bcName", t.getName());
							item.setImage(ImgUtil.createImg(ImgUtil.BC));
						});
			}
		});

		businessComponents.forEach(t -> {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText(t.getName() == null ? "" : t.getName());
			item.setData("id", t.getId());
			item.setData("beType", "2");
			item.setData("status", t.getRegistrationStatus());
			item.setData("bcName", t.getName());
			item.setImage(ImgUtil.createImg(ImgUtil.BC));
		});
		Button okButton = new SummaryRowTextComposite(parentComp, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"OK")).getButton();
		okButton.setBounds(280, 510, 80, 30);
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
			synonymsWindow.close();
		});
		Button calBtn = new SummaryRowTextComposite(parentComp, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Cancel")).getButton();
		calBtn.setBounds(280, 510, 80, 30);
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

		java.util.List<EcoreBusinessComponent> bcs = MrRepository.get().ecoreBusinessComponents;

		Shell businessComponentWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		businessComponentWindow.setText("Select Business Component type:");
		businessComponentWindow.setLayout(new FormLayout());

		// 改变弹窗位置
		SystemUtil.center(businessComponentWindow);

		Composite c = new Composite(businessComponentWindow, SWT.NONE);
		Text searchText = new Text(c, SWT.BORDER);
		searchText.setBounds(10, 10, 270, 30);
		Button searchBtn = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_DEEP,"Search")).getButton();
		searchBtn.setBounds(290, 10, 60, 30);

		Table businessComponentTable = new Table(c, SWT.BORDER | SWT.VIRTUAL);
		businessComponentTable.setLinesVisible(true);
		businessComponentTable.setBounds(10, 50, 350, 400);

		searchBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				businessComponentTable.removeAll();
				java.util.List<EcoreBusinessComponent> ebcs = bcs.stream().filter(t -> (t.getName() != null && t.getName().toUpperCase().indexOf(searchText.getText().toUpperCase()) != -1)).collect(Collectors.toList());
				setEcoreBusinessComponentTable(businessComponentTable, ebcs);
			}
		});
		setEcoreBusinessComponentTable(businessComponentTable, bcs);
		businessComponentTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				searchText.setText("" + businessComponentTable.getSelection()[0].getText());
			}
		});
		Button okButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"OK")).getButton();
		okButton.setBounds(275, 460, 35, 30);
		okButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TableItem[] tableItemArray = businessComponentTable.getSelection();
				for (TableItem treeItem : tableItemArray) {
					typeText.setText(treeItem.getText());
					typeText.setData("bc_id", treeItem.getData("id"));
				}
				businessComponentWindow.close();
			}
		});
		Button cancelButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Cancel")).getButton();
		cancelButton.setBounds(320, 460, 52, 30);
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

	private static void setEcoreBusinessComponentTable(Table businessComponentTable, java.util.List<EcoreBusinessComponent> bcs) {
		businessComponentTable.addListener(SWT.SetData, new Listener(){
			@Override
			public void handleEvent(Event event) {
				TableItem tableItem = (TableItem)event.item;
				EcoreBusinessComponent bc = bcs.get(event.index);
				tableItem.setText(bc.getName() == null ? "" : bc.getName());
				tableItem.setData("id", bc.getId());
				tableItem.setImage(ImgUtil.createImg(ImgUtil.BC));
			}
		});
		businessComponentTable.setItemCount(bcs.size());
	}
	
	// MC模块BusniessTrace页面,表格数据展示1
	public static void createBusinessComponetTrace(Text bcTypeText, TreeItem item1,
			Map<String, ArrayList<EcoreBusinessElement>> allBeByComponentId,
			Map<String, ArrayList<EcoreTreeNode>> allSubBusiness, Map<String, String> allBusinessComponent) {
		Shell businessElementWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		businessElementWindow.setText("Define semantics");

		// 改变弹窗位置
		SystemUtil.center(businessElementWindow);

		Composite c = new Composite(businessElementWindow, SWT.NONE);
		GridData gr = new GridData();
		gr.heightHint = 1200;
		c.setBounds(0, 0, 380, 1200);
		c.setLayoutData(gr);
		Label nameLabel = new Label(c, SWT.NONE);
		nameLabel.setText("Choose the trace target from AcceptorRole");
		nameLabel.setBounds(10, 10, 260, 30);

		Composite msgComponentTable = new Composite(c, SWT.BORDER | SWT.MULTI);
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
		tableItem.setData("FirstTreeName", ".");
		elementsTree.setData("bcType_id", bcTypeText.getData("bcType_id"));
		MrEditorTreeCreator mrEditorTreeCreator = new MrEditorTreeCreator();
		if (bcTypeText.getData("bcType_id") != null && allSubBusiness != null) {
			arrayList = allSubBusiness.get(bcTypeText.getData("bcType_id").toString());
			if (arrayList != null) {
				arrayList.forEach(g -> {
					string = allBusinessComponent.get(g.getId());
					TreeItem subItem = new TreeItem(elementsTree, SWT.NONE);
					subItem.setImage(ImgUtil.createImg(ImgUtil.BC));
					subItem.setText("as " + string);
					subItem.setData("FirstTreeName", "(as " + string + ")");
					subItem.setData("subId", g.getId());
					arrayList3 = allSubBusiness.get(g.getId());
					if (arrayList3 != null || allBeByComponentId.get(g.getId()) != null) {
						// bc树头 AS和BE都为空则不构造树头
						TreeItem fistTree = new TreeItem(subItem, SWT.NONE);
						fistTree.setText(string);
						fistTree.setImage(ImgUtil.createImg(ImgUtil.BC));
						fistTree.setData("FirstTreeName", "(" + subItem.getText() + ")/.");
					}
					// AS-AS
					if (arrayList3 != null) {
						arrayList3.forEach(h -> {
							string2 = allBusinessComponent.get(h.getId());
							TreeItem subItems = new TreeItem(subItem, SWT.NONE);
							subItems.setImage(ImgUtil.createImg(ImgUtil.BC));
							subItems.setText("as " + string2);
							subItems.setData("FirstTreeName",
									subItem.getData("FirstTreeName") + "/" + "as(" + string2 + ")");
							// AS
							arrayList4 = allSubBusiness.get(h.getId());
							if (arrayList4 != null || allBeByComponentId.get(h.getId()) != null) {
								// bc树头 AS和BE都为空则不构造树头
								TreeItem fistTree_1 = new TreeItem(subItems, SWT.NONE);
								fistTree_1.setText(string2);
								fistTree_1.setImage(ImgUtil.createImg(ImgUtil.BC));
								fistTree_1.setData("FirstTreeName",
										subItems.getData("FirstTreeName") + "(" + subItems.getText() + ")/.");
							}
							if (arrayList4 != null) {// 为空则不构造下层节点
								arrayList4.forEach(f -> {
									string3 = allBusinessComponent.get(f.getId());
									TreeItem subItem_1 = new TreeItem(subItems, SWT.NONE);
									subItem_1.setImage(ImgUtil.createImg(ImgUtil.BC));
									subItem_1.setText("as " + string3);
									subItem_1.setData("FirstTreeName",
											subItems.getData("FirstTreeName") + "/" + string3);
									// AS-AS-AS-BE
									if (allBeByComponentId.get(f.getId()) != null) {
										mrEditorTreeCreator.generateMCOfBcSubTypeforDefineTrace(subItem_1,
												allBeByComponentId.get(f.getId()), allBeByComponentId, allSubBusiness,
												allBusinessComponent);
									}
								});
							}
							// AS-AS-BE
							if (allBeByComponentId.get(h.getId()) != null) {
								mrEditorTreeCreator.generateMCOfBcSubTypeforDefineTrace(subItems,
										allBeByComponentId.get(h.getId()), allBeByComponentId, allSubBusiness,
										allBusinessComponent);
							}
						});
					}
					// AS-BE
					if (allBeByComponentId.get(g.getId()) != null) {
						mrEditorTreeCreator.generateMCOfBcSubTypeforDefineTrace(subItem,
								allBeByComponentId.get(g.getId()), allBeByComponentId, allSubBusiness,
								allBusinessComponent);
					}
				});
			}
		}

		// 生成be节点
		if (allBeByComponentId != null && bcTypeText.getData("bcType_id") != null
				&& allBeByComponentId.get(String.valueOf(bcTypeText.getData("bcType_id"))) != null) {
			mrEditorTreeCreator.generateMCDefineTrace(elementsTree,
					allBeByComponentId.get(bcTypeText.getData("bcType_id")), allBeByComponentId, allSubBusiness,
					allBusinessComponent);
		}
		Button bt = new SummaryRowTextComposite(msgComponentTable, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"OK")).getButton();
		bt.setBounds(280, 420, 80, 30);
		bt.addListener(SWT.MouseDown, new Listener() {
			private String substring;
			private int indexOf;
			private String data;

			@Override
			public void handleEvent(Event event) {
				TreeItem[] selection = elementsTree.getSelection();
				String name = selection[0].getText();
				for (int i = 0; i < name.length(); i++) {
					if (name.contains("[")) {
						indexOf = name.indexOf("[");
						substring = name.substring(0, indexOf);
						item1.setImage(2, ImgUtil.createImg(ImgUtil.BC_BE));
						item1.setData("Image", "1");
					} else if (name.contains("AS ")) {
						indexOf = name.indexOf("AS ");
						substring = name.substring(indexOf + 3, name.length());
						item1.setImage(2, ImgUtil.createImg(ImgUtil.BC));
						item1.setData("Image", "2");
					} else {
						item1.setImage(2, ImgUtil.createImg(ImgUtil.BC));
						item1.setData("Image", "2");
					}

				}
				// 根据树节点信息生成表格2
				if (substring != null) {
					item1.setText(2, substring);
				} else {
					item1.setText(2, name);
				}
				// 取出树中层级信息生成表格3数据
				if (selection[0].getData("FirstTreeName") != null) {
					data = selection[0].getData("FirstTreeName").toString();
					item1.setText(3, data);
				}

				businessElementWindow.close();
			}

		});
		businessElementWindow.setSize(400, 550);
		businessElementWindow.open();
		businessElementWindow.layout();
	}
}
