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
import org.eclipse.jface.dialogs.MessageDialog;
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

import com.cfets.cufir.plugin.mr.bean.TransferDataBean;
import com.cfets.cufir.plugin.mr.bean.TreeListItem;
import com.cfets.cufir.plugin.mr.enums.ObjTypeEnum;
import com.cfets.cufir.plugin.mr.enums.RegistrationStatus;
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
import com.cfets.cufir.s.data.bean.EcoreBusinessElement;
import com.cfets.cufir.s.data.bean.EcoreCode;
import com.cfets.cufir.s.data.bean.EcoreConstraint;
import com.cfets.cufir.s.data.bean.EcoreDataType;
import com.cfets.cufir.s.data.bean.EcoreExample;
import com.cfets.cufir.s.data.bean.EcoreMessageComponent;
import com.cfets.cufir.s.data.bean.EcoreMessageDefinition;
import com.cfets.cufir.s.data.bean.EcoreMessageElement;
import com.cfets.cufir.s.data.bean.EcoreMessageSet;
import com.cfets.cufir.s.data.dao.EcoreMessageComponentDao;
import com.cfets.cufir.s.data.dao.impl.EcoreMessageComponentDaoImpl;
import com.cfets.cufir.s.data.vo.EcoreMessageComponentVO;
import com.cfets.cufir.s.data.vo.EcoreMessageElementVO;
import com.cfets.cufir.s.data.vo.EcoreTreeNode;
import com.cfets.cufir.s.data.vo.SynonymVO;

/**
 * MessageComponents新建、编辑
 * @author qiming_het
 *
 */
public class MessageComponentCreateEditor extends MultiPageEditorParent {

	private EcoreMessageComponentDao ecoreMessageComponentDao = new EcoreMessageComponentDaoImpl();

	private String messageComponentId;

	// -----------For Summary Page -----------
	private Text summaryNameText, summaryDocText, summaryobjectIdentifierText, summaryRemovalDate;
	public Combo summaryStatusCombo;
	private Button btBtn,bt,renameButton;
	
	private Button choiceButton,choButton,Delbutton;
	private Table summaryConstraintsTable;
	private List exampleList;

	// ----------- For Content Page -------------
	private Tree elementsTree,tableTree;
	private Text meNameText, meDocText, meMinOccursText, meMaxOccursText, meTypeText, meXmlTagText,
			meObjectIdentifierText,codeDocumentText,codeNameText,nameText,bcTypeText;
//	private Combo meStatusCombo;
	private Button isDerivedCheckButton, compositeCheckButton,addExampleBtn,deleteExampleBtn,addConstraintBtn,editConstraintBtn,deleteConstraintBtn,editConstraintBtn1,deleteConstraintBtn1
	,addMessageElementBtn,deleteBusinessElementBtn,addSynonymsBtn,deleteButtonForSynonyms,addConstraintBtn1;
	private Table meConstraintsTable;
	private Table meSynonymsTable;
	private TreeItem selectedTableItem ,item1;

	// ------------For All--------------
	// 基本数据类型
	private String dataType;
	private TreeItem modelExploreTreeItem;
	private MyEditorInput myEditorInput;

	// ----------For Business Trace Page -----
	private Table messageDefinitionTable;
	private Table messageComponentTable;

	// ----------For impact page -------------
	private Table messageSetTable;

	// ----------For Version/Subsets Page -----
	private List nextMsgDefinitionListWidget;
	private List previousVersionListWidget;

	private EcoreMessageComponent msgComponent = new EcoreMessageComponent();
	private java.util.List<EcoreMessageElementVO> msgElementList = new ArrayList<>();
	private ArrayList<EcoreMessageComponent> msgComponentList = new ArrayList<>();
	private ArrayList<EcoreMessageDefinition> msgDefinitionList = new ArrayList<>();
	private ArrayList<EcoreMessageSet> messageSetList = new ArrayList<>();
	private EcoreMessageComponent previousVersionMsgComponent = new EcoreMessageComponent();
	private ArrayList<EcoreMessageComponent> nextVersionMsgComponentList = new ArrayList<>();

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	// ----填充的数据------
	private EcoreMessageComponentVO ecoreMessageComponentVO;

	private Map<String, ArrayList<EcoreBusinessElement>> allBeByComponentId;


	private Map<String, String> allBusinessComponent;


	private Map<String, ArrayList<EcoreBusinessElement>> allBeByTyId;

	private Map<String, ArrayList<EcoreTreeNode>> allSubBusiness;

	private TreeItem[] items;

	private EcoreDataType dataTypeById;


//	private Map<String, ArrayList<EcoreBusinessElement>> allSubBusinessByBe;

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		this.setSite(site);
		this.setInput(input);
		this.setPartProperty("customName", "msgComponentCreate");
		this.myEditorInput = (MyEditorInput) input;
		this.dataType = this.myEditorInput.getTransferDataBean().getType();
		modelExploreTreeItem = this.myEditorInput.getTransferDataBean().getTreeListItem();
		messageComponentId = this.myEditorInput.getTransferDataBean().getId();
		this.setPartProperty("ID", messageComponentId);
		// 初始化值
		initFilledData();
		// ctrl + s保存必须要
		ContextServiceUtil.setContext(site);

		// 不为null代表是打开的,可编辑
		if (ecoreMessageComponentVO.getEcoreMessageComponent() != null) {
			this.setPartName(ecoreMessageComponentVO.getEcoreMessageComponent().getName());
			this.setDirty(false);
			if (ecoreMessageComponentVO.getEcoreMessageComponent().getRegistrationStatus().equals("Provisionally Registered")
					||ecoreMessageComponentVO.getEcoreMessageComponent().getRegistrationStatus().equals("Added Registered")) {
				this.setTitleImage(ImgUtil.createImg(ImgUtil.MC_SUB3_COMPONENT));
			}else {
				this.setTitleImage(ImgUtil.createImg(ImgUtil.MC_SUB1_COMPONENT));
			}
		} else {
			this.setPartName("");
			this.setDirty(true);
			if (ecoreMessageComponentVO.getEcoreMessageComponent() == null) {
				this.setTitleImage(ImgUtil.createImg(ImgUtil.MC_SUB3_COMPONENT));
			}else {
				if(ecoreMessageComponentVO.getEcoreMessageComponent().getRegistrationStatus()==null) {
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
			ecoreMessageComponentVO = ecoreMessageComponentDao.findMessageComponentVO(messageComponentId);

			if (ecoreMessageComponentVO != null) {
				msgComponent = ecoreMessageComponentVO.getEcoreMessageComponent();
				msgElementList = ecoreMessageComponentVO.getEcoreMessageElementVOs();

				msgComponentList = DerbyDaoUtil.getMessageComponentListByMCId(messageComponentId);
				msgDefinitionList = DerbyDaoUtil.getmsgDefinitionListByMsgComponentId(messageComponentId);
				messageSetList = DerbyDaoUtil.getMessageSetByMsgDefinitionList(msgDefinitionList);
				previousVersionMsgComponent = DerbyDaoUtil.getPreviousVersionMsgComponentByMsgComponentId(msgComponent);
				nextVersionMsgComponentList = DerbyDaoUtil
						.getMessageComponentList(DerbyDaoUtil.getNextVersionIds(messageComponentId));
				allBeByComponentId = DerbyDaoUtil.getAllBeByComponentId();
				allSubBusiness = DerbyDaoUtil.getAllSubBusiness();
				allBusinessComponent = DerbyDaoUtil.getAllBusinessComponent();
				allBeByTyId = DerbyDaoUtil.getAllBeByTyId();
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ecoreMessageComponentVO = new EcoreMessageComponentVO();
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
		if (ecoreMessageComponentVO.getEcoreMessageComponent() != null) {
			this.setPartName(ecoreMessageComponentVO.getEcoreMessageComponent().getName());
			if ("2".equals(ecoreMessageComponentVO.getEcoreMessageComponent().getComponentType())) {
				if ( ecoreMessageComponentVO.getEcoreMessageComponent().getRegistrationStatus().equals(RegistrationStatus.Registered.getStatus()) 
						||ecoreMessageComponentVO.getEcoreMessageComponent().getRegistrationStatus().equals(RegistrationStatus.Obsolete.getStatus())) {
					this.setTitleImage(ImgUtil.createImg(ImgUtil.MC_SUB1_CHOICE));
					this.setDirty(false);
				}else {
					this.setTitleImage(ImgUtil.createImg(ImgUtil.MC_SUB1_CHOICE1));
					this.setDirty(false);
				}
			}else {
				if ( ecoreMessageComponentVO.getEcoreMessageComponent().getRegistrationStatus().equals(RegistrationStatus.Registered.getStatus())
						||ecoreMessageComponentVO.getEcoreMessageComponent().getRegistrationStatus().equals(RegistrationStatus.Obsolete.getStatus())) {
					this.setTitleImage(ImgUtil.createImg(ImgUtil.MC_SUB1_COMPONENT));
					this.setDirty(false);
				}else {
					this.setTitleImage(ImgUtil.createImg(ImgUtil.MC_SUB3_COMPONENT));
					this.setDirty(false);
				}
			}	
		}	
	}
	
	@Override
	public void setDirty(boolean dirty) {
		if (dirty && ecoreMessageComponentVO.getEcoreMessageComponent() == null) {
			super.setDirty(dirty);
		} else if (dirty && ecoreMessageComponentVO.getEcoreMessageComponent().getRegistrationStatus().equals(RegistrationStatus.Registered.getStatus())) {
			super.setDirty(false);
		} else {
			super.setDirty(dirty);
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
		generalInformationComposite.setBounds(0, 0, 350, 300);
//		generalInformationComposite.setLayout(new GridLayout(3, false));

		Label nameLabel = new Label(generalInformationComposite, SWT.NONE);
		nameLabel.setText("Name");
		nameLabel.setBounds(10, 10, 100, 30);
		
//		GridData nameLabelGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
//		nameLabelGridData.widthHint = 200;
//		nameLabel.setLayoutData(nameLabelGridData);
		summaryNameText = new Text(generalInformationComposite, SWT.BORDER);
		summaryNameText.setBounds(120, 10, 400, 30);
//		GridData nameValueGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
//		nameValueGridData.widthHint = 350;
//		summaryNameText.setLayoutData(nameValueGridData);
		if (ecoreMessageComponentVO.getEcoreMessageComponent() != null) {
			summaryNameText.setText(ecoreMessageComponentVO.getEcoreMessageComponent().getName() != null
					? ecoreMessageComponentVO.getEcoreMessageComponent().getName()
					: "");
		}
		summaryNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				modelExploreTreeItem.setText(summaryNameText.getText()==null?"":summaryNameText.getText());
				setPartName(summaryNameText.getText());
				setDirty(true);
				Map<String, String> map;
				try {
					map = ecoreMessageComponentDao.findAllMessageComponentName();
					//校验数据库中是否有相同name并无视大小写
					Boolean collect = map.values().stream().filter(O->
					O.equalsIgnoreCase(summaryNameText.getText())).findFirst().isPresent();
					if (collect==true) {
						MessageDialog.openWarning(parentComposite.getShell(),"提示", "命名已存在，请重新命名或点击右方按钮命名");
					}
					//只校验字符串
//					if(map.containsValue(summaryNameText.getText())) {
//						MessageDialog.openWarning(parentComposite.getShell(),"提示", "命名已存在，请重新命名或点击右方按钮命名");
//					}else if(map.containsValue(summaryNameText.getText())){
//						MessageDialog.openWarning(parentComposite.getShell(),"提示", "命名已存在，请重新命名或点击右方按钮命名");
//					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
			}
		});
		

		renameButton=new Button(generalInformationComposite, SWT.NONE);
		renameButton.setBounds(540, 10, 120, 30);
		renameButton.setText("Assign Fresh Name");
		renameButton.setBackground(new Color(Display.getCurrent(),135,206,250));//rgb值
		renameButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				generateName();
			}
		});
		
