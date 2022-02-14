package org.cufir.plugin.mr.editor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.cufir.plugin.mr.MrHelper;
import org.cufir.plugin.mr.bean.ButtonPolicy;
import org.cufir.plugin.mr.bean.ComboPolicy;
import org.cufir.plugin.mr.bean.MrTreeItem;
import org.cufir.plugin.mr.bean.ObjTypeEnum;
import org.cufir.plugin.mr.bean.RegistrationStatusEnum;
import org.cufir.plugin.mr.bean.TextPolicy;
import org.cufir.plugin.mr.bean.TransferDataBean;
import org.cufir.plugin.mr.bean.TreeLevelEnum;
import org.cufir.plugin.mr.handlers.SaveHandler;
import org.cufir.plugin.mr.utils.ImgUtil;
import org.cufir.plugin.mr.utils.SystemUtil;
import org.cufir.s.data.bean.EcoreBusinessComponent;
import org.cufir.s.data.bean.EcoreBusinessComponentRL;
import org.cufir.s.data.bean.EcoreBusinessElement;
import org.cufir.s.data.bean.EcoreConstraint;
import org.cufir.s.data.bean.EcoreDataType;
import org.cufir.s.data.bean.EcoreExample;
import org.cufir.s.data.bean.EcoreMessageBuildingBlock;
import org.cufir.s.data.bean.EcoreMessageComponent;
import org.cufir.s.data.bean.EcoreMessageDefinition;
import org.cufir.s.data.bean.EcoreMessageSet;
import org.cufir.s.data.bean.EcoreSemanticMarkupElement;
import org.cufir.s.data.vo.EcoreBusinessComponentVO;
import org.cufir.s.data.vo.EcoreBusinessElementVO;
import org.cufir.s.data.vo.EcoreTreeNode;
import org.cufir.s.data.vo.SynonymVO;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.springframework.util.StringUtils;

import com.cfets.cufir.s.ide.utils.i18n.I18nApi;

/**
 * Business Component （业务组）编辑和显示
 * @author jiangqiming_het,gongyi_tt
 *
 */
public class BusinessComponentEditor extends MrMultiPageEditor {

	private java.util.List<EcoreConstraint> css = MrRepository.get().ecoreConstraints;
	private java.util.List<EcoreMessageBuildingBlock> mbbs =  MrRepository.get().ecoreMessageBuildingBlocks;
	private java.util.List<EcoreMessageComponent> mcs = MrRepository.get().ecoreMessageComponents;
	
	private Map<String, EcoreBusinessComponent> bcMap = MrRepository.get().ecoreBusinessComponentMapById;
	private Map<String, java.util.List<EcoreExample>> eMap = MrRepository.get().ecoreExampleMapByObjId;
	private Map<String, java.util.List<EcoreConstraint>> cMap = MrRepository.get().ecoreConstraintMapByObjId;
	private Map<String, java.util.List<EcoreBusinessElement>> beMap = MrRepository.get().ecoreBusinessElementMapByBusinessComponentId;
	private Map<String, EcoreDataType> dtMap = MrRepository.get().ecoreDataTypeMapById;
	
	//查看的数据
//	private EcoreBusinessComponentVO businessComponentVO;
	private String businessComponentId;
	private Tree elementsTree;
	private Label noteLabel;
	// -----------For Summary Page -----------
	private Text nameText, documentationText, previosDocText, objectIdentifierText,
			summaryRemovalDate;
	private Combo summaryStatusCombo;
	private List examplesList;
	private Table summaryConstraintsTable;
	private TreeItem treeRoot;

	// ----------- For Content Page -------------
	private Text beNameText, beDocText, beMinOccursText, beMaxOccursText, beTypeText, beObjectIdentifierText;
	private Button isDerivedCheckButton,addExamplesBtn,deleteExamplesBtn,addConstraintBtn,editConstraintBtn,deleteConstraintBtn,
	addBusinessElementBtn,deleteBusinessElementBtn,addSynonymsBtn,deleteButtonForSynonyms,btBtn;
	private Table beConstraintsTable;
	private Table beSynonymsTable;
	private TreeItem selectedTableItem;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	// ------------For All--------------
	// 基本数据类型
	private String dataType;
	public TreeItem modelExploreTreeItem;
	private MrEditorInput myEditorInput;
	
	// Use for Impact Page 
	private java.util.List<EcoreMessageComponent> messageComponentList = new ArrayList<>();
	// Use for Impact Page
	private java.util.List<EcoreMessageDefinition> msgDefinitionList = new ArrayList<>();
	// Use for Impact Page
	private java.util.List<EcoreMessageSet> msgSetList = new ArrayList<>();
	// Use for Incoming Association Page
	private java.util.List<EcoreBusinessComponent> imcomingAssociationList = new ArrayList<>();
	// Use for Version/Subsets Page
	private java.util.List<EcoreBusinessComponent> bizComponentNextVersionList;
	// children business components on Summary page
	private EcoreTreeNode inheritanceRootEcoreTreeNode = null;
	
