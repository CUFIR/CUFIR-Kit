package org.cufir.s.ecore.iso20022.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.cufir.s.ecore.bean.EcoreBusinessArea;
import org.cufir.s.ecore.iso20022.BusinessArea;
import org.cufir.s.ecore.iso20022.TopLevelCatalogueEntry;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * 解析BusinessArea
 * @author zqh
 *
 */
public class BusinessAreaAnalysis {

	// BusinessArea
	private static List<EcoreBusinessArea> items = new ArrayList<EcoreBusinessArea>();
	
	/**
	 * 解析BusinessArea
	 * @param obj
	 * @param resource
	 */
	public static void parse(TopLevelCatalogueEntry obj,XMIResource resource) {
		EcoreBusinessArea po=new EcoreBusinessArea();
		BusinessArea ba=(BusinessArea)obj;
		try {
			BeanUtils.copyProperties(po, obj);
			String uuid=resource.getID(obj);
			po.setId(uuid);
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			if(ba.getRemovalDate() != null && ba.getRemovalDate() instanceof Date) {
				po.setRemovalDate(ba.getRemovalDate());
			}
			//解析公共内容
			CommonAnalysis.parse(uuid, "6", obj, resource);
			//解析MessageDefinition
			if(ba.getMessageDefinition()!=null && !ba.getMessageDefinition().isEmpty()) {
				MessageDefinitionAnalysis.parse(true,uuid, ba.getMessageDefinition(), resource);
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		items.add(po);
	}

	public static List<EcoreBusinessArea> getItems() {
		return items;
	}

	public static void clear() {
		items.clear();
	}
}
