
package com.wasselni.common.model.common;

public class PreferencesModel {

	private String userId;
	private String username;
	private String language;
	private Long allowedProfile;
	private String ipAddress;
	private Long roleId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Long getAllowedProfile() {
		return allowedProfile;
	}

	public void setAllowedProfile(Long allowedProfile) {
		this.allowedProfile = allowedProfile;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

}
