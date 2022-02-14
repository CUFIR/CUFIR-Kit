package com.cfets.cufir.s.data.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.emf.ecore.xmi.XMIResource;

import com.cfets.cufir.s.data.bean.EcoreBusinessComponent;
import com.cfets.cufir.s.data.iso20022.BusinessComponent;
import com.cfets.cufir.s.data.iso20022.ModelEntity;
import com.cfets.cufir.s.data.iso20022.TopLevelDictionaryEntry;

/**
 * 解析BusinessComponent
 * @author zqh
 *
 */
public class AnalysisIso20022BusinessComponent {

	// BusinessComponent
	private static List<EcoreBusinessComponent> ecoreBusinessComponents = new ArrayList<EcoreBusinessComponent>();
	
	/**
	 * 解析BusinessComponent
	 * @param obj
	 * @param resource
	 */
	public static void parseBusinessComponent(TopLevelDictionaryEntry obj,XMIResource resource) {
		EcoreBusinessComponent po=new EcoreBusinessComponent();
		BusinessComponent bc=(BusinessComponent)obj;
		try {
			BeanUtils.copyProperties(po, bc);
			String uuid=resource.getID(bc);
			po.setId(uuid);
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			//解析Element
			if(bc.getElement()!=null && !bc.getElement().isEmpty()){
				AnalysisIso20022BusinessElement.parseBusinessElement(uuid, bc.getElement(), resource);
			}
			//解析公共内容
			AnalysisIso20022Common.parseCommon(uuid, "2", obj, resource);
			//解析BusinessComponentRL
			if(bc.getSuperType()!= null) {
				AnalysisIso20022BusinessComponentRL.parseBusinessComponentRL(uuid,bc.getSuperType(),resource);
			}else {
				AnalysisIso20022BusinessComponentRL.parseBusinessComponentRL(uuid,null,resource);
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
		ecoreBusinessComponents.add(po);
	}

	public static List<EcoreBusinessComponent> getEcoreBusinessComponents() {
		return ecoreBusinessComponents;
	}

	public static void setEcoreBusinessComponents(List<EcoreBusinessComponent> ecoreBusinessComponents) {
		AnalysisIso20022BusinessComponent.ecoreBusinessComponents = ecoreBusinessComponents;
	}

}
