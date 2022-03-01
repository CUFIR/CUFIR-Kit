package org.cufir.s.data.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class EcoreDocumentTreeNode implements Serializable{

	private static final long serialVersionUID = 1L;
	private String index;
	private int lvl;
	private String name;
	private String xmlTag;
	private String mult;
	private String type;
	private String constraint;
	private String dataTypeId;
	private String dataType;
	private List<EcoreDocumentTreeNode> childNodes=new ArrayList<EcoreDocumentTreeNode>();
}
