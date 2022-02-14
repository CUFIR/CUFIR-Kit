package com.cfets.cufir.s.data.analysis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.xmi.XMIResource;

import com.cfets.cufir.s.data.bean.EcoreMessageBuildingBlock;
import com.cfets.cufir.s.data.iso20022.ChoiceComponent;
import com.cfets.cufir.s.data.iso20022.DataType;
import com.cfets.cufir.s.data.iso20022.MessageBuildingBlock;
import com.cfets.cufir.s.data.iso20022.MessageComponent;
import com.cfets.cufir.s.data.iso20022.MessageComponentType;
import com.cfets.cufir.s.data.iso20022.ModelEntity;

/**
 * 解析MessageBuildingBlock
 * @author zqh
 *
 */
public class AnalysisIso20022MessageBuildingBlock {

	// MessageBuildingBlock
	private static List<EcoreMessageBuildingBlock> ecoreEcoreMessageBuildingBlocks = new ArrayList<EcoreMessageBuildingBlock>();
	
	/**
	 * 解析MessageBuildingBlock
	 * @param messageId
	 * @param eles
	 * @param resource
	 */
	public static void parseMessageBuildingBlock(String messageId, EList<MessageBuildingBlock> eles,XMIResource resource) {
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
			//解析公共内容
			AnalysisIso20022Common.parseCommon(cuuid, "12", c, resource);
			//解析PreviousVersion
			if(c.getPreviousVersion()!=null) {
				ModelEntity me=c.getPreviousVersion();
				String previousVersion=resource.getID(me);
				po.setPreviousVersion(previousVersion);
			}
			ecoreEcoreMessageBuildingBlocks.add(po);
		}
	}

	public static List<EcoreMessageBuildingBlock> getEcoreEcoreMessageBuildingBlocks() {
		return ecoreEcoreMessageBuildingBlocks;
	}

	public static void setEcoreEcoreMessageBuildingBlocks(List<EcoreMessageBuildingBlock> ecoreEcoreMessageBuildingBlocks) {
		AnalysisIso20022MessageBuildingBlock.ecoreEcoreMessageBuildingBlocks = ecoreEcoreMessageBuildingBlocks;
	}
	
}
