package com.wasselni.common.utils.format.xml;

import java.util.List;

import com.wasselni.common.utils.format.domain.FormatVar;

public class FormatFile {

	public static final String fileTypeDelimited = "D";
	public static final String fileTypeFixedLength = "F";
	public static final String fileTypeExcel = "X";

	private long StartIndex;
	private String Type;
	private String Delimiter;
	private FormatLine FormatLine;
	private FormatLine Header;
	private FormatLine Footer;
	private String EndOfLine;
	private String tableName;
	private String headerTableName;
	private String footerTableName;
	private Boolean migrateDb;
	private Boolean AppendSeperator;
	private Boolean CheckLengthInBytes;
	private boolean SkipLengthCheck;

	private List<FormatVar> vars;
	private List<FormatVar> headerVars;
	private List<FormatVar> footerVars;

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getDelimiter() {
		return Delimiter;
	}

	public void setDelimiter(String delimiter) {
		Delimiter = delimiter;
	}

	public FormatLine getImportLine() {
		return FormatLine;
	}

	public void setImportLine(FormatLine formatLine) {
		FormatLine = formatLine;
	}

	public FormatLine getHeader() {
		return Header;
	}

	public FormatLine getFooter() {
		return Footer;
	}

	public void setHeader(FormatLine header) {
		Header = header;
	}

	public void setFooter(FormatLine footer) {
		Footer = footer;
	}

	public boolean isFixedLength() {
		return getType().equals(fileTypeFixedLength);
	}

	public boolean isExcelOutput() {
		return getType().equals(fileTypeExcel);
	}

	public List<FormatVar> getVars() {
		return vars;
	}

	public void setVars(List<FormatVar> vars) {
		this.vars = vars;
	}

	public List<FormatVar> getHeaderVars() {
		return headerVars;
	}

	public void setHeaderVars(List<FormatVar> headerVars) {
		this.headerVars = headerVars;
	}

	public List<FormatVar> getFooterVars() {
		return footerVars;
	}

	public void setFooterVars(List<FormatVar> footerVars) {
		this.footerVars = footerVars;
	}

	public String getEndOfLine() {
		return EndOfLine;
	}

	public void setEndOfLine(String endOfLine) {
		EndOfLine = endOfLine;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Boolean isMigrateDb() {
		return migrateDb;
	}

	public void setMigrateDb(Boolean migrateDb) {
		this.migrateDb = migrateDb;
	}

	public Boolean getAppendSeperator() {
		return AppendSeperator;
	}

	public void setAppendSeperator(Boolean appendSeperator) {
		AppendSeperator = appendSeperator;
	}

	public Boolean getCheckLengthInBytes() {
		return CheckLengthInBytes;
	}

	public void setCheckLengthInBytes(Boolean checkLengthInBytes) {
		CheckLengthInBytes = checkLengthInBytes;
	}

	public FormatLine getFormatLine() {
		return FormatLine;
	}

	public void setFormatLine(FormatLine formatLine) {
		FormatLine = formatLine;
	}

	public String getHeaderTableName() {
		return headerTableName;
	}

	public void setHeaderTableName(String headerTableName) {
		this.headerTableName = headerTableName;
	}

	public String getFooterTableName() {
		return footerTableName;
	}

	public void setFooterTableName(String footerTableName) {
		this.footerTableName = footerTableName;
	}

	public Boolean getMigrateDb() {
		return migrateDb;
	}

	public long getStartIndex() {
		return StartIndex;
	}

	public void setStartIndex(long startIndex) {
		StartIndex = startIndex;
	}

	public boolean isSkipLengthCheck() {
		return SkipLengthCheck;
	}

	public void setSkipLengthCheck(boolean skipLengthCheck) {
		SkipLengthCheck = skipLengthCheck;
	}

	public static String getFiletypedelimited() {
		return fileTypeDelimited;
	}

	public static String getFiletypefixedlength() {
		return fileTypeFixedLength;
	}

	public static String getFiletypeexcel() {
		return fileTypeExcel;
	}

}
