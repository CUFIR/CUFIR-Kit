package com.cfets.cufir.s.data.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.xmi.XMIResource;

import com.cfets.cufir.s.data.bean.EcoreMessageElement;
import com.cfets.cufir.s.data.iso20022.BusinessComponent;
import com.cfets.cufir.s.data.iso20022.BusinessElement;
import com.cfets.cufir.s.data.iso20022.DataType;
import com.cfets.cufir.s.data.iso20022.ExternalSchema;
import com.cfets.cufir.s.data.iso20022.MessageAssociationEnd;
import com.cfets.cufir.s.data.iso20022.MessageAttribute;
import com.cfets.cufir.s.data.iso20022.MessageComponentType;
import com.cfets.cufir.s.data.iso20022.MessageElement;
import com.cfets.cufir.s.data.iso20022.ModelEntity;
import com.cfets.cufir.s.data.iso20022.UserDefined;

/**
 * 解析MessageElement
 * @author zqh
 *
 */
public class AnalysisIso20022MessageElement {

	// MessageElement
	private static List<EcoreMessageElement> ecoreMessageElements = new ArrayList<EcoreMessageElement>();
	
	/**
	 * 解析MessageElement
	 * @param messageComponentId
	 * @param eles
	 * @param resource
	 */
	public static void parseMessageElement(String messageComponentId, EList<MessageElement> eles,XMIResource resource) {
		for(MessageElement c:eles) {
			EcoreMessageElement po=new EcoreMessageElement();
			String type="";
			String typeId="";
			if(c instanceof MessageAttribute) {
				MessageAttribute aobj=(MessageAttribute)c;
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
					MessageComponentType mt=aobj.getComplexType();
					typeId=resource.getID(mt);
					type="2";
					try {
						BeanUtils.copyProperties(po, aobj);
					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}
					if(mt instanceof UserDefined) {
						type="3";
					} else if(mt instanceof ExternalSchema) {
						type="4";
					}
					//获取type的trace信息
					if(mt.getTrace()!=null) {
						po.setTypeOfTracesTo(mt.getTrace().getName());
					}
				}
				po.setIsMessageAssociationEnd("0");
			}else if(c instanceof MessageAssociationEnd) {
				MessageAssociationEnd mobj=(MessageAssociationEnd)c;
				MessageComponentType mt=mobj.getType();
				typeId=resource.getID(mt);
				type="2";
				try {
					BeanUtils.copyProperties(po, mobj);
				} catch (IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}
				if(mt instanceof UserDefined) {
					type="3";
				} else if(mt instanceof ExternalSchema) {
					type="4";
				}
				po.setIsMessageAssociationEnd("1");
				//获取type的trace信息
				if(mt.getTrace()!=null) {
					po.setTypeOfTracesTo(mt.getTrace().getName());
				}
			}
			//trace
			if(c.getBusinessElementTrace()!=null) {
				BusinessElement bel=c.getBusinessElementTrace();
				po.setTrace(resource.getID(bel));
				po.setTraceType("1");
				po.setTracesTo(bel.getName());
				po.setTechnical("false");
				po.setTracePath(bel.getName());
			} else {
				if(c.getBusinessComponentTrace()!=null) {
					BusinessComponent bcl=c.getBusinessComponentTrace();
					po.setTrace(resource.getID(bcl));
					po.setTraceType("2");
					po.setTracesTo(bcl.getName());
					po.setTechnical("false");
					po.setTracePath(bcl.getName());
				}else {
					po.setTechnical("true");
					po.setTypeOfTracesTo("--TECHNICAL--");
				}
			}
			String cuuid=resource.getID(c);
			po.setId(cuuid);
			po.setType(type);
			po.setTypeId(typeId);
			po.setMessageComponentId(messageComponentId);
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			//解析公共内容
			AnalysisIso20022Common.parseCommon(cuuid, "10", c, resource);
			//解析PreviousVersion
			if(c.getPreviousVersion()!=null) {
				ModelEntity me=c.getPreviousVersion();
				String previousVersion=resource.getID(me);
				po.setPreviousVersion(previousVersion);
			}
			ecoreMessageElements.add(po);
		}
	}

	public static List<EcoreMessageElement> getEcoreMessageElements() {
		return ecoreMessageElements;
	}

	public static void setEcoreMessageElements(List<EcoreMessageElement> ecoreMessageElements) {
		AnalysisIso20022MessageElement.ecoreMessageElements = ecoreMessageElements;
	}
	
}
