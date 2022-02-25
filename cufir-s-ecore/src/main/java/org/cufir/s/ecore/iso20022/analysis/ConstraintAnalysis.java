package org.cufir.s.ecore.iso20022.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.cufir.s.ecore.bean.EcoreConstraint;
import org.cufir.s.ecore.iso20022.Constraint;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * 解析Constraint
 * @author zqh
 *
 */
public class ConstraintAnalysis {

	// Constraint
	private static List<EcoreConstraint> items = new ArrayList<EcoreConstraint>();
	
	/**
	 * 解析Constraint
	 * @param uuid
	 * @param obj_type
	 * @param constraints
	 * @param resource
	 */
	public static void parse(String uuid,String obj_type, EList<Constraint> constraints,XMIResource resource) {
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
			if(c.getRemovalDate() != null && c.getRemovalDate() instanceof Date) {
				po.setRemovalDate(c.getRemovalDate());
			}
			items.add(po);
		}
	}

	public static List<EcoreConstraint> getItems() {
		return items;
	}

	public static void clear() {
		items.clear();
	}
	
}
