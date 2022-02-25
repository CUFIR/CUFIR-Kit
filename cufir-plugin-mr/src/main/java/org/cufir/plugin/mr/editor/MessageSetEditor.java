package org.cufir.plugin.mr.editor;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.cufir.plugin.mr.MrHelper;
import org.cufir.plugin.mr.bean.ButtonPolicy;
import org.cufir.plugin.mr.bean.ComboPolicy;
import org.cufir.plugin.mr.bean.ObjTypeEnum;
import org.cufir.plugin.mr.bean.RegistrationStatusEnum;
import org.cufir.plugin.mr.bean.TextPolicy;
import org.cufir.plugin.mr.bean.TransferDataBean;
import org.cufir.plugin.mr.handlers.SaveHandler;
import org.cufir.plugin.mr.utils.ImgUtil;
import org.cufir.plugin.mr.utils.SystemUtil;
import org.cufir.s.ecore.bean.EcoreCode;
import org.cufir.s.ecore.bean.EcoreConstraint;
import org.cufir.s.ecore.bean.EcoreExample;
import org.cufir.s.ecore.bean.EcoreMessageBuildingBlock;
import org.cufir.s.ecore.bean.EcoreMessageDefinition;
import org.cufir.s.ecore.bean.EcoreMessageElement;
import org.cufir.s.ecore.bean.EcoreMessageSet;
import org.cufir.s.ecore.bean.EcoreMessageSetDefinitionRL;
import org.cufir.s.ide.utils.i18n.I18nApi;
import org.cufir.s.data.vo.EcoreMessageSetVO;
import org.cufir.s.data.vo.EcoreTreeNode;
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

/**
 * Message Set （报文集）展示与编辑
 * @author tangmaoquan
 * @Date 2021年9月29日
 */
public class MessageSetEditor extends MrMultiPageEditor {
	
	private List<EcoreConstraint> css = MrRepository.get().ecoreConstraints;
	private List<EcoreMessageBuildingBlock> mbbs = MrRepository.get().ecoreMessageBuildingBlocks;

	private MrEditorInput myEditorInput;

	private TreeItem modelExploreTreeItem;

	private EcoreMessageSet messageSet;

	private List<EcoreMessageDefinition> messageDefinitionList;

	private EcoreTreeNode ecoreTreeNode;

	// -----------For Summary Page -----------
	private Text nameValue;
	private Text documentationValue;
	private Text objectIdentifierValue;
	private Combo statusCombo;
	private Text titleValue;
	private Text removalDateValue;
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
	private Table mbbSynonymsTable;
	private Text typeValue;
	private Text mbbObjectIdentifierValue;
	private Table mbbConstraintsTable;
	private Text constraintNameValue;
	private Text constraintDocValue;
	private Text expressionValue;
	private Text expressionLanguageValue;
	private Text codeDetailNameValue;
	private Text codeDocumentValue;
	private Text codeNameValue;

	@Override
	public void setPartName(String partName) {
		super.setPartName(partName);
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		this.setSite(site);
		this.setInput(input);
		this.setPartProperty("customName", "msgSetCreate");
		this.myEditorInput = (MrEditorInput) input;
		modelExploreTreeItem = this.myEditorInput.getTransferDataBean().getTreeListItem();
		ecoreTreeNode = (EcoreTreeNode) modelExploreTreeItem.getData("EcoreTreeNode");
		setContext(site);
	}

