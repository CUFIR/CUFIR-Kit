package org.cufir.s.data.vo;

import java.util.List;

import org.cufir.s.ecore.bean.EcoreCode;
import org.cufir.s.ecore.bean.EcoreConstraint;
import org.cufir.s.ecore.bean.EcoreExample;
import org.cufir.s.ecore.bean.EcoreNextVersions;

import lombok.Data;

@Data
public class EcoreCodeVO {

	private EcoreCode ecoreCode;
	//样例
	private List<EcoreExample> ecoreExamples;
	//约束
	private List<EcoreConstraint> ecoreConstraints;
	//NextVersions
	private List<EcoreNextVersions> ecoreNextVersions;
}
