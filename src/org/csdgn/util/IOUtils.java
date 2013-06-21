/**
 * Copyright (c) 2011-2013 Robert Maupin
 * 
 * This software is provided 'as-is', without any express or implied
 * warranty. In no event will the authors be held liable for any damages
 * arising from the use of this software.
 * 
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 * 
 *    1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgment in the product documentation would be
 *    appreciated but is not required.
 * 
 *    2. Altered source versions must be plainly marked as such, and must not be
 *    misrepresented as being the original software.
 * 
 *    3. This notice may not be removed or altered from any source
 *    distribution.
 */
package org.csdgn.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.csdgn.io.ReaderLoader;

/**
 * Basic utility class for quickly reading and writing text files.
 * @author Chase
 */
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
