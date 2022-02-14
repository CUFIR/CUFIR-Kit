package com.cfets.cufir.plugin.mr.editor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import com.cfets.cufir.plugin.mr.bean.TransferDataBean;
import com.cfets.cufir.plugin.mr.bean.TreeListItem;
import com.cfets.cufir.plugin.mr.enums.ObjTypeEnum;
import com.cfets.cufir.plugin.mr.enums.RegistrationStatus;
import com.cfets.cufir.plugin.mr.enums.TreeLevelEnum;
import com.cfets.cufir.plugin.mr.handlers.SaveHandler;
import com.cfets.cufir.plugin.mr.utils.ContextServiceUtil;
import com.cfets.cufir.plugin.mr.utils.DerbyDaoUtil;
import com.cfets.cufir.plugin.mr.utils.EditorUtil;
import com.cfets.cufir.plugin.mr.utils.GenerateCommonTree;
import com.cfets.cufir.plugin.mr.utils.GenerateTableForMCandMDandMS;
import com.cfets.cufir.plugin.mr.utils.ImgUtil;
import com.cfets.cufir.plugin.mr.utils.MessageBoxUtil;
import com.cfets.cufir.plugin.mr.utils.NumberFormatUtil;
import com.cfets.cufir.plugin.mr.utils.SystemUtil;
import com.cfets.cufir.plugin.mr.view.TreeListView;
import com.cfets.cufir.s.data.bean.EcoreBusinessComponent;
import com.cfets.cufir.s.data.bean.EcoreBusinessComponentRL;
import com.cfets.cufir.s.data.bean.EcoreBusinessElement;
import com.cfets.cufir.s.data.bean.EcoreConstraint;
import com.cfets.cufir.s.data.bean.EcoreDataType;
import com.cfets.cufir.s.data.bean.EcoreExample;
import com.cfets.cufir.s.data.bean.EcoreMessageComponent;
import com.cfets.cufir.s.data.bean.EcoreMessageDefinition;
import com.cfets.cufir.s.data.bean.EcoreMessageSet;
import com.cfets.cufir.s.data.dao.EcoreBusinessComponentDao;
import com.cfets.cufir.s.data.dao.impl.EcoreBusinessComponentDaoImpl;
import com.cfets.cufir.s.data.vo.EcoreBusinessComponentVO;
import com.cfets.cufir.s.data.vo.EcoreBusinessElementVO;
import com.cfets.cufir.s.data.vo.EcoreTreeNode;
import com.cfets.cufir.s.data.vo.SynonymVO;

/**
 * Business Component 编辑和显示
 * @author jiangqiming_het,gongyi_tt
 *
 */
public class BusinessComponentCreateEditor extends MultiPageEditorParent {

	//查看的数据
	private EcoreBusinessComponentDao businessComponentDao = new EcoreBusinessComponentDaoImpl();
	private EcoreBusinessComponentVO businessComponentVO;
	private String businessComponentId;
	private Tree elementsTree;
	// -----------For Summary Page -----------
	private Text summaryNameText, summaryDocText, summaryPreviosDocText, summaryobjectIdentifierText,
			summaryRemovalDate;
	private Combo summaryStatusCombo;
	private List exampleList;
	private Table summaryConstraintsTable;
	private Tree inheritanceTree;

	// ----------- For Content Page -------------
	private Text beNameText, beDocText, beMinOccursText, beMaxOccursText, beTypeText, beObjectIdentifierText;
	private Combo beStatusCombo;
	private Button isDerivedCheckButton,addExampleBtn,deleteExampleBtn,addConstraintBtn,editConstraintBtn,deleteConstraintBtn,
	addBusinessElementBtn,deleteBusinessElementBtn,addSynonymsBtn,deleteButtonForSynonyms,addConstraintBtn1,editConstraintBtn1,
	deleteConstraintBtn1,btBtn;
	private Table beConstraintsTable;
	private Table beSynonymsTable;
	private TreeItem selectedTableItem;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	// ------------For All--------------
	// 基本数据类型
	private String dataType;
	private TreeItem modelExploreTreeItem;
	private MyEditorInput myEditorInput;
	
	// Use for Impact Page 
	private ArrayList<EcoreMessageComponent> messageComponentList = new ArrayList<>();
	// Use for Impact Page
	private ArrayList<EcoreMessageDefinition> msgDefinitionList = new ArrayList<>();
	// Use for Impact Page
	private ArrayList<EcoreMessageSet> msgSetList = new ArrayList<>();
	// Use for Incoming Association Page
	private ArrayList<EcoreBusinessComponent> imcomingAssociationList = new ArrayList<>();
	// Use for Version/Subsets Page
	private ArrayList<EcoreBusinessComponent> bizComponentNextVersionList;
	// children business components on Summary page
	private EcoreTreeNode inheritanceRootEcoreTreeNode = null;

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		this.setSite(site);
		this.setInput(input);
		this.setPartProperty("customName", "bizComponentCreate");
		this.myEditorInput = (MyEditorInput) input;
		this.dataType = this.myEditorInput.getTransferDataBean().getType();
		modelExploreTreeItem = this.myEditorInput.getTransferDataBean().getTreeListItem();
		//ctrl + s保存必须要
		ContextServiceUtil.setContext(site);
		this.businessComponentId = this.myEditorInput.getTransferDataBean().getId();
		this.setPartProperty("ID", businessComponentId);
		
		initFilledData();
		

	}
	
	/**
	 * 初始化编辑的值
	 */
	private void initFilledData() {
		// 不为null代表是打开的,可编辑
		try {
			businessComponentVO = businessComponentDao.findBusinessComponentVO(businessComponentId);
			if (businessComponentVO != null) {
				messageComponentList = DerbyDaoUtil.getMessageComponentList(businessComponentId);
				msgDefinitionList = DerbyDaoUtil.getMessageDefinitionList(messageComponentList);
				msgSetList = DerbyDaoUtil.getMessageSetList(msgDefinitionList);
				imcomingAssociationList = DerbyDaoUtil.getIncomingAssociationsList(businessComponentId);
				bizComponentNextVersionList = DerbyDaoUtil.getBusinessComponentListByIds(DerbyDaoUtil.getNextVersionIds(businessComponentId));
				inheritanceRootEcoreTreeNode = businessComponentDao.findBusinessComponentInheritances(businessComponentId);	//DerbyDaoUtil.getSubBusinessComponentById(businessComponentId);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		businessComponentVO = new EcoreBusinessComponentVO();
	}

	@Override
	protected void createPages() {
		createSummaryPage();
		createContentPage();
		createImpactPage();
		createIncomingAssociationsPage();
		createVersionSubset();
		changeStatus();
		
		// 设置标题 myEditorInput.getName()
		// 不为null代表是打开的,可编辑
		if (businessComponentVO.getEcoreBusinessComponent() != null) {
			this.setPartName(businessComponentVO.getEcoreBusinessComponent().getName());
			if (RegistrationStatus.Registered.getStatus().equals(businessComponentVO.getEcoreBusinessComponent().getRegistrationStatus())) {
				this.setDirty(false);
			} else {
				this.setDirty(true);
			}
		} else {
			this.setPartName("");
			this.setDirty(true);
		}
	}


	@Override
	public void doSave(IProgressMonitor monitor) {
		SaveHandler sh=new SaveHandler();
		try {
			sh.execute(null);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	/**
	 * Summary Page 显示和编辑
	 */
	void createSummaryPage() {
		Composite parentComposite = getContainer();
		ScrolledComposite scrolledComposite = new ScrolledComposite(parentComposite, SWT.V_SCROLL);
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(composite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		composite.setLayout(new FillLayout());
		int index = addPage(scrolledComposite);
		setPageText(index, "Summary");

		// ================= General Information Group Begin =======================

		ExpandBar summaryBar = new ExpandBar(composite, SWT.NONE);
		summaryBar.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		summaryBar.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		summaryBar.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));

		Composite generalInformationComposite = new Composite(summaryBar, SWT.NONE);
		generalInformationComposite.setLayout(new GridLayout(2, false));

		Label nameLabel = new Label(generalInformationComposite, SWT.NONE);
		nameLabel.setText("Name");
		GridData nameLabelGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		nameLabelGridData.widthHint = 200;
		nameLabel.setLayoutData(nameLabelGridData);

		summaryNameText = new Text(generalInformationComposite, SWT.BORDER);
		GridData nameValueGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		nameValueGridData.widthHint = 500;
		summaryNameText.setLayoutData(nameValueGridData);
		summaryNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setPartName(summaryNameText.getText());
				// 左边的树的名称展示
				modelExploreTreeItem.setText(summaryNameText.getText()==null?"":summaryNameText.getText());
				setPartName(summaryNameText.getText());
				setDirty(true);
//				Map<String, String> map;
//				try {
//					map = ecoreMessageComponentDao.findAllMessageComponentName();
//					if(map.containsValue(summaryNameText.getText())) {
//						MessageDialog.openWarning(parentComposite.getShell(),"提示", "命名重复，请重新命名或点击右方按钮命名");
//					}
//				} catch (Exception e1) {
//					e1.printStackTrace();
//				}
			}
		});
		if(businessComponentVO.getEcoreBusinessComponent() != null) {
			summaryNameText.setText("" + businessComponentVO.getEcoreBusinessComponent().getName());
		}

		Label documentationLabel = new Label(generalInformationComposite, SWT.NONE);
		documentationLabel.setLayoutData(nameLabelGridData);
		documentationLabel.setText("Documentation");
		summaryDocText = new Text(generalInformationComposite, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		GridData docValueGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		docValueGridData.widthHint = 500;
		docValueGridData.heightHint = 150;
		summaryDocText.setLayoutData(docValueGridData);
		summaryDocText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});
		if (businessComponentVO.getEcoreBusinessComponent() != null) {
			summaryDocText.setText(businessComponentVO.getEcoreBusinessComponent().getDefinition() != null
					? businessComponentVO.getEcoreBusinessComponent().getDefinition()
					: "");
		}
//		if(businessComponentVO.getEcoreBusinessComponent() != null) {
//			summaryDocText.setText("" + businessComponentVO.getEcoreBusinessComponent().getDefinition());
//		}

		Label previousDocLabel = new Label(generalInformationComposite, SWT.NONE);
		previousDocLabel.setLayoutData(nameLabelGridData);
		previousDocLabel.setText("Previous Version's Documentation");
		summaryPreviosDocText = new Text(generalInformationComposite, SWT.BORDER | SWT.V_SCROLL);
		summaryPreviosDocText.setLayoutData(docValueGridData);
		summaryPreviosDocText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});
		if (businessComponentVO.getEcoreBusinessComponent() != null) {
			summaryPreviosDocText.setText(businessComponentVO.getEcoreBusinessComponent().getPreviousVersion() != null
					? businessComponentVO.getEcoreBusinessComponent().getPreviousVersion()
					: "");
		}
//		if(businessComponentVO.getEcoreBusinessComponent() != null) {
//			summaryPreviosDocText.setText("" + businessComponentVO.getEcoreBusinessComponent().getPreviousVersion());
//		}
		
		ExpandItem generalInformationItem = new ExpandItem(summaryBar, SWT.NONE);
		generalInformationItem.setText("General Information");
		generalInformationItem.setHeight(380);
		generalInformationItem.setExpanded(true);
		generalInformationItem.setControl(generalInformationComposite);
		// ================= General Information Group End ============================

		// ================= CMP Information Group Begin ==============================

		// ================= CMP Information Group Begin ==============================
		Composite cmpComposite = new Composite(summaryBar, SWT.NONE);
		cmpComposite.setLayout(new GridLayout(2, false));

		Label objectIdentifierLabel = new Label(cmpComposite, SWT.NONE);
		objectIdentifierLabel.setLayoutData(nameLabelGridData);
		objectIdentifierLabel.setText("Object Identifier");
		summaryobjectIdentifierText = new Text(cmpComposite, SWT.BORDER);
		summaryobjectIdentifierText.setLayoutData(nameValueGridData);
		summaryobjectIdentifierText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});
		if (businessComponentVO.getEcoreBusinessComponent() != null) {
			summaryobjectIdentifierText.setText(businessComponentVO.getEcoreBusinessComponent().getObjectIdentifier() != null
					? businessComponentVO.getEcoreBusinessComponent().getObjectIdentifier()
					: "");
		}
		
