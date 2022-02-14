package com.cfets.cufir.s.data.analysis;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import com.cfets.cufir.s.data.bean.EcoreExample;

/**
 * 解析Example
 * @author zqh
 *
 */
public class AnalysisIso20022Example {

	// Example
	private static List<EcoreExample> ecoreExamples = new ArrayList<EcoreExample>();
	
	/**
	 * 解析ExternalSchema
	 * @param uuid
	 * @param type
	 * @param examples
	 */
	public static void parseExample(String uuid,String type,EList<String> examples) {
		for(String str:examples) {
			EcoreExample po=new EcoreExample();
			po.setExample(str);
			po.setId(uuid);
			po.setObj_id(uuid);
			po.setObj_type(type);
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			ecoreExamples.add(po);
		}
	}

	public static List<EcoreExample> getEcoreExamples() {
		return ecoreExamples;
	}

	public static void setEcoreExamples(List<EcoreExample> ecoreExamples) {
		AnalysisIso20022Example.ecoreExamples = ecoreExamples;
	}
	
}
