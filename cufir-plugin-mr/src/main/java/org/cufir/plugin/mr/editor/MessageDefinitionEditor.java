package org.cufir.plugin.mr.editor;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
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
import org.cufir.s.data.bean.EcoreBusinessArea;
import org.cufir.s.data.bean.EcoreCode;
import org.cufir.s.data.bean.EcoreConstraint;
import org.cufir.s.data.bean.EcoreDataType;
import org.cufir.s.data.bean.EcoreExample;
import org.cufir.s.data.bean.EcoreMessageBuildingBlock;
import org.cufir.s.data.bean.EcoreMessageComponent;
import org.cufir.s.data.bean.EcoreMessageDefinition;
import org.cufir.s.data.bean.EcoreMessageElement;
import org.cufir.s.data.bean.EcoreMessageSet;
import org.cufir.s.data.bean.EcoreNextVersions;
import org.cufir.s.data.vo.EcoreMessageBuildingBlockVO;
import org.cufir.s.data.vo.EcoreMessageDefinitionVO;
import org.cufir.s.data.vo.EcoreTreeNode;
import org.cufir.s.data.vo.SynonymVO;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
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
import org.springframework.util.StringUtils;

import com.cfets.cufir.s.ide.utils.i18n.I18nApi;

/**
 * Message Definition（报文）显示和编辑
 * 
 * @author gongyi_tt
 *
 */
public class MessageDefinitionEditor extends MrMultiPageEditor {

	private java.util.List<EcoreMessageComponent> mcs = MrRepository.get().ecoreMessageComponents;
	private java.util.List<EcoreConstraint> css = MrRepository.get().ecoreConstraints;
	private java.util.List<EcoreMessageElement> mes = MrRepository.get().ecoreMessageElements;
	private java.util.List<EcoreCode> cs = MrRepository.get().ecoreCodes;
	private java.util.List<EcoreMessageBuildingBlock> mbbs =  MrRepository.get().ecoreMessageBuildingBlocks;
	private java.util.List<EcoreMessageDefinition> mds =  MrRepository.get().ecoreMessageDefinitions;
	private Map<String, EcoreMessageDefinition> emdMapById = MrRepository.get().ecoreMessageDefinitionMapById;
	
	private MrEditorInput myEditorInput;

	public TreeItem modelExploreTreeItem;

	private EcoreMessageDefinition msgDefinition;

	private EcoreBusinessArea businessArea;

	private java.util.List<EcoreMessageBuildingBlock> mbbList;

	private java.util.List<EcoreMessageSet> messageSet;

	private java.util.List<EcoreMessageDefinition> nextMsgDefinitions;

	private EcoreTreeNode ecoreTreeNode;

	// -------------for Summary Page use-----------------
	public Text nameText;
	private Text documentationText;
	private Text messageDefinitionIdentifierText;
	private Text xmlTagText;
	private Text purposeText;
	private Text rootElementText;
	private Text objectIdentifierText;
	private Combo statusCombo;
	private Text removalDateValue;
	private Table synonymsTable;
	private Table examplesTable;
	private TreeItem selectedTableItem;
	// -----------For Content Page ------------
	private Tree elementsTree;
	private Text mbbXmlTagText;
	private Text mbbNameText;
	private Text mbbDocumentationText;
	private Text mbbMinOccursText;
	private Text mbbMaxOccursText;
	private Table mbbSynonymsTable;
	private Text mbbTypeText;
	private Button mbbIsDerivedButton;
	private Button mbbChooseButton;
	private Button addMbbSynonymsBtn;
	private Button deleteMbbSynonymsBtn;
	private Text mbbObjectIdentifierValue;
	private Table mbbConstraintsTable;
	private Text constraintNameValue;
	private Text constraintDocValue;
	private Text expressionValue;
	private Text expressionLanguageValue;
	private Text codeDetailNameValue;
	private Text codeDocumentValue;
	private Text codeNameValue;
	private Button addMbbConstraintsBtn;
	private Button deleteMbbConstraintsBtn;

	// ----------For Business Trace Page -----
	private Table msgElementTable;

	// ----------For Impact Analysis Page -----
	private Tree containmentTree;

	// ----------For Version/Subsets Page -----
	private Table nextMsgDefinitionListWidget;
	private List nextVersionListWidget;
	private List previousVersionListWidget;

	@Override
	public void setPartName(String partName) {
		super.setPartName(partName);
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		
		this.setSite(site);
		this.setInput(input);
		this.setPartProperty("customName", "msgDefinitionCreate");
		this.myEditorInput = (MrEditorInput) input;
		modelExploreTreeItem = this.myEditorInput.getTransferDataBean().getTreeListItem();
		ecoreTreeNode = (EcoreTreeNode) modelExploreTreeItem.getData("EcoreTreeNode");
		// 设置标题图片
		this.setTitleImage(ImgUtil.createImg(this.myEditorInput.getTransferDataBean().getImgPath()));
		setContext(site);
	}

	public EcoreMessageDefinition getMsgDefinition() {
		return msgDefinition;
	}

