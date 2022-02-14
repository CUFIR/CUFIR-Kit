package com.cfets.cufir.s.data.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.emf.ecore.xmi.XMIResource;

import com.cfets.cufir.s.data.bean.EcoreExternalSchema;
import com.cfets.cufir.s.data.iso20022.ExternalSchema;
import com.cfets.cufir.s.data.iso20022.ModelEntity;
import com.cfets.cufir.s.data.iso20022.TopLevelDictionaryEntry;

/**
 * 解析ExternalSchema
 * @author zqh
 *
 */
public class AnalysisIso20022ExternalSchema {

	// ExternalSchema
	private static List<EcoreExternalSchema> ecoreExternalSchemas = new ArrayList<EcoreExternalSchema>();
	
	/**
	 * 解析ExternalSchema
	 * @param obj
	 * @param resource
	 */
	public static void parseExternalSchema(TopLevelDictionaryEntry obj, XMIResource resource) {
		EcoreExternalSchema po=new EcoreExternalSchema();
		ExternalSchema es=(ExternalSchema)obj;
		try {
			BeanUtils.copyProperties(po, obj);
			String uuid=resource.getID(obj);
			po.setId(uuid);
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			//解析公共内容
			AnalysisIso20022Common.parseCommon(uuid, "8", obj, resource);
			//解析PreviousVersion
			if(obj.getPreviousVersion()!=null) {
				ModelEntity me=obj.getPreviousVersion();
				String previousVersion=resource.getID(me);
				po.setPreviousVersion(previousVersion);
			}
			//解析NamespaceList
			if(es.getNamespaceList()!= null && !es.getNamespaceList().isEmpty()) {
				AnalysisIso20022NamespaceList.parseNamespaceList(uuid, es.getNamespaceList());
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		ecoreExternalSchemas.add(po);
	}

	public static List<EcoreExternalSchema> getEcoreExternalSchemas() {
		return ecoreExternalSchemas;
	}

	public static void setEcoreExternalSchemas(List<EcoreExternalSchema> ecoreExternalSchemas) {
		AnalysisIso20022ExternalSchema.ecoreExternalSchemas = ecoreExternalSchemas;
	}
	
}
