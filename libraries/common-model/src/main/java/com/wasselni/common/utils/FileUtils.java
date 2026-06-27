package com.wasselni.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wasselni.common.model.errors.constants.SystemError;
import com.wasselni.common.model.errors.exception.SystemException;

/**
 * 
 * Utility functions to handle file
 *
 */
@SuppressWarnings("deprecation")
public class FileUtils {

	private static final Log log = LogFactory.getLog(FileUtils.class);

	/**
	 * Gets the file path of a file name
	 * 
	 * @param fileName file name to get the file path from
	 * @return file path in case the file name has a path, empty string otherwise
	 */
	public static String getFilePath(String fileName) {

		File file = new File(fileName);
		String absolutePath = file.getAbsolutePath();
		if (absolutePath.contains(File.separator)) {
			return absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
		} else {
			return "";
		}
	}

	/**
	 * Gets the list of files in the specified folder
	 * 
	 * @param folder folder to check for files in
	 * @return list of file names in the specified folder
	 */
	public static List<String> getListOfFiles(File folder) {
		String s[] = folder.list();
		Arrays.sort(s);
		return Arrays.asList(s);
	}

	/**
	 * Deletes the specified file
	 * 
	 * @param resource file to delete
	 * @return true if the file is deleted successfully, false otherwise
	 */
	public static boolean deleteFile(File resource) {

		try {

			if (resource.isFile()) {
				return resource.delete();
			} else {
			}
			return false;

		} catch (Exception ioex) {
			log.error("Failed deleting file [" + resource.getAbsolutePath() + "]", ioex);
			return false;
		}
	}

	/**
	 * Deletes the directory passed to it
	 * 
	 * @param directory directory to delete
	 * @return true if the directory was successfully deleted, false otherwise
	 */
	public static boolean deleteDirectory(String directory) {
		try {
			org.apache.commons.io.FileUtils.deleteDirectory(new File(directory));
			return true;
		} catch (IOException e) {
			log.error("Failed deleting directory [" + directory + "]", e);
			return false;
		}
	}

	/**
	 * Copies the source file to the destination
	 * 
	 * @param source file to copy
	 * @param dest   output file required
	 * @return true if the file is copied successfully and false otherwise
	 */
	public static boolean copyFile(File source, File dest) {

		try {
			org.apache.commons.io.FileUtils.copyFile(source, dest);
		} catch (IOException e) {
			log.error("Failed copying file", e);
			return false;
		}

		return true;
	}

	/**
	 * Moves a file from the source to the destination
	 * 
	 * @param source source file to move
	 * @param dest   destination file
	 * @return true if the file is moved successfully, false otherwise
	 */
	public static boolean moveFile(File source, File dest) {

		try {
			if (dest.exists())
				org.apache.commons.io.FileUtils.forceDelete(dest);
			org.apache.commons.io.FileUtils.moveFile(source, dest);
		} catch (IOException e) {
			log.error("Failed moving file [" + source.getAbsolutePath() + "] to [" + dest.getAbsolutePath() + "]", e);
			return false;
		}

		return true;
	}

	/**
	 * Appends a line to a file. No indication if returned if the line is appended
	 * or not
	 * 
	 * @param file file to append the line to
	 * @param line line to append to the file
	 */
	public static synchronized void append(File file, String line) {

		FileWriter fw;
		try {
			fw = new FileWriter(file, true);
			fw.write(line + "\n");
			fw.close();
		} catch (IOException e1) {
			log.error("Failed appending line to file", e1);
		}
	}

	/**
	 * Appends the list of lines specified to the file specified
	 * 
	 * @param file  file to append the lines to
	 * @param lines lines to append
	 * @return true if the lines were appended successfully, false otherwise
	 */
	public static synchronized boolean append(File file, List<String> lines) {

		if (Utils.emptyList(lines))
			return true;

		try {
			org.apache.commons.io.FileUtils.writeLines(file, lines, "\n", true);
			return true;
		} catch (IOException e) {
			log.error("Failed appending lines to file [" + file.getAbsolutePath() + "]", e);
			return false;
		}
	}

	/**
	 * Generates a file name based on a configured file name by substitution the {}
	 * by the simple date format specified
	 * 
	 * @param configFileName file name to check for substitutions in
	 * @return the formatted file name with the date formats replaced in it
	 */
	public static String generateFileName(String configFileName) {

		return generateFileName(configFileName, new Date());
	}

