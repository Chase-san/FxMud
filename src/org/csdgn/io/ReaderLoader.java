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
package org.csdgn.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * @author Robert Maupin
 */
public abstract class ReaderLoader {
	private static final int IO_BUFFER_SIZE = 16384;
	private final Reader input;
	
	/** 
	 * @param input
	 */
	public ReaderLoader(Reader input) {
		this.input = input;
	}
	
	/**
	 * Called when an array of bytes is read.
	 * @param read
	 * @param length
	 */
	public void onCharacterArrayRead(char[] read, int length) {
		for(int i=0;i < length; ++i)
			onCharacterRead(read[i]);
	}
	
	/**
	 * Called when a byte is read.
	 * @param input
	 */
	public abstract void onCharacterRead(char input);
	
	/**
	 * @throws IOException 
	 * 
	 */
	public void readAll() throws IOException {
		if (input != null) {
			final BufferedReader input = new BufferedReader(this.input);
			final char[] read = new char[IO_BUFFER_SIZE];
			int r = 0;
			while ((r = input.read(read, 0, IO_BUFFER_SIZE)) != -1)
				onCharacterArrayRead(read, r);
		} 
	}
	
	/**
	 * @throws IOException 
	 * 
	 */
	public void readAllAndClose() throws IOException {
		if (input != null) try {
			final BufferedReader input = new BufferedReader(this.input);
			final char[] read = new char[IO_BUFFER_SIZE];
			int r = 0;
			while ((r = input.read(read, 0, IO_BUFFER_SIZE)) != -1)
				onCharacterArrayRead(read, r);
		}  finally {
			this.input.close();
		}
	}
	
	/**
	 * Closes the underlying stream.
	 * @throws IOException
	 */
	public void close() throws IOException {
		this.input.close();
	}
	
	/**
	 * Get an every byte from an input stream and put it into a byte array.
	 * 
	 * @param stream
	 *            Input stream to read from
	 * @return bytes retrieved from stream
	 * @throws IOException
	 *             if an error occurs
	 */
	public static final String get(Reader reader) throws IOException {
		if (reader != null) {
			/*
			 * This is the fastest method I know for reading from an
			 * indeterminate reader. So fast in fact, that it pars fairly well
			 * with FileChannel stuff in test cases.
			 * 
			 * Small and simple enough the JIT can grab hold and optimize it,
			 * but with a large enough buffer to not make reading slow.
			 */

			final BufferedReader input = new BufferedReader(reader);
			final StringBuilder buffer = new StringBuilder();

			final char[] read = new char[IO_BUFFER_SIZE];
			int r = 0;
			while ((r = input.read(read, 0, IO_BUFFER_SIZE)) != -1)
				buffer.append(read, 0, r);

			return buffer.toString();
		}
		return null;
	}

	/**
	 * Get an every byte from an input stream and put it into a byte array. Then
	 * it also closes the stream after it is done.
	 * 
	 * @param stream
	 *            Input stream to read from
	 * @return bytes retrieved from stream
	 * @throws IOException
	 *             if an error occurs
	 */
	public static final String getAndClose(Reader reader) throws IOException {
		if (reader != null) try {
			/*
			 * This is the fastest method I know for reading from an
			 * indeterminate reader. So fast in fact, that it pars fairly well
			 * with FileChannel stuff in test cases.
			 * 
			 * Small and simple enough the JIT can grab hold and optimize it,
			 * but with a large enough buffer to not make reading slow.
			 */
			final BufferedReader input = new BufferedReader(reader);
			final StringBuilder buffer = new StringBuilder();

			final char[] read = new char[IO_BUFFER_SIZE];
			int r = 0;
			while ((r = input.read(read, 0, IO_BUFFER_SIZE)) != -1)
				buffer.append(read, 0, r);

			return buffer.toString();
		}
		finally {
			reader.close();
		}
		return null;
	}
}
