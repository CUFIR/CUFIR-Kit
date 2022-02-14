package org.cufir.s.data.vo;

import java.util.List;

import org.cufir.s.ecore.bean.EcoreConstraint;
import org.cufir.s.ecore.bean.EcoreExample;
import org.cufir.s.ecore.bean.EcoreMessageSet;
import org.cufir.s.ecore.bean.EcoreMessageSetDefinitionRL;

import lombok.Data;

@Data
public class EcoreMessageSetVO {

	private EcoreMessageSet ecoreMessageSet;	
	//样例
	private List<EcoreExample> ecoreExamples;
	//约束
	private List<EcoreConstraint> ecoreConstraints;
	private List<EcoreMessageSetDefinitionRL> ecoreMessageSetDefinitionRLs;
}
