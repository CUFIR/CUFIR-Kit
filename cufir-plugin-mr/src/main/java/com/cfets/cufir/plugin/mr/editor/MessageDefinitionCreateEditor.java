/**
 * 
 */
package com.cfets.cufir.plugin.mr.editor;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.UUID;

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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
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
import com.cfets.cufir.plugin.mr.utils.GenerateChooseMC;
import com.cfets.cufir.plugin.mr.utils.GenerateCommonTree;
import com.cfets.cufir.plugin.mr.utils.GenerateExampleAndConstraintAndSynonyms;
import com.cfets.cufir.plugin.mr.utils.GenerateSynonymsAndConstraintDialogForContent;
import com.cfets.cufir.plugin.mr.utils.GenerateTableForMCandMDandMS;
import com.cfets.cufir.plugin.mr.utils.ImgUtil;
import com.cfets.cufir.plugin.mr.utils.MessageBoxUtil;
import com.cfets.cufir.plugin.mr.utils.SystemUtil;
import com.cfets.cufir.plugin.mr.view.ChangeShellLocation;
import com.cfets.cufir.s.data.bean.EcoreBusinessArea;
import com.cfets.cufir.s.data.bean.EcoreCode;
import com.cfets.cufir.s.data.bean.EcoreConstraint;
import com.cfets.cufir.s.data.bean.EcoreDataType;
import com.cfets.cufir.s.data.bean.EcoreExample;
import com.cfets.cufir.s.data.bean.EcoreMessageBuildingBlock;
import com.cfets.cufir.s.data.bean.EcoreMessageComponent;
import com.cfets.cufir.s.data.bean.EcoreMessageDefinition;
import com.cfets.cufir.s.data.bean.EcoreMessageElement;
import com.cfets.cufir.s.data.bean.EcoreMessageSet;
import com.cfets.cufir.s.data.bean.EcoreNextVersions;
import com.cfets.cufir.s.data.vo.EcoreMessageBuildingBlockVO;
import com.cfets.cufir.s.data.vo.EcoreMessageDefinitionVO;
import com.cfets.cufir.s.data.vo.EcoreTreeNode;
import com.cfets.cufir.s.data.vo.SynonymVO;

/**
 * Message Definition 显示和编辑
 * 
 * @author gongyi_tt
 *
 */
public class MessageDefinitionCreateEditor extends MultiPageEditorParent {
	
	private MyEditorInput myEditorInput;
	
	private TreeItem modelExploreTreeItem;
	
	private EcoreMessageDefinition msgDefinition;
	
	private EcoreBusinessArea businessArea;
	
	private ArrayList<EcoreMessageBuildingBlock> mbbList;
	
	private ArrayList<EcoreMessageSet> messageSet;
	
	private EcoreMessageDefinition previousMsgDefintion;
	
	private ArrayList<EcoreMessageDefinition> nextMsgDefinitions;
	
	private EcoreTreeNode ecoreTreeNode;
	
	//-------------for Summary Page use-----------------
	private Text nameValue;
	private Text documentationValue;
	private Text messageDefinitionIdentifierValue;
	private Text xmlTagValue;
	private Text purposeValue;
	private Text rootElementValue;
	private Text objectIdentifierValue;
	private ComboViewer statusComboViewer;
	private Text removalDateValue;
	private Table synonymsTable;
	private Table exampleTable;
	private TreeItem selectedTableItem;
	//-----------For Content Page ------------
	private Tree elementsTree;
	private Text xmlTagValueOfContentPage;
	private Text mbbNameValue;
	private Text mbbDocumentationValue;
	private Text mbbMinOccursValue;
	private Text mbbMaxOccursValue;
	private Table mbbSynonymsTable;
	private Text typeValue;
	private Button isDerivedCheckButton;
	private Button dotBtn;
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
	private List previousVersionListWidget;
	
	// ----------For Versions/Subsets
//	private Set<String> nextVersionMsgDefinitionIdSet = new TreeSet<>(); 
	
	@Override
	public void setPartName(String partName) {
		super.setPartName(partName);
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		this.setSite(site);
		this.setInput(input);
		this.setPartProperty("customName", "msgDefinitionCreate");
		this.myEditorInput = (MyEditorInput) input;
		
		modelExploreTreeItem = this.myEditorInput.getTransferDataBean().getTreeListItem();
		
		ecoreTreeNode = (EcoreTreeNode)modelExploreTreeItem.getData("EcoreTreeNode");

		// 设置标题图片
		this.setTitleImage(ImgUtil.createImg(this.myEditorInput.getTransferDataBean().getImgPath()));
	
		ContextServiceUtil.setContext(site);
		
	}



	public EcoreMessageDefinition getMsgDefinition() {
		return msgDefinition;
	}


	public java.util.List<EcoreMessageDefinitionVO> getEcoreMessageDefinitionVOList() {
		
		msgDefinition.setName(this.nameValue.getText());
		msgDefinition.setDefinition(this.documentationValue.getText());
		msgDefinition.setRegistrationStatus(statusComboViewer.getCombo().getText());
		msgDefinition.setRootElement(this.rootElementValue.getText());
		msgDefinition.setObjectIdentifier(this.objectIdentifierValue.getText());
		String msgDefiIdentiInputValue = this.messageDefinitionIdentifierValue.getText();
		if (msgDefiIdentiInputValue.matches("([0-9A-Za-z]){1,}\\.([0-9A-Za-z]){1,}\\.([0-9A-Za-z]){1,}\\.([0-9A-Za-z]){1,}")) {
			msgDefinition.setMessageDefinitionIdentifier(msgDefiIdentiInputValue);
		} else {
			msgDefinition.setMessageDefinitionIdentifier("");
		}
		
		msgDefinition.setRegistrationStatus(this.statusComboViewer.getCombo().getText());
		msgDefinition.setXmlTag(this.xmlTagValue.getText());
		
		java.util.List<EcoreMessageDefinitionVO> ecoreMessageDefinitionVOList = new ArrayList<>();
		EcoreMessageDefinitionVO ecoreMessageDefinitionVO = new EcoreMessageDefinitionVO();
		ecoreMessageDefinitionVO.setEcoreMessageDefinition(msgDefinition);
		ecoreMessageDefinitionVO.setEcoreExamples(getExampleData(msgDefinition));
		ecoreMessageDefinitionVO.setSynonyms(getSynonymVO(msgDefinition));
		
			java.util.List<EcoreMessageBuildingBlockVO> ecoreMessageBuildingBlockVOList = new ArrayList<>();
			ArrayList<EcoreConstraint> constraintList = new ArrayList<>();
			for (TreeItem mbbTreeItem: elementsTree.getItems()) {
				
				Object object = mbbTreeItem.getData("msgBldBlock");
				
				if (object != null) {
					EcoreMessageBuildingBlockVO ecoreMessageBuildingBlockVO = new EcoreMessageBuildingBlockVO();
					EcoreMessageBuildingBlock ecoreMessageBuildingBlock = (EcoreMessageBuildingBlock)object;
					ecoreMessageBuildingBlockVO.setEcoreMessageBuildingBlock(ecoreMessageBuildingBlock);
					
					object = mbbTreeItem.getData("mbbSynonymsTableItems");
					if (object != null) {
						ecoreMessageBuildingBlockVO.setSynonyms(convertToSynonymVO((ArrayList<String>)object, ecoreMessageBuildingBlock.getId()));
					}
					object = mbbTreeItem.getData("mbbConstraintList");
					if (object != null) {
						ecoreMessageBuildingBlockVO.setEcoreConstraints(insertMbbId((ArrayList<EcoreConstraint>)object, ecoreMessageBuildingBlock.getId()));
					}
					ecoreMessageBuildingBlockVOList.add(ecoreMessageBuildingBlockVO);
				} else if (mbbTreeItem.getData("isNewConstraint") != null) {
					EcoreConstraint ecoreConstraints = new EcoreConstraint();
					ecoreConstraints.setId(UUID.randomUUID().toString());
					ecoreConstraints.setName(String.valueOf(mbbTreeItem.getData("name")));
					ecoreConstraints.setDefinition(String.valueOf(mbbTreeItem.getData("document")));
					ecoreConstraints.setExpression(String.valueOf(mbbTreeItem.getData("expression") == null? "": mbbTreeItem.getData("expression")));
					ecoreConstraints.setExpressionlanguage(String.valueOf(mbbTreeItem.getData("expressionLanguage") == null? "": mbbTreeItem.getData("expressionLanguage")));
					ecoreConstraints.setObj_id(msgDefinition.getId());
					ecoreConstraints.setObj_type(ObjTypeEnum.MessageDefinition.getType());
					constraintList.add(ecoreConstraints);
					ecoreMessageDefinitionVO.setEcoreConstraints(constraintList);
				} else if (mbbTreeItem.getData("constraint") != null) {
					constraintList.add((EcoreConstraint)mbbTreeItem.getData("constraint"));
					ecoreMessageDefinitionVO.setEcoreConstraints(constraintList);
				}
			}
			ecoreMessageDefinitionVO.setEcoreMessageBuildingBlockVOs(ecoreMessageBuildingBlockVOList);
			
			ecoreMessageDefinitionVO.setEcoreNextVersions(this.getNextVersionsData(msgDefinition));
			
			ecoreMessageDefinitionVOList.add(ecoreMessageDefinitionVO);
			
		return ecoreMessageDefinitionVOList;
	}
	
