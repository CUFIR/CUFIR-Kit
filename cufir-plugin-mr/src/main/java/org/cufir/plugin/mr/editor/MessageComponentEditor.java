package org.cufir.plugin.mr.editor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.cufir.plugin.mr.MrHelper;
import org.cufir.plugin.mr.bean.ButtonPolicy;
import org.cufir.plugin.mr.bean.ComboPolicy;
import org.cufir.plugin.mr.bean.MrTreeItem;
import org.cufir.plugin.mr.bean.ObjTypeEnum;
import org.cufir.plugin.mr.bean.RegistrationStatusEnum;
import org.cufir.plugin.mr.bean.TextPolicy;
import org.cufir.plugin.mr.bean.TransferDataBean;
import org.cufir.plugin.mr.handlers.SaveHandler;
import org.cufir.plugin.mr.utils.ImgUtil;
import org.cufir.plugin.mr.utils.NumberUtil;
import org.cufir.plugin.mr.utils.SystemUtil;
import org.cufir.s.data.bean.EcoreBusinessComponent;
import org.cufir.s.data.bean.EcoreBusinessElement;
import org.cufir.s.data.bean.EcoreCode;
import org.cufir.s.data.bean.EcoreConstraint;
import org.cufir.s.data.bean.EcoreDataType;
import org.cufir.s.data.bean.EcoreExample;
import org.cufir.s.data.bean.EcoreExternalSchema;
import org.cufir.s.data.bean.EcoreMessageBuildingBlock;
import org.cufir.s.data.bean.EcoreMessageComponent;
import org.cufir.s.data.bean.EcoreMessageDefinition;
import org.cufir.s.data.bean.EcoreMessageElement;
import org.cufir.s.data.bean.EcoreMessageSet;
import org.cufir.s.data.bean.EcoreNextVersions;
import org.cufir.s.data.vo.EcoreMessageComponentVO;
import org.cufir.s.data.vo.EcoreMessageElementVO;
import org.cufir.s.data.vo.EcoreTreeNode;
import org.cufir.s.data.vo.SynonymVO;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.springframework.util.StringUtils;

import com.cfets.cufir.s.ide.utils.i18n.I18nApi;

/**
 * MessageComponents（元素组）新建、编辑
 * 
 * @author qiming_het
 *
 */
public class MessageComponentEditor extends MrMultiPageEditor {

	private java.util.List<EcoreMessageComponent> mcs = MrRepository.get().ecoreMessageComponents;
	private java.util.List<EcoreConstraint> css = MrRepository.get().ecoreConstraints;
	private java.util.List<EcoreMessageElement> mes = MrRepository.get().ecoreMessageElements;
	private java.util.List<EcoreDataType> dts = MrRepository.get().ecoreDataTypes;
	private java.util.List<EcoreCode> cs = MrRepository.get().ecoreCodes;
	private java.util.List<EcoreMessageBuildingBlock> mbbs =  MrRepository.get().ecoreMessageBuildingBlocks;
	private java.util.List<EcoreExternalSchema> ess =  MrRepository.get().ecoreExternalSchemas;
	
	private Map<String, EcoreMessageComponent> emcMap = MrRepository.get().ecoreMessageComponentMapById;
	private Map<String, EcoreMessageElement> emeMap = MrRepository.get().ecoreMessageElementMapById;
	
	private String messageComponentId;

	// -----------For Summary Page -----------
	private Text summaryNameText, summaryDocText, summaryobjectIdentifierText, summaryRemovalDate;
	public Combo summaryStatusCombo;
	private Button btBtn, bt;

	private Button choiceButton, choButton, Delbutton;
	private Table summaryConstraintsTable;
	private List examplesList;

	// ----------- For Content Page -------------
	private Tree elementsTree, tableTree;
	private Text meNameText, meDocText, meMinOccursText, meMaxOccursText, meTypeText, meXmlTagText,
			meObjectIdentifierText, codeDocumentText, codeNameText, nameText, bcTypeText;
//	private Combo meStatusCombo;
	private Button isDerivedCheckButton, compositeCheckButton, addExamplesBtn, deleteExamplesBtn, addConstraintBtn,
			editConstraintBtn, deleteConstraintBtn, addMessageElementBtn,
			deleteBusinessElementBtn, addSynonymsBtn, deleteButtonForSynonyms;
	private Table meConstraintsTable;
	private Table meSynonymsTable;
	private TreeItem selectedTableItem, item1;

	// ------------For All--------------
	// 基本数据类型
	private String dataType;
	public TreeItem modelExploreTreeItem;
	private MrEditorInput myEditorInput;

	// ----------For Business Traces Page -----
	private Table messageDefinitionTable;
	private Table messageComponentTable;

	// ----------For impact page -------------
	private Table messageSetTable;

	// ----------For Version/Subsets Page -----
	private List nextVersionListWidget;
	private List previousVersionListWidget;

	private EcoreMessageComponent msgComponent = null;
	private java.util.List<EcoreMessageElementVO> msgElementList = new ArrayList<>();
	private java.util.List<EcoreMessageDefinition> mds = new ArrayList<>();
	private EcoreTreeNode ecoreTreeNode;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	// ----填充的数据------
	private EcoreMessageComponentVO ecoreMessageComponentVO;

	private Map<String, ArrayList<EcoreBusinessElement>> allBeByComponentId;

	private Map<String, ArrayList<EcoreTreeNode>> allSubBusiness;

	private TreeItem[] items;

	private EcoreDataType dataTypeById;

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		this.setSite(site);
		this.setInput(input);
		this.setPartProperty("customName", "msgComponentCreate");
		this.myEditorInput = (MrEditorInput) input;
		this.dataType = this.myEditorInput.getTransferDataBean().getType();
		modelExploreTreeItem = this.myEditorInput.getTransferDataBean().getTreeListItem();
		ecoreTreeNode = (EcoreTreeNode) modelExploreTreeItem.getData("EcoreTreeNode");
		messageComponentId = this.myEditorInput.getTransferDataBean().getId();
		this.setPartProperty("ID", messageComponentId);
		// 初始化值
		initFilledData();
		// ctrl + s保存必须要
		setContext(site);

