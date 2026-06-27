package com.wasselni.common.utils;

import java.io.PrintStream;

public class HexDump {
	public static final char[] hexchars = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c',
			'd', 'e', 'f' };

	/**
	 * Generates a hex dump for the specified byte buffer with a width of 80
	 * 
	 * @param buf buffer to generate the hex dump for
	 * @return the hex dump of the specified byte buffer with a width of 80
	 */
	public static String format(byte[] buf) {
		return format(buf, 80);
	}

	/**
	 * Generates a hex dump for the specified byte buffer with the specified width
	 * 
	 * @param buf   buffer to generate the hex dump for
	 * @param width width of the output hex dump
	 * @return the hex dump of the specified byte buffer with the specified width
	 */
	public static String format(byte[] buf, int width) {
		int bs = (width - 8) / 4;
		int i = 0;
		StringBuffer sb = new StringBuffer();
		do {
			for (int j = 0; j < 6; j++) {
				sb.append(hexchars[(i << (j * 4) & 0xF00000) >> 20]);
			}
			sb.append('\t');
			sb.append(toHex(buf, i, bs));
			sb.append(' ');
			sb.append(toAscii(buf, i, bs));
			sb.append('\n');
			i += bs;
		} while (i < buf.length);
		return sb.toString();
	}

	/**
	 * Prints the hex dump of the byte buffer specified to the standard error stream
	 * 
	 * @param buf buffer to print the hex dump for
	 */
	public static void print(byte[] buf) {
		print(buf, System.err);
	}

	/**
	 * Prints the hex dump of the byte buffer specified to the standard error stream
	 * with the width specified
	 * 
	 * @param buf   buffer to print the hex dump for
	 * @param width the width of the output hex dump
	 */
	public static void print(byte[] buf, int width) {
		print(buf, width, System.err);
	}

	/**
	 * Prints the hex dump of the byte buffer specified to the specified output
	 * stream with a specified width
	 * 
	 * @param buf   buffer to print the hex dump for
	 * @param width the width of the output hex dump
	 * @param out   print stream to use for printing
	 */
	public static void print(byte[] buf, int width, PrintStream out) {
		out.print(format(buf, width));
	}

	/**
	 * Prints the hex dump of the byte buffer specified to the specified output
	 * stream with a width of 80
	 * 
	 * @param buf buffer to print the hex dump for
	 * @param out print stream to use for printing
	 */
	public static void print(byte[] buf, PrintStream out) {
		out.print(format(buf));
	}

	/**
	 * Converts a byte buffer to an ascii string
	 * 
	 * @param buf byte buffer to convert to ascii
	 * @return the converted string
	 */
	public static String toAscii(byte[] buf) {
		return toAscii(buf, 0, buf.length);
	}

	/**
	 * Converts the specified byte buffer to an ascii string starting the offset
	 * used for the length used
	 * 
	 * @param buf byte buffer to convert to ascii
	 * @param ofs offset to start the conversion from
	 * @param len length of the string to return/convert from the byte buffer
	 * @return ascii string from the byte buffer
	 */
	public static String toAscii(byte[] buf, int ofs, int len) {

		StringBuffer sb = new StringBuffer();
		int j = ofs + len;
		for (int i = ofs; i < j; i++) {
			if (i < buf.length) {
				if ((20 <= buf[i]) && (126 >= buf[i])) {
					sb.append((char) buf[i]);
				} else {
					sb.append('.');
				}
			} else {
				sb.append(' ');
			}
		}
		return sb.toString();
	}

	/**
	 * Returns a string which can be written to a Java source file as part of a
	 * static initializer for a byte array. Returns data in the format 0xAB, 0xCD,
	 * .... use like: javafile.print("byte[] data = {")
	 * javafile.print(Hexdump.toByteArray(data)); javafile.println("};");
	 * 
	 * @param buf buffer to convert
	 * @return output string
	 */
	public static String toByteArray(byte[] buf) {
		return toByteArray(buf, 0, buf.length);
	}

	/**
	 * Returns a string which can be written to a Java source file as part of a
	 * static initializer for a byte array. Returns data in the format 0xAB, 0xCD,
	 * .... use like: javafile.print("byte[] data = {")
	 * javafile.print(Hexdump.toByteArray(data)); javafile.println("};");
	 * 
	 * @param buf buffer to convert
	 * @param ofs offset to start with in the byte buffer
	 * @param len length of bytes to read and convert
	 * @return output string
	 */
	public static String toByteArray(byte[] buf, int ofs, int len) {
		StringBuffer sb = new StringBuffer();
		for (int i = ofs; (i < len) && (i < buf.length); i++) {
			sb.append('0');
			sb.append('x');
			sb.append(hexchars[(buf[i] & 0xF0) >> 4]);
			sb.append(hexchars[buf[i] & 0x0F]);
			if (((i + 1) < len) && ((i + 1) < buf.length)) {
				sb.append(',');
			}
		}
		return sb.toString();
	}

	/**
	 * Converts a byte buffer to hex
	 * 
	 * @param buf buffer to convert
	 * @return hex string
	 */
	public static String toHex(byte[] buf) {
		return toHex(buf, 0, buf.length);
	}

	/**
	 * Converts the specified byte buffer starting the offset specified for the
	 * length specified to hex
	 * 
	 * @param buf buffer to convert
	 * @param ofs offset from the byte buffer to start with
	 * @param len length of bytes to convert
	 * @return output converted hex string
	 */
	public static String toHex(byte[] buf, int ofs, int len) {
		StringBuffer sb = new StringBuffer();
		int j = ofs + len;
		for (int i = ofs; i < j; i++) {
			if (i < buf.length) {
				sb.append(hexchars[(buf[i] & 0xF0) >> 4]);
				sb.append(hexchars[buf[i] & 0x0F]);
				sb.append(' ');
			} else {
				sb.append(' ');
				sb.append(' ');
				sb.append(' ');
			}
		}
		return sb.toString();
	}
}
