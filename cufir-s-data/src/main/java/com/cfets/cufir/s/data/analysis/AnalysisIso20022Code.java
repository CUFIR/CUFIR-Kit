package com.cfets.cufir.s.data.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.xmi.XMIResource;

import com.cfets.cufir.s.data.bean.EcoreCode;
import com.cfets.cufir.s.data.iso20022.Code;

/**
 * 解析Code
 * @author zqh
 *
 */
public class AnalysisIso20022Code {

	// Code
	private static List<EcoreCode> ecoreCodes = new ArrayList<EcoreCode>();
	private static Map<String,String> traceCode =new HashMap<String,String>();
	
	/**
	 * 解析Code
	 * @param uuid
	 * @param codes
	 * @param resource
	 */
	public static void parseCode(String uuid, EList<Code> codes,XMIResource resource) {
		for(Code c:codes) {
			EcoreCode po=new EcoreCode();
			try {
				BeanUtils.copyProperties(po, c);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
			String cuuid=resource.getID(c);
			po.setId(cuuid);
			po.setCodesetid(uuid);
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			//解析公共内容
			AnalysisIso20022Common.parseCommon(cuuid, "13", c, resource);
			ecoreCodes.add(po);
			traceCode.put(uuid+"_"+c.getName(), c.getCodeName());
		}
	}
	
	/**
	 * 解析NullCode
	 */
	public static void parseNullCode() {
		for(EcoreCode c:ecoreCodes) {
			if(c.getCodeName()==null) {
				String trace=AnalysisIso20022DataType.getTraceType().get(c.getCodesetid());
				String codeName=traceCode.get(trace+"_"+c.getName());
				c.setCodeName(codeName);
			}
		}
	}

	public static List<EcoreCode> getEcoreCodes() {
		return ecoreCodes;
	}

	public static void setEcoreCodes(List<EcoreCode> ecoreCodes) {
		AnalysisIso20022Code.ecoreCodes = ecoreCodes;
	}

	public static Map<String, String> getTraceCode() {
		return traceCode;
	}

	public static void setTraceCode(Map<String, String> traceCode) {
		AnalysisIso20022Code.traceCode = traceCode;
	}
	
}
