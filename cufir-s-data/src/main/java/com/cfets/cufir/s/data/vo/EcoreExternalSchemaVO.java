package com.cfets.cufir.s.data.vo;

import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreConstraint;
import com.cfets.cufir.s.data.bean.EcoreExample;
import com.cfets.cufir.s.data.bean.EcoreExternalSchema;
import com.cfets.cufir.s.data.bean.EcoreNamespaceList;
import com.cfets.cufir.s.data.bean.EcoreNextVersions;

public class EcoreExternalSchemaVO {
	
	private EcoreExternalSchema ecoreExternalSchema;
	//样例
	private List<EcoreExample> ecoreExamples;
	//约束
	private List<EcoreConstraint> ecoreConstraints;
	//NextVersions
	private List<EcoreNextVersions> ecoreNextVersions;
	//EcoreNamespaceList
	private List<EcoreNamespaceList> ecoreNamespaceList;
	public EcoreExternalSchema getEcoreExternalSchema() {
		return ecoreExternalSchema;
	}
	public void setEcoreExternalSchema(EcoreExternalSchema ecoreExternalSchema) {
		this.ecoreExternalSchema = ecoreExternalSchema;
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
	public List<EcoreNextVersions> getEcoreNextVersions() {
		return ecoreNextVersions;
	}
	public void setEcoreNextVersions(List<EcoreNextVersions> ecoreNextVersions) {
		this.ecoreNextVersions = ecoreNextVersions;
	}
	public List<EcoreNamespaceList> getEcoreNamespaceList() {
		return ecoreNamespaceList;
	}
	public void setEcoreNamespaceList(List<EcoreNamespaceList> ecoreNamespaceList) {
		this.ecoreNamespaceList = ecoreNamespaceList;
	}
}