		Label documentationLabel = new Label(generalInformationComposite, SWT.NONE);
		documentationLabel.setBounds(10, 50, 100, 30);
//		documentationLabel.setLayoutData(nameLabelGridData);
		documentationLabel.setText("Documentation");
		summaryDocText = new Text(generalInformationComposite, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		summaryDocText.setBounds(120, 50, 600, 200);
//		GridData docValueGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
//		docValueGridData.widthHint = 500;
//		docValueGridData.heightHint = 150;
//		summaryDocText.setLayoutData(docValueGridData);
		if (ecoreMessageComponentVO.getEcoreMessageComponent() != null) {
			summaryDocText.setText(ecoreMessageComponentVO.getEcoreMessageComponent().getDefinition() != null
					? ecoreMessageComponentVO.getEcoreMessageComponent().getDefinition()
					: "");
		}
		summaryDocText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});

		Label choice = new Label(generalInformationComposite, SWT.NONE);
		choice.setBounds(10, 260, 160, 30);
		choice.setText("This component is a choice");
		choiceButton = new Button(generalInformationComposite, SWT.CHECK);
		choiceButton.setBounds(175, 255, 100, 30);
		if (ecoreMessageComponentVO.getEcoreMessageComponent() != null) {
			if ("2".equals(ecoreMessageComponentVO.getEcoreMessageComponent().getComponentType())) {
				choiceButton.setSelection(true);
			}
		}

		choiceButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				if(!choiceButton.getSelection()) {
					if (ecoreMessageComponentVO.getEcoreMessageComponent() == null) {
						modelExploreTreeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB3_COMPONENT));	
						
					}else {
						if(ecoreMessageComponentVO.getEcoreMessageComponent().getRegistrationStatus()==null||
								ecoreMessageComponentVO.getEcoreMessageComponent().getRegistrationStatus().equals("Provisionally Registered")
								||ecoreMessageComponentVO.getEcoreMessageComponent().getRegistrationStatus().equals("Added Registered")){
						modelExploreTreeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB3_COMPONENT));	
						}
						else {
						modelExploreTreeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_COMPONENT));
					}
					}
				}else {
					if (ecoreMessageComponentVO.getEcoreMessageComponent() == null) {
						modelExploreTreeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_CHOICE1));
						
					}else {
					if(ecoreMessageComponentVO.getEcoreMessageComponent().getRegistrationStatus()==null) {
						modelExploreTreeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_CHOICE1));
						}
						else{
						if(ecoreMessageComponentVO.getEcoreMessageComponent().getRegistrationStatus().equals("Provisionally Registered")
								||ecoreMessageComponentVO.getEcoreMessageComponent().getRegistrationStatus().equals("Added Registered")){
						modelExploreTreeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_CHOICE1));	
						}else {
						modelExploreTreeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_CHOICE));
						}
						
						}
					
					}
					
					
				}
			}
		});

		ExpandItem generalInformationItem = new ExpandItem(summaryBar, SWT.NONE);
		generalInformationItem.setText("General Information");
		generalInformationItem.setHeight(300);
		generalInformationItem.setExpanded(true);
		generalInformationItem.setControl(generalInformationComposite);

		// ================= General Information Group End ============================

		// ================= CMP Information Group Begin ==============================
		Composite cmpComposite = new Composite(summaryBar, SWT.NONE);
//		cmpComposite.setLayout(new GridLayout(2, false));

		Label objectIdentifierLabel = new Label(cmpComposite, SWT.NONE);
		objectIdentifierLabel.setBounds(10, 5, 100, 25);
//		objectIdentifierLabel.setLayoutData(nameLabelGridData);
		objectIdentifierLabel.setText("Object Identifier");
		summaryobjectIdentifierText = new Text(cmpComposite, SWT.BORDER);
//		summaryobjectIdentifierText.setLayoutData(nameValueGridData);
		summaryobjectIdentifierText.setBounds(120, 5, 600, 25);
		if (ecoreMessageComponentVO.getEcoreMessageComponent() != null) {
			summaryobjectIdentifierText
					.setText(ecoreMessageComponentVO.getEcoreMessageComponent().getObjectIdentifier() != null
							? ecoreMessageComponentVO.getEcoreMessageComponent().getObjectIdentifier()
							: "");
		}
		summaryobjectIdentifierText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});

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
//		registrationStatusLabel.setLayoutData(nameLabelGridData);
		registrationStatusLabel.setBounds(10, 0, 200, 30);
		registrationStatusLabel.setText("Registration Status");

		GridData valueGridData = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
		valueGridData.widthHint = 150;
		ComboViewer summaryStatusComboViewer = new ComboViewer(registrationComposite, SWT.READ_ONLY);
		summaryStatusComboViewer.add(RegistrationStatus.Registered.getStatus());
		summaryStatusComboViewer.add(RegistrationStatus.Provisionally.getStatus());
		summaryStatusComboViewer.add(RegistrationStatus.Added.getStatus());
		summaryStatusComboViewer.add(RegistrationStatus.Obsolete.getStatus());
		summaryStatusCombo = summaryStatusComboViewer.getCombo();
		summaryStatusCombo.setLayoutData(valueGridData);
		summaryStatusCombo.setEnabled(false);
		if (ecoreMessageComponentVO.getEcoreMessageComponent() != null) {
			summaryStatusCombo.setText(ecoreMessageComponentVO.getEcoreMessageComponent().getRegistrationStatus()==
				null?"":ecoreMessageComponentVO.getEcoreMessageComponent().getRegistrationStatus());
		} else {
			summaryStatusCombo.setText(RegistrationStatus.Added.getStatus());
		}
		summaryStatusCombo.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}

			
		});
		
		Label removalDateLabel = new Label(registrationComposite, SWT.NONE);
		removalDateLabel.setText("Removal Date");
		summaryRemovalDate = new Text(registrationComposite, SWT.BORDER);
		summaryRemovalDate.setToolTipText("Date Format: MM/DD/YY or YYYY-MM-DD");
		summaryRemovalDate.setLayoutData(valueGridData);
		summaryRemovalDate.setEnabled(false);
		if (ecoreMessageComponentVO.getEcoreMessageComponent() != null
				&& ecoreMessageComponentVO.getEcoreMessageComponent().getRemovalDate() != null) {
			summaryRemovalDate.setText(sdf.format(ecoreMessageComponentVO.getEcoreMessageComponent().getRemovalDate()==null?"":ecoreMessageComponentVO.getEcoreMessageComponent().getRemovalDate()));
		}
		summaryRemovalDate.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});

		ExpandItem registrationItem = new ExpandItem(summaryBar, SWT.NONE);
		registrationItem.setText("Registration Information");
		registrationItem.setHeight(80);
		registrationItem.setExpanded(true);
		registrationItem.setControl(registrationComposite);

		// ===== Registration Information Group End ===============================

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
				setDirty(true);
				MessageBoxUtil.createExampleDialogue(exampleList);
				
			}
		});

		deleteExampleBtn = new Button(exampleToolbarComposite, SWT.NONE);
		deleteExampleBtn.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
		deleteExampleBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				MessageBoxUtil.deleteExampleDialogue(exampleList);
			}
		});

		exampleList = new List(exampleComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		GridData exampleListGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		exampleListGridData.widthHint = 500;
		exampleListGridData.heightHint = 100;
		exampleList.setLayoutData(exampleListGridData);
		if (ecoreMessageComponentVO.getEcoreExamples() != null) {
			exampleList.removeAll();
			Map<String,String>map=new HashMap<String,String>();
			java.util.List<EcoreExample> ecoreExampleList = ecoreMessageComponentVO.getEcoreExamples();
			for (EcoreExample ecoreExample : ecoreExampleList) {
				map.put(ecoreExample.getExample(),ecoreExample.getId());
				exampleList.add(ecoreExample.getExample());
			}
			exampleList.setData("map",map);
		}

		ExpandItem exampleItem = new ExpandItem(summaryBar, SWT.NONE);
		exampleItem.setText("Example");
		exampleItem.setHeight(210);
		exampleItem.setExpanded(true);
		exampleItem.setControl(exampleComposite);
		// =================== Exampel Group End ==============================

		// =================== constraints Begin ==============================
		Composite constraintComposite = new Composite(summaryBar, SWT.NONE);
		GridData constraintCompositeGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		constraintComposite.setLayout(new GridLayout(1, true));
		constraintComposite.setLayoutData(constraintCompositeGridData);

		Label contraintsDescLabel = new Label(constraintComposite, SWT.WRAP);
		contraintsDescLabel.setText(
				"All the constraints contained in this object (other constraints - such as constraints defined on type - may also apply).");
		GridData labelGridData = new GridData();
//				labelGridData.widthHint = 400;
		contraintsDescLabel.setLayoutData(labelGridData);

		Composite toolbarComposite = new Composite(constraintComposite, SWT.NONE);
		toolbarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

		addConstraintBtn = new Button(toolbarComposite, SWT.NONE);
		addConstraintBtn.setImage(ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR));
		addConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				MessageBoxUtil.createConstraintDialogue("add", "1", summaryConstraintsTable, null, dataType);
			}
		});

		editConstraintBtn = new Button(toolbarComposite, SWT.NONE);
		editConstraintBtn.setImage(ImgUtil.createImg(ImgUtil.EDIT_IMG_TOOLBAR));
		editConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				// 不选中不触发任何效果
				if (summaryConstraintsTable.getSelectionCount() > 0) {
					setDirty(true);
					MessageBoxUtil.createConstraintDialogue("edit", "1", summaryConstraintsTable, null, dataType);
				}
			}
		});

		deleteConstraintBtn = new Button(toolbarComposite, SWT.NONE);
		deleteConstraintBtn.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
		deleteConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				MessageBoxUtil.deleteConstraint("1", summaryConstraintsTable, null, dataType);
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
		if (ecoreMessageComponentVO.getEcoreConstraints() != null) {
			java.util.List<EcoreConstraint> ecoreConstraintList = ecoreMessageComponentVO.getEcoreConstraints();
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
		// =================== constraints End ==============================

		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	private void changeEditor() {
		// TODO Auto-generated method stub
		//注册状态为已注册和临时注册，则内容不可编辑
		summaryNameText.setEditable(false);
		summaryNameText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		summaryDocText.setEditable(false);
		summaryDocText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		summaryobjectIdentifierText.setEditable(false);
		summaryobjectIdentifierText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		choiceButton.setEnabled(false);
		addExampleBtn.setEnabled(false);
		deleteExampleBtn.setEnabled(false);
		addConstraintBtn.setEnabled(false);
		editConstraintBtn.setEnabled(false);
		deleteConstraintBtn.setEnabled(false);
		addMessageElementBtn.setEnabled(false);
		deleteBusinessElementBtn.setEnabled(false);
		bt.setEnabled(false);
		choButton.setEnabled(false);
		Delbutton.setEnabled(false);
		renameButton.setEnabled(false);
	}

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

		addMessageElementBtn = new Button(beToolbarComposite, SWT.NONE);
		addMessageElementBtn.setImage(ImgUtil.createImg(ImgUtil.MC_ADD_ELEMENT));
		addMessageElementBtn.addMouseListener(new MouseAdapter() {
			private String text;

			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				text = summaryStatusCombo.getText();
//				MessageBoxUtil.createMsgElementSelectionDialogue(elementsTree,text);
				addMessageElementDetailBar(rightComposite, scrolledComposite,elementsTree,text);
				
			}

			private void addMessageElementDetailBar(Composite rightComposite, ScrolledComposite scrolledComposite, Tree elementsTree, String text2) {
//				Shell messageElementWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
//				messageElementWindow.setText("Message Element General Information");
//				messageElementWindow.setLayout(new FormLayout());
//				for (Control control: Composite.getChildren()) {
//					control.dispose();
//				}
				// ------------ Message Element Details Begin ------------------------
				for (Control control: rightComposite.getChildren()) {
					control.dispose();
				}
				// ------------ Message Element Details Begin ------------------------
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
							MessageBoxUtil.createSelectMsgElementType(typeText);
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
						if (typeText.getText().isEmpty()) {
							nodeName = nameText.getText() + " [" + minText.getText() + ", " + maxOccursText.getText() + "]";
						} else {
							nodeName = nameText.getText() + " [" + minText.getText() + ", " + maxOccursText.getText() + "] : "
									+ typeText.getText();
						}
						treeItem.setText(nodeName);
						treeItem.setData("id",treeItem.getData("id")==null?UUID.randomUUID():treeItem.getData("id").toString());
//						treeItem.setData("id", UUID.randomUUID().toString());
						treeItem.setData("meNameText", nameText.getText());
						treeItem.setData("meDocText", docText.getText());
						treeItem.setData("meMinOccursText", minText.getText());
						treeItem.setData("meMaxOccursText", maxOccursText.getText());
						treeItem.setData("isTechnical", isTechnicalButton.getSelection());
						treeItem.setData("meType_id", String.valueOf(typeText.getData("meType_id")));
						treeItem.setData("meType", String.valueOf(typeText.getData("meType")));
//						treeItem.setData("meStatusCombo", String.valueOf(typeText.getData("status")));
						treeItem.setData("meStatusCombo", text);
						treeItem.setData("meTypeName", typeText.getText());
						treeItem.setData("mcstatus", 1);
						treeItem.setData("content", "add");
						
						if ("1".equals(String.valueOf(typeText.getData("meType")))) {
//							if (treeItem.getData("status")==null||treeItem.getData("status").equals("Provisionally Registered")) {
							treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));	
//							}
//							else {
//							treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
//							}
						} else if ("2".equals(String.valueOf(typeText.getData("meType")))) {
//							if (treeItem.getData("status")==null||treeItem.getData("status").equals("Provisionally Registered")) {
							treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT1));	
