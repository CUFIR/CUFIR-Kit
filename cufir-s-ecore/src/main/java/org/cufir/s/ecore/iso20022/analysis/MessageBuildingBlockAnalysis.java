package org.cufir.s.ecore.iso20022.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.cufir.s.ecore.bean.EcoreMessageBuildingBlock;
import org.cufir.s.ecore.iso20022.ChoiceComponent;
import org.cufir.s.ecore.iso20022.DataType;
import org.cufir.s.ecore.iso20022.MessageBuildingBlock;
import org.cufir.s.ecore.iso20022.MessageComponent;
import org.cufir.s.ecore.iso20022.MessageComponentType;
import org.cufir.s.ecore.iso20022.ModelEntity;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * 解析MessageBuildingBlock
 * @author zqh
 *
 */
public class MessageBuildingBlockAnalysis {

	// MessageBuildingBlock
	private static List<EcoreMessageBuildingBlock> items = new ArrayList<EcoreMessageBuildingBlock>();
	
	/**
	 * 解析MessageBuildingBlock
	 * @param messageId
	 * @param eles
	 * @param resource
	 */
	public static void parse(String messageId, EList<MessageBuildingBlock> eles,XMIResource resource) {
		for(MessageBuildingBlock c:eles) {
			EcoreMessageBuildingBlock po=new EcoreMessageBuildingBlock();
			String dataType="";
			String dataTypeId="";
			try {
				BeanUtils.copyProperties(po, c);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
			if (c.getComplexType() instanceof MessageComponent
					|| c.getComplexType() instanceof ChoiceComponent) {
				dataType="2";
				MessageComponentType mct=c.getComplexType();
				dataTypeId=resource.getID(mct);
			} else {
				dataType="1";
				DataType dt=c.getSimpleType();
				dataTypeId=resource.getID(dt);
			}
			po.setDataType(dataType);
			po.setDataTypeId(dataTypeId);
			String cuuid=resource.getID(c);
			po.setId(cuuid);
			po.setMessageId(messageId);
			po.setCreateUser("Import");
			po.setIsfromiso20022("1");
			if(c.getRemovalDate() != null && c.getRemovalDate() instanceof Date) {
				po.setRemovalDate(c.getRemovalDate());
			}
			//解析公共内容
			CommonAnalysis.parse(cuuid, "12", c, resource);
			//解析PreviousVersion
			if(c.getPreviousVersion()!=null) {
				ModelEntity me=c.getPreviousVersion();
				String previousVersion=resource.getID(me);
				po.setPreviousVersion(previousVersion);
			}
			items.add(po);
		}
	}

	public static List<EcoreMessageBuildingBlock> getItems() {
		return items;
	}

	public static void clear() {
		items.clear();
	}
}
