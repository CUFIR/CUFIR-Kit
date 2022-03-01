package org.cufir.s.data.vo;

import java.util.List;

import org.cufir.s.ecore.bean.EcoreConstraint;
import org.cufir.s.ecore.bean.EcoreDataType;
import org.cufir.s.ecore.bean.EcoreExample;
import org.cufir.s.ecore.bean.EcoreNextVersions;

import lombok.Data;

@Data
public class EcoreDataTypeVO {

	private EcoreDataType ecoreDataType;
	//样例
	private List<EcoreExample> ecoreExamples;
	//约束
	private List<EcoreConstraint> ecoreConstraints;
	//NextVersions
	private List<EcoreNextVersions> ecoreNextVersions;
	//codes
	private List<EcoreCodeVO> ecoreCodeVOs;
	//保存删除的对象集合
	private List<RemovedObjectVO> removedObjectVOs;
}
