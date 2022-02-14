package org.cufir.s.data.vo.web;

import java.util.Date;

import lombok.Data;

@Data
public class EcoreSemanticMarkupElementVo {

	private String id;
	private String name;
	private String value;
	private String semantic_markup_id;
	private String is_from_iso20022;
	private Date create_time;
	private Date update_time;
	private String create_user;
	private String update_user;
	private String owner;
}
