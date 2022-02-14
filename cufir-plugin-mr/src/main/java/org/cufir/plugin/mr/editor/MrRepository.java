package org.cufir.plugin.mr.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.cufir.plugin.mr.MrHelper;
import org.cufir.plugin.mr.bean.MrTreeItem;
import org.cufir.plugin.mr.bean.TreeMenuEnum;
import org.cufir.plugin.mr.handlers.MrRepositoryInitProcess;
import org.cufir.plugin.mr.utils.SystemUtil;
import org.cufir.plugin.mr.view.BusinessModelTreeClickListener;
import org.cufir.plugin.mr.view.MessageComponentsTreeClickListener;
import org.cufir.plugin.mr.view.MessagesTreeClickListener;
import org.cufir.plugin.mr.view.MrTreeViewHelper;
import org.cufir.s.data.IAnalysisProcessMonitor;
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
import org.cufir.s.data.bean.EcoreSemanticMarkup;
import org.cufir.s.data.bean.EcoreSemanticMarkupElement;
import org.cufir.s.data.vo.EcoreTreeNode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.cfets.cufir.s.ide.utils.i18n.I18nApi;

/**
 * 数据仓库
 * @author tangmaoquan_ntt
 * @since 3.2.1.0
 * @Date 2021年10月28日
 */
public class MrRepository{

	private static MrRepository instance = new MrRepository();
	
	private MrRepository() {}
	
	public static MrRepository get() {
		return instance;
	}
	
	public Tree businessModelTabTree;
	public Tree messageComponentsTabTree;
	public Tree messagesTabTree;
	public CTabFolder tabFolder;
	
	public Set<String> tab1ComboStrList = new HashSet<String>();
	public Set<String> tab2ComboStrList = new HashSet<String>();
	public Set<String> tab3ComboStrList = new HashSet<String>();
	public Set<String> tab4ComboStrList = new HashSet<String>();

	public Map<String, Map<String, Object>> tab1ComboMap = new HashMap<String, Map<String, Object>>();
	public Map<String, Map<String, Object>> tab2ComboMap = new HashMap<String, Map<String, Object>>();
	public Map<String, Map<String, Object>> tab3ComboMap = new HashMap<String, Map<String, Object>>();
	
	public EcoreTreeNode dtTreeNodes = new EcoreTreeNode();
	public EcoreTreeNode bcTreeNodes = new EcoreTreeNode();
	public EcoreTreeNode mcTreeNodes = new EcoreTreeNode();
	public EcoreTreeNode esTreeNodes = new EcoreTreeNode();
	public List<EcoreTreeNode> emTreeNodes = new ArrayList<>();
	
	public List<EcoreBusinessComponent> ecoreBusinessComponents;
	public List<EcoreBusinessElement> ecoreBusinessElements;
	public List<EcoreCode> ecoreCodes;
	public List<EcoreConstraint> ecoreConstraints;
	public List<EcoreDataType> ecoreDataTypes;
	public List<EcoreExample> ecoreExamples;
	public List<EcoreExternalSchema> ecoreExternalSchemas;
	public List<EcoreMessageBuildingBlock> ecoreMessageBuildingBlocks;
	public List<EcoreMessageComponent> ecoreMessageComponents;
	public List<EcoreMessageDefinition> ecoreMessageDefinitions;
	public List<EcoreMessageElement> ecoreMessageElements;
	public List<EcoreMessageSet> ecoreMessageSets;
	public List<EcoreNextVersions> ecoreNextVersionses;
	public List<EcoreSemanticMarkup> ecoreSemanticMarkups;
	public List<EcoreSemanticMarkupElement> ecoreSemanticMarkupElements;
	
	public Map<String, EcoreBusinessComponent> ecoreBusinessComponentMapById;
	public Map<String, EcoreDataType> ecoreDataTypeMapById;
	public Map<String, EcoreExternalSchema> ecoreExternalSchemaMapById;
	public Map<String, EcoreMessageComponent> ecoreMessageComponentMapById;
	public Map<String, EcoreMessageDefinition> ecoreMessageDefinitionMapById;
	public Map<String, EcoreMessageElement> ecoreMessageElementMapById;
	public Map<String, EcoreNextVersions> ecoreNextVersionsMapByNextVersionId;
	public Map<String, List<EcoreBusinessElement>> ecoreBusinessElementMapByBusinessComponentId;
	public Map<String, List<EcoreCode>> ecoreCodeMapByCodeSetId;
	public Map<String, List<EcoreConstraint>> ecoreConstraintMapByObjId;
	public Map<String, List<EcoreExample>> ecoreExampleMapByObjId;
	public Map<String, List<EcoreMessageElement>> ecoreMessageElementMapByMsgComponentId;
	public Map<String, List<EcoreNextVersions>> ecoreNextVersionsMapById;
	