//							}else {
//							treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
//							}
						}else {
							treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
						}
//						treeItem.setImage(ImgUtil.createImg(ImgUtil.ME));
						// 用Message Component里的元素，构造Message Component树
						GenerateCommonTree.generateContainmentTreeForMessageComponent(treeItem,
								DerbyDaoUtil.getMessageElementList(String.valueOf(typeText.getData("meType_id"))),true);
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
				messageElementExpandItem.setText("Message Element General Information");
				messageElementExpandItem.setHeight(340);
				messageElementExpandItem.setExpanded(true);
				messageElementExpandItem.setControl(c);
				
				rightComposite.requestLayout();
				
				scrolledComposite.setMinSize(rightComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				
			}
		});

//		Button addMessageConstraintBtn = new Button(beToolbarComposite, SWT.NONE);
//		addMessageConstraintBtn.setImage(ImgUtil.createImg(ImgUtil.MC_ADD_CONSTRAINT));
//		addMessageConstraintBtn.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseUp(MouseEvent e) {
//				MessageBoxUtil.createConstraintDialogueForMC(elementsTree);
//			}
//		});

		deleteBusinessElementBtn = new Button(beToolbarComposite, SWT.NONE);
		deleteBusinessElementBtn.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
		deleteBusinessElementBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				MessageBoxUtil.deleteTreeItemForMC(elementsTree);
				// 设置右边的数据为空
				// TODO
			}
		});

		elementsTree = new Tree(contentBarComposite, SWT.BORDER | SWT.SINGLE);
		elementsTree.setLayoutData(new GridData(330, 700));

		if (ecoreMessageComponentVO.getEcoreMessageElementVOs() != null) {
			java.util.List<EcoreMessageElementVO> ecoreMessageElementVOs = ecoreMessageComponentVO
					.getEcoreMessageElementVOs();
			//CESHI1
			System.out.println("MessageComponent中选中的主键: "+messageComponentId);
			for (EcoreMessageElementVO ecoreMessageElementVO : ecoreMessageElementVOs) {
				EcoreMessageElement ecoreMessageElement = ecoreMessageElementVO.getEcoreMessageElement();
				EcoreMessageComponent mc = DerbyDaoUtil.getMessageComponentById(ecoreMessageElement.getTypeId());
				TreeItem treeItem = new TreeItem(elementsTree, SWT.NONE);
				// type = 1 ---> messageElement
//				treeItem.setData("type", 1);
				treeItem.setData("id", ecoreMessageElement.getId());
				treeItem.setData("meNameText", ecoreMessageElement.getName());
				treeItem.setData("meDocText", ecoreMessageElement.getDefinition());
				treeItem.setData("meMinOccursText", ecoreMessageElement.getMinOccurs());
				treeItem.setData("meMaxOccursText", ecoreMessageElement.getMaxOccurs());
				treeItem.setData("meType_id", ecoreMessageElement.getTypeId());
				treeItem.setData("meType", ecoreMessageElement.getType());
				treeItem.setData("meTypeName", ecoreMessageElement.getTypeName());
				// --------------TODO 后面修改
				treeItem.setData("isDerivedCheckButton", Boolean.getBoolean(ecoreMessageElement.getIsDerived()));
				treeItem.setData("meXmlTagText", ecoreMessageElement.getXmlTag());
				treeItem.setData("isTechnical", "");
				treeItem.setData("meObjectIdentifierText", ecoreMessageElement.getObjectIdentifier());
				treeItem.setData("meStatusCombo", ecoreMessageElement.getRegistrationStatus());
				treeItem.setData("mcStatusCombo", mc.getRegistrationStatus());
//				treeItem.setData("meRemovalDate", ecoreMessageElement.getRemovalDate());
				//-------x
				
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
//				treeItem.setData("mcLever",1);
				if (summaryStatusCombo.getText().equals("Added Registered")||summaryStatusCombo.getText()==null) {
					treeItem.setData("mcstatus",1);
				}
				String nodeName = ecoreMessageElement.getName() + " [" + ecoreMessageElement.getMinOccurs() + ", "
						+ ecoreMessageElement.getMaxOccurs() + "]";
				if (ecoreMessageElement.getTypeName() != null) {
					nodeName = nodeName + " : " + ecoreMessageElement.getTypeName();
				}
				treeItem.setText(nodeName);
//				treeItem.setData("meStatus", ecoreMessageElement.getRegistrationStatus());
//				if ("1".equals(ecoreMessageElement.getType())) {
//					if (ecoreMessageElement.getRegistrationStatus()==null||ecoreMessageElement.getRegistrationStatus().equals("Provisionally Registered")
//							||ecoreMessageElement.getRegistrationStatus().equals("")) {
//					treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT1));
//					}else {
//					treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
//					}
//				} else if ("2".equals(ecoreMessageElement.getType())) {
//					if (ecoreMessageElement.getRegistrationStatus()==null||ecoreMessageElement.getRegistrationStatus().equals("Provisionally Registered")
//							||ecoreMessageElement.getRegistrationStatus().equals("")) {
//					treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
//					}else {
//					treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
//					}
//				}
				if (summaryStatusCombo.getText()!=null) {
					if (summaryStatusCombo.getText().equals("Registered")) {
						if ("1".equals(ecoreMessageElement.getType())) {
							dataTypeById = DerbyDaoUtil.getDataTypeById(ecoreMessageElement.getTypeId());
							treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
							for (EcoreCode ecoreCode: DerbyDaoUtil.getCodeByCodeSetId(dataTypeById.getId())) {
								TreeItem codeTreeItem = new TreeItem(treeItem, SWT.NONE);
								codeTreeItem.setText(ecoreCode.getName() + " [" + ecoreCode.getCodeName() + "]");
								codeTreeItem.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
//								codeTreeItem.setData("id",ecoreCode.getId());
//								constraintList = DerbyDaoUtil.getContraints(ecoreCode.getId());
//								addContraintsNode(codeTreeItem, constraintList, ecoreCode.getName(), null);
//								codeTreeItem.setData("codeType", "1");
//								codeTreeItem.setData("CodesName", ecoreCode.getName());
//								codeTreeItem.setData("Document", ecoreCode.getDefinition());
//								codeTreeItem.setData("CodeName", ecoreCode.getCodeName());
//								ArrayList<EcoreCode> codeByCodeSetId = DerbyDaoUtil.getCodeByCodeSetId(dataType.getId());
//								generateContainmentTreeForMessageComponent(treeItem, codeByCodeSetId, false);
							}
						} else if ("2".equals(ecoreMessageElement.getType())) {
							treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
						}
					}else {
						if ("1".equals(ecoreMessageElement.getType())) {
							treeItem.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
						} else if ("2".equals(ecoreMessageElement.getType())) {
							treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT1));
						}
					}
					
					
				}
				// 用Message Component里的元素，构造Message Component树
				GenerateCommonTree.generateContainmentTreeForMessageComponents(treeItem,
						DerbyDaoUtil.getMessageElementList(ecoreMessageElement.getTypeId()), true);
			}
		}
 
		ExpandItem contentItem = new ExpandItem(contentBar, SWT.NONE);
		contentItem.setText("Content");
		contentItem.setHeight(850);
		contentItem.setExpanded(true);
		contentItem.setControl(contentBarComposite);
		// ------------ Content Expand Bar End ----------------------------------
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		elementsTree.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem treeItem = (TreeItem) e.item;
				selectedTableItem=treeItem;
					String codeName = String.valueOf(treeItem.getData("CodeName"));
					String showName=treeItem.getText();
					if (treeItem.getData("meTypeName") != null) {
						
						showMessageElementDetailBar(rightComposite, scrolledComposite);
						meNameText
						.setText(String.valueOf(treeItem.getData("meNameText") == null ? "" : treeItem.getData("meNameText")));
						meDocText.setText(String.valueOf(treeItem.getData("meDocText") == null ? "" : treeItem.getData("meDocText")));
						meMinOccursText.setText(
								String.valueOf(treeItem.getData("meMinOccursText") == null ? "" : treeItem.getData("meMinOccursText")));
						meMaxOccursText.setText(
								String.valueOf(treeItem.getData("meMaxOccursText") == null ? "" : treeItem.getData("meMaxOccursText")));
						isDerivedCheckButton.setSelection(treeItem.getData("isDerivedCheckButton") == null ? false
								: (boolean) treeItem.getData("isDerivedCheckButton"));
						compositeCheckButton.setSelection(treeItem.getData("compositeCheckButton") == null ? false
								: (boolean) treeItem.getData("compositeCheckButton"));
						meXmlTagText.setText(
								String.valueOf(treeItem.getData("meXmlTagText") == null ? "" : treeItem.getData("meXmlTagText")));
						meTypeText
						.setText(String.valueOf(treeItem.getData("meTypeName") == null ? "" : treeItem.getData("meTypeName")));
						meTypeText.setData("meType_id", treeItem.getData("meType_id"));
						meTypeText.setData("meType", treeItem.getData("meType"));
						meObjectIdentifierText.setText(String.valueOf(
								treeItem.getData("meObjectIdentifierText") == null ? "" : treeItem.getData("meObjectIdentifierText")));
						selectedTableItem.setText(showName);
						meSynonymsTable.removeAll();
						java.util.List<String[]> synonymsTableItems = (java.util.List<String[]>) treeItem
								.getData("beSynonymsTableItems");
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
						
					} else if (!codeName.equalsIgnoreCase("null")) {
						showCodeDetailBar(rightComposite, scrolledComposite);
						nameText.setText(String.valueOf(treeItem.getData("CodesName")==null?"":treeItem.getData("CodesName")));
						codeDocumentText.setText(String.valueOf(treeItem.getData("Document")==null?"":treeItem.getData("Document")));
						codeNameText.setText(String.valueOf(treeItem.getData("CodeName")==null?"":treeItem.getData("CodeName")));
						meObjectIdentifierText.setText(String.valueOf(treeItem.getData("meObjectIdentifierText") == null ? "" : treeItem.getData("meObjectIdentifierText")));
						
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
					if(treeItem.getData("mcstatus") != null){
						btBtn.setEnabled(true);
						meNameText.setEditable(true);
						meDocText.setEditable(true);
						meMinOccursText.setEditable(true);
						meMaxOccursText.setEditable(true); 
						meXmlTagText.setEditable(true);
						isDerivedCheckButton.setEnabled(true);
					}
				
			}
		});
		

		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	void createBusinessTrace() {
		Composite parentComposite = getContainer();
		Composite composite = new Composite(parentComposite, SWT.NONE);
		composite.setLayout(new GridLayout(1, true));
		int index = addPage(composite);
		setPageText(index, "Business Trace");

		ScrolledComposite scrolledComposite = new ScrolledComposite(parentComposite, SWT.V_SCROLL | SWT.H_SCROLL);
		
		Label BusinessTraces = new Label(composite, SWT.NONE);
		BusinessTraces.setText("Business Traces");
		BusinessTraces.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		BusinessTraces.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		Label descriptionLabel = new Label(composite, SWT.NONE);
		descriptionLabel
				.setText("This section describes the Business traces of the message elements to the business model.");
		
		bt=new Button(composite, SWT.NONE);
		bt.setText("Change Trace");
//		bt.setEnabled(false);
		bt.setBackground(new Color(Display.getCurrent(),135,206,250));
		bt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				try {
					MessageBoxUtil.createSelectBusinessTrace(bcTypeText,tableTree);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
			}
		});