		// 不为null代表是打开的,可编辑
		if (msgComponent != null) {
			this.setPartName(msgComponent.getName());
			this.setDirty(false);
			if (msgComponent.getRegistrationStatus().equals("Provisionally Registered") || msgComponent.getRegistrationStatus().equals("Added Registered")) {
				this.setTitleImage(ImgUtil.createImg(ImgUtil.MC_SUB3_COMPONENT));
			} else {
				this.setTitleImage(ImgUtil.createImg(ImgUtil.MC_SUB1_COMPONENT));
			}
		} else {
			this.setPartName("");
			this.setDirty(true);
			if (msgComponent == null) {
				this.setTitleImage(ImgUtil.createImg(ImgUtil.MC_SUB3_COMPONENT));
			} else {
				if (msgComponent.getRegistrationStatus() == null) {
					this.setTitleImage(ImgUtil.createImg(ImgUtil.MC_SUB3_COMPONENT));
				}
			}
		}
	}

	/**
	 * 初始化编辑的值
	 */
	private void initFilledData() {
		// 不为null代表是打开的,可编辑
		try {
			msgComponent = emcMap.get(messageComponentId);
			if(msgComponent != null) {
				java.util.List<EcoreMessageBuildingBlock> embb = MrRepository.get().ecoreMessageBuildingBlocks;
				java.util.List<String> mbbMdIds = embb.stream().filter(eme -> eme.getDataTypeId() != null && eme.getDataTypeId().equals(messageComponentId)).map(eme -> eme.getMessageId()).collect(Collectors.toList());
				java.util.List<EcoreMessageDefinition> emds = MrRepository.get().ecoreMessageDefinitions;
				mds = emds.stream().filter(md -> mbbMdIds.contains(md.getId())).collect(Collectors.toList());
				java.util.List<EcoreMessageElement> mes = MrRepository.get().ecoreMessageElementMapByMsgComponentId.get(messageComponentId);
				allBeByComponentId = MrHelper.getAllBeByComponentId();
				allSubBusiness = MrHelper.getAllSubBusiness();
				ecoreMessageComponentVO = MrImplManager.get().getEcoreMessageComponentImpl().findMessageComponentVO(mes,dts,ess,mcs);
				if (ecoreMessageComponentVO != null) {
					msgElementList = ecoreMessageComponentVO.getEcoreMessageElementVOs();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void createPages() {
		createSummaryPage();
		createContentPage();
		createBusinessTrace();
		createImpactPage();
		createVersionsAndSubsets();
		changeStatus();

		// 不为null代表是打开的,可编辑
		if (msgComponent != null) {
			this.setPartName(msgComponent.getName());
			if ("2".equals(msgComponent.getComponentType())) {
				if (msgComponent.getRegistrationStatus().equals(RegistrationStatusEnum.Registered.getStatus())
						|| msgComponent.getRegistrationStatus().equals(RegistrationStatusEnum.Obsolete.getStatus())) {
					this.setTitleImage(ImgUtil.createImg(ImgUtil.MC_SUB1_CHOICE));
					this.setDirty(false);
				} else {
					this.setTitleImage(ImgUtil.createImg(ImgUtil.MC_SUB1_CHOICE1));
					this.setDirty(false);
				}
			} else {
				if (msgComponent.getRegistrationStatus().equals(RegistrationStatusEnum.Registered.getStatus())
						|| msgComponent.getRegistrationStatus().equals(RegistrationStatusEnum.Obsolete.getStatus())) {
					this.setTitleImage(ImgUtil.createImg(ImgUtil.MC_SUB1_COMPONENT));
					this.setDirty(false);
				} else {
					this.setTitleImage(ImgUtil.createImg(ImgUtil.MC_SUB3_COMPONENT));
					this.setDirty(false);
				}
			}
		}
	}

	@Override
	public void setDirty(boolean dirty) {
		if (dirty && msgComponent == null) {
			super.setDirty(dirty);
		} else if (dirty && msgComponent.getRegistrationStatus()
				.equals(RegistrationStatusEnum.Registered.getStatus())) {
			super.setDirty(false);
		} else {
			super.setDirty(dirty);
		}
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

	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	void createSummaryPage() {

		// 是否可编辑
		boolean isEditable = !RegistrationStatusEnum.Registered.getStatus().equals(this.ecoreTreeNode.getRegistrationStatus());
		
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

		String name = msgComponent == null ? "" : msgComponent.getName();
		ModifyListener modifyListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				modelExploreTreeItem.setText(summaryNameText.getText() == null ? "" : summaryNameText.getText());
				setPartName(summaryNameText.getText());
				setDirty(true);
			}
		};
		MouseAdapter mouseAdapter = new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				generateName();
			}
		};
		//名称
		summaryNameText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_BUTTON_TYPE, 45, "Name", isEditable, name, modifyListener, mouseAdapter)).getText();

		String documentation = msgComponent == null ? "" : msgComponent.getDefinition();
		summaryDocText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_SCROLL_TYPE, 73, "Documentation", isEditable, documentation, new MrEditorModifyListener(this))).getText();
		
		choiceButton = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_SUMMARY_TYPE_CHECK, 278, "This component is a choice", false)).getButton();
		if (msgComponent != null) {
			if ("2".equals(msgComponent.getComponentType())) {
				choiceButton.setSelection(true);
			}
		}
		choiceButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				if (!choiceButton.getSelection()) {
					if (msgComponent == null) {
						modelExploreTreeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB3_COMPONENT));
					} else {
						if (msgComponent.getRegistrationStatus() == null
								|| msgComponent.getRegistrationStatus()
										.equals("Provisionally Registered")
								|| msgComponent.getRegistrationStatus()
										.equals("Added Registered")) {
							modelExploreTreeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB3_COMPONENT));
						} else {
							modelExploreTreeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_COMPONENT));
						}
					}
				} else {
					if (msgComponent == null) {
						modelExploreTreeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_CHOICE1));
					} else {
						if (msgComponent.getRegistrationStatus() == null) {
							modelExploreTreeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_CHOICE1));
						} else {
							if (msgComponent.getRegistrationStatus()
									.equals("Provisionally Registered")
									|| msgComponent.getRegistrationStatus()
											.equals("Added Registered")) {
								modelExploreTreeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_CHOICE1));
							} else {
								modelExploreTreeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_CHOICE));
							}
						}
					}
				}
			}
		});
		// ================= General Information Group End ============================

		// ================= CMP Information Group Begin ==============================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 328, I18nApi.get("editor.title.cmp")));
		// 对象标识符
		String objectIdentifier = msgComponent == null ? "" : msgComponent.getObjectIdentifier();
		summaryobjectIdentifierText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 368, "Object Identifier", isEditable, objectIdentifier, new MrEditorModifyListener(this))).getText();
		// ================= CMP Information Group End ==============================

		// ===== Registration Information Group Begin ===============================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 418, I18nApi.get("editor.title.ri")));
		//状态
		java.util.List<String> list = new ArrayList<>();
		list.add(RegistrationStatusEnum.Registered.getStatus());
		list.add(RegistrationStatusEnum.Provisionally.getStatus());
		list.add(RegistrationStatusEnum.Added.getStatus());
		list.add(RegistrationStatusEnum.Obsolete.getStatus());
		
		String statusText = "";
		if (msgComponent != null) {
			statusText = msgComponent.getRegistrationStatus() == null ? "" : msgComponent.getRegistrationStatus();
		} else {
			statusText = RegistrationStatusEnum.Added.getStatus();
		}
		
		summaryStatusCombo = new SummaryRowComboComposite(summaryComposite, new ComboPolicy(ComboPolicy.COMBO_COMMONLY_TYPE, 458,"Registration Status", false, list, statusText)).getCombo();
		
		String removalDate = msgComponent == null || msgComponent.getRemovalDate() == null ? ""
				: DateFormat.getInstance().format(msgComponent.getRemovalDate());
		
		summaryRemovalDate = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_SHORT_TYPE, 486, "Removal Date", isEditable, removalDate, new MrEditorModifyListener(this))).getText();
		// ===== Registration Information Group End ===============================

		// =================== Exampel Group Begin ==============================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 536, I18nApi.get("editor.title.es")));
		
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_NOTE_TYPE, 586, "In this section, you can define exampless.")).getText();

		addExamplesBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR))).getButton();
		addExamplesBtn.setBounds(10, 614, 30, 30);
		addExamplesBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				MrEditorDialogCreator.createExamplesDialogue(examplesList);

			}
		});
		
		deleteExamplesBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES))).getButton();
		deleteExamplesBtn.setBounds(42, 614, 30, 30);
		deleteExamplesBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				MrEditorDialogCreator.deleteExamplesDialogue(examplesList);
			}
		});

		examplesList = new List(summaryComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		examplesList.setBounds(10, 649, 600, 100);
		GridData examplesListGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		examplesListGridData.widthHint = 500;
		examplesListGridData.heightHint = 100;
		examplesList.setLayoutData(examplesListGridData);
		
		examplesList.removeAll();
		java.util.List<EcoreExample> ecoreExampleList = MrImplManager.get().getEcoreExampleImpl().findByObjId(messageComponentId);
		Map<String, String> map = new HashMap<String, String>();
		for (EcoreExample ecoreExample : ecoreExampleList) {
			map.put(ecoreExample.getExample(), ecoreExample.getId());
			examplesList.add(ecoreExample.getExample());
		}
		examplesList.setData("map", map);
		// =================== Exampel Group End ==============================

		// =================== constraints Begin ==============================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 774, I18nApi.get("editor.title.cs")));
		
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_NOTE_TYPE, 814, "All the constraints contained in this object (other constraints - such as constraints defined on type - may also apply).")).getText();
		
		addConstraintBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR))).getButton();
		addConstraintBtn.setBounds(10, 842, 30, 30);
		addConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				MrEditorDialogCreator.createConstraintDialogue("add", "1", summaryConstraintsTable, null, dataType);
			}
		});

		editConstraintBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.EDIT_IMG_TOOLBAR))).getButton();
		editConstraintBtn.setBounds(42, 842, 30, 30);
		editConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				// 不选中不触发任何效果
				if (summaryConstraintsTable.getSelectionCount() > 0) {
					setDirty(true);
					MrEditorDialogCreator.createConstraintDialogue("edit", "1", summaryConstraintsTable, null,
							dataType);
				}
			}
		});

		deleteConstraintBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES))).getButton();
		deleteConstraintBtn.setBounds(74, 842, 30, 30);
		deleteConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				MrEditorDialogCreator.deleteConstraint("1", summaryConstraintsTable, null, dataType);
			}
		});

		summaryConstraintsTable = new Table(summaryComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		summaryConstraintsTable.setHeaderVisible(true);
		summaryConstraintsTable.setLinesVisible(true);
		summaryConstraintsTable.setBounds(10, 877, 600, 150);
		summaryConstraintsTable.layout();
		new SummaryTableComposite(summaryConstraintsTable, 149, "Name");
		new SummaryTableComposite(summaryConstraintsTable, 149, "Definiton");
		new SummaryTableComposite(summaryConstraintsTable, 149, "Expression Language");
		new SummaryTableComposite(summaryConstraintsTable, 149, "Expression");

		java.util.List<EcoreConstraint> ecoreConstraintList = MrHelper.findConstraintsByObjId(css, messageComponentId);
		// 处理Constraint
		summaryConstraintsTable.removeAll();
		for (EcoreConstraint ecoreConstraint : ecoreConstraintList) {
			TableItem tableItem = new TableItem(summaryConstraintsTable, SWT.NONE);
			tableItem.setText(new String[] { ecoreConstraint.getName(), ecoreConstraint.getDefinition(),
					ecoreConstraint.getExpression(), ecoreConstraint.getExpressionlanguage() });
		}
		// =================== constraints End ==============================

		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	private void changeEditor() {
		// 注册状态为已注册和临时注册，则内容不可编辑
		choiceButton.setEnabled(false);
		addExamplesBtn.setEnabled(false);
		deleteExamplesBtn.setEnabled(false);
		addConstraintBtn.setEnabled(false);
		editConstraintBtn.setEnabled(false);
		deleteConstraintBtn.setEnabled(false);
		addMessageElementBtn.setEnabled(false);
		deleteBusinessElementBtn.setEnabled(false);
		bt.setEnabled(false);
		choButton.setEnabled(false);
		Delbutton.setEnabled(false);
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
		rightComposite.setBounds(510, 0, 500, 1045);
		rightComposite.layout();

		// ------------ Content Expand Bar Begin ----------------------------------
		Composite leftCom = new Composite(leftComposite, SWT.NONE);
		leftCom.setBounds(0, 5, 490, 730);
		leftCom.layout();
		
		new SummaryRowTextComposite(leftCom, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 0, I18nApi.get("editor.title.ct")));

		addMessageElementBtn = new SummaryRowTextComposite(leftCom, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.MC_ADD_ELEMENT))).getButton();
		addMessageElementBtn.setBounds(5, 35, 30, 30);
		addMessageElementBtn.addMouseListener(new MouseAdapter() {
			private String text;

			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				text = summaryStatusCombo.getText();
				addMessageElementDetailBar(rightComposite, scrolledComposite, elementsTree, text);
			}

			private void addMessageElementDetailBar(Composite rightComposite, ScrolledComposite scrolledComposite,
					Tree elementsTree, String text2) {
				// ------------ Message Element Details Begin ------------------------
				for (Control control : rightComposite.getChildren()) {
					control.dispose();
				}
				// ------------ Message Element Details Begin ------------------------
				new SummaryRowTextComposite(leftCom, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 0, I18nApi.get("editor.title.megi")));
				
				Text nameText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 45, "Name")).getText();

				Text docText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SCROLL_TYPE, 73, "Documentation")).getText();
				
				Text minText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SHORT_TYPE, 228, "Min Occurs", "1")).getText();
				
				Text maxOccursText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SHORT_TYPE, 256, "Max Occurs", "1")).getText();
				
				Button isTechnicalButton = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_CHECK, 284, "Is Technical", false)).getButton();
				
				Text typeText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_BUTTON_SELECT_TYPE, 312, "Type", false, "")).getText();
				
				Button typeButton = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_PUSH_DEEP,"choose")).getButton();
				typeButton.setBounds(390, 312, 50, 25);
				typeButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseUp(MouseEvent e) {
						try {
							MrEditorDialogCreator.createSelectMsgElementType(typeText);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});
				Text xmlTagText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 340, "XML Tag")).getText();

				Button finishButton = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_PUSH_DEEP, "Finish")).getButton();
				finishButton.setBounds(280, 390, 60, 30);
				finishButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseUp(MouseEvent e) {
						TreeItem treeItem = new TreeItem(elementsTree, SWT.NONE);
						String nodeName;
						String min = StringUtils.isEmpty(minText.getText()) ? "0" : minText.getText();
						String max = StringUtils.isEmpty(maxOccursText.getText()) ? "*" : minText.getText();
						if (typeText.getText().isEmpty()) {
							nodeName = nameText.getText() + " [" + min + ", " + max + "]";
						} else {
							nodeName = nameText.getText() + " [" + min + ", " + max + "] : " + typeText.getText();
						}
						treeItem.setText(nodeName);
						treeItem.setData("id",
								treeItem.getData("id") == null ? UUID.randomUUID() : treeItem.getData("id").toString());
						treeItem.setData("meNameText", nameText.getText());
						treeItem.setData("meDocText", docText.getText());
						treeItem.setData("meMinOccursText", minText.getText());
						treeItem.setData("meMaxOccursText", maxOccursText.getText());
						treeItem.setData("isTechnical", isTechnicalButton.getSelection());
						treeItem.setData("meType_id", String.valueOf(typeText.getData("meType_id")));
						treeItem.setData("meType", String.valueOf(typeText.getData("meType")));
						treeItem.setData("meStatusCombo", text);
						treeItem.setData("meTypeName", typeText.getText());
						treeItem.setData("meXmlTagText", xmlTagText.getText());
						treeItem.setData("mcstatus", 1);
						treeItem.setData("content", "add");

						if ("1".equals(String.valueOf(typeText.getData("meType")))) {
							treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
						} else if ("2".equals(String.valueOf(typeText.getData("meType")))) {
							treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT1));
						} else {
							treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
						}
						// 用Message Component里的元素，构造Message Component树
						new MrEditorTreeCreator().generateContainmentTreeForMessageComponent(treeItem,
								MrHelper.getMsgElementList(String.valueOf(typeText.getData("meType_id"))), true);
						for (Control control : rightComposite.getChildren()) {
							control.dispose();
						}
					}
				});
				
				Button cancelButton = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_PUSH_DEEP,"Cancel")).getButton();
				cancelButton.setBounds(350, 390, 60, 30);
				cancelButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseUp(MouseEvent e) {
						for (Control control : rightComposite.getChildren()) {
							control.dispose();
						}
					}
				});
				scrolledComposite.setMinSize(rightComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
		});

		deleteBusinessElementBtn = new SummaryRowTextComposite(leftCom, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES))).getButton();
		deleteBusinessElementBtn.setBounds(42, 35, 30, 30);
		deleteBusinessElementBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				MrEditorDialogCreator.deleteTreeItemForMC(elementsTree);
			}
		});

		elementsTree = new Tree(leftCom, SWT.BORDER | SWT.SINGLE);
		elementsTree.setBounds(5, 70, 485, 650);
		elementsTree.layout();

		if (msgElementList != null && msgElementList.size() > 0) {
			System.out.println("MessageComponent中选中的主键: " + messageComponentId);
			for (EcoreMessageElementVO ecoreMessageElementVO : msgElementList) {
				EcoreMessageElement ecoreMessageElement = ecoreMessageElementVO.getEcoreMessageElement();
				EcoreMessageComponent mc = MrHelper.getMessageComponentById(mcs, ecoreMessageElement.getTypeId());
				TreeItem treeItem = new TreeItem(elementsTree, SWT.NONE);
				treeItem.setData("id", ecoreMessageElement.getId());
				treeItem.setData("meNameText", ecoreMessageElement.getName());
				treeItem.setData("meDocText", ecoreMessageElement.getDefinition());
				treeItem.setData("meMinOccursText", ecoreMessageElement.getMinOccurs());
				treeItem.setData("meMaxOccursText", ecoreMessageElement.getMaxOccurs());
				treeItem.setData("meType_id", ecoreMessageElement.getTypeId());
				treeItem.setData("meType", ecoreMessageElement.getType());
				treeItem.setData("meTypeName", ecoreMessageElement.getTypeName());
				treeItem.setData("isDerivedCheckButton", Boolean.getBoolean(ecoreMessageElement.getIsDerived()));
				treeItem.setData("meXmlTagText", ecoreMessageElement.getXmlTag());
				treeItem.setData("isTechnical", "");
				treeItem.setData("meObjectIdentifierText", ecoreMessageElement.getObjectIdentifier());
				treeItem.setData("meStatusCombo", ecoreMessageElement.getRegistrationStatus());
				treeItem.setData("mcStatusCombo", mc.getRegistrationStatus());
				java.util.List<SynonymVO> synonymVOs = ecoreMessageElementVO.getSynonyms();
				java.util.List<String[]> synonymsTableItems = new ArrayList<String[]>();
				if (synonymVOs != null && synonymVOs.size() > 0) {
					for (SynonymVO synonymVO : synonymVOs) {
						String[] str = new String[2];
						str[0] = synonymVO.getContext();
						str[1] = synonymVO.getSynonym();
						synonymsTableItems.add(str);
					}
				}
				treeItem.setData("beSynonymsTableItems", synonymsTableItems);

				java.util.List<EcoreConstraint> ecoreConstraints = ecoreMessageElementVO.getEcoreConstraints();
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
				treeItem.setData("beConstraintsTableItems", constraintsTableItems);
				if (summaryStatusCombo.getText().equals("Added Registered") || summaryStatusCombo.getText() == null) {
					treeItem.setData("mcstatus", 1);
				}
				String min = ecoreMessageElement.getMinOccurs() == null ? "0" : ecoreMessageElement.getMinOccurs() + "";
				String max = ecoreMessageElement.getMaxOccurs() == null ? "*" : ecoreMessageElement.getMaxOccurs() + "";
				String nodeName = ecoreMessageElement.getName() + " [" + min + ", " + max + "]";
				if (ecoreMessageElement.getTypeName() != null) {
					nodeName = nodeName + " : " + ecoreMessageElement.getTypeName();
				}
				treeItem.setText(nodeName);
				if (summaryStatusCombo.getText() != null) {
					if (summaryStatusCombo.getText().equals("Registered")) {
						if ("1".equals(ecoreMessageElement.getType())) {
							dataTypeById = MrHelper.getDataTypeById(dts, ecoreMessageElement.getTypeId());
							treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
							for (EcoreCode ecoreCode : MrHelper.findCodesByCodeSetId(cs, dataTypeById.getId())) {
								TreeItem codeTreeItem = new TreeItem(treeItem, SWT.NONE);
								codeTreeItem.setText(ecoreCode.getName() + " [" + ecoreCode.getCodeName() + "]");
								codeTreeItem.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
							}
						} else if ("2".equals(ecoreMessageElement.getType())) {
							treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
						}
					} else {
						if ("1".equals(ecoreMessageElement.getType())) {
							treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
						} else if ("2".equals(ecoreMessageElement.getType())) {
							treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT1));
						} else {
							treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
						}
					}
				}
				java.util.List<EcoreMessageElement> mesf = mes.stream()
						.filter(me -> me.getMessageComponentId().equals(ecoreMessageElement.getTypeId()))
						.collect(Collectors.toList());
				// 用Message Component里的元素，构造Message Component树
				new MrEditorTreeCreator().generateContainmentTreeForMessageComponents(treeItem, mesf, true);
			}
			//默认打开
			TreeItem item = elementsTree.getItem(0);
			if(item != null) {
				elementsTree.setSelection(item);
				elementsTreeListener(item, rightComposite);
			}
		}
		// ------------ Content Expand Bar End ----------------------------------
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		elementsTree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem treeItem = (TreeItem) e.item;
				selectedTableItem = treeItem;
				selectedTableItem.setText(treeItem.getText());
				elementsTreeListener(treeItem, rightComposite);
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void elementsTreeListener(TreeItem treeItem, Composite rightComposite) {
		String codeName = String.valueOf(treeItem.getData("CodeName"));
		
		if (treeItem.getData("meTypeName") != null) {
			showMessageElementDetailBar(treeItem, rightComposite);
			meSynonymsTable.removeAll();
			java.util.List<String[]> synonymsTableItems = (java.util.List<String[]>) treeItem.getData("beSynonymsTableItems");
			if (synonymsTableItems != null && synonymsTableItems.size() > 0) {
				for (int i = 0; i < synonymsTableItems.size(); i++) {
					TableItem tableItem = new TableItem(meSynonymsTable, SWT.NONE);
					tableItem.setText(synonymsTableItems.get(i));
				}
			}
			meConstraintsTable.removeAll();
			java.util.List<String[]> constraintsTableItems = (java.util.List<String[]>) treeItem.getData("beConstraintsTableItems");
			if (constraintsTableItems != null && constraintsTableItems.size() > 0) {
				for (int i = 0; i < constraintsTableItems.size(); i++) {
					TableItem tableItem = new TableItem(meConstraintsTable, SWT.NONE);
					tableItem.setText(constraintsTableItems.get(i));
				}
			}
		} else if (!codeName.equalsIgnoreCase("null")) {
			showCodeDetailBar(treeItem, rightComposite);
			meConstraintsTable.removeAll();
			java.util.List<String[]> constraintsTableItems = (java.util.List<String[]>) treeItem.getData("beConstraintsTableItems");
			if (constraintsTableItems != null && constraintsTableItems.size() > 0) {
				for (int i = 0; i < constraintsTableItems.size(); i++) {
					TableItem tableItem = new TableItem(meConstraintsTable, SWT.NONE);
					tableItem.setText(constraintsTableItems.get(i));
				}
			}
		}else {
			showMessageElementDetailBar(treeItem, rightComposite);
			meSynonymsTable.removeAll();
			java.util.List<String[]> synonymsTableItems = (java.util.List<String[]>) treeItem.getData("beSynonymsTableItems");
			if (synonymsTableItems != null && synonymsTableItems.size() > 0) {
				for (int i = 0; i < synonymsTableItems.size(); i++) {
					TableItem tableItem = new TableItem(meSynonymsTable, SWT.NONE);
					tableItem.setText(synonymsTableItems.get(i));
				}
			}
			meConstraintsTable.removeAll();
			java.util.List<String[]> constraintsTableItems = (java.util.List<String[]>) treeItem
					.getData("beConstraintsTableItems");
			if (constraintsTableItems != null && constraintsTableItems.size() > 0) {
				for (int i = 0; i < constraintsTableItems.size(); i++) {
					TableItem tableItem = new TableItem(meConstraintsTable, SWT.NONE);
					tableItem.setText(constraintsTableItems.get(i));
				}
			}
		}
		if (treeItem.getData("mcstatus") != null) {
			if(btBtn != null) {
				btBtn.setEnabled(true);
			}
			meNameText.setEditable(true);
			meDocText.setEditable(true);
			meMinOccursText.setEditable(true);
			meMaxOccursText.setEditable(true);
			meXmlTagText.setEditable(true);
			isDerivedCheckButton.setEnabled(true);
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
		bt = new SummaryRowTextComposite(composite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_DEEP,"Change Trace")).getButton();
		bt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				try {
					MrEditorDialogCreator.createSelectBusinessTrace(bcTypeText, tableTree);
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});

		Composite cmp = new Composite(composite, SWT.NONE);
		cmp.setLayoutData(new GridData(500, 40));
		cmp.setLayout(new GridLayout(2, false));

		Label choice = new Label(cmp, SWT.NONE);
		choice.setBounds(10, 300, 160, 30);
		choice.setText("This component is Technical");
		choButton = new SummaryRowTextComposite(cmp, new ButtonPolicy(0)).getButton();
		choButton.setBounds(175, 305, 100, 30);
		EcoreMessageComponent mc_1 = MrHelper.getMessageComponentById(MrRepository.get().ecoreMessageComponents, this.messageComponentId);
		if (mc_1.getTechnical() != null) {
			String a = mc_1.getTechnical();
			String value = "false";
			if (a.equals(value)) {
				choButton.setSelection(false);
			} else {
				choButton.setSelection(true);
			}
		}
		choButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				// 初始化按钮状态
				if (!choButton.getSelection()) {
					choButton.setSelection(false);
					bt.setEnabled(true);
				} else {
					choButton.setSelection(true);
					// 触发事件
					bcTypeText.setText(" ");
					bcTypeText.setData("bcType_id", null);
					bt.setEnabled(false);
				}
			}
		});
		if (choButton != null && choButton.getSelection() == true) {
			bt.setEnabled(false);
		}

		GridData grid = new GridData(SWT.NONE);
		grid.widthHint = 210;
		Composite cmp_1 = new Composite(composite, SWT.NONE);
		cmp_1.setLayoutData(new GridData(1600, 40));
		cmp_1.setLayout(new GridLayout(4, false));

		Label lab = new Label(cmp_1, SWT.NONE);
		lab.setBounds(10, 620, 160, 30);
		lab.setText("This component is a view on business component:");

		Label lab1 = new Label(cmp_1, SWT.NONE);
		lab1.setBounds(180, 620, 460, 30);
		bcTypeText = new Text(cmp_1, SWT.BORDER);
		bcTypeText.setEditable(false);
		bcTypeText.setLayoutData(grid);
		bcTypeText.setForeground(new Color(Display.getCurrent(), 57, 0, 255));// rgb值57,0,255
		if (mc_1.getTraceName() != null) {
			// 初始化text数据
			bcTypeText.setText(mc_1.getTraceName());
			bcTypeText.setData("bcType_id", mc_1.getTrace());
			bcTypeText.setData("bcName", mc_1.getTraceName());
			bcTypeText.setData("meLever", 1);

		}

		bcTypeText.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {
				// 跳转实现
				if (bcTypeText != null) {
					CTabFolder cTabFolder = MrRepository.get().tabFolder;
					Tree firstTabTree = MrRepository.get().businessModelTabTree;
					TreeItem mcTreeItem = firstTabTree.getItem(1);
					for (TreeItem bcItem : mcTreeItem.getItems()) {
						EcoreTreeNode ecoreTreeNode = (EcoreTreeNode) bcItem.getData("EcoreTreeNode");
						if (bcTypeText.getData("meLever") != null) {
							String meComponentId = (String) bcTypeText.getData("bcType_id");
							if (ecoreTreeNode.getId().equals(meComponentId)) {
								TransferDataBean transferDataBean = new TransferDataBean();
								transferDataBean.setId(ecoreTreeNode.getId());
								transferDataBean.setName(ecoreTreeNode.getName());
								transferDataBean.setLevel(ecoreTreeNode.getLevel());
								transferDataBean.setType((String) bcItem.getData("type"));
								transferDataBean.setChildId(
										bcItem.getData("childId") == null ? "" : (String) bcItem.getData("childId"));
								transferDataBean.setImgPath(ImgUtil.BC);
								transferDataBean.setTreeListItem((MrTreeItem) bcItem);
								firstTabTree.setSelection(bcItem);
								cTabFolder.setSelection(1);
								EditorUtil.open(transferDataBean.getType(), transferDataBean);
								return;
							}
						}
					}
				}
			}
		});
		tableTree = new Tree(composite, SWT.BORDER | SWT.FULL_SELECTION);
		tableTree.setHeaderVisible(true);
		tableTree.setLinesVisible(true);
		GridData tableGridData = new GridData();
		tableGridData.widthHint = 1200;
		tableGridData.heightHint = 700;
		tableGridData.grabExcessHorizontalSpace = true;
		tableGridData.grabExcessVerticalSpace = true;
		tableGridData.verticalAlignment = SWT.FILL;

		tableTree.setLayoutData(tableGridData);
		TreeColumn msgElementColumn = new TreeColumn(tableTree, SWT.NONE);
		msgElementColumn.setWidth(350);
		msgElementColumn.setText("Message Element");
		TreeColumn isTechnicalColumn = new TreeColumn(tableTree, SWT.NONE);
		isTechnicalColumn.setWidth(150);
		isTechnicalColumn.setText("Is Technical");
		TreeColumn TracesToColumn = new TreeColumn(tableTree, SWT.NONE);
		TracesToColumn.setWidth(150);
		TracesToColumn.setText("Traces to");
		TreeColumn TracePathColumn = new TreeColumn(tableTree, SWT.NONE);
		TracePathColumn.setWidth(150);
		TracePathColumn.setText("Trace Path");
		TreeColumn TypeOfMsgElementTracesToColumn = new TreeColumn(tableTree, SWT.NONE);
		TypeOfMsgElementTracesToColumn.setWidth(150);
		TypeOfMsgElementTracesToColumn.setText("Type of Message Element traces to");
		if (this.msgElementList != null && this.msgElementList.size() > 0) {
			for (EcoreMessageElementVO msgElementVO : msgElementList) {
				item1 = new TreeItem(tableTree, SWT.NONE);
				EcoreMessageElement msgElement = msgElementVO.getEcoreMessageElement();
				String tracesTo = "";
				if("1".equals(msgElement.getTraceType()) && msgElement.getTrace() != null) {
					EcoreMessageElement eme = emeMap.get(msgElement.getTrace());
					if(eme != null) {
						tracesTo = eme.getName();
					}
				}
				// 初始化加载表格数据 第一项默认为false 没有任何数据时
				item1.setText(1, msgElement.getTechnical() == null ? "false" : msgElement.getTechnical());
				item1.setText(2, msgElement.getTracesTo() == null ? "" : tracesTo);
				item1.setText(3, msgElement.getTracePath() == null ? "" : msgElement.getTracePath());
				item1.setText(4, msgElement.getTypeOfTracesTo() == null ? "" : msgElement.getTypeOfTracesTo());
				item1.setData("trace", msgElement.getTechnical());
				if (msgElement.getTypeOfTracesTo() != null) {
					item1.setData("me_bcTrace", msgElement.getTypeOfTracesTo());
				}
				if ("2".equals(msgElement.getTraceType())) {
					item1.setImage(2, ImgUtil.createImg(ImgUtil.BC));
					item1.setData("Image", 2);
				} else if ("1".equals(msgElement.getTraceType())) {
					item1.setImage(2, ImgUtil.createImg(ImgUtil.BC_BE));
					item1.setData("Image", 1);
				} else {

				}
				String appendix = "";
				item1.setData("meLever", 1);
				String status = null;
				if (summaryStatusCombo.getText() != null) {
					status = summaryStatusCombo.getText();
				}
				if ("2".equals(msgElement.getType())) {// 1：datatype，2：消息组件
					EcoreMessageComponent mc = MrHelper.getMessageComponentById(mcs, msgElement.getTypeId());
					String min = msgElement.getMinOccurs() == null ? "0" : msgElement.getMinOccurs() + "";
					String max = msgElement.getMaxOccurs() == null ? "*" : msgElement.getMaxOccurs() + "";
					appendix = " [" + min + ", " + max + "]" + " : " + mc.getName();
					// 已关联，有数据时
					if (mc.getTechnical().equals("true")) {
						item1.setText(4, "--TECHNICAL--");
					} else if (mc.getTraceName() != null) {
						item1.setText(4, mc.getTraceName());
					}

					item1.setData("me_id", msgElement.getId());
					// 表格中TypeOfTracesTo取值逻辑,getAll存储时使用
					if (mc.getTechnical().equals("true")) {
						item1.setData("me_bcTrace", "--TECHNICAL--");
					} else if (mc.getTraceName() != null) {
						item1.setData("me_bcTrace", mc.getTraceName());
					}
					item1.setData("mcStatusCombo", mc.getRegistrationStatus());

					if (item1.getData("bcType_id") != null) {
						item1.setData("trace", item1.getData("bcType_id"));
					} else if (item1.getData("beType_id") != null) {
						item1.setData("trace", item1.getData("beType_id"));
					}
					// 取关联mc绑定的bc内容
					if (mc.getTrace() != null) {
						item1.setData("TypeBcTrace", mc.getTrace());
					}
					item1.setText(msgElement.getName() + appendix);
					if (status == null || status.equals("Provisionally Registered")
							|| status.equals("Added Registered")) {
						item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT1));
					} else {
						item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
					}
					java.util.List<EcoreMessageElement> mesf = MrHelper.findMessageElementByMessageComponentId(mes, mc.getId());
					new MrEditorTreeCreator().generateContainmentTreeForBusniessTrace(item1, mesf);
				} else {
					// datatype
					EcoreDataType dataType = MrHelper.getDataTypeById(dts, msgElement.getTypeId());
					appendix = " : " + dataType.getName();
					item1.setText(msgElement.getName() + appendix);
					if (item1.getData("bcType_id") != null) {
						item1.setData("trace", item1.getData("bcType_id"));
					} else if (item1.getData("beType_id") != null) {
						item1.setData("trace", item1.getData("beType_id"));
					}
					item1.setText(1, msgElement.getTechnical() == null ? "false" : msgElement.getTechnical());
					item1.setText(2, msgElement.getTracesTo() == null ? "" : msgElement.getTracesTo());
					item1.setText(3, msgElement.getTracePath() == null ? "" : msgElement.getTracePath());
					item1.setText(4, "");
					if ("1".equals(dataType.getType())) { // codeSet 已到最底层了
						if (status == null || status.equals("Provisionally Registered")
								|| status.equals("Added Registered")) {
							item1.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
						} else {
							item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
						}
						for (EcoreCode ecoreCode : MrHelper.findCodesByCodeSetId(cs, dataType.getId())) {
							TreeItem codeTreeItem = new TreeItem(item1, SWT.NONE);
							codeTreeItem.setText(ecoreCode.getName() + " [" + ecoreCode.getCodeName() + "]");
							codeTreeItem.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
							codeTreeItem.setData(ecoreCode.getId());
						}
					} else {
						if (status == null || status.equals("Provisionally Registered")
								|| status.equals("Added Registered")) {
							item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT1));
						} else {
							item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
						}
					}
					item1.setText(0, msgElement.getName() + appendix);
				}
			}
		}
		ScrolledComposite scrolledComposite = new ScrolledComposite(parentComposite, SWT.V_SCROLL | SWT.H_SCROLL);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		tableTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent event) {
				setDirty(true);
				if (event.button == 3) {
					Menu menu = new Menu(tableTree);
					tableTree.setMenu(menu);
					MenuItem item = new MenuItem(menu, SWT.PUSH);
					item.setText("Define Trace");
					item.addListener(SWT.Selection, new Listener() {
						@Override
						public void handleEvent(Event event) {
							TreeItem item1 = tableTree.getSelection()[0];
							Tree parent = item1.getParent();
							if (parent != null) {
								// 新建后再次触发事件
								if (allBeByComponentId == null) {
									initFilledData();
								}
								if (bcTypeText != null) {
									java.util.List<EcoreBusinessComponent> bcs = MrRepository.get().ecoreBusinessComponents;
									Map<String, String> bcMap = bcs.stream().collect(Collectors.toMap(EcoreBusinessComponent::getId, EcoreBusinessComponent::getName));
									MrEditorDialogCreator.createBusinessComponetTrace(bcTypeText, item1, allBeByComponentId, allSubBusiness, bcMap);
								}
							}
						}
					});
					MenuItem item2 = new MenuItem(menu, SWT.PUSH);
					item2.setText("Remove Trace");
					item2.addListener(SWT.Selection, new Listener() {
						@Override
						public void handleEvent(Event event) {
							TreeItem item3 = tableTree.getSelection()[0];
							Tree parent = item3.getParent();
							if (parent != null) {
								item3.setText(2, " ");
								item3.setImage(2, null);
								item3.setText(3, " ");
							}
						}
					});
					MenuItem item4 = new MenuItem(menu, SWT.PUSH);
					item4.setText("Set/Unset Technical");
					item4.addListener(SWT.Selection, new Listener() {
						@Override
						public void handleEvent(Event event) {
							TreeItem item5 = tableTree.getSelection()[0];
							Tree parent = item5.getParent();
							if (parent != null) {
								String name = item5.getText(1);
								if (name.equals("true")) {
									item5.setText(1, "false");
								} else if (name.equals("false")) {
									item5.setText(1, "true");
								}
							}
						}
					});
					// 设置trace页面可编辑状态
					TreeItem items = tableTree.getSelection()[0];
					if (items.getData("meLever") != null && bcTypeText.getData("bcName") != null
							&& !bcTypeText.getData("bcName").equals("")) {
						item.setEnabled(true);
						item2.setEnabled(true);
						item4.setEnabled(true);
					} else {
						item.setEnabled(false);
						item2.setEnabled(false);
						item4.setEnabled(false);
					}
					// 修改可编辑状态
					if (msgComponent != null
							&& msgComponent.getRegistrationStatus() != null) {
						String status = msgComponent.getRegistrationStatus();
						if (status.equals("Registered") || status.equals("Provisionally Registered")
								|| status.equals("Obsolete")) {
							menu.setEnabled(false);
							item.setEnabled(false);
							item2.setEnabled(false);
							item4.setEnabled(false);
						}
					}
				}
			}
		});

		Delbutton = new SummaryRowTextComposite(cmp_1, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES))).getButton();
		Delbutton.setBounds(700, 620, 50, 30);
		Delbutton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				Shell parent = new Shell(Display.getCurrent());
				MessageBox messageBox = new MessageBox(parent, SWT.YES | SWT.NO);
				messageBox.setMessage(
						"All trace information on the message elements will be erased." + "Do you want to proceed？");
				if (messageBox.open() == SWT.YES) {
					// 清空text数据
					bcTypeText.setText(" ");
					bcTypeText.setData("bcType_id", null);
					bcTypeText.setData("bcName", null);
//					TreeItem ti=item1;
					// 清空表格中2,3位置数据
					TreeItem[] ti = tableTree.getItems();
					for (int i = 0; i < ti.length; i++) {
						TreeItem tTtem = ti[i];
						tTtem.setText(2, " ");
						tTtem.setImage(2, null);
						tTtem.setText(3, " ");
					}
//					
				}
				parent.dispose();
			}

		});
	}

	void createImpactPage() {
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
		groupGridData.heightHint = 200;

		Group messageSetsGroup = new Group(groupListComposite, SWT.NONE);
		messageSetsGroup.setText(I18nApi.get("editor.title.ms"));
		messageSetsGroup.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		messageSetsGroup.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		messageSetsGroup.setLayoutData(groupGridData);
		messageSetsGroup.setLayout(new FillLayout(SWT.VERTICAL));
		messageSetTable = new Table(messageSetsGroup, SWT.BORDER | SWT.V_SCROLL);
		messageSetTable.setLinesVisible(true);
		TableCursor messageSetTableCursor = new TableCursor(messageSetTable, SWT.NONE);
		// 构造Message Set表格
		if(mds != null && mds.size() > 0) {
			java.util.List<String> setIds = MrImplManager.get().getEcoreMessageSetDefinitionRLImpl().findSetIdByMsgDefinitions(mds);
			java.util.List<EcoreMessageSet> emss = MrRepository.get().ecoreMessageSets;
			java.util.List<EcoreMessageSet> mss = emss.stream().filter(ems -> setIds.contains(ems.getId())).collect(Collectors.toList());
			MrEditorTableCreator.generateMessageSetTableOfImpactPage(messageSetTable, mss);
		}

		Group messageDefinitionsGroup = new Group(groupListComposite, SWT.NONE);
		messageDefinitionsGroup.setText(I18nApi.get("editor.title.md"));
		messageDefinitionsGroup.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		messageDefinitionsGroup.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		messageDefinitionsGroup.setLayoutData(groupGridData);
		messageDefinitionsGroup.setLayout(new FillLayout(SWT.VERTICAL));
		messageDefinitionTable = new Table(messageDefinitionsGroup, SWT.BORDER | SWT.V_SCROLL);
		messageDefinitionTable.setLinesVisible(true);
		TableCursor messageDefinitionTableCursor = new TableCursor(messageDefinitionTable, SWT.NONE);
		// 构造Message Definition表格
		MrEditorTableCreator.generateMessageDefinationTableOfImpactPage(messageDefinitionTable, mds);

		Group messageComponentGroup = new Group(groupListComposite, SWT.NONE);
		messageComponentGroup.setText(I18nApi.get("editor.title.mc"));
		messageComponentGroup.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		messageComponentGroup.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		messageComponentGroup.setLayoutData(groupGridData);
		messageComponentGroup.setLayout(new FillLayout(SWT.VERTICAL));
		messageComponentTable = new Table(messageComponentGroup, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
		messageComponentTable.setLinesVisible(true);
		TableCursor msgComponentTableCursor = new TableCursor(messageComponentTable, SWT.NONE);
		// 构造Message Component表格
		java.util.List<EcoreMessageElement> emes = MrRepository.get().ecoreMessageElements;
		java.util.List<String> meMcIds = emes.stream().filter(eme -> eme.getTypeId().equals(messageComponentId)).map(eme -> eme.getMessageComponentId()).collect(Collectors.toList());
		java.util.List<EcoreMessageComponent> emcs = MrRepository.get().ecoreMessageComponents;
		java.util.List<EcoreMessageComponent>msgComponentList = emcs.stream().filter(emc -> meMcIds.contains(emc.getId())).collect(Collectors.toList());
		MrEditorTableCreator.generateMessageComponentTableOfImpactPage(messageComponentTable, msgComponentList);

		Composite treeComposite = new Composite(composite, SWT.NONE);
		FillLayout treeCompositetFillLayout = new FillLayout(SWT.VERTICAL);
		treeComposite.setLayout(treeCompositetFillLayout);
		GridData treeCompositeGridData = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		treeCompositeGridData.widthHint = 600;
		treeCompositeGridData.heightHint = 700;
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
				treeRoot.setText(msgSetName);
				treeRoot.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
				treeRoot.removeAll();
				String messageSetId = (String) tableItem.getData(String.valueOf(messageSetTable.getSelectionIndex()));
				MrEditorTreeCreator mrEditorTreeCreator = new MrEditorTreeCreator();
				mrEditorTreeCreator.generateContainmentTreeForMessageSetofImpactPage(treeRoot,
						MrImplManager.get().getEcoreMessageDefinitionImpl().findByMsgSetId(messageSetId));
				// 加载Constraint节点
				java.util.List<EcoreConstraint> ecs = MrHelper.findConstraintsByObjId(css, messageSetId);
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
				String msgDefinitionId = (String) tableItem
						.getData(String.valueOf(messageDefinitionTable.getSelectionIndex()));
				treeRoot.setText(msgDefinitionName);
				treeRoot.setImage(ImgUtil.createImg(ImgUtil.MD_SUB1));
				treeRoot.removeAll();
				// 递归生成contrainment tree on Impact page
				MrEditorTreeCreator mrEditorTreeCreator = new MrEditorTreeCreator();
				mrEditorTreeCreator.generateContainmentTreeForMessageDefinition(treeRoot,
						MrHelper.findMsgBuildingBlocksByMsgId(mbbs, msgDefinitionId));
				// 加载Constraint节点
				java.util.List<EcoreConstraint> ecs = MrHelper.findConstraintsByObjId(css, msgDefinitionId);
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
				String msgComponentId = (String) tableItem
						.getData(String.valueOf(messageComponentTable.getSelectionIndex()));
				treeRoot.setText(msgComponentName);
				String status = summaryStatusCombo.getText();
				if (status == null || status.equals("Provisionally Registered") || status.equals("Added Registered")) {
					treeRoot.setImage(ImgUtil.createImg(ImgUtil.MC_SUB3_COMPONENT));
				} else {
					treeRoot.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_COMPONENT));
				}
				treeRoot.removeAll();
				// 递归生成contrainment tree on Impact page
				MrEditorTreeCreator mrEditorTreeCreator = new MrEditorTreeCreator();
				mrEditorTreeCreator.generateContainmentTreeForMessageComponent(treeRoot,
						MrHelper.getMsgElementList(msgComponentId), true);
				// 加载Constraint节点
				java.util.List<EcoreConstraint> ecs = MrHelper.findConstraintsByObjId(css, msgComponentId);
				mrEditorTreeCreator.addConstraintsNode(treeRoot, ecs, null, null);
				treeRoot.setExpanded(true);
			}
		});
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	void createVersionsAndSubsets() {
		
		Map<String, java.util.List<EcoreNextVersions>> envMapById = MrRepository.get().ecoreNextVersionsMapById;
		Map<String, EcoreMessageComponent> emcMapById = MrRepository.get().ecoreMessageComponentMapById;
		
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
		GridData lc_gd = new GridData(SWT.TOP, SWT.TOP, false, false, 1, 1);
		lc_gd.widthHint = 550; // composite.getBounds().width / 3;
		leftComposite.setLayoutData(lc_gd);

		GridData rc_gd = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		rc_gd.widthHint = 500; // composite.getBounds().width / 3;
		Composite rightComposite = new Composite(composite, SWT.NONE);
		rightComposite.setLayout(new FillLayout(SWT.VERTICAL));
		rightComposite.setLayoutData(rc_gd);

		// ------------ Next Version Bar Begin ----------------------------------
		ExpandBar nextVersionBar = new SummaryExpandComposite(leftComposite, 0).getExpandBar();
		
		Composite nextVersionBarComposite = new Composite(nextVersionBar, SWT.NONE);
		nextVersionBarComposite.setLayout(new FillLayout(SWT.VERTICAL));

		nextVersionListWidget = new List(nextVersionBarComposite, SWT.BORDER);
		nextVersionListWidget.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		if (!StringUtils.isEmpty(messageComponentId)) {
			java.util.List<EcoreNextVersions> ecoreNextVersionses = envMapById.get(messageComponentId);
			if(ecoreNextVersionses != null && ecoreNextVersionses.size() > 0) {
				ecoreNextVersionses.forEach(env ->{
					EcoreMessageComponent emcNextVersions = emcMapById.get(env.getNextVersionId());
					if(emcNextVersions == null) {
						nextVersionListWidget.removeAll();
						nextVersionListWidget.add(I18nApi.get("editor.title.nv.tips"));
					}else {
						nextVersionListWidget.add(emcNextVersions.getName() == null ? "" : emcNextVersions.getName());
						nextVersionListWidget.setData("nextVersionId", emcNextVersions.getId());
					}
				});
			}else {
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
		if (!StringUtils.isEmpty(messageComponentId)) {
			setPreviousVersion(emcMapById);
		}
		new SummaryExpandComposite(previousVersionBar, previousVersionBarComposite, I18nApi.get("editor.title.pv"), 350);
		// ------------ Previous Version Bar End ------------------------
	}

	/**
	 * 设置历史版本
	 * @param emdMapById
	 */
	public void setPreviousVersion(Map<String, EcoreMessageComponent> emcMapById) {
		Map<String, EcoreNextVersions> envMapByNextVersionId = MrRepository.get().ecoreNextVersionsMapByNextVersionId;
		previousVersionListWidget.removeAll();
		java.util.List<EcoreNextVersions> list = new ArrayList<>();
		java.util.List<EcoreNextVersions> EcoreNextVersionses = MessageHelper.getPreviousVersions(envMapByNextVersionId, messageComponentId, list);
		if(list != null && list.size() > 0) {
			for(EcoreNextVersions ecoreNextVersions : EcoreNextVersionses) {
				EcoreMessageComponent emcNextVersions = emcMapById.get(ecoreNextVersions.getId());
				previousVersionListWidget.add(emcNextVersions.getName() == null ? "" : emcNextVersions.getName());
				previousVersionListWidget.setData("nextVersionId", emcNextVersions.getId());
			}
		}else {
			previousVersionListWidget.add(I18nApi.get("editor.title.pv.tips"));
		}
	}
	
	private void generateName() {
		try {
			String name = summaryNameText.getText();
			if (name.equals("")) {
				MessageDialog.openWarning(Display.getDefault().getActiveShell(), "提示", "请先输入命名Name!");
			} else {
				int count = MrImplManager.get().getEcoreMessageComponentImpl().countByName(name);
				//名称存在
				if(count > 0) {
					String version = "";
					String pat = "\\d+";
					Pattern p = Pattern.compile(pat);
					String[] split = p.split(name);
					String previousVersion = MessageHelper.getVersion(name, p, split);
					if("".equals(previousVersion)) {
						throw new RuntimeException("组件名称不规范,示例Test1,1是版本号！");
					}else {
						if(StringUtils.isEmpty(previousVersion)) {
							version = "1";
						}else {
							int i = Integer.parseInt(previousVersion);
							i++;
							version = "" + i;
						}
					}
					summaryNameText.setText(split[0] + version);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取所有数据
	 */
	@SuppressWarnings("unchecked")
	public EcoreMessageComponentVO getAllData() {
		String name = null;
		EcoreMessageComponentVO ecoreMessageComponentVO = new EcoreMessageComponentVO();
		EcoreMessageComponent ecoreMessageComponent = new EcoreMessageComponent();
		ecoreMessageComponent.setId(messageComponentId);
		ecoreMessageComponent.setName(this.summaryNameText.getText());
		ecoreMessageComponent.setDefinition(this.summaryDocText.getText());

		// 组件类型，1：MessageComponent，2：ChoiceComponent
		ecoreMessageComponent.setComponentType(this.choiceButton.getSelection() == true ? "2" : "1");
		ecoreMessageComponent.setObjectIdentifier(this.summaryobjectIdentifierText.getText());
		ecoreMessageComponent.setRegistrationStatus(this.summaryStatusCombo.getText());
		if (this.bcTypeText != null) {
			ecoreMessageComponent.setTrace(this.bcTypeText.getData("bcType_id") == null ? ""
					: this.bcTypeText.getData("bcType_id").toString());
			ecoreMessageComponent.setTraceName(
					this.bcTypeText.getData("bcName") == null ? "" : this.bcTypeText.getData("bcName").toString());
		}
		if (choButton.getSelection() == true) {
			ecoreMessageComponent.setTechnical("true");
		} else {
			ecoreMessageComponent.setTechnical("false");
		}
		Date contentRemovalDate;
		try {
			contentRemovalDate = (summaryRemovalDate.getText() == null || summaryRemovalDate.getText() == "") ? null
					: sdf.parse(summaryRemovalDate.getText());
		} catch (ParseException e) {
			contentRemovalDate = null;
		}
		ecoreMessageComponent.setRemovalDate(contentRemovalDate);
		ecoreMessageComponent.setIsfromiso20022(emcMap.get(messageComponentId) == null ? null : emcMap.get(messageComponentId).getIsfromiso20022());
		ecoreMessageComponentVO.setEcoreMessageComponent(ecoreMessageComponent);
		// 获取examples信息
		java.util.List<EcoreExample> ecoreExampleList = new ArrayList<EcoreExample>();
		String[] exampleItems = this.examplesList.getItems();
		Map<String, String> map = (Map<String, String>) examplesList.getData("map");
		if (exampleItems != null && exampleItems.length > 0) {
			for (String str : exampleItems) {
				EcoreExample bcExample = new EcoreExample();
				if (map != null) {
					bcExample.setId(map.get(str) == null ? UUID.randomUUID().toString() : map.get(str));
				} else {
					bcExample.setId(UUID.randomUUID().toString());
				}
				bcExample.setExample(str);
				bcExample.setObjId(ecoreMessageComponent.getId());
				bcExample.setObjType(ObjTypeEnum.BusinessComponent.getType());
				ecoreExampleList.add(bcExample);
			}
		}
		ecoreMessageComponentVO.setEcoreExamples(ecoreExampleList);

		// 获取constraints
		java.util.List<EcoreConstraint> constraints = new ArrayList<EcoreConstraint>();
		TableItem[] constraintItems = summaryConstraintsTable.getItems();
		if (constraintItems != null && constraintItems.length > 0) {
			for (TableItem tableItem : constraintItems) {
				EcoreConstraint bcConstraint = new EcoreConstraint();
				bcConstraint.setId(tableItem.getData("id") == null ? UUID.randomUUID().toString()
						: tableItem.getData("id").toString());
				bcConstraint.setObj_id(ecoreMessageComponent.getId());
				bcConstraint.setObj_type(ObjTypeEnum.BusinessComponent.getType());
				bcConstraint.setName(tableItem.getText(0));
				bcConstraint.setDefinition(tableItem.getText(1));
				bcConstraint.setExpressionlanguage(tableItem.getText(2));
				bcConstraint.setExpression(tableItem.getText(3));
				constraints.add(bcConstraint);
			}
		}
		ecoreMessageComponentVO.setEcoreConstraints(constraints);

		java.util.List<EcoreMessageElementVO> ecoreMessageElementVOs = new ArrayList<EcoreMessageElementVO>();

		TreeItem[] messageElementsTreeItem = elementsTree.getItems();
		for (int i = 0; i < messageElementsTreeItem.length; i++) {
			TreeItem item = messageElementsTreeItem[i];
			EcoreMessageElementVO ecoreMessageElementVO = new EcoreMessageElementVO();

			EcoreMessageElement ecoreMessageElement = new EcoreMessageElement();
			ecoreMessageElement
					.setId(item.getData("id") == null ? UUID.randomUUID() + "" : item.getData("id") + "");
			ecoreMessageElement.setMessageComponentId(messageComponentId);
			ecoreMessageElement
					.setName(item.getData("meNameText") == null ? null : item.getData("meNameText") + "");
			ecoreMessageElement
					.setDefinition(item.getData("meDocText") == null ? null : item.getData("meDocText") + "");
			ecoreMessageElement.setMinOccurs(NumberUtil
					.getInt(item.getData("meMinOccursText") == null ? "" : item.getData("meMinOccursText") + ""));
			ecoreMessageElement.setMaxOccurs(NumberUtil
					.getInt(item.getData("meMaxOccursText") == null ? "" : item.getData("meMaxOccursText") + ""));
			ecoreMessageElement.setIsDerived("" + (item.getData("isDerivedCheckButton") == null ? false
					: (boolean) item.getData("isDerivedCheckButton")));
			ecoreMessageElement
					.setXmlTag(item.getData("meXmlTagText") == null ? "" : item.getData("meXmlTagText") + "");
			// 1-component 2-dataType
			ecoreMessageElement.setType(item.getData("meType") == null ? "" : item.getData("meType") + "");
			ecoreMessageElement
					.setTypeId(item.getData("meType_id") == null ? "" : item.getData("meType_id") + "");
			ecoreMessageElement.setObjectIdentifier(item.getData("meObjectIdentifierText") == null ? ""
					: item.getData("meObjectIdentifierText") + "");
			ecoreMessageElement.setRegistrationStatus(item.getData("meStatusCombo") == null ? "Added Registered"
					: item.getData("meStatusCombo") + "");
			if (item.getData("meNameText") != null) {
				name = item.getData("meNameText") + "";
			}
			TreeItem[] messageElementsTableTreeItem = this.tableTree.getItems();

			for (int j = 0; j < messageElementsTableTreeItem.length; j++) {
				TreeItem item1 = messageElementsTableTreeItem[j];
				if (item.getData("id") != null && item1.getData("me_id") != null
						&& item.getData("id").toString().equals(item1.getData("me_id").toString())) {
					ecoreMessageElement
							.setTrace(item1.getData("trace") == null ? "" : item1.getData("trace") + "");
					ecoreMessageElement.setTechnical(item1.getText(1) == null ? "" : item1.getText(1) + "");
					ecoreMessageElement.setTracesTo(item1.getText(2) == null ? "" : item1.getText(2) + "");
					ecoreMessageElement.setTracePath(item1.getText(3) == null ? "" : item1.getText(3) + "");
					ecoreMessageElement.setTypeOfTracesTo(
							item1.getData("me_bcTrace") == null ? "" : item1.getData("me_bcTrace") + "");
					if (item1.getData("Image") != null) {
						String image = item1.getData("Image") + "";
						if (image != null && "2".equals(image)) {
							ecoreMessageElement.setTraceType("2");
						} else if (image != null && "1".equals(image)) {
							ecoreMessageElement.setTraceType("1");
						} else {
							ecoreMessageElement.setTraceType("");
						}
					}
				}
			}
			ecoreMessageElementVO.setEcoreMessageElement(ecoreMessageElement);
			java.util.List<String[]> constraintsTableItems = (java.util.List<String[]>) item
					.getData("beConstraintsTableItems");
			java.util.List<EcoreConstraint> beEcoreConstraintList = new ArrayList<EcoreConstraint>();
			if (constraintsTableItems != null && constraintsTableItems.size() > 0) {
				for (int j = 0; j < constraintsTableItems.size(); j++) {
					EcoreConstraint beConstraint = new EcoreConstraint();
					beConstraint.setId(item.getData("id") == null ? UUID.randomUUID() + "" : beConstraint.getId());
					beConstraint.setObj_id(ecoreMessageElement.getId());
					beConstraint.setObj_type(ObjTypeEnum.MessageElement.getType());
					beConstraint.setName(constraintsTableItems.get(j)[0]);
					beConstraint.setDefinition(constraintsTableItems.get(j)[1]);
					beConstraint.setExpressionlanguage(constraintsTableItems.get(j)[2]);
					beConstraint.setExpression(constraintsTableItems.get(j)[3]);
					beEcoreConstraintList.add(beConstraint);
				}
			}
			ecoreMessageElementVO.setEcoreConstraints(beEcoreConstraintList);

			// -----------------
			java.util.List<String[]> synonymsTableItems = (java.util.List<String[]>) item
					.getData("beSynonymsTableItems");
			java.util.List<SynonymVO> synonymVOList = new ArrayList<SynonymVO>();
			if (synonymsTableItems != null && synonymsTableItems.size() > 0) {
				for (int j = 0; j < synonymsTableItems.size(); j++) {
					SynonymVO synonymVO = new SynonymVO();
					synonymVO.setObjId(ecoreMessageElement.getId());
					synonymVO.setContext(synonymsTableItems.get(j)[0]);
					synonymVO.setSynonym(synonymsTableItems.get(j)[1]);
					synonymVOList.add(synonymVO);
				}
			}
			ecoreMessageElementVO.setSynonyms(synonymVOList);
			ecoreMessageElementVOs.add(ecoreMessageElementVO);
		}
		ecoreMessageComponentVO.setEcoreMessageElementVOs(ecoreMessageElementVOs);
		// me数据发生变化时 重构BusinessTrace表格
		if (name != null) {
			changeBusinessTrace(ecoreMessageComponentVO);
		}
		return ecoreMessageComponentVO;
	}

	private void changeBusinessTrace(EcoreMessageComponentVO ecoreMessageComponentVO2) {
		if (this.msgElementList != null) {
			this.msgElementList.clear();
		}
		msgElementList = ecoreMessageComponentVO2.getEcoreMessageElementVOs();
		items = tableTree.getItems();
		for (TreeItem treeItem : items) {
			treeItem.dispose();
		}
		if (msgElementList != null && msgElementList.size() > 0) {
			for (EcoreMessageElementVO msgElementVO : msgElementList) {
				item1 = new TreeItem(tableTree, SWT.NONE);
				EcoreMessageElement msgElement = msgElementVO.getEcoreMessageElement();
				// 初始化加载表格数据 第一项默认为false 没有任何数据时
				item1.setText(1, msgElement.getTechnical() == null ? "false" : msgElement.getTechnical());
				item1.setText(2, msgElement.getTracesTo() == null ? "" : msgElement.getTracesTo());
				item1.setText(3, msgElement.getTracePath() == null ? "" : msgElement.getTracePath());
				item1.setText(4, msgElement.getTypeOfTracesTo() == null ? "" : msgElement.getTypeOfTracesTo());
				item1.setData("trace", msgElement.getTechnical());
				if (msgElement.getTypeOfTracesTo() != null) {
					item1.setData("me_bcTrace", msgElement.getTypeOfTracesTo());
				}
				if ("2".equals(msgElement.getTraceType())) {
					item1.setImage(2, ImgUtil.createImg(ImgUtil.BC));
					item1.setData("Image", 2);
				} else if ("1".equals(msgElement.getTraceType())) {
					item1.setImage(2, ImgUtil.createImg(ImgUtil.BC_BE));
					item1.setData("Image", 1);
				} else {

				}
				String appendix = "";
				item1.setData("meLever", 1);
				String status = null;
				if (summaryStatusCombo.getText() != null) {
					status = summaryStatusCombo.getText();
				}
				if ("2".equals(msgElement.getType())) {// 1：datatype，2：消息组件
					EcoreMessageComponent mc = MrHelper.getMessageComponentById(mcs, msgElement.getTypeId());
					String min = msgElement.getMinOccurs() == null ? "0" : msgElement.getMinOccurs() + "";
					String max = msgElement.getMaxOccurs() == null ? "*" : msgElement.getMaxOccurs() + "";
					appendix = " [" + min + ", " + max + "]" + " : " + mc.getName();
					// 已关联，有数据时
					if (mc.getTechnical().equals("true")) {
						item1.setText(4, "--TECHNICAL--");
					} else if (mc.getTraceName() != null) {
						item1.setText(4, mc.getTraceName());
					}
					item1.setData("me_id", msgElement.getId());
					// 表格中TypeOfTracesTo取值逻辑,getAll存储时使用
					if (mc.getTechnical().equals("true")) {
						item1.setData("me_bcTrace", "--TECHNICAL--");
					} else if (mc.getTraceName() != null) {
						item1.setData("me_bcTrace", mc.getTraceName());
					}
					item1.setData("mcStatusCombo", mc.getRegistrationStatus());
					if (item1.getData("bcType_id") != null) {
						item1.setData("trace", item1.getData("bcType_id"));
					} else if (item1.getData("beType_id") != null) {
						item1.setData("trace", item1.getData("beType_id"));
					}
					// 取关联mc绑定的bc内容
					if (mc.getTrace() != null) {
						item1.setData("TypeBcTrace", mc.getTrace());
					}
					item1.setText(msgElement.getName() + appendix);
					if (status == null || status.equals("Provisionally Registered")
							|| status.equals("Added Registered")) {
						item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT1));
					} else {
						item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
					}
					java.util.List<EcoreMessageElement> mesf = mes.stream()
							.filter(me -> me.getMessageComponentId().equals(mc.getId())).collect(Collectors.toList());
					new MrEditorTreeCreator().generateContainmentTreeForBusniessTrace(item1, mesf);
				} else {
					// datatype
					EcoreDataType dataType = MrHelper.getDataTypeById(dts, msgElement.getTypeId());
					appendix = " : " + dataType.getName();
					item1.setText(msgElement.getName() + appendix);
					if (item1.getData("bcType_id") != null) {
						item1.setData("trace", item1.getData("bcType_id"));
					} else if (item1.getData("beType_id") != null) {
						item1.setData("trace", item1.getData("beType_id"));
					}
					item1.setText(1, msgElement.getTechnical() == null ? "false" : msgElement.getTechnical());
					item1.setText(2, msgElement.getTracesTo() == null ? "" : msgElement.getTracesTo());
					item1.setText(3, msgElement.getTracePath() == null ? "" : msgElement.getTracePath());
					item1.setText(4, "");
					if ("1".equals(dataType.getType())) { // codeSet 已到最底层了
						if (status == null || status.equals("Provisionally Registered")
								|| status.equals("Added Registered")) {
							item1.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
						} else {
							item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
						}
					} else {
						if (status == null || status.equals("Provisionally Registered")
								|| status.equals("Added Registered")) {
							item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT1));
						} else {
							item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
						}
					}
					item1.setText(0, msgElement.getName() + appendix);
				}
			}
		}
	}

	private void showMessageElementDetailBar(TreeItem treeItem, Composite rightComposite) {
		for (Control control : rightComposite.getChildren()) {
			control.dispose();
		}
		// ------------ Message Element Details Begin ------------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 5, I18nApi.get("editor.title.med")));
		
		String meName = treeItem.getData("meNameText") == null ? "" : treeItem.getData("meNameText") + "";
		meNameText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 45, "Name", false, meName)).getText();
		meNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTableItem.isDisposed()) {
					// 左边的树的名称展示
					String showText = "";
					String min = StringUtils.isEmpty(meMinOccursText.getText()) ? "0" : meMinOccursText.getText();
					String max = StringUtils.isEmpty(meMaxOccursText.getText()) ? "*" : meMaxOccursText.getText();
					if (meTypeText.getText().isEmpty()) {
						showText = meNameText.getText() + " [" + min + ", " + max + "]";
					} else {
						showText = meNameText.getText() + " [" + min + ", " + max + "] : " + meTypeText.getText();
					}
					selectedTableItem.setText(showText);

					selectedTableItem.setData("meNameText", meNameText.getText());
					setDirty(true);
				}
			}
		});

		String meDoc = treeItem.getData("meDocText") == null ? "" : treeItem.getData("meDocText") + "";
		meDocText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SCROLL_TYPE, 73, "Documentation", false, meDoc)).getText();
		meDocText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTableItem.isDisposed()) {
					selectedTableItem.setData("meDocText", meDocText.getText());
					setDirty(true);
				}
			}
		});
		
		String meMinOccurs = treeItem.getData("meMinOccursText") == null ? "" : treeItem.getData("meMinOccursText") + "";
		meMinOccursText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SHORT_TYPE, 228, "Min Occurs", false, meMinOccurs)).getText();
		meMinOccursText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTableItem.isDisposed()) {
					String showText = "";
					String min = StringUtils.isEmpty(meMinOccursText.getText()) ? "0" : meMinOccursText.getText();
					String max = StringUtils.isEmpty(meMaxOccursText.getText()) ? "*" : meMaxOccursText.getText();
					if (meTypeText.getText().isEmpty()) {
						showText = meNameText.getText() + " [" + min + ", " + max + "]";
					} else {
						showText = meNameText.getText() + " [" + min + ", " + max + "] : " + meTypeText.getText();
					}
					selectedTableItem.setText(showText);
					selectedTableItem.setData("meMinOccursText", meMinOccursText.getText());
					setDirty(true);
				}
			}
		});
		
		String meMaxOccurs = treeItem.getData("meMaxOccursText") == null ? "" : treeItem.getData("meMaxOccursText") + "";
		meMaxOccursText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SHORT_TYPE, 256, "Max Occurs", false, meMaxOccurs)).getText();
		meMaxOccursText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTableItem.isDisposed()) {
					String showText = "";
					String min = StringUtils.isEmpty(meMinOccursText.getText()) ? "0" : meMinOccursText.getText();
					String max = StringUtils.isEmpty(meMaxOccursText.getText()) ? "*" : meMaxOccursText.getText();
					if (meTypeText.getText().isEmpty()) {
						showText = meNameText.getText() + " [" + min + ", " + max + "]";
					} else {
						showText = meNameText.getText() + " [" + min + ", " + max + "] : " + meTypeText.getText();
					}
					selectedTableItem.setText(showText);
					selectedTableItem.setData("meMaxOccursText", meMaxOccursText.getText());
					setDirty(true);
				}
			}
		});

		String beType = treeItem.getData("meTypeName") == null ? "" : treeItem.getData("meTypeName") + "";
		meTypeText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_BUTTON_SELECT_TYPE, 284, "Type", false, beType)).getText();
		meTypeText.setData("meType_id", treeItem.getData("meType_id"));
		meTypeText.setData("meType", treeItem.getData("meType"));
		meTypeText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (selectedTableItem != null && !selectedTableItem.isDisposed()) {
					String showText = "";
					String min = StringUtils.isEmpty(meMinOccursText.getText()) ? "0" : meMinOccursText.getText();
					String max = StringUtils.isEmpty(meMaxOccursText.getText()) ? "*" : meMaxOccursText.getText();
					if (meTypeText.getText().isEmpty()) {
						showText = meNameText.getText() + " [" + min + ", " + max + "]";
					} else {
						showText = meNameText.getText() + " [" + min + ", " + max + "] : " + meTypeText.getText();
					}
					selectedTableItem.setText(showText);
					if (meTypeText.getData("meType_id") != null) {
						selectedTableItem.setData("meType_id", meTypeText.getData("meType_id"));
						selectedTableItem.setData("meType", meTypeText.getData("meType"));
					}
					setDirty(true);
				}
			}
		});
		btBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_DEEP,"choose", false)).getButton();
		btBtn.setBounds(390, 284, 50, 25);
		btBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					setDirty(true);
					MrEditorDialogCreator.createSelectMsgElementType(meTypeText, selectedTableItem);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		Button btSearchBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_DEEP,ImgUtil.createImg(ImgUtil.SEARCH_BTN))).getButton();
		btSearchBtn.setBounds(445, 284, 25, 25);
		btSearchBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (elementsTree.getSelectionCount() == 1) {
					TreeItem treeItem = elementsTree.getSelection()[0];
					String dataType = (String) treeItem.getData("meType");
					CTabFolder cTabFolder = MrRepository.get().tabFolder;
					// 跳转到datatype页
					if (dataType.equals("1")) {
						String dataTypeId = (String) treeItem.getData("meType_id");
						Tree firstTabTree = MrRepository.get().businessModelTabTree;
						TreeItem dataTypeTreeItem = firstTabTree.getItem(0);
						TreeItem[] allTypeItemArray = dataTypeTreeItem.getItems();
						for (TreeItem oneTypeTreeItem : allTypeItemArray) {
							for (TreeItem typeTreeItem : oneTypeTreeItem.getItems()) {
								EcoreTreeNode ecoreTreeNode = (EcoreTreeNode) typeTreeItem.getData("EcoreTreeNode");
								if (ecoreTreeNode.getId().equals(dataTypeId)) {
									TransferDataBean transferDataBean = new TransferDataBean();
									transferDataBean.setId(ecoreTreeNode.getId());
									transferDataBean.setName(ecoreTreeNode.getName());
									transferDataBean.setLevel(ecoreTreeNode.getLevel());
									transferDataBean.setType((String) typeTreeItem.getData("type"));
									transferDataBean.setChildId(typeTreeItem.getData("childId") == null ? ""
											: (String) typeTreeItem.getData("childId"));
									if ("1".equals(ecoreTreeNode.getObjType())) {
										transferDataBean.setImgPath(ImgUtil.CODE_SET_SUB1_2);
									} else if ("2".equals(ecoreTreeNode.getObjType())) {
										transferDataBean.setImgPath(ImgUtil.CODE_SET_SUB1);
									}
									transferDataBean.setTreeListItem((MrTreeItem) typeTreeItem);
									cTabFolder.setSelection(0);
									firstTabTree.setSelection(typeTreeItem);
									EditorUtil.open(transferDataBean.getType(), transferDataBean);
									return;
								}
							}
						}
					} else {
						// 跳转到
						String msgComponentId = (String) treeItem.getData("meType_id");
						Tree secondTabTree = MrRepository.get().messageComponentsTabTree;
						TreeItem mcTreeItem = secondTabTree.getItem(1);
						for (TreeItem mcItem : mcTreeItem.getItems()) {
							EcoreTreeNode ecoreTreeNode = (EcoreTreeNode) mcItem.getData("EcoreTreeNode");
							// 选择后跳转
							if (meTypeText.getData("meLever") != null) {
								String meComponentId = (String) meTypeText.getData("meType_id");
								if (ecoreTreeNode.getId().equals(meComponentId)) {
									TransferDataBean transferDataBean = new TransferDataBean();
									transferDataBean.setId(ecoreTreeNode.getId());
									transferDataBean.setName(ecoreTreeNode.getName());
									transferDataBean.setLevel(ecoreTreeNode.getLevel());
									transferDataBean.setType((String) mcItem.getData("type"));
									transferDataBean.setChildId(mcItem.getData("childId") == null ? ""
											: (String) mcItem.getData("childId"));
									String status = ecoreTreeNode.getRegistrationStatus();
									if ("1".equals(ecoreTreeNode.getType())) {
										if (status == null || status.equals("Provisionally Registered")
												|| status.equals("Added Registered")) {
											transferDataBean.setImgPath(ImgUtil.MC_SUB3_COMPONENT);
										} else {
											transferDataBean.setImgPath(ImgUtil.MC_SUB1_COMPONENT);
										}
									} else if ("2".equals(ecoreTreeNode.getType())) {
										if (status == null || status.equals("Provisionally Registered")
												|| status.equals("Added Registered")) {
											transferDataBean.setImgPath(ImgUtil.MC_SUB1_CHOICE1);
										} else {
											transferDataBean.setImgPath(ImgUtil.MC_SUB1_CHOICE);
										}
									}
									transferDataBean.setTreeListItem((MrTreeItem) mcItem);
									cTabFolder.setSelection(1);
									secondTabTree.setSelection(mcItem);
									EditorUtil.open(transferDataBean.getType(), transferDataBean);
									return;
								}
							} else if (ecoreTreeNode.getId().equals(msgComponentId)) {
								TransferDataBean transferDataBean = new TransferDataBean();
								transferDataBean.setId(ecoreTreeNode.getId());
								transferDataBean.setName(ecoreTreeNode.getName());
								transferDataBean.setLevel(ecoreTreeNode.getLevel());
								transferDataBean.setType((String) mcItem.getData("type"));
								transferDataBean.setChildId(
										mcItem.getData("childId") == null ? "" : (String) mcItem.getData("childId"));
								String status = ecoreTreeNode.getRegistrationStatus();
								if ("1".equals(ecoreTreeNode.getType())) {
									if (status == null || status.equals("Provisionally Registered")
											|| status.equals("Added Registered")) {
										transferDataBean.setImgPath(ImgUtil.MC_SUB3_COMPONENT);
									} else {
										transferDataBean.setImgPath(ImgUtil.MC_SUB1_COMPONENT);
									}
								} else if ("2".equals(ecoreTreeNode.getType())) {
									if (status == null || status.equals("Provisionally Registered")
											|| status.equals("Added Registered")) {
										transferDataBean.setImgPath(ImgUtil.MC_SUB1_CHOICE1);
									} else {
										transferDataBean.setImgPath(ImgUtil.MC_SUB1_CHOICE);
									}
								}
								transferDataBean.setTreeListItem((MrTreeItem) mcItem);
								cTabFolder.setSelection(1);
								secondTabTree.setSelection(mcItem);
								EditorUtil.open(transferDataBean.getType(), transferDataBean);
								return;
							}
						}
					}
				}
			}
		});
		String meXmlTag = treeItem.getData("meXmlTagText") == null ? "" : treeItem.getData("meXmlTagText") + "";
		meXmlTagText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 312, "XML Tag", false, meXmlTag)).getText();
		meXmlTagText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTableItem.isDisposed()) {
					selectedTableItem.setData("meXmlTagText", meXmlTagText.getText());
					setDirty(true);
				}
			}
		});
		Boolean isDerivedCheck = treeItem.getData("isDerivedCheckButton") == null ? false : (boolean) treeItem.getData("isDerivedCheckButton");
		isDerivedCheckButton = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_CHECK, 340, "Derived",isDerivedCheck)).getButton();
		isDerivedCheckButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (!selectedTableItem.isDisposed()) {
					setDirty(true);
					selectedTableItem.setData("isDerivedCheckButton", isDerivedCheckButton.getSelection());
				}
			}
		});

		Boolean compositeCheck = treeItem.getData("compositeCheckButton") == null ? false : (boolean) treeItem.getData("compositeCheckButton");
		compositeCheckButton = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_CONTENT_TYPE_CHECK, 368, "Composite",compositeCheck)).getButton();
		compositeCheckButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (!selectedTableItem.isDisposed()) {
					setDirty(true);
					selectedTableItem.setData("compositeCheckButton", compositeCheckButton.getSelection());
				}
			}
		});
		// ------------ Message Element Details End ------------------------
		// -----------------------CMP Information Group Begin -----------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 418, I18nApi.get("editor.title.cmp")));
		
		String meObjectIdentifier = treeItem.getData("meObjectIdentifierText") == null ? "" : treeItem.getData("meObjectIdentifierText") + "";
		meObjectIdentifierText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 458, "Object Identifier", false, meObjectIdentifier)).getText();
		// -----------------------CMP Information Group End -----------------------

		// ---------- Synonyms Expand Item Begin ----------------------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 508, I18nApi.get("editor.title.ss")));
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_NOTE_ONE_TYPE, 548, "This section show the synonyms of object"));

		addSynonymsBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR), false)).getButton();
		addSynonymsBtn.setBounds(10, 576, 30, 30);
		addSynonymsBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				MrEditorDialogCreator.createSynonymsDialogue(meSynonymsTable, selectedTableItem);
			}
		});

		deleteButtonForSynonyms = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES), false)).getButton();
		deleteButtonForSynonyms.setBounds(42, 576, 30, 30);
		deleteButtonForSynonyms.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				MrEditorDialogCreator.deleteSynonymDialogue(meSynonymsTable, selectedTableItem);
			}
		});

		meSynonymsTable = new Table(rightComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		meSynonymsTable.setHeaderVisible(true);
		meSynonymsTable.setLinesVisible(true);
		meSynonymsTable.setBounds(10, 611, 480, 150);
		meSynonymsTable.layout();
		
		new SummaryTableComposite(meSynonymsTable, 238, "Context");
		new SummaryTableComposite(meSynonymsTable, 238, "Synonums");
		// ---------- Synonyms Expand Item End -------------------------------------

		// ---------- Constraints Expand Item Begin --------------------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 766, I18nApi.get("editor.title.cs")));

		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_NOTE_TWO_TYPE, 806, "All the constraints contained in this object(other constraints - such as constraints"
				+ "\r\n defined on type - may also apply)."));

		Button addConstraintBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR), false)).getButton();
		addConstraintBtn.setBounds(10, 851, 30, 30);
		addConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				MrEditorDialogCreator.createConstraintDialogueForBC("add", meConstraintsTable, selectedTableItem);
			}
		});

		Button editConstraintBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.EDIT_IMG_TOOLBAR), false)).getButton();
		editConstraintBtn.setBounds(42, 851, 30, 30);
		editConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (meConstraintsTable.getSelectionCount() > 0) {
					setDirty(true);
					MrEditorDialogCreator.createConstraintDialogueForBC("edit", meConstraintsTable, selectedTableItem);
				}
			}
		});
		
		Button deleteConstraintBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES), false)).getButton();
		deleteConstraintBtn.setBounds(74, 851, 30, 30);
		deleteConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				MrEditorDialogCreator.deleteConstraintForBC(meConstraintsTable, selectedTableItem);
			}
		});

		meConstraintsTable = new Table(rightComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		meConstraintsTable.setHeaderVisible(true);
		meConstraintsTable.setLinesVisible(true);
		meConstraintsTable.setBounds(10, 886, 480, 150);
		meConstraintsTable.layout();
		new SummaryTableComposite(meConstraintsTable, 113, "Name");
		new SummaryTableComposite(meConstraintsTable, 113, "Definition");
		new SummaryTableComposite(meConstraintsTable, 137, "Expression Language");
		new SummaryTableComposite(meConstraintsTable, 113, "Expression");
		// ---------- Constraints Expand Item End -------------------------------------
	}

	private void showCodeDetailBar(TreeItem treeItem, Composite rightComposite) {
		for (Control control : rightComposite.getChildren()) {
			control.dispose();
		}
		// ------------ Code Details Begin ---------------------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 5, I18nApi.get("editor.title.cd")));
		
		String name = treeItem.getData("CodesName") == null ? "" : treeItem.getData("CodesName") + "";
		nameText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 45, "Name", false, name)).getText();
		
		String codeDocument = treeItem.getData("Document") == null ? "" : treeItem.getData("Document") + "";
		codeDocumentText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SCROLL_TYPE, 73, "Documentation", false, codeDocument)).getText();
		
		String codeName = treeItem.getData("CodeName") == null ? "" : treeItem.getData("CodeName") + "";
		codeNameText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 228, "CodeName", false, codeName)).getText();
		// ------------ Code Details End ---------------------------------
		// -----------------------CMP Information Group Begin -----------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 278, I18nApi.get("editor.title.cmp")));
		
		String objectIdentifier = treeItem.getData("meObjectIdentifierText") == null ? "" : treeItem.getData("meObjectIdentifierText") + "";
		meObjectIdentifierText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 318, "Object Identifier", false, objectIdentifier)).getText();

		// -----------------------CMP Information Group End -----------------------
		// ---------- Constraints Expand Item Begin--------------------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 368, I18nApi.get("editor.title.cs")));
		
		new SummaryRowTextComposite(rightComposite, 
				new TextPolicy(TextPolicy.TEXT_CONTENT_NOTE_TWO_TYPE, 396, "All the constraints contained in this object(other constraints - such as constraints \r\n"
						+ "defined on type - may also apply).")).getText();

		Button addConstraintBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR), false)).getButton();
		addConstraintBtn.setBounds(10, 451, 30, 30);
		addConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				MrEditorDialogCreator.createConstraintDialogueForBC("add", meConstraintsTable, selectedTableItem);
			}
		});
		
		Button editConstraintBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.EDIT_IMG_TOOLBAR), false)).getButton();
		editConstraintBtn.setBounds(10, 451, 30, 30);
		editConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (meConstraintsTable.getSelectionCount() > 0) {
					setDirty(true);
					MrEditorDialogCreator.createConstraintDialogueForBC("edit", meConstraintsTable, selectedTableItem);
				}
			}
		});
		
		Button deleteConstraintBtn = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES), false)).getButton();
		deleteConstraintBtn.setBounds(10, 451, 30, 30);
		deleteConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				MrEditorDialogCreator.deleteConstraintForBC(meConstraintsTable, selectedTableItem);
			}
		});

		meConstraintsTable = new Table(rightComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		meConstraintsTable.setHeaderVisible(true);
		meConstraintsTable.setLinesVisible(true);
		meConstraintsTable.setBounds(10, 486, 480, 150);
		meConstraintsTable.layout();
		new SummaryTableComposite(meConstraintsTable, 113, "Name");
		new SummaryTableComposite(meConstraintsTable, 113, "Definition");
		new SummaryTableComposite(meConstraintsTable, 137, "Expression Language");
		new SummaryTableComposite(meConstraintsTable, 113, "Expression");
		// ---------- Constraints Expand Item End -------------------------------------
	}

	public void changeStatus() {
		if (msgComponent == null) {
		} else {
			if (msgComponent.getRegistrationStatus() == null) {

			} else {
				String status = msgComponent.getRegistrationStatus();
				if (status.equals("Registered") || status.equals("Provisionally Registered")
						|| status.equals("Obsolete")) {
					changeEditor();
				} else if (status.equals("Added Registered")) {
//			renameButton.setEnabled(true);
				}
			}
		}
	}
}
