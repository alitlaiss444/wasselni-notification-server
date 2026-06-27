package com.wasselni.common.model;

import java.math.BigDecimal;

public class PortalConstant {

	public static final String SWF = "SWF";
	public static final String SHA = "SHA";
	public static final String SME = "SME";
	public static final String INACTIVE = "Y";
	public static final String ACTIVE = "N";

	public static final String TEMPLATE_ACTIVE = "A";
	public static final String TEMPLATE_INACTIVE = "U";

	public static final String BAJ = "BAJ";
	public static final String LOCAL = "LOCAL";
	public static final String GLOBAL = "GLOBAL";
	public static final String IPS = "IPS";
	public static final String SARIE = "SARIE";
	public static final String SWIFT = "SWIFT";

	public static final String BAJ_TYPE_DESC = "BAJACCOUNT";
	public static final String LOCAL_TYPE_DESC = "SARIE";
	public static final String GLOBAL_TYPE_DESC = "SWIFT";

	public static final String TYPE_GLOBAL = "SWF";
	public static final String TYPE_LOCAL = "EFT";

	public static final String ADD_ACTION = "ADD";
	public static final String UPDATE_ACTION = "UPDATE";
	public static final String DELETE_ACTION = "DELETE";

	public static final String DEFAULT_CURRENCY = "SAR";
	public static final String DEFAULT_CURRENCY_CODE = "682";
	public static final String DEFAULT_COUNTRY_CODE = "682";
	public static final String YES = "Y";
	public static final String NO = "N";

	public static final String MTID = "0200";
	public static final String MTID_ACCOUNT = "0200";

	public static final String AML_CODE = "RD";

	public static final String BAJ_TYPE = "01";
	public static final String LOCAL_TYPE = "04";
	public static final String GLOBAL_TYPE = "05";

	public static final String ALL = "ALL";
	public static final String ANY = "Any";

	public static final String TRANSFER_TYPE_MY_ACCOUNTS = "TransferMyAccounts";
	public static final String TRANSFER_TYPE_INTERNAL = "TransferToInternal";

	public static final Integer INQUIRY_LIMIT_CHANEL_ID = 112;

	public static final String TRUE_STRING = "true";

	public static final String CARD_ACCEPTOR_LOCATION_SARIE = "OUTGOING SARIE TRANSFER  JEDDAH       SA";
	public static final String CARD_ACCEPTOR_LOCATION_SWIFT = "OUTGOING SWIFT TRANSFER  JEDDAH       SA";
	public static final String CARD_ACCEPTOR_LOCATION_BAJ = "INTERNAL BAJ TRANSFER    JEDDAH       SA";
	public static final String CARD_ACCEPTOR_LOCATION_IPS = "OUTGOING IPS TRANSFER    JEDDAH       SA";

	public static final String PROCESSING_CODE_ACCOUNT = "400000";
	public static final String PROCESSING_CODE_SARIE = "400010";
	public static final String PROCESSING_CODE_BAJ = "400011";
	public static final String PROCESSING_CODE_SWIFT = "400012";
	public static final String PROCESSING_CODE_IPS = "400013";

	public static final String PAYMENT_TYPE_SARIE = "SARIE";
	public static final String PAYMENT_TYPE_BAJ = "FUNDTRAN";
	public static final String PAYMENT_TYPE_SWIFT = "SWIFT";

	public static final Integer MCC_CODE = 6012;
	public static final String CHANNEL_ID = "407";
	public static final Integer TRANSACTION_CLASSIFICATION = 6;
	public static final String ACQUIRER_ID = "VAMSPORTAL";
	public static final String ISSUER_BIN = "VACCROUTE";

	public static final String ACCEPT_FLAG_VALUE = "1";
	public static final String REJECT_FLAG_VALUE = "2";
	public static final Integer ACCEPT_REJECT_FLAG_POSITION = 1;

	public static final String ACTION_FLAG_VALUE = "2";
	public static final Integer ACTION_FLAG_POSITION = 2;

	public static final String OVERRIDE_FLAG_VALUE_EXTERNAL = "Y";
	public static final String OVERRIDE_FLAG_VALUE_INTERNAL = "N";
	public static final Integer OVERRIDE_FLAG_POSITION = 3;

	public static final String TRANSACTION_CODE_ACCOUNT = "00005";
	public static final String TRANSACTION_CODE_SARIE = "00009";
	public static final String TRANSACTION_CODE_BAJ = "00023";
	public static final String TRANSACTION_CODE_SWIFT = "00025";
	public static final String TRANSACTION_CODE_IPS = "00011";

	public static final String BANK_CHARGE = "REM";
	public static final String B2B_DEFAULT = "B2B_DEFAULT";

	public static final BigDecimal IPS_THRESHOLD = new BigDecimal("20000");
	public static final String TIME_FORMAT = "%02d%02d%02d";
	public static final String DATE_FOMRAT = "yyyy-MM-dd";

	public static final String RULE_A = "A";
	public static final String RULE_B = "B";
	public static final String RULE_C = "C";
	public static final String RULE_D = "D";
	public static final String RULE_ABC = "ABC";

	public static final String COMMISSION_TRANSACTION_CODE_SARIE = "00043";
	public static final String COMMISSION_TRANSACTION_CODE_BAJ = "00045";
	public static final String COMMISSION_TRANSACTION_CODE_SWIFT = "00047";
	public static final String COMMISSION_TRANSACTION_CODE_IPS = "00049";
	public static final String VAT_CODE = "00037";

	public static final String REMITTER_PRODUCT_TYPE = "42";

	public static final String BENEFICIARY_REFERENCE = "3";

	public static final String ADDRESS = "address";

	public static final String TRANSACTION_TYPE = "BE";

	public static final String MESSAGE_TYPE = "1";
	public static final String SCHEME_ID = "12";
	public static final String SERVICE_NAME = "Payment Initiation";
	public static final String LOCATION = "Jeddah, KSA";

	public static final String TRANSFER_CUSTOMER_SEGMENT = "NONE";
	public static final String CUSTOMER_SEGMENT = "None";

	public static final String BAJ_BANK_SHORT_CODE = "10";

	// Participant

	public static final String PARTICIPANT_STATUS = "ACTIVE";
	public static final String FROM_VALUE = "AAAAAA";
	public static final String TO_VALUE = "ZZZZZZ";
	public static final String RANGE_OPR = "1";
	public static final String RANGE_FLAG = "1";
	public static final String CODE_TYPE = "225";

	// Fee

	public static final String FEE_CHANNEL = "CORPPORTAL";

	// Exchange rate

	public static final String SAR_CURRENCY = "SAR";
	public static final BigDecimal SAR_CONVERSION_RATE = BigDecimal.valueOf(1);
	public static final String RATE_TYPE = "SMU";

	// Wafi constant

	public static final String WAFI_TRANSACTION_CLASSIFICATION = "WafiTransactionClassification";

	// ValidateBenAccountRTPRequest Constant

	public static final String ADD_BENEFICIARY = "ADD_BENEFICIARY";
	public static final String BENEFICIARY_ADDRESS_1 = "KSA, Riyadh";
	public static final String VALIDATE_BEN_ACCOUNT_RTP_VERSION = "1.0.1";
	public static final String ACCOUNT_STATUS = "ACTIVE";

}
