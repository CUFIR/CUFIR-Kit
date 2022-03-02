package org.cufir.s.data;

import org.cufir.s.data.dao.impl.EcoreBusinessAreaImpl;
import org.cufir.s.data.dao.impl.EcoreBusinessComponentImpl;
import org.cufir.s.data.dao.impl.EcoreBusinessComponentRLImpl;
import org.cufir.s.data.dao.impl.EcoreBusinessElementImpl;
import org.cufir.s.data.dao.impl.EcoreBusinessProcessImpl;
import org.cufir.s.data.dao.impl.EcoreBusinessRoleImpl;
import org.cufir.s.data.dao.impl.EcoreCodeImpl;
import org.cufir.s.data.dao.impl.EcoreCommonImpl;
import org.cufir.s.data.dao.impl.EcoreConstraintImpl;
import org.cufir.s.data.dao.impl.EcoreDataTypeImpl;
import org.cufir.s.data.dao.impl.EcoreDocletImpl;
import org.cufir.s.data.dao.impl.EcoreExampleImpl;
import org.cufir.s.data.dao.impl.EcoreExternalSchemaImpl;
import org.cufir.s.data.dao.impl.EcoreMessageBuildingBlockImpl;
import org.cufir.s.data.dao.impl.EcoreMessageComponentImpl;
import org.cufir.s.data.dao.impl.EcoreMessageDefinitionImpl;
import org.cufir.s.data.dao.impl.EcoreMessageElementImpl;
import org.cufir.s.data.dao.impl.EcoreMessageSetImpl;
import org.cufir.s.data.dao.impl.EcoreMessageSetDefinitionRLImpl;
import org.cufir.s.data.dao.impl.EcoreNamespaceListImpl;
import org.cufir.s.data.dao.impl.EcoreNextVersionsImpl;
import org.cufir.s.data.dao.impl.EcoreSemanticMarkupImpl;
import org.cufir.s.data.dao.impl.EcoreSemanticMarkupElementImpl;
import org.cufir.s.ecore.iso20022.BusinessArea;
import org.cufir.s.ecore.iso20022.BusinessComponent;
import org.cufir.s.ecore.iso20022.BusinessProcess;
import org.cufir.s.ecore.iso20022.ChoiceComponent;
import org.cufir.s.ecore.iso20022.DataType;
import org.cufir.s.ecore.iso20022.ExternalSchema;
import org.cufir.s.ecore.iso20022.Iso20022Package;
import org.cufir.s.ecore.iso20022.MessageComponent;
import org.cufir.s.ecore.iso20022.MessageSet;
import org.cufir.s.ecore.iso20022.Repository;
import org.cufir.s.ecore.iso20022.TopLevelCatalogueEntry;
import org.cufir.s.ecore.iso20022.TopLevelDictionaryEntry;
import org.cufir.s.ecore.iso20022.UserDefined;
import org.cufir.s.ecore.iso20022.analysis.BusinessAreaAnalysis;
import org.cufir.s.ecore.iso20022.analysis.BusinessComponentAnalysis;
import org.cufir.s.ecore.iso20022.analysis.BusinessComponentRLAnalysis;
import org.cufir.s.ecore.iso20022.analysis.BusinessElementAnalysis;
import org.cufir.s.ecore.iso20022.analysis.BusinessProcessAnalysis;
import org.cufir.s.ecore.iso20022.analysis.BusinessRoleAnalysis;
import org.cufir.s.ecore.iso20022.analysis.CodeAnalysis;
import org.cufir.s.ecore.iso20022.analysis.ConstraintAnalysis;
import org.cufir.s.ecore.iso20022.analysis.DataTypeAnalysis;
import org.cufir.s.ecore.iso20022.analysis.DocletAnalysis;
import org.cufir.s.ecore.iso20022.analysis.ExampleAnalysis;
import org.cufir.s.ecore.iso20022.analysis.ExternalSchemaAnalysis;
import org.cufir.s.ecore.iso20022.analysis.MessageBuildingBlockAnalysis;
import org.cufir.s.ecore.iso20022.analysis.MessageComponentAnalysis;
import org.cufir.s.ecore.iso20022.analysis.MessageDefinitionAnalysis;
import org.cufir.s.ecore.iso20022.analysis.MessageElementAnalysis;
import org.cufir.s.ecore.iso20022.analysis.MessageSetAnalysis;
import org.cufir.s.ecore.iso20022.analysis.MessageSetDefinitionRLAnalysis;
import org.cufir.s.ecore.iso20022.analysis.NamespaceListAnalysis;
import org.cufir.s.ecore.iso20022.analysis.NextVersionsAnalysis;
import org.cufir.s.ecore.iso20022.analysis.SemanticMarkupAnalysis;
import org.cufir.s.ecore.iso20022.analysis.SemanticMarkupElementAnalysis;
import org.cufir.s.ecore.iso20022.util.PerformantXMIResourceFactoryImpl;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * 解析ISO20022库入口
 */
