package com.cfets.cufir.s.data.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.emf.ecore.xmi.XMIResource;

import com.cfets.cufir.s.data.bean.EcoreMessageComponent;
import com.cfets.cufir.s.data.iso20022.BusinessComponent;
import com.cfets.cufir.s.data.iso20022.ChoiceComponent;
import com.cfets.cufir.s.data.iso20022.MessageComponent;
import com.cfets.cufir.s.data.iso20022.ModelEntity;
import com.cfets.cufir.s.data.iso20022.TopLevelDictionaryEntry;

/**
 * 解析MessageComponent
 * @author zqh
 *
 */
public class AnalysisIso20022MessageComponent {

	// MessageComponent
	private static List<EcoreMessageComponent> ecoreMessageComponents = new ArrayList<EcoreMessageComponent>();
	
	/**
	 * 解析MessageComponent
	 * @param obj
	 * @param resource
	 */
	public static void parseMessageComponent(TopLevelDictionaryEntry obj,XMIResource resource) {
		EcoreMessageComponent po=new EcoreMessageComponent();
		try {
			String componentType="";
			BeanUtils.copyProperties(po, obj);
			String uuid=resource.getID(obj);
			po.setId(uuid);
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			if (obj instanceof MessageComponent) {
				componentType="1";
				MessageComponent mc=(MessageComponent)obj;
				//消息元素
				if(mc.getMessageElement()!=null && !mc.getMessageElement().isEmpty()) {
					AnalysisIso20022MessageElement.parseMessageElement(uuid, mc.getMessageElement(), resource);
				}
				//trace
				BusinessComponent bc=mc.getTrace();
				if(bc!=null) {
					po.setTrace(resource.getID(bc));
					po.setTraceName(bc.getName());
					po.setTechnical("false");
				}else {
					po.setTechnical("true");
				}
			} else if (obj instanceof ChoiceComponent) {
				componentType="2";
				ChoiceComponent mc=(ChoiceComponent)obj;
				//消息元素
				if(mc.getMessageElement()!=null && !mc.getMessageElement().isEmpty()) {
					AnalysisIso20022MessageElement.parseMessageElement(uuid, mc.getMessageElement(), resource);
				}
				//trace
				BusinessComponent bc=mc.getTrace();
				if(bc!=null) {
					po.setTrace(resource.getID(bc));
					po.setTraceName(bc.getName());
					po.setTechnical("false");
				}else {
					po.setTechnical("true");
				}
			} 
			po.setComponentType(componentType);
			//解析公共内容
			AnalysisIso20022Common.parseCommon(uuid, "9", obj, resource);
			//解析PreviousVersion
			if(obj.getPreviousVersion()!=null) {
				ModelEntity me=obj.getPreviousVersion();
				String previousVersion=resource.getID(me);
				po.setPreviousVersion(previousVersion);
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		ecoreMessageComponents.add(po);
	}

	public static List<EcoreMessageComponent> getEcoreMessageComponents() {
		return ecoreMessageComponents;
	}

	public static void setEcoreMessageComponents(List<EcoreMessageComponent> ecoreMessageComponents) {
		AnalysisIso20022MessageComponent.ecoreMessageComponents = ecoreMessageComponents;
	}
	
}
