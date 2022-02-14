package com.cfets.cufir.s.data.analysis;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.xmi.XMIResource;

import com.cfets.cufir.s.data.bean.EcoreBusinessComponentRL;
import com.cfets.cufir.s.data.iso20022.BusinessComponent;

/**
 * 解析BusinessComponentRL
 * @author zqh
 *
 */
public class AnalysisIso20022BusinessComponentRL {

	// BusinessComponentRL
	private static List<EcoreBusinessComponentRL> ecoreBusinessComponentRLs = new ArrayList<EcoreBusinessComponentRL>();
	
	/**
	 * 解析BusinessComponentRL
	 * @param uuid
	 * @param bc
	 * @param resource
	 */
	public static void parseBusinessComponentRL(String businessComponentId,BusinessComponent bc,XMIResource resource) {
		EcoreBusinessComponentRL po=new EcoreBusinessComponentRL();
		String puuid=null;
		po.setId(businessComponentId);
		if(bc!=null) {
			puuid=resource.getID(bc);
		}
		po.setId(businessComponentId);
		po.setpId(puuid);
		po.setCreateUser("Import");
		po.setIsfromiso20022("1");
		ecoreBusinessComponentRLs.add(po);
	}

	public static List<EcoreBusinessComponentRL> getEcoreBusinessComponentRLs() {
		return ecoreBusinessComponentRLs;
	}

	public static void setEcoreBusinessComponentRLs(List<EcoreBusinessComponentRL> ecoreBusinessComponentRLs) {
		AnalysisIso20022BusinessComponentRL.ecoreBusinessComponentRLs = ecoreBusinessComponentRLs;
	}

}