	/**
	 * Generates a file name based on a configured file name by substitution the {}
	 * by the simple date format specified
	 * 
	 * @param configFileName file name to check for substitutions in
	 * @param date           date to be used for the file name
	 * @return the formatted file name with the date formats replaced in it
	 */
	public static String generateFileName(String configFileName, Date date) {

		while (configFileName.contains("{") && configFileName.contains("}")) {

			if (date == null)
				date = new Date();

			int startIndex = configFileName.indexOf("{");
			int endIndex = configFileName.indexOf("}");
			String substitutionString = configFileName.substring(startIndex, endIndex + 1);
			String substitutionStringIn = configFileName.substring(startIndex + 1, endIndex);
			SimpleDateFormat sdf = new SimpleDateFormat(substitutionStringIn);
			try {
				configFileName = configFileName.replace(substitutionString, sdf.format(date));
			} catch (Exception e) {
				log.error("Invalid substitution string - should be valid for date format");
				break;
			}
		}

		return configFileName;
	}

	/**
	 * Validates a directory and that is has the required permissions
	 * 
	 * @param directory        directory to check
	 * @param createIfNotFound boolean indicating if the directory should be created
	 *                         in case it's not found
	 * @param needsWrite       boolean indicating if the write permission is
	 *                         required in the specified directory
	 * @param needsRead        boolean indicating if read permission is required in
	 *                         the specified directory
	 * @throws SystemException Directory does not exist and createIfNotFound is set
	 *                         to false. Directory does not exist and an attempt to
	 *                         create it failed. Directory does not have write
	 *                         permission when it's requested. Directory does not
	 *                         have read permission when it's requested
	 * 
	 */
	public static void verifyDirectory(String directory, boolean createIfNotFound, boolean needsWrite,
			boolean needsRead) throws SystemException {

		log.info("Verifying Directory [" + directory + "]");
		DebugUtils.tv("Create if not found", createIfNotFound);
		DebugUtils.tv("Needs Write", needsWrite);
		DebugUtils.tv("Needs Read", needsRead);

		File file = new File(directory);

		// Check if the directory exists and if it doesn't create it
		if (!file.exists()) {

			log.debug("Directory specified [" + directory + "] does not exist");

			if (!createIfNotFound)
				throw new SystemException(SystemError.DIR_DIRECTORY_DOES_NOT_EXIST, directory);

			// Try to create the directory, and if failed, throw an exception
			if (!file.mkdirs()) {

				log.error("Dirctory does not exist and failed creating it");
				throw new SystemException(SystemError.DIR_DIRECTORY_DOES_NOT_EXIST_FAILED_CREATING, directory);
			}
			// Directory successfully created
			else {
				log.debug("Successfully created directory [" + directory + "]");
			}
		}
		// Directory exists
		else {
			log.debug("Directory [" + directory + "] exists");
		}

		// Check if it's a directory or file, if file throw an exception
		if (!file.isDirectory()) {
			log.error("[" + directory + "] is not a directory!");
			throw new SystemException(SystemError.DIR_NOT_A_DIRECTORY, directory);
		}

		// Check if permission to write is available for the directory
		if (needsWrite && !file.canWrite()) {
			log.error("No permission to write in directory [" + directory + "]");
			throw new SystemException(SystemError.DIR_NO_WRITE_PERMISSION, directory);
		}

		// Check if permission to read is available for the directory
		if (needsRead && !file.canRead()) {
			log.error("No permission to read from dirctory [" + directory + "]");
			throw new SystemException(SystemError.DIR_NO_READ_PERMISSION, directory);
		}

	}

