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
package org.csdgn.fxm.net;

import java.nio.charset.Charset;
import java.util.ArrayDeque;

import org.csdgn.fxm.controller.Login;
import org.csdgn.fxm.model.Character;

import io.netty.channel.Channel;

public class Session {
	private static final Charset ASCII = Charset.forName("US-ASCII");

	protected boolean disconnected = false;
	protected ArrayDeque<InputHandler> hStack;
	protected InputHandler handler;
	protected Channel channel;
	public Character character;
	public String username;
	

	/**
	 * Creates a new session with the given channel.
	 * 
	 * @param chan
	 *            The channel.
	 */
	public Session(Channel chan) {
		channel = chan;
		hStack = new ArrayDeque<InputHandler>();
		setMessageHandler(new Login());
	}

	/**
	 * Disconnects this Session
	 */
	public synchronized void disconnect() {
		if(!disconnected) {
			disconnected = true;
			channel.disconnect();
			character.quit();
		}
	}
	
	/**
	 * Forces a message to be interpreted.
	 */
	public void force(String request) {
		this.received(request);
	}

	/**
	 * Called when the user of this session sends a message.
	 * 
	 * @param request
	 */
	public void received(String request) {
		handler.received(this, request);
	}

	/**
	 * Exits the current handler and returns to the previous handler on the
	 * stack.
	 */
	public void popMessageHandler() {
		if (handler != null) {
			handler.exit(this);
		}
		handler = hStack.poll();
		if(handler != null) {
			handler.reenter(this);
		}
	}

	/**
	 * Pushes the given handler onto the handler stack.
	 */
	public void pushMessageHandler(InputHandler handler) {
		if (this.handler != null) {
			hStack.push(this.handler);
		}
		this.handler = handler;
		handler.enter(this);
	}

	/**
	 * Clears the handler stack and sets the message handler.
	 */
	public void setMessageHandler(InputHandler handler) {
		hStack.clear();
		if (this.handler != null) {
			this.handler.exit(this);
		}
		this.handler = handler;
		handler.enter(this);
	}

	/**
	 * Writes a string to the channel.
	 * 
	 * @param message
	 *            The message.
	 */
	public synchronized void write(String message) {
		channel.write(message);
	}

	/**
	 * Writes a line ending to the channel.
	 */
	public synchronized void writeLn() {
		channel.write("\r\n");
	}
	
	/**
	 * Writes messages. Each message in the array is terminated with
	 * <code>\r\n</code>.
	 * 
	 * @param messages
	 *            An array of messages.
	 */
	public synchronized void writeLn(String ... messages) {
		StringBuilder buf = new StringBuilder();
		for (String message : messages) {
			buf.append(message).append("\r\n");
		}

		channel.write(buf.toString());
	}

	/**
	 * Writes an ascii string to the channel.
	 * 
	 * @param message
	 *            The message.
	 */
	public synchronized void writeRaw(String message) {
		channel.write(message.getBytes(ASCII));
	}
}
