package org.cufir.s.ecore.iso20022.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.cufir.s.ecore.bean.EcoreDoclet;
import org.cufir.s.ecore.iso20022.Doclet;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * 解析Doclet
 * @author zqh
 *
 */
public class DocletAnalysis {

	// Doclet
	private static List<EcoreDoclet> items = new ArrayList<EcoreDoclet>();
	
	/**
	 * 解析Doclet
	 * @param uuid
	 * @param type
	 * @param doclets
	 */
	public static void parse(String uuid,String type,EList<Doclet> doclets,XMIResource resource) {
		for(Doclet c:doclets) {
			EcoreDoclet po=new EcoreDoclet();
			try {
				BeanUtils.copyProperties(po, c);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
			String cuuid=resource.getID(c);
			po.setId(cuuid);
			po.setObj_id(uuid);
			po.setObj_type(type);
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			items.add(po);
		}
	}

	public static List<EcoreDoclet> getItems() {
		return items;
	}

	public static void clear() {
		items.clear();
	}
	
}
