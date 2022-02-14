package com.cfets.cufir.s.data.vo;

import java.util.List;

public class TracePathVO {

	private String id;
	//1:BC,2:BE,3:SUBTYPE_BC
	private String name;
	private String tracePath;
	private String bcPath;
	private int level;
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getBcPath() {
		return bcPath;
	}
	public void setBcPath(String bcPath) {
		this.bcPath = bcPath;
	}
	public String getTracePath() {
		return tracePath;
	}
	public void setTracePath(String tracePath) {
		this.tracePath = tracePath;
	}
	private List<TracePathVO> childPath;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<TracePathVO> getChildPath() {
		return childPath;
	}
	public void setChildPath(List<TracePathVO> childPath) {
		this.childPath = childPath;
	}
}