//		composite.setLayout(new GridLayout(2, false));
//		GridData gd = new GridData(SWT.TOP, SWT.TOP, true, false, 1, 1);
//		composite.setLayoutData(gd);
		
		
		Composite cmp = new Composite(composite, SWT.NONE);
		cmp.setLayoutData(new GridData(500, 40));
		cmp.setLayout(new GridLayout(2, false));
		
		Label choice = new Label(cmp, SWT.NONE);
		choice.setBounds(10, 300, 160, 30);
		choice.setText("This component is Technical");
		choButton = new Button(cmp, SWT.CHECK);
		choButton.setBounds(175, 305, 100, 30);
		EcoreMessageComponent mc_1 = DerbyDaoUtil.getMessageComponentById(this.messageComponentId);
		if (mc_1.getTechnical()!=null) {
			String a=mc_1.getTechnical();
			String value="false";
			if (a.equals(value)) {
				choButton.setSelection(false);
			}else {
				choButton.setSelection(true);
			}
		}
		choButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				//初始化按钮状态
				if(!choButton.getSelection()) {
					choButton.setSelection(false);
					bt.setEnabled(true);
				}else {
					choButton.setSelection(true);
					//触发事件
					bcTypeText.setText(" ");
					bcTypeText.setData("bcType_id",null);
					bt.setEnabled(false);
				}
			}
		});
		if (choButton!=null&&choButton.getSelection()==true) {
			bt.setEnabled(false);
		}
		
		GridData grid=new GridData(SWT.NONE);
		grid.widthHint = 210;
		Composite cmp_1 = new Composite(composite, SWT.NONE);
		cmp_1.setLayoutData(new GridData(1600, 40));
		cmp_1.setLayout(new GridLayout(4, false));
		
		Label lab = new Label(cmp_1, SWT.NONE);
		lab.setBounds(10, 620, 160, 30);
		lab.setText("This component is a view on business component:");
		
		Label lab1 = new Label(cmp_1,SWT.NONE);
		lab1.setBounds(180, 620, 460, 30);
		bcTypeText=new Text(cmp_1,SWT.BORDER);
		bcTypeText.setEditable(false);
		bcTypeText.setLayoutData(grid);
		bcTypeText.setForeground(new Color(Display.getCurrent(),57,0,255));//rgb值57,0,255
		if (mc_1.getTraceName()!=null) {
			//初始化text数据
				bcTypeText.setText(mc_1.getTraceName());
				bcTypeText.setData("bcType_id", mc_1.getTrace());
				bcTypeText.setData("bcName", mc_1.getTraceName());
				bcTypeText.setData("meLever", 1);
				
			}
		
		bcTypeText.addMouseListener(new MouseAdapter()  {
			
			@Override
			public void mouseUp(MouseEvent e) {
				//跳转实现
				    if (bcTypeText!=null) {
//				    String dataType = bcTypeText.getData("bcType").toString();
//					String beTypeTextType = (String)treeItem.getData("beTypeTextType");
					CTabFolder cTabFolder =TreeListView.getTabFolder();
//					if ("2".equals(dataType)) {
//						String Id = bcTypeText.getData("bcType_id").toString();
//						String Id = (String)treeItem.getData("beTypeTextId");
						Tree firstTabTree = TreeListView.getFirstTabTree();
						TreeItem mcTreeItem = firstTabTree.getItem(1);
						for (TreeItem bcItem: mcTreeItem.getItems()) {
							EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)bcItem.getData("EcoreTreeNode");
							if (bcTypeText.getData("meLever")!=null) {
//								treeItem.setText(meNameText.getText() + " [" + meMinOccursText.getText() + ", "
//										+ meMaxOccursText.getText() + "] : " +meTypeText.getText());
								String meComponentId = (String) bcTypeText.getData("bcType_id");
							if (ecoreTreeNode.getId().equals(meComponentId)) {
								TransferDataBean transferDataBean = new TransferDataBean();
								transferDataBean.setId(ecoreTreeNode.getId());
								transferDataBean.setName(ecoreTreeNode.getName());
								transferDataBean.setLevel(ecoreTreeNode.getLevel());
								transferDataBean.setType((String)bcItem.getData("type"));
								transferDataBean.setChildId(bcItem.getData("childId") == null ? "" : (String)bcItem.getData("childId"));
//								String status=ecoreTreeNode.getRegistrationStatus();
								transferDataBean.setImgPath(ImgUtil.BC);
								transferDataBean.setTreeListItem((TreeListItem)bcItem);
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
		
				
		tableTree=new Tree(composite,SWT.BORDER|SWT.FULL_SELECTION);
		tableTree.setHeaderVisible(true);
		tableTree.setLinesVisible(true);
		GridData tableGridData = new GridData();
		tableGridData.widthHint = 1200;
		tableGridData.heightHint = 700;
		tableGridData.grabExcessHorizontalSpace=true;
		tableGridData.grabExcessVerticalSpace=true;
		tableGridData.verticalAlignment=SWT.FILL;
		
		tableTree.setLayoutData(tableGridData);
		TreeColumn msgElementColumn = new TreeColumn(tableTree,SWT.NONE);
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
		
		if(this.msgElementList != null && this.msgElementList.size() > 0){
			for (EcoreMessageElementVO msgElementVO : this.msgElementList) {
			item1=new TreeItem(tableTree, SWT.NONE);
			
			EcoreMessageElement msgElement = msgElementVO.getEcoreMessageElement();
			
			//初始化加载表格数据   第一项默认为false 没有任何数据时
			item1.setText(1,msgElement.getTechnical()==null?"false":msgElement.getTechnical());
			item1.setText(2,msgElement.getTracesTo()==null?"":msgElement.getTracesTo());
//			item1.setImage(2,ImgUtil.createImg(ImgUtil.BC));
			item1.setText(3,msgElement.getTracePath()==null?"":msgElement.getTracePath());
			item1.setText(4,msgElement.getTypeOfTracesTo()==null?"":msgElement.getTypeOfTracesTo());
			item1.setData("trace",msgElement.getTechnical());
			if (msgElement.getTypeOfTracesTo()!=null) {
			item1.setData("me_bcTrace",msgElement.getTypeOfTracesTo());	
			}
			if ("2".equals(msgElement.getTraceType())) {
				item1.setImage(2,ImgUtil.createImg(ImgUtil.BC));
				item1.setData("Image",2);
			}else if ("1".equals(msgElement.getTraceType())) {
				item1.setImage(2,ImgUtil.createImg(ImgUtil.BC_BE));
				item1.setData("Image",1);
			}else{
				
			}
//			if (item1.getData("Image")!=null&&item1.getData("Image").equals("BC")) {
//			item1.setImage(2,ImgUtil.createImg(ImgUtil.BC));
//			}else {
//				item1.setImage(2,ImgUtil.createImg(ImgUtil.BC_BE));	
//			}
			String appendix = "";
			item1.setData("meLever",1);
			String status = null;
			if (summaryStatusCombo.getText()!=null) {
				status=summaryStatusCombo.getText();
			}
			if ("2".equals(msgElement.getType())) {// 1：datatype，2：消息组件
				EcoreMessageComponent mc = DerbyDaoUtil.getMessageComponentById(msgElement.getTypeId());
				appendix = " [" + msgElement.getMinOccurs() + "," + msgElement.getMaxOccurs() + "]" + " : "
						+ mc.getName();
				//已关联，有数据时
				if (mc.getTechnical().equals("true")) {
				item1.setText(4,"--TECHNICAL--");
				}
				else if (mc.getTraceName()!=null) {
				item1.setText(4,mc.getTraceName());
				}
				
				item1.setData("me_id",msgElement.getId());
				//表格中TypeOfTracesTo取值逻辑,getAll存储时使用
				if (mc.getTechnical().equals("true")) {
				item1.setData("me_bcTrace","--TECHNICAL--");
				}else if (mc.getTraceName()!=null) {
				item1.setData("me_bcTrace",mc.getTraceName());
				}
				item1.setData("mcStatusCombo",mc.getRegistrationStatus());
				
				if (item1.getData("bcType_id")!=null) {
					item1.setData("trace",item1.getData("bcType_id"));
				}else if (item1.getData("beType_id")!=null) {
					item1.setData("trace",item1.getData("beType_id"));
				}
				
				//取关联mc绑定的bc内容
				if (mc.getTrace()!=null) {
					item1.setData("TypeBcTrace",mc.getTrace());
				}
				
				item1.setText(msgElement.getName() + appendix);
				
				if (status==null||status.equals("Provisionally Registered")
						||status.equals("Added Registered")) {
					item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT1));
				}else {
					item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
				}
//				if ("1".equals(mc.getComponentType())) {
//					
//					if (mc.getRegistrationStatus()==null||mc.getRegistrationStatus().equals("Provisionally Registered")
//							||mc.getRegistrationStatus().equals("Added Registered")) {
//						item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT1));
//					}else {
//						item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
//					}
//				} else if ("2".equals(mc.getComponentType())) {
//					if (mc.getRegistrationStatus()==null||mc.getRegistrationStatus().equals("Provisionally Registered")
//							||mc.getRegistrationStatus().equals("Added Registered")) {
//						item1.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
//					}else {
//						item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
//					}
//				}
//				if (mc.getTrace()!=null) {
//					item1.setText(4,mc.getTrace());
//				}
				
				ArrayList<EcoreMessageElement> subMeList = DerbyDaoUtil.getMessageElementList(mc.getId());
				GenerateCommonTree.generateContainmentTreeForBusniessTrace(item1, subMeList);
			} else {
				// datatype
				EcoreDataType dataType = DerbyDaoUtil.getDataTypeById(msgElement.getTypeId());
				appendix = " : " + dataType.getName();
				item1.setText(msgElement.getName() + appendix);
				if (item1.getData("bcType_id")!=null) {
					item1.setData("trace",item1.getData("bcType_id"));
				}else if (item1.getData("beType_id")!=null) {
					item1.setData("trace",item1.getData("beType_id"));
				}
				item1.setText(1,msgElement.getTechnical()==null?"false":msgElement.getTechnical());
				item1.setText(2,msgElement.getTracesTo()==null?"":msgElement.getTracesTo());
				item1.setText(3,msgElement.getTracePath()==null?"":msgElement.getTracePath());
				item1.setText(4,"");
				//typeOfTracesTo取当前me的mc中traceName
//				item1.setData("typeOfTracesTo",mc.getTraceName());
				if ("1".equals(dataType.getType())) { // codeSet 已到最底层了
					if (status==null||status.equals("Provisionally Registered")
							||status.equals("Added Registered")) {
						item1.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
					}else {
						item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
					}
//					treeItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
					for (EcoreCode ecoreCode: DerbyDaoUtil.getCodeByCodeSetId(dataType.getId())) {
						TreeItem codeTreeItem = new TreeItem(item1, SWT.NONE);
						codeTreeItem.setText(ecoreCode.getName() + " [" + ecoreCode.getCodeName() + "]");
						codeTreeItem.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
						codeTreeItem.setData(ecoreCode.getId());
					}
				} else {
					if (status==null||status.equals("Provisionally Registered")
							||status.equals("Added Registered")) {
						item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT1));
					}else {
						item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
					}
				}
//				item1.setText(1,"false");
				item1.setText(0, msgElement.getName() + appendix);
//				if ("1".equals(dataType.getType())) { // codeSet 已到最底层了
//					if (msgElement.getRegistrationStatus()==null||msgElement.getRegistrationStatus().equals("Provisionally Registered")) {
//						item1.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
//					}else {
//						item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
//					}
//				} else {
//					if (msgElement.getRegistrationStatus()==null||msgElement.getRegistrationStatus().equals("Provisionally Registered")) {
//						item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT1));
//					}else {
//						item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
//					}
//				}
				
			}
		}
			
		}
		
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		tableTree.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(MouseEvent event) {
				setDirty(true);
				if (event.button==3) {
					Menu menu =new Menu(tableTree);
					tableTree.setMenu(menu);
					MenuItem item=new MenuItem(menu,SWT.PUSH);
					item.setText("Define Trace");
					item.addListener(SWT.Selection,new Listener() {


						@Override
						public void handleEvent(Event event) {
							TreeItem item1=tableTree.getSelection()[0];
							Tree parent=item1.getParent();
//							String name="";
							if (parent!=null) {
								//新建后再次触发事件
								if (allBeByComponentId==null) {
									initFilledData();
								}
								if(bcTypeText!=null) {
								MessageBoxUtil.createBusinessComponetTrace(bcTypeText,item1,allBeByComponentId,allSubBusiness,allBusinessComponent,allBeByTyId);
								}
							}
						}
					});
					MenuItem item2=new MenuItem(menu,SWT.PUSH);
					item2.setText("Remove Trace");
					item2.addListener(SWT.Selection,new Listener() {

						@Override
						public void handleEvent(Event event) {
							TreeItem item3=tableTree.getSelection()[0];
							Tree parent=item3.getParent();
							if (parent!=null) {
								item3.setText(2, " ");
								item3.setImage(2,null);
								item3.setText(3, " ");
							}
						}
						
					});
					
					MenuItem item4=new MenuItem(menu,SWT.PUSH);
					item4.setText("Set/Unset Technical");
					item4.addListener(SWT.Selection,new Listener() {

						@Override
						public void handleEvent(Event event) {
							TreeItem item5=tableTree.getSelection()[0];
							Tree parent=item5.getParent();
							if (parent!=null) {
								String name=item5.getText(1);
								if (name.equals("true")) {
									item5.setText(1, "false");
								}else if(name.equals("false")) {
									item5.setText(1, "true");
								}
								
						}
							}
						
					});
					//设置trace页面可编辑状态
					TreeItem items=tableTree.getSelection()[0];
					if(items.getData("meLever")!=null&&bcTypeText.getText().equals(" ")){
						item.setEnabled(false);
						item2.setEnabled(false);
						item4.setEnabled(false);
					}else if (items.getData("meLever")!=null&&bcTypeText.getData("bcName")!=null&&!bcTypeText.getData("bcName").equals("")) {
						item.setEnabled(true);
						item2.setEnabled(true);
						item4.setEnabled(true);
					}else {
						item.setEnabled(false);
						item2.setEnabled(false);
						item4.setEnabled(false);
					}
					//修改可编辑状态
					if (ecoreMessageComponentVO.getEcoreMessageComponent()!=null&&ecoreMessageComponentVO.getEcoreMessageComponent().getRegistrationStatus()!=null) {
					String status=ecoreMessageComponentVO.getEcoreMessageComponent().getRegistrationStatus();
					if (status.equals("Registered")||status.equals("Provisionally Registered")||status.equals("Obsolete")) {
						menu.setEnabled(false);
						item.setEnabled(false);
						item2.setEnabled(false);
						item4.setEnabled(false);
					}
					}
					
					
				}
			}
			
		});
		
		Delbutton = new Button(cmp_1, SWT.NONE);
		Delbutton.setBounds(700, 620, 50, 30);
		Delbutton.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
		Delbutton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				Shell parent = new Shell(Display.getCurrent());
				MessageBox messageBox = new MessageBox(parent, SWT.YES | SWT.NO);
				messageBox.setMessage("All trace information on the message elements will be erased."
						+ "Do you want to proceed？");
				if (messageBox.open() == SWT.YES) {
					//清空text数据
					bcTypeText.setText(" ");
					bcTypeText.setData("bcType_id",null);
					bcTypeText.setData("bcName",null);
//					TreeItem ti=item1;
					//清空表格中2,3位置数据
					TreeItem[] ti=tableTree.getItems();
					for (int i = 0; i < ti.length; i++) {
						TreeItem tTtem=ti[i];
						tTtem.setText(2," ");
						tTtem.setImage(2,null);
						tTtem.setText(3," ");
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
		messageSetTable = new Table(messageSetsGroup, SWT.BORDER | SWT.V_SCROLL);
		messageSetTable.setLinesVisible(true);
		TableCursor messageSetTableCursor = new TableCursor(messageSetTable, SWT.NONE);
		// 构造Message Set表格
		GenerateTableForMCandMDandMS.generateMessageSetTableOfImpactPage(messageSetTable, messageSetList);

		Group messageDefinitionsGroup = new Group(groupListComposite, SWT.NONE);
		messageDefinitionsGroup.setText("Message Definitions");
		messageDefinitionsGroup.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		messageDefinitionsGroup.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		messageDefinitionsGroup.setLayoutData(groupGridData);
		messageDefinitionsGroup.setLayout(new FillLayout(SWT.VERTICAL));
		messageDefinitionTable = new Table(messageDefinitionsGroup, SWT.BORDER | SWT.V_SCROLL);
		messageDefinitionTable.setLinesVisible(true);
		TableCursor messageDefinitionTableCursor = new TableCursor(messageDefinitionTable, SWT.NONE);
		// 构造Message Definition表格
		GenerateTableForMCandMDandMS.generateMessageDefinationTableOfImpactPage(messageDefinitionTable,
				msgDefinitionList);

		Group messageComponentGroup = new Group(groupListComposite, SWT.NONE);
		messageComponentGroup.setText("Message Component");
		messageComponentGroup.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		messageComponentGroup.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		messageComponentGroup.setLayoutData(groupGridData);
		messageComponentGroup.setLayout(new FillLayout(SWT.VERTICAL));
		messageComponentTable = new Table(messageComponentGroup, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
		messageComponentTable.setLinesVisible(true);
		TableCursor msgComponentTableCursor = new TableCursor(messageComponentTable, SWT.NONE);
		// 构造Message Component表格
		GenerateTableForMCandMDandMS.generateMessageComponentTableOfImpactPage(messageComponentTable, msgComponentList);

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
				treeRoot.setText(msgSetName);
				treeRoot.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
				treeRoot.removeAll();
				String messageSetId = (String) tableItem.getData(String.valueOf(messageSetTable.getSelectionIndex()));
				ArrayList<EcoreMessageDefinition> msgDefinitionList = DerbyDaoUtil
						.getMsgDefinitionByMsgSetId(messageSetId);
				GenerateCommonTree.generateContainmentTreeForMessageSetofImpactPage(treeRoot, msgDefinitionList);
				// 加载Constraint节点
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
				String msgDefinitionId = (String) tableItem
						.getData(String.valueOf(messageDefinitionTable.getSelectionIndex()));
				treeRoot.setText(msgDefinitionName);
				treeRoot.setImage(ImgUtil.createImg(ImgUtil.MD_SUB1));
				treeRoot.removeAll();
				// 递归生成contrainment tree on Impact page
				GenerateCommonTree.generateContainmentTreeForMessageDefinition(treeRoot,
						DerbyDaoUtil.getMsgBuildingBlock(msgDefinitionId));
				// 加载Constraint节点
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
				String msgComponentId = (String) tableItem
						.getData(String.valueOf(messageComponentTable.getSelectionIndex()));
				treeRoot.setText(msgComponentName);
				String status=summaryStatusCombo.getText();
				if (status==null||status.equals("Provisionally Registered")||status.equals("Added Registered")) {
				treeRoot.setImage(ImgUtil.createImg(ImgUtil.MC_SUB3_COMPONENT));	
				}else {
				treeRoot.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_COMPONENT));
				}
				treeRoot.removeAll();
				// 递归生成contrainment tree on Impact page
				GenerateCommonTree.generateContainmentTreeForMessageComponent(treeRoot,
						DerbyDaoUtil.getMessageElementList(msgComponentId), true);
				// 加载Constraint节点
				GenerateCommonTree.addContraintsNode(treeRoot, DerbyDaoUtil.getContraints(msgComponentId), null, null);
				treeRoot.setExpanded(true);
			}
		});
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	}

	void createVersionsAndSubsets() {

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
		lc_gd.widthHint = 550; // composite.getBounds().width / 3;
		leftComposite.setLayoutData(lc_gd);

		GridData rc_gd = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		rc_gd.widthHint = 500; // composite.getBounds().width / 3;
		Composite rightComposite = new Composite(composite, SWT.NONE);
		rightComposite.setLayout(new FillLayout(SWT.VERTICAL));
		rightComposite.setLayoutData(rc_gd);

		// ------------ Next Version Bar Begin ----------------------------------
		ExpandBar nextVersionBar = new ExpandBar(leftComposite, SWT.NONE);

		Composite nextVersionBarComposite = new Composite(nextVersionBar, SWT.NONE);
		GridData contentBarCompositeGridData = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		nextVersionBarComposite.setLayout(new FillLayout(SWT.VERTICAL));
		nextVersionBarComposite.setLayoutData(contentBarCompositeGridData);

		nextVersionBar.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		nextVersionBar.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		nextVersionBar.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));

		nextMsgDefinitionListWidget = new List(nextVersionBarComposite, SWT.BORDER | SWT.SINGLE);
		nextMsgDefinitionListWidget.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));

		this.generateNextMsgDefinitionList();
		// elementsTable.addListener(SWT.Selection, new
		// ElementTableOnContentPageListener());

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


	private void generatePreviousVersionList() {
		if (previousVersionMsgComponent.getId() != null) {
			previousVersionListWidget.add(previousVersionMsgComponent.getName());
			previousVersionListWidget.setData("previousVersionId", previousVersionMsgComponent.getId());
		}
	}

	private void generateNextMsgDefinitionList() {
		for (int index = 0; index < this.nextVersionMsgComponentList.size(); index++) {
			EcoreMessageComponent msgComponent = nextVersionMsgComponentList.get(index);
			nextMsgDefinitionListWidget.add(msgComponent.getName());
			nextMsgDefinitionListWidget.setData(String.valueOf(index), msgComponent.getId());
		}
	}
	
	private void generateName() {
		try {
			Map<String,String>map=ecoreMessageComponentDao.findAllMessageComponentName();
//			String mapname=map.get(this.messageComponentId);
			String name=summaryNameText.getText();
			if (name.equals("")) {
				MessageDialog.openWarning(Display.getDefault().getActiveShell(), "提示", "请先输入命名Name!");
			}else {
				
			if (map.containsValue(name)) {
				String a=name.substring(name.length()-1,name.length());
				String a1=name.substring(0,name.length()-1);
				
				String rename;
				//如果后面有数字(尾数)，则在该数字上加1，如果没有则在后面补1
				if(a.matches("[0-9]{1,}")){
					int i=Integer.parseInt(a)+1;
					rename=a1.concat(i+"");
					summaryNameText.setText(rename);
				}else {
					rename=name.concat("1");
					summaryNameText.setText(rename);
				}
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
		String name=null;
		EcoreMessageComponentVO ecoreMessageComponentVO = new EcoreMessageComponentVO();
		EcoreMessageComponent ecoreMessageComponent = new EcoreMessageComponent();
		ecoreMessageComponent.setId(messageComponentId);
		ecoreMessageComponent.setName(this.summaryNameText.getText());
		ecoreMessageComponent.setDefinition(this.summaryDocText.getText());

		// 组件类型，1：MessageComponent，2：ChoiceComponent
		ecoreMessageComponent.setComponentType(this.choiceButton.getSelection() == true ? "2" : "1");
		ecoreMessageComponent.setObjectIdentifier(this.summaryobjectIdentifierText.getText());
		ecoreMessageComponent.setRegistrationStatus(this.summaryStatusCombo.getText());
		if (this.bcTypeText!=null) {
		ecoreMessageComponent.setTrace(this.bcTypeText.getData("bcType_id")==null?"":this.bcTypeText.getData("bcType_id").toString());
		ecoreMessageComponent.setTraceName(this.bcTypeText.getData("bcName")==null?"":this.bcTypeText.getData("bcName").toString());
		
		}
		if (choButton.getSelection()==true) {
		ecoreMessageComponent.setTechnical("true");
		}else {
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

		// --TODO trace_id
		ecoreMessageComponentVO.setEcoreMessageComponent(ecoreMessageComponent);

		// 获取example信息
		java.util.List<EcoreExample> ecoreExamples = new ArrayList<EcoreExample>();
		String[] exampleItems = this.exampleList.getItems();
		Map<String,String>map=(Map<String, String>) exampleList.getData("map");
		if (exampleItems != null && exampleItems.length > 0) {
			for (String str : exampleItems) {
				EcoreExample bcExample = new EcoreExample();
				if (map!=null) {
					bcExample.setId(map.get(str)==null?UUID.randomUUID().toString():map.get(str));
					}else {
					bcExample.setId(UUID.randomUUID().toString());
					}
				bcExample.setExample(str);
				bcExample.setObj_id(ecoreMessageComponent.getId());
				bcExample.setObj_type(ObjTypeEnum.BusinessComponent.getType());
				ecoreExamples.add(bcExample);
			}
		}
		ecoreMessageComponentVO.setEcoreExamples(ecoreExamples);

		// 获取constraints
		java.util.List<EcoreConstraint> constraints = new ArrayList<EcoreConstraint>();
		TableItem[] constraintItems = summaryConstraintsTable.getItems();
		if (constraintItems != null && constraintItems.length > 0) {
			for (TableItem tableItem : constraintItems) {
				EcoreConstraint bcConstraint = new EcoreConstraint();
//				bcConstraint.setId(tableItem.getData("id").toString());
				bcConstraint.setId(tableItem.getData("id")==null?UUID.randomUUID().toString():tableItem.getData("id").toString());
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
			ecoreMessageElement.setId(item.getData("id")==null?UUID.randomUUID().toString():item.getData("id").toString());
			ecoreMessageElement.setMessageComponentId(messageComponentId);
			ecoreMessageElement
					.setName(item.getData("meNameText") == null ? null : item.getData("meNameText").toString());
			ecoreMessageElement
					.setDefinition(item.getData("meDocText") == null ? null : item.getData("meDocText").toString());
			ecoreMessageElement.setMinOccurs(NumberFormatUtil
					.getInt(item.getData("meMinOccursText") == null ? "" : item.getData("meMinOccursText").toString()));
			ecoreMessageElement.setMaxOccurs(NumberFormatUtil
					.getInt(item.getData("meMaxOccursText") == null ? "" : item.getData("meMaxOccursText").toString()));
			ecoreMessageElement.setIsDerived("" + (item.getData("isDerivedCheckButton") == null ? false
					: (boolean) item.getData("isDerivedCheckButton")));
//			item.getData("compositeCheckButton");
			ecoreMessageElement
					.setXmlTag(item.getData("meXmlTagText") == null ? "" : item.getData("meXmlTagText").toString());
			// 1-component 2-dataType
			ecoreMessageElement.setType(item.getData("meType") == null ? "" : item.getData("meType").toString());
			ecoreMessageElement
					.setTypeId(item.getData("meType_id") == null ? "" : item.getData("meType_id").toString());
			ecoreMessageElement.setObjectIdentifier(item.getData("meObjectIdentifierText") == null ? ""
					: item.getData("meObjectIdentifierText").toString());
			ecoreMessageElement.setRegistrationStatus(
					item.getData("meStatusCombo") == null ? "Added Registered" : item.getData("meStatusCombo").toString());
			if (item.getData("meNameText")!=null) {
			name=item.getData("meNameText").toString();	
			}
//			ecoreMessageElement.setTrace(trace);
//			TableItem item1=(TableItem)msgElementTable.getSelection()[0];
//			ecoreMessageElement.setTraceType(traceType);
			
//			Date removalDate;
//			try {
//				removalDate = (item.getData("meRemovalDate") == null) ? null
//						: sdf.parse(item.getData("meRemovalDate").toString());
//			} catch (ParseException e) {
//				removalDate = null;
//			}
//
//			ecoreMessageElement.setRemovalDate(removalDate);

			// 基本信息
//			ecoreMessageElementVO.setEcoreMessageElement(ecoreMessageElement);

//			
			TreeItem[] messageElementsTableTreeItem =this.tableTree.getItems();
//			EcoreMessageElementVO ecoreMessageElementVO1 = new EcoreMessageElementVO();
			
			for (int j = 0; j < messageElementsTableTreeItem.length; j++) {
//				EcoreMessageElementVO ecoreMessageElementVO1 = new EcoreMessageElementVO();
				TreeItem item1 = messageElementsTableTreeItem[j];
//				EcoreMessageElement ecoreMessageElement1 = new EcoreMessageElement();
				if (item.getData("id")!=null&&item.getData("id").toString().equals(item1.getData("me_id").toString())) {
				 
				ecoreMessageElement.setTrace(item1.getData("trace")==null?"":item1.getData("trace").toString());
				ecoreMessageElement.setTechnical(item1.getText(1)==null?"":item1.getText(1).toString());
				ecoreMessageElement.setTracesTo(item1.getText(2)==null?"":item1.getText(2).toString());
				ecoreMessageElement.setTracePath(item1.getText(3)==null?"":item1.getText(3).toString());
				ecoreMessageElement.setTypeOfTracesTo(item1.getData("me_bcTrace")==null?"":item1.getData("me_bcTrace").toString());
				if (item1.getData("Image")!=null) {
				String image=item1.getData("Image").toString();
				if (image!=null&&"2".equals(image)) {
				ecoreMessageElement.setTraceType("2");	
				}else if(image!=null&&"1".equals(image)) {
				ecoreMessageElement.setTraceType("1");	
				}else {
				ecoreMessageElement.setTraceType("");	
				}	
				}
				
				
				}
				
//				ecoreMessageElementVO1.setEcoreMessageElement(ecoreMessageElement);
			}		
			ecoreMessageElementVO.setEcoreMessageElement(ecoreMessageElement);
			java.util.List<String[]> constraintsTableItems = (java.util.List<String[]>) item
					.getData("beConstraintsTableItems");
			java.util.List<EcoreConstraint> beEcoreConstraintList = new ArrayList<EcoreConstraint>();
			if (constraintsTableItems != null && constraintsTableItems.size() > 0) {
				for (int j = 0; j < constraintsTableItems.size(); j++) {
					EcoreConstraint beConstraint = new EcoreConstraint();
					beConstraint.setId(item.getData("id")==null?UUID.randomUUID().toString():beConstraint.getId());
//					beConstraint.setId(UUID.randomUUID().toString());
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
		//me数据发生变化时  重构BusinessTrace表格 
		if (name!=null) {
		changeBusinessTrace(ecoreMessageComponentVO);	
		}
			
		return ecoreMessageComponentVO;
		
	}
	
	private void changeBusinessTrace(EcoreMessageComponentVO ecoreMessageComponentVO2) {
		
		this.msgElementList.clear();
		msgElementList = ecoreMessageComponentVO2.getEcoreMessageElementVOs();
		items = tableTree.getItems();
		for (TreeItem treeItem : items) {
			treeItem.dispose();
		}
		if(msgElementList != null && msgElementList.size() > 0){
			for (EcoreMessageElementVO msgElementVO : msgElementList) {
			item1=new TreeItem(tableTree, SWT.NONE);
			
			EcoreMessageElement msgElement = msgElementVO.getEcoreMessageElement();
			
			//初始化加载表格数据   第一项默认为false 没有任何数据时
			item1.setText(1,msgElement.getTechnical()==null?"false":msgElement.getTechnical());
			item1.setText(2,msgElement.getTracesTo()==null?"":msgElement.getTracesTo());
//			item1.setImage(2,ImgUtil.createImg(ImgUtil.BC));
			item1.setText(3,msgElement.getTracePath()==null?"":msgElement.getTracePath());
			item1.setText(4,msgElement.getTypeOfTracesTo()==null?"":msgElement.getTypeOfTracesTo());
			item1.setData("trace",msgElement.getTechnical());
			if (msgElement.getTypeOfTracesTo()!=null) {
			item1.setData("me_bcTrace",msgElement.getTypeOfTracesTo());	
			}
			
			if ("2".equals(msgElement.getTraceType())) {
				item1.setImage(2,ImgUtil.createImg(ImgUtil.BC));
				item1.setData("Image",2);
			}else if ("1".equals(msgElement.getTraceType())) {
				item1.setImage(2,ImgUtil.createImg(ImgUtil.BC_BE));
				item1.setData("Image",1);
			}else{
				
			}
//			if (item1.getData("Image")!=null&&item1.getData("Image").equals("BC")) {
//			item1.setImage(2,ImgUtil.createImg(ImgUtil.BC));
//			}else {
//				item1.setImage(2,ImgUtil.createImg(ImgUtil.BC_BE));	
//			}
			String appendix = "";
			item1.setData("meLever",1);
			String status = null;
			if (summaryStatusCombo.getText()!=null) {
				status=summaryStatusCombo.getText();
			}
			if ("2".equals(msgElement.getType())) {// 1：datatype，2：消息组件
				EcoreMessageComponent mc = DerbyDaoUtil.getMessageComponentById(msgElement.getTypeId());
				appendix = " [" + msgElement.getMinOccurs() + "," + msgElement.getMaxOccurs() + "]" + " : "
						+ mc.getName();
				//已关联，有数据时
				if (mc.getTechnical().equals("true")) {
				item1.setText(4,"--TECHNICAL--");
				}
				else if (mc.getTraceName()!=null) {
				item1.setText(4,mc.getTraceName());
				}
				
				item1.setData("me_id",msgElement.getId());
				//表格中TypeOfTracesTo取值逻辑,getAll存储时使用
				if (mc.getTechnical().equals("true")) {
				item1.setData("me_bcTrace","--TECHNICAL--");
				}else if (mc.getTraceName()!=null) {
				item1.setData("me_bcTrace",mc.getTraceName());
				}
				item1.setData("mcStatusCombo",mc.getRegistrationStatus());
				if (item1.getData("bcType_id")!=null) {
					item1.setData("trace",item1.getData("bcType_id"));
				}else if (item1.getData("beType_id")!=null) {
					item1.setData("trace",item1.getData("beType_id"));
				}
				
				//取关联mc绑定的bc内容
				if (mc.getTrace()!=null) {
					item1.setData("TypeBcTrace",mc.getTrace());
				}
				
//				}
//				//typeOfTracesTo取当前me的mc中traceName
//				if (mc.getTraceName()!=null) {
//				item1.setText(4,mc.getTraceName());
//				}
				item1.setText(msgElement.getName() + appendix);
				
				if (status==null||status.equals("Provisionally Registered")
						||status.equals("Added Registered")) {
					item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT1));
				}else {
					item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
				}
//				if (mc.getTrace()!=null) {
//					item1.setText(4,mc.getTrace());
//				}
				
				ArrayList<EcoreMessageElement> subMeList = DerbyDaoUtil.getMessageElementList(mc.getId());
				GenerateCommonTree.generateContainmentTreeForBusniessTrace(item1, subMeList);
			} else {
				// datatype
				EcoreDataType dataType = DerbyDaoUtil.getDataTypeById(msgElement.getTypeId());
				appendix = " : " + dataType.getName();
				item1.setText(msgElement.getName() + appendix);
				if (item1.getData("bcType_id")!=null) {
					item1.setData("trace",item1.getData("bcType_id"));
				}else if (item1.getData("beType_id")!=null) {
					item1.setData("trace",item1.getData("beType_id"));
				}
				item1.setText(1,msgElement.getTechnical()==null?"false":msgElement.getTechnical());
				item1.setText(2,msgElement.getTracesTo()==null?"":msgElement.getTracesTo());
				item1.setText(3,msgElement.getTracePath()==null?"":msgElement.getTracePath());
				item1.setText(4,"");
				
				
				//typeOfTracesTo取当前me的mc中traceName
//				item1.setData("typeOfTracesTo",mc.getTraceName());
				if ("1".equals(dataType.getType())) { // codeSet 已到最底层了
					if (status==null||status.equals("Provisionally Registered")
							||status.equals("Added Registered")) {
						item1.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
					}else {
						item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
					}
				} else {
					if (status==null||status.equals("Provisionally Registered")
							||status.equals("Added Registered")) {
						item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT1));
					}else {
						item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
					}
				}
//				item1.setText(1,"false");
				item1.setText(0, msgElement.getName() + appendix);
//				if ("1".equals(dataType.getType())) { // codeSet 已到最底层了
//					if (msgElement.getRegistrationStatus()==null||msgElement.getRegistrationStatus().equals("Provisionally Registered")) {
//						item1.setImage(ImgUtil.createImg(ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK));
//					}else {
//						item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_DATA_TYPE));
//					}
//				} else {
//					if (msgElement.getRegistrationStatus()==null||msgElement.getRegistrationStatus().equals("Provisionally Registered")) {
//						item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT1));
//					}else {
//						item1.setImage(ImgUtil.createImg(ImgUtil.MC_SUB2_COMPONENT));
//					}
//				}
				
			}
		}
		
	}
	}
	private void showMessageElementDetailBar(Composite Composite, ScrolledComposite scrolledComposite) {
		for (Control control: Composite.getChildren()) {
			control.dispose();
		}
		// ------------ Message Element Details Begin ------------------------
		ExpandBar messageElementBar = new ExpandBar(Composite, SWT.V_SCROLL);
		messageElementBar.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		messageElementBar.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		messageElementBar.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));

		Composite messageElementComposite = new Composite(messageElementBar, SWT.NONE);
		messageElementComposite.setLayout(new GridLayout(2, false));

		GridData messageElementGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		messageElementGridData.widthHint = 350;
		Label beNameLabel = new Label(messageElementComposite, SWT.NONE);
		beNameLabel.setText("Name");
		meNameText = new Text(messageElementComposite, SWT.BORDER);
		meNameText.setLayoutData(messageElementGridData);
		meNameText.setEditable(false);
		meNameText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		meNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTableItem.isDisposed()) {
					// 左边的树的名称展示
					String showText = "";
					if (meTypeText.getText().isEmpty()) {
						showText = meNameText.getText() + " [" + meMinOccursText.getText() + ", "
								+ meMaxOccursText.getText() + "]";
					} else {
						showText = meNameText.getText() + " [" + meMinOccursText.getText() + ", "
								+ meMaxOccursText.getText() + "] : " + meTypeText.getText();
					}
					selectedTableItem.setText(showText);

					selectedTableItem.setData("meNameText", meNameText.getText());
					setDirty(true);
				}
			}
		});

		GridData documentationGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		documentationGridData.widthHint = 350;
		documentationGridData.heightHint = 150;
		Label documentationLabel = new Label(messageElementComposite, SWT.NONE);
		documentationLabel.setText("Documentation");
		meDocText = new Text(messageElementComposite, SWT.BORDER | SWT.WRAP);
		meDocText.setLayoutData(documentationGridData);
		meDocText.setEditable(false);
		meDocText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		meDocText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTableItem.isDisposed()) {
					selectedTableItem.setData("meDocText", meDocText.getText());
					setDirty(true);
				}
			}
		});

		Label minOccursLabel = new Label(messageElementComposite, SWT.NONE);
		minOccursLabel.setText("Min Occurs");
		meMinOccursText = new Text(messageElementComposite, SWT.BORDER);
		meMinOccursText.setEditable(false);
		meMinOccursText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		meMinOccursText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTableItem.isDisposed()) {

					String showText = "";
					if (meTypeText.getText().isEmpty()) {
						showText = meNameText.getText() + " [" + meMinOccursText.getText() + ", "
								+ meMaxOccursText.getText() + "]";
					} else {
						showText = meNameText.getText() + " [" + meMinOccursText.getText() + ", "
								+ meMaxOccursText.getText() + "] : " + meTypeText.getText();
					}
					selectedTableItem.setText(showText);

					selectedTableItem.setData("meMinOccursText", meMinOccursText.getText());
					setDirty(true);
				}
			}
		});

		Label MaxOccursLabel = new Label(messageElementComposite, SWT.NONE);
		MaxOccursLabel.setText("Max Occurs");
		meMaxOccursText = new Text(messageElementComposite, SWT.BORDER);
		meMaxOccursText.setEditable(false);
		meMaxOccursText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		meMaxOccursText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTableItem.isDisposed()) {

					String showText = "";
					if (meTypeText.getText().isEmpty()) {
						showText = meNameText.getText() + " [" + meMinOccursText.getText() + ", "
								+ meMaxOccursText.getText() + "]";
					} else {
						showText = meNameText.getText() + " [" + meMinOccursText.getText() + ", "
								+ meMaxOccursText.getText() + "] : " + meTypeText.getText();
					}
					selectedTableItem.setText(showText);

					selectedTableItem.setData("meMaxOccursText", meMaxOccursText.getText());
					setDirty(true);
				}
			}
		});

		Label typeLabel = new Label(messageElementComposite, SWT.NONE);
		typeLabel.setText("Type");
		Composite cmp = new Composite(messageElementComposite, SWT.NONE);
		cmp.setLayoutData(new GridData(350, 40));
		cmp.setLayout(new GridLayout(3, false));

		Label xmlTagLabel = new Label(messageElementComposite, SWT.NONE);
		xmlTagLabel.setText("XML Tag");
		meXmlTagText = new Text(messageElementComposite, SWT.BORDER);
		meXmlTagText.setEditable(false);
		meXmlTagText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		meXmlTagText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTableItem.isDisposed()) {
					selectedTableItem.setData("meXmlTagText", meXmlTagText.getText());
					setDirty(true);
				}
			}
		});

		GridData generalTyperGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		generalTyperGridData.widthHint = 260;

		meTypeText = new Text(cmp, SWT.BORDER);
		meTypeText.setLayoutData(generalTyperGridData);
		meTypeText.setEnabled(false);
		meTypeText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (selectedTableItem != null && !selectedTableItem.isDisposed()) {

					String showText = "";
//					if (meTypeText.getData("meLever") != null) {
//						showText = meNameText.getText() + " [" + meMinOccursText.getText() + ", "
//								+ meMaxOccursText.getText() + "] : " + meTypeText.getText();
//					}
					if (meTypeText.getText().isEmpty()) {
						showText = meNameText.getText() + " [" + meMinOccursText.getText() + ", "
								+ meMaxOccursText.getText() + "]";
					} else {
						showText = meNameText.getText() + " [" + meMinOccursText.getText() + ", "
								+ meMaxOccursText.getText() + "] : " + meTypeText.getText();
					}
					selectedTableItem.setText(showText);

					if (meTypeText.getData("meType_id") != null) {
						selectedTableItem.setData("meType_id", meTypeText.getData("meType_id"));
						selectedTableItem.setData("meType", meTypeText.getData("meType"));
					}
					
					setDirty(true);
				}
