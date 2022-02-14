
package com.cfets.cufir.plugin.mr.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;

import com.cfets.cufir.plugin.mr.bean.TreeListItem;
import com.cfets.cufir.plugin.mr.enums.TreeParentEnum;
import com.cfets.cufir.plugin.mr.listener.FirstTreeDoubleClickListener;
import com.cfets.cufir.plugin.mr.listener.SecondTreeDoubleClickListener;
import com.cfets.cufir.plugin.mr.listener.ThirdTreeDoubleClickListener;
import com.cfets.cufir.plugin.mr.utils.ExceptionHandleUtil;
import com.cfets.cufir.plugin.mr.utils.ImgUtil;
import com.cfets.cufir.plugin.mr.utils.TreePopupMenuUtil;
import com.cfets.cufir.s.data.dao.EcoreBusinessComponentDao;
import com.cfets.cufir.s.data.dao.EcoreDataTypeDao;
import com.cfets.cufir.s.data.dao.EcoreExternalSchemaDao;
import com.cfets.cufir.s.data.dao.EcoreMessageComponentDao;
import com.cfets.cufir.s.data.dao.EcoreMessageDefinitionDao;
import com.cfets.cufir.s.data.dao.impl.EcoreBusinessComponentDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreDataTypeDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreExternalSchemaDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreMessageComponentDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreMessageDefinitionDaoImpl;
import com.cfets.cufir.s.data.vo.EcoreTreeNode;

/**
 *  TreeListView -> CufirModelView
 * 
 * @author jin.c.li
 *
 */
public class TreeListView extends ViewPart {

	private static Tree firstTabTree;
	private static Tree secondTabTree;
	private Tree thirdTabTree;
//	private Tree fourthTabTree;

	private static CTabFolder tabFolder;
	private Combo combo;

	private EcoreDataTypeDao ecoreDataTypeDao = new EcoreDataTypeDaoImpl();
	private EcoreBusinessComponentDao businessComponentDao = new EcoreBusinessComponentDaoImpl();
	private EcoreExternalSchemaDao externalSchemaDao = new EcoreExternalSchemaDaoImpl();
	private EcoreMessageComponentDao messageComponentDao = new EcoreMessageComponentDaoImpl();
	private EcoreMessageDefinitionDao messageDefinitionDao = new EcoreMessageDefinitionDaoImpl();

	private Set<String> tab1ComboStrList = new HashSet<String>();
	private Set<String> tab2ComboStrList = new HashSet<String>();
	private Set<String> tab3ComboStrList = new HashSet<String>();
	private Set<String> tab4ComboStrList = new HashSet<String>();

	private Map<String, Map<String, Object>> tab1ComboMap = new HashMap<String, Map<String, Object>>();
	private Map<String, Map<String, Object>> tab2ComboMap = new HashMap<String, Map<String, Object>>();
	private Map<String, Map<String, Object>> tab3ComboMap = new HashMap<String, Map<String, Object>>();
//	private Map<String, Map<String, Object>> tab4ComboMap = new HashMap<String, Map<String, Object>>();

	public static CTabFolder getTabFolder() {
		return tabFolder;
	}

	public static Tree getFirstTabTree() {
		return firstTabTree;
	}

	public static Tree getSecondTabTree() {
		return secondTabTree;
	}