//		if(businessComponentVO.getEcoreBusinessComponent() != null) {
//			summaryobjectIdentifierText.setText("" + businessComponentVO.getEcoreBusinessComponent().getObjectIdentifier());
//		}

		ExpandItem cmpItem = new ExpandItem(summaryBar, SWT.NONE);
		cmpItem.setText("CMP Information");
		cmpItem.setHeight(80);
		cmpItem.setExpanded(true);
		cmpItem.setControl(cmpComposite);
		// ================= CMP Information Group End ==============================

		// ===== Registration Information Group Begin ===============================
		Composite registrationComposite = new Composite(summaryBar, SWT.NONE);
		registrationComposite.setLayout(new GridLayout(2, false));

		Label registrationStatusLabel = new Label(registrationComposite, SWT.NONE);
		registrationStatusLabel.setLayoutData(nameLabelGridData);
		registrationStatusLabel.setText("Registration Status");

		GridData valueGridData = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
		valueGridData.widthHint = 150;
		ComboViewer summaryStatusComboViewer = new ComboViewer(registrationComposite, SWT.NONE);
		summaryStatusComboViewer.add(RegistrationStatus.Registered.getStatus());
		summaryStatusComboViewer.add(RegistrationStatus.Provisionally.getStatus());
		summaryStatusComboViewer.add(RegistrationStatus.Added.getStatus());
		summaryStatusCombo = summaryStatusComboViewer.getCombo();
		summaryStatusCombo.setLayoutData(valueGridData);
		summaryStatusCombo.setEnabled(false);
		summaryStatusCombo.select(2);
		summaryStatusCombo.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});
		if(businessComponentVO.getEcoreBusinessComponent() != null) {
			summaryStatusCombo.setText("" + businessComponentVO.getEcoreBusinessComponent().getRegistrationStatus());
		}

		Label removalDateLabel = new Label(registrationComposite, SWT.NONE);
		removalDateLabel.setText("Removal Date");
		summaryRemovalDate = new Text(registrationComposite, SWT.BORDER);
		summaryRemovalDate.setToolTipText("Date Format: MM/DD/YY or YYYY-MM-DD");
		summaryRemovalDate.setLayoutData(valueGridData);
		summaryRemovalDate.setEnabled(false);
		summaryRemovalDate.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});
		if(businessComponentVO.getEcoreBusinessComponent() != null
				&& businessComponentVO.getEcoreBusinessComponent().getRemovalDate() != null) {
			summaryRemovalDate.setText(sdf.format(businessComponentVO.getEcoreBusinessComponent().getRemovalDate()));
		}

		ExpandItem registrationItem = new ExpandItem(summaryBar, SWT.NONE);
		registrationItem.setText("Registration Information");
		registrationItem.setHeight(80);
		registrationItem.setExpanded(true);
		registrationItem.setControl(registrationComposite);
		// ===== Registration Information Group End ===============================

		// ==== Inheritance Group Begin ===========================================
		Composite inheritanceComposite = new Composite(summaryBar, SWT.NONE);
		inheritanceComposite.setLayout(new GridLayout(1, false));
		inheritanceTree = new Tree(inheritanceComposite, SWT.BORDER);
		GridData treeGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		treeGridData.widthHint = 450;
		treeGridData.heightHint = 260;
		inheritanceTree.setLayoutData(treeGridData);
		if(businessComponentVO.getEcoreBusinessComponent() != null) {
			this.generateInheritanceOfSummaryPage(inheritanceTree);
		}

		ExpandItem inheritanceItem = new ExpandItem(summaryBar, SWT.NONE);
		inheritanceItem.setText("Inheritance");
		inheritanceItem.setHeight(300);
		inheritanceItem.setExpanded(true);
		inheritanceItem.setControl(inheritanceComposite);
		// ==== Inheritance Group End ===========================================

		// =================== Exampel Group Begin ==============================
		Composite exampleComposite = new Composite(summaryBar, SWT.NONE);
		exampleComposite.setLayout(new GridLayout(1, false));

		Label exampleDescLabel = new Label(exampleComposite, SWT.WRAP);
		exampleDescLabel.setText("In this section, you can define examples.");
		GridData exampleLabelGridData = new GridData();
		exampleDescLabel.setLayoutData(exampleLabelGridData);

		Composite exampleToolbarComposite = new Composite(exampleComposite, SWT.NONE);
		exampleToolbarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

		addExampleBtn = new Button(exampleToolbarComposite, SWT.NONE);
		addExampleBtn.setImage(ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR));
		addExampleBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MessageBoxUtil.createExampleDialogue(exampleList);
				setDirty(true);
			}
		});

		deleteExampleBtn = new Button(exampleToolbarComposite, SWT.NONE);
		deleteExampleBtn.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
		deleteExampleBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MessageBoxUtil.deleteExampleDialogue(exampleList);
				setDirty(true);
			}
		});

		exampleList = new List(exampleComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		GridData exampleListGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		exampleListGridData.widthHint = 500;
		exampleListGridData.heightHint = 100;
		exampleList.setLayoutData(exampleListGridData);
		if(businessComponentVO.getEcoreExamples() != null && businessComponentVO.getEcoreExamples().size() > 0) {
			exampleList.removeAll();
			Map<String,String>map=new HashMap<String, String>();
			java.util.List<EcoreExample> ecoreExampleList = businessComponentVO.getEcoreExamples();
			for (EcoreExample ecoreExample : ecoreExampleList) {
				map.put(ecoreExample.getExample(),ecoreExample.getId());
				exampleList.add(ecoreExample.getExample());
			}
			exampleList.setData("map", map);
		}

		ExpandItem exampleItem = new ExpandItem(summaryBar, SWT.NONE);
		exampleItem.setText("Example");
		exampleItem.setHeight(210);
		exampleItem.setExpanded(true);
		exampleItem.setControl(exampleComposite);
		// =================== Exampel Group End ==============================

		// ===================== Contraints Group
		// Begin====================================
		Composite constraintComposite = new Composite(summaryBar, SWT.NONE);
		GridData constraintCompositeGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		constraintComposite.setLayout(new GridLayout(1, true));
		constraintComposite.setLayoutData(constraintCompositeGridData);

		Label contraintsDescLabel = new Label(constraintComposite, SWT.WRAP);
		contraintsDescLabel.setText(
				"All the constraints contained in this object (other constraints - such as constraints defined on type - may also apply).");
		GridData labelGridData = new GridData();
		contraintsDescLabel.setLayoutData(labelGridData);

		Composite toolbarComposite = new Composite(constraintComposite, SWT.NONE);
		toolbarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

		addConstraintBtn = new Button(toolbarComposite, SWT.NONE);
		addConstraintBtn.setImage(ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR));
		addConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MessageBoxUtil.createConstraintDialogue("add", "1", summaryConstraintsTable, null, dataType);
				setDirty(true);
			}
		});

		editConstraintBtn = new Button(toolbarComposite, SWT.NONE);
		editConstraintBtn.setImage(ImgUtil.createImg(ImgUtil.EDIT_IMG_TOOLBAR));
		editConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				// 不选中不触发任何效果
				if (summaryConstraintsTable.getSelectionCount() > 0) {
					MessageBoxUtil.createConstraintDialogue("edit", "1", summaryConstraintsTable, null, dataType);
					setDirty(true);
				}
			}
		});

		deleteConstraintBtn = new Button(toolbarComposite, SWT.NONE);
		deleteConstraintBtn.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
		deleteConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MessageBoxUtil.deleteConstraint("1", summaryConstraintsTable, null, dataType);
				setDirty(true);
			}
		});

		summaryConstraintsTable = new Table(constraintComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		summaryConstraintsTable.setHeaderVisible(true);
		summaryConstraintsTable.setLinesVisible(true);

		TableColumn nameColumn = new TableColumn(summaryConstraintsTable, SWT.NONE);
		nameColumn.setText("Name");
		nameColumn.setWidth(150);
		TableColumn DefinitionColumn = new TableColumn(summaryConstraintsTable, SWT.NONE);
		DefinitionColumn.setText("Definiton");
		DefinitionColumn.setWidth(150);
		TableColumn ExpressionLanguageColumn = new TableColumn(summaryConstraintsTable, SWT.NONE);
		ExpressionLanguageColumn.setText("Expression Language");
		ExpressionLanguageColumn.setWidth(150);
		TableColumn expressionColumn = new TableColumn(summaryConstraintsTable, SWT.NONE);
		expressionColumn.setText("Expression");
		expressionColumn.setWidth(150);

		GridData constraintsTableGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		constraintsTableGridData.widthHint = 620;
		constraintsTableGridData.heightHint = 100;
		summaryConstraintsTable.setLayoutData(constraintsTableGridData);
		// 处理Constraint
		if (businessComponentVO.getEcoreConstraints() != null) {
			java.util.List<EcoreConstraint> ecoreConstraintList = businessComponentVO.getEcoreConstraints();
			summaryConstraintsTable.removeAll();
			for (EcoreConstraint ecoreConstraint : ecoreConstraintList) {
				TableItem tableItem = new TableItem(summaryConstraintsTable, SWT.NONE);
				tableItem.setText(new String[] { ecoreConstraint.getName(), ecoreConstraint.getDefinition(),
						ecoreConstraint.getExpression(), ecoreConstraint.getExpressionlanguage() });
			}
		}

		ExpandItem contentItem = new ExpandItem(summaryBar, SWT.NONE);
		contentItem.setText("Constraints");
		contentItem.setHeight(200);
		contentItem.setExpanded(true);
		contentItem.setControl(constraintComposite);
		// ===================== Contraints Group
		// End======================================

		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	/**
	 * For Business Model生成Summary Page中的inheritance树
	 * 
	 * @param inheritanceTree
	 * @param bizComponent
	 * @param subBizComponentList
	 */
	private void generateInheritanceOfSummaryPage(Tree inheritanceTree) {
		inheritanceRootEcoreTreeNode.setLevel(TreeLevelEnum.LEVEL_1.getLevel());
		TreeItem treeRoot = new TreeItem(inheritanceTree, SWT.NONE);
		treeRoot.setText(inheritanceRootEcoreTreeNode.getName());
		treeRoot.setImage(ImgUtil.createImg(ImgUtil.BC));
		if (inheritanceRootEcoreTreeNode.getChildNodes()!=null) {
		this.populateInheritanceTree(treeRoot,inheritanceRootEcoreTreeNode.getChildNodes());
		}
		
		treeRoot.setExpanded(true);	
		
//		System.out.println("id:" + inheritanceRootEcoreTreeNode.getId() + "=============>" + treeRoot.getData("type"));
		
		inheritanceTree.addListener(SWT.MouseDoubleClick, new Listener() {

			@Override
			public void handleEvent(Event event) {
				
				CTabFolder cTabFolder =TreeListView.getTabFolder();
				Tree firstTabTree = TreeListView.getFirstTabTree();
				TreeItem bcTreeItem = firstTabTree.getItem(1);
				
				TreeItem treeItem = inheritanceTree.getSelection()[0];
				EcoreTreeNode ecoreTreeNodeOfInheritance = (EcoreTreeNode)treeItem.getData("EcoreTreeNode");
				for (TreeItem bcItem: bcTreeItem.getItems()) {
					EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)bcItem.getData("EcoreTreeNode");
					if (ecoreTreeNode.getId().equals(ecoreTreeNodeOfInheritance.getId())) {
						TransferDataBean transferDataBean = new TransferDataBean();
						transferDataBean.setId(ecoreTreeNode.getId());
						transferDataBean.setName(ecoreTreeNode.getName());
						transferDataBean.setLevel(ecoreTreeNode.getLevel());
						transferDataBean.setType((String)bcItem.getData("type"));
						transferDataBean.setChildId(bcItem.getData("childId") == null ? "" : (String)bcItem.getData("childId"));
						transferDataBean.setImgPath(ImgUtil.BC);
						transferDataBean.setTreeListItem((TreeListItem)bcItem);
						cTabFolder.setSelection(0);
						firstTabTree.setSelection(bcItem);
						EditorUtil.open(transferDataBean.getType(), transferDataBean);
						return;
					}
				
				}
				
			}
			
		});

	}
	
	private void populateInheritanceTree(TreeItem treeRoot, 
			java.util.List<EcoreTreeNode> childEcoreTreeNodeList) {
		
		if (childEcoreTreeNodeList==null) {
			return;
		} 
		
		for (EcoreTreeNode ecoreTreeNode: childEcoreTreeNodeList) {
			
 			TreeItem treeItem = new TreeItem(treeRoot, SWT.NONE);
			treeItem.setText(ecoreTreeNode.getName());
			treeItem.setImage(ImgUtil.createImg(ImgUtil.BC));
			treeItem.setData("EcoreTreeNode", ecoreTreeNode);
			String id=ecoreTreeNode.getId();
			if (ecoreTreeNode.getChildNodes()!=null) {
			this.populateInheritanceTree(treeItem, ecoreTreeNode.getChildNodes());		
			}
		}
	}
	
	/**
	 * Content Page 显示和编辑
	 */
	void createContentPage() {
		Composite parentComposite = getContainer();
		ScrolledComposite scrolledComposite = new ScrolledComposite(parentComposite, SWT.V_SCROLL | SWT.H_SCROLL);
		scrolledComposite.setBounds(parentComposite.getShell().getBounds());

		GridData sc_gd = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		scrolledComposite.setLayoutData(sc_gd);

		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(composite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		int index = addPage(scrolledComposite);
		setPageText(index, "Content");

		composite.setLayout(new GridLayout(2, false));
		GridData gd = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd.widthHint = scrolledComposite.getBounds().width;
		composite.setLayoutData(gd);

		Composite leftComposite = new Composite(composite, SWT.NONE);
		leftComposite.setLayout(new FillLayout(SWT.VERTICAL));
		GridData lc_gd = new GridData(SWT.TOP, SWT.TOP, false, false, 1, 1);
		lc_gd.widthHint = 400; // composite.getBounds().width / 3;
		leftComposite.setLayoutData(lc_gd);

		GridData rc_gd = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		rc_gd.widthHint = 500; // composite.getBounds().width / 3;
		Composite rightComposite = new Composite(composite, SWT.NONE);
		rightComposite.setLayout(new FillLayout(SWT.VERTICAL));
		rightComposite.setLayoutData(rc_gd);
		rightComposite.setEnabled(false);

		// ------------ Content Expand Bar Begin ----------------------------------
		ExpandBar contentBar = new ExpandBar(leftComposite, SWT.NONE);
		contentBar.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		contentBar.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		contentBar.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));

		Composite contentBarComposite = new Composite(contentBar, SWT.NONE);
		contentBarComposite.setBounds(contentBar.getBounds());
		contentBarComposite.setLayout(new GridLayout(1, false));
		contentBarComposite.setLayoutData(new GridData(350, 800));

		Composite beToolbarComposite = new Composite(contentBarComposite, SWT.NONE);
		beToolbarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

		addBusinessElementBtn = new Button(beToolbarComposite, SWT.NONE);
		addBusinessElementBtn.setImage(ImgUtil.createImg(ImgUtil.MC_ADD_ELEMENT));
		addBusinessElementBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
//				TableItem tableItem = new TableItem(contentBusinessElementTable, SWT.NONE);
//				tableItem.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
//				tableItem.setText("");
				setDirty(true);
