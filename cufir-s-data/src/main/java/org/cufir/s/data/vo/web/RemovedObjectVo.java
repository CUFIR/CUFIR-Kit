package org.cufir.s.data.vo.web;

import java.util.Date;

import lombok.Data;

@Data
public class RemovedObjectVo {

	private String obj_id;
	private String obj_type;
	private Date create_time;
}