	/**
	 * 入口方法
	 */
	@Override
	public void createPartControl(Composite parent) {

		Composite com = new Composite(parent, SWT.NONE);
		com.setLayout(new GridLayout(1, false));

		Composite com1 = new Composite(com, SWT.NONE);
		com1.setBounds(0, 0, 280, 25);

		Text text = new Text(com1, SWT.BORDER);
		text.setBounds(0, 0, 280, 25);

		Button searchBtn = new Button(com1, SWT.NONE);
//		searchBtn.setText("Search");
		searchBtn.setBounds(300, 0, 50, 25);
		searchBtn.setImage(ImgUtil.createImg(ImgUtil.SEARCH_BTN));
		searchBtn.setBackground(new Color(Display.getCurrent(), 135, 206, 250));

		combo = new Combo(com1, SWT.DROP_DOWN);
		combo.setVisible(false);
		combo.setBounds(0, 0, 280, 25);

		searchBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String inputText = text.getText();
				Set<String> collect;

				if (inputText == null || inputText == "") {
					if (tabFolder.getSelectionIndex() == 1) {
						collect = tab2ComboStrList;
					} else if (tabFolder.getSelectionIndex() == 2) {
						collect = tab3ComboStrList;
					} else if (tabFolder.getSelectionIndex() == 3) {
						collect = tab4ComboStrList;
					} else {
						collect = tab1ComboStrList;
					}
				} else {
					if (tabFolder.getSelectionIndex() == 1) {
						collect = tab2ComboStrList.stream()
								.filter(t -> t.toUpperCase().startsWith(inputText.toUpperCase()))
								.collect(Collectors.toSet());
					} else if (tabFolder.getSelectionIndex() == 2) {
						collect = tab3ComboStrList.stream()
								.filter(t -> t.toUpperCase().startsWith(inputText.toUpperCase()))
								.collect(Collectors.toSet());
					} else if (tabFolder.getSelectionIndex() == 3) {
						collect = tab4ComboStrList.stream()
								.filter(t -> t.toUpperCase().startsWith(inputText.toUpperCase()))
								.collect(Collectors.toSet());
					} else {
						collect = tab1ComboStrList.stream()
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

				if (tabFolder.getSelectionIndex() == 1) {
					map = tab2ComboMap.get(combo.getText());
					tabTree = secondTabTree;
				} else if (tabFolder.getSelectionIndex() == 2) {
					map = tab3ComboMap.get(combo.getText());
					tabTree = thirdTabTree;
				}
//				else if(tabFolder.getSelectionIndex() == 3) {
//					map = tab4ComboMap.get(combo.getText());
//					tabTree = fourthTabTree;
//				}
				else {
					map = tab1ComboMap.get(combo.getText());
					tabTree = firstTabTree;
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

		tabFolder = new CTabFolder(com, SWT.BOTTOM | SWT.FLAT);
		tabFolder.setSimple(false);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		tabFolder.setSelectionBackground(
				Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		initFirstTab("Business Model");

		initSecondTab("Message Components");

		initThirdTab("Messages");

//		initFourthTab("Views");

		// 最上方的工具栏
		this.initToolBars();
	}

	@Override
	public void setFocus() {

	}

	/**
	 * 检索pkg数据
	 */
	public List<String> change(List<String> data, String str) {
		List<String> changedData = new ArrayList<String>();
		data.stream().filter(pkg -> pkg.startsWith(str)).distinct().sorted().forEach(pkg -> changedData.add(pkg));
		return changedData;
	}

	/**
	 * 加载第一个tab
	 */
	private void initFirstTab(String tabName) {
		CTabItem item = new CTabItem(tabFolder, SWT.NONE);
		item.setText(tabName);
		// 初始化Tree控件
		this.initFirstTabTree(tabFolder);
		item.setControl(firstTabTree);

		// 设置默认选中状态
		tabFolder.setSelection(0);
		// 加载combo的选项
//		List<String> collect = tab1ComboStrList.stream().sorted().collect(Collectors.toList());
//		combo.setItems(collect.toArray(new String[0]));
	}

	private void initSecondTab(String tabName) {
		CTabItem item = new CTabItem(tabFolder, SWT.NONE);
		item.setText(tabName);
		// 初始化Tree控件
		this.initSecondTabTree(tabFolder);
		item.setControl(secondTabTree);
	}

	private void initThirdTab(String tabName) {
		CTabItem item = new CTabItem(tabFolder, SWT.NONE);
		item.setText(tabName);
		// 初始化Tree控件
		this.initThirdTree(tabFolder);
		item.setControl(thirdTabTree);
	}

//	private void initFourthTab(String tabName) {
//		CTabItem item = new CTabItem(tabFolder, SWT.NONE);
//		item.setText(tabName);
//		// 初始化Tree控件
//		this.initFourthTree(tabFolder);
//		item.setControl(fourthTabTree);
//	}

	/**
	 * 初始化IActionBars，包括菜单、工具栏等
	 */
	private void initToolBars() {

		MenuManager menuManager = new MenuManager("#popupmenu");
		menuManager.setRemoveAllWhenShown(true);

//		IActionBars bars = getViewSite().getActionBars();
//		IMenuManager imm = bars.getMenuManager();
//		IToolBarManager itbm = bars.getToolBarManager();
//		
//		// 定义收缩group -- trace
//		Action traceAction = new Action() {
//			public void run() {
//				System.out.println("traceAction");
//			}
//		};
//		traceAction.setText("Trace");
//		traceAction.setToolTipText("Groups Message Components according to the Business Component they trace to");
//		traceAction.setImageDescriptor(Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/toolbar/GroupByBC_16.png"));
//		
//		// 定义收缩group -- development status
//		Action developmentStatusAction = new Action() {
//			public void run() {
//				System.out.println("developmentStatusAction");
//			}
//		};
//		developmentStatusAction.setText("Development Status");
//		developmentStatusAction.setToolTipText("Group Elements by their Development Status");
//		developmentStatusAction.setImageDescriptor(Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/toolbar/lock_unlock_sort_16.png"));
//		
//		// 定义收缩group -- name
//		Action groupByNameAction = new Action() {
//			public void run() {
//				System.out.println("groupByNameAction");
//			}
//		};
//		groupByNameAction.setText("Name");
//		groupByNameAction.setToolTipText("Group Element by their name");
//		groupByNameAction.setImageDescriptor(Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/toolbar/sortbyname_16.png"));
//		
//		// 定义收缩sort -- msgId
//		Action sortByMsgIdAction = new Action() {
//			public void run() {
//				System.out.println("sortByMsgIdAction");
//			}
//		};
//		sortByMsgIdAction.setText("Msg Id");
//		sortByMsgIdAction.setToolTipText("Sort Message Definitions by their identifier");
//		sortByMsgIdAction.setImageDescriptor(Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/toolbar/sortbyID_16.png"));
//		
//		// 定义收缩sort -- msgName
//		Action sortByMsgNameAction = new Action() {
//			public void run() {
//				System.out.println("sortByMsgNameAction");
//			}
//		};
//		sortByMsgNameAction.setText("Msg Name");
//		sortByMsgNameAction.setToolTipText("Sort Message Definitions by their name");
//		sortByMsgNameAction.setImageDescriptor(Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/toolbar/sortbyname_16.png"));

		// 定义收缩filter -- viewFilter
//		Action viewFilterAction = new Action() {
//			public void run() {
//				System.out.println("viewFilterAction");
//			}
//		};
//		viewFilterAction.setText("View Filter");
//		viewFilterAction.setImageDescriptor(Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/hook.png"));
//		
		// 定义收缩filter -- displaySubsets
//		Action displaySubsetsAction = new Action() {
//			public void run() {
//				System.out.println("displaySubsetsAction");
//			}
//		};
//		displaySubsetsAction.setText("Display subsets");
//		displaySubsetsAction.setImageDescriptor(Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/hook.png"));
//		
//		{
//			MenuManager filterMenuManager = new MenuManager("Filter");
//			imm.add(filterMenuManager);
//			filterMenuManager.add(viewFilterAction);
//			filterMenuManager.add(displaySubsetsAction);
//		}
//		{
//			MenuManager groupMenuManager = new MenuManager("Group");
//			imm.add(groupMenuManager);
//			groupMenuManager.add(traceAction);
//			groupMenuManager.add(developmentStatusAction);
//			groupMenuManager.add(groupByNameAction);
//		}
//		{
//			MenuManager sortMenuManager = new MenuManager("Sort");
//			imm.add(sortMenuManager);
//			sortMenuManager.add(sortByMsgIdAction);
//			sortMenuManager.add(sortByMsgNameAction);
//		}

//		itbm.add(traceAction);
//		itbm.add(developmentStatusAction);
//		itbm.add(groupByNameAction);
//		itbm.add(sortByMsgIdAction);
//		itbm.add(sortByMsgNameAction);
	}

	/**
	 * 初始化创建树
	 */
	private void initFirstTabTree(Composite parent) {

		firstTabTree = new Tree(parent, SWT.SINGLE | SWT.BORDER_SOLID | SWT.V_SCROLL | SWT.H_SCROLL);
		firstTabTree.computeSize(10, 10, true);
//		dataTypeTree.setLinesVisible(true);
		firstTabTree.setRedraw(true);
		firstTabTree.setLayout(new FillLayout(0));
		firstTabTree.setData("tabIndex", 1);

		EcoreTreeNode ecoreBusinessComponentTreeNodes;
		try {
			ecoreBusinessComponentTreeNodes = businessComponentDao.findEcoreBusinessComponentTreeNodes();
			EcoreTreeNode ecoreDataTypeTreeNodes = ecoreDataTypeDao.findEcoreDataTypeTreeNodes();

			// 根目录
			new TreeListItem(firstTabTree, ecoreDataTypeTreeNodes);
			new TreeListItem(firstTabTree, ecoreBusinessComponentTreeNodes);
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandleUtil.handle(e);
		}

		// 添加双击事件响应
		firstTabTree.addListener(SWT.MouseDoubleClick, new FirstTreeDoubleClickListener(firstTabTree));

		// 添加右键PopupMenu
		TreePopupMenuUtil.init(firstTabTree, "1");

		// 加载搜索框下面的选项
		tab1ComboStrList.clear();
		tab1ComboMap.clear();
		for (TreeItem treeItem : firstTabTree.getItems()) {
			getComboList(treeItem, 1, tab1ComboStrList, tab1ComboMap);
		}
	}

	/**
	 * 初始化创建树
	 */
	private void initSecondTabTree(Composite parent) {

		secondTabTree = new Tree(parent, SWT.SINGLE | SWT.BORDER_SOLID | SWT.V_SCROLL | SWT.H_SCROLL);
		secondTabTree.computeSize(10, 10, true);
		secondTabTree.setRedraw(true);
		secondTabTree.setLayout(new FillLayout(0));
		secondTabTree.setData("tabIndex", 2);

		try {
			EcoreTreeNode externalSchemaTreeNodes = externalSchemaDao.findEcoreExternalSchemaTreeNodes();
			EcoreTreeNode ecoreMessageComponentTreeNodes = messageComponentDao.findEcoreMessageComponentTreeNodes();
			// 根目录
			new TreeListItem(secondTabTree, externalSchemaTreeNodes);
			new TreeListItem(secondTabTree, ecoreMessageComponentTreeNodes);
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandleUtil.handle(e);
		}

		// 单击加载树
//		secondTabTree.addTreeListener(new TreeSingleClickListener(secondTabTree));
		// 添加双击事件响应
		secondTabTree.addListener(SWT.MouseDoubleClick, new SecondTreeDoubleClickListener(secondTabTree));

		// 添加右键PopupMenu
		TreePopupMenuUtil.init(secondTabTree, "2");

		// 加载搜索框下面的选项
		tab2ComboStrList.clear();
		tab2ComboMap.clear();
		for (TreeItem treeItem : secondTabTree.getItems()) {
			getComboList(treeItem, 2, tab2ComboStrList, tab2ComboMap);
		}
	}

	/**
	 * 初始化创建树
	 */
	private void initThirdTree(Composite parent) {

		thirdTabTree = new Tree(parent, SWT.SINGLE | SWT.BORDER_SOLID | SWT.V_SCROLL | SWT.H_SCROLL);
		thirdTabTree.computeSize(10, 10, true);
		thirdTabTree.setRedraw(true);
		thirdTabTree.setLayout(new FillLayout(0));
		thirdTabTree.setData("tabIndex", 3);

		try {
			List<EcoreTreeNode> findEcoreMessageTreeNodes = messageDefinitionDao.findEcoreMessageTreeNodes();
			findEcoreMessageTreeNodes.stream().forEach(t -> {
				new TreeListItem(thirdTabTree, t);
			});
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandleUtil.handle(e);
		}

		// 添加双击事件响应
		thirdTabTree.addListener(SWT.MouseDoubleClick, new ThirdTreeDoubleClickListener(thirdTabTree));

		// 添加右键PopupMenu
		TreePopupMenuUtil.init(thirdTabTree, "3");

		// 加载搜索框下面的选项
		tab3ComboStrList.clear();
		tab3ComboMap.clear();
		for (TreeItem treeItem : thirdTabTree.getItems()) {
			getComboList(treeItem, 3, tab3ComboStrList, tab3ComboMap);
		}
	}

//	/**
//	 * 初始化创建树
//	 */
//	private void initFourthTree(Composite parent) {
//
//		fourthTabTree = new Tree(parent, SWT.SINGLE | SWT.BORDER_SOLID | SWT.V_SCROLL | SWT.H_SCROLL);
//		fourthTabTree.computeSize(10, 10, true);
//		fourthTabTree.setRedraw(true);
//		fourthTabTree.setLayout(new FillLayout(0));
//		fourthTabTree.setData("tabIndex", 4);
//
//		//根目录
//		EcoreTreeNode ecoreTreeNode = new EcoreTreeNode();
//		ecoreTreeNode.setLevel(TreeLevelEnum.LEVEL_1.getLevel());
//		ecoreTreeNode.setType("8");
//		ecoreTreeNode.setName("View Identical Components");
//		new TreeListItem(fourthTabTree, ecoreTreeNode);
//
//		//单击加载树
////		fourthTabTree.addTreeListener(new TreeSingleClickListener(fourthTabTree));
//		// 添加双击事件响应
//		fourthTabTree.addListener(SWT.MouseDoubleClick, new TreeListViewDoubleClickListener(fourthTabTree));
//		
//		// 添加右键PopupMenu
//		TreePopupMenuUtil.init(fourthTabTree, "4");
//		
//		//加载搜索框下面的选项
//		tab4ComboStrList.clear();
//		tab4ComboMap.clear();
//		for (TreeItem treeItem : fourthTabTree.getItems()) {
//			getComboList(treeItem, 4, tab4ComboStrList, tab4ComboMap);
//		}
//	}

	/**
	 * 获取combo的选项
	 * 
	 * @param firstNode
	 * @param tabType
	 * @param comboStrList
	 * @param comboMap
	 */
	private void getComboList(TreeItem firstNode, int tabType, Set<String> comboStrList,
			Map<String, Map<String, Object>> comboMap) {
		comboStrList.add(firstNode.getText());
		HashMap<String, Object> firstNodeMap = new HashMap<String, Object>();
		firstNodeMap.put("tabType", tabType);
		firstNodeMap.put("TreeItem", firstNode);
		comboMap.put(firstNode.getText(), firstNodeMap);

		TreeItem[] secondChildNodes = firstNode.getItems();
		if (secondChildNodes != null && secondChildNodes.length > 0) {
			for (TreeItem secondNode : secondChildNodes) {
				comboStrList.add(secondNode.getText());
				HashMap<String, Object> secondNodeMap = new HashMap<String, Object>();
				secondNodeMap.put("tabType", tabType);
				secondNodeMap.put("TreeItem", secondNode);
				comboMap.put(secondNode.getText(), secondNodeMap);
				if (tabType == 1 && firstNode.getText().equals(TreeParentEnum.DATA_TYPES.getName())) {
					TreeItem[] thirdChildNodes = secondNode.getItems();
					if (thirdChildNodes != null && thirdChildNodes.length > 0) {
						for (TreeItem thirdChild : thirdChildNodes) {
							comboStrList.add(thirdChild.getText());
							HashMap<String, Object> thirdChildMap = new HashMap<String, Object>();
							thirdChildMap.put("tabType", tabType);
							thirdChildMap.put("TreeItem", thirdChild);
							comboMap.put(thirdChild.getText(), thirdChildMap);
						}
					}
				}
			}
		}

		// 对set进行排序
		comboStrList = comboStrList.stream().sorted().collect(Collectors.toSet());
	}
}
