package com.cfets.cufir.s.data.analysis;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.xmi.XMIResource;

import com.cfets.cufir.s.data.bean.EcoreNextVersions;
import com.cfets.cufir.s.data.iso20022.ModelEntity;

/**
 * 解析NextVersions
 * @author zqh
 *
 */
public class AnalysisIso20022NextVersions {

	// NextVersions
	private static List<EcoreNextVersions> ecoreNextVersionss = new ArrayList<EcoreNextVersions>();
	
	/**
	 * 解析NextVersions
	 * @param uuid
	 * @param objType
	 * @param modelEntitys
	 * @param resource
	 */
	public static void parseNextVersions(String uuid,String objType,EList<ModelEntity> modelEntitys,XMIResource resource) {
		for(ModelEntity me:modelEntitys) {
			EcoreNextVersions po=new EcoreNextVersions();
			po.setId(uuid);
			po.setObjType(objType);
			String nextVersionId=resource.getID(me);
			po.setNextVersionId(nextVersionId);
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			ecoreNextVersionss.add(po);
		}
	}

	public static List<EcoreNextVersions> getEcoreNextVersionss() {
		return ecoreNextVersionss;
	}

	public static void setEcoreNextVersionss(List<EcoreNextVersions> ecoreNextVersionss) {
		AnalysisIso20022NextVersions.ecoreNextVersionss = ecoreNextVersionss;
	}

}
