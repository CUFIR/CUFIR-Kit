package com.cfets.cufir.plugin.mr.editor;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
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
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import com.cfets.cufir.plugin.mr.bean.TransferDataBean;
import com.cfets.cufir.plugin.mr.enums.ObjTypeEnum;
import com.cfets.cufir.plugin.mr.enums.RegistrationStatus;
import com.cfets.cufir.plugin.mr.handlers.SaveHandler;
import com.cfets.cufir.plugin.mr.listener.TypeButtonForMessageDefinition;
import com.cfets.cufir.plugin.mr.utils.ContextServiceUtil;
import com.cfets.cufir.plugin.mr.utils.DerbyDaoUtil;
import com.cfets.cufir.plugin.mr.utils.EditorModifyListener;
import com.cfets.cufir.plugin.mr.utils.GenerateCommonTree;
import com.cfets.cufir.plugin.mr.utils.GenerateExampleAndConstraintAndSynonyms;
import com.cfets.cufir.plugin.mr.utils.ImgUtil;
import com.cfets.cufir.plugin.mr.utils.MessageBoxUtil;
import com.cfets.cufir.plugin.mr.utils.SystemUtil;
import com.cfets.cufir.plugin.mr.view.ChangeShellLocation;
import com.cfets.cufir.s.data.bean.EcoreBusinessArea;
import com.cfets.cufir.s.data.bean.EcoreCode;
import com.cfets.cufir.s.data.bean.EcoreConstraint;
import com.cfets.cufir.s.data.bean.EcoreExample;
import com.cfets.cufir.s.data.bean.EcoreMessageBuildingBlock;
import com.cfets.cufir.s.data.bean.EcoreMessageDefinition;
import com.cfets.cufir.s.data.bean.EcoreMessageElement;
import com.cfets.cufir.s.data.vo.EcoreBusinessAreaVO;
import com.cfets.cufir.s.data.vo.EcoreTreeNode;

/**
 * BusinessArea（业务领域）新建，编辑
 * TODO -> BusinessAreaEditor
 * @author gongyi_tt
 *
 */
public class BusinessAreaCreateEditor extends MultiPageEditorParent {

	private MyEditorInput myEditorInput;

	private TreeItem modelExploreTreeItem;

	private EcoreBusinessArea businessArea;

	private ArrayList<EcoreMessageDefinition> messageDefinitionList;

	private EcoreTreeNode ecoreTreeNode;

	// -------------for Summary Page use-----------------
	private Text nameValue;
	private Text documentationValue;
	private Text bizAreaCodeValue;
	private Text objectIdentifierValue;
	private ComboViewer statusComboViewer;
	private Text titleValue;
	private Text subTitleValue;
	private Text publishingDateValue;
	private Table exampleTable;
	private Table constraintsTable;

	// -----------For Content Page ------------
	private Tree elementsTree;
	private Text xmlTagValueOfContentPage;
	private Set<String> msgDefinitionIdIncluded = new TreeSet<>();
	private Text mbbNameValue;
	private Text mbbDocumentationValue;
	private Text mbbMinOccursValue;
	private Text mbbMaxOccursValue;
	private Text mbbObjectIdentifierValue;
	private Table mbbSynonymsTable;
	private Text typeValue;
	private Table mbbConstraintsTable;
	private Text constraintNameValue;
	private Text constraintDocValue;
	private Text expressionValue;
	private Text expressionLanguageValue;
	private Text codeDetailNameValue;
	private Text codeDocumentValue;
	private Text codeNameValue;

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		this.setSite(site);
		this.setInput(input);
		this.setPartProperty("customName", "businessAreaCreate");
		this.myEditorInput = (MyEditorInput) input;

		modelExploreTreeItem = this.myEditorInput.getTransferDataBean().getTreeListItem();

		ecoreTreeNode = (EcoreTreeNode) modelExploreTreeItem.getData("EcoreTreeNode");

		// 设置标题图片
//		this.setTitleImage(ImgUtil.createImg(this.myEditorInput.getTransferDataBean().getImgPath()));

