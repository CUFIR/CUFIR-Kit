package com.cfets.cufir.s.data;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;

import com.cfets.cufir.s.data.analysis.AnalysisIso20022BusinessArea;
import com.cfets.cufir.s.data.analysis.AnalysisIso20022BusinessComponent;
import com.cfets.cufir.s.data.analysis.AnalysisIso20022BusinessComponentRL;
import com.cfets.cufir.s.data.analysis.AnalysisIso20022BusinessElement;
import com.cfets.cufir.s.data.analysis.AnalysisIso20022BusinessProcess;
import com.cfets.cufir.s.data.analysis.AnalysisIso20022BusinessRole;
import com.cfets.cufir.s.data.analysis.AnalysisIso20022Code;
import com.cfets.cufir.s.data.analysis.AnalysisIso20022Constraint;
import com.cfets.cufir.s.data.analysis.AnalysisIso20022DataType;
import com.cfets.cufir.s.data.analysis.AnalysisIso20022Doclet;
import com.cfets.cufir.s.data.analysis.AnalysisIso20022Example;
import com.cfets.cufir.s.data.analysis.AnalysisIso20022ExternalSchema;
import com.cfets.cufir.s.data.analysis.AnalysisIso20022MessageBuildingBlock;
import com.cfets.cufir.s.data.analysis.AnalysisIso20022MessageComponent;
import com.cfets.cufir.s.data.analysis.AnalysisIso20022MessageDefinition;
import com.cfets.cufir.s.data.analysis.AnalysisIso20022MessageElement;
import com.cfets.cufir.s.data.analysis.AnalysisIso20022MessageSet;
import com.cfets.cufir.s.data.analysis.AnalysisIso20022MessageSetDefinitionRL;
import com.cfets.cufir.s.data.analysis.AnalysisIso20022NamespaceList;
import com.cfets.cufir.s.data.analysis.AnalysisIso20022NextVersions;
import com.cfets.cufir.s.data.analysis.AnalysisIso20022SemanticMarkup;
import com.cfets.cufir.s.data.analysis.AnalysisIso20022SemanticMarkupElement;
import com.cfets.cufir.s.data.dao.EcoreBusinessAreaDao;
import com.cfets.cufir.s.data.dao.EcoreBusinessComponentDao;
import com.cfets.cufir.s.data.dao.EcoreBusinessComponentRLDao;
import com.cfets.cufir.s.data.dao.EcoreBusinessElementDao;
import com.cfets.cufir.s.data.dao.EcoreBusinessProcessDao;
import com.cfets.cufir.s.data.dao.EcoreBusinessRoleDao;
import com.cfets.cufir.s.data.dao.EcoreCodeDao;
import com.cfets.cufir.s.data.dao.EcoreCommonDao;
import com.cfets.cufir.s.data.dao.EcoreConstraintDao;
import com.cfets.cufir.s.data.dao.EcoreDataTypeDao;
import com.cfets.cufir.s.data.dao.EcoreDocletDao;
import com.cfets.cufir.s.data.dao.EcoreExampleDao;
import com.cfets.cufir.s.data.dao.EcoreExternalSchemaDao;
import com.cfets.cufir.s.data.dao.EcoreMessageBuildingBlockDao;
import com.cfets.cufir.s.data.dao.EcoreMessageComponentDao;
import com.cfets.cufir.s.data.dao.EcoreMessageDefinitionDao;
import com.cfets.cufir.s.data.dao.EcoreMessageElementDao;
import com.cfets.cufir.s.data.dao.EcoreMessageSetDao;
import com.cfets.cufir.s.data.dao.EcoreMessageSetDefinitionRLDao;
import com.cfets.cufir.s.data.dao.EcoreNamespaceListDao;
import com.cfets.cufir.s.data.dao.EcoreNextVersionsDao;
import com.cfets.cufir.s.data.dao.EcoreSemanticMarkupDao;
import com.cfets.cufir.s.data.dao.EcoreSemanticMarkupElementDao;
import com.cfets.cufir.s.data.dao.impl.EcoreBusinessAreaDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreBusinessComponentDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreBusinessComponentRLDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreBusinessElementDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreBusinessProcessDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreBusinessRoleDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreCodeDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreCommonDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreConstraintDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreDataTypeDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreDocletDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreExampleDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreExternalSchemaDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreMessageBuildingBlockDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreMessageComponentDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreMessageDefinitionDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreMessageElementDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreMessageSetDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreMessageSetDefinitionRLDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreNamespaceListDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreNextVersionsDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreSemanticMarkupDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreSemanticMarkupElementDaoImpl;
import com.cfets.cufir.s.data.iso20022.BusinessArea;
import com.cfets.cufir.s.data.iso20022.BusinessComponent;
import com.cfets.cufir.s.data.iso20022.BusinessProcess;
import com.cfets.cufir.s.data.iso20022.ChoiceComponent;
import com.cfets.cufir.s.data.iso20022.DataType;
import com.cfets.cufir.s.data.iso20022.ExternalSchema;
import com.cfets.cufir.s.data.iso20022.Iso20022Package;
import com.cfets.cufir.s.data.iso20022.MessageComponent;
import com.cfets.cufir.s.data.iso20022.MessageSet;
import com.cfets.cufir.s.data.iso20022.Repository;
import com.cfets.cufir.s.data.iso20022.TopLevelCatalogueEntry;
import com.cfets.cufir.s.data.iso20022.TopLevelDictionaryEntry;
import com.cfets.cufir.s.data.iso20022.UserDefined;
import com.cfets.cufir.s.data.iso20022.util.PerformantXMIResourceFactoryImpl;

