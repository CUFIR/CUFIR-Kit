package org.cufir.s.data.vo;

import java.util.List;

import org.cufir.s.ecore.bean.EcoreBusinessArea;
import org.cufir.s.ecore.bean.EcoreConstraint;
import org.cufir.s.ecore.bean.EcoreExample;
import org.cufir.s.ecore.bean.EcoreMessageDefinition;
import org.cufir.s.ecore.bean.EcoreNextVersions;

import lombok.Data;

@Data
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
}
