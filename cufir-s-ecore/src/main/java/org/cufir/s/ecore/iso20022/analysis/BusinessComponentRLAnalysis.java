package org.cufir.s.ecore.iso20022.analysis;

import java.util.ArrayList;
import java.util.List;

import org.cufir.s.ecore.bean.EcoreBusinessComponentRL;
import org.cufir.s.ecore.iso20022.BusinessComponent;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * 解析BusinessComponentRL
 */
public class BusinessComponentRLAnalysis {

	// BusinessComponentRL
	private static List<EcoreBusinessComponentRL> items = new ArrayList<EcoreBusinessComponentRL>();
	
	/**
	 * 解析BusinessComponentRL
	 * @param uuid
	 * @param bc
	 * @param resource
	 */
	public static void parse(String businessComponentId,BusinessComponent bc,XMIResource resource) {
		EcoreBusinessComponentRL po=new EcoreBusinessComponentRL();
		String puuid=null;
		po.setId(businessComponentId);
		if(bc!=null) {
			puuid=resource.getID(bc);
		}
		po.setId(businessComponentId);
		po.setPId(puuid);
		po.setCreateUser("Import");
		po.setIsfromiso20022("1");
		items.add(po);
	}

	public static List<EcoreBusinessComponentRL> getItems() {
		return items;
	}

	public static void clear() {
		items.clear();
	}
}