/**
 * 解析ISO20022库入口
 * 
 * @author zhuqh
 *
 */
public class Iso20022ResMgr implements Runnable {
	
	/**
	 * 
	 */
	private IAnalysisProcessMonitor monitor;
	private String filePath;

	/**
	 * 构造函数
	 * 
	 * @param filePath
	 * @param monitor
	 */
	public Iso20022ResMgr(String filePath, IAnalysisProcessMonitor monitor) {
		this.filePath = filePath;
		this.monitor = monitor;
	}
	
	public Iso20022ResMgr() {
		
	}
	

	/**
	 * 执行导入数据
	 */
	public void execute() throws Exception{
		// 初始化模型
		Iso20022Package.eINSTANCE.eClass();
		// 注册
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		reg.getExtensionToFactoryMap().put("iso20022", new PerformantXMIResourceFactoryImpl());
		// 创建资源
		ResourceSet resSet = new ResourceSetImpl();
		System.out.println("execute filePath == " + filePath);
		// 获取资源
		XMIResource resource = (XMIResource) resSet.getResource(URI.createURI(filePath), true);
		Repository repository = (Repository) resource.getContents().get(0);
		// 初始化数据
		if(clearEcoreData()) {
			// 开始解析
			analysis(repository, resource);
			// 入库
			initDB();
		}
	}
	
	/**
	 * 是否已经导入ISO20022文件
	 * @return
	 */
	public boolean imported() throws Exception{
		EcoreDataTypeDao ecoreDataTypeDao=new EcoreDataTypeDaoImpl();
		return ecoreDataTypeDao.isImported();
	}
	
