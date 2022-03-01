package org.cufir.s.data.vo;

import java.util.List;

import org.cufir.s.ecore.bean.EcoreConstraint;
import org.cufir.s.ecore.bean.EcoreExample;
import org.cufir.s.ecore.bean.EcoreMessageComponent;
import org.cufir.s.ecore.bean.EcoreNextVersions;

import lombok.Data;

@Data
public class EcoreMessageComponentVO {

	private EcoreMessageComponent ecoreMessageComponent;
	//样例
	private List<EcoreExample> ecoreExamples;
	//约束
	private List<EcoreConstraint> ecoreConstraints;
	//NextVersions
	private List<EcoreNextVersions> ecoreNextVersions;
	//codes
	private List<EcoreMessageElementVO> ecoreMessageElementVOs;
	//保存删除的对象集合
	private List<RemovedObjectVO> removedObjectVOs;
}

