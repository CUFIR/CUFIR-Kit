package org.cufir.s.ecore.iso20022.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.cufir.s.ecore.bean.EcoreBusinessRole;
import org.cufir.s.ecore.iso20022.BusinessRole;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * 解析BusinessRole
 * @author zqh
 *
 */
public class BusinessRoleAnalysis {

	// BusinessRole
	private static List<EcoreBusinessRole> items = new ArrayList<EcoreBusinessRole>();
	
	/**
	 * 解析BusinessRole
	 * @param uuid
	 * @param roles
	 * @param resource
	 */
	public static void parse(String uuid, EList<BusinessRole> roles,XMIResource resource) {
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
			if(c.getRemovalDate() != null && c.getRemovalDate() instanceof Date) {
				po.setRemovalDate(c.getRemovalDate());
			}
			//解析公共内容
			CommonAnalysis.parse(cuuid, "5", c, resource);
			items.add(po);
		}
	}

	public static List<EcoreBusinessRole> getItems() {
		return items;
	}

	public static void clear() {
		items.clear();
	}
}
