package com.cfets.cufir.s.data.vo;

import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreConstraint;
import com.cfets.cufir.s.data.bean.EcoreExample;
import com.cfets.cufir.s.data.bean.EcoreMessageElement;
import com.cfets.cufir.s.data.bean.EcoreNextVersions;

public class EcoreMessageElementVO {

	private EcoreMessageElement ecoreMessageElement;
	//样例
	private List<EcoreExample> ecoreExamples;
	//约束
	private List<EcoreConstraint> ecoreConstraints;
	//NextVersions
	private List<EcoreNextVersions> ecoreNextVersions;
	//Synonym
	private List<SynonymVO> synonyms;
	public EcoreMessageElement getEcoreMessageElement() {
		return ecoreMessageElement;
	}
	public void setEcoreMessageElement(EcoreMessageElement ecoreMessageElement) {
		this.ecoreMessageElement = ecoreMessageElement;
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
	public List<SynonymVO> getSynonyms() {
		return synonyms;
	}
	public void setSynonyms(List<SynonymVO> synonyms) {
		this.synonyms = synonyms;
	}
}

