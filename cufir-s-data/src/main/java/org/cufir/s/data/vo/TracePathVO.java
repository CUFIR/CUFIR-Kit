package org.cufir.s.data.vo;

import java.util.List;

import lombok.Data;

@Data
public class TracePathVO {

	private String id;
	//1:BC,2:BE,3:SUBTYPE_BC
	private String name;
	private String tracePath;
	private String bcPath;
	private int level;
	private List<TracePathVO> childPath;
}
