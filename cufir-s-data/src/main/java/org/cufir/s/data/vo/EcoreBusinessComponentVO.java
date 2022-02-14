package org.cufir.s.data.vo;

import java.util.List;

import org.cufir.s.ecore.bean.EcoreBusinessComponent;
import org.cufir.s.ecore.bean.EcoreBusinessComponentRL;
import org.cufir.s.ecore.bean.EcoreConstraint;
import org.cufir.s.ecore.bean.EcoreExample;
import org.cufir.s.ecore.bean.EcoreNextVersions;

import lombok.Data;

@Data
public class EcoreBusinessComponentVO {

	private EcoreBusinessComponent ecoreBusinessComponent;
	//父节点关联组件
	private EcoreBusinessComponentRL parentBusinessComponent;
	//子节点节点关联组件
	private List<EcoreBusinessComponentRL> childBusinessComponents;
	//样例
	private List<EcoreExample> ecoreExamples;
	//约束
	private List<EcoreConstraint> ecoreConstraints;
	//NextVersions
	private List<EcoreNextVersions> ecoreNextVersions;
	//EcoreBusinessElementVO
	private List<EcoreBusinessElementVO> ecoreBusinessElementVOs;
	//保存删除的对象集合
	private List<RemovedObjectVO> removedObjectVOs;
}