//				TreeItem treeItem = elementsTree.getSelection()[0];
//				treeItem.setData("meType_id",meTypeText.getData("meType_id"));
			}
		});
		btBtn = new Button(cmp, SWT.NONE);
		btBtn.setText("choose");
		btBtn.setEnabled(false);
		btBtn.setBackground(new Color(Display.getCurrent(),135,206,250));
		btBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					setDirty(true);
					MessageBoxUtil.createSelectMsgElementType(meTypeText,selectedTableItem);
//					changeTest();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
//				if (elementsTree.getSelectionCount() == 1) {
//					TreeItem treeItem = elementsTree.getSelection()[0];
//					if (meTypeText.getData("meLever")!=null) {
//					String showText = "";
//					showText=meNameText.getText() + " [" + meMinOccursText.getText() + ", "
//						+ meMaxOccursText.getText() + "] : " +meTypeText.getText();
//					treeItem.setText(showText);
//					}
//				}
			}

//			public void changeTest() {
//				if (elementsTree.getSelectionCount() == 1) {
//					TreeItem treeItem = elementsTree.getSelection()[0];
//					if (meTypeText.getData("meLever")!=null) {
//					String showText = "";
//					showText=meNameText.getText() + " [" + meMinOccursText.getText() + ", "
//						+ meMaxOccursText.getText() + "] : " +meTypeText.getText();
//					treeItem.setText(showText);
//					}
//				}
//				
//			}
		});
		Button btSearchBtn = new Button(cmp, SWT.NONE);
		btSearchBtn.setImage(ImgUtil.createImg(ImgUtil.SEARCH_BTN));
		btSearchBtn.setBackground(new Color(Display.getCurrent(),135,206,250));
		btSearchBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
      				if (elementsTree.getSelectionCount() == 1) {
					TreeItem treeItem = elementsTree.getSelection()[0];
					String dataType = (String)treeItem.getData("meType");
					CTabFolder cTabFolder =TreeListView.getTabFolder();
					//跳转到datatype页
					if (dataType.equals("1")) {
						String dataTypeId = (String)treeItem.getData("meType_id");
						Tree firstTabTree = TreeListView.getFirstTabTree();
						TreeItem dataTypeTreeItem = firstTabTree.getItem(0);
						TreeItem[] allTypeItemArray = dataTypeTreeItem.getItems();
						for (TreeItem oneTypeTreeItem: allTypeItemArray) {
							for (TreeItem typeTreeItem: oneTypeTreeItem.getItems()) {
								EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)typeTreeItem.getData("EcoreTreeNode");
								if (ecoreTreeNode.getId().equals(dataTypeId)) {
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
					}else {
						//跳转到
						String msgComponentId = (String)treeItem.getData("meType_id");
//						String DataId = (String)treeItem.getData("meType_id");
						Tree secondTabTree = TreeListView.getSecondTabTree();
						TreeItem mcTreeItem = secondTabTree.getItem(1);
						for (TreeItem mcItem: mcTreeItem.getItems()) {
							EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)mcItem.getData("EcoreTreeNode");
							//选择后跳转
							if (meTypeText.getData("meLever")!=null) {
//								treeItem.setText(meNameText.getText() + " [" + meMinOccursText.getText() + ", "
//										+ meMaxOccursText.getText() + "] : " +meTypeText.getText());
								String meComponentId = (String) meTypeText.getData("meType_id");
							if (ecoreTreeNode.getId().equals(meComponentId)) {
								TransferDataBean transferDataBean = new TransferDataBean();
								transferDataBean.setId(ecoreTreeNode.getId());
								transferDataBean.setName(ecoreTreeNode.getName());
								transferDataBean.setLevel(ecoreTreeNode.getLevel());
								transferDataBean.setType((String)mcItem.getData("type"));
								transferDataBean.setChildId(mcItem.getData("childId") == null ? "" : (String)mcItem.getData("childId"));
								String status=ecoreTreeNode.getRegistrationStatus();
//								if (status==null||status.equals("Provisionally Registered")) {
								if("1".equals(ecoreTreeNode.getType())) {
									if (status==null||status.equals("Provisionally Registered")||status.equals("Added Registered")) {
										transferDataBean.setImgPath(ImgUtil.MC_SUB3_COMPONENT);	
									}else {
										transferDataBean.setImgPath(ImgUtil.MC_SUB1_COMPONENT);
									}
								}else if("2".equals(ecoreTreeNode.getType())){
									if (status==null||status.equals("Provisionally Registered")||status.equals("Added Registered")) {
										transferDataBean.setImgPath(ImgUtil.MC_SUB1_CHOICE1);	
									}else {
										transferDataBean.setImgPath(ImgUtil.MC_SUB1_CHOICE);
									}
								}
								transferDataBean.setTreeListItem((TreeListItem)mcItem);
								cTabFolder.setSelection(1);
								secondTabTree.setSelection(mcItem);
								EditorUtil.open(transferDataBean.getType(), transferDataBean);
								return;
							}
							}
							
							else if (ecoreTreeNode.getId().equals(msgComponentId)) {
								TransferDataBean transferDataBean = new TransferDataBean();
								transferDataBean.setId(ecoreTreeNode.getId());
								transferDataBean.setName(ecoreTreeNode.getName());
								transferDataBean.setLevel(ecoreTreeNode.getLevel());
								transferDataBean.setType((String)mcItem.getData("type"));
								transferDataBean.setChildId(mcItem.getData("childId") == null ? "" : (String)mcItem.getData("childId"));
								String status=ecoreTreeNode.getRegistrationStatus();
//								if (status==null||status.equals("Provisionally Registered")) {
								if("1".equals(ecoreTreeNode.getType())) {
									if (status==null||status.equals("Provisionally Registered")||status.equals("Added Registered")) {
										transferDataBean.setImgPath(ImgUtil.MC_SUB3_COMPONENT);	
									}else {
										transferDataBean.setImgPath(ImgUtil.MC_SUB1_COMPONENT);
									}
								}else if("2".equals(ecoreTreeNode.getType())){
									if (status==null||status.equals("Provisionally Registered")||status.equals("Added Registered")) {
										transferDataBean.setImgPath(ImgUtil.MC_SUB1_CHOICE1);	
									}else {
										transferDataBean.setImgPath(ImgUtil.MC_SUB1_CHOICE);
									}
								}
								transferDataBean.setTreeListItem((TreeListItem)mcItem);
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

		Label isDerivedLabel = new Label(messageElementComposite, SWT.NONE);
		isDerivedLabel.setText("Derived");
		isDerivedCheckButton = new Button(messageElementComposite, SWT.CHECK);
		isDerivedCheckButton.setEnabled(false);
		isDerivedCheckButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (!selectedTableItem.isDisposed()) {
					setDirty(true);
					selectedTableItem.setData("isDerivedCheckButton", isDerivedCheckButton.getSelection());
				}
			}
		});

		Label compositeLabel = new Label(messageElementComposite, SWT.NONE);
		compositeLabel.setText("Composite");
		compositeCheckButton = new Button(messageElementComposite, SWT.CHECK);
		compositeCheckButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (!selectedTableItem.isDisposed()) {
					setDirty(true);
					selectedTableItem.setData("compositeCheckButton", compositeCheckButton.getSelection());
				}
			}
		});

		ExpandItem messageElementExpandItem = new ExpandItem(messageElementBar, SWT.NONE);
		messageElementExpandItem.setText("Message Element Details");
		messageElementExpandItem.setHeight(340);
		messageElementExpandItem.setExpanded(true);
		messageElementExpandItem.setControl(messageElementComposite);

		// ------------ Message Element Details End ------------------------
		// -----------------------CMP Information Group Begin -----------------------

		Composite cmpInformationComposite = new Composite(messageElementBar, SWT.NONE);
		cmpInformationComposite.setLayout(new GridLayout(2, false));
		Label objectIdentifierLabel = new Label(cmpInformationComposite, SWT.NONE);
		objectIdentifierLabel.setText("Object Identifier");
		meObjectIdentifierText = new Text(cmpInformationComposite, SWT.BORDER);
		meObjectIdentifierText.setEditable(false);
		meObjectIdentifierText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
