package com.cfets.cufir.s.data.vo;

import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreBusinessArea;
import com.cfets.cufir.s.data.bean.EcoreConstraint;
import com.cfets.cufir.s.data.bean.EcoreExample;

public class EcoreBusinessAreaVO {

	private EcoreBusinessArea ecoreBusinessArea;	
	//样例
	private List<EcoreExample> ecoreExamples;
	//约束
	private List<EcoreConstraint> ecoreConstraints;
	private List<String> ecoreMessageDefinitionIds;
	
	public List<EcoreExample> getEcoreExamples() {
		return ecoreExamples;
	}
	public void setEcoreExamples(List<EcoreExample> ecoreExamples) {
		this.ecoreExamples = ecoreExamples;
	}
	public List<EcoreConstraint> getEcoreConstraints() {
		return ecoreConstraints;
	}
	public void setEcoreConstraints(List<EcoreConstraint> ecoreConstraints) {
		this.ecoreConstraints = ecoreConstraints;
	}
	public EcoreBusinessArea getEcoreBusinessArea() {
		return ecoreBusinessArea;
	}
	public void setEcoreBusinessArea(EcoreBusinessArea ecoreBusinessArea) {
		this.ecoreBusinessArea = ecoreBusinessArea;
	}
	public List<String> getEcoreMessageDefinitionIds() {
		return ecoreMessageDefinitionIds;
	}
	public void setEcoreMessageDefinitionIds(List<String> ecoreMessageDefinitionIds) {
		this.ecoreMessageDefinitionIds = ecoreMessageDefinitionIds;
	}
}