//				MessageBoxUtil.createBusinessComponetElementSelectionDialogue(elementsTree);
				addBusinessComponetElementSelectionDialogue(rightComposite, scrolledComposite,elementsTree);
	
			}

			private void addBusinessComponetElementSelectionDialogue(Composite rightComposite,
					ScrolledComposite scrolledComposite, Tree elementsTree) {
//				Shell businessElementWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
//				businessElementWindow.setText("Business Element General Information");
//				businessElementWindow.setLayout(new FormLayout());
//				rightComposite.dispose();
				
				for (Control control: rightComposite.getChildren()) {
					control.dispose();
				}
				// ------------ Business Element Details Begin ------------------------
				ExpandBar messageElementBar = new ExpandBar(rightComposite, SWT.V_SCROLL);
				messageElementBar.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
				messageElementBar.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
				messageElementBar.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
					
				Composite c = new Composite(messageElementBar, SWT.NONE);
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
				typeButton.setText("choose");
				typeButton.setBounds(355, 215, 50, 30);
				typeButton.setBackground(new Color(Display.getCurrent(),135,206,250));
				typeButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseUp(MouseEvent e) {
						try {
							MessageBoxUtil.createSelectBusinessElementType(typeText);
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
//						treeItem.setData("type", 1);
						String nodeName;
						nodeName = nameText.getText() + " [" + minText.getText() + ", " + maxOccursText.getText() + "]";
						treeItem.setText(nodeName);
						treeItem.setData("id",treeItem.getData("id")==null?UUID.randomUUID():treeItem.getData("id").toString());
//						treeItem.setData("id", UUID.randomUUID().toString());
						treeItem.setData("beNameText", nameText.getText());
						treeItem.setData("beDocText", docText.getText());
						treeItem.setData("beMinOccursText", minText.getText());
						treeItem.setData("beMaxOccursText", maxOccursText.getText());
						treeItem.setData("isDerivedCheckButton", isTechnicalButton.getSelection());
						treeItem.setData("beType_id", String.valueOf(typeText.getData("beType_id")));
						treeItem.setData("beType", String.valueOf(typeText.getData("beType")));
//						treeItem.setData("beStatusCombo", String.valueOf(typeText.getData("status")));
						treeItem.setData("beStatusCombo", String.valueOf(RegistrationStatus.Added.getStatus()));
						treeItem.setData("beTypeText", typeText.getText());
						
//						if ("1".equals(String.valueOf(typeText.getData("beType")))) {
//							treeItem.setImage(ImgUtil.createImg(ImgUtil.BC));	
//						} else if ("2".equals(String.valueOf(typeText.getData("beType")))) {
						treeItem.setImage(ImgUtil.createImg(ImgUtil.BC_BE));	
//						}
//						treeItem.setImage(ImgUtil.createImg(ImgUtil.ME));
						// 用Message Component里的元素，构造Message Component树
						GenerateCommonTree.generateContainmentTreeForBComponent(treeItem,
								DerbyDaoUtil.getBizElementsByBizComponentId(String.valueOf(typeText.getData("beType_id"))),true);
						for (Control control: rightComposite.getChildren()) {
							control.dispose();
						}
					}
				});
				Button cancelButton = new Button(c, SWT.PUSH);
				cancelButton.setText("Cancel");
				cancelButton.setBounds(350, 265, 60, 30);
				cancelButton.setBackground(new Color(Display.getCurrent(),135,206,250));
				cancelButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseUp(MouseEvent e) {
						for (Control control: rightComposite.getChildren()) {
							control.dispose();
						}
					}
				});

				ExpandItem messageElementExpandItem = new ExpandItem(messageElementBar, SWT.NONE);
				messageElementExpandItem.setText("Business Element General Information");
				messageElementExpandItem.setHeight(340);
				messageElementExpandItem.setExpanded(true);
				messageElementExpandItem.setControl(c);
				
				rightComposite.requestLayout();
				
				scrolledComposite.setMinSize(rightComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				rightComposite.setEnabled(true);
			}
		});

		deleteBusinessElementBtn = new Button(beToolbarComposite, SWT.NONE);
		deleteBusinessElementBtn.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
		deleteBusinessElementBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TreeItem[] selection = elementsTree.getSelection();
				if (selection != null && selection.length > 0) {
					TreeItem item = selection[0];
					item.dispose();
					setDirty(true);
				}
			}
		});

//		contentBusinessElementTable = new Table(contentBarComposite, SWT.BORDER | SWT.SINGLE);
//		contentBusinessElementTable.setLayoutData(new GridData(330, 700));
//		TableColumn columnElement = new TableColumn(contentBusinessElementTable, SWT.NONE);
//		columnElement.setText("Elements");
//		columnElement.setWidth(300);
//		TableColumn columnDefinedIn = new TableColumn(contentBusinessElementTable, SWT.NONE);
//		columnDefinedIn.setText("Defined in");
//		columnDefinedIn.setWidth(120);
		elementsTree = new Tree(contentBarComposite, SWT.BORDER | SWT.SINGLE);
		elementsTree.setLayoutData(new GridData(330, 700));
		
		if(businessComponentVO.getEcoreBusinessElementVOs() != null && businessComponentVO.getEcoreBusinessElementVOs().size() > 0) {
			for (EcoreBusinessElementVO businessElementVO : businessComponentVO.getEcoreBusinessElementVOs()) {
				EcoreBusinessElement ecoreBusinessElement = businessElementVO.getEcoreBusinessElement();
				TreeItem tableItem = new TreeItem(elementsTree, SWT.NONE);
				tableItem.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
				tableItem.setText(ecoreBusinessElement.getName());
				tableItem.setData("id", ecoreBusinessElement.getId());
				tableItem.setData("beNameText", ecoreBusinessElement.getName());
				tableItem.setData("beDocText", ecoreBusinessElement.getDefinition());
				tableItem.setData("beMinOccursText", ecoreBusinessElement.getMinOccurs());
				tableItem.setData("beMaxOccursText", ecoreBusinessElement.getMaxOccurs());
				tableItem.setData("isDerivedCheckButton", ecoreBusinessElement.getIsDerived());
				tableItem.setData("beTypeText", ecoreBusinessElement.getTypeName());
				tableItem.setData("beType_id", ecoreBusinessElement.getTypeId());
				tableItem.setData("beType", ecoreBusinessElement.getType());
				tableItem.setData("beObjectIdentifierText", ecoreBusinessElement.getObjectIdentifier());
				tableItem.setData("beStatusCombo", ecoreBusinessElement.getRegistrationStatus());
//				tableItem.setData("beRemovalDate", ecoreBusinessElement.getRemovalDate());
				
				java.util.List<SynonymVO> synonymVOs = businessElementVO.getSynonyms();
				java.util.List<String[]> synonymsTableItems = new ArrayList<String[]>();
				if (synonymVOs != null && synonymVOs.size() > 0) {
					for (SynonymVO synonymVO : synonymVOs) {
						String[] str = new String[2];
						str[0] = synonymVO.getContext();
						str[1] = synonymVO.getSynonym();
						synonymsTableItems.add(str);
					}
				}
				tableItem.setData("beSynonymsTableItems", synonymsTableItems);

				java.util.List<EcoreConstraint> ecoreConstraints = businessElementVO.getEcoreConstraints();
				java.util.List<String[]> constraintsTableItems = new ArrayList<String[]>();
				if (ecoreConstraints != null && ecoreConstraints.size() > 0) {
					for (EcoreConstraint constraint : ecoreConstraints) {
						String[] str = new String[4];
						str[0] = constraint.getName();
						str[1] = constraint.getDefinition();
						str[2] = constraint.getExpression();
						str[3] = constraint.getExpressionlanguage();
						constraintsTableItems.add(str);
					}
				}
				tableItem.setData("beConstraintsTableItems", constraintsTableItems);
				GenerateCommonTree.generateContainmentTreeForBComponent(tableItem,
						DerbyDaoUtil.getBizElementsByBizComponentId(ecoreBusinessElement.getTypeId()),true);
			}
		}


		ExpandItem contentItem = new ExpandItem(contentBar, SWT.NONE);
		contentItem.setText("Content");
		contentItem.setHeight(850);
		contentItem.setExpanded(true);
		contentItem.setControl(contentBarComposite);
		// ------------ Content Expand Bar End ----------------------------------
		
		elementsTree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 选中可操作
				rightComposite.setEnabled(true);
				TreeItem item = (TreeItem) e.item;
				selectedTableItem = item;
				showBusinessElementDetailBar(rightComposite, scrolledComposite);
				beNameText
						.setText(String.valueOf(item.getData("beNameText") == null ? "" : item.getData("beNameText")));
				beDocText.setText(String.valueOf(item.getData("beDocText") == null ? "" : item.getData("beDocText")));
				beMinOccursText.setText(
						String.valueOf(item.getData("beMinOccursText") == null ? "" : item.getData("beMinOccursText")));
				beMaxOccursText.setText(
						String.valueOf(item.getData("beMaxOccursText") == null ? "" : item.getData("beMaxOccursText")));
				isDerivedCheckButton.setSelection(item.getData("isDerivedCheckButton") == null ? false
						: Boolean.getBoolean("" + item.getData("isDerivedCheckButton")));
				beTypeText.setData("beType_id", item.getData("beType_id")==null ? "":item.getData("beType_id"));
				beTypeText.setData("beType", item.getData("beType")==null ? "":item.getData("beType"));
				beTypeText
				.setText(String.valueOf(item.getData("beTypeText") == null ? "" : item.getData("beTypeText")));
				beObjectIdentifierText.setText(String.valueOf(
						item.getData("beObjectIdentifierText") == null ? "" : item.getData("beObjectIdentifierText")));
//				if (beTypeText.getData("beStatusCombo")!=null) {
//					beStatusCombo.setText(
//							String.valueOf(beTypeText.getData("beStatusCombo") == null ? "" : beTypeText.getData("beStatusCombo")));
//				} 
//				else {
//					beStatusCombo.setText(
//						String.valueOf(item.getData("beStatusCombo") == null ? "" : item.getData("beStatusCombo")));
//					
//				}
//				beStatusCombo.setText(RegistrationStatus.Added.getStatus());
				
//				beRemovalDate.setText(
//						String.valueOf(item.getData("beRemovalDate") == null ? "" : item.getData("beRemovalDate")));

				beSynonymsTable.removeAll();
				java.util.List<String[]> synonymsTableItems = (java.util.List<String[]>) item
						.getData("beSynonymsTableItems");
				if (synonymsTableItems != null && synonymsTableItems.size() > 0) {
					for (int i = 0; i < synonymsTableItems.size(); i++) {
						TableItem tableItem = new TableItem(beSynonymsTable, SWT.NONE);
						tableItem.setText(synonymsTableItems.get(i));
					}
				}

				beConstraintsTable.removeAll();
				java.util.List<String[]> constraintsTableItems = (java.util.List<String[]>) item
						.getData("beConstraintsTableItems");
				if (constraintsTableItems != null && constraintsTableItems.size() > 0) {
					for (int i = 0; i < constraintsTableItems.size(); i++) {
						TableItem tableItem = new TableItem(beConstraintsTable, SWT.NONE);
						tableItem.setText(constraintsTableItems.get(i));
					}
				}
				if (businessComponentVO.getEcoreBusinessComponent()!=null) {
					String status=businessComponentVO.getEcoreBusinessComponent().getRegistrationStatus();
					if (status.equals("Registered")) {
					btBtn.setEnabled(false);
					}
					//临时逻辑，根据bc注册状态控制整个页面编辑状态(待优化至当可编辑时只允许第一层可编辑)
					if(status.equals("Added Registered")||status==null){
						btBtn.setEnabled(true);
						beNameText.setEditable(true);
						beDocText.setEditable(true);
						beMinOccursText.setEditable(true);
						beMaxOccursText.setEditable(true); 
						beObjectIdentifierText.setEditable(true);
						isDerivedCheckButton.setEnabled(true);
					}
				}else {
					btBtn.setEnabled(true);
					beNameText.setEditable(true);
					beDocText.setEditable(true);
					beMinOccursText.setEditable(true);
					beMaxOccursText.setEditable(true); 
					beObjectIdentifierText.setEditable(true);
					isDerivedCheckButton.setEnabled(true);
				}
				
				
//				//临时逻辑，根据bc注册状态控制整个页面编辑状态(待优化至当可编辑时只允许第一层可编辑)
//				if(status.equals("Added Registered")||status==null){
//					
//				}
			}
		});
		
