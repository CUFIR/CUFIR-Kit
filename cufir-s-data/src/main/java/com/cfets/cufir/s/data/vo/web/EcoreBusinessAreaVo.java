package com.cfets.cufir.s.data.vo.web;

import java.util.Date;

public class EcoreBusinessAreaVo {

	private String id;
	private String definition;
	private String name;
	private String registration_status;
	private Date removal_date;
	private String object_identifier;
	private String code;
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
	public String getDefinition() {
		return definition;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRegistration_status() {
		return registration_status;
	}
	public void setRegistration_status(String registration_status) {
		this.registration_status = registration_status;
	}
	public Date getRemoval_date() {
		return removal_date;
	}
	public void setRemoval_date(Date removal_date) {
		this.removal_date = removal_date;
	}
	public String getObject_identifier() {
		return object_identifier;
	}
	public void setObject_identifier(String object_identifier) {
		this.object_identifier = object_identifier;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
