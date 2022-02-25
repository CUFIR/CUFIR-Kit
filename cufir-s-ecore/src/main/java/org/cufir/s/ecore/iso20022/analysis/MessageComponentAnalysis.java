package org.cufir.s.ecore.iso20022.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.cufir.s.ecore.bean.EcoreMessageComponent;
import org.cufir.s.ecore.iso20022.BusinessComponent;
import org.cufir.s.ecore.iso20022.ChoiceComponent;
import org.cufir.s.ecore.iso20022.MessageComponent;
import org.cufir.s.ecore.iso20022.ModelEntity;
import org.cufir.s.ecore.iso20022.TopLevelDictionaryEntry;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * 解析MessageComponent
 * @author zqh
 *
 */
public class MessageComponentAnalysis {

	// MessageComponent
	private static List<EcoreMessageComponent> items = new ArrayList<EcoreMessageComponent>();
	
	/**
	 * 解析MessageComponent
	 * @param obj
	 * @param resource
	 */
	public static void parse(TopLevelDictionaryEntry obj,XMIResource resource) {
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
					MessageElementAnalysis.parse(uuid, mc.getMessageElement(), resource);
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
				if(mc.getRemovalDate() != null && mc.getRemovalDate() instanceof Date) {
					po.setRemovalDate(mc.getRemovalDate());
				}
			} else if (obj instanceof ChoiceComponent) {
				componentType="2";
				ChoiceComponent mc=(ChoiceComponent)obj;
				//消息元素
				if(mc.getMessageElement()!=null && !mc.getMessageElement().isEmpty()) {
					MessageElementAnalysis.parse(uuid, mc.getMessageElement(), resource);
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
				if(mc.getRemovalDate() != null && mc.getRemovalDate() instanceof Date) {
					po.setRemovalDate(mc.getRemovalDate());
				}
			} 
			po.setComponentType(componentType);
			//解析公共内容
			CommonAnalysis.parse(uuid, "9", obj, resource);
			//解析PreviousVersion
			if(obj.getPreviousVersion()!=null) {
				ModelEntity me=obj.getPreviousVersion();
				String previousVersion=resource.getID(me);
				po.setPreviousVersion(previousVersion);
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		items.add(po);
	}

	public static List<EcoreMessageComponent> getItems() {
		return items;
	}

	public static void clear() {
		items.clear();
	}
}
