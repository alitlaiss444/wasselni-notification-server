package com.wasselni.common.model.users;

import java.util.Date;

public class GetUsersResponse {

	private Long id;
	private String username;
	private String firstName;
	private String lastName;
	private String mobileNumber;
	private String emailAddress;
	private Integer userStatus;
	private Integer userLocked;
	private Integer userRetries;
	private String userId;
	private Date lastSuccessLogin;
	private String lastIpAddress;
	private Integer callingCode;
	private String idNumber;
	private Date dob;
	private String profileImg;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Integer getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}

	public Integer getUserLocked() {
		return userLocked;
	}

	public void setUserLocked(Integer userLocked) {
		this.userLocked = userLocked;
	}

	public Integer getUserRetries() {
		return userRetries;
	}

	public void setUserRetries(Integer userRetries) {
		this.userRetries = userRetries;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getLastSuccessLogin() {
		return lastSuccessLogin;
	}

	public void setLastSuccessLogin(Date lastSuccessLogin) {
		this.lastSuccessLogin = lastSuccessLogin;
	}

	public String getLastIpAddress() {
		return lastIpAddress;
	}

	public void setLastIpAddress(String lastIpAddress) {
		this.lastIpAddress = lastIpAddress;
	}

	public Integer getCallingCode() {
		return callingCode;
	}

	public void setCallingCode(Integer callingCode) {
		this.callingCode = callingCode;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getProfileImg() {
		return profileImg;
	}

	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}

}
