package org.cufir.plugin.mr.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.cufir.plugin.mr.ImgUtil;
import org.cufir.plugin.mr.MrHelper;
import org.cufir.plugin.mr.bean.ButtonPolicy;
import org.cufir.plugin.mr.bean.ComboPolicy;
import org.cufir.plugin.mr.bean.ObjTypeEnum;
import org.cufir.plugin.mr.bean.RegistrationStatusEnum;
import org.cufir.plugin.mr.bean.TextPolicy;
import org.cufir.plugin.mr.bean.TransferDataBean;
import org.cufir.plugin.mr.bean.TreeMenuEnum;
import org.cufir.plugin.mr.handlers.SaveHandler;
import org.cufir.s.data.vo.EcoreBusinessAreaVO;
import org.cufir.s.data.vo.EcoreTreeNode;
import org.cufir.s.ecore.bean.EcoreBusinessArea;
import org.cufir.s.ecore.bean.EcoreCode;
import org.cufir.s.ecore.bean.EcoreConstraint;
import org.cufir.s.ecore.bean.EcoreExample;
import org.cufir.s.ecore.bean.EcoreMessageBuildingBlock;
import org.cufir.s.ecore.bean.EcoreMessageDefinition;
import org.cufir.s.ecore.bean.EcoreMessageElement;
import org.cufir.s.ide.i18n.I18nApi;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

/**
 * BusinessArea（业务领域）编辑和显示
 */
public class BusinessAreaEditor extends MrMultiPageEditor {
	
	private List<EcoreConstraint> css = MrRepository.get().ecoreConstraints;
	private List<EcoreMessageBuildingBlock> mbbs =  MrRepository.get().ecoreMessageBuildingBlocks;

	private MrEditorInput myEditorInput;

	private TreeItem modelExploreTreeItem;

	private EcoreBusinessArea businessArea;

	private ArrayList<EcoreMessageDefinition> messageDefinitionList;

	private EcoreTreeNode ecoreTreeNode;

	// -------------for Summary Page use-----------------
	private Text nameValue;
	private Text documentationValue;
	private Text bizAreaCodeValue;
	private Text objectIdentifierValue;
	private Combo statusCombo;
	private Text titleValue;
	private Text subTitleValue;
	private Text publishingDateValue;
	private Table examplesTable;
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
		this.setPartProperty(MrHelper.SAVE_CUSTOM_NAME, TreeMenuEnum.BUSINESS_AREAS.getName());
		this.myEditorInput = (MrEditorInput) input;

		modelExploreTreeItem = this.myEditorInput.getTransferDataBean().getTreeListItem();

		ecoreTreeNode = (EcoreTreeNode) modelExploreTreeItem.getData("EcoreTreeNode");

		setContext(site);

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
		businessArea = MrHelper.getBusinessAreaByBizAreaId(businessAreaId);
		messageDefinitionList = MrHelper.getmessageDefinitionListByBizAreaId(businessAreaId);
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