	public final String ECORE_BUSINESS_COMPONENT = "EcoreBusinessComponent";
	public final String ECORE_BUSINESS_ELEMENT = "EcoreBusinessElement";
	public final String ECORE_CODE = "EcoreCode";
	public final String ECORE_CONSTRAINT = "EcoreConstraint";
	public final String ECORE_DATA_TYPE = "EcoreDataType";
	public final String ECORE_EXAMPLE = "EcoreExample";
	public final String ECORE_EXTERNAL_SCHEMA = "EcoreExternalSchema";
	public final String ECORE_MESSAGE_BUILDING_BLOCK = "EcoreMessageBuildingBlock";
	public final String ECORE_MESSAGE_COMPONENT = "EcoreMessageComponent";
	public final String ECORE_MESSAGE_DEFINITION = "EcoreMessageDefinition";
	public final String ECORE_MESSAGE_ELEMENT = "EcoreMessageElement";
	public final String ECORE_MESSAGE_SET = "EcoreMessageSet";
	public final String ECORE_NEXT_VERSIONS = "EcoreNextVersions";
	public final String ECORE_SEMANTIC_MARKUP = "EcoreSemanticMarkup";
	public final String ECORE_SEMANTIC_MARKUP_ELEMENT = "EcoreSemanticMarkupElement";
	