	private EcoreBusinessComponent bc = null;

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		this.setSite(site);
		this.setInput(input);
		this.setPartProperty("customName", "bizComponentCreate");
		this.myEditorInput = (MrEditorInput) input;
		this.dataType = this.myEditorInput.getTransferDataBean().getType();
		modelExploreTreeItem = this.myEditorInput.getTransferDataBean().getTreeListItem();
		if(modelExploreTreeItem.getParentItem().getText().equals("Properties")) {
			modelExploreTreeItem = modelExploreTreeItem.getParentItem().getParentItem();
		}
		//ctrl + s保存必须要
		setContext(site);
		
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
			bc = bcMap.get(businessComponentId);
			if (bc != null) {
				messageComponentList = MrHelper.findMessageComponentByTrace(mcs, businessComponentId);
				msgDefinitionList = MrImplManager.get().getEcoreMessageDefinitionImpl().findByMsgComponents(messageComponentList);//MrHelper.getMessageDefinitionList(messageComponentList);
				msgSetList = MrImplManager.get().getEcoreMessageSetImpl().findByMsgDefinitions(msgDefinitionList);//MrHelper.getMessageSetList(msgDefinitionList);
				imcomingAssociationList = MrImplManager.get().getEcoreBusinessComponentImpl().findByBusinessElementTypeId(businessComponentId);//MrHelper.getIncomingAssociationsList(businessComponentId);
				java.util.List<String> nextVersionIds = MrImplManager.get().getEcoreNextVersionsImpl().findNextVersionIdById(businessComponentId);
				bizComponentNextVersionList = MrHelper.getBusinessComponentListByIds(nextVersionIds);
				//inheritance树
				inheritanceRootEcoreTreeNode = MrImplManager.get().getEcoreBusinessComponentImpl().findBusinessComponentInheritances(businessComponentId);	//DerbyDaoUtil.getSubBusinessComponentById(businessComponentId);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void createPages() {
		createSummaryPage();
		createContentPage();
		createImpactPage();
		createIncomingAssociationsPage();
//		createVersionSubset();
		changeStatus();
		
		// 设置标题 myEditorInput.getName()
		// 不为null代表是打开的,可编辑
		if (bc != null) {
			this.setPartName(bc.getName());
			this.setDirty(false);
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

	public void changeStatus() {
		if (bc==null) {
			addBusinessElementBtn.setEnabled(true);
			deleteBusinessElementBtn.setEnabled(true);
		}else {
			if (RegistrationStatusEnum.Registered.getStatus().equals(bc.getRegistrationStatus())) {
				//注册状态为已注册，则内容不可编辑
				nameText.setEditable(false);
				nameText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
				documentationText.setEditable(false);
				documentationText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
				objectIdentifierText.setEditable(false);
				objectIdentifierText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
				previosDocText.setEditable(false);
				previosDocText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
				addExamplesBtn.setEnabled(false);
				deleteExamplesBtn.setEnabled(false);
				addConstraintBtn.setEnabled(false);
				editConstraintBtn.setEnabled(false);
				deleteConstraintBtn.setEnabled(false);
				addBusinessElementBtn.setEnabled(false);
				deleteBusinessElementBtn.setEnabled(false);
			}else if (RegistrationStatusEnum.Provisionally.getStatus().equals(bc.getRegistrationStatus())) {
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
	
	
	/**
	 * Summary Page 显示和编辑
	 */
	void createSummaryPage() {
		// 是否可编辑
		boolean isEditable = true;
		if (bc != null && RegistrationStatusEnum.Registered.getStatus().equals(bc.getRegistrationStatus())) {
			isEditable = !RegistrationStatusEnum.Registered.getStatus().equals(bc.getRegistrationStatus());
		}	
		Composite parentComposite = getContainer();
		ScrolledComposite scrolledComposite = new ScrolledComposite(parentComposite, SWT.V_SCROLL);
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(composite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		composite.setLayout(new FillLayout());
		int index = addPage(scrolledComposite);
		setPageText(index, I18nApi.get("editor.label.sy"));
		
		Composite summaryComposite = new Composite(composite, SWT.NONE);
		summaryComposite.setBounds(5, 5, 850, 1250);
		summaryComposite.layout();

		// ================= General Information Group Begin =======================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 5, I18nApi.get("editor.title.gi")));

		String name = bc == null ? "" : bc.getName();
		nameText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 45, "Name", isEditable, name)).getText();
		nameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				// 左边的树的名称展示
				setPartName(nameText.getText());
				modelExploreTreeItem.setText(nameText.getText()==null?"":nameText.getText());
				
				if(treeRoot != null) {
					treeRoot.setText(nameText.getText());
				}
				setDirty(true);
			}
		});
		
		String documentation = bc == null ? "" : bc.getDefinition();
		documentationText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_SCROLL_TYPE, 73, "Documentation", isEditable, documentation, new MrEditorModifyListener(this))).getText();
		