	/**
	 * 解析ISO20022库
	 * 
	 * @param repository
	 * @param resource
	 */
	public void analysis(Repository repository, XMIResource resource) {
		// 数据字典解析
		EList<TopLevelDictionaryEntry> topLevelDictionaryEntry = repository.getDataDictionary()
				.getTopLevelDictionaryEntry();
		monitorInfo("加载数据字典目录！");
		for (TopLevelDictionaryEntry obj : topLevelDictionaryEntry) {
			if (obj instanceof DataType || obj instanceof UserDefined) {
				// 数据类型
				AnalysisIso20022DataType.parseDataType(obj, resource);
			} else if (obj instanceof MessageComponent || obj instanceof ChoiceComponent) {
				// 消息组件
				AnalysisIso20022MessageComponent.parseMessageComponent(obj, resource);
			} else if (obj instanceof BusinessComponent) {
				// 业务组件
				AnalysisIso20022BusinessComponent.parseBusinessComponent(obj, resource);
			} else if (obj instanceof ExternalSchema) {
				// 扩展
				AnalysisIso20022ExternalSchema.parseExternalSchema(obj, resource);
			}
		}
		//单独处理code_name 为空的数据
		AnalysisIso20022Code.parseNullCode();
		monitorInfo("加载业务过程目录！");
		// 业务处理解析
		EList<TopLevelCatalogueEntry> messageSets = new BasicEList<TopLevelCatalogueEntry>();
		EList<TopLevelCatalogueEntry> topLevelCatalogueEntry = repository.getBusinessProcessCatalogue()
				.getTopLevelCatalogueEntry();
		for (TopLevelCatalogueEntry obj : topLevelCatalogueEntry) {
			if (obj instanceof BusinessArea) {
				// 业务领域
				AnalysisIso20022BusinessArea.parseBusinessArea(obj, resource);
			} else if (obj instanceof MessageSet) {
				// 消息集
				messageSets.add(obj);
			} else if (obj instanceof BusinessProcess) {
				// 业务处理、角色
				AnalysisIso20022BusinessProcess.parseBusinessProcess(obj, resource);
			}
		}
		// 消息集
		for (TopLevelCatalogueEntry obj : messageSets) {
			AnalysisIso20022MessageSet.parseMessageSet(obj, resource);
		}

	}

	/**
	 * 设置导入进度
	 * @param info
	 */
	public void monitorInfo(String info) {
		if (monitor != null) {
			monitor.info(info, 5);
		}
		System.out.println(info);
	}

