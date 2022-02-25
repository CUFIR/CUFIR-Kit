package org.cufir.s.ecore.iso20022.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.cufir.s.ecore.bean.EcoreMessageDefinition;
import org.cufir.s.ecore.iso20022.MessageDefinition;
import org.cufir.s.ecore.iso20022.MessageDefinitionIdentifier;
import org.cufir.s.ecore.iso20022.ModelEntity;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * 解析MessageDefinition
 * @author zqh
 *
 */
public class MessageDefinitionAnalysis {

	// MessageDefinition
	private static List<EcoreMessageDefinition> items = new ArrayList<EcoreMessageDefinition>();
	private static Set<String> set = new HashSet<String>();
	
	/**
	 * 解析MessageDefinition
	 * @param isFromBusiness
	 * @param pId
	 * @param eles
	 * @param resource
	 */
	public static void parse(boolean isFromBusiness,String pId, EList<MessageDefinition> eles,XMIResource resource) {
		for(MessageDefinition c:eles) {
			EcoreMessageDefinition po=new EcoreMessageDefinition();
			String cuuid=resource.getID(c);
			if(set.contains(cuuid)) {
				continue ;
			}else {
				set.add(cuuid);
			}
			try {
				BeanUtils.copyProperties(po, c);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
			po.setId(cuuid);
			MessageDefinitionIdentifier messageDefinitionIdentifier=c.getMessageDefinitionIdentifier();
			po.setFlavour(messageDefinitionIdentifier.getFlavour());
			po.setMessageFunctionality(messageDefinitionIdentifier.getMessageFunctionality());
			po.setVersion(messageDefinitionIdentifier.getVersion());
			if(isFromBusiness) {
				po.setBusinessArea(messageDefinitionIdentifier.getBusinessArea());
				po.setBusinessAreaId(pId);
			}
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			if(c.getRemovalDate() != null && c.getRemovalDate() instanceof Date) {
				po.setRemovalDate(c.getRemovalDate());
			}
			//解析公共内容
			CommonAnalysis.parse(cuuid, "11", c, resource);
			//解析MessageBuildingBlock
			if(c.getMessageBuildingBlock()!=null && !c.getMessageBuildingBlock().isEmpty()) {
				MessageBuildingBlockAnalysis.parse(cuuid, c.getMessageBuildingBlock(), resource);
			}
			//解析PreviousVersion
			if(c.getPreviousVersion()!=null) {
				ModelEntity me=c.getPreviousVersion();
				String previousVersion=resource.getID(me);
				po.setPreviousVersion(previousVersion);
			}
			items.add(po);
		}
	}

	public static List<EcoreMessageDefinition> getItems() {
		return items;
	}

	public static Set<String> getSet() {
		return set;
	}

	public static void clear() {
		items.clear();
		set.clear();
	}
	
}
