package com.cfets.cufir.s.data.analysis;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import com.cfets.cufir.s.data.bean.EcoreNamespaceList;

/**
 * 解析NamespaceList
 * @author zqh
 *
 */
public class AnalysisIso20022NamespaceList {

	// NamespaceList
	private static List<EcoreNamespaceList> ecoreNamespaceLists = new ArrayList<EcoreNamespaceList>();
	
	/**
	 * 解析NamespaceList
	 * @param uuid
	 * @param namespaceLists
	 */
	public static void parseNamespaceList(String uuid,EList<String> namespaceLists) {
		for(String namespacelList:namespaceLists) {
			EcoreNamespaceList po=new EcoreNamespaceList();
			po.setNamespaceList(namespacelList);
			po.setId(uuid);
			po.setObj_id(uuid);
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			ecoreNamespaceLists.add(po);
		}
	}

	public static List<EcoreNamespaceList> getEcoreNamespaceLists() {
		return ecoreNamespaceLists;
	}

	public static void setEcoreNamespaceLists(List<EcoreNamespaceList> ecoreNamespaceLists) {
		AnalysisIso20022NamespaceList.ecoreNamespaceLists = ecoreNamespaceLists;
	}
	
}