//		meObjectIdentifierText.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseUp(MouseEvent e) {
//				if (!selectedTableItem.isDisposed()) {
//					setDirty(true);
//					selectedTableItem.setData("meObjectIdentifierText", meObjectIdentifierText.getText());
//				}
//			}
//		});
		GridData objectIdentifierGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		objectIdentifierGridData.widthHint = 350;
		meObjectIdentifierText.setLayoutData(objectIdentifierGridData);

		ExpandItem cmpInformationItem = new ExpandItem(messageElementBar, SWT.NONE);
		cmpInformationItem.setText("CMP Information");
		cmpInformationItem.setHeight(90);
		cmpInformationItem.setExpanded(true);
		cmpInformationItem.setControl(cmpInformationComposite);

		// -----------------------CMP Information Group End -----------------------

		// -----------------------

		// ---------- Synonyms Expand Item Begin -------------------------------------
		Composite synonymsComposite = new Composite(messageElementBar, SWT.NONE);
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
				setDirty(true);
				MessageBoxUtil.createSynonymsDialogue(meSynonymsTable, selectedTableItem);
			}
		});

		deleteButtonForSynonyms = new Button(synonymsToolbarComposite, SWT.NONE);
		deleteButtonForSynonyms.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
		deleteButtonForSynonyms.setEnabled(false);
		deleteButtonForSynonyms.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				MessageBoxUtil.deleteSynonymDialogue(meSynonymsTable, selectedTableItem);
			}
		});

		meSynonymsTable = new Table(synonymsComposite, SWT.BORDER);
		meSynonymsTable.setLayoutData(tableGridData);
		meSynonymsTable.setHeaderVisible(true);
		meSynonymsTable.setLinesVisible(true);

		TableColumn contextColumn = new TableColumn(meSynonymsTable, SWT.NONE);
		contextColumn.setWidth(100);
		contextColumn.setText("Context");
		TableColumn SynonymsColumn = new TableColumn(meSynonymsTable, SWT.NONE);
		SynonymsColumn.setWidth(100);
		SynonymsColumn.setText("Synonyms");

		ExpandItem synonymsItem = new ExpandItem(messageElementBar, SWT.NONE);
		synonymsItem.setText("Synonyms");
		synonymsItem.setHeight(250);
		synonymsItem.setExpanded(true);
		synonymsItem.setControl(synonymsComposite);
		// ---------- Synonyms Expand Item End -------------------------------------

		// ---------- Constraints Expand Item Begin
		// -------------------------------------
		Composite contraintsComposite = new Composite(messageElementBar, SWT.NONE);
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
				setDirty(true);
				MessageBoxUtil.createConstraintDialogueForBC("add", meConstraintsTable, selectedTableItem);
			}
		});

		editConstraintBtn1 = new Button(toolbarComposite, SWT.NONE);
		editConstraintBtn1.setImage(ImgUtil.createImg(ImgUtil.EDIT_IMG_TOOLBAR));
		editConstraintBtn1.setEnabled(false);
		editConstraintBtn1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (meConstraintsTable.getSelectionCount() > 0) {
					setDirty(true);
					MessageBoxUtil.createConstraintDialogueForBC("edit", meConstraintsTable, selectedTableItem);
				}
			}
		});

		deleteConstraintBtn1 = new Button(toolbarComposite, SWT.NONE);
		deleteConstraintBtn1.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
		deleteConstraintBtn1.setEnabled(false);
		deleteConstraintBtn1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				MessageBoxUtil.deleteConstraintForBC(meConstraintsTable, selectedTableItem);
			}
		});

		meConstraintsTable = new Table(contraintsComposite, SWT.BORDER);
		meConstraintsTable.setHeaderVisible(true);
		meConstraintsTable.setLinesVisible(true);
		TableColumn nameColumn = new TableColumn(meConstraintsTable, SWT.NONE);
		nameColumn.setWidth(100);
		nameColumn.setText("Name");
		TableColumn defifinitionColumn = new TableColumn(meConstraintsTable, SWT.NONE);
		defifinitionColumn.setWidth(100);
		defifinitionColumn.setText("Definition");
		TableColumn expressionLanguageColumn = new TableColumn(meConstraintsTable, SWT.NONE);
		expressionLanguageColumn.setWidth(100);
		expressionLanguageColumn.setText("Expression Language");
		TableColumn expressionColumn = new TableColumn(meConstraintsTable, SWT.NONE);
		expressionColumn.setWidth(200);
		expressionColumn.setText("Expression");

		GridData constraintsTableGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		constraintsTableGridData.widthHint = 430;
		constraintsTableGridData.heightHint = 150;
		meConstraintsTable.setLayoutData(constraintsTableGridData);

		ExpandItem contraintsItem = new ExpandItem(messageElementBar, SWT.NONE);
		contraintsItem.setText("Contraints");
		contraintsItem.setHeight(250);
		contraintsItem.setExpanded(true);
		contraintsItem.setControl(contraintsComposite);
		
		Composite.requestLayout();
		
		scrolledComposite.setMinSize(Composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		// ---------- Constraints Expand Item End -------------------------------------
	}
	private void showCodeDetailBar(Composite rightComposite, ScrolledComposite scrolledComposite) {
		for (Control control: rightComposite.getChildren()) {
			control.dispose();
		}
		// ------------ Code Details Begin ---------------------------------
		ExpandBar codeDetailBar = new ExpandBar(rightComposite, SWT.V_SCROLL);
		codeDetailBar.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		codeDetailBar.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		codeDetailBar.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));

		Composite codeDetailComposite = new Composite(codeDetailBar, SWT.NONE);
		codeDetailComposite.setLayout(new GridLayout(2, false));

		GridData messageElementGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		messageElementGridData.widthHint = 350;
		Label beNameLabel = new Label(codeDetailComposite, SWT.NONE);
		beNameLabel.setText("Name");
		nameText = new Text(codeDetailComposite, SWT.BORDER);
		nameText.setEditable(false);
		nameText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		nameText.setLayoutData(messageElementGridData);