	private ArrayList<EcoreConstraint> insertMbbId(ArrayList<EcoreConstraint> ecoreconstraintList, String mbbId) {
		for (EcoreConstraint ecoreConstraint: ecoreconstraintList) {
			ecoreConstraint.setObj_type(ObjTypeEnum.MessageBlock.getType());
			ecoreConstraint.setObj_id(mbbId);
		}
		return ecoreconstraintList;
	}
	
	private java.util.List<SynonymVO> convertToSynonymVO(ArrayList<String> synonymsList, String mbbId) {
		java.util.List<SynonymVO> synonymVOList = new ArrayList<>();
		for (String keyValuePair: synonymsList) {
			SynonymVO synonymVO = new SynonymVO();
			String key = keyValuePair.split(",")[0];
			String value = keyValuePair.split(",")[1];
			synonymVO.setObjId(mbbId);
			synonymVO.setContext(key);
			synonymVO.setSynonym(value);
			synonymVOList.add(synonymVO);
		}
		return synonymVOList;
	}	

	private ArrayList<EcoreExample> getExampleData(EcoreMessageDefinition ecoreMsgDefinition) {
		ArrayList<EcoreExample> ecoreExampleList = new ArrayList<>();
		for (TableItem tableItem: this.exampleTable.getItems()) {
			EcoreExample ecoreExample = (EcoreExample)tableItem.getData();
			ecoreExample.setObj_id(ecoreMsgDefinition.getId());
			ecoreExample.setObj_type(ObjTypeEnum.MessageDefinition.getType());
			ecoreExampleList.add(ecoreExample);
		}
		return ecoreExampleList;
	}

	private ArrayList<SynonymVO> getSynonymVO(EcoreMessageDefinition msgDefinition) {
		ArrayList<SynonymVO> synonymVOList = new ArrayList<SynonymVO>();
		for (TableItem tableItem: synonymsTable.getItems()) {
			SynonymVO synonymsVO = new SynonymVO();
			synonymsVO.setContext(tableItem.getText(0));
			synonymsVO.setSynonym(tableItem.getText(1));
			synonymsVO.setObjId(msgDefinition.getId());
			synonymVOList.add(synonymsVO);
		}
		return synonymVOList;
	}

	private ArrayList<EcoreNextVersions> getNextVersionsData(EcoreMessageDefinition ecoreMsgDefinition) {
		ArrayList<EcoreNextVersions> nextVersionList = new ArrayList<>();
		for (TableItem tableItem: this.nextMsgDefinitionListWidget.getItems()) {
			EcoreNextVersions nextVersion = new EcoreNextVersions();
			nextVersion.setId(ecoreMsgDefinition.getId());
			nextVersion.setNextVersionId(String.valueOf(tableItem.getData()));
			nextVersion.setObjType(ObjTypeEnum.MessageDefinition.getType());
			nextVersionList.add(nextVersion);
		}
		return nextVersionList;
	}
	
	
	private void loadMessageDefinitionData() {
		TransferDataBean transferDataBean = this.myEditorInput.getTransferDataBean();
		String msgDefinitionId = transferDataBean.getId();
		this.setPartProperty("ID", msgDefinitionId);
		msgDefinition = DerbyDaoUtil.getMessageDefinitionById(msgDefinitionId);
		messageSet = DerbyDaoUtil.getMsgSetListByMsgDefinitionId(msgDefinitionId);
		businessArea = DerbyDaoUtil.getBusinessAreaByBizAreaId(msgDefinition.getBusinessAreaId());
		previousMsgDefintion = DerbyDaoUtil.getPreviousVersionMsgDefinitionByMsgDefId(msgDefinition);
		nextMsgDefinitions = DerbyDaoUtil.getMsgDefinitionListByIds(DerbyDaoUtil.getNextVersionIds(msgDefinitionId));
	}	
	
