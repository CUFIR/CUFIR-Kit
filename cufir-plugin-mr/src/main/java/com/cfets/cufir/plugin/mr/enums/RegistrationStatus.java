/**
 * 
 */
package com.cfets.cufir.plugin.mr.enums;

/**
 * 登记状态枚举
 * @author gongyi_tt
 *
 */
public enum RegistrationStatus {

	Provisionally("Provisionally Registered"),
	Registered("Registered"),
	Added("Added Registered"),
	Obsolete("Obsolete");
	
	private String status;
	
	private RegistrationStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