//		
//		nameText.addModifyListener(new ModifyListener() {
//			@Override
//			public void modifyText(ModifyEvent e) {
//				if (!selectedTableItem.isDisposed()) {
//					selectedTableItem.setData("CodesName", nameText.getText());
//					setDirty(true);
//				}
//			}
//		});
		
		Label codeDocumentLabel = new Label(codeDetailComposite, SWT.NONE);
		codeDocumentLabel.setText("Documentation");
		
		GridData documentationGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		documentationGridData.widthHint = 350;
		documentationGridData.heightHint = 150;
		
		codeDocumentText = new Text(codeDetailComposite, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
		codeDocumentText.setLayoutData(documentationGridData);
		codeDocumentText.setEditable(false);
		codeDocumentText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
//		codeDocumentText.addModifyListener(new ModifyListener() {
//			@Override
//			public void modifyText(ModifyEvent e) {
//				if (!selectedTableItem.isDisposed()) {
//					selectedTableItem.setData("Document", codeDocumentText.getText());
//					setDirty(true);
//				}
//			}
//		});
		
		Label codeNameLabel = new Label(codeDetailComposite, SWT.NONE);
		codeNameLabel.setText("CodeName");
		codeNameText = new Text(codeDetailComposite, SWT.BORDER);
		codeNameText.setLayoutData(messageElementGridData);
		codeNameText.setEditable(false);
		codeNameText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
//		codeNameText.addModifyListener(new ModifyListener() {
//			@Override
//			public void modifyText(ModifyEvent e) {
//				if (!selectedTableItem.isDisposed()) {
//					selectedTableItem.setData("CodeName", codeNameText.getText());
//					setDirty(true);
//				}
//			}
//		});
		
		ExpandItem codeDetailItem = new ExpandItem(codeDetailBar, SWT.NONE);
		codeDetailItem.setText("Code Detail");
		codeDetailItem.setHeight(300);
		codeDetailItem.setExpanded(true);
		codeDetailItem.setControl(codeDetailComposite);
		
		// ------------ Code Details End ---------------------------------
		// -----------------------CMP Information Group Begin -----------------------

		Composite cmpInformationComposite = new Composite(codeDetailBar, SWT.NONE);
		cmpInformationComposite.setLayout(new GridLayout(2, false));
		Label objectIdentifierLabel = new Label(cmpInformationComposite, SWT.NONE);
		objectIdentifierLabel.setText("Object Identifier");
		meObjectIdentifierText = new Text(cmpInformationComposite, SWT.BORDER);
		meObjectIdentifierText.setEditable(false);
		meObjectIdentifierText.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
//		meObjectIdentifierText.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseUp(MouseEvent e) {
//				if (!selectedTableItem.isDisposed()) {
//					setDirty(true);
//					selectedTableItem.setData("meObjectIdentifierText", meObjectIdentifierText.getText());
//				}
//			}
//		});
		GridData objectIdentifierGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		objectIdentifierGridData.widthHint = 350;
		meObjectIdentifierText.setLayoutData(objectIdentifierGridData);

		ExpandItem cmpInformationItem = new ExpandItem(codeDetailBar, SWT.NONE);
		cmpInformationItem.setText("CMP Information");
		cmpInformationItem.setHeight(90);
		cmpInformationItem.setExpanded(true);
		cmpInformationItem.setControl(cmpInformationComposite);

		// -----------------------CMP Information Group End -----------------------
		// ---------- Constraints Expand Item Begin
		// -------------------------------------
		Composite contraintsComposite = new Composite(codeDetailBar, SWT.NONE);
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
				setDirty(true);
				MessageBoxUtil.createConstraintDialogueForBC("add", meConstraintsTable, selectedTableItem);
			}
		});

		editConstraintBtn1 = new Button(toolbarComposite, SWT.NONE);
		editConstraintBtn1.setImage(ImgUtil.createImg(ImgUtil.EDIT_IMG_TOOLBAR));
		editConstraintBtn1.setEnabled(false);
		editConstraintBtn1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (meConstraintsTable.getSelectionCount() > 0) {
					setDirty(true);
					MessageBoxUtil.createConstraintDialogueForBC("edit", meConstraintsTable, selectedTableItem);
				}
			}
		});

		deleteConstraintBtn1 = new Button(toolbarComposite, SWT.NONE);
		deleteConstraintBtn1.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
		deleteConstraintBtn1.setEnabled(false);
		deleteConstraintBtn1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				setDirty(true);
				MessageBoxUtil.deleteConstraintForBC(meConstraintsTable, selectedTableItem);
			}
		});

		meConstraintsTable = new Table(contraintsComposite, SWT.BORDER);
		meConstraintsTable.setHeaderVisible(true);
		meConstraintsTable.setLinesVisible(true);
		TableColumn nameColumn = new TableColumn(meConstraintsTable, SWT.NONE);
		nameColumn.setWidth(100);
		nameColumn.setText("Name");
		TableColumn defifinitionColumn = new TableColumn(meConstraintsTable, SWT.NONE);
		defifinitionColumn.setWidth(100);
		defifinitionColumn.setText("Definition");
		TableColumn expressionLanguageColumn = new TableColumn(meConstraintsTable, SWT.NONE);
		expressionLanguageColumn.setWidth(100);
		expressionLanguageColumn.setText("Expression Language");
		TableColumn expressionColumn = new TableColumn(meConstraintsTable, SWT.NONE);
		expressionColumn.setWidth(200);
		expressionColumn.setText("Expression");
		GridData constraintsTableGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		
		constraintsTableGridData.widthHint = 430;
		constraintsTableGridData.heightHint = 150;
		meConstraintsTable.setLayoutData(constraintsTableGridData);

		ExpandItem contraintsItem = new ExpandItem(codeDetailBar, SWT.NONE);
		contraintsItem.setText("Contraints");
		contraintsItem.setHeight(500);
		contraintsItem.setExpanded(true);
		contraintsItem.setControl(contraintsComposite);
		// ---------- Constraints Expand Item End -------------------------------------
		rightComposite.requestLayout();
		
		scrolledComposite.setMinSize(rightComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
	}
	public void changeStatus() {
		if (ecoreMessageComponentVO.getEcoreMessageComponent()==null) {
		}else {
		if (ecoreMessageComponentVO.getEcoreMessageComponent().getRegistrationStatus()==null) {
			
		}else {
		String status=ecoreMessageComponentVO.getEcoreMessageComponent().getRegistrationStatus();
		if (status.equals("Registered")||status.equals("Provisionally Registered")||status.equals("Obsolete")) {
		changeEditor();
		}else if (status.equals("Added Registered")) {
//			renameButton.setEnabled(true);
		}
	}
	}
	}
}
