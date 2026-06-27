package com.wasselni.common.model;

public class ProfileModel {

	private Long roleId;
	private String roleName;
	private String roleOptions;

	public ProfileModel() {
		super();
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleOptions() {
		return roleOptions;
	}

	public void setRoleOptions(String roleOptions) {
		this.roleOptions = roleOptions;
	}

}
