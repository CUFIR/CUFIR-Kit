package com.cfets.cufir.s.data.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.xmi.XMIResource;

import com.cfets.cufir.s.data.bean.EcoreDoclet;
import com.cfets.cufir.s.data.iso20022.Doclet;

/**
 * 解析Doclet
 * @author zqh
 *
 */
public class AnalysisIso20022Doclet {

	// Doclet
	private static List<EcoreDoclet> ecoreDoclets = new ArrayList<EcoreDoclet>();
	
	/**
	 * 解析Doclet
	 * @param uuid
	 * @param type
	 * @param doclets
	 */
	public static void parseDoclet(String uuid,String type,EList<Doclet> doclets,XMIResource resource) {
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
			ecoreDoclets.add(po);
		}
	}

	public static List<EcoreDoclet> getEcoreDoclets() {
		return ecoreDoclets;
	}

	public static void setEcoreDoclets(List<EcoreDoclet> ecoreDoclets) {
		AnalysisIso20022Doclet.ecoreDoclets = ecoreDoclets;
	}
	
}
