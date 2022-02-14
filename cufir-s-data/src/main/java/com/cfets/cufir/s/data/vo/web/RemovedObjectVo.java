package com.cfets.cufir.s.data.vo.web;

import java.util.Date;

public class RemovedObjectVo {

	private String obj_id;
	private String obj_type;
	private Date create_time;
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
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
}