//		// ------------ Business Element Details Begin ------------------------
//		for (Control control: rightComposite.getChildren()) {
//			control.dispose();
//		}
//		ExpandBar businessElementBar = new ExpandBar(rightComposite, SWT.V_SCROLL);
//		businessElementBar.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
//		businessElementBar.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
//		businessElementBar.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
//
//		Composite generalInformationComposite = new Composite(businessElementBar, SWT.NONE);
//		generalInformationComposite.setLayout(new GridLayout(2, false));
//
//		GridData generalInforGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
//		generalInforGridData.widthHint = 350;
//		Label beNameLabel = new Label(generalInformationComposite, SWT.NONE);
//		beNameLabel.setText("Name");
//		beNameText = new Text(generalInformationComposite, SWT.BORDER);
//		beNameText.setEditable(false);
//		beNameText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
//		beNameText.setLayoutData(generalInforGridData);
//		beNameText.addModifyListener(new ModifyListener() {
//			@Override
//			public void modifyText(ModifyEvent e) {
//				if (!selectedTableItem.isDisposed()) {
//					// 左边的树的名称展示
//					String showText = "";
//					if (selectedTableItem.getData("codeType")==null) {
//						showText = beNameText.getText() + " [" + beMinOccursText.getText() + ", "
//							+ beMaxOccursText.getText() + "]";
//						selectedTableItem.setText(showText);
//					}
//					
//					selectedTableItem.setData("beNameText", beNameText.getText());
//					setDirty(true);
//				}
//			}
//		});
//
//		GridData documentationGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
//		documentationGridData.widthHint = 350;
//		documentationGridData.heightHint = 150;
//		Label documentationLabel = new Label(generalInformationComposite, SWT.NONE);
//		documentationLabel.setText("Documentation");
//		beDocText = new Text(generalInformationComposite, SWT.BORDER | SWT.WRAP);
//		beDocText.setEditable(false);
//		beDocText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
//		beDocText.setLayoutData(documentationGridData);
//		beDocText.addModifyListener(new ModifyListener() {
//			@Override
//			public void modifyText(ModifyEvent e) {
//				if (!selectedTableItem.isDisposed()) {
//					selectedTableItem.setData("beDocText", beDocText.getText());
//					setDirty(true);
//				}
//			}
//		});
//
//		Label minOccursLabel = new Label(generalInformationComposite, SWT.NONE);
//		minOccursLabel.setText("Min Occurs");
//		beMinOccursText = new Text(generalInformationComposite, SWT.BORDER);
//		beMinOccursText.setEditable(false);
//		beMinOccursText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
//		beMinOccursText.addModifyListener(new ModifyListener() {
//			@Override
//			public void modifyText(ModifyEvent e) {
//				if (!selectedTableItem.isDisposed()) {
//					String showText = "";
//					if (selectedTableItem.getData("codeType")==null) {
//					showText = beNameText.getText() + " [" + beMinOccursText.getText() + ", "
//							+ beMaxOccursText.getText() + "]" ;
//					selectedTableItem.setText(showText);
//					}
//					selectedTableItem.setData("beMinOccursText", beMinOccursText.getText());
//					setDirty(true);
//				}
//			}
//		});
//
//		Label MaxOccursLabel = new Label(generalInformationComposite, SWT.NONE);
//		MaxOccursLabel.setText("Max Occurs");
//		beMaxOccursText = new Text(generalInformationComposite, SWT.BORDER);
//		beMaxOccursText.setEditable(false);
//		beMaxOccursText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
//		beMaxOccursText.addModifyListener(new ModifyListener() {
//			@Override
//			public void modifyText(ModifyEvent e) {
//				if (!selectedTableItem.isDisposed()) {
//					String showText = "";
//					if (selectedTableItem.getData("codeType")==null) {
//					showText = beNameText.getText() + " [" + beMinOccursText.getText() + ", "
//								+ beMaxOccursText.getText() + "]";
//					selectedTableItem.setText(showText);
//					}
//						
//					selectedTableItem.setData("beMaxOccursText", beMaxOccursText.getText());
//					setDirty(true);
//				}
//			}
//		});
//
//		Label isDerivedLabel = new Label(generalInformationComposite, SWT.NONE);
//		isDerivedLabel.setText("Is Derived");
//		isDerivedCheckButton = new Button(generalInformationComposite, SWT.CHECK);
//		isDerivedCheckButton.setEnabled(false);
//		isDerivedCheckButton.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseUp(MouseEvent e) {
//				if (!selectedTableItem.isDisposed()) {
//					selectedTableItem.setData("isDerivedCheckButton", isDerivedCheckButton.getSelection());
//					setDirty(true);
//				}
//			}
//		});
//
//		Label typeLabel = new Label(generalInformationComposite, SWT.NONE);
//		typeLabel.setText("Type");
//		Composite cmp = new Composite(generalInformationComposite, SWT.NONE);
//		cmp.setLayoutData(new GridData(350, 40));
//		cmp.setLayout(new GridLayout(3, false));
//
//		GridData generalTyperGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
//		generalTyperGridData.widthHint = 260;
//
//		beTypeText = new Text(cmp, SWT.BORDER);
//		beTypeText.setLayoutData(generalTyperGridData);
//		beTypeText.setEnabled(false);
//		beTypeText.addModifyListener(new ModifyListener() {
//			@Override
//			public void modifyText(ModifyEvent e) {
//				if (selectedTableItem != null && !selectedTableItem.isDisposed()) {
//					selectedTableItem.setData("beTypeText", beTypeText.getText());
//					String showText = "";
//					if (selectedTableItem.getData("codeType")==null) {
//					showText = beNameText.getText() + " [" + beMinOccursText.getText() + ", "
//							+ beMaxOccursText.getText() + "]";
//					selectedTableItem.setText(showText);	
//					}
//					if (beTypeText.getData("beType_id") != null) {
//						selectedTableItem.setData("beTypeTextId", beTypeText.getData("beType_id"));
//						selectedTableItem.setData("beType", beTypeText.getData("beType"));
//					}
//					setDirty(true);
//				}
//			}
//		});
//		btBtn = new Button(cmp, SWT.NONE);
//		btBtn.setText("...");
//		btBtn.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseUp(MouseEvent e) {
//				MessageBoxUtil.createSearchDialogue(beTypeText,selectedTableItem);
//				setDirty(true);
//			}
//		});
//		Button btSearchBtn = new Button(cmp, SWT.NONE);
//		btSearchBtn.setImage(ImgUtil.createImg(ImgUtil.SEARCH_BTN));
//		btSearchBtn.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseUp(MouseEvent e) {
//				if (elementsTree.getSelectionCount() == 1) {
//					TreeItem treeItem = elementsTree.getSelection()[0];
//					String dataType = (String)treeItem.getData("beType");
////					String beTypeTextType = (String)treeItem.getData("beTypeTextType");
//					CTabFolder cTabFolder =TreeListView.getTabFolder();
//					if ("2".equals(dataType)) {
//						String Id = beTypeText.getData("beType_id").toString();
////						String Id = (String)treeItem.getData("beTypeTextId");
//						Tree firstTabTree = TreeListView.getFirstTabTree();
//						TreeItem mcTreeItem = firstTabTree.getItem(1);
//						for (TreeItem mcItem: mcTreeItem.getItems()) {
//							EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)mcItem.getData("EcoreTreeNode");
//							if (beTypeText.getData("meLever")!=null) {
////								treeItem.setText(meNameText.getText() + " [" + meMinOccursText.getText() + ", "
////										+ meMaxOccursText.getText() + "] : " +meTypeText.getText());
//								String meComponentId = (String) beTypeText.getData("beType_id");
//							if (ecoreTreeNode.getId().equals(meComponentId)) {
//								TransferDataBean transferDataBean = new TransferDataBean();
//								transferDataBean.setId(ecoreTreeNode.getId());
//								transferDataBean.setName(ecoreTreeNode.getName());
//								transferDataBean.setLevel(ecoreTreeNode.getLevel());
//								transferDataBean.setType((String)mcItem.getData("type"));
//								transferDataBean.setChildId(mcItem.getData("childId") == null ? "" : (String)mcItem.getData("childId"));
////								String status=ecoreTreeNode.getRegistrationStatus();
//								transferDataBean.setImgPath(ImgUtil.BC);
//								transferDataBean.setTreeListItem((TreeListItem)mcItem);
//								firstTabTree.setSelection(mcItem);
////								cTabFolder.setSelection(1);
//								EditorUtil.open(transferDataBean.getType(), transferDataBean);
//								return;
//							}
//							}
//							
//							else if (ecoreTreeNode.getId().equals(Id)) {
//								TransferDataBean transferDataBean = new TransferDataBean();
//								transferDataBean.setId(ecoreTreeNode.getId());
//								transferDataBean.setName(ecoreTreeNode.getName());
//								transferDataBean.setLevel(ecoreTreeNode.getLevel());
//								transferDataBean.setType((String)mcItem.getData("type"));
//								transferDataBean.setChildId(mcItem.getData("childId") == null ? "" : (String)mcItem.getData("childId"));
//								transferDataBean.setImgPath(ImgUtil.BC);
//								firstTabTree.setSelection(mcItem);
////								cTabFolder.setSelection(1);
//								transferDataBean.setTreeListItem((TreeListItem)mcItem);
//								EditorUtil.open(transferDataBean.getType(), transferDataBean);
//								return;
//							}
//						}
//					}else {
//						String dataTypeId = (String)treeItem.getData("beTypeTextId");
//						Tree firstTabTree = TreeListView.getFirstTabTree();
//						TreeItem dataTypeTreeItem = firstTabTree.getItem(0);
//						TreeItem[] allTypeItemArray = dataTypeTreeItem.getItems();
//						for (TreeItem oneTypeTreeItem: allTypeItemArray) {
//							for (TreeItem typeTreeItem: oneTypeTreeItem.getItems()) {
//								EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)typeTreeItem.getData("EcoreTreeNode");
//								if (beTypeText.getData("meLever")!=null) {
////									treeItem.setText(meNameText.getText() + " [" + meMinOccursText.getText() + ", "
////											+ meMaxOccursText.getText() + "] : " +meTypeText.getText());
//									String meComponentId = (String) beTypeText.getData("beType_id");
//								if (ecoreTreeNode.getId().equals(meComponentId)) {
//									TransferDataBean transferDataBean = new TransferDataBean();
//									transferDataBean.setId(ecoreTreeNode.getId());
//									transferDataBean.setName(ecoreTreeNode.getName());
//									transferDataBean.setLevel(ecoreTreeNode.getLevel());
//									transferDataBean.setType((String)typeTreeItem.getData("type"));
//									transferDataBean.setChildId(typeTreeItem.getData("childId") == null ? "" : (String)typeTreeItem.getData("childId"));
//									String status=ecoreTreeNode.getRegistrationStatus();
//									if("1".equals(ecoreTreeNode.getObjType())){
//										transferDataBean.setImgPath(ImgUtil.CODE_SET_SUB1_2);
//									}else if("2".equals(ecoreTreeNode.getObjType())) {
//										transferDataBean.setImgPath(ImgUtil.CODE_SET_SUB1);
//									}
//									transferDataBean.setTreeListItem((TreeListItem)typeTreeItem);
//									cTabFolder.setSelection(0);
//									firstTabTree.setSelection(typeTreeItem);
//									EditorUtil.open(transferDataBean.getType(), transferDataBean);
//									return;
//								}
//								}
//								else if (ecoreTreeNode.getId().equals(dataTypeId)) {
//									TransferDataBean transferDataBean = new TransferDataBean();
//									transferDataBean.setId(ecoreTreeNode.getId());
//									transferDataBean.setName(ecoreTreeNode.getName());
//									transferDataBean.setLevel(ecoreTreeNode.getLevel());
//									transferDataBean.setType((String)typeTreeItem.getData("type"));
//									transferDataBean.setChildId(typeTreeItem.getData("childId") == null ? "" : (String)typeTreeItem.getData("childId"));
//									if("1".equals(ecoreTreeNode.getObjType())){
//										transferDataBean.setImgPath(ImgUtil.CODE_SET_SUB1_2);
//									}else if("2".equals(ecoreTreeNode.getObjType())) {
//										transferDataBean.setImgPath(ImgUtil.CODE_SET_SUB1);
//									}
//									transferDataBean.setTreeListItem((TreeListItem)typeTreeItem);
//									cTabFolder.setSelection(0);
//									firstTabTree.setSelection(typeTreeItem);
//									EditorUtil.open(transferDataBean.getType(), transferDataBean);
//									return;
//								}
//							}
//						}
//					}
//				}
//			}
//		});
//
//		ExpandItem generalInformationItem = new ExpandItem(businessElementBar, SWT.NONE);
//		generalInformationItem.setText("Business Element Details");
//		generalInformationItem.setHeight(340);
//		generalInformationItem.setExpanded(true);
//		generalInformationItem.setControl(generalInformationComposite);
//
//		// ------------ Business Element Details End ------------------------
//
//		// -----------------------CMP Information Group Begin -----------------------
//
//		Composite cmpInformationComposite = new Composite(businessElementBar, SWT.NONE);
//		cmpInformationComposite.setLayout(new GridLayout(2, false));
//		Label objectIdentifierLabel = new Label(cmpInformationComposite, SWT.NONE);
//		objectIdentifierLabel.setText("Object Identifier");
//		beObjectIdentifierText = new Text(cmpInformationComposite, SWT.BORDER);
//		beObjectIdentifierText.setEditable(false);
//		beObjectIdentifierText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
//		beObjectIdentifierText.addModifyListener(new ModifyListener() {
//			@Override
//			public void modifyText(ModifyEvent e) {
//				if (!selectedTableItem.isDisposed()) {
//					selectedTableItem.setData("beObjectIdentifierText", beObjectIdentifierText.getText());
//					rightComposite.setEnabled(true);
//					setDirty(true);
//				}
//			}
//		});
//		GridData objectIdentifierGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
//		objectIdentifierGridData.widthHint = 350;
//		beObjectIdentifierText.setLayoutData(objectIdentifierGridData);
//
//		ExpandItem cmpInformationItem = new ExpandItem(businessElementBar, SWT.NONE);
//		cmpInformationItem.setText("CMP Information");
//		cmpInformationItem.setHeight(90);
//		cmpInformationItem.setExpanded(true);
//		cmpInformationItem.setControl(cmpInformationComposite);
//
//		// -----------------------CMP Information Group End -----------------------
//
//		// ----------------------- Registration Information Group Begin -----------
//		Composite registrationInfoComposite = new Composite(businessElementBar, SWT.NONE);
//		registrationInfoComposite.setLayout(new GridLayout(2, false));
//		Label registrationStatusLabel = new Label(registrationInfoComposite, SWT.NONE);
//		registrationStatusLabel.setText("Registration Status");
//		ComboViewer statusComboViewer = new ComboViewer(registrationInfoComposite, SWT.NONE);
//		beStatusCombo = statusComboViewer.getCombo();
//		beStatusCombo.add("Registered");
//		beStatusCombo.add("Provisionally Registered");
//		beStatusCombo.setEnabled(false);
//		GridData valueGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
//		valueGridData.widthHint = 150;
//		beStatusCombo.setLayoutData(valueGridData);
//		beStatusCombo.addModifyListener(new ModifyListener() {
//			@Override
//			public void modifyText(ModifyEvent e) {
//				if (!selectedTableItem.isDisposed()) {
//					selectedTableItem.setData("beStatusCombo", beStatusCombo.getText());
//					setDirty(true);
//				}
//			}
//		});
//
//		Label removalDateLabel = new Label(registrationInfoComposite, SWT.NONE);
//		removalDateLabel.setText("Removal Date");
//		beRemovalDate = new Text(registrationInfoComposite, SWT.BORDER);
//		beRemovalDate.setToolTipText("Date Format: MM/DD/YY or YYYY-MM-DD");
//		beRemovalDate.setLayoutData(valueGridData);
//		beRemovalDate.setEnabled(false);
//		beRemovalDate.addModifyListener(new ModifyListener() {
//			@Override
//			public void modifyText(ModifyEvent e) {
//				if (!selectedTableItem.isDisposed()) {
//					selectedTableItem.setData("beRemovalDate", beRemovalDate.getText());
//					setDirty(true);
//				}
//			}
//		});
//
//		ExpandItem registrationInfoItem = new ExpandItem(businessElementBar, SWT.NONE);
//		registrationInfoItem.setText("Registration Information");
//		registrationInfoItem.setHeight(90);
//		registrationInfoItem.setExpanded(true);
//		registrationInfoItem.setControl(registrationInfoComposite);
//
//		// ----------------------- Registration Information Group End
//		// -----------------------
//
//		// ---------- Synonyms Expand Item Begin -------------------------------------
//		Composite synonymsComposite = new Composite(businessElementBar, SWT.NONE);
//		synonymsComposite.setLayout(new GridLayout(1, false));
//		Label descriptionLabel = new Label(synonymsComposite, SWT.NONE);
//		descriptionLabel.setText("This section show the synonyms of object");
//		GridData descriptionGridData = new GridData();
//		descriptionGridData.heightHint = 25;
//		descriptionLabel.setLayoutData(descriptionGridData);
//		GridData tableGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
//		tableGridData.widthHint = 350;
//		tableGridData.heightHint = 150;
//
//		Composite synonymsToolbarComposite = new Composite(synonymsComposite, SWT.NONE);
//		synonymsToolbarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
//
//		addSynonymsBtn = new Button(synonymsToolbarComposite, SWT.NONE);
//		addSynonymsBtn.setImage(ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR));
//		addSynonymsBtn.setEnabled(false);
//		addSynonymsBtn.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseUp(MouseEvent e) {
//				MessageBoxUtil.createSynonymsDialogue(beSynonymsTable, selectedTableItem);
//				setDirty(true);
//			}
//		});
//
//		deleteButtonForSynonyms = new Button(synonymsToolbarComposite, SWT.NONE);
//		deleteButtonForSynonyms.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
//		deleteButtonForSynonyms.setEnabled(false);
//		deleteButtonForSynonyms.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseUp(MouseEvent e) {
//				MessageBoxUtil.deleteSynonymDialogue(beSynonymsTable, selectedTableItem);
//				setDirty(true);
//			}
//		});
//
//		beSynonymsTable = new Table(synonymsComposite, SWT.BORDER);
//		beSynonymsTable.setLayoutData(tableGridData);
//		beSynonymsTable.setHeaderVisible(true);
//		beSynonymsTable.setLinesVisible(true);
//
//		TableColumn contextColumn = new TableColumn(beSynonymsTable, SWT.NONE);
//		contextColumn.setWidth(100);
//		contextColumn.setText("Context");
//		TableColumn SynonymsColumn = new TableColumn(beSynonymsTable, SWT.NONE);
//		SynonymsColumn.setWidth(100);
//		SynonymsColumn.setText("Synonyms");
//
//		ExpandItem synonymsItem = new ExpandItem(businessElementBar, SWT.NONE);
//		synonymsItem.setText("Synonyms");
//		synonymsItem.setHeight(250);
//		synonymsItem.setExpanded(true);
//		synonymsItem.setControl(synonymsComposite);
//		// ---------- Synonyms Expand Item End -------------------------------------
//
//		// ---------- Constraints Expand Item Begin
//		// -------------------------------------
//		Composite contraintsComposite = new Composite(businessElementBar, SWT.NONE);
//		contraintsComposite.setLayout(new GridLayout(1, false));
//		Label contraintsDescLabel = new Label(contraintsComposite, SWT.WRAP);
//		contraintsDescLabel.setText(
//				"All the constraints contained in this object(other constraints - such as constraints defined on type - may also apply).");
//		GridData labelGridData = new GridData();
//		labelGridData.widthHint = 430;
//		contraintsDescLabel.setLayoutData(labelGridData);
//
//		Composite toolbarComposite = new Composite(contraintsComposite, SWT.NONE);
//		toolbarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
//
//		addConstraintBtn1 = new Button(toolbarComposite, SWT.NONE);
//		addConstraintBtn1.setImage(ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR));
//		addConstraintBtn1.setEnabled(false);
//		addConstraintBtn1.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseUp(MouseEvent e) {
//				MessageBoxUtil.createConstraintDialogueForBC("add", beConstraintsTable, selectedTableItem);
//				setDirty(true);
//			}
//		});
//
//		editConstraintBtn1 = new Button(toolbarComposite, SWT.NONE);
//		editConstraintBtn1.setImage(ImgUtil.createImg(ImgUtil.EDIT_IMG_TOOLBAR));
//		editConstraintBtn1.setEnabled(false);
//		editConstraintBtn1.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseUp(MouseEvent e) {
//				if (beConstraintsTable.getSelectionCount() > 0) {
//					MessageBoxUtil.createConstraintDialogueForBC("edit", beConstraintsTable, selectedTableItem);
//					setDirty(true);
//				}
//			}
//		});
//
//		deleteConstraintBtn1 = new Button(toolbarComposite, SWT.NONE);
//		deleteConstraintBtn1.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
//		deleteConstraintBtn1.setEnabled(false);
//		deleteConstraintBtn1.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseUp(MouseEvent e) {
//				MessageBoxUtil.deleteConstraintForBC(beConstraintsTable, selectedTableItem);
//				setDirty(true);
//			}
//		});
//
//		beConstraintsTable = new Table(contraintsComposite, SWT.BORDER);
//		beConstraintsTable.setHeaderVisible(true);
//		beConstraintsTable.setLinesVisible(true);
//		TableColumn nameColumn = new TableColumn(beConstraintsTable, SWT.NONE);
//		nameColumn.setWidth(100);
//		nameColumn.setText("Name");
//		TableColumn defifinitionColumn = new TableColumn(beConstraintsTable, SWT.NONE);
//		defifinitionColumn.setWidth(100);
//		defifinitionColumn.setText("Definition");
//		TableColumn expressionLanguageColumn = new TableColumn(beConstraintsTable, SWT.NONE);
//		expressionLanguageColumn.setWidth(100);
//		expressionLanguageColumn.setText("Expression Language");
//		TableColumn expressionColumn = new TableColumn(beConstraintsTable, SWT.NONE);
//		expressionColumn.setWidth(200);
//		expressionColumn.setText("Expression");
//
//		GridData constraintsTableGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
//		constraintsTableGridData.widthHint = 430;
//		constraintsTableGridData.heightHint = 150;
//		beConstraintsTable.setLayoutData(constraintsTableGridData);
//
//		ExpandItem contraintsItem = new ExpandItem(businessElementBar, SWT.NONE);
//		contraintsItem.setText("Contraints");
//		contraintsItem.setHeight(250);
//		contraintsItem.setExpanded(true);
//		contraintsItem.setControl(contraintsComposite);
//		// ---------- Constraints Expand Item End -------------------------------------
//
//		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	/**
	 * Impact Page 显示和编辑
	 */
	void createImpactPage() {
		Composite parentComposite = getContainer();
		ScrolledComposite scrolledComposite = new ScrolledComposite(parentComposite, SWT.V_SCROLL);
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(composite);
		composite.setLayout(new GridLayout(2, false));
		int index = addPage(scrolledComposite);
		setPageText(index, "Impact");
		GridData gd = new GridData(SWT.TOP, SWT.TOP, true, false, 1, 1);
		composite.setLayoutData(gd);

		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		Label descriptionLeft = new Label(composite, SWT.WRAP);
		descriptionLeft.setText(
				"This section shows all the repository items that refer to this respository item. Select any of then to see how it is used.");
		GridData leftGridData = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		leftGridData.widthHint = 500;
		descriptionLeft.setLayoutData(leftGridData);
		
		Label descriptionRight = new Label(composite, SWT.WRAP);
		descriptionRight.setText(
				"This section shows the content of the impacted element that is currently selected in left-hand pane.");
		GridData rightGridData = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		rightGridData.widthHint = 600;
		descriptionRight.setLayoutData(rightGridData);

		Composite groupListComposite = new Composite(composite, SWT.NONE);
		groupListComposite.setLayout(new GridLayout(1, true));
		GridData groupListCompositeGridData = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		groupListCompositeGridData.widthHint = 500;
		groupListComposite.setLayoutData(groupListCompositeGridData);

		GridData groupGridData = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		groupGridData.widthHint = 500;
		groupGridData.heightHint = 300;

		Group messageSetsGroup = new Group(groupListComposite, SWT.NONE);
		messageSetsGroup.setText("Message Sets");
		messageSetsGroup.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		messageSetsGroup.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		messageSetsGroup.setLayoutData(groupGridData);
		messageSetsGroup.setLayout(new FillLayout(SWT.VERTICAL));
		Table messageSetTable = new Table(messageSetsGroup, SWT.BORDER | SWT.V_SCROLL);
		messageSetTable.setLinesVisible(true);
		TableCursor messageSetTableCursor = new TableCursor(messageSetTable, SWT.NONE);
		// 构造Message Set表格
		GenerateTableForMCandMDandMS.generateMessageSetTableOfImpactPage(messageSetTable, msgSetList);

		Group messageDefinitionsGroup = new Group(groupListComposite, SWT.NONE);
		messageDefinitionsGroup.setText("Message Definitions");
		messageDefinitionsGroup.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		messageDefinitionsGroup.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		messageDefinitionsGroup.setLayoutData(groupGridData);
		messageDefinitionsGroup.setLayout(new FillLayout(SWT.VERTICAL));
		Table messageDefinitionTable = new Table(messageDefinitionsGroup, SWT.BORDER | SWT.V_SCROLL);
		messageDefinitionTable.setLinesVisible(true);
		TableCursor messageDefinitionTableCursor = new TableCursor(messageDefinitionTable, SWT.NONE);
		// 构造Message Definition表格
		GenerateTableForMCandMDandMS.generateMessageDefinationTableOfImpactPage(messageDefinitionTable, msgDefinitionList);

		Group messageComponentGroup = new Group(groupListComposite, SWT.NONE);
		messageComponentGroup.setText("Message Component");
		messageComponentGroup.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		messageComponentGroup.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		messageComponentGroup.setLayoutData(groupGridData);
		messageComponentGroup.setLayout(new FillLayout(SWT.VERTICAL));
		Table messageComponentTable = new Table(messageComponentGroup, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
		messageComponentTable.setLinesVisible(true);
		TableCursor msgComponentTableCursor = new TableCursor(messageComponentTable, SWT.NONE);
		// 构造Message Component表格
		GenerateTableForMCandMDandMS.generateMessageComponentTableOfImpactPage(messageComponentTable, messageComponentList);

		Composite treeComposite = new Composite(composite, SWT.NONE);
		FillLayout treeCompositetFillLayout = new FillLayout(SWT.VERTICAL);
		treeComposite.setLayout(treeCompositetFillLayout);
		GridData treeCompositeGridData = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		treeCompositeGridData.widthHint = 600;
		treeCompositeGridData.heightHint = 800;
		treeComposite.setLayoutData(treeCompositeGridData);

		Tree containmentTree = new Tree(treeComposite, SWT.BORDER);
		TreeItem treeRoot = new TreeItem(containmentTree, SWT.NONE);
		treeRoot.setExpanded(true);
		
		// For Message Set表格，添加点击事件
		messageSetTableCursor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TableItem tableItem = messageSetTableCursor.getRow();
				String msgSetName = tableItem.getText(messageSetTableCursor.getColumn());
				String messageSetId = (String)tableItem.getData(String.valueOf(messageSetTable.getSelectionIndex()));
				treeRoot.setText(msgSetName);
				treeRoot.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
				treeRoot.removeAll();
				// 递归生成contrainment tree on Impact page
				GenerateCommonTree.generateContainmentTreeForMessageSetofImpactPage(treeRoot, DerbyDaoUtil.getMsgDefinitionByMsgSetId(messageSetId));
				// 生成Message Set的约束
				GenerateCommonTree.addContraintsNode(treeRoot, DerbyDaoUtil.getContraints(messageSetId), null, null);
				treeRoot.setExpanded(true);
			}
		});
		
		// For 消息定义表添加单击事件
		messageDefinitionTableCursor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TableItem tableItem = messageDefinitionTableCursor.getRow();
				String msgDefinitionName = tableItem.getText(messageDefinitionTableCursor.getColumn());
				String msgDefinitionId = (String)tableItem.getData(String.valueOf(messageDefinitionTable.getSelectionIndex()));
				treeRoot.setText(msgDefinitionName);
				treeRoot.setImage(ImgUtil.createImg(ImgUtil.MD_SUB1));
				treeRoot.removeAll();
				// 递归生成contrainment tree on Impact page, Message Definition自身无约束
				GenerateCommonTree.generateContainmentTreeForMessageDefinition(treeRoot, DerbyDaoUtil.getMsgBuildingBlock(msgDefinitionId));
				// 生成 Message Definition 约束
				GenerateCommonTree.addContraintsNode(treeRoot, DerbyDaoUtil.getContraints(msgDefinitionId), null, null);
				treeRoot.setExpanded(true);
			}
		});
		
		// For 消息组件表 添加单击事件
		msgComponentTableCursor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TableItem tableItem = msgComponentTableCursor.getRow();
				String msgComponentName = tableItem.getText(msgComponentTableCursor.getColumn());
				String msgComponentId = (String)tableItem.getData(String.valueOf(messageComponentTable.getSelectionIndex()));
				treeRoot.setText(msgComponentName);
				treeRoot.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_COMPONENT));
				treeRoot.removeAll();
				// 递归生成contrainment tree on Impact page,
				GenerateCommonTree.generateContainmentTreeForMessageComponent(treeRoot, DerbyDaoUtil.getMessageElementList(msgComponentId));
				// 生成  Message component 约束
				GenerateCommonTree.addContraintsNode(treeRoot, DerbyDaoUtil.getContraints(msgComponentId), null, null);
				treeRoot.setExpanded(true);
			}
		});
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	}

	/**
	 * Incoming Associations Page 显示和编辑
	 */
	void createIncomingAssociationsPage() {
		Composite composite = new Composite(getContainer(), SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		int index = addPage(composite);
		setPageText(index, "Incoming Associations");
		GridData gd = new GridData(SWT.TOP, SWT.TOP, true, false, 1, 1);
		composite.setLayoutData(gd);

		Label descriptionLabel = new Label(composite, SWT.WRAP);
		descriptionLabel.setText(
				"Impactd Elements: This section shows all the Business Component that contain an element typed by this repository item. Select any of them to see how it is used.");
		GridData leftGridData = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		leftGridData.widthHint = 500;
		descriptionLabel.setLayoutData(leftGridData);

		Label containmentTreeLabel = new Label(composite, SWT.WRAP);
		containmentTreeLabel.setText(
				"This section shows the content of the impacted element that is currently selected in the left-hand pane.");
		GridData rightGridData = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		rightGridData.widthHint = 600;
		containmentTreeLabel.setLayoutData(rightGridData);

		Table elementTable = new Table(composite, SWT.BORDER);
		elementTable.setLinesVisible(true);
		GridData elementTableGridData = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		elementTableGridData.widthHint = 500;
		elementTable.setLayoutData(elementTableGridData);
		TableCursor elementTableCursor = new TableCursor(elementTable, SWT.NONE);
		this.generateImpactedElementOfIncomingPage(elementTable);
		
		GridData containmentTreeGridData = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		containmentTreeGridData.widthHint = 600;
		containmentTreeGridData.heightHint = 500;

		Tree containmentTree = new Tree(composite, SWT.BORDER);
		TreeItem treeRoot = new TreeItem(containmentTree, SWT.NONE);
		containmentTree.setLayoutData(containmentTreeGridData);
		
		elementTableCursor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TableItem tableItem = elementTableCursor.getRow();
				String bizComponentName = tableItem.getText(elementTableCursor.getColumn());
				String bizComponentId = (String)tableItem.getData("bizComponentId");
				treeRoot.setText(bizComponentName);
				treeRoot.setImage(ImgUtil.createImg(ImgUtil.BC));
				treeRoot.removeAll();
				// 生成contrainment tree on Incoming Association page
				generateContainmentTreeForIncomingAssociationPage(treeRoot, DerbyDaoUtil.getBizElementsByBizComponentId(bizComponentId));
				treeRoot.setExpanded(true);
			}
		});
	}
	
	private void generateImpactedElementOfIncomingPage(Table elementTable) {
		for (EcoreBusinessComponent bizComponent: imcomingAssociationList) {
			TableItem tableItem1 = new TableItem(elementTable, SWT.NONE);
			tableItem1.setText(bizComponent.getName());
			tableItem1.setData("bizComponentId", bizComponent.getId());
			tableItem1.setImage(ImgUtil.createImg(ImgUtil.BC));
		}
	}
	
	private void generateContainmentTreeForIncomingAssociationPage(TreeItem treeRoot, ArrayList<EcoreBusinessElement> bizElementList) {
		for (EcoreBusinessElement bizElement: bizElementList) {
			TreeItem treeItem = new TreeItem(treeRoot, SWT.NONE);
			String elementDisplayName = bizElement.getName() + " [" + bizElement.getMinOccurs() + "," + bizElement.getMaxOccurs() + "]";
			// 数据类型 1：datatype，2：消息组件
			if ("2".equals(bizElement.getType())) {
				EcoreBusinessComponent bizComponent = DerbyDaoUtil.getBusinessComponentById(bizElement.getTypeId());
				treeItem.setText(elementDisplayName + " : " + bizComponent.getName());
			} else {
				// 数据类型 1：datatype
				EcoreDataType dataType = DerbyDaoUtil.getDataTypeById(bizElement.getTypeId());
				treeItem.setText(elementDisplayName + " : " + dataType.getName());
			}
			treeItem.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
		}
	}

	/**
	 * Version Sub set Page 显示和编辑
	 */
	void createVersionSubset() {
		
		Composite parentComposite = getContainer();
		ScrolledComposite scrolledComposite = new ScrolledComposite(parentComposite, SWT.V_SCROLL | SWT.H_SCROLL);
		scrolledComposite.setBounds(parentComposite.getShell().getBounds());
		
		GridData sc_gd = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		scrolledComposite.setLayoutData(sc_gd);
		
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(composite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		int index = addPage(scrolledComposite);
		setPageText(index, "Version/Subsets");
		
		composite.setLayout(new GridLayout(2, false));
		GridData gd = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd.widthHint = scrolledComposite.getBounds().width;
		composite.setLayoutData(gd);

		Composite leftComposite = new Composite(composite, SWT.NONE);
		leftComposite.setLayout(new FillLayout(SWT.VERTICAL));
		GridData lc_gd = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		lc_gd.widthHint = 550; // composite.getBounds().width / 3;
		leftComposite.setLayoutData(lc_gd);
		
		GridData rc_gd = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		rc_gd.widthHint = 500; // composite.getBounds().width / 3;
		Composite rightComposite = new Composite(composite, SWT.NONE);
		rightComposite.setLayout(new FillLayout(SWT.VERTICAL));
		rightComposite.setLayoutData(rc_gd);
		
		ExpandBar nextVersionBar = new ExpandBar(leftComposite, SWT.NONE);
		Composite nextVersionBarComposite = new Composite(nextVersionBar, SWT.NONE);
		GridData nextVersionBarCompositeGridData = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		nextVersionBarComposite.setLayout(new GridLayout(1, false));
		nextVersionBarComposite.setLayoutData(nextVersionBarCompositeGridData);
		
		nextVersionBar.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		nextVersionBar.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		nextVersionBar.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		
		Label leftDescriptionLabel = new Label(nextVersionBarComposite, SWT.WRAP);
		leftDescriptionLabel.setText("Shows the new versions of this repository itme.");
		List versionListWidget = new List(nextVersionBarComposite, SWT.BORDER);
		GridData versionListGridData = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
		versionListGridData.widthHint = 500;
		versionListWidget.setLayoutData(versionListGridData);
		if(this.bizComponentNextVersionList != null && this.bizComponentNextVersionList.size() > 0) {
			this.generateNextVersionList(versionListWidget);
		}
		
		ExpandItem nextVersionItem = new ExpandItem(nextVersionBar, SWT.NONE);
		nextVersionItem.setText("Next Version");
		nextVersionItem.setHeight(450);
		nextVersionItem.setExpanded(true);
		nextVersionItem.setControl(nextVersionBarComposite);
		
		ExpandBar previousVersionBar = new ExpandBar(rightComposite, SWT.NONE);
		previousVersionBar.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		previousVersionBar.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		previousVersionBar.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		
		Composite previousVersionComposite = new Composite(previousVersionBar, SWT.NONE);
		previousVersionComposite.setLayout(new GridLayout(1, false));
		Label rightDescriptionLabel = new Label(previousVersionComposite, SWT.WRAP);
		rightDescriptionLabel.setText("Shows the previous version of this repository item.");
		Label previousVersionLabel = new Label(previousVersionComposite, SWT.WRAP);
		previousVersionLabel.setText("The previous version of this object is: ");
		Link previousVersionLink = new Link(previousVersionComposite, SWT.NONE);
		EcoreBusinessComponent previousBizComponent = DerbyDaoUtil.getPreviousVersionForBizComponent(businessComponentId);
		if (previousBizComponent.getName() != null) {
			previousVersionLink.setText(previousBizComponent.getName());
			previousVersionLink.setData(previousBizComponent.getId());
		}

		ExpandItem previousVersionItem = new ExpandItem(previousVersionBar, SWT.NONE);
		previousVersionItem.setText("Previous Version");
		previousVersionItem.setHeight(450);
		previousVersionItem.setExpanded(true);
		previousVersionItem.setControl(previousVersionComposite);
	}
	
	private void generateNextVersionList(List versionListWidget) {
		for (int index = 0; index < this.bizComponentNextVersionList.size(); index++) {
			versionListWidget.add(bizComponentNextVersionList.get(index).getName());
			versionListWidget.setData(String.valueOf(index), bizComponentNextVersionList.get(index).getId());
		}
	}
	
	public void showBusinessElementDetailBar(Composite rightComposite,ScrolledComposite scrolledComposite) {
		// ------------ Business Element Details Begin ------------------------
		for (Control control: rightComposite.getChildren()) {
			control.dispose();
		}
		ExpandBar businessElementBar = new ExpandBar(rightComposite, SWT.V_SCROLL);
		businessElementBar.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		businessElementBar.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		businessElementBar.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));

		Composite generalInformationComposite = new Composite(businessElementBar, SWT.NONE);
		generalInformationComposite.setLayout(new GridLayout(2, false));

		GridData generalInforGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		generalInforGridData.widthHint = 350;
		Label beNameLabel = new Label(generalInformationComposite, SWT.NONE);
		beNameLabel.setText("Name");
		beNameText = new Text(generalInformationComposite, SWT.BORDER);
		beNameText.setEditable(false);
		beNameText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		beNameText.setLayoutData(generalInforGridData);
		beNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTableItem.isDisposed()) {
					// 左边的树的名称展示
					String showText = "";
					if (selectedTableItem.getData("codeType")==null) {
						showText = beNameText.getText() + " [" + beMinOccursText.getText() + ", "
							+ beMaxOccursText.getText() + "]";
						selectedTableItem.setText(showText);
					}
					
					selectedTableItem.setData("beNameText", beNameText.getText());
					setDirty(true);
				}
			}
		});

		GridData documentationGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		documentationGridData.widthHint = 350;
		documentationGridData.heightHint = 150;
		Label documentationLabel = new Label(generalInformationComposite, SWT.NONE);
		documentationLabel.setText("Documentation");
		beDocText = new Text(generalInformationComposite, SWT.BORDER | SWT.WRAP);
		beDocText.setEditable(false);
		beDocText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		beDocText.setLayoutData(documentationGridData);
		beDocText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTableItem.isDisposed()) {
					selectedTableItem.setData("beDocText", beDocText.getText());
					setDirty(true);
				}
			}
		});

		Label minOccursLabel = new Label(generalInformationComposite, SWT.NONE);
		minOccursLabel.setText("Min Occurs");
		beMinOccursText = new Text(generalInformationComposite, SWT.BORDER);
		beMinOccursText.setEditable(false);
		beMinOccursText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		beMinOccursText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTableItem.isDisposed()) {
					String showText = "";
					if (selectedTableItem.getData("codeType")==null) {
					showText = beNameText.getText() + " [" + beMinOccursText.getText() + ", "
							+ beMaxOccursText.getText() + "]" ;
					selectedTableItem.setText(showText);
					}
					selectedTableItem.setData("beMinOccursText", beMinOccursText.getText());
					setDirty(true);
				}
			}
		});

		Label MaxOccursLabel = new Label(generalInformationComposite, SWT.NONE);
		MaxOccursLabel.setText("Max Occurs");
		beMaxOccursText = new Text(generalInformationComposite, SWT.BORDER);
		beMaxOccursText.setEditable(false);
		beMaxOccursText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		beMaxOccursText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTableItem.isDisposed()) {
					String showText = "";
					if (selectedTableItem.getData("codeType")==null) {
					showText = beNameText.getText() + " [" + beMinOccursText.getText() + ", "
								+ beMaxOccursText.getText() + "]";
					selectedTableItem.setText(showText);
					}
						
					selectedTableItem.setData("beMaxOccursText", beMaxOccursText.getText());
					setDirty(true);
				}
			}
		});

		Label isDerivedLabel = new Label(generalInformationComposite, SWT.NONE);
		isDerivedLabel.setText("Is Derived");
		isDerivedCheckButton = new Button(generalInformationComposite, SWT.CHECK);
		isDerivedCheckButton.setEnabled(false);
		isDerivedCheckButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (!selectedTableItem.isDisposed()) {
					selectedTableItem.setData("isDerivedCheckButton", isDerivedCheckButton.getSelection());
					setDirty(true);
				}
			}
		});

		Label typeLabel = new Label(generalInformationComposite, SWT.NONE);
		typeLabel.setText("Type");
		Composite cmp = new Composite(generalInformationComposite, SWT.NONE);
		cmp.setLayoutData(new GridData(350, 40));
		cmp.setLayout(new GridLayout(3, false));

		GridData generalTyperGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		generalTyperGridData.widthHint = 260;

		beTypeText = new Text(cmp, SWT.BORDER);
		beTypeText.setLayoutData(generalTyperGridData);
		beTypeText.setEnabled(false);
		beTypeText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (selectedTableItem != null && !selectedTableItem.isDisposed()) {
					selectedTableItem.setData("beTypeText", beTypeText.getText());
					String showText = "";
					if (selectedTableItem.getData("codeType")==null) {
					showText = beNameText.getText() + " [" + beMinOccursText.getText() + ", "
							+ beMaxOccursText.getText() + "]";
					selectedTableItem.setText(showText);	
					}
					if (beTypeText.getData("beType_id") != null) {
						selectedTableItem.setData("beTypeTextId", beTypeText.getData("beType_id"));
						selectedTableItem.setData("beType", beTypeText.getData("beType"));
					}
					setDirty(true);
				}
			}
		});
		btBtn = new Button(cmp, SWT.NONE);
		btBtn.setText("choose");
		btBtn.setBackground(new Color(Display.getCurrent(),135,206,250));
		btBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MessageBoxUtil.createSearchDialogue(beTypeText,selectedTableItem);
				setDirty(true);
			}
		});
		Button btSearchBtn = new Button(cmp, SWT.NONE);
		btSearchBtn.setImage(ImgUtil.createImg(ImgUtil.SEARCH_BTN));
		btSearchBtn.setBackground(new Color(Display.getCurrent(),135,206,250));
		btSearchBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (elementsTree.getSelectionCount() == 1) {
					TreeItem treeItem = elementsTree.getSelection()[0];
					String dataType = (String)treeItem.getData("beType");
//					String beTypeTextType = (String)treeItem.getData("beTypeTextType");
					CTabFolder cTabFolder =TreeListView.getTabFolder();
					if ("2".equals(dataType)) {
						String Id = beTypeText.getData("beType_id").toString();
//						String Id = (String)treeItem.getData("beTypeTextId");
						Tree firstTabTree = TreeListView.getFirstTabTree();
						TreeItem mcTreeItem = firstTabTree.getItem(1);
						for (TreeItem mcItem: mcTreeItem.getItems()) {
							EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)mcItem.getData("EcoreTreeNode");
							if (beTypeText.getData("meLever")!=null) {
//								treeItem.setText(meNameText.getText() + " [" + meMinOccursText.getText() + ", "
//										+ meMaxOccursText.getText() + "] : " +meTypeText.getText());
								String meComponentId = (String) beTypeText.getData("beType_id");
							if (ecoreTreeNode.getId().equals(meComponentId)) {
								TransferDataBean transferDataBean = new TransferDataBean();
								transferDataBean.setId(ecoreTreeNode.getId());
								transferDataBean.setName(ecoreTreeNode.getName());
								transferDataBean.setLevel(ecoreTreeNode.getLevel());
								transferDataBean.setType((String)mcItem.getData("type"));
								transferDataBean.setChildId(mcItem.getData("childId") == null ? "" : (String)mcItem.getData("childId"));
//								String status=ecoreTreeNode.getRegistrationStatus();
								transferDataBean.setImgPath(ImgUtil.BC);
								transferDataBean.setTreeListItem((TreeListItem)mcItem);
								firstTabTree.setSelection(mcItem);
//								cTabFolder.setSelection(1);
								EditorUtil.open(transferDataBean.getType(), transferDataBean);
								return;
							}
							}
							
							else if (ecoreTreeNode.getId().equals(Id)) {
								TransferDataBean transferDataBean = new TransferDataBean();
								transferDataBean.setId(ecoreTreeNode.getId());
								transferDataBean.setName(ecoreTreeNode.getName());
								transferDataBean.setLevel(ecoreTreeNode.getLevel());
								transferDataBean.setType((String)mcItem.getData("type"));
								transferDataBean.setChildId(mcItem.getData("childId") == null ? "" : (String)mcItem.getData("childId"));
								transferDataBean.setImgPath(ImgUtil.BC);
								firstTabTree.setSelection(mcItem);
//								cTabFolder.setSelection(1);
								transferDataBean.setTreeListItem((TreeListItem)mcItem);
								EditorUtil.open(transferDataBean.getType(), transferDataBean);
								return;
							}
						}
					}else {
						String dataTypeId = (String)treeItem.getData("beTypeTextId");
						Tree firstTabTree = TreeListView.getFirstTabTree();
						TreeItem dataTypeTreeItem = firstTabTree.getItem(0);
						TreeItem[] allTypeItemArray = dataTypeTreeItem.getItems();
						for (TreeItem oneTypeTreeItem: allTypeItemArray) {
							for (TreeItem typeTreeItem: oneTypeTreeItem.getItems()) {
								EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)typeTreeItem.getData("EcoreTreeNode");
								if (beTypeText.getData("meLever")!=null) {
//									treeItem.setText(meNameText.getText() + " [" + meMinOccursText.getText() + ", "
//											+ meMaxOccursText.getText() + "] : " +meTypeText.getText());
									String meComponentId = (String) beTypeText.getData("beType_id");
								if (ecoreTreeNode.getId().equals(meComponentId)) {
									TransferDataBean transferDataBean = new TransferDataBean();
									transferDataBean.setId(ecoreTreeNode.getId());
									transferDataBean.setName(ecoreTreeNode.getName());
									transferDataBean.setLevel(ecoreTreeNode.getLevel());
									transferDataBean.setType((String)typeTreeItem.getData("type"));
									transferDataBean.setChildId(typeTreeItem.getData("childId") == null ? "" : (String)typeTreeItem.getData("childId"));
									String status=ecoreTreeNode.getRegistrationStatus();
									if("1".equals(ecoreTreeNode.getObjType())){
										transferDataBean.setImgPath(ImgUtil.CODE_SET_SUB1_2);
									}else if("2".equals(ecoreTreeNode.getObjType())) {
										transferDataBean.setImgPath(ImgUtil.CODE_SET_SUB1);
									}
									transferDataBean.setTreeListItem((TreeListItem)typeTreeItem);
									cTabFolder.setSelection(0);
									firstTabTree.setSelection(typeTreeItem);
									EditorUtil.open(transferDataBean.getType(), transferDataBean);
									return;
								}
								}
								else if (ecoreTreeNode.getId().equals(dataTypeId)) {
									TransferDataBean transferDataBean = new TransferDataBean();
									transferDataBean.setId(ecoreTreeNode.getId());
									transferDataBean.setName(ecoreTreeNode.getName());
									transferDataBean.setLevel(ecoreTreeNode.getLevel());
									transferDataBean.setType((String)typeTreeItem.getData("type"));
									transferDataBean.setChildId(typeTreeItem.getData("childId") == null ? "" : (String)typeTreeItem.getData("childId"));
									if("1".equals(ecoreTreeNode.getObjType())){
										transferDataBean.setImgPath(ImgUtil.CODE_SET_SUB1_2);
									}else if("2".equals(ecoreTreeNode.getObjType())) {
										transferDataBean.setImgPath(ImgUtil.CODE_SET_SUB1);
									}
									transferDataBean.setTreeListItem((TreeListItem)typeTreeItem);
									cTabFolder.setSelection(0);
									firstTabTree.setSelection(typeTreeItem);
									EditorUtil.open(transferDataBean.getType(), transferDataBean);
									return;
								}
							}
						}
					}
				}
			}
		});

		ExpandItem generalInformationItem = new ExpandItem(businessElementBar, SWT.NONE);
		generalInformationItem.setText("Business Element Details");
		generalInformationItem.setHeight(340);
		generalInformationItem.setExpanded(true);
		generalInformationItem.setControl(generalInformationComposite);

		// ------------ Business Element Details End ------------------------

		// -----------------------CMP Information Group Begin -----------------------

		Composite cmpInformationComposite = new Composite(businessElementBar, SWT.NONE);
		cmpInformationComposite.setLayout(new GridLayout(2, false));
		Label objectIdentifierLabel = new Label(cmpInformationComposite, SWT.NONE);
		objectIdentifierLabel.setText("Object Identifier");
		beObjectIdentifierText = new Text(cmpInformationComposite, SWT.BORDER);
		beObjectIdentifierText.setEditable(false);
		beObjectIdentifierText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		beObjectIdentifierText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTableItem.isDisposed()) {
					selectedTableItem.setData("beObjectIdentifierText", beObjectIdentifierText.getText());
					rightComposite.setEnabled(true);
					setDirty(true);
				}
			}
		});
		GridData objectIdentifierGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		objectIdentifierGridData.widthHint = 350;
		beObjectIdentifierText.setLayoutData(objectIdentifierGridData);

		ExpandItem cmpInformationItem = new ExpandItem(businessElementBar, SWT.NONE);
		cmpInformationItem.setText("CMP Information");
		cmpInformationItem.setHeight(90);
		cmpInformationItem.setExpanded(true);
		cmpInformationItem.setControl(cmpInformationComposite);

		// -----------------------CMP Information Group End -----------------------

		// ----------------------- Registration Information Group Begin -----------