	@Override
	protected void createPages() {
		
		loadMessageDefinitionData();

		if (msgDefinition.getId() == null) {
			msgDefinition.setId(this.myEditorInput.getTransferDataBean().getId());
			this.setPartName("");
			this.setDirty(true);
		} else {
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
			for (TreeItem treeItem: this.elementsTree.getItems()) {
				Object object = treeItem.getData("msgBldBlock");
				if (object != null) {
					EcoreMessageBuildingBlock msgBldBlock = (EcoreMessageBuildingBlock)object;
					if (msgBldBlockId.equals(msgBldBlock.getId())) {
						this.elementsTree.setSelection(treeItem);
					}
				}
			}
		}
		
		if ( RegistrationStatus.Provisionally.getStatus().equals(this.ecoreTreeNode.getRegistrationStatus()) || RegistrationStatus.Added.getStatus().equals(this.ecoreTreeNode.getRegistrationStatus()) ) {
			this.setDirty(true);
		} else if (RegistrationStatus.Registered.getStatus().equals(this.ecoreTreeNode.getRegistrationStatus())) {
			this.setDirty(false);
		}
		
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
		
		if (RegistrationStatus.Registered.getStatus().equals(this.ecoreTreeNode.getRegistrationStatus())) {
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
		GridData nameValueGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		nameValueGridData.widthHint = 850;
		nameValue.setLayoutData(nameValueGridData);
		nameValue.setText((msgDefinition == null || msgDefinition.getName() == null)? "": msgDefinition.getName());
		nameValue.addModifyListener(new EditorModifyListener(this, modelExploreTreeItem));
		
		Label documentationLabel = new Label(generalInformationComposite, SWT.NONE);
		documentationLabel.setLayoutData(nameLabelGridData);
		documentationLabel.setText("Documentation");
		documentationValue = new Text(generalInformationComposite, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		GridData docValueGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		docValueGridData.widthHint = 650;
		docValueGridData.heightHint = 250;
		documentationValue.setLayoutData(docValueGridData);
		documentationValue.setText((msgDefinition == null || msgDefinition.getDefinition() == null)? "": msgDefinition.getDefinition());
		documentationValue.addModifyListener(new EditorModifyListener(this));
		
		Label messageDefinitionIdentifierLabel = new Label(generalInformationComposite, SWT.NONE);
		messageDefinitionIdentifierLabel.setText("Message Definition Identifier");
		messageDefinitionIdentifierValue = new Text(generalInformationComposite, SWT.BORDER);
		messageDefinitionIdentifierValue.setLayoutData(nameValueGridData);
		if (msgDefinition == null || (msgDefinition.getBusinessArea() == null || msgDefinition.getMessageFunctionality() == null || msgDefinition.getFlavour() == null || msgDefinition.getVersion() == null)) {
			messageDefinitionIdentifierValue.setText("");
		} else {
			messageDefinitionIdentifierValue.setText(msgDefinition.getBusinessArea() + "." + msgDefinition.getMessageFunctionality() + "." + msgDefinition.getFlavour() + "." + msgDefinition.getVersion());
		}
		messageDefinitionIdentifierValue.addModifyListener(new EditorModifyListener(this));
		
		Label xmlTagLabel = new Label(generalInformationComposite, SWT.NONE);
		xmlTagLabel.setText("XML Tag");
		xmlTagValue = new Text(generalInformationComposite, SWT.BORDER);
		xmlTagValue.setLayoutData(nameValueGridData);
		xmlTagValue.setText((msgDefinition == null || msgDefinition.getXmlTag() == null)? "": msgDefinition.getXmlTag());
		xmlTagValue.addModifyListener(new EditorModifyListener(this));
		
		Label purposeLabel = new Label(generalInformationComposite, SWT.NONE);
		purposeLabel.setText("Purpose");
		purposeLabel.setLayoutData(nameLabelGridData);
		purposeValue = new Text(generalInformationComposite, SWT.BORDER);
		purposeValue.setLayoutData(docValueGridData);
				
		Label rootElementLabel = new Label(generalInformationComposite, SWT.NONE);
		rootElementLabel.setText("Root Element");
		rootElementValue = new Text(generalInformationComposite, SWT.BORDER);
		rootElementValue.setLayoutData(nameValueGridData);
		rootElementValue.setText((msgDefinition == null || msgDefinition.getRootElement() == null)? "": msgDefinition.getRootElement());
		rootElementValue.addModifyListener(new EditorModifyListener(this));
		
		ExpandItem generalInformationItem = new ExpandItem(summaryBar, SWT.NONE);
		generalInformationItem.setText("General Information");
		generalInformationItem.setHeight(700);
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
		objectIdentifierValue.setText((msgDefinition == null || msgDefinition.getObjectIdentifier() == null)? "": msgDefinition.getObjectIdentifier());
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
		if (msgDefinition.getRegistrationStatus() == null) {
			statusCombo.select(2);
		} else {
			statusCombo.setText(msgDefinition.getRegistrationStatus());
		}
		statusCombo.setEnabled(false);
		
		Label removealDateLabel = new Label(registrationComposite, SWT.NONE);
		removealDateLabel.setText("Removal Date");
		removealDateLabel.setLayoutData(nameLabelGridData);
		removalDateValue = new Text(registrationComposite, SWT.BORDER);
		removalDateValue.setLayoutData(valueGridData);
		removalDateValue.setText((msgDefinition == null || msgDefinition.getRemovalDate() == null)? "" : DateFormat.getInstance().format(msgDefinition.getRemovalDate()));
		removalDateValue.addModifyListener(new EditorModifyListener(this));
		
		ExpandItem registrationItem = new ExpandItem(summaryBar, SWT.NONE);
		registrationItem.setText("Registration Information");
		registrationItem.setHeight(100);
		registrationItem.setExpanded(true);
		registrationItem.setControl(registrationComposite);
		
		// ===== Registration Information Group End ===============================
		
		// ========== Synonyms Group Begin ========================================
		Composite synonymsComposite = new Composite(summaryBar, SWT.NONE);
		synonymsComposite.setLayout(new GridLayout(1, false));
		Label descriptionLabel = new Label(synonymsComposite, SWT.NONE);
		descriptionLabel.setText("This section show the synonyms of object");
		GridData descriptionGridData = new GridData();
		descriptionGridData.heightHint = 25;
		descriptionLabel.setLayoutData(descriptionGridData);
		
		Composite synonymsToolbarComposite = new Composite(synonymsComposite, SWT.NONE);
		synonymsToolbarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		Button addSynonymsBtn = new Button(synonymsToolbarComposite, SWT.NONE);
		addSynonymsBtn.setImage(ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR));
		addSynonymsBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				GenerateSynonymsAndConstraintDialogForContent.createSynonymsDialogue(synonymsTable);
				setDirty(true);
			}
		});
		Button deleteSynonymsBtn = new Button(synonymsToolbarComposite, SWT.NONE);
		deleteSynonymsBtn.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
		deleteSynonymsBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				for (TableItem tableItem: synonymsTable.getSelection()) {
					tableItem.dispose();
				}
				setDirty(true);
			}
		});
		
		GridData tableGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		tableGridData.widthHint = 350;
		tableGridData.heightHint = 150;
		synonymsTable = new Table(synonymsComposite, SWT.BORDER);
		synonymsTable.setLayoutData(tableGridData);
		synonymsTable.setHeaderVisible(true);
		synonymsTable.setLinesVisible(true);
		
		TableColumn contextColumn = new TableColumn(synonymsTable, SWT.NONE);
		contextColumn.setWidth(100);
		contextColumn.setText("Context");
		TableColumn SynonymsColumn = new TableColumn(synonymsTable, SWT.NONE);
		SynonymsColumn.setWidth(100);
		SynonymsColumn.setText("Synonyms");
		
		if (msgDefinition != null && msgDefinition.getId() != null) {
			GenerateExampleAndConstraintAndSynonyms.generateSynonymsTable(synonymsTable, msgDefinition.getId(), null);
		}
		
		ExpandItem synonymsItem = new ExpandItem(summaryBar, SWT.NONE);
		synonymsItem.setText("Synonyms");
		synonymsItem.setHeight(250);
		synonymsItem.setExpanded(true);
		synonymsItem.setControl(synonymsComposite);
		// ========== Synonyms Group End ==========================================
		
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
		
		if (msgDefinition != null && msgDefinition.getId() != null) {
			GenerateExampleAndConstraintAndSynonyms.generateExampleTable(exampleTable, msgDefinition.getId());
		}
		
		
		ExpandItem exampleItem = new ExpandItem(summaryBar, SWT.NONE);
		exampleItem.setText("Example");
		exampleItem.setHeight(300);
		exampleItem.setExpanded(true);
		exampleItem.setControl(exampleComposite);
		// =================== Exampel Group End ==============================
		
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
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
		lc_gd.widthHint = 550; //composite.getBounds().width / 3;
		leftComposite.setLayoutData(lc_gd);
		
		GridData rc_gd = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		rc_gd.widthHint = 500;	//composite.getBounds().width / 3;
		Composite rightComposite = new Composite(composite, SWT.NONE);
		rightComposite.setLayout(new FillLayout(SWT.VERTICAL));
		rightComposite.setLayoutData(rc_gd);


		// ------------ Content Expand Bar Begin ----------------------------------
		ExpandBar contentBar = new ExpandBar(leftComposite, SWT.NONE);

		Composite contentBarComposite = new Composite(contentBar, SWT.NONE);
		GridData contentBarCompositeGridData = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		contentBarComposite.setLayout(new GridLayout(1, true));
		contentBarComposite.setLayoutData(contentBarCompositeGridData);
		
		contentBar.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		contentBar.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		contentBar.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		
		Composite toolbarComposite = new Composite(contentBarComposite, SWT.NONE);
		toolbarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Button plusButtonForMsgElement = new Button(toolbarComposite, SWT.NONE);
		plusButtonForMsgElement.setImage(ImgUtil.createImg(ImgUtil.ME));
		plusButtonForMsgElement.addMouseListener(new AddMsgElementListener());
		this.setUnavailable(plusButtonForMsgElement);
		
		Button plusButtonForConstraint = new Button(toolbarComposite, SWT.NONE);
		plusButtonForConstraint.setImage(ImgUtil.createImg(ImgUtil.CONSTRAINT));
		plusButtonForConstraint.addMouseListener(new AddConstraintListener());
		this.setUnavailable(plusButtonForConstraint);
		
		Button deleteButtonForContent = new Button(toolbarComposite, SWT.NONE);
		deleteButtonForContent.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
		deleteButtonForContent.addMouseListener(new DeleteTreeNodeListener(rightComposite));
		this.setUnavailable(deleteButtonForContent);
		
		elementsTree = new Tree(contentBarComposite, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData elementsTreeGridData = new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1);
		elementsTreeGridData.widthHint = 510;
		elementsTreeGridData.heightHint = 700;
		elementsTree.setLayoutData(elementsTreeGridData);

		this.generateContentTree();
		
		ExpandItem contentItem = new ExpandItem(contentBar, SWT.NONE);
		contentItem.setText("Content");
		contentItem.setHeight(850);
		contentItem.setExpanded(true);
		contentItem.setControl(contentBarComposite);
		// ------------ Content Expand Bar End ----------------------------------
		
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		elementsTree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem treeItem_1 = (TreeItem) e.item;
				TreeItem[] treeItems = elementsTree.getSelection();
				String name=treeItem_1.getText();
				for (TreeItem treeItem: treeItems) {
					selectedTableItem=treeItem;
					if ( RegistrationStatus.Provisionally.getStatus().equals(ecoreTreeNode.getRegistrationStatus()) || RegistrationStatus.Added.getStatus().equals(ecoreTreeNode.getRegistrationStatus()) ) {
						if (treeItem.getParentItem() != null) {
							deleteButtonForContent.setEnabled(false);
						} else {
							deleteButtonForContent.setEnabled(true);
						}
					}
					
					String constraintId = String.valueOf(treeItem.getData("constraintId"));
					String msgElementID = String.valueOf(treeItem.getData("msgElementID"));
					String codeId = String.valueOf(treeItem.getData("codeId"));
					
					if (treeItem.getData("msgBldBlock") != null) {
						showMessageBuildingBlockBar(rightComposite, scrolledComposite);
						EcoreMessageBuildingBlock msgBB = (EcoreMessageBuildingBlock)treeItem.getData("msgBldBlock");
						mbbNameValue
						.setText(String.valueOf(treeItem_1.getData("mbbNameValue") == null ? "" : treeItem.getData("mbbNameValue")));
						mbbDocumentationValue
						.setText(String.valueOf(treeItem_1.getData("mbbDocumentationValue") == null ? "" : treeItem.getData("mbbDocumentationValue")));
						mbbMinOccursValue
						.setText(String.valueOf(treeItem_1.getData("mbbMinOccursValue") == null ? "" : treeItem.getData("mbbMinOccursValue")));
						mbbMaxOccursValue
						.setText(String.valueOf(treeItem_1.getData("mbbMaxOccursValue") == null ? "" : treeItem.getData("mbbMaxOccursValue")));
//						mbbNameValue
//						.setText(String.valueOf(treeItem.getData("mbbNameValue") == null ? "" : treeItem.getData("mbbNameValue")));
//						mbbNameValue.setText(msgBB.getName());
//						mbbDocumentationValue.setText(msgBB.getDefinition());
//						mbbMinOccursValue.setText(String.valueOf(msgBB.getMinOccurs()));
//						mbbMaxOccursValue.setText(String.valueOf(msgBB.getMaxOccurs()));
						xmlTagValueOfContentPage.setText(msgBB.getXmlTag() == null? "": msgBB.getXmlTag());
						typeValue.setText(treeItem.getData("typeName") == null? "": String.valueOf(treeItem.getData("typeName")));
						mbbObjectIdentifierValue.setText(msgBB.getObjectIdentifier() == null? "": msgBB.getObjectIdentifier());
						GenerateExampleAndConstraintAndSynonyms.generateConstraintTable(mbbConstraintsTable, msgBB.getId(), treeItem);
						GenerateExampleAndConstraintAndSynonyms.generateSynonymsTable(mbbSynonymsTable, msgBB.getId(), treeItem);
						
					} else if (!constraintId.equalsIgnoreCase("null")) {
						showConstraintBar(rightComposite, scrolledComposite);
						EcoreConstraint constraint = (EcoreConstraint)treeItem.getData("constraint");
						constraintNameValue.setText(constraint.getName() == null? "": constraint.getName());
						constraintDocValue.setText(constraint.getDefinition() == null? "": constraint.getDefinition());
						expressionValue.setText(constraint.getExpression() == null? "": constraint.getExpression());
						expressionLanguageValue.setText(constraint.getExpressionlanguage() == null? "": constraint.getExpressionlanguage());
						if (treeItem.getParentItem() != null) { // Content页面右边，不是Message Definition下的Constraint detail展示时，不需要编辑。
							constraintNameValue.setEditable(false);
							constraintDocValue.setEditable(false);
							expressionValue.setEditable(false);
							expressionLanguageValue.setEditable(false);
						}
						
					} else if (treeItem.getData("isNewConstraint") != null) {
						showConstraintBar(rightComposite, scrolledComposite);
						constraintNameValue.setText(treeItem.getData("name") == null? "": String.valueOf(treeItem.getData("name")));
						constraintDocValue.setText(treeItem.getData("document") == null? "": String.valueOf(treeItem.getData("document")));
						expressionValue.setText(treeItem.getData("expression") == null? "": String.valueOf(treeItem.getData("expression")));
						expressionLanguageValue.setText(treeItem.getData("expressionLanguage") == null? "": String.valueOf(treeItem.getData("expressionLanguage")));
						
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
						mbbObjectIdentifierValue.setText(ecoreMessageElement.getObjectIdentifier() == null? "": ecoreMessageElement.getObjectIdentifier());
						isDerivedCheckButton.setEnabled(false);
						dotBtn.setEnabled(false);
						addMbbSynonymsBtn.setEnabled(false);
						deleteMbbSynonymsBtn.setEnabled(false);
						addMbbConstraintsBtn.setEnabled(false);
						deleteMbbConstraintsBtn.setEnabled(false);
						GenerateExampleAndConstraintAndSynonyms.generateConstraintTable(mbbConstraintsTable, msgElementID, treeItem);
						GenerateExampleAndConstraintAndSynonyms.generateSynonymsTable(mbbSynonymsTable, msgElementID, null);
						
					} else if (!codeId.equalsIgnoreCase("null")) {
						showCodeDetailBar(rightComposite, scrolledComposite);
						EcoreCode ecoreCode = (EcoreCode)treeItem.getData("ecoreCode");
						codeDetailNameValue.setEditable(false);
						codeDetailNameValue.setText(ecoreCode.getName());
						codeDocumentValue.setEditable(false);
						codeDocumentValue.setText(ecoreCode.getDefinition());
						codeNameValue.setEditable(false);
						codeNameValue.setText(ecoreCode.getCodeName());
						GenerateExampleAndConstraintAndSynonyms.generateConstraintTable(mbbConstraintsTable, codeId, treeItem);
//						GenerateExampleAndConstraintAndSynonyms.generateSynonymsTable(mbbSynonymsTable, codeId, treeItem);
					}
					treeItem_1.setText(name);
				}
			}
		});
	}
	
	void createBusinessTrace() {
		Composite parentComposite = getContainer();
		Composite composite = new Composite(parentComposite, SWT.NONE);
		composite.setLayout(new GridLayout(1, true));
		int index = addPage(composite);
		setPageText(index, "Business Trace");
		
		Label BusinessTraces = new Label(composite, SWT.NONE);
		BusinessTraces.setText("Business Traces");
		BusinessTraces.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		BusinessTraces.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		Label descriptionLabel = new Label(composite, SWT.NONE);
		descriptionLabel.setText("This section describes the Business traces of the message elements to the business model.");
		msgElementTable = new Table(composite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		msgElementTable.setHeaderVisible(true);
		msgElementTable.setLinesVisible(true);
		GridData tableGridData = new GridData();
		tableGridData.widthHint = 1200;
		tableGridData.heightHint = 700;
		msgElementTable.setLayoutData(tableGridData);
		TableColumn msgElementColumn = new TableColumn(msgElementTable, SWT.NONE);
		msgElementColumn.setWidth(350);
		msgElementColumn.setText("Message Element");
		TableColumn isTechnicalColumn = new TableColumn(msgElementTable, SWT.NONE);
		isTechnicalColumn.setWidth(150);
		isTechnicalColumn.setText("Is Technical");
		TableColumn TracesToColumn = new TableColumn(msgElementTable, SWT.NONE);
		TracesToColumn.setWidth(150);
		TracesToColumn.setText("Traces to");
		TableColumn TracePathColumn = new TableColumn(msgElementTable, SWT.NONE);
		TracePathColumn.setWidth(150);
		TracePathColumn.setText("Trace Path");
		TableColumn TypeOfMsgElementTracesToColumn = new TableColumn(msgElementTable, SWT.NONE);
		TypeOfMsgElementTracesToColumn.setWidth(150);
		TypeOfMsgElementTracesToColumn.setText("Type of Message Element traces to");
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
		setPageText(index, "Impact Analysis");
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
		GenerateTableForMCandMDandMS.generateMessageSetTableOfImpactPage(messageSetTable, messageSet);
		
		Group businessAreatGroup = new Group(groupListComposite, SWT.NONE);
		businessAreatGroup.setText("Business Area");
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
		treeCompositeGridData.heightHint = 800;
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
				String messageSetId = (String)tableItem.getData(String.valueOf(messageSetTable.getSelectionIndex()));
				ArrayList<EcoreMessageDefinition> msgDefinitionList = DerbyDaoUtil.getMsgDefinitionByMsgSetId(messageSetId);
				GenerateCommonTree.generateContainmentTreeForMessageSetofImpactPage(treeRoot, msgDefinitionList);
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
				GenerateCommonTree.generateContainmentTreeForMessageSetofImpactPage(treeRoot, DerbyDaoUtil.getMsgDefintionListByBizAreaId(bizAreaId));
			}
		});
		
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	/**
	 * Version Subset page 显示和编辑
	 */
	void createVersionAndSubsets() {

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
		GridData lc_gd = new GridData(SWT.TOP, SWT.TOP, false, false, 1, 1);
		lc_gd.widthHint = 550; //composite.getBounds().width / 3;
		leftComposite.setLayoutData(lc_gd);
		
		GridData rc_gd = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		rc_gd.widthHint = 500;	//composite.getBounds().width / 3;
		Composite rightComposite = new Composite(composite, SWT.NONE);
		rightComposite.setLayout(new FillLayout(SWT.VERTICAL));
		rightComposite.setLayoutData(rc_gd);
		
		// ------------ Next Version Bar Begin ----------------------------------
		ExpandBar nextVersionBar = new ExpandBar(leftComposite, SWT.NONE);

		Composite nextVersionBarComposite = new Composite(nextVersionBar, SWT.NONE);
		GridData contentBarCompositeGridData = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		nextVersionBarComposite.setLayout(new GridLayout(1, true));
		nextVersionBarComposite.setLayoutData(contentBarCompositeGridData);
		
		nextVersionBar.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		nextVersionBar.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		nextVersionBar.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		
		Composite toolbarComposite = new Composite(nextVersionBarComposite, SWT.NONE);
		toolbarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		Button plusButtonForNextVersion = new Button(toolbarComposite, SWT.NONE);
		plusButtonForNextVersion.setImage(ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR));
		plusButtonForNextVersion.addMouseListener(new PlusButtonListener());
		this.setUnavailable(plusButtonForNextVersion);
		
		Button deleteButtonForNextVersion = new Button(toolbarComposite, SWT.NONE);
		deleteButtonForNextVersion.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
		deleteButtonForNextVersion.addMouseListener(new DeleteButtonListener());
		this.setUnavailable(deleteButtonForNextVersion);
		
		nextMsgDefinitionListWidget = new Table(nextVersionBarComposite, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		GridData nextVersionWidgetGridData = new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1);
		nextVersionWidgetGridData.widthHint = 450;
		nextVersionWidgetGridData.heightHint = 250;
		nextMsgDefinitionListWidget.setLayoutData(nextVersionWidgetGridData);

		this.generateNextMsgDefinitionList();
		
		ExpandItem contentItem = new ExpandItem(nextVersionBar, SWT.NONE);
		contentItem.setText("Next Version");
		contentItem.setHeight(350);
		contentItem.setExpanded(true);
		contentItem.setControl(nextVersionBarComposite);
		// ------------ Next Version Bar End ----------------------------------
		
		// ------------ Previous Version Bar Begin ------------------------
		
		ExpandBar previousVersionBar = new ExpandBar(rightComposite, SWT.V_SCROLL);
		previousVersionBar.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		previousVersionBar.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		previousVersionBar.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		
		Composite previousVersionBarComposite = new Composite(previousVersionBar, SWT.NONE);
		previousVersionBarComposite.setLayout(new FillLayout(SWT.VERTICAL));
		
		previousVersionListWidget = new List(previousVersionBarComposite, SWT.BORDER);
		previousVersionListWidget.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));

		this.generatePreviousVersionList();
		
		ExpandItem generalInformationItem = new ExpandItem(previousVersionBar, SWT.NONE);
		generalInformationItem.setText("Previous Version");
		generalInformationItem.setHeight(350);
		generalInformationItem.setExpanded(true);
		generalInformationItem.setControl(previousVersionBarComposite);
		
		// ------------ Previous Version Bar End ------------------------
	
	}
	
	
	private void generateContentTree() {
		if (this.msgDefinition != null && this.msgDefinition.getId() != null) {
			mbbList = DerbyDaoUtil.getMsgBuildingBlock(this.msgDefinition.getId());
			GenerateCommonTree.generateContentTreeForMessageDefinition(null, mbbList, elementsTree, msgDefinition.getRegistrationStatus());
			GenerateCommonTree.addContraintsNode(null, DerbyDaoUtil.getContraints(this.msgDefinition.getId()), null, elementsTree);
		}
	}
	
	private void generatemsgElementTable() {
		if (mbbList == null) {
			return;
		}
		String[] valueArray = new String[5];
		for (EcoreMessageBuildingBlock mbb: mbbList) {
			valueArray[0] = mbb.getName();
			if ("2".equals(mbb.getDataType())) {
				EcoreMessageComponent mc = DerbyDaoUtil.getMessageComponentById(mbb.getDataTypeId());
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
	
	private void generateNextMsgDefinitionList() {
		for (int index = 0; index < this.nextMsgDefinitions.size(); index++) {
			EcoreMessageDefinition msgDefinition = nextMsgDefinitions.get(index);
			StringBuilder sb = new StringBuilder();
			sb.append(msgDefinition.getName());
			sb.append(" (");
			sb.append(msgDefinition.getBusinessArea());
			sb.append(".");
			sb.append(msgDefinition.getMessageFunctionality());
			sb.append(".");
			sb.append(msgDefinition.getFlavour());
			sb.append(".");
			sb.append(msgDefinition.getVersion());
			sb.append(")");
			TableItem tableItem =new TableItem(nextMsgDefinitionListWidget, SWT.NONE);
			tableItem.setText(sb.substring(0));
			tableItem.setData(msgDefinition.getId());
		}
	}
	
	private void generatePreviousVersionList() {
		if (previousMsgDefintion.getId() != null) {
			previousVersionListWidget.add(previousMsgDefintion.getName());
			previousVersionListWidget.setData("previousVersionId", previousMsgDefintion.getId());
		}
	}
	
	void createMessageBox(ArrayList<EcoreMessageDefinition> msgDefinitions) {
		Shell messageDefinitionWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		messageDefinitionWindow.setText("Select Message Definition to Add");
		messageDefinitionWindow.setLayout(new FormLayout());
		
		//改变弹窗位置
		ChangeShellLocation.center(messageDefinitionWindow);
		
		Composite c = new Composite(messageDefinitionWindow, SWT.NONE);
		FormData fd_c = new FormData();
		fd_c.top = new FormAttachment(0);
		fd_c.left = new FormAttachment(0);
		fd_c.bottom = new FormAttachment(100);
		fd_c.right = new FormAttachment(100);
		Text searchText = new Text(c, SWT.BORDER);
		searchText.setBounds(10, 10, 350, 30);
		
		Table msgDefinitionTable = new Table(c, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
		msgDefinitionTable.setLinesVisible(true);

		this.generateAllMsgDefinitionTable(msgDefinitionTable, msgDefinitions);
		
		msgDefinitionTable.setBounds(10, 50, 350, 400);
		
		Button okButton = new Button(c, SWT.PUSH);
		okButton.setText("OK");
		okButton.setBounds(275, 460, 35, 30);
		okButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		okButton.addMouseListener(new OkButtonListenerForVersion(msgDefinitionTable, messageDefinitionWindow));
		
		Button cancelButton = new Button(c, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setBounds(320, 460, 52, 30);
		cancelButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		cancelButton.addMouseListener(new CancelButtonListener(messageDefinitionWindow));

		messageDefinitionWindow.setSize(400, 550);
		messageDefinitionWindow.open();
		messageDefinitionWindow.layout();
		
		searchText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				
				msgDefinitionTable.removeAll();
				
				msgDefinitions.stream().filter(t -> (t.getName() != null && t.getName().toUpperCase().indexOf(searchText.getText().toUpperCase()) != -1)).forEach(t -> {
					TableItem tableItem = new TableItem(msgDefinitionTable, SWT.NONE);
					tableItem.setText(t.getName());
					tableItem.setData(t.getId());
					tableItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB1));
				});
				
			}
			
		});
	}
	
	private void generateAllMsgDefinitionTable(Table msgDefinitionLisTable,
			ArrayList<EcoreMessageDefinition> msgDefinitions) {
		for (EcoreMessageDefinition definition: msgDefinitions) {
			TableItem tableItem = new TableItem(msgDefinitionLisTable, SWT.NONE);
			tableItem.setText(definition.getName());
			tableItem.setData(definition.getId());
			tableItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB1));
		}
		msgDefinitionLisTable.setSelection(0);
	}
	
	class PlusButtonListener extends MouseAdapter  {
		
		@Override
		public void mouseUp(MouseEvent e) {
			createMessageBox(DerbyDaoUtil.getAllMsgDefinition());
			setDirty(true);
		}
	}
	
	class DeleteButtonListener extends MouseAdapter {
		
		@Override
		public void mouseUp(MouseEvent e) {
			for (int index: nextMsgDefinitionListWidget.getSelectionIndices()) {
				nextMsgDefinitionListWidget.remove(index);
			}
			setDirty(true);
		}
	}
	
	class OkButtonListenerForVersion extends MouseAdapter  {
		
		private Table table;
		
		private Shell shell;
		
		public OkButtonListenerForVersion(Table table, Shell shell) {
			this.table = table;
			this.shell = shell;
		}
		
		@Override
		public void mouseUp(MouseEvent e) {
			
			int count = nextMsgDefinitionListWidget.getItemCount();
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
	
	class AddMsgElementListener extends MouseAdapter  {
		
		@Override
		public void mouseUp(MouseEvent e) {
			GenerateChooseMC.createMsgElementSelectionDialogue(elementsTree, msgDefinition);
			setDirty(true);
		}
	}
	
// ------------------ 三个点按钮用 Begin --------------------
	class TypeButtonListener extends MouseAdapter {
		
		private Text typeText;
		
		public TypeButtonListener(Text typeText) {
			super();
			this.typeText = typeText;
		}

		@Override
		public void mouseUp(MouseEvent e) {
			if (elementsTree.getSelectionCount() == 1 && elementsTree.getSelection()[0].getData("msgBldBlock") != null) {
				createSelectMsgElementType(typeText, DerbyDaoUtil.getMessageComponentList(), DerbyDaoUtil.getAllDataType());
			}
			
		}
	}
	
	void createSelectMsgElementType(Text typeText, ArrayList<EcoreMessageComponent> allMessageComponentList, ArrayList<EcoreDataType> allDataTypeList) {
		Shell messageComponentWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		messageComponentWindow.setText("Select Message Element type:");
		messageComponentWindow.setLayout(new FormLayout());
		
		//改变弹窗位置
		ChangeShellLocation.center(messageComponentWindow);
		
		Composite c = new Composite(messageComponentWindow, SWT.NONE);
		FormData fd_c = new FormData();
		fd_c.top = new FormAttachment(0);
		fd_c.left = new FormAttachment(0);
		fd_c.bottom = new FormAttachment(100);
		fd_c.right = new FormAttachment(100);
		Text searchText = new Text(c, SWT.BORDER);
		searchText.setBounds(10, 10, 280, 30);
		Button searchBtn = new Button(c, SWT.NONE);
		searchBtn.setText("Search");
		searchBtn.setBackground(new Color(Display.getCurrent(),135,206,250));
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
		
		Button okButton = new Button(c, SWT.PUSH);
		okButton.setText("OK");
		okButton.setBounds(275, 460, 35, 30);
		okButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		okButton.addMouseListener(new OkButtonListener(typeText, msgComponentTable, messageComponentWindow));
		
		Button cancelButton = new Button(c, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setBounds(320, 460, 52, 30);
		cancelButton.setBackground(new Color(Display.getCurrent(),135,206,250));
		cancelButton.addMouseListener(new CancelButtonListener(messageComponentWindow));
		
		messageComponentWindow.setSize(400, 550);
		messageComponentWindow.open();
		messageComponentWindow.layout();
		
		searchBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				msgComponentTable.removeAll();
				allMessageComponentList.stream().filter(t -> (t.getName() != null && t.getName().toUpperCase().indexOf(searchText.getText().toUpperCase()) != -1)).forEach(t -> {
					TableItem tableItem = new TableItem(msgComponentTable, SWT.NONE);
					tableItem.setText(t.getName());
					tableItem.setData("msgComponentId", t.getId());
					tableItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_COMPONENT));
				});
				allDataTypeList.stream().filter(t -> (t.getName() != null && t.getName().toUpperCase().indexOf(searchText.getText().toUpperCase()) != -1)).forEach(t -> {
					TableItem tableItem = new TableItem(msgComponentTable, SWT.NONE);
					tableItem.setText(t.getName());
					tableItem.setData("dataTypeId", t.getId());
					tableItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
				});
			}
		});
	}
		
	private void generateMessageComponentTable(Table msgComponentTable,
			ArrayList<EcoreMessageComponent> allMessageComponentList, ArrayList<EcoreDataType> allDataTypeList) {
		for (EcoreMessageComponent msgComponent: allMessageComponentList) {
			TableItem tableItem = new TableItem(msgComponentTable, SWT.NONE);
			tableItem.setText(msgComponent.getName());
			tableItem.setData("msgComponentId", msgComponent.getId());
			tableItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_COMPONENT));
		}
		
		for (EcoreDataType ecoreDataType: allDataTypeList) {
			TableItem tableItem = new TableItem(msgComponentTable, SWT.NONE);
			tableItem.setText(ecoreDataType.getName());
			tableItem.setData("dataTypeId", ecoreDataType.getId());
			tableItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
		}
		msgComponentTable.setSelection(0);
	}	
	
	class CancelButtonListener extends MouseAdapter  {
		
		private Shell shell;
		
		public CancelButtonListener(Shell shell) {
			this.shell = shell;
		}
		
		@Override
		public void mouseUp(MouseEvent e) {
			shell.close();
		}
	}
	
	
	class OkButtonListener extends MouseAdapter  {
		
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
			
			for (TableItem tableItem: tableItemArray) {
				
				TreeItem currentTreeItem = elementsTree.getSelection()[0];
				EcoreMessageBuildingBlock msgBldBlock = (EcoreMessageBuildingBlock)currentTreeItem.getData("msgBldBlock");
				
				this.typeText.setText(tableItem.getText());
				String displayName = mbbNameValue.getText() + " [" + mbbMinOccursValue.getText() + ", "
						+ mbbMaxOccursValue.getText() + "] : " + tableItem.getText();
				currentTreeItem.setText(displayName);
				
				if (tableItem.getData("dataTypeId") != null) {
//					this.typeText.setData("dataTypeId", String.valueOf(tableItem.getData("dataTypeId")));

					currentTreeItem.setData("dataTypeId", String.valueOf(tableItem.getData("dataTypeId")));
					currentTreeItem.setData("dataType", ObjTypeEnum.DataType.getType());
					msgBldBlock.setDataTypeId(String.valueOf(tableItem.getData("dataTypeId")));
					msgBldBlock.setDataType("1");
					currentTreeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
					currentTreeItem.setData("typeName", tableItem.getText());
					// 删除原来的Datatype下的节点
					currentTreeItem.removeAll();
					// load datatype 下的 TYPE是1的 code(ECORE_CODE)
					GenerateCommonTree.generateContentTreeForDataType(currentTreeItem, DerbyDaoUtil.getEcoreCodeListByDataTypeId(String.valueOf(tableItem.getData("dataTypeId"))));
					// 当前选中的DataType的约束
					GenerateCommonTree.addContraintsNode(currentTreeItem, DerbyDaoUtil.getContraints(String.valueOf(tableItem.getData("dataTypeId"))), tableItem.getText(), null);
				}
				
				if (tableItem.getData("msgComponentId") != null) {
//					this.typeText.setData("msgComponentId", String.valueOf(tableItem.getData("msgComponentId")));
					
					currentTreeItem.setData("msgComponentId", String.valueOf(tableItem.getData("msgComponentId")));
					currentTreeItem.setData("dataType", ObjTypeEnum.MessageComponent.getType());
					msgBldBlock.setDataTypeId(String.valueOf(tableItem.getData("msgComponentId")));
					msgBldBlock.setDataType("2");
					currentTreeItem.setImage(ImgUtil.createImg(ImgUtil.ME));
					currentTreeItem.setData("typeName", tableItem.getText());
					// 删除原来的Message Component下的节点
					currentTreeItem.removeAll();
					// 用Message Component里的元素，构造Message Component树
					GenerateCommonTree.generateContentTreeForMessageComponent(currentTreeItem, DerbyDaoUtil.getMessageElementList(String.valueOf(tableItem.getData("msgComponentId"))), 
							DerbyDaoUtil.getMessageComponentById(String.valueOf(tableItem.getData("msgComponentId"))).getRegistrationStatus());
					// 当前选中的Message Component的约束
					GenerateCommonTree.addContraintsNode(currentTreeItem, DerbyDaoUtil.getContraints(String.valueOf(tableItem.getData("msgComponentId"))), tableItem.getText(), null);
				}
				
			}
			shell.close();
		}
	}
	
	// ------------------ 三个点按钮用 End --------------------	
	
	class AddConstraintListener extends MouseAdapter  {
		
		@Override
		public void mouseUp(MouseEvent e) {
			GenerateChooseMC.createConstraintDialogue(elementsTree);
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
			for (TreeItem treeItem: elementsTree.getSelection()) {
				treeItem.dispose();
			}
			
			if (elementsTree.getItemCount() == 0) {
				for (Control control: rightComposite.getChildren()) {
					control.dispose();
				}
			}
			
			setDirty(true);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void setUnavailable(Widget swtWidget) {
		if (RegistrationStatus.Registered.getStatus().equals(this.ecoreTreeNode.getRegistrationStatus())) {
			if (swtWidget instanceof Text) {
				((Text) swtWidget).setEditable(false);
			} else if (swtWidget instanceof Button) {
				((Button) swtWidget).setEnabled(false);
			}
		}
	}
	
	@Override
	public void setDirty(boolean dirty) {
		if (dirty && RegistrationStatus.Registered.getStatus().equals(this.ecoreTreeNode.getRegistrationStatus())) {
			return;
		}
		super.setDirty(dirty);
	}
	
	private void showMessageBuildingBlockBar(Composite composite, ScrolledComposite scrolledComposite) {
		
		for (Control control: composite.getChildren()) {
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
//		mbbNameValue.addModifyListener(new EditorModifyListener(this, this.elementsTree));
		mbbNameValue.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTableItem.isDisposed()) {
					// 左边的树的名称展示
					String showText = "";
					if (mbbNameValue.getText().isEmpty()) {
						showText = mbbNameValue.getText() + " [" + mbbMinOccursValue.getText() + ", "
								+ mbbMaxOccursValue.getText() + "]";
					} else {
						showText = mbbNameValue.getText() + " [" + mbbMinOccursValue.getText() + ", "
								+ mbbMaxOccursValue.getText() + "] : " + mbbNameValue.getText();
					}
					selectedTableItem.setText(showText);

					selectedTableItem.setData("mbbNameValue", mbbNameValue.getText());
					setDirty(true);
				}
			}
		});
		this.setUnavailable(mbbNameValue);
		
		GridData documentationGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		documentationGridData.widthHint = 350;
		documentationGridData.heightHint = 150;
		Label documentationLabel = new Label(generalInformationComposite, SWT.NONE);
		documentationLabel.setText("Documentation");
		mbbDocumentationValue = new Text(generalInformationComposite, SWT.BORDER | SWT.WRAP);
		mbbDocumentationValue.setData("hint", "definition");
		mbbDocumentationValue.setLayoutData(documentationGridData);
