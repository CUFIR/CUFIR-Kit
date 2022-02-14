package com.cfets.cufir.s.data.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.xmi.XMIResource;

import com.cfets.cufir.s.data.bean.EcoreSemanticMarkup;
import com.cfets.cufir.s.data.iso20022.SemanticMarkup;

/**
 * 解析SemanticMarkup
 * @author zqh
 *
 */
public class AnalysisIso20022SemanticMarkup {

	// EcoreSemanticMarkup
	private static List<EcoreSemanticMarkup> ecorSemanticMarkups = new ArrayList<EcoreSemanticMarkup>();
	
	/**
	 * 解析SemanticMarkup
	 * @param uuid
	 * @param objType
	 * @param ups
	 * @param resource
	 */
	public static void parseSemanticMarkup(String uuid, String objType,EList<SemanticMarkup> ups,XMIResource resource) {
		for(SemanticMarkup c:ups) {
			EcoreSemanticMarkup po=new EcoreSemanticMarkup();
			try {
				BeanUtils.copyProperties(po, c);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
			String cuuid=resource.getID(c);
			po.setId(cuuid);
			po.setObjId(uuid);
			po.setObjType(objType);
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			if(c.getElements()!=null && !c.getElements().isEmpty()) {
				AnalysisIso20022SemanticMarkupElement.parseSemanticMarkup(cuuid, c.getElements(), resource);
			}
			ecorSemanticMarkups.add(po);
		}
	}

	public static List<EcoreSemanticMarkup> getEcorSemanticMarkups() {
		return ecorSemanticMarkups;
	}

	public static void setEcorSemanticMarkups(List<EcoreSemanticMarkup> ecorSemanticMarkups) {
		AnalysisIso20022SemanticMarkup.ecorSemanticMarkups = ecorSemanticMarkups;
	}
	
}
