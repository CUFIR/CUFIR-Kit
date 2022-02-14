package com.cfets.cufir.s.data.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.xmi.XMIResource;

import com.cfets.cufir.s.data.bean.EcoreMessageDefinition;
import com.cfets.cufir.s.data.iso20022.MessageDefinition;
import com.cfets.cufir.s.data.iso20022.MessageDefinitionIdentifier;
import com.cfets.cufir.s.data.iso20022.ModelEntity;

/**
 * 解析MessageDefinition
 * @author zqh
 *
 */
public class AnalysisIso20022MessageDefinition {

	// MessageDefinition
	private static List<EcoreMessageDefinition> ecoreMessageDefinitions = new ArrayList<EcoreMessageDefinition>();
	private static Set<String> definitionIds = new HashSet<String>();
	
	/**
	 * 解析MessageDefinition
	 * @param isFromBusiness
	 * @param pId
	 * @param eles
	 * @param resource
	 */
	public static void parseMessageDefinition(boolean isFromBusiness,String pId, EList<MessageDefinition> eles,XMIResource resource) {
		for(MessageDefinition c:eles) {
			EcoreMessageDefinition po=new EcoreMessageDefinition();
			String cuuid=resource.getID(c);
			if(definitionIds.contains(cuuid)) {
				continue ;
			}else {
				definitionIds.add(cuuid);
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
			//解析公共内容
			AnalysisIso20022Common.parseCommon(cuuid, "11", c, resource);
			//解析MessageBuildingBlock
			if(c.getMessageBuildingBlock()!=null && !c.getMessageBuildingBlock().isEmpty()) {
				AnalysisIso20022MessageBuildingBlock.parseMessageBuildingBlock(cuuid, c.getMessageBuildingBlock(), resource);
			}
			//解析PreviousVersion
			if(c.getPreviousVersion()!=null) {
				ModelEntity me=c.getPreviousVersion();
				String previousVersion=resource.getID(me);
				po.setPreviousVersion(previousVersion);
			}
			ecoreMessageDefinitions.add(po);
		}
	}

	public static List<EcoreMessageDefinition> getEcoreMessageDefinitions() {
		return ecoreMessageDefinitions;
	}

	public static void setEcoreMessageDefinitions(List<EcoreMessageDefinition> ecoreMessageDefinitions) {
		AnalysisIso20022MessageDefinition.ecoreMessageDefinitions = ecoreMessageDefinitions;
	}

	public static Set<String> getDefinitionIds() {
		return definitionIds;
	}

	public static void setDefinitionIds(Set<String> definitionIds) {
		AnalysisIso20022MessageDefinition.definitionIds = definitionIds;
	}
	
}
