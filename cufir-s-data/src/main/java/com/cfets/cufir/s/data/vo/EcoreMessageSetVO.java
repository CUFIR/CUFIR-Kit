package com.cfets.cufir.s.data.vo;

import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreConstraint;
import com.cfets.cufir.s.data.bean.EcoreExample;
import com.cfets.cufir.s.data.bean.EcoreMessageSet;
import com.cfets.cufir.s.data.bean.EcoreMessageSetDefinitionRL;

public class EcoreMessageSetVO {

	private EcoreMessageSet ecoreMessageSet;	
	//样例
	private List<EcoreExample> ecoreExamples;
	//约束
	private List<EcoreConstraint> ecoreConstraints;
	private List<EcoreMessageSetDefinitionRL> ecoreMessageSetDefinitionRLs;
	public EcoreMessageSet getEcoreMessageSet() {
		return ecoreMessageSet;
	}
	public void setEcoreMessageSet(EcoreMessageSet ecoreMessageSet) {
		this.ecoreMessageSet = ecoreMessageSet;
	}
	public List<EcoreMessageSetDefinitionRL> getEcoreMessageSetDefinitionRLs() {
		return ecoreMessageSetDefinitionRLs;
	}
	public void setEcoreMessageSetDefinitionRLs(List<EcoreMessageSetDefinitionRL> ecoreMessageSetDefinitionRLs) {
		this.ecoreMessageSetDefinitionRLs = ecoreMessageSetDefinitionRLs;
	}
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
}