//		Composite registrationInfoComposite = new Composite(businessElementBar, SWT.NONE);
//		registrationInfoComposite.setLayout(new GridLayout(2, false));
//		Label registrationStatusLabel = new Label(registrationInfoComposite, SWT.NONE);
//		registrationStatusLabel.setText("Registration Status");
//		ComboViewer statusComboViewer = new ComboViewer(registrationInfoComposite, SWT.NONE);
//		beStatusCombo = statusComboViewer.getCombo();
//		beStatusCombo.add("Registered");
//		beStatusCombo.add("Provisionally Registered");
//		beStatusCombo.setEnabled(false);
//		GridData valueGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
//		valueGridData.widthHint = 150;
//		beStatusCombo.setLayoutData(valueGridData);
//		beStatusCombo.addModifyListener(new ModifyListener() {
//			@Override
//			public void modifyText(ModifyEvent e) {
//				if (!selectedTableItem.isDisposed()) {
//					selectedTableItem.setData("beStatusCombo", beStatusCombo.getText());
//					setDirty(true);
//				}
//			}
//		});

//		Label removalDateLabel = new Label(registrationInfoComposite, SWT.NONE);
//		removalDateLabel.setText("Removal Date");
//		beRemovalDate = new Text(registrationInfoComposite, SWT.BORDER);
//		beRemovalDate.setToolTipText("Date Format: MM/DD/YY or YYYY-MM-DD");
//		beRemovalDate.setLayoutData(valueGridData);
//		beRemovalDate.setEnabled(false);
//		beRemovalDate.addModifyListener(new ModifyListener() {
//			@Override
//			public void modifyText(ModifyEvent e) {
//				if (!selectedTableItem.isDisposed()) {
//					selectedTableItem.setData("beRemovalDate", beRemovalDate.getText());
//					setDirty(true);
//				}
//			}
//		});

