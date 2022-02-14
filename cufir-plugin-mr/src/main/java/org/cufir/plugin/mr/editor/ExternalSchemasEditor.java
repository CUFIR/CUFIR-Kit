package org.cufir.plugin.mr.editor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.map.HashedMap;
import org.cufir.plugin.mr.MrHelper;
import org.cufir.plugin.mr.bean.ButtonPolicy;
import org.cufir.plugin.mr.bean.ComboPolicy;
import org.cufir.plugin.mr.bean.ObjTypeEnum;
import org.cufir.plugin.mr.bean.RegistrationStatusEnum;
import org.cufir.plugin.mr.bean.TextPolicy;
import org.cufir.plugin.mr.handlers.SaveHandler;
import org.cufir.plugin.mr.utils.ImgUtil;
import org.cufir.plugin.mr.utils.SystemUtil;
import org.cufir.s.data.bean.EcoreConstraint;
import org.cufir.s.data.bean.EcoreExample;
import org.cufir.s.data.bean.EcoreExternalSchema;
import org.cufir.s.data.bean.EcoreMessageBuildingBlock;
import org.cufir.s.data.bean.EcoreMessageComponent;
import org.cufir.s.data.bean.EcoreMessageDefinition;
import org.cufir.s.data.bean.EcoreMessageSet;
import org.cufir.s.data.bean.EcoreNamespaceList;
import org.cufir.s.data.vo.EcoreExternalSchemaVO;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import com.cfets.cufir.s.ide.utils.i18n.I18nApi;

/**
 * ExternalSchemas（自定义数据类型）编辑和显示
 * @author hrj
 *
 */
public class ExternalSchemasEditor extends MrMultiPageEditor {

	private java.util.List<EcoreConstraint> css = MrRepository.get().ecoreConstraints;
	private java.util.List<EcoreMessageBuildingBlock> mbbs =  MrRepository.get().ecoreMessageBuildingBlocks;
	
	private int width;
	private int height;

	// 基本数据类型
	private String dataType;
	private MrEditorInput myEditorInput;

	private TreeItem modelExploreTreeItem;

	private Text nameText, docText, btText, oiText, rdText;

	private Combo pcCombo, rmCombo;

	private Button technicalButton,btBtn,addNamespaceListBtn,deleteNamespaceListBtn,addExamplesBtn,deleteExamplesBtn,addConstrainBtn,editConstrainBtn,deleteConstraintBtn;

	private List namespaceList, examplesList;

	private String externalSchemaId;

	private Table constraintsTable;

	private EcoreExternalSchemaVO ecoreExternalSchemaVO;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	// ----------For impact page -------------
	private Table messageSetTable;
	private Table messageDefinitionTable;
	private Table messageComponentTable;

