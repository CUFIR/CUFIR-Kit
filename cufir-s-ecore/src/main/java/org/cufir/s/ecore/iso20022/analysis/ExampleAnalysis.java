package org.cufir.s.ecore.iso20022.analysis;

import java.util.ArrayList;
import java.util.List;

import org.cufir.s.ecore.bean.EcoreExample;
import org.eclipse.emf.common.util.EList;

/**
 * 解析Example
 * @author zqh
 *
 */
public class ExampleAnalysis {

	// Example
	private static List<EcoreExample> items = new ArrayList<EcoreExample>();
	
	/**
	 * 解析ExternalSchema
	 * @param uuid
	 * @param type
	 * @param examples
	 */
	public static void parse(String uuid,String type,EList<String> examples) {
		for(String str:examples) {
			EcoreExample po=new EcoreExample();
			po.setExample(str);
			po.setId(uuid);
			po.setObjId(uuid);
			po.setObjType(type);
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			items.add(po);
		}
	}

	public static List<EcoreExample> getItems() {
		return items;
	}

	public static void clear() {
		items.clear();
	}
	
}