//		mbbDocumentationValue.addModifyListener(new EditorModifyListener(this, this.elementsTree));
		mbbDocumentationValue.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTableItem.isDisposed()) {
					selectedTableItem.setData("mbbDocumentationValue", mbbDocumentationValue.getText());
					setDirty(true);
				}
			}
		});
		this.setUnavailable(mbbDocumentationValue);
		
		Label minOccursLabel = new Label(generalInformationComposite, SWT.NONE);
		minOccursLabel.setText("Min Occurs");
		mbbMinOccursValue = new Text(generalInformationComposite, SWT.BORDER);
		mbbMinOccursValue.setData("hint", "min");
//		mbbMinOccursValue.addModifyListener(new EditorModifyListener(this, this.elementsTree));
		mbbMinOccursValue.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTableItem.isDisposed()) {

					String showText = "";
					if (typeValue.getText().isEmpty()) {
						showText = mbbNameValue.getText() + " [" + mbbMinOccursValue.getText() + ", "
								+ mbbMaxOccursValue.getText() + "]";
					} else {
						showText = mbbNameValue.getText() + " [" + mbbMinOccursValue.getText() + ", "
								+ mbbMaxOccursValue.getText() + "] : " + typeValue.getText();
					}
					selectedTableItem.setText(showText);

					selectedTableItem.setData("mbbMinOccursValue", mbbMinOccursValue.getText());
					setDirty(true);
				}
			}
		});
		this.setUnavailable(mbbMinOccursValue);
			
		Label MaxOccursLabel = new Label(generalInformationComposite, SWT.NONE);
		MaxOccursLabel.setText("Max Occurs");
		mbbMaxOccursValue = new Text(generalInformationComposite, SWT.BORDER);
		mbbMaxOccursValue.setData("hint", "max");
		mbbMaxOccursValue.addModifyListener(new EditorModifyListener(this, this.elementsTree));
		mbbMaxOccursValue.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTableItem.isDisposed()) {

					String showText = "";
					if (typeValue.getText().isEmpty()) {
						showText = mbbNameValue.getText() + " [" + mbbMinOccursValue.getText() + ", "
								+ mbbMaxOccursValue.getText() + "]";
					} else {
						showText = mbbNameValue.getText() + " [" + mbbMinOccursValue.getText() + ", "
								+ mbbMaxOccursValue.getText() + "] : " + typeValue.getText();
					}
					selectedTableItem.setText(showText);

					selectedTableItem.setData("mbbMaxOccursValue", mbbMaxOccursValue.getText());
					setDirty(true);
				}
			}
		});
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
						EcoreMessageBuildingBlock msgBldBlock = (EcoreMessageBuildingBlock)treeItem.getData("msgBldBlock");
						msgBldBlock.setXmlTag(xmlTagValue);
					}
				}
			}
			
		});
		
		this.setUnavailable(xmlTagValueOfContentPage);
		
		Label isDerivedLabel = new Label(generalInformationComposite, SWT.NONE);
		isDerivedLabel.setText("Derived");
		isDerivedCheckButton = new Button(generalInformationComposite, SWT.CHECK);
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
		
		dotBtn = new Button(cmp, SWT.NONE);
		dotBtn.setText("choose");
		dotBtn.addMouseListener(new TypeButtonListener(typeValue));
		dotBtn.setBackground(new Color(Display.getCurrent(),135,206,250));
		this.setUnavailable(dotBtn);
		
		Button typeBtn = new Button(cmp, SWT.NONE);
		typeBtn.setImage(ImgUtil.createImg(ImgUtil.SEARCH_BTN));
		typeBtn.setBackground(new Color(Display.getCurrent(),135,206,250));
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
		addMbbSynonymsBtn = new Button(synonymsToolbarComposite, SWT.NONE);
		addMbbSynonymsBtn.setImage(ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR));
		addMbbSynonymsBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (elementsTree.getSelectionCount() == 1) {
					TreeItem selectedItem = elementsTree.getSelection()[0];
					if (selectedItem.getData("msgComponentId") != null || selectedItem.getData("dataTypeId") != null) {
						GenerateSynonymsAndConstraintDialogForContent.createSynonymsDialogue(mbbSynonymsTable, selectedItem);
					}
				}
				setDirty(true);
			}
		});
		this.setUnavailable(addMbbSynonymsBtn);
		
		deleteMbbSynonymsBtn = new Button(synonymsToolbarComposite, SWT.NONE);
		deleteMbbSynonymsBtn.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
		deleteMbbSynonymsBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				for (TableItem tableItem: mbbSynonymsTable.getSelection()) {
					tableItem.dispose();
				}
				ArrayList<String> synonymsList = new ArrayList<>();
				for (TableItem tableRow: mbbSynonymsTable.getItems()) {
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
		
		// ---------- Constraints Expand Item Begin -------------------------------------
		
		Composite contraintsComposite = new Composite(msgBldBlockBar, SWT.NONE);
		contraintsComposite.setLayout(new GridLayout(1, false));
		Label contraintsDescLabel = new Label(contraintsComposite, SWT.WRAP);
		contraintsDescLabel.setText("All the constraints contained in this object(other constraints - such as constraints defined on type - may also apply).");
		GridData labelGridData = new GridData();
		labelGridData.widthHint = 430;
		contraintsDescLabel.setLayoutData(labelGridData);
		
		Composite constraintsToolbarComposite = new Composite(contraintsComposite, SWT.NONE);
		constraintsToolbarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		addMbbConstraintsBtn = new Button(constraintsToolbarComposite, SWT.NONE);
		addMbbConstraintsBtn.setImage(ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR));
		deleteMbbConstraintsBtn = new Button(constraintsToolbarComposite, SWT.NONE);
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
		
		addMbbConstraintsBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (elementsTree.getSelectionCount() == 1) {
					TreeItem selectedItem = elementsTree.getSelection()[0];
					if (selectedItem.getData("msgComponentId") != null || selectedItem.getData("dataTypeId") != null) {
						GenerateSynonymsAndConstraintDialogForContent.createConstraintDialogueOfContent(mbbConstraintsTable, selectedItem);
					}
				}
				setDirty(true);
			}
		});
		this.setUnavailable(addMbbConstraintsBtn);
		
		deleteMbbConstraintsBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				for (TableItem tableItem: mbbConstraintsTable.getSelection()) {
					tableItem.dispose();
				}
				ArrayList<EcoreConstraint> mbbConstraintList = new ArrayList<>(); 
				for (TableItem tableRow: mbbConstraintsTable.getItems()) {
					mbbConstraintList.add((EcoreConstraint)tableRow.getData());
				}
				TreeItem selectedItem = elementsTree.getSelection()[0];
				selectedItem.setData("mbbConstraintList", mbbConstraintList);
				setDirty(true);
			}
		});
		this.setUnavailable(deleteMbbConstraintsBtn);
		
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
		
		for (Control control: composite.getChildren()) {
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
		codeDetailNameValue.setLayoutData(generalInforGridData);
		
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
		contraintsDescLabel.setText("All the constraints contained in this object(other constraints - such as constraints defined on type - may also apply).");
		GridData labelGridData = new GridData();
		labelGridData.widthHint = 430;
		contraintsDescLabel.setLayoutData(labelGridData);
		
		Composite constraintsToolbarComposite = new Composite(contraintsComposite, SWT.NONE);
		constraintsToolbarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		addMbbConstraintsBtn = new Button(constraintsToolbarComposite, SWT.NONE);
		addMbbConstraintsBtn.setImage(ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR));
		deleteMbbConstraintsBtn = new Button(constraintsToolbarComposite, SWT.NONE);
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
		
		addMbbConstraintsBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (elementsTree.getSelectionCount() == 1) {
					TreeItem selectedItem = elementsTree.getSelection()[0];
					if (selectedItem.getData("msgComponentId") != null || selectedItem.getData("dataTypeId") != null) {
						GenerateSynonymsAndConstraintDialogForContent.createConstraintDialogueOfContent(mbbConstraintsTable, selectedItem);
					}
				}
				setDirty(true);
			}
		});
		this.setUnavailable(addMbbConstraintsBtn);
		
		deleteMbbConstraintsBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				for (TableItem tableItem: mbbConstraintsTable.getSelection()) {
					tableItem.dispose();
				}
				ArrayList<EcoreConstraint> mbbConstraintList = new ArrayList<>(); 
				for (TableItem tableRow: mbbConstraintsTable.getItems()) {
					mbbConstraintList.add((EcoreConstraint)tableRow.getData());
				}
				TreeItem selectedItem = elementsTree.getSelection()[0];
				selectedItem.setData("mbbConstraintList", mbbConstraintList);
				setDirty(true);
			}
		});
		this.setUnavailable(deleteMbbConstraintsBtn);
		
		ExpandItem contraintsItem = new ExpandItem(codeDetailBar, SWT.NONE);
		contraintsItem.setText("Contraints");
		contraintsItem.setHeight(500);
		contraintsItem.setExpanded(true);
		contraintsItem.setControl(contraintsComposite);
		
		
		// ---------- Code Detail Constraints Expand Item End -------------------------------------

		composite.requestLayout();
		
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		// ------------ Code Details Bar End ---------------------------------		
	}
	
	private void showConstraintBar(Composite composite, ScrolledComposite scrolledComposite) {
		
		for (Control control: composite.getChildren()) {
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
