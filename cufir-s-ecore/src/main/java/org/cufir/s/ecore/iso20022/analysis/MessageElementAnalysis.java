package org.cufir.s.ecore.iso20022.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.cufir.s.ecore.bean.EcoreMessageElement;
import org.cufir.s.ecore.iso20022.BusinessComponent;
import org.cufir.s.ecore.iso20022.BusinessElement;
import org.cufir.s.ecore.iso20022.DataType;
import org.cufir.s.ecore.iso20022.ExternalSchema;
import org.cufir.s.ecore.iso20022.MessageAssociationEnd;
import org.cufir.s.ecore.iso20022.MessageAttribute;
import org.cufir.s.ecore.iso20022.MessageComponentType;
import org.cufir.s.ecore.iso20022.MessageElement;
import org.cufir.s.ecore.iso20022.ModelEntity;
import org.cufir.s.ecore.iso20022.UserDefined;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * 解析MessageElement
 * @author zqh
 *
 */
public class MessageElementAnalysis {

	// MessageElement
	private static List<EcoreMessageElement> items = new ArrayList<EcoreMessageElement>();
	
	/**
	 * 解析MessageElement
	 * @param messageComponentId
	 * @param eles
	 * @param resource
	 */
	public static void parse(String messageComponentId, EList<MessageElement> eles,XMIResource resource) {
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
			if(c.getRemovalDate() != null && c.getRemovalDate() instanceof Date) {
				po.setRemovalDate(c.getRemovalDate());
			}
			//解析公共内容
			CommonAnalysis.parse(cuuid, "10", c, resource);
			//解析PreviousVersion
			if(c.getPreviousVersion()!=null) {
				ModelEntity me=c.getPreviousVersion();
				String previousVersion=resource.getID(me);
				po.setPreviousVersion(previousVersion);
			}
			items.add(po);
		}
	}

	public static List<EcoreMessageElement> getItems() {
		return items;
	}

	public static void clear() {
		items.clear();
	}
	
}
