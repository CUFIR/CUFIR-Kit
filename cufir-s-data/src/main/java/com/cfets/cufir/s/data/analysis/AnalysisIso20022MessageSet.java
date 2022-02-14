package com.cfets.cufir.s.data.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.emf.ecore.xmi.XMIResource;

import com.cfets.cufir.s.data.bean.EcoreMessageSet;
import com.cfets.cufir.s.data.iso20022.MessageDefinition;
import com.cfets.cufir.s.data.iso20022.MessageSet;
import com.cfets.cufir.s.data.iso20022.TopLevelCatalogueEntry;

/**
 * 解析MessageSet
 * @author zqh
 *
 */
public class AnalysisIso20022MessageSet {

	// MessageSet
	private static List<EcoreMessageSet> ecoreMessageSets = new ArrayList<EcoreMessageSet>();
	
	/**
	 * 解析MessageSet
	 * @param obj
	 * @param resource
	 */
	public static void parseMessageSet(TopLevelCatalogueEntry obj,XMIResource resource) {
		EcoreMessageSet po=new EcoreMessageSet();
		MessageSet ms=(MessageSet)obj;
		try {
			BeanUtils.copyProperties(po, ms);
			String uuid=resource.getID(ms);
			po.setId(uuid);
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			//解析公共内容
			AnalysisIso20022Common.parseCommon(uuid, "7", obj, resource);
			//解析MessageDefinition
			if(ms.getMessageDefinition()!=null && !ms.getMessageDefinition().isEmpty()) {
				AnalysisIso20022MessageDefinition.parseMessageDefinition(false,uuid, ms.getMessageDefinition(), resource);
				for(MessageDefinition c:ms.getMessageDefinition()) {
					//解析MessageSetDefinitionRL
					AnalysisIso20022MessageSetDefinitionRL.parseMessageSetDefinitionRL(resource.getID(c), uuid);
				}
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		ecoreMessageSets.add(po);
	}

	public static List<EcoreMessageSet> getEcoreMessageSets() {
		return ecoreMessageSets;
	}

	public static void setEcoreMessageSets(List<EcoreMessageSet> ecoreMessageSets) {
		AnalysisIso20022MessageSet.ecoreMessageSets = ecoreMessageSets;
	}
	
}
