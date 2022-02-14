package org.cufir.s.data.vo.web;

import java.util.Date;

import lombok.Data;

@Data
public class EcoreMessageSetDefinitionRLVo {

	private String message_id;
	private String set_id;
	private String is_from_iso20022;
	private Date create_time;
	private Date update_time;
	private String create_user;
	private String update_user;
	private String owner;
}
