package org.cufir.s.data.vo.web;

import java.util.Date;

import lombok.Data;

@Data
public class EcoreSemanticMarkupVo {

	private String id;
	private String type;
	private String obj_id;
	private String obj_type;
	private String is_from_iso20022;
	private Date create_time;
	private Date update_time;
	private String create_user;
	private String update_user;
	private String owner;
}