	/**
	 * Validates a file and that is has the required permissions
	 * 
	 * @param fileName         file name to verify
	 * @param createIfNotFound boolean specifying if the file should be created if
	 *                         it does not exist
	 * @param needsWrite       boolean specifying if read permission is required on
	 *                         the file
	 * @param needsRead        boolean specifying if read permission is required on
	 *                         the file
	 * @param checkEmpty       boolean specifying if the file should be checked if
	 *                         empty
	 * @throws SystemException if the file does not exist and createIfNotFound is
	 *                         set to false. File does not exist and an attempt to
	 *                         create it failed. File does not have a write
	 *                         permission when it's requested. File does not have
	 *                         read permission when it's requested. File is empty
	 *                         and check empty is specified
	 */
	public static void verifyFile(String fileName, boolean createIfNotFound, boolean needsWrite, boolean needsRead,
			boolean checkEmpty) throws SystemException {

		log.info("Verifying file [" + fileName + "]");
		DebugUtils.tv("Create if not found", createIfNotFound);
		DebugUtils.tv("Needs Write", needsWrite);
		DebugUtils.tv("Needs Read", needsRead);
		DebugUtils.tv("Check Empty", checkEmpty);

		File file = new File(fileName);

		// Check if the file exists and if it doesn't create it
		if (!file.exists()) {

			log.debug("File specified [" + fileName + "] does not exist");

			if (!createIfNotFound)
				throw new SystemException(SystemError.DIR_FILE_DOES_NOT_EXIST, fileName);

			// Try to create the File, and if failed, throw an exception
			try {

				if (file.getParentFile() != null && !file.getParentFile().exists()) {
					log.debug("Parent directory does not exist, " + "trying to create it");
					if (!file.getParentFile().mkdirs()) {
						throw new SystemException(SystemError.DIR_FAILED_CREATING_DIR,
								file.getParentFile().getAbsolutePath());
					}
				}

				if (!file.createNewFile()) {

					log.error("File does not exist and failed creating it");
					throw new SystemException(SystemError.DIR_FILE_DOES_NOT_EXIST_FAILED_CREATING, fileName);
				}
				// File successfully created
				else {
					log.debug("Successfully created file [" + fileName + "]");
				}
			} catch (IOException e) {
				log.error("File does not exist and failed creating it");
				throw new SystemException(SystemError.DIR_FILE_DOES_NOT_EXIST_FAILED_CREATING, fileName);
			}
		}
		// File exists
		else {
			log.debug("File [" + fileName + "] exists");
		}

		// Check if it's a directory or file, if not a file throw an exception
		if (!file.isFile()) {
			log.error("[" + fileName + "] is not a file!");
			throw new SystemException(SystemError.DIR_NOT_A_FILE, fileName);
		}

		// Check if permission to write is available for the file
		if (needsWrite && !file.canWrite()) {
			log.error("No permission to write in file [" + fileName + "]");
			throw new SystemException(SystemError.DIR_NO_WRITE_PERMISSION_FILE, fileName);
		}

		// Check if permission to read is available for the directory
		if (needsRead && !file.canRead()) {
			log.error("No permission to read from file [" + fileName + "]");
			throw new SystemException(SystemError.DIR_NO_READ_PERMISSION_FILE, fileName);
		}

		if (checkEmpty) {

			checkEmptyFile(fileName);

		}

	}

	/**
	 * Checks if a file is empty
	 * 
	 * @param fileName file name to check
	 * @throws SystemException File not found. File is empty
	 */
	public static void checkEmptyFile(String fileName) throws SystemException {

		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			log.error("Failed checking if file is empty", e);
			throw new SystemException(SystemError.SE_SYSTEM_ERROR,
					"Failed checking if file is empty: " + e.getMessage());
		}
		try {
			if (br.readLine() == null) {
				try {
					br.close();
				} catch (IOException e1) {
					log.warn("Failed closing file", e1);
				}
				throw new SystemException(SystemError.DIR_EMPTY_FILE, fileName);
			}
		} catch (IOException e) {
			log.error("Failed checking if file is empty", e);
			try {
				br.close();
			} catch (IOException e1) {
				log.warn("Failed closing file", e);
			}
			throw new SystemException(SystemError.SE_SYSTEM_ERROR,
					"Failed checking if file is empty: " + e.getMessage());
		}

