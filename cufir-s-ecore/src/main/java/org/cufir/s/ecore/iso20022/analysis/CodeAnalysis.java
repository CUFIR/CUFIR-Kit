package org.cufir.s.ecore.iso20022.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.cufir.s.ecore.bean.EcoreCode;
import org.cufir.s.ecore.iso20022.Code;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * 解析Code
 * @author zqh
 *
 */
public class CodeAnalysis {

	// Code
	private static List<EcoreCode> items = new ArrayList<EcoreCode>();
	private static Map<String,String> map =new HashMap<String,String>();
	
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
			if(c.getRemovalDate() != null && c.getRemovalDate() instanceof Date) {
				po.setRemovalDate(c.getRemovalDate());
			}
			//解析公共内容
			CommonAnalysis.parse(cuuid, "13", c, resource);
			items.add(po);
			map.put(uuid+"_"+c.getName(), c.getCodeName());
		}
	}
	
	/**
	 * 解析NullCode
	 */
	public static void parseNullCode() {
		for(EcoreCode c:items) {
			if(c.getCodeName()==null) {
				String trace=DataTypeAnalysis.getMap().get(c.getCodesetid());
				String codeName=map.get(trace+"_"+c.getName());
				c.setCodeName(codeName);
			}
		}
	}

	public static List<EcoreCode> getItems() {
		return items;
	}

	public static void clear() {
		items.clear();
		map.clear();
	}

	public static Map<String, String> getMap() {
		return map;
	}
}
