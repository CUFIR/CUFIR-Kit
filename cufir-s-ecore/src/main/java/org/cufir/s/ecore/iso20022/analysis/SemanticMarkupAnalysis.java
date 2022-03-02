package org.cufir.s.ecore.iso20022.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.cufir.s.ecore.bean.EcoreSemanticMarkup;
import org.cufir.s.ecore.iso20022.SemanticMarkup;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * 解析SemanticMarkup
 */
public class SemanticMarkupAnalysis {

	// EcoreSemanticMarkup
	private static List<EcoreSemanticMarkup> items = new ArrayList<EcoreSemanticMarkup>();
	
	/**
	 * 解析SemanticMarkup
	 * @param uuid
	 * @param objType
	 * @param ups
	 * @param resource
	 */
	public static void parse(String uuid, String objType,EList<SemanticMarkup> ups,XMIResource resource) {
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
				SemanticMarkupElementAnalysis.parse(cuuid, c.getElements(), resource);
			}
			items.add(po);
		}
	}

	public static List<EcoreSemanticMarkup> getItems() {
		return items;
	}

	public static void clear() {
		items.clear();
	}
	
}
