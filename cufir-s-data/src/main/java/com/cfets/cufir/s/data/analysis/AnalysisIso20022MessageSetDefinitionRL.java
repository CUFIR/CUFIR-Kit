package com.cfets.cufir.s.data.analysis;

import java.util.ArrayList;
import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreMessageSetDefinitionRL;

/**
 * 解析MessageSetDefinitionRL
 * @author zqh
 *
 */
public class AnalysisIso20022MessageSetDefinitionRL {

	// MessageSetDefinitionRL
	private static List<EcoreMessageSetDefinitionRL> ecoreMessageSetDefinitionRLs = new ArrayList<EcoreMessageSetDefinitionRL>();
	
	/**
	 * 解析MessageSetDefinitionRL
	 * @param messageId
	 * @param setId
	 */
	public static void parseMessageSetDefinitionRL(String messageId,String setId) {
		EcoreMessageSetDefinitionRL po=new EcoreMessageSetDefinitionRL();
		po.setMessageId(messageId);
		po.setSetId(setId);
		po.setCreateUser("Import");
		po.setIsfromiso20022("1");
		ecoreMessageSetDefinitionRLs.add(po);
	}

	public static List<EcoreMessageSetDefinitionRL> getEcoreMessageSetDefinitionRLs() {
		return ecoreMessageSetDefinitionRLs;
	}

	public static void setEcoreMessageSetDefinitionRLs(List<EcoreMessageSetDefinitionRL> ecoreMessageSetDefinitionRLs) {
		AnalysisIso20022MessageSetDefinitionRL.ecoreMessageSetDefinitionRLs = ecoreMessageSetDefinitionRLs;
	}
	
}