	@Override
	protected void createPages() {
		loadMessageSetData();
		if (messageSet.getId() == null) {
			messageSet.setId(this.myEditorInput.getTransferDataBean().getId());
			this.setPartName("");
			this.setDirty(true);
		} else {
			this.setPartName(messageSet.getName());
		}
		if (RegistrationStatusEnum.Added.getStatus().equals(this.ecoreTreeNode.getRegistrationStatus())) {
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

	/**
	 * Message Set 获取
	 * 
	 */
	void loadMessageSetData() {
		TransferDataBean transferBean = this.myEditorInput.getTransferDataBean();
		String msgSetId = transferBean.getId();
//		String name = transferBean.getName();
		if (msgSetId != null) {
			this.setPartProperty("ID", msgSetId);
			messageSet = MrImplManager.get().getEcoreMessageSetImpl().getById(msgSetId);
			messageDefinitionList = MrImplManager.get().getEcoreMessageDefinitionImpl().findByMsgSetId(msgSetId);
		}
	}

	/**
	 * Summary Page 显示和编辑
	 */
	void createSummaryPage() {
		Composite parentComposite = getContainer();
		ScrolledComposite scrolledComposite = new ScrolledComposite(parentComposite, SWT.V_SCROLL);
		
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		composite.setLayout(new FillLayout());
		
		scrolledComposite.setContent(composite);
		int index = addPage(scrolledComposite);
		setPageText(index, I18nApi.get("editor.label.sy"));
		
		Composite summaryComposite = new Composite(composite, SWT.NONE);
		summaryComposite.setBounds(5, 5, 850, 1250);
		summaryComposite.layout();

		// 是否可编辑
		boolean isEditable = !RegistrationStatusEnum.Registered.getStatus().equals(this.ecoreTreeNode.getRegistrationStatus())
				&& !RegistrationStatusEnum.Obsolete.getStatus().equals(this.ecoreTreeNode.getRegistrationStatus());
		// ================= General Information Group Begin =======================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 5, I18nApi.get("editor.title.gi")));

		String name = messageSet == null ? "" : messageSet.getName();
		nameValue = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 45, "Name", isEditable, name, new MrEditorModifyListener(this, modelExploreTreeItem))).getText();

		String documentation = messageSet == null ? "" : messageSet.getDefinition();
		documentationValue = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_SCROLL_TYPE, 73, "Documentation", isEditable, documentation, new MrEditorModifyListener(this))).getText();
		// ================= General Information Group End ============================

		// ================= CMP Information Group Begin ==============================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 298, I18nApi.get("editor.title.cmp")));
		
		String objectIdentifier = messageSet == null ? "" : messageSet.getObjectIdentifier();
		objectIdentifierValue = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 338, "Object Identifier", isEditable, objectIdentifier, new MrEditorModifyListener(this))).getText();
		// ================= CMP Information Group End ==============================

		// ===== Registration Information Group Begin ===============================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 388, I18nApi.get("editor.title.ri")));
		
		java.util.List<String> list = new ArrayList<>();
		list.add(RegistrationStatusEnum.Registered.getStatus());
		list.add(RegistrationStatusEnum.Provisionally.getStatus());
		list.add(RegistrationStatusEnum.Added.getStatus());
		String status = "";
		if (messageSet.getRegistrationStatus() == null) {
			status = RegistrationStatusEnum.Added.getStatus();
		} else {
			status = messageSet.getRegistrationStatus();
		}
		statusCombo = new SummaryRowComboComposite(summaryComposite, new ComboPolicy(ComboPolicy.COMBO_COMMONLY_TYPE, 428,"Registration Status", isEditable, list, status)).getCombo();

		String removalDate = messageSet == null || messageSet.getRemovalDate() == null ? ""
				: DateFormat.getInstance().format(messageSet.getRemovalDate());
		
		removalDateValue = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_SHORT_TYPE, 456, "Removal Date", isEditable, removalDate, new MrEditorModifyListener(this))).getText();
		// ===== Registration Information Group End ===============================

		// ========================Documentation Information Begin==================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 506, I18nApi.get("editor.title.di")));
		
		String title = titleValue == null ? "" : titleValue.getText();
		titleValue = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 546, "Title", isEditable, title)).getText();

		String subTitle = subTitleValue == null ? "" : subTitleValue.getText();
		subTitleValue = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_SCROLL_TYPE, 574, "Subtitle", isEditable, subTitle)).getText();

		String publishingDate = publishingDateValue == null ? "" : publishingDateValue.getText();
		publishingDateValue = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 779, "Publishing Date", isEditable, publishingDate)).getText();
		// ======================Documentation Information End ==================

		// =================== Exampel Group Begin ==============================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 829, I18nApi.get("editor.title.es")));
		Button addExamplesBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR))).getButton();
		addExamplesBtn.setBounds(10, 869, 30, 30);
		addExamplesBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MrEditorDialogCreator.createExamplesDialogue(examplesTable);
				setDirty(true);
			}
		});
		Button deleteExamplesBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES))).getButton();
		deleteExamplesBtn.setBounds(42, 869, 30, 30);
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
		examplesTable.setBounds(10, 904, 400, 150);
		examplesTable.layout();
		if (this.messageSet != null && this.messageSet.getId() != null) {
			MrEditorHelper.setExamplesTable(examplesTable, this.messageSet.getId());
		}
		// =================== Exampel Group End ==============================

		// ===================== Constraints Group Begin====================================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 1079, I18nApi.get("editor.title.cs")));
		Button addConstraintBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR))).getButton();
		addConstraintBtn.setBounds(10, 1119, 30, 30);
		addConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MrEditorDialogCreator.createConstraintDialogueForBAandMS("add", constraintsTable);
				setDirty(true);
			}
		});

		Button deleteConstraintBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES))).getButton();
		deleteConstraintBtn.setBounds(42, 1119, 30, 30);
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
		constraintsTable.setBounds(10, 1154, 600, 100);
		constraintsTable.layout();
		new SummaryTableComposite(constraintsTable, 149, "Name");
		new SummaryTableComposite(constraintsTable, 149, "Definiton");
		new SummaryTableComposite(constraintsTable, 149, "Expression Language");
		new SummaryTableComposite(constraintsTable, 149, "Expression");

		if (this.messageSet != null && this.messageSet.getId() != null) {
			MrEditorHelper.setConstraintTable(constraintsTable, this.messageSet.getId(), null);
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
		rightComposite.setBounds(510, 0, 500, 1020);
		rightComposite.layout();
		
		// ------------ Content Expand Bar Begin ----------------------------------
		Composite leftCom = new Composite(leftComposite, SWT.NONE);
		leftCom.setBounds(0, 5, 490, 730);
		leftCom.layout();
		
		
		
		// ------------ Content Expand Bar Begin ----------------------------------
		new SummaryRowTextComposite(leftCom, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 0, I18nApi.get("editor.title.ct")));
		Button plusButton = new SummaryRowTextComposite(leftCom, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR))).getButton();
		plusButton.setBounds(5, 35, 30, 30);
		plusButton.addMouseListener(new PlusButtonListener());
		this.setUnavailable(plusButton);

		Button deleteButton = new SummaryRowTextComposite(leftCom, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES))).getButton();
		deleteButton.setBounds(42, 35, 30, 30);
		deleteButton.addMouseListener(new DeleteButtonListener());
		this.setUnavailable(deleteButton);

		elementsTree = new Tree(leftCom, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
		elementsTree.setBounds(5, 70, 485, 650);
		elementsTree.layout();

		this.generateContentTree();
		// ------------ Content Expand Bar End ----------------------------------

		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
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
						mbbNameValue.setText(msgBB.getName());
						mbbNameValue.setEditable(false);
						mbbDocumentationValue.setText(msgBB.getDefinition());
						mbbDocumentationValue.setEditable(false);
						mbbMinOccursValue.setText(String.valueOf(msgBB.getMinOccurs()));
						mbbMinOccursValue.setEditable(false);
						mbbMaxOccursValue.setText(String.valueOf(msgBB.getMaxOccurs()));
						mbbMaxOccursValue.setEditable(false);
						xmlTagValueOfContentPage.setText(msgBB.getXmlTag() == null ? "" : msgBB.getXmlTag());
						xmlTagValueOfContentPage.setEditable(false);
						typeValue.setText(treeItem.getData("typeName") == null ? ""
								: String.valueOf(treeItem.getData("typeName")));
						typeValue.setEditable(false);
						mbbObjectIdentifierValue
								.setText(msgBB.getObjectIdentifier() == null ? "" : msgBB.getObjectIdentifier());
						mbbObjectIdentifierValue.setEditable(false);
						MrEditorHelper.setConstraintTable(mbbConstraintsTable, msgBB.getId(), treeItem);
						MrEditorHelper.setSynonymsTable(mbbSynonymsTable, msgBB.getId(), treeItem);
					} else if (!constraintId.equalsIgnoreCase("null")) {
						showConstraintBar(rightComposite, scrolledComposite);
						EcoreConstraint constraint = (EcoreConstraint) treeItem.getData("constraint");
						constraintNameValue.setText(constraint.getName() == null ? "" : constraint.getName());
						constraintNameValue.setEditable(false);
						constraintDocValue
								.setText(constraint.getDefinition() == null ? "" : constraint.getDefinition());
						constraintDocValue.setEditable(false);
						expressionValue.setText(constraint.getExpression() == null ? "" : constraint.getExpression());
						expressionValue.setEditable(false);
						expressionLanguageValue.setText(
								constraint.getExpressionlanguage() == null ? "" : constraint.getExpressionlanguage());
						expressionLanguageValue.setEditable(false);
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
						codeDetailNameValue.setText(ecoreCode.getName());
						codeDetailNameValue.setEditable(false);
						codeDocumentValue.setText(ecoreCode.getDefinition());
						codeDocumentValue.setEditable(false);
						codeNameValue.setText(ecoreCode.getCodeName());
						codeNameValue.setEditable(false);
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

	private void generateContentTree() {
		if (messageDefinitionList == null) {
			return;
		}
		for (EcoreMessageDefinition msgDefinition : messageDefinitionList) {
			TreeItem treeItem = new TreeItem(elementsTree, SWT.NONE);
			treeItem.setText(msgDefinition.getName());
			treeItem.setData("msgDefinitionId", msgDefinition.getId());
			msgDefinitionIdIncluded.add(msgDefinition.getId());
			if (RegistrationStatusEnum.Registered.getStatus().equals(msgDefinition.getRegistrationStatus())) {
				treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB1));
			}else if (RegistrationStatusEnum.Obsolete.getStatus().equals(msgDefinition.getRegistrationStatus())) {
				treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB4));
			} else {
				treeItem.setImage(ImgUtil.createImg(ImgUtil.MESSAGE_DEFINITIONS));
			}
			List<EcoreMessageBuildingBlock> embbs = mbbs.stream().filter(b -> msgDefinition.getId().equals(b.getMessageId())).collect(Collectors.toList());
			MrEditorTreeCreator mrEditorTreeCreator = new MrEditorTreeCreator();
			mrEditorTreeCreator.generateContentTreeForMessageDefinition(treeItem, embbs, null, msgDefinition.getRegistrationStatus());
			List<EcoreConstraint> ecs = MrHelper.findConstraintsByObjId(css, msgDefinition.getId());
			mrEditorTreeCreator.addConstraintsNode(treeItem, ecs, null, null);
		}

	}

	void createMessageBox(List<EcoreMessageDefinition> msgDefinitions) {
		Shell messageDefinitionWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		messageDefinitionWindow.setText("Select Message Definition to Add");
		messageDefinitionWindow.setLayout(new FormLayout());

		// 改变弹窗位置
		SystemUtil.center(messageDefinitionWindow);

		Composite c = new Composite(messageDefinitionWindow, SWT.NONE);
		FormData fd_c = new FormData();
		fd_c.top = new FormAttachment(0);
		fd_c.left = new FormAttachment(0);
		fd_c.bottom = new FormAttachment(100);
		fd_c.right = new FormAttachment(100);
		Text searchText = new Text(c, SWT.BORDER);
		searchText.setBounds(10, 10, 280, 30);
		Button searchBtn = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,"Search")).getButton();
		searchBtn.setBounds(300, 10, 50, 30);

		Table msgDefinitionTable = new Table(c, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
		msgDefinitionTable.setLinesVisible(true);

		this.generateAllMsgDefinitionTable(msgDefinitionTable, msgDefinitions);

		msgDefinitionTable.setBounds(10, 50, 350, 400);

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
							} else {
								tableItem.setImage(ImgUtil.createImg(ImgUtil.MESSAGE_DEFINITIONS));
							}
						});
			}
		});
	}

	private void generateAllMsgDefinitionTable(Table msgDefinitionLisTable,
			List<EcoreMessageDefinition> msgDefinitions) {
		for (EcoreMessageDefinition definition : msgDefinitions) {
			TableItem tableItem = new TableItem(msgDefinitionLisTable, SWT.NONE);
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
		msgDefinitionLisTable.setSelection(0);
	}

	public EcoreMessageSetVO getEcoreMessageSetVO() {
		EcoreMessageSet msgSet = this.getMessageSet();
		EcoreMessageSetVO ecoreMessageSetVO = new EcoreMessageSetVO();
		ecoreMessageSetVO.setEcoreMessageSet(msgSet);
		ecoreMessageSetVO.setEcoreMessageSetDefinitionRLs(this.getMsgSetDefinitionRLList(msgSet));
		ecoreMessageSetVO.setEcoreExamples(this.getEcoreExampleTable(msgSet));
		ecoreMessageSetVO.setEcoreConstraints(this.getEcoreConstraintList(msgSet));
		return ecoreMessageSetVO;
	}

	private EcoreMessageSet getMessageSet() {

		messageSet.setName(this.nameValue.getText());
		messageSet.setDefinition(this.documentationValue.getText());
		messageSet.setObjectIdentifier(this.objectIdentifierValue.getText());
		messageSet.setRegistrationStatus(this.statusCombo.getText());
		return messageSet;

	}

	private ArrayList<EcoreMessageSetDefinitionRL> getMsgSetDefinitionRLList(EcoreMessageSet messageSet) {
		ArrayList<EcoreMessageSetDefinitionRL> rlList = new ArrayList<>();
		for (String msgDefinitionId : this.msgDefinitionIdIncluded) {
			EcoreMessageSetDefinitionRL rl = new EcoreMessageSetDefinitionRL();
			rl.setMessageId(msgDefinitionId);
			rl.setSetId(messageSet.getId());
			rlList.add(rl);
		}
		return rlList;
	}

	private ArrayList<EcoreExample> getEcoreExampleTable(EcoreMessageSet messageSet) {
		ArrayList<EcoreExample> ecoreExampleList = new ArrayList<>();
		for (TableItem tableItem : this.examplesTable.getItems()) {
			EcoreExample ecoreExample = (EcoreExample) tableItem.getData();
			ecoreExample.setObjId(messageSet.getId());
			ecoreExample.setObjType(ObjTypeEnum.MessageSet.getType());
			ecoreExampleList.add(ecoreExample);
		}
		return ecoreExampleList;
	}

	private ArrayList<EcoreConstraint> getEcoreConstraintList(EcoreMessageSet messageSet) {
		ArrayList<EcoreConstraint> ecoreConstraintList = new ArrayList<>();
		for (TableItem tableItem : this.constraintsTable.getItems()) {
			if (tableItem.getData() != null) {
				EcoreConstraint ecoreConstraint = (EcoreConstraint) tableItem.getData();
				ecoreConstraint.setObj_id(messageSet.getId());
				ecoreConstraint.setObj_type(ObjTypeEnum.MessageSet.getType());
				ecoreConstraintList.add(ecoreConstraint);
			}
		}
		return ecoreConstraintList;
	}

	class PlusButtonListener extends MouseAdapter {

		@Override
		public void mouseUp(MouseEvent e) {
			createMessageBox(MrImplManager.get().getEcoreMessageDefinitionImpl().findAll());
			setDirty(true);
		}
	}

	class DeleteButtonListener extends MouseAdapter {

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
					mrEditorTreeCreator.generateContentTreeForMessageDefinition(treeNode, MrHelper.findMsgBuildingBlocksByMsgId(mbbs, msgDefinitionId), null, registrationStatus);
					// Message Definition下的约束
					mrEditorTreeCreator.addConstraintsNode(treeNode, MrHelper.findConstraintsByObjId(css, msgDefinitionId), null, null);
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

	@Override
	public void setDirty(boolean dirty) {
		if (dirty && (RegistrationStatusEnum.Registered.getStatus().equals(this.ecoreTreeNode.getRegistrationStatus())
				|| RegistrationStatusEnum.Obsolete.getStatus().equals(this.ecoreTreeNode.getRegistrationStatus()))) {
			return;
		}
		super.setDirty(dirty);
	}

	/**
	 * 右边 Building Block 区域的显示和编辑
	 * 
	 * @param composite
	 * @param scrolledComposite
	 */
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

		Button dotBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,"choose")).getButton();
		dotBtn.setBounds(390, 340, 50, 25);
		this.setUnavailable(dotBtn);

		Button typeBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_PUSH_DEEP,ImgUtil.createImg(ImgUtil.SEARCH_BTN))).getButton();
		typeBtn.setBounds(445, 340, 25, 25);
		typeBtn.addMouseListener(new MrSearchMouseAdapter(this.elementsTree));
		// ------------ Message Building block Details End ------------------------

		// -----------------------CMP Information expand Begin -----------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 390, I18nApi.get("editor.title.cmp")));
		
		mbbObjectIdentifierValue = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 430, "Object Identifier")).getText();
		this.setUnavailable(mbbObjectIdentifierValue);
		// -----------------------CMP Information expand End -----------------------

		// ---------- Synonyms Expand Item Begin -------------------------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 480, I18nApi.get("editor.title.ss")));

		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_NOTE_ONE_TYPE, 520, "This section show the synonyms of object"));
		

		Button addSynonymsBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR), false)).getButton();
		addSynonymsBtn.setBounds(10, 548, 30, 30);
		Button deleteButtonForSynonyms = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES), false)).getButton();
		deleteButtonForSynonyms.setBounds(42, 548, 30, 30);
		
		mbbSynonymsTable = new Table(rightComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		mbbSynonymsTable.setHeaderVisible(true);
		mbbSynonymsTable.setLinesVisible(true);
		mbbSynonymsTable.setBounds(10, 585, 480, 150);
		mbbSynonymsTable.layout();

		new SummaryTableComposite(mbbSynonymsTable, 238, "Context");
		new SummaryTableComposite(mbbSynonymsTable, 238, "Synonums");
		// ---------- Synonyms Expand Item End -------------------------------------

		// ---------- Constraints Expand Item Begin -------------------------------------
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
		// ------------ Code Details Bar End ---------------------------------
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
