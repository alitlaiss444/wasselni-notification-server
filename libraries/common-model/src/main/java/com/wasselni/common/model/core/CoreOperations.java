package com.wasselni.common.model.core;

public final class CoreOperations {

	// RootService

	public static final String HEALTH = "HEALTH";
	public static final String GET_PUBLIC_KEY = "GET_PUBLIC_KEY";

	// AuthenticationService

	public static final String LOGIN = "LOGIN";

	// UserTokenService

	public static final String VALIDATE_JWT = "VALIDATE_JWT";

	// AuditService

	public static final String PERFORM_AUDIT = "PERFORM_AUDIT";

	// CommonService

	public static final String FETCH_ENDPOINTS_REQUIRING_OTP = "FETCH_ENDPOINTS_REQUIRING_OTP";

	// OtpService

	public static final String GENERATE_OTP = "GENERATE_OTP";
	public static final String VALIDATE_OTP = "VALIDATE_OTP";
	public static final String OTP_VERIFIED = "OTP_VERIFIED";

}