		String previousDoc = bc == null ? "" : bc.getPreviousVersion();
		previosDocText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_SCROLL_TYPE, 278, "Previous Version's Documentation", isEditable, previousDoc, new MrEditorModifyListener(this))).getText();

		// ================= General Information Group End ============================

		// ================= CMP Information Group Begin ==============================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 503, I18nApi.get("editor.title.cmp")));
		
		// 对象标识符
		String objectIdentifier = bc == null ? "" : bc.getObjectIdentifier();
		objectIdentifierText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 543, "Object Identifier", isEditable, objectIdentifier, new MrEditorModifyListener(this))).getText();
		// ================= CMP Information Group End ==============================

		// ===== Registration Information Group Begin ===============================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 593, I18nApi.get("editor.title.ri")));
		
		//状态
		java.util.List<String> list = new ArrayList<>();
		list.add(RegistrationStatusEnum.Registered.getStatus());
		list.add(RegistrationStatusEnum.Provisionally.getStatus());
		list.add(RegistrationStatusEnum.Added.getStatus());
		
		String statusText = "";
		if (bc != null) {
			statusText = bc.getRegistrationStatus() == null ? "" : bc.getRegistrationStatus();
		} else {
			statusText = RegistrationStatusEnum.Added.getStatus();
		}
		summaryStatusCombo = new SummaryRowComboComposite(summaryComposite, new ComboPolicy(ComboPolicy.COMBO_COMMONLY_TYPE, 633,"Registration Status", isEditable, list, statusText)).getCombo();
		String removalDate = bc == null || bc.getRemovalDate() == null ? "" : DateFormat.getInstance().format(bc.getRemovalDate());
		
		summaryRemovalDate = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_SHORT_TYPE, 661, "Removal Date", isEditable, removalDate, new MrEditorModifyListener(this))).getText();

		// ===== Registration Information Group End ===============================

		// ==== Inheritance Group Begin ===========================================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 711, I18nApi.get("editor.title.ie")));
		
		Tree inheritanceTree = new Tree(summaryComposite, SWT.BORDER);
		inheritanceTree.setBounds(10, 756, 445, 260);
		inheritanceTree.layout();
		if(bc != null) {
			inheritanceRootEcoreTreeNode.setLevel(TreeLevelEnum.LEVEL_1.getLevel());
			treeRoot = new TreeItem(inheritanceTree, SWT.NONE);
			treeRoot.setText(inheritanceRootEcoreTreeNode.getName());
			treeRoot.setImage(ImgUtil.createImg(ImgUtil.BC));
			if (inheritanceRootEcoreTreeNode.getChildNodes()!=null) {
				populateInheritanceTree(inheritanceTree, treeRoot,inheritanceRootEcoreTreeNode.getChildNodes());
			}
			treeRoot.setExpanded(true);
			
			inheritanceTree.addListener(SWT.MouseDoubleClick, new Listener() {

				@Override
				public void handleEvent(Event event) {
					CTabFolder cTabFolder = MrRepository.get().tabFolder;
					Tree firstTabTree = MrRepository.get().businessModelTabTree;
					TreeItem bcTreeItem = firstTabTree.getItem(1);
					TreeItem treeItem = inheritanceTree.getSelection()[0];
					EcoreTreeNode ecoreTreeNodeOfInheritance = (EcoreTreeNode)treeItem.getData("EcoreTreeNode");
					for (TreeItem bcItem: bcTreeItem.getItems()) {
						EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)bcItem.getData("EcoreTreeNode");
						if (ecoreTreeNode.getId().equals(ecoreTreeNodeOfInheritance.getId())) {
							TransferDataBean transferDataBean = setTransferData(ecoreTreeNode, bcItem.getData("type") + "", 
									bcItem.getData("childId") == null ? "" : bcItem.getData("childId") + "", ImgUtil.BC, (MrTreeItem)bcItem);
							cTabFolder.setSelection(0);
							firstTabTree.setSelection(bcItem);
							EditorUtil.open(transferDataBean.getType(), transferDataBean);
							return;
						}
					}
				}
			});
		}
		// ==== Inheritance Group End ===========================================

		// =================== Exampel Group Begin ==============================
		
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 1041, I18nApi.get("editor.title.es")));
		
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_NOTE_TYPE, 1081, "In this section, you can define examples."));
		
		addExamplesBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR),isEditable)).getButton();
		addExamplesBtn.setBounds(10, 1109, 30, 30);
		addExamplesBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MrEditorDialogCreator.createExamplesDialogue(examplesList);
				setDirty(true);
			}
		});

		deleteExamplesBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT, ImgUtil.createImg(ImgUtil.DELETE_IMG_YES),isEditable)).getButton();
		deleteExamplesBtn.setBounds(42, 1109, 30, 30);
		deleteExamplesBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MrEditorDialogCreator.deleteExamplesDialogue(examplesList);
				setDirty(true);
			}
		});

		examplesList = new List(summaryComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		examplesList.setBounds(10, 1144, 600, 100);
		GridData examplesListGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		examplesListGridData.widthHint = 500;
		examplesListGridData.heightHint = 100;
		examplesList.setLayoutData(examplesListGridData);
		java.util.List<EcoreExample> es = eMap.get(businessComponentId);
		if(es != null && es.size() > 0) {
			examplesList.removeAll();
			Map<String,String>map=new HashMap<String, String>();
			for (EcoreExample e : es) {
				map.put(e.getExample(),e.getId());
				examplesList.add(e.getExample());
			}
			examplesList.setData("map", map);
		}
		// =================== Exampel Group End ==============================

		// ===================== Constraints Group Begin====================================

		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 1271, I18nApi.get("editor.title.cs")));
		
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_NOTE_TYPE, 1311, "All the constraints contained in this object (other constraints - such as constraints defined on type - may also apply)."));


		addConstraintBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT, ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR),isEditable)).getButton();
		addConstraintBtn.setBounds(10, 1339, 30, 30);
		addConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MrEditorDialogCreator.createConstraintDialogue("add", "1", summaryConstraintsTable, null, dataType);
				setDirty(true);
			}
		});

		editConstraintBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT, ImgUtil.createImg(ImgUtil.EDIT_IMG_TOOLBAR),isEditable)).getButton();
		editConstraintBtn.setBounds(42, 1339, 30, 30);
		editConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				// 不选中不触发任何效果
				if (summaryConstraintsTable.getSelectionCount() > 0) {
					MrEditorDialogCreator.createConstraintDialogue("edit", "1", summaryConstraintsTable, null, dataType);
					setDirty(true);
				}
			}
		});

		deleteConstraintBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT, ImgUtil.createImg(ImgUtil.DELETE_IMG_YES),isEditable)).getButton();
		deleteConstraintBtn.setBounds(74, 1339, 30, 30);
		deleteConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MrEditorDialogCreator.deleteConstraint("1", summaryConstraintsTable, null, dataType);
				setDirty(true);
			}
		});
		
		summaryConstraintsTable = new Table(summaryComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		summaryConstraintsTable.setHeaderVisible(true);
		summaryConstraintsTable.setLinesVisible(true);
		summaryConstraintsTable.setBounds(10, 1374, 600, 150);
		summaryConstraintsTable.layout();
		new SummaryTableComposite(summaryConstraintsTable, 149, "Name");
		new SummaryTableComposite(summaryConstraintsTable, 149, "Definiton");
		new SummaryTableComposite(summaryConstraintsTable, 149, "Expression Language");
		new SummaryTableComposite(summaryConstraintsTable, 149, "Expression");
		// 处理Constraint
		summaryConstraintsTable.removeAll();
		java.util.List<EcoreConstraint> cs = cMap.get(businessComponentId);
		if (cs != null && cs.size() > 0) {
			for (EcoreConstraint ecoreConstraint : cs) {
				TableItem tableItem = new TableItem(summaryConstraintsTable, SWT.NONE);
				tableItem.setText(new String[] { ecoreConstraint.getName(), ecoreConstraint.getDefinition(),
						ecoreConstraint.getExpression(), ecoreConstraint.getExpressionlanguage() });
			}
		}

		// ===================== Constraints Group End======================================

		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	private void populateInheritanceTree(Tree inheritanceTree, TreeItem treeRoot, 
			java.util.List<EcoreTreeNode> childEcoreTreeNodeList) {
		
		if (childEcoreTreeNodeList==null) {
			return;
		} 
		
		for (EcoreTreeNode ecoreTreeNode: childEcoreTreeNodeList) {
			
 			TreeItem treeItem = new TreeItem(treeRoot, SWT.NONE);
			treeItem.setText(ecoreTreeNode.getName());
			treeItem.setImage(ImgUtil.createImg(ImgUtil.BC));
			treeItem.setData("EcoreTreeNode", ecoreTreeNode);
			if(bc.getName().equals(ecoreTreeNode.getName())) {
				inheritanceTree.setSelection(treeItem);
			}
			if (ecoreTreeNode.getChildNodes()!=null) {
			this.populateInheritanceTree(inheritanceTree, treeItem, ecoreTreeNode.getChildNodes());		
			}
		}
	}
	
	/**
	 * 创建横竖滚动框
	 * @return
	 */
	private ScrolledComposite createContentComposite() {
		Composite parentComposite = getContainer();
		ScrolledComposite scrolledComposite = new ScrolledComposite(parentComposite, SWT.V_SCROLL | SWT.H_SCROLL);
		scrolledComposite.setBounds(parentComposite.getShell().getBounds());
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		int index = addPage(scrolledComposite);
		setPageText(index, I18nApi.get("editor.label.ct"));
		return scrolledComposite;
	}
	
	/**
	 * Content Page 显示和编辑
	 */
	void createContentPage() {
		
		ScrolledComposite scrolledComposite = createContentComposite();
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(composite);
		
		Composite leftComposite = new Composite(composite, SWT.BORDER);
		leftComposite.setLayout(new FillLayout(SWT.VERTICAL));
		leftComposite.setBounds(0, 0, 500, 730);
		leftComposite.layout();
		
		Composite rightComposite = new Composite(composite, SWT.BORDER);
		rightComposite.setLayout(new FillLayout(SWT.VERTICAL));
		rightComposite.setBounds(510, 0, 500, 1000);
		rightComposite.layout();
		
		// ------------ Content Expand Bar Begin ----------------------------------
		Composite leftCom = new Composite(leftComposite, SWT.NONE);
		leftCom.setBounds(0, 5, 490, 730);
		leftCom.layout();
		
		new SummaryRowTextComposite(leftCom, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 0, I18nApi.get("editor.title.ct")));
		
		addBusinessElementBtn = new SummaryRowTextComposite(leftCom, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT, ImgUtil.createImg(ImgUtil.MC_ADD_ELEMENT))).getButton();
		addBusinessElementBtn.setBounds(5, 35, 30, 30);
		addBusinessElementBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				addBusinessComponetElementSelectionDialogue(rightComposite,elementsTree);
	
			}

			private void addBusinessComponetElementSelectionDialogue(Composite rightComposite, Tree elementsTree) {
				for (Control control: rightComposite.getChildren()) {
					control.dispose();
				}
				// ------------ Business Element Details Begin ------------------------
				new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 0, I18nApi.get("editor.title.begi")));
				
				Text nameText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 45, "Name")).getText();

				Text docText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SCROLL_TYPE, 73, "Documentation")).getText();
				
				Text minText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SHORT_TYPE, 228, "Min Occurs")).getText();
				
				Text maxOccursText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SHORT_TYPE, 256, "Max Occurs")).getText();
				
				Button isTechnicalButton = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_CHECK, 284, "Is Technical", false)).getButton();
				
				Text typeText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_BUTTON_SELECT_TYPE, 312, "Type", false, "")).getText();
				
				Button typeButton = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_PUSH_DEEP,"choose")).getButton();
				typeButton.setBounds(390, 312, 50, 25);
				typeButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseUp(MouseEvent e) {
						try {
							MrEditorDialogCreator.createSelectBusinessElementType(typeText);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});

				Button finishButton = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_PUSH_DEEP,"Finish")).getButton();
				finishButton.setBounds(280, 362, 60, 30);
				finishButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseUp(MouseEvent e) {
						TreeItem treeItem = new TreeItem(elementsTree, SWT.NONE);
						//type = 1 ---> messageElement
						String nodeName;
						String min = StringUtils.isEmpty(minText.getText()) ? "0" : minText.getText();
						String max = StringUtils.isEmpty(maxOccursText.getText()) ? "*" : minText.getText();
						nodeName = nameText.getText() + " [" + min + ", " + max + "]:" + typeText.getText();
						treeItem.setText(nodeName);
						treeItem.setData("id",treeItem.getData("id")==null?UUID.randomUUID():treeItem.getData("id").toString());
						treeItem.setData("beNameText", nameText.getText());
						treeItem.setData("beDocText", docText.getText());
						treeItem.setData("beMinOccursText", minText.getText());
						treeItem.setData("beMaxOccursText", maxOccursText.getText());
						treeItem.setData("isDerivedCheckButton", isTechnicalButton.getSelection());
						treeItem.setData("beType_id", String.valueOf(typeText.getData("beType_id")));
						treeItem.setData("beType", String.valueOf(typeText.getData("beType")));
						treeItem.setData("beStatusCombo", String.valueOf(RegistrationStatusEnum.Added.getStatus()));
						treeItem.setData("beTypeText", typeText.getText());
						treeItem.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
						// 用Message Component里的元素，构造Message Component树
						new MrEditorTreeCreator().generateContainmentTreeForBComponent(treeItem,
								MrHelper.getBizElementsByBizComponentId(String.valueOf(typeText.getData("beType_id"))),true);
						for (Control control: rightComposite.getChildren()) {
							control.dispose();
						}
						if(noteLabel != null) {
							noteLabel.dispose();
						}
					}
				});
				Button cancelButton = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_PUSH_DEEP,"Cancel")).getButton();
				cancelButton.setBounds(350, 362, 60, 30);
				cancelButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseUp(MouseEvent e) {
						for (Control control: rightComposite.getChildren()) {
							control.dispose();
						}
					}
				});
			}
		});

		deleteBusinessElementBtn = new SummaryRowTextComposite(leftCom, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT, ImgUtil.createImg(ImgUtil.DELETE_IMG_YES))).getButton();
		deleteBusinessElementBtn.setBounds(42, 35, 30, 30);
		deleteBusinessElementBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TreeItem[] selection = elementsTree.getSelection();
				if (selection != null && selection.length > 0) {
					TreeItem item = selection[0];
					item.dispose();
					setDirty(true);
					
					for (Control control: rightComposite.getChildren()) {
						control.dispose();
					}
					if(bc != null && !RegistrationStatusEnum.Added.getStatus().equals(bc.getRegistrationStatus())) {
						TreeItem[] items = elementsTree.getItems();
						if(items == null || items.length < 1) {
							noteLabel = new Label(elementsTree, SWT.NONE);
							noteLabel.setText("该组件下无业务元素");
							noteLabel.setBounds(10, 5, 200, 30);
							noteLabel.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
						}
					}
				}
			}
		});
		
		elementsTree = new Tree(leftCom, SWT.BORDER | SWT.SINGLE);
		elementsTree.setBounds(5, 70, 485, 650);
		elementsTree.layout();
		
		java.util.List<EcoreBusinessElement> bes = beMap.get(businessComponentId);
		if(bes != null) {
			java.util.List<EcoreSemanticMarkupElement> smes = MrImplManager.get().getEcoreSemanticMarkupElementImpl().findBySynonymSemanticMarkupObjId(businessComponentId);
			java.util.List<SynonymVO> svos = new ArrayList<SynonymVO>();
			HashMap<String, Map<String, String>> mkeMap = new HashMap<String, Map<String, String>>();
			for(EcoreSemanticMarkupElement sme : smes) {
				if (mkeMap.containsKey(sme.getSemanticMarkupId())) {
					Map<String, String> mp = mkeMap.get(sme.getSemanticMarkupId());
					if (mp != null && !mp.isEmpty()) {
						mp.put(sme.getName(), sme.getValue());
					}
				} else {
					Map<String, String> mp = new HashMap<String, String>();
					mp.put(sme.getName(), sme.getValue());
					mkeMap.put(sme.getSemanticMarkupId(), mp);
				}
			}
			if (mkeMap != null && !mkeMap.isEmpty()) {
				for (String eid : mkeMap.keySet()) {
					Map<String, String> mt = mkeMap.get(eid);
					SynonymVO v = new SynonymVO();
					v.setObjId(businessComponentId);
					v.setContext(mt.get("context"));
					v.setSynonym(mt.get("value"));
					svos.add(v);
				}
			}
			if(bes != null && bes.size() > 0) {
				for (EcoreBusinessElement ecoreBusinessElement : bes) {
					String typeName = "";
					if("1".equals(ecoreBusinessElement.getType())) {
						EcoreDataType ecoreDataType = dtMap.get(ecoreBusinessElement.getTypeId());
						if(ecoreDataType != null) {
							typeName = ecoreDataType.getName();
						}
					}else {
						EcoreBusinessComponent ecoreBusinessComponent = bcMap.get(ecoreBusinessElement.getTypeId());
						if(ecoreBusinessComponent != null) {
							typeName = ecoreBusinessComponent.getName();
						}
					}
					
					TreeItem tableItem = new TreeItem(elementsTree, SWT.NONE);
					tableItem.setImage(ImgUtil.createImg(ImgUtil.BC_BE));
					String min = ecoreBusinessElement.getMinOccurs() == null ? "0" : ecoreBusinessElement.getMinOccurs() + "";
					String max = ecoreBusinessElement.getMaxOccurs() == null ? "*" : ecoreBusinessElement.getMaxOccurs() + "";
					tableItem.setText(ecoreBusinessElement.getName() + " [" + min + ", " + max + "] : " + typeName);
					tableItem.setData("id", ecoreBusinessElement.getId());
					tableItem.setData("beNameText", ecoreBusinessElement.getName());
					tableItem.setData("beDocText", ecoreBusinessElement.getDefinition());
					tableItem.setData("beMinOccursText", ecoreBusinessElement.getMinOccurs());
					tableItem.setData("beMaxOccursText", ecoreBusinessElement.getMaxOccurs());
					tableItem.setData("isDerivedCheckButton", ecoreBusinessElement.getIsDerived());
					tableItem.setData("beTypeText", typeName);
					tableItem.setData("beType_id", ecoreBusinessElement.getTypeId());
					tableItem.setData("beType", ecoreBusinessElement.getType());
					tableItem.setData("beObjectIdentifierText", ecoreBusinessElement.getObjectIdentifier());
					tableItem.setData("beStatusCombo", ecoreBusinessElement.getRegistrationStatus());
					
					java.util.List<String[]> synonymsTableItems = new ArrayList<String[]>();
					if (svos != null && svos.size() > 0) {
						for (SynonymVO synonymVO : svos) {
							String[] str = new String[2];
							str[0] = synonymVO.getContext();
							str[1] = synonymVO.getSynonym();
							synonymsTableItems.add(str);
						}
					}
					tableItem.setData("beSynonymsTableItems", synonymsTableItems);
					
					java.util.List<EcoreConstraint> ecoreConstraints = cMap.get(businessComponentId);
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
					new MrEditorTreeCreator().generateContainmentTreeForBComponent(tableItem,
							MrHelper.getBizElementsByBizComponentId(ecoreBusinessElement.getTypeId()),true);
				}
				//默认打开
				TreeItem item = elementsTree.getItem(0);
				if(item != null) {
					elementsTree.setSelection(item);
					elementsTreeListener(item, rightComposite);
				}
			}
		}else {
			if(bc != null && !RegistrationStatusEnum.Added.getStatus().equals(bc.getRegistrationStatus())) {
				noteLabel = new Label(elementsTree, SWT.NONE);
				noteLabel.setText("该组件下无业务元素");
				noteLabel.setBounds(10, 5, 200, 30);
				noteLabel.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
			}
		}
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		// ------------ Content Expand Bar End ----------------------------------
		
		elementsTree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 选中可操作
				rightComposite.setEnabled(true);
				TreeItem item = (TreeItem) e.item;
				elementsTreeListener(item, rightComposite);
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void elementsTreeListener(TreeItem item, Composite rightComposite) {
		selectedTableItem = item;
		showBusinessElementDetailBar(item, rightComposite);
		beSynonymsTable.removeAll();
		java.util.List<String[]> synonymsTableItems = (java.util.List<String[]>) item.getData("beSynonymsTableItems");
		if (synonymsTableItems != null && synonymsTableItems.size() > 0) {
			for (int i = 0; i < synonymsTableItems.size(); i++) {
				TableItem tableItem = new TableItem(beSynonymsTable, SWT.NONE);
				tableItem.setText(synonymsTableItems.get(i));
			}
		}

		beConstraintsTable.removeAll();
		java.util.List<String[]> constraintsTableItems = (java.util.List<String[]>) item.getData("beConstraintsTableItems");
		if (constraintsTableItems != null && constraintsTableItems.size() > 0) {
			for (int i = 0; i < constraintsTableItems.size(); i++) {
				TableItem tableItem = new TableItem(beConstraintsTable, SWT.NONE);
				tableItem.setText(constraintsTableItems.get(i));
			}
		}
		if (bc!=null) {
			if (RegistrationStatusEnum.Registered.getStatus().equals(bc.getRegistrationStatus())) {
			btBtn.setEnabled(false);
			}
			//临时逻辑，根据bc注册状态控制整个页面编辑状态(待优化至当可编辑时只允许第一层可编辑)
			if(RegistrationStatusEnum.Added.getStatus().equals(bc.getRegistrationStatus())||bc.getRegistrationStatus()==null){
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
		setPageText(index, I18nApi.get("editor.label.it"));
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
		messageSetsGroup.setText(I18nApi.get("editor.title.ms"));
		messageSetsGroup.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		messageSetsGroup.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		messageSetsGroup.setLayoutData(groupGridData);
		messageSetsGroup.setLayout(new FillLayout(SWT.VERTICAL));
		Table messageSetTable = new Table(messageSetsGroup, SWT.BORDER | SWT.V_SCROLL);
		messageSetTable.setLinesVisible(true);
		TableCursor messageSetTableCursor = new TableCursor(messageSetTable, SWT.NONE);
		// 构造Message Set表格
		MrEditorTableCreator.generateMessageSetTableOfImpactPage(messageSetTable, msgSetList);

		Group messageDefinitionsGroup = new Group(groupListComposite, SWT.NONE);
		messageDefinitionsGroup.setText(I18nApi.get("editor.title.md"));
		messageDefinitionsGroup.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		messageDefinitionsGroup.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		messageDefinitionsGroup.setLayoutData(groupGridData);
		messageDefinitionsGroup.setLayout(new FillLayout(SWT.VERTICAL));
		Table messageDefinitionTable = new Table(messageDefinitionsGroup, SWT.BORDER | SWT.V_SCROLL);
		messageDefinitionTable.setLinesVisible(true);
		TableCursor messageDefinitionTableCursor = new TableCursor(messageDefinitionTable, SWT.NONE);
		// 构造Message Definition表格
		MrEditorTableCreator.generateMessageDefinationTableOfImpactPage(messageDefinitionTable, msgDefinitionList);

		Group messageComponentGroup = new Group(groupListComposite, SWT.NONE);
		messageComponentGroup.setText(I18nApi.get("editor.title.mc"));
		messageComponentGroup.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		messageComponentGroup.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		messageComponentGroup.setLayoutData(groupGridData);
		messageComponentGroup.setLayout(new FillLayout(SWT.VERTICAL));
		Table messageComponentTable = new Table(messageComponentGroup, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
		messageComponentTable.setLinesVisible(true);
		TableCursor msgComponentTableCursor = new TableCursor(messageComponentTable, SWT.NONE);
		// 构造Message Component表格
		MrEditorTableCreator.generateMessageComponentTableOfImpactPage(messageComponentTable, messageComponentList);

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
				MrEditorTreeCreator mrEditorTreeCreator = new MrEditorTreeCreator();
				mrEditorTreeCreator.generateContainmentTreeForMessageSetofImpactPage(treeRoot, MrImplManager.get().getEcoreMessageDefinitionImpl().findByMsgSetId(messageSetId));
				// 生成Message Set的约束
				java.util.List<EcoreConstraint> ecs = MrImplManager.get().getEcoreConstraintImpl().findByObjId(messageSetId);
				mrEditorTreeCreator.addConstraintsNode(treeRoot, ecs, null, null);
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
				MrEditorTreeCreator mrEditorTreeCreator = new MrEditorTreeCreator();
				mrEditorTreeCreator.generateContainmentTreeForMessageDefinition(treeRoot, MrHelper.findMsgBuildingBlocksByMsgId(mbbs, msgDefinitionId));
				// 生成 Message Definition 约束
				java.util.List<EcoreConstraint> ecs = MrImplManager.get().getEcoreConstraintImpl().findByObjId(msgDefinitionId);
				mrEditorTreeCreator.addConstraintsNode(treeRoot, ecs, null, null);
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
				MrEditorTreeCreator mrEditorTreeCreator = new MrEditorTreeCreator();
				mrEditorTreeCreator.generateContainmentTreeForMessageComponent(treeRoot, MrHelper.getMsgElementList(msgComponentId));
				// 生成  Message component 约束
				java.util.List<EcoreConstraint> ecs = MrHelper.findConstraintsByObjId(css, msgComponentId);
				mrEditorTreeCreator.addConstraintsNode(treeRoot, ecs, null, null);
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
		setPageText(index, I18nApi.get("editor.label.ia"));
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

		GridData elementTableGridData = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		elementTableGridData.widthHint = 500;
		Table elementTable = new Table(composite, SWT.BORDER);
		elementTable.setLinesVisible(true);
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
				generateContainmentTreeForIncomingAssociationPage(treeRoot, MrHelper.getBizElementsByBizComponentId(bizComponentId));
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
		java.util.List<EcoreDataType> dts = MrImplManager.get().getEcoreDataTypeImpl().findAll();
		for (EcoreBusinessElement bizElement: bizElementList) {
			TreeItem treeItem = new TreeItem(treeRoot, SWT.NONE);
			String min = bizElement.getMinOccurs() == null ? "0" : bizElement.getMinOccurs() + "";
			String max = bizElement.getMaxOccurs() == null ? "*" : bizElement.getMaxOccurs() + "";
			String elementDisplayName = bizElement.getName() + " [" + min + ", " + max + "]";
			// 数据类型 1：datatype，2：消息组件
			if ("2".equals(bizElement.getType())) {
				EcoreBusinessComponent bizComponent = MrHelper.getBusinessComponentById(bizElement.getTypeId());
				treeItem.setText(elementDisplayName + " : " + bizComponent.getName());
			} else {
				// 数据类型 1：datatype
				EcoreDataType dataType = MrHelper.getDataTypeById(dts, bizElement.getTypeId());
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
		setPageText(index, I18nApi.get("editor.label.vs"));
		
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
		
		ExpandBar nextVersionBar = new SummaryExpandComposite(leftComposite, 0).getExpandBar();
		
		Composite nextVersionBarComposite = new Composite(nextVersionBar, SWT.NONE);
		GridData nextVersionBarCompositeGridData = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		nextVersionBarComposite.setLayout(new GridLayout(1, false));
		nextVersionBarComposite.setLayoutData(nextVersionBarCompositeGridData);
		Label leftDescriptionLabel = new Label(nextVersionBarComposite, SWT.WRAP);
		leftDescriptionLabel.setText("Shows the new versions of this repository itme.");
		List versionListWidget = new List(nextVersionBarComposite, SWT.BORDER);
		GridData versionListGridData = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
		versionListGridData.widthHint = 500;
		versionListWidget.setLayoutData(versionListGridData);
		if(this.bizComponentNextVersionList != null && this.bizComponentNextVersionList.size() > 0) {
			this.generateNextVersionList(versionListWidget);
		}
		
		new SummaryExpandComposite(nextVersionBar, nextVersionBarComposite, I18nApi.get("editor.title.nv"), 450);
		
		ExpandBar previousVersionBar = new SummaryExpandComposite(rightComposite, 0).getExpandBar();
		
		Composite previousVersionComposite = new Composite(previousVersionBar, SWT.NONE);
		previousVersionComposite.setLayout(new GridLayout(1, false));
		Label rightDescriptionLabel = new Label(previousVersionComposite, SWT.WRAP);
		rightDescriptionLabel.setText("Shows the previous version of this repository item.");
		Label previousVersionLabel = new Label(previousVersionComposite, SWT.WRAP);
		previousVersionLabel.setText("The previous version of this object is: ");
		Link previousVersionLink = new Link(previousVersionComposite, SWT.NONE);
		EcoreBusinessComponent previousBizComponent = MrHelper.getPreviousVersionForBizComponent(businessComponentId);
		if (previousBizComponent.getName() != null) {
			previousVersionLink.setText(previousBizComponent.getName());
			previousVersionLink.setData(previousBizComponent.getId());
		}

		new SummaryExpandComposite(previousVersionBar, previousVersionComposite, I18nApi.get("editor.title.pv"), 450);
	}
	
	private void generateNextVersionList(List versionListWidget) {
		for (int index = 0; index < this.bizComponentNextVersionList.size(); index++) {
			versionListWidget.add(bizComponentNextVersionList.get(index).getName());
			versionListWidget.setData(String.valueOf(index), bizComponentNextVersionList.get(index).getId());
		}
	}
	
	private void showBusinessElementDetailBar(TreeItem item, Composite rightComposite) {
		// ------------ Business Element Details Begin ------------------------
		for (Control control: rightComposite.getChildren()) {
			control.dispose();
		}
		
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 5, I18nApi.get("editor.title.bed")));
		
		String beName = item.getData("beNameText") == null ? "" : item.getData("beNameText") + "";
		beNameText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 45, "Name", false, beName)).getText();
		beNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTableItem.isDisposed()) {
					// 左边的树的名称展示
					String showText = "";
					if (selectedTableItem.getData("codeType")==null) {
						String min = StringUtils.isEmpty(beMinOccursText.getText()) ? "0" : beMinOccursText.getText();
						String max = StringUtils.isEmpty(beMaxOccursText.getText()) ? "*" : beMaxOccursText.getText();
						showText = beNameText.getText() + " [" + min + ", " + max + "] : " + beTypeText.getText() ;
						selectedTableItem.setText(showText);
					}
					selectedTableItem.setData("beNameText", beNameText.getText());
					setDirty(true);
				}
			}
		});

		String beDoc = item.getData("beDocText") == null ? "" : item.getData("beDocText") + "";
		beDocText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SCROLL_TYPE, 73, "Documentation", false, beDoc)).getText();
		beDocText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTableItem.isDisposed()) {
					selectedTableItem.setData("beDocText", beDocText.getText());
					setDirty(true);
				}
			}
		});

		String beMinOccurs = item.getData("beMinOccursText") == null ? "" : item.getData("beMinOccursText") + "";
		beMinOccursText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SHORT_TYPE, 228, "Min Occurs", false, beMinOccurs)).getText();
		beMinOccursText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTableItem.isDisposed()) {
					String showText = "";
					if (selectedTableItem.getData("codeType")==null) {
						String min = StringUtils.isEmpty(beMinOccursText.getText()) ? "0" : beMinOccursText.getText();
						String max = StringUtils.isEmpty(beMaxOccursText.getText()) ? "*" : beMaxOccursText.getText();
						showText = beNameText.getText() + " [" + min + ", " + max + "] : " + beTypeText.getText() ;
						selectedTableItem.setText(showText);
					}
					selectedTableItem.setData("beMinOccursText", beMinOccursText.getText());
					setDirty(true);
				}
			}
		});

		
		String beMaxOccurs = item.getData("beMaxOccursText") == null ? "" : item.getData("beMaxOccursText") + "";
		beMaxOccursText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SHORT_TYPE, 256, "Max Occurs", false, beMaxOccurs)).getText();
		beMaxOccursText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTableItem.isDisposed()) {
					String showText = "";
					if (selectedTableItem.getData("codeType")==null) {
						String min = StringUtils.isEmpty(beMinOccursText.getText()) ? "0" : beMinOccursText.getText();
						String max = StringUtils.isEmpty(beMaxOccursText.getText()) ? "*" : beMaxOccursText.getText();
						showText = beNameText.getText() + " [" + min + ", " + max + "] : " + beTypeText.getText() ;
						selectedTableItem.setText(showText);
					}
						
					selectedTableItem.setData("beMaxOccursText", beMaxOccursText.getText());
					setDirty(true);
				}
			}
		});
		
		boolean isDerivedCheck = item.getData("isDerivedCheckButton") == null ? false : Boolean.getBoolean("" + item.getData("isDerivedCheckButton"));
		isDerivedCheckButton = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_CHECK, 284, "Is Derived", isDerivedCheck)).getButton();
		isDerivedCheckButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (!selectedTableItem.isDisposed()) {
					selectedTableItem.setData("isDerivedCheckButton", isDerivedCheckButton.getSelection());
					setDirty(true);
				}
			}
		});

		String beType = item.getData("beTypeText") == null ? "" : item.getData("beTypeText") + "";
		beTypeText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_BUTTON_SELECT_TYPE, 312, "Type", false, beType)).getText();
		beTypeText.setData("beType_id", item.getData("beType_id")==null ? "":item.getData("beType_id"));
		beTypeText.setData("beType", item.getData("beType")==null ? "":item.getData("beType"));
		beTypeText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (selectedTableItem != null && !selectedTableItem.isDisposed()) {
					selectedTableItem.setData("beTypeText", beTypeText.getText());
					String showText = "";
					if (selectedTableItem.getData("codeType")==null) {
						String min = StringUtils.isEmpty(beMinOccursText.getText()) ? "0" : beMinOccursText.getText();
						String max = StringUtils.isEmpty(beMaxOccursText.getText()) ? "*" : beMaxOccursText.getText();
						showText = beNameText.getText() + " [" + min + ", " + max + "] : " + beTypeText.getText() ;
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
		btBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_DEEP,"choose")).getButton();
		btBtn.setBounds(390, 312, 50, 25);
		btBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MrEditorDialogCreator.createSearchDialogue(beTypeText,selectedTableItem);
				setDirty(true);
			}
		});
		
		Button btSearchBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_DEEP,ImgUtil.createImg(ImgUtil.SEARCH_BTN))).getButton();
		btSearchBtn.setBounds(445, 312, 25, 25);
		btSearchBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (elementsTree.getSelectionCount() == 1) {
					TreeItem treeItem = elementsTree.getSelection()[0];
					String dataType = (String)treeItem.getData("beType");
					CTabFolder cTabFolder =MrRepository.get().tabFolder;
					Tree businessModelTabTree = MrRepository.get().businessModelTabTree;
					if ("2".equals(dataType)) {
						String Id = beTypeText.getData("beType_id").toString();
						TreeItem mcTreeItem = businessModelTabTree.getItem(1);
						for (TreeItem mcItem: mcTreeItem.getItems()) {
							EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)mcItem.getData("EcoreTreeNode");
							if (beTypeText.getData("meLever")!=null) {
								String meComponentId = (String) beTypeText.getData("beType_id");
								if (ecoreTreeNode.getId().equals(meComponentId)) {
									TransferDataBean transferDataBean = setTransferData(ecoreTreeNode, mcItem.getData("type") + "", 
											mcItem.getData("childId") == null ? "" : mcItem.getData("childId") + "", ImgUtil.BC, (MrTreeItem)mcItem);
									businessModelTabTree.setSelection(mcItem);
									EditorUtil.open(transferDataBean.getType(), transferDataBean);
									return;
								}
							}else if (ecoreTreeNode.getId().equals(Id)) {
								TransferDataBean transferDataBean = setTransferData(ecoreTreeNode, mcItem.getData("type") + "", 
										mcItem.getData("childId") == null ? "" : mcItem.getData("childId") + "", ImgUtil.BC, (MrTreeItem)mcItem);
								businessModelTabTree.setSelection(mcItem);
								EditorUtil.open(transferDataBean.getType(), transferDataBean);
								return;
							}
						}
					}else {
						String dataTypeId = (String)treeItem.getData("beTypeTextId");
						TreeItem dataTypeTreeItem = businessModelTabTree.getItem(0);
						TreeItem[] allTypeItemArray = dataTypeTreeItem.getItems();
						for (TreeItem oneTypeTreeItem: allTypeItemArray) {
							for (TreeItem typeTreeItem: oneTypeTreeItem.getItems()) {
								EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)typeTreeItem.getData("EcoreTreeNode");
								String imgPath = "";
								if("1".equals(ecoreTreeNode.getObjType())){
									imgPath = ImgUtil.CODE_SET_SUB1_2;
								}else if("2".equals(ecoreTreeNode.getObjType())) {
									imgPath = ImgUtil.CODE_SET_SUB1;
								}
								if (beTypeText.getData("meLever")!=null) {
									String meComponentId = (String) beTypeText.getData("beType_id");
									if (ecoreTreeNode.getId().equals(meComponentId)) {
										TransferDataBean transferDataBean = setTransferData(ecoreTreeNode, typeTreeItem.getData("type") + "", 
												typeTreeItem.getData("childId") == null ? "" : typeTreeItem.getData("childId") + "", imgPath, (MrTreeItem)typeTreeItem);
										cTabFolder.setSelection(0);
										businessModelTabTree.setSelection(typeTreeItem);
										EditorUtil.open(transferDataBean.getType(), transferDataBean);
										return;
									}
								}else if (ecoreTreeNode.getId().equals(dataTypeId)) {
									TransferDataBean transferDataBean = setTransferData(ecoreTreeNode, typeTreeItem.getData("type") + "", 
											typeTreeItem.getData("childId") == null ? "" : typeTreeItem.getData("childId") + "", imgPath, (MrTreeItem)typeTreeItem);
									cTabFolder.setSelection(0);
									businessModelTabTree.setSelection(typeTreeItem);
									EditorUtil.open(transferDataBean.getType(), transferDataBean);
									return;
								}
							}
						}
					}
				}
			}
		});
		// ------------ Business Element Details End ------------------------

		// -----------------------CMP Information Group Begin ------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 362, I18nApi.get("editor.title.cmp")));
		
		String beObjectIdentifier = item.getData("beObjectIdentifierText") == null ? "" : item.getData("beObjectIdentifierText") + "";
		beObjectIdentifierText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 402, "Object Identifier", false, beObjectIdentifier)).getText();
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
		// -----------------------CMP Information Group End -----------------------

		// ---------- Synonyms Expand Item Begin -------------------------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 452, I18nApi.get("editor.title.ss")));

		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_NOTE_ONE_TYPE, 492, "This section show the synonyms of object"));

		addSynonymsBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR),false)).getButton();
		addSynonymsBtn.setBounds(10, 520, 30, 30);
		addSynonymsBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MrEditorDialogCreator.createSynonymsDialogue(beSynonymsTable, selectedTableItem);
				setDirty(true);
			}
		});

		deleteButtonForSynonyms = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES),false)).getButton();
		deleteButtonForSynonyms.setBounds(42, 520, 30, 30);
		deleteButtonForSynonyms.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MrEditorDialogCreator.deleteSynonymDialogue(beSynonymsTable, selectedTableItem);
				setDirty(true);
			}
		});

		beSynonymsTable = new Table(rightComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		beSynonymsTable.setHeaderVisible(true);
		beSynonymsTable.setLinesVisible(true);
		beSynonymsTable.setBounds(10, 555, 480, 150);
		beSynonymsTable.layout();

		new SummaryTableComposite(beSynonymsTable, 238, "Context");
		new SummaryTableComposite(beSynonymsTable, 238, "Synonums");
		// ---------- Synonyms Expand Item End -------------------------------------

		// ---------- Constraints Expand Item Begin -------------------------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 710, I18nApi.get("editor.title.cs")));

		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_NOTE_TWO_TYPE, 750, "All the constraints contained in this object(other constraints - such as constraints"
				+ "\r\n defined on type - may also apply)."));

		Button addConstraintBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR),false)).getButton();
		addConstraintBtn.setBounds(10, 805, 30, 30);
		addConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MrEditorDialogCreator.createConstraintDialogueForBC("add", beConstraintsTable, selectedTableItem);
				setDirty(true);
			}
		});

		Button editConstraintBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.EDIT_IMG_TOOLBAR),false)).getButton();
		editConstraintBtn.setBounds(42, 805, 30, 30);
		editConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (beConstraintsTable.getSelectionCount() > 0) {
					MrEditorDialogCreator.createConstraintDialogueForBC("edit", beConstraintsTable, selectedTableItem);
					setDirty(true);
				}
			}
		});

		Button deleteConstraintBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES),false)).getButton();
		deleteConstraintBtn.setBounds(74, 805, 30, 30);
		deleteConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MrEditorDialogCreator.deleteConstraintForBC(beConstraintsTable, selectedTableItem);
				setDirty(true);
			}
		});

		beConstraintsTable = new Table(rightComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		beConstraintsTable.setHeaderVisible(true);
		beConstraintsTable.setLinesVisible(true);
		beConstraintsTable.setBounds(10, 840, 480, 150);
		beConstraintsTable.layout();
		new SummaryTableComposite(beConstraintsTable, 113, "Name");
		new SummaryTableComposite(beConstraintsTable, 113, "Definition");
		new SummaryTableComposite(beConstraintsTable, 137, "Expression Language");
		new SummaryTableComposite(beConstraintsTable, 113, "Expression");
		// ---------- Constraints Expand Item End -------------------------------------
	}
	
	private TransferDataBean setTransferData(EcoreTreeNode ecoreTreeNode, String type, 
			String childId, String imgPath, MrTreeItem mrTreeItem) {
		TransferDataBean transferDataBean = new TransferDataBean();
		transferDataBean.setId(ecoreTreeNode.getId());
		transferDataBean.setName(ecoreTreeNode.getName());
		transferDataBean.setLevel(ecoreTreeNode.getLevel());
		transferDataBean.setType(type);
		transferDataBean.setChildId(childId);
		transferDataBean.setImgPath(imgPath);
		transferDataBean.setTreeListItem(mrTreeItem);
		return transferDataBean;
	}
	
	/**
	 * 获取页面所有的值
	 */
	@SuppressWarnings("unchecked")
	public EcoreBusinessComponentVO getAllData() {
		EcoreBusinessComponentVO businessComponentVO = new EcoreBusinessComponentVO();
		EcoreBusinessComponent businessComponent = new EcoreBusinessComponent();
		businessComponent.setId(businessComponentId);
		businessComponent.setName(nameText.getText());
		businessComponent.setDefinition(documentationText.getText());
		businessComponent.setPreviousVersion(previosDocText.getText());
		businessComponent.setObjectIdentifier(objectIdentifierText.getText());
		businessComponent.setRegistrationStatus(summaryStatusCombo.getText());
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

		// 获取examples信息
		java.util.List<EcoreExample> examples = new ArrayList<EcoreExample>();
		String[] exampleItems = this.examplesList.getItems();
		Map<String,String> map=(Map<String, String>) examplesList.getData("map");
		if (exampleItems != null && exampleItems.length > 0) {
			for (String str : exampleItems) {
				EcoreExample bcExample = new EcoreExample();
				if (map!=null) {
					bcExample.setId(map.get(str)==null?UUID.randomUUID().toString():map.get(str));
					}else {
						bcExample.setId(UUID.randomUUID().toString());
					}
				bcExample.setExample(str);
				bcExample.setObjId(businessComponent.getId());
				bcExample.setObjType(ObjTypeEnum.BusinessComponent.getType());
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
			ecoreBusinessElement.setName(String.valueOf(beTableItem.getData("beNameText") == null ? "" : beTableItem.getData("beNameText")));
			ecoreBusinessElement.setDefinition(String.valueOf(beTableItem.getData("beDocText") == null ? "" : beTableItem.getData("beDocText")));
			ecoreBusinessElement.setIsDerived("" + (beTableItem.getData("isDerivedCheckButton") == null ? false : Boolean.getBoolean("" + beTableItem.getData("isDerivedCheckButton"))));
			ecoreBusinessElement.setIsMessageAssociationEnd("");
			Object minOccurs = beTableItem.getData("beMinOccursText");
			Object maxOccurs = beTableItem.getData("beMaxOccursText");
			ecoreBusinessElement.setMinOccurs(beTableItem.getData("beMinOccursText") == null || "".equals(minOccurs) ? null : Integer.valueOf(minOccurs + ""));
			ecoreBusinessElement.setMaxOccurs(beTableItem.getData("beMaxOccursText") == null || "".equals(maxOccurs) ? null : Integer.valueOf(maxOccurs + ""));
			ecoreBusinessElement.setObjectIdentifier(String.valueOf(beTableItem.getData("beObjectIdentifierText") == null ? "" : beTableItem.getData("beObjectIdentifierText")));
			ecoreBusinessElement.setPreviousVersion("");
			// ---------------------------确认一下------------------------
			ecoreBusinessElement.setType(String.valueOf(beTableItem.getData("beType") == null ? "" : beTableItem.getData("beType")));
			ecoreBusinessElement.setTypeName(String.valueOf(beTableItem.getData("beTypeText") == null ? "" : beTableItem.getData("beTypeText")));
			ecoreBusinessElement.setTypeId(String.valueOf(beTableItem.getData("beType_id") == null ? "" : beTableItem.getData("beType_id")));
			// be基本信息
			businessElementVO.setEcoreBusinessElement(ecoreBusinessElement);

			java.util.List<String[]> constraintsTableItems = (java.util.List<String[]>) beTableItem
					.getData("beConstraintsTableItems");
			java.util.List<EcoreConstraint> beEcoreConstraintList = new ArrayList<EcoreConstraint>();
			if (constraintsTableItems != null && constraintsTableItems.size() > 0) {
				for (int j = 0; j < constraintsTableItems.size(); j++) {
					EcoreConstraint beConstraint = new EcoreConstraint();
					beConstraint.setId(beTableItem.getData("id")==null?UUID.randomUUID().toString():beTableItem.getData("id").toString());
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
		if(modelExploreTreeItem.getItems() != null && modelExploreTreeItem.getItems().length != 0) {
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
				ecoreBusinessComponentRL.setPId(businessComponentId);
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
					ecoreBusinessComponentRL.setPId(id);
				}else {
				ecoreBusinessComponentRL.setPId(superTypesItems[i].getData("id").toString());
				}
				ecoreBusinessComponentRL.setId(businessComponentId);
				superTypesBusinessComponentRLList.add(ecoreBusinessComponentRL);
			}
			businessComponentVO.setParentBusinessComponent(
					(superTypesBusinessComponentRLList == null || superTypesBusinessComponentRLList.size() == 0) ? null
							: superTypesBusinessComponentRLList.get(0));
		}
		return businessComponentVO;
	}
	
	@Override
	public void setDirty(boolean dirty) {
		if (dirty && bc != null && RegistrationStatusEnum.Registered.getStatus().equals(bc.getRegistrationStatus())) {
			super.setDirty(false);
		} else {
			super.setDirty(dirty);
		}
	}
}