	/**
	 * 保存时获取数据
	 * @return
	 */
	public EcoreMessageDefinitionVO getEcoreMessageDefinitionVO() {
		msgDefinition.setName(this.nameText.getText());
		msgDefinition.setDefinition(this.documentationText.getText());
		msgDefinition.setRegistrationStatus(statusCombo.getText());
		msgDefinition.setRootElement(this.rootElementText.getText());
		msgDefinition.setObjectIdentifier(this.objectIdentifierText.getText());
		String msgDefiIdentiInputValue = this.messageDefinitionIdentifierText.getText();
		if (msgDefiIdentiInputValue.matches("([0-9A-Za-z]){1,}\\.([0-9A-Za-z]){1,}\\.([0-9A-Za-z]){1,}\\.([0-9A-Za-z]){1,}")) {
			msgDefinition.setMessageDefinitionIdentifier(msgDefiIdentiInputValue);
		} else {
			msgDefinition.setMessageDefinitionIdentifier("");
		}
		msgDefinition.setRegistrationStatus(this.statusCombo.getText());
		msgDefinition.setXmlTag(this.xmlTagText.getText());
		msgDefinition.setIsfromiso20022(emdMapById.get(msgDefinition.getId()) == null ? null : emdMapById.get(msgDefinition.getId()).getIsfromiso20022());

		EcoreMessageDefinitionVO ecoreMessageDefinitionVO = new EcoreMessageDefinitionVO();
		ecoreMessageDefinitionVO.setEcoreMessageDefinition(msgDefinition);
		//设置EcoreExample
		java.util.List<EcoreExample> ecoreExampleList = new ArrayList<>();
		for (TableItem tableItem : this.examplesTable.getItems()) {
			EcoreExample ecoreExample = (EcoreExample) tableItem.getData();
			ecoreExample.setObjId(msgDefinition.getId());
			ecoreExample.setObjType(ObjTypeEnum.MessageDefinition.getType());
			ecoreExampleList.add(ecoreExample);
		}
		ecoreMessageDefinitionVO.setEcoreExamples(ecoreExampleList);
		//设置SynonymVO
		java.util.List<SynonymVO> synonymVOList = new ArrayList<SynonymVO>();
		for (TableItem tableItem : synonymsTable.getItems()) {
			SynonymVO synonymsVO = new SynonymVO();
			synonymsVO.setContext(tableItem.getText(0));
			synonymsVO.setSynonym(tableItem.getText(1));
			synonymsVO.setObjId(msgDefinition.getId());
			synonymVOList.add(synonymsVO);
		}
		ecoreMessageDefinitionVO.setSynonyms(synonymVOList);
		//设置EcoreMessageBuildingBlockVOs
		java.util.List<EcoreMessageBuildingBlockVO> ecoreMessageBuildingBlockVOList = new ArrayList<>();
		ArrayList<EcoreConstraint> constraintList = new ArrayList<>();
		for (TreeItem mbbTreeItem : elementsTree.getItems()) {
			Object object = mbbTreeItem.getData("msgBldBlock");
			if (object != null) {
				EcoreMessageBuildingBlockVO ecoreMessageBuildingBlockVO = new EcoreMessageBuildingBlockVO();
				EcoreMessageBuildingBlock ecoreMessageBuildingBlock = (EcoreMessageBuildingBlock) object;
				ecoreMessageBuildingBlockVO.setEcoreMessageBuildingBlock(ecoreMessageBuildingBlock);
				object = mbbTreeItem.getData("mbbSynonymsTableItems");
				if (object != null) {
					java.util.List<SynonymVO> synonymVOs = new ArrayList<>();
					java.util.List<String> synonymsList = (ArrayList) object;
					synonymsList.forEach(s ->{
						SynonymVO synonymVO = new SynonymVO();
						String key = s.split(",")[0];
						String value = s.split(",")[1];
						synonymVO.setObjId(ecoreMessageBuildingBlock.getId());
						synonymVO.setContext(key);
						synonymVO.setSynonym(value);
						synonymVOs.add(synonymVO);
					});
					ecoreMessageBuildingBlockVO.setSynonyms(synonymVOs);
				}
				object = mbbTreeItem.getData("mbbConstraintList");
				if (object != null) {
					java.util.List<EcoreConstraint> ecs = (ArrayList) object;
					ecs.forEach(ec ->{
						ec.setObj_type(ObjTypeEnum.MessageBlock.getType());
						ec.setObj_id(ecoreMessageBuildingBlock.getId());
					});
					ecoreMessageBuildingBlockVO.setEcoreConstraints(ecs);
				}
				ecoreMessageBuildingBlockVOList.add(ecoreMessageBuildingBlockVO);
			} else if (mbbTreeItem.getData("isNewConstraint") != null) {
				EcoreConstraint ecoreConstraints = new EcoreConstraint();
				ecoreConstraints.setId(UUID.randomUUID().toString());
				ecoreConstraints.setName(String.valueOf(mbbTreeItem.getData("name")));
				ecoreConstraints.setDefinition(String.valueOf(mbbTreeItem.getData("document")));
				ecoreConstraints.setExpression(String.valueOf(mbbTreeItem.getData("expression") == null ? "" : mbbTreeItem.getData("expression")));
				ecoreConstraints.setExpressionlanguage(String.valueOf(mbbTreeItem.getData("expressionLanguage") == null ? "" : mbbTreeItem.getData("expressionLanguage")));
				ecoreConstraints.setObj_id(msgDefinition.getId());
				ecoreConstraints.setObj_type(ObjTypeEnum.MessageDefinition.getType());
				constraintList.add(ecoreConstraints);
				ecoreMessageDefinitionVO.setEcoreConstraints(constraintList);
			} else if (mbbTreeItem.getData("constraint") != null) {
				constraintList.add((EcoreConstraint) mbbTreeItem.getData("constraint"));
				ecoreMessageDefinitionVO.setEcoreConstraints(constraintList);
			}
		}
		ecoreMessageDefinitionVO.setEcoreMessageBuildingBlockVOs(ecoreMessageBuildingBlockVOList);
		return ecoreMessageDefinitionVO;
	}

	private void loadMessageDefinitionData() {
		TransferDataBean transferDataBean = this.myEditorInput.getTransferDataBean();
		String msgDefinitionId = transferDataBean.getId();
		this.setPartProperty("ID", msgDefinitionId);
		msgDefinition = MrHelper.getMsgDefinitionById(mds, msgDefinitionId);
		messageSet = MrImplManager.get().getEcoreMessageSetImpl().findByMsgDefinitionId(msgDefinitionId);
		businessArea = MrHelper.getBusinessAreaByBizAreaId(msgDefinition.getBusinessAreaId());
		java.util.List<String> nextVersionIds = MrImplManager.get().getEcoreNextVersionsImpl().findNextVersionIdById(msgDefinitionId);
		nextMsgDefinitions = MrHelper.getMsgDefinitionListByIds(nextVersionIds);
	}

	@Override
	protected void createPages() {
		loadMessageDefinitionData();
		if (msgDefinition.getId() == null) {
			msgDefinition.setId(this.myEditorInput.getTransferDataBean().getId());
			this.setPartName("");
			this.setDirty(true);
		} else {
			this.setDirty(false);
			this.setPartName(msgDefinition.getName());
		}
		createSummaryPage();
		createContentPage();
		createBusinessTrace();
		createImapctPage();
		createVersionAndSubsets();

		String msgBldBlockId = this.myEditorInput.getTransferDataBean().getChildId();
		if (msgBldBlockId != null && !msgBldBlockId.isEmpty()) {
			this.setActivePage(1); // make content page to be current active page
			for (TreeItem treeItem : this.elementsTree.getItems()) {
				Object object = treeItem.getData("msgBldBlock");
				if (object != null) {
					EcoreMessageBuildingBlock msgBldBlock = (EcoreMessageBuildingBlock) object;
					if (msgBldBlockId.equals(msgBldBlock.getId())) {
						this.elementsTree.setSelection(treeItem);
					}
				}
			}
		}
	}

