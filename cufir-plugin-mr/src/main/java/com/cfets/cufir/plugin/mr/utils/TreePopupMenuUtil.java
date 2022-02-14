package com.cfets.cufir.plugin.mr.utils;

import java.util.ArrayList;
import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.cfets.cufir.plugin.mr.bean.TransferDataBean;
import com.cfets.cufir.plugin.mr.bean.TreeItemDataBean;
import com.cfets.cufir.plugin.mr.bean.TreeListItem;
import com.cfets.cufir.plugin.mr.enums.DataTypesEnum;
import com.cfets.cufir.plugin.mr.enums.RegistrationStatus;
import com.cfets.cufir.plugin.mr.enums.TreeLevelEnum;
import com.cfets.cufir.plugin.mr.enums.TreeParentEnum;
import com.cfets.cufir.s.data.MDR3Mgr;
import com.cfets.cufir.s.data.MessageDataMgr;
import com.cfets.cufir.s.data.bean.EcoreBusinessArea;
import com.cfets.cufir.s.data.bean.EcoreMessageSet;
import com.cfets.cufir.s.data.dao.EcoreBusinessAreaDao;
import com.cfets.cufir.s.data.dao.EcoreBusinessComponentDao;
import com.cfets.cufir.s.data.dao.EcoreDataTypeDao;
import com.cfets.cufir.s.data.dao.EcoreExternalSchemaDao;
import com.cfets.cufir.s.data.dao.EcoreMessageComponentDao;
import com.cfets.cufir.s.data.dao.EcoreMessageDefinitionDao;
import com.cfets.cufir.s.data.dao.EcoreMessageSetDao;
import com.cfets.cufir.s.data.dao.impl.EcoreBusinessAreaDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreBusinessComponentDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreDataTypeDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreExternalSchemaDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreMessageComponentDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreMessageDefinitionDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreMessageSetDaoImpl;
import com.cfets.cufir.s.data.vo.EcoreCodeVO;
import com.cfets.cufir.s.data.vo.EcoreDataTypeVO;
import com.cfets.cufir.s.data.vo.EcoreTreeNode;
import com.cfets.cufir.s.xsd.XSDCreateDaoImpl;
import com.cfets.cufir.s.xsd.dao.XSDTreeSetDao;

public class TreePopupMenuUtil {
	
	private static EcoreDataTypeDao ecoreDataTypeDao = new EcoreDataTypeDaoImpl();
	private static EcoreBusinessComponentDao businessComponentDao = new EcoreBusinessComponentDaoImpl();
	private static EcoreExternalSchemaDao externalSchemaDao = new EcoreExternalSchemaDaoImpl();
	private static EcoreMessageComponentDao messageComponentDao = new EcoreMessageComponentDaoImpl();
	private static EcoreBusinessAreaDao businessAreaDao = new EcoreBusinessAreaDaoImpl();
	private static EcoreMessageSetDao ecoreMessageSetDao = new EcoreMessageSetDaoImpl();
	private static EcoreMessageDefinitionDao ecoreMessageDefinitionDao = new EcoreMessageDefinitionDaoImpl();

	private static TreeListItem parentItem;
	
	private static TreeListItem selectedTreeListItem;
	/**
	 * 初始化树结构
	 * @param tree
	 * @param tabType
	 */
	public static void init(Tree tree, String tabType) {
		// 添加右键PopupMenu
		Menu menu = new Menu(tree);
//		new MenuItem(menu, SWT.SEPARATOR);
//		addDeleteMenu(menu, false, tree);
		tree.setMenu(menu);
		if(tabType == "1") {
			addFirstTabNewChildMenu(menu, tree);
			addNewVersion(menu, tree);
			addNewDerive(menu, tree);
			addFirstTabDeleteMenu(menu, tree);
		}else if(tabType == "2") {
			addSecondTabNewChildMenu(menu, tree);
			addSecondTabDeleteMenu(menu, tree);
		}else if(tabType == "3") {
			addThirdTabNewChildMenu(menu, tree);
			addThirdTabDeleteMenu(menu, tree);
			addGeneratorsMenu(menu, tree);
			addGeneratorsExcelMenu(menu, tree);
			addGenerateXMLMenu(menu, tree);
			addGenerateMDR3Menu(menu, tree);
		}else if(tabType == "4") {
			addFourthTabNewChildMenu(menu, tree);
			//addFourthTabDeleteMenu(menu, tree);
		}
	}

	public static void addUndoMenu(Menu menu, boolean useStatus) {
		MenuItem mi = new MenuItem(menu, SWT.PUSH);
		mi.setText("Undo");
		mi.setAccelerator(SWT.CTRL | 'Z');
		String imgPath = "/icons/toolbar/undo_edit.gif";
		if (useStatus) {
			imgPath = "/icons/toolbar/undo_edit (2).gif";
		}
		mi.setImage(ImgUtil.createImg(imgPath));
		mi.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
	}

	public static void addRedoMenu(Menu menu, boolean useStatus) {
		MenuItem mi = new MenuItem(menu, SWT.PUSH);
		mi.setText("Redo");
		mi.setAccelerator(SWT.CTRL | 'Y');
		String imgPath = "/icons/toolbar/redo_edit.gif";
		if (useStatus) {
			imgPath = "/icons/toolbar/redo_edit (2).gif";
		}
		mi.setImage(ImgUtil.createImg(imgPath));
		mi.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
	}

	public static void addCutMenu(Menu menu, boolean useStatus) {
		MenuItem mi = new MenuItem(menu, SWT.PUSH);
		mi.setText("Cut");
		String imgPath = "/icons/toolbar/cut_edit.gif";
		if (useStatus) {
			imgPath = "/icons/toolbar/cut_edit (2).gif";
		}
		mi.setImage(ImgUtil.createImg(imgPath));
		mi.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
	}

	public static void addCopyMenu(Menu menu, boolean useStatus) {
		MenuItem mi = new MenuItem(menu, SWT.PUSH);
		mi.setText("Copy");
		String imgPath = "/icons/toolbar/copy_edit.gif";
		if (useStatus) {
			imgPath = "/icons/toolbar/copy_edit (2).gif";
		}
		mi.setImage(ImgUtil.createImg(imgPath));
		mi.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
	}

	public static void addPasteMenu(Menu menu, boolean useStatus) {
		MenuItem mi = new MenuItem(menu, SWT.PUSH);
		mi.setText("Paste");
		String imgPath = "/icons/toolbar/paste_edit.gif";
		if (useStatus) {
			imgPath = "/icons/toolbar/paste_edit (2).gif";
		}
		mi.setImage(ImgUtil.createImg(imgPath));
		mi.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
	}

	public static void addDeleteMenu(Menu menu, boolean useStatus, Tree tabTree) {
		MenuItem mi = new MenuItem(menu, SWT.PUSH);
		mi.setText("Delete");
		String imgPath = "/icons/toolbar/delete_edit.gif";
		if (useStatus) {
			imgPath = "/icons/toolbar/delete_edit (2).gif";
		}

		mi.setImage(ImgUtil.createImg(imgPath));
		mi.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell parent = new Shell(Display.getCurrent());
				MessageBox messageBox = new MessageBox(parent, SWT.OK | SWT.CANCEL);
				messageBox.setMessage("是否确认删除？");
				if (messageBox.open() == SWT.OK) {
					// 移除选中的TreeItem
					TreeItem treeItem = tabTree.getSelection()[0];
					treeItem.dispose();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
	}

	public static void addRefreshMenu(Menu menu, Tree tabTree) {
		MenuItem mi = new MenuItem(menu, SWT.PUSH);
		mi.setText("Refresh");
		mi.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				TreeItem[] selection = tabTree.getSelection();
//				TreeListItem selectedTreeListItem = (TreeListItem) selection[0];
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
	}

	public static MenuItem addGeneratorsMenu(Menu menu, Tree tabTree) {
		MenuItem generateXmlMenu = new MenuItem(menu, SWT.PUSH);
		generateXmlMenu.setText("Generate xsd");
		generateXmlMenu.setEnabled(false);
		generateXmlMenu.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 移除选中的TreeItem
				TreeItem treeItem = tabTree.getSelection()[0];
				TreeListItem treeListItem = (TreeListItem) treeItem;
				EcoreTreeNode ecoreTreeNode = (EcoreTreeNode) treeListItem.getData("EcoreTreeNode");
				System.out.println("获取到的id：" + ecoreTreeNode.getId());
				
				String path = openFile(ecoreTreeNode.getName(), "xsd");
				if(path == null || path == "") {
					return;
				}
				System.out.println("文件路径：" + path);
				XSDTreeSetDao treeSetDao = new XSDCreateDaoImpl();
				try {
					boolean generateXsd = treeSetDao.generateXsd(ecoreTreeNode.getId(), path);

					if(generateXsd) {
						MessageBoxUtil.showMessageBox("xsd文件生成成功！");
					}else {
						MessageBoxUtil.showMessageBox("xsd文件生成失败！");
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		
		tabTree.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
				
				//右键的时候设置
				if(e.button == 3) {
					TreeItem[] selection = tabTree.getSelection();
					TreeListItem treeListItem = (TreeListItem)selection[0];
					EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)treeListItem.getData("EcoreTreeNode");
					
					if(ecoreTreeNode != null) {
						if(ecoreTreeNode.getLevel().endsWith(TreeLevelEnum.LEVEL_2.getLevel())) {
							if(treeListItem.getParentItem().getText().equals(TreeParentEnum.MESSAGE_DEFINITIONS.getName())) {
								generateXmlMenu.setEnabled(true);
								return;
							}
						}else if(ecoreTreeNode.getLevel().endsWith(TreeLevelEnum.LEVEL_3.getLevel())) {
							if((treeListItem.getParentItem().getParentItem().getText().equalsIgnoreCase(TreeParentEnum.MESSAGE_SETS.getName()) 
									|| treeListItem.getParentItem().getParentItem().getText().equalsIgnoreCase(TreeParentEnum.BUSINESS_AREAS.getName()))) {
								generateXmlMenu.setEnabled(true);
								return;
							}
						}
					}
					generateXmlMenu.setEnabled(false);
				}
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
			}
		});

