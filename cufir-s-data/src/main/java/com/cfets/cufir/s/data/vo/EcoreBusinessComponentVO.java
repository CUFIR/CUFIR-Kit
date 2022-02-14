package com.cfets.cufir.s.data.vo;

import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreBusinessComponent;
import com.cfets.cufir.s.data.bean.EcoreBusinessComponentRL;
import com.cfets.cufir.s.data.bean.EcoreConstraint;
import com.cfets.cufir.s.data.bean.EcoreExample;
import com.cfets.cufir.s.data.bean.EcoreNextVersions;

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
	public EcoreBusinessComponent getEcoreBusinessComponent() {
		return ecoreBusinessComponent;
	}
	public void setEcoreBusinessComponent(EcoreBusinessComponent ecoreBusinessComponent) {
		this.ecoreBusinessComponent = ecoreBusinessComponent;
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
	public List<EcoreBusinessElementVO> getEcoreBusinessElementVOs() {
		return ecoreBusinessElementVOs;
	}
	public void setEcoreBusinessElementVOs(List<EcoreBusinessElementVO> ecoreBusinessElementVOs) {
		this.ecoreBusinessElementVOs = ecoreBusinessElementVOs;
	}
	public EcoreBusinessComponentRL getParentBusinessComponent() {
		return parentBusinessComponent;
	}
	public void setParentBusinessComponent(EcoreBusinessComponentRL parentBusinessComponent) {
		this.parentBusinessComponent = parentBusinessComponent;
	}
	public List<EcoreBusinessComponentRL> getChildBusinessComponents() {
		return childBusinessComponents;
	}
	public void setChildBusinessComponents(List<EcoreBusinessComponentRL> childBusinessComponents) {
		this.childBusinessComponents = childBusinessComponents;
	}
	public List<RemovedObjectVO> getRemovedObjectVOs() {
		return removedObjectVOs;
	}
	public void setRemovedObjectVOs(List<RemovedObjectVO> removedObjectVOs) {
		this.removedObjectVOs = removedObjectVOs;
	}
	
}
