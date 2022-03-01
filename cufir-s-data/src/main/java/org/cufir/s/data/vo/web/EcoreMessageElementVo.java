package org.cufir.s.data.vo.web;

import java.util.Date;

import lombok.Data;

@Data
public class EcoreMessageElementVo {

	private String id;
	private String definition;
	private String name;
	private String registration_status;
	private Date removal_date;
	private String object_identifier;
	private String previous_version;
	private String xml_tag;
	private String type;
	private String type_id;
	private Integer max_occurs;
	private Integer min_occurs;
	private String message_component_id;
	private String version;
	private String is_message_association_end;
	private String trace;
	private String trace_type;
	private String is_derived;
	private String is_from_iso20022;
	private Date create_time;
	private Date update_time;
	private String create_user;
	private String update_user;
	private String type_name;
	private String owner;
	private String xml_member_type;
}

