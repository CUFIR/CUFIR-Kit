package com.cfets.cufir.s.data.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.xmi.XMIResource;

import com.cfets.cufir.s.data.bean.EcoreBusinessRole;
import com.cfets.cufir.s.data.iso20022.BusinessRole;

/**
 * 解析BusinessRole
 * @author zqh
 *
 */
public class AnalysisIso20022BusinessRole {

	// BusinessRole
	private static List<EcoreBusinessRole> ecoreBusinessRoles = new ArrayList<EcoreBusinessRole>();
	
	/**
	 * 解析BusinessRole
	 * @param uuid
	 * @param roles
	 * @param resource
	 */
	public static void parseBusinessRole(String uuid, EList<BusinessRole> roles,XMIResource resource) {
		for(BusinessRole c:roles) {
			EcoreBusinessRole po=new EcoreBusinessRole();
			try {
				BeanUtils.copyProperties(po, c);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
			String cuuid=resource.getID(c);
			po.setId(cuuid);
			po.setBusiness_process_id(uuid);
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			//解析公共内容
			AnalysisIso20022Common.parseCommon(cuuid, "5", c, resource);
			ecoreBusinessRoles.add(po);
		}
	}

	public static List<EcoreBusinessRole> getEcoreBusinessRoles() {
		return ecoreBusinessRoles;
	}

	public static void setEcoreBusinessRoles(List<EcoreBusinessRole> ecoreBusinessRoles) {
		AnalysisIso20022BusinessRole.ecoreBusinessRoles = ecoreBusinessRoles;
	}
	
}
