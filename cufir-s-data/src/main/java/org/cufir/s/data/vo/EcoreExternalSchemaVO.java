package org.cufir.s.data.vo;

import java.util.List;

import org.cufir.s.ecore.bean.EcoreConstraint;
import org.cufir.s.ecore.bean.EcoreExample;
import org.cufir.s.ecore.bean.EcoreExternalSchema;
import org.cufir.s.ecore.bean.EcoreNamespaceList;
import org.cufir.s.ecore.bean.EcoreNextVersions;

import lombok.Data;

@Data
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
}

