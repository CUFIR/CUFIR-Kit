package com.cfets.cufir.s.data.vo.web;

import java.util.Date;

public class EcoreNextVersionsVo {

	private String id;
	private String next_version_id;
	private String obj_type;
	private String is_from_iso20022;
	private Date create_time;
	private Date update_time;
	private String create_user;
	private String update_user;
	private String owner;
	
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNext_version_id() {
		return next_version_id;
	}
	public void setNext_version_id(String next_version_id) {
		this.next_version_id = next_version_id;
	}
	public String getObj_type() {
		return obj_type;
	}
	public void setObj_type(String obj_type) {
		this.obj_type = obj_type;
	}
	public String getIs_from_iso20022() {
		return is_from_iso20022;
	}
	public void setIs_from_iso20022(String is_from_iso20022) {
		this.is_from_iso20022 = is_from_iso20022;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	public String getCreate_user() {
		return create_user;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
	public String getUpdate_user() {
		return update_user;
	}
	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}
	
}
