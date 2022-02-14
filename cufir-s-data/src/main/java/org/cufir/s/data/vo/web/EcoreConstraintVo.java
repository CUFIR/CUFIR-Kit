package org.cufir.s.data.vo.web;

import java.util.Date;

import lombok.Data;

@Data
public class EcoreConstraintVo {
	
	private String id;
	private String obj_id;
	private String obj_type;
	private String expression;
	private String expression_language;
	private String definition;
	private String name;
	private String registration_status;
	private Date removal_date;
	private String object_identifier;
	private String is_from_iso20022;
	private Date create_time;
	private Date update_time;
	private String create_user;
	private String update_user;
	private String owner;
}
