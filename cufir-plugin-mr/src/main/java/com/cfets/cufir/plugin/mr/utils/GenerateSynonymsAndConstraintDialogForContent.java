package com.cfets.cufir.plugin.mr.utils;

import java.util.ArrayList;
import java.util.UUID;

import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.TreeItem;

import com.cfets.cufir.s.data.bean.EcoreConstraint;

/**
 * ？？？
 */
public class GenerateSynonymsAndConstraintDialogForContent {

	/**
	 * 创建表格对话框
	 * 
	 * @param beSynonymsTable
	 */
	public static void createSynonymsDialogue(Table synonymsTable, Object... selectedTableItem) {
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
				TableItem tableItem = new TableItem(synonymsTable, SWT.NONE);
				tableItem.setText(new String[] { nameText.getText(), synonymsText.getText() });
				ArrayList<String> synonymsList = new ArrayList<>();
				for (TableItem tableRow: synonymsTable.getItems()) {
					String key = tableRow.getText(0);
					String value = tableRow.getText(1);
					synonymsList.add(key + "," + value);
				}
				if(selectedTableItem != null && selectedTableItem.length == 1 && selectedTableItem[0] instanceof TreeItem) {
					((TreeItem)selectedTableItem[0]).setData("mbbSynonymsTableItems", synonymsList);
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


	public static void createConstraintDialogueOfContent(Table constraintTable, Object... selectedTableItem) {
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
				TableItem tableItem = new TableItem(constraintTable, SWT.NONE);
				tableItem.setText(new String[] { nameText.getText(), docText.getText(), expressionLanguageText.getText(), expressionText.getText() });
				EcoreConstraint ecoreConstraint = new EcoreConstraint();
				ecoreConstraint.setId(UUID.randomUUID().toString());
				ecoreConstraint.setName(nameText.getText());
				ecoreConstraint.setDefinition(docText.getText());
				ecoreConstraint.setExpressionlanguage(expressionLanguageText.getText());
				ecoreConstraint.setExpression(expressionText.getText());
				tableItem.setData(ecoreConstraint);
				
				ArrayList<EcoreConstraint> mbbConstraintList = new ArrayList<>(); 
				for (TableItem tableRow: constraintTable.getItems()) {
					mbbConstraintList.add((EcoreConstraint)tableRow.getData());
				}
				if(selectedTableItem[0] instanceof TreeItem ) {
					((TreeItem)selectedTableItem[0]).setData("mbbConstraintList", mbbConstraintList);
				}
				constraintWindow.close();
			}
		});
		Button cancelButton = new Button(c, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setBounds(350, 265, 60, 30);
		cancelButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		cancelButton.addMouseListener(new CancelButtonListener(constraintWindow));
		
		constraintWindow.setBounds((Display.getDefault().getBounds().width - 480) / 2,
				(Display.getDefault().getBounds().height - 350) / 2, 480, 350);
		constraintWindow.open();
		constraintWindow.layout();
	}
	

	
	static class CancelButtonListener extends MouseAdapter  {
		
		private Shell shell;
		
		public CancelButtonListener(Shell shell) {
			this.shell = shell;
		}
		
		@Override
		public void mouseUp(MouseEvent e) {
			shell.close();
		}
	}



	public static void deleteSynonymDialogue(Table synonymsTable, TreeItem... selectedTableItem) {
		// 删除操作
		TableItem[] selection = synonymsTable.getSelection();
		if (selection != null && selection.length > 0) {
			selection[0].dispose();

//			if (selectedTableItem != null && selectedTableItem.length > 0) {
//				TableItem[] items = synonymsTable.getItems();
//				java.util.List<String[]> textList = new ArrayList<String[]>();
//				for (int i = 0; i < items.length; i++) {
//					textList.add(new String[] { items[i].getText(0), items[i].getText(1), items[i].getText(2),
//							items[i].getText(3) });
//				}
//				if(selectedTableItem[0] instanceof TreeItem) {
//					((TreeItem)selectedTableItem[0]).setData("beSynonymsTableItems", textList);
//				} else if(selectedTableItem[0] instanceof TableItem){
//					((TableItem)selectedTableItem[0]).setData("beSynonymsTableItems", textList);
//				}
//			}
		}
		ArrayList<String> synonymsList = new ArrayList<>();
		for (TableItem tableRow: synonymsTable.getItems()) {
			String key = tableRow.getText(0);
			String value = tableRow.getText(1);
			synonymsList.add(key + "," + value);
		}
		if(selectedTableItem != null && selectedTableItem[0] instanceof TreeItem) {
			((TreeItem)selectedTableItem[0]).setData("mbbSynonymsTableItems", synonymsList);
		}
	}
}
