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
import org.cufir.plugin.mr.bean.DataTypesEnum;
import org.cufir.plugin.mr.bean.ObjTypeEnum;
import org.cufir.plugin.mr.bean.RegistrationStatusEnum;
import org.cufir.plugin.mr.bean.TextPolicy;
import org.cufir.plugin.mr.bean.TreeLevelEnum;
import org.cufir.plugin.mr.handlers.SaveHandler;
import org.cufir.plugin.mr.utils.ImgUtil;
import org.cufir.plugin.mr.utils.NumberUtil;
import org.cufir.plugin.mr.utils.SystemUtil;
import org.cufir.s.data.bean.EcoreBusinessComponent;
import org.cufir.s.data.bean.EcoreCode;
import org.cufir.s.data.bean.EcoreConstraint;
import org.cufir.s.data.bean.EcoreDataType;
import org.cufir.s.data.bean.EcoreExample;
import org.cufir.s.data.bean.EcoreMessageBuildingBlock;
import org.cufir.s.data.bean.EcoreMessageComponent;
import org.cufir.s.data.bean.EcoreMessageDefinition;
import org.cufir.s.data.bean.EcoreMessageSet;
import org.cufir.s.data.vo.EcoreCodeVO;
import org.cufir.s.data.vo.EcoreDataTypeVO;
import org.cufir.s.data.vo.EcoreTreeNode;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import com.cfets.cufir.s.ide.utils.i18n.I18nApi;

/**
 * DataType （数据类型）编辑和显示
 * 
 * @author jiangqiming_het
 *
 */

public class DataTypesEditor extends MrMultiPageEditor {
	
	private java.util.List<EcoreConstraint> css = MrRepository.get().ecoreConstraints;
	private java.util.List<EcoreMessageBuildingBlock> mbbs =  MrRepository.get().ecoreMessageBuildingBlocks;

	private MrEditorInput myEditorInput;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	EcoreDataTypeVO ecoreDataTypeVO;

	// 基本数据类型
	private String dataType;
	// -----------For Summary Page -----------
	private Text nameText, documentationText, minLengthText, maxLengthText, lengthText, patternText, summaryRemovalDate,
			identificationSchemeText, meaningWhenTrueText, meaningWhenFalseText, minInclusiveText, minExclusiveText,
			maxInclusiveText, maxExclusiveText, totalDigitsText, fractionDigitsText, baseValueText, baseUnitCodeText,
			namespaceListText;
	private Combo namespaceCombo, processContentCombo;
	private Text objectIdentifierValue;
	private Combo statusCombo;
	private List examplesList;

	private Tree newCodeTree;

	private Table summaryConstraintsTable;

	private Table contentConstraintsTable;

	private TreeItem selectedTreeItem;

	public TreeItem modelExploreTreeItem;

	private Text contentNameText, contentDocText, contentCodeNameText, contentExpressionText,
			contentExpressionLanguageText, contentObjectIdentifierText, contentRemovalDate;

	private String ecoreDataTypeId;

	private String traceId;

	// 为展示数据
	// Use for Impact Page
	private ArrayList<EcoreMessageComponent> ecoreMessageComponentList = new ArrayList<>();
	// Use for Impact Page
	private java.util.List<EcoreMessageDefinition> msgDefinitionList = new ArrayList<>();
	// Use for Impact Page
	private java.util.List<EcoreMessageSet> msgSetList = new ArrayList<>();
	// Use for Incoming Association Page
	private ArrayList<EcoreBusinessComponent> imcomingAssociationList = new ArrayList<>();

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		this.setSite(site);
		this.setInput(input);
		this.setPartProperty("customName", "dataTypesCreate");
		this.myEditorInput = (MrEditorInput) input;

		modelExploreTreeItem = this.myEditorInput.getTransferDataBean().getTreeListItem();
		this.dataType = this.myEditorInput.getTransferDataBean().getType();

		// 设置标题图片
		this.setTitleImage(ImgUtil.createImg(this.myEditorInput.getTransferDataBean().getImgPath()));
		// ctrl + s保存必须要
		setContext(site);

		this.ecoreDataTypeId = this.myEditorInput.getTransferDataBean().getId();

		this.setPartProperty("ID", this.ecoreDataTypeId);

