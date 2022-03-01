package org.cufir.s.data.vo.web;

import java.util.Date;

import lombok.Data;

@Data
public class EcoreMessageBuildingBlockVo {

	private String id;
	private String definition;
	private String name;
	private String registration_status;
	private Date removal_date;
	private String object_identifier;
	private String previous_version;
	private String xml_tag;
	private String data_type;
	private String data_type_id;
	private Integer max_occurs;
	private Integer min_occurs;
	private String message_id;
	private String version;
	private String is_from_iso20022;
	private Date create_time;
	private Date update_time;
	private String create_user;
	private String update_user;
	private String owner;
	private String xml_member_type;
}