		return generateXmlMenu;
	}

	public static MenuItem addGeneratorsExcelMenu(Menu menu, Tree tabTree) {
		MenuItem generateExcelMenu = new MenuItem(menu, SWT.PUSH);
		generateExcelMenu.setText("Generate excel");
		generateExcelMenu.setEnabled(false);
		generateExcelMenu.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 移除选中的TreeItem
				TreeItem treeItem = tabTree.getSelection()[0];
				TreeListItem treeListItem = (TreeListItem) treeItem;
				EcoreTreeNode ecoreTreeNode = (EcoreTreeNode) treeListItem.getData("EcoreTreeNode");
				System.out.println("获取到的id：" + ecoreTreeNode.getId());
				
				String path = openExcelFile(ecoreTreeNode.getName());
				if(path == null || path == "") {
					return;
				}
				System.out.println("文件路径：" + path);
				MessageDataMgr mgr=new MessageDataMgr();
                try {
                	boolean generateExcel =mgr.generateMessageDataExcel(ecoreTreeNode.getId(),path);
                	if(generateExcel) {
						MessageBoxUtil.showMessageBox("Excel文件生成成功！");
					}else {
						MessageBoxUtil.showMessageBox("Excel文件生成失败！");
					}
                } catch (Exception e1) {
					e1.printStackTrace();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		
		tabTree.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
				
				//右键的时候设置
				if(e.button == 3) {
					TreeItem[] selection = tabTree.getSelection();
					TreeListItem treeListItem = (TreeListItem)selection[0];
					EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)treeListItem.getData("EcoreTreeNode");
					
					if(ecoreTreeNode != null) {
						if(ecoreTreeNode.getLevel().endsWith(TreeLevelEnum.LEVEL_2.getLevel())) {
							if(treeListItem.getParentItem().getText().equals(TreeParentEnum.MESSAGE_DEFINITIONS.getName())) {
								generateExcelMenu.setEnabled(true);
								return;
							}
						}else if(ecoreTreeNode.getLevel().endsWith(TreeLevelEnum.LEVEL_3.getLevel())) {
							if((treeListItem.getParentItem().getParentItem().getText().equalsIgnoreCase(TreeParentEnum.MESSAGE_SETS.getName()) || treeListItem.getParentItem().getParentItem().getText().equalsIgnoreCase(TreeParentEnum.BUSINESS_AREAS.getName()))) {
								generateExcelMenu.setEnabled(true);
								return;
							}
						}
					}
					generateExcelMenu.setEnabled(false);
				}
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
			}
		});

		return generateExcelMenu;
	}
	
	public static MenuItem addGenerateXMLMenu(Menu menu, Tree tabTree) {
		MenuItem generateDataMenu = new MenuItem(menu, SWT.PUSH);
		generateDataMenu.setText("Generate XML");
		generateDataMenu.setEnabled(false);
		
		tabTree.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				
			}

			@Override
			public void mouseUp(MouseEvent e) {
				//右键的时候设置
				if(e.button == 3) {
					TreeItem[] selection = tabTree.getSelection();
					TreeListItem treeListItem = (TreeListItem)selection[0];
					EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)treeListItem.getData("EcoreTreeNode");
					
					if(ecoreTreeNode != null) {
						if(ecoreTreeNode.getLevel().endsWith(TreeLevelEnum.LEVEL_2.getLevel())) {
							if(treeListItem.getParentItem().getText().equals(TreeParentEnum.MESSAGE_DEFINITIONS.getName())) {
								generateDataMenu.setEnabled(true);
								return;
							}
						}
						
						if (ecoreTreeNode.getLevel().endsWith(TreeLevelEnum.LEVEL_3.getLevel())) {
							if (treeListItem.getParentItem().getParentItem().getText().equalsIgnoreCase(TreeParentEnum.MESSAGE_SETS.getName())) {
								generateDataMenu.setEnabled(true);
								return;
							}
						}
					}
					generateDataMenu.setEnabled(false);
				}
			}
			
		});
		
		generateDataMenu.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// 移除选中的TreeItem
				TreeItem treeItem = tabTree.getSelection()[0];
				TreeListItem treeListItem = (TreeListItem) treeItem;
				EcoreTreeNode ecoreTreeNode = (EcoreTreeNode) treeListItem.getData("EcoreTreeNode");
				
				String path = openFile(ecoreTreeNode.getName(), "xml");
				if(path == null || path == "") {
					return;
				}
				
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {
						MessageDataMgr mgr = new MessageDataMgr();
						try {
							boolean isSuccess = mgr.generateMessageDataXML(ecoreTreeNode.getId(), path);
							if (isSuccess) {
								MessageBoxUtil.showMessageBox("生成Message XML成功!");
							} else {
								MessageBoxUtil.showMessageBox("生成Message XML失败!");
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
			
		});
		return generateDataMenu;
	}
	
	public static MenuItem addGenerateMDR3Menu(Menu menu, Tree tabTree) {
		MenuItem generateDataMenu = new MenuItem(menu, SWT.PUSH);
		generateDataMenu.setText("Generate MDR3");
		generateDataMenu.setEnabled(false);
		
		tabTree.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				
			}

			@Override
			public void mouseUp(MouseEvent e) {
				//右键的时候设置
				if(e.button == 3) {
					TreeItem[] selection = tabTree.getSelection();
					TreeListItem treeListItem = (TreeListItem)selection[0];
					EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)treeListItem.getData("EcoreTreeNode");
					
					if(ecoreTreeNode != null) {
						if(ecoreTreeNode.getLevel().endsWith(TreeLevelEnum.LEVEL_2.getLevel())) {
							if(treeListItem.getParentItem().getText().equals(TreeParentEnum.MESSAGE_SETS.getName())) {
								generateDataMenu.setEnabled(true);
								return;
							}
						}
						
						if (ecoreTreeNode.getLevel().endsWith(TreeLevelEnum.LEVEL_3.getLevel())) {
							if (treeListItem.getParentItem().getParentItem().getText().equalsIgnoreCase(TreeParentEnum.MESSAGE_SETS.getName())) {
								generateDataMenu.setEnabled(true);
								return;
							}
						}
					}
					generateDataMenu.setEnabled(false);
				}
			}
			
		});
		
		generateDataMenu.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// 移除选中的TreeItem
				TreeItem treeItem = tabTree.getSelection()[0];
				TreeListItem treeListItem = (TreeListItem) treeItem;
				EcoreTreeNode ecoreTreeNode = (EcoreTreeNode) treeListItem.getData("EcoreTreeNode");
				
				String path = openMDR3File(ecoreTreeNode.getName());
				if(path == null || path == "") {
					return;
				}
				
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {
//						MessageDataMgr mgr = new MessageDataMgr();
						MDR3Mgr mgr=new MDR3Mgr();
						try {
							boolean isSuccess=mgr.generateMDR3Excel(ecoreTreeNode.getId(),ecoreTreeNode.getName()
			                                ,path);
//							boolean isSuccess = mgr.generateMDR3Excel(ecoreTreeNode.getId(), path);
							if (isSuccess) {
								MessageBoxUtil.showMessageBox("MDR3文件生成成功!");
							} else {
								MessageBoxUtil.showMessageBox("MDR3文件生成失败!");
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
			
		});
		return generateDataMenu;
	}
	/**
	 * 加载第一棵树的右键新建菜单
	 * @param menu
	 * @param tabTree
	 */
	public static void addFirstTabNewChildMenu(Menu menu, Tree tabTree) {

		MenuItem addNewChildMenu = new MenuItem(menu, SWT.CASCADE);
		addNewChildMenu.setText("New Child");

		tabTree.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
				// 右键的时候设置
				if (e.button == 3) {
					Menu childMenu = new Menu(addNewChildMenu);
					addNewChildMenu.setMenu(childMenu);

					TreeItem[] selection = tabTree.getSelection();
					TreeListItem selectedTreeListItem = (TreeListItem) selection[0];
					EcoreTreeNode ecoreTreeNode = (EcoreTreeNode) selectedTreeListItem.getData("EcoreTreeNode");

					if (ecoreTreeNode != null) {
						// 一级菜单
						if (TreeLevelEnum.LEVEL_1.getLevel().equals(ecoreTreeNode.getLevel())) {
							if (TreeParentEnum.DATA_TYPES.getName().equals(ecoreTreeNode.getName())) {
								// 置灰
								addNewChildMenu.setEnabled(false);
							} else if (TreeParentEnum.BUSINESS_COMPONENTS.getName().equals(ecoreTreeNode.getName())) {
								// 显示
								addNewChildMenu.setEnabled(true);

								// 创建
								MenuItem bcMenu = new MenuItem(childMenu, SWT.PUSH);
								bcMenu.setText("Business Component");
								bcMenu.setImage(ImgUtil.createImg(ImgUtil.BC_ADD_ICON));

								bcMenu.addSelectionListener(new SelectionAdapter() {
									@Override
									public void widgetSelected(SelectionEvent e) {
										String uuid = UUID.randomUUID().toString();
										
										TreeListItem treeItem = (TreeListItem) tabTree.getSelection()[0];
										TreeListItem treeListItem = new TreeListItem(treeItem, new TreeItemDataBean("",
												"", ImgUtil.BC, "2", TreeLevelEnum.LEVEL_2.getLevel()));
										
										EcoreTreeNode ecoreTreeNode_parent = new EcoreTreeNode();
										ecoreTreeNode_parent.setLevel(TreeLevelEnum.LEVEL_2.getLevel());
										ecoreTreeNode_parent.setId(uuid);
										treeListItem.setData("EcoreTreeNode", ecoreTreeNode_parent);
										treeListItem.setData("type", TreeParentEnum.BUSINESS_COMPONENTS.getName());

										new TreeListItem(treeListItem, new TreeItemDataBean("", "Properties",
												ImgUtil.BC_BE, "2", TreeLevelEnum.LEVEL_3.getLevel()));

										TreeListItem treeListItem3 = new TreeListItem(treeListItem, new TreeItemDataBean("", "Sub-Types",
												ImgUtil.BC_SUB_TYPES, "2", TreeLevelEnum.LEVEL_3.getLevel()));
										EcoreTreeNode ecoreTreeNode1 = new EcoreTreeNode();
										ecoreTreeNode1.setLevel(TreeLevelEnum.LEVEL_3.getLevel());
										ecoreTreeNode1.setName("Sub-Types");
										ecoreTreeNode1.setId(uuid);
										treeListItem3.setData("EcoreTreeNode", ecoreTreeNode1);
										
										TreeListItem treeListItem4 = new TreeListItem(treeListItem, new TreeItemDataBean("", "Super-Types",
												ImgUtil.BC_SUPER_TYPES, "2", TreeLevelEnum.LEVEL_3.getLevel()));
										EcoreTreeNode ecoreTreeNode2 = new EcoreTreeNode();
										ecoreTreeNode2.setLevel(TreeLevelEnum.LEVEL_3.getLevel());
										ecoreTreeNode2.setName("Super-Types");
										ecoreTreeNode2.setId(uuid);
										treeListItem4.setData("EcoreTreeNode", ecoreTreeNode2);

										tabTree.setSelection(treeListItem);
										treeListItem.getParentItem().setExpanded(true);
										treeListItem.setExpanded(true);

										// 打开editor编辑器
										TransferDataBean transferDataBean = new TransferDataBean();
										transferDataBean.setId(uuid);
										transferDataBean.setName("");
										transferDataBean.setLevel(TreeLevelEnum.LEVEL_2.getLevel());
										transferDataBean.setType(TreeParentEnum.BUSINESS_COMPONENTS.getName());
										transferDataBean.setImgPath(ImgUtil.BC);
										transferDataBean.setTreeListItem(treeListItem);

										EditorUtil.open(TreeParentEnum.BUSINESS_COMPONENTS.getName(), transferDataBean);
									}
								});
							}
						} else if (TreeLevelEnum.LEVEL_2.getLevel().equals(ecoreTreeNode.getLevel())) {
							// 打开
							addNewChildMenu.setEnabled(true);

							String imgPath;
							if (DataTypesEnum.CODE_SETS.getName().equals(ecoreTreeNode.getName())) {
								imgPath = ImgUtil.CODE_SET_SUB1_2;
							} else if (DataTypesEnum.TEXT.getName().equals(ecoreTreeNode.getName())) {
								imgPath = ImgUtil.TEXT_SUB1_WITHOUT_LOCK;
							} else if (DataTypesEnum.BOOLEAN.getName().equals(ecoreTreeNode.getName())) {
								imgPath = ImgUtil.BOOLEAN_SUB1;
							} else if (DataTypesEnum.INDICATOR.getName().equals(ecoreTreeNode.getName())) {
								imgPath = ImgUtil.INDICATOR_SUB1_WITHOUT_LOCK;
							} else if (DataTypesEnum.DECIMAL.getName().equals(ecoreTreeNode.getName())) {
								imgPath = ImgUtil.DECIMAL_SUB1;
							} else if (DataTypesEnum.RATE.getName().equals(ecoreTreeNode.getName())) {
								imgPath = ImgUtil.RATE_SUB1_WITHOUT_LOCK;
							} else if (DataTypesEnum.AMOUNT.getName().equals(ecoreTreeNode.getName())) {
								imgPath = ImgUtil.AMOUNT_SUB1_WITHOUT_LOCK;
							} else if (DataTypesEnum.QUANTITY.getName().equals(ecoreTreeNode.getName())) {
								imgPath = ImgUtil.QUANTITY_SUB1_WITHOUT_LOCK;
							} else if (DataTypesEnum.TIME.getName().equals(ecoreTreeNode.getName())) {
								imgPath = ImgUtil.TIME_SUB1_WITHOUT_LOCK;
							} else if (DataTypesEnum.BINARY.getName().equals(ecoreTreeNode.getName())) {
								imgPath = ImgUtil.BINARY_SUB1_WITHOUT_LOCK;
							} else if (DataTypesEnum.SCHEMA_TYPES.getName().equals(ecoreTreeNode.getName())) {
								imgPath = ImgUtil.SCHEMA_TYPES_SUB1;
							} else if (DataTypesEnum.USER_DEFINED.getName().equals(ecoreTreeNode.getName())) {
								imgPath = ImgUtil.USER_DEFINED_SUB1;
							} else {
								// business component
								addNewChildMenu.setEnabled(false);
								return;
							}

							// 二级菜单
							MenuItem dataTypeMenuItem = new MenuItem(childMenu, SWT.PUSH);
							dataTypeMenuItem.setText(ecoreTreeNode.getName());
							dataTypeMenuItem.setImage(ImgUtil.createImg(ImgUtil.BC_ADD_ICON));

							dataTypeMenuItem.addSelectionListener(new SelectionAdapter() {
								@Override
								public void widgetSelected(SelectionEvent e) {
									String uuid = UUID.randomUUID().toString();
									
									TreeListItem treeItem = (TreeListItem) tabTree.getSelection()[0];
									TreeListItem treeListItem = new TreeListItem(treeItem, new TreeItemDataBean("", "",
											imgPath, "1", TreeLevelEnum.LEVEL_3.getLevel()));
									
									EcoreTreeNode ecoreTreeNode_parent = new EcoreTreeNode();
									ecoreTreeNode_parent.setLevel(TreeLevelEnum.LEVEL_3.getLevel());
									ecoreTreeNode_parent.setId(uuid);
									treeListItem.setData("EcoreTreeNode", ecoreTreeNode_parent);
									treeListItem.setData("type", ecoreTreeNode.getName());

									tabTree.setSelection(treeListItem);
									treeListItem.getParentItem().setExpanded(true);
									treeListItem.setExpanded(true);

									// 打开editor编辑器
									TransferDataBean transferDataBean = new TransferDataBean();
									transferDataBean.setId(uuid);
									transferDataBean.setName("");
									transferDataBean.setLevel(TreeLevelEnum.LEVEL_3.getLevel());
									transferDataBean.setType(ecoreTreeNode.getName());
									transferDataBean.setImgPath(imgPath);
									transferDataBean.setTreeListItem(treeListItem);
									EditorUtil.open(ecoreTreeNode.getName(), transferDataBean);
								}
							});
						} else if (TreeLevelEnum.LEVEL_3.getLevel().equals(ecoreTreeNode.getLevel())) {
							// 打开
							addNewChildMenu.setEnabled(true);

							// 三级菜单
							MenuItem dataTypeMenuItem = new MenuItem(childMenu, SWT.PUSH);
							dataTypeMenuItem.setImage(ImgUtil.createImg(ImgUtil.BC_ADD_ICON));

							if ("Properties".equals(ecoreTreeNode.getName())) {
								dataTypeMenuItem.setText("Business Element");
								dataTypeMenuItem.setEnabled(false);
								return;
							} else if ("Sub-Types".equals(ecoreTreeNode.getName())) {
								dataTypeMenuItem.setText("Business Component");
							} else if ("Super-Types".equals(ecoreTreeNode.getName())) {
								dataTypeMenuItem.setText("Business Component");
							} else {
								// 非business component
								addNewChildMenu.setEnabled(false);
								return;
							}

							dataTypeMenuItem.addSelectionListener(new SelectionAdapter() {
								@Override
								public void widgetSelected(SelectionEvent e) {
									try {
											MessageBoxUtil.selectBCTypes(tabTree, ecoreTreeNode.getName(),ecoreTreeNode.getId());								
									} catch (Exception e1) {
										e1.printStackTrace();
									}
								}
							});
						}
					} else {
						addNewChildMenu.setEnabled(false);
					}
				}
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
	}
	
	/**
	 * 第一个tab页的 Derive Code from selection
	 * @param menu
	 * @param tabTree
	 */
	public static void addNewDerive(Menu menu, Tree tabTree) {
		MenuItem newDeriveMenu = new MenuItem(menu, SWT.CASCADE);
		newDeriveMenu.setText("Derive Code from selection");
		tabTree.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
				// 右键的时候设置
				if (e.button == 3) {
					TreeItem[] selection = tabTree.getSelection();
					selectedTreeListItem = (TreeListItem) selection[0];
					EcoreTreeNode ecoreTreeNode = (EcoreTreeNode) selectedTreeListItem.getData("EcoreTreeNode");
					newDeriveMenu.setData("ecoreTreeNode", ecoreTreeNode);
					
					if (ecoreTreeNode != null) {
						// 一级菜单
						if (TreeLevelEnum.LEVEL_3.getLevel().equals(ecoreTreeNode.getLevel())) {
							parentItem = (TreeListItem)selectedTreeListItem.getParentItem();
							if(DataTypesEnum.CODE_SETS.getName().equals(parentItem.getText())) {
								if("1".equals(ecoreTreeNode.getObjType())) { // 1:  可派生的    2:  被派生的 
									newDeriveMenu.setEnabled(true);
									return;
								}
							}
						}
					}
					newDeriveMenu.setEnabled(false);
				}
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		
		newDeriveMenu.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
					try {
						EcoreTreeNode ecoreTreeNode1 = (EcoreTreeNode)newDeriveMenu.getData("ecoreTreeNode");
						EcoreDataTypeVO ecoreDataTypeVO = ecoreDataTypeDao.newCodeSetDerive(ecoreTreeNode1.getId());
						if(ecoreDataTypeVO != null) {
							TreeListItem treeListItem = new TreeListItem(parentItem, new TreeItemDataBean(ecoreDataTypeVO.getEcoreDataType().getId(), ecoreDataTypeVO.getEcoreDataType().getName(),
									ImgUtil.CODE_SET_SUB1, "1", TreeLevelEnum.LEVEL_3.getLevel()), parentItem.indexOf(selectedTreeListItem));
							
							// 设置打开事件必备
							EcoreTreeNode ecoreTreeNodeDT = new EcoreTreeNode();
							ecoreTreeNodeDT.setId(ecoreDataTypeVO.getEcoreDataType().getId());
							ecoreTreeNodeDT.setName(ecoreDataTypeVO.getEcoreDataType().getName());
							ecoreTreeNodeDT.setType(DataTypesEnum.CODE_SETS.getName());
							ecoreTreeNodeDT.setObjType("2");
							ecoreTreeNodeDT.setLevel(TreeLevelEnum.LEVEL_3.getLevel());
							
							treeListItem.setData("type", DataTypesEnum.CODE_SETS.getName());
							treeListItem.setData("EcoreTreeNode", ecoreTreeNodeDT);

							if (ecoreDataTypeVO.getEcoreCodeVOs() != null && ecoreDataTypeVO.getEcoreCodeVOs().size() > 0) {
								for (EcoreCodeVO ecoreCodeVO : ecoreDataTypeVO.getEcoreCodeVOs()) {
									if (ecoreDataTypeVO.getEcoreDataType().getType().equals(DataTypesEnum.CODE_SETS.getType())) {
										TreeListItem item = new TreeListItem(treeListItem, new TreeItemDataBean(ecoreCodeVO.getEcoreCode().getId(),
														ecoreCodeVO.getEcoreCode().getName(), ImgUtil.CODE_SET_SUB2, "1", TreeLevelEnum.LEVEL_4.getLevel()));
										EcoreTreeNode ecoreTreeNodeCS = new EcoreTreeNode();
										ecoreTreeNodeCS.setLevel(TreeLevelEnum.LEVEL_4.getLevel());
										ecoreTreeNodeCS.setName(ecoreCodeVO.getEcoreCode().getName());
										String childId = ecoreCodeVO.getEcoreCode().getId();
										ecoreTreeNodeCS.setId(ecoreTreeNodeDT.getId());
										item.setData("childId", childId);
										item.setData("EcoreTreeNode", ecoreTreeNodeCS);
										item.setData("type", DataTypesEnum.CODE.getName());
									}
								}
							}
							return;
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
//				}

			}
		});		
	}
	
	/**
	 * 第一个tab页的new version
	 * @param menu
	 * @param tabTree
	 */
	public static void addNewVersion(Menu menu, Tree tabTree) {
		MenuItem newVersionMenu = new MenuItem(menu, SWT.CASCADE);
		newVersionMenu.setText("New Version");
		newVersionMenu.setImage(ImgUtil.createImg(ImgUtil.NEW_VERSION));
		tabTree.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
				// 右键的时候设置
				if (e.button == 3) {
					TreeItem[] selection = tabTree.getSelection();
					TreeListItem selectedTreeListItem = (TreeListItem) selection[0];
					EcoreTreeNode ecoreTreeNode = (EcoreTreeNode) selectedTreeListItem.getData("EcoreTreeNode");
					newVersionMenu.setData("ecoreTreeNode", ecoreTreeNode);
					if (ecoreTreeNode != null) {
						// 一级菜单
						if (TreeLevelEnum.LEVEL_3.getLevel().equals(ecoreTreeNode.getLevel())) {
							TreeListItem parentItem = (TreeListItem)selectedTreeListItem.getParentItem();
							if(DataTypesEnum.CODE_SETS.getName().equals(parentItem.getText())) {
								if("2".equals(ecoreTreeNode.getObjType())) {
									newVersionMenu.setEnabled(true);
									newVersionMenu.setData("count", 1);
									newVersionMenu.addSelectionListener(new SelectionAdapter() {
										@Override
										public void widgetSelected(SelectionEvent e) {
											int count = (int)newVersionMenu.getData("count");
											if (count++ == 1) {
												 newVersionMenu.setData("count", count);
												 
												try {
													
													EcoreTreeNode ecoreTreeNode1 = (EcoreTreeNode)newVersionMenu.getData("ecoreTreeNode");
													EcoreDataTypeVO ecoreDataTypeVO = ecoreDataTypeDao.newCodeSetVersion(ecoreTreeNode1.getId());
													
													if(ecoreDataTypeVO != null) {
														TreeListItem treeListItem = new TreeListItem(parentItem, new TreeItemDataBean(ecoreDataTypeVO.getEcoreDataType().getId(), ecoreDataTypeVO.getEcoreDataType().getName(),
																ImgUtil.CODE_SET_SUB1, "1", TreeLevelEnum.LEVEL_3.getLevel()));
														// 设置打开事件必备
														EcoreTreeNode ecoreTreeNodeDT = new EcoreTreeNode();
														ecoreTreeNodeDT.setId(ecoreDataTypeVO.getEcoreDataType().getId());
														ecoreTreeNodeDT.setName(ecoreDataTypeVO.getEcoreDataType().getName());
														ecoreTreeNodeDT.setType(DataTypesEnum.CODE_SETS.getName());
														ecoreTreeNodeDT.setObjType("2");
														ecoreTreeNodeDT.setLevel(TreeLevelEnum.LEVEL_3.getLevel());
														
														treeListItem.setData("type", DataTypesEnum.CODE_SETS.getName());
														treeListItem.setData("EcoreTreeNode", ecoreTreeNodeDT);
	
														if (ecoreDataTypeVO.getEcoreCodeVOs() != null && ecoreDataTypeVO.getEcoreCodeVOs().size() > 0) {
															for (EcoreCodeVO ecoreCodeVO : ecoreDataTypeVO.getEcoreCodeVOs()) {
																if (ecoreDataTypeVO.getEcoreDataType().getType().equals(DataTypesEnum.CODE_SETS.getType())) {
																	TreeListItem item = new TreeListItem(treeListItem, new TreeItemDataBean(ecoreCodeVO.getEcoreCode().getId(),
																					ecoreCodeVO.getEcoreCode().getName(), ImgUtil.CODE_SET_SUB2, "1", TreeLevelEnum.LEVEL_4.getLevel()));
																	EcoreTreeNode ecoreTreeNodeCS = new EcoreTreeNode();
																	ecoreTreeNodeCS.setLevel(TreeLevelEnum.LEVEL_4.getLevel());
																	String childId = ecoreCodeVO.getEcoreCode().getId();
																	ecoreTreeNodeCS.setId(ecoreTreeNodeDT.getId());
																	item.setData("childId", childId);
																	item.setData("EcoreTreeNode", ecoreTreeNodeCS);
																	item.setData("type", DataTypesEnum.CODE_SETS.getName());
																}
															}
														}
														newVersionMenu.setSelection(false);
														
														return;
													}
												} catch (Exception e1) {
													e1.printStackTrace();
												}
											}
										}
									});
									return;
								}
							}
						}
					}
					newVersionMenu.setEnabled(false);
				}
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
	}

	/**
	 * 第二个tab页
	 * @param menu
	 * @param tabTree
	 */
	public static void addSecondTabNewChildMenu(Menu menu, Tree tabTree) {

		MenuItem addNewChildMenu = new MenuItem(menu, SWT.CASCADE);
		addNewChildMenu.setText("New Child");

		tabTree.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
				// 右键的时候设置
				if (e.button == 3) {
					Menu childMenu = new Menu(addNewChildMenu);
					addNewChildMenu.setMenu(childMenu);

					TreeItem[] selection = tabTree.getSelection();
					TreeListItem selectedTreeListItem = (TreeListItem) selection[0];
					EcoreTreeNode ecoreTreeNode = (EcoreTreeNode) selectedTreeListItem.getData("EcoreTreeNode");

					if (ecoreTreeNode != null) {
						// 一级菜单
						if (TreeLevelEnum.LEVEL_1.getLevel().equals(ecoreTreeNode.getLevel())) {
							String text = "";
							if (TreeParentEnum.EXTERNAL_SCHEMAS.getName().equals(ecoreTreeNode.getName())) {
								text = "External Schema";
							} else if (TreeParentEnum.MESSAGE_COMPONENTS.getName().equals(ecoreTreeNode.getName())) {
								text = "Message Component";
							}
							
							// 显示
							addNewChildMenu.setEnabled(true);
							// 创建
							MenuItem bcMenu = new MenuItem(childMenu, SWT.PUSH);
							bcMenu.setText(text);
							bcMenu.setImage(ImgUtil.createImg(ImgUtil.BC_ADD_ICON));

							bcMenu.addSelectionListener(new SelectionAdapter() {
								@Override
								public void widgetSelected(SelectionEvent e) {
									String uuid = UUID.randomUUID().toString();
									
									String itemImg = "";
									String tabType = "";
									String tabIndex = "";
									if (TreeParentEnum.EXTERNAL_SCHEMAS.getName().equals(ecoreTreeNode.getName())) {
										itemImg = ImgUtil.EXTERNAL_SCHEMAS;
										tabType = TreeParentEnum.EXTERNAL_SCHEMAS.getName();
										tabIndex = "3";
									} else if (TreeParentEnum.MESSAGE_COMPONENTS.getName().equals(ecoreTreeNode.getName())) {
										itemImg = ImgUtil.MC_SUB3_COMPONENT;
										tabType = TreeParentEnum.MESSAGE_COMPONENTS.getName();
										tabIndex = "4";
									}
									
									TreeListItem treeItem = (TreeListItem) tabTree.getSelection()[0];
									TreeListItem treeListItem = new TreeListItem(treeItem, new TreeItemDataBean("",
											"", itemImg, tabIndex, TreeLevelEnum.LEVEL_2.getLevel()));
									
									EcoreTreeNode ecoreTreeNode_parent = new EcoreTreeNode();
									ecoreTreeNode_parent.setLevel(TreeLevelEnum.LEVEL_2.getLevel());
									ecoreTreeNode_parent.setId(uuid);
									treeListItem.setData("EcoreTreeNode", ecoreTreeNode_parent);
									treeListItem.setData("type", ecoreTreeNode.getName());

									tabTree.setSelection(treeListItem);
									treeListItem.getParentItem().setExpanded(true);
									treeListItem.setExpanded(true);

									// 打开editor编辑器
									TransferDataBean transferDataBean = new TransferDataBean();
									transferDataBean.setId(uuid);
									transferDataBean.setName("");
									transferDataBean.setLevel(TreeLevelEnum.LEVEL_2.getLevel());
									transferDataBean.setType(tabType);
									transferDataBean.setTreeListItem(treeListItem);
									
									EditorUtil.open(tabType, transferDataBean);
								}
							});
						} else if (TreeLevelEnum.LEVEL_2.getLevel().equals(ecoreTreeNode.getLevel())) {
							// 打开
							addNewChildMenu.setEnabled(false);
							return;
//							if(TreeParentEnum.MESSAGE_COMPONENTS.getName().equals(selectedTreeListItem.getParentItem().getText())) {
//								// 二级菜单
//								MenuItem messageElementMenu = new MenuItem(childMenu, SWT.PUSH);
//								messageElementMenu.setText("Message Element/BuildingBlock");
//								messageElementMenu.setImage(ImgUtil.createImg(ImgUtil.BC_ADD_ICON));
//								messageElementMenu.addSelectionListener(new SelectionAdapter() {
//									@Override
//									public void widgetSelected(SelectionEvent e) {
//										TreeListItem treeItem = (TreeListItem) tabTree.getSelection()[0];
//										TreeListItem treeListItem = new TreeListItem(treeItem, new TreeItemDataBean("", "",
//												ImgUtil.MC_SUB2_COMPONENT, "4", TreeLevelEnum.LEVEL_3.getLevel()));
//
//										tabTree.setSelection(treeListItem);
//										treeListItem.getParentItem().setExpanded(true);
//										treeListItem.setExpanded(true);
//
//										// 打开editor编辑器
//										TransferDataBean transferDataBean = new TransferDataBean();
//										transferDataBean.setId(UUID.randomUUID().toString());
//										transferDataBean.setName("");
//										transferDataBean.setLevel(TreeLevelEnum.LEVEL_3.getLevel());
//										transferDataBean.setType(TreeParentEnum.MESSAGE_COMPONENTS.getName());
//										transferDataBean.setTreeListItem(treeListItem);
//										
//										EditorUtil.open(EditorUtil.CREATE_STATUS, TreeParentEnum.MESSAGE_COMPONENTS.getName(), transferDataBean);
//									}
//								});
//								
//							}else {
//								addNewChildMenu.setEnabled(false);
//								return;
//							}
						} else {
							addNewChildMenu.setEnabled(false);
						}
					} else {
						addNewChildMenu.setEnabled(false);
					}
				}
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
	}
	
	/**
	 * 第三个tab页
	 * @param menu
	 * @param tabTree
	 */
	public static void addThirdTabNewChildMenu(Menu menu, Tree tabTree) {
		
		MenuItem addNewChildMenu = new MenuItem(menu, SWT.CASCADE);
		addNewChildMenu.setText("New Child");

		tabTree.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
				// 右键的时候设置
				if (e.button == 3) {
					Menu childMenu = new Menu(addNewChildMenu);
					addNewChildMenu.setMenu(childMenu);

					TreeItem[] selection = tabTree.getSelection();
					TreeListItem selectedTreeListItem = (TreeListItem) selection[0];
					EcoreTreeNode ecoreTreeNode = (EcoreTreeNode) selectedTreeListItem.getData("EcoreTreeNode");

					if (ecoreTreeNode != null) {
						// 一级菜单
						if (TreeLevelEnum.LEVEL_1.getLevel().equals(ecoreTreeNode.getLevel())) {
							String text = "";
							if (TreeParentEnum.BUSINESS_AREAS.getName().equals(ecoreTreeNode.getName())) {
								text = "Business Area";
							} else if (TreeParentEnum.MESSAGE_SETS.getName().equals(ecoreTreeNode.getName())) {
								text = "Message Set";
							} else if(TreeParentEnum.MESSAGE_DEFINITIONS.getName().equals(ecoreTreeNode.getName())){
								text = "Message Definition";
							}
							
							// 显示
							addNewChildMenu.setEnabled(true);
							// 创建
							MenuItem bcMenu = new MenuItem(childMenu, SWT.PUSH);
							bcMenu.setText(text);
							bcMenu.setImage(ImgUtil.createImg(ImgUtil.BC_ADD_ICON));

							bcMenu.addSelectionListener(new SelectionAdapter() {
								@Override
								public void widgetSelected(SelectionEvent e) {
									String uuid = UUID.randomUUID().toString();
									String itemImg = "";
									String tabType = "";
									String tabIndex = "";
									if (TreeParentEnum.BUSINESS_AREAS.getName().equals(ecoreTreeNode.getName())) {
										itemImg = ImgUtil.BUSINESS_AREAS;
										tabType = TreeParentEnum.BUSINESS_AREAS.getName();
										tabIndex = "5";
									} else if (TreeParentEnum.MESSAGE_SETS.getName().equals(ecoreTreeNode.getName())) {
										itemImg = ImgUtil.MS_SUB1;
										tabType = TreeParentEnum.MESSAGE_SETS.getName();
										tabIndex = "6";
									} else if (TreeParentEnum.MESSAGE_DEFINITIONS.getName().equals(ecoreTreeNode.getName())) {
										itemImg = ImgUtil.MESSAGE_DEFINITIONS;
										tabType = TreeParentEnum.MESSAGE_DEFINITIONS.getName();
										tabIndex = "7";
									}
									
									TreeListItem treeItem = (TreeListItem) tabTree.getSelection()[0];
									TreeListItem treeListItem = new TreeListItem(treeItem, new TreeItemDataBean("",
											"", itemImg, tabIndex, TreeLevelEnum.LEVEL_2.getLevel()));
									
									EcoreTreeNode ecoreTreeNode_parent = new EcoreTreeNode();
									ecoreTreeNode_parent.setLevel(TreeLevelEnum.LEVEL_2.getLevel());
									ecoreTreeNode_parent.setId(uuid);
									ecoreTreeNode_parent.setImgPath(itemImg);
									treeListItem.setData("EcoreTreeNode", ecoreTreeNode_parent);
									treeListItem.setData("type", ecoreTreeNode.getName());

									tabTree.setSelection(treeListItem);
									treeListItem.getParentItem().setExpanded(true);
									treeListItem.setExpanded(true);

									// 打开editor编辑器
									TransferDataBean transferDataBean = new TransferDataBean();
									transferDataBean.setId(uuid);
									transferDataBean.setName("");
									transferDataBean.setLevel(TreeLevelEnum.LEVEL_2.getLevel());
									transferDataBean.setType(tabType);
									transferDataBean.setTreeListItem(treeListItem);
									transferDataBean.setImgPath(itemImg);

									EditorUtil.open(tabType, transferDataBean);
								}
							});
						} else {
							addNewChildMenu.setEnabled(false);
						}
					} else {
						addNewChildMenu.setEnabled(false);
					}
				}
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
	}
	
	/**
	 * 第四个tab页
	 * @param menu
	 * @param tabTree
	 */
	public static void addFourthTabNewChildMenu(Menu menu, Tree tabTree) {
		
		MenuItem addNewChildMenu = new MenuItem(menu, SWT.CASCADE);
		addNewChildMenu.setText("New Child");

		tabTree.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
				// 右键的时候设置
				if (e.button == 3) {
					Menu childMenu = new Menu(addNewChildMenu);
					addNewChildMenu.setMenu(childMenu);

					TreeItem[] selection = tabTree.getSelection();
					TreeListItem selectedTreeListItem = (TreeListItem) selection[0];
					EcoreTreeNode ecoreTreeNode = (EcoreTreeNode) selectedTreeListItem.getData("EcoreTreeNode");

					if (ecoreTreeNode != null) {
						// 一级菜单
						if (TreeLevelEnum.LEVEL_1.getLevel().equals(ecoreTreeNode.getLevel())) {
							String text = "View";
							
							// 显示
							addNewChildMenu.setEnabled(true);
							// 创建
							MenuItem bcMenu = new MenuItem(childMenu, SWT.PUSH);
							bcMenu.setText(text);
							bcMenu.setImage(ImgUtil.createImg(ImgUtil.BC_ADD_ICON));

							bcMenu.addSelectionListener(new SelectionAdapter() {
								@Override
								public void widgetSelected(SelectionEvent e) {
									String uuid = UUID.randomUUID().toString();
									String itemImg = "";
									String tabType = "";
									String tabIndex = "";
									if (TreeParentEnum.VIEW_IDENTICAL_COMPONENTS.getName().equals(ecoreTreeNode.getName())) {
										itemImg = ImgUtil.VIEW_IDENTICAL_COMPONENTS;
										tabType = TreeParentEnum.VIEW_IDENTICAL_COMPONENTS.getName();
										tabIndex = "8";
									}
									
									TreeListItem treeItem = (TreeListItem) tabTree.getSelection()[0];
									TreeListItem treeListItem = new TreeListItem(treeItem, new TreeItemDataBean("",
											"", itemImg, tabIndex, TreeLevelEnum.LEVEL_2.getLevel()));

									tabTree.setSelection(treeListItem);
									treeListItem.getParentItem().setExpanded(true);
									treeListItem.setExpanded(true);

									// 打开editor编辑器
									TransferDataBean transferDataBean = new TransferDataBean();
									transferDataBean.setId(uuid);
									transferDataBean.setName("");
									transferDataBean.setLevel(TreeLevelEnum.LEVEL_2.getLevel());
									transferDataBean.setType(tabType);
									transferDataBean.setTreeListItem(treeListItem);

									EditorUtil.open(tabType, transferDataBean);
								}
							});
						} else {
							addNewChildMenu.setEnabled(false);
						}
					} else {
						addNewChildMenu.setEnabled(false);
					}
				}
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
	}
	
	/**
	 * 第一个tab页的删除
	 * @param menu
	 * @param tabTree
	 */
	public static void addFirstTabDeleteMenu(Menu menu, Tree tabTree) {
		
		MenuItem mi = new MenuItem(menu, SWT.PUSH);
		mi.setText("Delete");
		mi.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
		mi.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell parent = new Shell(Display.getCurrent());
				MessageBox messageBox = new MessageBox(parent, SWT.OK | SWT.CANCEL);
				messageBox.setMessage("是否确认删除？");
				if (messageBox.open() == SWT.OK) {
					// 移除选中的TreeItem
					TreeItem treeItem = tabTree.getSelection()[0];
					Object type = treeItem.getData("type");
					Object data = treeItem.getData("EcoreTreeNode");
					EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)data;
					if(TreeParentEnum.DATA_TYPES.getName().equals(String.valueOf(type)) || DataTypesEnum.checkTypeName(String.valueOf(type))) {
						try {
							if(ecoreTreeNode.getId() != null) {
								boolean deleteStatus = ecoreDataTypeDao.deleteEcoreDataType(ecoreTreeNode.getId());
								/*
								 * if(!deleteStatus) { //删除失败 MessageBoxUtil.deleteFail(); }
								 */
							}
							treeItem.dispose();
						} catch (Exception e1) {
							e1.printStackTrace();
							ExceptionHandleUtil.handle(e1);
							//删除失败
							MessageBoxUtil.deleteFail();
						}
					}else if(TreeParentEnum.BUSINESS_COMPONENTS.getName().equals(String.valueOf(type))) {
						try {
							if(ecoreTreeNode.getId() != null) {
								boolean deleteStatus = businessComponentDao.deleteEcoreBusinessComponent(ecoreTreeNode.getId());
//								if(!deleteStatus) {
//									//删除失败
//									MessageBoxUtil.deleteFail();
//								}
							}
							treeItem.dispose();
						} catch (Exception e1) {
							e1.printStackTrace();
							ExceptionHandleUtil.handle(e1);
							//删除失败
							MessageBoxUtil.deleteFail();
						}
					}
					
					// 关闭被删除节点, 对应打开的Editor
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					for (IEditorReference editorReference: page.getEditorReferences()) {
						String id = editorReference.getPartProperty("ID");
						if (ecoreTreeNode.getId().equals(id)) {
							page.closeEditor(editorReference.getEditor(false), false);
							break;
						}
					}						
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		/**
		 * 右键点击事件
		 */
		tabTree.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (e.button == 3) {
					// 移除选中的TreeItem
					TreeListItem selectedTreeListItem = (TreeListItem)tabTree.getSelection()[0];
					EcoreTreeNode ecoreTreeNode = (EcoreTreeNode) selectedTreeListItem.getData("EcoreTreeNode");
					mi.setEnabled(false);
					if(ecoreTreeNode != null) {
						if(TreeLevelEnum.LEVEL_2.getLevel().equals(ecoreTreeNode.getLevel())) {
							if ( TreeParentEnum.BUSINESS_COMPONENTS.getName().equals(selectedTreeListItem.getParentItem().getText()) 
									&& RegistrationStatus.Added.getStatus().equals(ecoreTreeNode.getRegistrationStatus()) || ecoreTreeNode.getRegistrationStatus() == null ) {
								mi.setEnabled(true);
							}
						}else if(TreeLevelEnum.LEVEL_3.getLevel().equals(ecoreTreeNode.getLevel())) {
							if ( TreeParentEnum.DATA_TYPES.getName().equals(selectedTreeListItem.getParentItem().getParentItem().getText()) 
									&& RegistrationStatus.Added.getStatus().equals(ecoreTreeNode.getRegistrationStatus()) || ecoreTreeNode.getRegistrationStatus() == null ) {
								mi.setEnabled(true);
							}
						}
					}
				}
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
	}
	
	/**
	 * 第二个tab页的删除
	 * @param menu
	 * @param tabTree
	 */
	public static void addSecondTabDeleteMenu(Menu menu, Tree tabTree) {
		
		MenuItem mi = new MenuItem(menu, SWT.PUSH);
		mi.setText("Delete");
		mi.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
		mi.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell parent = new Shell(Display.getCurrent());
				MessageBox messageBox = new MessageBox(parent, SWT.OK | SWT.CANCEL);
				messageBox.setMessage("是否确认删除？");
				if (messageBox.open() == SWT.OK) {
					// 移除选中的TreeItem
					TreeItem treeItem = tabTree.getSelection()[0];
					Object type = treeItem.getData("type");
					Object data = treeItem.getData("EcoreTreeNode");
					EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)data;
					if(TreeParentEnum.EXTERNAL_SCHEMAS.getName().equals(String.valueOf(type))) {
						try {
							if(ecoreTreeNode.getId() != null) {
								boolean deleteStatus = externalSchemaDao.deleteEcoreExternalSchema(ecoreTreeNode.getId());
//								if(!deleteStatus) {
//									//删除失败
//									MessageBoxUtil.deleteFail();
//								}
							}
							
							treeItem.dispose();
						} catch (Exception e1) {
							e1.printStackTrace();
							ExceptionHandleUtil.handle(e1);
							//删除失败
							MessageBoxUtil.deleteFail();
						}
					}else if(TreeParentEnum.MESSAGE_COMPONENTS.getName().equals(String.valueOf(type))) {
						try {
							if(ecoreTreeNode.getId() != null) {
								boolean deleteStatus = messageComponentDao.deleteEcoreMessageComponent(ecoreTreeNode.getId());
//								if(!deleteStatus) {
//									//删除失败
//									MessageBoxUtil.deleteFail();
//								}
							}
							treeItem.dispose();
						} catch (Exception e1) {
							e1.printStackTrace();
							ExceptionHandleUtil.handle(e1);
							//删除失败
							MessageBoxUtil.deleteFail();
						}
					}
					// 关闭被删除节点, 对应打开的Editor
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					for (IEditorReference editorReference: page.getEditorReferences()) {
						String id = editorReference.getPartProperty("ID");
						if (ecoreTreeNode.getId().equals(id)) {
							page.closeEditor(editorReference.getEditor(false), false);
							break;
						}
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		/**
		 * 右键点击事件
		 */
		tabTree.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (e.button == 3) {
					// 移除选中的TreeItem
					TreeListItem selectedTreeListItem = (TreeListItem)tabTree.getSelection()[0];
					EcoreTreeNode ecoreTreeNode = (EcoreTreeNode) selectedTreeListItem.getData("EcoreTreeNode");
					mi.setEnabled(false);
					if(ecoreTreeNode != null) {
						if(TreeLevelEnum.LEVEL_2.getLevel().equals(ecoreTreeNode.getLevel())) {
							if( (TreeParentEnum.EXTERNAL_SCHEMAS.getName().equals(selectedTreeListItem.getParentItem().getText()) 
									&& RegistrationStatus.Added.getStatus().equals(ecoreTreeNode.getRegistrationStatus()) || ecoreTreeNode.getRegistrationStatus() == null) || 
									(TreeParentEnum.MESSAGE_COMPONENTS.getName().equals(selectedTreeListItem.getParentItem().getText()) 
											&& RegistrationStatus.Added.getStatus().equals(ecoreTreeNode.getRegistrationStatus()) || ecoreTreeNode.getRegistrationStatus() == null) ) {
								mi.setEnabled(true);
							}
						}
					}
				}
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
	}
	
	/**
	 * 第三个tab页的删除
	 * @param menu
	 * @param tabTree
	 */
	public static void addThirdTabDeleteMenu(Menu menu, Tree tabTree) {
		
		MenuItem mi = new MenuItem(menu, SWT.PUSH);
		mi.setText("Delete");
		mi.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
		mi.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell parent = new Shell(Display.getCurrent());
				MessageBox messageBox = new MessageBox(parent, SWT.OK | SWT.CANCEL);
				messageBox.setMessage("是否确认删除？");
				if (messageBox.open() == SWT.OK) {
					// 移除选中的TreeItem
					TreeItem treeItem = tabTree.getSelection()[0];
					Object type = treeItem.getData("type");
					Object data = treeItem.getData("EcoreTreeNode");
					EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)data;
					if(TreeParentEnum.BUSINESS_AREAS.getName().equals(String.valueOf(type))) {
						try {
							if(ecoreTreeNode.getId() != null) {
								boolean deleteStatus = businessAreaDao.deleteEcoreBusinessArea(ecoreTreeNode.getId());
//								if(!deleteStatus) {
//									//删除失败
//									MessageBoxUtil.deleteFail();
//								}
								treeItem.dispose();
								
							}
							
						} catch (Exception e1) {
							e1.printStackTrace();
							ExceptionHandleUtil.handle(e1);
							//删除失败
							MessageBoxUtil.deleteFail();
						}
					}else if(TreeParentEnum.MESSAGE_SETS.getName().equals(String.valueOf(type))) {
						try {
							if(ecoreTreeNode.getId() != null) {
								boolean deleteStatus = ecoreMessageSetDao.deleteEcoreMessageSet(ecoreTreeNode.getId());
//								if(!deleteStatus) {
//									//删除失败
//									MessageBoxUtil.deleteFail();
//								}
								treeItem.dispose();
							}
							
						} catch (Exception e1) {
							e1.printStackTrace();
							ExceptionHandleUtil.handle(e1);
							//删除失败
							MessageBoxUtil.deleteFail();
						}
					}else if(TreeParentEnum.MESSAGE_DEFINITIONS.getName().equals(String.valueOf(type))) {
						try {
							if(ecoreTreeNode.getId() != null) {
								ArrayList<EcoreMessageSet> msgSetList = DerbyDaoUtil.getMsgSetListByMsgDefinitionId(ecoreTreeNode.getId());
								EcoreBusinessArea ecoreBusinessArea = DerbyDaoUtil.getBusinessAreaByDefinitionId(ecoreTreeNode.getId());
								boolean deleteStatus = ecoreMessageDefinitionDao.deleteEcoreMessageDefinition(ecoreTreeNode.getId());
//								if(!deleteStatus) {
									//删除失败
//									MessageBoxUtil.deleteFail();
//									treeItem.dispose();
//								}
								treeItem.dispose();
								if (!msgSetList.isEmpty()) {
									// 删除Message Set节点下的Message Definition节点
									TreeItem msgSetTreeRoot = tabTree.getItem(1);
									for (TreeItem msgSetTreeItem: msgSetTreeRoot.getItems()) {
										EcoreTreeNode msgSetEcoreTreeNode = (EcoreTreeNode)msgSetTreeItem.getData("EcoreTreeNode");
										for (EcoreMessageSet ecoreMsgSet: msgSetList) {
											if (ecoreMsgSet.getId().equals(msgSetEcoreTreeNode.getId())) {
												for (TreeItem msgDefinitionTreeItem: msgSetTreeItem.getItems()) {
													EcoreTreeNode msgDefinitionEcoreTreeNode = (EcoreTreeNode)msgDefinitionTreeItem.getData("EcoreTreeNode");
													if (msgDefinitionEcoreTreeNode.getId().equals(ecoreTreeNode.getId())) {
														msgDefinitionTreeItem.dispose();
														break;
													}
												}
												break;
											}
										}
									}

								}
								if (ecoreBusinessArea.getId() != null) {
									// 删除Business Area节点下的Message Definition节点
									TreeItem bizAreaTreeRoot = tabTree.getItem(0);
									for (TreeItem bizAreaTreeItem: bizAreaTreeRoot.getItems()) {
										EcoreTreeNode bizAreaEcoreTreeNode = (EcoreTreeNode)bizAreaTreeItem.getData("EcoreTreeNode");
										if (bizAreaEcoreTreeNode.getId().equals(ecoreBusinessArea.getId())) {
											for (TreeItem msgDefinitionTreeItem: bizAreaTreeItem.getItems()) {
												EcoreTreeNode msgDefinitionEcoreTreeNode = (EcoreTreeNode)msgDefinitionTreeItem.getData("EcoreTreeNode");
												if (msgDefinitionEcoreTreeNode.getId().equals(ecoreTreeNode.getId())) {
													msgDefinitionTreeItem.dispose();
													break;
												}
											}
											break;
										}
									}
								}
								
							
								
							}
						} catch (Exception e1) {
							e1.printStackTrace();
							ExceptionHandleUtil.handle(e1);
							//删除失败
							MessageBoxUtil.deleteFail();
						}
					}
					// 关闭被删除节点, 对应打开的Editor
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					for (IEditorReference editorReference: page.getEditorReferences()) {
						String id = editorReference.getPartProperty("ID");
						if (ecoreTreeNode.getId().equals(id)) {
							page.closeEditor(editorReference.getEditor(false), false);
							break;
						}
					}	
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		/**
		 * 右键点击事件, 显示Context菜单, 并控制菜单项可用或不可用
		 */
		tabTree.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (e.button == 3) {
					// 移除选中的TreeItem
					TreeListItem selectedTreeListItem = (TreeListItem)tabTree.getSelection()[0];
					EcoreTreeNode ecoreTreeNode = (EcoreTreeNode) selectedTreeListItem.getData("EcoreTreeNode");
					mi.setEnabled(false);
					if(ecoreTreeNode != null) {
						if(TreeLevelEnum.LEVEL_2.getLevel().equals(ecoreTreeNode.getLevel())) {
							if( (selectedTreeListItem.getParentItem().getText().equals(TreeParentEnum.BUSINESS_AREAS.getName()) && RegistrationStatus.Added.getStatus().equals(ecoreTreeNode.getRegistrationStatus()) || ecoreTreeNode.getRegistrationStatus() == null) || 
									(selectedTreeListItem.getParentItem().getText().equals(TreeParentEnum.MESSAGE_SETS.getName()) && RegistrationStatus.Added.getStatus().equals(ecoreTreeNode.getRegistrationStatus()) || ecoreTreeNode.getRegistrationStatus() == null) || 
									(selectedTreeListItem.getParentItem().getText().equals(TreeParentEnum.MESSAGE_DEFINITIONS.getName()) && RegistrationStatus.Added.getStatus().equals(ecoreTreeNode.getRegistrationStatus()) || ecoreTreeNode.getRegistrationStatus() == null) ) {
								mi.setEnabled(true);
							}
						}
					}
				}
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
	}
	
	/**
	 * 浏览文件
	 */
	public static String openFile(String defaultFileName, String extentionFileName) {
		
		Display display = Display.getCurrent();
		Shell shell = new Shell(display.getActiveShell());
		try {
			Rectangle bounds = Display.getCurrent().getClientArea(); // 获取屏幕尺寸
			shell.setLocation(bounds.width / 2 - 350, bounds.height / 2);
			shell.setSize(400, 100);
			
			FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
			if ("xsd".equalsIgnoreCase(extentionFileName)) {
				fileDialog.setFilterExtensions(new String[] {  "*.xsd" });
			} else if ("xml".equalsIgnoreCase(extentionFileName)) {
				fileDialog.setFilterExtensions(new String[] {  "*.xml" });
			}
			
			String filePath = fileDialog.open(); // 获取文件全路径

			//设置默认文件名称---
			System.out.println("defaultFileName: " + defaultFileName);
			fileDialog.setText(defaultFileName);
			
			if ("" == filePath || null == filePath) {
				shell.dispose();
				return null;
			}else {
				return filePath;
			}
			
		} finally {
			if (shell.isDisposed()) {
				shell.dispose();
			}
		}
	}
	
	/**
	 * 浏览文件
	 */
	public static String openExcelFile(String defaultFileName) {
		
		Display display = Display.getCurrent();
		Shell shell = new Shell(display.getActiveShell());
		try {
			Rectangle bounds = Display.getCurrent().getClientArea(); // 获取屏幕尺寸
			shell.setLocation(bounds.width / 2 - 350, bounds.height / 2);
			shell.setSize(400, 100);
			
			FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
			fileDialog.setFilterExtensions(new String[] {  "*.xls" });
			String filePath = fileDialog.open(); // 获取文件全路径

			//设置默认文件名称---
			System.out.println("defaultFileName: " + defaultFileName);
			fileDialog.setText(defaultFileName);
			
			if ("" == filePath || null == filePath) {
				shell.dispose();
				return null;
			}else {
				return filePath;
			}
			
		} finally {
			if (shell.isDisposed()) {
				shell.dispose();
			}
		}
	}
	
	/**
	 * MDR3
	 */
	public static String openMDR3File(String defaultFileName) {
		
		Display display = Display.getCurrent();
		Shell shell = new Shell(display.getActiveShell());
		try {
			Rectangle bounds = Display.getCurrent().getClientArea(); // 获取屏幕尺寸
			shell.setLocation(bounds.width / 2 - 350, bounds.height / 2);
			shell.setSize(400, 100);
			
			FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
			fileDialog.setFilterExtensions(new String[] {  "*.xls" });
			String filePath = fileDialog.open(); // 获取文件全路径

			//设置默认文件名称---
			System.out.println("defaultFileName: " + defaultFileName);
			fileDialog.setText(defaultFileName);
			
			if ("" == filePath || null == filePath) {
				shell.dispose();
				return null;
			}else {
				return filePath;
			}
			
		} finally {
			if (shell.isDisposed()) {
				shell.dispose();
			}
		}
	}
}