		if (RegistrationStatusEnum.Added.getStatus().equals(ecoreTreeNode.getRegistrationStatus())) {
			this.setDirty(true);
		} else {
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

	public EcoreBusinessAreaVO getEcoreMessageDefinitionVO() {

		businessArea.setName(this.nameValue.getText());
		businessArea.setDefinition(this.documentationValue.getText());
		businessArea.setObjectIdentifier(objectIdentifierValue.getText());
		businessArea.setRegistrationStatus(statusCombo.getText());
		businessArea.setCode(this.bizAreaCodeValue.getText());

		EcoreBusinessAreaVO ecoreBusinessAreaVO = new EcoreBusinessAreaVO();
		ecoreBusinessAreaVO.setEcoreBusinessArea(businessArea);
		ecoreBusinessAreaVO.setEcoreExamples(this.getEcoreExamplesTable(businessArea));
		ecoreBusinessAreaVO.setEcoreConstraints(getEcoreConstraintList(businessArea));
		ecoreBusinessAreaVO.setEcoreMessageDefinitionIds(this.msgDefinitionIdIncluded.stream().collect(Collectors.toList()));
		return ecoreBusinessAreaVO;
	}

	private ArrayList<EcoreExample> getEcoreExamplesTable(EcoreBusinessArea businessArea) {
		ArrayList<EcoreExample> ecoreExampleList = new ArrayList<>();
		for (TableItem tableItem : this.examplesTable.getItems()) {
			EcoreExample ecoreExample = (EcoreExample) tableItem.getData();
			ecoreExample.setObjId(businessArea.getId());
			ecoreExample.setObjType(ObjTypeEnum.MessageSet.getType());
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
		
		boolean isEditable = !RegistrationStatusEnum.Registered.getStatus().equals(ecoreTreeNode.getRegistrationStatus())
				&& !RegistrationStatusEnum.Obsolete.getStatus().equals(ecoreTreeNode.getRegistrationStatus());
		
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

		String name = businessArea == null ? "" : businessArea.getName();
		nameValue = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 45, "Name", isEditable, name, new MrEditorModifyListener(this, modelExploreTreeItem))).getText();

		String documentation = businessArea == null ? "" : businessArea.getDefinition();
		documentationValue = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_SCROLL_TYPE, 73, "Documentation", isEditable, documentation, new MrEditorModifyListener(this))).getText();
		
		String code = businessArea == null ? "" : businessArea.getCode();
		bizAreaCodeValue = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 278, "Business Area Code", isEditable, code, new MrEditorModifyListener(this))).getText();
		// ================= General Information Group End ============================

		// ================= CMP Information Group Begin ==============================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 328, I18nApi.get("editor.title.cmp")));

		String objectIdentifier = businessArea == null ? "" : businessArea.getObjectIdentifier();
		objectIdentifierValue = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 368, "Object Identifier", isEditable, objectIdentifier, new MrEditorModifyListener(this))).getText();
		// ================= CMP Information Group End ==============================

		// ===== Registration Information Group Begin ===============================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 418, I18nApi.get("editor.title.ri")));

		//状态
		java.util.List<String> list = new ArrayList<>();
		list.add(RegistrationStatusEnum.Registered.getStatus());
		list.add(RegistrationStatusEnum.Provisionally.getStatus());
		list.add(RegistrationStatusEnum.Added.getStatus());
		
		String status = "";
		if (businessArea.getRegistrationStatus() == null) {
			status = RegistrationStatusEnum.Added.getStatus();
		} else {
			status = businessArea.getRegistrationStatus();
		}
		statusCombo = new SummaryRowComboComposite(summaryComposite, new ComboPolicy(ComboPolicy.COMBO_COMMONLY_TYPE, 458,"Registration Status", isEditable, list, status)).getCombo();
		// ===== Registration Information Group End ===============================

		// ========================Documentation Information Begin==================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 508, I18nApi.get("editor.title.di")));
		
		titleValue = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 548, "Title", isEditable, "")).getText();
		subTitleValue = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_SCROLL_TYPE, 576, "Subtitle", isEditable, "")).getText();
		publishingDateValue = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 781, "Publishing Date", isEditable, "")).getText();
		// ======================Documentation Information End ==================

		// =================== Exampel Group Begin ==============================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 831, I18nApi.get("editor.title.es")));
		Button addExamplesBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR),isEditable)).getButton();
		addExamplesBtn.setBounds(10, 871, 30, 30);
		addExamplesBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MrEditorDialogCreator.createExamplesDialogue(examplesTable);
				setDirty(true);
			}
		});
		Button deleteExamplesBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES),isEditable)).getButton();
		deleteExamplesBtn.setBounds(42, 871, 30, 30);
		deleteExamplesBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MrEditorDialogCreator.deleteExamplesDialogue(examplesTable);
				setDirty(true);
			}
		});
		examplesTable = new Table(summaryComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		examplesTable.setHeaderVisible(true);
		examplesTable.setLinesVisible(true);
		examplesTable.setBounds(10, 906, 600, 150);
		examplesTable.layout();
		if (this.businessArea != null && this.businessArea.getId() != null) {
			MrEditorHelper.setExamplesTable(examplesTable, this.businessArea.getId());
		}
		// =================== Exampel Group End ==============================

		// ===================== Constraints Group Begin====================================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 1083, I18nApi.get("editor.title.cs")));
		Button addConstraintBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR),isEditable)).getButton();
		addConstraintBtn.setBounds(10, 1123, 30, 30);
		addConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MrEditorDialogCreator.createConstraintDialogueForBAandMS("add", constraintsTable);
				setDirty(true);
			}
		});

		Button deleteConstraintBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES),isEditable)).getButton();
		deleteConstraintBtn.setBounds(42, 1123, 30, 30);
		deleteConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MrEditorDialogCreator.deleteConstraintForBA(constraintsTable);
				setDirty(true);
			}
		});

		constraintsTable = new Table(summaryComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		constraintsTable.setHeaderVisible(true);
		constraintsTable.setLinesVisible(true);
		constraintsTable.setBounds(10, 1158, 600, 250);
		constraintsTable.layout();

		new SummaryTableComposite(constraintsTable,149, "Name");
		new SummaryTableComposite(constraintsTable,149, "Definiton");
		new SummaryTableComposite(constraintsTable,149, "Expression Language");
		new SummaryTableComposite(constraintsTable,149, "Expression");
		
		if (this.businessArea != null && this.businessArea.getId() != null) {
			MrEditorHelper.setConstraintTable(constraintsTable, this.businessArea.getId(), null);
		}
		// ===================== Constraints Group End======================================
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
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
	
	/*
	 * Content page的显示
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
		rightComposite.setBounds(510, 0, 500, 1020);
		rightComposite.layout();
		
		// ------------ Content Expand Bar Begin ----------------------------------
		Composite leftCom = new Composite(leftComposite, SWT.NONE);
		leftCom.setBounds(0, 5, 490, 730);
		leftCom.layout();
		
		new SummaryRowTextComposite(leftCom, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 0, I18nApi.get("editor.title.ct")));

		Button addButtonForContent = new SummaryRowTextComposite(leftCom, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR))).getButton();
		addButtonForContent.setBounds(5, 35, 30, 30);
		addButtonForContent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				createMessageBox(MrImplManager.get().getEcoreMessageDefinitionImpl().findIsNullBusinessAreaId());
				setDirty(true);
			}
		});
		this.setUnavailable(addButtonForContent);

		Button deleteButtonForContent = new SummaryRowTextComposite(leftCom, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES))).getButton();
		deleteButtonForContent.setBounds(42, 35, 30, 30);
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

		elementsTree = new Tree(leftCom, SWT.BORDER | SWT.SINGLE);
		elementsTree.setBounds(5, 70, 485, 650);
		elementsTree.layout();

		if(messageDefinitionList != null) {
			for (EcoreMessageDefinition msgDefinition : messageDefinitionList) {
				TreeItem treeItem = new TreeItem(elementsTree, SWT.NONE);
				treeItem.setText(msgDefinition.getName());
				treeItem.setData("msgDefinitionId", msgDefinition.getId());
				msgDefinitionIdIncluded.add(msgDefinition.getId());
				if (RegistrationStatusEnum.Registered.getStatus().equals(msgDefinition.getRegistrationStatus())) {
					treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB1));
				}else if (RegistrationStatusEnum.Obsolete.getStatus().equals(msgDefinition.getRegistrationStatus())) {
					treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB4));
				}else {
					treeItem.setImage(ImgUtil.createImg(ImgUtil.MESSAGE_DEFINITIONS));
				}
				MrEditorTreeCreator mrEditorTreeCreator = new MrEditorTreeCreator();
				mrEditorTreeCreator.generateContentTreeForMessageDefinition(treeItem,
						MrHelper.findMsgBuildingBlocksByMsgId(mbbs, msgDefinition.getId()), null, msgDefinition.getRegistrationStatus());
				List<EcoreConstraint> ecs = MrHelper.findConstraintsByObjId(css, msgDefinition.getId());
				mrEditorTreeCreator.addConstraintsNode(treeItem, ecs, null, null);
			}
		}
		// ------------ Content Expand Bar End ----------------------------------
		// 鼠标监听事件
		elementsTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TreeItem[] treeItems = elementsTree.getSelection();
				for (TreeItem treeItem : treeItems) {
					String bbid = String.valueOf(treeItem.getData("bbId"));
					String constraintId = String.valueOf(treeItem.getData("constraintId"));
					String msgElementId = String.valueOf(treeItem.getData("msgElementID"));
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
						MrEditorHelper.setConstraintTable(mbbConstraintsTable,
								msgBB.getId(), treeItem);
						MrEditorHelper.setSynonymsTable(mbbSynonymsTable, msgBB.getId(),
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

					} else if (!msgElementId.equalsIgnoreCase("null")) {
						showMessageBuildingBlockBar(rightComposite, scrolledComposite);
						EcoreMessageElement ecoreMessageElement = MrImplManager.get().getEcoreMessageElementImpl().getById(msgElementId);
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
						MrEditorHelper.setConstraintTable(mbbConstraintsTable, msgElementId, treeItem);
						MrEditorHelper.setSynonymsTable(mbbSynonymsTable, msgElementId, null);
					} else if (!codeId.equalsIgnoreCase("null")) {
						showCodeDetailBar(rightComposite, scrolledComposite);
						EcoreCode ecoreCode = (EcoreCode) treeItem.getData("ecoreCode");
						codeDetailNameValue.setEditable(false);
						codeDetailNameValue.setText(ecoreCode.getName());
						codeDocumentValue.setEditable(false);
						codeDocumentValue.setText(ecoreCode.getDefinition());
						codeNameValue.setEditable(false);
						codeNameValue.setText(ecoreCode.getCodeName());
						MrEditorHelper.setConstraintTable(mbbConstraintsTable, codeId, treeItem);
					} else {
						// 鼠标单击到Message Definition节点时，Content右边全部清楚
						for (Control control : rightComposite.getChildren()) {
							control.dispose();
						}
					}
				}
			}
		});
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	void createMessageBox(List<EcoreMessageDefinition> msgDefinitions) {
		Shell messageDefinitionWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		messageDefinitionWindow.setText("Select Message Definition to Add");
		messageDefinitionWindow.setLayout(new FormLayout());

		// 改变弹窗位置
		MrHelper.center(messageDefinitionWindow);

		Composite c = new Composite(messageDefinitionWindow, SWT.NONE);
		Text searchText = new Text(c, SWT.BORDER);
		searchText.setBounds(10, 10, 280, 30);
		Button searchBtn = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Search")).getButton();
		searchBtn.setBounds(300, 10, 50, 30);

		Table msgDefinitionTable = new Table(c, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
		msgDefinitionTable.setLinesVisible(true);
		for (EcoreMessageDefinition definition : msgDefinitions) {
			TableItem tableItem = new TableItem(msgDefinitionTable, SWT.NONE);
			tableItem.setText(definition.getName());
			tableItem.setData("msgDefinitionId", definition.getId());
			tableItem.setData("registrationStatus", definition.getRegistrationStatus());
			if (RegistrationStatusEnum.Registered.getStatus().equals(definition.getRegistrationStatus())) {
				tableItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB1));
			}else if (RegistrationStatusEnum.Obsolete.getStatus().equals(definition.getRegistrationStatus())) {
				tableItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB4));
			}else {
				tableItem.setImage(ImgUtil.createImg(ImgUtil.MESSAGE_DEFINITIONS));
			}
		}
		msgDefinitionTable.setSelection(0);
		msgDefinitionTable.setBounds(10, 50, 350, 400);
		// 鼠标选择时间
		msgDefinitionTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				searchText.setText(msgDefinitionTable.getSelection()[0].getText());
			}
		});
		
		Button okButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"OK")).getButton();
		okButton.setBounds(275, 460, 35, 30);
		okButton.addMouseListener(new OkButtonListener(msgDefinitionTable, messageDefinitionWindow));

		Button cancelButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"Cancel")).getButton();
		cancelButton.setBounds(320, 460, 52, 30);
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
							if (RegistrationStatusEnum.Registered.getStatus().equals(t.getRegistrationStatus())) {
								tableItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB1));
							}else if (RegistrationStatusEnum.Obsolete.getStatus().equals(t.getRegistrationStatus())) {
								tableItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB4));
							}else {
								tableItem.setImage(ImgUtil.createImg(ImgUtil.MESSAGE_DEFINITIONS));
							}
						});
			}
		});
	}

	private class OkButtonListener extends MouseAdapter {

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
					if (RegistrationStatusEnum.Registered.getStatus().equals(registrationStatus)) {
						treeNode.setImage(ImgUtil.createImg(ImgUtil.MD_SUB1));
					}else if (RegistrationStatusEnum.Obsolete.getStatus().equals(registrationStatus)) {
						tableItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB4));
					}else {
						treeNode.setImage(ImgUtil.createImg(ImgUtil.MESSAGE_DEFINITIONS));
					}
					treeNode.setData("msgDefinitionId", msgDefinitionId);
					// 构造一个以Message Definiton 为根节点的树
					MrEditorTreeCreator mrEditorTreeCreator = new MrEditorTreeCreator();
					mrEditorTreeCreator.generateContentTreeForMessageDefinition(treeNode,
							MrHelper.findMsgBuildingBlocksByMsgId(mbbs, msgDefinitionId), null, registrationStatus);
					List<EcoreConstraint> ecs = MrHelper.findConstraintsByObjId(css, msgDefinitionId);
					mrEditorTreeCreator.addConstraintsNode(treeNode, ecs, null, null);

					// 暂存Message Definition ID, 为保存Message Set用
					msgDefinitionIdIncluded.add(msgDefinitionId);
				}
			}
			shell.close();
		}
	}

	private class CancelButtonListener extends MouseAdapter {

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
		if (RegistrationStatusEnum.Registered.getStatus().equals(this.ecoreTreeNode.getRegistrationStatus()) 
				|| RegistrationStatusEnum.Obsolete.getStatus().equals(this.ecoreTreeNode.getRegistrationStatus())) {
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
		if (dirty && RegistrationStatusEnum.Registered.getStatus().equals(this.ecoreTreeNode.getRegistrationStatus())
				|| RegistrationStatusEnum.Obsolete.getStatus().equals(this.ecoreTreeNode.getRegistrationStatus())) {
			return;
		}
		super.setDirty(dirty);
	}

	private void showMessageBuildingBlockBar(Composite rightComposite, ScrolledComposite scrolledComposite) {

		for (Control control : rightComposite.getChildren()) {
			control.dispose();
		}

		// ------------ Message Building block Details Begin ------------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 5, I18nApi.get("editor.title.mbbd")));

		mbbNameValue = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 45, "Name")).getText();
		mbbNameValue.setData("hint", "name");
		mbbNameValue.addModifyListener(new MrEditorModifyListener(this, this.elementsTree));
		this.setUnavailable(mbbNameValue);

		mbbDocumentationValue = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SCROLL_TYPE, 73, "Documentation")).getText();
		mbbDocumentationValue.setData("hint", "definition");
		mbbDocumentationValue.addModifyListener(new MrEditorModifyListener(this, this.elementsTree));
		this.setUnavailable(mbbDocumentationValue);

		mbbMinOccursValue = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SHORT_TYPE, 228, "Min Occurs")).getText();
		mbbMinOccursValue.setData("hint", "min");
		mbbMinOccursValue.addModifyListener(new MrEditorModifyListener(this, this.elementsTree));
		this.setUnavailable(mbbMinOccursValue);

		mbbMaxOccursValue = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SHORT_TYPE, 256, "Max Occurs")).getText();
		mbbMaxOccursValue.setData("hint", "max");
		mbbMaxOccursValue.addModifyListener(new MrEditorModifyListener(this, this.elementsTree));
		this.setUnavailable(mbbMaxOccursValue);

		xmlTagValueOfContentPage = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 284, "XML Tag")).getText();
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

		Button isDerivedCheckButton = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_CHECK, 312, "Derived", false)).getButton();
		this.setUnavailable(isDerivedCheckButton);

		typeValue = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_BUTTON_SELECT_TYPE, 340, "Type")).getText();
		this.setUnavailable(typeValue);

		Button btBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_DEEP,"choose")).getButton();
		btBtn.setBounds(390, 340, 50, 25);
		
		Button btSearchBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_DEEP,ImgUtil.createImg(ImgUtil.SEARCH_BTN))).getButton();
		btSearchBtn.setBounds(445, 340, 25, 25);
		btSearchBtn.addMouseListener(new MrSearchMouseAdapter(this.elementsTree));
		// ------------ Message Building block Details End ------------------------

		// -----------------------CMP Information expand Begin -----------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 390, I18nApi.get("editor.title.cmp")));
		
		mbbObjectIdentifierValue = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 430, "Object Identifier")).getText();
		this.setUnavailable(mbbObjectIdentifierValue);
		// -----------------------CMP Information expand End -----------------------

		// ---------- Synonyms Expand Item Begin -------------------------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 480, I18nApi.get("editor.title.ss")));

		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_NOTE_ONE_TYPE, 520, "This section show the synonyms of object"));
		
		Button addSynonymsBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT, ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR),false)).getButton();
		addSynonymsBtn.setBounds(10, 548, 30, 30);
		
		Button deleteButtonForSynonyms = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT, ImgUtil.createImg(ImgUtil.DELETE_IMG_YES),false)).getButton();
		deleteButtonForSynonyms.setBounds(42, 548, 30, 30);
		
		mbbSynonymsTable = new Table(rightComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		mbbSynonymsTable.setHeaderVisible(true);
		mbbSynonymsTable.setLinesVisible(true);
		mbbSynonymsTable.setBounds(10, 585, 480, 150);
		mbbSynonymsTable.layout();

		new SummaryTableComposite(mbbSynonymsTable, 238, "Context");
		new SummaryTableComposite(mbbSynonymsTable, 238, "Synonums");
		// ---------- Synonyms Expand Item End -------------------------------------

		// ---------- Constraints Expand Item Begin ------------------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 740, I18nApi.get("editor.title.cs")));
		
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_NOTE_TWO_TYPE, 780, "All the constraints contained in this object(other constraints - such as constraints"
				+ "\r\n defined on type - may also apply)."));
		
		Button addConstraintBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT, ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR),false)).getButton();
		addConstraintBtn.setBounds(10, 825, 30, 30);
		Button deleteConstraintBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT, ImgUtil.createImg(ImgUtil.DELETE_IMG_YES),false)).getButton();
		deleteConstraintBtn.setBounds(42, 825, 30, 30);
		
		mbbConstraintsTable = new Table(rightComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		mbbConstraintsTable.setHeaderVisible(true);
		mbbConstraintsTable.setLinesVisible(true);
		mbbConstraintsTable.setBounds(10, 860, 480, 150);
		mbbConstraintsTable.layout();
		
		new SummaryTableComposite(mbbConstraintsTable, 113, "Name");
		new SummaryTableComposite(mbbConstraintsTable, 113, "Definition");
		new SummaryTableComposite(mbbConstraintsTable, 137, "Expression Language");
		new SummaryTableComposite(mbbConstraintsTable, 113, "Expression");
		// ---------- Constraints Expand Item End -------------------------------------
	}

	private void showCodeDetailBar(Composite rightComposite, ScrolledComposite scrolledComposite) {
		for (Control control : rightComposite.getChildren()) {
			control.dispose();
		}
		// ------------ Code Detail Expand Item Begin ------------------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 5, I18nApi.get("editor.title.cd")));

		codeDetailNameValue = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 45, "Name")).getText();
		this.setUnavailable(codeDetailNameValue);

		codeDocumentValue = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SCROLL_TYPE, 73, "Documentation")).getText();
		this.setUnavailable(codeDocumentValue);
		
		codeNameValue = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SCROLL_TYPE, 228, "CodeName")).getText();
		this.setUnavailable(codeNameValue);
		// ------------ Code Detail Expand Item End ------------------------------

		// ---------- Code Detail Constraints Expand Item Begin ------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 278, I18nApi.get("editor.title.cs")));
		
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_NOTE_TWO_TYPE, 318, "All the constraints contained in this object(other constraints - such as constraints"
				+ "\r\n defined on type - may also apply)."));

		Button addConstraintBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT, ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR),false)).getButton();
		addConstraintBtn.setBounds(10, 363, 30, 30);
		Button deleteConstraintBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT, ImgUtil.createImg(ImgUtil.DELETE_IMG_YES),false)).getButton();
		deleteConstraintBtn.setBounds(42, 363, 30, 30);
		
		mbbConstraintsTable = new Table(rightComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		mbbConstraintsTable.setHeaderVisible(true);
		mbbConstraintsTable.setLinesVisible(true);
		mbbConstraintsTable.setBounds(10, 398, 480, 150);
		mbbConstraintsTable.layout();
		new SummaryTableComposite(mbbConstraintsTable, 113, "Name");
		new SummaryTableComposite(mbbConstraintsTable, 113, "Definition");
		new SummaryTableComposite(mbbConstraintsTable, 137, "Expression Language");
		new SummaryTableComposite(mbbConstraintsTable, 113, "Expression");
		// ---------- Code Detail Constraints Expand Item End --------------------------
	}

	private void showConstraintBar(Composite rightComposite, ScrolledComposite scrolledComposite) {

		for (Control control : rightComposite.getChildren()) {
			control.dispose();
		}
		// -------------------- Constraints Bar Begin -------------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 710, I18nApi.get("editor.title.ct")));
		constraintNameValue = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 45, "Name")).getText();
		constraintNameValue.setData("hint", "name");
		constraintNameValue.addModifyListener(new MrEditorModifyListener(this, this.elementsTree));
		this.setUnavailable(constraintNameValue);

		constraintDocValue = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SCROLL_TYPE, 73, "Documentation")).getText();
		constraintDocValue.setData("hint", "document");
		constraintDocValue.addModifyListener(new MrEditorModifyListener(this, this.elementsTree));
		this.setUnavailable(constraintDocValue);

		constraintDocValue = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SCROLL_TYPE, 228, "Expression")).getText();
		expressionValue.setData("hint", "expression");
		expressionValue.addModifyListener(new MrEditorModifyListener(this, this.elementsTree));
		this.setUnavailable(expressionValue);

		expressionLanguageValue = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SCROLL_TYPE, 383, "Expression Language")).getText();
		expressionLanguageValue.setData("hint", "expressionLanguage");
		expressionLanguageValue.addModifyListener(new MrEditorModifyListener(this, this.elementsTree));
		this.setUnavailable(expressionLanguageValue);
		// -------------------- Constraints Bar End -------------------------
	}
}
