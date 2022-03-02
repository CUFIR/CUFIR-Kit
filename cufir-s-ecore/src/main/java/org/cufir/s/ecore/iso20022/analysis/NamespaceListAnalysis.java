package org.cufir.s.ecore.iso20022.analysis;

import java.util.ArrayList;
import java.util.List;

import org.cufir.s.ecore.bean.EcoreNamespaceList;
import org.eclipse.emf.common.util.EList;

/**
 * 解析NamespaceList
 */
public class NamespaceListAnalysis {

	// NamespaceList
	private static List<EcoreNamespaceList> items = new ArrayList<EcoreNamespaceList>();
	
	/**
	 * 解析NamespaceList
	 * @param uuid
	 * @param namespaceLists
	 */
	public static void parse(String uuid,EList<String> namespaceLists) {
		for(String namespacelList:namespaceLists) {
			EcoreNamespaceList po=new EcoreNamespaceList();
			po.setNamespaceList(namespacelList);
			po.setId(uuid);
			po.setObj_id(uuid);
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			items.add(po);
		}
	}

	public static List<EcoreNamespaceList> getItems() {
		return items;
	}

	public static void clear() {
		items.clear();
	}
	
}
