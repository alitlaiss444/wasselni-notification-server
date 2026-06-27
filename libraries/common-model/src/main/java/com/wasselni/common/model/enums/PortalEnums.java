
package com.wasselni.common.model.enums;

public class PortalEnums {

	public enum EntityType {
		BANK, CORPORATE;
	}

	public enum CustomClaims {
		USERKEY, IP, PERMISSIONS, PROFILES, DEFAULT_LANG, MOBILE_NUMBER, CALLING_CODE;
	}

	public enum ProfileRole {
		SUPER_ADMIN(1, "Super Admin"), CHECKER(2, "Checker"), MAKER(3, "Maker");

		private Integer value;
		private String name;

		public Integer getValue() {
			return this.value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		private ProfileRole(Integer value, String name) {
			this.value = value;
			this.name = name;
		}

		public static ProfileRole getProfileRole(Integer value) {
			if (value != null) {
				for (ProfileRole tagEnumType : ProfileRole.values()) {
					if (tagEnumType.getValue().intValue() == value.intValue())
						return tagEnumType;
				}
			}
			return null;
		}
	}

	public enum UserStatus {
		ACTIVE(1, "Active"), BLOCKED(2, "Blocked"), SUSPENDED(3, "Suspended"), DELETED(4, "Deleted");

		// declaring private variable for getting values
		private Integer value;
		private String label;

		public Integer getValue() {
			return this.value;
		}

		public String getLabel() {
			return this.label;
		}

		private UserStatus(Integer value, String label) {
			this.value = value;
			this.label = label;
		}

		public static UserStatus getUserStatus(Integer value) {
			if (value != null) {
				for (UserStatus tagEnumType : UserStatus.values()) {
					if (tagEnumType.getValue().intValue() == value.intValue())
						return tagEnumType;
				}
			}
			return null;
		}
	}

	public enum UserLocked {
		UNLOCKED(0, "Unlocked"), LOCKED(1, "Locked");

		// declaring private variable for getting values
		private Integer value;
		private String label;

		public Integer getValue() {
			return this.value;
		}

		public String getLabel() {
			return this.label;
		}

		private UserLocked(Integer value, String label) {
			this.value = value;
			this.label = label;
		}

		public static UserLocked getUserLocked(Integer value) {
			if (value != null) {
				for (UserLocked tagEnumType : UserLocked.values()) {
					if (tagEnumType.getValue().intValue() == value.intValue())
						return tagEnumType;
				}
			}
			return null;
		}
	}

	public enum Language {
		ENGLISH("EN"), ARABIC("AR");

		private String value;

		public String getValue() {
			return this.value;
		}

		private Language(String value) {
			this.value = value;
		}

		public static Language getLanguage(String value) {
			if (value != null) {
				for (Language tagEnumType : Language.values()) {
					if (tagEnumType.getValue().equals(value))
						return tagEnumType;
				}
			}
			return null;
		}
	}

	public enum NotificationTemplate {
		OTP("OTP", "OTP"), NEW_USER("NEW_USER", "NEW_USER"),
		ONBOARD_ACCOUNT_HOLDER_CREATION_NOTIF("ONBOARD_ACCOUNT_HOLDER_CREATION_NOTIF", "User Corporate invitation"),;

		private String value;
		private String label;

		public String getValue() {
			return this.value;
		}

		public String getLabel() {
			return this.label;
		}

		private NotificationTemplate(String value, String label) {
			this.value = value;
			this.label = label;
		}

		public static NotificationTemplate getNotificationTemplate(String value) {
			if (value != null) {
				for (NotificationTemplate tagEnumType : NotificationTemplate.values()) {
					if (tagEnumType.getValue().equals(value))
						return tagEnumType;
				}
			}
			return null;
		}
	}

	public enum DocumentFormat {
		PDF, XLSX, XLS, CSV;
	}

	public enum GenericData {
		ACQUIRERS("ACQ"), MERCHANTS("MER"), TERMINALS("TERM");

		private String value;

		public String getValue() {
			return this.value;
		}

		private GenericData(String value) {
			this.value = value;
		}

	}

	public enum InvitationStatus {
		NEW(1, "New"), PENDING(2, "Pending"), EXPIRED(3, "Expired"), REJECTED(4, "Rejected"),
		CARD_CREATED(5, "Card created");

		// declaring private variable for getting values
		private Integer value;
		private String label;

		public Integer getValue() {
			return this.value;
		}

		public String getLabel() {
			return this.label;
		}

		private InvitationStatus(Integer value, String label) {
			this.value = value;
			this.label = label;
		}

		public static InvitationStatus getInvitationStatus(Integer value) {
			if (value != null) {
				for (InvitationStatus tagEnumType : InvitationStatus.values()) {
					if (tagEnumType.getValue().intValue() == value.intValue())
						return tagEnumType;
				}
			}
			return null;
		}
	}

