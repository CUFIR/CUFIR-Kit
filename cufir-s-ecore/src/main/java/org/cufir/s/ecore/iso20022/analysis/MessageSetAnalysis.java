package org.cufir.s.ecore.iso20022.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.cufir.s.ecore.bean.EcoreMessageSet;
import org.cufir.s.ecore.iso20022.MessageDefinition;
import org.cufir.s.ecore.iso20022.MessageSet;
import org.cufir.s.ecore.iso20022.TopLevelCatalogueEntry;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * 解析MessageSet
 */
public class MessageSetAnalysis {

	// MessageSet
	private static List<EcoreMessageSet> items = new ArrayList<EcoreMessageSet>();
	
	/**
	 * 解析MessageSet
	 * @param obj
	 * @param resource
	 */
	public static void parse(TopLevelCatalogueEntry obj,XMIResource resource) {
		EcoreMessageSet po=new EcoreMessageSet();
		MessageSet ms=(MessageSet)obj;
		try {
			BeanUtils.copyProperties(po, ms);
			String uuid=resource.getID(ms);
			po.setId(uuid);
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			if(ms.getRemovalDate() != null && ms.getRemovalDate() instanceof Date) {
				po.setRemovalDate(ms.getRemovalDate());
			}
			//解析公共内容
			CommonAnalysis.parse(uuid, "7", obj, resource);
			//解析MessageDefinition
			if(ms.getMessageDefinition()!=null && !ms.getMessageDefinition().isEmpty()) {
				MessageDefinitionAnalysis.parse(false,uuid, ms.getMessageDefinition(), resource);
				for(MessageDefinition c:ms.getMessageDefinition()) {
					//解析MessageSetDefinitionRL
					MessageSetDefinitionRLAnalysis.parse(resource.getID(c), uuid);
				}
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		items.add(po);
	}

	public static List<EcoreMessageSet> getItems() {
		return items;
	}

	public static void clear() {
		items.clear();
	}
	
}
