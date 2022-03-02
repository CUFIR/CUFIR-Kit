package org.cufir.s.ecore.iso20022.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.cufir.s.ecore.bean.EcoreDataType;
import org.cufir.s.ecore.iso20022.Amount;
import org.cufir.s.ecore.iso20022.Binary;
import org.cufir.s.ecore.iso20022.Boolean;
import org.cufir.s.ecore.iso20022.CodeSet;
import org.cufir.s.ecore.iso20022.Date;
import org.cufir.s.ecore.iso20022.DateTime;
import org.cufir.s.ecore.iso20022.Day;
import org.cufir.s.ecore.iso20022.Decimal;
import org.cufir.s.ecore.iso20022.IdentifierSet;
import org.cufir.s.ecore.iso20022.Indicator;
import org.cufir.s.ecore.iso20022.ModelEntity;
import org.cufir.s.ecore.iso20022.Month;
import org.cufir.s.ecore.iso20022.MonthDay;
import org.cufir.s.ecore.iso20022.Quantity;
import org.cufir.s.ecore.iso20022.Rate;
import org.cufir.s.ecore.iso20022.SchemaType;
import org.cufir.s.ecore.iso20022.Text;
import org.cufir.s.ecore.iso20022.Time;
import org.cufir.s.ecore.iso20022.TopLevelDictionaryEntry;
import org.cufir.s.ecore.iso20022.UserDefined;
import org.cufir.s.ecore.iso20022.Year;
import org.cufir.s.ecore.iso20022.YearMonth;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * 解析DataType
 */
public class DataTypeAnalysis {

	// DataType
	private static List<EcoreDataType> items = new ArrayList<EcoreDataType>();
	private static Map<String,String> map =new HashMap<String,String>();
	
	/**
	 * 解析DataType
	 * @param obj
	 * @param resource
	 */
	public static void parse(TopLevelDictionaryEntry obj,XMIResource resource) {
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
					CodeAnalysis.parseCode(uuid,codeSet.getCode(),resource);
				}
				//解析trace
				if(codeSet.getTrace()!=null) {
					CodeSet cs=codeSet.getTrace();
					String trace=resource.getID(cs);
					po.setTrace(trace);
					if(cs.getRemovalDate() != null && cs.getRemovalDate() instanceof java.util.Date) {
						po.setRemovalDate(cs.getRemovalDate());
					}
					map.put(uuid, trace);
				};
			} else if (obj instanceof SchemaType) {
				type="11";
			} else if (obj instanceof UserDefined) {
				type="12";
			} 
			po.setType(type);
			po.setCreateUser("Import");
			//解析公共内容
			CommonAnalysis.parse(uuid, "1", obj, resource);
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

	public static List<EcoreDataType> getItems() {
		return items;
	}

	public static Map<String, String> getMap() {
		return map;
	}

	public static void clear() {
		items.clear();
		map.clear();
	}
	
}