	/**
	 * Summary Page 显示和编辑
	 */
	void createSummaryPage() {
		// 是否可编辑
		boolean isEditable = !RegistrationStatusEnum.Registered.getStatus().equals(this.ecoreTreeNode.getRegistrationStatus());
		// 显示
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
		
		// 名称
		String name = msgDefinition == null ? "" : msgDefinition.getName();
		MouseAdapter mouseAdapter = new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MessageHelper.generateName(nameText, mds);
			}
		};
		ModifyListener modifyListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				modelExploreTreeItem.setText(nameText.getText() == null ? "" : nameText.getText());
				setPartName(nameText.getText());
				setDirty(true);
			}
		};
		nameText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_BUTTON_TYPE, 45, "Name", isEditable, name, modifyListener, mouseAdapter)).getText();

		String documentation = msgDefinition == null ? "" : msgDefinition.getDefinition();
		documentationText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_SCROLL_TYPE, 73, "Documentation", isEditable, documentation, new MrEditorModifyListener(this))).getText();

		// 报文标识符
		boolean textNotIsNull = msgDefinition != null && (msgDefinition.getBusinessArea() != null && msgDefinition.getMessageFunctionality() != null
						&& msgDefinition.getFlavour() != null && msgDefinition.getVersion() != null);
		String messageDefinitionIdentifier = "";
		if (textNotIsNull) {
			messageDefinitionIdentifier = msgDefinition.getBusinessArea() + "."
					+ msgDefinition.getMessageFunctionality() + "." + msgDefinition.getFlavour() + "."
					+ msgDefinition.getVersion();
		}
		messageDefinitionIdentifierText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 278, "Message Definition Identifier", isEditable, messageDefinitionIdentifier, new MrEditorModifyListener(this))).getText();
		
		// XML标签
		String xmlTag = msgDefinition == null ? "" : msgDefinition.getXmlTag();
		xmlTagText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 306, "XML Tag", isEditable, xmlTag, new MrEditorModifyListener(this))).getText();
		
		// Purpose（未知，没有值）
		purposeText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_SCROLL_TYPE, 334, "Purpose", isEditable, "", new MrEditorModifyListener(this))).getText();
		
		// 根节点
		String rootElement = msgDefinition == null ? "" : msgDefinition.getRootElement();
		rootElementText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 539, "Root Element", isEditable, rootElement, new MrEditorModifyListener(this))).getText();
		// ================= General Information Group End ============================
		
		// ================= CMP Information Group Begin ==============================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 589, I18nApi.get("editor.title.cmp")));
		// 对象标识符
		String objectIdentifier = msgDefinition == null ? "" : msgDefinition.getObjectIdentifier();
		objectIdentifierText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 629, "Object Identifier", isEditable, objectIdentifier, new MrEditorModifyListener(this))).getText();
		// ================= CMP Information Group End ==============================
		
		// ===== Registration Information Group Begin ===============================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 679, I18nApi.get("editor.title.ri")));
		//状态
		java.util.List<String> list = new ArrayList<>();
		list.add(RegistrationStatusEnum.Registered.getStatus());
		list.add(RegistrationStatusEnum.Provisionally.getStatus());
		list.add(RegistrationStatusEnum.Added.getStatus());
		
		String statusText = "";
		if (msgDefinition.getRegistrationStatus() == null) {
			statusText = RegistrationStatusEnum.Added.getStatus();
		} else {
			statusText = msgDefinition.getRegistrationStatus();
		}
		statusCombo = new SummaryRowComboComposite(summaryComposite, new ComboPolicy(ComboPolicy.COMBO_COMMONLY_TYPE, 719,"Registration Status", false, list, statusText)).getCombo();
		
		// 删除时间
		String removalDateText = msgDefinition == null || msgDefinition.getRemovalDate() == null ? "" : DateFormat.getInstance().format(msgDefinition.getRemovalDate());
		removalDateValue = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_SHORT_TYPE, 747, "Removal Date", isEditable, removalDateText, new MrEditorModifyListener(this))).getText();
		// ===== Registration Information Group End ===============================
		
		// ========== Synonyms Group Begin ========================================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 797, I18nApi.get("editor.title.ss")));
		
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_NOTE_TYPE, 837, "This section show the synonyms of object"));
		
		//添加按钮
		Button addSynonymsBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR), isEditable)).getButton();
		addSynonymsBtn.setBounds(10, 865, 30, 30);
		addSynonymsBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MessageDefinitionEditorDialogContent.createSynonymsDialogue(synonymsTable);
				setDirty(true);
			}
		});
		
		//删除按钮
		Button deleteSynonymsBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES), isEditable)).getButton();
		deleteSynonymsBtn.setBounds(42, 865, 30, 30);
		deleteSynonymsBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				for (TableItem tableItem: synonymsTable.getSelection()) {
					tableItem.dispose();
				}
				setDirty(true);
			}
		});

		synonymsTable = new Table(summaryComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		synonymsTable.setHeaderVisible(true);
		synonymsTable.setLinesVisible(true);
		synonymsTable.setBounds(10, 900, 400, 150);
		synonymsTable.layout();
		new SummaryTableComposite(synonymsTable,198, "Context");
		new SummaryTableComposite(synonymsTable,198, "Synonums");
		
		if (msgDefinition != null && msgDefinition.getId() != null) {
			MessageHelper.generateSynonymsTable(synonymsTable, msgDefinition.getId(), null);
		}
		// ========== Synonyms Group End ==========================================
		
		// =================== Exampel Group Begin ==============================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 1075, I18nApi.get("editor.title.es")));
		
		//添加按钮
		Button addExamplesBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR), isEditable)).getButton();
		addExamplesBtn.setBounds(10, 1115, 30, 30);
		addExamplesBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MrEditorDialogCreator.createExamplesDialogue(examplesTable);
				setDirty(true);
			}
		});
		
		//删除按钮
		Button deleteExamplesBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES), isEditable)).getButton();
		deleteExamplesBtn.setBounds(42, 1115, 30, 30);
		deleteExamplesBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MrEditorDialogCreator.deleteExamplesDialogue(examplesTable);
				setDirty(true);
			}
		});
		
		//表格
		examplesTable = new Table(summaryComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		examplesTable.setHeaderVisible(true);
		examplesTable.setLinesVisible(true);
		examplesTable.setBounds(10, 1150, 600, 100);
		examplesTable.layout();
		if (msgDefinition != null && msgDefinition.getId() != null) {
			MessageHelper.generateExamplesTable(examplesTable, msgDefinition.getId());
		}
		// =================== Exampel Group End ==============================

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
		rightComposite.setBounds(510, 0, 500, 1015);
		rightComposite.layout();

		// ------------ Content Expand Bar Begin ----------------------------------
		Composite leftCom = new Composite(leftComposite, SWT.NONE);
		leftCom.setBounds(0, 5, 490, 730);
		leftCom.layout();

		// ------------ Content Expand Bar Begin ----------------------------------
		new SummaryRowTextComposite(leftCom, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 0, I18nApi.get("editor.title.ct")));

		Button plusButtonForMsgElement = new SummaryRowTextComposite(leftCom, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ME))).getButton();
		plusButtonForMsgElement.setBounds(5, 35, 30, 30);
		plusButtonForMsgElement.addMouseListener(new AddMsgElementListener());
		this.setUnavailable(plusButtonForMsgElement);

		Button plusButtonForConstraint = new SummaryRowTextComposite(leftCom, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.CONSTRAINT))).getButton();
		plusButtonForConstraint.setBounds(37, 35, 30, 30);
		plusButtonForConstraint.addMouseListener(new AddConstraintListener());
		this.setUnavailable(plusButtonForConstraint);

		Button deleteButtonForContent = new SummaryRowTextComposite(leftCom, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES))).getButton();
		deleteButtonForContent.setBounds(69, 35, 30, 30);
		deleteButtonForContent.addMouseListener(new DeleteTreeNodeListener(rightComposite));
		this.setUnavailable(deleteButtonForContent);

		elementsTree = new Tree(leftCom, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
		elementsTree.setBounds(5, 70, 485, 650);
		elementsTree.layout();
		if (this.msgDefinition != null && this.msgDefinition.getId() != null) {
			mbbList = MrHelper.findMsgBuildingBlocksByMsgId(mbbs, this.msgDefinition.getId());
			MrEditorTreeCreator mrEditorTreeCreator = new MrEditorTreeCreator();
			mrEditorTreeCreator.generateContentTreeForMessageDefinition(null, mbbList, elementsTree, msgDefinition.getRegistrationStatus());
			if (mbbList != null && mbbList.size() > 0) {
				//默认打开
				TreeItem item = elementsTree.getItem(0);
				if(item != null) {
					elementsTree.setSelection(item);
					elementListener(item, deleteButtonForContent, rightComposite, scrolledComposite);
				}
			}
			java.util.List<EcoreConstraint> ecs = MrHelper.findConstraintsByObjId(css, this.msgDefinition.getId());
			if(ecs != null && ecs.size() > 0) {
				mrEditorTreeCreator.addConstraintsNode(null, ecs, null, elementsTree);
			}
		}
		elementsTree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem treeItem_1 = (TreeItem) e.item;
				elementListener(treeItem_1, deleteButtonForContent, rightComposite, scrolledComposite);
			}
		});
		// ------------ Content Expand Bar End ----------------------------------
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	private void elementListener(TreeItem treeItem_1, Button deleteButtonForContent, Composite rightComposite, ScrolledComposite scrolledComposite) {
		TreeItem[] treeItems = elementsTree.getSelection();
		String name = treeItem_1.getText();
		for (TreeItem treeItem : treeItems) {
			selectedTableItem = treeItem;
			if (RegistrationStatusEnum.Provisionally.getStatus().equals(ecoreTreeNode.getRegistrationStatus())
					|| RegistrationStatusEnum.Added.getStatus().equals(ecoreTreeNode.getRegistrationStatus())) {
				if (treeItem.getParentItem() != null) {
					deleteButtonForContent.setEnabled(false);
				} else {
					deleteButtonForContent.setEnabled(true);
				}
			}
			String constraintId = String.valueOf(treeItem.getData("constraintId"));
			String msgElementId = String.valueOf(treeItem.getData("msgElementID"));
			String codeId = String.valueOf(treeItem.getData("codeId"));

			if (treeItem.getData("msgBldBlock") != null) {
				EcoreMessageBuildingBlock msgBB = (EcoreMessageBuildingBlock) treeItem.getData("msgBldBlock");
				showMessageBuildingBlockBar(rightComposite, msgBB.getName(), msgBB.getDefinition(),
						msgBB.getMinOccurs(), msgBB.getMaxOccurs(), msgBB.getXmlTag(), treeItem.getData("typeName"), msgBB.getObjectIdentifier());
				MessageHelper.generateConstraintTable(mbbConstraintsTable, msgBB.getId(), treeItem);
				MessageHelper.generateSynonymsTable(mbbSynonymsTable, msgBB.getId(), treeItem);
			} else if (!constraintId.equalsIgnoreCase("null")) {
				EcoreConstraint constraint = (EcoreConstraint) treeItem.getData("constraint");
				showConstraintBar(rightComposite, constraint.getName(), constraint.getDefinition(), constraint.getExpression(), constraint.getExpressionlanguage());
				if (treeItem.getParentItem() != null) { // Content页面右边，不是Message Definition下的Constraint
					constraintNameValue.setEditable(false);
					constraintDocValue.setEditable(false);
					expressionValue.setEditable(false);
					expressionLanguageValue.setEditable(false);
				}
			} else if (treeItem.getData("isNewConstraint") != null) {
				showConstraintBar(rightComposite, treeItem.getData("name"), treeItem.getData("document"),
						treeItem.getData("expression"), treeItem.getData("expressionLanguage"));
			} else if (!msgElementId.equalsIgnoreCase("null")) {
				EcoreMessageElement ecoreMessageElement = MrImplManager.get().getEcoreMessageElementImpl().getById(msgElementId);
				showMessageBuildingBlockBar(rightComposite,ecoreMessageElement.getName(),ecoreMessageElement.getDefinition(),
						ecoreMessageElement.getMinOccurs(), ecoreMessageElement.getMaxOccurs(), ecoreMessageElement.getXmlTag(),
						treeItem.getData("typeName"), ecoreMessageElement.getObjectIdentifier());
				mbbNameText.setEditable(false);
				mbbDocumentationText.setEditable(false);
				mbbMinOccursText.setEditable(false);
				mbbMaxOccursText.setEditable(false);
				mbbXmlTagText.setEditable(false);
				mbbTypeText.setEditable(false);
				mbbObjectIdentifierValue.setEditable(false);
				mbbIsDerivedButton.setEnabled(false);
				mbbChooseButton.setEnabled(false);
				addMbbSynonymsBtn.setEnabled(false);
				deleteMbbSynonymsBtn.setEnabled(false);
				addMbbConstraintsBtn.setEnabled(false);
				deleteMbbConstraintsBtn.setEnabled(false);
				MessageHelper.generateConstraintTable(mbbConstraintsTable, msgElementId, treeItem);
				MessageHelper.generateSynonymsTable(mbbSynonymsTable, msgElementId, null);
			} else if (!codeId.equalsIgnoreCase("null")) {
				EcoreCode ecoreCode = (EcoreCode) treeItem.getData("ecoreCode");
				showCodeDetailBar(rightComposite, ecoreCode);
				codeDetailNameValue.setEditable(false);
				codeDocumentValue.setEditable(false);
				codeNameValue.setEditable(false);
				MessageHelper.generateConstraintTable(mbbConstraintsTable, codeId, treeItem);
			}
			treeItem_1.setText(name);
		}
	}
	private void createBusinessTrace() {
		Composite parentComposite = getContainer();
		Composite composite = new Composite(parentComposite, SWT.NONE);
		composite.setLayout(new GridLayout(1, true));
		int index = addPage(composite);
		setPageText(index, I18nApi.get("editor.label.bt"));
		Label BusinessTraces = new Label(composite, SWT.NONE);
		BusinessTraces.setText(I18nApi.get("editor.title.bt"));
		BusinessTraces.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		BusinessTraces.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		Label descriptionLabel = new Label(composite, SWT.NONE);
		descriptionLabel.setText("This section describes the Business traces of the message elements to the business model.");
		msgElementTable = new SummaryTableComposite(composite, 1200, 700, 2).getTable();
		new SummaryTableComposite(msgElementTable,350, "Message Element");
		new SummaryTableComposite(msgElementTable,150, "Is Technical");
		new SummaryTableComposite(msgElementTable,150, "Traces to");
		new SummaryTableComposite(msgElementTable,150, "Trace Path");
		new SummaryTableComposite(msgElementTable,150, "Type of Message Element traces to");
		this.generatemsgElementTable();
	}
	
	/**
	 * Impact Page 显示和编辑
	 */
	void createImapctPage() {
		Composite parentComposite = getContainer();
		ScrolledComposite scrolledComposite = new ScrolledComposite(parentComposite, SWT.V_SCROLL);
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(composite);
		composite.setLayout(new GridLayout(2, false));
		int index = addPage(scrolledComposite);
		setPageText(index, I18nApi.get("editor.label.ia"));
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
		groupGridData.heightHint = 330;

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
		MrEditorTableCreator.generateMessageSetTableOfImpactPage(messageSetTable, messageSet);

		Group businessAreatGroup = new Group(groupListComposite, SWT.NONE);
		businessAreatGroup.setText(I18nApi.get("editor.title.ba"));
		businessAreatGroup.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		businessAreatGroup.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		businessAreatGroup.setLayoutData(groupGridData);
		businessAreatGroup.setLayout(new FillLayout(SWT.VERTICAL));
		Table businessAreaTable = new Table(businessAreatGroup, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
		businessAreaTable.setLinesVisible(true);
		TableCursor businessAreaTableCursor = new TableCursor(businessAreaTable, SWT.NONE);
		// 构造Business Area表格
		this.generateBusinessAreaTableOfImpactPage(businessAreaTable);

		Composite treeComposite = new Composite(composite, SWT.NONE);
		FillLayout treeCompositetFillLayout = new FillLayout(SWT.VERTICAL);
		treeComposite.setLayout(treeCompositetFillLayout);
		GridData treeCompositeGridData = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		treeCompositeGridData.widthHint = 600;
		treeCompositeGridData.heightHint = 700;
		treeComposite.setLayoutData(treeCompositeGridData);

		containmentTree = new Tree(treeComposite, SWT.BORDER);

		// For Message Set表格，添加点击事件
		messageSetTableCursor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				containmentTree.removeAll();
				TableItem tableItem = messageSetTableCursor.getRow();
				String msgSetName = tableItem.getText(messageSetTableCursor.getColumn());
				TreeItem treeRoot = new TreeItem(containmentTree, SWT.NONE);
				treeRoot.setText(msgSetName);
				treeRoot.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
				String messageSetId = (String) tableItem.getData(String.valueOf(messageSetTable.getSelectionIndex()));
				new MrEditorTreeCreator().generateContainmentTreeForMessageSetofImpactPage(treeRoot, MrImplManager.get().getEcoreMessageDefinitionImpl().findByMsgSetId(messageSetId));
				treeRoot.setExpanded(true);
			}
		});

		businessAreaTableCursor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				containmentTree.removeAll();
				TableItem tableItem = businessAreaTableCursor.getRow();
				String bizAreaName = tableItem.getText(businessAreaTableCursor.getColumn());
				TreeItem treeRoot = new TreeItem(containmentTree, SWT.NONE);
				treeRoot.setText(bizAreaName);
				treeRoot.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
				String bizAreaId = String.valueOf(tableItem.getData());
				new MrEditorTreeCreator().generateContainmentTreeForMessageSetofImpactPage(treeRoot,
						MrHelper.getMsgDefintionListByBizAreaId(bizAreaId));
			}
		});
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	/**
	 * Version Subset page 显示和编辑
	 */
	void createVersionAndSubsets() {
		Map<String, java.util.List<EcoreNextVersions>> envMapById = MrRepository.get().ecoreNextVersionsMapById;
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

		GridData lc_gd = new GridData(SWT.TOP, SWT.TOP, false, false, 1, 1);
		lc_gd.widthHint = 550;
		Composite leftComposite = new Composite(composite, SWT.NONE);
		leftComposite.setLayout(new FillLayout(SWT.VERTICAL));
		leftComposite.setLayoutData(lc_gd);

		GridData rc_gd = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		rc_gd.widthHint = 550;
		Composite rightComposite = new Composite(composite, SWT.NONE);
		rightComposite.setLayout(new FillLayout(SWT.VERTICAL));
		rightComposite.setLayoutData(rc_gd);

		// ------------ Next Version Bar Begin ----------------------------------
		ExpandBar nextVersionBar = new SummaryExpandComposite(leftComposite, 0).getExpandBar();
		
		Composite nextVersionBarComposite = new Composite(nextVersionBar, SWT.NONE);
		nextVersionBarComposite.setLayout(new FillLayout(SWT.VERTICAL));

		nextVersionListWidget = new List(nextVersionBarComposite, SWT.BORDER);
		nextVersionListWidget.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		if (msgDefinition.getId() != null) {
			java.util.List<EcoreNextVersions> ecoreNextVersionses = envMapById.get(msgDefinition.getId());
			if(ecoreNextVersionses != null && ecoreNextVersionses.size() > 0) {
				ecoreNextVersionses.forEach(env ->{
					EcoreMessageDefinition emdNextVersions = emdMapById.get(env.getNextVersionId());
					if(emdNextVersions == null) {
						nextVersionListWidget.removeAll();
						nextVersionListWidget.add(I18nApi.get("editor.title.nv.tips"));
					}else {
						nextVersionListWidget.add(emdNextVersions.getName() == null ? "" : emdNextVersions.getName());
						nextVersionListWidget.setData("nextVersionId", emdNextVersions.getId());
					}
				});
			}else {
				nextVersionListWidget.removeAll();
				nextVersionListWidget.add(I18nApi.get("editor.title.nv.tips"));
			}
		}

		new SummaryExpandComposite(nextVersionBar, nextVersionBarComposite, I18nApi.get("editor.title.nv"), 350);
		// ------------ Next Version Bar End ----------------------------------

		// ------------ Previous Version Bar Begin ------------------------
		ExpandBar previousVersionBar = new SummaryExpandComposite(rightComposite, 1).getExpandBar();

		Composite previousVersionBarComposite = new Composite(previousVersionBar, SWT.NONE);
		previousVersionBarComposite.setLayout(new FillLayout(SWT.VERTICAL));

		previousVersionListWidget = new List(previousVersionBarComposite, SWT.BORDER);
		previousVersionListWidget.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));
		if (!StringUtils.isEmpty(msgDefinition.getId())) {
			setPreviousVersion(emdMapById);
		}
		new SummaryExpandComposite(previousVersionBar, previousVersionBarComposite, I18nApi.get("editor.title.pv"), 350);
		// ------------ Previous Version Bar End ------------------------
	}
	
	/**
	 * 设置历史版本
	 * @param emdMapById
	 */
	public void setPreviousVersion(Map<String, EcoreMessageDefinition> emdMapById) {
		Map<String, EcoreNextVersions> envMapByNextVersionId = MrRepository.get().ecoreNextVersionsMapByNextVersionId;
		java.util.List<EcoreNextVersions> list = new ArrayList<>();
		java.util.List<EcoreNextVersions> EcoreNextVersionses = MessageHelper.getPreviousVersions(envMapByNextVersionId, msgDefinition.getId(), list);
		previousVersionListWidget.removeAll();
		if(list != null && list.size() > 0) {
			for(EcoreNextVersions ecoreNextVersions : EcoreNextVersionses) {
				EcoreMessageDefinition emdNextVersions = emdMapById.get(ecoreNextVersions.getId());
				previousVersionListWidget.add(emdNextVersions.getName() == null ? "" : emdNextVersions.getName());
				previousVersionListWidget.setData("nextVersionId", emdNextVersions.getId());
			}
		}else {
			previousVersionListWidget.add(I18nApi.get("editor.title.pv.tips"));
		}
	}

	private void generatemsgElementTable() {
		if (mbbList == null) {
			return;
		}
		String[] valueArray = new String[5];
		for (EcoreMessageBuildingBlock mbb : mbbList) {
			valueArray[0] = mbb.getName();
			if ("2".equals(mbb.getDataType())) {
				EcoreMessageComponent mc = MrHelper.getMessageComponentById(mcs, mbb.getDataTypeId());
				valueArray[2] = mc.getTrace();
			}
			TableItem tableItem = new TableItem(msgElementTable, SWT.NONE);
			tableItem.setText(valueArray);
			tableItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
		}
	}

	private void generateBusinessAreaTableOfImpactPage(Table tableComposite) {
		if (businessArea.getId() != null) {
			TableItem tableItem = new TableItem(tableComposite, SWT.NONE);
			tableItem.setImage(ImgUtil.createImg(ImgUtil.BUSINESS_AREAS));
			StringBuilder sb = new StringBuilder(businessArea.getName());
			if (businessArea.getCode() != null) {
				sb.append(" - " + "(" + businessArea.getCode() + ")");
			}
			tableItem.setText(sb.toString());
			tableItem.setData(businessArea.getId());
		}
	}

	void createMessageBox(java.util.List<EcoreMessageDefinition> msgDefinitions) {
		Shell messageDefinitionWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		messageDefinitionWindow.setText("Select Message Definition to Add");
		messageDefinitionWindow.setLayout(new FormLayout());

		// 改变弹窗位置
		SystemUtil.center(messageDefinitionWindow);

		Composite c = new Composite(messageDefinitionWindow, SWT.NONE);
		Text searchText = new Text(c, SWT.BORDER);
		searchText.setBounds(10, 10, 350, 30);

		Table msgDefinitionTable = new Table(c, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
		msgDefinitionTable.setLinesVisible(true);

		this.generateAllMsgDefinitionTable(msgDefinitionTable, msgDefinitions);

		msgDefinitionTable.setBounds(10, 50, 350, 400);

		Button okButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_PUSH_DEEP,"OK")).getButton();
		okButton.setBounds(275, 460, 35, 30);
		okButton.addMouseListener(new OkButtonListenerForVersion(msgDefinitionTable, messageDefinitionWindow));

		Button cancelButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_PUSH_DEEP,"Cancel")).getButton();
		cancelButton.setBounds(320, 460, 52, 30);
		cancelButton.addMouseListener(new CancelButtonListener(messageDefinitionWindow));

		messageDefinitionWindow.setSize(400, 550);
		messageDefinitionWindow.open();
		messageDefinitionWindow.layout();

		searchText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {

				msgDefinitionTable.removeAll();

				msgDefinitions.stream()
						.filter(t -> (t.getName() != null
								&& t.getName().toUpperCase().indexOf(searchText.getText().toUpperCase()) != -1))
						.forEach(t -> {
							TableItem tableItem = new TableItem(msgDefinitionTable, SWT.NONE);
							tableItem.setText(t.getName());
							tableItem.setData(t.getId());
							tableItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB1));
						});
			}

		});
	}

	private void generateAllMsgDefinitionTable(Table msgDefinitionLisTable,
			java.util.List<EcoreMessageDefinition> msgDefinitions) {
		for (EcoreMessageDefinition definition : msgDefinitions) {
			TableItem tableItem = new TableItem(msgDefinitionLisTable, SWT.NONE);
			tableItem.setText(definition.getName());
			tableItem.setData(definition.getId());
			tableItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB1));
		}
		msgDefinitionLisTable.setSelection(0);
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
			for (int index : nextMsgDefinitionListWidget.getSelectionIndices()) {
				nextMsgDefinitionListWidget.remove(index);
			}
			setDirty(true);
		}
	}

	class OkButtonListenerForVersion extends MouseAdapter {

		private Table table;

		private Shell shell;

		public OkButtonListenerForVersion(Table table, Shell shell) {
			this.table = table;
			this.shell = shell;
		}

		@Override
		public void mouseUp(MouseEvent e) {

			TableItem[] tableItemArray = table.getSelection();

			for (int index = 0; index < tableItemArray.length; index++) {
				String msgDefinitionId = String.valueOf(tableItemArray[index].getData());
				TableItem tableItem = new TableItem(nextMsgDefinitionListWidget, SWT.NONE);
				tableItem.setText(tableItemArray[index].getText());
				tableItem.setData(msgDefinitionId);
			}

			shell.close();
		}
	}

	class AddMsgElementListener extends MouseAdapter {

		@Override
		public void mouseUp(MouseEvent e) {
			MessageDefinitionEditorChooser.createMsgElementSelectionDialogue(elementsTree, msgDefinition);
			setDirty(true);
		}
	}