	public enum Disputes {
		FRAUD("Fraud"), UNIDENTIFIED("Unidentified"), NOT_AUTHORIZED("Not Authorized"),
		INCORRECT_AMOUNT("Incorrect Amount"), INCORRECT_CURRENCY("Incorrect Currency"),
		DOUBLE_TRANSACTION("Double Transaction"),
		GOODS_NOT_RECEIVED_DAMAGED("Goods or Services Not Received or Damaged"), REFUNDS_RETURNS("Refunds and Returns"),
		REVERSED("Reversed");

		private String value;

		public String getValue() {
			return this.value;
		}

		private Disputes(String value) {
			this.value = value;
		}

		public static Disputes getDisputes(String value) {
			if (value != null) {
				for (Disputes tagEnumType : Disputes.values()) {
					if (tagEnumType.getValue().equals(value))
						return tagEnumType;
				}
			}
			return null;
		}
	}

	public enum ProductType {
		PLASTIC(0, "Plastic"), VIRTUAL(1, "Virtual"), BOTH(2, "Both");

		// declaring private variable for getting values
		private Integer value;
		private String label;

		public Integer getValue() {
			return this.value;
		}

		public String getLabel() {
			return this.label;
		}

		private ProductType(Integer value, String label) {
			this.value = value;
			this.label = label;
		}

		public static ProductType getProductType(Integer value) {
			if (value != null) {
				for (ProductType tagEnumType : ProductType.values()) {
					if (tagEnumType.getValue().intValue() == value.intValue())
						return tagEnumType;
				}
			}
			return null;
		}
	}

	public enum DocumentLinkType {
		APPLICATION("A");

		private String value;

		public String getValue() {
			return this.value;
		}

		private DocumentLinkType(String value) {
			this.value = value;
		}

		public static DocumentLinkType getDocumentLinkType(String value) {
			if (value != null) {
				for (DocumentLinkType tagEnumType : DocumentLinkType.values()) {
					if (tagEnumType.getValue().equals(value))
						return tagEnumType;
				}
			}
			return null;
		}
	}

	public enum LoginLogStatus {
		FAILED(0, "Failed"), SUCCESS(1, "Success");

		// declaring private variable for getting values
		private Integer value;
		private String label;

		public Integer getValue() {
			return this.value;
		}

		public String getLabel() {
			return this.label;
		}

		private LoginLogStatus(Integer value, String label) {
			this.value = value;
			this.label = label;
		}

		public static LoginLogStatus getLoginLogStatus(Integer value) {
			if (value != null) {
				for (LoginLogStatus tagEnumType : LoginLogStatus.values()) {
					if (tagEnumType.getValue().intValue() == value.intValue())
						return tagEnumType;
				}
			}
			return null;
		}
	}

	public enum PortalAuditStatus {
		FAILED(0, "Failed"), SUCCESS(1, "Success");

		// declaring private variable for getting values
		private Integer value;
		private String label;

		public Integer getValue() {
			return this.value;
		}

		public String getLabel() {
			return this.label;
		}

		private PortalAuditStatus(Integer value, String label) {
			this.value = value;
			this.label = label;
		}

		public static PortalAuditStatus getLoginLogStatus(Integer value) {
			if (value != null) {
				for (PortalAuditStatus tagEnumType : PortalAuditStatus.values()) {
					if (tagEnumType.getValue().intValue() == value.intValue())
						return tagEnumType;
				}
			}
			return null;
		}
	}

	public enum SegmentRuleAction {
		ACCEPT("A", "Accept"), REJECT("R", "Reject"), WORKING_ITEMS("W", "Working items");

		private String value;
		private String name;

		public String getValue() {
			return this.value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		private SegmentRuleAction(String value, String name) {
			this.value = value;
			this.name = name;
		}

		public static SegmentRuleAction getSegmentRuleAction(String value) {
			if (value != null) {
				for (SegmentRuleAction tagEnumType : SegmentRuleAction.values()) {
					if (tagEnumType.getValue().equals(value))
						return tagEnumType;
				}
			}
			return null;
		}
	}

	public enum PaymentHubRedirectionScreen {
		WORKING_ITEMS("name_matching_wi", "working-items");

		private String value;
		private String name;

		public String getValue() {
			return this.value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		private PaymentHubRedirectionScreen(String value, String name) {
			this.value = value;
			this.name = name;
		}

		public static PaymentHubRedirectionScreen getPaymentHubRedirectionScreen(String value) {
			if (value != null) {
				for (PaymentHubRedirectionScreen tagEnumType : PaymentHubRedirectionScreen.values()) {
					if (tagEnumType.getValue().equals(value))
						return tagEnumType;
				}
			}
			return null;
		}
	}

}
