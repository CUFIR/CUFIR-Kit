package com.cfets.cufir.s.data.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.xmi.XMIResource;

import com.cfets.cufir.s.data.bean.EcoreConstraint;
import com.cfets.cufir.s.data.iso20022.Constraint;

/**
 * 解析Constraint
 * @author zqh
 *
 */
public class AnalysisIso20022Constraint {

	// Constraint
	private static List<EcoreConstraint> ecoreConstraints = new ArrayList<EcoreConstraint>();
	
	/**
	 * 解析Constraint
	 * @param uuid
	 * @param obj_type
	 * @param constraints
	 * @param resource
	 */
	public static void parseConstraint(String uuid,String obj_type, EList<Constraint> constraints,XMIResource resource) {
		for(Constraint c:constraints) {
			EcoreConstraint po=new EcoreConstraint();
			try {
				BeanUtils.copyProperties(po, c);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
			String cuuid=resource.getID(c);
			po.setId(cuuid);
			po.setObj_id(uuid);
			po.setObj_type(obj_type);
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			ecoreConstraints.add(po);
		}
	}

	public static List<EcoreConstraint> getEcoreConstraints() {
		return ecoreConstraints;
	}

	public static void setEcoreConstraints(List<EcoreConstraint> ecoreConstraints) {
		AnalysisIso20022Constraint.ecoreConstraints = ecoreConstraints;
	}
	
}