		ContextServiceUtil.setContext(site);

	}

	@Override
	public void setPartName(String partName) {
		super.setPartName(partName);
	}

	/**
	 * 获取businessArea，为了在页面展示
	 */
	private void loadBusinessAreaData() {
		TransferDataBean transferBean = this.myEditorInput.getTransferDataBean();
		String businessAreaId = transferBean.getId();
		this.setPartProperty("ID", businessAreaId);
		businessArea = DerbyDaoUtil.getBusinessAreaByBizAreaId(businessAreaId);
		messageDefinitionList = DerbyDaoUtil.getmessageDefinitionListByBizAreaId(businessAreaId);
	}

	@Override
	protected void createPages() {

		loadBusinessAreaData();
		if (businessArea.getId() == null) {
			businessArea.setId(this.myEditorInput.getTransferDataBean().getId());
			this.setPartName("");
			this.setDirty(true);
		} else {
			this.setPartName(businessArea.getName());
		}

		if (RegistrationStatus.Provisionally.getStatus().equals(this.ecoreTreeNode.getRegistrationStatus())
				|| RegistrationStatus.Added.getStatus().equals(this.ecoreTreeNode.getRegistrationStatus())) {
			this.setDirty(true);
		} else if (RegistrationStatus.Registered.getStatus().equals(this.ecoreTreeNode.getRegistrationStatus())) {
			this.setDirty(false);
		}

		createSummaryPage();
		createContentPage();

	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		SaveHandler sh = new SaveHandler();
		try {
			sh.execute(null);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	public EcoreBusinessArea getBusinessArea() {
		return businessArea;
	}

	public java.util.List<EcoreBusinessAreaVO> getEcoreMessageDefinitionVOList() {

		businessArea.setName(this.nameValue.getText());
		businessArea.setDefinition(this.documentationValue.getText());
		businessArea.setObjectIdentifier(objectIdentifierValue.getText());
		businessArea.setRegistrationStatus(statusComboViewer.getCombo().getText());
		businessArea.setCode(this.bizAreaCodeValue.getText());

		java.util.List<EcoreBusinessAreaVO> ecoreBusinessAreaVOList = new ArrayList<>();
		EcoreBusinessAreaVO ecoreBusinessAreaVO = new EcoreBusinessAreaVO();
		ecoreBusinessAreaVO.setEcoreBusinessArea(businessArea);
		ecoreBusinessAreaVO.setEcoreExamples(this.getEcoreExampleTable(businessArea));
		ecoreBusinessAreaVO.setEcoreConstraints(getEcoreConstraintList(businessArea));
		ecoreBusinessAreaVO
				.setEcoreMessageDefinitionIds(this.msgDefinitionIdIncluded.stream().collect(Collectors.toList()));

		ecoreBusinessAreaVOList.add(ecoreBusinessAreaVO);
		return ecoreBusinessAreaVOList;
	}

	private ArrayList<EcoreExample> getEcoreExampleTable(EcoreBusinessArea businessArea) {
		ArrayList<EcoreExample> ecoreExampleList = new ArrayList<>();
		for (TableItem tableItem : this.exampleTable.getItems()) {
			EcoreExample ecoreExample = (EcoreExample) tableItem.getData();
			ecoreExample.setObj_id(businessArea.getId());
			ecoreExample.setObj_type(ObjTypeEnum.MessageSet.getType());
			ecoreExampleList.add(ecoreExample);
		}
		return ecoreExampleList;
	}

	private ArrayList<EcoreConstraint> getEcoreConstraintList(EcoreBusinessArea businessArea) {
		ArrayList<EcoreConstraint> ecoreConstraintList = new ArrayList<>();
		for (TableItem tableItem : this.constraintsTable.getItems()) {
			if (tableItem.getData() != null) {
				EcoreConstraint ecoreConstraint = (EcoreConstraint) tableItem.getData();
				ecoreConstraint.setObj_id(businessArea.getId());
				ecoreConstraint.setObj_type(ObjTypeEnum.MessageSet.getType());
				ecoreConstraintList.add(ecoreConstraint);
			}
		}

		return ecoreConstraintList;
	}

	/*
	 * Summary page的显示
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

		if ("Registered".equals(this.ecoreTreeNode.getRegistrationStatus())) {
			composite.setEnabled(false);
		}

		// ================= General Information Group Begin =======================

		ExpandBar summaryBar = new ExpandBar(composite, SWT.NONE);
		summaryBar.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		summaryBar.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		summaryBar.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));

		Composite generalInformationComposite = new Composite(summaryBar, SWT.NONE);
		generalInformationComposite.setLayout(new GridLayout(2, false));

		Label nameLabel = new Label(generalInformationComposite, SWT.NONE);
		nameLabel.setText("Name");
		GridData nameLabelGridData = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		nameLabelGridData.widthHint = 300;
		nameLabel.setLayoutData(nameLabelGridData);
		nameValue = new Text(generalInformationComposite, SWT.BORDER);
		nameValue.setText((businessArea == null || businessArea.getName() == null) ? "" : businessArea.getName());
		GridData nameValueGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		nameValueGridData.widthHint = 850;
		nameValue.setLayoutData(nameValueGridData);
		nameValue.addModifyListener(new EditorModifyListener(this, this.modelExploreTreeItem));

		Label documentationLabel = new Label(generalInformationComposite, SWT.NONE);
		documentationLabel.setLayoutData(nameLabelGridData);
		documentationLabel.setText("Documentation");
		documentationValue = new Text(generalInformationComposite, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		GridData docValueGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		docValueGridData.widthHint = 650;
		docValueGridData.heightHint = 250;
		documentationValue.setLayoutData(docValueGridData);
		documentationValue.setText(
				(businessArea == null || businessArea.getDefinition() == null) ? "" : businessArea.getDefinition());
		documentationValue.addModifyListener(new EditorModifyListener(this));

		Label bizAreaCodeLabel = new Label(generalInformationComposite, SWT.NONE);
		bizAreaCodeLabel.setText("Business Area Code");
		bizAreaCodeLabel.setLayoutData(nameLabelGridData);
		bizAreaCodeValue = new Text(generalInformationComposite, SWT.BORDER);
		bizAreaCodeValue.setLayoutData(nameValueGridData);
		bizAreaCodeValue
				.setText((businessArea == null || businessArea.getCode() == null) ? "" : businessArea.getCode());
		bizAreaCodeValue.addModifyListener(new EditorModifyListener(this));

		ExpandItem generalInformationItem = new ExpandItem(summaryBar, SWT.NONE);
		generalInformationItem.setText("General Information");
		generalInformationItem.setHeight(350);
		generalInformationItem.setExpanded(true);
		generalInformationItem.setControl(generalInformationComposite);

		// ================= General Information Group End ============================

		// ================= CMP Information Group Begin ==============================

		Composite cmpComposite = new Composite(summaryBar, SWT.NONE);
		cmpComposite.setLayout(new GridLayout(2, false));

		Label objectIdentifierLabel = new Label(cmpComposite, SWT.NONE);
		objectIdentifierLabel.setLayoutData(nameLabelGridData);
		objectIdentifierLabel.setText("Object Identifier");
		objectIdentifierValue = new Text(cmpComposite, SWT.BORDER);
		objectIdentifierValue.setLayoutData(nameValueGridData);
		objectIdentifierValue.setText((businessArea == null || businessArea.getObjectIdentifier() == null) ? ""
				: businessArea.getObjectIdentifier());
		objectIdentifierValue.addModifyListener(new EditorModifyListener(this));

		ExpandItem cmpItem = new ExpandItem(summaryBar, SWT.NONE);
		cmpItem.setText("CMP Information");
		cmpItem.setHeight(100);
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
		statusComboViewer = new ComboViewer(registrationComposite, SWT.NONE);
		statusComboViewer.add(RegistrationStatus.Registered.getStatus());
		statusComboViewer.add(RegistrationStatus.Provisionally.getStatus());
		statusComboViewer.add(RegistrationStatus.Added.getStatus());
		Combo statusCombo = statusComboViewer.getCombo();
		statusCombo.setLayoutData(valueGridData);
		if (businessArea.getRegistrationStatus() == null) {
			statusCombo.select(2);
		} else {
			statusCombo.setText(businessArea.getRegistrationStatus());
		}
		statusCombo.setEnabled(false);

		ExpandItem registrationItem = new ExpandItem(summaryBar, SWT.NONE);
		registrationItem.setText("Registration Information");
		registrationItem.setHeight(100);
		registrationItem.setExpanded(true);
		registrationItem.setControl(registrationComposite);

		// ===== Registration Information Group End ===============================

		// ========================Documentation Information Begin==================

		Composite docInformationComposite = new Composite(summaryBar, SWT.NONE);
		docInformationComposite.setLayout(new GridLayout(2, false));
		Label titleLable = new Label(docInformationComposite, SWT.NONE);
		titleLable.setLayoutData(nameLabelGridData);
		titleLable.setText("Title");
		titleValue = new Text(docInformationComposite, SWT.BORDER);
		titleValue.setLayoutData(nameValueGridData);

		Label subTitle = new Label(docInformationComposite, SWT.NONE);
		subTitle.setText("Subtitle");
		subTitle.setLayoutData(nameLabelGridData);
		subTitleValue = new Text(docInformationComposite, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		docValueGridData.widthHint = 650;
		docValueGridData.heightHint = 250;
		subTitleValue.setLayoutData(docValueGridData);

		Label publishingDate = new Label(docInformationComposite, SWT.NONE);
		publishingDate.setLayoutData(nameLabelGridData);
		publishingDate.setText("Publishing Date");
		publishingDateValue = new Text(docInformationComposite, SWT.BORDER);
		publishingDateValue.setLayoutData(nameValueGridData);

		ExpandItem docInformationItem = new ExpandItem(summaryBar, SWT.NONE);
		docInformationItem.setText("Documentation Information");
		docInformationItem.setHeight(350);
		docInformationItem.setExpanded(true);
		docInformationItem.setControl(docInformationComposite);
		// ======================Documentation Information End ==================

		// =================== Exampel Group Begin ==============================
		Composite exampleComposite = new Composite(summaryBar, SWT.NONE);
		exampleComposite.setLayout(new GridLayout(1, false));

		Composite exampleToolbarComposite = new Composite(exampleComposite, SWT.NONE);
		exampleToolbarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

		Button addExampleBtn = new Button(exampleToolbarComposite, SWT.NONE);
		addExampleBtn.setImage(ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR));
		addExampleBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MessageBoxUtil.createExampleDialogue(exampleTable);
				setDirty(true);
			}
		});
		Button deleteExampleBtn = new Button(exampleToolbarComposite, SWT.NONE);
		deleteExampleBtn.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
		deleteExampleBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MessageBoxUtil.deleteExampleDialogue(exampleTable);
				setDirty(true);
			}
		});

		exampleTable = new Table(exampleComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		GridData exampleListGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		exampleListGridData.widthHint = 500;
		exampleListGridData.heightHint = 250;
		exampleTable.setLayoutData(exampleListGridData);

		if (this.businessArea != null && this.businessArea.getId() != null) {
			GenerateExampleAndConstraintAndSynonyms.generateExampleTable(exampleTable, this.businessArea.getId());
		}

		ExpandItem exampleItem = new ExpandItem(summaryBar, SWT.NONE);
		exampleItem.setText("Example");
		exampleItem.setHeight(400);
		exampleItem.setExpanded(true);
		exampleItem.setControl(exampleComposite);
		// =================== Exampel Group End ==============================

		// ===================== Contraints Group
		// Begin====================================
		Composite contraintsComposite = new Composite(summaryBar, SWT.NONE);
		contraintsComposite.setLayout(new GridLayout(1, false));

		Composite toolbarComposite = new Composite(contraintsComposite, SWT.NONE);
		toolbarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

		Button addConstraintBtn = new Button(toolbarComposite, SWT.NONE);
		addConstraintBtn.setImage(ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR));
		addConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MessageBoxUtil.createConstraintDialogueForBAandMS("add", constraintsTable);
				setDirty(true);
			}
		});

		Button deleteConstraintBtn = new Button(toolbarComposite, SWT.NONE);
		deleteConstraintBtn.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
		deleteConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MessageBoxUtil.deleteConstraintForBA(constraintsTable);
				setDirty(true);
			}
		});

		constraintsTable = new Table(contraintsComposite, SWT.BORDER);
		constraintsTable.setHeaderVisible(true);
		constraintsTable.setLinesVisible(true);
		TableColumn nameColumn = new TableColumn(constraintsTable, SWT.NONE);
		nameColumn.setText("Name");
		nameColumn.setWidth(150);
		TableColumn DefinitionColumn = new TableColumn(constraintsTable, SWT.NONE);
		DefinitionColumn.setText("Definiton");
		DefinitionColumn.setWidth(150);
		TableColumn ExpressionLanguageColumn = new TableColumn(constraintsTable, SWT.NONE);
		ExpressionLanguageColumn.setText("Expression Language");
		ExpressionLanguageColumn.setWidth(150);
		TableColumn expressionColumn = new TableColumn(constraintsTable, SWT.NONE);
		expressionColumn.setText("Expression");
		expressionColumn.setWidth(150);

		GridData constraintsTableGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		constraintsTableGridData.widthHint = 800;
		constraintsTableGridData.heightHint = 250;
		constraintsTable.setLayoutData(constraintsTableGridData);

		if (this.businessArea != null && this.businessArea.getId() != null) {
			GenerateExampleAndConstraintAndSynonyms.generateConstraintTable(constraintsTable, this.businessArea.getId(),
					null);
		}

		ExpandItem constraintsItem = new ExpandItem(summaryBar, SWT.NONE);
		constraintsItem.setText("Contraints");
		constraintsItem.setHeight(500);
		constraintsItem.setExpanded(true);
		constraintsItem.setControl(contraintsComposite);
		// ===================== Contraints Group
		// End======================================
		// 滚动条/样式
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	/*
	 * Content page的显示
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
		lc_gd.widthHint = 550; // composite.getBounds().width / 3;
		leftComposite.setLayoutData(lc_gd);

		GridData rc_gd = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		rc_gd.widthHint = 500; // composite.getBounds().width / 3;
		Composite rightComposite = new Composite(composite, SWT.NONE);
		rightComposite.setLayout(new FillLayout(SWT.VERTICAL));
		rightComposite.setLayoutData(rc_gd);

		// ------------ Content Expand Bar Begin ----------------------------------
		ExpandBar contentBar = new ExpandBar(leftComposite, SWT.NONE);

		contentBar.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		contentBar.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		contentBar.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));

		Composite contentBarComposite = new Composite(contentBar, SWT.NONE);
		contentBarComposite.setBounds(contentBar.getBounds());
		contentBarComposite.setLayout(new GridLayout(1, false));
		contentBarComposite.setLayoutData(new GridData(550, 750));

		Composite contentToolbarComposite = new Composite(contentBarComposite, SWT.NONE);
		contentToolbarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

		Button addButtonForContent = new Button(contentToolbarComposite, SWT.NONE);
		addButtonForContent.setImage(ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR));
		addButtonForContent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				createMessageBox(DerbyDaoUtil.getMsgDefinitionWithoutBA());
				setDirty(true);
			}
		});
		this.setUnavailable(addButtonForContent);

		Button deleteButtonForContent = new Button(contentToolbarComposite, SWT.NONE);
		deleteButtonForContent.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
		deleteButtonForContent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				for (TreeItem treeItem : elementsTree.getSelection()) {
					if (treeItem.getData("msgDefinitionId") != null) {
						msgDefinitionIdIncluded.remove(treeItem.getData("msgDefinitionId"));
						treeItem.dispose();
					}
				}

				setDirty(true);
			}
		});
		this.setUnavailable(deleteButtonForContent);

		elementsTree = new Tree(contentBarComposite, SWT.BORDER | SWT.SINGLE);
		GridData elementsTreeGridData = new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1);
		elementsTreeGridData.widthHint = 510;
		elementsTreeGridData.heightHint = 750;
		elementsTree.setLayoutData(elementsTreeGridData);

		this.generateContentTree();

		ExpandItem contentItem = new ExpandItem(contentBar, SWT.NONE);
		contentItem.setText("Content");
		contentItem.setHeight(850);
		contentItem.setExpanded(true);
		contentItem.setControl(contentBarComposite);
		// ------------ Content Expand Bar End ----------------------------------

		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		// 鼠标监听事件
		elementsTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TreeItem[] treeItems = elementsTree.getSelection();
				for (TreeItem treeItem : treeItems) {
					String bbid = String.valueOf(treeItem.getData("bbId"));
					String constraintId = String.valueOf(treeItem.getData("constraintId"));
					String msgElementID = String.valueOf(treeItem.getData("msgElementID"));
					String codeId = String.valueOf(treeItem.getData("codeId"));

					if (!bbid.equalsIgnoreCase("null")) {
						showMessageBuildingBlockBar(rightComposite, scrolledComposite);
						EcoreMessageBuildingBlock msgBB = (EcoreMessageBuildingBlock) treeItem.getData("msgBldBlock");
						mbbNameValue.setEditable(false);
						mbbNameValue.setText(msgBB.getName());
						mbbDocumentationValue.setEditable(false);
						mbbDocumentationValue.setText(msgBB.getDefinition());
						mbbMinOccursValue.setEditable(false);
						mbbMinOccursValue.setText(String.valueOf(msgBB.getMinOccurs()));
						mbbMaxOccursValue.setEditable(false);
						mbbMaxOccursValue.setText(String.valueOf(msgBB.getMaxOccurs()));
						xmlTagValueOfContentPage.setEditable(false);
						xmlTagValueOfContentPage.setText(msgBB.getXmlTag() == null ? "" : msgBB.getXmlTag());
						typeValue.setEditable(false);
						typeValue.setText(treeItem.getData("typeName") == null ? ""
								: String.valueOf(treeItem.getData("typeName")));
						mbbObjectIdentifierValue.setEditable(false);
						mbbObjectIdentifierValue
								.setText(msgBB.getObjectIdentifier() == null ? "" : msgBB.getObjectIdentifier());
						GenerateExampleAndConstraintAndSynonyms.generateConstraintTable(mbbConstraintsTable,
								msgBB.getId(), treeItem);
						GenerateExampleAndConstraintAndSynonyms.generateSynonymsTable(mbbSynonymsTable, msgBB.getId(),
								treeItem);

					} else if (!constraintId.equalsIgnoreCase("null")) {
						showConstraintBar(rightComposite, scrolledComposite);
						EcoreConstraint constraint = (EcoreConstraint) treeItem.getData("constraint");
						constraintNameValue.setEditable(false);
						constraintNameValue.setText(constraint.getName() == null ? "" : constraint.getName());
						constraintDocValue.setEditable(false);
						constraintDocValue
								.setText(constraint.getDefinition() == null ? "" : constraint.getDefinition());
						expressionValue.setEditable(false);
						expressionValue.setText(constraint.getExpression() == null ? "" : constraint.getExpression());
						expressionLanguageValue.setEditable(false);
						expressionLanguageValue.setText(
								constraint.getExpressionlanguage() == null ? "" : constraint.getExpressionlanguage());

					} else if (!msgElementID.equalsIgnoreCase("null")) {
						showMessageBuildingBlockBar(rightComposite, scrolledComposite);
						EcoreMessageElement ecoreMessageElement = DerbyDaoUtil.getMessageElementById(msgElementID);
						mbbNameValue.setEditable(false);
						mbbNameValue.setText(ecoreMessageElement.getName());
						mbbDocumentationValue.setEditable(false);
						mbbDocumentationValue.setText(ecoreMessageElement.getDefinition());
						mbbMinOccursValue.setEditable(false);
						mbbMinOccursValue.setText(String.valueOf(ecoreMessageElement.getMinOccurs()));
						mbbMaxOccursValue.setEditable(false);
						mbbMaxOccursValue.setText(String.valueOf(ecoreMessageElement.getMaxOccurs()));
						xmlTagValueOfContentPage.setEditable(false);
						xmlTagValueOfContentPage.setText(String.valueOf(ecoreMessageElement.getXmlTag()));
						typeValue.setEditable(false);
						typeValue.setText(String.valueOf(treeItem.getData("typeName")));
						mbbObjectIdentifierValue.setEditable(false);
						mbbObjectIdentifierValue.setText(ecoreMessageElement.getObjectIdentifier() == null ? ""
								: ecoreMessageElement.getObjectIdentifier());
						GenerateExampleAndConstraintAndSynonyms.generateConstraintTable(mbbConstraintsTable,
								msgElementID, treeItem);
						GenerateExampleAndConstraintAndSynonyms.generateSynonymsTable(mbbSynonymsTable, msgElementID,
								null);

					} else if (!codeId.equalsIgnoreCase("null")) {
						showCodeDetailBar(rightComposite, scrolledComposite);
						EcoreCode ecoreCode = (EcoreCode) treeItem.getData("ecoreCode");
						codeDetailNameValue.setEditable(false);
						codeDetailNameValue.setText(ecoreCode.getName());
						codeDocumentValue.setEditable(false);
						codeDocumentValue.setText(ecoreCode.getDefinition());
						codeNameValue.setEditable(false);
						codeNameValue.setText(ecoreCode.getCodeName());
						GenerateExampleAndConstraintAndSynonyms.generateConstraintTable(mbbConstraintsTable, codeId,
								treeItem);
//						GenerateExampleAndConstraintAndSynonyms.generateSynonymsTable(mbbSynonymsTable, codeId, treeItem);

					} else {
						// 鼠标单击到Message Definition节点时，Content右边全部清楚
						for (Control control : rightComposite.getChildren()) {
							control.dispose();
						}

					}
				}
			}
		});
	}

	// 生成结构树
	private void generateContentTree() {
		if (messageDefinitionList == null) {
			return;
		}
		for (EcoreMessageDefinition msgDefinition : messageDefinitionList) {
			TreeItem treeItem = new TreeItem(elementsTree, SWT.NONE);
			treeItem.setText(msgDefinition.getName());
			treeItem.setData("msgDefinitionId", msgDefinition.getId());
			msgDefinitionIdIncluded.add(msgDefinition.getId());
			if (RegistrationStatus.Registered.getStatus().equals(msgDefinition.getRegistrationStatus())) {
				treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB1));
			} else {
				treeItem.setImage(ImgUtil.createImg(ImgUtil.MESSAGE_DEFINITIONS));
			}
			GenerateCommonTree.generateContentTreeForMessageDefinition(treeItem,
					DerbyDaoUtil.getMsgBuildingBlock(msgDefinition.getId()), null,
					msgDefinition.getRegistrationStatus());
			GenerateCommonTree.addContraintsNode(treeItem, DerbyDaoUtil.getContraints(msgDefinition.getId()), null,
					null);
		}
	}

	void createMessageBox(ArrayList<EcoreMessageDefinition> msgDefinitions) {
		Shell messageDefinitionWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		messageDefinitionWindow.setText("Select Message Definition to Add");
		messageDefinitionWindow.setLayout(new FormLayout());

		// 改变弹窗位置
		ChangeShellLocation.center(messageDefinitionWindow);

		Composite c = new Composite(messageDefinitionWindow, SWT.NONE);
		FormData fd_c = new FormData();
		fd_c.top = new FormAttachment(0);
		fd_c.left = new FormAttachment(0);
		fd_c.bottom = new FormAttachment(100);
		fd_c.right = new FormAttachment(100);
		Text searchText = new Text(c, SWT.BORDER);
		searchText.setBounds(10, 10, 280, 30);
		Button searchBtn = new Button(c, SWT.NONE);
		searchBtn.setText("Search");
		searchBtn.setBounds(300, 10, 50, 30);
		searchBtn.setBackground(new Color(Display.getCurrent(), 135, 206, 250));

		Table msgDefinitionTable = new Table(c, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
		msgDefinitionTable.setLinesVisible(true);

		this.generateAllMsgDefinitionTable(msgDefinitionTable, msgDefinitions);

		msgDefinitionTable.setBounds(10, 50, 350, 400);
		// 鼠标选择时间
		msgDefinitionTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				searchText.setText(msgDefinitionTable.getSelection()[0].getText());
			}
		});

		Button okButton = new Button(c, SWT.PUSH);
		okButton.setText("OK");
		okButton.setBounds(275, 460, 35, 30);
		okButton.setBackground(new Color(Display.getCurrent(), 135, 206, 250));
		okButton.addMouseListener(new OkButtonListener(msgDefinitionTable, messageDefinitionWindow));

		Button cancelButton = new Button(c, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setBounds(320, 460, 52, 30);
		cancelButton.setBackground(new Color(Display.getCurrent(), 135, 206, 250));
		cancelButton.addMouseListener(new CancelButtonListener(messageDefinitionWindow));

		messageDefinitionWindow.setSize(400, 550);
		messageDefinitionWindow.open();
		messageDefinitionWindow.layout();
		// 查询按钮监听事件
		searchBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				msgDefinitionTable.removeAll();
				msgDefinitions.stream()
						.filter(t -> (t.getName() != null
								&& t.getName().toUpperCase().indexOf(searchText.getText().toUpperCase()) != -1))
						.forEach(t -> {
							TableItem tableItem = new TableItem(msgDefinitionTable, SWT.NONE);
							tableItem.setText(t.getName());
							tableItem.setData("msgDefinitionId", t.getId());
							tableItem.setData("registrationStatus", t.getRegistrationStatus());
							if (RegistrationStatus.Registered.getStatus().equals(t.getRegistrationStatus())) {
								tableItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB1));
							} else {
								tableItem.setImage(ImgUtil.createImg(ImgUtil.MESSAGE_DEFINITIONS));
							}
						});
			}
		});
	}

	private void generateAllMsgDefinitionTable(Table msgDefinitionLisTable,
			ArrayList<EcoreMessageDefinition> msgDefinitions) {
		for (EcoreMessageDefinition definition : msgDefinitions) {
			TableItem tableItem = new TableItem(msgDefinitionLisTable, SWT.NONE);
			tableItem.setText(definition.getName());
			tableItem.setData("msgDefinitionId", definition.getId());
			tableItem.setData("registrationStatus", definition.getRegistrationStatus());
			if (RegistrationStatus.Registered.getStatus().equals(definition.getRegistrationStatus())) {
				tableItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB1));
			} else {
				tableItem.setImage(ImgUtil.createImg(ImgUtil.MESSAGE_DEFINITIONS));
			}
		}
		msgDefinitionLisTable.setSelection(0);
	}

	class OkButtonListener extends MouseAdapter {

		private Table table;

		private Shell shell;

		public OkButtonListener(Table table, Shell shell) {
			this.table = table;
			this.shell = shell;
		}

		@Override
		public void mouseUp(MouseEvent e) {
			TableItem[] tableItemArray = table.getSelection();
			for (TableItem tableItem : tableItemArray) {
				String msgDefinitionId = String.valueOf(tableItem.getData("msgDefinitionId"));
				String registrationStatus = String.valueOf(tableItem.getData("registrationStatus"));
				// 已包含，不必重复加入到Message set中
				if (msgDefinitionIdIncluded.contains(msgDefinitionId)) {
					continue;
				} else {
					TreeItem treeNode = new TreeItem(elementsTree, SWT.NONE);
					treeNode.setText(tableItem.getText());
					if (RegistrationStatus.Registered.getStatus().equals(registrationStatus)) {
						treeNode.setImage(ImgUtil.createImg(ImgUtil.MD_SUB1));
					} else {
						treeNode.setImage(ImgUtil.createImg(ImgUtil.MESSAGE_DEFINITIONS));
					}
					treeNode.setData("msgDefinitionId", msgDefinitionId);
					// 构造一个以Message Definiton 为根节点的树
					GenerateCommonTree.generateContentTreeForMessageDefinition(treeNode,
							DerbyDaoUtil.getMsgBuildingBlock(msgDefinitionId), null, registrationStatus);
					GenerateCommonTree.addContraintsNode(treeNode, DerbyDaoUtil.getContraints(msgDefinitionId), null,
							null);

					// 暂存Message Definition ID, 为保存Message Set用
					msgDefinitionIdIncluded.add(msgDefinitionId);
				}
			}
			shell.close();
		}
	}

	class CancelButtonListener extends MouseAdapter {

		private Shell shell;

		public CancelButtonListener(Shell shell) {
			this.shell = shell;
		}

		@Override
		public void mouseUp(MouseEvent e) {
			shell.close();
		}
	}

	// 根据注册状态设置编辑状态
	private void setUnavailable(Widget swtWidget) {
		if ("Registered".equals(this.ecoreTreeNode.getRegistrationStatus())) {
			if (swtWidget instanceof Text) {
				((Text) swtWidget).setEditable(false);
			} else if (swtWidget instanceof Button) {
				((Button) swtWidget).setEnabled(false);
			}
		}
	}

	// 设置操作状态 -> *
	@Override
	public void setDirty(boolean dirty) {
		if (dirty && "Registered".equals(this.ecoreTreeNode.getRegistrationStatus())) {
			return;
		}
		super.setDirty(dirty);
	}

	private void showMessageBuildingBlockBar(Composite composite, ScrolledComposite scrolledComposite) {

		for (Control control : composite.getChildren()) {
			control.dispose();
		}

		// ------------ Message Building block Details Begin ------------------------
		ExpandBar msgBldBlockBar = new ExpandBar(composite, SWT.V_SCROLL);
		msgBldBlockBar.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		msgBldBlockBar.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		msgBldBlockBar.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));

		Composite generalInformationComposite = new Composite(msgBldBlockBar, SWT.NONE);
		generalInformationComposite.setLayout(new GridLayout(2, false));

		GridData generalInforGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		generalInforGridData.widthHint = 350;
		Label beNameLabel = new Label(generalInformationComposite, SWT.NONE);
		beNameLabel.setText("Name");
		mbbNameValue = new Text(generalInformationComposite, SWT.BORDER);
		mbbNameValue.setData("hint", "name");
		mbbNameValue.setLayoutData(generalInforGridData);
		mbbNameValue.addModifyListener(new EditorModifyListener(this, this.elementsTree));
		this.setUnavailable(mbbNameValue);

		GridData documentationGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		documentationGridData.widthHint = 350;
		documentationGridData.heightHint = 150;
		Label documentationLabel = new Label(generalInformationComposite, SWT.NONE);
		documentationLabel.setText("Documentation");
		mbbDocumentationValue = new Text(generalInformationComposite, SWT.BORDER | SWT.WRAP);
		mbbDocumentationValue.setData("hint", "definition");
		mbbDocumentationValue.setLayoutData(documentationGridData);
		mbbDocumentationValue.addModifyListener(new EditorModifyListener(this, this.elementsTree));
		this.setUnavailable(mbbDocumentationValue);

		Label minOccursLabel = new Label(generalInformationComposite, SWT.NONE);
		minOccursLabel.setText("Min Occurs");
		mbbMinOccursValue = new Text(generalInformationComposite, SWT.BORDER);
		mbbMinOccursValue.setData("hint", "min");
		mbbMinOccursValue.addModifyListener(new EditorModifyListener(this, this.elementsTree));
		this.setUnavailable(mbbMinOccursValue);

		Label MaxOccursLabel = new Label(generalInformationComposite, SWT.NONE);
		MaxOccursLabel.setText("Max Occurs");
		mbbMaxOccursValue = new Text(generalInformationComposite, SWT.BORDER);
		mbbMaxOccursValue.setData("hint", "max");
		mbbMaxOccursValue.addModifyListener(new EditorModifyListener(this, this.elementsTree));
		this.setUnavailable(mbbMaxOccursValue);

		Label xmlTagLabel = new Label(generalInformationComposite, SWT.NONE);
		xmlTagLabel.setText("XML Tag");
		xmlTagValueOfContentPage = new Text(generalInformationComposite, SWT.BORDER);
		xmlTagValueOfContentPage.setLayoutData(generalInforGridData);
		xmlTagValueOfContentPage.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {

				setDirty(true);

				if (elementsTree.getSelection().length > 0) {
					TreeItem treeItem = elementsTree.getSelection()[0];
					String xmlTagValue = xmlTagValueOfContentPage.getText();
					if (treeItem.getData("msgComponentId") != null) {
						treeItem.setData("xmlTag", xmlTagValue);
					}

					if (treeItem.getData("msgBldBlock") != null) {
						EcoreMessageBuildingBlock msgBldBlock = (EcoreMessageBuildingBlock) treeItem
								.getData("msgBldBlock");
						msgBldBlock.setXmlTag(xmlTagValue);
					}
				}
			}

		});

		this.setUnavailable(xmlTagValueOfContentPage);

		Label isDerivedLabel = new Label(generalInformationComposite, SWT.NONE);
		isDerivedLabel.setText("Derived");
		Button isDerivedCheckButton = new Button(generalInformationComposite, SWT.CHECK);
//		if (this.bizElement.getIsDerived().equals("true")) {
//			isDerivedCheckButton.setSelection(true);
//		}
		this.setUnavailable(isDerivedCheckButton);

		Label typeLabel = new Label(generalInformationComposite, SWT.NONE);
		typeLabel.setText("Type");

		Composite cmp = new Composite(generalInformationComposite, SWT.NONE);
		cmp.setLayoutData(new GridData(350, 40));
		cmp.setLayout(new GridLayout(3, false));
		typeValue = new Text(cmp, SWT.BORDER);
		typeValue.setLayoutData(generalInforGridData);
		this.setUnavailable(typeValue);

		Button btBtn = new Button(cmp, SWT.NONE);
		btBtn.setText("choose");
		btBtn.setEnabled(false);
		btBtn.setBackground(new Color(Display.getCurrent(), 135, 206, 250));
//		btBtn.addMouseListener(new TypeButtonListener(typeValue));
//		this.setUnavailable(btBtn);

		Button typeBtn = new Button(cmp, SWT.NONE);
		typeBtn.setImage(ImgUtil.createImg(ImgUtil.SEARCH_BTN));
		typeBtn.setBackground(new Color(Display.getCurrent(), 135, 206, 250));
		typeBtn.addMouseListener(new TypeButtonForMessageDefinition(this.elementsTree));
//		this.setUnavailable(typeBtn);

		ExpandItem generalInformationItem = new ExpandItem(msgBldBlockBar, SWT.NONE);
		generalInformationItem.setText("Message Building block Details");
		generalInformationItem.setHeight(340);
		generalInformationItem.setExpanded(true);
		generalInformationItem.setControl(generalInformationComposite);

		// ------------ Message Building block Details End ------------------------

		// -----------------------CMP Information expand Begin -----------------------

		Composite cmpInformationComposite = new Composite(msgBldBlockBar, SWT.NONE);
		cmpInformationComposite.setLayout(new GridLayout(2, false));
		Label objectIdentifierLabel = new Label(cmpInformationComposite, SWT.NONE);
		objectIdentifierLabel.setText("Object Identifier");
		mbbObjectIdentifierValue = new Text(cmpInformationComposite, SWT.BORDER);
		GridData objectIdentifierGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		objectIdentifierGridData.widthHint = 350;
		mbbObjectIdentifierValue.setLayoutData(objectIdentifierGridData);
		this.setUnavailable(mbbObjectIdentifierValue);

		ExpandItem cmpInformationItem = new ExpandItem(msgBldBlockBar, SWT.NONE);
		cmpInformationItem.setText("CMP Information");
		cmpInformationItem.setHeight(90);
		cmpInformationItem.setExpanded(true);
		cmpInformationItem.setControl(cmpInformationComposite);

		// -----------------------CMP Information expand End -----------------------

		// ---------- Synonyms Expand Item Begin -------------------------------------

		Composite synonymsComposite = new Composite(msgBldBlockBar, SWT.NONE);
		synonymsComposite.setLayout(new GridLayout(1, false));
		Label descriptionLabel = new Label(synonymsComposite, SWT.NONE);
		descriptionLabel.setText("This section show the synonyms of object");
		GridData descriptionGridData = new GridData();
		descriptionGridData.heightHint = 25;
		descriptionLabel.setLayoutData(descriptionGridData);

		Composite synonymsToolbarComposite = new Composite(synonymsComposite, SWT.NONE);
		synonymsToolbarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		Button addMbbSynonymsBtn = new Button(synonymsToolbarComposite, SWT.NONE);
		// 新增按钮
		addMbbSynonymsBtn.setImage(ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR));
//		addMbbSynonymsBtn.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseUp(MouseEvent e) {
//				if (elementsTree.getSelectionCount() == 1) {
//					TreeItem selectedItem = elementsTree.getSelection()[0];
//					if (selectedItem.getData("msgComponentId") != null || selectedItem.getData("dataTypeId") != null) {
//						GenerateSynonymsAndConstraintDialogForContent.createSynonymsDialogue(mbbSynonymsTable, selectedItem);
//					}
//				}
//				setDirty(true);
//			}
//		});
		addMbbSynonymsBtn.setEnabled(false);
		// 删除按钮
		Button deleteMbbSynonymsBtn = new Button(synonymsToolbarComposite, SWT.NONE);
		deleteMbbSynonymsBtn.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
//		deleteMbbSynonymsBtn.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseUp(MouseEvent e) {
//				for (TableItem tableItem: mbbSynonymsTable.getSelection()) {
//					tableItem.dispose();
//				}
//				ArrayList<String> synonymsList = new ArrayList<>();
//				for (TableItem tableRow: mbbSynonymsTable.getItems()) {
//					String key = tableRow.getText(0);
//					String value = tableRow.getText(1);
//					synonymsList.add(key + "," + value);
//				}
//				TreeItem selectedItem = elementsTree.getSelection()[0];
//				if (selectedItem.getData("msgComponentId") != null) {
//					selectedItem.setData("mbbSynonymsTableItems", synonymsList);
//				}
//				setDirty(true);
//			}
//		});
		deleteMbbSynonymsBtn.setEnabled(false);

		GridData tableGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		tableGridData.widthHint = 350;
		tableGridData.heightHint = 250;
		mbbSynonymsTable = new Table(synonymsComposite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		mbbSynonymsTable.setHeaderVisible(true);
		mbbSynonymsTable.setLinesVisible(true);
		mbbSynonymsTable.setLayoutData(tableGridData);

		TableColumn contextColumn = new TableColumn(mbbSynonymsTable, SWT.NONE);
		contextColumn.setWidth(100);
		contextColumn.setText("Context");
		TableColumn SynonymsColumn = new TableColumn(mbbSynonymsTable, SWT.NONE);
		SynonymsColumn.setWidth(100);
		SynonymsColumn.setText("Synonyms");

		ExpandItem synonymsItem = new ExpandItem(msgBldBlockBar, SWT.NONE);
		synonymsItem.setText("Synonyms");
		synonymsItem.setHeight(350);
		synonymsItem.setExpanded(true);
		synonymsItem.setControl(synonymsComposite);

		// ---------- Synonyms Expand Item End -------------------------------------

		// ---------- Constraints Expand Item Begin
		// -------------------------------------

		Composite contraintsComposite = new Composite(msgBldBlockBar, SWT.NONE);
		contraintsComposite.setLayout(new GridLayout(1, false));
		Label contraintsDescLabel = new Label(contraintsComposite, SWT.WRAP);
		contraintsDescLabel.setText(
				"All the constraints contained in this object(other constraints - such as constraints defined on type - may also apply).");
		GridData labelGridData = new GridData();
		labelGridData.widthHint = 430;
		contraintsDescLabel.setLayoutData(labelGridData);

		Composite constraintsToolbarComposite = new Composite(contraintsComposite, SWT.NONE);
		constraintsToolbarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		Button addMbbConstraintsBtn = new Button(constraintsToolbarComposite, SWT.NONE);
		addMbbConstraintsBtn.setImage(ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR));
		Button deleteMbbConstraintsBtn = new Button(constraintsToolbarComposite, SWT.NONE);
		deleteMbbConstraintsBtn.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));

		mbbConstraintsTable = new Table(contraintsComposite, SWT.BORDER);
		mbbConstraintsTable.setHeaderVisible(true);
		mbbConstraintsTable.setLinesVisible(true);
		TableColumn nameColumn = new TableColumn(mbbConstraintsTable, SWT.NONE);
		nameColumn.setWidth(100);
		nameColumn.setText("Name");
		TableColumn defifinitionColumn = new TableColumn(mbbConstraintsTable, SWT.NONE);
		defifinitionColumn.setWidth(100);
		defifinitionColumn.setText("Definition");
		TableColumn expressionLanguageColumn = new TableColumn(mbbConstraintsTable, SWT.NONE);
		expressionLanguageColumn.setWidth(100);
		expressionLanguageColumn.setText("Expression Language");
		TableColumn expressionColumn = new TableColumn(mbbConstraintsTable, SWT.NONE);
		expressionColumn.setWidth(200);
		expressionColumn.setText("Expression");
		GridData constraintsTableGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		constraintsTableGridData.widthHint = 800;
		constraintsTableGridData.heightHint = 250;
		mbbConstraintsTable.setLayoutData(constraintsTableGridData);

//		addMbbConstraintsBtn.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseUp(MouseEvent e) {
//				if (elementsTree.getSelectionCount() == 1) {
//					TreeItem selectedItem = elementsTree.getSelection()[0];
//					if (selectedItem.getData("msgComponentId") != null || selectedItem.getData("dataTypeId") != null) {
//						GenerateSynonymsAndConstraintDialogForContent.createConstraintDialogueOfContent(mbbConstraintsTable, selectedItem);
//					}
//				}
//				setDirty(true);
//			}
//		});
		// 设置不可点击
		addMbbConstraintsBtn.setEnabled(false);

//		deleteMbbConstraintsBtn.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseUp(MouseEvent e) {
//				for (TableItem tableItem: mbbConstraintsTable.getSelection()) {
//					tableItem.dispose();
//				}
//				ArrayList<EcoreConstraint> mbbConstraintList = new ArrayList<>(); 
//				for (TableItem tableRow: mbbConstraintsTable.getItems()) {
//					mbbConstraintList.add((EcoreConstraint)tableRow.getData());
//				}
//				TreeItem selectedItem = elementsTree.getSelection()[0];
//				selectedItem.setData("mbbConstraintList", mbbConstraintList);
//				setDirty(true);
//			}
//		});
		deleteMbbConstraintsBtn.setEnabled(false);

		ExpandItem contraintsItem = new ExpandItem(msgBldBlockBar, SWT.NONE);
		contraintsItem.setText("Contraints");
		contraintsItem.setHeight(500);
		contraintsItem.setExpanded(true);
		contraintsItem.setControl(contraintsComposite);

		composite.requestLayout();

		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		// ---------- Constraints Expand Item End -------------------------------------
	}

	private void showCodeDetailBar(Composite composite, ScrolledComposite scrolledComposite) {

		for (Control control : composite.getChildren()) {
			control.dispose();
		}

		// ------------ Code Details Bar Begin ---------------------------------

		// ------------ Code Detail Expand Item Begin ------------------------------
		ExpandBar codeDetailBar = new ExpandBar(composite, SWT.V_SCROLL);
		codeDetailBar.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		codeDetailBar.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		codeDetailBar.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));

		Composite codeDetailComposite = new Composite(codeDetailBar, SWT.NONE);
		codeDetailComposite.setLayout(new GridLayout(2, false));
		Label nameLabel = new Label(codeDetailComposite, SWT.NONE);
		nameLabel.setText("Name");

		GridData generalInforGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		generalInforGridData.widthHint = 350;

		codeDetailNameValue = new Text(codeDetailComposite, SWT.BORDER);
		nameValue.setLayoutData(generalInforGridData);
		this.setUnavailable(nameValue);

		Label codeDocumentLabel = new Label(codeDetailComposite, SWT.NONE);
		codeDocumentLabel.setText("Documentation");

		GridData documentationGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		documentationGridData.widthHint = 350;
		documentationGridData.heightHint = 150;

		codeDocumentValue = new Text(codeDetailComposite, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
		codeDocumentValue.setLayoutData(documentationGridData);
		this.setUnavailable(codeDocumentValue);

		Label codeNameLabel = new Label(codeDetailComposite, SWT.NONE);
		codeNameLabel.setText("CodeName");
		codeNameValue = new Text(codeDetailComposite, SWT.BORDER);
		codeNameValue.setLayoutData(generalInforGridData);
		this.setUnavailable(codeNameValue);

		ExpandItem codeDetailItem = new ExpandItem(codeDetailBar, SWT.NONE);
		codeDetailItem.setText("Code Detail");
		codeDetailItem.setHeight(340);
		codeDetailItem.setExpanded(true);
		codeDetailItem.setControl(codeDetailComposite);

		// ------------ Code Detail Expand Item End ------------------------------

		// ---------- Code Detail Constraints Expand Item Begin ------------------

		Composite contraintsComposite = new Composite(codeDetailBar, SWT.NONE);
		contraintsComposite.setLayout(new GridLayout(1, false));
		Label contraintsDescLabel = new Label(contraintsComposite, SWT.WRAP);
		contraintsDescLabel.setText(
				"All the constraints contained in this object(other constraints - such as constraints defined on type - may also apply).");
		GridData labelGridData = new GridData();
		labelGridData.widthHint = 430;
		contraintsDescLabel.setLayoutData(labelGridData);

		Composite constraintsToolbarComposite = new Composite(contraintsComposite, SWT.NONE);
		constraintsToolbarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		Button addMbbConstraintsBtn = new Button(constraintsToolbarComposite, SWT.NONE);
		addMbbConstraintsBtn.setImage(ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR));
		Button deleteMbbConstraintsBtn = new Button(constraintsToolbarComposite, SWT.NONE);
		deleteMbbConstraintsBtn.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));

		mbbConstraintsTable = new Table(contraintsComposite, SWT.BORDER);
		mbbConstraintsTable.setHeaderVisible(true);
		mbbConstraintsTable.setLinesVisible(true);
		TableColumn nameColumn = new TableColumn(mbbConstraintsTable, SWT.NONE);
		nameColumn.setWidth(100);
		nameColumn.setText("Name");
		TableColumn defifinitionColumn = new TableColumn(mbbConstraintsTable, SWT.NONE);
		defifinitionColumn.setWidth(100);
		defifinitionColumn.setText("Definition");
		TableColumn expressionLanguageColumn = new TableColumn(mbbConstraintsTable, SWT.NONE);
		expressionLanguageColumn.setWidth(100);
		expressionLanguageColumn.setText("Expression Language");
		TableColumn expressionColumn = new TableColumn(mbbConstraintsTable, SWT.NONE);
		expressionColumn.setWidth(200);
		expressionColumn.setText("Expression");
		GridData constraintsTableGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		constraintsTableGridData.widthHint = 800;
		constraintsTableGridData.heightHint = 250;
		mbbConstraintsTable.setLayoutData(constraintsTableGridData);

//		addMbbConstraintsBtn.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseUp(MouseEvent e) {
//				if (elementsTree.getSelectionCount() == 1) {
//					TreeItem selectedItem = elementsTree.getSelection()[0];
//					if (selectedItem.getData("msgComponentId") != null || selectedItem.getData("dataTypeId") != null) {
//						GenerateSynonymsAndConstraintDialogForContent.createConstraintDialogueOfContent(mbbConstraintsTable, selectedItem);
//					}
//				}
//				setDirty(true);
//			}
//		});
		addMbbConstraintsBtn.setEnabled(false);

//		deleteMbbConstraintsBtn.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseUp(MouseEvent e) {
//				for (TableItem tableItem: mbbConstraintsTable.getSelection()) {
//					tableItem.dispose();
//				}
//				ArrayList<EcoreConstraint> mbbConstraintList = new ArrayList<>(); 
//				for (TableItem tableRow: mbbConstraintsTable.getItems()) {
//					mbbConstraintList.add((EcoreConstraint)tableRow.getData());
//				}
//				TreeItem selectedItem = elementsTree.getSelection()[0];
//				selectedItem.setData("mbbConstraintList", mbbConstraintList);
//				setDirty(true);
//			}
//		});
		deleteMbbConstraintsBtn.setEnabled(false);

		ExpandItem contraintsItem = new ExpandItem(codeDetailBar, SWT.NONE);
		contraintsItem.setText("Contraints");
		contraintsItem.setHeight(500);
		contraintsItem.setExpanded(true);
		contraintsItem.setControl(contraintsComposite);

		// ---------- Code Detail Constraints Expand Item End
		// -------------------------------------

		composite.requestLayout();

		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		// ------------ Code Details Bar End ---------------------------------
	}

	private void showConstraintBar(Composite composite, ScrolledComposite scrolledComposite) {

		for (Control control : composite.getChildren()) {
			control.dispose();
		}

		// -------------------- Constraints Bar Begin -------------------------

		ExpandBar constraintBar = new ExpandBar(composite, SWT.V_SCROLL);
		constraintBar.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		constraintBar.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		constraintBar.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));

		Composite constraintComposite = new Composite(constraintBar, SWT.NONE);
		constraintComposite.setLayout(new GridLayout(2, false));

		Label constraintNameLabel = new Label(constraintComposite, SWT.NONE);
		constraintNameLabel.setText("Name");

		GridData generalInforGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		generalInforGridData.widthHint = 350;

		constraintNameValue = new Text(constraintComposite, SWT.BORDER);
		constraintNameValue.setData("hint", "name");
		constraintNameValue.setLayoutData(generalInforGridData);
		constraintNameValue.addModifyListener(new EditorModifyListener(this, this.elementsTree));
		this.setUnavailable(constraintNameValue);

		Label constraintDocLabel = new Label(constraintComposite, SWT.NONE);
		constraintDocLabel.setText("Documentation");

		GridData documentationGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		documentationGridData.widthHint = 350;
		documentationGridData.heightHint = 150;

		constraintDocValue = new Text(constraintComposite, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
		constraintDocValue.setLayoutData(documentationGridData);
		constraintDocValue.setData("hint", "document");
		constraintDocValue.addModifyListener(new EditorModifyListener(this, this.elementsTree));
		this.setUnavailable(constraintDocValue);

		Label expressionLabel = new Label(constraintComposite, SWT.NONE);
		expressionLabel.setText("Expression");
		expressionValue = new Text(constraintComposite, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
		expressionValue.setData("hint", "expression");
		expressionValue.setLayoutData(documentationGridData);
		expressionValue.addModifyListener(new EditorModifyListener(this, this.elementsTree));
		this.setUnavailable(expressionValue);

		Label expressionLanguageLabel = new Label(constraintComposite, SWT.NONE);
		expressionLanguageLabel.setText("Expression Language");
		expressionLanguageValue = new Text(constraintComposite, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
		expressionLanguageValue.setData("hint", "expressionLanguage");
		expressionLanguageValue.setLayoutData(documentationGridData);
		expressionLanguageValue.addModifyListener(new EditorModifyListener(this, this.elementsTree));
		this.setUnavailable(expressionLanguageValue);

		ExpandItem constraintItem = new ExpandItem(constraintBar, SWT.NONE);
		constraintItem.setText("Constraint");
		constraintItem.setHeight(600);
		constraintItem.setExpanded(true);
		constraintItem.setControl(constraintComposite);

		composite.requestLayout();

		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		// -------------------- Constraints Bar End -------------------------
	}

}
