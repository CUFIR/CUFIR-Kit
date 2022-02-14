package com.cfets.cufir.s.data.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.emf.ecore.xmi.XMIResource;

import com.cfets.cufir.s.data.bean.EcoreDataType;
import com.cfets.cufir.s.data.iso20022.Amount;
import com.cfets.cufir.s.data.iso20022.Binary;
import com.cfets.cufir.s.data.iso20022.Boolean;
import com.cfets.cufir.s.data.iso20022.CodeSet;
import com.cfets.cufir.s.data.iso20022.Date;
import com.cfets.cufir.s.data.iso20022.DateTime;
import com.cfets.cufir.s.data.iso20022.Day;
import com.cfets.cufir.s.data.iso20022.Decimal;
import com.cfets.cufir.s.data.iso20022.IdentifierSet;
import com.cfets.cufir.s.data.iso20022.Indicator;
import com.cfets.cufir.s.data.iso20022.ModelEntity;
import com.cfets.cufir.s.data.iso20022.Month;
import com.cfets.cufir.s.data.iso20022.MonthDay;
import com.cfets.cufir.s.data.iso20022.Quantity;
import com.cfets.cufir.s.data.iso20022.Rate;
import com.cfets.cufir.s.data.iso20022.SchemaType;
import com.cfets.cufir.s.data.iso20022.Text;
import com.cfets.cufir.s.data.iso20022.Time;
import com.cfets.cufir.s.data.iso20022.TopLevelDictionaryEntry;
import com.cfets.cufir.s.data.iso20022.UserDefined;
import com.cfets.cufir.s.data.iso20022.Year;
import com.cfets.cufir.s.data.iso20022.YearMonth;

/**
 * 解析DataType
 * @author zqh
 *
 */
public class AnalysisIso20022DataType {

	// DataType
	private static List<EcoreDataType> ecoreDataTypes = new ArrayList<EcoreDataType>();
	private static Map<String,String> traceType =new HashMap<String,String>();
	
	/**
	 * 解析DataType
	 * @param obj
	 * @param resource
	 */
	public static void parseDataType(TopLevelDictionaryEntry obj,XMIResource resource) {
		EcoreDataType po=new EcoreDataType();
		try {
			String type="";
			BeanUtils.copyProperties(po, obj);
			String uuid=resource.getID(obj);
			po.setId(uuid);
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			if (obj instanceof Rate) {
				type="6";
			} else if (obj instanceof Year || obj instanceof Date || obj instanceof DateTime
					|| obj instanceof YearMonth || obj instanceof Day || obj instanceof MonthDay 
					|| obj instanceof Month || obj instanceof Time) {
				type="9";
			} else if (obj instanceof Text || obj instanceof IdentifierSet) {
				type="2";
			} else if (obj instanceof Binary) {
				type="10";
			} else if (obj instanceof Amount) {
				type="7";
			} else if (obj instanceof Quantity) {
				type="8";
			} else if (obj instanceof Indicator) {
				type="4";
			} else if (obj instanceof Decimal) {
				type="5";
			} else if (obj instanceof Boolean) {
				type="3";
			} else if (obj instanceof CodeSet) {
				CodeSet codeSet=(CodeSet)obj;
				type="1";
				//解析Code
				if(codeSet.getCode()!=null && !codeSet.getCode().isEmpty()) {
					AnalysisIso20022Code.parseCode(uuid,codeSet.getCode(),resource);
				}
				//解析trace
				if(codeSet.getTrace()!=null) {
					CodeSet cs=codeSet.getTrace();
					String trace=resource.getID(cs);
					po.setTrace(trace);
					traceType.put(uuid, trace);
				};
			} else if (obj instanceof SchemaType) {
				type="11";
			} else if (obj instanceof UserDefined) {
				type="12";
			} 
			po.setType(type);
			po.setCreateUser("Import");
			//解析公共内容
			AnalysisIso20022Common.parseCommon(uuid, "1", obj, resource);
			//解析PreviousVersion
			if(obj.getPreviousVersion()!=null) {
				ModelEntity me=obj.getPreviousVersion();
				String previousVersion=resource.getID(me);
				po.setPreviousVersion(previousVersion);
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		ecoreDataTypes.add(po);
	}

	public static List<EcoreDataType> getEcoreDataTypes() {
		return ecoreDataTypes;
	}

	public static void setEcoreDataTypes(List<EcoreDataType> ecoreDataTypes) {
		AnalysisIso20022DataType.ecoreDataTypes = ecoreDataTypes;
	}

	public static Map<String, String> getTraceType() {
		return traceType;
	}

	public static void setTraceType(Map<String, String> traceType) {
		AnalysisIso20022DataType.traceType = traceType;
	}
	
}
