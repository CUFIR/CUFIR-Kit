package org.cufir.s.ecore.iso20022.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.cufir.s.ecore.bean.EcoreBusinessProcess;
import org.cufir.s.ecore.iso20022.BusinessProcess;
import org.cufir.s.ecore.iso20022.TopLevelCatalogueEntry;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * 解析BusinessProcess
 */
public class BusinessProcessAnalysis {

	// DataType
	private static List<EcoreBusinessProcess> items = new ArrayList<EcoreBusinessProcess>();
	
	/**
	 * 解析BusinessProcess
	 * @param obj
	 * @param resource
	 */
	public static void parse(TopLevelCatalogueEntry obj,XMIResource resource) {
		BusinessProcess pro=(BusinessProcess)obj;
		EcoreBusinessProcess po=new EcoreBusinessProcess();
		try {
			BeanUtils.copyProperties(po, obj);
			String uuid=resource.getID(obj);
			po.setId(uuid);
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			if(pro.getRemovalDate() != null && pro.getRemovalDate() instanceof Date) {
				po.setRemovalDate(pro.getRemovalDate());
			}
			if(pro.getBusinessRole()!=null && !pro.getBusinessRole().isEmpty()) {
				BusinessRoleAnalysis.parse(uuid, pro.getBusinessRole(), resource);
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		items.add(po);
	}

	public static List<EcoreBusinessProcess> getItems() {
		return items;
	}

	public static void clear() {
		items.clear();
	}
}
