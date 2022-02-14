package org.cufir.s.data.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class EcoreTreeNode implements Serializable{

	private static final long serialVersionUID = 6106874791901271158L;
	private String id;
	private String name;
	private String type;
	private String level;
	private String objType;
	private String status;
	private String registrationStatus;
	private String imgPath;
	private String pid;
	private String mdr3;
	
	private List<EcoreTreeNode> childNodes=new ArrayList<EcoreTreeNode>();
}
