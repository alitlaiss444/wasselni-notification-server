/**
 * 
 */
package com.wasselni.common.utils.audit;

public class AuditStringBuilder {

	private static final String CRLF = "\r\n";
	private StringBuilder builder = null;
	private boolean header;
	private boolean useTab;

	/**
	 * 
	 * @param header
	 * @param useTab
	 */
	public AuditStringBuilder(boolean header, boolean useTab) {

		this.header = header;
		this.useTab = useTab;

		if (header) {
			builder = new StringBuilder(CRLF
					+ "----------------------------------------------<Audit>----------------------------------------------"
					+ CRLF);
		} else {
			builder = new StringBuilder();
		}
	}

	/**
	 * 
	 * @param line
	 */
	public void append(String line) {
		builder.append(((useTab) ? "\t\t\t\t" : "\t\t\t") + line + CRLF);
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return builder.toString() + ((header)
				? "----------------------------------------------</Audit>----------------------------------------------"
				: "") + CRLF;
	}

}
