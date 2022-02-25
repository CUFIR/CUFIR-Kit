package org.cufir.s.ecore.iso20022.analysis;

import java.util.ArrayList;
import java.util.List;

import org.cufir.s.ecore.bean.EcoreNextVersions;
import org.cufir.s.ecore.iso20022.ModelEntity;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * 解析NextVersions
 * @author zqh
 *
 */
public class NextVersionsAnalysis {

	// NextVersions
	private static List<EcoreNextVersions> items = new ArrayList<EcoreNextVersions>();
	
	/**
	 * 解析NextVersions
	 * @param uuid
	 * @param objType
	 * @param modelEntitys
	 * @param resource
	 */
	public static void parse(String uuid,String objType,EList<ModelEntity> modelEntitys,XMIResource resource) {
		for(ModelEntity me:modelEntitys) {
			EcoreNextVersions po=new EcoreNextVersions();
			po.setId(uuid);
			po.setObjType(objType);
			String nextVersionId=resource.getID(me);
			po.setNextVersionId(nextVersionId);
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			items.add(po);
		}
	}

	public static List<EcoreNextVersions> getItems() {
		return items;
	}

	public static void clear() {
		items.clear();
	}

}
