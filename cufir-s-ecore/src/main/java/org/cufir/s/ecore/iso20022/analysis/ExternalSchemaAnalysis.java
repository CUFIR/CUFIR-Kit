package org.cufir.s.ecore.iso20022.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.cufir.s.ecore.bean.EcoreExternalSchema;
import org.cufir.s.ecore.iso20022.ExternalSchema;
import org.cufir.s.ecore.iso20022.ModelEntity;
import org.cufir.s.ecore.iso20022.TopLevelDictionaryEntry;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * 解析ExternalSchema
 * @author zqh
 *
 */
public class ExternalSchemaAnalysis {

	// ExternalSchema
	private static List<EcoreExternalSchema> items = new ArrayList<EcoreExternalSchema>();
	
	/**
	 * 解析ExternalSchema
	 * @param obj
	 * @param resource
	 */
	public static void parse(TopLevelDictionaryEntry obj, XMIResource resource) {
		EcoreExternalSchema po=new EcoreExternalSchema();
		ExternalSchema es=(ExternalSchema)obj;
		try {
			BeanUtils.copyProperties(po, obj);
			String uuid=resource.getID(obj);
			po.setId(uuid);
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			if(es.getRemovalDate() != null && es.getRemovalDate() instanceof Date) {
				po.setRemovalDate(es.getRemovalDate());
			}
			//解析公共内容
			CommonAnalysis.parse(uuid, "8", obj, resource);
			//解析PreviousVersion
			if(obj.getPreviousVersion()!=null) {
				ModelEntity me=obj.getPreviousVersion();
				String previousVersion=resource.getID(me);
				po.setPreviousVersion(previousVersion);
			}
			//解析NamespaceList
			if(es.getNamespaceList()!= null && !es.getNamespaceList().isEmpty()) {
				NamespaceListAnalysis.parse(uuid, es.getNamespaceList());
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		items.add(po);
	}

	public static List<EcoreExternalSchema> getItems() {
		return items;
	}

	public static void clear() {
		items.clear();
	}
}
