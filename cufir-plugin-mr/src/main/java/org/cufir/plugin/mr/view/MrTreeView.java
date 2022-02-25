package org.cufir.plugin.mr.view;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.cufir.plugin.mr.editor.MrRepository;
import org.cufir.plugin.mr.utils.ImgUtil;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;

/**
 * 树视图
 * @author tangmaoquan
 * @Date 2021年9月29日
 */
public class MrTreeView extends ViewPart {
	
	public static Composite composite;
	/**
	 * 入口方法
	 */
	@Override
	public void createPartControl(Composite parent) {
		
		composite = parent;
		Composite com = new Composite(composite, SWT.NONE);
		com.setLayout(new GridLayout(1, false));

		Composite com1 = new Composite(com, SWT.NONE);
		com1.setBounds(0, 0, 280, 25);

		Text text = new Text(com1, SWT.BORDER);
		text.setBounds(0, 0, 280, 25);

		Button searchBtn = new Button(com1, SWT.NONE);
		searchBtn.setBounds(300, 0, 50, 25);
		searchBtn.setImage(ImgUtil.createImg(ImgUtil.SEARCH_BTN));
		searchBtn.setBackground(new Color(Display.getCurrent(), 201, 218, 248));

		Combo combo = new Combo(com1, SWT.DROP_DOWN);
		combo.setVisible(false);
		combo.setBounds(0, 0, 280, 25);

		searchBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String inputText = text.getText();
				Set<String> collect;

				if (inputText == null || inputText == "") {
					if (MrRepository.get().tabFolder.getSelectionIndex() == 1) {
						collect = MrRepository.get().tab2ComboStrList;
					} else if (MrRepository.get().tabFolder.getSelectionIndex() == 2) {
						collect = MrRepository.get().tab3ComboStrList;
					} else if (MrRepository.get().tabFolder.getSelectionIndex() == 3) {
						collect = MrRepository.get().tab4ComboStrList;
					} else {
						collect = MrRepository.get().tab1ComboStrList;
					}
				} else {
					if (MrRepository.get().tabFolder.getSelectionIndex() == 1) {
						collect = MrRepository.get().tab2ComboStrList.stream()
								.filter(t -> t.toUpperCase().startsWith(inputText.toUpperCase()))
								.collect(Collectors.toSet());
					} else if (MrRepository.get().tabFolder.getSelectionIndex() == 2) {
						collect = MrRepository.get().tab3ComboStrList.stream()
								.filter(t -> t.toUpperCase().startsWith(inputText.toUpperCase()))
								.collect(Collectors.toSet());
					} else if (MrRepository.get().tabFolder.getSelectionIndex() == 3) {
						collect = MrRepository.get().tab4ComboStrList.stream()
								.filter(t -> t.toUpperCase().startsWith(inputText.toUpperCase()))
								.collect(Collectors.toSet());
					} else {
						collect = MrRepository.get().tab1ComboStrList.stream()
								.filter(t -> t.toUpperCase().startsWith(inputText.toUpperCase()))
								.collect(Collectors.toSet());
					}
				}
				combo.setItems(collect.toArray(new String[collect.size()]));

				if (!combo.getListVisible()) {
					combo.setListVisible(true);
				}
			}
		});

		// 选中状态
		combo.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Map<String, Object> map;
				Tree tabTree;
				if (MrRepository.get().tabFolder.getSelectionIndex() == 1) {
					map = MrRepository.get().tab2ComboMap.get(combo.getText());
					tabTree = MrRepository.get().messageComponentsTabTree;
				} else if (MrRepository.get().tabFolder.getSelectionIndex() == 2) {
					map = MrRepository.get().tab3ComboMap.get(combo.getText());
					tabTree = MrRepository.get().messagesTabTree;
				} else {
					map = MrRepository.get().tab1ComboMap.get(combo.getText());
					tabTree = MrRepository.get().businessModelTabTree;
				}
				if (map != null) {
					TreeItem treeListItem = (TreeItem) map.get("TreeItem");
					treeListItem.getParentItem().setExpanded(true);
					treeListItem.setExpanded(true);
					tabTree.setSelection(treeListItem);

					combo.setData("select", "1");
					combo.clearSelection();
					text.setText(combo.getText());
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		//加载树视图
		MrRepository.get().tabFolder.setParent(com);
		
		// 最上方的工具栏
		MenuManager menuManager = new MenuManager("#popupmenu");
		menuManager.setRemoveAllWhenShown(true);
//		new MrColorManage(MrColorManage.BLUE_COLOR);

	}

	@Override
	public void setFocus() {

	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}
}