		// 加载数据
		loadDataTypeData();

	}

	private void loadDataTypeData() {
		try {
			ecoreDataTypeVO = MrImplManager.get().getEcoreDataTypeImpl().findDataTypeVO(ecoreDataTypeId);
			if (ecoreDataTypeVO != null) {
				ecoreMessageComponentList = MrHelper.getMessageComponentByDataTypeId(ecoreDataTypeId);
				msgDefinitionList = MrImplManager.get().getEcoreMessageDefinitionImpl().findByMsgComponents(ecoreMessageComponentList);
				msgSetList = MrImplManager.get().getEcoreMessageSetImpl().findByMsgDefinitions(msgDefinitionList);
				imcomingAssociationList = MrHelper.getIncomingAssociationsListForDataType(ecoreDataTypeId);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ecoreDataTypeVO = new EcoreDataTypeVO();
	}

	@Override
	protected void createPages() {
		createSummaryPage();
		if (this.dataType.equals(DataTypesEnum.CODE_SETS.getName())) {
			traceId = ecoreDataTypeVO.getEcoreDataType() != null ? ecoreDataTypeVO.getEcoreDataType().getTrace() : null;
			createContentPageForCodeSets();
		} else {
			createContentPageForOthers();
		}
		createImpactPage();
		createIncomingAssociationsPage();
//		createVersionSubset();
		if (ecoreDataTypeVO.getEcoreDataType() != null) {
			this.setDirty(false);
			this.setPartName(ecoreDataTypeVO.getEcoreDataType().getName());
		} else {
			this.setDirty(true);
			this.setPartName("");
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
	public void doSaveAs() {
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	private void createSummaryPage() {
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
		
		boolean isEditable = true;
		if (ecoreDataTypeVO.getEcoreDataType() != null && RegistrationStatusEnum.Registered.getStatus().equals(ecoreDataTypeVO.getEcoreDataType().getRegistrationStatus())) {
			if ("1".equals(ecoreDataTypeVO.getEcoreDataType().getType()) && ecoreDataTypeVO.getEcoreDataType().getTrace() == null) { // 是Code Set，并且是 可衍生的: 那么可编辑
				isEditable = true;
			} else {
				isEditable = false;
			}
		}
		// ================= General Information Group Begin =======================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 5, I18nApi.get("editor.title.gi")));
		
		// 名称
		String name = ecoreDataTypeVO.getEcoreDataType() == null ? "" : ecoreDataTypeVO.getEcoreDataType().getName();
		nameText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 45, "Name", isEditable, name)).getText();
		nameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setPartName(nameText.getText());
				// 左边的树的名称展示
				EcoreTreeNode ecoreTreeNode = (EcoreTreeNode) modelExploreTreeItem.getData("EcoreTreeNode");
				String type = (String) modelExploreTreeItem.getData("type");
				if (DataTypesEnum.checkTypeName(type)) {
					if (TreeLevelEnum.LEVEL_3.getLevel().equals(ecoreTreeNode.getLevel())) {
						modelExploreTreeItem.setText(nameText.getText());
						setDirty(true);
					} else {
						modelExploreTreeItem.getParentItem().setText(nameText.getText());
					}
				}
			}
		});
		
		String documentation = ecoreDataTypeVO.getEcoreDataType() == null ? "" : ecoreDataTypeVO.getEcoreDataType().getDefinition();
		documentationText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_SCROLL_TYPE, 73, "Documentation", isEditable, documentation, new MrEditorModifyListener(this))).getText();

		if (dataType.equals(DataTypesEnum.CODE_SETS.getName()) || dataType.equals(DataTypesEnum.TEXT.getName())
				|| dataType.equals(DataTypesEnum.BINARY.getName())) {
			
			String minLength = ecoreDataTypeVO.getEcoreDataType() == null ? "" : ecoreDataTypeVO.getEcoreDataType().getMinLength() + "";
			
			minLengthText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 278, "Min Length", isEditable, minLength, new MrEditorModifyListener(this))).getText();

			String maxLength = ecoreDataTypeVO.getEcoreDataType() == null ? "" : ecoreDataTypeVO.getEcoreDataType().getMaxLength() + "";
			maxLengthText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 306, "Max Length", isEditable, maxLength, new MrEditorModifyListener(this))).getText();

			String length = ecoreDataTypeVO.getEcoreDataType() == null ? "" : ecoreDataTypeVO.getEcoreDataType().getLength() + "";
			lengthText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 334, "Length", isEditable, length, new MrEditorModifyListener(this))).getText();
		}

		if (dataType.equals(DataTypesEnum.INDICATOR.getName())) {
			String meaningWhenTrue = ecoreDataTypeVO.getEcoreDataType() == null ? "" : ecoreDataTypeVO.getEcoreDataType().getMeaningWhenTrue();
			meaningWhenTrueText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 278, "Meaning When True", isEditable, meaningWhenTrue, new MrEditorModifyListener(this))).getText();

			String meaningWhenFalse = ecoreDataTypeVO.getEcoreDataType() == null ? "" : ecoreDataTypeVO.getEcoreDataType().getMeaningWhenFalse();
			meaningWhenFalseText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 306, "Meaning When False", isEditable, meaningWhenFalse, new MrEditorModifyListener(this))).getText();
		}

		if (dataType.equals(DataTypesEnum.DECIMAL.getName()) || dataType.equals(DataTypesEnum.RATE.getName())
				|| dataType.equals(DataTypesEnum.AMOUNT.getName()) || dataType.equals(DataTypesEnum.QUANTITY.getName())
				|| dataType.equals(DataTypesEnum.TIME.getName())) {
			
			String minInclusive = ecoreDataTypeVO.getEcoreDataType() == null ? "" : ecoreDataTypeVO.getEcoreDataType().getMinInclusive();
			minInclusiveText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 278, "Min Inclusive", isEditable, minInclusive, new MrEditorModifyListener(this))).getText();

			String minExclusive = ecoreDataTypeVO.getEcoreDataType() == null ? "" : ecoreDataTypeVO.getEcoreDataType().getMinExclusive();
			minExclusiveText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 306, "Min Exclusive", isEditable, minExclusive, new MrEditorModifyListener(this))).getText();

			String maxInclusive = ecoreDataTypeVO.getEcoreDataType() == null ? "" : ecoreDataTypeVO.getEcoreDataType().getMaxInclusive();
			maxInclusiveText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 334, "Max Inclusive", isEditable, maxInclusive, new MrEditorModifyListener(this))).getText();

			String maxExclusive = ecoreDataTypeVO.getEcoreDataType() == null ? "" : ecoreDataTypeVO.getEcoreDataType().getMaxExclusive();
			maxExclusiveText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 362, "Max Exclusive", isEditable, maxExclusive, new MrEditorModifyListener(this))).getText();

			if (!dataType.equals(DataTypesEnum.TIME.getName())) {
				String totalDigits = ecoreDataTypeVO.getEcoreDataType() == null ? "" : ecoreDataTypeVO.getEcoreDataType().getTotalDigits() + "";
				totalDigitsText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 390, "Total Digits", isEditable, totalDigits, new MrEditorModifyListener(this))).getText();

				String fractionDigits = ecoreDataTypeVO.getEcoreDataType() == null ? "" : ecoreDataTypeVO.getEcoreDataType().getFractionDigits() + "";
				fractionDigitsText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 418, "Fraction Digits", isEditable, fractionDigits, new MrEditorModifyListener(this))).getText();
			}
		}

		if (dataType.equals(DataTypesEnum.USER_DEFINED.getName())) {
			String namespace = ecoreDataTypeVO.getEcoreDataType() == null ? "" : ecoreDataTypeVO.getEcoreDataType().getNamespace();
			java.util.List<String> namespaceList = new ArrayList<>();
			namespaceList.add("##any");
			namespaceList.add("##other");
			namespaceList.add("list");
			
			namespaceCombo = new SummaryRowComboComposite(summaryComposite, new ComboPolicy(ComboPolicy.COMBO_COMMONLY_TYPE, 278,"Namespace", isEditable, namespaceList, namespace)).getCombo();
			namespaceCombo.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					setDirty(true);
				}
			});

			String nameSpaceList = ecoreDataTypeVO.getEcoreDataType() == null ? "" : ecoreDataTypeVO.getEcoreDataType().getNamespaceList();
			namespaceListText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_SCROLL_TYPE, 306, "Namespace List", isEditable, nameSpaceList, new MrEditorModifyListener(this))).getText();
			
			String processContent = ecoreDataTypeVO.getEcoreDataType() == null ? "" : ecoreDataTypeVO.getEcoreDataType().getProcessContents();
			java.util.List<String> processContentList = new ArrayList<>();
			processContentList.add("lax");
			processContentList.add("skip");
			processContentList.add("strict");
			processContentCombo = new SummaryRowComboComposite(summaryComposite, new ComboPolicy(ComboPolicy.COMBO_COMMONLY_TYPE, 471,"Process Content", isEditable, processContentList, processContent)).getCombo();
		}else {
			String pattern = ecoreDataTypeVO.getEcoreDataType() == null ? "" : ecoreDataTypeVO.getEcoreDataType().getPattern();
			patternText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 278, "Pattern", isEditable, pattern, new MrEditorModifyListener(this))).getText();
		}

		if (dataType.equals(DataTypesEnum.CODE_SETS.getName()) || dataType.equals(DataTypesEnum.TEXT.getName())) {
			String identificationScheme = ecoreDataTypeVO.getEcoreDataType() == null ? "" : ecoreDataTypeVO.getEcoreDataType().getIdentificationScheme();
			identificationSchemeText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 278, "Identification Scheme", isEditable, identificationScheme, new MrEditorModifyListener(this))).getText();
			if (dataType.equals(DataTypesEnum.TEXT.getName())) {
				identificationSchemeText.setMessage("This entry is required if it is of type Identifier.");
			}
		}

		if (dataType.equals(DataTypesEnum.RATE.getName())) {
			String baseValue = ecoreDataTypeVO.getEcoreDataType() == null ? "" : ecoreDataTypeVO.getEcoreDataType().getBaseValue() + "";
			baseValueText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 278, "Base Value", isEditable, baseValue, new MrEditorModifyListener(this))).getText();

			String baseUnitCode = ecoreDataTypeVO.getEcoreDataType() == null ? "" : ecoreDataTypeVO.getEcoreDataType().getBaseUnitCode() + "";
			baseUnitCodeText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 306, "Base Unit Code", isEditable, baseUnitCode, new MrEditorModifyListener(this))).getText();
		}
		// ================= General Information Group End ============================

		// ================= CMP Information Group Begin ==============================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 521, I18nApi.get("editor.title.cmp")));

		String objectIdentifier = ecoreDataTypeVO.getEcoreDataType() == null ? "" : ecoreDataTypeVO.getEcoreDataType().getObjectIdentifier();
		objectIdentifierValue = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 561, "Object Identifier", isEditable, objectIdentifier, new MrEditorModifyListener(this))).getText();
		// ================= CMP Information Group End ==============================

		// ===== Registration Information Group Begin ===============================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 611, I18nApi.get("editor.title.ri")));
		
		//状态
		java.util.List<String> list = new ArrayList<>();
		list.add(RegistrationStatusEnum.Registered.getStatus());
		list.add(RegistrationStatusEnum.Provisionally.getStatus());
		list.add(RegistrationStatusEnum.Added.getStatus());
		
		String statusText = "";
		if(ecoreDataTypeVO.getEcoreDataType() != null) {
			statusText = ecoreDataTypeVO.getEcoreDataType().getRegistrationStatus()==null?"":ecoreDataTypeVO.getEcoreDataType().getRegistrationStatus();
		}else {
			statusText = RegistrationStatusEnum.Added.getStatus();
		}
		
		statusCombo = new SummaryRowComboComposite(summaryComposite, new ComboPolicy(ComboPolicy.COMBO_COMMONLY_TYPE, 651,"Registration Status", false, list, statusText)).getCombo();
		statusCombo.addModifyListener(new MrEditorModifyListener(this));

		//时间
		String removalDateText = ecoreDataTypeVO.getEcoreDataType() == null || ecoreDataTypeVO.getEcoreDataType().getRemovalDate() == null ? "" : DateFormat.getInstance().format(ecoreDataTypeVO.getEcoreDataType().getRemovalDate());
		summaryRemovalDate = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_SHORT_TYPE, 679, "Removal Date", false, removalDateText, new MrEditorModifyListener(this))).getText();
		if (ecoreDataTypeVO.getEcoreDataType() != null) {
			String date;
			try {
				date = sdf.format(ecoreDataTypeVO.getEcoreDataType().getRemovalDate());
			} catch (Exception e) {
				date = "";
			}
			summaryRemovalDate.setText(date);
		}
		
		// ===== Registration Information Group End ===============================

		// =================== Exampel Group Begin ==============================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 729, I18nApi.get("editor.title.es")));

		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_NOTE_TYPE, 769, "In this section, you can define examples."));

		Button addExamplesBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR),isEditable)).getButton();
		addExamplesBtn.setBounds(10, 797, 30, 30);
		addExamplesBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MrEditorDialogCreator.createExamplesDialogue(examplesList);
				setDirty(true);
			}
		});

		Button deleteExamplesBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES),isEditable)).getButton();
		deleteExamplesBtn.setBounds(42, 797, 30, 30);
		deleteExamplesBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MrEditorDialogCreator.deleteExamplesDialogue(examplesList);
				setDirty(true);
			}
		});

		examplesList = new List(summaryComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		examplesList.setBounds(10, 832, 600, 100);
		GridData examplesListGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		examplesListGridData.widthHint = 500;
		examplesListGridData.heightHint = 100;
		examplesList.setLayoutData(examplesListGridData);
		if (this.ecoreDataTypeVO.getEcoreExamples() != null && this.ecoreDataTypeVO.getEcoreExamples().size() > 0) {
			Map<String, String> map = new HashMap<String, String>();
			for (EcoreExample example : this.ecoreDataTypeVO.getEcoreExamples()) {
				map.put(example.getExample(), example.getId());
				examplesList.add(example.getExample());
			}
			examplesList.setData("map", map);
		}
		// =================== Exampel Group End ==============================

		// =================== constraints Begin ==============================
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 957, I18nApi.get("editor.title.cs")));

		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_NOTE_TYPE, 997, "All the constraints contained in this object (other constraints - such as constraints defined on type - may also apply)."));
		
		Button addConstraintBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR),isEditable)).getButton();
		addConstraintBtn.setBounds(10, 1025, 30, 30);
		addConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MrEditorDialogCreator.createConstraintDialogue("add", "1", summaryConstraintsTable, newCodeTree,
						dataType);
				setDirty(true);
			}
		});

		Button editConstraintBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.EDIT_IMG_TOOLBAR),isEditable)).getButton();
		editConstraintBtn.setBounds(42, 1025, 30, 30);
		editConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				// 不选中不触发任何效果
				if (summaryConstraintsTable.getSelectionCount() > 0) {
					MrEditorDialogCreator.createConstraintDialogue("edit", "1", summaryConstraintsTable, newCodeTree,
							dataType);
					setDirty(true);
				}
			}
		});

		Button deleteConstraintBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES),isEditable)).getButton();
		deleteConstraintBtn.setBounds(74, 1025, 30, 30);
		deleteConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MrEditorDialogCreator.deleteConstraint("1", summaryConstraintsTable, newCodeTree, dataType);
				setDirty(true);
			}
		});

		summaryConstraintsTable = new Table(summaryComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		summaryConstraintsTable.setHeaderVisible(true);
		summaryConstraintsTable.setLinesVisible(true);
		summaryConstraintsTable.setBounds(10, 1060, 600, 150);
		summaryConstraintsTable.layout();
		new SummaryTableComposite(summaryConstraintsTable, 149, "Name");
		new SummaryTableComposite(summaryConstraintsTable, 149, "Definiton");
		new SummaryTableComposite(summaryConstraintsTable, 149, "Expression Language");
		new SummaryTableComposite(summaryConstraintsTable, 149, "Expression");
		
		if (this.ecoreDataTypeVO.getEcoreConstraints() != null
				&& this.ecoreDataTypeVO.getEcoreConstraints().size() > 0) {
			summaryConstraintsTable.removeAll();
			for (EcoreConstraint ecoreConstraint : this.ecoreDataTypeVO.getEcoreConstraints()) {
				TableItem tableItem = new TableItem(summaryConstraintsTable, SWT.NONE);
				tableItem.setText(new String[] { ecoreConstraint.getName(), ecoreConstraint.getDefinition(),
						ecoreConstraint.getExpression(), ecoreConstraint.getExpressionlanguage() });
			}
		}
		// =================== constraints End ==============================
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	/**
	 * content页面
	 */
	private void createContentPageForCodeSets() {
		
		ScrolledComposite scrolledComposite = createContentComposite();
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(composite);
		
		Composite leftComposite = new Composite(composite, SWT.BORDER);
		leftComposite.setLayout(new FillLayout(SWT.VERTICAL));
		leftComposite.setBounds(0, 0, 500, 730);
		leftComposite.layout();
		
		Composite rightComposite = new Composite(composite, SWT.BORDER);
		rightComposite.setLayout(new FillLayout(SWT.VERTICAL));
		rightComposite.setBounds(510, 0, 500, 754);
		rightComposite.layout();

		// 如果是派生的 右边不能编辑
		if (this.traceId != null) {
			rightComposite.setEnabled(false);
		}

		// ------------ Content Expand Bar Begin ----------------------------------
		Composite leftCom = new Composite(leftComposite, SWT.NONE);
		leftCom.setBounds(0, 5, 490, 730);
		leftCom.layout();
		
		new SummaryRowTextComposite(leftCom, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 0, I18nApi.get("editor.title.ct")));

		Button addButtonForContent = new SummaryRowTextComposite(leftCom, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR))).getButton();
		addButtonForContent.setBounds(5, 35, 30, 30);
		addButtonForContent.addListener(SWT.MouseUp, p -> {
			TreeItem item = new TreeItem(newCodeTree, SWT.NONE);
			item.setImage(ImgUtil.createImg(ImgUtil.CODE_SET_SUB2));
			item.setText("");
			item.setData("contentStatusCombo", RegistrationStatusEnum.Added.getStatus());
			selectedTreeItem = item;
			newCodeTree.setSelection(item);
			codeTreeListener(item, rightComposite);
		});
		
		if (ecoreDataTypeVO.getEcoreDataType() != null && RegistrationStatusEnum.Registered.getStatus()
				.equals(ecoreDataTypeVO.getEcoreDataType().getRegistrationStatus())) {
			if ("1".equals(ecoreDataTypeVO.getEcoreDataType().getType())
					&& ecoreDataTypeVO.getEcoreDataType().getTrace() == null) { // 是Code Set，并且是可衍生的，
				addButtonForContent.setEnabled(true);
			} else {
				addButtonForContent.setEnabled(false);
			}
		} else if (ecoreDataTypeVO.getEcoreDataType() != null
				&& ecoreDataTypeVO.getEcoreDataType().getTrace() != null) {
			addButtonForContent.setEnabled(false);
		}

		Button deleteButtonForContent = new SummaryRowTextComposite(leftCom, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES))).getButton();
		deleteButtonForContent.setBounds(37, 35, 30, 30);
		deleteButtonForContent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				// 删除操作
				TreeItem[] selection = newCodeTree.getSelection();
				if (selection != null && selection.length > 0) {
					TreeItem item = selection[0];
					item.dispose();
					setDirty(true);
					
					for (Control control: rightComposite.getChildren()) {
						control.dispose();
					}
				}
			}
		});
		if (ecoreDataTypeVO.getEcoreDataType() != null && RegistrationStatusEnum.Registered.getStatus()
				.equals(ecoreDataTypeVO.getEcoreDataType().getRegistrationStatus())) {
			if ("1".equals(ecoreDataTypeVO.getEcoreDataType().getType())
					&& ecoreDataTypeVO.getEcoreDataType().getTrace() == null) { // 是Code Set，并且是可衍生的，
				deleteButtonForContent.setEnabled(true);
			} else {
				deleteButtonForContent.setEnabled(false);
			}
		} else if (ecoreDataTypeVO.getEcoreDataType() != null
				&& ecoreDataTypeVO.getEcoreDataType().getTrace() != null) {
			deleteButtonForContent.setEnabled(false);
		}
		
		// 构造Content下的Code节点
		if (traceId != null) {
			newCodeTree = new Tree(leftCom, SWT.CHECK | SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
			newCodeTree.setBounds(5, 70, 485, 650);
			newCodeTree.layout();
			java.util.List<EcoreCode> ecoreCodeList = MrHelper.findCodesByCodeSetId(MrRepository.get().ecoreCodes, traceId);
			for (EcoreCode ecoreCode : ecoreCodeList) {
				TreeItem treeItem = new TreeItem(newCodeTree, SWT.NONE);
				treeItem.setText("" + ecoreCode.getName());
				treeItem.setImage(ImgUtil.createImg(ImgUtil.CODE_SET_SUB2));

				for (TreeItem codeTreeItem : this.modelExploreTreeItem.getItems()) {
					EcoreTreeNode ecoreTreeNode = (EcoreTreeNode) codeTreeItem.getData("EcoreTreeNode");
					if (ecoreTreeNode.getName().equals(ecoreCode.getName())) {
						treeItem.setChecked(true);
					}
				}
				treeItem.setData("contentNameText", ecoreCode.getName());
				treeItem.setData("contentDocText", ecoreCode.getDefinition());
				treeItem.setData("contentCodeNameText", ecoreCode.getCodeName());
				treeItem.setData("contentObjectIdentifierText", ecoreCode.getObjectIdentifier());
				treeItem.setData("contentStatusCombo", ecoreCode.getRegistrationStatus());
				treeItem.setData("contentRemovalDate", ecoreCode.getRemovalDate());

				if (ecoreDataTypeVO.getEcoreDataType() != null && RegistrationStatusEnum.Registered.getStatus()
						.equals(ecoreDataTypeVO.getEcoreDataType().getRegistrationStatus())) {
					treeItem.setData("disableCheckbox", Boolean.TRUE);
				}
				java.util.List<EcoreConstraint> ecs = MrHelper.findConstraintsByObjId(css, ecoreCode.getId());
				java.util.List<String[]> constraintsTableItems = new ArrayList<String[]>();
				if (ecs != null && ecs.size() > 0) {
					for (EcoreConstraint constraint : ecs) {
						String[] str = new String[4];
						str[0] = constraint.getName();
						str[1] = constraint.getDefinition();
						str[2] = constraint.getExpression();
						str[3] = constraint.getExpressionlanguage();
						constraintsTableItems.add(str);
					}
				}
				treeItem.setData("constraintsTableItems", constraintsTableItems);
			}
		} else {
			newCodeTree = new Tree(leftCom, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
			newCodeTree.setBounds(5, 70, 485, 650);
			newCodeTree.layout();

			if (ecoreDataTypeVO.getEcoreCodeVOs() != null && ecoreDataTypeVO.getEcoreCodeVOs().size() > 0) {
				java.util.List<EcoreCodeVO> ecoreCodeVOs = ecoreDataTypeVO.getEcoreCodeVOs();
				if(ecoreCodeVOs != null && ecoreCodeVOs.size() > 0) {
					for (EcoreCodeVO ecoreCodeVO : ecoreCodeVOs) {
						EcoreCode ecoreCode = ecoreCodeVO.getEcoreCode();
						TreeItem treeItem = new TreeItem(newCodeTree, SWT.NONE);
						treeItem.setText("" + ecoreCode.getName());
						treeItem.setImage(ImgUtil.createImg(ImgUtil.CODE_SET_SUB2));
						treeItem.setData("contentNameText", ecoreCode.getName());
						treeItem.setData("contentDocText", ecoreCode.getDefinition());
						treeItem.setData("contentCodeNameText", ecoreCode.getCodeName());
						treeItem.setData("contentObjectIdentifierText", ecoreCode.getObjectIdentifier());
						treeItem.setData("contentStatusCombo", ecoreCode.getRegistrationStatus());
						treeItem.setData("contentRemovalDate", ecoreCode.getRemovalDate());

						java.util.List<EcoreConstraint> ecoreConstraints = ecoreCodeVO.getEcoreConstraints();
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
						treeItem.setData("constraintsTableItems", constraintsTableItems);
					}
					//默认打开
					TreeItem item = newCodeTree.getItem(0);
					if(item != null) {
						newCodeTree.setSelection(item);
						codeTreeListener(item, rightComposite);
					}
				}
			}
		}
		
		newCodeTree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) e.item;
				selectedTreeItem = item;
				if (item.getData("disableCheckbox") != null && (Boolean) item.getData("disableCheckbox")
						&& e.detail == SWT.CHECK) {
					item.setChecked(!item.getChecked());
				}
				codeTreeListener(item, rightComposite);
			}
		});
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		// ------------ Content Expand Bar End ----------------------------------
	}

	/**
	 * content页面(其它统一)
	 */
	private void createContentPageForOthers() {
		
		ScrolledComposite scrolledComposite = createContentComposite();
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(composite);
		
		Composite leftComposite = new Composite(composite, SWT.BORDER);
		leftComposite.setLayout(new FillLayout(SWT.VERTICAL));
		leftComposite.setBounds(0, 0, 500, 695);
		leftComposite.layout();
		
		Composite rightComposite = new Composite(composite, SWT.BORDER);
		rightComposite.setLayout(new FillLayout(SWT.VERTICAL));
		rightComposite.setBounds(510, 0, 500, 498);
		rightComposite.layout();

		// ------------ Content Expand Bar Begin ----------------------------------
		
		Composite leftCom = new Composite(leftComposite, SWT.NONE);
		leftCom.setBounds(0, 0, 490, 730);
		leftCom.layout();
		
		new SummaryRowTextComposite(leftCom, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 0, I18nApi.get("editor.title.ct")));

		newCodeTree = new Tree(leftCom, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
		newCodeTree.setBounds(5, 35, 485, 650);
		newCodeTree.layout();

		if (ecoreDataTypeVO.getEcoreConstraints() != null && ecoreDataTypeVO.getEcoreConstraints().size() > 0) {
			java.util.List<EcoreConstraint> ecoreConstraints = ecoreDataTypeVO.getEcoreConstraints();
			if (ecoreConstraints != null && ecoreConstraints.size() > 0) {
				for (EcoreConstraint constraint : ecoreConstraints) {
					TreeItem treeItem = new TreeItem(newCodeTree, SWT.NONE);
					treeItem.setText("" + constraint.getName());
					treeItem.setImage(ImgUtil.createImg(ImgUtil.CONSTRAINT));
					treeItem.setData("contentNameText", constraint.getName());
					treeItem.setData("contentDocText", constraint.getDefinition());
					treeItem.setData("contentExpressionText", constraint.getExpression());
					treeItem.setData("contentExpressionLanguageText", constraint.getExpressionlanguage());
					treeItem.setData("contentObjectIdentifierText", constraint.getObjectIdentifier());
				}
				//默认打开
				TreeItem item = newCodeTree.getItem(0);
				if(item != null) {
					newCodeTree.setSelection(item);
					showOtherDetailBar(item, rightComposite);
				}
			}
		}
		
		newCodeTree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 选中可操作
				TreeItem item = (TreeItem) e.item;
				selectedTreeItem = item;
				showOtherDetailBar(item, rightComposite);
			}
		});
		// ------------ Content Expand Bar End ----------------------------------
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
	
	private void codeTreeListener(TreeItem item, Composite rightComposite) {
		showCodesDetail(item, rightComposite);
		if (RegistrationStatusEnum.Registered.getStatus().equals(item.getData("contentStatusCombo"))) {
			setUnavailable(contentNameText);
			setUnavailable(contentDocText);
			setUnavailable(contentCodeNameText);
			setUnavailable(contentObjectIdentifierText);
			setUnavailable(contentRemovalDate);
		} else if (traceId != null) {
			setUnavailable(contentNameText);
			setUnavailable(contentDocText);
			setUnavailable(contentCodeNameText);
			setUnavailable(contentObjectIdentifierText);
			setUnavailable(contentRemovalDate);
		} else {
			rightComposite.setEnabled(true);
			setAvailable(contentNameText);
			setAvailable(contentDocText);
			setAvailable(contentCodeNameText);
			setAvailable(contentObjectIdentifierText);
			setAvailable(contentRemovalDate);
		}
		
		contentConstraintsTable.removeAll();
		@SuppressWarnings("unchecked")
		java.util.List<String[]> tableItems = (java.util.List<String[]>) item.getData("constraintsTableItems");
		if (tableItems != null && tableItems.size() > 0) {
			for (int i = 0; i < tableItems.size(); i++) {
				TableItem tableItem = new TableItem(contentConstraintsTable, SWT.NONE);
				tableItem.setText(tableItems.get(i));
			}
		}
	}
	
	private void showCodesDetail(TreeItem item, Composite rightComposite) {
		for (Control control: rightComposite.getChildren()) {
			control.dispose();
		}
		// ------------ Code Details Begin ------------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 5, I18nApi.get("editor.title.cd")));
		
		String contentName = item.getData("contentNameText") == null ? "" : item.getData("contentNameText") + "";
		contentNameText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 45, "Name", contentName)).getText();
		contentNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (selectedTreeItem != null && !selectedTreeItem.isDisposed()) {
					selectedTreeItem.setData("contentNameText", contentNameText.getText());
					selectedTreeItem.setText(contentNameText.getText() == null ? "" : contentNameText.getText());
					setDirty(true);
				}
			}
		});

		String contentDoc = item.getData("contentDocText") == null ? "" : item.getData("contentDocText") + "";
		contentDocText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SCROLL_TYPE, 73, "Documentation", contentDoc)).getText();
		contentDocText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (selectedTreeItem != null && !selectedTreeItem.isDisposed()) {
					selectedTreeItem.setData("contentDocText", contentDocText.getText());
					setDirty(true);
				}
			}
		});

		String contentCodeName = item.getData("contentCodeNameText") == null ? "" : item.getData("contentCodeNameText") + "";
		contentCodeNameText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 228, "CodeName", contentCodeName)).getText();
		contentCodeNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (selectedTreeItem != null && !selectedTreeItem.isDisposed()) {
					selectedTreeItem.setData("contentCodeNameText", contentCodeNameText.getText());
					setDirty(true);
				}
			}
		});
		// ------------ Code Details End ------------------------

		// -----------------------CMP Information Group Begin -----------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 266, I18nApi.get("editor.title.cmp")));
		
		String contentObjectIdentifier = item.getData("contentObjectIdentifierText") == null ? "" : item.getData("contentObjectIdentifierText") + "";
		contentObjectIdentifierText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 306, "Object Identifier", contentObjectIdentifier)).getText();
		contentObjectIdentifierText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTreeItem.isDisposed()) {
					selectedTreeItem.setData("contentObjectIdentifierText", contentObjectIdentifierText.getText());
					setDirty(true);
				}
			}
		});
		// -----------------------CMP Information Group End -----------------------

		// ----------------------- Registration Information Group Begin -----------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 344, I18nApi.get("editor.title.ri")));
		
		String contentStatusCombo = item.getData("contentObjectIdentifierText") == null ? "" : item.getData("contentObjectIdentifierText") + "";
		statusCombo = new SummaryRowComboComposite(rightComposite, new ComboPolicy(ComboPolicy.COMBO_CONTENT_TYPE, 384, "Registration Status", false, contentStatusCombo)).getCombo();
		statusCombo.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTreeItem.isDisposed()) {
					selectedTreeItem.setData("contentStatusCombo", statusCombo.getText());
					setDirty(true);
				}
			}
		});

		String objectIdentifier = item.getData("contentObjectIdentifierText") == null ? "" : item.getData("contentObjectIdentifierText") + "";
		contentObjectIdentifierText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 412, "Object Identifier", objectIdentifier)).getText();

		String removalDate = item.getData("contentRemovalDate") == null ? "" : item.getData("contentRemovalDate") + "";
		contentRemovalDate = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 436, "Removal Date", false, removalDate)).getText();
		contentRemovalDate.setToolTipText("Date Format: MM/DD/YY or YYYY-MM-DD");
		contentRemovalDate.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTreeItem.isDisposed()) {
					selectedTreeItem.setData("contentRemovalDate", contentRemovalDate.getText());
					setDirty(true);
				}
			}
		});
		// ----------------------- Registration Information Group End

		// ---------- Constraints Expand Item Begin
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 474, I18nApi.get("editor.title.cs")));
		
		contentObjectIdentifierText = new SummaryRowTextComposite(rightComposite, 
				new TextPolicy(TextPolicy.TEXT_CONTENT_NOTE_TWO_TYPE, 514, "All the constraints contained in this object(other constraints - such as constraints \r\n"
						+ "defined on type - may also apply).")).getText();

		Button addButtonForConstraints = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR))).getButton();
		addButtonForConstraints.setBounds(10, 559, 30, 30);
		addButtonForConstraints.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MrEditorDialogCreator.createConstraintDialogue("add", "1", contentConstraintsTable, newCodeTree, dataType, selectedTreeItem);
				setDirty(true);
			}
		});

		Button deleteButtonForConstraint = new SummaryRowTextComposite(rightComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES))).getButton();
		deleteButtonForConstraint.setBounds(42, 559, 30, 30);
		deleteButtonForConstraint.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MrEditorDialogCreator.deleteConstraint("1", contentConstraintsTable, newCodeTree, dataType, selectedTreeItem);
				setDirty(true);
			}
		});

		contentConstraintsTable = new Table(rightComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		contentConstraintsTable.setHeaderVisible(true);
		contentConstraintsTable.setLinesVisible(true);
		contentConstraintsTable.setBounds(10, 594, 480, 150);
		contentConstraintsTable.layout();
		new SummaryTableComposite(contentConstraintsTable, 113, "Name");
		new SummaryTableComposite(contentConstraintsTable, 113, "Definition");
		new SummaryTableComposite(contentConstraintsTable, 137, "Expression Language");
		new SummaryTableComposite(contentConstraintsTable, 113, "Expression");
		// ---------- Constraints Expand Item End -------------------------------------
	}
	
	private void showOtherDetailBar(TreeItem item, Composite rightComposite) {
		for (Control control: rightComposite.getChildren()) {
			control.dispose();
		}
		
		// ------------ Code Details Begin ------------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 5, I18nApi.get("editor.title.gi")));
		
		String contentName = item.getData("contentNameText") == null ? "" : item.getData("contentNameText") + "";
		contentNameText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 45, "Name", contentName)).getText();
		contentNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (selectedTreeItem != null && !selectedTreeItem.isDisposed()) {
					selectedTreeItem.setData("contentNameText", contentNameText.getText());
					selectedTreeItem.setText(contentNameText.getText() == null ? "" : contentNameText.getText());
					setDirty(true);
				}
			}
		});
		this.setUnavailable(contentNameText);
		
		String contentDoc = item.getData("contentDocText") == null ? "" : item.getData("contentDocText") + "";
		contentDocText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SCROLL_TYPE, 73, "Documentation", contentDoc)).getText();
		contentDocText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (selectedTreeItem != null && !selectedTreeItem.isDisposed()) {
					selectedTreeItem.setData("contentDocText", contentDocText.getText());
					setDirty(true);
				}
			}
		});
		this.setUnavailable(contentDocText);
		
		String contentExpression = item.getData("contentExpressionText") == null ? "" : item.getData("contentExpressionText") + "";
		contentExpressionText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_SCROLL_TYPE, 228, "Expression", contentExpression)).getText();
		contentExpressionText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (selectedTreeItem != null && !selectedTreeItem.isDisposed()) {
					selectedTreeItem.setData("contentExpressionText", contentExpressionText.getText());
					setDirty(true);
				}
			}
		});
		this.setUnavailable(contentExpressionText);
		
		String contentExpressionLanguage = item.getData("contentExpressionLanguageText") == null ? "" : item.getData("contentExpressionLanguageText") + "";
		contentExpressionLanguageText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 383, "Expression Language", contentExpressionLanguage)).getText();
		contentExpressionLanguageText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (selectedTreeItem != null && !selectedTreeItem.isDisposed()) {
					selectedTreeItem.setData("contentExpressionLanguageText", contentExpressionLanguageText.getText());
					setDirty(true);
				}
			}
		});
		this.setUnavailable(contentExpressionLanguageText);
		// ------------ Code Details End ------------------------

		// -----------------------CMP Information Group Begin -----------------------
		new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 421, I18nApi.get("editor.title.cmp")));
		
		String contentObjectIdentifier = item.getData("contentObjectIdentifierText") == null ? "" : item.getData("contentObjectIdentifierText") + "";
		contentObjectIdentifierText = new SummaryRowTextComposite(rightComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_COMMONLY_TYPE, 461, "Object Identifier", contentObjectIdentifier)).getText();
		contentObjectIdentifierText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (selectedTreeItem != null && !selectedTreeItem.isDisposed()) {
					selectedTreeItem.setData("contentObjectIdentifierText", contentObjectIdentifierText.getText());
					setDirty(true);
				}
			}
		});
		this.setUnavailable(contentObjectIdentifierText);
		// -----------------------CMP Information Group End -----------------------
	}
	
	private void createImpactPage() {
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
		groupGridData.heightHint = 200;

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
		MrEditorTableCreator.generateMessageComponentTableOfImpactPage(messageComponentTable,
				ecoreMessageComponentList);

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
				// 递归生成contrainment tree on Impact page
				MrEditorTreeCreator mrEditorTreeCreator = new MrEditorTreeCreator();
				mrEditorTreeCreator.generateContainmentTreeForMessageSetofImpactPage(treeRoot, MrImplManager.get().getEcoreMessageDefinitionImpl().findByMsgSetId(messageSetId));
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
				treeRoot.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_COMPONENT));
				treeRoot.removeAll();
				// 递归生成contrainment tree on Impact page
				MrEditorTreeCreator mrEditorTreeCreator = new MrEditorTreeCreator();
				mrEditorTreeCreator.generateContainmentTreeForMessageComponent(treeRoot,
						MrHelper.getMsgElementList(msgComponentId));
				// 加载Constraint节点
				java.util.List<EcoreConstraint> ecs = MrHelper.findConstraintsByObjId(css, msgComponentId);
				mrEditorTreeCreator.addConstraintsNode(treeRoot, ecs, null, null);
				treeRoot.setExpanded(true);
			}
		});
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	private void createIncomingAssociationsPage() {
		Composite composite = new Composite(getContainer(), SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		int index = addPage(composite);
		setPageText(index, I18nApi.get("editor.label.iaa"));
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

		Table elementTable = new Table(composite, SWT.BORDER | SWT.V_SCROLL);
		elementTable.setLinesVisible(true);
		GridData elementTableGridData = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		elementTableGridData.widthHint = 500;
		elementTableGridData.heightHint = 700;
		elementTable.setLayoutData(elementTableGridData);
		this.generateImpactedElementOfIncomingPage(elementTable);
	}

	private void generateImpactedElementOfIncomingPage(Table elementTable) {
		for (EcoreBusinessComponent bizComponent : imcomingAssociationList) {
			TableItem tableItem1 = new TableItem(elementTable, SWT.NONE);
			tableItem1.setText(bizComponent.getName());
			tableItem1.setData("bizComponentId", bizComponent.getId());
			tableItem1.setImage(ImgUtil.createImg(ImgUtil.BC));
		}
	}

//	private void createVersionSubset() {
//
//		Composite parentComposite = getContainer();
//		ScrolledComposite scrolledComposite = new ScrolledComposite(parentComposite, SWT.V_SCROLL | SWT.H_SCROLL);
//		scrolledComposite.setBounds(parentComposite.getShell().getBounds());
//
//		GridData sc_gd = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
//		scrolledComposite.setLayoutData(sc_gd);
//
//		Composite composite = new Composite(scrolledComposite, SWT.NONE);
//		scrolledComposite.setContent(composite);
//		scrolledComposite.setExpandHorizontal(true);
//		scrolledComposite.setExpandVertical(true);
//		int index = addPage(scrolledComposite);
//		setPageText(index, I18nApi.get("editor.label.vs"));
//
//		composite.setLayout(new GridLayout(2, false));
//		GridData gd = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
//		gd.widthHint = scrolledComposite.getBounds().width;
//		composite.setLayoutData(gd);
//
//		Composite leftComposite = new Composite(composite, SWT.NONE);
//		leftComposite.setLayout(new FillLayout(SWT.VERTICAL));
//		GridData lc_gd = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
//		lc_gd.widthHint = 550; // composite.getBounds().width / 3;
//		leftComposite.setLayoutData(lc_gd);
//
//		GridData rc_gd = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
//		rc_gd.widthHint = 500; // composite.getBounds().width / 3;
//		Composite rightComposite = new Composite(composite, SWT.NONE);
//		rightComposite.setLayout(new FillLayout(SWT.VERTICAL));
//		rightComposite.setLayoutData(rc_gd);
//
//		ExpandBar nextVersionBar = new SummaryExpandComposite(leftComposite, 0).getExpandBar();
//
//		Composite nextVersionBarComposite = new Composite(nextVersionBar, SWT.NONE);
//		GridData nextVersionBarCompositeGridData = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
//		nextVersionBarComposite.setLayout(new GridLayout(1, false));
//		nextVersionBarComposite.setLayoutData(nextVersionBarCompositeGridData);
//
//		Label leftDescriptionLabel = new Label(nextVersionBarComposite, SWT.WRAP);
//		leftDescriptionLabel.setText("Shows the new versions of this repository itme.");
//		List versionListWidget = new List(nextVersionBarComposite, SWT.BORDER);
//		GridData versionListGridData = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
//		versionListGridData.widthHint = 500;
//		versionListWidget.setLayoutData(versionListGridData);
//		this.generateNextDataTypeVersionList(versionListWidget);
//
//		new SummaryExpandComposite(nextVersionBar, nextVersionBarComposite, I18nApi.get("editor.title.nv"), 450);
//
//		ExpandBar previousVersionBar = new SummaryExpandComposite(rightComposite, 0).getExpandBar();
//
//		Composite previousVersionComposite = new Composite(previousVersionBar, SWT.NONE);
//		previousVersionComposite.setLayout(new GridLayout(1, false));
//		Label rightDescriptionLabel = new Label(previousVersionComposite, SWT.WRAP);
//		rightDescriptionLabel.setText("Shows the previous version of this repository item.");
//		Label previousVersionLabel = new Label(previousVersionComposite, SWT.WRAP);
//		previousVersionLabel.setText("The previous version of this object is: ");
//		Link previousVersionLink = new Link(previousVersionComposite, SWT.NONE);
//		previousVersionLink.setText(previousEcoreDataType.getName() == null ? "" : previousEcoreDataType.getName());
//		previousVersionLink.setData(previousEcoreDataType.getId());
//
//		new SummaryExpandComposite(previousVersionBar, previousVersionComposite, I18nApi.get("editor.title.pv"), 450);
//	}
//
//	private void generateNextDataTypeVersionList(List versionListWidget) {
//		for (int index = 0; index < this.nextDataTypeList.size(); index++) {
//			EcoreDataType ecoreDataType = nextDataTypeList.get(index);
//			versionListWidget.add(ecoreDataType.getName());
//			versionListWidget.setData(String.valueOf(index), ecoreDataType.getId());
//		}
//	}

	/**
	 * 获取所有信息
	 * 
	 * @return
	 */
	public EcoreDataTypeVO getAllData() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		EcoreDataTypeVO dataTypeVO = new EcoreDataTypeVO();

		// 获取基本信息
		EcoreDataType ecoreDataType = new EcoreDataType();
		ecoreDataType.setId(ecoreDataTypeId);
		DataTypesEnum[] dataTypesEnums = DataTypesEnum.values();
		for (DataTypesEnum dataTypesEnum : dataTypesEnums) {
			if (dataTypesEnum.getName().equals(this.dataType)) {
				ecoreDataType.setType(dataTypesEnum.getType());
				break;
			}
		}

		ecoreDataType.setName(this.nameText == null ? null : this.nameText.getText());
		ecoreDataType.setDefinition(this.documentationText == null ? null : this.documentationText.getText());
		ecoreDataType.setMinLength(this.minLengthText == null ? null : NumberUtil.getInt(this.minLengthText.getText()));
		ecoreDataType.setMaxLength(this.maxLengthText == null ? null : NumberUtil.getInt(this.maxLengthText.getText()));
		ecoreDataType.setLength(this.lengthText == null ? null : NumberUtil.getInt(this.lengthText.getText()));
		ecoreDataType.setPattern(this.patternText == null ? null : this.patternText.getText());
		ecoreDataType.setIdentificationScheme(this.identificationSchemeText == null ? null : this.identificationSchemeText.getText());
		ecoreDataType.setMeaningWhenTrue(this.meaningWhenTrueText == null ? null : this.meaningWhenTrueText.getText());
		ecoreDataType.setMeaningWhenFalse(this.meaningWhenFalseText == null ? null : this.meaningWhenFalseText.getText());
		ecoreDataType.setMinInclusive(this.minInclusiveText == null ? null : this.minInclusiveText.getText());
		ecoreDataType.setMinExclusive(this.minExclusiveText == null ? null : this.minExclusiveText.getText());
		ecoreDataType.setMaxInclusive(this.maxInclusiveText == null ? null : this.maxInclusiveText.getText());
		ecoreDataType.setMaxExclusive(this.maxExclusiveText == null ? null : this.maxExclusiveText.getText());
		ecoreDataType.setTotalDigits(this.totalDigitsText == null ? null : NumberUtil.getInt(this.totalDigitsText.getText()));
		ecoreDataType.setFractionDigits(this.fractionDigitsText == null ? null : NumberUtil.getInt(this.fractionDigitsText.getText()));
		ecoreDataType.setBaseValue(this.baseValueText == null ? null : NumberUtil.getDouble(this.baseValueText.getText()));
		ecoreDataType.setBaseUnitCode(this.baseUnitCodeText == null ? null : this.baseUnitCodeText.getText());
		ecoreDataType.setNamespace(this.namespaceCombo == null ? null : this.namespaceCombo.getText());
		ecoreDataType.setNamespaceList(this.namespaceListText == null ? null : this.namespaceListText.getText());
		ecoreDataType.setProcessContents(this.processContentCombo == null ? null : this.processContentCombo.getText());
		ecoreDataType.setObjectIdentifier(this.objectIdentifierValue.getText());
		ecoreDataType.setRegistrationStatus(statusCombo.getText());
		ecoreDataType.setIsfromiso20022(ecoreDataTypeVO.getEcoreDataType() != null ? ecoreDataTypeVO.getEcoreDataType().getIsfromiso20022() : null);
		Date removalDate;
		try {
			removalDate = (summaryRemovalDate.getText() == null ? null : sdf.parse(summaryRemovalDate.getText()));
		} catch (ParseException e) {
			removalDate = null;
		}
		ecoreDataType.setRemovalDate(removalDate);

		dataTypeVO.setEcoreDataType(ecoreDataType);

		// 获取examples信息
		java.util.List<EcoreExample> examples = new ArrayList<EcoreExample>();
		String[] exampleItems = this.examplesList.getItems();
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) examplesList.getData("map");
		if (exampleItems != null && exampleItems.length > 0) {
			for (String str : exampleItems) {
				EcoreExample ecoreExample = new EcoreExample();
				if (map != null) {
					ecoreExample.setId(map.get(str) == null ? UUID.randomUUID().toString() : map.get(str));
				} else {
					ecoreExample.setId(UUID.randomUUID().toString());
				}
				ecoreExample.setExample(str);
				ecoreExample.setObjId(ecoreDataType.getId());
				ecoreExample.setObjType(ObjTypeEnum.DataType.getType());
				examples.add(ecoreExample);
			}
		}
		dataTypeVO.setEcoreExamples(examples);

		// 获取constraints
		java.util.List<EcoreConstraint> constraints = new ArrayList<EcoreConstraint>();
		TableItem[] constraintItems = summaryConstraintsTable.getItems();
		if (constraintItems != null && constraintItems.length > 0) {
			for (TableItem tableItem : constraintItems) {
				EcoreConstraint ecoreConstraint = new EcoreConstraint();
				ecoreConstraint.setId(
						ecoreConstraint.getId() == null ? UUID.randomUUID().toString() : ecoreConstraint.getId());
				ecoreConstraint.setObj_id(ecoreDataType.getId());
				ecoreConstraint.setObj_type(ecoreDataType.getType());
				ecoreConstraint.setName(tableItem.getText(0));
				ecoreConstraint.setDefinition(tableItem.getText(1));
				ecoreConstraint.setExpressionlanguage(tableItem.getText(2));
				ecoreConstraint.setExpression(tableItem.getText(3));
				constraints.add(ecoreConstraint);
			}
		}
		dataTypeVO.setEcoreConstraints(constraints);

		java.util.List<EcoreCodeVO> ecoreCodeVOs = new ArrayList<EcoreCodeVO>();
		TreeItem[] treeItems = newCodeTree.getItems();

		// Derived Code Set
		if (this.traceId != null) {
			ecoreDataType.setTrace(this.traceId);
			for (TreeItem treeItem : treeItems) {
				if (!treeItem.getChecked()) {
					continue;
				}
				String contentNameText = treeItem.getData("contentNameText") == null ? "" : treeItem.getData("contentNameText") + "";
				String contentDocText = treeItem.getData("contentDocText") == null ? "" : treeItem.getData("contentDocText") + "";
				String contentCodeNameText = treeItem.getData("contentCodeNameText") == null ? "" : treeItem.getData("contentCodeNameText") + "";
				String contentObjectIdentifierText = treeItem.getData("contentObjectIdentifierText") == null ? "" : treeItem.getData("contentObjectIdentifierText") + "";
				String contentStatusCombo = treeItem.getData("contentStatusCombo") == null ? "" : treeItem.getData("contentStatusCombo") + "";
				Date contentRemovalDate;
				try {
					contentRemovalDate = treeItem.getData("contentRemovalDate") == null ? null : sdf.parse(treeItem.getData("contentRemovalDate") + "");
				} catch (ParseException e) {
					contentRemovalDate = null;
				}
				EcoreCodeVO ecoreCodeVO = new EcoreCodeVO();
				if (ecoreDataType.getType().equals(DataTypesEnum.CODE_SETS.getType())) {
					EcoreCode ecoreCode = new EcoreCode();
					ecoreCode.setId(ecoreCode.getId() == null ? UUID.randomUUID().toString() : ecoreCode.getId());
					ecoreCode.setName(contentNameText);
					ecoreCode.setDefinition(contentDocText);
					ecoreCode.setCodeName(contentCodeNameText);
					ecoreCode.setObjectIdentifier(contentObjectIdentifierText);
					ecoreCode.setRegistrationStatus(contentStatusCombo);
					ecoreCode.setRemovalDate(contentRemovalDate);
					ecoreCode.setCodesetid(ecoreDataType.getId());
					ecoreCodeVO.setEcoreCode(ecoreCode);
					// constraints
					java.util.List<EcoreConstraint> contentConstraints = new ArrayList<EcoreConstraint>();
					TableItem[] contentConstraintItems = contentConstraintsTable.getItems();
					if (contentConstraintItems != null && contentConstraintItems.length > 0) {
						for (TableItem tableItem : contentConstraintItems) {
							EcoreConstraint contentEcoreConstraint = new EcoreConstraint();
							contentEcoreConstraint.setId(contentEcoreConstraint.getId() == null ? UUID.randomUUID().toString(): contentEcoreConstraint.getId());
							contentEcoreConstraint.setObj_id(ecoreCode.getId());
							contentEcoreConstraint.setObj_type(ObjTypeEnum.DataType_CodeSet.getType());
							contentEcoreConstraint.setName(tableItem.getText(0));
							contentEcoreConstraint.setDefinition(tableItem.getText(1));
							contentEcoreConstraint.setExpressionlanguage(tableItem.getText(2));
							contentEcoreConstraint.setExpression(tableItem.getText(3));
							contentConstraints.add(contentEcoreConstraint);
						}
					}
					ecoreCodeVO.setEcoreConstraints(contentConstraints);
					ecoreCodeVOs.add(ecoreCodeVO);
				}
			}
		} else {
			for (TreeItem treeItem : treeItems) {
				String contentNameText = treeItem.getData("contentNameText") == null ? "" : treeItem.getData("contentNameText") + "";
				String contentDocText = treeItem.getData("contentDocText") == null ? "" : treeItem.getData("contentDocText") + "";
				String contentCodeNameText = treeItem.getData("contentCodeNameText") == null ? "" : treeItem.getData("contentCodeNameText") + "";
				String contentObjectIdentifierText = treeItem.getData("contentObjectIdentifierText") == null ? "" : treeItem.getData("contentObjectIdentifierText") + "";
				String contentStatusCombo = treeItem.getData("contentStatusCombo") == null ? "" : treeItem.getData("contentStatusCombo") + "";
				Date contentRemovalDate;
				try {
					contentRemovalDate = treeItem.getData("contentRemovalDate") == null ? null : sdf.parse(treeItem.getData("contentRemovalDate") + "");
				} catch (ParseException e) {
					contentRemovalDate = null;
				}
				EcoreCodeVO ecoreCodeVO = new EcoreCodeVO();
				if (ecoreDataType.getType().equals(DataTypesEnum.CODE_SETS.getType())) {
					EcoreCode ecoreCode = new EcoreCode();
					ecoreCode.setId(ecoreCode.getId() == null ? UUID.randomUUID().toString() : ecoreCode.getId());
					ecoreCode.setName(contentNameText);
					ecoreCode.setDefinition(contentDocText);
					ecoreCode.setCodeName(contentCodeNameText);
					ecoreCode.setObjectIdentifier(contentObjectIdentifierText);
					ecoreCode.setRegistrationStatus(contentStatusCombo);
					ecoreCode.setRemovalDate(contentRemovalDate);
					ecoreCode.setCodesetid(ecoreDataType.getId());
					ecoreCodeVO.setEcoreCode(ecoreCode);
					// constraints
					java.util.List<EcoreConstraint> contentConstraints = new ArrayList<EcoreConstraint>();
					TableItem[] contentConstraintItems = contentConstraintsTable.getItems();
					if (contentConstraintItems != null && contentConstraintItems.length > 0) {
						for (TableItem tableItem : contentConstraintItems) {
							EcoreConstraint contentEcoreConstraint = new EcoreConstraint();
							contentEcoreConstraint.setId(contentEcoreConstraint.getId() == null ? UUID.randomUUID().toString() : contentEcoreConstraint.getId());
							contentEcoreConstraint.setObj_id(ecoreCode.getId());
							contentEcoreConstraint.setObj_type(ObjTypeEnum.DataType_CodeSet.getType());
							contentEcoreConstraint.setName(tableItem.getText(0));
							contentEcoreConstraint.setDefinition(tableItem.getText(1));
							contentEcoreConstraint.setExpressionlanguage(tableItem.getText(2));
							contentEcoreConstraint.setExpression(tableItem.getText(3));
							contentConstraints.add(contentEcoreConstraint);
						}
					}
					ecoreCodeVO.setEcoreConstraints(contentConstraints);
					ecoreCodeVOs.add(ecoreCodeVO);
				}
			}
		}
		dataTypeVO.setEcoreCodeVOs(ecoreCodeVOs);
		dataTypeVO.setEcoreNextVersions(null);
		return dataTypeVO;
	}

	@Override
	public void setDirty(boolean dirty) {

		if (dirty && ecoreDataTypeVO.getEcoreDataType() == null) {
			super.setDirty(dirty);
		}else if (ecoreDataTypeVO.getEcoreDataType() != null) {
			if (dirty && RegistrationStatusEnum.Registered.getStatus().equals(ecoreDataTypeVO.getEcoreDataType().getRegistrationStatus())) {
				if ("1".equals(ecoreDataTypeVO.getEcoreDataType().getType()) && ecoreDataTypeVO.getEcoreDataType().getTrace() == null) { // 是Code Set，并且是 可衍生的: 可编辑
					super.setDirty(true);
				} else {
					super.setDirty(false);
				}
			} else { // 临时注册和新增可以编辑
				super.setDirty(dirty);
			}
		} else {
			super.setDirty(dirty);
		}
	}

	private void setUnavailable(Widget swtWidget) {
		if (swtWidget instanceof Text) {
			((Text) swtWidget).setEditable(false);
		} else if (swtWidget instanceof Button) {
			((Button) swtWidget).setEnabled(false);
		}
	}

	private void setAvailable(Widget swtWidget) {
		if (swtWidget instanceof Text) {
			((Text) swtWidget).setEditable(true);
		} else if (swtWidget instanceof Button) {
			((Button) swtWidget).setEnabled(true);
		}
	}
}
