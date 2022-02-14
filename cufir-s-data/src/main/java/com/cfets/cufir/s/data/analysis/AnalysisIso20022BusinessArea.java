package com.cfets.cufir.s.data.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.emf.ecore.xmi.XMIResource;

import com.cfets.cufir.s.data.bean.EcoreBusinessArea;
import com.cfets.cufir.s.data.iso20022.BusinessArea;
import com.cfets.cufir.s.data.iso20022.TopLevelCatalogueEntry;

/**
 * 解析BusinessArea
 * @author zqh
 *
 */
public class AnalysisIso20022BusinessArea {

	// BusinessArea
	private static List<EcoreBusinessArea> ecoreBusinessAreas = new ArrayList<EcoreBusinessArea>();
	
	/**
	 * 解析BusinessArea
	 * @param obj
	 * @param resource
	 */
	public static void parseBusinessArea(TopLevelCatalogueEntry obj,XMIResource resource) {
		EcoreBusinessArea po=new EcoreBusinessArea();
		BusinessArea ba=(BusinessArea)obj;
		try {
			BeanUtils.copyProperties(po, obj);
			String uuid=resource.getID(obj);
			po.setId(uuid);
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			//解析公共内容
			AnalysisIso20022Common.parseCommon(uuid, "6", obj, resource);
			//解析MessageDefinition
			if(ba.getMessageDefinition()!=null && !ba.getMessageDefinition().isEmpty()) {
				AnalysisIso20022MessageDefinition.parseMessageDefinition(true,uuid, ba.getMessageDefinition(), resource);
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		ecoreBusinessAreas.add(po);
	}

	public static List<EcoreBusinessArea> getEcoreBusinessAreas() {
		return ecoreBusinessAreas;
	}

	public static void setEcoreBusinessAreas(List<EcoreBusinessArea> ecoreBusinessAreas) {
		AnalysisIso20022BusinessArea.ecoreBusinessAreas = ecoreBusinessAreas;
	}
	
}
