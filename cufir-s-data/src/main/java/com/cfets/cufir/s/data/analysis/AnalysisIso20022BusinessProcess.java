package com.cfets.cufir.s.data.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.emf.ecore.xmi.XMIResource;

import com.cfets.cufir.s.data.bean.EcoreBusinessProcess;
import com.cfets.cufir.s.data.iso20022.BusinessProcess;
import com.cfets.cufir.s.data.iso20022.TopLevelCatalogueEntry;

/**
 * 解析BusinessProcess
 * @author zqh
 *
 */
public class AnalysisIso20022BusinessProcess {

	// DataType
	private static List<EcoreBusinessProcess> ecoreBusinessProcesss = new ArrayList<EcoreBusinessProcess>();
	
	/**
	 * 解析BusinessProcess
	 * @param obj
	 * @param resource
	 */
	public static void parseBusinessProcess(TopLevelCatalogueEntry obj,XMIResource resource) {
		BusinessProcess pro=(BusinessProcess)obj;
		EcoreBusinessProcess po=new EcoreBusinessProcess();
		try {
			BeanUtils.copyProperties(po, obj);
			String uuid=resource.getID(obj);
			po.setId(uuid);
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			if(pro.getBusinessRole()!=null && !pro.getBusinessRole().isEmpty()) {
				AnalysisIso20022BusinessRole.parseBusinessRole(uuid, pro.getBusinessRole(), resource);
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		ecoreBusinessProcesss.add(po);
	}

	public static List<EcoreBusinessProcess> getEcoreBusinessProcesss() {
		return ecoreBusinessProcesss;
	}

	public static void setEcoreBusinessProcesss(List<EcoreBusinessProcess> ecoreBusinessProcesss) {
		AnalysisIso20022BusinessProcess.ecoreBusinessProcesss = ecoreBusinessProcesss;
	}
	
}
