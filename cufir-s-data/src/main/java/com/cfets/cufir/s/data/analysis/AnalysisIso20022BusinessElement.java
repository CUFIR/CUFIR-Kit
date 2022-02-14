package com.cfets.cufir.s.data.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.xmi.XMIResource;

import com.cfets.cufir.s.data.bean.EcoreBusinessElement;
import com.cfets.cufir.s.data.iso20022.BusinessAssociationEnd;
import com.cfets.cufir.s.data.iso20022.BusinessAttribute;
import com.cfets.cufir.s.data.iso20022.BusinessComponent;
import com.cfets.cufir.s.data.iso20022.BusinessElement;
import com.cfets.cufir.s.data.iso20022.DataType;
import com.cfets.cufir.s.data.iso20022.ModelEntity;

/**
 * 解析BusinessElement
 * @author zqh
 *
 */
public class AnalysisIso20022BusinessElement {

	// BusinessElement
	private static List<EcoreBusinessElement> ecoreBusinessElements = new ArrayList<EcoreBusinessElement>();
	
	/**
	 * 解析BusinessElement
	 * @param uuid
	 * @param eles
	 * @param resource
	 */
	public static void parseBusinessElement(String uuid, EList<BusinessElement> eles,XMIResource resource) {
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
			//解析公共内容
			AnalysisIso20022Common.parseCommon(uuid, "3", c, resource);
			//解析PreviousVersion
			if(c.getPreviousVersion()!=null) {
				ModelEntity me=c.getPreviousVersion();
				String previousVersion=resource.getID(me);
				po.setPreviousVersion(previousVersion);
			}
			ecoreBusinessElements.add(po);
		}
	}

	public static List<EcoreBusinessElement> getEcoreBusinessElements() {
		return ecoreBusinessElements;
	}

	public static void setEcoreBusinessElements(List<EcoreBusinessElement> ecoreBusinessElements) {
		AnalysisIso20022BusinessElement.ecoreBusinessElements = ecoreBusinessElements;
	}
}
