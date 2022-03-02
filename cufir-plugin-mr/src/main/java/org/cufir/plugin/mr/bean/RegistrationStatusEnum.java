package org.cufir.plugin.mr.bean;

/**
 * 登记状态枚举
 */
public enum RegistrationStatusEnum {

	Provisionally("Provisionally Registered"),
	Registered("Registered"),
	Added("Added Registered"),
	Obsolete("Obsolete");
	
	private String status;
	
	private RegistrationStatusEnum(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