//		ExpandItem registrationInfoItem = new ExpandItem(businessElementBar, SWT.NONE);
//		registrationInfoItem.setText("Registration Information");
//		registrationInfoItem.setHeight(90);
//		registrationInfoItem.setExpanded(true);
//		registrationInfoItem.setControl(registrationInfoComposite);

		// ----------------------- Registration Information Group End
		// -----------------------

		// ---------- Synonyms Expand Item Begin -------------------------------------
		Composite synonymsComposite = new Composite(businessElementBar, SWT.NONE);
		synonymsComposite.setLayout(new GridLayout(1, false));
		Label descriptionLabel = new Label(synonymsComposite, SWT.NONE);
		descriptionLabel.setText("This section show the synonyms of object");
		GridData descriptionGridData = new GridData();
		descriptionGridData.heightHint = 25;
		descriptionLabel.setLayoutData(descriptionGridData);
		GridData tableGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		tableGridData.widthHint = 350;
		tableGridData.heightHint = 150;

		Composite synonymsToolbarComposite = new Composite(synonymsComposite, SWT.NONE);
		synonymsToolbarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

		addSynonymsBtn = new Button(synonymsToolbarComposite, SWT.NONE);
		addSynonymsBtn.setImage(ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR));
		addSynonymsBtn.setEnabled(false);
		addSynonymsBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MessageBoxUtil.createSynonymsDialogue(beSynonymsTable, selectedTableItem);
				setDirty(true);
			}
		});

		deleteButtonForSynonyms = new Button(synonymsToolbarComposite, SWT.NONE);
		deleteButtonForSynonyms.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
		deleteButtonForSynonyms.setEnabled(false);
		deleteButtonForSynonyms.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MessageBoxUtil.deleteSynonymDialogue(beSynonymsTable, selectedTableItem);
				setDirty(true);
			}
		});

		beSynonymsTable = new Table(synonymsComposite, SWT.BORDER);
		beSynonymsTable.setLayoutData(tableGridData);
		beSynonymsTable.setHeaderVisible(true);
		beSynonymsTable.setLinesVisible(true);

		TableColumn contextColumn = new TableColumn(beSynonymsTable, SWT.NONE);
		contextColumn.setWidth(100);
		contextColumn.setText("Context");
		TableColumn SynonymsColumn = new TableColumn(beSynonymsTable, SWT.NONE);
		SynonymsColumn.setWidth(100);
		SynonymsColumn.setText("Synonyms");

		ExpandItem synonymsItem = new ExpandItem(businessElementBar, SWT.NONE);
		synonymsItem.setText("Synonyms");
		synonymsItem.setHeight(250);
		synonymsItem.setExpanded(true);
		synonymsItem.setControl(synonymsComposite);
		// ---------- Synonyms Expand Item End -------------------------------------

		// ---------- Constraints Expand Item Begin
		// -------------------------------------
		Composite contraintsComposite = new Composite(businessElementBar, SWT.NONE);
		contraintsComposite.setLayout(new GridLayout(1, false));
		Label contraintsDescLabel = new Label(contraintsComposite, SWT.WRAP);
		contraintsDescLabel.setText(
				"All the constraints contained in this object(other constraints - such as constraints defined on type - may also apply).");
		GridData labelGridData = new GridData();
		labelGridData.widthHint = 430;
		contraintsDescLabel.setLayoutData(labelGridData);

		Composite toolbarComposite = new Composite(contraintsComposite, SWT.NONE);
		toolbarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

		addConstraintBtn1 = new Button(toolbarComposite, SWT.NONE);
		addConstraintBtn1.setImage(ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR));
		addConstraintBtn1.setEnabled(false);
		addConstraintBtn1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MessageBoxUtil.createConstraintDialogueForBC("add", beConstraintsTable, selectedTableItem);
				setDirty(true);
			}
		});

		editConstraintBtn1 = new Button(toolbarComposite, SWT.NONE);
		editConstraintBtn1.setImage(ImgUtil.createImg(ImgUtil.EDIT_IMG_TOOLBAR));
		editConstraintBtn1.setEnabled(false);
		editConstraintBtn1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (beConstraintsTable.getSelectionCount() > 0) {
					MessageBoxUtil.createConstraintDialogueForBC("edit", beConstraintsTable, selectedTableItem);
					setDirty(true);
				}
			}
		});

		deleteConstraintBtn1 = new Button(toolbarComposite, SWT.NONE);
		deleteConstraintBtn1.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
		deleteConstraintBtn1.setEnabled(false);
		deleteConstraintBtn1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MessageBoxUtil.deleteConstraintForBC(beConstraintsTable, selectedTableItem);
				setDirty(true);
			}
		});

		beConstraintsTable = new Table(contraintsComposite, SWT.BORDER);
		beConstraintsTable.setHeaderVisible(true);
		beConstraintsTable.setLinesVisible(true);
		TableColumn nameColumn = new TableColumn(beConstraintsTable, SWT.NONE);
		nameColumn.setWidth(100);
		nameColumn.setText("Name");
		TableColumn defifinitionColumn = new TableColumn(beConstraintsTable, SWT.NONE);
		defifinitionColumn.setWidth(100);
		defifinitionColumn.setText("Definition");
		TableColumn expressionLanguageColumn = new TableColumn(beConstraintsTable, SWT.NONE);
		expressionLanguageColumn.setWidth(100);
		expressionLanguageColumn.setText("Expression Language");
		TableColumn expressionColumn = new TableColumn(beConstraintsTable, SWT.NONE);
		expressionColumn.setWidth(200);
		expressionColumn.setText("Expression");

		GridData constraintsTableGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		constraintsTableGridData.widthHint = 430;
		constraintsTableGridData.heightHint = 150;
		beConstraintsTable.setLayoutData(constraintsTableGridData);

		ExpandItem contraintsItem = new ExpandItem(businessElementBar, SWT.NONE);
		contraintsItem.setText("Contraints");
		contraintsItem.setHeight(250);
		contraintsItem.setExpanded(true);
		contraintsItem.setControl(contraintsComposite);
		// ---------- Constraints Expand Item End -------------------------------------
		rightComposite.requestLayout();
		scrolledComposite.setMinSize(rightComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	/**
	 * 获取页面所有的值
	 */
	public EcoreBusinessComponentVO getAllData() {
		EcoreBusinessComponentVO businessComponentVO = new EcoreBusinessComponentVO();
		EcoreBusinessComponent businessComponent = new EcoreBusinessComponent();
		businessComponent.setId(businessComponentId);
		businessComponent.setName(this.summaryNameText.getText());
		businessComponent.setDefinition(this.summaryDocText.getText());
		businessComponent.setPreviousVersion(this.summaryPreviosDocText.getText());
		businessComponent.setObjectIdentifier(this.summaryobjectIdentifierText.getText());
		businessComponent.setRegistrationStatus(this.summaryStatusCombo.getText());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date contentRemovalDate;
		try {
			contentRemovalDate = (summaryRemovalDate.getText() == null || summaryRemovalDate.getText() == "") ? null
					: sdf.parse(summaryRemovalDate.getText());
		} catch (ParseException e) {
			contentRemovalDate = null;
		}
		businessComponent.setRemovalDate(contentRemovalDate);
		// 基本信息
		businessComponentVO.setEcoreBusinessComponent(businessComponent);

		// 获取example信息
		java.util.List<EcoreExample> examples = new ArrayList<EcoreExample>();
		String[] exampleItems = this.exampleList.getItems();
		Map<String,String> map=(Map<String, String>) exampleList.getData("map");
		if (exampleItems != null && exampleItems.length > 0) {
			for (String str : exampleItems) {
				EcoreExample bcExample = new EcoreExample();
				if (map!=null) {
					bcExample.setId(map.get(str)==null?UUID.randomUUID().toString():map.get(str));
					}else {
						bcExample.setId(UUID.randomUUID().toString());
					}
				bcExample.setExample(str);
				bcExample.setObj_id(businessComponent.getId());
				bcExample.setObj_type(ObjTypeEnum.BusinessComponent.getType());
				examples.add(bcExample);
			}
		}
		businessComponentVO.setEcoreExamples(examples);

		// 获取constraints
		java.util.List<EcoreConstraint> constraints = new ArrayList<EcoreConstraint>();
		TableItem[] constraintItems = summaryConstraintsTable.getItems();
		if (constraintItems != null && constraintItems.length > 0) {
			for (TableItem tableItem : constraintItems) {
				EcoreConstraint bcConstraint = new EcoreConstraint();
				bcConstraint.setId(tableItem.getData("id")==null?UUID.randomUUID().toString():tableItem.getData("id").toString());
//				bcConstraint.setId(UUID.randomUUID().toString());
				bcConstraint.setObj_id(businessComponent.getId());
				bcConstraint.setObj_type(ObjTypeEnum.BusinessComponent.getType());
				bcConstraint.setName(tableItem.getText(0));
				bcConstraint.setDefinition(tableItem.getText(1));
				bcConstraint.setExpressionlanguage(tableItem.getText(2));
				bcConstraint.setExpression(tableItem.getText(3));
				constraints.add(bcConstraint);
			}
		}
		businessComponentVO.setEcoreConstraints(constraints);
		businessComponentVO.setEcoreNextVersions(null);

		java.util.List<EcoreBusinessElementVO> ecoreBusinessElementVOs = new ArrayList<EcoreBusinessElementVO>();
		// be信息
		TreeItem[] beItems = elementsTree.getItems();
		for (int i = 0; i < beItems.length; i++) {
			EcoreBusinessElementVO businessElementVO = new EcoreBusinessElementVO();

			TreeItem beTableItem = beItems[i];

			EcoreBusinessElement ecoreBusinessElement = new EcoreBusinessElement();
			ecoreBusinessElement.setBusinessComponentId(businessComponentId);
			ecoreBusinessElement.setId(beTableItem.getData("id")==null?UUID.randomUUID().toString():beTableItem.getData("id").toString());
//			ecoreBusinessElement.setId(UUID.randomUUID().toString());
			ecoreBusinessElement.setName(String.valueOf(beTableItem.getData("beNameText") == null ? "" : beTableItem.getData("beNameText")));
			ecoreBusinessElement.setDefinition(String.valueOf(beTableItem.getData("beDocText") == null ? "" : beTableItem.getData("beDocText")));
			ecoreBusinessElement.setIsDerived("" + (beTableItem.getData("isDerivedCheckButton") == null ? false : Boolean.getBoolean("" + beTableItem.getData("isDerivedCheckButton"))));
			ecoreBusinessElement.setIsMessageAssociationEnd("");
			ecoreBusinessElement.setMinOccurs(NumberFormatUtil.getInt(beTableItem.getData("beMinOccursText") == null ? "" : beTableItem.getData("beMinOccursText").toString()));
			ecoreBusinessElement.setMaxOccurs(NumberFormatUtil.getInt(beTableItem.getData("beMaxOccursText") == null ? "" : beTableItem.getData("beMaxOccursText").toString()));
			ecoreBusinessElement.setObjectIdentifier(String.valueOf(beTableItem.getData("beObjectIdentifierText") == null ? "" : beTableItem.getData("beObjectIdentifierText")));
			ecoreBusinessElement.setPreviousVersion("");
//			ecoreBusinessElement.setRegistrationStatus(String.valueOf(beTableItem.getData("beStatusCombo") == null ? "" : beTableItem.getData("beStatusCombo")));
//			Date removalDate;
//			try {
//				removalDate = (beRemovalDate.getText() == null || beRemovalDate.getText() == "") ? null
//						: sdf.parse(beRemovalDate.getText());
//			} catch (ParseException e) {
//				removalDate = null;
//			}
//			ecoreBusinessElement.setRemovalDate(removalDate);
			// ---------------------------确认一下------------------------
			ecoreBusinessElement.setType(String.valueOf(beTableItem.getData("beType") == null ? "" : beTableItem.getData("beType")));
			ecoreBusinessElement.setTypeName(String.valueOf(beTableItem.getData("beTypeText") == null ? "" : beTableItem.getData("beTypeText")));
			ecoreBusinessElement.setTypeId(String.valueOf(beTableItem.getData("beType_id") == null ? "" : beTableItem.getData("beType_id")));

			// be基本信息
			businessElementVO.setEcoreBusinessElement(ecoreBusinessElement);
			// ---------------------------确认一下synonyms------------------------
//			businessElementVO.setEcoreExamples(ecoreExamples);

//			java.util.List<String[]> synonymsTableItems = (java.util.List<String[]>) beTableItem.getData("beSynonymsTableItems");
//			if (synonymsTableItems != null && synonymsTableItems.size() > 0) {
//				for (int i = 0; i < synonymsTableItems.size(); i++) {
//					TableItem tableItem = new TableItem(beSynonymsTable, SWT.NONE);
//					tableItem.setText(synonymsTableItems.get(i));
//				}
//			}

			java.util.List<String[]> constraintsTableItems = (java.util.List<String[]>) beTableItem
					.getData("beConstraintsTableItems");
			java.util.List<EcoreConstraint> beEcoreConstraintList = new ArrayList<EcoreConstraint>();
			if (constraintsTableItems != null && constraintsTableItems.size() > 0) {
				for (int j = 0; j < constraintsTableItems.size(); j++) {
					EcoreConstraint beConstraint = new EcoreConstraint();
					beConstraint.setId(beTableItem.getData("id")==null?UUID.randomUUID().toString():beTableItem.getData("id").toString());
//					beConstraint.setId(UUID.randomUUID().toString());
					beConstraint.setObj_id(ecoreBusinessElement.getId());
					beConstraint.setObj_type(ObjTypeEnum.BusinessElement.getType());
					
					beConstraint.setName(constraintsTableItems.get(j)[0]);
					beConstraint.setDefinition(constraintsTableItems.get(j)[1]);
					beConstraint.setExpressionlanguage(constraintsTableItems.get(j)[2]);
					beConstraint.setExpression(constraintsTableItems.get(j)[3]);
					beEcoreConstraintList.add(beConstraint);
				}
			}
			businessElementVO.setEcoreConstraints(beEcoreConstraintList);

			ecoreBusinessElementVOs.add(businessElementVO);
		}
		businessComponentVO.setEcoreBusinessElementVOs(ecoreBusinessElementVOs);

		// Sub-Types
		TreeItem[] subTypesItems = modelExploreTreeItem.getItems()[1].getItems();
		java.util.List<EcoreBusinessComponentRL> subTypesBusinessComponentRLList = new ArrayList<EcoreBusinessComponentRL>();
		for (int i = 0; i < subTypesItems.length; i++) {
			EcoreBusinessComponentRL ecoreBusinessComponentRL = new EcoreBusinessComponentRL();
			if (subTypesItems[i].getData("id")==null) {
				EcoreTreeNode node = (EcoreTreeNode) subTypesItems[i].getData("EcoreTreeNode");
				String id=node.getId();
				ecoreBusinessComponentRL.setId(id);
			}else {
			ecoreBusinessComponentRL.setId(subTypesItems[i].getData("id").toString());
			}
			ecoreBusinessComponentRL.setpId(businessComponentId);
			subTypesBusinessComponentRLList.add(ecoreBusinessComponentRL);
		}
		businessComponentVO.setChildBusinessComponents(subTypesBusinessComponentRLList);

		// Super-Types
		TreeItem[] superTypesItems = modelExploreTreeItem.getItems()[2].getItems();
		java.util.List<EcoreBusinessComponentRL> superTypesBusinessComponentRLList = new ArrayList<EcoreBusinessComponentRL>();
		for (int i = 0; i < superTypesItems.length; i++) {
			EcoreBusinessComponentRL ecoreBusinessComponentRL = new EcoreBusinessComponentRL();
			if (superTypesItems[i].getData("id")==null) {
				EcoreTreeNode node = (EcoreTreeNode) superTypesItems[i].getData("EcoreTreeNode");
				String id=node.getId();
				ecoreBusinessComponentRL.setpId(id);
			}else {
			ecoreBusinessComponentRL.setpId(superTypesItems[i].getData("id").toString());
			}
			ecoreBusinessComponentRL.setId(businessComponentId);
			superTypesBusinessComponentRLList.add(ecoreBusinessComponentRL);
		}
		businessComponentVO.setParentBusinessComponent(
				(superTypesBusinessComponentRLList == null || superTypesBusinessComponentRLList.size() == 0) ? null
						: superTypesBusinessComponentRLList.get(0));

		return businessComponentVO;
	}
	
	public void changeStatus() {
		if (businessComponentVO.getEcoreBusinessComponent()==null) {
			addBusinessElementBtn.setEnabled(true);
			deleteBusinessElementBtn.setEnabled(true);
//			isDerivedCheckButton.setEnabled(true);
//			beNameText.setEditable(true);
//			beDocText.setEditable(true);
//			beMinOccursText.setEditable(true);
//			beMaxOccursText.setEditable(true); 
//			beObjectIdentifierText.setEditable(true);
		}else {
			if (businessComponentVO.getEcoreBusinessComponent().getRegistrationStatus()==null) {
				
			}else {
				String status=businessComponentVO.getEcoreBusinessComponent().getRegistrationStatus();
				if (status.equals("Registered")) {
					// TODO Auto-generated method stub
					//注册状态为已注册，则内容不可编辑
					summaryNameText.setEditable(false);
					summaryNameText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
					summaryDocText.setEditable(false);
					summaryDocText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
					summaryobjectIdentifierText.setEditable(false);
					summaryobjectIdentifierText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
					summaryPreviosDocText.setEditable(false);
					summaryPreviosDocText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
					addExampleBtn.setEnabled(false);
					deleteExampleBtn.setEnabled(false);
					addConstraintBtn.setEnabled(false);
					editConstraintBtn.setEnabled(false);
					deleteConstraintBtn.setEnabled(false);
					addBusinessElementBtn.setEnabled(false);
					deleteBusinessElementBtn.setEnabled(false);
//					btBtn.setEnabled(false);
				}else if (status.equals("Provisionally Registered")) {
					addBusinessElementBtn.setEnabled(true);
					deleteBusinessElementBtn.setEnabled(true);
					isDerivedCheckButton.setEnabled(true);
					beNameText.setEditable(true);
					beDocText.setEditable(true);
					beMinOccursText.setEditable(true);
					beMaxOccursText.setEditable(true); 
					beObjectIdentifierText.setEditable(true);
				}
			}
		}
	}
	
	@Override
	public void setDirty(boolean dirty) {
		if (dirty && businessComponentVO.getEcoreBusinessComponent() != null 
				&& RegistrationStatus.Registered.getStatus().equals(businessComponentVO.getEcoreBusinessComponent().getRegistrationStatus())) {
			super.setDirty(false);
		} else {
			super.setDirty(dirty);
		}
	}
}
