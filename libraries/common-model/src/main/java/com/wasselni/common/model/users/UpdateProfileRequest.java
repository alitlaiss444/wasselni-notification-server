package com.wasselni.common.model.users;

import org.apache.commons.lang3.StringUtils;

import com.wasselni.common.model.audit.IncludeAudit;
import com.wasselni.common.model.common.DefaultValidationMessage;
import com.wasselni.common.utils.PortalUtils;

import jakarta.validation.constraints.AssertTrue;

public class UpdateProfileRequest {

	private String profile;

	@IncludeAudit(name = "Profile name")
	private String profileName;

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	@AssertTrue(message = DefaultValidationMessage.INVALID_PROFILE)
	private boolean isValidProfile() {

		if (StringUtils.isNotBlank(profile)) {

			PortalUtils portalUtils = new PortalUtils();

			if (!portalUtils.validateImage(profileName)) {
				return false;
			}

			if (!portalUtils.isValidBase64(profile)) {
				return false;
			}

			// Max 1000 Kb
			if (portalUtils.validateBase64Size(profile) > 1000000) {
				return false;
			}
		}

		return true;

	}

}