	// ---------For Version and Subset Page
	private EcoreExternalSchema previousEcoreExternalSchema = new EcoreExternalSchema();
	private java.util.List<EcoreExternalSchema> externalSchemaNextVersionList = new ArrayList<>();
	private ArrayList<EcoreMessageComponent> ecoreMessageComponentList = new ArrayList<>();
	private java.util.List<EcoreMessageDefinition> ecoreMsgDefinitonList = new ArrayList<>();
	private java.util.List<EcoreMessageSet> ecoreMessageSetList = new ArrayList<>();

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		this.setSite(site);
		this.setInput(input);
		// 设置标题
		this.setPartName("");
		this.setPartProperty("customName", "externalSchemasCreate");
		this.myEditorInput = (MrEditorInput) input;
		this.dataType = this.myEditorInput.getTransferDataBean().getType();
		modelExploreTreeItem = this.myEditorInput.getTransferDataBean().getTreeListItem();
		this.externalSchemaId = this.myEditorInput.getTransferDataBean().getId();
		this.setPartProperty("ID", this.externalSchemaId);
		initFilledData();
		// ctrl + s保存必须要
		setContext(site);
	}

	/**
	 * 初始化编辑的值
	 */
	private void initFilledData() {
		// 不为null代表是打开的,可编辑
		try {
			ecoreExternalSchemaVO = MrImplManager.get().getEcoreExternalSchemaImpl().findExternalSchemaVO(externalSchemaId);
			if (ecoreExternalSchemaVO != null) {
				ecoreMessageComponentList = MrHelper.getMessageComponentListByExternalSchemaId(externalSchemaId);
				ecoreMsgDefinitonList = MrImplManager.get().getEcoreMessageDefinitionImpl().findByMsgComponents(ecoreMessageComponentList);//MrHelper.getMessageDefinitionList(ecoreMessageComponentList);
				ecoreMessageSetList = MrImplManager.get().getEcoreMessageSetImpl().findByMsgDefinitions(ecoreMsgDefinitonList);//MrHelper.getMessageSetList(ecoreMsgDefinitonList);
				previousEcoreExternalSchema = MrHelper.getPreviousVersionExternalSchema(externalSchemaId);
				java.util.List<String> nextVersionIds = MrImplManager.get().getEcoreNextVersionsImpl().findNextVersionIdById(externalSchemaId);
				externalSchemaNextVersionList = MrHelper.getEcoreExternalSchemaListByIds(nextVersionIds);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ecoreExternalSchemaVO = new EcoreExternalSchemaVO();
	}

	@Override
	public void setPartName(String name) {
		super.setPartName(name);
	}

	@Override
	protected void createPages() {
		width = this.getContainer().getShell().getBounds().width - 250;
		height = this.getContainer().getShell().getBounds().height;
		createSummaryPage();
		createImpactPage();
		createVersionAndSubsetsPage();
		changeStatus();
		
		// 不为null代表是打开的,可编辑
		if (ecoreExternalSchemaVO.getEcoreExternalSchema() != null) {
			this.setPartName(ecoreExternalSchemaVO.getEcoreExternalSchema().getName());
			if (RegistrationStatusEnum.Registered.getStatus().equals(ecoreExternalSchemaVO.getEcoreExternalSchema().getRegistrationStatus())) {
				this.setDirty(false);
			} else {
				this.setDirty(true);
			}
		} else {
			this.setPartName("");
			this.setDirty(true);
		}		
	}

	private void createSummaryPage() {
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
		
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_TITLE_TYPE, 5, I18nApi.get("editor.title.gi")));
		
		new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_NOTE_TYPE, 837, "This section describes general information about this repository item."));
		
		String name = "";
		if (ecoreExternalSchemaVO.getEcoreExternalSchema() != null) {
			name = ecoreExternalSchemaVO.getEcoreExternalSchema().getName()==null?"":ecoreExternalSchemaVO.getEcoreExternalSchema().getName();
		}
		
		nameText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_COMMONLY_TYPE, 45, "Name", name)).getText();
		nameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setPartName(nameText.getText());
				// 左边的树的名称展示
				modelExploreTreeItem.setText(nameText.getText());
				setPartName(nameText.getText());
				setDirty(true);
			}
		});

		String documentation = "";
		if (ecoreExternalSchemaVO.getEcoreExternalSchema() != null) {
			documentation = ecoreExternalSchemaVO.getEcoreExternalSchema().getDefinition()==null?"":ecoreExternalSchemaVO.getEcoreExternalSchema().getDefinition();
		}
		docText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_SCROLL_TYPE, 73, "Documentation", documentation)).getText();
		docText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});

		String pc = "";
		if (ecoreExternalSchemaVO.getEcoreExternalSchema() != null) {
			pc = ecoreExternalSchemaVO.getEcoreExternalSchema().getProcessContent()==null?"":ecoreExternalSchemaVO.getEcoreExternalSchema().getProcessContent();
		}
		java.util.List<String> list = new ArrayList<>();
		list.add("lax");
		list.add("skip");
		list.add("strict");
		pcCombo = new SummaryRowComboComposite(summaryComposite, new ComboPolicy(ComboPolicy.COMBO_COMMONLY_TYPE, 719,"Process Contents", false, list, pc)).getCombo();
		pcCombo.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});

		technicalButton = new SummaryRowTextComposite(summaryComposite, new TextPolicy(ButtonPolicy.BUTTON_SUMMARY_TYPE_CHECK, 73, "Technical", documentation)).getButton();
		if (ecoreExternalSchemaVO.getEcoreExternalSchema() != null) {
			if (ecoreExternalSchemaVO.getEcoreExternalSchema().getTechnical() != null) {
				technicalButton.setSelection(true);
			}
		}
		technicalButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
			}
		});

		btText = new SummaryRowTextComposite(summaryComposite, new TextPolicy(TextPolicy.TEXT_CONTENT_BUTTON_SELECT_TYPE, 548, "Business Trace", false, "")).getText();
		
		btBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_PUSH_DEEP,"choose")).getButton();
		btBtn.setBounds(390, 312, 50, 25);
		btBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					setDirty(true);
					MrEditorDialogCreator.createSelectBusinessComponentDialog(btText);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		Button btSearchBtn = new SummaryRowTextComposite(summaryComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_DEEP,ImgUtil.createImg(ImgUtil.SEARCH_BTN))).getButton();
		btSearchBtn.setBounds(390, 312, 50, 25);
		
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	private void addSummaryPageLeftComposite(Composite parent) {

		Composite leftComposite = new Composite(parent, SWT.NONE);
		leftComposite.setLayoutData(new GridData(width / 2, height));

		Composite composite = new Composite(leftComposite, SWT.NONE);
		composite.setSize(width / 2, height);

		ExpandBar summaryBar = new SummaryExpandComposite(composite, 0).getExpandBar();
		summaryBar.setSize(width / 2, height);

		addGeneralInformation(summaryBar);
		addNameSpaceList(summaryBar);
		addCMPInformation(summaryBar);

	}

	private void addSummaryPageRightComposite(Composite parent) {
		Composite rightComposite = new Composite(parent, SWT.NONE);
		rightComposite.setLayoutData(new GridData(width / 2, height));

		Composite composite = new Composite(rightComposite, SWT.NONE);
		composite.setSize(width / 2, height);

		ExpandBar summaryBar = new SummaryExpandComposite(composite, 0).getExpandBar();
		summaryBar.setSize(width / 2, height);

		addRegistrationInformation(summaryBar);
		addExamples(summaryBar);
		addConstraints(summaryBar);
	}

	private void addGeneralInformation(ExpandBar summaryBar) {
		int swidht = width / 2 - 150;
		Composite generalInformationComposite = new Composite(summaryBar, SWT.NONE);
		Label desLb = new Label(generalInformationComposite, SWT.LEFT);
		desLb.setBounds(10, 2, width, 15);
		desLb.setText("This section describes general information about this repository item.");

		Label nameLb = new Label(generalInformationComposite, SWT.LEFT);
		nameLb.setBounds(10, 20, 100, 25);
		nameLb.setText("Name");
		nameText = new Text(generalInformationComposite, SWT.BORDER);
		nameText.setBounds(110, 20, swidht, 25);
		if (ecoreExternalSchemaVO.getEcoreExternalSchema() != null) {
			nameText.setText("" + ecoreExternalSchemaVO.getEcoreExternalSchema().getName()==null?"":ecoreExternalSchemaVO.getEcoreExternalSchema().getName());
		}
		nameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setPartName(nameText.getText());
				// 左边的树的名称展示
				modelExploreTreeItem.setText(nameText.getText());
				setPartName(nameText.getText());
				setDirty(true);
			}
		});

		Label docLb = new Label(generalInformationComposite, SWT.LEFT);
		docLb.setBounds(10, 50, 100, 25);
		docLb.setText("Documentation");
		docText = new Text(generalInformationComposite, SWT.BORDER);
		docText.setBounds(110, 50, swidht, 100);
		if (ecoreExternalSchemaVO.getEcoreExternalSchema() != null) {
			docText.setText("" + ecoreExternalSchemaVO.getEcoreExternalSchema().getDefinition()==null?"":ecoreExternalSchemaVO.getEcoreExternalSchema().getDefinition());
		}
		docText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});

		Label pcLb = new Label(generalInformationComposite, SWT.LEFT);
		pcLb.setBounds(10, 160, 100, 25);
		pcLb.setText("Process Contents");
		pcCombo = new Combo(generalInformationComposite, SWT.BORDER);
		pcCombo.add("lax");
		pcCombo.add("skip");
		pcCombo.add("strict");
		pcCombo.setBounds(110, 160, 80, 25);
		if (ecoreExternalSchemaVO.getEcoreExternalSchema() != null) {
			pcCombo.setText("" + ecoreExternalSchemaVO.getEcoreExternalSchema().getProcessContent()==null?"":ecoreExternalSchemaVO.getEcoreExternalSchema().getProcessContent());
		}
		pcCombo.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});

		Label tLb = new Label(generalInformationComposite, SWT.LEFT);
		tLb.setBounds(10, 190, 100, 25);
		tLb.setText("Technical");
		technicalButton = new SummaryRowTextComposite(generalInformationComposite, new ButtonPolicy(0)).getButton();
		technicalButton.setBounds(110, 190, 100, 25);
		if (ecoreExternalSchemaVO.getEcoreExternalSchema() != null) {
			if (ecoreExternalSchemaVO.getEcoreExternalSchema().getTechnical() != null) {
				technicalButton.setSelection(true);
			}
		}
		technicalButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
			}
		});

		Label btLb = new Label(generalInformationComposite, SWT.LEFT);
		btLb.setBounds(10, 240, 100, 25);
		btLb.setText(I18nApi.get("editor.title.bt"));
		btText = new Text(generalInformationComposite, SWT.BORDER);
		btText.setEnabled(false);
		btText.setBounds(110, 240, swidht - 100, 25);
		if (ecoreExternalSchemaVO.getEcoreExternalSchema() != null) {
//			btText.setText(ecoreExternalSchemaVO.getEcoreExternalSchema().get);
		}
		btBtn = new SummaryRowTextComposite(generalInformationComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_PUSH_DEEP,"choose")).getButton();
		btBtn.setBounds(swidht + 10, 240, 50, 25);
		btBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					setDirty(true);
					MrEditorDialogCreator.createSelectBusinessComponentDialog(btText);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		Button btSearchBtn = new SummaryRowTextComposite(generalInformationComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_DEEP,ImgUtil.createImg(ImgUtil.SEARCH_BTN))).getButton();
		btSearchBtn.setBounds(swidht + 60, 240, 25, 25);
		new SummaryExpandComposite(summaryBar, generalInformationComposite, I18nApi.get("editor.title.gi"), 270);
	}

	private void addNameSpaceList(ExpandBar summaryBar) {
		Composite namespaceListComposite = new Composite(summaryBar, SWT.NONE);
		Label examplesDescLabel = new Label(namespaceListComposite, SWT.WRAP);
		examplesDescLabel.setText("In this section, you can define namespaces.");
		examplesDescLabel.setBounds(10, 2, width, 15);

		addNamespaceListBtn = new SummaryRowTextComposite(namespaceListComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR))).getButton();
		addNamespaceListBtn.setBounds(10, 20, 25, 25);
		addNamespaceListBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MrEditorDialogCreator.createNamespaceListDialogue(namespaceList);
				setDirty(true);
			}
		});

		deleteNamespaceListBtn = new SummaryRowTextComposite(namespaceListComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES))).getButton();
		deleteNamespaceListBtn.setBounds(40, 20, 25, 25);
		deleteNamespaceListBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MrEditorDialogCreator.deleteExamplesDialogue(namespaceList);
				setDirty(true);
			}
		});

		namespaceList = new List(namespaceListComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		namespaceList.setBounds(10, 50, width / 2 - 70, 100);

		if (ecoreExternalSchemaVO.getEcoreNamespaceList() != null
				&& ecoreExternalSchemaVO.getEcoreNamespaceList().size() > 0) {
			Map<String,String>map=new HashMap<String, String>();
			for (EcoreNamespaceList ecoreNamespaceList : ecoreExternalSchemaVO.getEcoreNamespaceList()) {
				map.put(ecoreNamespaceList.getNamespaceList(),ecoreNamespaceList.getId());
				namespaceList.add(ecoreNamespaceList.getNamespaceList());
			}
			namespaceList.setData("map",map);
		}
		new SummaryExpandComposite(summaryBar, namespaceListComposite, I18nApi.get("editor.title.nl"), 160);
	}

	private void addCMPInformation(ExpandBar summaryBar) {
		Composite cmpInformationComposite = new Composite(summaryBar, SWT.NONE);

		Label desLb = new Label(cmpInformationComposite, SWT.WRAP);
		desLb.setText(I18nApi.get("editor.title.cmp"));
		desLb.setBounds(10, 2, width, 15);

		Label oiLb = new Label(cmpInformationComposite, SWT.NONE);
		oiLb.setBounds(10, 20, 120, 25);
		oiLb.setText("Object Identifier");

		oiText = new Text(cmpInformationComposite, SWT.BORDER);
		oiText.setBounds(140, 20, width / 2 - 160, 25);
		oiText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});

		if (ecoreExternalSchemaVO.getEcoreExternalSchema() != null) {
			oiText.setText(ecoreExternalSchemaVO.getEcoreExternalSchema().getObjectIdentifier()==null?
					"":ecoreExternalSchemaVO.getEcoreExternalSchema().getObjectIdentifier() + "");
		}
		new SummaryExpandComposite(summaryBar, cmpInformationComposite, I18nApi.get("editor.title.cmp"), 65);
	}

	private void addRegistrationInformation(ExpandBar summaryBar) {

		Composite generalInformationComposite = new Composite(summaryBar, SWT.NONE);

		Label desLb = new Label(generalInformationComposite, SWT.WRAP);
		desLb.setText(I18nApi.get("editor.title.ri"));
		desLb.setBounds(10, 2, width, 15);

		Label rsLb = new Label(generalInformationComposite, SWT.NONE);
		rsLb.setBounds(10, 20, 120, 25);
		rsLb.setText("Registration Status");

		rmCombo = new Combo(generalInformationComposite, SWT.BORDER);
		rmCombo.setBounds(140, 20, width / 2 - 200, 25);
		rmCombo.add(RegistrationStatusEnum.Registered.getStatus());
		rmCombo.add(RegistrationStatusEnum.Provisionally.getStatus());
		rmCombo.add(RegistrationStatusEnum.Added.getStatus());
		rmCombo.setEnabled(false);
		rmCombo.select(2);
		if (ecoreExternalSchemaVO.getEcoreExternalSchema() != null) {
			rmCombo.setText(ecoreExternalSchemaVO.getEcoreExternalSchema().getRegistrationStatus()==null?
					"":ecoreExternalSchemaVO.getEcoreExternalSchema().getRegistrationStatus() + "");
		}
		rmCombo.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});

		Label rdLb = new Label(generalInformationComposite, SWT.NONE);
		rdLb.setBounds(10, 50, 120, 25);
		rdLb.setText("Removal Date");

		rdText = new Text(generalInformationComposite, SWT.BORDER);
		rdText.setBounds(140, 50, width / 2 - 200, 25);
		rdText.setEnabled(false);
		rdText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});
		if (ecoreExternalSchemaVO.getEcoreExternalSchema() != null) {
			String date;
			try {
				date = sdf.format(ecoreExternalSchemaVO.getEcoreExternalSchema().getRemovalDate());
			} catch (Exception e) {
				date = "";
			}
			rdText.setText(date);
		}

		new SummaryExpandComposite(summaryBar, generalInformationComposite, I18nApi.get("editor.title.ri"), 90);
	}

	private void addExamples(ExpandBar summaryBar) {

		Composite generalInformationComposite = new Composite(summaryBar, SWT.NONE);

		Label examplesDescLabel = new Label(generalInformationComposite, SWT.WRAP);
		examplesDescLabel.setText("In this section, you can define examples.");
		examplesDescLabel.setBounds(10, 2, width, 15);

		addExamplesBtn = new SummaryRowTextComposite(generalInformationComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR))).getButton();
		addExamplesBtn.setBounds(10, 20, 25, 25);
		addExamplesBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				MrEditorDialogCreator.createExamplesDialogue(examplesList);
			}
		});

		deleteExamplesBtn = new SummaryRowTextComposite(generalInformationComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES))).getButton();
		deleteExamplesBtn.setBounds(40, 20, 25, 25);
		deleteExamplesBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				MrEditorDialogCreator.deleteExamplesDialogue(examplesList);
			}
		});

		examplesList = new List(generalInformationComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		examplesList.setBounds(10, 50, width / 2 - 70, 100);
		if (ecoreExternalSchemaVO.getEcoreExamples() != null && ecoreExternalSchemaVO.getEcoreExamples().size() > 0) {
			@SuppressWarnings("unchecked")
			Map<String,String>map=new HashedMap();
			for (EcoreExample ecoreExample : ecoreExternalSchemaVO.getEcoreExamples()) {
				map.put(ecoreExample.getExample(), ecoreExample.getId());
				examplesList.add(ecoreExample.getExample());
			}
			examplesList.setData("map",map);
		}
		new SummaryExpandComposite(summaryBar, generalInformationComposite, I18nApi.get("editor.title.es"), 150);
	}

	private void addConstraints(ExpandBar summaryBar) {

		Composite generalInformationComposite = new Composite(summaryBar, SWT.NONE);

		Label constraintsDescLabel = new Label(generalInformationComposite, SWT.WRAP);
		constraintsDescLabel.setText(
				"All the constraints contained in this object (other constraints - such as constraints defined on type - may also apply).");
		constraintsDescLabel.setBounds(10, 10, (width - 150) / 2, 40);

		addConstrainBtn = new SummaryRowTextComposite(generalInformationComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR))).getButton();
		addConstrainBtn.setBounds(10, 50, 25, 25);
		addConstrainBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				MrEditorDialogCreator.createConstraintDialogue("add", "1", constraintsTable, null, dataType);
			}
		});

		editConstrainBtn = new SummaryRowTextComposite(generalInformationComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.EDIT_IMG_TOOLBAR))).getButton();
		editConstrainBtn.setBounds(40, 50, 25, 25);
		editConstrainBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				// 不选中不触发任何效果
				if (constraintsTable.getSelectionCount() > 0) {
					setDirty(true);
					MrEditorDialogCreator.createConstraintDialogue("edit", "1", constraintsTable, null, dataType);
				}
			}
		});

		deleteConstraintBtn = new SummaryRowTextComposite(generalInformationComposite, new ButtonPolicy(ButtonPolicy.BUTTON_TYPE_NONE_LIGHT,ImgUtil.createImg(ImgUtil.DELETE_IMG_YES))).getButton();
		deleteConstraintBtn.setBounds(70, 50, 25, 25);
		deleteConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				MrEditorDialogCreator.deleteConstraint("1", constraintsTable, null, dataType);
			}
		});
		int tableWidth = width / 2 - 70;
		constraintsTable = new Table(generalInformationComposite, SWT.BORDER);
		constraintsTable.setBounds(10, 80, tableWidth, 130);
		constraintsTable.setHeaderVisible(true);
		TableColumn tableColumn1 = new TableColumn(constraintsTable, SWT.None);
		tableColumn1.setText("Name");
		tableColumn1.setWidth(80);
		TableColumn tableColumn2 = new TableColumn(constraintsTable, SWT.None);
		tableColumn2.setText("Definition");
		tableColumn2.setWidth(80);
		TableColumn tableColumn3 = new TableColumn(constraintsTable, SWT.None);
		tableColumn3.setText("Expression Language");
		tableColumn3.setWidth(tableWidth - 250);
		TableColumn tableColumn4 = new TableColumn(constraintsTable, SWT.None);
		tableColumn4.setText("Expression");
		tableColumn4.setWidth(80);

		if (ecoreExternalSchemaVO.getEcoreConstraints() != null
				&& ecoreExternalSchemaVO.getEcoreConstraints().size() > 0) {
			for (EcoreConstraint ecoreConstraint : ecoreExternalSchemaVO.getEcoreConstraints()) {
				TableItem tableItem = new TableItem(constraintsTable, SWT.NONE);
				tableItem.setText(new String[] { ecoreConstraint.getName(), ecoreConstraint.getDefinition(),
						ecoreConstraint.getExpression(), ecoreConstraint.getExpressionlanguage() });
			}
		}
		new SummaryExpandComposite(summaryBar, generalInformationComposite, I18nApi.get("editor.title.cs"), 220);
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
		MrEditorTableCreator.generateMessageSetTableOfImpactPage(messageSetTable, ecoreMessageSetList);

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
		MrEditorTableCreator.generateMessageDefinationTableOfImpactPage(messageDefinitionTable,
				ecoreMsgDefinitonList);

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

	void createVersionAndSubsetsPage() {

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
		lc_gd.widthHint = composite.getBounds().width / 3;
		leftComposite.setLayoutData(lc_gd);

		GridData rc_gd = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		rc_gd.widthHint = composite.getBounds().width / 3;
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
		this.generateNextDataTypeVersionList(versionListWidget);
		new SummaryExpandComposite(nextVersionBar, nextVersionBarComposite, I18nApi.get("editor.title.nv"), 450);
		ExpandBar previousVersionBar = new SummaryExpandComposite(rightComposite, 0).getExpandBar();

		Composite previousVersionComposite = new Composite(previousVersionBar, SWT.NONE);
		previousVersionComposite.setLayout(new GridLayout(1, false));
		Label rightDescriptionLabel = new Label(previousVersionComposite, SWT.WRAP);
		rightDescriptionLabel.setText("Shows the previous version of this repository item.");
		Label previousVersionLabel = new Label(previousVersionComposite, SWT.WRAP);
		previousVersionLabel.setText("The previous version of this object is: ");
		Link previousVersionLink = new Link(previousVersionComposite, SWT.NONE);
		previousVersionLink
				.setText(previousEcoreExternalSchema.getName() == null ? "" : previousEcoreExternalSchema.getName());
		previousVersionLink.setData(previousEcoreExternalSchema.getId());
		new SummaryExpandComposite(previousVersionBar, previousVersionComposite, I18nApi.get("editor.title.pv"), 450);
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

	private void generateNextDataTypeVersionList(List versionListWidget) {
		for (int index = 0; index < externalSchemaNextVersionList.size(); index++) {
			versionListWidget.add(externalSchemaNextVersionList.get(index).getName());
			versionListWidget.setData(String.valueOf(index), externalSchemaNextVersionList.get(index).getId());
		}
	}

	/**
	 * 获取所有的数据
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public EcoreExternalSchemaVO getAllData() {
		EcoreExternalSchemaVO ecoreExternalSchemaVO = new EcoreExternalSchemaVO();

		EcoreExternalSchema ecoreExternalSchema = new EcoreExternalSchema();
		ecoreExternalSchema.setId(externalSchemaId);
		ecoreExternalSchema.setName(this.nameText.getText());
		ecoreExternalSchema.setDefinition(this.docText.getText());
		ecoreExternalSchema.setProcessContent(this.pcCombo.getText());
		ecoreExternalSchema.setTechnical(this.technicalButton.getText());
		ecoreExternalSchema.setObjectIdentifier(this.oiText.getText());
		ecoreExternalSchema.setRegistrationStatus(this.rmCombo.getText());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date removalDate;
		try {
			removalDate = (rdText.getText() == null || rdText.getText() == "") ? null : sdf.parse(rdText.getText());
		} catch (ParseException e) {
			removalDate = null;
		}
		ecoreExternalSchema.setRemovalDate(removalDate);
		ecoreExternalSchemaVO.setEcoreExternalSchema(ecoreExternalSchema);

		// 获取namespaces信息
		java.util.List<EcoreNamespaceList> namespaces = new ArrayList<EcoreNamespaceList>();
		String[] namespaceItems = this.namespaceList.getItems();
		Map<String,String>map1=(Map<String, String>) namespaceList.getData("map");
		if (namespaceItems != null && namespaceItems.length > 0) {
			for (String str : namespaceItems) {
				EcoreNamespaceList namespaceList = new EcoreNamespaceList();
				if (map1!=null) {
					namespaceList.setId(map1.get(str)==null?UUID.randomUUID().toString():map1.get(str));
					}else {
						namespaceList.setId(UUID.randomUUID().toString());
					}
				namespaceList.setNamespaceList(str);
				namespaceList.setObj_id(ecoreExternalSchema.getId());
				namespaces.add(namespaceList);
			}
		}
		ecoreExternalSchemaVO.setEcoreNamespaceList(namespaces);

		// 获取examples信息
		java.util.List<EcoreExample> examples = new ArrayList<EcoreExample>();
		String[] exampleItems = this.examplesList.getItems();
		Map<String,String>map=(Map<String, String>) examplesList.getData("map");
		if (exampleItems != null && exampleItems.length > 0) {
			for (String str : exampleItems) {
				EcoreExample bcExample = new EcoreExample();
				bcExample.setId(map.get(str)==null?UUID.randomUUID().toString():map.get(str));
				bcExample.setExample(str);
				bcExample.setObjId(ecoreExternalSchema.getId());
				bcExample.setObjType(ObjTypeEnum.ExternalSchema.getType());
				examples.add(bcExample);
			}
		}
		ecoreExternalSchemaVO.setEcoreExamples(examples);

		// 获取constraints
		java.util.List<EcoreConstraint> constraints = new ArrayList<EcoreConstraint>();
		TableItem[] constraintItems = constraintsTable.getItems();
		if (constraintItems != null && constraintItems.length > 0) {
			for (TableItem tableItem : constraintItems) {
				EcoreConstraint bcConstraint = new EcoreConstraint();
				bcConstraint.setId(tableItem.getData("id")==null?UUID.randomUUID().toString():tableItem.getData("id").toString());
				bcConstraint.setObj_id(ecoreExternalSchema.getId());
				bcConstraint.setObj_type(ObjTypeEnum.ExternalSchema.getType());
				bcConstraint.setName(tableItem.getText(0));
				bcConstraint.setDefinition(tableItem.getText(1));
				bcConstraint.setExpressionlanguage(tableItem.getText(2));
				bcConstraint.setExpression(tableItem.getText(3));
				constraints.add(bcConstraint);
			}
		}
		ecoreExternalSchemaVO.setEcoreConstraints(constraints);

		return ecoreExternalSchemaVO;
	}
	public void changeStatus() {
		if (ecoreExternalSchemaVO.getEcoreExternalSchema()==null) {
		}else {
		if (ecoreExternalSchemaVO.getEcoreExternalSchema().getRegistrationStatus()==null) {
			
		}else {
		String status=ecoreExternalSchemaVO.getEcoreExternalSchema().getRegistrationStatus();
		if (status.equals("Registered")) {
			// TODO Auto-generated method stub
			//注册状态为已注册，则内容不可编辑
			nameText.setEditable(false);
			nameText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
			docText.setEditable(false);
			docText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
			oiText.setEditable(false);
			oiText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
			pcCombo.setEnabled(false);
			addExamplesBtn.setEnabled(false);
			deleteExamplesBtn.setEnabled(false);
			technicalButton.setEnabled(false);
			addNamespaceListBtn.setEnabled(false);
			addConstrainBtn.setEnabled(false);
			editConstrainBtn.setEnabled(false);
			deleteConstraintBtn.setEnabled(false);
			deleteNamespaceListBtn.setEnabled(false);
			deleteExamplesBtn.setEnabled(false);
			btBtn.setEnabled(false);
		}else if (status.equals("Provisionally Registered")) {
		}
	}
	}
}
}
