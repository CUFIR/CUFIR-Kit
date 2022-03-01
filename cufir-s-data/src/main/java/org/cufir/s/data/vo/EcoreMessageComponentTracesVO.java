package org.cufir.s.data.vo;

import lombok.Data;

@Data
public class EcoreMessageComponentTracesVO {

	private String messageComponentName;
	private String messageElementName;
	private String typeTracTo;
	private String technical;
	private String traceToBusinessComponent;
	private String traceToElement;
	private String tracePath;
	private String type;
	private String cId;
}
