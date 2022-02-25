package org.cufir.s.ecore.iso20022.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.cufir.s.ecore.bean.EcoreBusinessElement;
import org.cufir.s.ecore.iso20022.BusinessAssociationEnd;
import org.cufir.s.ecore.iso20022.BusinessAttribute;
import org.cufir.s.ecore.iso20022.BusinessComponent;
import org.cufir.s.ecore.iso20022.BusinessElement;
import org.cufir.s.ecore.iso20022.DataType;
import org.cufir.s.ecore.iso20022.ModelEntity;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * 解析BusinessElement
 * @author zqh
 *
 */
public class BusinessElementAnalysis {

	// BusinessElement
	private static List<EcoreBusinessElement> items = new ArrayList<EcoreBusinessElement>();
	
	/**
	 * 解析BusinessElement
	 * @param uuid
	 * @param eles
	 * @param resource
	 */
	public static void parse(String uuid, EList<BusinessElement> eles,XMIResource resource) {
		String type="";
		String typeId="";
		for(BusinessElement c:eles) {
			EcoreBusinessElement po=new EcoreBusinessElement();
			if(c instanceof BusinessAttribute) {
				BusinessAttribute aobj=(BusinessAttribute)c;
				if(aobj.getSimpleType()!=null) {
					DataType dt=aobj.getSimpleType();
					typeId=resource.getID(dt);
					type="1";
					try {
						BeanUtils.copyProperties(po, aobj);
					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}else {
					BusinessComponent mt=aobj.getComplexType();
					typeId=resource.getID(mt);
					type="2";
					try {
						BeanUtils.copyProperties(po, mt);
					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
				po.setIsMessageAssociationEnd("0");
			}else if(c instanceof BusinessAssociationEnd) {
				BusinessAssociationEnd mobj=(BusinessAssociationEnd)c;
				BusinessComponent mt=mobj.getType();
				typeId=resource.getID(mt);
				type="2";
				try {
					BeanUtils.copyProperties(po, mobj);
				} catch (IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}
				po.setIsMessageAssociationEnd("1");
			}
			String cuuid=resource.getID(c);
			po.setId(cuuid);
			po.setType(type);
			po.setTypeId(typeId);
			po.setBusinessComponentId(uuid);
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			if(c.getRemovalDate() != null && c.getRemovalDate() instanceof Date) {
				po.setRemovalDate(c.getRemovalDate());
			}
			//解析公共内容
			CommonAnalysis.parse(uuid, "3", c, resource);
			//解析PreviousVersion
			if(c.getPreviousVersion()!=null) {
				ModelEntity me=c.getPreviousVersion();
				String previousVersion=resource.getID(me);
				po.setPreviousVersion(previousVersion);
			}
			items.add(po);
		}
	}

	public static List<EcoreBusinessElement> getItems() {
		return items;
	}

	public static void clear() {
		items.clear();
	}
}
