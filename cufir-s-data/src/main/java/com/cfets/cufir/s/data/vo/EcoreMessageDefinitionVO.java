package com.cfets.cufir.s.data.vo;

import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreBusinessArea;
import com.cfets.cufir.s.data.bean.EcoreConstraint;
import com.cfets.cufir.s.data.bean.EcoreExample;
import com.cfets.cufir.s.data.bean.EcoreMessageDefinition;
import com.cfets.cufir.s.data.bean.EcoreNextVersions;

public class EcoreMessageDefinitionVO {

	private EcoreMessageDefinition ecoreMessageDefinition;
	//EcoreBusinessArea
	private EcoreBusinessArea ecoreBusinessArea;
	//样例
	private List<EcoreExample> ecoreExamples;
	//约束
	private List<EcoreConstraint> ecoreConstraints;
	//NextVersions
	private List<EcoreNextVersions> ecoreNextVersions;
	//EcoreMessageBuildingBlockVO
	private List<EcoreMessageBuildingBlockVO> ecoreMessageBuildingBlockVOs;
	//Synonym
	private List<SynonymVO> synonyms;
	//保存删除的对象集合
	private List<RemovedObjectVO> removedObjectVOs;
	public EcoreMessageDefinition getEcoreMessageDefinition() {
		return ecoreMessageDefinition;
	}
	public void setEcoreMessageDefinition(EcoreMessageDefinition ecoreMessageDefinition) {
		this.ecoreMessageDefinition = ecoreMessageDefinition;
	}
	public EcoreBusinessArea getEcoreBusinessArea() {
		return ecoreBusinessArea;
	}
	public void setEcoreBusinessArea(EcoreBusinessArea ecoreBusinessArea) {
		this.ecoreBusinessArea = ecoreBusinessArea;
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
	public List<EcoreMessageBuildingBlockVO> getEcoreMessageBuildingBlockVOs() {
		return ecoreMessageBuildingBlockVOs;
	}
	public void setEcoreMessageBuildingBlockVOs(List<EcoreMessageBuildingBlockVO> ecoreMessageBuildingBlockVOs) {
		this.ecoreMessageBuildingBlockVOs = ecoreMessageBuildingBlockVOs;
	}
	public List<SynonymVO> getSynonyms() {
		return synonyms;
	}
	public void setSynonyms(List<SynonymVO> synonyms) {
		this.synonyms = synonyms;
	}
	public List<RemovedObjectVO> getRemovedObjectVOs() {
		return removedObjectVOs;
	}
	public void setRemovedObjectVOs(List<RemovedObjectVO> removedObjectVOs) {
		this.removedObjectVOs = removedObjectVOs;
	}
}
