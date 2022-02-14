package com.cfets.cufir.s.data.vo;

import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreConstraint;
import com.cfets.cufir.s.data.bean.EcoreExample;
import com.cfets.cufir.s.data.bean.EcoreMessageComponent;
import com.cfets.cufir.s.data.bean.EcoreNextVersions;

public class EcoreMessageComponentVO {

	private EcoreMessageComponent ecoreMessageComponent;
	//样例
	private List<EcoreExample> ecoreExamples;
	//约束
	private List<EcoreConstraint> ecoreConstraints;
	//NextVersions
	private List<EcoreNextVersions> ecoreNextVersions;
	//codes
	private List<EcoreMessageElementVO> ecoreMessageElementVOs;
	//保存删除的对象集合
	private List<RemovedObjectVO> removedObjectVOs;
	public EcoreMessageComponent getEcoreMessageComponent() {
		return ecoreMessageComponent;
	}
	public void setEcoreMessageComponent(EcoreMessageComponent ecoreMessageComponent) {
		this.ecoreMessageComponent = ecoreMessageComponent;
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
	public List<EcoreMessageElementVO> getEcoreMessageElementVOs() {
		return ecoreMessageElementVOs;
	}
	public void setEcoreMessageElementVOs(List<EcoreMessageElementVO> ecoreMessageElementVOs) {
		this.ecoreMessageElementVOs = ecoreMessageElementVOs;
	}
	public List<RemovedObjectVO> getRemovedObjectVOs() {
		return removedObjectVOs;
	}
	public void setRemovedObjectVOs(List<RemovedObjectVO> removedObjectVOs) {
		this.removedObjectVOs = removedObjectVOs;
	}
}