	/**
	 * 启动初始化内存
	 * @param iapm
	 * @param summaryProgressComposite
	 */
	public void init(MrRepositoryInitProcess iapm, ProgressBar pb) {
		try {
			//同步更新进度条
			pb.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					//初始化缓存数据(加载时间最长)
					initData(iapm);
					setNodes();
					iapm.end();
				}
			});
			getNodes();
		} catch (Exception e) {
			SystemUtil.handle(e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 导入重置内存
	 * @param iapm
	 * @param summaryProgressComposite
	 */
	public void init() {
		try {
			initData();
			getNodes();
			setNodes();
		} catch (Exception e) {
			SystemUtil.handle(e);
			e.printStackTrace();
		}
	}
	
	private void getNodes() throws Exception {
		//初始化树节点数据
		dtTreeNodes = MrImplManager.get().getEcoreDataTypeImpl().findEcoreDataTypeTreeNodes();
		bcTreeNodes = MrImplManager.get().getEcoreBusinessComponentImpl().findEcoreBusinessComponentTreeNodes();
		mcTreeNodes = MrImplManager.get().getEcoreMessageComponentImpl().findEcoreMessageComponentTreeNodes();
		emTreeNodes = MrImplManager.get().getEcoreMessageDefinitionImpl().findEcoreMessageTreeNodes();
		List<Map<String, Object>> ess = MrImplManager.get().getEcoreExternalSchemaImpl().findTreeNodes();			
		esTreeNodes=new EcoreTreeNode();
		List<EcoreTreeNode> externalSchemas=new ArrayList<EcoreTreeNode>();
		ess.forEach(es ->{
			EcoreTreeNode po =new EcoreTreeNode();
        	po.setId(es.get("ID") + "");
        	po.setName(es.get("NAME") + "");
        	po.setLevel("2");
        	po.setRegistrationStatus(es.get("REGISTRATION_STATUS") + "");
        	externalSchemas.add(po);
		});
		
		//顶级
		esTreeNodes.setName("External Schemas");
		esTreeNodes.setLevel("1");
		esTreeNodes.setType("3");
		esTreeNodes.setChildNodes(externalSchemas);
	}
	
	private void setNodes() {
		//初始化树数据(最后加载，全部加载完结束)
		tabFolder = new CTabFolder(new Shell(), SWT.BOTTOM | SWT.FLAT);
		tabFolder.setSimple(false);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		initBusinessModelTab(I18nApi.get("tree.bm.name"), dtTreeNodes, bcTreeNodes);
		initMessageComponentsTab(I18nApi.get("tree.mc.name"), esTreeNodes, mcTreeNodes);
		initMessagesTab(I18nApi.get("tree.ms.name"), emTreeNodes);
	}
	
	/**
	 * 批量重置内存数据
	 * @param key
	 */
	public void reset(String... keys) {
		for (String key : keys) {
			reset(key);
		}
	}
	
	/**
	 * 重置内存数据
	 * @param key
	 */
	public void reset(String key) {
		switch(key){
			case ECORE_BUSINESS_COMPONENT:
				ecoreBusinessComponents = MrImplManager.get().getEcoreBusinessComponentImpl().findAll();
				ecoreBusinessComponentMapById = MrHelper.getBusinessComponentMapById(ecoreBusinessComponents);
				break;
			case ECORE_BUSINESS_ELEMENT:
				ecoreBusinessElements = MrImplManager.get().getEcoreBusinessElementImpl().findAll();
				ecoreBusinessElementMapByBusinessComponentId = MrHelper.getBusinessElementMapByBusinessComponentId(ecoreBusinessElements);
				break;
			case ECORE_CODE:
				ecoreCodes = MrImplManager.get().getEcoreCodeImpl().findAll();
				ecoreCodeMapByCodeSetId = MrHelper.getCodeMapByCodeSetId(ecoreCodes);
				break;
			case ECORE_CONSTRAINT:
				ecoreConstraints = MrImplManager.get().getEcoreConstraintImpl().findAll();
				ecoreConstraintMapByObjId = MrHelper.getConstraintMapByObjId(ecoreConstraints);
				break;
			case ECORE_DATA_TYPE:
				ecoreDataTypes = MrImplManager.get().getEcoreDataTypeImpl().findAll();
				ecoreDataTypeMapById = MrHelper.getDataTypeMapById(ecoreDataTypes);
				break;
			case ECORE_EXAMPLE:
				ecoreExamples = MrImplManager.get().getEcoreExampleImpl().findAll();
				ecoreExampleMapByObjId = MrHelper.getExampleMapByObjId(ecoreExamples);
				break;
			case ECORE_EXTERNAL_SCHEMA:
				ecoreExternalSchemas = MrImplManager.get().getEcoreExternalSchemaImpl().findAll();
				ecoreExternalSchemaMapById = MrHelper.getExternalSchemaMapById(ecoreExternalSchemas);
				break;
			case ECORE_MESSAGE_BUILDING_BLOCK:
				ecoreMessageBuildingBlocks = MrImplManager.get().getEcoreMessageBuildingBlockImpl().findAll();
				break;
			case ECORE_MESSAGE_COMPONENT:
				ecoreMessageComponents = MrImplManager.get().getEcoreMessageComponentImpl().findAll();
				ecoreMessageComponentMapById = MrHelper.getMessageComponentMapById(ecoreMessageComponents);
				break;
			case ECORE_MESSAGE_DEFINITION:
				ecoreMessageDefinitions = MrImplManager.get().getEcoreMessageDefinitionImpl().findAll();
				ecoreMessageDefinitionMapById = MrHelper.getMessageDefinitionMapById(ecoreMessageDefinitions);
				break;
			case ECORE_MESSAGE_ELEMENT:
				ecoreMessageElements = MrHelper.getMsgElementList();
				ecoreMessageElementMapByMsgComponentId = MrHelper.getMsgElementMapByMsgComponentId(ecoreMessageElements);
				ecoreMessageElementMapById = MrHelper.getMsgElementMapById(ecoreMessageElements);
				break;
			case ECORE_MESSAGE_SET:
				ecoreMessageSets = MrImplManager.get().getEcoreMessageSetImpl().findAll();
				break;
			case ECORE_NEXT_VERSIONS:
				ecoreNextVersionses = MrImplManager.get().getEcoreNextVersionsImpl().findAll();
				ecoreNextVersionsMapByNextVersionId = MrHelper.getNextVersionsMapByNextVersionId(ecoreNextVersionses);
				ecoreNextVersionsMapById = MrHelper.getNextVersionsMapById(ecoreNextVersionses);
				break;
			case ECORE_SEMANTIC_MARKUP:
				ecoreSemanticMarkups = MrImplManager.get().getEcoreSemanticMarkupImpl().findAll();
				break;
			case ECORE_SEMANTIC_MARKUP_ELEMENT:
				ecoreSemanticMarkupElements = MrImplManager.get().getEcoreSemanticMarkupElementImpl().findAll();
				break;
			default:
				break;
		}
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(IAnalysisProcessMonitor iapm) {
		ecoreBusinessComponents = MrImplManager.get().getEcoreBusinessComponentImpl().findAll();
		iapm.info("EcoreBusinessComponent init OK", 5);
		ecoreBusinessElements = MrImplManager.get().getEcoreBusinessElementImpl().findAll();
		iapm.info("EcoreBusinessElement init OK", 10);
		ecoreCodes = MrImplManager.get().getEcoreCodeImpl().findAll();
		iapm.info("EcoreCode init OK", 15);
		ecoreConstraints = MrImplManager.get().getEcoreConstraintImpl().findAll();
		iapm.info("EcoreConstraint init OK", 20);
		ecoreDataTypes = MrImplManager.get().getEcoreDataTypeImpl().findAll();
		iapm.info("EcoreDataType init OK", 25);
		ecoreExamples = MrImplManager.get().getEcoreExampleImpl().findAll();
		iapm.info("EcoreExample init OK", 40);
		ecoreExternalSchemas = MrImplManager.get().getEcoreExternalSchemaImpl().findAll();
		iapm.info("EcoreExternalSchema init OK", 50);
		ecoreMessageBuildingBlocks = MrImplManager.get().getEcoreMessageBuildingBlockImpl().findAll();
		iapm.info("EcoreMessageBuildingBlock init OK", 60);
		ecoreMessageComponents = MrImplManager.get().getEcoreMessageComponentImpl().findAll();
		iapm.info("EcoreMessageComponent init OK", 65);
		ecoreMessageDefinitions = MrImplManager.get().getEcoreMessageDefinitionImpl().findAll();
		iapm.info("EcoreMessageDefinition init OK", 70);
		ecoreMessageElements = MrHelper.getMsgElementList();
		iapm.info("EcoreMessageElement init OK", 75);
		ecoreMessageSets = MrImplManager.get().getEcoreMessageSetImpl().findAll();
		iapm.info("EcoreMessageSet init OK", 80);
		ecoreNextVersionses = MrImplManager.get().getEcoreNextVersionsImpl().findAll();
		iapm.info("EcoreNextVersions init OK", 85);
		ecoreSemanticMarkups = MrImplManager.get().getEcoreSemanticMarkupImpl().findAll();
		iapm.info("EcoreSemanticMarkup init OK", 90);
		ecoreSemanticMarkupElements = MrImplManager.get().getEcoreSemanticMarkupElementImpl().findAll();
		iapm.info("EcoreSemanticMarkupElement init OK", 95);
		
		ecoreBusinessComponentMapById = MrHelper.getBusinessComponentMapById(ecoreBusinessComponents);
		ecoreBusinessElementMapByBusinessComponentId = MrHelper.getBusinessElementMapByBusinessComponentId(ecoreBusinessElements);
		ecoreCodeMapByCodeSetId = MrHelper.getCodeMapByCodeSetId(ecoreCodes);
		ecoreConstraintMapByObjId = MrHelper.getConstraintMapByObjId(ecoreConstraints);
		ecoreDataTypeMapById = MrHelper.getDataTypeMapById(ecoreDataTypes);
		ecoreExampleMapByObjId = MrHelper.getExampleMapByObjId(ecoreExamples);
		ecoreExternalSchemaMapById = MrHelper.getExternalSchemaMapById(ecoreExternalSchemas);
		ecoreMessageComponentMapById = MrHelper.getMessageComponentMapById(ecoreMessageComponents);
		ecoreMessageDefinitionMapById = MrHelper.getMessageDefinitionMapById(ecoreMessageDefinitions);
		ecoreMessageElementMapById = MrHelper.getMsgElementMapById(ecoreMessageElements);
		ecoreMessageElementMapByMsgComponentId = MrHelper.getMsgElementMapByMsgComponentId(ecoreMessageElements);
		ecoreNextVersionsMapByNextVersionId = MrHelper.getNextVersionsMapByNextVersionId(ecoreNextVersionses);
		ecoreNextVersionsMapById = MrHelper.getNextVersionsMapById(ecoreNextVersionses);
		iapm.info("Success", 100);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		ecoreBusinessComponents = MrImplManager.get().getEcoreBusinessComponentImpl().findAll();
		ecoreBusinessElements = MrImplManager.get().getEcoreBusinessElementImpl().findAll();
		ecoreCodes = MrImplManager.get().getEcoreCodeImpl().findAll();
		ecoreConstraints = MrImplManager.get().getEcoreConstraintImpl().findAll();
		ecoreDataTypes = MrImplManager.get().getEcoreDataTypeImpl().findAll();
		ecoreExamples = MrImplManager.get().getEcoreExampleImpl().findAll();
		ecoreExternalSchemas = MrImplManager.get().getEcoreExternalSchemaImpl().findAll();
		ecoreMessageBuildingBlocks = MrImplManager.get().getEcoreMessageBuildingBlockImpl().findAll();
		ecoreMessageComponents = MrImplManager.get().getEcoreMessageComponentImpl().findAll();
		ecoreMessageDefinitions = MrImplManager.get().getEcoreMessageDefinitionImpl().findAll();
		ecoreMessageElements = MrHelper.getMsgElementList();
		ecoreMessageSets = MrImplManager.get().getEcoreMessageSetImpl().findAll();
		ecoreNextVersionses = MrImplManager.get().getEcoreNextVersionsImpl().findAll();
		ecoreSemanticMarkups = MrImplManager.get().getEcoreSemanticMarkupImpl().findAll();
		ecoreSemanticMarkupElements = MrImplManager.get().getEcoreSemanticMarkupElementImpl().findAll();
		
		ecoreBusinessComponentMapById = MrHelper.getBusinessComponentMapById(ecoreBusinessComponents);
		ecoreBusinessElementMapByBusinessComponentId = MrHelper.getBusinessElementMapByBusinessComponentId(ecoreBusinessElements);
		ecoreCodeMapByCodeSetId = MrHelper.getCodeMapByCodeSetId(ecoreCodes);
		ecoreConstraintMapByObjId = MrHelper.getConstraintMapByObjId(ecoreConstraints);
		ecoreDataTypeMapById = MrHelper.getDataTypeMapById(ecoreDataTypes);
		ecoreExampleMapByObjId = MrHelper.getExampleMapByObjId(ecoreExamples);
		ecoreExternalSchemaMapById = MrHelper.getExternalSchemaMapById(ecoreExternalSchemas);
		ecoreMessageComponentMapById = MrHelper.getMessageComponentMapById(ecoreMessageComponents);
		ecoreMessageDefinitionMapById = MrHelper.getMessageDefinitionMapById(ecoreMessageDefinitions);
		ecoreMessageElementMapById = MrHelper.getMsgElementMapById(ecoreMessageElements);
		ecoreMessageElementMapByMsgComponentId = MrHelper.getMsgElementMapByMsgComponentId(ecoreMessageElements);
		ecoreNextVersionsMapByNextVersionId = MrHelper.getNextVersionsMapByNextVersionId(ecoreNextVersionses);
		ecoreNextVersionsMapById = MrHelper.getNextVersionsMapById(ecoreNextVersionses);
	}
	
	/**
	 * 加载Business Model
	 */
	private void initBusinessModelTab(String tabName, EcoreTreeNode dtTreeNodes, EcoreTreeNode bcTreeNodes) {
		CTabItem item = new CTabItem(tabFolder, SWT.NONE);
		item.setText(tabName);
		// 初始化Tree控件
		businessModelTabTree = new Tree(tabFolder, SWT.SINGLE | SWT.BORDER_SOLID | SWT.V_SCROLL | SWT.H_SCROLL);
		businessModelTabTree.computeSize(10, 10, true);
		businessModelTabTree.setRedraw(true);
		businessModelTabTree.setLayout(new FillLayout(0));
		businessModelTabTree.setData("tabIndex", 1);
		
		// 根目录
		new MrTreeItem(businessModelTabTree, dtTreeNodes);
		new MrTreeItem(businessModelTabTree, bcTreeNodes);

		// 添加双击事件响应
		businessModelTabTree.addListener(SWT.MouseDoubleClick, new BusinessModelTreeClickListener(businessModelTabTree));

		// 添加右键PopupMenu
		MrTreeViewHelper.init(businessModelTabTree, "1");

		// 加载搜索框下面的选项
		tab1ComboStrList.clear();
		tab1ComboMap.clear();
		for (TreeItem treeItem : businessModelTabTree.getItems()) {
			getComboList(treeItem, 1, tab1ComboStrList, tab1ComboMap);
		}
		item.setControl(businessModelTabTree);

		// 设置默认选中状态
		tabFolder.setSelection(0);
	}

	/**
	 * 加载Message Components
	 * @param tabName
	 */
	private void initMessageComponentsTab(String tabName, EcoreTreeNode esTreeNodes, EcoreTreeNode mcTreeNodes) {
		CTabItem item = new CTabItem(tabFolder, SWT.NONE);
		item.setText(tabName);
		// 初始化Tree控件
		messageComponentsTabTree = new Tree(tabFolder, SWT.SINGLE | SWT.BORDER_SOLID | SWT.V_SCROLL | SWT.H_SCROLL);
		messageComponentsTabTree.computeSize(10, 10, true);
		messageComponentsTabTree.setRedraw(true);
		messageComponentsTabTree.setLayout(new FillLayout(0));
		messageComponentsTabTree.setData("tabIndex", 2);

		// 根目录
		new MrTreeItem(messageComponentsTabTree, esTreeNodes);
		new MrTreeItem(messageComponentsTabTree, mcTreeNodes);
		// 添加双击事件响应
		messageComponentsTabTree.addListener(SWT.MouseDoubleClick, new MessageComponentsTreeClickListener(messageComponentsTabTree));

		// 添加右键PopupMenu
		MrTreeViewHelper.init(messageComponentsTabTree, "2");

		// 加载搜索框下面的选项
		tab2ComboStrList.clear();
		tab2ComboMap.clear();
		for (TreeItem treeItem : messageComponentsTabTree.getItems()) {
			getComboList(treeItem, 2, tab2ComboStrList, tab2ComboMap);
		}
		item.setControl(messageComponentsTabTree);
	}

	/**
	 * 加载Messages
	 * @param tabName
	 */
	private void initMessagesTab(String tabName, List<EcoreTreeNode> emTreeNodes) {
		CTabItem item = new CTabItem(tabFolder, SWT.NONE);
		item.setText(tabName);
		// 初始化Tree控件
		messagesTabTree = new Tree(tabFolder, SWT.SINGLE | SWT.BORDER_SOLID | SWT.V_SCROLL | SWT.H_SCROLL);
		messagesTabTree.computeSize(10, 10, true);
		messagesTabTree.setRedraw(true);
		messagesTabTree.setLayout(new FillLayout(0));
		messagesTabTree.setData("tabIndex", 3);

		emTreeNodes.stream().forEach(t -> {
			new MrTreeItem(messagesTabTree, t);
		});

		// 添加双击事件响应
		messagesTabTree.addListener(SWT.MouseDoubleClick, new MessagesTreeClickListener(messagesTabTree));

		// 添加右键PopupMenu
		MrTreeViewHelper.init(messagesTabTree, "3");

		// 加载搜索框下面的选项
		tab3ComboStrList.clear();
		tab3ComboMap.clear();
		for (TreeItem treeItem : messagesTabTree.getItems()) {
			getComboList(treeItem, 3, tab3ComboStrList, tab3ComboMap);
		}
		item.setControl(messagesTabTree);
	}
	
	/**
	 * 获取combo的选项
	 * 
	 * @param firstNode
	 * @param tabType
	 * @param comboStrList
	 * @param comboMap
	 */
	private void getComboList(TreeItem firstNode, int tabType, Set<String> comboStrList,
			Map<String, Map<String, Object>> comboMap) {
		comboStrList.add(firstNode.getText());
		HashMap<String, Object> firstNodeMap = new HashMap<String, Object>();
		firstNodeMap.put("tabType", tabType);
		firstNodeMap.put("TreeItem", firstNode);
		comboMap.put(firstNode.getText(), firstNodeMap);

		TreeItem[] secondChildNodes = firstNode.getItems();
		if (secondChildNodes != null && secondChildNodes.length > 0) {
			for (TreeItem secondNode : secondChildNodes) {
				comboStrList.add(secondNode.getText());
				HashMap<String, Object> secondNodeMap = new HashMap<String, Object>();
				secondNodeMap.put("tabType", tabType);
				secondNodeMap.put("TreeItem", secondNode);
				comboMap.put(secondNode.getText(), secondNodeMap);
				if (tabType == 1 && firstNode.getText().equals(TreeMenuEnum.DATA_TYPES.getName())) {
					TreeItem[] thirdChildNodes = secondNode.getItems();
					if (thirdChildNodes != null && thirdChildNodes.length > 0) {
						for (TreeItem thirdChild : thirdChildNodes) {
							comboStrList.add(thirdChild.getText());
							HashMap<String, Object> thirdChildMap = new HashMap<String, Object>();
							thirdChildMap.put("tabType", tabType);
							thirdChildMap.put("TreeItem", thirdChild);
							comboMap.put(thirdChild.getText(), thirdChildMap);
						}
					}
				}
			}
		}

		// 对set进行排序
		comboStrList = comboStrList.stream().sorted().collect(Collectors.toSet());
	}
}
