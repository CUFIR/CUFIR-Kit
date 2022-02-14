package com.cfets.cufir.s.xsd.bean;

import com.cfets.cwap.s.util.db.Table;
import com.cfets.cwap.s.util.db.TableColumn;

/**
 * Bean对象
 *
 */
@Table(name = "user")
public class MockBean {

	@TableColumn(name = "id", note = "用户ID")
	private String id;

	@TableColumn(name = "name", note = "用户名")
	private String name;

	@TableColumn(name = "gender", note = "性别")
	private String gender;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

}
