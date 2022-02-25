package org.cufir.s.ecore.iso20022.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.cufir.s.ecore.bean.EcoreBusinessComponent;
import org.cufir.s.ecore.iso20022.BusinessComponent;
import org.cufir.s.ecore.iso20022.ModelEntity;
import org.cufir.s.ecore.iso20022.TopLevelDictionaryEntry;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * 解析BusinessComponent
 * @author zqh
 *
 */
public class BusinessComponentAnalysis {

	// BusinessComponent
	private static List<EcoreBusinessComponent> items = new ArrayList<EcoreBusinessComponent>();
	
	/**
	 * 解析BusinessComponent
	 * @param obj
	 * @param resource
	 */
	public static void parse(TopLevelDictionaryEntry obj,XMIResource resource) {
		EcoreBusinessComponent po=new EcoreBusinessComponent();
		BusinessComponent bc=(BusinessComponent)obj;
		try {
			BeanUtils.copyProperties(po, bc);
			String uuid=resource.getID(bc);
			po.setId(uuid);
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			if(bc.getRemovalDate() != null && bc.getRemovalDate() instanceof Date) {
				po.setRemovalDate(bc.getRemovalDate());
			}
			//解析Element
			if(bc.getElement()!=null && !bc.getElement().isEmpty()){
				BusinessElementAnalysis.parse(uuid, bc.getElement(), resource);
			}
			//解析公共内容
			CommonAnalysis.parse(uuid, "2", obj, resource);
			//解析BusinessComponentRL
			if(bc.getSuperType()!= null) {
				BusinessComponentRLAnalysis.parse(uuid,bc.getSuperType(),resource);
			}else {
				BusinessComponentRLAnalysis.parse(uuid,null,resource);
			}
			//解析PreviousVersion
			if(bc.getPreviousVersion()!=null) {
				ModelEntity me=bc.getPreviousVersion();
				String previousVersion=resource.getID(me);
				po.setPreviousVersion(previousVersion);
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		items.add(po);
	}

	public static List<EcoreBusinessComponent> getItems() {
		return items;
	}

	public static void clear() {
		items.clear();
	}

}
