package org.csdgn.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.csdgn.io.ReaderLoader;

public class IOUtils {
	private IOUtils() {}
	/**
	 * Gets the contents of the given file.
	 * @param filename The file's filename.
	 * @return the contents of the file, or <code>null</code> on failure
	 */
	public static String getFileContents(String filename) {
		try {
			return ReaderLoader.getAndClose(new FileReader(filename));
		} catch (IOException e) { }
		return null;
	}
	
	/**
	 * Gets the contents of the given file.
	 * @param file The file.
	 * @return the contents of the file, or <code>null</code> on failure
	 */
	public static String getFileContents(File file) {
		try {
			return ReaderLoader.getAndClose(new FileReader(file));
		} catch (IOException e) { }
		return null;
	}
	
	/**
	 * Sets the contents of the given file.
	 * @param filename The file's filename.
	 * @param contents The contents to store.
	 * @return <code>true</code> on success, <code>false</code> otherwise.
	 */
	public static boolean setFileContents(String filename, String contents) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(filename);
			fw.write(contents);
			return true;
		} catch (IOException e) {
		} finally {
			if(fw != null) try {
				fw.close();
			} catch(IOException e) {}
		}
		return false;
	}
	

	/**
	 * Sets the contents of the given file.
	 * @param file The file.
	 * @param contents The contents to store.
	 * @return <code>true</code> on success, <code>false</code> otherwise.
	 */
	public static boolean setFileContents(File file, String contents) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			fw.write(contents);
			return true;
		} catch (IOException e) {
		} finally {
			if(fw != null) try {
				fw.close();
			} catch(IOException e) {}
		}
		return false;
	}
}