public class Iso20022Mgr implements Runnable {
	
	/**
	 * 
	 */
	private IAnalysisProgressMonitor monitor;
	private String filePath;

	/**
	 * 构造函数
	 * 
	 * @param filePath
	 * @param monitor
	 */
	public Iso20022Mgr(String filePath, IAnalysisProgressMonitor monitor) {
		this.filePath = filePath;
		this.monitor = monitor;
	}
	
	public Iso20022Mgr() {
		
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
		clear();
		// 开始解析
		analysis(repository, resource);
		// 初始化数据入库
		writeDB();
	}
	
	/**
	 * 是否已经导入ISO20022文件
	 * @return
	 */
	public boolean imported() throws Exception{
		EcoreDataTypeImpl ecoreDataTypeDao=new EcoreDataTypeImpl();
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
				DataTypeAnalysis.parse(obj, resource);
			} else if (obj instanceof MessageComponent || obj instanceof ChoiceComponent) {
				// 消息组件
				MessageComponentAnalysis.parse(obj, resource);
			} else if (obj instanceof BusinessComponent) {
				// 业务组件
				BusinessComponentAnalysis.parse(obj, resource);
			} else if (obj instanceof ExternalSchema) {
				// 扩展
				ExternalSchemaAnalysis.parse(obj, resource);
			}
		}
		//单独处理code_name 为空的数据
		CodeAnalysis.parseNullCode();
		monitorInfo("加载业务过程目录！");
		// 业务处理解析
		EList<TopLevelCatalogueEntry> messageSets = new BasicEList<TopLevelCatalogueEntry>();
		EList<TopLevelCatalogueEntry> topLevelCatalogueEntry = repository.getBusinessProcessCatalogue()
				.getTopLevelCatalogueEntry();
		for (TopLevelCatalogueEntry obj : topLevelCatalogueEntry) {
			if (obj instanceof BusinessArea) {
				// 业务领域
				BusinessAreaAnalysis.parse(obj, resource);
			} else if (obj instanceof MessageSet) {
				// 消息集
				messageSets.add(obj);
			} else if (obj instanceof BusinessProcess) {
				// 业务处理、角色
				BusinessProcessAnalysis.parse(obj, resource);
			}
		}
		// 消息集
		for (TopLevelCatalogueEntry obj : messageSets) {
			MessageSetAnalysis.parse(obj, resource);
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

	/**
	 * 入库
	 * @throws Exception
	 */
	public void writeDB() throws Exception{
		//删除数据库中数据
		monitorInfo("清除历史数据！");
		EcoreCommonImpl ecoreCommonDao=new EcoreCommonImpl();
		ecoreCommonDao.deleteIso20022EcoreData();
		
		// 数据类型
		monitorInfo("解析数据类型！");
		EcoreDataTypeImpl ecoreDataTypeDao = new EcoreDataTypeImpl();
		System.out.println(DataTypeAnalysis.getItems().size() + "DataTypeAnalysis++++++++++++++++++++++++");
		ecoreDataTypeDao.saveDataTypes(DataTypeAnalysis.getItems());
		// 样例
		monitorInfo("解析样例数据！");
		EcoreExampleImpl ecoreExampleDao = new EcoreExampleImpl();
		System.out.println(ExampleAnalysis.getItems().size() + "ExampleAnalysis++++++++++++++++++++++++");
		ecoreExampleDao.saveExamples(ExampleAnalysis.getItems());
		// 约束
		monitorInfo("解析约束数据！");
		EcoreConstraintImpl ecoreConstraintDao = new EcoreConstraintImpl();
		System.out.println(ConstraintAnalysis.getItems().size() + "ConstraintAnalysis++++++++++++++++++++++++");
		ecoreConstraintDao.saveConstraints(ConstraintAnalysis.getItems());
		// Code
		monitorInfo("解析代码值数据！");
		EcoreCodeImpl ecoreCodeDao = new EcoreCodeImpl();
		System.out.println(CodeAnalysis.getItems().size() + "CodeAnalysis++++++++++++++++++++++++");
		ecoreCodeDao.saveCode(CodeAnalysis.getItems());
		// BusinessArea
		monitorInfo("解析业务领域数据！");
		EcoreBusinessAreaImpl ecoreBusinessAreaDao = new EcoreBusinessAreaImpl();
		System.out.println(BusinessAreaAnalysis.getItems().size() + "BusinessAreaAnalysis++++++++++++++++++++++++");
		ecoreBusinessAreaDao.addEcoreBusinessAreaList(BusinessAreaAnalysis.getItems());
		// MessageSet
		monitorInfo("解析数据集数据！");
		EcoreMessageSetImpl ecoreMessageSetDao = new EcoreMessageSetImpl();
		System.out.println(MessageSetAnalysis.getItems().size() + "MessageSetAnalysis++++++++++++++++++++++++");
		ecoreMessageSetDao.addEcoreMessageSetList(MessageSetAnalysis.getItems());
		// SemanticMarkup
		monitorInfo("解析语义数据！");
		EcoreSemanticMarkupImpl ecoreSemanticMarkupDao = new EcoreSemanticMarkupImpl();
		System.out.println(SemanticMarkupAnalysis.getItems().size() + "SemanticMarkupAnalysis++++++++++++++++++++++++");
		ecoreSemanticMarkupDao.addEcoreSemanticMarkupList(SemanticMarkupAnalysis.getItems());
		// SemanticMarkupElement
		EcoreSemanticMarkupElementImpl ecoreSemanticMarkupElementDao = new EcoreSemanticMarkupElementImpl();
		System.out.println(SemanticMarkupElementAnalysis.getItems().size() + "SemanticMarkupElementAnalysis++++++++++++++++++++++++");
		ecoreSemanticMarkupElementDao.addEcoreSemanticMarkupElementList(SemanticMarkupElementAnalysis.getItems());
		// Doclet
		monitorInfo("解析文档数据！");
		EcoreDocletImpl ecoreDocletDao = new EcoreDocletImpl();
		System.out.println(DocletAnalysis.getItems().size() + "DocletAnalysis++++++++++++++++++++++++");
		ecoreDocletDao.addEcoreDocletList(DocletAnalysis.getItems());
		// BusinessComponent
		monitorInfo("解析业务组件数据！");
		EcoreBusinessComponentImpl ecoreBusinessComponentDao = new EcoreBusinessComponentImpl();
		System.out.println(BusinessComponentAnalysis.getItems().size() + "BusinessComponentAnalysis++++++++++++++++++++++++");
		ecoreBusinessComponentDao.addEcoreBusinessComponentList(BusinessComponentAnalysis.getItems());
		// BusinessElement
		monitorInfo("解析业务元素数据！");
		EcoreBusinessElementImpl ecoreBusinessElementDao = new EcoreBusinessElementImpl();
		System.out.println(BusinessElementAnalysis.getItems().size() + "BusinessElementAnalysis++++++++++++++++++++++++");
		ecoreBusinessElementDao.addEcoreBusinessElementList(BusinessElementAnalysis.getItems());
		// BusinessComponentRL
		EcoreBusinessComponentRLImpl ecoreBusinessComponentRLDao = new EcoreBusinessComponentRLImpl();
		System.out.println(BusinessComponentRLAnalysis.getItems().size() + "BusinessComponentRLAnalysis++++++++++++++++++++++++");
		ecoreBusinessComponentRLDao.addEcoreBusinessComponentRLList(BusinessComponentRLAnalysis.getItems());
		// ExternalSchema
		monitorInfo("解析外部结构数据！");
		EcoreExternalSchemaImpl ecoreExternalSchemaDao = new EcoreExternalSchemaImpl();
		System.out.println(ExternalSchemaAnalysis.getItems().size() + "ExternalSchemaAnalysis++++++++++++++++++++++++");
		ecoreExternalSchemaDao.addEcoreExternalSchemaList(ExternalSchemaAnalysis.getItems());
		// NamespaceList
		EcoreNamespaceListImpl ecoreNamespaceListDao = new EcoreNamespaceListImpl();
		System.out.println(NamespaceListAnalysis.getItems().size() + "NamespaceListAnalysis++++++++++++++++++++++++");
		ecoreNamespaceListDao.addEcoreNamespaceListList(NamespaceListAnalysis.getItems());
		// MessageComponent
		monitorInfo("解析消息组件数据！");
		EcoreMessageComponentImpl ecoreMessageComponentDao = new EcoreMessageComponentImpl();
		System.out.println(MessageComponentAnalysis.getItems().size() + "MessageComponentAnalysis++++++++++++++++++++++++");
		ecoreMessageComponentDao.saveMessageComponents(MessageComponentAnalysis.getItems());
		// MessageElement
		monitorInfo("解析消息元素数据！");
		EcoreMessageElementImpl ecoreMessageElementDao = new EcoreMessageElementImpl();
		System.out.println(MessageElementAnalysis.getItems().size() + "MessageElementAnalysis++++++++++++++++++++++++");
		ecoreMessageElementDao.addEcoreMessageElementList(MessageElementAnalysis.getItems());
		// MessageSetDefinitionRL
		EcoreMessageSetDefinitionRLImpl ecoreMessageSetDefinitionRLDao = new EcoreMessageSetDefinitionRLImpl();
		System.out.println(MessageSetDefinitionRLAnalysis.getItems().size() + "MessageSetDefinitionRLAnalysis++++++++++++++++++++++++");
		ecoreMessageSetDefinitionRLDao.addEcoreMessageSetDefinitionRLList(MessageSetDefinitionRLAnalysis.getItems());
		// MessageDefinition
		monitorInfo("解析消息定义数据！");
		EcoreMessageDefinitionImpl ecoreMessageDefinitionDao = new EcoreMessageDefinitionImpl();
		System.out.println(MessageDefinitionAnalysis.getItems().size() + "MessageDefinitionAnalysis++++++++++++++++++++++++");
		ecoreMessageDefinitionDao.addEcoreMessageDefinitionList(MessageDefinitionAnalysis.getItems());
		// MessageBuildingBlock
		EcoreMessageBuildingBlockImpl ecoreMessageBuildingBlockDao = new EcoreMessageBuildingBlockImpl();
		System.out.println(MessageBuildingBlockAnalysis.getItems().size() + "MessageBuildingBlockAnalysis++++++++++++++++++++++++");
		ecoreMessageBuildingBlockDao.addEcoreMessageBuildingBlockList(MessageBuildingBlockAnalysis.getItems());
		// NextVersions
		monitorInfo("解析版本数据！");
		EcoreNextVersionsImpl ecoreNextVersionsDao = new EcoreNextVersionsImpl();
		System.out.println(NextVersionsAnalysis.getItems().size() + "NextVersionsAnalysis++++++++++++++++++++++++");
		ecoreNextVersionsDao.saveNextVersionses(NextVersionsAnalysis.getItems());
		monitorInfo("解析业务处理和角色数据！");
		//EcoreBusinessProcess
		EcoreBusinessProcessImpl ecoreBusinessProcessDao=new EcoreBusinessProcessImpl();
		System.out.println(BusinessProcessAnalysis.getItems().size() + "BusinessProcessAnalysis++++++++++++++++++++++++");
		ecoreBusinessProcessDao.addEcoreBusinessProcessList(BusinessProcessAnalysis.getItems());
		//EcoreBusinessRole
		EcoreBusinessRoleImpl ecoreBusinessRoleDao=new EcoreBusinessRoleImpl();
		System.out.println(BusinessRoleAnalysis.getItems().size() + "BusinessRoleAnalysis++++++++++++++++++++++++");
		ecoreBusinessRoleDao.addEcoreBusinessRoleList(BusinessRoleAnalysis.getItems());
	}

	/**
	 * 初始化数据，清空所有内存集合对象
	 */
	public void clear(){
		// 数据类型
		DataTypeAnalysis.clear();
		// 样例
		ExampleAnalysis.clear();
		// 约束
		ConstraintAnalysis.clear();
		// Code
		CodeAnalysis.clear();
		// BusinessArea
		BusinessAreaAnalysis.clear();
		// MessageSet
		MessageSetAnalysis.clear();
		// BusinessProcess
		BusinessProcessAnalysis.clear();
		// BusinessRole
		BusinessRoleAnalysis.clear();
		// SemanticMarkup
		SemanticMarkupAnalysis.clear();
		// SemanticMarkupElement
		SemanticMarkupElementAnalysis.clear();
		// Doclet
		DocletAnalysis.clear();
		// MessageComponent
		MessageComponentAnalysis.clear();
		// MessageElement
		MessageElementAnalysis.clear();
		// BusinessComponent
		BusinessComponentAnalysis.clear();
		// BusinessElement
		BusinessElementAnalysis.clear();
		// BusinessComponentRL
		BusinessComponentRLAnalysis.clear();
		// MessageDefinition
		MessageDefinitionAnalysis.clear();
		// MessageBuildingBlock
		MessageBuildingBlockAnalysis.clear();
		// MessageSetDefinitionRL
		MessageSetDefinitionRLAnalysis.clear();
		// NextVersions
		NextVersionsAnalysis.clear();
		// ExternalSchema
		ExternalSchemaAnalysis.clear();
		// NamespaceList
		NamespaceListAnalysis.clear();
	}

	@Override
	public void run() {
		
	}

}
