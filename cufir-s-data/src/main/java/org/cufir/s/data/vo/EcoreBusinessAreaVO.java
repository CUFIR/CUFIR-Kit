package org.cufir.s.data.vo;

import java.util.List;

import org.cufir.s.ecore.bean.EcoreBusinessArea;
import org.cufir.s.ecore.bean.EcoreConstraint;
import org.cufir.s.ecore.bean.EcoreExample;

import lombok.Data;

@Data
public class EcoreBusinessAreaVO {

	private EcoreBusinessArea ecoreBusinessArea;	
	//样例
	private List<EcoreExample> ecoreExamples;
	//约束
	private List<EcoreConstraint> ecoreConstraints;
	private List<String> ecoreMessageDefinitionIds;
}