	// 入库
	public void initDB() throws Exception{
		//删除数据库中数据
		monitorInfo("清除历史数据！");
		EcoreCommonDao ecoreCommonDao=new EcoreCommonDaoImpl();
		ecoreCommonDao.deleteIso20022EcoreData();
		
		// 数据类型
		monitorInfo("解析数据类型！");
		EcoreDataTypeDao ecoreDataTypeDao = new EcoreDataTypeDaoImpl();
		ecoreDataTypeDao.addEcoreDataTypeList(AnalysisIso20022DataType.getEcoreDataTypes());
		// 样例
		monitorInfo("解析样例数据！");
		EcoreExampleDao ecoreExampleDao = new EcoreExampleDaoImpl();
		ecoreExampleDao.addEcoreExampleList(AnalysisIso20022Example.getEcoreExamples());
		// 约束
		monitorInfo("解析约束数据！");
		EcoreConstraintDao ecoreConstraintDao = new EcoreConstraintDaoImpl();
		ecoreConstraintDao.addEcoreConstraintList(AnalysisIso20022Constraint.getEcoreConstraints());
		// Code
		monitorInfo("解析代码值数据！");
		EcoreCodeDao ecoreCodeDao = new EcoreCodeDaoImpl();
		ecoreCodeDao.addEcoreCodeList(AnalysisIso20022Code.getEcoreCodes());
		// BusinessArea
		monitorInfo("解析业务领域数据！");
		EcoreBusinessAreaDao ecoreBusinessAreaDao = new EcoreBusinessAreaDaoImpl();
		ecoreBusinessAreaDao.addEcoreBusinessAreaList(AnalysisIso20022BusinessArea.getEcoreBusinessAreas());
		// MessageSet
		monitorInfo("解析数据集数据！");
		EcoreMessageSetDao ecoreMessageSetDao = new EcoreMessageSetDaoImpl();
		ecoreMessageSetDao.addEcoreMessageSetList(AnalysisIso20022MessageSet.getEcoreMessageSets());
		// SemanticMarkup
		monitorInfo("解析语义数据！");
		EcoreSemanticMarkupDao ecoreSemanticMarkupDao = new EcoreSemanticMarkupDaoImpl();
		ecoreSemanticMarkupDao.addEcoreSemanticMarkupList(AnalysisIso20022SemanticMarkup.getEcorSemanticMarkups());
		// SemanticMarkupElement
		EcoreSemanticMarkupElementDao ecoreSemanticMarkupElementDao = new EcoreSemanticMarkupElementDaoImpl();
		ecoreSemanticMarkupElementDao.addEcoreSemanticMarkupElementList(
				AnalysisIso20022SemanticMarkupElement.getEcorSemanticMarkupElements());
		// Doclet
		monitorInfo("解析文档数据！");
		EcoreDocletDao ecoreDocletDao = new EcoreDocletDaoImpl();
		ecoreDocletDao.addEcoreDocletList(AnalysisIso20022Doclet.getEcoreDoclets());
		// BusinessComponent
		monitorInfo("解析业务组件数据！");
		EcoreBusinessComponentDao ecoreBusinessComponentDao = new EcoreBusinessComponentDaoImpl();
		ecoreBusinessComponentDao
				.addEcoreBusinessComponentList(AnalysisIso20022BusinessComponent.getEcoreBusinessComponents());
		// BusinessElement
		monitorInfo("解析业务元素数据！");
		EcoreBusinessElementDao ecoreBusinessElementDao = new EcoreBusinessElementDaoImpl();
		ecoreBusinessElementDao.addEcoreBusinessElementList(AnalysisIso20022BusinessElement.getEcoreBusinessElements());
		// BusinessComponentRL
		EcoreBusinessComponentRLDao ecoreBusinessComponentRLDao = new EcoreBusinessComponentRLDaoImpl();
		ecoreBusinessComponentRLDao
				.addEcoreBusinessComponentRLList(AnalysisIso20022BusinessComponentRL.getEcoreBusinessComponentRLs());
		// ExternalSchema
		monitorInfo("解析外部结构数据！");
		EcoreExternalSchemaDao ecoreExternalSchemaDao = new EcoreExternalSchemaDaoImpl();
		ecoreExternalSchemaDao.addEcoreExternalSchemaList(AnalysisIso20022ExternalSchema.getEcoreExternalSchemas());
		// NamespaceList
		EcoreNamespaceListDao ecoreNamespaceListDao = new EcoreNamespaceListDaoImpl();
		ecoreNamespaceListDao.addEcoreNamespaceListList(AnalysisIso20022NamespaceList.getEcoreNamespaceLists());
		// MessageComponent
		monitorInfo("解析消息组件数据！");
		EcoreMessageComponentDao ecoreMessageComponentDao = new EcoreMessageComponentDaoImpl();
		ecoreMessageComponentDao
				.addEcoreMessageComponentList(AnalysisIso20022MessageComponent.getEcoreMessageComponents());
		// MessageElement
		monitorInfo("解析消息元素数据！");
		EcoreMessageElementDao ecoreMessageElementDao = new EcoreMessageElementDaoImpl();
		ecoreMessageElementDao.addEcoreMessageElementList(AnalysisIso20022MessageElement.getEcoreMessageElements());
		// MessageSetDefinitionRL
		EcoreMessageSetDefinitionRLDao ecoreMessageSetDefinitionRLDao = new EcoreMessageSetDefinitionRLDaoImpl();
		ecoreMessageSetDefinitionRLDao.addEcoreMessageSetDefinitionRLList(
				AnalysisIso20022MessageSetDefinitionRL.getEcoreMessageSetDefinitionRLs());
		// MessageDefinition
		monitorInfo("解析消息定义数据！");
		EcoreMessageDefinitionDao ecoreMessageDefinitionDao = new EcoreMessageDefinitionDaoImpl();
		ecoreMessageDefinitionDao
				.addEcoreMessageDefinitionList(AnalysisIso20022MessageDefinition.getEcoreMessageDefinitions());
		// MessageBuildingBlock
		EcoreMessageBuildingBlockDao ecoreMessageBuildingBlockDao = new EcoreMessageBuildingBlockDaoImpl();
		ecoreMessageBuildingBlockDao.addEcoreMessageBuildingBlockList(
				AnalysisIso20022MessageBuildingBlock.getEcoreEcoreMessageBuildingBlocks());
		// NextVersions
		monitorInfo("解析版本数据！");
		EcoreNextVersionsDao ecoreNextVersionsDao = new EcoreNextVersionsDaoImpl();
		ecoreNextVersionsDao.addEcoreNextVersionsList(AnalysisIso20022NextVersions.getEcoreNextVersionss());
		monitorInfo("解析业务处理和角色数据！");
		//EcoreBusinessProcess
		EcoreBusinessProcessDao ecoreBusinessProcessDao=new EcoreBusinessProcessDaoImpl();
		ecoreBusinessProcessDao.addEcoreBusinessProcessList(AnalysisIso20022BusinessProcess.getEcoreBusinessProcesss());
		//EcoreBusinessRole
		EcoreBusinessRoleDao ecoreBusinessRoleDao=new EcoreBusinessRoleDaoImpl();
		ecoreBusinessRoleDao.addEcoreBusinessRoleList(AnalysisIso20022BusinessRole.getEcoreBusinessRoles());
	}