// ------------------ 三个点按钮用 Begin --------------------
	private class TypeButtonListener extends MouseAdapter {

		private Text typeText;

		public TypeButtonListener(Text typeText) {
			super();
			this.typeText = typeText;
		}

		@Override
		public void mouseUp(MouseEvent e) {
			if (elementsTree.getSelectionCount() == 1
					&& elementsTree.getSelection()[0].getData("msgBldBlock") != null) {
				createSelectMsgElementType(typeText, MrRepository.get().ecoreMessageComponents, MrRepository.get().ecoreDataTypes);
			}
		}
	}

	private void createSelectMsgElementType(Text typeText, java.util.List<EcoreMessageComponent> allMessageComponentList,
			java.util.List<EcoreDataType> allDataTypeList) {
		Shell messageComponentWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		messageComponentWindow.setText("Select Message Element type:");
		messageComponentWindow.setLayout(new FormLayout());

		// 改变弹窗位置
		SystemUtil.center(messageComponentWindow);

		Composite c = new Composite(messageComponentWindow, SWT.NONE);
		Text searchText = new Text(c, SWT.BORDER);
		searchText.setBounds(10, 10, 280, 30);
		Button searchBtn = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_DEEP,"Search")).getButton();
		searchBtn.setBounds(300, 10, 50, 30);

		Table msgComponentTable = new Table(c, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
		msgComponentTable.setLinesVisible(true);
		this.generateMessageComponentTable(msgComponentTable, allMessageComponentList, allDataTypeList);
		msgComponentTable.setBounds(10, 50, 350, 400);
		msgComponentTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				searchText.setText(msgComponentTable.getSelection()[0].getText());
			}
		});

		Button okButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_PUSH_DEEP,"OK")).getButton();
		okButton.setBounds(275, 460, 35, 30);
		okButton.addMouseListener(new OkButtonListener(typeText, msgComponentTable, messageComponentWindow));

		Button cancelButton = new SummaryRowTextComposite(c, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_PUSH_DEEP,"Cancel")).getButton();
		cancelButton.setBounds(320, 460, 52, 30);
		cancelButton.addMouseListener(new CancelButtonListener(messageComponentWindow));

		messageComponentWindow.setSize(400, 550);
		messageComponentWindow.open();
		messageComponentWindow.layout();

		searchBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				msgComponentTable.removeAll();
				allMessageComponentList.stream()
						.filter(t -> (t.getName() != null
								&& t.getName().toUpperCase().indexOf(searchText.getText().toUpperCase()) != -1))
						.forEach(t -> {
							TableItem tableItem = new TableItem(msgComponentTable, SWT.NONE);
							tableItem.setText(t.getName());
							tableItem.setData("msgComponentId", t.getId());
							tableItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_COMPONENT));
						});
				allDataTypeList.stream()
						.filter(t -> (t.getName() != null
								&& t.getName().toUpperCase().indexOf(searchText.getText().toUpperCase()) != -1))
						.forEach(t -> {
							TableItem tableItem = new TableItem(msgComponentTable, SWT.NONE);
							tableItem.setText(t.getName());
							tableItem.setData("dataTypeId", t.getId());
							tableItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
						});
			}
		});
	}

	private void generateMessageComponentTable(Table msgComponentTable,
			java.util.List<EcoreMessageComponent> allMessageComponentList, java.util.List<EcoreDataType> allDataTypeList) {
		for (EcoreMessageComponent msgComponent : allMessageComponentList) {
			TableItem tableItem = new TableItem(msgComponentTable, SWT.NONE);
			tableItem.setText(msgComponent.getName());
			tableItem.setData("msgComponentId", msgComponent.getId());
			tableItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_COMPONENT));
		}

		for (EcoreDataType ecoreDataType : allDataTypeList) {
			TableItem tableItem = new TableItem(msgComponentTable, SWT.NONE);
			tableItem.setText(ecoreDataType.getName());
			tableItem.setData("dataTypeId", ecoreDataType.getId());
			tableItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
		}
		msgComponentTable.setSelection(0);
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

	private class OkButtonListener extends MouseAdapter {

		private Table table;

		private Shell shell;

		private Text typeText;

		public OkButtonListener(Text typeText, Table table, Shell shell) {
			this.typeText = typeText;
			this.table = table;
			this.shell = shell;
		}

		@Override
		public void mouseUp(MouseEvent e) {

			TableItem[] tableItemArray = table.getSelection();
			
			for (TableItem tableItem : tableItemArray) {
				TreeItem currentTreeItem = elementsTree.getSelection()[0];
				EcoreMessageBuildingBlock msgBldBlock = (EcoreMessageBuildingBlock) currentTreeItem.getData("msgBldBlock");
				this.typeText.setText(tableItem.getText());
				String min = StringUtils.isEmpty(mbbMinOccursText.getText()) ? "0" : mbbMinOccursText.getText();
				String max = StringUtils.isEmpty(mbbMaxOccursText.getText()) ? "*" : mbbMaxOccursText.getText();
				String displayName = mbbNameText.getText() + " [" + min + ", " + max + "] : " + tableItem.getText();
				currentTreeItem.setText(displayName);

				if (tableItem.getData("dataTypeId") != null) {
					currentTreeItem.setData("dataTypeId", String.valueOf(tableItem.getData("dataTypeId")));
					currentTreeItem.setData("dataType", ObjTypeEnum.DataType.getType());
					msgBldBlock.setDataTypeId(String.valueOf(tableItem.getData("dataTypeId")));
					msgBldBlock.setDataType("1");
					currentTreeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
					currentTreeItem.setData("typeName", tableItem.getText());
					// 删除原来的Datatype下的节点
					currentTreeItem.removeAll();
					// load datatype 下的 TYPE是1的 code(ECORE_CODE)
					MrEditorTreeCreator mrEditorTreeCreator = new MrEditorTreeCreator();
					mrEditorTreeCreator.generateContentTreeForDataType(currentTreeItem,MrHelper.findCodesByCodeSetId(cs, tableItem.getData("dataTypeId") + ""));
					// 当前选中的DataType的约束
					java.util.List<EcoreConstraint> ecs = MrHelper.findConstraintsByObjId(css, tableItem.getData("dataTypeId") + "");
					mrEditorTreeCreator.addConstraintsNode(currentTreeItem, ecs, tableItem.getText(), null);
				}

				if (tableItem.getData("msgComponentId") != null) {
					currentTreeItem.setData("msgComponentId", tableItem.getData("msgComponentId") + "");
					currentTreeItem.setData("dataType", ObjTypeEnum.MessageComponent.getType());
					msgBldBlock.setDataTypeId(tableItem.getData("msgComponentId") + "");
					msgBldBlock.setDataType("2");
					currentTreeItem.setImage(ImgUtil.createImg(ImgUtil.ME));
					currentTreeItem.setData("typeName", tableItem.getText());
					// 删除原来的Message Component下的节点
					currentTreeItem.removeAll();
					// 用Message Component里的元素，构造Message Component树
					java.util.List<EcoreMessageElement> mesf = mes.stream().filter(me -> me.getMessageComponentId().equals(tableItem.getData("msgComponentId") + "")).collect(Collectors.toList());
					//调用递归
					MrEditorTreeCreator mrEditorTreeCreator = new MrEditorTreeCreator();
					mrEditorTreeCreator.generateContentTreeForMessageComponent(currentTreeItem, mesf,
							MrHelper.getMessageComponentById(mcs, tableItem.getData("msgComponentId") + "").getRegistrationStatus());
					// 当前选中的Message Component的约束
					java.util.List<EcoreConstraint> ecs = MrHelper.findConstraintsByObjId(css, tableItem.getData("msgComponentId") + "");
					mrEditorTreeCreator.addConstraintsNode(currentTreeItem, ecs, tableItem.getText(), null);
				}
			}
			shell.close();
		}
	}

	// ------------------ 三个点按钮用 End --------------------
	private class AddConstraintListener extends MouseAdapter {

		@Override
		public void mouseUp(MouseEvent e) {
			MessageDefinitionEditorChooser.createConstraintDialogue(elementsTree);
			setDirty(true);
		}
	}

	class DeleteTreeNodeListener extends MouseAdapter {

		private Composite rightComposite;

		public DeleteTreeNodeListener(Composite rightComposite) {
			this.rightComposite = rightComposite;
		}

		@Override
		public void mouseUp(MouseEvent e) {
			for (TreeItem treeItem : elementsTree.getSelection()) {
				treeItem.dispose();
			}
			if (elementsTree.getItemCount() == 0) {
				for (Control control : rightComposite.getChildren()) {
					control.dispose();
				}
			}
			setDirty(true);
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		SaveHandler sh = new SaveHandler();
		try {
			sh.execute(null);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doSaveAs() {}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	private void setUnavailable(Widget swtWidget) {
		if (RegistrationStatusEnum.Registered.getStatus().equals(this.ecoreTreeNode.getRegistrationStatus())) {
			if (swtWidget instanceof Text) {
				((Text) swtWidget).setEditable(false);
			} else if (swtWidget instanceof Button) {
				((Button) swtWidget).setEnabled(false);
			}
		}
	}

	@Override
	public void setDirty(boolean dirty) {
		if (dirty && RegistrationStatusEnum.Registered.getStatus().equals(this.ecoreTreeNode.getRegistrationStatus())) {
			return;
		}
		super.setDirty(dirty);
	}

	private void showMessageBuildingBlockBar(Composite rightComposite, String name, String documentation,
			Integer minOccurs, Integer maxOccurs, String tag, Object type, String objectIdentifier) {
		for (Control control : rightComposite.getChildren()) {
			control.dispose();
		}

		// ------------ Message Building block Details Begin ------------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 5, I18nApi.get("editor.title.mbbd")));
		
		mbbNameText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 45, "Name", name == null ? "" : name)).getText();
		mbbNameText.setData("hint", "name");
		mbbNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTableItem.isDisposed()) {
					// 左边的树的名称展示
					String showText = "";
					String min = StringUtils.isEmpty(mbbMinOccursText.getText()) ? "0" : mbbMinOccursText.getText();
					String max = StringUtils.isEmpty(mbbMaxOccursText.getText()) ? "*" : mbbMaxOccursText.getText();
					if (mbbNameText.getText().isEmpty()) {
						showText = mbbNameText.getText() + " [" + min + ", " + max + "]";
					} else {
						showText = mbbNameText.getText() + " [" + min + ", " + max + "] : " + mbbNameText.getText();
					}
					selectedTableItem.setText(showText);
					setDirty(true);
				}
			}
		});
		this.setUnavailable(mbbNameText);

		mbbDocumentationText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SCROLL_TYPE, 73, "Documentation", documentation == null ? "" : documentation)).getText();
		mbbDocumentationText.setData("hint", "definition");
		mbbDocumentationText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTableItem.isDisposed()) {
//					selectedTableItem.setData("mbbDocumentationText", mbbDocumentationText.getText());
					setDirty(true);
				}
			}
		});
		this.setUnavailable(mbbDocumentationText);

		mbbMinOccursText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SHORT_TYPE, 228, "Min Occurs", minOccurs == null ? "" : minOccurs + "")).getText();
		mbbMinOccursText.setData("hint", "min");
		mbbMinOccursText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTableItem.isDisposed()) {
					String showText = "";
					String min = StringUtils.isEmpty(mbbMinOccursText.getText()) ? "0" : mbbMinOccursText.getText();
					String max = StringUtils.isEmpty(mbbMaxOccursText.getText()) ? "*" : mbbMaxOccursText.getText();
					if (mbbTypeText.getText().isEmpty()) {
						showText = mbbNameText.getText() + " [" + min + ", " + max + "]";
					} else {
						showText = mbbNameText.getText() + " [" + min + ", " + max + "] : " + mbbTypeText.getText();
					}
					selectedTableItem.setText(showText);
					setDirty(true);
				}
			}
		});
		this.setUnavailable(mbbMinOccursText);

		mbbMaxOccursText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SHORT_TYPE, 256, "Max Occurs", maxOccurs == null ? "" : maxOccurs + "")).getText();
		mbbMaxOccursText.setData("hint", "max");
		mbbMaxOccursText.addModifyListener(new MrEditorModifyListener(this, this.elementsTree));
		mbbMaxOccursText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTableItem.isDisposed()) {
					String showText = "";
					String min = StringUtils.isEmpty(mbbMinOccursText.getText()) ? "0" : mbbMinOccursText.getText();
					String max = StringUtils.isEmpty(mbbMaxOccursText.getText()) ? "*" : mbbMaxOccursText.getText();
					if (mbbTypeText.getText().isEmpty()) {
						showText = mbbNameText.getText() + " [" + min + ", " + max + "]";
					} else {
						showText = mbbNameText.getText() + " [" + min + ", " + max + "] : " + mbbTypeText.getText();
					}
					selectedTableItem.setText(showText);
					setDirty(true);
				}
			}
		});
		this.setUnavailable(mbbMaxOccursText);
		
		mbbXmlTagText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 284, "XML Tag", tag == null ? "" : tag)).getText();
		mbbXmlTagText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
				if (elementsTree.getSelection().length > 0) {
					TreeItem treeItem = elementsTree.getSelection()[0];
					String xmlTagValue = mbbXmlTagText.getText();
					if (treeItem.getData("msgComponentId") != null) {
						treeItem.setData("xmlTag", xmlTagValue);
					}
					if (treeItem.getData("msgBldBlock") != null) {
						EcoreMessageBuildingBlock msgBldBlock = (EcoreMessageBuildingBlock) treeItem.getData("msgBldBlock");
						msgBldBlock.setXmlTag(xmlTagValue);
					}
				}
			}
		});

		this.setUnavailable(mbbXmlTagText);
		
		mbbIsDerivedButton = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_CHECK, 312, "Derived", false)).getButton();
		this.setUnavailable(mbbIsDerivedButton);
		
		mbbTypeText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_BUTTON_SELECT_TYPE, 340, "Type", false, type == null ? "" : type + "")).getText();
		this.setUnavailable(mbbTypeText);

		mbbChooseButton = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_DEEP,"choose")).getButton();
		mbbChooseButton.setBounds(390, 340, 50, 25);
		mbbChooseButton.addMouseListener(new TypeButtonListener(mbbTypeText));
		this.setUnavailable(mbbChooseButton);

		Button mbbTypeButton = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_DEEP,ImgUtil.createImg(ImgUtil.SEARCH_BTN))).getButton();
		mbbTypeButton.setBounds(445, 340, 25, 25);
		mbbTypeButton.addMouseListener(new MrSearchMouseAdapter(this.elementsTree));
		// ------------ Message Building block Details End ------------------------

		// -----------------------CMP Information expand Begin -----------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 390, I18nApi.get("editor.title.cmp")));
		
		mbbObjectIdentifierValue = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 430, "Object Identifier", objectIdentifier == null ? "" : objectIdentifier)).getText();
		this.setUnavailable(mbbObjectIdentifierValue);
		// -----------------------CMP Information expand End -----------------------

		// ---------- Synonyms Expand Item Begin -------------------------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 480, I18nApi.get("editor.title.ss")));
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_NOTE_ONE_TYPE, 520, "This section show the synonyms of object"));
		
		addMbbSynonymsBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR))).getButton();
		addMbbSynonymsBtn.setBounds(10, 548, 30, 30);
		addMbbSynonymsBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (elementsTree.getSelectionCount() == 1) {
					TreeItem selectedItem = elementsTree.getSelection()[0];
					if (selectedItem.getData("msgComponentId") != null || selectedItem.getData("dataTypeId") != null) {
						MessageDefinitionEditorDialogContent.createSynonymsDialogue(mbbSynonymsTable, selectedItem);
					}
				}
				setDirty(true);
			}
		});
		this.setUnavailable(addMbbSynonymsBtn);

		deleteMbbSynonymsBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES))).getButton();
		deleteMbbSynonymsBtn.setBounds(42, 548, 30, 30);
		deleteMbbSynonymsBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				for (TableItem tableItem : mbbSynonymsTable.getSelection()) {
					tableItem.dispose();
				}
				ArrayList<String> synonymsList = new ArrayList<>();
				for (TableItem tableRow : mbbSynonymsTable.getItems()) {
					String key = tableRow.getText(0);
					String value = tableRow.getText(1);
					synonymsList.add(key + "," + value);
				}
				TreeItem selectedItem = elementsTree.getSelection()[0];
				if (selectedItem.getData("msgComponentId") != null) {
					selectedItem.setData("mbbSynonymsTableItems", synonymsList);
				}
				setDirty(true);
			}
		});
		this.setUnavailable(deleteMbbSynonymsBtn);

		mbbSynonymsTable = new Table(rightComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		mbbSynonymsTable.setHeaderVisible(true);
		mbbSynonymsTable.setLinesVisible(true);
		mbbSynonymsTable.setBounds(10, 585, 480, 150);
		mbbSynonymsTable.layout();
		
		new SummaryTableComposite(mbbSynonymsTable,238, "Context");
		new SummaryTableComposite(mbbSynonymsTable,238, "Synonums");
		// ---------- Synonyms Expand Item End -------------------------------------

		// ---------- Constraints Expand Item Begin -------------------------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 740, I18nApi.get("editor.title.cs")));
		
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_NOTE_TWO_TYPE, 780, "All the constraints contained in this object(other constraints - such as constraints"
				+ "\r\n defined on type - may also apply)."));
		
		addMbbConstraintsBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR))).getButton();
		addMbbConstraintsBtn.setBounds(10, 825, 30, 30);
		addMbbConstraintsBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (elementsTree.getSelectionCount() == 1) {
					TreeItem selectedItem = elementsTree.getSelection()[0];
					if (selectedItem.getData("msgComponentId") != null || selectedItem.getData("dataTypeId") != null) {
						MessageDefinitionEditorDialogContent.createConstraintDialogueOfContent(mbbConstraintsTable,
								selectedItem);
					}
				}
				setDirty(true);
			}
		});
		this.setUnavailable(addMbbConstraintsBtn);
		
		deleteMbbConstraintsBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES))).getButton();
		deleteMbbConstraintsBtn.setBounds(42, 825, 30, 30);
		deleteMbbConstraintsBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				for (TableItem tableItem : mbbConstraintsTable.getSelection()) {
					tableItem.dispose();
				}
				ArrayList<EcoreConstraint> mbbConstraintList = new ArrayList<>();
				for (TableItem tableRow : mbbConstraintsTable.getItems()) {
					mbbConstraintList.add((EcoreConstraint) tableRow.getData());
				}
				TreeItem selectedItem = elementsTree.getSelection()[0];
				selectedItem.setData("mbbConstraintList", mbbConstraintList);
				setDirty(true);
			}
		});
		this.setUnavailable(deleteMbbConstraintsBtn);
		
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

	private void showCodeDetailBar(Composite rightComposite, EcoreCode ecoreCode) {
		for (Control control : rightComposite.getChildren()) {
			control.dispose();
		}
		// ------------ Code Details Bar Begin ---------------------------------

		// ------------ Code Detail Expand Item Begin ------------------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 5, I18nApi.get("editor.title.cd")));
		
		codeDetailNameValue = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 45, "Name", ecoreCode.getName())).getText();
		this.setUnavailable(codeDetailNameValue);

		codeDocumentValue = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SCROLL_TYPE, 73, "Documentation", ecoreCode.getDefinition())).getText();
		this.setUnavailable(codeDocumentValue);

		codeNameValue = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 228, "CodeName", ecoreCode.getCodeName())).getText();
		this.setUnavailable(codeNameValue);
		// ------------ Code Detail Expand Item End ------------------------------

		// ---------- Code Detail Constraints Expand Item Begin ------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 278, I18nApi.get("editor.title.cs")));
		
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_NOTE_TWO_TYPE, 318, "All the constraints contained in this object(other constraints - such as constraints"
				+ "\r\n defined on type - may also apply)."));
		
		addMbbConstraintsBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR))).getButton();
		addMbbConstraintsBtn.setBounds(10, 363, 30, 30);
		addMbbConstraintsBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (elementsTree.getSelectionCount() == 1) {
					TreeItem selectedItem = elementsTree.getSelection()[0];
					if (selectedItem.getData("msgComponentId") != null || selectedItem.getData("dataTypeId") != null) {
						MessageDefinitionEditorDialogContent.createConstraintDialogueOfContent(mbbConstraintsTable,
								selectedItem);
					}
				}
				setDirty(true);
			}
		});
		this.setUnavailable(addMbbConstraintsBtn);
		
		deleteMbbConstraintsBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES))).getButton();
		deleteMbbConstraintsBtn.setBounds(42, 363, 30, 30);
		deleteMbbConstraintsBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				for (TableItem tableItem : mbbConstraintsTable.getSelection()) {
					tableItem.dispose();
				}
				ArrayList<EcoreConstraint> mbbConstraintList = new ArrayList<>();
				for (TableItem tableRow : mbbConstraintsTable.getItems()) {
					mbbConstraintList.add((EcoreConstraint) tableRow.getData());
				}
				TreeItem selectedItem = elementsTree.getSelection()[0];
				selectedItem.setData("mbbConstraintList", mbbConstraintList);
				setDirty(true);
			}
		});
		this.setUnavailable(deleteMbbConstraintsBtn);
		
		mbbConstraintsTable = new Table(rightComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		mbbConstraintsTable.setHeaderVisible(true);
		mbbConstraintsTable.setLinesVisible(true);
		mbbConstraintsTable.setBounds(10, 398, 480, 150);
		mbbConstraintsTable.layout();
		
		new SummaryTableComposite(mbbConstraintsTable, 113, "Name");
		new SummaryTableComposite(mbbConstraintsTable, 113, "Definition");
		new SummaryTableComposite(mbbConstraintsTable, 137, "Expression Language");
		new SummaryTableComposite(mbbConstraintsTable, 113, "Expression");
		// ---------- Code Detail Constraints Expand Item End ---------------------------
	}

	private void showConstraintBar(Composite rightComposite, Object name, Object definition, Object expression, Object expressionlanguage) {

		for (Control control : rightComposite.getChildren()) {
			control.dispose();
		}

		// -------------------- Constraints Bar Begin -------------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 5, I18nApi.get("editor.title.cs")));
		
		constraintNameValue = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 45, "Name", name == null ? "" : name + "")).getText();
		constraintNameValue.setData("hint", "name");
		constraintNameValue.addModifyListener(new MrEditorModifyListener(this, this.elementsTree));
		this.setUnavailable(constraintNameValue);

		constraintDocValue = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SCROLL_TYPE, 73, "Documentation", definition == null ? "" : definition + "")).getText();
		constraintDocValue.setData("hint", "document");
		constraintDocValue.addModifyListener(new MrEditorModifyListener(this, this.elementsTree));
		this.setUnavailable(constraintDocValue);

		expressionValue = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SCROLL_TYPE, 228, "Expression", expression == null ? "" : expression + "")).getText();
		expressionValue.setData("hint", "expression");
		expressionValue.addModifyListener(new MrEditorModifyListener(this, this.elementsTree));
		this.setUnavailable(expressionValue);

		expressionLanguageValue = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SCROLL_TYPE, 383, "Expression Language", expressionlanguage == null ? "" : expressionlanguage + "")).getText();
		expressionLanguageValue.setData("hint", "expressionLanguage");
		expressionLanguageValue.addModifyListener(new MrEditorModifyListener(this, this.elementsTree));
		this.setUnavailable(expressionLanguageValue);
		// -------------------- Constraints Bar End -------------------------
	}
}
