package org.cufir.s.ecore.iso20022.analysis;

import java.util.ArrayList;
import java.util.List;

import org.cufir.s.ecore.bean.EcoreMessageSetDefinitionRL;

/**
 * 解析MessageSetDefinitionRL
 */
public class MessageSetDefinitionRLAnalysis {

	// MessageSetDefinitionRL
	private static List<EcoreMessageSetDefinitionRL> items = new ArrayList<EcoreMessageSetDefinitionRL>();
	
	/**
	 * 解析MessageSetDefinitionRL
	 * @param messageId
	 * @param setId
	 */
	public static void parse(String messageId,String setId) {
		EcoreMessageSetDefinitionRL po=new EcoreMessageSetDefinitionRL();
		po.setMessageId(messageId);
		po.setSetId(setId);
		po.setCreateUser("Import");
		po.setIsfromiso20022("1");
		items.add(po);
	}

	public static List<EcoreMessageSetDefinitionRL> getItems() {
		return items;
	}

	public static void clear() {
		items.clear();
	}
	
}