	// 初始化数据
	public boolean clearEcoreData() throws Exception{
		boolean key=false;
		// 数据类型
		AnalysisIso20022DataType.getEcoreDataTypes().clear();
		AnalysisIso20022DataType.getTraceType().clear();
		// 样例
		AnalysisIso20022Example.getEcoreExamples().clear();
		// 约束
		AnalysisIso20022Constraint.getEcoreConstraints().clear();
		// Code
		AnalysisIso20022Code.getEcoreCodes().clear();
		AnalysisIso20022Code.getTraceCode().clear();
		// BusinessArea
		AnalysisIso20022BusinessArea.getEcoreBusinessAreas().clear();
		// MessageSet
		AnalysisIso20022MessageSet.getEcoreMessageSets().clear();
		// BusinessProcess
		AnalysisIso20022BusinessProcess.getEcoreBusinessProcesss().clear();
		// BusinessRole
		AnalysisIso20022BusinessRole.getEcoreBusinessRoles().clear();
		// SemanticMarkup
		AnalysisIso20022SemanticMarkup.getEcorSemanticMarkups().clear();
		// SemanticMarkupElement
		AnalysisIso20022SemanticMarkupElement.getEcorSemanticMarkupElements().clear();
		// Doclet
		AnalysisIso20022Doclet.getEcoreDoclets().clear();
		// MessageComponent
		AnalysisIso20022MessageComponent.getEcoreMessageComponents().clear();
		// MessageElement
		AnalysisIso20022MessageElement.getEcoreMessageElements().clear();
		// BusinessComponent
		AnalysisIso20022BusinessComponent.getEcoreBusinessComponents().clear();
		// BusinessElement
		AnalysisIso20022BusinessElement.getEcoreBusinessElements().clear();
		// BusinessComponentRL
		AnalysisIso20022BusinessComponentRL.getEcoreBusinessComponentRLs().clear();
		// MessageDefinition
		AnalysisIso20022MessageDefinition.getEcoreMessageDefinitions().clear();
		AnalysisIso20022MessageDefinition.getDefinitionIds().clear();
		// MessageBuildingBlock
		AnalysisIso20022MessageBuildingBlock.getEcoreEcoreMessageBuildingBlocks().clear();
		// MessageSetDefinitionRL
		AnalysisIso20022MessageSetDefinitionRL.getEcoreMessageSetDefinitionRLs().clear();
		// NextVersions
		AnalysisIso20022NextVersions.getEcoreNextVersionss().clear();
		// ExternalSchema
		AnalysisIso20022ExternalSchema.getEcoreExternalSchemas().clear();
		// NamespaceList
		AnalysisIso20022NamespaceList.getEcoreNamespaceLists().clear();
		key=true;
		return key;
	}

	@Override
	public void run() {
		
	}

}
