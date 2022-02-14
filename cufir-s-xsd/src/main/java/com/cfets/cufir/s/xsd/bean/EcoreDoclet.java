package com.cfets.cufir.s.xsd.bean;

import java.util.List;

public class EcoreDoclet {

	private String id;
	private String obj_id;
	private String obj_type;
	private String type;
	private String content;
	private String isfromiso20022;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getObj_id() {
		return obj_id;
	}
	public void setObj_id(String obj_id) {
		this.obj_id = obj_id;
	}
	public String getObj_type() {
		return obj_type;
	}
	public void setObj_type(String obj_type) {
		this.obj_type = obj_type;
	}
	public String getIsfromiso20022() {
		return isfromiso20022;
	}
	public void setIsfromiso20022(String isfromiso20022) {
		this.isfromiso20022 = isfromiso20022;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