		try {
			br.close();
		} catch (IOException e) {
			log.warn("Failed closing file", e);
		}
	}

	/**
	 * Combines 2 paths together
	 * 
	 * @param path1 first path to combine
	 * @param path2 second path to combine
	 * @return combined paths
	 */
	public static String combine(String path1, String path2) {
		File file1 = new File(path1);
		File file2 = new File(file1, path2);
		return file2.getPath();
	}

	/**
	 * Combines multiple paths together
	 * 
	 * @param paths paths to combines
	 * @return combined paths
	 */
	public static String combineMultiple(String... paths) {
		if (paths.length == 0)
			return null;

		String output = "";
		for (String path : paths) {

			output = FileUtils.combine(output, path);
		}
		return output;
	}

	/**
	 * Reads the file contents and returns them as string
	 * 
	 * @param description description of the file being read
	 * @param fileName    file name to read and get data as string
	 * @return {@link String} with the full file read
	 * @throws SystemException
	 */
	public static String readFile(String description, String fileName) throws SystemException {

		File file = new File(fileName);

		verifyFile(fileName, false, false, true, false);

		try {
			return org.apache.commons.io.FileUtils.readFileToString(file);
		} catch (IOException e2) {
			log.error("Failed reading file!", e2);
			throw new SystemException(SystemError.SE_SYSTEM_ERROR,
					description + ": Failed reading file [" + file.getAbsolutePath() + "]");
		}

	}

	/**
	 * Reads the file contents and returns them as string
	 * 
	 * @param description description of the file being read
	 * @param fileName    file name to read and get data as string
	 * @return {@link String} with the full file read
	 * @throws SystemException
	 */
	public static String readFileAndEncodeBase64(String description, String fileName) throws SystemException {

		File file = new File(fileName);

		verifyFile(fileName, false, false, true, false);

		try {
			byte[] binaryFileContent = org.apache.commons.io.FileUtils.readFileToByteArray(file);

			return Base64.encodeBase64String(binaryFileContent);

		} catch (IOException e2) {
			log.error("Failed reading file!", e2);
			throw new SystemException(SystemError.SE_SYSTEM_ERROR,
					description + ": Failed reading file [" + file.getAbsolutePath() + "]");
		}

	}

	/**
	 * Reads the file contents and returns them as List of String, one entry for
	 * each line
	 * 
	 * @param description description of the file being read
	 * @param fileName    file name to read and get data as string
	 * @return {@link List} of {@link String} each entry corresponding to a line
	 * @throws SystemException
	 */
	public static List<String> readFileLines(String description, String fileName) throws SystemException {

		verifyFile(fileName, false, false, true, false);

		try {
			return org.apache.commons.io.FileUtils.readLines(new File(fileName));
		} catch (FileNotFoundException e) {
			// Should never happen verified before opening the file
			return null;
		} catch (IOException e) {
			log.error("Failed reading file!", e);
			throw new SystemException(SystemError.SE_SYSTEM_ERROR,
					description + ": Failed reading file lines [" + fileName + "]");
		}

	}

	/**
	 * Writes the string contents provided to the file provided
	 * 
	 * @param file    file to write the contents to
	 * @param content content to write to the file
	 * @return true if the file is written successfully, false otherwise
	 */
	public static boolean writeContentToFile(String file, String content) {

		File f = new File(file);

		if (!f.getParentFile().exists()) {
			if (!f.getParentFile().mkdirs()) {
				log.error("Failed creating parent directory for file [" + f.getParentFile().getAbsolutePath() + "]");
				return false;
			}
		}

		try {
			org.apache.commons.io.FileUtils.write(f, content, false);
		} catch (IOException e) {
			log.error("Failed to write contents to file [" + file + "]", e);
			return false;
		}

		return true;

	}

	/**
	 * Writes the string contents provided to the file provided
	 * 
	 * @param file    file to write the contents to
	 * @param content content to write to the file
	 * @return true if the file is written successfully, false otherwise
	 */
	public static boolean writeBinaryContentToFile(String file, byte[] content) {

		File f = new File(file);

		if (!f.getParentFile().exists()) {
			if (!f.getParentFile().mkdirs()) {
				log.error("Failed creating parent directory for file [" + f.getParentFile().getAbsolutePath() + "]");
				return false;
			}
		}

		try {
			org.apache.commons.io.FileUtils.writeByteArrayToFile(f, content, false);
		} catch (IOException e) {
			log.error("Failed to write contents to file [" + file + "]", e);
			return false;
		}

		return true;

	}

	/**
	 * Appends the contents specified to the file specified. If the file does not
	 * exist, an attempt is done to create it
	 * 
	 * @param file    file to write the content on
	 * @param content content to write to the file
	 * @return true if the append was successful
	 */
	public static boolean appendContentToFile(String file, String content) {
		File f = new File(file);

		if (!f.getParentFile().exists()) {
			if (!f.getParentFile().mkdirs()) {
				log.error("Failed creating parent directory for file [" + f.getParentFile().getAbsolutePath() + "]");
				return false;
			}
		}

		try {
			org.apache.commons.io.FileUtils.write(f, content, true);
		} catch (IOException e) {
			log.error("Failed to write contents to file [" + file + "]", e);
			return false;
		}

		return true;
	}

	/**
	 * Appends files into one output file
	 * 
	 * @param outputFileName output file name to append the others into
	 * @param separator      separator that should be at the end of file (i.e. new
	 *                       line). Each file is checked to make sure it ends with
	 *                       this value before the second is appended to it. If the
	 *                       file doesn't have it, it is added by the process
	 * @param files          files to append
	 * @return true if the files were appended successfully, false otherwise
	 * @throws SystemException
	 */
	public static boolean appendFiles(String outputFileName, String separator, String... files) throws SystemException {

		if (files == null || files.length == 0) {
			log.warn("No files provided to append");
			return false;
		}

		// Validate all files exist
		for (String file : files) {
			verifyFile(file, false, false, true, false);
		}

		if (new File(outputFileName).exists())
			deleteFile(new File(outputFileName));

		// Validate the output file exists has read/write permission
		verifyFile(outputFileName, true, true, true, false);

		// Only one file provided. This is similar to copying a file
		if (files.length == 1) {
			return copyFile(new File(outputFileName), new File(files[0]));
		}

		boolean success = true;

		RandomAccessFile outputFile = null;
		FileInputStream inputFileStream = null;

		// Creat a separator Byte buffer to be appended if needed to end of
		// files if the separator is specified
		ByteBuffer separatorByteBuffer = null;
		if (separator != null && separator.length() > 0) {
			separatorByteBuffer = ByteBuffer.allocate(separator.length());
			separatorByteBuffer.put(separator.getBytes());
			separatorByteBuffer.flip();
		}

		try {
			outputFile = new RandomAccessFile(new File(outputFileName), "rws");

			FileChannel outputChannel = outputFile.getChannel();

			for (int i = 0; i < files.length; i++) {

				String file = files[i];

				inputFileStream = new FileInputStream(new File(file));
				FileChannel inputChannel = inputFileStream.getChannel();

				// If the separator is specified, check if it needs to be
				// appended (for the first file it will not be required)
				if (separator != null && separator.length() > 0 && i > 0) {

					String lastNCharacters = getLastNCharactersOfFile(outputChannel, separator.getBytes().length);
					if (lastNCharacters != null && lastNCharacters.length() > 0 && !lastNCharacters.equals(separator)) {

						log.debug("Appended [" + outputChannel.write(separatorByteBuffer, outputChannel.size())
								+ "] bytes for separator to output file");
						separatorByteBuffer.flip();
					}
				}

				// Transfer the input file to the output one
				outputChannel.transferFrom(inputChannel, outputChannel.size(), inputChannel.size());

				inputFileStream.close();

			}

		} catch (Exception e) {
			log.error("Failed merging files", e);
			success = false;
		} finally {
			if (outputFile != null) {
				try {
					outputFile.close();
				} catch (IOException e) {
					log.warn("Failed closing file output stream", e);
				}
			}

			if (inputFileStream != null) {
				try {
					inputFileStream.close();
				} catch (IOException e) {
					log.warn("Failed closing file input stream", e);
				}
			}

		}

		return success;
	}

	/**
	 * Gets the last n characters of a file channel
	 * 
	 * @param fileChannel        file channel to get the last n characters from
	 * @param numberOfCharacters number of characters to read from
	 * @return read string. In case the file length has less than the specified
	 *         number of characters, everything in the file is returned
	 * @throws SystemException
	 */
	private static String getLastNCharactersOfFile(FileChannel fileChannel, int numberOfCharacters)
			throws SystemException {

		try {
			ByteBuffer buffer = ByteBuffer.allocate(numberOfCharacters);

			if (fileChannel.size() > numberOfCharacters) {
				fileChannel.read(buffer, fileChannel.size() - numberOfCharacters);
				String out = new String(buffer.array());
				log.debug("Last [" + numberOfCharacters + "] characters from file [" + out + "]");

				return out;
			} else {
				fileChannel.read(buffer);
				String out = new String(buffer.array());
				log.warn("File has less than [" + numberOfCharacters + "]. Read [" + fileChannel.size() + "][" + out
						+ "]");

				return out;
			}
		} catch (Exception e) {
			log.error("Failed getting last [" + numberOfCharacters + "] from file", e);
			throw new SystemException(SystemError.SE_SYSTEM_ERROR,
					"Failed reading [" + numberOfCharacters + "] from file");
		}
	}

	/**
	 * Removes the extension from the specified file name. If the file name is null
	 * or empty or does not have an extension (.), the same file name is returned
	 * 
	 * @param fileName file name to remove the extension from
	 * @return file name without the extension or original string if the input is
	 *         invalid or does not include a .
	 */
	public static String removeExtension(String fileName) {

		if (StringUtils.isBlank(fileName))
			return fileName;

		int dotIndex = fileName.lastIndexOf(".");

		if (dotIndex > 0) {
			return fileName.substring(0, dotIndex);
		}

		return fileName;
	}
}
