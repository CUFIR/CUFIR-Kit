package com.cfets.cufir.s.data;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;

import com.cfets.cufir.s.data.iso20022.Iso20022Package;
import com.cfets.cufir.s.data.iso20022.Repository;
import com.cfets.cufir.s.data.iso20022.util.PerformantXMIResourceFactoryImpl;

public class TestAnalysis {

	public static void main(String[] args) {
		try {

			boolean isimported=false;
			//测试是否已经导入
			Iso20022ResMgr analysisIso20022Resource2 = new Iso20022ResMgr(null, null);
			isimported=analysisIso20022Resource2.imported();
			//如果没有导入就执行导入
			if(true) {
				System.out.println("-----数据未导入---------开始导入数据--------");
				Iso20022ResMgr analysisIso20022Resource = new Iso20022ResMgr(null, null);
				// 初始化模型
				Iso20022Package.eINSTANCE.eClass();
				// 注册
				Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
				reg.getExtensionToFactoryMap().put("iso20022", new PerformantXMIResourceFactoryImpl());
				// 创建资源
				ResourceSet resSet = new ResourceSetImpl();
				// 获取资源
				XMIResource resource = (XMIResource) resSet
						.getResource(URI.createURI("model/20200406_ISO20022_2013_eRepository.iso20022"), true);
				Repository repository = (Repository) resource.getContents().get(0);
				// 初始化数据
				if(analysisIso20022Resource.clearEcoreData()) {
					// 开始解析
					analysisIso20022Resource.analysis(repository, resource);
					// 入库
					analysisIso20022Resource.initDB();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
