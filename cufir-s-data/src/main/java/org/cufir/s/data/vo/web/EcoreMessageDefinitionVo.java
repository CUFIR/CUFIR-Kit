package org.cufir.s.data.vo.web;

import java.util.Date;

import lombok.Data;

@Data
public class EcoreMessageDefinitionVo {

	private String id;
	private String definition;
	private String name;
	private String registration_status;
	private Date removal_date;
	private String object_identifier;
	private String previous_version;
	private String business_area_id;
	private String root_element;
	private String flavour;
	private String message_functionality;
	private String xml_tag;
	private String xml_name;
	private String version;
	private String business_area;
	private String is_from_iso20022;
	private Date create_time;
	private Date update_time;
	private String create_user;
	private String update_user;
	private String message_definition_identifier;
	private String owner;
}
