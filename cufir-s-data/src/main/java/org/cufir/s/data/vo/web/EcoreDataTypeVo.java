package org.cufir.s.data.vo.web;

import java.util.Date;

import lombok.Data;

@Data
public class EcoreDataTypeVo {

	private String id;
	private String definition;
	private String name;
	private String min_inclusive;
	private String min_exclusive;
	private String max_inclusive;
	private String max_exclusive;
	private String pattern;
	private String previous_version;
	private Integer fraction_digits;
	private Integer total_digits;
	private String unit_code;
	private Double base_value;
	private String base_unit_code;
	private Integer min_length;
	private Integer max_length;
	private Integer length;
	private String meaning_when_true;
	private String meaning_when_false;
	private String identification_scheme;
	private String value;
	private String literal;
	private String type;
	private String version;
	private String trace;
	private String process_contents;
	private String namespace;
	private String namespace_list;
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
