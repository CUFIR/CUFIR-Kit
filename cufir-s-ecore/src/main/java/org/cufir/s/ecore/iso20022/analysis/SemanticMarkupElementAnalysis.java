package org.cufir.s.ecore.iso20022.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.cufir.s.ecore.bean.EcoreSemanticMarkupElement;
import org.cufir.s.ecore.iso20022.SemanticMarkupElement;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * 解析SemanticMarkupElement
 */
public class SemanticMarkupElementAnalysis {

	// SemanticMarkupElement
	private static List<EcoreSemanticMarkupElement> items = new ArrayList<EcoreSemanticMarkupElement>();
	
	/**
	 * 解析SemanticMarkup
	 * @param uuid
	 * @param objType
	 * @param ups
	 * @param resource
	 */
	public static void parse(String semanticMarkupId,EList<SemanticMarkupElement> els,XMIResource resource) {
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
			items.add(po);
		}
	}

	public static List<EcoreSemanticMarkupElement> getItems() {
		return items;
	}

	public static void clear() {
		items.clear();
	}

}
