package com.cfets.cufir.s.data.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.xmi.XMIResource;

import com.cfets.cufir.s.data.bean.EcoreSemanticMarkupElement;
import com.cfets.cufir.s.data.iso20022.SemanticMarkupElement;

/**
 * 解析SemanticMarkupElement
 * @author zqh
 *
 */
public class AnalysisIso20022SemanticMarkupElement {

	// SemanticMarkupElement
	private static List<EcoreSemanticMarkupElement> ecorSemanticMarkupElements = new ArrayList<EcoreSemanticMarkupElement>();
	
	/**
	 * 解析SemanticMarkup
	 * @param uuid
	 * @param objType
	 * @param ups
	 * @param resource
	 */
	public static void parseSemanticMarkup(String semanticMarkupId,EList<SemanticMarkupElement> els,XMIResource resource) {
		for(SemanticMarkupElement c:els) {
			EcoreSemanticMarkupElement po=new EcoreSemanticMarkupElement();
			try {
				BeanUtils.copyProperties(po, c);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
			String cuuid=resource.getID(c);
			po.setId(cuuid);
			po.setSemanticMarkupId(semanticMarkupId);
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			ecorSemanticMarkupElements.add(po);
		}
	}

	public static List<EcoreSemanticMarkupElement> getEcorSemanticMarkupElements() {
		return ecorSemanticMarkupElements;
	}

	public static void setEcorSemanticMarkupElements(List<EcoreSemanticMarkupElement> ecorSemanticMarkupElements) {
		AnalysisIso20022SemanticMarkupElement.ecorSemanticMarkupElements = ecorSemanticMarkupElements;
	}

}
